package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
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

	public  boolean splitGeometry(Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance){
		boolean result=false;
		if (geometry.getType()== GeometryType.GEOLINE){
			IDrawingSplit lineSplit=new LineSplitByDrawing();
			result=lineSplit.SplitGeometry(geometry,splitGeometry,resultGeometry,values,geoStyle,tolerance);
		}
		else if (geometry.getType()== GeometryType.GEOREGION)
		{
			IDrawingSplit regionSplit=new RegionSplitByDrawing();
			result=regionSplit.SplitGeometry(geometry,splitGeometry,resultGeometry,values,geoStyle,tolerance);
		}
		return result;
	}
}
