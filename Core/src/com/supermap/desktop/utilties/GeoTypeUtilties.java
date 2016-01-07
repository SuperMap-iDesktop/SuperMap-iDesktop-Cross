package com.supermap.desktop.utilties;

import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.GeoTypeProperties;

public class GeoTypeUtilties {

	private GeoTypeUtilties() {
		// 工具类不提供构造函数
	}

	public static String toString(GeometryType data) {
		String result = "";
		if (data == GeometryType.GEOARC) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOARC);
		} else if (data == GeometryType.GEOBOX) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOBOX);
		} else if (data == GeometryType.GEOBSPLINE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOBSPLINE);
		} else if (data == GeometryType.GEOCARDINAL) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCARDINAL);
		} else if (data == GeometryType.GEOCHORD) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCHORD);
		} else if (data == GeometryType.GEOCIRCLE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCIRCLE);
		} else if (data == GeometryType.GEOCIRCLE3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCIRCLE3D);
		} else if (data == GeometryType.GEOCOMPOUND) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCOMPOUND);
		} else if (data == GeometryType.GEOCONE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCONE);
		} else if (data == GeometryType.GEOCURVE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCURVE);
		} else if (data == GeometryType.GEOCYLINDER) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOCYLINDER);
		} else if (data == GeometryType.GEOELLIPSE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOELLIPSE);
		} else if (data == GeometryType.GEOELLIPSOID) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOELLIPSOID);
		} else if (data == GeometryType.GEOELLIPTICARC) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOELLIPTICARC);
		} else if (data == GeometryType.GEOHEMISPHERE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOHEMISPHERE);
		} else if (data == GeometryType.GEOLEGEND) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOLEGEND);
		} else if (data == GeometryType.GEOLINE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOLINE);
		} else if (data == GeometryType.GEOLINE3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOLINE3D);
		} else if (data == GeometryType.GEOLINEM) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOLINEM);
		} else if (data == GeometryType.GEOMAP) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOMAP);
		} else if (data == GeometryType.GEOMAPBORDER) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOMAPBORDER);
		} else if (data == GeometryType.GEOMAPSCALE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOMAPSCALE);
		} else if (data == GeometryType.GEOMODEL) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOMODEL);
		} else if (data == GeometryType.GEOMULTIPOINT) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOMULTIPOINT);
		} else if (data == GeometryType.GEONORTHARROW) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEONORTHARROW);
		} else if (data == GeometryType.GEOPARAMETRICLINE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICLINE);
		} else if (data == GeometryType.GEOPARAMETRICLINECOMPOUND) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICLINECOMPOUND);
		} else if (data == GeometryType.GEOPARAMETRICREGION) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICREGION);
		} else if (data == GeometryType.GEOPARAMETRICREGIONCOMPOUND) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICREGIONCOMPOUND);
		} else if (data == GeometryType.GEOPARTICLE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPARTICALE);
		} else if (data == GeometryType.GEOPICTURE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPICTURE);
		} else if (data == GeometryType.GEOPICTURE3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPICTURE3D);
		} else if (data == GeometryType.GEOPIE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPIE);
		} else if (data == GeometryType.GEOPIE3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPIE3D);
		} else if (data == GeometryType.GEOPIECYLINDER) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPIECYLINDER);
		} else if (data == GeometryType.GEOPLACEMARK) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPLACEMARK);
		} else if (data == GeometryType.GEOPOINT) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPOINT);
		} else if (data == GeometryType.GEOPOINT3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPOINT3D);
		} else if (data == GeometryType.GEOPYRAMID) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOPYRAMID);
		} else if (data == GeometryType.GEORECTANGLE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEORECTANGLE);
		} else if (data == GeometryType.GEOREGION) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOREGION);
		} else if (data == GeometryType.GEOREGION3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOREGION3D);
		} else if (data == GeometryType.GEOROUNDRECTANGLE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOROUNDRECTANGLE);
		} else if (data == GeometryType.GEOSPHERE) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOSPHERE);
		} else if (data == GeometryType.GEOTEXT) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOTEXT);
		} else if (data == GeometryType.GEOTEXT3D) {
			result = GeoTypeProperties.getString(GeoTypeProperties.GEOTEXT3D);
		}
		return result;
	}

	public static GeometryType valueof(String text) {
		GeometryType result = GeometryType.GEOARC;
		try {
			if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOARC))) {
				result = GeometryType.GEOARC;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOBOX))) {
				result = GeometryType.GEOBOX;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOBSPLINE))) {
				result = GeometryType.GEOBSPLINE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCARDINAL))) {
				result = GeometryType.GEOCARDINAL;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCHORD))) {
				result = GeometryType.GEOCHORD;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCIRCLE))) {
				result = GeometryType.GEOCIRCLE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCIRCLE3D))) {
				result = GeometryType.GEOCIRCLE3D;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCOMPOUND))) {
				result = GeometryType.GEOCOMPOUND;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCONE))) {
				result = GeometryType.GEOCONE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCURVE))) {
				result = GeometryType.GEOCURVE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOCYLINDER))) {
				result = GeometryType.GEOCYLINDER;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOELLIPSE))) {
				result = GeometryType.GEOELLIPSE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOELLIPSOID))) {
				result = GeometryType.GEOELLIPSOID;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOELLIPTICARC))) {
				result = GeometryType.GEOELLIPTICARC;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOHEMISPHERE))) {
				result = GeometryType.GEOHEMISPHERE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOLEGEND))) {
				result = GeometryType.GEOLEGEND;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOLINE))) {
				result = GeometryType.GEOLINE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOLINE3D))) {
				result = GeometryType.GEOLINE3D;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOLINEM))) {
				result = GeometryType.GEOLINEM;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOMAP))) {
				result = GeometryType.GEOMAP;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOMAPBORDER))) {
				result = GeometryType.GEOMAPBORDER;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOMAPSCALE))) {
				result = GeometryType.GEOMAPSCALE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOMODEL))) {
				result = GeometryType.GEOMODEL;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOMULTIPOINT))) {
				result = GeometryType.GEOMULTIPOINT;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEONORTHARROW))) {
				result = GeometryType.GEONORTHARROW;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICLINE))) {
				result = GeometryType.GEOPARAMETRICLINE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICLINECOMPOUND))) {
				result = GeometryType.GEOPARAMETRICLINECOMPOUND;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICREGION))) {
				result = GeometryType.GEOPARAMETRICREGION;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPARAMETRICREGIONCOMPOUND))) {
				result = GeometryType.GEOPARAMETRICREGIONCOMPOUND;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPARTICALE))) {
				result = GeometryType.GEOPARTICLE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPICTURE))) {
				result = GeometryType.GEOPICTURE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPICTURE3D))) {
				result = GeometryType.GEOPICTURE3D;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPIE))) {
				result = GeometryType.GEOPIE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPIE3D))) {
				result = GeometryType.GEOPIE3D;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPIECYLINDER))) {
				result = GeometryType.GEOPIECYLINDER;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPLACEMARK))) {
				result = GeometryType.GEOPLACEMARK;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPOINT))) {
				result = GeometryType.GEOPOINT;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPOINT3D))) {
				result = GeometryType.GEOPOINT3D;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOPYRAMID))) {
				result = GeometryType.GEOPYRAMID;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEORECTANGLE))) {
				result = GeometryType.GEORECTANGLE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOREGION))) {
				result = GeometryType.GEOREGION;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOREGION3D))) {
				result = GeometryType.GEOREGION3D;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOROUNDRECTANGLE))) {
				result = GeometryType.GEOROUNDRECTANGLE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOSPHERE))) {
				result = GeometryType.GEOSPHERE;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOTEXT))) {
				result = GeometryType.GEOTEXT;
			} else if (text.equalsIgnoreCase(GeoTypeProperties.getString(GeoTypeProperties.GEOTEXT3D))) {
				result = GeometryType.GEOTEXT3D;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
