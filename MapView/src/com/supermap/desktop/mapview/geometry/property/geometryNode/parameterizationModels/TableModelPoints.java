package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.data.Point2Ds;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJT
 */
public class TableModelPoints extends GeometryParameterModel {

	private Point2Ds controlPoints;

	private String[] columnNames = new String[]{
			CoreProperties.getString("String_Number"),
			ControlsProperties.getString("String_DataGridViewColumn_X"),
			ControlsProperties.getString("String_DataGridViewColumn_Y")
	};

	public TableModelPoints(Point2Ds controlPoints) {
		this.controlPoints = controlPoints;

	}


	@Override
	public Object getValue(int row, int column) {
		switch (column) {
			case 0:
				return String.valueOf(row + 1);
			case 1:
				return controlPoints.getItem(row).getX();
			case 2:
				return controlPoints.getItem(row).getY();
		}
		return "";
	}


	@Override
	public int getRowCount() {
		return controlPoints.getCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
}
