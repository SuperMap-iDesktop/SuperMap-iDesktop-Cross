package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.Point3D;
import com.supermap.data.Point3Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;

/**
 * @author XiaJT
 */
public class TableModelPoint3Ds extends VectorTableModel {
	private Point3Ds point3DsBase;
	private Point3Ds point3Ds;

	public TableModelPoint3Ds(Point3Ds data) {
		this.point3DsBase = data;
		this.point3Ds = point3DsBase.clone();
	}

	@Override
	public int getRowCount() {
		return point3Ds.getCount();
	}

	@Override
	public void removeRows(int[] selectedRows) {
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			point3Ds.remove(selectedRows[i]);
		}
	}

	@Override
	public void insertPoint(int selectedRow) {
		point3Ds.insert(selectedRow + 1, point3Ds.getItem(selectedRow));
	}

	@Override
	public void addPoint(int selectedRow) {
		point3Ds.insert(selectedRow + 1, new Point3D(0, 0, 0));
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			return point3Ds.getItem(row).getX();
		} else if (column == 2) {
			return point3Ds.getItem(row).getY();
		} else if (column == 3) {
			return point3Ds.getItem(row).getZ();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		Point3D item = point3Ds.getItem(row);
		if (column == 1) {
			item.setX(Double.valueOf(((String) aValue)));
			point3Ds.setItem(row, item);
		} else if (column == 2) {
			item.setY(Double.valueOf(((String) aValue)));
			point3Ds.setItem(row, item);
		} else if (column == 3) {
			item.setZ(Double.valueOf(((String) aValue)));
			point3Ds.setItem(row, item);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public void revert() {
		this.point3Ds = point3DsBase.clone();
	}

	@Override
	public void apply(Recordset recordset) {
		int count = point3DsBase.getCount();
		point3DsBase.addRange(point3Ds.toArray());
		point3DsBase.removeRange(0, count);
		recordset.moveFirst();
		recordset.seekID(id);
		IGeometry iGeometry = DGeometryFactory.create(recordset.getGeometry());
		((IMultiPartFeature) iGeometry).setPart(partIndex, point3DsBase);
		recordset.edit();
		recordset.setGeometry(iGeometry.getGeometry());
		recordset.update();
	}
}
