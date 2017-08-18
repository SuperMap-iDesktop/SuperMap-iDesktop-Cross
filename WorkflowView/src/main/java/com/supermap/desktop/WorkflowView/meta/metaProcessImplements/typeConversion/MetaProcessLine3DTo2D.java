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
public class MetaProcessLine3DTo2D extends MetaProcessPointLineRegion {
	public MetaProcessLine3DTo2D() {
		super(DatasetType.LINE3D, DatasetType.LINE);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "Line3DTo2DResult";
	}

	@Override
	protected String getOutputName() {
		return "result_line3DTo2D";
	}

	@Override
	protected  String getOutputResultName(){
		return ProcessOutputResultProperties.getString("String_3DLineResult");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		boolean isConverted = true;

		if (geometry.getGeometry() instanceof GeoLine3D) {
			GeoLine3D geoLine3D = (GeoLine3D) geometry.getGeometry();
			for (int i = 0; i < geoLine3D.getPartCount(); i++) {
				Point3Ds point3Ds = geoLine3D.getPart(i);
				Point2Ds point2Ds = new Point2Ds();
				for (int j = 0; j < point3Ds.getCount(); j++) {
					point2Ds.add(new Point2D(point3Ds.getItem(j).getX(), point3Ds.getItem(j).getY()));
				}
				GeoLine geoLine = new GeoLine(point2Ds);
				recordset.addNew(geoLine, value);
				geoLine.dispose();
			}
			geoLine3D.dispose();
		} else {
			isConverted = false;
		}
		return isConverted;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_LINE3D_TO_2D;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_Line3DTo2D");
	}
}
