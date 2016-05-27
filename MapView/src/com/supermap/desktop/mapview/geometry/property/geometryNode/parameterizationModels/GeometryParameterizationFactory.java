package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeoArc;
import com.supermap.desktop.geometry.Implements.DGeoCircle;
import com.supermap.desktop.geometry.Implements.DGeoEllipticArc;
import com.supermap.desktop.geometry.Implements.DGeoPie;
import com.supermap.desktop.geometry.Implements.DGeoRectangle;

/**
 * @author XiaJT
 */
public class GeometryParameterizationFactory {
	private GeometryParameterizationFactory() {

	}

	public static IGeometryP getIGeometryPInstance(IGeometry geometry) {
		if (geometry instanceof DGeoArc) {

		} else if (geometry instanceof DGeoCircle) {

		} else if (geometry instanceof DGeoRectangle) {

		} else if (geometry instanceof DGeoPie) {

		} else if (geometry instanceof DGeoEllipticArc) {

		}
		return null;
	}
}
