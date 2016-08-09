package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class GeoTypeProperties extends Properties {

	public static final String GEOTYPE = "GeoType";

	public static final String getString(String key) {
		return getString(GEOTYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String GEOARC = "String_GeoArc";
	public static final String GEOBOX = "String_GeoBox";
	public static final String GEOBSPLINE = "String_GeoBspline";
	public static final String GEOCARDINAL = "String_GeoCardinal";
	public static final String GEOCHORD = "String_GeoChord";
	public static final String GEOCIRCLE = "String_GeoCircle";
	public static final String GEOCIRCLE3D = "String_GeoCircle3D";
	public static final String GEOCOMPOUND = "String_GeoCompound";
	public static final String GEOCONE = "GeoCone";
	public static final String GEOCURVE = "String_GeoCurve";
	public static final String GEOCYLINDER = "String_GeoCylinder";
	public static final String GEOELLIPSE = "String_GeoEllipse";
	public static final String GEOELLIPSOID = "String_GeoEllipsoid";
	public static final String GEOELLIPTICARC = "String_GeoEllipticarc";
	public static final String GEOHEMISPHERE = "String_GeoHemisphere";
	public static final String GEOLEGEND = "String_GeoLegend";
	public static final String GEOLINE = "String_GeoLine";
	public static final String GEOLINE3D = "String_GeoLine3D";
	public static final String GEOLINEM = "String_GeoLineM";
	public static final String GEOMAP = "String_GeoMap";
	public static final String GEOMAPBORDER = "String_GeoMapBorder";
	public static final String GEOMAPSCALE = "String_GeoMapScale";
	public static final String GEOMODEL = "String_GeoModel";
	public static final String GEOMULTIPOINT = "String_GeoMultipoint";
	public static final String GEONORTHARROW = "String_GeoNorthArrow";
	public static final String GEOPARAMETRICLINE = "String_GeoParametricLine";
	public static final String GEOPARAMETRICLINECOMPOUND = "String_GeoParametricLineCompound";
	public static final String GEOPARAMETRICREGION = "String_GeoParametricRegion";
	public static final String GEOPARAMETRICREGIONCOMPOUND = "String_GeoParametricRegioncompound";
	public static final String GEOPARTICALE = "String_GeoPartical";
	public static final String GEOPICTURE = "String_GeoPicture";
	public static final String GEOPICTURE3D = "String_GeoPicture3D";
	public static final String GEOPIE = "String_GeoPie";
	public static final String GEOPIE3D = "String_GeoPie3D";
	public static final String GEOPIECYLINDER = "String_GeoPieCylinder";
	public static final String GEOPLACEMARK = "String_GeoPlacemark";
	public static final String GEOPOINT = "String_GeoPoint";
	public static final String GEOPOINT3D = "String_GeoPoint3D";
	public static final String GEOPYRAMID = "String_GeoPyramid";
	public static final String GEORECTANGLE = "String_GeoRectangle";
	public static final String GEOREGION = "String_GeoRegion";
	public static final String GEOREGION3D = "String_GeoRegion3D";
	public static final String GEOROUNDRECTANGLE = "String_GeoRoundRectangle";
	public static final String GEOSPHERE = "String_Geosphere";
	public static final String GEOTEXT = "String_GeoText";
	public static final String GEOTEXT3D = "String_GeoText3D";
}
