package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.*;

/**
 * @author XiaJT
 */
public class GeometryNodeFactory {
	private GeometryNodeFactory() {

	}

	public static IGeometryNode getGeometryNode(IGeometry geometry) {
		if (geometry instanceof DGeoCompound) {
			return new JPanelGeometryCompound(geometry);
		}
		if (geometry instanceof DGeoPoint3D || geometry instanceof DGeoPoint || geometry instanceof IMultiPartFeature) {
			return new JPanelGeometryNodeVector(geometry);
		}
		if (geometry instanceof DGeoArc || geometry instanceof DGeoBSpline || geometry instanceof DGeoCardinal || geometry instanceof DGeoChord || geometry instanceof DGeoCircle
				|| geometry instanceof DGeoCurve || geometry instanceof DGeoEllipse || geometry instanceof DGeoEllipticArc || geometry instanceof DGeoPie
				|| geometry instanceof DGeoRectangle || geometry instanceof DGeoRoundRectangle) {
			return new JPanelGeometryNodeParameterization(geometry);
		}
		return new EmptyPanel();
	}

	public static boolean isSupportGeometry(IGeometry geometry) {
		if (geometry instanceof DGeoCompound) {
			return true;
		}
		if (geometry instanceof DGeoPoint3D || geometry instanceof DGeoPoint || geometry instanceof IMultiPartFeature) {
			return true;
		}
		if (geometry instanceof DGeoArc || geometry instanceof DGeoBSpline || geometry instanceof DGeoCardinal || geometry instanceof DGeoChord || geometry instanceof DGeoCircle
				|| geometry instanceof DGeoCurve || geometry instanceof DGeoEllipse || geometry instanceof DGeoEllipticArc || geometry instanceof DGeoPie
				|| geometry instanceof DGeoRectangle || geometry instanceof DGeoRoundRectangle) {
			return true;
		}
		return false;
	}
}
