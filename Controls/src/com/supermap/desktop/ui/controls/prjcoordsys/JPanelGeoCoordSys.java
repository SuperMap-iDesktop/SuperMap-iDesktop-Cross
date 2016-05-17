package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class JPanelGeoCoordSys extends JPanel {

	JLabel labelType = new JLabel();
	JComboBox comboBoxType = new JComboBox();

	// 大地参考系
	JPanel panelGeoDatum = new JPanel();
	JLabel labelGeoDatumType = new JLabel();
	JComboBox comboBoxGeoDatumType = new JComboBox();

	// 椭球参数
	JPanel panelGeoSpheroid = new JPanel();
	JLabel labelGeoSpheroidType = new JLabel();
	JComboBox comboBoxGeoSpheroidType = new JComboBox();
	JLabel labelAxis = new JLabel();
	SmTextFieldLegit textFieldAxis = new SmTextFieldLegit();
	JLabel labelFlatten = new JLabel();
	SmTextFieldLegit textFieldFlatten = new SmTextFieldLegit();

	// 中央经线
	JPanel panelCentralMeridian = new JPanel();
	JLabel labelCentralMeridianType = new JLabel();
	JComboBox comboBoxCentralMeridianType = new JComboBox();
	JLabel labelLongitude = new JLabel();
	SmTextFieldLegit textFieldLongitude = new SmTextFieldLegit();

	public JPanelGeoCoordSys() {
		initComponents();
		addListeners();
		initLayout();
		initResources();
		initComponentStates();
	}

	private void initComponents() {
		// TODO: 2016/5/17 初始化
	}

	private void addListeners() {

	}

	//region 初始化布局
	private void initLayout() {
		initPanelGeoDatum();
		initPanelCentralMeridian();
		this.setLayout(new GridBagLayout());
		this.add(labelType, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		this.add(comboBoxType, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(2, 0).setFill(GridBagConstraints.HORIZONTAL));

		this.add(panelGeoDatum, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(2, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(panelCentralMeridian, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(2, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initPanelGeoDatum() {
		initPanelGeoSpheroid();
		panelGeoDatum.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GeoDatum")));
		panelGeoDatum.setLayout(new GridBagLayout());
		panelGeoDatum.add(labelGeoDatumType, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1));
		panelGeoDatum.add(comboBoxGeoDatumType, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panelGeoDatum.add(panelGeoSpheroid, new GridBagConstraintsHelper(0, 1, 0, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
	}

	private void initPanelGeoSpheroid() {
		panelGeoSpheroid.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GeoSpheroid")));
		panelGeoSpheroid.setLayout(new GridBagLayout());
		panelGeoSpheroid.add(labelGeoSpheroidType, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		panelGeoSpheroid.add(comboBoxGeoSpheroidType, new GridBagConstraintsHelper(1, 0, 3, 1).setWeight(2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

		panelGeoSpheroid.add(labelAxis, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		panelGeoSpheroid.add(textFieldAxis, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		panelGeoSpheroid.add(labelFlatten, new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		panelGeoSpheroid.add(textFieldFlatten, new GridBagConstraintsHelper(3, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initPanelCentralMeridian() {
		panelCentralMeridian.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_PrjParameter_CenterMeridian")));
		panelCentralMeridian.setLayout(new GridBagLayout());
		panelCentralMeridian.add(labelCentralMeridianType, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panelCentralMeridian.add(comboBoxCentralMeridianType, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));

		panelCentralMeridian.add(labelLongitude, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panelCentralMeridian.add(textFieldLongitude, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));
	}

	//endregion

	private void initResources() {
		labelType.setText(ControlsProperties.getString("String_Label_Type"));
		labelCentralMeridianType.setText(ControlsProperties.getString("String_Label_Type"));
		labelGeoDatumType.setText(ControlsProperties.getString("String_Label_Type"));
		labelGeoSpheroidType.setText(ControlsProperties.getString("String_Label_Type"));
		labelAxis.setText(ControlsProperties.getString("String_Axis"));
		labelFlatten.setText(ControlsProperties.getString("String_Flatten"));
		labelLongitude.setText(ControlsProperties.getString("String_Label_LongitudeValue"));
	}

	private void initComponentStates() {

	}
}
