package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.data.GeoBSpline;
import com.supermap.data.GeoCardinal;
import com.supermap.data.GeoCurve;
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

	public static GeometryParameterModel getIGeometryPInstance(IGeometry geometry) {
		if (geometry instanceof DGeoArc) {
			//圆弧几何对象
			return new TableModelArc(geometry);
		} else if (geometry instanceof DGeoBSpline) {
			// 二维 B 样条曲线
			return new TableModelPoints(((GeoBSpline) geometry.getGeometry()).getControlPoints());
		} else if (geometry instanceof DGeoCardinal) {
			//  二维 Cardinal 样条
			return new TableModelPoints(((GeoCardinal) geometry.getGeometry()).getControlPoints());
		} else if (geometry instanceof DGeoChord) {
			// 弓形
			return new TableModelChord(geometry);
		} else if (geometry instanceof DGeoCircle) {
			// 圆
			return new TableModelCircle(geometry);
		} else if (geometry instanceof DGeoCurve) {
			// Curve
			return new TableModelPoints(((GeoCurve) geometry.getGeometry()).getControlPoints());
		} else if (geometry instanceof DGeoEllipse) {
			//椭圆
			return new TableModelEllipse(geometry);
		} else if (geometry instanceof DGeoEllipticArc) {
			// 椭圆弧
			return new TableModelEllipticArc(geometry);
		} else if (geometry instanceof DGeoPie) {
			// 扇面
			return new TableModelPie(geometry);
		} else if (geometry instanceof DGeoRectangle) {
			// 矩形
			return new TableModelRectangle(geometry);
		} else if (geometry instanceof DGeoRoundRectangle) {
			// 圆角矩形
			return new TableModelRoundRectangle(geometry);
		}
		return null;
	}
}
