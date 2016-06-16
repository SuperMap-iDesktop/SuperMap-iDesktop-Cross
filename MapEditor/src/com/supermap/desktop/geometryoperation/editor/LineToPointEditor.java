package com.supermap.desktop.geometryoperation.editor;

import java.util.Map;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.ListUtilities;

public class LineToPointEditor extends GeometryConvertEditor {

	@Override
	public boolean enble(EditEnvironment environment) {
		return ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypeFeatures(), ILineFeature.class);
	}

	public DatasetType getDesDatasetType() {
		return DatasetType.POINT;
	}

	@Override
	public String getTitle() {
		return MapEditorProperties.getString("String_GeometryOperation_LineToPoint");
	}

	@Override
	public DatasetType getSrcDatasetType() {
		return DatasetType.LINE;
	}

	@Override
	public boolean convert(Recordset desRecordset, IGeometry srcGeometry, Map<String, Object> properties) {
		boolean isConverted = true;

		if (srcGeometry instanceof ILineFeature) {
			GeoLine geoLine = ((ILineFeature) srcGeometry).convertToLine(120);

			for (int i = 0; i < geoLine.getPartCount(); i++) {
				Point2Ds points = geoLine.getPart(i);

				for (int j = 0; j < points.getCount(); j++) {
					GeoPoint geoPoint = new GeoPoint(points.getItem(j));
					desRecordset.addNew(geoPoint, properties);
					geoPoint.dispose();
				}
			}
			geoLine.dispose();
		} else {
			isConverted = false;
		}
		return isConverted;
	}
}
