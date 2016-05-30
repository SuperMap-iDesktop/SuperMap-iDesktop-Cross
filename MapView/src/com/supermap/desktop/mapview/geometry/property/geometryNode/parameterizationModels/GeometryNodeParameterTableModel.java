package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.desktop.geometry.Abstract.IGeometry;

import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
public class GeometryNodeParameterTableModel extends DefaultTableModel {

	private IGeometry geometry;
	private GeometryParameterModel geometryParameterModel;

	public GeometryNodeParameterTableModel(IGeometry iGeometry) {
		geometry = iGeometry;
		geometryParameterModel = GeometryParameterizationFactory.getIGeometryPInstance(geometry);
		init();
	}

	private void init() {
//		fireTableStructureChanged();
	}

	public void refreshData() {
		geometryParameterModel = GeometryParameterizationFactory.getIGeometryPInstance(geometry);
		init();
	}

	@Override
	public int getRowCount() {
		if (geometryParameterModel == null) {
			return 0;
		}
		return geometryParameterModel.getRowCount();
	}

	@Override
	public int getColumnCount() {
		if (geometryParameterModel == null) {
			return 2;
		}
		return geometryParameterModel.getColumnCount();
	}


	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getValue(row, column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return String.class;
		} else if (columnIndex == 1) {
			return Double.class;
		} else if (columnIndex == 2) {
			return Double.class;
		}
		return Object.class;
	}

	public Object getValueAtRow0(int row, int column, boolean isSelected) {
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getValueAtRow0(row, column, isSelected);
	}

	@Override
	public String getColumnName(int column) {
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getColumnName(column);
	}
}
