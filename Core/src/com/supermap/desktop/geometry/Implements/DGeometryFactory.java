package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoArc;
import com.supermap.data.GeoBSpline;
import com.supermap.data.GeoCardinal;
import com.supermap.data.GeoChord;
import com.supermap.data.GeoCircle;
import com.supermap.data.GeoCurve;
import com.supermap.data.GeoEllipse;
import com.supermap.data.GeoEllipticArc;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoPie;
import com.supermap.data.GeoRectangle;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRoundRectangle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.desktop.geometry.Abstract.IGeometry;

public class DGeometryFactory {
	public static IGeometry create(Geometry geometry) {
		if (geometry != null) {
			GeometryType type = geometry.getType();

			if (type == GeometryType.GEOTEXT) {
				return new DGeoText((GeoText) geometry);
			}
			if (type == GeometryType.GEOREGION) {
				return new DGeoRegion((GeoRegion) geometry);
			}
			if (type == GeometryType.GEORECTANGLE) {
				return new DGeoRectangle((GeoRectangle) geometry);
			}
			if (type == GeometryType.GEOROUNDRECTANGLE) {
				return new DGeoRoundRectangle((GeoRoundRectangle) geometry);
			}
			if (type == GeometryType.GEOCIRCLE) {
				return new DGeoCircle((GeoCircle) geometry);
			}
			if (type == GeometryType.GEOELLIPSE) {
				return new DGeoEllipse((GeoEllipse) geometry);
			}
			if (type == GeometryType.GEOPIE) {
				return new DGeoPie((GeoPie) geometry);
			}
			if (type == GeometryType.GEOLINE) {
				return new DGeoLine((GeoLine) geometry);
			}
			if (type == GeometryType.GEOARC) {
				return new DGeoArc((GeoArc) geometry);
			}
			if (type == GeometryType.GEOELLIPTICARC) {
				return new DGeoEllipticArc((GeoEllipticArc) geometry);
			}
			if (type == GeometryType.GEOCURVE) {
				return new DGeoCurve((GeoCurve) geometry);
			}
			if (type == GeometryType.GEOBSPLINE) {
				return new DGeoBSpline((GeoBSpline) geometry);
			}
			if (type == GeometryType.GEOLINEM) {
				return new DGeoLineM((GeoLineM) geometry);
			}
			if (type == GeometryType.GEOCARDINAL) {
				return new DGeoCardinal((GeoCardinal) geometry);
			}
			if (type == GeometryType.GEOCHORD) {
				return new DGeoChord((GeoChord) geometry);
			}
		}
		return null;
	}
}
