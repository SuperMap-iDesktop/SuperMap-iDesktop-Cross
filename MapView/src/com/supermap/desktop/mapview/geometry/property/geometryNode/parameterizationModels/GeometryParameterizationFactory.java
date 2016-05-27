package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeoArc;
import com.supermap.desktop.geometry.Implements.DGeoBSpline;
import com.supermap.desktop.geometry.Implements.DGeoCardinal;
import com.supermap.desktop.geometry.Implements.DGeoChord;
import com.supermap.desktop.geometry.Implements.DGeoCircle;
import com.supermap.desktop.geometry.Implements.DGeoCurve;
import com.supermap.desktop.geometry.Implements.DGeoEllipse;
import com.supermap.desktop.geometry.Implements.DGeoEllipticArc;
import com.supermap.desktop.geometry.Implements.DGeoPie;
import com.supermap.desktop.geometry.Implements.DGeoRectangle;
import com.supermap.desktop.geometry.Implements.DGeoRoundRectangle;

/**
 * @author XiaJT
 */
public class GeometryParameterizationFactory {
	private GeometryParameterizationFactory() {

	}

	public static IGeometryP getIGeometryPInstance(IGeometry geometry) {
		if (geometry instanceof DGeoArc) {
			//圆弧几何对象

		} else if (geometry instanceof DGeoBSpline) {
			// 二维 B 样条曲线

		} else if (geometry instanceof DGeoCardinal) {
			//  二维 Cardinal 样条

		} else if (geometry instanceof DGeoChord) {
			// 弓形

		} else if (geometry instanceof DGeoCircle) {
			// 几何对象
		} else if (geometry instanceof DGeoCurve) {
			// 二维曲线几何对象
		} else if (geometry instanceof DGeoEllipse) {
			//椭圆
		} else if (geometry instanceof DGeoEllipticArc) {
			// 椭圆弧
		} else if (geometry instanceof DGeoPie) {
			// 扇面
		} else if (geometry instanceof DGeoRectangle) {
			// 矩形
		} else if (geometry instanceof DGeoRoundRectangle) {
			// 圆角矩形
		}
		return null;
	}
}
