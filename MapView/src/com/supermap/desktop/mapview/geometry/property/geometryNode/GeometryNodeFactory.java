package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeoArc;
import com.supermap.desktop.geometry.Implements.DGeoBSpline;
import com.supermap.desktop.geometry.Implements.DGeoCardinal;
import com.supermap.desktop.geometry.Implements.DGeoChord;
import com.supermap.desktop.geometry.Implements.DGeoCircle;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeoCurve;
import com.supermap.desktop.geometry.Implements.DGeoEllipse;
import com.supermap.desktop.geometry.Implements.DGeoEllipticArc;
import com.supermap.desktop.geometry.Implements.DGeoPie;
import com.supermap.desktop.geometry.Implements.DGeoPoint;
import com.supermap.desktop.geometry.Implements.DGeoPoint3D;
import com.supermap.desktop.geometry.Implements.DGeoRectangle;
import com.supermap.desktop.geometry.Implements.DGeoRoundRectangle;

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
