package com.supermap.desktop.mapview.geometry.property;

import java.util.ArrayList;

import com.supermap.data.DatasetVector;
import com.supermap.data.Geometry;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IProperty;

public class GeometryPropertyFactory {
	private static GeometrySpatialPropertyControl geometrySpatialPropertyControl;
	private static GeometryRecordsetPropertyControl geometryRecordsetPropertyControl;

	private GeometryPropertyFactory() {
		// 不提供构造函数
	}

	// public static IProperty[] createProperties(Geometry geometry, DatasetVector datasetVector, Recordset recordset) {
	// ArrayList<IProperty> properties = new ArrayList<IProperty>();
	//
	// try {
	// if (geometry != null && datasetVector != null) {
	// properties.add(getGeometryRecordsetPropertyControl(recordset));
	// properties.add(getGeometrySpatialPropertyControl(geometry, datasetVector.getPrjCoordSys()));
	// }
	// } catch (Exception e) {
	// Application.getActiveApplication().getOutput().output(e);
	// }
	// return properties.toArray(new IProperty[properties.size()]);
	// }

	public static GeometrySpatialPropertyControl getGeometrySpatialPropertyControl(Geometry geometry, PrjCoordSys prjCoordSys) {
		if (geometrySpatialPropertyControl == null) {
			geometrySpatialPropertyControl = new GeometrySpatialPropertyControl(geometry, prjCoordSys);
		} else {
			geometrySpatialPropertyControl.setData(geometry, prjCoordSys);
		}

		return geometrySpatialPropertyControl;
	}

	public static GeometryRecordsetPropertyControl getGeometryRecordsetPropertyControl(Recordset recordset) {
		if (geometryRecordsetPropertyControl == null) {
			geometryRecordsetPropertyControl = new GeometryRecordsetPropertyControl(recordset);
		} else {
			geometryRecordsetPropertyControl.setRecordset(recordset);
		}

		return geometryRecordsetPropertyControl;
	}
}
