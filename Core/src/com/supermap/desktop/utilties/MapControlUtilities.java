package com.supermap.desktop.utilties;

import com.supermap.ui.Action;
import com.supermap.ui.MapControl;

public class MapControlUtilities {

	private MapControlUtilities() {
		// 工具类，不提供构造方法
	}

	/**
	 * 指定 mapControl 是否正在创建几何对象
	 * 
	 * @param mapControl
	 * @return
	 */
	public static boolean isCreateGeometry(MapControl mapControl) {
		return mapControl.getAction() == Action.CREATE_ALONG_LINE_TEXT || mapControl.getAction() == Action.CREATE_ARC_3P
				|| mapControl.getAction() == Action.CREATE_CIRCLE_2P || mapControl.getAction() == Action.CREATE_CIRCLE_3P
				|| mapControl.getAction() == Action.CREATE_ELLIPSE_ARC || mapControl.getAction() == Action.CREATE_FREE_POLYLINE
				|| mapControl.getAction() == Action.CREATE_GEOLEGEND || mapControl.getAction() == Action.CREATE_MAPSCALE
				|| mapControl.getAction() == Action.CREATE_NORTHARROW || mapControl.getAction() == Action.CREATE_OBLIQUE_ELLIPSE
				|| mapControl.getAction() == Action.CREATE_POLYGON2_CARDINAL || mapControl.getAction() == Action.CREATE_POLYGON2_FREE_POLYLINE
				|| mapControl.getAction() == Action.CREATE_POLYGON2_POLYLINE || mapControl.getAction() == Action.CREATE_POLYLINE2_CARDINAL
				|| mapControl.getAction() == Action.CREATE_POLYLINE2_FREE_POLYLINE || mapControl.getAction() == Action.CREATE_POLYLINE2_POLYLINE
				|| mapControl.getAction() == Action.CREATE_ROUND_RECTANGLE || mapControl.getAction() == Action.CREATEBSPLINE
				|| mapControl.getAction() == Action.CREATECARDINAL || mapControl.getAction() == Action.CREATECIRCLE
				|| mapControl.getAction() == Action.CREATECURVE || mapControl.getAction() == Action.CREATEELLIPSE
				|| mapControl.getAction() == Action.CREATELINE || mapControl.getAction() == Action.CREATEPARALLEL
				|| mapControl.getAction() == Action.CREATEPARALLELOGRAM || mapControl.getAction() == Action.CREATEPIE
				|| mapControl.getAction() == Action.CREATEPOINT || mapControl.getAction() == Action.CREATEPOLYGON
				|| mapControl.getAction() == Action.CREATEPOLYLINE || mapControl.getAction() == Action.CREATERECTANGLE
				|| mapControl.getAction() == Action.CREATETEXT;
	}
}
