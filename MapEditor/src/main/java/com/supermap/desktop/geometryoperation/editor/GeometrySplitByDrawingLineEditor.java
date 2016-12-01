package com.supermap.desktop.geometryoperation.editor;

/**
 * @author Cooler——lixiaoyao
 */

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

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

	public boolean runSplitRegion(GeoRegion sourceRegion, Geometry splitGeometry, GeoRegion resultRegion1, GeoRegion resultRegion2) {
		GeoLine splitLine = (GeoLine) splitGeometry;
		boolean resultSplit = Geometrist.splitRegion(sourceRegion, splitLine, resultRegion1, resultRegion2);
		return resultSplit;
	}

	public GeoLine[] runSplitLine(GeoLine sourceLine, Geometry splitGeometry, Double tolerance) {
		GeoLine splitLine = (GeoLine) splitGeometry;
		GeoLine resultGeolines[] = null;
		resultGeolines = Geometrist.splitLine(sourceLine, splitLine, tolerance);

		return resultGeolines;
	}
}
