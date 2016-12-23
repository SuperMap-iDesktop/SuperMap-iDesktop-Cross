package com.supermap.desktop.newtheme.themeRange;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.data.CursorType;
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
import com.supermap.data.QueryParameter;
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
import com.supermap.desktop.utilities.MathUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.RangeMode;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeRangeItem;
import com.supermap.mapping.ThemeType;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

import static com.supermap.desktop.newtheme.themeUnique.ThemeUniqueContainer.TAG_CONTINUOUSMAPLOCATION;

/**
 * 分段专题图
 *
 * @author xie
 */
public class ThemeRangeContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private static final int TABLE_COLUMN_VISIBLE = 0;
	private static final int TABLE_COLUMN_GEOSTYLE = 1;
	private static final int TABLE_COLUMN_RANGEVALUE = 2;
	private static final int TABLE_COLUMN_CAPTION = 3;

	private JTabbedPane tabbedPaneInfo = new JTabbedPane();
	private JPanel panelProperty = new JPanel();
	private JPanel panelAdvance = new JPanel();
	private JPanel panelOffsetSet = new JPanel();
	private JLabel labelExpression = new JLabel();
	private JComboBox<String> comboBoxExpression = new JComboBox<String>();
	private JLabel labelRangeMethod = new JLabel();
	private JComboBox<String> comboBoxRangeMethod = new JComboBox<String>();
	private JLabel labelRangeCount = new JLabel();
	private JComboBox<String> comboBoxRangeCount = new JComboBox<String>();
	private JLabel labelRangeLength = new JLabel();
	private JSpinner spinnerRangeLength = new JSpinner();
	private JLabel labelRangePrecision = new JLabel();
	private JComboBox<String> comboBoxRangePrecision = new JComboBox<String>();
	private JLabel labelRangeFormat = new JLabel();
	private JComboBox<String> comboBoxRangeFormat = new JComboBox<String>();
	private JLabel labelColorStyle = new JLabel();
	private ColorsComboBox comboBoxColorStyle = new ColorsComboBox(ThemeType.RANGE);
	private JToolBar toolBar = new JToolBar();
	private JButton buttonMerge = new JButton();
	private JButton buttonSplit = new JButton();
	private JButton buttonVisible = new JButton();
	private JButton buttonStyle = new JButton();
	//增加关联浏览按钮开关
	private JButton buttonContinuousMapLocation = new JButton();
	//是否进行连续浏览
	private boolean isContinuousMapLocation = false;
	//增加子项右键菜单
	private JPopupMenu tablePopupMenuRangeTheme = new JPopupMenu();
	//修改风格
	private JMenuItem menuItemReviseStyle = new JMenuItem();
	//编辑定位
	private JMenuItem menuItemMapLocation = new JMenuItem();
	//跟踪层
	private TrackingLayer rangeThemeTrackingLayer;

	private JScrollPane scrollPane = new JScrollPane();
	private JTable tableRangeInfo = new JTable();
	private JLabel labelOffsetUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetUnity = new JComboBox<String>();
	private JLabel labelOffsetX = new JLabel();
	private JLabel labelOffsetXUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetX = new JComboBox<String>();
	private JLabel labelOffsetY = new JLabel();
	private JLabel labelOffsetYUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetY = new JComboBox<String>();

	private static String[] nameStrings = {MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_Sytle"),
			MapViewProperties.getString("String_Title_RangeValue"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption")};
	private transient DatasetVector datasetVector;
	private transient Map map;
	private transient ThemeRange themeRange;
	private transient Layer themeRangeLayer;
	private String rangeExpression;
	private transient RangeMode rangeMode = RangeMode.EQUALINTERVAL;
	private transient int rangeCount = 5;
	private String captiontype = "";
	private boolean isRefreshAtOnce = true;
	private boolean isCustom = false;
	private double precision;
	private boolean isMergeOrSplit = false;
	private boolean isResetComboBox = false;
	private boolean isNewTheme = false;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private String layerName;
	private ArrayList<String> comboBoxArray = new ArrayList<String>();

	//private Selection nowSelection = new Selection();
	private IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
	private MapControl nowMapControl = formMap.getMapControl();

	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalMouseListener mouseListener = new LocalMouseListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalComboBoxItemListener itemListener = new LocalComboBoxItemListener();
	private transient LocalSpinnerChangeListener changeListener = new LocalSpinnerChangeListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();
	private PropertyChangeListener layersTreePropertyChangeListener = new LayerChangeListener();
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			initComboBoxRangeExpression();
		}
	};

	public ThemeRangeContainer(Layer layer, boolean isNewTheme) {
		this.themeRangeLayer = layer;
		this.isNewTheme = isNewTheme;
		this.layerName = this.themeRangeLayer.getName();
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeRange = new ThemeRange((ThemeRange) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		//获得跟踪层
		this.rangeThemeTrackingLayer = map.getTrackingLayer();
		this.precision = themeRange.getPrecision();
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
		// 暂时屏蔽掉高级选项界面
		// this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Advanced"),
		// this.panelAdvance);
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		//右键菜单布局
		this.tablePopupMenuRangeTheme.add(this.menuItemReviseStyle);
		//暂时移除定位菜单
		//this.tablePopupMenuRangeTheme.add(this.menuItemMapLocation);

		initPanelProperty();
		initPanelAdvance();
	}

	/**
	 * 初始化属性界面
	 */
	private void initPanelProperty() {
		//@formatter:off
        initToolBar();
        initComboBoxRangeExpression();
        initComboBoxRangMethod();
        initComboBoxRangeCount();
        initComboBoxRangePrecision();
        initComboBoxRangeFormat();
        this.panelProperty.setLayout(new GridBagLayout());
        this.panelProperty.add(this.labelExpression, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.panelProperty.add(this.comboBoxExpression, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.labelRangeMethod, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.panelProperty.add(this.comboBoxRangeMethod, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.labelRangeCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.panelProperty.add(this.comboBoxRangeCount, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.labelRangeLength, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.spinnerRangeLength.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
        this.spinnerRangeLength.setEnabled(false);
        this.panelProperty.add(this.spinnerRangeLength, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.labelRangePrecision, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.panelProperty.add(this.comboBoxRangePrecision, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.labelRangeFormat, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.panelProperty.add(this.comboBoxRangeFormat, new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.labelColorStyle, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
        this.panelProperty.add(this.comboBoxColorStyle, new GridBagConstraintsHelper(1, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.panelProperty.add(this.toolBar, new GridBagConstraintsHelper(0, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(100, 0).setIpad(40, 0));
        this.panelProperty.add(this.scrollPane, new GridBagConstraintsHelper(0, 8, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setWeight(100, 3).setFill(GridBagConstraints.BOTH));
        this.scrollPane.setViewportView(this.tableRangeInfo);
        if (isNewTheme) {
            refreshColor();
            refreshAtOnce();
        }
        getTable();
        this.tableRangeInfo.setRowSelectionInterval(0, 0);
        //@formatter:on
	}

	/**
	 * 初始化表达式下拉框
	 */
	private void initComboBoxRangeExpression() {
		ThemeUtil.initComboBox(comboBoxExpression, this.themeRange.getRangeExpression(), datasetVector, this.themeRangeLayer.getDisplayFilter().getJoinItems(),
				comboBoxArray, true, false);
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
        this.panelOffsetSet.add(this.labelOffsetUnity, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1));
        this.panelOffsetSet.add(this.comboBoxOffsetUnity, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        this.panelOffsetSet.add(this.labelOffsetX, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1));
        this.panelOffsetSet.add(this.comboBoxOffsetX, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        this.panelOffsetSet.add(this.labelOffsetXUnity, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1));
        this.panelOffsetSet.add(this.labelOffsetY, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1));
        this.panelOffsetSet.add(this.comboBoxOffsetY, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        this.panelOffsetSet.add(this.labelOffsetYUnity, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(1, 1));
        //@formatter:on
	}

	/**
	 * 初始化偏移量单位
	 */
	private void initComboBoxOffsetUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[]{
				MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map")}));
		if (this.themeRange.isOffsetFixed()) {
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

		this.comboBoxOffsetX.addItem("0");
		String offsetX = this.themeRange.getOffsetX();
		if (StringUtilities.isNullOrEmpty(offsetX)) {
			offsetX = "0";
		}
		this.comboBoxOffsetX.setSelectedItem(offsetX);
		if (!offsetX.equals(this.comboBoxOffsetX.getSelectedItem())) {
			this.comboBoxOffsetX.addItem(offsetX);
			this.comboBoxOffsetX.setSelectedItem(offsetX);
		}
	}

	/**
	 * 初始化垂直偏移量
	 */
	private void initComboBoxOffsetY() {
		this.comboBoxOffsetY.addItem("0");
		String offsetY = this.themeRange.getOffsetY();
		if (StringUtilities.isNullOrEmpty(offsetY)) {
			offsetY = "0";
		}
		this.comboBoxOffsetY.setSelectedItem(offsetY);
		if (!offsetY.equals(this.comboBoxOffsetY.getSelectedItem())) {
			this.comboBoxOffsetY.addItem(offsetY);
			this.comboBoxOffsetY.setSelectedItem(offsetY);
		}
	}

	/**
	 * 初始化分段方法项
	 */
	private void initComboBoxRangMethod() {
		this.comboBoxRangeMethod.setModel(new DefaultComboBoxModel<String>(new String[]{MapViewProperties.getString("String_RangeMode_EqualInterval"),
				MapViewProperties.getString("String_RangeMode_SquareRoot"), MapViewProperties.getString("String_RangeMode_StdDeviation"),
				MapViewProperties.getString("String_RangeMode_Logarithm"), MapViewProperties.getString("String_RangeMode_Quantile"),
				MapViewProperties.getString("String_RangeMode_CustomInterval")}));
		if (this.themeRange.getRangeMode() == RangeMode.NONE) {
			this.comboBoxRangeMethod.setSelectedIndex(0);
		} else if (themeRange.getRangeMode() == RangeMode.SQUAREROOT) {
			this.comboBoxRangeMethod.setSelectedIndex(1);
		} else if (this.themeRange.getRangeMode() == RangeMode.STDDEVIATION) {
			this.comboBoxRangeMethod.setSelectedIndex(2);
		} else if (this.themeRange.getRangeMode() == RangeMode.LOGARITHM) {
			this.comboBoxRangeMethod.setSelectedIndex(3);
		} else if (this.themeRange.getRangeMode() == RangeMode.QUANTILE) {
			this.comboBoxRangeMethod.setSelectedIndex(4);
		} else if (this.themeRange.getRangeMode() == RangeMode.CUSTOMINTERVAL) {
			this.comboBoxRangeMethod.setSelectedIndex(5);
		}
	}

	/**
	 * 初始化段数
	 */
	private void initComboBoxRangeCount() {
		this.comboBoxRangeCount.setModel(new DefaultComboBoxModel<String>(new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32"}));
		this.comboBoxRangeCount.setEditable(true);
		this.comboBoxRangeCount.setSelectedItem(String.valueOf(this.themeRange.getCount()));
	}

	/**
	 * 初始化段值精度项
	 */
	private void initComboBoxRangePrecision() {
		this.comboBoxRangePrecision.setModel(new DefaultComboBoxModel<String>(new String[]{"10000000", "1000000", "100000", "10000", "1000", "100", "10",
				"1", "0.1", "0.01", "0.001", "0.0001", "0.00001", "0.000001", "0.0000001"}));
		String numeric = initPrecision(String.valueOf(this.themeRange.getPrecision()));
		this.comboBoxRangePrecision.setSelectedItem(numeric);
	}

	/**
	 * 科学计数法转换
	 *
	 * @param precesion
	 * @return
	 */
	private String initPrecision(String precesion) {
		String resultPrecision = precesion;
		if ("1.0E-4".equals(precesion)) {
			resultPrecision = "0.0001";
		} else if ("1.0E-5".equals(precesion)) {
			resultPrecision = "0.00001";
		} else if ("1.0E-6".equals(precesion)) {
			resultPrecision = "0.000001";
		} else if ("1.0E-7".equals(precesion)) {
			resultPrecision = "0.0000001";
		} else if ("1.0E-8".equals(precesion)) {
			resultPrecision = "0.00000001";
		} else if ("1.0E7".equals(precesion)) {
			resultPrecision = "10000000";
		} else if (precesion.endsWith(".0")) {
			resultPrecision = precesion.substring(0, precesion.indexOf("."));
		}
		return resultPrecision;
	}

	/**
	 * 初始化段标题格式
	 */
	private void initComboBoxRangeFormat() {
		this.comboBoxRangeFormat.setModel(new DefaultComboBoxModel<String>(new String[]{"0-100", "0<=x<100"}));
		if (this.themeRange.getItem(0).getCaption().contains("X")) {
			this.comboBoxRangeFormat.setSelectedIndex(1);
		} else {
			this.comboBoxRangeFormat.setSelectedIndex(0);
		}
	}

	/*
	 * 资源化
	 */
	private void initResources() {
		this.labelExpression.setText(MapViewProperties.getString("String_label_Expression"));
		this.labelRangeMethod.setText(MapViewProperties.getString("String_Label_RangeMethed"));
		this.labelRangeCount.setText(MapViewProperties.getString("String_Label_RangeCount"));
		this.labelRangeLength.setText(MapViewProperties.getString("String_Label_RangeSize"));
		this.labelRangePrecision.setText(MapViewProperties.getString("String_RangePrecision"));
		this.labelRangeFormat.setText(MapViewProperties.getString("String_Label_CaptionFormat"));
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.buttonMerge.setEnabled(false);
		this.buttonMerge.setToolTipText(MapViewProperties.getString("String_Title_Merge"));
		this.buttonSplit.setToolTipText(MapViewProperties.getString("String_Title_Split"));
		this.buttonStyle.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonVisible.setToolTipText(MapViewProperties.getString("String_Title_Visible"));
		//增加连续定位按钮开关
		this.buttonContinuousMapLocation.setToolTipText(MapViewProperties.getString("String_ContinuousMapLocation"));
		//给jtable右键菜单设置资源化
		this.menuItemReviseStyle.setText(MapViewProperties.getString("String_ThemeGraphItemManager_ModifyStyle"));
		this.menuItemMapLocation.setText(MapViewProperties.getString("String_MapLocation"));

		this.labelOffsetUnity.setText(MapViewProperties.getString("String_LabelOffsetUnit"));
		this.labelOffsetX.setText(MapViewProperties.getString("String_LabelOffsetX"));
		this.labelOffsetY.setText(MapViewProperties.getString("String_LabelOffsetY"));
		this.comboBoxOffsetX.setEditable(true);
		this.comboBoxOffsetY.setEditable(true);
		this.panelOffsetSet.setBorder(new TitledBorder(null, MapViewProperties.getString("String_GroupBoxOffset"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
	}

	/**
	 * 表格初始化
	 *
	 * @return m_table
	 */
	private JTable getTable() {
		this.rangeCount = this.themeRange.getCount();
		this.tableRangeInfo.setModel(new LocalDefualTableModel(new Object[this.rangeCount][4], nameStrings));
		initColumnIcon();
		this.tableRangeInfo.setRowHeight(20);
		this.tableRangeInfo.getColumn(MapViewProperties.getString("String_Title_Visible")).setMaxWidth(40);
		this.tableRangeInfo.getColumn(MapViewProperties.getString("String_Title_Sytle")).setMaxWidth(100);
		this.tableRangeInfo.getColumn(MapViewProperties.getString("String_Title_RangeValue")).setMaxWidth(200);
		this.tableRangeInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.tableRangeInfo.getModel().addTableModelListener(this.tableModelListener);
		return this.tableRangeInfo;
	}

	/**
	 * 填充图片和字段
	 */
	private void initColumnIcon() {
		for (int i = 0; i < this.rangeCount; i++) {
			ThemeRangeItem rangeItem = this.themeRange.getItem(i);
			boolean isVisible = rangeItem.isVisible();
			ImageIcon visibleIcon = InternalImageIconFactory.VISIBLE;
			if (!isVisible) {
				visibleIcon = InternalImageIconFactory.INVISIBLE;
			}
			this.tableRangeInfo.setValueAt(visibleIcon, i, TABLE_COLUMN_VISIBLE);
			GeoStyle geoStyle = rangeItem.getStyle();
			this.tableRangeInfo.setValueAt(ThemeItemLabelDecorator.buildGeoStyleIcon(datasetVector, geoStyle), i, TABLE_COLUMN_GEOSTYLE);
			if (i == rangeCount - 1) {
				this.tableRangeInfo.setValueAt("Max", i, TABLE_COLUMN_RANGEVALUE);
			}
			if (i != rangeCount - 1 && Double.compare(precision, 1) == 0) {
				this.tableRangeInfo.setValueAt((int) rangeItem.getEnd(), i, TABLE_COLUMN_RANGEVALUE);
			}
			if (i != rangeCount - 1 && Double.compare(precision, 1) != 0) {
				this.tableRangeInfo.setValueAt(rangeItem.getEnd(), i, TABLE_COLUMN_RANGEVALUE);
			}
			String caption = rangeItem.getCaption();
			if (this.captiontype.contains("-")) {
				caption = caption.replaceAll("<= X <", "-");
				caption = caption.replaceAll("< X <", "-");
			} else if (this.captiontype.contains("<=x<") && !caption.contains(" X <")) {
				caption = caption.replaceAll(" - ", " <= X < ");
			}
			rangeItem.setCaption(caption);
			this.tableRangeInfo.setValueAt(rangeItem.getCaption(), i, TABLE_COLUMN_CAPTION);
		}
	}

	/**
	 * 颜色方案改变时刷新颜色
	 */
	private void refreshColor() {
		if (comboBoxColorStyle != null) {
			Colors colors = comboBoxColorStyle.getSelectedItem();

			Color[] colors1 = new Color[colors.getCount()];
			for (int i = 0; i < colors.getCount(); i++) {
				colors1[i] = colors.get(i);
			}
			int rangeCount = themeRange.getCount();

			colors = Colors.makeGradient(rangeCount, colors1);
			if (rangeCount > 0) {
				for (int i = 0; i < rangeCount; i++) {
					setGeoStyleColor(this.themeRange.getItem(i).getStyle(), colors.get(i));
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
		DatasetType datasetType = this.datasetVector.getType();
		if (CommonToolkit.DatasetTypeWrap.isPoint(datasetType) || CommonToolkit.DatasetTypeWrap.isLine(datasetType)) {
			geoStyle.setLineColor(color);
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(datasetType)) {
			geoStyle.setFillForeColor(color);
		}
	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonMerge);
		this.toolBar.add(this.buttonSplit);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonVisible);
		this.toolBar.add(this.buttonStyle);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonContinuousMapLocation);
		this.buttonMerge.setIcon(InternalImageIconFactory.Merge);
		this.buttonSplit.setIcon(InternalImageIconFactory.Split);
		if (CommonToolkit.DatasetTypeWrap.isRegion(this.datasetVector.getType())) {
			this.buttonStyle.setIcon(InternalImageIconFactory.REGION_STYLE);
		} else if (CommonToolkit.DatasetTypeWrap.isLine(this.datasetVector.getType())) {
			this.buttonStyle.setIcon(InternalImageIconFactory.LINE_STYLE);
		} else {
			this.buttonStyle.setIcon(InternalImageIconFactory.POINT_STYLE);
		}
		this.buttonVisible.setIcon(InternalImageIconFactory.VISIBLE);
		//关联浏览添加图标
		this.buttonContinuousMapLocation.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_OpenLinkageLayer.png"));
	}

	/**
	 * 注册事件
	 */
	public void registActionListener() {
		unregistActionListener();
		this.buttonVisible.addActionListener(this.actionListener);
		this.buttonStyle.addActionListener(this.actionListener);
		this.buttonMerge.addActionListener(this.actionListener);
		this.buttonSplit.addActionListener(this.actionListener);
		//关联浏览按钮开关添加监听
		this.buttonContinuousMapLocation.addActionListener(this.actionListener);
		//增加jtable右键菜单项的监听
		this.menuItemReviseStyle.addActionListener(this.actionListener);
		this.menuItemMapLocation.addActionListener(this.actionListener);

		this.tableRangeInfo.addMouseListener(this.mouseListener);
		//给jtable添加键盘监听
		this.tableRangeInfo.addKeyListener(this.localKeyListener);
		//給mapContorl添加键盘监听
		this.nowMapControl.addKeyListener(this.localKeyListener);

		this.comboBoxColorStyle.addItemListener(this.itemListener);
		this.comboBoxColorStyle.addColorChangedListener();
		this.comboBoxExpression.addItemListener(this.itemListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(mouseAdapter);
		this.comboBoxRangePrecision.addItemListener(this.itemListener);
		this.comboBoxRangeCount.addItemListener(this.itemListener);
		this.comboBoxRangeCount.getComponent(0).addMouseListener(this.mouseListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.addMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.addItemListener(this.itemListener);
		this.comboBoxRangeFormat.addItemListener(this.itemListener);
		this.comboBoxOffsetUnity.addItemListener(this.itemListener);
		this.comboBoxOffsetX.addItemListener(this.itemListener);
		this.comboBoxOffsetY.addItemListener(this.itemListener);
		this.spinnerRangeLength.addChangeListener(this.changeListener);
		this.tableRangeInfo.putClientProperty("terminateEditOnFocusLost", true);
		this.tableRangeInfo.getModel().addTableModelListener(this.tableModelListener);
		this.layersTree.addPropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);

	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.buttonVisible.removeActionListener(this.actionListener);
		this.buttonStyle.removeActionListener(this.actionListener);
		this.buttonMerge.removeActionListener(this.actionListener);
		this.buttonSplit.removeActionListener(this.actionListener);
		//注销关联浏览
		this.buttonContinuousMapLocation.removeActionListener(this.actionListener);
		//注销jtable右键菜单项的监听
		this.menuItemReviseStyle.removeActionListener(this.actionListener);
		this.menuItemMapLocation.removeActionListener(this.actionListener);

		this.tableRangeInfo.removeMouseListener(this.mouseListener);
		//注销table键盘监听
		this.tableRangeInfo.removeKeyListener(this.localKeyListener);
		//mapContorl移除键盘监听
		this.nowMapControl.removeKeyListener(this.localKeyListener);

		this.comboBoxColorStyle.removeItemListener(this.itemListener);
		this.comboBoxColorStyle.removeColorChangedListener();
		this.comboBoxExpression.removeItemListener(this.itemListener);
		this.comboBoxExpression.getComponent(0).removeMouseListener(mouseAdapter);
		this.comboBoxRangePrecision.removeItemListener(this.itemListener);
		this.comboBoxRangeCount.removeItemListener(this.itemListener);
		this.comboBoxRangeCount.getComponent(0).removeMouseListener(this.mouseListener);
		this.comboBoxExpression.getComponent(0).removeMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.removeMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.removeItemListener(this.itemListener);
		this.comboBoxRangeFormat.removeItemListener(this.itemListener);
		this.spinnerRangeLength.removeChangeListener(this.changeListener);
		this.tableRangeInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.layersTree.removePropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
		// this.layersTree.removePropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}

	/**
	 * 设置是否开启子项连续定位
	 */
	private void setContinuousMapLocation() {
		if (isContinuousMapLocation) {
			this.buttonContinuousMapLocation.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_OpenLinkageLayer.png"));
			this.isContinuousMapLocation = false;
			//当关闭连续定位功能时，清空跟踪层
			MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION);
			this.map.refreshTrackingLayer();
		} else {
			this.buttonContinuousMapLocation.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_CloseLinkageLayer.png"));
			this.isContinuousMapLocation = true;
			//当开启连续点选联动时，进行一次定位操作
			new LocalMouseListener().ContinuousMapLocation();
		}
	}

	/**
	 * 批量设置文本风格
	 */
	private void setItemGeoSytle() {
		SymbolType symbolType = null;
		if (CommonToolkit.DatasetTypeWrap.isPoint(this.datasetVector.getType())) {
			symbolType = SymbolType.MARKER;
		} else if (CommonToolkit.DatasetTypeWrap.isLine(this.datasetVector.getType())) {
			symbolType = SymbolType.LINE;
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(this.datasetVector.getType())) {
			symbolType = SymbolType.FILL;
		}

		final int[] selectedRow = this.tableRangeInfo.getSelectedRows();
		SymbolDialog textStyleDialog = SymbolDialogFactory.getSymbolDialog(symbolType);

		if (selectedRow.length == 1) {
			GeoStyle geoStyle = this.themeRange.getItem(selectedRow[0]).getStyle();

			DialogResult dialogResult = textStyleDialog.showDialog(geoStyle, new ISymbolApply() {
				@Override
				public void apply(GeoStyle geoStyle) {
					resetGeoSytle(selectedRow[0], geoStyle);
				}
			});
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getCurrentGeoStyle();
				resetGeoSytle(selectedRow[0], nowGeoStyle);
			}
		} else if (selectedRow.length > 1) {
			java.util.List<GeoStyle> geoStyleList = new ArrayList<>();
			for (int i = 0; i < selectedRow.length; i++) {
				geoStyleList.add(this.themeRange.getItem(selectedRow[i]).getStyle());
			}
			JDialogSymbolsChange jDialogSymbolsChange = new JDialogSymbolsChange(symbolType, geoStyleList);
			jDialogSymbolsChange.showDialog();
		}

		getTable();
		if (selectedRow.length > 0) {
			for (int i = 0; i < selectedRow.length; i++) {
				this.tableRangeInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
			}
		}
	}

	/**
	 * 重置文本风格
	 *
	 * @param selectRow   要重置文本风格的行
	 * @param nowGeoStyle 新的文本风格
	 */
	private void resetGeoSytle(int selectRow, GeoStyle nowGeoStyle) {
		ThemeRangeItem item = themeRange.getItem(selectRow);
		item.setStyle(nowGeoStyle);
		ImageIcon nowGeoStyleIcon = ThemeItemLabelDecorator.buildGeoStyleIcon(this.datasetVector, nowGeoStyle);
		this.tableRangeInfo.setValueAt(nowGeoStyleIcon, selectRow, TABLE_COLUMN_GEOSTYLE);
	}

	class LayerChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			int[] selectRows = tableRangeInfo.getSelectedRows();
			map = ThemeGuideFactory.getMapControl().getMap();
			themeRangeLayer = MapUtilities.findLayerByName(map, layerName);
			if (null != themeRangeLayer && null != themeRangeLayer.getTheme() && themeRangeLayer.getTheme() instanceof ThemeRange) {
				datasetVector = (DatasetVector) themeRangeLayer.getDataset();
				themeRange = new ThemeRange((ThemeRange) themeRangeLayer.getTheme());
				getTable();
				map.refresh();
				for (int i = 0; i < selectRows.length; i++) {
					tableRangeInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			}
		}
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonMerge) {
				int[] selectedRows = tableRangeInfo.getSelectedRows();
				if (selectedRows.length == tableRangeInfo.getRowCount()) {
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Warning_RquiredTwoFieldForRange"));
				} else {
					// 合并选中项
					mergeItem();
				}
			} else if (e.getSource() == buttonSplit) {
				// 拆分选中项
				splitItem();
			} else if (e.getSource() == buttonVisible) {
				// 批量修改分段的可见状态
				setItemVisble();
			} else if (e.getSource() == buttonStyle) {
				// 批量修改单值段的符号方案
				setItemGeoSytle();
			} else if (e.getSource() == buttonContinuousMapLocation) {
				//图层是否更随点选联动
				setContinuousMapLocation();
			} else if (e.getSource() == menuItemMapLocation) {
				//进行地图定位
				mapLocation();
			} else if (e.getSource() == menuItemReviseStyle) {
				//进行风格修改
				int selectRow = tableRangeInfo.getSelectedRow();
				setItemGeoSytle();
				tableRangeInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			}
			refreshAtOnce();
		}

		/**
		 * 子项高亮显示并定位 2016.12.13
		 * yuanR
		 */
		private void mapLocation() {
			//System.out.println("开始编辑定位");
		}

		/**
		 * 拆分
		 */
		private void splitItem() {
			int selectRow = tableRangeInfo.getSelectedRow();
			if (selectRow >= 0) {
				ThemeRangeItem item = themeRange.getItem(selectRow);
				double splitValue = (item.getEnd() + item.getStart()) / 2;
				if (selectRow == 0) {
					// 第零条数据的拆分中值
					splitValue = (item.getEnd() + ((int) item.getEnd()) - 1) / 2;
				}
				if (selectRow == tableRangeInfo.getRowCount() - 1) {
					// 最后一条的拆分中值
					splitValue = (item.getStart() + ((int) item.getStart()) + 1) / 2;
				}
				String rangePrecision = comboBoxRangePrecision.getSelectedItem().toString();
				DecimalFormat format = new DecimalFormat(rangePrecision.replace("1", "0"));
				String diff = String.valueOf(item.getEnd() - item.getStart());
				// 首尾项差值和舍入精度不同时才能进行拆分
				if (!rangePrecision.equals(diff)) {
					String startCaption = "";
					String endCaption = "";
					if (selectRow == 0) {
						startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat1"), "Min", format.format(splitValue));
						endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(splitValue),
								format.format(item.getEnd()));
					} else if (selectRow == tableRangeInfo.getRowCount() - 1) {
						startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(item.getStart()),
								format.format(splitValue));
						endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(splitValue), "Max");
					} else {
						startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(item.getStart()),
								format.format(splitValue));
						endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(splitValue),
								format.format(item.getEnd()));
					}
					themeRange.split(selectRow, splitValue, item.getStyle(), startCaption, item.getStyle(), endCaption);
					isMergeOrSplit = true;
					getTable();
					int rangCount = themeRange.getCount();
					comboBoxRangeCount.setSelectedItem(String.valueOf(rangCount));
					tableRangeInfo.setRowSelectionInterval(selectRow, selectRow);
				}
			}
		}

		/**
		 * 合并项
		 */
		private void mergeItem() {
			int[] selectedRows = tableRangeInfo.getSelectedRows();
			int startIndex = selectedRows[0];
			int endIndex = selectedRows[selectedRows.length - 1];
			ThemeRangeItem startItem = themeRange.getItem(startIndex);
			ThemeRangeItem endItem = themeRange.getItem(endIndex);
			String caption = "";
			if (startIndex == 0) {
				caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat1"), "Min", String.valueOf(endItem.getEnd()));
			} else if (endIndex == tableRangeInfo.getRowCount() - 1) {
				caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(startItem.getStart()), "Max");
			} else {
				caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(startItem.getStart()),
						String.valueOf(endItem.getEnd()));
			}
			themeRange.merge(startIndex, selectedRows.length, startItem.getStyle(), caption);
			isMergeOrSplit = true;
			getTable();
			int rangeCount = themeRange.getCount();
			comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCount));
			tableRangeInfo.setRowSelectionInterval(selectedRows[0], selectedRows[0]);
			buttonMerge.setEnabled(false);
			buttonSplit.setEnabled(true);
		}

		/**
		 * 设置分段项是否可见
		 */
		private void setItemVisble() {
			int[] selectedRow = tableRangeInfo.getSelectedRows();
			// 有不可见的项就全部设置为不可见，全部不可见，或者全部可见就设置为相反状态
			if (hasInvisible(selectedRow) && !allItemInvisible(selectedRow)) {
				for (int i = 0; i < selectedRow.length; i++) {
					themeRange.getItem(selectedRow[i]).setVisible(false);
				}
			} else {
				for (int i = 0; i < selectedRow.length; i++) {
					resetVisible(selectedRow[i]);
				}
			}
			getTable();
			for (int i = 0; i < selectedRow.length; i++) {
				tableRangeInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
			}
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
			for (int i = 0; i < selectedRows.length; i++) {
				if (!themeRange.getItem(selectedRows[i]).isVisible()) {
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
			for (int i = 0; i < selectedRows.length; i++) {
				if (!themeRange.getItem(selectedRows[i]).isVisible()) {
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
			ThemeRangeItem tempThemeRangeItem = themeRange.getItem(selectRow);
			boolean visible = tempThemeRangeItem.isVisible();
			if (visible) {
				tempThemeRangeItem.setVisible(false);
				tableRangeInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
			} else {
				tempThemeRangeItem.setVisible(true);
				tableRangeInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
			}
		}

	}

	class LocalMouseListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == tableRangeInfo) {
				int[] selectedRows = tableRangeInfo.getSelectedRows();
				if (selectedRows.length == 1) {
					buttonMerge.setEnabled(false);
					buttonSplit.setEnabled(true);
				} else if (selectedRows.length >= 2) {
					buttonSplit.setEnabled(false);
				}
				if (selectedRows.length >= 2 && MathUtilities.isContinuouslyArray(selectedRows)) {
					buttonMerge.setEnabled(true);
				} else {
					buttonMerge.setEnabled(false);
				}
			}

			//满足鼠标拖拽，也可以实现多选效果
			if (e.getSource() == tableRangeInfo && 1 == e.getClickCount() && e.getButton() == MouseEvent.BUTTON1) {
				//此时进行专题图子项连续定位
				ContinuousMapLocation();
			}

			if (e.getSource() == comboBoxRangeCount.getComponent(0)) {
				isMergeOrSplit = false;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == tableRangeInfo && 1 == e.getClickCount() && tableRangeInfo.getSelectedColumn() == TABLE_COLUMN_VISIBLE
					&& tableRangeInfo.getSelectedRows().length == 1) {
				int selectRow = tableRangeInfo.getSelectedRow();
				ThemeRangeItem item = themeRange.getItem(selectRow);
				boolean isVisible = item.isVisible();
				if (isVisible) {
					item.setVisible(false);
					tableRangeInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					item.setVisible(true);
					tableRangeInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
				tableRangeInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			} else if (e.getSource() == tableRangeInfo && 2 == e.getClickCount() && tableRangeInfo.getSelectedColumn() == TABLE_COLUMN_GEOSTYLE) {
				int selectRow = tableRangeInfo.getSelectedRow();
				setItemGeoSytle();
				tableRangeInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			}
			//打开jtable右键菜单
			if (e.getSource() == tableRangeInfo && e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
				//打开右键菜单根据是否可视(可选择)状态设置定位功能是否可用
				if (themeRangeLayer.isVisible() && themeRangeLayer.isSelectable()) {
					menuItemMapLocation.setEnabled(false);
				} else {
					menuItemMapLocation.setEnabled(false);
				}
				tablePopupMenuRangeTheme.show(tableRangeInfo, e.getX(), e.getY());
			}

			if (e.getSource() == comboBoxExpression.getComponent(0) || e.getSource() == comboBoxRangeMethod) {
				isResetComboBox = false;
			}
		}

		/**
		 * 专题图子项连续定位   2016.12.23yuanR
		 */
		private void ContinuousMapLocation() {
			if (isContinuousMapLocation) {
				//清除跟踪层
				MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION);
				map.refreshTrackingLayer();
				Recordset selectedRecordsets;
				int[] selectRow = tableRangeInfo.getSelectedRows();
				for (int i = 0; i < tableRangeInfo.getSelectedRowCount(); i++) {
					ThemeRangeItem item = themeRange.getItem(selectRow[i]);
					String expression = comboBoxExpression.getSelectedItem().toString();
					// 构建选中子项的sql查询
					QueryParameter selectedParameter = new QueryParameter();
					selectedParameter.setAttributeFilter(expression + " Between " + item.getStart() + " AND " + item.getEnd());
					selectedParameter.setCursorType(CursorType.STATIC);
					selectedRecordsets = datasetVector.query(selectedParameter);
					selectedParameter.dispose();
					if (selectedRecordsets.getRecordCount() > 0) {
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
							rangeThemeTrackingLayer.add(selectedGeo, TAG_CONTINUOUSMAPLOCATION);
							points.add(selectedGeo.getBounds().leftBottom);
							points.add(selectedGeo.getBounds().rightTop);
							//对象释放
							selectedGeo.dispose();
							selectedRecordsets.moveNext();
						}

						//如果构建的最小矩形没有完全包含于map的矩形，移动其到map中心
						if (getMInRectangle2D(points) != null) {
							if (!map.getViewBounds().contains(getMInRectangle2D(points))) {
								map.setCenter(getMInRectangle2D(points).getCenter());
								map.refresh();
							}
						}
						//对象释放
						if (selectedGeoStyle != null) {
							selectedGeoStyle.dispose();
						}
						if (selectedGeoStyle3D != null) {
							selectedGeoStyle3D.dispose();
						}
					} else {
						Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_NullQuery"), item.getCaption()));
					}
					//对象释放
					selectedRecordsets.dispose();
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


	class LocalKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == tableRangeInfo && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
				//当通过键盘改变jtable行选时，同步实现定位功能
				new LocalMouseListener().ContinuousMapLocation();
			}
			if (e.getSource() == tableRangeInfo && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				//当按下esc键，清除跟踪层
				MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION);
				map.refreshTrackingLayer();
			}
			if (e.getSource() == nowMapControl && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				//当焦点在mapContorl上时，按esc键清除跟踪层
				MapUtilities.clearTrackingObjects(map, TAG_CONTINUOUSMAPLOCATION);
				map.refreshTrackingLayer();
			}

		}
	}

	class LocalComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Dataset[] datasets = ThemeUtil.getDatasets(themeRangeLayer, datasetVector);
				if (e.getSource() == comboBoxColorStyle) {
					// 修改颜色方案
					refreshColor();
					getTable();
				} else if (e.getSource() == comboBoxExpression) {
					// sql表达式
					boolean isItemChanged = ThemeUtil.getSqlExpression(comboBoxExpression, datasets, comboBoxArray, themeRange.getRangeExpression(), true);
					if (isItemChanged) {
						// 修改表达式
						setFieldInfo();
					}
				} else if (e.getSource() == comboBoxRangeCount && !isCustom && !isMergeOrSplit) {
					// 修改段数
					setRangeCount();
				} else if (e.getSource() == comboBoxRangePrecision) {
					// 设置分段舍入精度
					setRangePrecision();
				} else if (e.getSource() == comboBoxRangeMethod) {
					// 设置分段方法
					setRangeMethod();
				} else if (e.getSource() == comboBoxRangeFormat) {
					// 设置标题格式
					setRangeFormat();
				} else if (e.getSource() == comboBoxOffsetUnity) {
					// 修改偏移量单位
					setOffsetUnity();
				} else if (e.getSource() == comboBoxOffsetX) {
					// 修改水平偏移量
					setOffsetX();
				} else if (e.getSource() == comboBoxOffsetY) {
					// 修改垂直偏移量
					setOffsetY();
				}
				refreshAtOnce();
				tableRangeInfo.setRowSelectionInterval(0, 0);
			}
		}

		/**
		 * 修改垂直偏移量
		 */
		private void setOffsetY() {
			String expression = comboBoxOffsetY.getSelectedItem().toString();
			if ("0".equals(expression)) {
				themeRange.setOffsetY("0");
			} else {
				expression = expression.substring(expression.lastIndexOf(".") + 1, expression.length());
				themeRange.setOffsetY(expression);
			}
		}

		/**
		 * 修改水平偏移量
		 */
		private void setOffsetX() {
			String expression = comboBoxOffsetX.getSelectedItem().toString();
			if ("0".equals(expression)) {
				themeRange.setOffsetX("0");
			} else {
				expression = expression.substring(expression.lastIndexOf(".") + 1, expression.length());
				themeRange.setOffsetX(expression);
			}
		}

		/**
		 * 设置标题格式
		 */
		private void setRangeFormat() {
			int count = comboBoxRangeFormat.getSelectedIndex();
			if (0 == count) {
				captiontype = "-";
			} else {
				captiontype = "<=x<";
			}
			getTable();
		}

		private void setRangeCount() {
			if (!StringUtilities.isNullOrEmptyString(comboBoxRangeCount.getSelectedItem().toString())) {
				rangeCount = Integer.valueOf(comboBoxRangeCount.getSelectedItem().toString());
				resetThemeInfo();
			}
		}

		/**
		 * 设置分段方法
		 */
		private void setRangeMethod() {
			int rangeMethod = comboBoxRangeMethod.getSelectedIndex();
			rangeExpression = comboBoxExpression.getSelectedItem().toString();
			switch (rangeMethod) {
				case 0:
					// 等距分段
					rangeMode = RangeMode.EQUALINTERVAL;
					comboBoxRangeCount.setEnabled(true);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
					break;
				case 1:
					// 平方根分段
					if (ThemeUtil.hasNegative(datasetVector, rangeExpression)) {
						// 有负数且为平方根分段
						UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
								MapViewProperties.getString("String_RangeMode_SquareRoot")));
						// 重置分段方法下拉框
						isResetComboBox = true;
						resetComboBoxRangeMode();
						return;
					} else {
						rangeMode = RangeMode.SQUAREROOT;
						comboBoxRangeCount.setEnabled(true);
						spinnerRangeLength.setEnabled(false);
						isCustom = false;
						resetThemeInfo();
					}

					break;
				case 2:
					// 标准差分段
					rangeMode = RangeMode.STDDEVIATION;
					comboBoxRangeCount.setEnabled(false);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
					break;
				case 3:
					// 对数分段
					if (ThemeUtil.hasNegative(datasetVector, rangeExpression)) {
						// 有负数且为对数分段
						UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
								MapViewProperties.getString("String_RangeMode_Logarithm")));
						isResetComboBox = true;
						resetComboBoxRangeMode();
						return;
					} else {
						rangeMode = RangeMode.LOGARITHM;
						comboBoxRangeCount.setEnabled(true);
						spinnerRangeLength.setEnabled(false);
						isCustom = false;
						resetThemeInfo();
					}
					break;
				case 4:
					// 等计数分段
					rangeMode = RangeMode.QUANTILE;
					comboBoxRangeCount.setEnabled(true);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
					break;
				case 5:
					// 自定义分段
					rangeMode = RangeMode.CUSTOMINTERVAL;
					double defaultRangeCount = 0.0;
					if (themeRange.getCount() > 2) {
						if (StringUtilities.isNumber(new DecimalFormat("0").format(themeRange.getItem(1).getEnd() - themeRange.getItem(1).getStart()))) {
							defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeRange.getItem(1).getEnd() - themeRange.getItem(1).getStart()));
						}
					} else {
						if (StringUtilities.isNumber(new DecimalFormat("0").format(themeRange.getItem(0).getEnd()))) {
							defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeRange.getItem(0).getEnd()));
						}
					}
					spinnerRangeLength.setValue(defaultRangeCount);
					comboBoxRangeCount.setEnabled(false);
					spinnerRangeLength.setEnabled(true);
					makeDefaultAsCustom();
					break;
				default:
					break;
			}
		}

		/**
		 * 设置分段舍入精度
		 */
		private void setRangePrecision() {
			String precisionStr = comboBoxRangePrecision.getSelectedItem().toString();
			rangeCount = Integer.valueOf(comboBoxRangeCount.getSelectedItem().toString());
			// 设置分段舍入精度，用于分度段数确定
			precision = Double.valueOf(precisionStr);
			// 这里做一个精度设置，以避免出现1<=x<1这样的表达式
			themeRange.setPrecision(precision);
			if (rangeMode == RangeMode.CUSTOMINTERVAL) {
				makeDefaultAsCustom();
			} else {
				resetThemeInfo();
			}
			getTable();
		}

		/**
		 * 字段表达式
		 */
		private void setFieldInfo() {
			rangeExpression = (String) comboBoxExpression.getSelectedItem();
			if (ThemeUtil.hasNegative(datasetVector, rangeExpression) && rangeMode == RangeMode.LOGARITHM) {
				// 有负数且为对数分段
				UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
						MapViewProperties.getString("String_RangeMode_Logarithm")));
				// 重置字段表达式下拉框
				isResetComboBox = true;
				resetComboBoxRangeExpression(themeRange.getRangeExpression());
				return;
			}
			if (ThemeUtil.hasNegative(datasetVector, rangeExpression) && rangeMode == RangeMode.SQUAREROOT) {
				// 有负数且为平方根分段
				UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
						MapViewProperties.getString("String_RangeMode_SquareRoot")));
				// 重置字段表达式下拉框
				isResetComboBox = true;
				resetComboBoxRangeExpression(themeRange.getRangeExpression());
				return;
			}
			if (rangeMode == RangeMode.CUSTOMINTERVAL) {
				makeDefaultAsCustom();
				return;
			} else {
				resetThemeInfo();
				return;
			}
		}

		/**
		 * 重建专题图
		 */
		private void resetThemeInfo() {
			if (isResetComboBox) {
				return;
			}
			rangeExpression = comboBoxExpression.getSelectedItem().toString();
			if (rangeExpression.isEmpty()) {
				comboBoxExpression.setSelectedIndex(0);
			} else if (rangeCount < 2 || rangeCount > 32) {
				// 段数小于2，或者段数大于最大值
				comboBoxRangeCount.setSelectedItem(String.valueOf(themeRange.getCount()));
			} else {
				ThemeRange theme = null;
				// 外部连接表字段创建专题图
				theme = ThemeRange.makeDefault(datasetVector, rangeExpression, rangeMode, rangeCount, ColorGradientType.GREENRED, themeRangeLayer
						.getDisplayFilter().getJoinItems(), precision);
				if (null == theme) {
					// 专题图为空，提示专题图更新失败
					UICommonToolkit.showErrorMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
					resetComboBoxRangeExpression(themeRange.getRangeExpression());
				} else {
					refreshThemeRange(theme);
				}
			}
		}

		private void resetComboBoxRangeMode() {
			if (rangeMode.equals(RangeMode.EQUALINTERVAL)) {
				comboBoxRangeMethod.setSelectedIndex(0);
				return;
			}
			if (rangeMode.equals(RangeMode.SQUAREROOT)) {
				comboBoxRangeMethod.setSelectedIndex(1);
				return;
			}
			if (rangeMode.equals(RangeMode.STDDEVIATION)) {
				comboBoxRangeMethod.setSelectedIndex(2);
				return;
			}
			if (rangeMode.equals(RangeMode.LOGARITHM)) {
				comboBoxRangeMethod.setSelectedIndex(3);
				return;
			}
			if (rangeMode.equals(RangeMode.QUANTILE)) {
				comboBoxRangeMethod.setSelectedIndex(4);
				return;
			}
			if (rangeMode.equals(RangeMode.CUSTOMINTERVAL)) {
				comboBoxRangeMethod.setSelectedIndex(5);
				return;
			}
		}

		/**
		 * 修改偏移量单位
		 */
		private void setOffsetUnity() {
			if (MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit").equals(comboBoxOffsetUnity.getSelectedItem().toString())) {
				themeRange.setOffsetFixed(true);
				labelOffsetXUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				labelOffsetYUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
			} else {
				themeRange.setOffsetFixed(false);
				labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
				labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
			}
		}
	}

	/**
	 * 判断段值是否合法
	 *
	 * @return
	 */
	public boolean isRightRangeValue(String rangeValue, int selectRow) {
		boolean isRightValue = false;
		double range = Double.parseDouble(rangeValue);
		if (selectRow == 0) {
			double nextValue = themeRange.getItem(selectRow + 1).getEnd();
			if (nextValue - range > 0.0) {
				isRightValue = true;
			}
		} else if (selectRow != tableRangeInfo.getRowCount() - 1) {
			double prewValue = themeRange.getItem(selectRow - 1).getEnd();
			double nextValue = themeRange.getItem(selectRow + 1).getEnd();
			if (nextValue - range > 0.0 && range - prewValue > 0.0) {
				isRightValue = true;
			}
		}
		return isRightValue;
	}

	class LocalTableModelListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent arg0) {
			int selectRow = arg0.getFirstRow();
			int selectColumn = arg0.getColumn();
			try {
				if (selectColumn == TABLE_COLUMN_RANGEVALUE && !StringUtilities.isNullOrEmptyString(tableRangeInfo.getValueAt(selectRow, selectColumn))) {
					String rangeValue = tableRangeInfo.getValueAt(selectRow, selectColumn).toString();
					if (StringUtilities.isNumber(rangeValue) && isRightRangeValue(rangeValue, selectRow)) {
						// 如果输入为数值且段值合法时修改段值
						setRangeValue(selectRow, rangeValue);
					}
				} else if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilities.isNullOrEmptyString(tableRangeInfo.getValueAt(selectRow, selectColumn))) {
					String caption = tableRangeInfo.getValueAt(selectRow, selectColumn).toString();
					themeRange.getItem(selectRow).setCaption(caption);
				}
				refreshAtOnce();
				getTable();
				tableRangeInfo.addRowSelectionInterval(selectRow, selectRow);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		private void setRangeValue(int selectRow, String rangeValue) {
			String numberDecimalFormat = comboBoxRangePrecision.getSelectedItem().toString();
			numberDecimalFormat = numberDecimalFormat.replaceAll("1", "0");
			numberDecimalFormat = numberDecimalFormat.replaceAll("0", "#");
			DecimalFormat decimalFormat = new DecimalFormat(numberDecimalFormat);
			double end = themeRange.getItem(selectRow).getEnd();
			String endString = decimalFormat.format(end);
			String tempStr = decimalFormat.format(Double.valueOf(rangeValue));
			themeRange.getItem(selectRow).setEnd(Double.parseDouble(tempStr));
			String caption = themeRange.getItem(selectRow).getCaption();
			// 替换当前行的标题
			if (StringUtilities.isNumber(endString)) {
				caption = caption.replace(endString, tempStr);
				themeRange.getItem(selectRow).setCaption(caption);
			}
			// 替换下一行的标题
			if (selectRow != themeRange.getCount() - 1) {
				String nextCaption = themeRange.getItem(selectRow + 1).getCaption();
				nextCaption = nextCaption.replace(endString, tempStr);
				themeRange.getItem(selectRow + 1).setCaption(nextCaption);
			}
		}

	}

	private void resetComboBoxRangeExpression(String expression) {
		if (comboBoxArray.contains(expression)) {
			comboBoxExpression.setSelectedItem(expression);
		} else {
			comboBoxExpression.setSelectedItem(expression.substring(expression.indexOf(".") + 1, expression.length()));
		}
	}

	private void resetItemLineColor() {
		for (int i = 0; i < this.themeRange.getCount(); i++) {
			GeoStyle textStyle = themeRange.getItem(i).getStyle();
			textStyle.setLineColor(Color.GRAY);
		}
	}

	/**
	 * 刷新theme
	 *
	 * @param theme
	 */
	private void refreshThemeRange(ThemeRange theme) {
		try {
			this.themeRange = new ThemeRange(theme);
			this.themeRange.setRangeExpression(rangeExpression);
			resetItemLineColor();
			refreshColor();
			getTable();
			if (2 <= themeRange.getCount()) {
				this.rangeCount = this.themeRange.getCount();
				this.comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCount));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 创建自定义的分段专题图
	 */
	private void makeDefaultAsCustom() {
		if (isResetComboBox) {
			return;
		}
		double rangeLength = (double) spinnerRangeLength.getValue();
		ThemeRange theme = null;
		if (rangeLength > 0) {
			// 外部连接表字段创建专题图
			theme = ThemeRange.makeDefault(datasetVector, rangeExpression, rangeMode, rangeLength, ColorGradientType.GREENRED, themeRangeLayer
					.getDisplayFilter().getJoinItems(), precision);
			if (null == theme || theme.getCount() == 0) {
				// 专题图为空，提示专题图更新失败
				UICommonToolkit.showErrorMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
				comboBoxExpression.setSelectedItem(themeRange.getRangeExpression());
			} else {
				this.isCustom = true;
				refreshThemeRange(theme);
			}
		}
	}

	class LocalSpinnerChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			makeDefaultAsCustom();
			refreshAtOnce();
		}
	}

	class LocalDefualTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public LocalDefualTableModel(Object[][] obj, String[] name) {
			super(obj, name);
		}

		@Override
		public Class getColumnClass(int column) {
			// 要这样定义table，要重写这个方法(0,0)的意思就是别的格子的类型都跟(0,0)的一样。
			if (TABLE_COLUMN_VISIBLE == column || TABLE_COLUMN_GEOSTYLE == column) {
				return getValueAt(0, 0).getClass();
			}
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == TABLE_COLUMN_RANGEVALUE || columnIndex == TABLE_COLUMN_CAPTION;
		}
	}

	/**
	 * 获取是否及时刷新值
	 *
	 * @return
	 */
	public boolean isRefreshAtOnece() {
		return this.isRefreshAtOnce;
	}

	/**
	 * 设置是否及时刷新
	 *
	 * @param isRefreshAtOnece
	 */
	public void setRefreshAtOnece(boolean isRefreshAtOnece) {
		this.isRefreshAtOnce = isRefreshAtOnece;
	}

	/**
	 * 获取分段专题图图层
	 *
	 * @return
	 */
	public Layer getThemeRangeLayer() {
		return this.themeRangeLayer;
	}

	/**
	 * 设置分段专题图图层
	 *
	 * @param themeRangeLayer
	 */
	public void setThemeRangeLayer(Layer themeRangeLayer) {
		this.themeRangeLayer = themeRangeLayer;
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeRange;
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	public void refreshMapAndLayer() {
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeRangeLayer = MapUtilities.findLayerByName(map, layerName);
		if (null != themeRangeLayer && null != themeRangeLayer.getTheme() && themeRangeLayer.getTheme().getType() == ThemeType.RANGE) {
			ThemeRange nowThemeRange = (ThemeRange) this.themeRangeLayer.getTheme();
			nowThemeRange.clear();
			if (0 < this.themeRange.getCount()) {
				for (int i = 0; i < this.themeRange.getCount(); i++) {
					nowThemeRange.addToTail(this.themeRange.getItem(i), true);
				}
			}
			nowThemeRange.setRangeExpression(this.themeRange.getRangeExpression());
			nowThemeRange.setPrecision(this.themeRange.getPrecision());
			UICommonToolkit.getLayersManager().getLayersTree().refreshNode(this.themeRangeLayer);
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

	@Override
	public Layer getCurrentLayer() {
		return themeRangeLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeRangeLayer = layer;
	}
}
