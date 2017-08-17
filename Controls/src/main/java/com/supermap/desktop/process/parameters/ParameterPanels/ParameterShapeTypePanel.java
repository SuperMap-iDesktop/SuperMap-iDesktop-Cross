package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterShapeType;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created By Chens on 2017/8/16 0016
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.SHAPE_TYPE)
public class ParameterShapeTypePanel extends SwingPanel implements IParameterPanel {
	JLabel labelShapeType;
	JComboBox comboBoxShapeType;
	JLabel labelUnitType;
	JComboBox comboBoxUnitType;
	JLabel labelWidth;
	SmTextFieldLegit textFieldWidth;
	JLabel labelHeight;
	SmTextFieldLegit textFieldHeight;
	JLabel labelRadius;
	SmTextFieldLegit textFieldRadius;
	JLabel labelInnerRadius;
	SmTextFieldLegit textFieldInnerRadius;
	JLabel labelOuterRadius;
	SmTextFieldLegit textFieldOuterRadius;
	JLabel labelStartAngle;
	SmTextFieldLegit textFieldStartAngle;
	JLabel labelEndAngle;
	SmTextFieldLegit textFieldEndAngle;

	private boolean isSelectingItem = false;
	ParameterShapeType parameterShapeType;
	NeighbourStatisticsParameter neighbourStatisticsParameter;

	static final String RECTANGLE = ProcessProperties.getString("String_Rectangle");
	static final String CIRCLE = ProcessProperties.getString("String_Circle");
	static final String ANNULUS = ProcessProperties.getString("String_Annulus");
	static final String WEDGE = ProcessProperties.getString("String_Wedge");

	public ParameterShapeTypePanel(IParameter parameterShapeType) {
		super(parameterShapeType);
		this.parameterShapeType = (ParameterShapeType) parameterShapeType;
		this.neighbourStatisticsParameter = (NeighbourStatisticsParameter) ((ParameterShapeType) parameterShapeType).getSelectedItem();
		initComponent();
		initResources();
		initLayout();
		initComponentState();
		initListener();
	}

	private void initComponent() {
		labelShapeType =new JLabel();
		labelUnitType =new JLabel();
		labelWidth =new JLabel();
		labelHeight =new JLabel();
		labelRadius =new JLabel();
		labelInnerRadius =new JLabel();
		labelOuterRadius =new JLabel();
		labelStartAngle =new JLabel();
		labelEndAngle =new JLabel();
		comboBoxShapeType = new JComboBox();
		comboBoxUnitType = new JComboBox();
		textFieldWidth = new SmTextFieldLegit();
		textFieldHeight = new SmTextFieldLegit();
		textFieldRadius = new SmTextFieldLegit();
		textFieldInnerRadius = new SmTextFieldLegit();
		textFieldOuterRadius = new SmTextFieldLegit();
		textFieldStartAngle = new SmTextFieldLegit();
		textFieldEndAngle = new SmTextFieldLegit();
	}

	private void initResources() {
		labelShapeType.setText(ProcessProperties.getString("String_Label_NeighbourShapeType"));
		labelUnitType.setText(ProcessProperties.getString("String_Label_NeighbourUnitType"));
		labelWidth.setText(ProcessProperties.getString("String_Label_Width"));
		labelHeight.setText(ProcessProperties.getString("String_Label_Height"));
		labelRadius.setText(ProcessProperties.getString("String_Label_Radius"));
		labelInnerRadius.setText(ProcessProperties.getString("String_Label_InnerRadius"));
		labelOuterRadius.setText(ProcessProperties.getString("String_Label_OuterRadius"));
		labelStartAngle.setText(ProcessProperties.getString("String_Label_StartAngle"));
		labelEndAngle.setText(ProcessProperties.getString("String_Label_EndAngle"));
	}

	private void initLayout() {
		panel.setLayout(new GridBagLayout());

		panel.add(labelUnitType, new GridBagConstraintsHelper(0,0,1,1).setFill(GridBagConstraints.NONE).setInsets(10,10,5,5));
		panel.add(comboBoxUnitType, new GridBagConstraintsHelper(1,0,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,5,5,10));
		panel.add(labelShapeType, new GridBagConstraintsHelper(0,1,1,1).setFill(GridBagConstraints.NONE).setInsets(5,10,5,5));
		panel.add(comboBoxShapeType, new GridBagConstraintsHelper(1,1,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,10));
		panel.add(labelWidth, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 5));
		panel.add(textFieldWidth, new GridBagConstraintsHelper(1,2,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,5));
		panel.add(labelHeight, new GridBagConstraintsHelper(0, 3, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 5));
		panel.add(textFieldHeight, new GridBagConstraintsHelper(1,3,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,10));
		panel.add(labelRadius, new GridBagConstraintsHelper(0, 4, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 5));
		panel.add(textFieldRadius, new GridBagConstraintsHelper(1,4,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,10));
		panel.add(labelInnerRadius, new GridBagConstraintsHelper(0, 5, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 5));
		panel.add(textFieldInnerRadius, new GridBagConstraintsHelper(1,5,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,10));
		panel.add(labelOuterRadius, new GridBagConstraintsHelper(0, 6, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 5));
		panel.add(textFieldOuterRadius, new GridBagConstraintsHelper(1,6,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,10));
		panel.add(labelStartAngle, new GridBagConstraintsHelper(0, 7, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 5));
		panel.add(textFieldStartAngle, new GridBagConstraintsHelper(1,7,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,5,10));
		panel.add(labelEndAngle, new GridBagConstraintsHelper(0, 8, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 10, 5));
		panel.add(textFieldEndAngle, new GridBagConstraintsHelper(1,8,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,5,10,10));
		labelWidth.setVisible(true);
		labelHeight.setVisible(true);
		labelRadius.setVisible(false);
		labelInnerRadius.setVisible(false);
		labelOuterRadius.setVisible(false);
		labelStartAngle.setVisible(false);
		labelEndAngle.setVisible(false);
		textFieldWidth.setVisible(true);
		textFieldHeight.setVisible(true);
		textFieldRadius.setVisible(false);
		textFieldInnerRadius.setVisible(false);
		textFieldOuterRadius.setVisible(false);
		textFieldStartAngle.setVisible(false);
		textFieldEndAngle.setVisible(false);
	}

	private void initListener() {
		comboBoxShapeType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					if (comboBoxShapeType.getSelectedItem().equals(RECTANGLE)) {
						labelWidth.setVisible(true);
						labelHeight.setVisible(true);
						labelRadius.setVisible(false);
						labelInnerRadius.setVisible(false);
						labelOuterRadius.setVisible(false);
						labelStartAngle.setVisible(false);
						labelEndAngle.setVisible(false);
						textFieldWidth.setVisible(true);
						textFieldHeight.setVisible(true);
						textFieldRadius.setVisible(false);
						textFieldInnerRadius.setVisible(false);
						textFieldOuterRadius.setVisible(false);
						textFieldStartAngle.setVisible(false);
						textFieldEndAngle.setVisible(false);
						neighbourStatisticsParameter = new NeighbourStatisticsRectangleParameter();
						((NeighbourStatisticsRectangleParameter)neighbourStatisticsParameter).setWidth(Double.valueOf(textFieldWidth.getText().toString()));
						((NeighbourStatisticsRectangleParameter)neighbourStatisticsParameter).setHeight(Double.valueOf(textFieldHeight.getText().toString()));
					} else if (comboBoxShapeType.getSelectedItem().equals(CIRCLE)) {
						labelWidth.setVisible(false);
						labelHeight.setVisible(false);
						labelRadius.setVisible(true);
						labelInnerRadius.setVisible(false);
						labelOuterRadius.setVisible(false);
						labelStartAngle.setVisible(false);
						labelEndAngle.setVisible(false);
						textFieldWidth.setVisible(false);
						textFieldHeight.setVisible(false);
						textFieldRadius.setVisible(true);
						textFieldInnerRadius.setVisible(false);
						textFieldOuterRadius.setVisible(false);
						textFieldStartAngle.setVisible(false);
						textFieldEndAngle.setVisible(false);
						neighbourStatisticsParameter = new NeighbourStatisticsCircleParameter();
						((NeighbourStatisticsCircleParameter)neighbourStatisticsParameter).setRadius(Double.valueOf(textFieldRadius.getText().toString()));
					} else if (comboBoxShapeType.getSelectedItem().equals(ANNULUS)) {
						labelWidth.setVisible(false);
						labelHeight.setVisible(false);
						labelRadius.setVisible(false);
						labelInnerRadius.setVisible(true);
						labelOuterRadius.setVisible(true);
						labelStartAngle.setVisible(false);
						labelEndAngle.setVisible(false);
						textFieldWidth.setVisible(false);
						textFieldHeight.setVisible(false);
						textFieldRadius.setVisible(false);
						textFieldInnerRadius.setVisible(true);
						textFieldOuterRadius.setVisible(true);
						textFieldStartAngle.setVisible(false);
						textFieldEndAngle.setVisible(false);
						neighbourStatisticsParameter = new NeighbourStatisticsAnnulusParameter();
						((NeighbourStatisticsAnnulusParameter)neighbourStatisticsParameter).setInnerRadius(Double.valueOf(textFieldInnerRadius.getText().toString()));
						((NeighbourStatisticsAnnulusParameter)neighbourStatisticsParameter).setOuterRadius(Double.valueOf(textFieldOuterRadius.getText().toString()));
					} else if (comboBoxShapeType.getSelectedItem().equals(WEDGE)) {
						labelWidth.setVisible(false);
						labelHeight.setVisible(false);
						labelRadius.setVisible(true);
						labelInnerRadius.setVisible(false);
						labelOuterRadius.setVisible(false);
						labelStartAngle.setVisible(true);
						labelEndAngle.setVisible(true);
						textFieldWidth.setVisible(false);
						textFieldHeight.setVisible(false);
						textFieldRadius.setVisible(true);
						textFieldInnerRadius.setVisible(false);
						textFieldOuterRadius.setVisible(false);
						textFieldStartAngle.setVisible(true);
						textFieldEndAngle.setVisible(true);
						neighbourStatisticsParameter = new NeighbourStatisticsWedgeParameter();
						((NeighbourStatisticsWedgeParameter)neighbourStatisticsParameter).setRadius(Double.valueOf(textFieldRadius.getText().toString()));
						((NeighbourStatisticsWedgeParameter)neighbourStatisticsParameter).setStartAngle(Double.valueOf(textFieldStartAngle.getText().toString()));
						((NeighbourStatisticsWedgeParameter)neighbourStatisticsParameter).setEndAngle(Double.valueOf(textFieldEndAngle.getText().toString()));
					}
					parameterShapeType.setSelectedItem(neighbourStatisticsParameter);
					isSelectingItem = false;
				}
			}
		});


	}

	private void initComponentState() {
		comboBoxUnitType.addItem(ProcessProperties.getString("String_NeighbourUnitType_Cell"));
		comboBoxUnitType.addItem(ProcessProperties.getString("String_NeighbourUnitType_Map"));
		comboBoxShapeType.addItem(RECTANGLE);
		comboBoxShapeType.addItem(CIRCLE);
		comboBoxShapeType.addItem(ANNULUS);
		comboBoxShapeType.addItem(WEDGE);
		textFieldWidth.setText("3");
		textFieldHeight.setText("3");
		textFieldRadius.setText("3");
		textFieldInnerRadius.setText("1");
		textFieldOuterRadius.setText("3");
		textFieldStartAngle.setText("0");
		textFieldEndAngle.setText("360");
	}
}
