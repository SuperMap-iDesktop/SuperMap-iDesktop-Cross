package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoPoint3D;
import com.supermap.data.Recordset;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.process.ProcessProperties;

import java.util.Map;

/**
 * Created By Chens on 2017/7/27 0027
 */
public class MetaProcessPoint3DTo2D extends MetaProcessPointLineRegion {
	public MetaProcessPoint3DTo2D() {
		super(DatasetType.POINT3D, DatasetType.POINT);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "Point3DTo2DResult";
	}

	@Override
	protected String getOutputName() {
		return "result_point3DTo2D";
	}

	@Override
	protected  String getOutputResultName(){
		return ProcessOutputResultProperties.getString("String_3DPointResult");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		boolean isConverted = true;

		if (geometry.getGeometry() instanceof GeoPoint3D) {
			GeoPoint3D geoPoint3D = (GeoPoint3D) geometry.getGeometry();
			GeoPoint geoPoint = new GeoPoint(geoPoint3D.getX(), geoPoint3D.getY());
			recordset.addNew(geoPoint, value);
			geoPoint.dispose();
			geoPoint3D.dispose();
		} else {
			isConverted = false;
		}
		return isConverted;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_POINT3D_TO_2D;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_Point3DTo2D");
	}
}
