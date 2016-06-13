package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.PointM;
import com.supermap.data.PointMs;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJT
 */
public class TableModelPointMs extends VectorTableModel {
	private PointMs pointMsBase;
	private PointMs pointMs;

	public TableModelPointMs(PointMs data) {
		this.pointMsBase = data;
		this.pointMs = this.pointMsBase.clone();
	}

	@Override
	public int getRowCount() {
		return pointMs.getCount();
	}

	@Override
	public void removeRows(int[] selectedRows) {
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			pointMs.remove(selectedRows[i]);
		}
	}

	@Override
	public void insertPoint(int selectedRow) {
		pointMs.insert(selectedRow + 1, pointMs.getItem(selectedRow));
	}

	@Override
	public void addPoint(int selectedRow) {
		pointMs.insert(selectedRow + 1, new PointM(0, 0, 0));
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			return pointMs.getItem(row).getX();
		} else if (column == 2) {
			return pointMs.getItem(row).getY();
		} else if (column == 3) {
			return pointMs.getItem(row).getM();
		}
		throw new UnsupportedOperationException(String.valueOf(column));
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		PointM item = pointMs.getItem(row);
		if (column == 1) {
			item.setX(Double.valueOf((String) aValue));
		} else if (column == 2) {
			item.setY(Double.valueOf((String) aValue));
		} else if (column == 3) {
			item.setM(Double.valueOf((String) aValue));
		}
		pointMs.setItem(row, item);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public void revert() {
		pointMs = pointMsBase.clone();
	}

	@Override
	public void apply(Recordset recordset) {
		int count = pointMsBase.getCount();
		pointMsBase.addRange(pointMs.toArray());
		pointMsBase.removeRange(0, count);
		recordset.moveFirst();
		recordset.seekID(id);
		IGeometry iGeometry = DGeometryFactory.create(recordset.getGeometry());
		((IMultiPartFeature) iGeometry).setPart(partIndex, pointMsBase);
		recordset.edit();
		recordset.setGeometry(iGeometry.getGeometry());
		recordset.update();
	}

	@Override
	public String getColumnName() {
		return CoreProperties.getString("String_DataGridViewColumn_M");
	}

}
