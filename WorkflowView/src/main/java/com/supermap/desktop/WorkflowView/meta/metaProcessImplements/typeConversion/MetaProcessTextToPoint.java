package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.process.ProcessProperties;

import java.util.Map;

/**
 * Created By Chens on 2017/7/22 0022
 */
public class MetaProcessTextToPoint extends MetaProcessPointLineRegion {
	public MetaProcessTextToPoint() {
		super(DatasetType.TEXT, DatasetType.POINT);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "TextToPointResult";
	}

	@Override
	protected String getOutputName() {
		return "result_textToPoint";
	}

	@Override
	protected String getOutputResultName() {
		return ProcessOutputResultProperties.getString("String_TextToPointResult");
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_TEXT_TO_POINT;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_TextToPoint");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		if (geometry instanceof GeoText) {
			GeoText geoText = null;
			try {
				geoText = (GeoText) geometry.getGeometry();
				for (int i = 0; i < geoText.getPartCount(); i++) {
					Point2D point = new Point2D(geoText.getPart(i).getX(), geoText.getPart(i).getY());
					GeoPoint geoPoint = new GeoPoint(point);
					if (!recordset.addNew(geoPoint, value)) {
						return false;
					}
					geoPoint.dispose();
				}
			} catch (UnsupportedOperationException e) {
				// 此时返回false-yuanR2017.9.19
				return false;
			} finally {
				if (geoText != null) {
					geoText.dispose();
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
