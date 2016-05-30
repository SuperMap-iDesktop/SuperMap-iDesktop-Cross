package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IRegion3DFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
public class GeometryNodeVectorTableModel extends DefaultTableModel {
	private IGeometry geometry;
	private VectorTableModel vectorTableModel = null;
	private boolean isCellEditable = false;

	public GeometryNodeVectorTableModel(IGeometry geometry) {
		this.geometry = geometry;
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
		if (geometry instanceof IRegionFeature || geometry instanceof IRegion3DFeature) {
			// 面图形，第一个点和最后一个点是相同的
			if (row == 0) {
				vectorTableModel.doSetValueAt(aValue, getRowCount() - 1, column);
			}
			if (row == getRowCount() - 1) {
				vectorTableModel.doSetValueAt(aValue, 0, column);
			}
		}
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
		return column != 0 && isCellEditable;
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
		// TODO: 2016/5/30
		vectorTableModel.doAddPoint(selectedRow);
		fireTableDataChanged();
	}

	public void insertPoint(int selectedRow) {
		vectorTableModel.doInsertPoint(selectedRow);
		fireTableDataChanged();

	}

	public void removeRows(int[] selectedRows) {
		vectorTableModel.doRemoveRows(selectedRows);
		if (geometry instanceof IRegionFeature || geometry instanceof IRegion3DFeature) {
			if (selectedRows[0] == 0) {
				for (int i = 1; i < vectorTableModel.getColumnCount() + 1; i++) {
					vectorTableModel.doSetValueAt(String.valueOf(vectorTableModel.getValueAt(0, i)), vectorTableModel.getRowCount() - 1, i);
				}
			}
		}
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

	public void setCellEditable(boolean cellEditable) {
		isCellEditable = cellEditable;
	}
}
