package com.supermap.desktop.spatialanalyst.vectoranalyst;

import javax.swing.JComboBox;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;

public class ComboBoxField {
	private Dataset dataset;
	private JComboBox<Object> comboBoxFieldControl;
	private JComboBox<Object> comboBoxFieldLeft;
	private JComboBox<Object> comboBoxFieldRight;

	public ComboBoxField(Dataset dataset, JComboBox<Object> comboBoxFieldControl) {
		this.dataset = dataset;
		this.comboBoxFieldControl = comboBoxFieldControl;
	}

	public ComboBoxField(Dataset dataset, JComboBox<Object> comboBoxFieldLeft, JComboBox<Object> comboBoxFieldRight) {
		this.dataset = dataset;
		this.comboBoxFieldLeft = comboBoxFieldLeft;
		this.comboBoxFieldRight = comboBoxFieldRight;
	}

	public void createComboBoxField(Dataset dataset, JComboBox<Object> comboBoxFieldControl) {
		if (this.dataset instanceof DatasetVector) {
			DatasetVector comboBoxDatasetVector = (DatasetVector) this.dataset;
			for (int i = 0; i < comboBoxDatasetVector.getFieldCount(); i++) {
				FieldInfo fieldInfo = comboBoxDatasetVector.getFieldInfos().get(i);
				if (!fieldInfo.isSystemField()) {
					if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.SINGLE
							|| fieldInfo.getType() == FieldType.DOUBLE) {
						this.comboBoxFieldControl.addItem(comboBoxDatasetVector.getFieldInfos().get(i).getName());
					}
				}
			}
		}
	}

	public void createComboBoxField(Dataset dataset, JComboBox<Object> comboBoxFieldLeft, JComboBox<Object> comboBoxFieldRight) {
		if (this.dataset instanceof DatasetVector) {
			DatasetVector comboBoxDatasetVector = (DatasetVector) this.dataset;
			for (int i = 0; i < comboBoxDatasetVector.getFieldCount(); i++) {
				FieldInfo fieldInfo = comboBoxDatasetVector.getFieldInfos().get(i);
				if (!fieldInfo.isSystemField()) {
					if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.SINGLE
							|| fieldInfo.getType() == FieldType.DOUBLE) {
						this.comboBoxFieldLeft.addItem(comboBoxDatasetVector.getFieldInfos().get(i).getName());
						this.comboBoxFieldRight.addItem(comboBoxDatasetVector.getFieldInfos().get(i).getName());
					}
				}
			}
		}
	}
}
