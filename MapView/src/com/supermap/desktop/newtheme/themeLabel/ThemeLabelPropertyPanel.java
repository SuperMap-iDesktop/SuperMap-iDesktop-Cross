package com.supermap.desktop.newtheme.themeLabel;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.ui.controls.SymbolDialog;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 标签专题图属性界面
 *
 * @author Administrator
 */
public class ThemeLabelPropertyPanel extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;

	private static final int LABELEXPRESSION_TYPE = 1;
	private static final int OFFSETX_TYPE = 2;
	private static final int OFFSETY_TYPE = 3;

	private JLabel labelLabelExpression = new JLabel();
	private JComboBox<String> comboBoxLabelExpression = new JComboBox<String>();
	// panelBackgroundSet
	private JLabel labelBGShape = new JLabel();
	private JComboBox<String> comboBoxBGShape = new JComboBox<String>();
	private JLabel labelBGSytle = new JLabel();
	private JButton buttonBGStyle = new JButton();
	// panelLabelOffset
	private JLabel labelOffsetUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetUnity = new JComboBox<String>();
	private JLabel labelOffsetX = new JLabel();
	private JLabel labelOffsetXUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetX = new JComboBox<String>();
	private JLabel labelOffsetY = new JLabel();
	private JLabel labelOffsetYUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetY = new JComboBox<String>();
	// panelLabelEffectSet
	private JCheckBox checkBoxFlowVisual = new JCheckBox();
	private JCheckBox checkBoxShowSubscription = new JCheckBox();
	private JLabel labelShowSubscription = new JLabel("?");
	private JCheckBox checkBoxShowSmallLabel = new JCheckBox();
	private JCheckBox checkBoxShowLabelVertical = new JCheckBox();
	private JLabel labelShowLabelVertical = new JLabel("?");
	private JCheckBox checkBoxAutoAvoidance = new JCheckBox();
	private JComboBox<String> comboBoxAutoAvoidance = new JComboBox<String>();
	private JCheckBox checkBoxDraftLine = new JCheckBox();
	private JButton buttonDraftLine = new JButton();
	private JLabel labelTextPrecision = new JLabel();
	private JComboBox<String> comboBoxTextPrecision = new JComboBox<String>();

	private transient DatasetVector datasetVector;
	private transient ThemeLabel themeLabel;
	private transient Map map;
	private transient SymbolType symbolType;
	private boolean isRefreshAtOnce = true;
	private ArrayList<String> comboBoxArray;
	private Layer themelabelLayer;
	private String layerName;
	private JoinItems joinItems;
	private transient LocalComboBoxItemListener itemListener = new LocalComboBoxItemListener();
	private transient LocalButtonActionListener actionListener = new LocalButtonActionListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();


	public ThemeLabelPropertyPanel(Layer themelabelLayer,JoinItems joinItems) {
		this.joinItems = joinItems;
		this.themelabelLayer = themelabelLayer;
		this.layerName = themelabelLayer.getName();
		this.datasetVector = (DatasetVector) themelabelLayer.getDataset();
		this.themeLabel = new ThemeLabel((ThemeLabel) themelabelLayer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initResources();
		initComponents();
		registActionListener();
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelLabelExpression.setText(MapViewProperties.getString("String_label_Expression"));
		this.labelBGShape.setText(MapViewProperties.getString("String_BackShape"));
		this.labelBGSytle.setText(MapViewProperties.getString("String_BackStyle"));
		this.labelOffsetUnity.setText(MapViewProperties.getString("String_LabelOffsetUnit"));
		this.labelOffsetX.setText(MapViewProperties.getString("String_LabelOffsetX"));
		this.labelOffsetY.setText(MapViewProperties.getString("String_LabelOffsetY"));
		this.checkBoxFlowVisual.setSelected(true);
		this.checkBoxFlowVisual.setText(MapViewProperties.getString("String_CheckBox_ShowFlow"));
		this.checkBoxShowSubscription.setText(MapViewProperties.getString("String_TextExpression"));
		this.labelShowSubscription.setToolTipText(MapViewProperties.getString("String_TextExpression_InformationText"));
		this.checkBoxShowSmallLabel.setSelected(true);
		this.checkBoxShowSmallLabel.setText(MapViewProperties.getString("String_SmallGeometry"));
		this.checkBoxShowLabelVertical.setText(MapViewProperties.getString("String_IsVertical"));
		this.labelShowLabelVertical.setToolTipText(MapViewProperties.getString("String_IsVertical_InformationText"));
		this.checkBoxAutoAvoidance.setSelected(true);
		this.checkBoxAutoAvoidance.setText(MapViewProperties.getString("String_CheckBox_AutoAvoid"));
		this.checkBoxDraftLine.setText(MapViewProperties.getString("String_ShowLeaderLine"));
		this.labelTextPrecision.setText(MapViewProperties.getString("String_Precision"));
		this.buttonBGStyle.setEnabled(false);
		this.buttonBGStyle.setText(ControlsProperties.getString("String_Button_Setting"));
		this.buttonDraftLine.setText(MapViewProperties.getString("String_Button_LineStyle"));
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		JPanel panelBGSet = new JPanel();
		panelBGSet
				.setBorder(new TitledBorder(null, MapViewProperties.getString("String_BackShapeSetting"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JPanel panelLabelOffset = new JPanel();
		panelLabelOffset
				.setBorder(new TitledBorder(null, MapViewProperties.getString("String_LabelOffset"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JPanel panelLabelEffectSet = new JPanel();
		panelLabelEffectSet.setBorder(new TitledBorder(null, MapViewProperties.getString("String_EffectOption"), TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
		initPanelBGSet(panelBGSet);
		initPanelLabelOffset(panelLabelOffset);
		initPanelLabelEffectSet(panelLabelEffectSet);
		this.comboBoxLabelExpression.setEditable(true);
		initComboBoxLabelExpression();
		getFieldComboBox(comboBoxLabelExpression, LABELEXPRESSION_TYPE);
		//@formatter:off
		JPanel panelPropertyContent = new JPanel();
		this.add(panelPropertyContent, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		panelPropertyContent.setLayout(new GridBagLayout());
		panelPropertyContent.add(this.labelLabelExpression,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(5, 10, 5, 0));
		panelPropertyContent.add(this.comboBoxLabelExpression, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(55, 0).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
		panelPropertyContent.add(panelBGSet,                   new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelPropertyContent.add(panelLabelOffset,             new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setWeight(1,0).setFill(GridBagConstraints.HORIZONTAL));
		panelPropertyContent.add(panelLabelEffectSet,          new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void initComboBoxLabelExpression() {
		String expression = themeLabel.getLabelExpression();
		if (StringUtilties.isNullOrEmpty(expression)) {
			expression = "0";
		}
		this.comboBoxLabelExpression.setSelectedItem(expression);
		if (this.comboBoxLabelExpression.getSelectedItem() != expression) {
			this.comboBoxLabelExpression.addItem(expression);
			this.comboBoxLabelExpression.setSelectedItem(expression);
		}
	}

	/**
	 * 背景设置布局入口
	 *
	 * @param panelBGSet
	 */
	private void initPanelBGSet(JPanel panelBGSet) {
		//@formatter:off
		panelBGSet.setLayout(new GridBagLayout());
		initComboBoxBackGround();
		panelBGSet.add(this.labelBGShape,    new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(5,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelBGSet.add(this.comboBoxBGShape, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(70, 0).setInsets(5,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelBGSet.add(this.labelBGSytle,    new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelBGSet.add(this.buttonBGStyle,   new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(70, 0).setInsets(5,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	/**
	 * 初始化背景形状下拉框
	 */
	private void initComboBoxBackGround() {
		this.comboBoxBGShape.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_ColorTable_Default"),
				MapViewProperties.getString("String_ThemeLabelBackShape_Rect"), MapViewProperties.getString("String_ThemeLabelBackShape_BoundRect"),
				MapViewProperties.getString("String_ThemeLabelBackShape_Ellipse"), MapViewProperties.getString("String_ThemeLabelBackShape_Diamond"),
				MapViewProperties.getString("String_ThemeLabelBackShape_Triangle"), MapViewProperties.getString("String_ThemeLabelBackShape_Marker") }));
		LabelBackShape labelBackShape = themeLabel.getBackShape();
		if (labelBackShape == LabelBackShape.NONE) {
			this.comboBoxBGShape.setSelectedIndex(0);
			this.buttonBGStyle.setEnabled(false);
		} else if (labelBackShape == LabelBackShape.RECT) {
			this.comboBoxBGShape.setSelectedIndex(1);
			this.buttonBGStyle.setEnabled(true);
		} else if (labelBackShape == LabelBackShape.ROUNDRECT) {
			this.comboBoxBGShape.setSelectedIndex(2);
			this.buttonBGStyle.setEnabled(true);
		} else if (labelBackShape == LabelBackShape.ELLIPSE) {
			this.comboBoxBGShape.setSelectedIndex(3);
			this.buttonBGStyle.setEnabled(true);
		} else if (labelBackShape == LabelBackShape.DIAMOND) {
			this.comboBoxBGShape.setSelectedIndex(4);
			this.buttonBGStyle.setEnabled(true);
		} else if (labelBackShape == LabelBackShape.TRIANGLE) {
			this.comboBoxBGShape.setSelectedIndex(5);
			this.buttonBGStyle.setEnabled(true);
		} else if (labelBackShape == LabelBackShape.MARKER) {
			this.comboBoxBGShape.setSelectedIndex(6);
			this.buttonBGStyle.setEnabled(true);
		}
		if (labelBackShape != LabelBackShape.MARKER) {
			symbolType = SymbolType.FILL;
		} else {
			symbolType = symbolType.MARKER;
		}
	}

	/**
	 * 偏移量设置布局入口
	 *
	 * @param panelLabelOffset
	 */
	private void initPanelLabelOffset(JPanel panelLabelOffset) {
		//@formatter:off
		panelLabelOffset.setLayout(new GridBagLayout());
		initComboBoxUnity();
		initComboBoxOffsetX();
		initComboBoxOffsetY();
		this.comboBoxOffsetUnity.setPreferredSize(new Dimension(200,23));
		Dimension textDimension = new Dimension(160,23);
		this.comboBoxOffsetX.setPreferredSize(textDimension);
		this.comboBoxOffsetY.setPreferredSize(textDimension);
		panelLabelOffset.add(this.labelOffsetUnity,    new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(5,10,5,0));
		panelLabelOffset.add(this.comboBoxOffsetUnity, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(5,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelOffset.add(this.labelOffsetX,        new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(0,10,5,0));
		this.comboBoxOffsetX.setEditable(true);
		panelLabelOffset.add(this.comboBoxOffsetX,     new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelOffset.add(this.labelOffsetXUnity,   new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(0,10,5,10));
		panelLabelOffset.add(this.labelOffsetY,        new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(0,10,5,0));
		this.comboBoxOffsetY.setEditable(true);
		panelLabelOffset.add(this.comboBoxOffsetY,     new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelOffset.add(this.labelOffsetYUnity,   new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(0,10,5,10));
		//@formatter:on
	}

	/**
	 * 初始化偏移量单位下拉框
	 */
	private void initComboBoxUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[] {
				MapViewProperties.getString("String_ThemeLabelOffsetUnit_Millimeter"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map") }));
		if (this.themeLabel.isOffsetFixed()) {
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
		getFieldComboBox(this.comboBoxOffsetX, OFFSETX_TYPE);
		this.comboBoxOffsetX.addItem("0");
		String offsetX = themeLabel.getOffsetX();
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
		getFieldComboBox(this.comboBoxOffsetY, OFFSETY_TYPE);
		this.comboBoxOffsetY.addItem("0");
		String offsetY = themeLabel.getOffsetY();
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
	 * 效果设置布局入口
	 *
	 * @param panelLabelEffectSet
	 */
	private void initPanelLabelEffectSet(JPanel panelLabelEffectSet) {
		//@formatter:off
		initCheckboxState();
		initComboboxAutoAvoidance();
		initComboBoxTextPrecision();
		panelLabelEffectSet.setLayout(new GridBagLayout());
		panelLabelEffectSet.add(this.checkBoxFlowVisual,       new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(5,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.checkBoxShowSubscription, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 0).setInsets(5,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.labelShowSubscription,    new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,10));
		panelLabelEffectSet.add(this.checkBoxShowSmallLabel,   new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.checkBoxShowLabelVertical,new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.labelShowLabelVertical,   new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,10));
		panelLabelEffectSet.add(this.checkBoxAutoAvoidance,    new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.comboBoxAutoAvoidance,    new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.checkBoxDraftLine,        new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.buttonDraftLine,          new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelLabelEffectSet.add(this.labelTextPrecision,       new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelLabelEffectSet.add(this.comboBoxTextPrecision,    new GridBagConstraintsHelper(2, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void initCheckboxState() {
		// 如果数据集类型为点，不允许流动显示
		if (datasetVector.getType() == DatasetType.POINT || datasetVector.getType() == DatasetType.POINT3D) {
			this.checkBoxFlowVisual.setEnabled(false);
		} else {
			this.checkBoxFlowVisual.setEnabled(true);
			this.checkBoxFlowVisual.setSelected(themeLabel.isFlowEnabled());
		}
		this.checkBoxShowSubscription.setSelected(themeLabel.isTextExpression());
		this.checkBoxAutoAvoidance.setSelected(themeLabel.isOverlapAvoided());
		this.checkBoxShowSmallLabel.setSelected(themeLabel.isSmallGeometryLabeled());
		this.checkBoxShowLabelVertical.setSelected(themeLabel.isVertical());
		this.checkBoxDraftLine.setSelected(themeLabel.isLeaderLineDisplayed());
		this.comboBoxAutoAvoidance.setEnabled(themeLabel.isOverlapAvoided());
		this.buttonDraftLine.setEnabled(themeLabel.isLeaderLineDisplayed());
	}

	/**
	 * 初始化文本避让下拉框
	 */
	private void initComboboxAutoAvoidance() {
		this.comboBoxAutoAvoidance.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_AllDirectionsAvoided"),
				MapViewProperties.getString("String_TwoDirectionsAvoided") }));
		boolean isAutoAvoidance = themeLabel.isAllDirectionsOverlappedAvoided();
		if (isAutoAvoidance) {
			this.comboBoxAutoAvoidance.setSelectedIndex(1);
		} else {
			this.comboBoxAutoAvoidance.setSelectedIndex(0);
		}
	}

	/**
	 * 初始化数值文本精度下拉框
	 */
	private void initComboBoxTextPrecision() {
		this.comboBoxTextPrecision.setModel(new DefaultComboBoxModel<String>(new String[] { "1", "0.1", "0.01", "0.001", "0.0001", "0.00001", "0.000001",
				"0.0000001", "0.00000001", "0.000000001", "0.0000000001" }));
		this.comboBoxTextPrecision.setSelectedItem(themeLabel.getNumericPrecision());
	}

	/**
	 * 表达式
	 *
	 * @return m_fieldComboBox
	 */
	private JComboBox<String> getFieldComboBox(JComboBox<String> comboBox, int type) {
		JoinItems joinItems = this.themelabelLayer.getDisplayFilter().getJoinItems();
		int itemsCount = joinItems.getCount();
		int count = datasetVector.getFieldCount();
		this.comboBoxArray = new ArrayList<String>();
		if (type == LABELEXPRESSION_TYPE) {
			for (int j = 0; j < count; j++) {
				FieldInfo tempFieldInfo = datasetVector.getFieldInfos().get(j);
				String item = tempFieldInfo.getName();
				comboBox.addItem(item);
				comboBoxArray.add(item);
			}
			for (int i = 0; i < itemsCount; i++) {
				if (datasetVector.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable()) instanceof DatasetVector) {
					DatasetVector dataset = (DatasetVector) datasetVector.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable());
					int datasetFieldCount = dataset.getFieldCount();
					for (int j = 0; j < datasetFieldCount; j++) {
						String datasetItem = dataset.getFieldInfos().get(j).getName();
						comboBox.addItem(dataset.getName() + "." + datasetItem);
						comboBoxArray.add(dataset.getName() + "." + datasetItem);
					}
				}
			}
		} else {
			for (int j = 0; j < count; j++) {
				FieldInfo fieldInfo = datasetVector.getFieldInfos().get(j);
				if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.INT64
						|| fieldInfo.getType() == FieldType.DOUBLE || fieldInfo.getType() == FieldType.SINGLE) {
					String item = fieldInfo.getName();
					comboBox.addItem(item);
					comboBoxArray.add(item);
				}
			}
			for (int i = 0; i < itemsCount; i++) {
				if (datasetVector.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable()) instanceof DatasetVector) {
					DatasetVector tempDataset = (DatasetVector) datasetVector.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable());
					int tempDatasetFieldCount = tempDataset.getFieldCount();
					for (int j = 0; j < tempDatasetFieldCount; j++) {
						FieldInfo tempfieldInfo = tempDataset.getFieldInfos().get(j);
						if (tempfieldInfo.getType() == FieldType.INT16 || tempfieldInfo.getType() == FieldType.INT32
								|| tempfieldInfo.getType() == FieldType.INT64 || tempfieldInfo.getType() == FieldType.DOUBLE
								|| tempfieldInfo.getType() == FieldType.SINGLE) {
							String tempDatasetItem = tempDataset.getName() + "." + tempDataset.getFieldInfos().get(j).getName();
							comboBox.addItem(tempDatasetItem);
							comboBoxArray.add(tempDatasetItem);
						}
					}
				}
			}
		}
		comboBox.addItem(MapViewProperties.getString("String_Combobox_Expression"));
		return comboBox;
	}

	/**
	 * 注册事件
	 */
	public void registActionListener() {
		unregistActionListener();
		this.comboBoxLabelExpression.addItemListener(this.itemListener);
		this.comboBoxBGShape.addItemListener(this.itemListener);
		this.comboBoxOffsetUnity.addItemListener(this.itemListener);
		this.comboBoxOffsetX.addItemListener(this.itemListener);
		this.comboBoxOffsetY.addItemListener(this.itemListener);
		this.comboBoxAutoAvoidance.addItemListener(this.itemListener);
		this.comboBoxTextPrecision.addItemListener(this.itemListener);
		this.buttonBGStyle.addActionListener(this.actionListener);
		this.buttonDraftLine.addActionListener(this.actionListener);
		this.checkBoxAutoAvoidance.addActionListener(this.actionListener);
		this.checkBoxDraftLine.addActionListener(this.actionListener);
		this.checkBoxFlowVisual.addActionListener(this.actionListener);
		this.checkBoxShowLabelVertical.addActionListener(this.actionListener);
		this.checkBoxShowSmallLabel.addActionListener(this.actionListener);
		this.checkBoxShowSubscription.addActionListener(this.actionListener);
		this.comboBoxTextPrecision.getEditor().getEditorComponent().addKeyListener(this.localKeyListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.comboBoxLabelExpression.removeItemListener(this.itemListener);
		this.comboBoxBGShape.removeItemListener(this.itemListener);
		this.comboBoxOffsetUnity.removeItemListener(this.itemListener);
		this.comboBoxOffsetX.removeItemListener(this.itemListener);
		this.comboBoxOffsetY.removeItemListener(this.itemListener);
		this.comboBoxAutoAvoidance.removeItemListener(this.itemListener);
		this.comboBoxTextPrecision.removeItemListener(this.itemListener);
		this.buttonBGStyle.removeActionListener(this.actionListener);
		this.buttonDraftLine.removeActionListener(this.actionListener);
		this.checkBoxAutoAvoidance.removeActionListener(this.actionListener);
		this.checkBoxDraftLine.removeActionListener(this.actionListener);
		this.checkBoxFlowVisual.removeActionListener(this.actionListener);
		this.checkBoxShowLabelVertical.removeActionListener(this.actionListener);
		this.checkBoxShowSmallLabel.removeActionListener(this.actionListener);
		this.checkBoxShowSubscription.removeActionListener(this.actionListener);
		this.comboBoxTextPrecision.getEditor().getEditorComponent().removeKeyListener(this.localKeyListener);
	}

	/**
	 * 获取表达式项
	 *
	 * @param jComboBoxField
	 */
	private void getSqlExpression(JComboBox<String> jComboBoxField, int type) {
		// 判断是否为“表达式”项
		if (MapViewProperties.getString("String_Combobox_Expression").equals(jComboBoxField.getSelectedItem())) {
			SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
			int allItems = jComboBoxField.getItemCount();
			Dataset[] datasets = new Dataset[1];
			datasets[0] = datasetVector;
			DialogResult dialogResult = null;
			ArrayList<FieldType> fieldTypes = new ArrayList<FieldType>();
			fieldTypes.add(FieldType.INT16);
			fieldTypes.add(FieldType.INT32);
			fieldTypes.add(FieldType.INT64);
			fieldTypes.add(FieldType.DOUBLE);
			fieldTypes.add(FieldType.SINGLE);
			if (type == LABELEXPRESSION_TYPE) {
				dialogResult = sqlDialog.showDialog(themeLabel.getLabelExpression(), datasets);
			} else if (type == OFFSETX_TYPE) {
				dialogResult = sqlDialog.showDialog(datasets, fieldTypes, themeLabel.getOffsetX());
			} else {
				dialogResult = sqlDialog.showDialog(datasets, fieldTypes, themeLabel.getOffsetY());
			}
			if (null != dialogResult && dialogResult == DialogResult.OK) {
				String filter = sqlDialog.getQueryParameter().getAttributeFilter();
				if (filter != null && !filter.isEmpty()) {
					jComboBoxField.insertItemAt(filter, allItems - 1);
					jComboBoxField.setSelectedIndex(allItems - 1);
				} else {
					resetComboBoxSelectItem(type, jComboBoxField);
				}
			} else {
				resetComboBoxSelectItem(type, jComboBoxField);
			}

		}

	}

	private void resetComboBoxSelectItem(int type, JComboBox<String> jComboBoxField) {
		if (type == LABELEXPRESSION_TYPE) {
			jComboBoxField.setSelectedItem(themeLabel.getLabelExpression());
		} else if (type == OFFSETX_TYPE) {
			if (!StringUtilties.isNullOrEmpty(themeLabel.getOffsetX())) {
				jComboBoxField.setSelectedItem(themeLabel.getOffsetX());
			} else {
				jComboBoxField.setSelectedItem(0);
			}
		} else {
			if (!StringUtilties.isNullOrEmpty(themeLabel.getOffsetY())) {
				jComboBoxField.setSelectedItem(themeLabel.getOffsetY());
			} else {
				jComboBoxField.setSelectedItem(0);
			}
		}
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	class LocalComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxLabelExpression) {
					// sql表达式
					getSqlExpression(comboBoxLabelExpression, LABELEXPRESSION_TYPE);
					// 设置标签表达式
					setFieldInfo();
				} else if (e.getSource() == comboBoxBGShape) {
					// 设置背景形状
					setBackgroundShap();
				} else if (e.getSource() == comboBoxOffsetUnity) {
					// 设置偏移量单位
					setOffsetUnity();
				} else if (e.getSource() == comboBoxOffsetX) {
					// 设置水平偏移量
					getSqlExpression(comboBoxOffsetX, OFFSETX_TYPE);
					setOffsetX();
				} else if (e.getSource() == comboBoxOffsetY) {
					// 设置垂直偏移量
					getSqlExpression(comboBoxOffsetY, OFFSETY_TYPE);
					setOffsetY();
				} else if (e.getSource() == comboBoxAutoAvoidance) {
					// 设置文本自动避让方式
					setAutoAvoidanceStyle();
				} else if (e.getSource() == comboBoxTextPrecision) {
					// 设置数值文本精度
					setTextPrecision();
				}
				refreshAtOnce();
			}
		}

		/**
		 * 设置数值文本精度
		 */
		private void setTextPrecision() {
			int textPrecision = comboBoxTextPrecision.getSelectedIndex();
			themeLabel.setNumericPrecision(textPrecision);
		}

		/**
		 * 设置文本自动避让方式
		 */
		private void setAutoAvoidanceStyle() {
			themeLabel.setOverlapAvoided(true);
			int index = comboBoxAutoAvoidance.getSelectedIndex();
			if (0 == index) {
				themeLabel.setAllDirectionsOverlappedAvoided(false);
			} else {
				themeLabel.setAllDirectionsOverlappedAvoided(true);
			}
		}

		/**
		 * 设置垂直偏移量
		 */
		private void setOffsetY() {
			String expression = comboBoxOffsetY.getSelectedItem().toString();
			themeLabel.setOffsetY(expression);
		}

		/**
		 * 设置水平偏移量
		 */
		private void setOffsetX() {
			String expression = comboBoxOffsetX.getSelectedItem().toString();
			themeLabel.setOffsetX(expression);
		}

		/**
		 * 设置偏移量单位
		 */
		private void setOffsetUnity() {
			String offsetUnity = comboBoxOffsetUnity.getSelectedItem().toString();
			if (MapViewProperties.getString("String_ThemeLabelOffsetUnit_Millimeter").equals(offsetUnity)) {
				labelOffsetXUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				labelOffsetYUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				themeLabel.setOffsetFixed(true);
			} else {
				themeLabel.setOffsetFixed(false);
				labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
				labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
			}
		}

		/**
		 * 设置背景形状
		 */
		private void setBackgroundShap() {
			String backgroudShap = comboBoxBGShape.getSelectedItem().toString();
			if (MapViewProperties.getString("String_ColorTable_Default").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(false);
				themeLabel.setBackShape(LabelBackShape.NONE);
				symbolType = SymbolType.FILL;
			} else if (MapViewProperties.getString("String_ThemeLabelBackShape_Rect").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(true);
				themeLabel.setBackShape(LabelBackShape.RECT);
				symbolType = SymbolType.FILL;
			} else if (MapViewProperties.getString("String_ThemeLabelBackShape_BoundRect").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(true);
				themeLabel.setBackShape(LabelBackShape.ROUNDRECT);
				symbolType = SymbolType.FILL;
			} else if (MapViewProperties.getString("String_ThemeLabelBackShape_Ellipse").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(true);
				themeLabel.setBackShape(LabelBackShape.ELLIPSE);
				symbolType = SymbolType.FILL;
			} else if (MapViewProperties.getString("String_ThemeLabelBackShape_Diamond").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(true);
				themeLabel.setBackShape(LabelBackShape.DIAMOND);
				symbolType = SymbolType.FILL;
			} else if (MapViewProperties.getString("String_ThemeLabelBackShape_Triangle").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(true);
				themeLabel.setBackShape(LabelBackShape.TRIANGLE);
				symbolType = SymbolType.FILL;
			} else if (MapViewProperties.getString("String_ThemeLabelBackShape_Marker").equals(backgroudShap)) {
				buttonBGStyle.setEnabled(true);
				themeLabel.setBackShape(LabelBackShape.MARKER);
				symbolType = SymbolType.MARKER;
			}
		}

		/**
		 * 设置标签表达式
		 */
		private void setFieldInfo() {
			String labelExpression = comboBoxLabelExpression.getSelectedItem().toString();
			if (comboBoxArray.contains(labelExpression)) {
				DatasetVector dataset = datasetVector;
				if (labelExpression.contains(".")) {
					String datasetName = labelExpression.substring(0, labelExpression.indexOf("."));
					dataset = (DatasetVector) datasetVector.getDatasource().getDatasets().get(datasetName);
					labelExpression = labelExpression.substring(labelExpression.indexOf(".") + 1, labelExpression.length());
				}
				FieldInfo fieldInfo = dataset.getFieldInfos().get(labelExpression);
				// 控制数值文本精度下拉框的可使用性
				if (fieldInfo.getType() == FieldType.DOUBLE || fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32
						|| fieldInfo.getType() == FieldType.INT64 || fieldInfo.getType() == FieldType.LONGBINARY) {
					comboBoxTextPrecision.setEnabled(true);
				} else {
					comboBoxTextPrecision.setEnabled(false);
				}
			}
			themeLabel.setLabelExpression(labelExpression);
		}
	}

	class LocalKeyListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent e) {
			// 输入限制
			int keyChar = e.getKeyChar();
			if ((keyChar < '0' || keyChar > '9') && keyChar != '.' && keyChar != '-') {
				e.consume();
			}

		}

	}

	class LocalButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonBGStyle) {
				// 设置背景风格
				setBackgroundStyle();
			} else if (e.getSource() == buttonDraftLine) {
				// 设置标签与其标注对象之间牵引线的风格
				setLineStyle();
			} else if (e.getSource() == checkBoxAutoAvoidance) {
				// 设置文本是否自动避让
				setAutoAvoidance();
			} else if (e.getSource() == checkBoxDraftLine) {
				// 设置显示牵引线
				setDraftLine();
			} else if (e.getSource() == checkBoxFlowVisual) {
				// 设置流动显示
				setFlowVisual();
			} else if (e.getSource() == checkBoxShowLabelVertical) {
				// 设置是否竖直显示标签
				setShowLabelVertical();
			} else if (e.getSource() == checkBoxShowSmallLabel) {
				// 设置是否显示小对象标签
				setShowSmallLabel();
			} else if (e.getSource() == checkBoxShowSubscription) {
				// 设置是否显示上下标
				setShowTextExpression();
			}
			refreshAtOnce();
		}

		private void setShowTextExpression() {
			boolean isShowTextExpression = checkBoxShowSubscription.isSelected();
			themeLabel.setTextExpression(isShowTextExpression);
		}

		/**
		 * 设置是否显示小对象标签
		 */
		private void setShowSmallLabel() {
			boolean isShowSmallLabel = checkBoxShowSmallLabel.isSelected();
			themeLabel.setSmallGeometryLabeled(isShowSmallLabel);
		}

		/**
		 * 设置是否竖直显示标签
		 */
		private void setShowLabelVertical() {
			boolean isShowLabelVertical = checkBoxShowLabelVertical.isSelected();
			themeLabel.setVertical(isShowLabelVertical);
		}

		/**
		 * 设置流动显示
		 */
		private void setFlowVisual() {
			boolean isFlowVisual = checkBoxFlowVisual.isSelected();
			themeLabel.setFlowEnabled(isFlowVisual);
		}

		/**
		 * 设置显示牵引线
		 */
		private void setDraftLine() {
			boolean isDraftLine = checkBoxDraftLine.isSelected();
			themeLabel.setLeaderLineDisplayed(isDraftLine);
			buttonDraftLine.setEnabled(isDraftLine);

		}

		/**
		 * 设置文本是否自动避让
		 */
		private void setAutoAvoidance() {
			boolean isAvoidance = checkBoxAutoAvoidance.isSelected();
			themeLabel.setOverlapAvoided(isAvoidance);
			comboBoxAutoAvoidance.setEnabled(isAvoidance);

		}

		/**
		 * 设置标签与其标注对象之间牵引线的风格
		 */
		private void setLineStyle() {
			symbolType = SymbolType.LINE;
			SymbolDialog textStyleDialog = new SymbolDialog();
			int width = buttonDraftLine.getWidth();
			int height = buttonDraftLine.getHeight();
			int x = buttonDraftLine.getLocationOnScreen().x + width;
			int y = buttonDraftLine.getLocationOnScreen().y - height;
			textStyleDialog.setLocation(x, y);
			Resources resources = Application.getActiveApplication().getWorkspace().getResources();
			GeoStyle geoStyle = new GeoStyle();
			DialogResult dialogResult = textStyleDialog.showDialog(resources, geoStyle, symbolType);
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getStyle();
				themeLabel.setLeaderLineStyle(nowGeoStyle);
			}
		}

		/**
		 * 设置背景风格
		 */
		private void setBackgroundStyle() {
			SymbolDialog textStyleDialog = new SymbolDialog();
			int width = buttonBGStyle.getWidth();
			int height = buttonBGStyle.getHeight();
			int x = buttonBGStyle.getLocationOnScreen().x + width;
			int y = buttonBGStyle.getLocationOnScreen().y - height;
			textStyleDialog.setLocation(x, y);
			Resources resources = Application.getActiveApplication().getWorkspace().getResources();
			GeoStyle geoStyle = themeLabel.getBackStyle();
			DialogResult dialogResult = textStyleDialog.showDialog(resources, geoStyle, symbolType);
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getStyle();
				themeLabel.setBackStyle(nowGeoStyle);
			}
		}

	}

	public boolean isRefreshAtOnce() {
		return isRefreshAtOnce;
	}

	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	public Theme getCurrentTheme() {
		return themeLabel;
	}

	@Override
	public void refreshMapAndLayer() {
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.themelabelLayer = MapUtilties.findLayerByName(this.map, layerName);
		ThemeLabel themeLabelTemp = (ThemeLabel) this.themelabelLayer.getTheme();
		themeLabelTemp.setLabelExpression(this.themeLabel.getLabelExpression());
		themeLabelTemp.setBackShape(this.themeLabel.getBackShape());
		themeLabelTemp.setBackStyle(this.themeLabel.getBackStyle());
		themeLabelTemp.setOffsetFixed(this.themeLabel.isOffsetFixed());
		themeLabelTemp.setOffsetX(this.themeLabel.getOffsetX());
		themeLabelTemp.setOffsetY(this.themeLabel.getOffsetY());
		themeLabelTemp.setFlowEnabled(this.themeLabel.isFlowEnabled());
		themeLabelTemp.setTextExpression(this.themeLabel.isTextExpression());
		themeLabelTemp.setSmallGeometryLabeled(this.themeLabel.isSmallGeometryLabeled());
		themeLabelTemp.setVertical(this.themeLabel.isVertical());
		themeLabelTemp.setOverlapAvoided(this.themeLabel.isOverlapAvoided());
		themeLabelTemp.setAllDirectionsOverlappedAvoided(this.themeLabel.isAllDirectionsOverlappedAvoided());
		themeLabelTemp.setLeaderLineDisplayed(this.themeLabel.isLeaderLineDisplayed());
		themeLabelTemp.setLeaderLineStyle(this.themeLabel.getLeaderLineStyle());
		themeLabelTemp.setNumericPrecision(this.themeLabel.getNumericPrecision());
		this.map.refresh();
	}

	@Override
	public Layer getCurrentLayer() {
		return themelabelLayer;
	}

}
