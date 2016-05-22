package com.supermap.desktop.geometryoperation.editor;

import java.util.Map;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoLine;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.ListUtilties;

public class RegionExtractBorderEditor extends GeometryConvertEditor {

	@Override
	public String getTitle() {
		return MapEditorProperties.getString("String_GeometryOperation_RegionExtractBorder");
	}

	@Override
	public DatasetType getDesDatasetType() {
		return DatasetType.LINE;
	}

	@Override
	public DatasetType getSrcDatasetType() {
		return DatasetType.REGION;
	}

	@Override
	public boolean convert(Recordset desRecordset, IGeometry srcGeometry, Map<String, Object> properties) {
		boolean isConverted = true;

		if (srcGeometry instanceof IRegionFeature && srcGeometry instanceof ILineConvertor) {
			GeoLine geoLine = ((ILineConvertor) srcGeometry).convertToLine(120);

			if (geoLine != null) {
				desRecordset.addNew(geoLine, properties);
				geoLine.dispose();
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
		return ListUtilties.isListContainAny(environment.getEditProperties().getSelectedGeometryTypeFeatures(), IRegionFeature.class);
	}
}
