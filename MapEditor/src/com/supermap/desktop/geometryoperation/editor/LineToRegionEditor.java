package com.supermap.desktop.geometryoperation.editor;

import java.util.Map;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoRegion;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.ListUtilities;

public class LineToRegionEditor extends GeometryConvertEditor {

	@Override
	public String getTitle() {
		return MapEditorProperties.getString("String_GeometryOperation_LineToRegion");
	}

	@Override
	public DatasetType getDesDatasetType() {
		return DatasetType.REGION;
	}

	@Override
	public DatasetType getSrcDatasetType() {
		return DatasetType.LINE;
	}

	@Override
	public boolean convert(Recordset desRecordset, IGeometry srcGeometry, Map<String, Object> properties) {
		boolean isConverted = true;

		if (srcGeometry instanceof ILineFeature && srcGeometry instanceof IRegionConvertor) {
			GeoRegion geoRegion = ((IRegionConvertor) srcGeometry).convertToRegion(120);

			if (geoRegion != null) {
				desRecordset.addNew(geoRegion, properties);
				geoRegion.dispose();
			} else {
				isConverted = false;
			}
		} else {
			isConverted = false;
		}
		return isConverted;
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypeFeatures(), ILineFeature.class);
	}
}
