package com.supermap.desktop.utilties;

import com.supermap.data.GeoCompound;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometrist;
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

	/**
	 * 判断是否点对象
	 * 
	 * @param geometry
	 * @return
	 */
	public static boolean isPointGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof IPointFeature;
	}

	/**
	 * 判断是否面特性对象
	 * 
	 * @param geometry
	 * @return
	 */
	public static boolean isRegionGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof IRegionFeature;
	}

	/**
	 * 判断是否线特性对象
	 * 
	 * @param geometry
	 * @return
	 */
	public static boolean isLineGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof ILineFeature;
	}

	/**
	 * 判断是否文本特性对象
	 * 
	 * @param geometry
	 * @return
	 */
	public static boolean isTextGeometry(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);
		return dGeometry instanceof ITextFeature;
	}

	/**
	 * 设置几何对象风格
	 * 
	 * @param geometry
	 * @param style
	 */
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

	/**
	 * 判断复合对象是否包含面特性对象
	 * 
	 * @param geoCompound
	 * @return
	 */
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

	// @formatter:off
	/**
	 * 两个对象求交。
	 * @param geometry1
	 * @param geometry2
	 * @return
	 */
	// @formatter:on
	public static Geometry intersetct(Geometry geometry1, Geometry geometry2, boolean isDispose) {
		try {
			if (geometry1 == null && geometry2 != null) {
				return geometry2.clone();
			}

			if (geometry1 != null && geometry2 == null) {
				return geometry1.clone();
			}

			if (geometry1 != null && geometry2 != null) {
				return Geometrist.intersect(geometry1, geometry2);
			}

			return null;
		} finally {
			if (isDispose && geometry1 != null) {
				geometry1.dispose();
			}

			if (isDispose && geometry2 != null) {
				geometry2.dispose();
			}
		}
	}
}
