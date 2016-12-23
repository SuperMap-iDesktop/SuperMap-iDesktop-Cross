package com.supermap.desktop.newtheme.themeUnique;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRegion3D;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoStyle3D;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometry;
import com.supermap.data.Geometry3D;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.controls.utilities.SymbolDialogFactory;
import com.supermap.desktop.dialog.symbolDialogs.ISymbolApply;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeItemLabelDecorator;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.ui.controls.JDialogSymbolsChange;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeType;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static com.supermap.data.CursorType.STATIC;
import static com.supermap.desktop.Application.getActiveApplication;

/**
 * 单值专题图
 *
 * @author xie
 */
public class ThemeUniqueContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPaneInfo = new JTabbedPane(JTabbedPane.TOP);
	private JPanel panelProperty = new JPanel();
	private JPanel panelAdvance = new JPanel();
	private JLabel labelExpression = new JLabel();
	private JComboBox<String> comboBoxExpression = new JComboBox<String>();
	private JLabel labelColorStyle = new JLabel();
	private ColorsComboBox comboboxColor = new ColorsComboBox(ThemeType.UNIQUE);
	private JToolBar toolBar = new JToolBar();
	private JScrollPane scollPane = new JScrollPane();
	private JTable tableUniqueInfo = new JTable();
	private JButton buttonVisble = new JButton();
	private JButton buttonGeoStyle = new JButton();
	private JButton buttonAdd = new JButton();
	private JButton buttonDelete = new JButton();
	private JButton buttonAscend = new JButton();
	private JButton buttonDescend = new JButton();
	private JButton buttonAntitone = new JButton();
	//增加关联浏览按钮开关
	private JButton buttonContinuousMapLocation = new JButton();
	//是否进行关联浏览
	private boolean isContinuousMapLocation = false;
	//增加子项右键菜单
	private JPopupMenu tablePopupMenuUniqurTheme = new JPopupMenu();
	//修改风格
	private JMenuItem menuItemReviseStyle = new JMenuItem();
	//删除
	private JMenuItem menuItemDelete = new JMenuItem();
	//定位
	private JMenuItem menuItemMapLocation = new JMenuItem();
	//跟踪层
	private TrackingLayer uniqurThemetrackingLayer;

	private JPanel panelOffsetSet = new JPanel();
	private JLabel labelOffsetUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetUnity = new JComboBox<String>();
	private JLabel labelOffsetX = new JLabel();
	private JLabel labelOffsetXUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetX = new JComboBox<String>();
	private JLabel labelOffsetY = new JLabel();
	private JLabel labelOffsetYUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetY = new JComboBox<String>();
	private AddItemPanel addItemPanel;
	private String[] nameStrings = {MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_Sytle"),
			MapViewProperties.getString("String_ThemeGraphItemManager_UniqueValue"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption")};
	private transient ThemeUnique themeUnique;
	private transient DatasetVector datasetVector;
	private transient Layer themeUniqueLayer;
	private transient Map map;
	private ArrayList<ThemeUniqueItem> deleteItems = new ArrayList<ThemeUniqueItem>();
	private boolean isRefreshAtOnce = true;
	private String expression;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private String layerName;
	private ArrayList<String> comboBoxArray = new ArrayList<String>();
	private ArrayList<String> comboBoxArrayForOffsetX = new ArrayList<String>();
	private ArrayList<String> comboBoxArrayForOffsetY = new ArrayList<String>();
	private boolean isNewTheme = false;

	//private Selection nowSelection = new Selection();

	private IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
	private MapControl nowMapControl = formMap.getMapControl();

	private static int TABLE_COLUMN_VISIBLE = 0;
	private static int TABLE_COLUMN_GEOSTYLE = 1;
	private static int TABLE_COLUMN_UNIQUE = 2;
	private static int TABLE_COLUMN_CAPTION = 3;
	//连续地图定位的tag
	private static final String TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE = "Tag_ContinuousMapLocation_ThemeUnique";

	private transient LocalComboBoxItemListener comboBoxItemListener = new LocalComboBoxItemListener();
	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalTableMouseListener localTableMouseListener = new LocalTableMouseListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalPopmenuListener popmenuListener = new LocalPopmenuListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();
	private LayersTreeChangeListener layersTreePropertyChangeListener = new LayersTreeChangeListener();
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent arg0) {
			//此处动态刷新字段信息
			if (arg0.getSource().equals(comboBoxExpression.getComponent(0))) {
				// 刷新表达式字段信息
				ThemeUtil.initComboBox(comboBoxExpression, themeUnique.getUniqueExpression(), datasetVector, themeUniqueLayer.getDisplayFilter().getJoinItems(),
						comboBoxArray, false, false);
			} else if (arg0.getSource().equals(comboBoxOffsetX.getComponent(0))) {
				// 刷新水平偏移量字段信息
				ThemeUtil.initComboBox(comboBoxOffsetX, themeUnique.getOffsetX(), datasetVector, themeUniqueLayer.getDisplayFilter().getJoinItems(),
						comboBoxArrayForOffsetX, true, true);
			} else if (arg0.getSource().equals(comboBoxOffsetY.getComponent(0))) {
				// 刷新垂直偏移量字段信息
				ThemeUtil.initComboBox(comboBoxOffsetY, themeUnique.getOffsetY(), datasetVector, themeUniqueLayer.getDisplayFilter().getJoinItems(),
						comboBoxArrayForOffsetY, true, true);
			}
		}
	};

	public ThemeUniqueContainer(Layer layer, boolean isNewTheme) {
		this.themeUniqueLayer = layer;

		this.isNewTheme = isNewTheme;
		this.layerName = this.themeUniqueLayer.getName();
		this.themeUnique = new ThemeUnique((ThemeUnique) themeUniqueLayer.getTheme());
		this.datasetVector = (DatasetVector) layer.getDataset();

		this.map = ThemeGuideFactory.getMapControl().getMap();
		//获得跟踪层
		this.uniqurThemetrackingLayer = this.map.getTrackingLayer();
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);

		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		//右键菜单布局
		this.tablePopupMenuUniqurTheme.add(this.menuItemReviseStyle);
		this.tablePopupMenuUniqurTheme.add(this.menuItemDelete);
		//暂时移除定位菜单
		//this.tablePopupMenuUniqurTheme.add(this.menuItemMapLocation);

		if (isNewTheme) {
			refreshColor();
			refreshAtOnce();
		}
		initPanelProperty();
		initPanelAdvance();
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelExpression.setText(MapViewProperties.getString("String_label_Expression"));
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.buttonVisble.setToolTipText(MapViewProperties.getString("String_Title_Visible"));
		this.buttonGeoStyle.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonAdd.setToolTipText(MapViewProperties.getString("String_Title_Add"));
		this.buttonDelete.setToolTipText(MapViewProperties.getString("String_Title_Delete"));
		this.buttonAscend.setToolTipText(MapViewProperties.getString("String_Title_Ascend"));
		this.buttonDescend.setToolTipText(MapViewProperties.getString("String_Title_Descend"));
		this.buttonAntitone.setToolTipText(MapViewProperties.getString("String_Title_Antitone"));
		//增加连续定位按钮开关
		this.buttonContinuousMapLocation.setToolTipText(MapViewProperties.getString("String_ContinuousMapLocation"));
		this.panelOffsetSet.setBorder(new TitledBorder(null, MapViewProperties.getString("String_GroupBoxOffset"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.labelOffsetUnity.setText(MapViewProperties.getString("String_LabelOffsetUnit"));
		this.labelOffsetX.setText(MapViewProperties.getString("String_LabelOffsetX"));
		this.labelOffsetY.setText(MapViewProperties.getString("String_LabelOffsetY"));
		//给jtable右键菜单设置资源化
		this.menuItemReviseStyle.setText(MapViewProperties.getString("String_ThemeGraphItemManager_ModifyStyle"));
		this.menuItemDelete.setText(MapViewProperties.getString("String_Title_Delete"));
		this.menuItemMapLocation.setText(MapViewProperties.getString("String_MapLocation"));
	}

	class LayersTreeChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			int[] selectRows = tableUniqueInfo.getSelectedRows();
			// 属性修改后原有的map，themeUniqueLayer,themeUnique已经不存在，需要重新赋值
			map = ThemeGuideFactory.getMapControl().getMap();
			themeUniqueLayer = MapUtilities.findLayerByName(map, layerName);
			if (null != themeUniqueLayer && null != themeUniqueLayer.getTheme() && themeUniqueLayer.getTheme() instanceof ThemeUnique) {
				datasetVector = (DatasetVector) themeUniqueLayer.getDataset();
				themeUnique = new ThemeUnique((ThemeUnique) themeUniqueLayer.getTheme());
				getTable();
				map.refresh();
				for (int i = 0; i < selectRows.length; i++) {
					tableUniqueInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			}
		}
	}

	/**
	 * 控件注册事件
	 */
	public void registActionListener() {
		unregistActionListener();
		this.comboBoxExpression.addItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetX.addItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetY.addItemListener(this.comboBoxItemListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(mouseAdapter);
		this.comboBoxOffsetX.getComponent(0).addMouseListener(mouseAdapter);
		this.comboBoxOffsetY.getComponent(0).addMouseListener(mouseAdapter);
		this.comboboxColor.addItemListener(this.comboBoxItemListener);
		this.comboboxColor.addColorChangedListener();
		this.comboBoxOffsetUnity.addItemListener(this.comboBoxItemListener);
		this.buttonVisble.addActionListener(this.actionListener);
		this.buttonAdd.addActionListener(this.actionListener);
		this.buttonDelete.addActionListener(this.actionListener);
		this.buttonGeoStyle.addActionListener(this.actionListener);
		this.buttonAntitone.addActionListener(this.actionListener);
		//关联浏览按钮开关添加监听
		this.buttonContinuousMapLocation.addActionListener(this.actionListener);
		//增加jtable右键菜单项的监听
		this.menuItemReviseStyle.addActionListener(this.actionListener);
		this.menuItemDelete.addActionListener(this.actionListener);
		this.menuItemMapLocation.addActionListener(this.actionListener);
		//給mapContorl添加键盘监听
		this.nowMapControl.addKeyListener(this.localKeyListener);

		this.tableUniqueInfo.addMouseListener(this.localTableMouseListener);
		this.tableUniqueInfo.addKeyListener(this.localKeyListener);
		this.tableUniqueInfo.putClientProperty("terminateEditOnFocusLost", true);
		this.tableUniqueInfo.getModel().addTableModelListener(this.tableModelListener);
		this.layersTree.addPropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
	}

	/**
	 * 修改单值项
	 */
	private void setUniqueItemUnique(String uniqueValue) {
		int index = 0;
		if (0 <= this.tableUniqueInfo.getSelectedRow() && !uniqueValue.isEmpty() && !hasUnique(uniqueValue)) {
			index = this.tableUniqueInfo.getSelectedRow();
			if (index != tableUniqueInfo.getRowCount() - 1) {
				this.themeUnique.getItem(index).setUnique(uniqueValue);
			}
		}
	}

	/**
	 * 修改单值标题
	 */
	private void setUniqueItemCaption(String caption) {
		int index = 0;
		if (0 <= this.tableUniqueInfo.getSelectedRow() && !caption.isEmpty()) {
			index = this.tableUniqueInfo.getSelectedRow();
			if (index != tableUniqueInfo.getRowCount() - 1) {
				this.themeUnique.getItem(index).setCaption(caption);
			}
		}
	}

	/**
	 * 销毁事件
	 */
	public void unregistActionListener() {
		this.comboBoxExpression.removeItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetX.removeItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetY.removeItemListener(this.comboBoxItemListener);
		this.comboBoxExpression.getComponent(0).removeMouseListener(mouseAdapter);
		this.comboBoxOffsetX.getComponent(0).removeMouseListener(mouseAdapter);
		this.comboBoxOffsetY.getComponent(0).removeMouseListener(mouseAdapter);
		this.comboboxColor.removeItemListener(this.comboBoxItemListener);
		this.comboboxColor.removeColorChangedListener();
		this.comboBoxOffsetUnity.removeItemListener(this.comboBoxItemListener);
		this.buttonVisble.removeActionListener(this.actionListener);
		this.buttonAdd.removeActionListener(this.actionListener);
		this.buttonDelete.removeActionListener(this.actionListener);
		this.buttonGeoStyle.removeActionListener(this.actionListener);
		this.buttonAntitone.removeActionListener(this.actionListener);
		//注销关联浏览
		this.buttonContinuousMapLocation.removeActionListener(this.actionListener);
		//注销jtable右键菜单项的监听
		this.menuItemReviseStyle.removeActionListener(this.actionListener);
		this.menuItemDelete.removeActionListener(this.actionListener);
		this.menuItemMapLocation.removeActionListener(this.actionListener);
		//trackingLayer移除键盘监听
		this.nowMapControl.removeKeyListener(this.localKeyListener);

		this.tableUniqueInfo.removeMouseListener(this.localTableMouseListener);
		this.tableUniqueInfo.removeKeyListener(this.localKeyListener);
		this.tableUniqueInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.layersTree.removePropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
	}

	/**
	 * 高级面板布局
	 */
	private void initPanelAdvance() {
		this.panelAdvance.setLayout(new GridBagLayout());
		initComboBoxOffsetUnity();
		initComboBoxOffsetX();
		initComboBoxOffsetY();
		//@formatter:off
		this.panelAdvance.add(this.panelOffsetSet, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		this.panelOffsetSet.setLayout(new GridBagLayout());
		this.panelOffsetSet.add(this.labelOffsetUnity,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(20, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.comboBoxOffsetUnity, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(80, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetX,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(20, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.comboBoxOffsetX,     new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetXUnity,   new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(10, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.labelOffsetY,        new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(20, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.comboBoxOffsetY,     new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetYUnity,   new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(20, 1).setIpad(20, 0));
		//@formatter:on
	}

	/**
	 * 初始化偏移量单位
	 */
	private void initComboBoxOffsetUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[]{
				MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map")}));
		if (this.themeUnique.isOffsetFixed()) {
			this.comboBoxOffsetUnity.setSelectedIndex(0);
		} else {
			this.comboBoxOffsetUnity.setSelectedIndex(1);
			this.labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
			this.labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
		}
	}

	/**
	 * 初始化水平偏移量
	 */
	private void initComboBoxOffsetX() {
		ThemeUtil.initComboBox(comboBoxOffsetX, themeUnique.getOffsetX(), datasetVector, themeUniqueLayer.getDisplayFilter().getJoinItems(),
				comboBoxArrayForOffsetX, true, true);
	}

	/**
	 * 初始化垂直偏移量
	 */
	private void initComboBoxOffsetY() {
		ThemeUtil.initComboBox(comboBoxOffsetY, themeUnique.getOffsetY(), datasetVector, themeUniqueLayer.getDisplayFilter().getJoinItems(),
				comboBoxArrayForOffsetY, true, true);
	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonVisble);
		this.toolBar.add(this.buttonGeoStyle);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonAdd);
		this.toolBar.add(this.buttonDelete);
		this.toolBar.add(this.buttonAntitone);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonContinuousMapLocation);
		this.buttonVisble.setIcon(InternalImageIconFactory.VISIBLE);
		if (CommonToolkit.DatasetTypeWrap.isRegion(datasetVector.getType())) {
			this.buttonGeoStyle.setIcon(InternalImageIconFactory.REGION_STYLE);
		} else if (CommonToolkit.DatasetTypeWrap.isLine(datasetVector.getType())) {
			this.buttonGeoStyle.setIcon(InternalImageIconFactory.LINE_STYLE);
		} else {
			this.buttonGeoStyle.setIcon(InternalImageIconFactory.POINT_STYLE);
		}
		this.buttonAdd.setIcon(InternalImageIconFactory.ADD_ITEM);
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonAntitone.setIcon(InternalImageIconFactory.Rever);
		//给关闭关联浏览设置图标
		this.buttonContinuousMapLocation.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_OpenLinkageLayer.png"));
	}

	/**
	 * 属性面板布局
	 */
	private void initPanelProperty() {
		this.panelProperty.setLayout(new GridBagLayout());
		initToolBar();
		initComboBoxExpression();
		//@formatter:off
		this.panelProperty.add(this.labelExpression,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboBoxExpression, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelColorStyle,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboboxColor,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolBar,            new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(100, 0));
		this.panelProperty.add(this.scollPane,          new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(100, 3));
		getTable();
		this.tableUniqueInfo.setRowSelectionInterval(0, 0);
		this.scollPane.setViewportView(tableUniqueInfo);		
		//@formatter:on
	}

	/**
	 * 初始化表达式
	 */
	private void initComboBoxExpression() {
		ThemeUtil.initComboBox(comboBoxExpression, themeUnique.getUniqueExpression(), datasetVector, themeUniqueLayer.getDisplayFilter().getJoinItems(),
				comboBoxArray, false, false);
	}

	/**
	 * 表格初始化
	 *
	 * @return m_table
	 */
	private JTable getTable() {
		int uniqueCount = this.themeUnique.getCount();
		DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[uniqueCount + 1][4], nameStrings) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({"unchecked", "rawtypes"})
			@Override
			public Class getColumnClass(int column) {// 要这样定义table，要重写这个方法0，0的意思就是别的格子的类型都跟0,0的一样。
				if (TABLE_COLUMN_VISIBLE == column || TABLE_COLUMN_GEOSTYLE == column) {
					return getValueAt(0, 0).getClass();
				}
				return String.class;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == TABLE_COLUMN_UNIQUE || columnIndex == TABLE_COLUMN_CAPTION;
			}

		};
		this.tableUniqueInfo.setModel(defaultTableModel);
		initColumnIcon();
		this.tableUniqueInfo.setRowHeight(20);

		TableColumn visibleColumn = tableUniqueInfo.getColumn(MapViewProperties.getString("String_Title_Visible"));
		TableColumn viewColumn = tableUniqueInfo.getColumn(MapViewProperties.getString("String_Title_Sytle"));
		visibleColumn.setMaxWidth(40);
		viewColumn.setMaxWidth(100);
		// 先注销事件再注册事件，避免重复注册事件
		this.tableUniqueInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.tableUniqueInfo.getModel().addTableModelListener(this.tableModelListener);
		return this.tableUniqueInfo;
	}

	/**
	 * 填充图片
	 */
	private void initColumnIcon() {
		int uniqueCount = this.themeUnique.getCount();
		for (int i = 0; i < uniqueCount; i++) {
			ThemeUniqueItem uniqueItem = themeUnique.getItem(i);
			boolean isVisible = uniqueItem.isVisible();
			ImageIcon visibleIcon = InternalImageIconFactory.VISIBLE;
			if (!isVisible) {
				visibleIcon = InternalImageIconFactory.INVISIBLE;
			}
			this.tableUniqueInfo.setValueAt(visibleIcon, i, TABLE_COLUMN_VISIBLE);
			GeoStyle geoStyle = uniqueItem.getStyle();
			this.tableUniqueInfo.setValueAt(ThemeItemLabelDecorator.buildGeoStyleIcon(datasetVector, geoStyle), i, TABLE_COLUMN_GEOSTYLE);
			this.tableUniqueInfo.setValueAt(uniqueItem.getUnique(), i, TABLE_COLUMN_UNIQUE);
			this.tableUniqueInfo.setValueAt(uniqueItem.getCaption(), i, TABLE_COLUMN_CAPTION);
		}
		boolean defualtVisibleIcon = this.themeUnique.isDefaultStyleVisible();
		ImageIcon icon = InternalImageIconFactory.VISIBLE;
		if (!defualtVisibleIcon) {
			icon = InternalImageIconFactory.INVISIBLE;
		}
		GeoStyle defaultStyle = this.themeUnique.getDefaultStyle();
		this.tableUniqueInfo.setValueAt(icon, uniqueCount, TABLE_COLUMN_VISIBLE);
		this.tableUniqueInfo.setValueAt(ThemeItemLabelDecorator.buildGeoStyleIcon(datasetVector, defaultStyle), uniqueCount, TABLE_COLUMN_GEOSTYLE);
		this.tableUniqueInfo.setValueAt(MapViewProperties.getString("String_defualt_style"), uniqueCount, TABLE_COLUMN_CAPTION);
	}

	class LocalTableMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (1 == e.getClickCount() && tableUniqueInfo.getSelectedColumn() == TABLE_COLUMN_VISIBLE && tableUniqueInfo.getSelectedRows().length == 1) {
				if (tableUniqueInfo.getSelectedRow() != tableUniqueInfo.getRowCount() - 1) {
					int selectRow = tableUniqueInfo.getSelectedRow();
					ThemeUniqueItem item = themeUnique.getItem(selectRow);
					boolean isVisible = item.isVisible();
					if (isVisible) {
						item.setVisible(false);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
					} else {
						item.setVisible(true);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
					}
				} else {
					boolean isDefaultStyle = themeUnique.isDefaultStyleVisible();
					if (isDefaultStyle) {
						themeUnique.setDefaultStyleVisible(false);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, tableUniqueInfo.getRowCount() - 1, TABLE_COLUMN_VISIBLE);
					} else {
						themeUnique.setDefaultStyleVisible(true);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, tableUniqueInfo.getRowCount() - 1, TABLE_COLUMN_VISIBLE);
					}
				}
			} else if (2 == e.getClickCount() && tableUniqueInfo.getSelectedColumn() == TABLE_COLUMN_GEOSTYLE) {
				int selectRow = tableUniqueInfo.getSelectedRow();
				setItemGeoSytle();
				tableUniqueInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			} else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {//打开右键菜单
				//打开右键菜单根据是否可视(可选择)状态设置定位功能是否可用
				if (themeUniqueLayer.isVisible() && themeUniqueLayer.isSelectable()) {
					menuItemMapLocation.setEnabled(false);
				} else {
					menuItemMapLocation.setEnabled(false);
				}
				//设置删除键是否可用，当选中最后一行时，不能进行删除操作
				if (tableUniqueInfo.getSelectedRow() == tableUniqueInfo.getRowCount() - 1) {
					menuItemDelete.setEnabled(false);
				} else {
					menuItemDelete.setEnabled(true);
				}
				tablePopupMenuUniqurTheme.show(tableUniqueInfo, e.getX(), e.getY());
			}
			// 包含最后一行不能做删除操作
			int[] selectRows = tableUniqueInfo.getSelectedRows();
			if (selectRows[selectRows.length - 1] == tableUniqueInfo.getRowCount() - 1) {
				buttonDelete.setEnabled(false);
			} else {
				buttonDelete.setEnabled(true);
			}
		}

		/**
		 * @param e
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			//满足鼠标拖拽，也可以实现多选效果
			if (e.getSource() == tableUniqueInfo && 1 == e.getClickCount() && e.getButton() == MouseEvent.BUTTON1) {
				//此时进行专题图子项连续定位
				ContinuousMapLocation();
			}
		}

		/**
		 * 进行专题图子项连续定位  12.23yuanR
		 */
		private void ContinuousMapLocation() {
			if (isContinuousMapLocation) {
				int[] selectRow = tableUniqueInfo.getSelectedRows();
				//此时选中了最后一行
				if (selectRow[tableUniqueInfo.getSelectedRowCount() - 1] != tableUniqueInfo.getRowCount() - 1) {
					MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE);
					Recordset selectedRecordsets;
					for (int i = 0; i < tableUniqueInfo.getSelectedRowCount(); i++) {
						ThemeUniqueItem item = themeUnique.getItem(selectRow[i]);
						//判断子项值为数字还是字符
						if (StringUtilities.isNumber(item.getUnique())) {//为数字
							Double itemUnique = StringUtilities.getNumber(item.getUnique());
							selectedRecordsets = datasetVector.query("Abs(" + expression + "-" + itemUnique + ")<" + 0.00001, STATIC);
						} else {//不为数字
							selectedRecordsets = datasetVector.query(expression + " = " + "'" + item.getUnique() + "'", STATIC);
						}
						if (selectedRecordsets.getRecordCount() != 0) {
							//设置选中子项跟踪层风格
							GeoStyle selectedGeoStyle = new GeoStyle();
							GeoStyle3D selectedGeoStyle3D = new GeoStyle3D();
							Point2Ds points = new Point2Ds();
							selectedRecordsets.moveFirst();
							for (int n = 0; n < selectedRecordsets.getRecordCount(); n++) {
								Geometry selectedGeo = selectedRecordsets.getGeometry();
								//设置geometry的显示
								if (selectedGeo instanceof Geometry3D) {
									if (!(selectedGeo instanceof GeoText3D)) {
										if (selectedGeo instanceof GeoRegion3D) {
											//当为三维面数据时，设置填充颜色为透明，显示原本风格
											selectedGeoStyle.setFillOpaqueRate(0);
										}
										selectedGeoStyle3D.setLineWidth(0.5);
										selectedGeoStyle3D.setLineColor(Color.red);
										((Geometry3D) selectedGeo).setStyle3D(selectedGeoStyle3D);
									}
								} else {
									if (!(selectedGeo instanceof GeoText3D)) {
										if (selectedGeo instanceof GeoRegion) {
											//当为面数据时，设置填充颜色为透明，显示原本风格
											selectedGeoStyle.setFillOpaqueRate(0);
										}
										selectedGeoStyle.setLineWidth(0.5);
										selectedGeoStyle.setLineColor(Color.red);
										selectedGeo.setStyle(selectedGeoStyle);
									}
								}
								uniqurThemetrackingLayer.add(selectedGeo, TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE);

								points.add(selectedGeo.getBounds().leftBottom);
								points.add(selectedGeo.getBounds().rightTop);
								//对象释放
								selectedGeo.dispose();
								selectedRecordsets.moveNext();
							}
							//如果构建的最小矩形没有完全包含于map的矩形，移动其到map中心
							if (getMInRectangle2D(points) != null && !map.getViewBounds().contains(getMInRectangle2D(points))) {
								map.setCenter(getMInRectangle2D(points).getCenter());
							}
							//对象释放
							if (selectedGeoStyle != null) {
								selectedGeoStyle.dispose();
							}
							if (selectedGeoStyle3D != null) {
								selectedGeoStyle3D.dispose();
							}
						} else {//未找到子项，弹出提示信息
							Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_NullQuery"));
						}
						map.refresh();
						//对象释放
						selectedRecordsets.dispose();
					}
				} else {
					//点击了最后一行,仅清除自己在跟踪层中的绘制
					MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE);
					map.refresh();
				}
			}
		}

		/**
		 * 获得点集的最小外接矩形
		 * yuanR 2016.12.20
		 */
		private Rectangle2D getMInRectangle2D(Point2Ds point2Ds) {
			if (point2Ds.getCount() > 0) {
				Double maxX = point2Ds.getItem(0).getX();
				Double maxY = point2Ds.getItem(0).getY();
				Double minX = point2Ds.getItem(0).getX();
				Double minY = point2Ds.getItem(0).getY();
				if (point2Ds.getCount() > 1) {
					for (int i = 1; i < point2Ds.getCount(); i++) {
						if (point2Ds.getItem(i).getX() > maxX) {
							maxX = point2Ds.getItem(i).getX();
						}
						if (point2Ds.getItem(i).getY() > maxY) {
							maxY = point2Ds.getItem(i).getY();
						}
						if (point2Ds.getItem(i).getX() < minX) {
							minX = point2Ds.getItem(i).getX();
						}
						if (point2Ds.getItem(i).getY() < minY) {
							minY = point2Ds.getItem(i).getY();
						}
					}
				}
				Point2D rightTopPoint = new Point2D(maxX, maxY);
				Point2D leftBottomPoint = new Point2D(minX, minY);
				Rectangle2D rectangle2D = new Rectangle2D(leftBottomPoint, rightTopPoint);
				return rectangle2D;
			} else {
				return null;
			}
		}
	}


	/**
	 * 更改删除键状态
	 */
	private void updateButtonDeleteState() {
		int selectRow = tableUniqueInfo.getSelectedRow();
		if (selectRow == tableUniqueInfo.getRowCount() - 1) {
			buttonDelete.setEnabled(false);
		} else {
			buttonDelete.setEnabled(true);
		}
	}

	class LocalKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == tableUniqueInfo && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
				updateButtonDeleteState();
				//当通过键盘改变jtable行选时，同步实现定位功能
				new LocalTableMouseListener().ContinuousMapLocation();
			}

			if (e.getSource() == tableUniqueInfo && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				//当按下esc键，清除跟踪层
				MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE);
				map.refresh();
			}
			if (e.getSource() == nowMapControl && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				//当焦点在mapContorl上时，按esc键清除跟踪层
				MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE);
				map.refresh();
			}

		}
	}

	class LocalFocusLostListener extends FocusAdapter {

		@Override
		public void focusLost(FocusEvent e) {
			// 修改单值项的单值
			refreshAtOnce();
		}
	}

	/**
	 * 判断单值项是否已经存在
	 *
	 * @param uniqueValue
	 * @return
	 */
	private boolean hasUnique(String uniqueValue) {
		boolean itemHasExist = false;
		for (int i = 0; i < this.themeUnique.getCount(); i++) {
			if (this.themeUnique.getItem(i).getUnique().equals(uniqueValue)) {
				itemHasExist = true;
			}
		}
		return itemHasExist;
	}

	/**
	 * 颜色方案改变时刷新颜色
	 */
	private void refreshColor() {
		if (comboboxColor != null) {
			Colors colors = comboboxColor.getSelectedItem();

			Color[] colors1 = new Color[colors.getCount()];
			for (int i = 0; i < colors.getCount(); i++) {
				colors1[i] = colors.get(i);
			}
			int rangeCount = themeUnique.getCount();

			colors = Colors.makeGradient(rangeCount, colors1);
			if (rangeCount > 0) {
				for (int i = 0; i < rangeCount; i++) {
					setGeoStyleColor(themeUnique.getItem(i).getStyle(), colors.get(i));
				}
			}
		}

	}

	/**
	 * 根据当前数据集类型设置颜色方案
	 *
	 * @param geoStyle 需要设置的风格
	 * @param color    设置的颜色
	 */
	private void setGeoStyleColor(GeoStyle geoStyle, Color color) {
		DatasetType datasetType = datasetVector.getType();
		if (CommonToolkit.DatasetTypeWrap.isPoint(datasetType) || CommonToolkit.DatasetTypeWrap.isLine(datasetType)) {
			geoStyle.setLineColor(color);
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(datasetType)) {
			geoStyle.setFillForeColor(color);
		}
	}

	class LocalPopmenuListener implements PopupMenuListener {

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			getTable();
			refreshAtOnce();
			tableUniqueInfo.setRowSelectionInterval(0, 0);
			if (tableUniqueInfo.getRowCount() > 1) {
				buttonDelete.setEnabled(true);
			} else {
				buttonDelete.setEnabled(false);
			}
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			// Do something

		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			// Do something

		}

	}

	/**
	 * 下拉项发生变化时的事件处理类
	 *
	 * @author Administrator
	 */
	class LocalComboBoxItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Dataset[] datasets = ThemeUtil.getDatasets(themeUniqueLayer, datasetVector);
				if (e.getSource() == comboBoxExpression) {
					// sql表达式
					String tempExpression = themeUnique.getUniqueExpression();
					if (!comboBoxArray.contains(tempExpression)) {
						tempExpression = tempExpression.substring(tempExpression.indexOf(".") + 1, tempExpression.length());
					}
					boolean itemHasChanged = ThemeUtil.getSqlExpression(comboBoxExpression, datasets, comboBoxArray, tempExpression, false);
					// 修改表达式
					if (itemHasChanged) {
						// 如果sql表达式中修改了选项
						setFieldInfo();
						tableUniqueInfo.setRowSelectionInterval(0, 0);
					}
				} else if (e.getSource() == comboBoxOffsetX) {
					String offsetXExpression = themeUnique.getOffsetX();
					if (StringUtilities.isNullOrEmpty(offsetXExpression)) {
						offsetXExpression = "0";
					}
					boolean itemChangedForOffsetX = ThemeUtil.getSqlExpression(comboBoxOffsetX, datasets, comboBoxArrayForOffsetX, offsetXExpression, true);
					if (itemChangedForOffsetX) {
						// 修改水平偏移量
						setOffsetX();
					}
				} else if (e.getSource() == comboBoxOffsetY) {
					String offsetYExpression = themeUnique.getOffsetY();
					if (StringUtilities.isNullOrEmpty(offsetYExpression)) {
						offsetYExpression = "0";
					}
					boolean itemChangedForOffsetY = ThemeUtil.getSqlExpression(comboBoxOffsetY, datasets, comboBoxArrayForOffsetY, offsetYExpression, true);
					if (itemChangedForOffsetY) {
						// 修改垂直偏移量
						setOffsetY();
					}
				} else if (e.getSource() == comboboxColor) {
					// 修改颜色方案
					int[] selectRows = null;
					if (tableUniqueInfo.getSelectedRows().length > 0) {
						selectRows = tableUniqueInfo.getSelectedRows();
					}
					refreshColor();
					getTable();
					if (null != selectRows) {
						tableUniqueInfo.setRowSelectionInterval(selectRows[0], selectRows[selectRows.length - 1]);
					} else {
						tableUniqueInfo.setRowSelectionInterval(0, 0);
					}
				} else if (e.getSource() == comboBoxOffsetUnity) {
					// 修改偏移量单位
					setOffsetUnity();
				}
				refreshAtOnce();
			}
		}

		/**
		 * 字段表达式
		 */
		private void setFieldInfo() {
			expression = comboBoxExpression.getSelectedItem().toString();
			if (expression.isEmpty()) {
				comboBoxExpression.setSelectedIndex(0);
			}
			if (ThemeUtil.isCountBeyond(datasetVector, expression)) {
				// 字段记录数大于3000条时建议不做专题图
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_ThemeGridUnique_MessageBoxInfo"));
				resetThemeItem();
			} else {
				ThemeUnique theme = ThemeUnique.makeDefault(datasetVector, expression, ColorGradientType.GREENORANGEVIOLET, themeUniqueLayer.getDisplayFilter()
						.getJoinItems());

				if (null != theme) {
					boolean isOffsetFixed = themeUnique.isOffsetFixed();
					String offsetX = themeUnique.getOffsetX();
					String offsetY = themeUnique.getOffsetY();
					themeUnique = new ThemeUnique(theme);
					themeUnique.setOffsetFixed(isOffsetFixed);
					themeUnique.setOffsetX(offsetX);
					themeUnique.setOffsetY(offsetY);
					for (int i = 0; i < themeUnique.getCount(); i++) {
						GeoStyle textStyle = themeUnique.getItem(i).getStyle();
						textStyle.setLineColor(Color.GRAY);
					}
					refreshColor();
					getTable();
				} else {
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
					resetThemeItem();

				}
			}
		}

		private void resetThemeItem() {
			String tempExpression = themeUnique.getUniqueExpression();
			if (comboBoxArray.contains(tempExpression)) {
				comboBoxExpression.setSelectedItem(tempExpression);
			} else {
				comboBoxExpression.setSelectedItem(tempExpression.substring(tempExpression.indexOf(".") + 1, tempExpression.length()));
			}
		}

		/**
		 * 修改水平偏移量
		 */
		private void setOffsetX() {
			String expressionOffsetX = comboBoxOffsetX.getSelectedItem().toString();
			themeUnique.setOffsetX(expressionOffsetX);
		}

		/**
		 * 修改垂直偏移量
		 */
		private void setOffsetY() {
			String expressionOffsetY = comboBoxOffsetY.getSelectedItem().toString();
			themeUnique.setOffsetY(expressionOffsetY);
		}

		private void setOffsetUnity() {
			if (MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit").equals(comboBoxOffsetUnity.getSelectedItem().toString())) {
				themeUnique.setOffsetFixed(true);
				labelOffsetXUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				labelOffsetYUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
			} else {
				themeUnique.setOffsetFixed(false);
				labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
				labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
			}
		}
	}

	class LocalTableModelListener implements TableModelListener {
		@Override
		public void tableChanged(TableModelEvent e) {
			try {
				int selectColumn = e.getColumn();
				int selectRow = e.getFirstRow();
				if (selectColumn == TABLE_COLUMN_UNIQUE && null != tableUniqueInfo.getValueAt(selectRow, selectColumn)
						&& !hasUnique(tableUniqueInfo.getValueAt(selectRow, selectColumn).toString())) {
					String uniqueValue = tableUniqueInfo.getValueAt(selectRow, selectColumn).toString();
					setUniqueItemUnique(uniqueValue);
					setUniqueItemCaption(uniqueValue);
				}
				if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilities.isNullOrEmptyString(tableUniqueInfo.getValueAt(selectRow, selectColumn))) {
					String caption = tableUniqueInfo.getValueAt(selectRow, selectColumn).toString();
					setUniqueItemCaption(caption);
				}
				getTable();
				tableUniqueInfo.addRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			} catch (Exception ex) {
				getActiveApplication().getOutput().output(ex);
			}
		}

	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

//	class LayerPropertyChangeListener implements PropertyChangeListener {
//
//		@Override
//		public void propertyChange(PropertyChangeEvent e) {
//			if (null != themeUniqueLayer && !themeUniqueLayer.isDisposed() && ((Layer) e.getNewValue()).equals(themeUniqueLayer)) {
//				isResetLayerProperty = true;
//				initComboBoxExpression();
//				initComboBoxOffsetX();
//				initComboBoxOffsetY();
//			}
//		}
//
//	}

	class LocalActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectRows = null;
			if (tableUniqueInfo.getSelectedRows().length > 0) {
				selectRows = tableUniqueInfo.getSelectedRows();
			}
			if (e.getSource() == buttonVisble) {
				// 批量修改单值段的可见状态
				setItemVisble();
			} else if (e.getSource() == buttonGeoStyle) {
				// 批量修改单值段的符号方案
				setItemGeoSytle();
			} else if (e.getSource() == buttonAdd) {
				// 添加单值段
				addThemeItem();
			} else if (e.getSource() == buttonDelete) {
				// 删除单值段
				deleteItem();
			} else if (e.getSource() == buttonAntitone) {
				// 颜色方案反序
				setGeoStyleAntitone();
			} else if (e.getSource() == buttonContinuousMapLocation) {
				//图层是否更随点选联动
				setContinuousMapLocation();
			} else if (e.getSource() == menuItemMapLocation) {
				//进行地图定位
				mapLocation();
			} else if (e.getSource() == menuItemReviseStyle) {
				//进行风格修改
				int selectRow = tableUniqueInfo.getSelectedRow();
				setItemGeoSytle();
				tableUniqueInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			} else if (e.getSource() == menuItemDelete) {
				//进行子项删除
				deleteItem();
			}
			refreshAtOnce();
			if (null != selectRows && e.getSource() != buttonDelete) {
				for (int i = 0; i < selectRows.length; i++) {
					tableUniqueInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
				return;
			}
		}

		/**
		 * 子项高亮显示并定位 2016.12.12
		 * yuanR
		 * 之后实现
		 */
		private void mapLocation() {
			/*
			if (themeUniqueLayer.getSelection().getCount() > 0) {
				themeUniqueLayer.getSelection().clear();
			}
			String expression = comboBoxExpression.getSelectedItem().toString();
			if (tableUniqueInfo.getSelectedRow() != tableUniqueInfo.getRowCount() - 1) {
				//获得选中的行,需要区分多选和单选
				if (tableUniqueInfo.getSelectedRowCount() > 1) {
					//此时为多选
					themeUniqueLayer.getSelection().addRange(tableUniqueInfo.getSelectedRows());
				} else {
					themeUniqueLayer.getSelection().add(tableUniqueInfo.getSelectedRow());
				}

			} else {//点击了最后一行，全部选择
				//Recordset recordsets = datasetVector.getRecordset(false, STATIC);
			}
			//清空之前的选择集

			//themeUniqueLayer.getSelection().addRange(tableUniqueInfo.getSelectedRows());
			//themeUniqueLayer.getSelection().fromRecordset(recordsets);
			//Selection s=new Selection();
			//s=	themeUniqueLayer.getSelection();
			themeUniqueLayer.getSelection().setDefaultStyleEnabled(true);
			themeUniqueLayer.setSelection(themeUniqueLayer.getSelection());
			MapUtilities.getMapControl().revalidate();
			map.refresh();

			//重设map中心点
			Recordset recordsets;
			int selectRow = tableUniqueInfo.getSelectedRow();
			ThemeUniqueItem item = themeUnique.getItem(selectRow);
			//构造选中行的记录集
			recordsets = datasetVector.query(expression + "=" + "'" + item.getUnique() + "'", STATIC);
			Geometry geo = recordsets.getGeometry();
			if (null != geo) {
				Rectangle2D rectangle2d = null;
				Point2Ds points = new Point2Ds(new Point2D[]{new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
						new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop())});
				rectangle2d = new Rectangle2D(points.getItem(0), points.getItem(1));
				map.setCenter(rectangle2d.getCenter());
			}
			//随用随释放
			recordsets.dispose();
			*/
		}

		/**
		 * 添加单值段
		 */
		private void addThemeItem() {
			addItemPanel = new AddItemPanel(ThemeType.UNIQUE);
			addItemPanel.setJoinItems(themeUniqueLayer.getDisplayFilter().getJoinItems());
			addItemPanel.setDataset(datasetVector);
			addItemPanel.setThemeUnique(themeUnique);
			addItemPanel.setDeleteUniqueItems(deleteItems);
			addItemPanel.init();
			addItemPanel.addPopupMenuListener(popmenuListener);
			addItemPanel.show(buttonAdd, -addItemPanel.getWidth() / 2, buttonAdd.getHeight());
			addItemPanel.setVisible(true);
		}

		/**
		 * 判断单值项是否需要添加到未添加项中
		 *
		 * @param deleteItem
		 * @return
		 */
		private boolean isNeedAddToDeleteItems(ThemeUniqueItem deleteItem) {
			String deleteItemUnique = deleteItem.getUnique();
			ThemeUnique themeUniqueTemp = ThemeUnique.makeDefault(datasetVector, themeUnique.getUniqueExpression(), ColorGradientType.YELLOWGREEN,
					themeUniqueLayer.getDisplayFilter().getJoinItems());
			for (int i = 0; i < themeUniqueTemp.getCount(); i++) {
				if (themeUniqueTemp.getItem(i).getUnique().equals(deleteItemUnique)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 删除单值段
		 */
		private void deleteItem() {
			int[] selectedRow = tableUniqueInfo.getSelectedRows();
			int uniqueCount = themeUnique.getCount();
			if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1 && selectedRow.length == 1) {
				ThemeUniqueItem item = themeUnique.getItem(selectedRow[0]);

				if (isNeedAddToDeleteItems(item)) {
					ThemeUniqueItem itemClone = new ThemeUniqueItem(item);
					deleteItems.add(itemClone);
				}

				themeUnique.remove(selectedRow[0]);

			} else if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1 && uniqueCount > 0) {
				for (int i = selectedRow.length - 1; i >= 0; i--) {
					ThemeUniqueItem item = themeUnique.getItem(selectedRow[i]);
					if (isNeedAddToDeleteItems(item)) {
						ThemeUniqueItem itemClone = new ThemeUniqueItem(item);
						deleteItems.add(itemClone);
					}
					themeUnique.remove(selectedRow[i]);
				}
			} else if (selectedRow[selectedRow.length - 1] == tableUniqueInfo.getRowCount() - 1) {
				for (int i = selectedRow.length - 2; i >= 0; i--) {
					ThemeUniqueItem item = themeUnique.getItem(selectedRow[i]);
					if (isNeedAddToDeleteItems(item)) {
						ThemeUniqueItem itemClone = new ThemeUniqueItem(item);
						deleteItems.add(itemClone);
					}
					themeUnique.remove(selectedRow[i]);
				}
			}
			getTable();
			if (tableUniqueInfo.getRowCount() == 1) {
				buttonDelete.setEnabled(false);
			} else {
				buttonDelete.setEnabled(true);
			}
			if (selectedRow[0] != tableUniqueInfo.getRowCount() - 1) {
				tableUniqueInfo.setRowSelectionInterval(selectedRow[0], selectedRow[0]);
				return;
			}
			if (selectedRow[0] == tableUniqueInfo.getRowCount() - 1 && selectedRow[0] > 0) {
				tableUniqueInfo.setRowSelectionInterval(selectedRow[0] - 1, selectedRow[0] - 1);
				return;
			}
		}

		/**
		 * 设置颜色方案与当前颜色方案反序
		 */
		private void setGeoStyleAntitone() {
			themeUnique.reverseStyle();
			getTable();
		}

		/**
		 * 设置单值项是否可见
		 */
		private void setItemVisble() {
			int[] selectedRow = tableUniqueInfo.getSelectedRows();
			// 有不可见的项就全部设置为不可见，全部不可见，或者全部可见就设置为相反状态
			if (hasInvisible(selectedRow) && !allItemInvisible(selectedRow)) {
				if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1) {
					for (int i = 0; i < selectedRow.length; i++) {
						themeUnique.getItem(selectedRow[i]).setVisible(false);
					}
				} else {
					for (int i = 0; i < selectedRow.length - 1; i++) {
						themeUnique.getItem(selectedRow[i]).setVisible(false);
					}
					themeUnique.setDefaultStyleVisible(false);
				}
			} else {
				for (int i = 0; i < selectedRow.length; i++) {
					resetVisible(selectedRow[i]);
				}
			}
			getTable();
		}

		/**
		 * 判断选中项是否全部不可见
		 *
		 * @param selectedRows
		 * @return
		 */
		private boolean allItemInvisible(int[] selectedRows) {
			int count = 0;
			boolean allItemInvisible = false;
			if (selectedRows[selectedRows.length - 1] != tableUniqueInfo.getRowCount() - 1) {
				for (int i = 0; i < selectedRows.length; i++) {
					if (!themeUnique.getItem(selectedRows[i]).isVisible()) {
						count++;
					}
				}
			} else {
				for (int i = 0; i < selectedRows.length - 1; i++) {
					if (!themeUnique.getItem(selectedRows[i]).isVisible()) {
						count++;
					}
				}
				if (!themeUnique.isDefaultStyleVisible()) {
					count++;
				}
			}
			if (count == selectedRows.length) {
				allItemInvisible = true;
			}
			return allItemInvisible;
		}

		/**
		 * 判断选中项中是否存在不可见子项
		 *
		 * @param selectedRows
		 * @return
		 */
		private boolean hasInvisible(int[] selectedRows) {
			boolean hasInvisible = false;
			if (selectedRows[selectedRows.length - 1] != tableUniqueInfo.getRowCount() - 1) {
				for (int i = 0; i < selectedRows.length; i++) {
					if (!themeUnique.getItem(selectedRows[i]).isVisible()) {
						hasInvisible = true;
					}
				}
			} else {
				if (!themeUnique.isDefaultStyleVisible()) {
					hasInvisible = true;
				}
			}
			return hasInvisible;
		}

		/**
		 * 重置可见选项
		 *
		 * @param selectRow 要重置的行
		 */
		private void resetVisible(int selectRow) {
			if (selectRow != tableUniqueInfo.getRowCount() - 1) {
				boolean visible = themeUnique.getItem(selectRow).isVisible();
				if (visible) {
					themeUnique.getItem(selectRow).setVisible(false);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					themeUnique.getItem(selectRow).setVisible(true);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
			} else {
				boolean visible = themeUnique.isDefaultStyleVisible();
				if (visible) {
					themeUnique.setDefaultStyleVisible(false);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					themeUnique.setDefaultStyleVisible(true);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
			}
		}

	}

	/**
	 * 设置是否开启子项连续定位
	 */
	private void setContinuousMapLocation() {
		if (isContinuousMapLocation) {
			this.buttonContinuousMapLocation.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_OpenLinkageLayer.png"));
			this.isContinuousMapLocation = false;
			//当关闭连续定位功能时，清空跟踪层
			MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION_THEMEUNIQUE);
			this.map.refresh();
		} else {
			this.buttonContinuousMapLocation.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_CloseLinkageLayer.png"));
			this.isContinuousMapLocation = true;
			//当开启连续点选联动时，进行一次定位操作
			new LocalTableMouseListener().ContinuousMapLocation();
		}
	}

	/**
	 * 设置文本风格
	 */
	private void setItemGeoSytle() {
		SymbolType symbolType = null;
		if (CommonToolkit.DatasetTypeWrap.isPoint(datasetVector.getType())) {
			symbolType = SymbolType.MARKER;
		} else if (CommonToolkit.DatasetTypeWrap.isLine(datasetVector.getType())) {
			symbolType = SymbolType.LINE;
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(datasetVector.getType())) {
			symbolType = SymbolType.FILL;
		}
		final int[] selectedRow = this.tableUniqueInfo.getSelectedRows();
		SymbolDialog textStyleDialog = SymbolDialogFactory.getSymbolDialog(symbolType);
		GeoStyle geoStyle;

		if (selectedRow.length == 1) {
			if (selectedRow[0] != this.tableUniqueInfo.getRowCount() - 1) {
				geoStyle = this.themeUnique.getItem(selectedRow[0]).getStyle();
			} else {
				geoStyle = this.themeUnique.getDefaultStyle();
			}
			DialogResult dialogResult = textStyleDialog.showDialog(geoStyle, new ISymbolApply() {
				@Override
				public void apply(GeoStyle geoStyle) {
					if (selectedRow[0] != tableUniqueInfo.getRowCount() - 1) {
						resetGeoSytle(selectedRow[0], geoStyle);
					} else {
						themeUnique.setDefaultStyle(geoStyle);
					}
				}
			});
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getCurrentGeoStyle();
				if (selectedRow[0] != tableUniqueInfo.getRowCount() - 1) {
					resetGeoSytle(selectedRow[0], nowGeoStyle);
				} else {
					this.themeUnique.setDefaultStyle(nowGeoStyle);
				}
			}
		} else {
			java.util.List<GeoStyle> geoStyleList = new ArrayList<>();
			GeoStyle style;
			for (int i = 0; i < selectedRow.length; i++) {
				if (selectedRow[i] != this.tableUniqueInfo.getRowCount() - 1) {
					style = themeUnique.getItem(selectedRow[i]).getStyle();
				} else {
					style = themeUnique.getDefaultStyle();
				}
				geoStyleList.add(style);
			}
			JDialogSymbolsChange jDialogSymbolsChange = new JDialogSymbolsChange(symbolType, geoStyleList);
			jDialogSymbolsChange.showDialog();
		}
		getTable();
	}

	/**
	 * 重置文本风格
	 *
	 * @param selectRow   要重置文本风格的行
	 * @param nowGeoStyle 新的文本风格
	 */
	private void resetGeoSytle(int selectRow, GeoStyle nowGeoStyle) {
		ThemeUniqueItem item = this.themeUnique.getItem(selectRow);
		item.setStyle(nowGeoStyle);
		ImageIcon nowGeoStyleIcon = ThemeItemLabelDecorator.buildGeoStyleIcon(this.datasetVector, nowGeoStyle);
		this.tableUniqueInfo.setValueAt(nowGeoStyleIcon, selectRow, TABLE_COLUMN_GEOSTYLE);
	}

	/**
	 * 获取是否及时刷新
	 *
	 * @return
	 */
	public boolean isRfreshAtOnece() {
		return isRefreshAtOnce;
	}

	/**
	 * 设置及时刷新
	 *
	 * @param isRfreshAtOnece
	 */
	public void setRfreshAtOnece(boolean isRfreshAtOnece) {
		this.isRefreshAtOnce = isRfreshAtOnece;
	}

	/**
	 * 获取当前的单值专题图图层
	 *
	 * @return
	 */
	public Layer getThemeUniqueLayer() {
		return themeUniqueLayer;
	}

	/**
	 * 设置当前的单值专题图图层
	 *
	 * @param themeUniqueLayer
	 */
	public void setThemeUniqueLayer(Layer themeUniqueLayer) {
		this.themeUniqueLayer = themeUniqueLayer;
	}

	public void refreshMapAndLayer() {
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeUniqueLayer = MapUtilities.findLayerByName(map, layerName);
		if (null != themeUniqueLayer && null != themeUniqueLayer.getTheme() && themeUniqueLayer.getTheme().getType() == ThemeType.UNIQUE) {
			ThemeUnique nowThemeUnique = ((ThemeUnique) this.themeUniqueLayer.getTheme());
			nowThemeUnique.fromXML(themeUnique.toXML());
			UICommonToolkit.getLayersManager().getLayersTree().refreshNode(this.themeUniqueLayer);
			//增加刷新清除选择
			/*
			if (nowSelection.getCount() > 0) {
				this.nowSelection.clear();
				this.themeUniqueLayer.setSelection(nowSelection);
			}
			*/
			this.map.refresh();
		}
	}

	// 获取当前的专题图
	@Override
	public Theme getCurrentTheme() {
		return themeUnique;
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	public Layer getCurrentLayer() {
		return themeUniqueLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeUniqueLayer = layer;
	}
}
