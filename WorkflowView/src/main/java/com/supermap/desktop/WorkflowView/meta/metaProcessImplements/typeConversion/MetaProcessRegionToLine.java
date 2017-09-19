package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoLine;
import com.supermap.data.Recordset;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.process.ProcessProperties;

import java.util.Map;

public class MetaProcessRegionToLine extends MetaProcessPointLineRegion {
	public MetaProcessRegionToLine() {
		super(DatasetType.REGION, DatasetType.LINE);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "RegionToLineResult";
	}

	@Override
	protected String getOutputName() {
		return "result_regionToLine";
	}

	@Override
	protected String getOutputResultName() {
		return ProcessOutputResultProperties.getString("String_RegionToLineResult");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {

		if (geometry instanceof IRegionFeature && geometry instanceof ILineConvertor) {
			GeoLine geoLine = null;
			try {
				geoLine = ((ILineConvertor) geometry).convertToLine(120);
				if (!recordset.addNew(geoLine, value)) {
					return false;
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
		return MetaKeys.CONVERSION_REGION_TO_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_RegionToLine");
	}
}
