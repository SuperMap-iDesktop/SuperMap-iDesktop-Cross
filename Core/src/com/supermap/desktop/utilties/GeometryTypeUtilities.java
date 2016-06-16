package com.supermap.desktop.utilties;

import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJT
 */
public class GeometryTypeUtilities {
	private GeometryTypeUtilities() {
		// 工具类不提供构造方法
	}

	public static String toString(GeometryType type) {
		String typeName = null;
		try {
			if (type == GeometryType.GEOARC) {
				typeName = CoreProperties.getString("String_GeometryType_GeoArc");
			} else if (type == GeometryType.GEOBOX) {
				typeName = CoreProperties.getString("String_GeometryType_GeoBox");
			} else if (type == GeometryType.GEOBSPLINE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoBSpline");
			} else if (type == GeometryType.GEOCARDINAL) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCardinal");
			} else if (type == GeometryType.GEOCHORD) {
				typeName = CoreProperties.getString("String_GeometryType_GeoChord");
			} else if (type == GeometryType.GEOCIRCLE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCircle");
			} else if (type == GeometryType.GEOCIRCLE3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCircle3D");
			} else if (type == GeometryType.GEOCOMPOUND) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCompound");
			} else if (type == GeometryType.GEOCONE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCone");
			} else if (type == GeometryType.GEOCURVE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCurve");
			} else if (type == GeometryType.GEOCYLINDER) {
				typeName = CoreProperties.getString("String_GeometryType_GeoCylinder");
			} else if (type == GeometryType.GEOELLIPSE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoEllipse");
			} else if (type == GeometryType.GEOELLIPSOID) {
				typeName = CoreProperties.getString("String_GeometryType_GeoEllipsoid");
			} else if (type == GeometryType.GEOELLIPTICARC) {
				typeName = CoreProperties.getString("String_GeometryType_GeoEllipticArc");
			} else if (type == GeometryType.GEOHEMISPHERE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoHemiSphere");
			} else if (type == GeometryType.GEOLINE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoLine");
			} else if (type == GeometryType.GEOLINE3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoLine3D");
			} else if (type == GeometryType.GEOLINEM) {
				typeName = CoreProperties.getString("String_GeometryType_GeoLineM");
			} else if (type == GeometryType.GEOMAP) {
				typeName = CoreProperties.getString("String_GeometryType_GeoMap");
			} else if (type == GeometryType.GEOMAPBORDER) {
				typeName = CoreProperties.getString("String_GeometryType_GeoMapBorder");
			} else if (type == GeometryType.GEOMAPSCALE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoMapScale");
			} else if (type == GeometryType.GEOMODEL) {
				typeName = CoreProperties.getString("String_GeometryType_GeoModel");
			} else if (type == GeometryType.GEONORTHARROW) {
				typeName = CoreProperties.getString("String_GeometryType_GeoNorthArrow");
			} else if (type == GeometryType.GEOPICTURE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPicture");
			} else if (type == GeometryType.GEOPICTURE3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPicture3D");
			} else if (type == GeometryType.GEOPIE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPie");
			} else if (type == GeometryType.GEOPIE3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPie3D");
			} else if (type == GeometryType.GEOPIECYLINDER) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPieCylinder");
			} else if (type == GeometryType.GEOPLACEMARK) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPlacemark");
			} else if (type == GeometryType.GEOPOINT) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPoint");
			} else if (type == GeometryType.GEOPOINT3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPoint3D");
			} else if (type == GeometryType.GEOPYRAMID) {
				typeName = CoreProperties.getString("String_GeometryType_GeoPyramid");
			} else if (type == GeometryType.GEORECTANGLE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoRectangle");
			} else if (type == GeometryType.GEOREGION) {
				typeName = CoreProperties.getString("String_GeometryType_GeoRegion");
			} else if (type == GeometryType.GEOREGION3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoRegion3D");
			} else if (type == GeometryType.GEOROUNDRECTANGLE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoRoundRectangle");
			} else if (type == GeometryType.GEOSPHERE) {
				typeName = CoreProperties.getString("String_GeometryType_GeoSphere");
			} else if (type == GeometryType.GEOTEXT) {
				typeName = CoreProperties.getString("String_GeometryType_GeoText");
			} else if (type == GeometryType.GEOTEXT3D) {
				typeName = CoreProperties.getString("String_GeometryType_GeoText3D");
			} else if (type == GeometryType.GEOPARAMETRICLINECOMPOUND) {
				typeName = CoreProperties.getString("String_GeometryType_GeoParametricLineCompound");
			} else if (type == GeometryType.GEOPARAMETRICREGIONCOMPOUND) {
				typeName = CoreProperties.getString("String_GeometryType_GeoParametricRegionCompound");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return typeName;
	}

}
