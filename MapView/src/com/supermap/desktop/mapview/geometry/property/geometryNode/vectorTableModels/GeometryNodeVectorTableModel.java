package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
public class GeometryNodeVectorTableModel extends DefaultTableModel {
	private VectorTableModel vectorTableModel = null;

	public GeometryNodeVectorTableModel() {

	}


	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0) {
			return row + 1;
		}
		return vectorTableModel.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		vectorTableModel.doSetValueAt(aValue, row, column);
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		// 索引+1
		if (vectorTableModel == null) {
			return 3;
		}
		return vectorTableModel.getColumnCount() + 1;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != 0;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return CoreProperties.getString("String_Number");
		} else if (column == 1) {
			return ControlsProperties.getString("String_DataGridViewColumn_X");
		} else if (column == 2) {
			return ControlsProperties.getString("String_DataGridViewColumn_Y");
		} else if (column == 3) {
			return vectorTableModel.getColumnName();
		}
		throw new UnsupportedOperationException(String.valueOf(column));
	}


	@Override
	public int getRowCount() {
		if (vectorTableModel == null) {
			return 0;
		}
		return vectorTableModel.getRowCount();
	}

	public void addPoint(int selectedRow) {
		vectorTableModel.doAddPoint(selectedRow);
		fireTableDataChanged();
	}

	public void insertPoint(int selectedRow) {
		vectorTableModel.doInsertPoint(selectedRow);
		fireTableDataChanged();

	}

	public void removeRows(int[] selectedRows) {
		vectorTableModel.doRemoveRows(selectedRows);
		fireTableDataChanged();
	}

	public void reset() {
		vectorTableModel.doRevert();
		fireTableDataChanged();
	}

	public void apply(Recordset recordset) {
		vectorTableModel.doApply(recordset);
		fireTableDataChanged();
	}

	public void setModel(VectorTableModel vectorTableModel) {
		this.vectorTableModel = vectorTableModel;
		fireTableStructureChanged();
	}
}
