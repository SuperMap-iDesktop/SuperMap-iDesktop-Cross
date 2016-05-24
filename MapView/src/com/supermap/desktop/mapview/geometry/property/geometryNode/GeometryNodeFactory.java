package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeoPoint;
import com.supermap.desktop.geometry.Implements.DGeoPoint3D;

/**
 * @author XiaJT
 */
public class GeometryNodeFactory {
	private GeometryNodeFactory() {

	}

	public static IGeometryNode getGeometryNode(IGeometry geometry) {
		if (geometry instanceof DGeoCompound) {
			// FIXME: 2016/5/23
			return null;
		}
		if (geometry instanceof DGeoPoint3D || geometry instanceof DGeoPoint || geometry instanceof IMultiPartFeature) {
			return new JPanelGeometryNodeVector(geometry);
		}
		return null;
	}
}
