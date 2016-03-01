package com.supermap.desktop.newtheme;

import com.supermap.data.Enum;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ColorSelectButton;
import com.supermap.desktop.ui.controls.FontComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.FontUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapDrawnEvent;
import com.supermap.mapping.MapDrawnListener;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeGraph;
import com.supermap.mapping.ThemeLabel;

import javax.swing.*;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class TextStyleContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelFontName = new JLabel();
	private transient FontComboBox comboBoxFontName = new FontComboBox();
	private JLabel labelAlign = new JLabel();
	private JComboBox<String> comboBoxAlign = new JComboBox<String>();
	private JLabel labelFontSize = new JLabel();
	private JComboBox<String> comboBoxFontSize = new JComboBox<String>();
	private JLabel labelFontHeight = new JLabel();
	private JLabel labelFontHeightUnity = new JLabel();
	private JSpinner spinnerFontHeight = new JSpinner();
	private JLabel labelFontWidth = new JLabel();
	private JLabel labelFontWidthUnity = new JLabel();
	private JSpinner spinnerFontWidth = new JSpinner();
	private JLabel labelRotationAngl = new JLabel();
	private JSpinner spinnerRotationAngl = new JSpinner();
	private JLabel labelInclinationAngl = new JLabel();
	private JSpinner spinnerInclinationAngl = new JSpinner();
	private JLabel labelFontColor = new JLabel();

	// 经验值
	private final static double EXPERIENCE = 0.283;
	// 单位转换
	private final static int UNIT_CONVERSION = 10;

	private transient ColorSelectButton buttonFontColorSelect;
	private JLabel labelBGColor = new JLabel();
	private transient ColorSelectButton buttonBGColorSelect;
	// panelFontEffect
	private JCheckBox checkBoxBorder = new JCheckBox();
	private JCheckBox checkBoxStrickout = new JCheckBox();
	private JCheckBox checkBoxItalic = new JCheckBox();
	private JCheckBox checkBoxUnderline = new JCheckBox();
	private JCheckBox checkBoxShadow = new JCheckBox();
	private JCheckBox checkBoxFixedSize = new JCheckBox();
	private JCheckBox checkBoxOutlook = new JCheckBox();
	private JCheckBox checkBoxBGTransparent = new JCheckBox();
	private JSlider sliderOutLineWidth;

	private transient Map map;
	private transient TextStyle textStyle;
	private transient Layer themeLayer;

	// 对齐方式名称和对齐方式值构成的HashMap
	private static HashMap<String, Integer> hashMapTextAlignment = new HashMap<String, Integer>();
	private JTextField textFieldFontSize;
	private JTextField textFieldFontHeight;
	private JTextField textFieldFontWidth;
	private JTextField textFieldFontItalicAngl;
	private JTextField textFieldFontRotationAngl;

	// 对齐方式名称
	private static final String[] TEXTALIGNMENT_NAMES = { ControlsProperties.getString("String_TextAlignment_LeftTop"),
			ControlsProperties.getString("String_TextAlignment_MidTop"), ControlsProperties.getString("String_TextAlignment_RightTop"),
			ControlsProperties.getString("String_TextAlignment_LeftBaseline"), ControlsProperties.getString("String_TextAlignment_MidBaseline"),
			ControlsProperties.getString("String_TextAlignment_RightBaseline"), ControlsProperties.getString("String_TextAlignment_LeftBottom"),
			ControlsProperties.getString("String_TextAlignment_MidBottom"), ControlsProperties.getString("String_TextAlignment_RightBottom"),
			ControlsProperties.getString("String_TextAlignment_LeftMid"), ControlsProperties.getString("String_TextAlignment_Mid"),
			ControlsProperties.getString("String_TextAlignment_RightMid"), };
	// 字号与字高之间的转换精度
	// 显示精度
	private String numeric = "0.00";
	private boolean isRefreshAtOnce = true;
	private boolean isUniformStyle = false;
	private int[] selectRow;
	private int textStyleType = -1;
	public final int graphTextFormat = 0;
	public final int graphAxisText = 1;

	private transient LocalItemListener itemListener = new LocalItemListener();
	private transient LocalChangedListener changedListener = new LocalChangedListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalCheckBoxActionListener checkBoxActionListener = new LocalCheckBoxActionListener();
	private transient LocalPropertyListener propertyListener = new LocalPropertyListener();
	private transient LocalMapDrawnListener mapDrawnListener = new LocalMapDrawnListener();
	private ChangeListener outLineWidthChangeListener = new OutLineChangeListener();

	public TextStyleContainer(TextStyle textStyle, Map map, Layer themeLabelLayer) {
		this.textStyle = textStyle;
		this.map = map;
		this.themeLayer = themeLabelLayer;
		initComponent();
		initResources();
		registActionListener();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public TextStyleContainer(ThemeLabel themeLabel, int[] selectRow, Map map, Layer themeLabelLayer) {
		this.selectRow = selectRow;
		this.map = map;
		this.themeLayer = themeLabelLayer;
		this.textStyle = themeLabel.getItem(selectRow[selectRow.length - 1]).getStyle();
		initComponent();
		initResources();
		registActionListener();
	}

	/**
	 * 资源化
	 */
	private void initResources() {

		this.labelFontName.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontName"));
		this.labelAlign.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelAlinement"));
		this.labelFontSize.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontSize"));
		this.labelFontHeight.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontHeight"));
		this.labelFontWidth.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontWidth"));
		this.labelRotationAngl.setText(ControlsProperties.getString("String_Label_SymbolAngle"));
		this.labelInclinationAngl.setText(ControlsProperties.getString("String_Label_ItalicAngle"));
		this.labelFontColor.setText(ControlsProperties.getString("String_Label_TextStyleForeColor"));
		this.labelBGColor.setText(ControlsProperties.getString("String_BackgroundColor") + ":");

		this.checkBoxBorder.setText(ControlsProperties.getString("String_OverStriking"));
		this.checkBoxStrickout.setText(ControlsProperties.getString("String_DeleteLine"));
		this.checkBoxItalic.setText(ControlsProperties.getString("String_Italic"));
		this.checkBoxUnderline.setText(ControlsProperties.getString("String_Underline"));
		this.checkBoxShadow.setText(ControlsProperties.getString("String_Shadow"));

		this.checkBoxFixedSize.setText(ControlsProperties.getString("String_FixedSize"));
		this.checkBoxOutlook.setText(ControlsProperties.getString("String_Contour"));
		this.checkBoxBGTransparent.setText(ControlsProperties.getString("String_BackgroundTransparency"));
		this.labelFontHeightUnity.setText("0.1mm");
		this.labelFontWidthUnity.setText("0.1mm");
	}

	/**
	 * 初始化风格界面布局
	 */
	private void initComponent() {
		this.setLayout(new GridBagLayout());
		JPanel panelSytleContent = new JPanel();
		this.add(
				panelSytleContent,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL)
						.setInsets(5, 10, 5, 10));
		panelSytleContent.setLayout(new GridBagLayout());
		this.buttonFontColorSelect = new ColorSelectButton(this.textStyle.getForeColor());
		this.buttonBGColorSelect = new ColorSelectButton(this.textStyle.getBackColor());
		this.comboBoxFontName.setSelectedItem(this.textStyle.getFontName());
		initComboBoxAlign();
		initComboBoxFontSize();
		initTextFieldFontWidth();
		initTextFieldFontItalicAngl();
		initTextFieldFontRotation();
		//@formatter:off
		panelSytleContent.add(this.labelFontName,          new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(10,10,5,0));
		panelSytleContent.add(this.comboBoxFontName,       new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(10,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelAlign,             new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.comboBoxAlign,          new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelFontSize,          new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.comboBoxFontSize,       new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelFontHeight,        new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.spinnerFontHeight,      new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelFontHeightUnity,   new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0, 0,5,10));
		panelSytleContent.add(this.labelFontWidth,         new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.spinnerFontWidth,       new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelFontWidthUnity,    new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,0,5,10));
		panelSytleContent.add(this.labelRotationAngl,      new GridBagConstraintsHelper(0, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.spinnerRotationAngl,    new GridBagConstraintsHelper(2, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelInclinationAngl,   new GridBagConstraintsHelper(0, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.spinnerInclinationAngl, new GridBagConstraintsHelper(2, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelFontColor,         new GridBagConstraintsHelper(0, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.buttonFontColorSelect,  new GridBagConstraintsHelper(2, 7, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelSytleContent.add(this.labelBGColor,           new GridBagConstraintsHelper(0, 8, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 0).setInsets(0,10,5,0));
		panelSytleContent.add(this.buttonBGColorSelect,    new GridBagConstraintsHelper(2, 8, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 0).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		JPanel panelFontEffect = new JPanel();
		panelFontEffect.setBorder(new TitledBorder(null, ControlsProperties.getString("String_GeometryPropertyTextControl_GroupBoxFontEffect"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		initPanelFontEffect(panelFontEffect);
		panelSytleContent.add(panelFontEffect,             new GridBagConstraintsHelper(0, 9, 4, 1).setAnchor(GridBagConstraints.CENTER).setWeight(2, 0).setInsets(5).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	/**
	 * 初始化倾斜角度左侧textField值
	 */
	private void initTextFieldFontRotation() {
		this.spinnerRotationAngl.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerRotationAngl.getEditor();
		this.textFieldFontRotationAngl = numberEditor.getTextField();
		if (null != this.textStyle) {
			this.textFieldFontRotationAngl.setText(new DecimalFormat(numeric).format(textStyle.getRotation()));
		}
	}

	/**
	 * 初始化旋转角度左侧textField值
	 */
	private void initTextFieldFontItalicAngl() {
		this.spinnerInclinationAngl.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		this.spinnerInclinationAngl.setEnabled(false);
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerInclinationAngl.getEditor();
		this.textFieldFontItalicAngl = numberEditor.getTextField();
		if (null != this.textStyle) {
			this.textFieldFontItalicAngl.setText(new DecimalFormat(numeric).format(textStyle.getItalicAngle()));
		}
	}

	/**
	 * 初始化字宽左侧textField值
	 */
	private void initTextFieldFontWidth() {
		this.spinnerFontWidth.setModel(new SpinnerNumberModel(new Double(0.0), null, null, new Double(1.0)));
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerFontWidth.getEditor();
		this.textFieldFontWidth = numberEditor.getTextField();
		if (null != this.textStyle) {
			this.textFieldFontWidth.setText(new DecimalFormat(numeric).format(textStyle.getFontWidth()));
		}
	}

	/**
	 * 初始化字号下拉框和字高
	 */
	private void initComboBoxFontSize() {
		this.comboBoxFontSize.setModel(new DefaultComboBoxModel<String>(new String[] { "1", "2", "3", "4", "5", "5.5", "6.5", "7.5", "8", "9", "10", "11",
				"12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" }));
		this.comboBoxFontSize.setEditable(true);
		this.textFieldFontSize = (JTextField) this.comboBoxFontSize.getEditor().getEditorComponent();
		this.spinnerFontHeight.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerFontHeight.getEditor();
		this.textFieldFontHeight = numberEditor.getTextField();
		if (null != this.textStyle) {
			Double size = FontUtilties.mapHeightToFontSize(textStyle.getFontHeight(), map, textStyle.isSizeFixed());
			DecimalFormat decimalFormat = new DecimalFormat("0.0");
			String textFieldString = "";
			if (Double.compare(size, size.intValue()) > 0) {
				textFieldString = decimalFormat.format(size);
				this.textFieldFontSize.setText(textFieldString);
			} else {
				decimalFormat = new DecimalFormat("0");
				textFieldString = decimalFormat.format(size);
				this.textFieldFontSize.setText(textFieldString);
			}
			double height = Double.parseDouble(textFieldString);
			this.textFieldFontHeight.setText(new DecimalFormat(numeric).format(height / EXPERIENCE));
		}
	}

	/**
	 * 初始化文本对齐方式下拉框
	 */
	private void initComboBoxAlign() {
		this.comboBoxAlign.setModel(new DefaultComboBoxModel<String>(TEXTALIGNMENT_NAMES));
		initHashMapTextAlignment();
		Object[] textAlignmentValues = hashMapTextAlignment.values().toArray();
		Object[] textAlignmentNames = hashMapTextAlignment.keySet().toArray();
		String alignMent = "";
		if (null != this.textStyle) {
			for (int i = 0; i < textAlignmentValues.length; i++) {
				Integer temp = (Integer) textAlignmentValues[i];
				if (temp == this.textStyle.getAlignment().value()) {
					alignMent = (String) textAlignmentNames[i];
				}
			}
			this.comboBoxAlign.setSelectedItem(alignMent);
		}
	}

	/**
	 * 字体效果界面布局
	 *
	 * @param panelFontEffect
	 */
	private void initPanelFontEffect(JPanel panelFontEffect) {
		//@formatter:off
		initCheckBoxState();
		JPanel panelSlider = new JPanel();
		initSliderOutLineWidth(panelSlider);
		panelFontEffect.setLayout(new GridBagLayout());
		panelFontEffect.add(this.checkBoxBorder,         new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxStrickout,      new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxItalic,         new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxUnderline,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxShadow,         new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxFixedSize,      new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxBGTransparent,  new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelFontEffect.add(this.checkBoxOutlook,        new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,0,10));
		panelFontEffect.add(panelSlider,                 new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(0,10,2,10));
		//@formatter:on
	}

	private void initSliderOutLineWidth(JPanel panelSlider) {
		this.sliderOutLineWidth = new JSlider(0, 4, this.textStyle.getOutlineWidth() - 1);
		this.sliderOutLineWidth.setPaintTicks(true);// 显示标尺
		this.sliderOutLineWidth.setPaintLabels(true);
		this.sliderOutLineWidth.setMajorTickSpacing(1);// 1一大格
		this.sliderOutLineWidth.setMinorTickSpacing(1);// 1一小格

		Dictionary<Integer, Component> labelTable = new Hashtable<Integer, Component>();
		labelTable.put(0, new JLabel("1"));
		labelTable.put(1, new JLabel("2"));
		labelTable.put(2, new JLabel("3"));
		labelTable.put(3, new JLabel("4"));
		labelTable.put(4, new JLabel("5"));
		panelSlider.add(this.sliderOutLineWidth);
		panelSlider.add(new JLabel(CommonProperties.getString("String_Label_Pixel")));
		this.sliderOutLineWidth.setLabelTable(labelTable);
		this.sliderOutLineWidth.setEnabled(false);
	}

	private void initCheckBoxState() {
		if (null != this.textStyle) {
			this.checkBoxBorder.setSelected(textStyle.getBold());
			this.checkBoxStrickout.setSelected(textStyle.getStrikeout());
			this.checkBoxItalic.setSelected(textStyle.getItalic());
			this.spinnerInclinationAngl.setEnabled(textStyle.getItalic());
			this.checkBoxUnderline.setSelected(textStyle.getUnderline());
			this.checkBoxShadow.setSelected(textStyle.getShadow());
			this.checkBoxFixedSize.setSelected(textStyle.isSizeFixed());
			this.checkBoxOutlook.setSelected(textStyle.getOutline());
			this.checkBoxBGTransparent.setSelected(!textStyle.getBackOpaque());
			this.checkBoxOutlook.setEnabled(!textStyle.getBackOpaque());
			boolean isOpare = this.checkBoxBGTransparent.isSelected();
			boolean isOutlook = this.checkBoxOutlook.isSelected();
			if (!isOpare || isOutlook) {
				buttonBGColorSelect.setEnabled(true);
			} else {
				buttonBGColorSelect.setEnabled(false);
			}
		}
	}

	/**
	 * 注册事件
	 */
	void registActionListener() {
		unregistActionListener();
		this.comboBoxFontName.addItemListener(this.itemListener);
		this.comboBoxAlign.addItemListener(this.itemListener);
		this.comboBoxFontSize.addItemListener(this.itemListener);
		this.spinnerFontHeight.addChangeListener(this.changedListener);
		this.spinnerFontWidth.addChangeListener(this.changedListener);
		this.spinnerInclinationAngl.addChangeListener(this.changedListener);
		this.spinnerRotationAngl.addChangeListener(this.changedListener);
		this.textFieldFontSize.addKeyListener(this.localKeyListener);
		this.textFieldFontHeight.addKeyListener(this.localKeyListener);
		this.textFieldFontWidth.addKeyListener(this.localKeyListener);
		this.textFieldFontItalicAngl.addKeyListener(this.localKeyListener);
		this.textFieldFontRotationAngl.addKeyListener(this.localKeyListener);
		this.checkBoxBGTransparent.addItemListener(this.checkBoxActionListener);
		this.checkBoxBorder.addItemListener(this.checkBoxActionListener);
		this.checkBoxFixedSize.addItemListener(this.checkBoxActionListener);
		this.checkBoxItalic.addItemListener(this.checkBoxActionListener);
		this.checkBoxOutlook.addItemListener(this.checkBoxActionListener);
		this.checkBoxShadow.addItemListener(this.checkBoxActionListener);
		this.checkBoxUnderline.addItemListener(this.checkBoxActionListener);
		this.checkBoxStrickout.addItemListener(this.checkBoxActionListener);
		this.buttonFontColorSelect.addPropertyChangeListener("m_selectionColors", this.propertyListener);
		this.buttonBGColorSelect.addPropertyChangeListener("m_selectionColors", this.propertyListener);
		this.comboBoxFontSize.getEditor().getEditorComponent().addKeyListener(this.localKeyListener);
		this.map.addDrawnListener(this.mapDrawnListener);
		this.sliderOutLineWidth.addChangeListener(this.outLineWidthChangeListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.comboBoxFontName.removeItemListener(this.itemListener);
		this.comboBoxAlign.removeItemListener(this.itemListener);
		this.comboBoxFontSize.removeItemListener(this.itemListener);
		this.spinnerFontHeight.removeChangeListener(this.changedListener);
		this.spinnerFontWidth.removeChangeListener(this.changedListener);
		this.spinnerInclinationAngl.removeChangeListener(this.changedListener);
		this.spinnerRotationAngl.removeChangeListener(this.changedListener);
		this.textFieldFontSize.removeKeyListener(this.localKeyListener);
		this.textFieldFontHeight.removeKeyListener(this.localKeyListener);
		this.textFieldFontWidth.removeKeyListener(this.localKeyListener);
		this.textFieldFontItalicAngl.removeKeyListener(this.localKeyListener);
		this.textFieldFontRotationAngl.removeKeyListener(this.localKeyListener);
		this.checkBoxBGTransparent.removeItemListener(this.checkBoxActionListener);
		this.checkBoxBorder.removeItemListener(this.checkBoxActionListener);
		this.checkBoxFixedSize.removeItemListener(this.checkBoxActionListener);
		this.checkBoxItalic.removeItemListener(this.checkBoxActionListener);
		this.checkBoxOutlook.removeItemListener(this.checkBoxActionListener);
		this.checkBoxShadow.removeItemListener(this.checkBoxActionListener);
		this.checkBoxUnderline.removeItemListener(this.checkBoxActionListener);
		this.checkBoxStrickout.removeItemListener(this.checkBoxActionListener);
		this.buttonFontColorSelect.removePropertyChangeListener("m_selectionColors", this.propertyListener);
		this.buttonBGColorSelect.removePropertyChangeListener("m_selectionColors", this.propertyListener);
		this.comboBoxFontSize.getEditor().getEditorComponent().removeKeyListener(this.localKeyListener);
		this.map.removeDrawnListener(this.mapDrawnListener);
		this.sliderOutLineWidth.removeChangeListener(this.outLineWidthChangeListener);
	}

	private void initHashMapTextAlignment() {
		int[] textAlignmentValues = Enum.getValues(TextAlignment.class);
		for (int i = 0; i < textAlignmentValues.length; i++) {
			hashMapTextAlignment.put(TEXTALIGNMENT_NAMES[i], textAlignmentValues[i]);
		}
	}

	class LocalItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxFontName) {
				// 设置字体名称
				setFontName();
				refreshMapAtOnce();
			} else if (e.getSource() == comboBoxAlign) {
				// 设置文本对齐方式
				setFontAlign();
				refreshMapAtOnce();
			} else if (e.getSource() == comboBoxFontSize) {
				// 设置字号
				setFontSize();
				refreshMapAtOnce();
			}
		}

		/**
		 * 设置文本对齐方式
		 */
		private void setFontAlign() {
			String textAlignmentName = comboBoxAlign.getSelectedItem().toString();
			int value = hashMapTextAlignment.get(textAlignmentName);
			TextAlignment textAlignment = (TextAlignment) Enum.parse(TextAlignment.class, value);
			textStyle.setAlignment(textAlignment);
		}

		/**
		 * 设置字体名称
		 */
		private void setFontName() {
			String fontName = comboBoxFontName.getSelectedItem().toString();
			textStyle.setFontName(fontName);
		}

	}

	class OutLineChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			textStyle.setOutlineWidth(sliderOutLineWidth.getValue() + 1);
			refreshMapAtOnce();
		}
	}

	class LocalChangedListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == spinnerFontHeight) {
				// 设置字高
				setFontHeight();
				refreshMapAtOnce();
			} else if (e.getSource() == spinnerFontWidth) {
				// 设置字宽
				setFontWidth();
				refreshMapAtOnce();
			} else if (e.getSource() == spinnerInclinationAngl) {
				// 设置文本倾斜角度
				setFontInclinationAngl();
				refreshMapAtOnce();
			} else if (e.getSource() == spinnerRotationAngl) {
				// 设置旋转角度
				setRotationAngl();
				refreshMapAtOnce();
			}
		}
	}

	private void refreshMapAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce && null != map) {
			refreshMapAndLayer();
		}
		return;
	}

	class LocalKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == textFieldFontSize) {
				setFontSize();
				refreshMapAtOnce();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// 输入限制
			int keyChar = e.getKeyChar();
			if (keyChar != '.' && (keyChar < '0' || keyChar > '9')) {
				e.consume();
			}
		}

	}

	class LocalCheckBoxActionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == checkBoxBorder) {
				// 设置字体加粗
				setFontBorder();
				refreshMapAtOnce();
			} else if (e.getSource() == checkBoxStrickout) {
				// 设置添加删除线
				setFontStrickout();
				refreshMapAtOnce();
			} else if (e.getSource() == checkBoxItalic) {
				// 设置字体样式为斜体
				setItalic();
				refreshMapAtOnce();
			} else if (e.getSource() == checkBoxUnderline) {
				// 设置添加下划线
				setUnderline();
				refreshMapAtOnce();
			} else if (e.getSource() == checkBoxShadow) {
				// 设置添加阴影
				setShadow();
			} else if (e.getSource() == checkBoxFixedSize) {
				// 设置字体固定大小
				setFixedSize();
				refreshMapAtOnce();
			} else if (e.getSource() == checkBoxOutlook) {
				// 设置字体轮廓
				setOutLook();
				refreshMapAtOnce();
			} else if (e.getSource() == checkBoxBGTransparent) {
				// 设置背景透明
				setBGOpare();
				refreshMapAtOnce();
			}
		}

		/**
		 * 设置背景透明
		 */
		private void setBGOpare() {
			boolean isOpare = checkBoxBGTransparent.isSelected();
			textStyle.setBackOpaque(!isOpare);
			checkBoxOutlook.setEnabled(isOpare);
			boolean isOutlook = checkBoxOutlook.isSelected();
			if (!isOpare || isOutlook) {
				buttonBGColorSelect.setEnabled(true);
			} else {
				buttonBGColorSelect.setEnabled(false);
			}
		}

		/**
		 * 设置字体轮廓
		 */
		private void setOutLook() {
			boolean isOutlook = checkBoxOutlook.isSelected();
			sliderOutLineWidth.setEnabled(isOutlook);
			textStyle.setOutline(isOutlook);
			boolean isOpare = checkBoxBGTransparent.isSelected();
			if (!isOpare || isOutlook) {
				buttonBGColorSelect.setEnabled(true);
				buttonBGColorSelect.setColor(textStyle.getBackColor());
			} else {
				buttonBGColorSelect.setEnabled(false);
				buttonBGColorSelect.setColor(Color.black);
			}
		}

		/**
		 * 设置字体固定大小
		 */
		private void setFixedSize() {

			boolean isFixedSize = checkBoxFixedSize.isSelected();
			textStyle.setSizeFixed(isFixedSize);
			Double height = 0.0;
			if (isFixedSize) {
				height = Double.parseDouble(textFieldFontHeight.getText());
				height /= 10;
			} else {
				double size = Double.parseDouble(textFieldFontSize.getText());
				height = FontUtilties.fontSizeToMapHeight(size, map, textStyle.isSizeFixed());
			}
			if (height > 0) {
				textStyle.setFontHeight(height);
			}

		}

		/**
		 * 设置添加阴影
		 */
		private void setShadow() {
			boolean isShadow = checkBoxShadow.isSelected();
			textStyle.setShadow(isShadow);
		}

		/**
		 * 设置添加下划线
		 */
		private void setUnderline() {
			boolean isUnderline = checkBoxUnderline.isSelected();
			textStyle.setUnderline(isUnderline);
		}

		/**
		 * 设置字体样式为斜体
		 */
		private void setItalic() {
			boolean isItalic = checkBoxItalic.isSelected();
			spinnerInclinationAngl.setEnabled(isItalic);
			textStyle.setItalic(isItalic);
		}

		/**
		 * 设置添加删除线
		 */
		private void setFontStrickout() {
			boolean isStrickout = checkBoxStrickout.isSelected();
			textStyle.setStrikeout(isStrickout);
		}

		/**
		 * 设置字体加粗
		 */
		private void setFontBorder() {
			boolean isBorder = checkBoxBorder.isSelected();
			textStyle.setBold(isBorder);
		}

	}

	class LocalPropertyListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getSource() == buttonFontColorSelect) {
				// 设置文本颜色
				setFontColor();
				refreshMapAtOnce();
			} else if (e.getSource() == buttonBGColorSelect) {
				// 设置背景颜色
				setBackgroundColor();
				refreshMapAtOnce();
			}
		}

		/**
		 * 设置背景颜色
		 */
		private void setBackgroundColor() {
			Color color = buttonBGColorSelect.getColor();
			if (color != null) {
				textStyle.setBackColor(color);
			}
		}

		/**
		 * 设置文本颜色
		 */
		private void setFontColor() {
			Color color = buttonFontColorSelect.getColor();
			if (color != null) {
				textStyle.setForeColor(color);
			}
		}

	}

	class LocalMapDrawnListener implements MapDrawnListener {

		@Override
		public void mapDrawn(MapDrawnEvent mapDrawnEvent) {
			changeFontSizeWithMapObject();
		}
	}

	/**
	 * 设置旋转角度
	 */
	private void setRotationAngl() {
		if (null != spinnerRotationAngl.getValue()) {
			double rotationAngl = (double) spinnerRotationAngl.getValue();
			textStyle.setRotation(rotationAngl);
		}
	}

	/**
	 * 设置文本倾斜角度
	 */
	private void setFontInclinationAngl() {
		if (null != spinnerInclinationAngl.getValue()) {
			double italicAngl = (double) spinnerInclinationAngl.getValue();
			textStyle.setItalicAngle(italicAngl);
		}
	}

	/**
	 * 设置字宽
	 */
	private void setFontWidth() {
		if (null != spinnerFontWidth.getValue()) {
			boolean isFixSize = checkBoxFixedSize.isSelected();
			double logicalWidth = Double.parseDouble(spinnerFontWidth.getValue().toString());
			double fontWidth = FontUtilties.fontWidthToMapWidth(logicalWidth, map, isFixSize);
			textStyle.setFontWidth(fontWidth / UNIT_CONVERSION);
		}
	}

	/**
	 * 设置字高
	 */
	private void setFontHeight() {
		double logicalHeight = 0.0;
		logicalHeight = Double.parseDouble(textFieldFontHeight.getText());
		Double size = logicalHeight * EXPERIENCE;
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		if (Double.compare(size, size.intValue()) > 0) {
			textFieldFontSize.setText(decimalFormat.format(size));
		} else {
			decimalFormat = new DecimalFormat("0");
			textFieldFontSize.setText(decimalFormat.format(size));
		}
		double fontHeight = logicalHeight;
		fontHeight = FontUtilties.fontSizeToMapHeight(size, map, textStyle.isSizeFixed());
		if (fontHeight > 0) {
			textStyle.setFontHeight(fontHeight);
		}

	}

	/**
	 * 设置字号
	 */
	private void setFontSize() {
		// 保证字高控件的值正确
		double size = Double.valueOf(textFieldFontSize.getText());
		double fontHeight = size / EXPERIENCE;
		fontHeight = FontUtilties.fontSizeToMapHeight(size, map, textStyle.isSizeFixed());
		if (fontHeight > 0) {
			textFieldFontHeight.setText(new DecimalFormat(numeric).format((size / EXPERIENCE)));
			textStyle.setFontHeight(fontHeight);
		}
	}

	private void changeFontSizeWithMapObject() {

		try {
			// 非固定文本大小
			if (!checkBoxFixedSize.isSelected()) {
				// 非固定时，地图中显示的字体在屏幕中显示的大小肯定发生了变化，所以需要重新计算现在的字体大小
				// 字体信息从现在的TextStyle属性中获取，经过计算后显示其字号大小
				Double size = FontUtilties.mapHeightToFontSize(textStyle.getFontHeight(), map, textStyle.isSizeFixed());
				DecimalFormat decimalFormat = new DecimalFormat("0.0");
				if (Double.compare(size, size.intValue()) > 0) {
					textFieldFontSize.setText(decimalFormat.format(size));
				} else {
					decimalFormat = new DecimalFormat("0");
					textFieldFontSize.setText(decimalFormat.format(size));
				}
				if (spinnerFontHeight.getFocusTraversalKeysEnabled()) {
					textFieldFontHeight.setText(new DecimalFormat(numeric).format(size / EXPERIENCE));
				}
			} else {
				// 字体是固定大小时，字体显示的大小不发生变化，不需要更新任何控件内容
			}
		} catch (Exception e) {

		}
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

	@Override
	public Theme getCurrentTheme() {
		return null;
	}

	@Override
	void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	public void setUniformStyle(boolean isUniformStyle) {
		this.isUniformStyle = isUniformStyle;
	}

	@Override
	void refreshMapAndLayer() {

		if (this.isUniformStyle && this.themeLayer.getTheme() instanceof ThemeLabel) {
			((ThemeLabel) this.themeLayer.getTheme()).setUniformStyle(this.textStyle);
		} else if (!this.isUniformStyle && this.themeLayer.getTheme() instanceof ThemeLabel) {
			for (int i = 0; i < this.selectRow.length; i++) {
				((ThemeLabel) this.themeLayer.getTheme()).getItem(this.selectRow[i]).setStyle(this.textStyle);
			}
		}
		if (textStyleType == graphTextFormat) {
			((ThemeGraph) this.themeLayer.getTheme()).setGraphTextStyle(this.textStyle);
		}
		if (textStyleType == graphAxisText) {
			((ThemeGraph) this.themeLayer.getTheme()).setAxesTextStyle(this.textStyle);
		}
		this.map.refresh();
	}

	public void setTextStyleType(int textStyleType) {
		this.textStyleType = textStyleType;
	}

	@Override
	public Layer getCurrentLayer() {
		return themeLayer;
	}

}
