package com.supermap.desktop.geometry.Implements;

import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.desktop.geometry.Abstract.IGeometry;

public class DGeometryFactory {
	public static IGeometry create(Geometry geometry) {
		if (geometry != null) {
			GeometryType type = geometry.getType();

			if (type == GeometryType.GEOTEXT) {
				return new DGeoText(geometry);
			}
			if (type == GeometryType.GEOREGION) {
				return new DGeoRegion(geometry);
			}
			if (type == GeometryType.GEORECTANGLE) {
				return new DGeoRectangle(geometry);
			}
			if (type == GeometryType.GEOROUNDRECTANGLE) {
				return new DGeoRoundRectangle(geometry);
			}
			if (type == GeometryType.GEOCIRCLE) {
				return new DGeoCircle(geometry);
			}
			if (type == GeometryType.GEOELLIPSE) {
				return new DGeoEllipse(geometry);
			}
			if (type == GeometryType.GEOPIE) {
				return new DGeoPie(geometry);
			}
			if (type == GeometryType.GEOLINE) {
				return new DGeoLine(geometry);
			}
			if (type == GeometryType.GEOARC) {
				return new DGeoArc(geometry);
			}
			if (type == GeometryType.GEOELLIPTICARC) {
				return new DGeoEllipticArc(geometry);
			}
			if (type == GeometryType.GEOCURVE) {
				return new DGeoCurve(geometry);
			}
			if (type == GeometryType.GEOBSPLINE) {
				return new DGeoBSpline(geometry);
			}
			if (type == GeometryType.GEOLINEM) {
				return new DGeoLineM(geometry);
			}
			if (type == GeometryType.GEOCARDINAL) {
				return new DGeoCardinal(geometry);
			}
			if (type == GeometryType.GEOCHORD) {
				return new DGeoChord(geometry);
			}
		}
		return null;
	}
}
