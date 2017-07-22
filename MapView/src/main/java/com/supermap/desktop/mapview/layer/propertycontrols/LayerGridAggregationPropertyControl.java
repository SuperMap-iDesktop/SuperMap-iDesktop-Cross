package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerGridAggregationPropertyModel;
import com.supermap.desktop.ui.SMSpinner;
import com.supermap.desktop.ui.controls.ComponentDropDown;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxCharset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by lixiaoyao on 2017/7/21.
 */
public class LayerGridAggregationPropertyControl extends AbstractLayerPropertyControl {

	private static final long serialVersionUID = 1L;
	private JLabel labelGridField;
	private JComboBox comboBoxGridField;
	private JLabel labelGridType;
	private JComboBox comboBoxGridType;
	private JLabel labelHeightAndWidth;
	private SmTextFieldLegit textFieldLegitHeight;
	private SmTextFieldLegit textFieldLegitWidth;
	private JLabel labelPixel;
	private JLabel labelColorScheme;
	private ColorsComboBox colorsComboBox;
	private JLabel labelMaxColor;
	private ComponentDropDown componentDropDownMaxColor;
	private JLabel labelMaxColorTransparence;
	private SMSpinner smSpinnerMaxColorTransparence;
	private JLabel labelMinColor;
	private ComponentDropDown componentDropDownMinColor;
	private JLabel labelMinColorTransparence;
	private SMSpinner smSpinnerMinColorTransparence;
	private JCheckBox checkboxShowLabel;
	private JButton buttonSetingLabel;
	private JPanel panelBorderStyle;
	private JLabel labelLineType;
	private JComboBox comboBoxLineType;
	private JLabel labelLineColor;
	private ComponentDropDown componentDropDownLineColor;
	private JLabel labelBorderTransparence;
	private SMSpinner smSpinnerBorderTransparence;
	private JLabel labelBoderWidth;
	private SMSpinner smSpinnerBoderWidth;

	public LayerGridAggregationPropertyControl() {
		// do nothing
	}

	@Override
	public LayerGridAggregationPropertyModel getLayerPropertyModel() {
		return (LayerGridAggregationPropertyModel) super.getLayerPropertyModel();
	}

	@Override
	public LayerGridAggregationPropertyModel getModifiedLayerPropertyModel() {
		return (LayerGridAggregationPropertyModel) super.getModifiedLayerPropertyModel();
	}

	@Override
	protected void initializeComponents() {
		this.setBorder(BorderFactory.createTitledBorder("parameterSetting"));

		this.labelGridField = new JLabel("GridField");
		this.comboBoxGridField = new JComboBox();
		this.labelGridType = new JLabel("GridType");
		this.comboBoxGridType = new JComboBox();
		this.labelHeightAndWidth = new JLabel("HeightAndWidth");
		this.textFieldLegitWidth = new SmTextFieldLegit();
		this.textFieldLegitHeight = new SmTextFieldLegit();
		this.labelPixel = new JLabel("Pixel");
		this.labelColorScheme = new JLabel("ColorScheme");
		this.colorsComboBox = new ColorsComboBox();
		this.labelMaxColor = new JLabel("MaxColor");
		this.componentDropDownMaxColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
		this.labelMaxColorTransparence = new JLabel("MaxColorTransparence");
		this.smSpinnerMaxColorTransparence = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		this.labelMinColor = new JLabel("MinColor");
		this.componentDropDownMinColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
		this.labelMinColorTransparence = new JLabel("MinColorTransparence");
		this.smSpinnerMinColorTransparence = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		this.checkboxShowLabel = new JCheckBox();
		this.buttonSetingLabel = new JButton("SetingLabel");
		this.panelBorderStyle = new JPanel();
		this.labelLineType = new JLabel("LineType");
		this.comboBoxLineType = new JComboBox();
		this.labelLineColor = new JLabel("LineColor");
		this.componentDropDownLineColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
		this.labelBorderTransparence = new JLabel("BorderTransparence");
		this.smSpinnerBorderTransparence = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		this.labelBoderWidth = new JLabel("BoderWidth");
		this.smSpinnerBoderWidth = new SMSpinner(new SpinnerNumberModel(0, 0, 20, 1)); //todo 需要确定最大值是20固定值吗？

		GroupLayout groupLayoutBorderStyle = new GroupLayout(this.panelBorderStyle);
		groupLayoutBorderStyle.setAutoCreateGaps(true);
		groupLayoutBorderStyle.setAutoCreateContainerGaps(true);
		this.panelBorderStyle.setLayout(groupLayoutBorderStyle);

		groupLayoutBorderStyle.setHorizontalGroup(groupLayoutBorderStyle.createSequentialGroup()
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelLineType)
						.addComponent(this.labelLineColor)
						.addComponent(this.labelBorderTransparence)
						.addComponent(this.labelBoderWidth))
				.addGap(50)
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.comboBoxLineType, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.componentDropDownLineColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.smSpinnerBorderTransparence, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.smSpinnerBoderWidth, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE))
		);
		groupLayoutBorderStyle.setVerticalGroup(groupLayoutBorderStyle.createSequentialGroup()
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLineType)
						.addComponent(this.comboBoxLineType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLineColor)
						.addComponent(this.componentDropDownLineColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelBorderTransparence)
						.addComponent(this.smSpinnerBorderTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelBoderWidth)
						.addComponent(this.smSpinnerBoderWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.labelGridField, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelGridType, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelHeightAndWidth, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelColorScheme, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelMaxColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelMinColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.checkboxShowLabel, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.comboBoxGridField, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
								.addComponent(this.comboBoxGridType, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(this.textFieldLegitWidth, 120, 120, Short.MAX_VALUE)
										.addComponent(this.textFieldLegitHeight, 120, 120, Short.MAX_VALUE)
										.addComponent(this.labelPixel))
								.addComponent(this.colorsComboBox, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(this.componentDropDownMaxColor, 55, 55, Short.MAX_VALUE)
												.addComponent(this.componentDropDownMinColor, 55, 55, Short.MAX_VALUE))
										.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(this.labelMaxColorTransparence)
												.addComponent(this.labelMinColorTransparence))
										.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(this.smSpinnerMaxColorTransparence, 55, 55, Short.MAX_VALUE)
												.addComponent(this.smSpinnerMinColorTransparence, 55, 55, Short.MAX_VALUE)))
								.addComponent(this.buttonSetingLabel, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)))
				.addComponent(this.panelBorderStyle)
		);

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelGridField)
						.addComponent(this.comboBoxGridField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelGridType)
						.addComponent(this.comboBoxGridType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelHeightAndWidth)
						.addComponent(this.textFieldLegitWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.textFieldLegitHeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelPixel))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelColorScheme)
						.addComponent(this.colorsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelMaxColor)
						.addComponent(this.componentDropDownMaxColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelMaxColorTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.smSpinnerMaxColorTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelMinColor)
						.addComponent(this.componentDropDownMinColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelMinColorTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.smSpinnerMinColorTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkboxShowLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonSetingLabel, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_HEIGHT, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.panelBorderStyle));

	}

	@Override
	protected void initializeResources() {
		((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_LayerHeatmap_ParameterSetting"));
		this.panelBorderStyle.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_LayerGridAggregation_BorderStyle")));
		this.labelGridField.setText(MapViewProperties.getString("String_LayerGridAggregation_GridField"));
		this.labelGridType.setText(MapViewProperties.getString("String_LayerGridAggregation_GridType"));
		this.labelHeightAndWidth.setText(MapViewProperties.getString("String_LayerGridAggregation_HeightAndWidth"));
		this.labelPixel.setText(MapViewProperties.getString("String_LayerGridAggregation_Pixel"));
		this.labelColorScheme.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.labelMaxColor.setText(MapViewProperties.getString("String_LayerHeatmap_MaxColor"));
		this.labelMaxColorTransparence.setText(MapViewProperties.getString("String_Opaque"));
		this.labelMinColor.setText(MapViewProperties.getString("String_LayerHeatmap_MinColor"));
		this.labelMinColorTransparence.setText(MapViewProperties.getString("String_Opaque"));
		this.checkboxShowLabel.setText(MapViewProperties.getString("String_LayerGridAggregation_ShowLabel"));
		this.buttonSetingLabel.setText(MapViewProperties.getString("String_LayerGridAggregation_SetingLabel"));
		this.labelLineType.setText(MapViewProperties.getString("String_LayerGridAggregation_LineType"));
		this.labelLineColor.setText(MapViewProperties.getString("String_LayerGridAggregation_LineColor"));
		this.labelBorderTransparence.setText(MapViewProperties.getString("String_Opaque"));
		this.labelBoderWidth.setText(MapViewProperties.getString("String_LayerGridAggregation_BoderWidth"));
	}

	@Override
	protected void fillComponents() {
		if (getLayerPropertyModel() != null) {

		}
	}

	@Override
	protected void registerEvents() {

	}

	@Override
	protected void unregisterEvents() {

	}

	@Override
	protected void setControlEnabled(String propertyName, boolean enabled) {

	}
}
