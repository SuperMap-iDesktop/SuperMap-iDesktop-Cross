package com.supermap.desktop.utilities;

import java.util.ArrayList;

import com.supermap.data.GeoCompound;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Point3D;
import com.supermap.data.Point3Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Abstract.ITextFeature;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeoText;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.mapping.Layer;

public class GeometryUtilities {

	private GeometryUtilities() {
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

	/**
	 * 将指定图层的选中对象做求交处理
	 * 
	 * @param layer
	 * @return
	 */
	public static Geometry intersect(Layer layer) {
		Geometry result = null;
		Recordset recordset = null;

		try {

			if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
				recordset = layer.getSelection().toRecordset();
				recordset.moveFirst();

				while (!recordset.isEOF()) {
					Geometry geometry = recordset.getGeometry();

					try {
						IGeometry dGeometry = DGeometryFactory.create(geometry); // 桌面对 Geometry 进行封装后的对象

						// 表明是面特性对象
						if (dGeometry instanceof IRegionFeature) {
							result = GeometryUtilities.intersetct(result, ((IRegionFeature) dGeometry).convertToRegion(60), true);
						}
						recordset.moveNext();
					} finally {
						if (geometry != null) {
							geometry.dispose();
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
		return result;
	}

	// @formatter:off
	/**
	 * 两个对象合并。
	 * @param geometry1
	 * @param geometry2
	 * @return
	 */
	// @formatter:on
	public static Geometry union(Geometry geometry1, Geometry geometry2, boolean isDispose) {
		try {
			if (geometry1 == null && geometry2 != null) {
				return geometry2.clone();
			}

			if (geometry1 != null && geometry2 == null) {
				return geometry1.clone();
			}

			if (geometry1 != null && geometry2 != null) {
				return Geometrist.union(geometry1, geometry2);
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

	/**
	 * 将指定图层的选中对象做合并处理
	 * 
	 * @param layer
	 * @return
	 */
	public static Geometry union(Layer layer) {
		Geometry result = null;
		Recordset recordset = null;

		try {

			if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
				recordset = layer.getSelection().toRecordset();
				recordset.moveFirst();

				while (!recordset.isEOF()) {
					Geometry geometry = recordset.getGeometry();

					try {
						IGeometry dGeometry = DGeometryFactory.create(geometry); // 桌面对 Geometry 进行封装后的对象

						// 表明是面特性对象
						if (dGeometry instanceof IRegionFeature) {
							result = GeometryUtilities.union(result, ((IRegionFeature) dGeometry).convertToRegion(60), true);
						}
						recordset.moveNext();
					} finally {
						if (geometry != null) {
							geometry.dispose();
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
		return result;
	}

	// @formatter:off
	/**
	 * 两个对象异或。
	 * @param geometry1
	 * @param geometry2
	 * @return
	 */
	// @formatter:on
	public static Geometry xor(Geometry geometry1, Geometry geometry2, boolean isDispose) {
		try {
			if (geometry1 == null && geometry2 != null) {
				return geometry2.clone();
			}

			if (geometry1 != null && geometry2 == null) {
				return geometry1.clone();
			}

			if (geometry1 != null && geometry2 != null) {
				return Geometrist.xOR(geometry1, geometry2);
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

	/**
	 * 将指定图层选中的几何对象组合到 geometry 中
	 * 
	 * @param target
	 * @param layer
	 * @return
	 */
	public static IGeometry combination(IGeometry target, Layer layer) {
		Recordset recordset = null;

		try {
			if (target instanceof IMultiPartFeature<?> && layer.getSelection() != null && layer.getSelection().getCount() > 0) {
				recordset = layer.getSelection().toRecordset();
				recordset.moveFirst();

				while (!recordset.isEOF()) {
					Geometry geometry = recordset.getGeometry();

					try {
						((IMultiPartFeature<?>) target).addPart(geometry);
					} finally {
						if (geometry != null) {
							geometry.dispose();
						}
					}
					recordset.moveNext();
				}

				// 设置文本风格
				if (target instanceof DGeoText) {
					recordset.seekID(layer.getSelection().get(0));
					Geometry geometry = recordset.getGeometry();
					if (geometry instanceof GeoText) {
						GeoText geoText = (GeoText) geometry;
						if (geoText.getTextStyle() != null) {
							((DGeoText) target).setTextStyle(geoText.getTextStyle().clone());
						}
					}
					geometry.dispose();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
		return target;
	}

	// @formatter:off
	/**
	 * 将 geometry 完全添加到 IMultiPartFeature 的子项中。
	 * 如果 geometry 是 GeoCompound，就把 geometry 完全分解，然后选取可以添加的部分添加为feature 的子项。
	 * 
	 * @param feature
	 * @param geometry
	 */
	// @formatter:on
	public static void addPartCompletely(IMultiPartFeature<?> feature, Geometry geometry) {
		if (feature != null && geometry != null) {
			if (geometry instanceof GeoCompound) {
				DGeoCompound compound = (DGeoCompound) DGeometryFactory.create(geometry);

				// 完全拆分 GeoCompound
				Geometry[] divideGeometries = compound.divide();

				for (int i = 0; i < divideGeometries.length; i++) {
					feature.addPart(divideGeometries[i]);
				}
			} else {
				feature.addPart(geometry);
			}
		}
	}

	/**
	 * 将指定的 Point2Ds 反序
	 * 
	 * @param point2Ds
	 * @return
	 */
	public static Point2Ds reverse(Point2Ds point2Ds) {
		Point2Ds result = null;

		if (point2Ds.getCount() > 0) {
			ArrayList<Point2D> point2DList = new ArrayList<>();
			ListUtilities.addArray(point2DList, point2Ds.toArray());
			ListUtilities.reverse(point2DList);
			result = new Point2Ds(point2DList.toArray(new Point2D[point2DList.size()]));
		}
		return result;
	}

	/**
	 * 将指定的 Point3Ds 反序
	 * 
	 * @param point3Ds
	 * @return
	 */
	public static Point3Ds reverse(Point3Ds point3Ds) {
		Point3Ds result = null;

		if (point3Ds.getCount() > 0) {
			ArrayList<Point3D> point3DList = new ArrayList<>();
			ListUtilities.addArray(point3DList, point3Ds.toArray());
			ListUtilities.reverse(point3DList);
			result = new Point3Ds(point3DList.toArray(new Point3D[point3DList.size()]));
		}
		return result;
	}
}
