package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

import java.util.Map;

public class GeometrySplitByDrawingRegionEditor extends GeometryDrawingSplitEditor {

	public String getTagTip() {
		return "Tag_RegionSplitByRegionTracking";
	}

	public String getSplitTip() {
		return "String_GeometryOperation_RegionSplitByRegion";
	}

	public Action getMapControlAction() {
		return Action.CREATEPOLYGON;
	}

	public TrackMode getTrackMode() {
		return TrackMode.TRACK;
	}

	public  boolean splitGeometry(Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance){
		boolean result=false;
		IDrawingSplit geometrySplitByRegion=DrawingSplitFactory.getGeometry(geometry);
		result=geometrySplitByRegion.SplitGeometry(geometry,splitGeometry,resultGeometry,values,geoStyle,tolerance);
		return result;
	}

}
