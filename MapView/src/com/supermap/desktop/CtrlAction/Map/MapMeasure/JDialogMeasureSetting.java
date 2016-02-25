package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.enums.AngleUnit;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrator on 2016/2/3.
 */
public class JDialogMeasureSetting extends SmDialog {

	private JLabel labelDistance;
	private JLabel labelArea;
	private JLabel labelAngle;

	private JComboBox comboBoxDistance;
	private JComboBox comboBoxArea;
	private JComboBox comboBoxAngle;

	private static final String[] distanceModel = new String[]{
			CommonProperties.getString("String_DistanceUnit_Millimeter"),
			CommonProperties.getString("String_DistanceUnit_Centimeter"),
			CommonProperties.getString("String_DistanceUnit_Inch"),
			CommonProperties.getString("String_DistanceUnit_Decimeter"),
			CommonProperties.getString("String_DistanceUnit_Foot"),
			CommonProperties.getString("String_DistanceUnit_Yard"),
			CommonProperties.getString("String_DistanceUnit_Meter"),
			CommonProperties.getString("String_DistanceUnit_Kilometer"),
			CommonProperties.getString("String_DistanceUnit_Mile")
	};

	private static final String[] areaModel = new String[]{
			CommonProperties.getString("String_AreaUnit_Millimeter"),
			CommonProperties.getString("String_AreaUnit_Centimeter"),
			CommonProperties.getString("String_AreaUnit_Inch"),
			CommonProperties.getString("String_AreaUnit_Decimeter"),
			CommonProperties.getString("String_AreaUnit_Foot"),
			CommonProperties.getString("String_AreaUnit_Yard"),
			CommonProperties.getString("String_AreaUnit_Meter"),
			CommonProperties.getString("String_AreaUnit_Kilometer"),
			CommonProperties.getString("String_AreaUnit_Mile"),
	};

	private static final String[] angleModel = new String[]{
			CoreProperties.getString("String_Degree_Format_Degree"),
			CoreProperties.getString("String_Degree_Format_DDMMSS"),
			CoreProperties.getString("String_Degree_Format_Radian")
	};

	private JPanel panelButton;
	private JButton buttonOK;
	private JButton buttonCancle;

	public JDialogMeasureSetting() {
		super();
		this.setSize(250, 200);
		this.setLocationRelativeTo(null);
		this.setTitle(MapViewProperties.getString("String_MeasureSetting"));

		initComponents();
		initLayout();
		initResources();
		addListeners();
		initComponentState();
	}

	private void initComponents() {
		this.labelDistance = new JLabel();
		this.labelArea = new JLabel();
		this.labelAngle = new JLabel();

		this.comboBoxDistance = new JComboBox();
		this.comboBoxDistance.setModel(new DefaultComboBoxModel(distanceModel));
		this.comboBoxArea = new JComboBox();
		this.comboBoxArea.setModel(new DefaultComboBoxModel(areaModel));
		this.comboBoxAngle = new JComboBox();
		this.comboBoxAngle.setModel(new DefaultComboBoxModel(angleModel));

		this.panelButton = new JPanel();
		this.buttonOK = new JButton();
		this.buttonCancle = new JButton();
	}

	private void initLayout() {
		initPanelButton();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(labelDistance, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5));
		panel.add(comboBoxDistance, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(99, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));

		panel.add(labelArea, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5));
		panel.add(comboBoxArea, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(99, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));

		panel.add(labelAngle, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5));
		panel.add(comboBoxAngle, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(99, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new GridBagLayout());
		contentPane.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 5, 10));
		contentPane.add(panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 10, 10, 10));
	}

	private void initPanelButton() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(99, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 0, 5));
		panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
	}

	private void initResources() {
		this.labelDistance.setText(MapViewProperties.getString("label_DistanceUnit"));
		this.labelArea.setText(MapViewProperties.getString("label_AreaUnit"));
		this.labelAngle.setText(MapViewProperties.getString("label_AngleUnit"));
		this.buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));

	}

	private void addListeners() {
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FormMap formMap = (FormMap) Application.getActiveApplication().getActiveForm();
				formMap.setLengthUnit(LengthUnit.getValueOf(comboBoxDistance.getSelectedItem().toString()));
				formMap.setAreaUnit(AreaUnit.getValueOf(comboBoxArea.getSelectedItem().toString()));
				formMap.setAngleUnit(AngleUnit.getvalueOf(comboBoxAngle.getSelectedItem().toString()));
				dispose();
			}
		});

		this.buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void initComponentState() {
		FormMap formMap = (FormMap) Application.getActiveApplication().getActiveForm();
		this.comboBoxDistance.setSelectedItem(formMap.getLengthUnit().toString());
		this.comboBoxArea.setSelectedItem(formMap.getAreaUnit().toString());
		this.comboBoxAngle.setSelectedItem(formMap.getAngleUnit().toString());
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
