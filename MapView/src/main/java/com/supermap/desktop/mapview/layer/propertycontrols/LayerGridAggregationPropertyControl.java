package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.GeoStyle;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerGridAggregationPropertyModel;
import com.supermap.desktop.ui.SMSpinner;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.LayerGridAggregationType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/7/21.
 */
public class LayerGridAggregationPropertyControl extends AbstractLayerPropertyControl {

	private static final long serialVersionUID = 1L;
	private JLabel labelGridField;
	private JComboBox comboBoxGridField;
	private JLabel labelGridType;
	private JComboBox comboBoxGridType;
	private JLabel labelLength;
	private SmTextFieldLegit textFieldLength;
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
	private SMSpinner smSpinnerLineColorTransparence;
	private JLabel labelBoderWidth;
	private SMSpinner smSpinnerLineWidth;
	private TextStyle textStyle;
	private GeoStyle lineStyle;
	private LabelSettingDialog labelSettingDialog = null;
	private final String urlStr = "/coreresources/";
	private DataCell dataCellNULL;
	private DataCell dataCellSolidLine;
	private DataCell dataCellDottedLine;

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
		this.labelLength = new JLabel("Length");
		this.textFieldLength = new SmTextFieldLegit();
		this.labelPixel = new JLabel("Pixel");
		this.labelColorScheme = new JLabel("ColorScheme");
		this.colorsComboBox = new ColorsComboBox(ControlsProperties.getString("String_ColorSchemeManager_Map_GridAggregation"));
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
		this.smSpinnerLineColorTransparence = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		this.labelBoderWidth = new JLabel("BoderWidth");
		this.smSpinnerLineWidth = new SMSpinner(new SpinnerNumberModel(0, 0, 20, 1));
		this.buttonSetingLabel.setEnabled(false);
		this.dataCellNULL = new DataCell();
		this.dataCellSolidLine = new DataCell();
		this.dataCellDottedLine = new DataCell();
		this.dataCellNULL.initDataImage(((ImageIcon) CoreResources.getIcon(urlStr + "Image_Null.png")), MapViewProperties.getString("String_SymbolImage_None"));
		this.dataCellSolidLine.initDataImage(((ImageIcon) CoreResources.getIcon(urlStr + "Image_SolidLine.png")), MapViewProperties.getString("String_SymbolImage_SolidLine"));
		this.dataCellDottedLine.initDataImage(((ImageIcon) CoreResources.getIcon(urlStr + "Image_DottedLine.png")), MapViewProperties.getString("String_SymbolImage_DottedLine"));


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
						.addComponent(this.smSpinnerLineColorTransparence, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.smSpinnerLineWidth, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE))
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
						.addComponent(this.smSpinnerLineColorTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayoutBorderStyle.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelBoderWidth)
						.addComponent(this.smSpinnerLineWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
//		if (this.textStyle == null) {
//			this.textStyle = new TextStyle();
//		}

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(this.labelGridField, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
										.addComponent(this.labelGridType, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
										.addComponent(this.labelLength, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
										.addComponent(this.labelColorScheme, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
										.addComponent(this.labelMaxColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
										.addComponent(this.labelMinColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
										.addComponent(this.checkboxShowLabel, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH))
								.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(this.comboBoxGridField, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
										.addComponent(this.comboBoxGridType, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(this.textFieldLength, 240, 240, Short.MAX_VALUE)
												//.addComponent(this.textFieldLegitHeight, 120, 120, Short.MAX_VALUE)
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
//				.addComponent(this.panelTextStyle)
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
						.addComponent(this.labelLength)
						.addComponent(this.textFieldLength, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						//.addComponent(this.textFieldLegitHeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
//				.addComponent(this.panelTextStyle)
				.addComponent(this.panelBorderStyle));

	}

	@Override
	protected void initializeResources() {
		((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_LayerHeatmap_ParameterSetting"));
		this.panelBorderStyle.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_LayerGridAggregation_BorderStyle")));
		this.labelGridField.setText(MapViewProperties.getString("String_LayerGridAggregation_GridField"));
		this.labelGridType.setText(MapViewProperties.getString("String_LayerGridAggregation_GridType"));
		this.labelLength.setText(MapViewProperties.getString("String_LayerGridAggregation_HeightAndWidth"));
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
			fillWeightField(getLayerPropertyModel().getFieldInfos(), getLayerPropertyModel().getGridField());
			fillGridType(getLayerPropertyModel().getLayerGridAggregationType());
			this.textFieldLength.setText(getLayerPropertyModel().getLength().toString());
			this.componentDropDownMaxColor.setColor(getLayerPropertyModel().getMaxColor());
			this.componentDropDownMinColor.setColor(getLayerPropertyModel().getMinColor());
			this.smSpinnerMaxColorTransparence.setValue(getLayerPropertyModel().getMaxColorTransparence());
			this.smSpinnerMinColorTransparence.setValue(getLayerPropertyModel().getMinColorTransparence());
			this.checkboxShowLabel.setSelected(getLayerPropertyModel().getShowGridLabel());
			this.buttonSetingLabel.setEnabled(this.checkboxShowLabel.isSelected());
//			if (getLayerPropertyModel().getLineStyle()==null){
//				System.out.println("111111111");
//			}
			fillLineStyle(getLayerPropertyModel().getLineStyle());
			if (getLayerPropertyModel().getTextStyle() == null) {
				//System.out.println("22222222");
				this.textStyle = new TextStyle();
			}else{
				this.textStyle=getLayerPropertyModel().getTextStyle().clone();
			}
		}
	}

	@Override
	protected void registerEvents() {
		this.comboBoxGridField.addItemListener(this.selectedChangeListenerGridField);
		this.comboBoxGridType.addItemListener(this.selectedChangeListenerGridType);
		this.textFieldLength.setSmTextFieldLegit(this.iSmTextFieldLegitLengthRadius);
		this.colorsComboBox.addItemListener(this.selectedChangeListenerColors);
		this.componentDropDownMaxColor.addPropertyChangeListener(this.propertyChangeListenerMaxColor);
		this.smSpinnerMaxColorTransparence.addChangeListener(this.changeListenerMaxColorTransparence);
		this.componentDropDownMinColor.addPropertyChangeListener(this.propertyChangeListenerMinColor);
		this.smSpinnerMinColorTransparence.addChangeListener(this.changeListenerMinColorTransparence);
		this.checkboxShowLabel.addActionListener(this.actionListenerIsSettingLabelStyle);
		this.buttonSetingLabel.addActionListener(this.buttonSettingLabelStyle);
		this.comboBoxLineType.addItemListener(this.selectedChangeListenerLineType);
		this.componentDropDownLineColor.addPropertyChangeListener(this.propertyChangeListenerlineColor);
		this.smSpinnerLineColorTransparence.addChangeListener(this.changeListenerLineColorTransparence);
		this.smSpinnerLineWidth.addChangeListener(this.changeListenerLineWidth);
	}

	@Override
	protected void unregisterEvents() {
		this.comboBoxGridField.removeItemListener(this.selectedChangeListenerGridField);
		this.comboBoxGridType.removeItemListener(this.selectedChangeListenerGridType);
		this.colorsComboBox.removeItemListener(this.selectedChangeListenerColors);
		this.componentDropDownMaxColor.removePropertyChangeListener(this.propertyChangeListenerMaxColor);
		this.smSpinnerMaxColorTransparence.removeChangeListener(this.changeListenerMaxColorTransparence);
		this.componentDropDownMinColor.removePropertyChangeListener(this.propertyChangeListenerMinColor);
		this.smSpinnerMinColorTransparence.removeChangeListener(this.changeListenerMinColorTransparence);
		this.checkboxShowLabel.removeActionListener(this.actionListenerIsSettingLabelStyle);
		this.buttonSetingLabel.removeActionListener(this.buttonSettingLabelStyle);
		this.comboBoxLineType.removeItemListener(this.selectedChangeListenerLineType);
		this.componentDropDownLineColor.removePropertyChangeListener(this.propertyChangeListenerlineColor);
		this.smSpinnerLineColorTransparence.removeChangeListener(this.changeListenerLineColorTransparence);
		this.smSpinnerLineWidth.removeChangeListener(this.changeListenerLineWidth);
	}

	@Override
	protected void setControlEnabled(String propertyName, boolean enabled) {
		if (propertyName.equals(LayerGridAggregationPropertyModel.GRID_FIELD)) {
			this.comboBoxGridField.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.GRID_TYPE)) {
			this.comboBoxGridType.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.LENGTH)) {
			this.textFieldLength.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.COLOR_SCHEME)) {
			this.colorsComboBox.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.MAX_COLOR)) {
			this.componentDropDownMaxColor.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.MAX_COLOR_TRANSPARENCE)) {
			this.smSpinnerMaxColorTransparence.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.MIN_COLOR)) {
			this.componentDropDownMinColor.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.MIN_COLOR_TRANSPARENCE)) {
			this.smSpinnerMinColorTransparence.setEnabled(enabled);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.IS_SHOW_GRID_LABEL)) {
			this.checkboxShowLabel.setEnabled(true);
		} else if (propertyName.equals(LayerGridAggregationPropertyModel.LINE_STYLE)) {
			this.comboBoxLineType.setEnabled(enabled);
			this.componentDropDownLineColor.setEnabled(enabled);
			this.smSpinnerLineColorTransparence.setEnabled(enabled);
			this.smSpinnerLineWidth.setEnabled(enabled);
		}
	}

	private ItemListener selectedChangeListenerGridField = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			comboBoxGridFieldChange();
		}
	};

	private ItemListener selectedChangeListenerGridType = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			comboBoxGridTypeChange();
		}
	};

	private ISmTextFieldLegit iSmTextFieldLegitLengthRadius = new ISmTextFieldLegit() {
		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			if (StringUtilities.isNullOrEmpty(textFieldValue)) {
				return false;
			}
			try {
				Integer integer = Integer.valueOf(textFieldValue);
			} catch (Exception e) {
				return false;
			}
			textFieldLengthChange();
			return true;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	private ItemListener selectedChangeListenerColors = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			comboBoxColorsChange();
		}
	};

	private PropertyChangeListener propertyChangeListenerMaxColor = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			maxColorChange();
		}
	};

	private PropertyChangeListener propertyChangeListenerMinColor = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			minColorChange();
		}
	};

	private ChangeListener changeListenerMaxColorTransparence = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			maxColorTransparenceChange();
		}
	};

	private ChangeListener changeListenerMinColorTransparence = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			minColorTransparenceChange();
		}
	};

	private ActionListener actionListenerIsSettingLabelStyle = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			showLabelChange();
		}
	};

	private ActionListener buttonSettingLabelStyle = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			labelSettingDialog = new LabelSettingDialog(textStyle);
			labelSettingDialog.showDialog();
			if (labelSettingDialog.getDialogResult()==DialogResult.OK){
				textStyleChange();
			}
		}
	};

	private ItemListener selectedChangeListenerLineType=new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			comboBoxLineTypeChange();
		}
	};

	private PropertyChangeListener propertyChangeListenerlineColor = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			lineColorChange();
		}
	};

	private ChangeListener changeListenerLineColorTransparence = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			lineColorTransparenceChange();
		}
	};

	private ChangeListener changeListenerLineWidth = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			lineWidthTransparenceChange();
		}
	};

	private void fillWeightField(FieldInfos fieldInfos, String weightField) {
		this.comboBoxGridField.removeAllItems();
		this.comboBoxGridField.addItem("");
		for (int i = 0; i < fieldInfos.getCount(); i++) {
			if (fieldInfos.get(i).getType() == FieldType.INT32 || fieldInfos.get(i).getType() == FieldType.INT16 ||
					fieldInfos.get(i).getType() == FieldType.INT64 || fieldInfos.get(i).getType() == FieldType.SINGLE ||
					fieldInfos.get(i).getType() == FieldType.DOUBLE) {
				this.comboBoxGridField.addItem(fieldInfos.get(i).getName());
			}
		}
		this.comboBoxGridField.setSelectedItem(weightField);
	}

	private void fillGridType(LayerGridAggregationType layerGridAggregationType) {
		this.comboBoxGridType.removeAllItems();
		this.comboBoxGridType.addItem(MapViewProperties.getString("String_LayerGridAggregation_GridType_Four"));
		this.comboBoxGridType.addItem(MapViewProperties.getString("String_LayerGridAggregation_GridType_Six"));
		if (layerGridAggregationType == LayerGridAggregationType.HEXAGON) {
			this.comboBoxGridType.setSelectedIndex(1);
		}
	}

	private void fillLineStyle(GeoStyle lineStyle) {
		this.comboBoxLineType.removeAllItems();
		try {
			this.comboBoxLineType.addItem(this.dataCellNULL);
			this.comboBoxLineType.addItem(this.dataCellSolidLine);
			this.comboBoxLineType.addItem(this.dataCellDottedLine);
			this.comboBoxLineType.setRenderer(new CommonListCellRenderer());
			if (lineStyle != null) {
				if (lineStyle.getLineSymbolID()==0){
					this.comboBoxLineType.setSelectedIndex(1);
				}else if (lineStyle.getLineSymbolID()==1){
					this.comboBoxLineType.setSelectedIndex(2);
				}
				this.componentDropDownLineColor.setColor(lineStyle.getLineColor());
				this.smSpinnerLineColorTransparence.setValue((int) Math.round((1.0 - lineStyle.getLineColor().getAlpha() / 255.0) * 100));
				this.smSpinnerLineWidth.setValue(lineStyle.getLineWidth());
				this.lineStyle=lineStyle.clone();
			} else {
				this.lineStyle=new GeoStyle();
				this.lineStyle.setLineColor(Color.WHITE);
				this.lineStyle.setLineWidth(0);
				this.componentDropDownLineColor.setColor(this.lineStyle.getLineColor());
				this.smSpinnerLineColorTransparence.setValue((int) Math.round((1.0 - this.lineStyle.getLineColor().getAlpha() / 255.0) * 100));
				this.smSpinnerLineWidth.setValue(this.lineStyle.getLineWidth());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void comboBoxGridFieldChange() {
		getModifiedLayerPropertyModel().setGridField(String.valueOf(this.comboBoxGridField.getSelectedItem()));
		checkChanged();
	}

	private void comboBoxGridTypeChange() {
		if (this.comboBoxGridType.getSelectedItem().toString().equals(MapViewProperties.getString("String_LayerGridAggregation_GridType_Four"))) {
			getModifiedLayerPropertyModel().setLayerGridAggregationType(LayerGridAggregationType.QUADRANGLE);
		} else {
			getModifiedLayerPropertyModel().setLayerGridAggregationType(LayerGridAggregationType.HEXAGON);
		}
		checkChanged();
	}

	private void textFieldLengthChange() {
		getModifiedLayerPropertyModel().setLength(Integer.valueOf(this.textFieldLength.getText()));
		checkChanged();
	}

	private void comboBoxColorsChange() {
		getModifiedLayerPropertyModel().setColorScheme(this.colorsComboBox.getSelectedItem());
		this.componentDropDownMaxColor.setColor(this.colorsComboBox.getSelectedItem().get(0));
		this.componentDropDownMinColor.setColor(this.colorsComboBox.getSelectedItem().get((this.colorsComboBox.getSelectedItem().getCount() - 1)));
		maxColorChange();
		minColorChange();
	}

	private void maxColorChange() {
		getModifiedLayerPropertyModel().setMaxColor(this.componentDropDownMaxColor.getColor());
		int temp = (int) Math.round((1.0 - this.componentDropDownMaxColor.getColor().getAlpha() / 255.0) * 100);
		getModifiedLayerPropertyModel().setMaxColorTransparence(temp);
		this.smSpinnerMaxColorTransparence.removeChangeListener(this.changeListenerMaxColorTransparence);
		this.smSpinnerMaxColorTransparence.setValue(temp);
		this.smSpinnerMaxColorTransparence.addChangeListener(this.changeListenerMaxColorTransparence);
		checkChanged();
	}

	private void minColorChange() {
		getModifiedLayerPropertyModel().setMinColor(this.componentDropDownMinColor.getColor());
		int temp = (int) Math.round((1.0 - this.componentDropDownMinColor.getColor().getAlpha() / 255.0) * 100);
		getModifiedLayerPropertyModel().setMinColorTransparence(temp);
		this.smSpinnerMinColorTransparence.removeChangeListener(this.changeListenerMinColorTransparence);
		this.smSpinnerMinColorTransparence.setValue(temp);
		this.smSpinnerMinColorTransparence.addChangeListener(this.changeListenerMinColorTransparence);
		checkChanged();
	}

	private void maxColorTransparenceChange() {
		this.componentDropDownMaxColor.setColor(resetColor(this.componentDropDownMaxColor.getColor(), (int) this.smSpinnerMaxColorTransparence.getValue()));
		getModifiedLayerPropertyModel().setMaxColor(this.componentDropDownMaxColor.getColor());
		getModifiedLayerPropertyModel().setMaxColorTransparence((int) Math.round((1.0 - this.componentDropDownMaxColor.getColor().getAlpha() / 255.0) * 100));
		checkChanged();
	}

	private void minColorTransparenceChange() {
		this.componentDropDownMinColor.setColor(resetColor(this.componentDropDownMinColor.getColor(), (int) this.smSpinnerMinColorTransparence.getValue()));
		getModifiedLayerPropertyModel().setMinColor(this.componentDropDownMinColor.getColor());
		int temp = (int) Math.round((1.0 - this.componentDropDownMinColor.getColor().getAlpha() / 255.0) * 100);
		getModifiedLayerPropertyModel().setMinColorTransparence(temp);
		checkChanged();
	}

	private Color resetColor(Color oldColor, int transparence) {
		Color newColor = new Color((float) (oldColor.getRed() / 255.0), (float) (oldColor.getGreen() / 255.0), (float) (oldColor.getBlue() / 255.0), (float) (1.0 - transparence / 100.0));
		return newColor;
	}

	private void showLabelChange(){
		this.buttonSetingLabel.setEnabled(this.checkboxShowLabel.isSelected());
		getModifiedLayerPropertyModel().setShowGridLabel(this.checkboxShowLabel.isSelected());
		checkChanged();
	}

	private void textStyleChange(){
		this.textStyle=this.labelSettingDialog.getTextStyle().clone();
		getModifiedLayerPropertyModel().setTextStyle(this.textStyle.clone());
		checkChanged();
	}

	private void comboBoxLineTypeChange(){
		if (comboBoxLineType.getSelectedItem()!=null){
			if (comboBoxLineType.getSelectedIndex()==0){
				this.lineStyle.setLineSymbolID(5);
			}else if (comboBoxLineType.getSelectedIndex()==1){
				this.lineStyle.setLineSymbolID(0);
			}else if (comboBoxLineType.getSelectedIndex()==2){
				this.lineStyle.setLineSymbolID(1);
			}
		}
		lineStyleChange();
	}

	private void lineColorChange(){
		if (this.componentDropDownLineColor.getColor()==null){
			this.componentDropDownLineColor.setColor(Color.WHITE);
		}
		this.lineStyle.setLineColor(this.componentDropDownLineColor.getColor());
		int temp = (int) Math.round((1.0 - this.componentDropDownLineColor.getColor().getAlpha() / 255.0) * 100);
		this.smSpinnerLineColorTransparence.removeChangeListener(this.changeListenerLineColorTransparence);
		this.smSpinnerLineColorTransparence.setValue(temp);
		this.smSpinnerLineColorTransparence.addChangeListener(this.changeListenerLineColorTransparence);
		lineStyleChange();
	}

	private void lineColorTransparenceChange() {
		this.componentDropDownLineColor.setColor(resetColor(this.componentDropDownLineColor.getColor(), (int) this.smSpinnerLineColorTransparence.getValue()));
		this.lineStyle.setLineColor(this.componentDropDownLineColor.getColor());
		lineStyleChange();
	}

	private void lineWidthTransparenceChange() {
		this.lineStyle.setLineWidth(Double.valueOf(this.smSpinnerLineWidth.getValue().toString()));
		lineStyleChange();
	}

	private void lineStyleChange(){
		getModifiedLayerPropertyModel().setLineStyle(this.lineStyle.clone());
		checkChanged();
	}
}
