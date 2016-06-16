package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Size2D;
import com.supermap.data.SymbolType;

import java.awt.*;

/**
 * @author XiaJt
 */
public class SymbolPanelGeometryFactory {

	public static Geometry getPaintGeometry(SymbolType symbolType) {
		if (symbolType == SymbolType.MARKER || symbolType == SymbolType.MARKER3D) {
			return getPaintPoint();
		} else if (symbolType == SymbolType.LINE) {
			return getPaintLine();
		} else if (symbolType == SymbolType.FILL) {
			return getPaintRegion();
		}
		throw new UnsupportedOperationException("unSupport" + symbolType.name());
	}

	/**
	 * 获取一个画点符号的点几何对象
	 *
	 * @return
	 */
	private static GeoPoint getPaintPoint() {
		GeoPoint paintPoint = new GeoPoint(20, 20);
		GeoStyle markerGeoStyle = new GeoStyle();
		markerGeoStyle.setMarkerSize(new Size2D(10, 10));
		markerGeoStyle.setLineColor(new Color(13, 80, 143));
		paintPoint.setStyle(markerGeoStyle);
		return paintPoint;
	}

	/**
	 * 获取一个画线符号的线几何对象
	 *
	 * @return
	 */
	private static GeoLine getPaintLine() {
		// 初始化线对象并设置风格
		Point2Ds point2Ds = new Point2Ds();
		point2Ds.add(new Point2D(0, 30));
		point2Ds.add(new Point2D(60, 30));
		GeoLine paintLine = new GeoLine(point2Ds);
		GeoStyle lineGeoStyle = new GeoStyle();
		lineGeoStyle.setLineColor(new Color(13, 80, 143));
		lineGeoStyle.setLineWidth(0.2);
		paintLine.setStyle(lineGeoStyle);
		return paintLine;
	}

	/**
	 * 获取一个画面符号的面几何对象
	 *
	 * @return
	 */
	private static GeoRegion getPaintRegion() {
		Point2Ds point2Ds = new Point2Ds();
		point2Ds.add(new Point2D(0, 0));
		point2Ds.add(new Point2D(0, 59));
		point2Ds.add(new Point2D(59, 59));
		point2Ds.add(new Point2D(59, 0));
		GeoRegion paintregion = new GeoRegion(point2Ds);
		GeoStyle fillGeoStyle = new GeoStyle();
		fillGeoStyle.setFillForeColor(new Color(13, 80, 143));
		fillGeoStyle.setLineWidth(0.1);
		paintregion.setStyle(fillGeoStyle);
		return paintregion;
	}


}
