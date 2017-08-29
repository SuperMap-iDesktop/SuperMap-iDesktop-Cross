package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.Map;

/**
 * Created By Chens on 2017/7/27 0027
 */
public class MetaProcessLine3DTo2D extends MetaProcess3DTo2D {
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
	protected void convert(DatasetVector resultDataset, FieldInfos fieldInfos, Map<String, Object> fieldValuesIgnoreCase, Recordset recordsetResult, IGeometry geometry) {
		if (geometry.getGeometry() instanceof GeoLine3D) {
			GeoLine3D geoLine3D = (GeoLine3D) geometry.getGeometry();
			for (int i = 0; i < geoLine3D.getPartCount(); i++) {
				Point3Ds point3Ds = geoLine3D.getPart(i);
				Point2Ds point2Ds = new Point2Ds();
				double zValue=0;
				for (int j = 0; j < point3Ds.getCount(); j++) {
					point2Ds.add(new Point2D(point3Ds.getItem(j).getX(), point3Ds.getItem(j).getY()));
					zValue += point3Ds.getItem(j).getZ();
				}
				zValue = zValue / point3Ds.getCount();
				GeoLine geoLine = new GeoLine(point2Ds);
				Map<String, Object> value = mergePropertyData(resultDataset, fieldInfos, fieldValuesIgnoreCase, zValue);
				recordsetResult.addNew(geoLine, value);
				geoLine.dispose();
			}
			geoLine3D.dispose();
		}
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
