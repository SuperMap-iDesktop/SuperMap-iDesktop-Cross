package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

import java.util.Map;

public class GeometrySplitByDrawingLineEditor extends GeometryDrawingSplitEditor {

	public String getTagTip() {
		return "Tag_lineSegmentationTracking";
	}

	public String getSplitTip() {
		return "String_GeometryOperation_RegionSplitByLine";
	}

	public Action getMapControlAction() {
		return Action.CREATEPOLYLINE;
	}

	public TrackMode getTrackMode() {
		return TrackMode.TRACK;
	}

	public  boolean splitGeometry(EditEnvironment environment,Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance){
		boolean result=false;
		IDrawingSplit geometrySplitByLine=DrawingSplitFactory.getGeometry(geometry);
		result=geometrySplitByLine.SplitGeometry(environment,geometry,splitGeometry,resultGeometry,values,geoStyle,tolerance);
		return result;
	}
}
