package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.*;
import com.supermap.desktop.geometry.Implements.DGeoPoint;
import com.supermap.desktop.geometry.Implements.DGeoPoint3D;

/**
 * @author XiaJT
 */
public class VectorTableModelFactory {
	private VectorTableModelFactory() {

	}

	public static VectorTableModel getVectorTableModel(Object data) {
		if (data instanceof Point2Ds) {
			return new TableModelPoint2Ds((Point2Ds) data);
		}
		if (data instanceof PointMs) {
			return new TableModelPointMs((PointMs) data);
		}
		if (data instanceof Point3Ds) {
			return new TableModelPoint3Ds((Point3Ds) data);
		}
		if (data instanceof TextPart) {
			return new TableModelTextPart((TextPart) data);
		}
		if (data instanceof DGeoPoint) {
			return new TableModelPoint(((GeoPoint) ((DGeoPoint) data).getGeometry()));
		}
		if (data instanceof DGeoPoint3D) {
			return new TableModelPoint3D(((GeoPoint3D) ((DGeoPoint3D) data).getGeometry()));
		}
		throw new UnsupportedOperationException(data.getClass().getName());
	}
}
