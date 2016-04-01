package com.supermap.desktop.utilties;

import com.supermap.data.GeoCompound;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Abstract.ITextFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;

public class GeometryUtilties {

	private GeometryUtilties() {
		// 工具类，不提供构造方法
	}

	public static boolean isPointGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof IPointFeature;
	}

	public static boolean isRegionGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof IRegionFeature;
	}

	public static boolean isLineGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof ILineFeature;
	}

	public static boolean isTextGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof ITextFeature;
	}

	public static void setGeometryStyle(Geometry geometry, GeoStyle style) {
		try {
			if (!(geometry instanceof GeoText || geometry instanceof GeoText3D)) {
				geometry.setStyle(style);
				if (geometry instanceof GeoCompound) {
					GeoCompound geoCompound = (GeoCompound) geometry;
					for (int i = 0; i < geoCompound.getPartCount(); i++) {
						setGeometryStyle(geoCompound.getPart(i), style);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public static boolean isCompoundContainRegion(GeoCompound geoCompound) {
		boolean result = false;
		try {
			if (geoCompound == null) {
			} else {
				for (int i = 0; i < geoCompound.getPartCount(); i++) {
					if (geoCompound.getPart(i) instanceof GeoCompound) {
						if (isCompoundContainRegion((GeoCompound) geoCompound.getPart(i))) {
							result = true;
							break;
						}
					} else if (isRegionGeometry(geoCompound.getPart(i))) {
						result = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
