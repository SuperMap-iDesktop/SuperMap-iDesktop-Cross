package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.process.ProcessProperties;

import java.util.Map;

/**
 * Created By Chens on 2017/7/27 0027
 */
public class MetaProcessRegion3DTo2D extends MetaProcessPointLineRegion {
	public MetaProcessRegion3DTo2D() {
		super(DatasetType.REGION3D, DatasetType.REGION);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "Region3DTo2DResult";
	}

	@Override
	protected String getOutputName() {
		return "result_region3DTo2D";
	}

	@Override
	protected  String getOutputResultName(){
		return ProcessOutputResultProperties.getString("String_3DRegionResult");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		boolean isConverted = true;

		if (geometry.getGeometry() instanceof GeoRegion3D) {
			GeoRegion3D geoRegion3D = (GeoRegion3D) geometry.getGeometry();
			for (int i = 0; i < geoRegion3D.getPartCount(); i++) {
				Point3Ds point3Ds = geoRegion3D.getPart(i);
				Point2Ds point2Ds = new Point2Ds();
				for (int j = 0; j < point3Ds.getCount(); j++) {
					point2Ds.add(new Point2D(point3Ds.getItem(j).getX(), point3Ds.getItem(j).getY()));
				}
				GeoRegion geoRegion = new GeoRegion(point2Ds);
				recordset.addNew(geoRegion, value);
				geoRegion.dispose();
			}
			geoRegion3D.dispose();
		} else {
			isConverted = false;
		}
		return isConverted;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_REGION3D_TO_2D;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_Region3DTo2D");
	}
}
