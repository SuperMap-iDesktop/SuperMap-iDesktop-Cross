package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Resources;
import com.supermap.data.Size2D;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolFill;
import com.supermap.data.SymbolGroup;
import com.supermap.data.SymbolGroups;
import com.supermap.data.SymbolLibrary;
import com.supermap.data.SymbolLine;
import com.supermap.data.SymbolMarker;
import com.supermap.data.SymbolMarker3D;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.UICommonToolkit;

/**
 * 符号选择面板
 * 
 * @author xuzw
 * 
 */
public class SymbolPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem importSymFileMenuItem;
	private JMenuItem importGridSymbolMenuItem;

	private JPanel jPanelView;
	private JPanel jPanelNorth;
	private JPanel jPanelCenter;
	private JPanel jPanelEast;
	private JPanel jPanelOperate;
	private JPanel jPanelSymbolsView;
	private JScrollPane jScrollPaneSymbolsView;
	private JPanel jPanelSearchTree;
	private JComboBox jComboBoxSearchTree;

	private transient SymbolSearchPanel jSymbolSearchPanelSymbolSearch;

	private transient SymbolPreViewPanel symbolPreViewPanel;

	private transient SymbolSettingPanel symbolSettingPanel;

	private transient SymbolBasicInfoPanel symbolBasicInfoPanel;

	private transient Resources currentResources;

	private transient SymbolType currentSymbolType;

	private transient SymbolGroup symbolRootGroup;

	private transient SymbolGroup currentSymbolGroup;

	private JTree jTreeSymbols;

	private static final int SYMBOL_PANEL_WIDTH = 510;

	// add by xuzw 2010-10-11
	// 填充库显示面板的宽度定在420像素，因为填充库的设置太多，需要占用较大的空间
	private static final int SYMBOLFILL_PANEL_WIDTH = 420;

	// 点符号浏览面板高度增量
	private static final int SYMBOLMARKER_PANEL_HEIGHT_INCREMENT = 88;
	// 线符号浏览面板高度增量
	private static final int SYMBOLLINE_PANEL_HEIGHT_INCREMENT = 87;
	// 面符号浏览面板高度增量
	private static final int SYMBOLREGION_PANEL_HEIGHT_INCREMENT = 108;

	private transient GeoStyle activeStyle;

	private JLabel currentLabel;

	private JLabel preLabel;

	// 判断是不是第一次鼠标按下，该boolean控制移除预览框中Map的TrackingLayerDrawing监听
	private boolean isFirstMousePressed = true;

	// 判断是不是仅显示符号库基类，如果为true，那么预览面板，设置面板都应该不显示
	// 默认是false
	private boolean isSymbolLibraryPanel = false;

	private JFileChooser fileChooserImportSymFile;

	// 点符号和线符号绘制的JLabel大小，及每行之间的高度，注意高度要比JLabel大
	private static final int SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE = 80;

	private static final int SYMBOLMARKER_AND_SYMBOLLINE_ROW_HEIGHT = 85;

	// 面符号绘制的JLabel大小，及每行之间的高度，注意高度要比JLabel大
	private static final int SYMBOLFILL_LABEL_SIZE = 100;

	private static final int SYMBOLFILL_ROW_HEIGHT = 105;

	// 鼠标适配器的一个实现
	private transient SymbolsViewRefresh symbolsViewRefresh = new SymbolsViewRefresh();

	// 用于存放标签信息的数组列表，该数组列表负责绘制->搜索->绘制过程中在符号显示面板中的信息
	private ArrayList<LabelInfo> labelInfoArray = new ArrayList<LabelInfo>();

	// 用于存放所有标签信息的数组列表，初始化时就确定，只在searchPaintSymbol方法中用到
	private ArrayList<LabelInfo> allLabelInfo = new ArrayList<LabelInfo>();

	// 用于存放符号在根组中的路径
	private transient Object[] symbolTreePath = null;

	/**
	 * 默认构造函数，
	 */
	public SymbolPanel() {
		super();
		setLayout(new BorderLayout());
	}

	/**
	 * Create the panel
	 */
	public SymbolPanel(Resources resources, GeoStyle geoStyle, SymbolType symbolType) {
		super();
		setLayout(new BorderLayout());
		this.currentSymbolType = symbolType;
		this.activeStyle = geoStyle;
		this.currentResources = resources;

		this.labelInfoArray = new ArrayList<LabelInfo>();
		initialize();
	}

	/**
	 * 初始化根组
	 */
	private void initRootGroup() {
		if (currentSymbolType.equals(SymbolType.MARKER)) {
			SymbolLibrary symbolMarkerLibrary = currentResources.getMarkerLibrary();
			symbolRootGroup = symbolMarkerLibrary.getRootGroup();
			// 编码格式不同，重新赋值
			symbolRootGroup.setName(ControlsProperties.getString("String_SymbolRootGroupName"));
			// modified by zhaosy 不是系统符号时根组直接显示当前符号所在路径
			if (activeStyle.getMarkerSymbolID() > 0) {
				getTreePath();
			}
		} else if (currentSymbolType.equals(SymbolType.LINE)) {
			SymbolLibrary symbolLineLibrary = currentResources.getLineLibrary();
			symbolRootGroup = symbolLineLibrary.getRootGroup();
			// 编码格式不同，重新赋值
			symbolRootGroup.setName(ControlsProperties.getString("String_SymbolRootGroupName"));
			if (activeStyle.getMarkerSymbolID() > 5) {
				getTreePath();
			}
		} else if (currentSymbolType.equals(SymbolType.FILL)) {
			SymbolLibrary symbolFillLibrary = currentResources.getFillLibrary();
			symbolRootGroup = symbolFillLibrary.getRootGroup();
			// 编码格式不同，重新赋值
			symbolRootGroup.setName(ControlsProperties.getString("String_SymbolRootGroupName"));
			if (activeStyle.getMarkerSymbolID() > 6) {
				getTreePath();
			}
		}
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public Resources getResources() {
		return currentResources;
	}

	/**
	 * 设置资源
	 * 
	 * @param resources
	 */
	public void setResources(Resources resources) {
		currentResources = resources;
		if (currentSymbolType != null && currentResources != null && activeStyle != null) {
			initialize();
		}
	}

	/**
	 * 获取符号面板类型
	 * 
	 * @return
	 */
	public SymbolType getType() {
		return currentSymbolType;
	}

	/**
	 * 设置符号面板类型
	 * 
	 * @param symbolType
	 */
	public void setType(SymbolType symbolType) {
		currentSymbolType = symbolType;
		if (currentSymbolType != null && currentResources != null && activeStyle != null) {
			initialize();
		}
	}

	/**
	 * 获取操作的GeoStyle
	 * 
	 * @return
	 */
	public GeoStyle getStyle() {
		return activeStyle;
	}

	/**
	 * 设置操作的GeoStyle
	 * 
	 * @param geoStyle
	 */
	public void setStyle(GeoStyle geoStyle) {
		activeStyle = geoStyle;
		if (currentSymbolType != null && currentResources != null && activeStyle != null) {
			initialize();
		}
	}

	protected void SetStyleAndRefresh(GeoStyle geoStyle) {
		activeStyle = geoStyle;
		if (geoStyle != null && currentSymbolType != null && symbolPreViewPanel != null) {
			symbolPreViewPanel.refreshPreViewMapControl(currentSymbolType, activeStyle);
		}
	}

	/**
	 * 初始化面板
	 */
	private void initialize() {
		initRootGroup();
		if (isSymbolLibraryPanel) {
			add(getMenuBar(), BorderLayout.NORTH);
		}
		add(getViewPanel(), BorderLayout.CENTER);
		// 设置树的内容并刷新各面板
		if (jComboBoxSearchTree != null) {
			// 刷新符号展示面板
			refreshSymbolsViewPanel();

			if (!isSymbolLibraryPanel) {
				// 刷新基本信息面板
				int symbolID = 0;
				if (currentSymbolType.equals(SymbolType.MARKER)) {
					symbolID = activeStyle.getMarkerSymbolID();
				} else if (currentSymbolType.equals(SymbolType.LINE)) {
					symbolID = activeStyle.getLineSymbolID();
				} else if (currentSymbolType.equals(SymbolType.FILL)) {
					symbolID = activeStyle.getFillSymbolID();
				}
				LabelInfo labelInfo = getLabelInfoBySymbolID(symbolID);
				symbolBasicInfoPanel.refreshComboBox(labelInfoArray);
				symbolBasicInfoPanel.refreshBasicInfo(symbolID, labelInfo.getSymbolName());

				// add by xuzw 2010-09-26
				// 初始化时高亮符号对应的JLabel
				changeCurrentLabel(labelInfo.getLabel());

				// 确保被选中的符号显示出来
				Rectangle rectangle = getCellRect(labelInfo.getRow(), labelInfo.getColumn());
				if (rectangle != null) {
					getPanelSymbolsView().scrollRectToVisible(rectangle);
				}

				// 刷新预览面板
				symbolPreViewPanel.refreshPreViewMapControl(currentSymbolType, activeStyle);
			}
		}
	}

	/**
	 * 获取菜单工具条
	 * 
	 * @return
	 */
	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFileMenu());
		}
		return menuBar;
	}

	/**
	 * 获取文件菜单
	 * 
	 * @return
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText(ControlsProperties.getString("String_File"));
			fileMenu.add(getImportSymFileMenuItem());
			fileMenu.add(getImportGridSymbolMenuItem());
		}
		return fileMenu;
	}

	/**
	 * 获取导入符号库文件菜单项
	 * 
	 * @return
	 */
	private JMenuItem getImportSymFileMenuItem() {
		if (importSymFileMenuItem == null) {
			importSymFileMenuItem = new JMenuItem();
			importSymFileMenuItem.setText(ControlsProperties.getString("String_ImportSymbolLibrary"));
			importSymFileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abstractImportSymFileMenuItem();
				}
			});
		}
		return importSymFileMenuItem;
	}

	private void abstractImportSymFileMenuItem() {
		JFileChooser fileChooser = getFileChooserImportSymFile();
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getPath();
			boolean result = false;
			if (currentSymbolType.equals(SymbolType.MARKER)) {
				result = currentResources.getMarkerLibrary().fromFile(filePath);
				if (result) {
					symbolRootGroup = currentResources.getMarkerLibrary().getRootGroup();
					setTreeModel(jTreeSymbols, symbolRootGroup);
					paintMarkerSymbols();
				} else {
					UICommonToolkit.showMessageDialog(ControlsProperties.getString("String_Tip_ImportWrong"));
				}
			} else if (currentSymbolType.equals(SymbolType.LINE)) {
				result = currentResources.getLineLibrary().fromFile(filePath);
				if (result) {
					symbolRootGroup = currentResources.getLineLibrary().getRootGroup();
					setTreeModel(jTreeSymbols, symbolRootGroup);
					paintLineSymbols();
				} else {
					UICommonToolkit.showMessageDialog(ControlsProperties.getString("String_Tip_ImportWrong"));
				}
			} else if (currentSymbolType.equals(SymbolType.FILL)) {
				result = currentResources.getFillLibrary().fromFile(filePath);
				if (result) {
					symbolRootGroup = currentResources.getFillLibrary().getRootGroup();
					setTreeModel(jTreeSymbols, symbolRootGroup);
					paintFillSymbols();
				} else {
					UICommonToolkit.showMessageDialog(ControlsProperties.getString("String_Tip_ImportWrong"));
				}
			}
		}
	}

	/**
	 * 获取导入栅格符号菜单项
	 * 
	 * @return
	 */
	private JMenuItem getImportGridSymbolMenuItem() {
		if (importGridSymbolMenuItem == null) {
			importGridSymbolMenuItem = new JMenuItem();
			importGridSymbolMenuItem.setText(ControlsProperties.getString("String_ImportGridSymbol"));
			importGridSymbolMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// do nothing
				}

			});
		}
		return importGridSymbolMenuItem;
	}

	/**
	 * 获取视图面板
	 * 
	 * @return
	 */
	protected JPanel getViewPanel() {
		if (jPanelView == null) {
			jPanelView = new JPanel();
			jPanelView.setLayout(new GridBagLayout());

			jPanelView.add(getNorthPanel(), new GridBagConstraintsHelper(0, 0, 2, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0));
			jPanelView.add(getCenterPanel(), new GridBagConstraintsHelper(0, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
			if (!isSymbolLibraryPanel) {
				jPanelView.add(getEastPanel(), new GridBagConstraintsHelper(1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 1));
			}
		}
		return jPanelView;
	}

	/**
	 * 获取北部面板，负责放置查找范围面板和搜索面板
	 * 
	 * @return
	 */
	protected JPanel getNorthPanel() {
		if (jPanelNorth == null) {
			jPanelNorth = new JPanel();
			jPanelNorth.setLayout(new GridLayout(1, 0));
			jPanelNorth.add(getSearchTreePanel());
			jSymbolSearchPanelSymbolSearch = new SymbolSearchPanel(this);
			jPanelNorth.add(jSymbolSearchPanelSymbolSearch);
		}
		return jPanelNorth;
	}

	/**
	 * 获取中央面板，该面板放置符号展示面板
	 * 
	 * @return
	 */
	protected JPanel getCenterPanel() {
		if (jPanelCenter == null) {
			jPanelCenter = new JPanel(new BorderLayout());
			jPanelCenter.add(getSymbolsViewScrollPane(), BorderLayout.CENTER);
		}
		return jPanelCenter;
	}

	/**
	 * 获取东部面板，该面板放置基本信息面板、预览面板和符号设置面板
	 * 
	 * @return
	 */
	protected JPanel getEastPanel() {
		if (jPanelEast == null) {
			jPanelEast = new JPanel();
			jPanelEast.setLayout(new BorderLayout());
			jPanelEast.add(getOperatePanel(), BorderLayout.CENTER);
		}
		return jPanelEast;
	}

	/**
	 * 获取操作面板，该面板放置基本信息面板、预览面板和符号设置面板
	 * 
	 * @return
	 */
	protected JPanel getOperatePanel() {
		if (jPanelOperate == null) {
			jPanelOperate = new JPanel();
			jPanelOperate.setLayout(new BoxLayout(jPanelOperate, BoxLayout.Y_AXIS));
			// 基本信息面板
			symbolBasicInfoPanel = new SymbolBasicInfoPanel(this);
			jPanelOperate.add(symbolBasicInfoPanel);
			// 预览面板
			symbolPreViewPanel = new SymbolPreViewPanel(currentResources.getWorkspace(), currentSymbolType);
			symbolPreViewPanel.addMapTrackingLayerDrawingListener();
			jPanelOperate.add(symbolPreViewPanel);
			// 符号设置面板
			symbolSettingPanel = new SymbolSettingPanel(this, activeStyle, currentSymbolType);
			jPanelOperate.add(symbolSettingPanel);
		}
		return jPanelOperate;
	}

	/**
	 * 获取查找范围面板
	 * 
	 * @return
	 */
	protected JPanel getSearchTreePanel() {
		if (jPanelSearchTree == null) {
			jPanelSearchTree = new JPanel();
			jPanelSearchTree.setLayout(new BorderLayout());
			jPanelSearchTree.setBorder(BorderFactory.createEmptyBorder(3, 2, 3, 0));
			final JLabel m_labelSearchTree = new JLabel();
			m_labelSearchTree.setText(ControlsProperties.getString("String_ViewRange"));
			jPanelSearchTree.add(m_labelSearchTree, BorderLayout.WEST);
			jPanelSearchTree.add(getSearchTreeComboBox(), BorderLayout.CENTER);
		}
		return jPanelSearchTree;
	}

	/**
	 * 获取查找范围的JComboBox
	 * 
	 * @return
	 */
	protected JComboBox getSearchTreeComboBox() {
		if (jComboBoxSearchTree == null) {
			// add by xuzw 2010-09-26
			// 设置树控件的节点Icon，Java默认的无法表达符号库组的含义
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setClosedIcon(InternalImageIconFactory.RESOURCE_NODE_INVISIBLE);
			renderer.setOpenIcon(InternalImageIconFactory.RESOURCE_NODE_INVISIBLE);
			renderer.setLeafIcon(InternalImageIconFactory.RESOURCE_NODE_INVISIBLE);
			jComboBoxSearchTree = new TreeComboBox(getSymbolsTree(), renderer);
		}
		return jComboBoxSearchTree;
	}

	/**
	 * 获得符号组成的树
	 * 
	 * @return
	 */
	protected JTree getSymbolsTree() {
		if (jTreeSymbols == null) {
			jTreeSymbols = new JTree();
			setTreeModel(jTreeSymbols, symbolRootGroup);

			if (symbolTreePath != null && symbolTreePath.length > 1) {
				TreePath treePath = new TreePath(symbolTreePath);
				jTreeSymbols.setSelectionPath(treePath);
			}

			jTreeSymbols.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// modify by xuzw 2010-09-26
					// 之前是在鼠标按下时绘制，体验不好，应当是鼠标弹起后绘制
					refreshSymbolsViewPanel();

					symbolBasicInfoPanel.refreshComboBox(labelInfoArray);
				}

			});
			// add by xuzw 2010-9-17
			// UGOSPII-1497支持回车键
			jTreeSymbols.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						refreshSymbolsViewPanel();

						symbolBasicInfoPanel.refreshComboBox(labelInfoArray);
					}

				}

			});
		}
		return jTreeSymbols;
	}

	/**
	 * 获取符号展示的滚动面板
	 * 
	 * @return
	 */
	protected JScrollPane getSymbolsViewScrollPane() {
		if (jScrollPaneSymbolsView == null) {
			jScrollPaneSymbolsView = new JScrollPane();

			JScrollBar scrollVertical = jScrollPaneSymbolsView.getVerticalScrollBar();
			// 修改UGOSPII-941，加大滚动条滚动增幅，提高用户体验
			scrollVertical.setUnitIncrement(scrollVertical.getMaximum() / 15);
			jScrollPaneSymbolsView.setPreferredSize(new Dimension(500, 400));
			jScrollPaneSymbolsView.setMinimumSize(new Dimension(500, 400));
			jScrollPaneSymbolsView.setViewportView(getPanelSymbolsView());

			jScrollPaneSymbolsView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPaneSymbolsView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jScrollPaneSymbolsView;
	}

	/**
	 * 获取符号展示面板
	 * 
	 * @return
	 */
	protected JPanel getPanelSymbolsView() {
		if (jPanelSymbolsView == null) {
			jPanelSymbolsView = new JPanel();
			jPanelSymbolsView.setBackground(Color.white);
			jPanelSymbolsView.setLayout(new FlowLayout(FlowLayout.LEFT));

			jPanelSymbolsView.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					abstrjPanelSymmbolsViewLisenter(e);
				}
			});
		}
		return jPanelSymbolsView;
	}

	private void abstrjPanelSymmbolsViewLisenter(KeyEvent e) {
		LabelInfo currentLabelInfo = getCurrentLabelInfo();
		if (currentLabelInfo == null) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// 2010-08-17 add by xuzw
			// 这里说明一下，m_hashMapRowColumnLabel的键是int[]，切记是对象级的，如果直接
			// 创建一个数组并判断是否在hashMap中，判断必然返回false，因为是对象
			changeCurrentLabel(getNextLabel(currentLabelInfo.getRow(), currentLabelInfo.getColumn() - 1));
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// 修改当前的选中的JLabel
			changeCurrentLabel(getNextLabel(currentLabelInfo.getRow(), currentLabelInfo.getColumn() + 1));
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			// 修改当前的选中的JLabel
			changeCurrentLabel(getNextLabel(currentLabelInfo.getRow() - 1, currentLabelInfo.getColumn()));
			// 得到要显示的那个JLabel的矩形
			Rectangle rectangle = getCellRect(currentLabelInfo.getRow() - 1, currentLabelInfo.getColumn());
			if (rectangle != null) {
				// 移动到要显示的那个矩形，这种实现思路是按照JDK的JTabel实现的
				// JTabel就能实现键盘向下移动同时滚动条合理下移的效果
				jPanelSymbolsView.scrollRectToVisible(rectangle);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			changeCurrentLabel(getNextLabel(currentLabelInfo.getRow() + 1, currentLabelInfo.getColumn()));
			Rectangle rectangle = getCellRect(currentLabelInfo.getRow() + 1, currentLabelInfo.getColumn());
			if (rectangle != null) {
				jPanelSymbolsView.scrollRectToVisible(rectangle);
			}
		}
	}

	/**
	 * 刷新符号库面板
	 */
	protected void refreshSymbolsViewPanel() {
		Object[] selectPaths = jTreeSymbols.getSelectionPath().getPath();
		currentSymbolGroup = getSymbolGroup(selectPaths);
		if (currentSymbolType.equals(SymbolType.MARKER)) {
			paintMarkerSymbols();
		} else if (currentSymbolType.equals(SymbolType.LINE)) {
			paintLineSymbols();
		} else if (currentSymbolType.equals(SymbolType.FILL)) {
			paintFillSymbols();
		}
		if (allLabelInfo != null) {
			allLabelInfo.clear();
		}
		allLabelInfo = (ArrayList<LabelInfo>) labelInfoArray.clone();
	}

	/**
	 * 根据树的路径找到符号库分组
	 * 
	 * @param selectPaths
	 * @return
	 */
	protected SymbolGroup getSymbolGroup(Object[] selectPaths) {
		int length = selectPaths.length;
		// 应该从1开始，因为第0个是根组的名字
		int i = 1;
		SymbolGroup result = symbolRootGroup;
		while (i < length) {
			result = result.getChildGroups().get(selectPaths[i].toString());
			i++;
		}
		return result;
	}

	/**
	 * 在符号展示面板上绘制所有点符号
	 */
	protected void paintMarkerSymbols() {
		JPanel panel = getPanelSymbolsView();
		try {
			int width = 48;
			int height = 48;
			panel.removeAll();
			int count = currentSymbolGroup.getCount();
			int row = 0;

			// 绘制前清空标签信息链表
			labelInfoArray.clear();
			GeoPoint geoPoint = getPaintPoint();
			// 列数，也就是一行能放几个JLabel
			int columnCount = SYMBOL_PANEL_WIDTH / SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE;
			// 循环得到符号并将符号添加到Label，后将Label添加到JPanel
			// 点的系统符号只有一个
			// add by xuzw 2010-09-08 不是根组就不画系统符号了
			if (currentSymbolGroup.equals(symbolRootGroup)) {
				for (int i = 0; i < 1; i++) {
					// 这里减4主要是为了下面的文本显示
					BufferedImage bufferedImage = new BufferedImage(width, height - 4, BufferedImage.TYPE_INT_ARGB);
					geoPoint.getStyle().setMarkerSymbolID(i);
					InternalToolkitControl.internalDraw(geoPoint, currentResources, bufferedImage.getGraphics());

					String symbolName = "System " + i;
					JLabel label = this.getSymbolLabel(symbolName, bufferedImage);
					label.setName(String.valueOf(i));

					if (i != 0 && i % (columnCount) == 0) {
						row++;
					}
					panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLMARKER_PANEL_HEIGHT_INCREMENT));
					panel.add(label);

					LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, i, symbolName);
					labelInfoArray.add(labelInfo);
				}
				for (int i = 1; i < count + 1; i++) {
					Symbol symbol = currentSymbolGroup.get(i - 1);
					BufferedImage bufferedImage = null;
					// 二维点符号与二三维点符号分别处理
					if (symbol instanceof SymbolMarker) {
						bufferedImage = getSymbolMarkerBufferdImage(width, height, (SymbolMarker) symbol, geoPoint);
					} else {
						bufferedImage = ((SymbolMarker3D) symbol).getThumbnail();
					}

					JLabel label = this.getSymbolLabel(symbol.getName(), bufferedImage);
					label.setName(String.valueOf(symbol.getID()));

					if (i % (columnCount) == 0) {
						row++;
					}
					panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLMARKER_PANEL_HEIGHT_INCREMENT));
					panel.add(label);

					LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
					labelInfoArray.add(labelInfo);
				}
			} else {
				for (int i = 0; i < count; i++) {
					Symbol symbol = currentSymbolGroup.get(i);

					BufferedImage bufferedImage = getSymbolMarkerBufferdImage(width, height, (SymbolMarker) symbol, geoPoint);

					JLabel label = this.getSymbolLabel(symbol.getName(), bufferedImage);
					label.setName(String.valueOf(symbol.getID()));

					if (i != 0 && i % (columnCount) == 0) {
						row++;
					}
					panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLMARKER_PANEL_HEIGHT_INCREMENT));
					panel.add(label);

					LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
					labelInfoArray.add(labelInfo);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			symbolBasicInfoPanel.refreshComboBox(labelInfoArray);
			panel.getParent().validate();
			panel.getParent().repaint();
			panel.validate();
			panel.repaint();
		}
	}

	/**
	 * 获取画点的BufferedImage
	 * 
	 * @param width
	 * @param height
	 * @param symbolMarker
	 * @param geoPoint
	 * @return
	 */
	protected BufferedImage getSymbolMarkerBufferdImage(int width, int height, SymbolMarker symbolMarker, GeoPoint geoPoint) {
		BufferedImage bufferedImage = new BufferedImage(width, height - 4, BufferedImage.TYPE_INT_ARGB);
		Point point = symbolMarker.getOrigin();
		int x = point.x * width / UIEnvironment.symbolPointMax;
		int y = point.y * height / UIEnvironment.symbolPointMax;
		geoPoint.setX(x);
		geoPoint.setY(y);
		geoPoint.getStyle().setMarkerSymbolID(symbolMarker.getID());
		InternalToolkitControl.internalDraw(geoPoint, currentResources, bufferedImage.getGraphics());

		return bufferedImage;
	}

	/**
	 * 在符号展示面板上绘制所有线符号
	 */
	protected void paintLineSymbols() {
		JPanel panel = getPanelSymbolsView();
		try {
			int width = 64;
			int height = 64;
			panel.removeAll();
			int count = currentSymbolGroup.getCount();
			int row = 0;

			labelInfoArray.clear();
			GeoLine geoLine = getPaintLine();
			// 列数，也就是一行能放几个JLabel
			int columnCount = SYMBOL_PANEL_WIDTH / SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE;
			// 循环得到符号并将符号添加到Label，后将Label添加到JPanel
			// 线的系统符号有6个
			// add by xuzw 2010-09-08 不是根组就不画系统符号了
			if (currentSymbolGroup.equals(symbolRootGroup)) {
				for (int i = 0; i < 6; i++) {
					geoLine = getPaintLine();
					try {
						// 这里减8主要是为了下面的文本显示
						BufferedImage bufferedImage = new BufferedImage(width, height - 8, BufferedImage.TYPE_INT_ARGB);
						geoLine.getStyle().setLineSymbolID(i);
						InternalToolkitControl.internalDraw(geoLine, currentResources, bufferedImage.getGraphics());

						String symbolName = "System " + i;
						// 第5个系统线符号是空的，画个NULL上去做标示
						if (i == 5) {
							Graphics graphics = bufferedImage.getGraphics();
							Font font = new Font("Dialog", 0, 14);
							graphics.setFont(font);
							graphics.setColor(new Color(13, 80, 143));
							graphics.drawString("NULL", 16, 38);
						}
						JLabel label = this.getSymbolLabel(symbolName, bufferedImage);
						label.setName(String.valueOf(i));

						if (i != 0 && i % (columnCount) == 0) {
							row++;
						}
						panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLLINE_PANEL_HEIGHT_INCREMENT));
						panel.add(label);

						LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, i, symbolName);
						labelInfoArray.add(labelInfo);
					} finally {
						if (geoLine != null) {
							geoLine.dispose();
						}
					}
				}
				// 前面画了六个系统符号，后面从6开始，以便计算行列，但是要取到所有符号，所以count也要加6
				for (int i = 6; i < count + 6; i++) {
					Symbol symbol = currentSymbolGroup.get(i - 6);

					// 重新拿一个实例，因为后面的绘制方法会导致 geoLine 的 Points 改变，从而无法对齐符号
					geoLine = getPaintLine();
					try {
						BufferedImage bufferedImage = getSymbolLineBufferedImage(width, height, (SymbolLine) symbol, geoLine);
						JLabel label = this.getSymbolLabel(symbol.getName(), bufferedImage);
						label.setName(String.valueOf(symbol.getID()));

						if (i % (columnCount) == 0) {
							row++;
						}
						panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLLINE_PANEL_HEIGHT_INCREMENT));
						panel.add(label);

						LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
						labelInfoArray.add(labelInfo);
					} finally {
						if (geoLine != null) {
							geoLine.dispose();
						}
					}
				}
			} else {
				for (int i = 0; i < count; i++) {
					geoLine = getPaintLine();
					try {
						Symbol symbol = currentSymbolGroup.get(i);
						BufferedImage bufferedImage = getSymbolLineBufferedImage(width, height, (SymbolLine) symbol, geoLine);
						JLabel label = this.getSymbolLabel(symbol.getName(), bufferedImage);
						label.setName(String.valueOf(symbol.getID()));

						if (i != 0 && i % (columnCount) == 0) {
							row++;
						}
						panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLLINE_PANEL_HEIGHT_INCREMENT));
						panel.add(label);

						LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
						labelInfoArray.add(labelInfo);
					} finally {
						if (geoLine != null) {
							geoLine.dispose();
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			symbolBasicInfoPanel.refreshComboBox(labelInfoArray);
			panel.getParent().validate();
			panel.getParent().repaint();
			panel.validate();
			panel.repaint();
		}
	}

	/**
	 * 获取画线的BufferedImage
	 * 
	 * @param width
	 * @param height
	 * @param symbolLine
	 * @param geoLine
	 * @return
	 */
	protected BufferedImage getSymbolLineBufferedImage(int width, int height, SymbolLine symbolLine, GeoLine geoLine) {
		BufferedImage bufferedImage = new BufferedImage(width, height - 8, BufferedImage.TYPE_INT_ARGB);

		geoLine.getStyle().setLineSymbolID(symbolLine.getID());
		// 下面这个方法会改掉 geoLine 的 Points 位置，很奇怪，因此在外面调用的时候不能一直使用同一个 geoLine，这会导致符号无法对齐
		InternalSymbolLine.internalDraw(symbolLine, bufferedImage.getGraphics(), geoLine);
		return bufferedImage;
	}

	/**
	 * 在符号展示面板上绘制所有面符号
	 */
	protected void paintFillSymbols() {
		JPanel panel = getPanelSymbolsView();
		try {
			int width = 75;
			int height = 75;
			panel.removeAll();
			int count = currentSymbolGroup.getCount();
			int row = 0;

			// 初始化HashMap
			labelInfoArray.clear();
			GeoRegion geoRegion = getPaintRegion();
			// 列数，也就是一行能放几个JLabel
			int columnCount = SYMBOLFILL_PANEL_WIDTH / SYMBOLFILL_LABEL_SIZE;

			if (currentSymbolGroup.equals(symbolRootGroup)) {
				// 循环得到符号并将符号添加到Label，后将Label添加到JPanel
				for (int i = 0; i < 7; i++) {
					// 面的系统符号有7个
					BufferedImage bufferedImage = new BufferedImage(width, height - 2, BufferedImage.TYPE_INT_ARGB);
					geoRegion.getStyle().setFillSymbolID(i);
					InternalToolkitControl.internalDraw(geoRegion, currentResources, bufferedImage.getGraphics());

					String symbolName = "System " + i;
					// 第1个系统面符号是空的，画个NULL上去做标示
					if (i == 1) {
						Graphics graphics = bufferedImage.getGraphics();
						Font font = new Font("Dialog", 0, 14);
						graphics.setFont(font);
						graphics.setColor(new Color(13, 80, 143));
						graphics.drawString("NULL", 25, 45);
					}
					JLabel label = this.getSymbolLabelForRegion(symbolName, bufferedImage);
					label.setName(String.valueOf(i));

					if (i != 0 && i % (columnCount) == 0) {
						row++;
					}
					panel.setPreferredSize(new Dimension(SYMBOLFILL_PANEL_WIDTH, (row + 1) * SYMBOLREGION_PANEL_HEIGHT_INCREMENT));
					panel.add(label);

					LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, i, symbolName);
					labelInfoArray.add(labelInfo);
				}
				// 前面画了七个系统符号，后面从7开始，以便计算行列，但是要取到所有符号，所以count也要加7
				for (int i = 7; i < count + 7; i++) {
					Symbol symbol = currentSymbolGroup.get(i - 7);
					BufferedImage bufferedImage = getSymbolFillBufferedImage(width, height, (SymbolFill) symbol, geoRegion);

					JLabel label = this.getSymbolLabelForRegion(symbol.getName(), bufferedImage);
					label.setName(String.valueOf(symbol.getID()));

					if (i % (columnCount) == 0) {
						row++;
					}
					panel.setPreferredSize(new Dimension(SYMBOLFILL_PANEL_WIDTH, (row + 1) * SYMBOLREGION_PANEL_HEIGHT_INCREMENT));
					panel.add(label);

					LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
					labelInfoArray.add(labelInfo);
				}
			} else {
				for (int i = 0; i < count; i++) {
					Symbol symbol = currentSymbolGroup.get(i);
					BufferedImage bufferedImage = getSymbolFillBufferedImage(width, height, (SymbolFill) symbol, geoRegion);

					JLabel label = this.getSymbolLabelForRegion(symbol.getName(), bufferedImage);
					label.setName(String.valueOf(symbol.getID()));

					if (i != 0 && i % (columnCount) == 0) {
						row++;
					}
					panel.setPreferredSize(new Dimension(SYMBOLFILL_PANEL_WIDTH, (row + 1) * SYMBOLREGION_PANEL_HEIGHT_INCREMENT));
					panel.add(label);

					LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
					labelInfoArray.add(labelInfo);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			symbolBasicInfoPanel.refreshComboBox(labelInfoArray);
			panel.getParent().validate();
			panel.getParent().repaint();
			panel.validate();
			panel.repaint();
		}
	}

	/**
	 * 获取画面的BufferedImage
	 * 
	 * @param width
	 * @param height
	 * @param symbolFill
	 * @param geoRegion
	 * @return
	 */
	protected BufferedImage getSymbolFillBufferedImage(int width, int height, SymbolFill symbolFill, GeoRegion geoRegion) {
		BufferedImage bufferedImage = new BufferedImage(width, height - 2, BufferedImage.TYPE_INT_ARGB);
		geoRegion.getStyle().setFillSymbolID(symbolFill.getID());
		InternalToolkitControl.internalDraw(geoRegion, currentResources, bufferedImage.getGraphics());

		return bufferedImage;
	}

	/**
	 * 创建每个符号的JLabel，该方法适用于点和线
	 * 
	 * @param text 符号名称
	 * @param bufferedImage 绘制符号的bufferedImage
	 * @return
	 */
	protected JLabel getSymbolLabel(String text, BufferedImage bufferedImage) {
		ImageIcon icon = new ImageIcon(bufferedImage); // 初始化Icon
		// 初始化Label对象
		JLabel label = new JLabel(text, icon, JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setPreferredSize(new Dimension(SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE, SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE));
		label.setBackground(Color.WHITE);
		label.addMouseListener(symbolsViewRefresh);

		return label;
	}

	/**
	 * 创建每个符号的JLabel，该方法适用于面
	 * 
	 * @param text 符号名称
	 * @param bufferedImage 绘制符号的bufferedImage
	 * @return
	 */
	protected JLabel getSymbolLabelForRegion(String text, BufferedImage bufferedImage) {
		ImageIcon icon = new ImageIcon(bufferedImage); // 初始化Icon
		// 初始化Label对象
		JLabel label = new JLabel(text, icon, JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setPreferredSize(new Dimension(SYMBOLFILL_LABEL_SIZE, SYMBOLFILL_LABEL_SIZE));
		label.setBackground(Color.WHITE);
		label.addMouseListener(new SymbolsViewRefresh());

		return label;
	}

	/**
	 * 设置树模型内容
	 */
	private void setTreeModel(JTree tree, SymbolGroup rootGroup) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootGroup.getName());
		setSymbolTree(rootNode, rootGroup);
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		tree.setModel(model);
		tree.setSelectionRow(0);
	}

	/**
	 * 构造树模型
	 */
	private void setSymbolTree(DefaultMutableTreeNode node, SymbolGroup markerGroup) {
		SymbolGroups symbolGroups = markerGroup.getChildGroups();
		for (int i = 0; i < symbolGroups.getCount(); i++) {
			SymbolGroup symbolGroup = symbolGroups.get(i);
			DefaultMutableTreeNode symbolGroupsNode = new DefaultMutableTreeNode(symbolGroup.getName());
			node.add(symbolGroupsNode);

			if (symbolGroup.getChildGroups() != null && symbolGroup.getChildGroups().getCount() > 0) {
				setSymbolTree(symbolGroupsNode, symbolGroup);
			}
		}
	}

	/**
	 * 获取一个画点符号的点几何对象
	 * 
	 * @return
	 */
	protected GeoPoint getPaintPoint() {
		GeoPoint paintPoint = new GeoPoint(24, 24);
		GeoStyle markerGeoStyle = new GeoStyle();
		markerGeoStyle.setMarkerSize(new Size2D(4, 4));
		markerGeoStyle.setLineColor(new Color(13, 80, 143));
		paintPoint.setStyle(markerGeoStyle);
		return paintPoint;
	}

	/**
	 * 获取一个画线符号的线几何对象
	 * 
	 * @return
	 */
	protected GeoLine getPaintLine() {
		// 初始化线对象并设置风格
		Point2Ds point2Ds = new Point2Ds();
		point2Ds.add(new Point2D(5, 32));
		point2Ds.add(new Point2D(60, 32));
		GeoLine paintLine = new GeoLine(point2Ds);
		GeoStyle lineGeoStyle = new GeoStyle();
		lineGeoStyle.setLineColor(new Color(13, 80, 143));
		lineGeoStyle.setLineWidth(0.2);
		paintLine.setStyle(lineGeoStyle);
		return paintLine;
	}

	/**
	 * 获取一个画面符号的面几何对象
	 * 
	 * @return
	 */
	protected GeoRegion getPaintRegion() {
		Point2Ds point2Ds = new Point2Ds();
		point2Ds.add(new Point2D(5, 5));
		point2Ds.add(new Point2D(5, 75));
		point2Ds.add(new Point2D(75, 75));
		point2Ds.add(new Point2D(75, 5));
		GeoRegion paintregion = new GeoRegion(point2Ds);
		GeoStyle fillGeoStyle = new GeoStyle();
		fillGeoStyle.setFillForeColor(new Color(13, 80, 143));
		fillGeoStyle.setLineWidth(0.1);
		paintregion.setStyle(fillGeoStyle);
		return paintregion;
	}

	/**
	 * 获取标签信息链表
	 * 
	 * @return
	 */
	protected ArrayList<LabelInfo> getLabelInfoArray() {
		return labelInfoArray;
	}

	protected ArrayList<LabelInfo> getAllLabelInfo() {
		return allLabelInfo;
	}

	/**
	 * 遍历m_labelInfoArray，根据符号ID获得相应的标签信息对象
	 * 
	 * @param symbolID 符号ID
	 * @return
	 */
	protected LabelInfo getLabelInfoBySymbolID(int symbolID) {
		LabelInfo result = null;
		for (int i = 0; i < labelInfoArray.size(); i++) {
			LabelInfo labelInfo = labelInfoArray.get(i);
			int id = labelInfo.getSymbolID();
			if (id == symbolID) {
				result = labelInfo;
				break;
			}
		}
		return result;
	}

	/**
	 * 遍历m_labelInfoArray，根据符号名获得相应的标签信息对象
	 * 
	 * @param symbolName 符号名
	 * @return
	 */
	protected LabelInfo getLabelInfoBySymbolName(String symbolName) {
		LabelInfo result = null;
		for (int i = 0; i < labelInfoArray.size(); i++) {
			LabelInfo labelInfo = labelInfoArray.get(i);
			String name = labelInfo.getSymbolName();
			if (name.equals(symbolName)) {
				result = labelInfo;
				break;
			}
		}
		return result;
	}

	/**
	 * 设置是否仅显示符号库基类，该方法在SymbolLibararyPanel中使用
	 * 
	 * @param value
	 */
	protected void setSymbolLibraryPanel(boolean value) {
		isSymbolLibraryPanel = value;
	}

	/**
	 * 获取导入符号文件选择器
	 * 
	 * @return
	 */
	protected JFileChooser getFileChooserImportSymFile() {
		return fileChooserImportSymFile;
	}

	/**
	 * 获取导入栅格符号选择器
	 * 
	 * @return
	 */
	protected JFileChooser getFileChooserImportGridSymbol() {
		return fileChooserImportSymFile;
	}

	/**
	 * 获取根组
	 * 
	 * @return
	 */
	protected SymbolGroup getRootGroup() {
		return symbolRootGroup;
	}

	/**
	 * 返回位于row和column相交位置的单元格矩形
	 * 
	 * @param row 行
	 * @param column 列
	 * @return
	 */
	protected Rectangle getCellRect(int row, int column) {
		if (row < 0 || column < 0) {
			return null;
		}
		Rectangle result = new Rectangle();
		if (currentSymbolType.equals(SymbolType.MARKER) || currentSymbolType.equals(SymbolType.LINE)) {

			result.height = SYMBOLMARKER_AND_SYMBOLLINE_ROW_HEIGHT;
			result.y = SYMBOLMARKER_AND_SYMBOLLINE_ROW_HEIGHT * row;

			result.width = SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE;
			result.x = SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE * column;
		} else {
			result.height = SYMBOLFILL_ROW_HEIGHT;
			result.y = SYMBOLFILL_ROW_HEIGHT * row;

			result.width = SYMBOLFILL_LABEL_SIZE;
			result.x = SYMBOLFILL_LABEL_SIZE * column;
		}

		result.setBounds(result.x, result.y, result.width, result.height);
		return result;
	}

	/**
	 * 根据传入的行列号获取一个JLabel
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	private JLabel getNextLabel(int row, int column) {
		JLabel resultLabel = null;
		for (int i = 0; i < labelInfoArray.size(); i++) {
			LabelInfo labelInfo = labelInfoArray.get(i);
			if (labelInfo.getRow() == row && labelInfo.getColumn() == column) {
				resultLabel = labelInfo.getLabel();
				break;
			}
		}
		return resultLabel;
	}

	/**
	 * 获取当前选中JLabel的标签信息
	 * 
	 * @return
	 */
	private LabelInfo getCurrentLabelInfo() {
		LabelInfo result = null;
		if (currentLabel != null) {
			for (int i = 0; i < labelInfoArray.size(); i++) {
				LabelInfo labelInfo = labelInfoArray.get(i);
				if (currentLabel.equals(labelInfo.getLabel())) {
					result = labelInfo;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 根据符号库分组找到树的路径
	 * 
	 * @param selectPaths
	 * @author zhaosy
	 */
	private void getTreePath() {
		int i = 1;
		int n = 0;
		SymbolLibrary symbolMarkerLibrary = currentResources.getMarkerLibrary();
		SymbolGroup symbolGroup = symbolMarkerLibrary.findGroup(activeStyle.getMarkerSymbolID());
		// 遍历父目录，压入栈，后进先出就是path
		Stack<String> stack = new Stack<String>();
		stack.push(symbolGroup.getName());
		while (symbolGroup.getParent() != null) {
			symbolGroup = symbolGroup.getParent();
			stack.push(symbolGroup.getName());
			i++;
		}
		symbolTreePath = new Object[i];
		while (stack.empty() != true) {
			symbolTreePath[n] = new DefaultMutableTreeNode(stack.pop());
			n++;
		}
	}

	/**
	 * 更换当前的JLabel，同时对各个面板都要进行刷新
	 * 
	 * @param label
	 */
	protected void changeCurrentLabel(JLabel label) {
		if (label == null) {
			return;
		}
		currentLabel = label;
		currentLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 1));
		if (preLabel != null && !currentLabel.equals(preLabel)) {
			preLabel.setBorder(null);
		}
		preLabel = currentLabel;

		if (!isSymbolLibraryPanel) {
			int symbolID = Integer.parseInt(label.getName());
			if (currentSymbolType.equals(SymbolType.MARKER)) {
				activeStyle.setMarkerSymbolID(symbolID);
			} else if (currentSymbolType.equals(SymbolType.LINE)) {
				activeStyle.setLineSymbolID(symbolID);
			} else if (currentSymbolType.equals(SymbolType.FILL)) {
				activeStyle.setFillSymbolID(symbolID);
			}
			// 刷新预览框
			symbolPreViewPanel.refreshPreViewMapControl(currentSymbolType, activeStyle);
			// 刷新基本信息面板
			LabelInfo labelInfo = getLabelInfoBySymbolID(symbolID);
			symbolBasicInfoPanel.refreshBasicInfo(symbolID, labelInfo.getSymbolName());

			// TODO 系统字段隐藏透明设置中的透明设置百分比
//			symbolSettingPanel.refreshS
		}
	}

	/**
	 * 获取基础信息面板，因为查询时需要刷新基础面板中的JComboBox
	 * 
	 * @return
	 */
	SymbolBasicInfoPanel getSymbolBasicInfoPanel() {
		return symbolBasicInfoPanel;
	}

	/**
	 * 符号展示的鼠标监听类，当鼠标按下时相应的面板要做刷新
	 * 
	 * @author xuzw
	 * 
	 */
	class SymbolsViewRefresh extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			try {
				JLabel label = (JLabel) e.getSource();
				refreshSymbolLabel(label);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}

		/**
		 * 高亮显示选中的JLabel，同时根据选中的点符号刷新预览框
		 * 
		 * @param label 选中的JLabel
		 */
		protected void refreshSymbolLabel(JLabel label) {
			jPanelSymbolsView.requestFocus();
			jPanelSymbolsView.requestFocusInWindow();
			changeCurrentLabel(label);
			if (!isSymbolLibraryPanel && isFirstMousePressed) {
				// 第一次鼠标点击移除预览框中map的监听
				symbolPreViewPanel.removeMapTrackingLayerDrawingListener();
				isFirstMousePressed = false;
			}
		}
	}
}

/**
 * 内部类型，该类型维护符号浏览面板中，每个符号的相关信息
 * 
 * @author xuzw
 * 
 */
class LabelInfo {
	private JLabel labelSymbol;
	private int row;
	private int column;
	private int symbolID;
	private String symbolName;

	public LabelInfo(JLabel label, int row, int column, int symbolID, String symbolName) {
		labelSymbol = label;
		this.row = row;
		this.column = column;
		this.symbolID = symbolID;
		this.symbolName = symbolName;
	}

	public JLabel getLabel() {
		return labelSymbol;
	}

	public void setLabel(JLabel label) {
		this.labelSymbol = label;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getSymbolID() {
		return symbolID;
	}

	public void setSymbolID(int symbolid) {
		symbolID = symbolid;
	}

	public String getSymbolName() {
		return symbolName;
	}

	public void setSymbolName(String name) {
		symbolName = name;
	}

	@Override
	public String toString() {
		return symbolName;
	}
}
