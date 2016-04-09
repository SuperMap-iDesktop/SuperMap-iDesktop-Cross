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
		throw new UnsupportedOperationException("unSuport" + symbolType.name());
	}

	/**
	 * 获取一个画点符号的点几何对象
	 *
	 * @return
	 */
	private static GeoPoint getPaintPoint() {
		GeoPoint paintPoint = new GeoPoint(24, 24);
		GeoStyle markerGeoStyle = new GeoStyle();
		markerGeoStyle.setMarkerSize(new Size2D(4, 4));
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
		point2Ds.add(new Point2D(5, 32));
		point2Ds.add(new Point2D(60, 32));
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
		point2Ds.add(new Point2D(5, 5));
		point2Ds.add(new Point2D(5, 75));
		point2Ds.add(new Point2D(75, 75));
		point2Ds.add(new Point2D(75, 5));
		GeoRegion paintregion = new GeoRegion(point2Ds);
		GeoStyle fillGeoStyle = new GeoStyle();
		fillGeoStyle.setFillForeColor(new Color(13, 80, 143));
		fillGeoStyle.setLineWidth(0.1);
		paintregion.setStyle(fillGeoStyle);
		return paintregion;
	}


}
