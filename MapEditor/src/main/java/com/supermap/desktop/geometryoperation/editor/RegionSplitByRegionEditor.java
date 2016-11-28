package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

public class RegionSplitByRegionEditor extends RegionSplitEditor{

	public String getTagTip(){
		return "Tag_RegionSplitByRegionTracking";
	}

	public String getSplitTip(){
		return "String_GeometryOperation_RegionSplitByRegion";
	}

	public Action getMapControlAction(){
		return Action.CREATEPOLYGON;
	}

	public  TrackMode getTrackMode(){
		return TrackMode.TRACK;
	}

	public boolean runSplit(GeoRegion sourceRegion,Geometry splitGeometry,GeoRegion resultRegion1,GeoRegion resultRegion2){
		GeoRegion splitRegion=(GeoRegion)splitGeometry;
		boolean resultSplit = Geometrist.splitRegion(sourceRegion,splitRegion, resultRegion1, resultRegion2);
		return  resultSplit;
	}
}
