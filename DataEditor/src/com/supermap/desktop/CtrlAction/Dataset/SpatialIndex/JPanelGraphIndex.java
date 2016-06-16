package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilties.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 图库索引
 *
 * @author XiaJT
 */
public class JPanelGraphIndex extends JPanel {

	private SpatialIndexInfoPropertyListener propertyListener;

	private boolean isFireListener = true;

	private ButtonGroup buttonGroup = new ButtonGroup();
	// 字段索引
	private JRadioButton radioButtonFieldIndex = new JRadioButton();
	private JComboBox<String> comboBoxFieldIndex = new JComboBox<>();

	// 范围索引
	private JRadioButton radioButtonRangeIndex = new JRadioButton();
	private JLabel labelWidth = new JLabel();
	private JLabel labelHeight = new JLabel();
	private SmTextFieldLegit textFieldWidth = new SmTextFieldLegit();
	private SmTextFieldLegit textFieldHeight = new SmTextFieldLegit();

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
		ISmTextFieldLegit smTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return true;
				}
				Double aDouble;
				try {
					aDouble = Double.valueOf(textFieldValue);
				} catch (NumberFormatException e) {
					return false;
				}
				return aDouble >= 0;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};
		textFieldWidth.setSmTextFieldLegit(smTextFieldLegit);
		textFieldHeight.setSmTextFieldLegit(smTextFieldLegit);
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
		radioButtonRangeIndex.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!radioButtonRangeIndex.isSelected()) {
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

		FocusAdapter focusAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				fireSpatialIndexPropertyChanged(getPropertyName(e.getSource()), getPropertyValue(e.getSource()));
			}


		};
		this.textFieldWidth.addFocusListener(focusAdapter);
		this.textFieldHeight.addFocusListener(focusAdapter);

		this.comboBoxFieldIndex.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && isFireListener) {
					fireSpatialIndexPropertyChanged(getPropertyName(comboBoxFieldIndex), getPropertyValue(comboBoxFieldIndex));
				}
			}
		});

		KeyAdapter keyAdapter = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char keyChar = e.getKeyChar();
				if (keyChar != '-' && keyChar != '.' && (keyChar > '9' || keyChar < '0')) {
					e.consume();
				}
			}
		};

		this.textFieldHeight.addKeyListener(keyAdapter);
		this.textFieldWidth.addKeyListener(keyAdapter);
	}

	private String getPropertyName(Object source) {
		if (source == textFieldHeight) {
			return SpatialIndexInfoPropertyListener.TILE_HEIGHT;
		} else if (source == textFieldWidth) {
			return SpatialIndexInfoPropertyListener.TILE_WIDTH;
		} else if (source == comboBoxFieldIndex) {
			return SpatialIndexInfoPropertyListener.TILE_FIELD;
		} else return null;
	}

	private Object getPropertyValue(Object source) {
		if (source == textFieldHeight) {
			return textFieldHeight.getText();
		} else if (source == textFieldWidth) {
			return textFieldWidth.getText();
		} else if (source == comboBoxFieldIndex) {
			return comboBoxFieldIndex.getSelectedItem();
		} else return null;
	}

	private void fireSpatialIndexPropertyChanged(String propertyName, Object value) {
		if (this.propertyListener != null) {
			propertyListener.propertyChanged(propertyName, value);
		}
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

		try {
			isFireListener = false;
			if (StringUtilities.isNullOrEmpty(spatialIndexInfoTileField)) {
				this.comboBoxFieldIndex.setSelectedIndex(-1);
			} else {
				this.comboBoxFieldIndex.setSelectedItem(spatialIndexInfoTileField);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isFireListener = true;
		}
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

	/**
	 * 设置字段索引radioButton是否选中
	 *
	 * @param b 是否选中
	 */
	public void setRadioFieldSelected(boolean b) {
		if (b) {
			this.radioButtonFieldIndex.setSelected(true);
		} else {
			this.radioButtonRangeIndex.setSelected(true);
		}
	}

	public boolean isRadioFieldSelected() {
		return this.radioButtonFieldIndex.isSelected();
	}

	public boolean isRadioRangeSelected() {
		return this.radioButtonRangeIndex.isSelected();
	}
	public void setPropertyListener(SpatialIndexInfoPropertyListener propertyListener) {
		this.propertyListener = propertyListener;
	}
}
