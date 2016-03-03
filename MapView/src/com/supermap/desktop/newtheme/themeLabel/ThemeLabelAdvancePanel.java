package com.supermap.desktop.newtheme.themeLabel;

import com.supermap.data.DatasetType;
import com.supermap.data.Size2D;
import com.supermap.data.StringAlignment;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class ThemeLabelAdvancePanel extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	// panelAdvanced
	private JCheckBox checkBoxRotateLabel = new JCheckBox();
	// panelRotateLabel
	private JCheckBox checkBoxFixedFontAngl = new JCheckBox();
	private JCheckBox checkBoxRemoveRepeatLabel = new JCheckBox();
	private JLabel labelLineDirection = new JLabel();
	private JComboBox<String> comboBoxLineDirection = new JComboBox<String>();
	private JLabel labelFontSpace = new JLabel();
	private JSpinner spinnerFontSpace = new JSpinner();
	private JLabel labelRepeatInterval = new JLabel();
	private JTextField textFieldRepeatInterval = new JTextField();
	private JLabel labelUnity = new JLabel();
	private JCheckBox checkBoxRepeatIntervalFixed = new JCheckBox();
	// panelTextFontSet
	private JLabel labelOverLength = new JLabel();
	private JComboBox<String> comboBoxOverLength = new JComboBox<String>();
	private JLabel labelFontCount = new JLabel();
	private JSpinner spinnerFontCount = new JSpinner();
	private JLabel labelAlignmentStyle = new JLabel();
	private JComboBox<String> comboBoxAlignmentStyle = new JComboBox<String>();
	// panelFontHeight
	private JLabel labelMaxFontHeight = new JLabel();
	private JTextField textFieldMaxFontHeight = new JTextField();
	private JLabel labelMaxFontHeightUnity = new JLabel();
	private JLabel labelMinFontHeight = new JLabel();
	private JTextField textFieldMinFontHeight = new JTextField();
	private JLabel labelMinFontHeightUnity = new JLabel();
	// panelFontWide
	private JLabel labelMaxFontWidth = new JLabel();
	private JTextField textFieldMaxFontWidth = new JTextField();
	private JLabel labelMaxFontWidthUnity = new JLabel();
	private JLabel labelMinFontWidth = new JLabel();
	private JTextField textFieldMinFontWidth = new JTextField();
	private JLabel labelMinFontWidthUnity = new JLabel();
	// panelTextExtentInflation
	private JLabel labelHorizontal = new JLabel();
	private JTextField textFieldHorizontal = new JTextField();
	private JLabel labelHorizontalUnity = new JLabel();
	private JLabel labelVertical = new JLabel();
	private JTextField textFieldVertical = new JTextField();
	private JLabel labelVerticalUnity = new JLabel();
	private Dimension textFieldDimension = new Dimension(400, 20);
	private Dimension labelDimension = new Dimension(30, 20);
	private transient Map map;
	private transient ThemeLabel themeLabel;
	private transient LocalItemChangedListener itemListener = new LocalItemChangedListener();
	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalChangedListener changedListener = new LocalChangedListener();
	private boolean isRefreshAtOnce = true;
	private Layer themeLabelLayer;

	public ThemeLabelAdvancePanel(Layer themelabelLayer) {
		this.themeLabelLayer = themelabelLayer;
		this.themeLabel = new ThemeLabel((ThemeLabel) themelabelLayer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
	}

	private void initComponents() {
		JPanel panelRotateLabel = new JPanel();
		panelRotateLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		JPanel panelTextFontSet = new JPanel();
		panelTextFontSet
				.setBorder(new TitledBorder(null, MapViewProperties.getString("String_CharLimited"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JPanel panelFontHeight = new JPanel();
		panelFontHeight.setBorder(new TitledBorder(null, MapViewProperties.getString("String_LimitedHeight"), TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
		JPanel panelFontWide = new JPanel();
		panelFontWide.setBorder(new TitledBorder(null, MapViewProperties.getString("String_WidthLimited"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JPanel panelTextExtentInflation = new JPanel();
		panelTextExtentInflation.setBorder(new TitledBorder(null, MapViewProperties.getString("String_TextExtentInflation"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		initPanelRotateLabel(panelRotateLabel);
		initPanelTextFontSet(panelTextFontSet);
		initPanelFontHeight(panelFontHeight);
		initPanelFontWidth(panelFontWide);
		initPanelTextExtentInflation(panelTextExtentInflation);
		//@formatter:off
		this.setLayout(new GridBagLayout());
		this.spinnerFontCount.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		this.spinnerFontSpace.setModel(new SpinnerNumberModel(1.0, 1.0, 8.0, 1.0));
		JPanel panelAdvanceContent = new JPanel();
		this.add(panelAdvanceContent, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		panelAdvanceContent.setLayout(new GridBagLayout());
		initCheckBoxState();
		initTextFieldAndSpinnerValue();
		initCheckBoxRotateLabel();
		panelAdvanceContent.add(this.checkBoxRotateLabel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelAdvanceContent.add(panelRotateLabel,         new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(panelTextFontSet,         new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(panelFontHeight,          new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(panelFontWide,            new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(panelTextExtentInflation, new GridBagConstraintsHelper(0, 5, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	// 初始化沿线标注复选框
	private void initCheckBoxRotateLabel() {
		if (themeLabelLayer.getDataset().getType() == DatasetType.LINE || themeLabelLayer.getDataset().getType() == DatasetType.NETWORK) {
			this.checkBoxRotateLabel.setEnabled(true);
			resetCheckBoxState(true);
		} else {
			this.checkBoxRotateLabel.setEnabled(false);
			resetCheckBoxState(false);
		}
	}

	/**
	 * 初始化checkbox的状态
	 */
	private void initCheckBoxState() {
		this.checkBoxRotateLabel.setSelected(themeLabel.isAlongLine());
		this.checkBoxFixedFontAngl.setSelected(themeLabel.isAngleFixed());
		this.checkBoxRemoveRepeatLabel.setSelected(themeLabel.isRepeatedLabelAvoided());
		this.checkBoxRepeatIntervalFixed.setSelected(themeLabel.isRepeatIntervalFixed());
		boolean isRepeatIntervalFixed = this.checkBoxRepeatIntervalFixed.isSelected();
		if (!isRepeatIntervalFixed) {
			labelUnity.setText(map.getCoordUnit().toString());
		} else {
			labelUnity.setText("0.1mm");
		}
		this.comboBoxLineDirection.setEnabled(themeLabel.isAngleFixed());
	}

	private void initTextFieldAndSpinnerValue() {
		Size2D size2d = themeLabel.getTextExtentInflation();
		this.textFieldHorizontal.setText(String.valueOf(size2d.getWidth()));
		this.textFieldVertical.setText(String.valueOf(size2d.getHeight()));
		this.textFieldMaxFontHeight.setText(String.valueOf(themeLabel.getMaxTextHeight()));
		this.textFieldMaxFontWidth.setText(String.valueOf(themeLabel.getMaxTextWidth()));
		this.textFieldMinFontHeight.setText(String.valueOf(themeLabel.getMinTextHeight()));
		this.textFieldMinFontWidth.setText(String.valueOf(themeLabel.getMinTextWidth()));
		this.textFieldRepeatInterval.setText(String.valueOf(themeLabel.getLabelRepeatInterval()));
		this.spinnerFontCount.setValue(themeLabel.getMaxLabelLength());
		this.spinnerFontSpace.setValue(themeLabel.getAlongLineSpaceRatio());
	}

	/**
	 * 注册事件
	 */
	public void registActionListener() {
		unregistActionListener();
		this.comboBoxLineDirection.addItemListener(this.itemListener);
		this.comboBoxOverLength.addItemListener(this.itemListener);
		this.checkBoxRotateLabel.addActionListener(this.actionListener);
		this.checkBoxFixedFontAngl.addActionListener(this.actionListener);
		this.checkBoxRemoveRepeatLabel.addActionListener(this.actionListener);
		this.checkBoxRepeatIntervalFixed.addActionListener(this.actionListener);
		this.textFieldHorizontal.addKeyListener(this.localKeyListener);
		this.textFieldRepeatInterval.addKeyListener(this.localKeyListener);
		this.textFieldVertical.addKeyListener(this.localKeyListener);
		this.textFieldMaxFontHeight.addKeyListener(this.localKeyListener);
		this.textFieldMaxFontWidth.addKeyListener(this.localKeyListener);
		this.textFieldMinFontHeight.addKeyListener(this.localKeyListener);
		this.textFieldMinFontWidth.addKeyListener(this.localKeyListener);
		this.spinnerFontCount.addChangeListener(this.changedListener);
		this.spinnerFontSpace.addChangeListener(this.changedListener);
		this.spinnerFontSpace.getEditor().addKeyListener(this.localKeyListener);
		this.comboBoxAlignmentStyle.addItemListener(this.itemListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.comboBoxLineDirection.removeItemListener(this.itemListener);
		this.comboBoxOverLength.removeItemListener(this.itemListener);
		this.checkBoxRotateLabel.removeActionListener(this.actionListener);
		this.checkBoxFixedFontAngl.removeActionListener(this.actionListener);
		this.checkBoxRemoveRepeatLabel.removeActionListener(this.actionListener);
		this.checkBoxRepeatIntervalFixed.removeActionListener(this.actionListener);
		this.textFieldHorizontal.removeKeyListener(this.localKeyListener);
		this.textFieldRepeatInterval.removeKeyListener(this.localKeyListener);
		this.textFieldVertical.removeKeyListener(this.localKeyListener);
		this.textFieldMaxFontHeight.removeKeyListener(this.localKeyListener);
		this.textFieldMaxFontWidth.removeKeyListener(this.localKeyListener);
		this.textFieldMinFontHeight.removeKeyListener(this.localKeyListener);
		this.textFieldMinFontWidth.removeKeyListener(this.localKeyListener);
		this.spinnerFontCount.removeChangeListener(this.changedListener);
		this.spinnerFontSpace.removeChangeListener(this.changedListener);
		this.spinnerFontSpace.getEditor().removeKeyListener(this.localKeyListener);
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.checkBoxRotateLabel.setText(MapViewProperties.getString("String_AlongLine"));
		this.checkBoxFixedFontAngl.setText(MapViewProperties.getString("String_CheckBox_IsTextAngleFixed"));
		this.checkBoxRemoveRepeatLabel.setText(MapViewProperties.getString("String_RemoveRepeat"));
		this.labelLineDirection.setText(MapViewProperties.getString("String_LineDirection"));
		this.labelFontSpace.setText(MapViewProperties.getString("String_SpaceRatio"));
		this.labelRepeatInterval.setText(MapViewProperties.getString("String_RepeatInterval"));
		this.checkBoxRepeatIntervalFixed.setText(MapViewProperties.getString("String_RepeatIntervalFixed"));

		this.labelOverLength.setText(MapViewProperties.getString("String_OverLengthLabelMode"));
		this.labelFontCount.setText(MapViewProperties.getString("String_CharCount"));

		this.labelMaxFontHeight.setText(MapViewProperties.getString("String_MaxHeight"));
		this.labelMaxFontHeightUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
		this.labelMinFontHeight.setText(MapViewProperties.getString("String_MinHeight"));
		this.labelMinFontHeightUnity.setText(MapViewProperties.getString("String_Combobox_MM"));

		this.labelMaxFontWidth.setText(MapViewProperties.getString("String_MaxWidth"));
		this.labelMaxFontWidthUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
		this.labelMinFontWidth.setText(MapViewProperties.getString("String_MinHeight"));
		this.labelMinFontWidthUnity.setText(MapViewProperties.getString("String_Combobox_MM"));

		this.labelHorizontal.setText(MapViewProperties.getString("String_TextExtentWidth"));
		this.labelHorizontalUnity.setText(String.valueOf(map.getCoordUnit()));
		this.labelVertical.setText(MapViewProperties.getString("String_TextExtentHeight"));
		this.labelVerticalUnity.setText(String.valueOf(map.getCoordUnit()));
		this.labelAlignmentStyle.setText(MapViewProperties.getString("String_TextAlignment"));
	}

	private void resetCheckBoxState(boolean isRotate) {
		this.checkBoxFixedFontAngl.setEnabled(isRotate);
		this.checkBoxRemoveRepeatLabel.setEnabled(isRotate);
		this.comboBoxLineDirection.setEnabled(isRotate);
		this.spinnerFontSpace.setEnabled(isRotate);
		this.textFieldRepeatInterval.setEnabled(isRotate);
		this.checkBoxRepeatIntervalFixed.setEnabled(isRotate);
		this.themeLabel.setAlongLine(isRotate);
		this.labelLineDirection.setEnabled(isRotate);
		this.labelFontSpace.setEnabled(isRotate);
		this.labelRepeatInterval.setEnabled(isRotate);
		this.labelUnity.setEnabled(isRotate);
	}

	/**
	 * 本文高度限制界面布局
	 *
	 * @param panelFontHeight
	 */
	private void initPanelFontHeight(JPanel panelFontHeight) {
		initTextFieldMaxFontHeight();
		initTextFieldMinFontHeight();

		//@formatter:off
		panelFontHeight.setLayout(new GridBagLayout());
		this.textFieldMaxFontHeight.setPreferredSize(textFieldDimension);
		this.textFieldMinFontHeight.setPreferredSize(textFieldDimension);
		this.labelMaxFontHeightUnity.setPreferredSize(labelDimension);
		this.labelMinFontHeightUnity.setPreferredSize(labelDimension);
		panelFontHeight.add(this.labelMaxFontHeight,     new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelFontHeight.add(this.textFieldMaxFontHeight, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelFontHeight.add(this.labelMaxFontHeightUnity,new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,10,2,0));
		panelFontHeight.add(this.labelMinFontHeight,     new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelFontHeight.add(this.textFieldMinFontHeight, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelFontHeight.add(this.labelMinFontHeightUnity,new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,10,2,0));
		//@formatter:on
	}

	/**
	 * 初始化最大文本高度
	 */
	private void initTextFieldMaxFontHeight() {
		if (themeLabel.getMaxTextHeight() > 0) {
			this.textFieldMaxFontHeight.setText(String.valueOf(themeLabel.getMaxTextHeight()));
		} else {
			this.textFieldMaxFontHeight.setText("0");
		}
	}

	/**
	 * 初始化最小文本高度
	 */
	private void initTextFieldMinFontHeight() {
		if (themeLabel.getMaxTextHeight() > 0) {
			this.textFieldMinFontHeight.setText(String.valueOf(themeLabel.getMinTextHeight()));
		} else {
			this.textFieldMinFontHeight.setText("0");
		}
	}

	/**
	 * 文本避让的缓冲范围界面布局
	 * 
	 * @param panelTextExtentInflation
	 */
	private void initPanelTextExtentInflation(JPanel panelTextExtentInflation) {
		//@formatter:off
		panelTextExtentInflation.setLayout(new GridBagLayout());
		this.textFieldHorizontal.setPreferredSize(textFieldDimension);
		this.textFieldVertical.setPreferredSize(textFieldDimension);
		this.labelHorizontalUnity.setPreferredSize(labelDimension);
		this.labelVerticalUnity.setPreferredSize(labelDimension);
		panelTextExtentInflation.add(this.labelHorizontal,        new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelTextExtentInflation.add(this.textFieldHorizontal,    new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelTextExtentInflation.add(this.labelHorizontalUnity,   new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,10,2,0));
		panelTextExtentInflation.add(this.labelVertical,          new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelTextExtentInflation.add(this.textFieldVertical,      new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelTextExtentInflation.add(this.labelVerticalUnity,     new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,10,2,0));
		//@formatter:on
	}

	/**
	 * 文本宽度限制界面布局
	 *
	 * @param panelFontWidth
	 */
	private void initPanelFontWidth(JPanel panelFontWidth) {
		//@formatter:off
		panelFontWidth.setLayout(new GridBagLayout());
		this.textFieldMaxFontWidth.setPreferredSize(textFieldDimension);
		this.textFieldMinFontWidth.setPreferredSize(textFieldDimension);
		this.labelMaxFontWidthUnity.setPreferredSize(labelDimension);
		this.labelMinFontWidthUnity.setPreferredSize(labelDimension);
		panelFontWidth.add(this.labelMaxFontWidth,          new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelFontWidth.add(this.textFieldMaxFontWidth,      new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelFontWidth.add(this.labelMaxFontWidthUnity,     new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,10,2,0));
		panelFontWidth.add(this.labelMinFontWidth,          new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelFontWidth.add(this.textFieldMinFontWidth,      new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelFontWidth.add(this.labelMinFontWidthUnity,     new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,10,2,0));
		//@formatter:on
	}

	private void initComboBoxLineDirection() {
		this.comboBoxLineDirection.setModel(new DefaultComboBoxModel<String>(new String[] {
				MapViewProperties.getString("String_AlongLineDirection_AlongLineNormal"),
				MapViewProperties.getString("String_AlongLineDirection_LeftTopToRightBottom"),
				MapViewProperties.getString("String_AlongLineDirection_RightTopToLeftBottom"),
				MapViewProperties.getString("String_AlongLineDirection_LeftBottomToRightTop"),
				MapViewProperties.getString("String_AlongLineDirection_RightBottomToLeftTop") }));
		this.comboBoxLineDirection.setEditable(true);
		if (themeLabel.getAlongLineDirection() == AlongLineDirection.ALONG_LINE_NORMAL) {
			this.comboBoxLineDirection.setSelectedIndex(0);
		} else if (themeLabel.getAlongLineDirection() == AlongLineDirection.LEFT_TOP_TO_RIGHT_BOTTOM) {
			this.comboBoxLineDirection.setSelectedIndex(1);
		} else if (themeLabel.getAlongLineDirection() == AlongLineDirection.RIGHT_TOP_TO_LEFT_BOTTOM) {
			this.comboBoxLineDirection.setSelectedIndex(2);
		} else if (themeLabel.getAlongLineDirection() == AlongLineDirection.LEFT_BOTTOM_TO_RIGHT_TOP) {
			this.comboBoxLineDirection.setSelectedIndex(3);
		} else {
			this.comboBoxLineDirection.setSelectedIndex(4);
		}
	}

	/**
	 * 沿线标注界面布局
	 * 
	 * @param panelRotateLabel
	 */
	private void initPanelRotateLabel(JPanel panelRotateLabel) {
		//@formatter:off
		panelRotateLabel.setLayout(new GridBagLayout());
		this.checkBoxFixedFontAngl.setSelected(true);
		this.textFieldRepeatInterval.setPreferredSize(textFieldDimension);
		initComboBoxLineDirection();
		this.textFieldRepeatInterval.setText("0");
		this.labelUnity.setText(map.getCoordUnit().toString());
		this.labelUnity.setPreferredSize(labelDimension);
		panelRotateLabel.add(this.checkBoxFixedFontAngl,      new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,10));
		panelRotateLabel.add(this.checkBoxRemoveRepeatLabel,  new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10));
		panelRotateLabel.add(this.labelLineDirection,         new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,10));
		panelRotateLabel.add(this.comboBoxLineDirection,      new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelRotateLabel.add(this.labelFontSpace,             new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,10));
		panelRotateLabel.add(this.spinnerFontSpace,           new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelRotateLabel.add(this.labelRepeatInterval,        new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,10));
		panelRotateLabel.add(this.textFieldRepeatInterval,    new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelRotateLabel.add(this.labelUnity,                 new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(2,0,2,0));
		panelRotateLabel.add(this.checkBoxRepeatIntervalFixed,new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 0).setInsets(2,10,2,10));
		//@formatter:on
	}

	/**
	 * 初始化超长处理方式下拉框
	 */
	private void initComboBoxOverLength() {
		this.comboBoxOverLength.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_Default"),
				MapViewProperties.getString("String_OverLengthLabelMode_NewLine"), MapViewProperties.getString("String_OverLengthLabelMode_Omit") }));
		if (themeLabel.getOverLengthMode() == OverLengthLabelMode.NONE) {
			this.comboBoxOverLength.setSelectedIndex(0);
			this.spinnerFontCount.setEnabled(false);
			this.comboBoxAlignmentStyle.setEnabled(false);
		} else if (themeLabel.getOverLengthMode() == OverLengthLabelMode.NEWLINE) {
			this.comboBoxOverLength.setSelectedIndex(1);
			this.spinnerFontCount.setEnabled(true);
			this.comboBoxAlignmentStyle.setEnabled(true);
		} else if (themeLabel.getOverLengthMode() == OverLengthLabelMode.OMIT) {
			this.comboBoxOverLength.setSelectedIndex(2);
			this.spinnerFontCount.setEnabled(true);
			this.comboBoxAlignmentStyle.setEnabled(true);
		}
	}

	/**
	 * 文本字符设置界面布局
	 * 
	 * @param panelTextFontSet
	 */
	private void initPanelTextFontSet(JPanel panelTextFontSet) {
		//@formatter:off
		initComboBoxOverLength();
		initComboBoxAlignmentStyle();
		panelTextFontSet.setLayout(new GridBagLayout());
		this.comboBoxOverLength.setPreferredSize(this.textFieldDimension);
		this.spinnerFontCount.setPreferredSize(this.textFieldDimension);
		this.comboBoxAlignmentStyle.setPreferredSize(this.textFieldDimension);
		panelTextFontSet.add(this.labelOverLength,          new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelTextFontSet.add(this.comboBoxOverLength,       new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelTextFontSet.add(this.labelFontCount,           new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,0).setFill(GridBagConstraints.HORIZONTAL));
		panelTextFontSet.add(this.spinnerFontCount,         new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelTextFontSet.add(this.labelAlignmentStyle,      new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		panelTextFontSet.add(this.comboBoxAlignmentStyle,   new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(2,10,2,10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void initComboBoxAlignmentStyle() {
		this.comboBoxAlignmentStyle.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_AlignLeft"),
				MapViewProperties.getString("String_AlignCenter"), MapViewProperties.getString("String_AlignRight"),
				MapViewProperties.getString("String_AlignDistributed") }));
		this.comboBoxAlignmentStyle.setEnabled(false);
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	class LocalItemChangedListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxLineDirection) {
					// 设置沿线显示方向
					setLineDirection();
					refreshAtOnce();
					return;
				}
				if (e.getSource() == comboBoxOverLength) {
					// 设置超长处理方式
					setOverLength();
					refreshAtOnce();
					return;
				}
				if (e.getSource() == comboBoxAlignmentStyle) {
					setTextAlignment();
					refreshAtOnce();
					return;
				}
			}
		}

		private void setTextAlignment() {
			int alignmentStyle = comboBoxAlignmentStyle.getSelectedIndex();
			TextStyle textStyle = themeLabel.getUniformStyle();
			switch (alignmentStyle) {
			case 0:
				textStyle.setStringAlignment(StringAlignment.LEFT);
				break;
			case 1:
				textStyle.setStringAlignment(StringAlignment.CENTER);
				break;
			case 2:
				textStyle.setStringAlignment(StringAlignment.RIGHT);
				break;
			case 3:
				textStyle.setStringAlignment(StringAlignment.DISTRIBUTED);
				break;
			default:
				break;
			}
		}

		/**
		 * 设置超长处理方式
		 */
		private void setOverLength() {
			int overLength = comboBoxOverLength.getSelectedIndex();
			if (0 == overLength) {
				themeLabel.setOverLengthMode(OverLengthLabelMode.NONE);
				spinnerFontCount.setEnabled(false);
			} else if (1 == overLength) {
				themeLabel.setOverLengthMode(OverLengthLabelMode.NEWLINE);
				spinnerFontCount.setEnabled(true);
				comboBoxAlignmentStyle.setEnabled(true);
			} else {
				themeLabel.setOverLengthMode(OverLengthLabelMode.OMIT);
				spinnerFontCount.setEnabled(true);
				comboBoxAlignmentStyle.setEnabled(true);
			}
		}

		/**
		 * 设置沿线显示方向
		 */
		private void setLineDirection() {
			themeLabel.setAngleFixed(false);
			int lineDirection = comboBoxLineDirection.getSelectedIndex();
			switch (lineDirection) {
			case 0:
				themeLabel.setAlongLineDirection(AlongLineDirection.ALONG_LINE_NORMAL);
				break;
			case 1:
				themeLabel.setAlongLineDirection(AlongLineDirection.LEFT_TOP_TO_RIGHT_BOTTOM);
				break;
			case 2:
				themeLabel.setAlongLineDirection(AlongLineDirection.RIGHT_TOP_TO_LEFT_BOTTOM);
				break;
			case 3:
				themeLabel.setAlongLineDirection(AlongLineDirection.LEFT_BOTTOM_TO_RIGHT_TOP);
				break;
			case 4:
				themeLabel.setAlongLineDirection(AlongLineDirection.RIGHT_BOTTOM_TO_LEFT_TOP);
				break;
			default:
				break;
			}
		}

	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == checkBoxFixedFontAngl) {
				// 固定文本角度
				setFixedFontAngl();
			} else if (e.getSource() == checkBoxRemoveRepeatLabel) {
				// 去除重复线
				setRemoveRepeatLabel();
			} else if (e.getSource() == checkBoxRepeatIntervalFixed) {
				// 设置固定循环标注间隔
				setRepeatIntervalFixed();
			} else if (e.getSource() == checkBoxRotateLabel) {
				// 设置沿线标注项可设置
				setRotateLabel();
			}
			refreshAtOnce();
		}

		/**
		 * 设置固定循环标注间隔
		 */
		private void setRepeatIntervalFixed() {
			boolean isRepeatIntervalFixed = checkBoxRepeatIntervalFixed.isSelected();
			themeLabel.setRepeatIntervalFixed(isRepeatIntervalFixed);
			if (!isRepeatIntervalFixed) {
				labelUnity.setText(map.getCoordUnit().toString());
			} else {
				labelUnity.setText("0.1mm");
			}
		}

		/**
		 * 去除重复线
		 */
		private void setRemoveRepeatLabel() {
			boolean isRemoveRepeat = checkBoxRemoveRepeatLabel.isSelected();
			themeLabel.setRepeatedLabelAvoided(isRemoveRepeat);
		}

		/**
		 * 固定文本角度
		 */
		private void setFixedFontAngl() {
			boolean isFixedFontAngl = checkBoxFixedFontAngl.isSelected();
			themeLabel.setAngleFixed(isFixedFontAngl);
			comboBoxLineDirection.setEnabled(isFixedFontAngl);
		}

		/**
		 * 设置沿线标注项可设置
		 */
		private void setRotateLabel() {
			boolean isRotate = checkBoxRotateLabel.isSelected();
			resetCheckBoxState(isRotate);
		}

	}

	class LocalKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == textFieldRepeatInterval) {
				// 设置沿线周期间距
				setTextRepeatInterval();
			} else if (e.getSource() == textFieldHorizontal) {
				// 设置水平方向上文本避让的缓冲范围
				setExtentInflation();
			} else if (e.getSource() == textFieldVertical) {
				// 设置垂直方向上文本避让的缓冲范围
				setExtentInflation();
			} else if (e.getSource() == textFieldMaxFontHeight) {
				// 设置最大文高度
				setMaxFontHeight();
			} else if (e.getSource() == textFieldMinFontHeight) {
				// 设置最小文本高度
				setMinFontHeight();
			} else if (e.getSource() == textFieldMaxFontWidth) {
				// 设置最大文本宽度
				setMaxFontWidth();
			} else if (e.getSource() == textFieldMinFontWidth) {
				// 设置最小文本宽度
				setMinFontWidth();
			}
			refreshAtOnce();
		}

		/**
		 * 设置最小文本宽度
		 */
		private void setMinFontWidth() {
			if (!textFieldMinFontWidth.getText().isEmpty()) {
				String minFontWidth = textFieldMinFontWidth.getText();
				if (StringUtilties.isNumber(minFontWidth) && minFontWidth.length() <= 8) {
					int minTextWidth = Integer.parseInt(minFontWidth);
					themeLabel.setMinTextWidth(minTextWidth);
				}
			} else {
				textFieldMinFontWidth.setText("0");
				themeLabel.setMinTextWidth(0);
			}
		}

		/**
		 * 设置最大文本宽度
		 */
		private void setMaxFontWidth() {
			if (!textFieldMaxFontWidth.getText().isEmpty()) {
				String maxFontWidth = textFieldMaxFontWidth.getText();
				if (StringUtilties.isNumber(maxFontWidth) && maxFontWidth.length() <= 8) {
					int maxTextWidth = Integer.parseInt(maxFontWidth);
					themeLabel.setMaxTextWidth(maxTextWidth);
				}
			} else {
				textFieldMaxFontWidth.setText("0");
				themeLabel.setMaxTextWidth(0);
			}
		}

		/**
		 * 设置最小文本高度
		 */
		private void setMinFontHeight() {
			if (!textFieldMinFontHeight.getText().isEmpty()) {
				String minFontHeight = textFieldMinFontHeight.getText();
				if (StringUtilties.isNumber(minFontHeight) && minFontHeight.length() <= 8) {
					int minTextHeight = Integer.parseInt(minFontHeight);
					themeLabel.setMinTextHeight(minTextHeight);
				}
			} else {
				textFieldMinFontHeight.setText("0");
				themeLabel.setMinTextHeight(0);
			}
		}

		/**
		 * 设置最大文高度
		 */
		private void setMaxFontHeight() {
			if (!textFieldMaxFontHeight.getText().isEmpty()) {
				String maxFontHeight = textFieldMaxFontHeight.getText();
				if (StringUtilties.isNumber(maxFontHeight) && maxFontHeight.length() <= 8) {
					int maxTextHeight = Integer.parseInt(maxFontHeight);
					themeLabel.setMaxTextHeight(maxTextHeight);
				}
			} else {
				textFieldMaxFontHeight.setText("0");
				themeLabel.setMaxTextHeight(0);
			}
		}

		/**
		 * 设置水平方向上文本避让的缓冲范围
		 */
		private void setExtentInflation() {
			if (!textFieldHorizontal.getText().isEmpty() && !textFieldVertical.getText().isEmpty()) {
				String horizontal = textFieldHorizontal.getText();
				String vertical = textFieldVertical.getText();
				if (StringUtilties.isNumber(vertical) && StringUtilties.isNumber(vertical)) {
					double textHorizontal = Double.parseDouble(horizontal);
					double textVertical = Double.parseDouble(vertical);
					Size2D size2d = new Size2D();
					size2d.setWidth(textHorizontal);
					size2d.setHeight(textVertical);
					themeLabel.setTextExtentInflation(size2d);
				}
			} else if (textFieldHorizontal.getText().isEmpty()) {
				textFieldHorizontal.setText("0");
				Size2D size2d = new Size2D();
				size2d.setWidth(0);
				String vertical = textFieldVertical.getText();
				if (StringUtilties.isNumber(vertical)) {
					double textVertical = Double.parseDouble(vertical);
					size2d.setHeight(textVertical);
					themeLabel.setTextExtentInflation(size2d);
				}
			} else if (textFieldVertical.getText().isEmpty()) {
				textFieldVertical.setText("0");
				Size2D size2d = new Size2D();
				size2d.setHeight(0);
				String horizontal = textFieldHorizontal.getText();
				if (StringUtilties.isNumber(horizontal)) {
					double textHorizontal = Double.parseDouble(horizontal);
					size2d.setWidth(textHorizontal);
					themeLabel.setTextExtentInflation(size2d);
				}
			}
		}

		/**
		 * 设置沿线周期间距
		 */
		private void setTextRepeatInterval() {
			if (!textFieldRepeatInterval.getText().isEmpty()) {
				String lineSpaceRatio = textFieldRepeatInterval.getText();
				if (!StringUtilties.isNullOrEmpty(lineSpaceRatio) && StringUtilties.isNumber(lineSpaceRatio)) {
					double labelRepeatIntervalTemp = Double.parseDouble(lineSpaceRatio);
					if (labelRepeatIntervalTemp - 100000.0 > 0) {
						themeLabel.setLabelRepeatInterval(0.0);
					} else {
						themeLabel.setLabelRepeatInterval(labelRepeatIntervalTemp);
					}
				} else {
					textFieldRepeatInterval.setText("0");
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// 输入限制
			int keyChar = e.getKeyChar();
			if (e.getSource() == textFieldRepeatInterval || e.getSource() == textFieldHorizontal || e.getSource() == textFieldVertical) {
				if (keyChar != '.' && (keyChar < '0' || keyChar > '9')) {
					e.consume();
				}
			} else {
				if (keyChar < '0' || keyChar > '9') {
					e.consume();
				}
			}
		}
	}

	class LocalChangedListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == spinnerFontCount) {
				// 设置单行文本字数
				setFontCount();
			} else if (e.getSource() == spinnerFontSpace) {
				// 设置沿线字间距
				setFontSpace();
			}
			refreshAtOnce();
		}

		/**
		 * 设置沿线字间距
		 */
		private void setFontSpace() {
			double fontSpace = (double) spinnerFontSpace.getValue();
			themeLabel.setAlongLineSpaceRatio(fontSpace);
		}

		/**
		 * 设置单行文本字数
		 */
		private void setFontCount() {
			int maxLabelLength = (int) spinnerFontCount.getValue();
			themeLabel.setMaxLabelLength(maxLabelLength);
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
		ThemeLabel themeLabelTemp = (ThemeLabel) this.themeLabelLayer.getTheme();
		themeLabelTemp.setAngleFixed(this.themeLabel.isAngleFixed());
		themeLabelTemp.setRepeatedLabelAvoided(this.themeLabel.isRepeatedLabelAvoided());
		themeLabelTemp.setAlongLineDirection(this.themeLabel.getAlongLineDirection());
		themeLabelTemp.setAlongLineSpaceRatio(this.themeLabel.getAlongLineSpaceRatio());
		themeLabelTemp.setLabelRepeatInterval(this.themeLabel.getLabelRepeatInterval());
		themeLabelTemp.setRepeatIntervalFixed(this.themeLabel.isRepeatIntervalFixed());
		themeLabelTemp.setOverLengthMode(this.themeLabel.getOverLengthMode());
		themeLabelTemp.setMaxLabelLength(this.themeLabel.getMaxLabelLength());
		themeLabelTemp.setMaxTextHeight(this.themeLabel.getMaxTextHeight());
		themeLabelTemp.setMinTextHeight(this.themeLabel.getMinTextHeight());
		themeLabelTemp.setMaxTextWidth(this.themeLabel.getMaxTextWidth());
		themeLabelTemp.setMinTextWidth(this.themeLabel.getMinTextWidth());
		themeLabelTemp.setTextExtentInflation(this.themeLabel.getTextExtentInflation());
		this.map.refresh();
	}

	@Override
	public Layer getCurrentLayer() {
		return themeLabelLayer;
	}
}
