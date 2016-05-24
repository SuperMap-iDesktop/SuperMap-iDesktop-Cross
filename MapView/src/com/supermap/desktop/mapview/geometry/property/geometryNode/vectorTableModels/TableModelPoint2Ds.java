package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;

/**
 * @author XiaJT
 */
public class TableModelPoint2Ds extends VectorTableModel {
	private Point2Ds point2DsBase;
	private Point2Ds point2Ds;

	public TableModelPoint2Ds(Point2Ds data) {
		this.point2DsBase = data;
		this.point2Ds = this.point2DsBase.clone();
	}

	@Override
	public int getRowCount() {
		return point2Ds.getCount();
	}

	@Override
	public void removeRows(int[] selectedRows) {
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			point2Ds.remove(selectedRows[i]);
		}
	}

	@Override
	public void insertPoint(int selectedRow) {
		point2Ds.insert(selectedRow + 1, point2Ds.getItem(selectedRow));
	}

	@Override
	public void addPoint(int selectedRow) {
		point2Ds.insert(selectedRow + 1, new Point2D(0, 0));
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			return point2Ds.getItem(row).getX();
		} else if (column == 2) {
			return point2Ds.getItem(row).getY();
		}
		throw new UnsupportedOperationException(String.valueOf(column));
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		Point2D item = point2Ds.getItem(row);
		if (column == 1) {
			item.setX(Double.valueOf((String) aValue));
			point2Ds.setItem(row, item);
		} else if (column == 2) {
			item.setY(Double.valueOf(((String) aValue)));
			point2Ds.setItem(row, item);
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public void revert() {
		this.point2Ds = this.point2DsBase.clone();
	}

	@Override
	public void apply(Recordset recordset) {
		int count = point2DsBase.getCount();
		point2DsBase.addRange(point2Ds.toArray());
		point2DsBase.removeRange(0, count);
		recordset.moveFirst();
		recordset.seekID(id);
		IGeometry iGeometry = DGeometryFactory.create(recordset.getGeometry());
		((IMultiPartFeature) iGeometry).setPart(partIndex, point2DsBase);
		recordset.edit();
		recordset.setGeometry(iGeometry.getGeometry());
		recordset.update();
	}
}
