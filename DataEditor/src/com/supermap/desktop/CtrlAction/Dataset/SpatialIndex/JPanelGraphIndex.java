package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * 图库索引
 *
 * @author XiaJT
 */
public class JPanelGraphIndex extends JPanel {

	private ButtonGroup buttonGroup = new ButtonGroup();
	// 字段索引
	private JRadioButton radioButtonFieldIndex = new JRadioButton();
	private JComboBox<String> comboBoxFieldIndex = new JComboBox<>();

	// 范围索引
	private JRadioButton radioButtonRangeIndex = new JRadioButton();
	private JLabel labelWidth = new JLabel();
	private JLabel labelHeight = new JLabel();
	private JTextField textFieldWidth = new JTextField();
	private JTextField textFieldHeight = new JTextField();

	// 说明
	private JPanelDescribe panelDescribe = new JPanelDescribe();

	public JPanelGraphIndex() {
		initComponent();
		initLayout();
		addListeners();
		initComponentState();
		initResources();
		this.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_GroupBoxParameter")));

	}

	private void initComponent() {
		buttonGroup.add(radioButtonFieldIndex);
		buttonGroup.add(radioButtonRangeIndex);
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(radioButtonFieldIndex, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 5));
		this.add(comboBoxFieldIndex, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(2, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));

		this.add(radioButtonRangeIndex, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 5));
		this.add(new JPanel(), new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(2, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));

		this.add(labelWidth, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 5, 5));
		this.add(textFieldWidth, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(2, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));

		this.add(labelHeight, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 5, 5));
		this.add(textFieldHeight, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(2, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));

		this.add(panelDescribe, new GridBagConstraintsHelper(0, 5, 2, 1).setWeight(3, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void addListeners() {
		radioButtonRangeIndex.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (radioButtonFieldIndex.isSelected()) {
					comboBoxFieldIndex.setEnabled(true);
					textFieldWidth.setEnabled(false);
					textFieldHeight.setEnabled(false);
					panelDescribe.setDescirbe("    " + CoreProperties.getString("String_TileIndexDescription") + CoreProperties.getString("String_TileIndex_ByField"));
				} else {
					comboBoxFieldIndex.setEnabled(false);
					textFieldWidth.setEnabled(true);
					textFieldHeight.setEnabled(true);
					panelDescribe.setDescirbe("    " + CoreProperties.getString("String_TileIndexDescription") + CoreProperties.getString("String_TileIndex_ByBound"));
				}
			}
		});
	}

	private void initComponentState() {
		radioButtonRangeIndex.setSelected(true);
	}

	private void initResources() {
		this.radioButtonFieldIndex.setText(CoreProperties.getString("String_DatasetTileIndexControl_Field"));
		this.radioButtonRangeIndex.setText(CoreProperties.getString("String_DatasetTileIndexControl_Range"));
		this.labelHeight.setText(CoreProperties.getString("String_DatasetTileIndexControl_Height"));
		this.labelWidth.setText(CoreProperties.getString("String_Label_Width"));
	}


	public void setField(String spatialIndexInfoTileField) {
		this.comboBoxFieldIndex.setSelectedItem(spatialIndexInfoTileField);
	}

	public void setWidth(String spatialIndexInfoTileWidth) {
		this.textFieldWidth.setText(spatialIndexInfoTileWidth);
	}

	public void setHeight(String spatialIndexInfoTileHeight) {
		this.textFieldHeight.setText(spatialIndexInfoTileHeight);
	}

	public void setFieldModel(String[] commonFields) {
		this.comboBoxFieldIndex.setModel(new DefaultComboBoxModel<>(commonFields));
	}
}
