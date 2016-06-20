package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.GeoPoint;
import com.supermap.data.Recordset;

/**
 * @author XiaJT
 */
public class TableModelPoint extends VectorTableModel {
	private GeoPoint geoPointBase;
	private GeoPoint geoPoint;

	public TableModelPoint(GeoPoint dGeoPoint) {
		this.geoPointBase = dGeoPoint;
		this.geoPoint = dGeoPoint.clone();
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public void removeRows(int[] selectedRows) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertPoint(int selectedRow) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addPoint(int selectedRow) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			return geoPoint.getX();
		}
		if (column == 2) {
			return geoPoint.getY();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == 1) {
			geoPoint.setX(Double.valueOf(((String) aValue)));
		} else if (column == 2) {
			geoPoint.setY(Double.valueOf(((String) aValue)));
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	public void revert() {
		if (this.geoPoint != null) {
			this.geoPoint.dispose();
		}
		this.geoPoint = geoPointBase.clone();
	}

	@Override
	public void apply(Recordset recordset) {
		this.geoPointBase.setX(geoPoint.getX());
		this.geoPointBase.setY(geoPoint.getY());
		recordset.moveFirst();
		recordset.seekID(geoPointBase.getID());
		recordset.edit();
		recordset.setGeometry(geoPointBase);
		recordset.update();
	}

	@Override
	public void dispose() {
		geoPoint.dispose();
	}
}
