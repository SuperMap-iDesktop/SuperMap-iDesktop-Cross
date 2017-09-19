package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.process.ProcessProperties;

import java.util.Map;

public class MetaProcessLineToPoint extends MetaProcessPointLineRegion {
	public MetaProcessLineToPoint() {
		super(DatasetType.LINE, DatasetType.POINT);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "LineToPointResult";
	}

	@Override
	protected String getOutputName() {
		return "result_lineToPoint";
	}

	@Override
	protected String getOutputResultName() {
		return ProcessOutputResultProperties.getString("String_LineToPointResult");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		if (geometry instanceof ILineFeature) {
			GeoLine geoLine = ((ILineFeature) geometry).convertToLine(120);
			try {
				for (int i = 0; i < geoLine.getPartCount(); i++) {
					Point2Ds points = geoLine.getPart(i);
					for (int j = 0; j < points.getCount(); j++) {
						GeoPoint geoPoint = new GeoPoint(points.getItem(j));
						if (!recordset.addNew(geoPoint, value)) {
							return false;
						}
						geoPoint.dispose();
					}
				}
			} catch (UnsupportedOperationException e) {
				// 此时返回false-yuanR2017.9.19
				return false;
			} finally {
				if (geoLine != null) {
					geoLine.dispose();
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_LINE_TO_POINT;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_LineToPoint");
	}
}
