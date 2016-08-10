package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.JPanelSymbols;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolFactory;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolGroupTree;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolGroupTreeNode;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 符号面板基类
 *
 * @author XiaJt
 */
public abstract class SymbolDialog extends SmDialog {

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu();
	private JMenuItem menuItemProperty = new JMenuItem();
	private JMenu menuEdit = new JMenu();

	private JLabel labelSearch = new JLabel();
	private JTextField textFieldSearch = new JTextField();

	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
	private JPanel panelWorkspaceResources = new JPanel();
	private JScrollPane scrollPaneWorkspaceResources = new JScrollPane();
	private SymbolGroupTree treeWorkspaceResources;

	protected JPanelSymbols panelSymbols;
	protected SymbolPreViewPanel panelPreview;


	private JPanel panelButton = new JPanel();
	private SmButton buttonOK = new SmButton();
	private SmButton buttonCancle = new SmButton();
	private SmButton buttonApply = new SmButton();
	protected Resources currentResources;
	private ISymbolApply symbolApply;

	protected GeoStyle currentGeoStyle = new GeoStyle();

	protected Color wrongColor = Color.red;
	protected Color defaultColor = Color.BLACK;
	protected SymbolGroup currentSymbolGroup;

	protected IGeoStylePropertyChange geoStylePropertyChange;

	private boolean isDisposed = false;
	/**
	 * 精度
	 */
	protected double pow = 2;

	public SymbolDialog() {
		init();
	}

	public SymbolDialog(JDialog dialog) {
		super(dialog, true);
		init();
	}

	public void escapePressed() {
		this.dialogResult = DialogResult.CANCEL;
		this.setVisible(false);
	}

	public void enterPressed() {
		this.dialogResult = DialogResult.OK;
		this.setVisible(false);
	}

	/**
	 * 不支持直接显示对话框,请使用showDialog(GeoStyle)方法或showDialog(GeoStyle，ISymbolApply)
	 *
	 * @see SymbolDialog#showDialog(GeoStyle)
	 * @see SymbolDialog#showDialog(GeoStyle, ISymbolApply)
	 */
	@Deprecated
	@Override
	public DialogResult showDialog() {
		LogUtilities.debug("unSupportMethod");
		throw new UnsupportedOperationException();
	}

	/**
	 * 根据geoStyle显示对话框
	 *
	 * @param geoStyle 需要修改的风格
	 * @return 结果
	 */
	public DialogResult showDialog(GeoStyle geoStyle) {
		return showDialog(geoStyle, null);
	}

	/**
	 * 更具指定参数显示对话框，并指定应用按钮功能
	 *
	 * @param geoStyle    需要修改的风格
	 * @param symbolApply 按应用按钮时的操作
	 * @return 结果
	 */
	public DialogResult showDialog(GeoStyle geoStyle, ISymbolApply symbolApply) {
		if (geoStyle == null) {
			throw new NullPointerException("geoStyle should not null");
		}
		this.symbolApply = symbolApply;
		if (currentGeoStyle != null) {
			currentGeoStyle.dispose();
		}
		this.currentGeoStyle = geoStyle.clone();
		prepareForShowDialog();
		textFieldSearch.setText("");
		panelSymbols.requestFocus();
//		treeWorkspaceResources.updateUI();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		return dialogResult;
	}

	private void init() {
		currentResources = Application.getActiveApplication().getWorkspace().getResources();
		this.setTitle(ControlsProperties.getString("String_Title_SymbolDialog"));
		initComponent();
		addListeners();
		initLayout();
		initResources();
	}

	//region 初始化面板

	/**
	 * 初始化面板
	 */
	private void initComponent() {
		if (!SystemPropertyUtilities.isWindows()) {
			labelSearch.setForeground(Color.white);
		}
		panelPreview = new SymbolPreViewPanel(getSymbolType());
		initPanelWorkspaceResources();
		initTabbedPane();
		getRootPane().setFocusable(true);
		getRootPane().requestFocus();
		initSize();
//		setMinimumSize(new Dimension(((int) (0.5 * width)), height));
		getRootPane().setDefaultButton(buttonOK);
		geoStylePropertyChange = new IGeoStylePropertyChange() {
			@Override
			public void propertyChange() {
				panelPreview.refreshMap();
			}
		};
		currentSymbolGroup = getLibrary().getRootGroup();
		initComponentHook();
		panelSymbols.setSymbolGroup(currentResources, currentSymbolGroup);
		this.setLocationRelativeTo(null);
	}

	private void initSize() {
		if (SystemPropertyUtilities.isWindows()) {
			int width = 880;
			int height = 650;
			setSize(width, height);
		} else {
			int width = 1000;
			int height = 700;
			setSize(width, height);
		}
	}

	/**
	 * 初始化工作空间资源列表
	 */
	private void initPanelWorkspaceResources() {
		SymbolLibrary symbolLibrary = SymbolFactory.getSymbolLibrary(currentResources, getSymbolType());
		if (symbolLibrary == null) {
			LogUtilities.debug("Symbol Library is null!");
			return;
		}
		Dimension minimumSize = new Dimension(200, 600);
		panelWorkspaceResources.setMinimumSize(minimumSize);
		panelWorkspaceResources.setPreferredSize(minimumSize);
		panelWorkspaceResources.setMaximumSize(minimumSize);

		treeWorkspaceResources = new SymbolGroupTree(symbolLibrary.getRootGroup());
		panelWorkspaceResources.setLayout(new GridBagLayout());
		panelWorkspaceResources.add(scrollPaneWorkspaceResources, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		scrollPaneWorkspaceResources.setViewportView(treeWorkspaceResources);
	}

	protected abstract SymbolType getSymbolType();

	/**
	 * 初始化左侧选择框
	 */
	private void initTabbedPane() {
		tabbedPane.addTab(getVerticalTitle(ControlsProperties.getString("String_WorkspaceResources")), panelWorkspaceResources);
//		tabbedPane.addTab(getVerticalTitle("hehe"), new JPanel());
	}

	private String getVerticalTitle(String s) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html>");
		if (s.length() > 0) {
			builder.append(s.charAt(0));
		}
		for (int i = 1; i < s.length(); i++) {
			builder.append("<br>");
			builder.append(s.charAt(i));
		}
		builder.append("</html>");
		return builder.toString();
	}

	/**
	 * 初始化时用于给子类使用，如无需要可不重写
	 */
	protected void initComponentHook() {

	}
	//endregion

	//region 初始化布局
	private void initLayout() {

		initPanelButton();
		initMenuBar();
//		this.setJMenuBar(this.menuBar);
		Dimension minimumSize = new Dimension(200, 200);
		panelPreview.setMinimumSize(minimumSize);
		panelPreview.setPreferredSize(minimumSize);
		panelPreview.setMaximumSize(minimumSize);

		JPanel panelParent = new JPanel();
		panelParent.setLayout(new GridBagLayout());
		panelParent.add(menuBar, new GridBagConstraintsHelper(0, 0, 3, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER));
		panelParent.add(tabbedPane, new GridBagConstraintsHelper(0, 1, 1, 2).setFill(GridBagConstraints.BOTH).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 0, 10, 0));

		panelParent.add(getPanelSymbols(), new GridBagConstraintsHelper(1, 1, 1, 2).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));

		panelParent.add(panelPreview, new GridBagConstraintsHelper(2, 1, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 0, 0, 10));
		panelParent.add(getPanelMain(), new GridBagConstraintsHelper(2, 2, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 1).setAnchor(GridBagConstraints.NORTH).setInsets(10, 0, 0, 10));

		panelParent.add(panelButton, new GridBagConstraintsHelper(0, 3, 3, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 10));

		this.setLayout(new GridBagLayout());
		this.add(panelParent, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 10, 0).setFill(GridBagConstraints.BOTH));
	}

	private JPanel getPanelSymbols() {
		JPanel panelSymbols = new JPanel();
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(this.panelSymbols);
		jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelSymbols.setLayout(new GridBagLayout());
		panelSymbols.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10));
		return panelSymbols;
	}

	/**
	 * 初始化按钮面板
	 */
	private void initPanelButton() {
		this.panelButton.setLayout(new GridBagLayout());
		//@formatter:off
		this.panelButton.add(this.buttonOK,     new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 1).setInsets(0, 0, 0, 10).setAnchor(GridBagConstraints.EAST));
		this.panelButton.add(this.buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setInsets(0, 0, 0, 10).setAnchor(GridBagConstraints.EAST));
		this.panelButton.add(this.buttonApply,  new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setInsets(0, 0, 0, 10).setAnchor(GridBagConstraints.EAST));
		//@formatter:on
	}

	/**
	 * 初始化菜单栏
	 */
	private void initMenuBar() {
		// 文件菜单
		this.menuBar.setLayout(new GridBagLayout());

//		this.menuFile.add(this.menuItemProperty);
//		this.menuFile.add(new JMenu("asd"));
//		this.menuBar.add(this.menuFile, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1));

		// 编辑菜单
//		this.menuBar.add(this.menuEdit, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1));
		this.menuBar.add(this.labelSearch, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 0, 5).setWeight(1, 1));
		this.menuBar.add(this.textFieldSearch, new GridBagConstraintsHelper(3, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0, 1).setInsets(0, 0, 0, 10).setIpad(150, -2));
	}
	//endregion

	private void addListeners() {
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterPressed();
			}
		});

		this.buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				escapePressed();
			}
		});

		this.buttonApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonApplyClicked();
			}
		});
		this.treeWorkspaceResources.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				SymbolGroupTreeNode lastSelectedPathComponent = (SymbolGroupTreeNode) treeWorkspaceResources.getLastSelectedPathComponent();
				if (lastSelectedPathComponent != null && lastSelectedPathComponent.getCurrentGroup() != currentSymbolGroup) {
					currentSymbolGroup = lastSelectedPathComponent.getCurrentGroup();
					panelSymbols.setSymbolGroup(currentResources, currentSymbolGroup);
				}
			}
		});
		this.textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 38 || e.getKeyCode() == 40) {
					panelSymbols.requestFocus();
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					e.consume();
				}
			}
		});
		this.textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}

			private void search() {
				panelSymbols.setSearchString(textFieldSearch.getText());
			}
		});

		this.panelSymbols.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) {

				} else if (e.getKeyChar() == '\b') {
					textFieldSearch.setText("");
				} else if (e.getKeyChar() != '\uFFFF') {
					textFieldSearch.setText(String.valueOf(e.getKeyChar()));
					textFieldSearch.requestFocus();
				}
			}

		});
	}

	private void initResources() {
		labelSearch.setText(ControlsProperties.getString("String_Label_SymbolSearch"));
		this.buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));

		this.menuFile.setText(CommonProperties.getString(CommonProperties.File));
		this.menuEdit.setText(CommonProperties.getString(CommonProperties.Edit));

		this.menuItemProperty.setText(CommonProperties.getString(CommonProperties.Property));

	}

	protected abstract JPanel getPanelMain();


	private void buttonApplyClicked() {
		if (symbolApply != null) {
			symbolApply.apply(currentGeoStyle);
		}
	}

	private void prepareForShowDialog() {
		this.dialogResult = DialogResult.CLOSED;
		this.buttonApply.setVisible(symbolApply != null);
		panelPreview.setGeoStyle(currentGeoStyle);
		prepareForShowDialogHook();
		initCurrentSymbolGroup();
		panelSymbols.setGeoStyle(currentGeoStyle);
	}

	/**
	 * 用于给子类在显示窗口之前进行准备，无需要可不重写
	 */
	protected void prepareForShowDialogHook() {
	}

	/**
	 * 获得修改后的风格
	 *
	 * @return 修改后的GeoStyle
	 */
	public GeoStyle getCurrentGeoStyle() {
		return currentGeoStyle.clone();
	}

	private void initCurrentSymbolGroup() {
		Symbol symbol = getLibrary().findSymbol(currentGeoStyle.getMarkerSymbolID());
		if (symbol != null) {
			treeWorkspaceResources.setSelectedSymbolGroup(findSymbolGroup(symbol.getID()));
		} else {
			treeWorkspaceResources.setSelectedSymbolGroup(getLibrary().getRootGroup());
		}
//		panelSymbols.setSymbolGroup(currentResources, currentSymbolGroup);
	}

	protected abstract SymbolLibrary getLibrary();

	private SymbolGroup findSymbolGroup(int symbol) {
		SymbolGroup symbolGroup = getLibrary().getRootGroup();
		return findSymbolGroup(symbolGroup, symbol);
	}

	private SymbolGroup findSymbolGroup(SymbolGroup symbolGroup, int symbol) {
		if (symbolGroup.indexOf(symbol) == -1) {
			SymbolGroups childGroups = symbolGroup.getChildGroups();
			for (int i = 0; i < childGroups.getCount(); i++) {
				SymbolGroup symbolGroup1 = findSymbolGroup(childGroups.get(i), symbol);
				if (symbolGroup1 != null) {
					return symbolGroup1;
				}
			}
		} else {
			return symbolGroup;
		}
		return null;
	}

	protected int getUnOpaqueRate(int value) {
		return 100 - value;
	}

	@Override
	public void dispose() {
		isDisposed = true;
		super.dispose();
		panelPreview.dispose();
	}

	public Resources getCurrentResources() {
		return currentResources;
	}

	public boolean isDisposed() {
		return isDisposed;
	}
}
