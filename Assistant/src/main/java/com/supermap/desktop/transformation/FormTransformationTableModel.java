package com.supermap.desktop.transformation;

import com.supermap.desktop.assistant.AssistantProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
public class FormTransformationTableModel extends DefaultTableModel {

	private static final String[] columnNames = new String[]{
			CommonProperties.getString(CommonProperties.Index),
			AssistantProperties.getString("String_TransformItem_OriginalX"),
			AssistantProperties.getString("String_TransformItem_OriginalY"),
			AssistantProperties.getString("String_TransformItem_ReferX"),
			AssistantProperties.getString("String_TransformItem_ReferY"),
			AssistantProperties.getString("String_TransformItem_ResidualX"),
			AssistantProperties.getString("String_TransformItem_ResidualY"),
			AssistantProperties.getString("String_TransformItem_ResidualTotal"),
	};

	@Override
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

}
