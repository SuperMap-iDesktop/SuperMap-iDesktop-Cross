package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterShapeType;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.NumTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
	NumTextFieldLegit textFieldWidth;
	JLabel labelHeight;
	NumTextFieldLegit textFieldHeight;
	JLabel labelRadius;
	NumTextFieldLegit textFieldRadius;
	JLabel labelInnerRadius;
	NumTextFieldLegit textFieldInnerRadius;
	JLabel labelOuterRadius;
	NumTextFieldLegit textFieldOuterRadius;
	JLabel labelStartAngle;
	NumTextFieldLegit textFieldStartAngle;
	JLabel labelEndAngle;
	NumTextFieldLegit textFieldEndAngle;

	private boolean isSelectingItem = false;
	ParameterShapeType parameterShapeType;
	NeighbourShape neighbourShape;

	static final String RECTANGLE = ProcessProperties.getString("String_Rectangle");
	static final String CIRCLE = ProcessProperties.getString("String_Circle");
	static final String ANNULUS = ProcessProperties.getString("String_Annulus");
	static final String WEDGE = ProcessProperties.getString("String_Wedge");
	static final String UNIT_TYPE_CELL = ProcessProperties.getString("String_NeighbourUnitType_Cell");
	static final String UNIT_TYPE_MAP = ProcessProperties.getString("String_NeighbourUnitType_Map");

	public ParameterShapeTypePanel(IParameter parameterShapeType) {
		super(parameterShapeType);
		this.parameterShapeType = (ParameterShapeType) parameterShapeType;
		this.neighbourShape = (NeighbourShape) ((ParameterShapeType) parameterShapeType).getSelectedItem();
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
		textFieldWidth = new NumTextFieldLegit();
		textFieldHeight = new NumTextFieldLegit();
		textFieldRadius = new NumTextFieldLegit();
		textFieldInnerRadius = new NumTextFieldLegit();
		textFieldOuterRadius = new NumTextFieldLegit();
		textFieldStartAngle = new NumTextFieldLegit();
		textFieldEndAngle = new NumTextFieldLegit();
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

		panel.add(labelUnitType, new GridBagConstraintsHelper(0,0,1,1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20));
		panel.add(comboBoxUnitType, new GridBagConstraintsHelper(1,0,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelShapeType, new GridBagConstraintsHelper(0,1,1,1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20));
		panel.add(comboBoxShapeType, new GridBagConstraintsHelper(1,1,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelWidth, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldWidth, new GridBagConstraintsHelper(1,2,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelHeight, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldHeight, new GridBagConstraintsHelper(1,3,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelRadius, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldRadius, new GridBagConstraintsHelper(1,4,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelInnerRadius, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldInnerRadius, new GridBagConstraintsHelper(1,5,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelOuterRadius, new GridBagConstraintsHelper(0, 6, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldOuterRadius, new GridBagConstraintsHelper(1,6,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelStartAngle, new GridBagConstraintsHelper(0, 7, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,5,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldStartAngle, new GridBagConstraintsHelper(1,7,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,5,0));
		panel.add(labelEndAngle, new GridBagConstraintsHelper(0, 8, 1, 1).setWeight(0,1).setFill(GridBagConstraints.NONE).setInsets(0,0,0,20).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldEndAngle, new GridBagConstraintsHelper(1,8,1,1).setWeight(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,20,0,0));
		setComponentVisible(new JComponent[]{labelWidth,labelHeight,textFieldWidth,textFieldHeight});
	}

	private void initListener() {
		comboBoxShapeType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					if (comboBoxShapeType.getSelectedItem().equals(RECTANGLE)) {
						setComponentVisible(new JComponent[]{labelWidth,labelHeight,textFieldWidth,textFieldHeight});
					} else if (comboBoxShapeType.getSelectedItem().equals(CIRCLE)) {
						setComponentVisible(new JComponent[]{labelRadius,textFieldRadius});
					} else if (comboBoxShapeType.getSelectedItem().equals(ANNULUS)) {
						setComponentVisible(new JComponent[]{labelInnerRadius,labelOuterRadius,textFieldInnerRadius,textFieldOuterRadius});
					} else if (comboBoxShapeType.getSelectedItem().equals(WEDGE)) {
						setComponentVisible(new JComponent[]{labelRadius,labelStartAngle,labelEndAngle,textFieldRadius,textFieldStartAngle,textFieldEndAngle});
					}
					resetNeighbourShape();
					parameterShapeType.setSelectedItem(neighbourShape);
					isSelectingItem = false;
				}
			}
		});
		textFieldWidth.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldWidth.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		textFieldHeight.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldHeight.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		textFieldRadius.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldRadius.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		textFieldInnerRadius.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldInnerRadius.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		textFieldOuterRadius.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldOuterRadius.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		textFieldStartAngle.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldStartAngle.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		textFieldEndAngle.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldEndAngle.getText())) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
		comboBoxUnitType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					resetNeighbourShape();
					isSelectingItem = false;
				}
			}
		});
	}

	private void initComponentState() {
		comboBoxUnitType.addItem(UNIT_TYPE_CELL);
		comboBoxUnitType.addItem(UNIT_TYPE_MAP);
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
		resetNeighbourShape();
	}

	private void setComponentVisible(JComponent[] components) {
		labelWidth.setVisible(false);
		labelHeight.setVisible(false);
		labelRadius.setVisible(false);
		labelInnerRadius.setVisible(false);
		labelOuterRadius.setVisible(false);
		labelStartAngle.setVisible(false);
		labelEndAngle.setVisible(false);
		textFieldWidth.setVisible(false);
		textFieldHeight.setVisible(false);
		textFieldRadius.setVisible(false);
		textFieldInnerRadius.setVisible(false);
		textFieldOuterRadius.setVisible(false);
		textFieldStartAngle.setVisible(false);
		textFieldEndAngle.setVisible(false);
		for (JComponent component : components) {
			component.setVisible(true);
		}
	}

	private void resetNeighbourShape() {
		if (comboBoxShapeType.getSelectedItem().equals(RECTANGLE)) {
			neighbourShape = new NeighbourShapeRectangle();
			((NeighbourShapeRectangle) neighbourShape).setWidth(Double.valueOf(textFieldWidth.getText().toString()));
			((NeighbourShapeRectangle) neighbourShape).setHeight(Double.valueOf(textFieldHeight.getText().toString()));
		} else if (comboBoxShapeType.getSelectedItem().equals(CIRCLE)) {
			neighbourShape = new NeighbourShapeCircle();
			((NeighbourShapeCircle) neighbourShape).setRadius(Double.valueOf(textFieldRadius.getText().toString()));
		} else if (comboBoxShapeType.getSelectedItem().equals(ANNULUS)) {
			neighbourShape = new NeighbourShapeAnnulus();
			((NeighbourShapeAnnulus) neighbourShape).setInnerRadius(Double.valueOf(textFieldInnerRadius.getText().toString()));
			((NeighbourShapeAnnulus) neighbourShape).setOuterRadius(Double.valueOf(textFieldOuterRadius.getText().toString()));
		} else if (comboBoxShapeType.getSelectedItem().equals(WEDGE)) {
			neighbourShape = new NeighbourShapeWedge();
			((NeighbourShapeWedge) neighbourShape).setRadius(Double.valueOf(textFieldRadius.getText().toString()));
			((NeighbourShapeWedge) neighbourShape).setStartAngle(Double.valueOf(textFieldStartAngle.getText().toString()));
			((NeighbourShapeWedge) neighbourShape).setEndAngle(Double.valueOf(textFieldEndAngle.getText().toString()));
		}
		if (comboBoxUnitType.getSelectedItem().equals(UNIT_TYPE_CELL)) {
			neighbourShape.setUnitType(NeighbourUnitType.CELL);
		} else {
			neighbourShape.setUnitType(NeighbourUnitType.MAP);
		}
		parameterShapeType.setSelectedItem(neighbourShape);
	}
}
