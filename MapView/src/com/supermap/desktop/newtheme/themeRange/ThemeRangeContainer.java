package com.supermap.desktop.newtheme.themeRange;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeItemLabelDecorator;
import com.supermap.desktop.newtheme.commonUtils.UniqueValueCountUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.MathUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

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
	private ColorsComboBox comboBoxColorStyle = new ColorsComboBox();
	private JToolBar toolBar = new JToolBar();
	private JButton buttonMerge = new JButton();
	private JButton buttonSplit = new JButton();
	private JButton buttonVisible = new JButton();
	private JButton buttonStyle = new JButton();
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

	private static String[] nameStrings = { MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_Sytle"),
			MapViewProperties.getString("String_Title_RangeValue"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption") };
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
	private boolean isNewTheme = false;
	private boolean isMergeOrSplit = false;
	private boolean isResetComboBox = false;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private String layerName;

	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalMouseListener mouseListener = new LocalMouseListener();
	private transient LocalComboBoxItemListener itemListener = new LocalComboBoxItemListener();
	private transient LocalSpinnerChangeListener changeListener = new LocalSpinnerChangeListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();
	private PropertyChangeListener layersTreePropertyChangeListener = new LayerChangeListener();;

	/**
	 * @wbp.parser.constructor
	 */
	public ThemeRangeContainer(DatasetVector datasetVector, ThemeRange themeRange) {
		this.datasetVector = datasetVector;
		this.themeRange = new ThemeRange(themeRange);
		this.map = initCurrentTheme(datasetVector);
		this.precision = themeRange.getPrecision();
		this.isNewTheme = true;
		initComponents();
		initResources();
		registActionListener();
	}

	public ThemeRangeContainer(Layer layer) {
		this.themeRangeLayer = layer;
		this.layerName = this.themeRangeLayer.getName();
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeRange = new ThemeRange((ThemeRange) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.precision = themeRange.getPrecision();
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 初始化单值专题图
	 *
	 * @param dataset
	 * @return
	 */
	private Map initCurrentTheme(DatasetVector dataset) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeRangeLayer = mapControl.getMap().getLayers().add(dataset, themeRange, true);
			this.layerName = this.themeRangeLayer.getName();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
			mapControl.getMap().refresh();
		}
		return mapControl.getMap();
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
		initPanelProperty();
		initPanelAdvance();
		this.comboBoxColorStyle.setSelectedIndex(21);
		if (this.isNewTheme) {
			refreshColor();
		}
	}

	/**
	 * 初始化属性界面
	 */
	private void initPanelProperty() {
		//@formatter:off
		initToolBar();
		getFieldComboBox(this.comboBoxExpression);
		initComboBoxRangeExpression();
		initComboBoxRangMethod();
		initComboBoxRangeCount();
		initComboBoxRangePrecision();
		initComboBoxRangeFormat();
		this.panelProperty.setLayout(new GridBagLayout());
		this.panelProperty.add(this.labelExpression,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxExpression,  new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeMethod,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxRangeMethod, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeCount,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxRangeCount,  new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeLength,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.spinnerRangeLength.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		this.spinnerRangeLength.setEnabled(false);
		this.panelProperty.add(this.spinnerRangeLength,    new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangePrecision,   new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxRangePrecision,new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeFormat,      new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxRangeFormat,   new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelColorStyle,       new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxColorStyle,    new GridBagConstraintsHelper(1, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolBar,               new GridBagConstraintsHelper(0, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,0).setWeight(100, 0).setIpad(40, 0));
		this.panelProperty.add(this.scrollPane,            new GridBagConstraintsHelper(0, 8, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0,10,5,10).setWeight(100, 3).setFill(GridBagConstraints.BOTH));
		
		getTable();
		this.tableRangeInfo.setRowSelectionInterval(0, 0);
		
		this.scrollPane.setViewportView(this.tableRangeInfo);
		//@formatter:on
	}

	/**
	 * 初始化表达式下拉框
	 */
	private void initComboBoxRangeExpression() {
		this.comboBoxExpression.setEditable(true);
		this.rangeExpression = this.themeRange.getRangeExpression();
		if (StringUtilties.isNullOrEmpty(this.rangeExpression)) {
			rangeExpression = "0";
		}
		this.comboBoxExpression.setSelectedItem(this.rangeExpression);
		if (!this.rangeExpression.equals(this.comboBoxExpression.getSelectedItem())) {
			this.comboBoxExpression.addItem(this.rangeExpression);
			this.comboBoxExpression.setSelectedItem(this.rangeExpression);
		}
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
		this.panelOffsetSet.add(this.labelOffsetUnity,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1));
		this.panelOffsetSet.add(this.comboBoxOffsetUnity, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetX,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1));
		this.panelOffsetSet.add(this.comboBoxOffsetX,     new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetXUnity,   new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1));
		this.panelOffsetSet.add(this.labelOffsetY,        new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1));
		this.panelOffsetSet.add(this.comboBoxOffsetY,     new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetYUnity,   new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(1, 1));
		//@formatter:on
	}

	/**
	 * 初始化偏移量单位
	 */
	private void initComboBoxOffsetUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[] {
				MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map") }));
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
		getFieldComboBox(this.comboBoxOffsetX);
		this.comboBoxOffsetX.addItem("0");
		String offsetX = this.themeRange.getOffsetX();
		if (StringUtilties.isNullOrEmpty(offsetX)) {
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
		getFieldComboBox(this.comboBoxOffsetY);
		this.comboBoxOffsetY.addItem("0");
		String offsetY = this.themeRange.getOffsetY();
		if (StringUtilties.isNullOrEmpty(offsetY)) {
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
		this.comboBoxRangeMethod.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_RangeMode_EqualInterval"),
				MapViewProperties.getString("String_RangeMode_SquareRoot"), MapViewProperties.getString("String_RangeMode_StdDeviation"),
				MapViewProperties.getString("String_RangeMode_Logarithm"), MapViewProperties.getString("String_RangeMode_Quantile"),
				MapViewProperties.getString("String_RangeMode_CustomInterval") }));
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
		this.comboBoxRangeCount.setModel(new DefaultComboBoxModel<String>(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32" }));
		this.comboBoxRangeCount.setEditable(true);
		this.comboBoxRangeCount.setSelectedItem(String.valueOf(this.themeRange.getCount()));
	}

	/**
	 * 初始化段值精度项
	 */
	private void initComboBoxRangePrecision() {
		this.comboBoxRangePrecision.setModel(new DefaultComboBoxModel<String>(new String[] { "10000000", "1000000", "100000", "10000", "1000", "100", "10",
				"1", "0.1", "0.01", "0.001", "0.0001", "0.00001", "0.000001", "0.0000001" }));
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
		this.comboBoxRangeFormat.setModel(new DefaultComboBoxModel<String>(new String[] { "0-100", "0<=x<100" }));
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
		this.tableRangeInfo.setModel(new LocalDefualTableModel(new Object[this.rangeCount][4], this.nameStrings));
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
			} else {
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
	 * 表达式
	 *
	 * @return m_fieldComboBox
	 */
	private JComboBox<String> getFieldComboBox(JComboBox<String> comboBox) {
		int count = this.datasetVector.getFieldCount();
		for (int j = 0; j < count; j++) {
			FieldInfo fieldInfo = this.datasetVector.getFieldInfos().get(j);
			if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.INT64
					|| fieldInfo.getType() == FieldType.DOUBLE || fieldInfo.getType() == FieldType.SINGLE) {
				String item = datasetVector.getName() + "." + fieldInfo.getName();
				comboBox.addItem(item);
			}
		}
		comboBox.addItem(MapViewProperties.getString("String_Combobox_Expression"));
		return comboBox;
	}

	/**
	 * 颜色方案改变时刷新颜色
	 */
	private void refreshColor() {
		if (this.comboBoxColorStyle != null) {
			int colorCount = ((Colors) this.comboBoxColorStyle.getSelectedItem()).getCount();
			Colors colors = (Colors) this.comboBoxColorStyle.getSelectedItem();
			int themeRangeCount = this.themeRange.getCount();
			if (themeRangeCount > 0) {
				float ratio = (1f * colorCount) / (1f * themeRangeCount);
				setGeoStyleColor(this.themeRange.getItem(0).getStyle(), colors.get(0));
				setGeoStyleColor(this.themeRange.getItem(themeRangeCount - 1).getStyle(), colors.get(colorCount - 1));
				for (int i = 1; i < themeRangeCount - 1; i++) {
					int colorIndex = Math.round(i * ratio);
					if (colorIndex == colorCount) {
						colorIndex--;
					}
					setGeoStyleColor(this.themeRange.getItem(i).getStyle(), colors.get(colorIndex));
				}
			}
		}
	}

	/**
	 * 根据当前数据集类型设置颜色方案
	 *
	 * @param geoStyle 需要设置的风格
	 * @param color 设置的颜色
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
		this.toolBar.add(this.buttonMerge);
		this.toolBar.add(this.buttonSplit);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonVisible);
		this.toolBar.add(this.buttonStyle);
		this.toolBar.addSeparator();
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
		this.tableRangeInfo.addMouseListener(this.mouseListener);
		this.comboBoxColorStyle.addItemListener(this.itemListener);
		this.comboBoxExpression.addItemListener(this.itemListener);
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
		this.tableRangeInfo.removeMouseListener(this.mouseListener);
		this.comboBoxColorStyle.removeItemListener(this.itemListener);
		this.comboBoxExpression.removeItemListener(this.itemListener);
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
	}

	/**
	 * 批量设置文本风格
	 */
	private void setItemGeoSytle() {
		int[] selectedRow = this.tableRangeInfo.getSelectedRows();
		SymbolDialog textStyleDialog = new SymbolDialog();
		String name = this.tableRangeInfo.getColumnName(TABLE_COLUMN_VISIBLE);
		int width = this.tableRangeInfo.getColumn(name).getWidth();
		int height = this.tableRangeInfo.getTableHeader().getHeight();
		int x = this.tableRangeInfo.getLocationOnScreen().x + width;
		int y = this.tableRangeInfo.getLocationOnScreen().y - height;
		textStyleDialog.setLocation(x, y);
		Resources resources = Application.getActiveApplication().getWorkspace().getResources();
		SymbolType symbolType = null;

		if (CommonToolkit.DatasetTypeWrap.isPoint(this.datasetVector.getType())) {
			symbolType = SymbolType.MARKER;
		} else if (CommonToolkit.DatasetTypeWrap.isLine(this.datasetVector.getType())) {
			symbolType = SymbolType.LINE;
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(this.datasetVector.getType())) {
			symbolType = SymbolType.FILL;
		}

		if (selectedRow.length == 1) {
			GeoStyle geoStyle = this.themeRange.getItem(selectedRow[0]).getStyle();

			DialogResult dialogResult = textStyleDialog.showDialog(resources, geoStyle, symbolType);
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getStyle();
				if (selectedRow.length == 1) {
					resetGeoSytle(selectedRow[0], nowGeoStyle);
				} else {
					for (int i = 0; i < selectedRow.length; i++) {
						resetGeoSytle(selectedRow[i], nowGeoStyle);
					}
				}
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
	 * @param selectRow 要重置文本风格的行
	 * @param nowGeoStyle 新的文本风格
	 * @param symbolType 文本的风格类型
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
			themeRangeLayer = MapUtilties.findLayerByName(map, layerName);
			if (null != themeRangeLayer && null != themeRangeLayer.getTheme() && themeRangeLayer.getTheme() instanceof ThemeRange) {
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
			}
			refreshAtOnce();
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
				String diff = String.valueOf(item.getEnd() - item.getStart());
				// 首尾项差值和舍入精度不同时才能进行拆分
				if (!rangePrecision.equals(diff)) {
					String startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(item.getStart()),
							String.valueOf(splitValue));
					String endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(splitValue),
							String.valueOf(item.getEnd()));
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
			String caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(startItem.getStart()),
					String.valueOf(endItem.getEnd()));
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
				if (selectedRows.length >= 2 && MathUtilties.isContiuityArray(selectedRows)) {
					buttonMerge.setEnabled(true);
				} else {
					buttonMerge.setEnabled(false);
				}
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
			if (e.getSource() == comboBoxRangeCount.getComponent(0)) {
				isMergeOrSplit = false;
			}
			if (e.getSource() == comboBoxExpression.getComponent(0) || e.getSource() == comboBoxRangeMethod) {
				isResetComboBox = false;
			}
		}
	}

	class LocalComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxColorStyle) {
					// 修改颜色方案
					refreshColor();
					getTable();
				} else if (e.getSource() == comboBoxExpression) {
					// sql表达式
					getSqlExpression(comboBoxExpression);
					// 修改表达式
					setFieldInfo();
					refreshColor();
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
					getSqlExpression(comboBoxOffsetX);
					// 修改水平偏移量
					setOffsetX();
				} else if (e.getSource() == comboBoxOffsetY) {
					getSqlExpression(comboBoxOffsetY);
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
			rangeCount = Integer.valueOf(comboBoxRangeCount.getSelectedItem().toString());
			resetThemeInfo();
		}

		/**
		 * 设置分段方法
		 */
		private void setRangeMethod() {
			String rangeMethod = comboBoxRangeMethod.getSelectedItem().toString();
			if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_EqualInterval"))) {
				// 等距分段
				rangeMode = RangeMode.EQUALINTERVAL;
				comboBoxRangeCount.setEnabled(true);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
			} else if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_StdDeviation"))) {
				// 标准差分段
				rangeMode = RangeMode.STDDEVIATION;
				comboBoxRangeCount.setEnabled(false);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
			} else if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_Quantile"))) {
				// 等计数分段
				rangeMode = RangeMode.QUANTILE;
				comboBoxRangeCount.setEnabled(true);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
			} else if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_SquareRoot"))) {
				if (UniqueValueCountUtil.hasNegative(datasetVector, rangeExpression)) {
					// 有负数且为平方根分段
					JOptionPane.showMessageDialog(
							null,
							MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
									MapViewProperties.getString("String_RangeMode_SquareRoot")), CommonProperties.getString("String_Error"),
							JOptionPane.ERROR_MESSAGE);
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
			} else if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_Logarithm"))) {
				if (UniqueValueCountUtil.hasNegative(datasetVector, rangeExpression)) {
					// 有负数且为对数分段
					JOptionPane.showMessageDialog(
							null,
							MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
									MapViewProperties.getString("String_RangeMode_Logarithm")), CommonProperties.getString("String_Error"),
							JOptionPane.ERROR_MESSAGE);
					// 重置分段方法下拉框
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
			}
			if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_CustomInterval"))) {
				// 自定义分段
				rangeMode = RangeMode.CUSTOMINTERVAL;
				double defaultRangeCount = 0.0;
				if (themeRange.getCount() > 2) {
					defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeRange.getItem(1).getEnd() - themeRange.getItem(1).getStart()));
				} else {
					defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeRange.getItem(0).getEnd()));
				}
				spinnerRangeLength.setValue(defaultRangeCount);
				comboBoxRangeCount.setEnabled(false);
				spinnerRangeLength.setEnabled(true);
				makeDefaultAsCustom();
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
			if (UniqueValueCountUtil.hasNegative(datasetVector, rangeExpression) && rangeMode == RangeMode.LOGARITHM) {
				// 有负数且为对数分段
				JOptionPane.showMessageDialog(
						null,
						MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
								MapViewProperties.getString("String_RangeMode_Logarithm")), CommonProperties.getString("String_Error"),
						JOptionPane.ERROR_MESSAGE);
				// 重置字段表达式下拉框
				isResetComboBox = true;
				resetComboBoxRangeExpression(themeRange.getRangeExpression());
				return;
			}
			if (UniqueValueCountUtil.hasNegative(datasetVector, rangeExpression) && rangeMode == RangeMode.SQUAREROOT) {
				// 有负数且为平方根分段
				JOptionPane.showMessageDialog(
						null,
						MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
								MapViewProperties.getString("String_RangeMode_SquareRoot")), CommonProperties.getString("String_Error"),
						JOptionPane.ERROR_MESSAGE);
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
			if (rangeExpression.isEmpty()) {
				comboBoxExpression.setSelectedIndex(0);
			} else if (rangeCount < 2 || rangeCount > 32) {
				// 段数小于2，或者段数大于最大值
				comboBoxRangeCount.setSelectedItem(String.valueOf(themeRange.getCount()));
			} else {
				ThemeRange theme = ThemeRange.makeDefault(datasetVector, rangeExpression, rangeMode, rangeCount, ColorGradientType.GREENRED, null, precision);
				if (null == theme) {
					// 专题图为空，提示专题图更新失败
					JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_Theme_UpdataFailed"), CommonProperties.getString("String_Error"),
							JOptionPane.ERROR_MESSAGE);
					comboBoxExpression.setSelectedItem(themeRange.getRangeExpression());
					isResetComboBox = true;
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
		 * 获取表达式项
		 *
		 * @param jComboBoxField
		 */
		private void getSqlExpression(JComboBox<String> jComboBoxField) {
			// 判断是否为“表达式”项
			if (MapViewProperties.getString("String_Combobox_Expression").equals(jComboBoxField.getSelectedItem())) {
				SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
				int allItems = jComboBoxField.getItemCount();
				Dataset[] datasets = new Dataset[1];
				datasets[0] = datasetVector;
				ArrayList<FieldType> fieldTypes = new ArrayList<FieldType>();
				fieldTypes.add(FieldType.INT16);
				fieldTypes.add(FieldType.INT32);
				fieldTypes.add(FieldType.INT64);
				fieldTypes.add(FieldType.DOUBLE);
				fieldTypes.add(FieldType.SINGLE);

				DialogResult dialogResult = sqlDialog.showDialog(datasets, fieldTypes, themeRange.getRangeExpression());
				if (dialogResult == DialogResult.OK) {
					String filter = sqlDialog.getQueryParameter().getAttributeFilter();
					if (null != filter && !filter.isEmpty()) {
						jComboBoxField.insertItemAt(filter, allItems - 1);
						jComboBoxField.setSelectedIndex(allItems - 1);
					} else {
						jComboBoxField.setSelectedItem(themeRange.getRangeExpression());
					}
				} else {
					jComboBoxField.setSelectedItem(themeRange.getRangeExpression());
				}

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
				if (selectColumn == TABLE_COLUMN_RANGEVALUE && !StringUtilties.isNullOrEmptyString(tableRangeInfo.getValueAt(selectRow, selectColumn))) {
					String rangeValue = tableRangeInfo.getValueAt(selectRow, selectColumn).toString();
					if (StringUtilties.isNumber(rangeValue) && isRightRangeValue(rangeValue, selectRow)) {
						// 如果输入为数值且段值合法时修改段值
						setRangeValue(selectRow, rangeValue);
					}
				} else if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilties.isNullOrEmptyString(tableRangeInfo.getValueAt(selectRow, selectColumn))) {
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
			if (StringUtilties.isNumber(endString)) {
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
		comboBoxExpression.setSelectedItem(expression);
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
		if (rangeLength > 0) {
			ThemeRange theme = ThemeRange.makeDefault(datasetVector, rangeExpression, rangeMode, rangeLength, ColorGradientType.GREENRED, null, precision);
			if (null == theme || theme.getCount() == 0) {
				// 专题图为空，提示专题图更新失败
				JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_Theme_UpdataFailed"), CommonProperties.getString("String_Error"),
						JOptionPane.ERROR_MESSAGE);
				comboBoxExpression.setSelectedItem(themeRange.getRangeExpression());
				isResetComboBox = true;
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
			if (columnIndex == TABLE_COLUMN_RANGEVALUE || columnIndex == TABLE_COLUMN_CAPTION) {
				return true;
			}
			return false;
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
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.themeRangeLayer = MapUtilties.findLayerByName(map, layerName);
		if (null != themeRangeLayer && null != themeRangeLayer.getTheme()) {
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
			this.map.refresh();
		}
	}

	@Override
	public Layer getCurrentLayer() {
		return themeRangeLayer;
	}
}
