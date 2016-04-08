package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.GeoStyle;
import com.supermap.data.Resources;
import com.supermap.data.SymbolLibrary;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolFactory;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolGroupTree;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.LogUtilties;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	private JTextField textFieldSearch = new JTextField();

	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
	private JPanel panelWorkspaceResources = new JPanel();
	private JScrollPane scrollPaneWorkspaceResources = new JScrollPane();
	private SymbolGroupTree treeWorkspaceResources;

	private JPanel panelButton = new JPanel();
	private SmButton buttonOK = new SmButton();
	private SmButton buttonCancle = new SmButton();
	private SmButton buttonApply = new SmButton();
	protected Resources currentResources;
	private ISymbolApply symbolApply;

	protected GeoStyle beforeGeoStyle;
	protected GeoStyle currentGeoStyle;

	protected Color wrongColor = Color.red;
	protected Color defaultColor = Color.BLACK;

	/**
	 * 精度
	 */
	protected double pow = 1;

	public SymbolDialog() {
		init();
	}

	@Override
	public void escapePressed() {
		this.dialogResult = DialogResult.CANCEL;
		this.setVisible(false);
	}

	@Override
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
		LogUtilties.debug("unSupportMethod");
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
		if (beforeGeoStyle != null) {
			beforeGeoStyle.dispose();
		}
		this.beforeGeoStyle = geoStyle;
		if (currentGeoStyle != null) {
			currentGeoStyle.dispose();
		}
		this.currentGeoStyle = beforeGeoStyle.clone();
		prepareForShowDialog();
		scrollPaneWorkspaceResources.requestFocus();
		this.setVisible(true);
		return dialogResult;
	}

	private void init() {
		currentResources = Application.getActiveApplication().getWorkspace().getResources();
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
		initPanelWorkspaceResources();
		initTabbedPane();
		getRootPane().setFocusable(true);
		getRootPane().requestFocus();
		int width = (int) (1000 / 1.25 * SystemPropertyUtilties.getSystemSizeRate());
		int height = (int) (600 / 1.25 * SystemPropertyUtilties.getSystemSizeRate());
		setSize(width, height);
		setMinimumSize(new Dimension(((int) (0.5 * width)), height));
		getRootPane().setDefaultButton(buttonOK);
		initComponentHook();
		this.setLocationRelativeTo(null);
	}

	/**
	 * 初始化工作空间资源列表
	 */
	private void initPanelWorkspaceResources() {
		SymbolLibrary symbolLibrary = SymbolFactory.getSymbolLibrary(currentResources, getSymbolType());
		if (symbolLibrary == null) {
			LogUtilties.debug("Symbol Library is null!");
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
		this.setJMenuBar(this.menuBar);


		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 0, 10, 0));
		panel.add(getPanelMain(), new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(80, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		panel.add(panelButton, new GridBagConstraintsHelper(0, 1, 2, 1).setFill(GridBagConstraints.BOTH).setWeight(100, 0).setAnchor(GridBagConstraints.CENTER));

		this.setLayout(new GridBagLayout());
		this.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 10, 10).setFill(GridBagConstraints.BOTH));
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

		this.menuFile.add(this.menuItemProperty);
//		this.menuFile.add(new JMenu("asd"));
		this.menuBar.add(this.menuFile, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1));

		// 编辑菜单
		this.menuBar.add(this.menuEdit, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1));
		this.menuBar.add(this.textFieldSearch, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(1, 1).setInsets(0, 0, 0, 20).setIpad(150, -2));
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
	}

	private void initResources() {
		this.setTitle(ControlsProperties.getString("String_Title_SymbolDialog"));

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
		this.buttonApply.setEnabled(symbolApply != null);
		prepareForShowDialogHook();
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
}