package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
public class FormTransformationTableModel extends DefaultTableModel {

	private static final String[] columnNames = new String[]{
			CommonProperties.getString(CommonProperties.Index),
			DataEditorProperties.getString("String_TransformItem_OriginalX"),
			DataEditorProperties.getString("String_TransformItem_OriginalY"),
			DataEditorProperties.getString("String_TransformItem_ReferX"),
			DataEditorProperties.getString("String_TransformItem_ReferY"),
			DataEditorProperties.getString("String_TransformItem_ResidualX"),
			DataEditorProperties.getString("String_TransformItem_ResidualY"),
			DataEditorProperties.getString("String_TransformItem_ResidualTotal"),
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
