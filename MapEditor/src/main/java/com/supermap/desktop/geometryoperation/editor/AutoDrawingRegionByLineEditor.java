package com.supermap.desktop.geometryoperation.editor;

import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

/**
 * @author lixiaoyao
 */
public class AutoDrawingRegionByLineEditor extends AutoDrawedRegionEditor {
	public String getTagTip() {
		return "Tag_AutoDrawingRegionByLine";
	}

	public String getDrawedTip() {
		return "string_GeometryOperation_AutoDrawingRegionByLine";
	}

	public Action getMapControlAction() {
		return Action.CREATEPOLYLINE;
	}

	public TrackMode getTrackMode() {
		return TrackMode.TRACK;
	}

	public Geometry[] runSuccessedRegion(Layer layer, Rectangle2D rectangle2D,Geometry geometry) {
		GeoRegion resultGeoRegion[] = null;
		try {
			if (geometry.getType() == GeometryType.GEOLINE) {
				resultGeoRegion = Generalization.autoCompletePolygon((DatasetVector) layer.getDataset(), rectangle2D, (GeoLine) geometry);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return resultGeoRegion;
	}
}
