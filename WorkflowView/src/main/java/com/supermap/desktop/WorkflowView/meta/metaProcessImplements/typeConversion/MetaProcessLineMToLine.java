package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLineM;
import com.supermap.data.Recordset;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.util.Map;

/**
 * Created by yuanR on 2017/8/8 .
 * 路由->线
 */
public class MetaProcessLineMToLine extends MetaProcessPointLineRegion {

	public MetaProcessLineMToLine() {
		super(DatasetType.LINEM, DatasetType.LINE);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "LineMToLineResult";
	}

	@Override
	protected String getOutputName() {
		return "result_lineMToLine";
	}

	@Override
	protected  String getOutputResultName(){
		return ProcessOutputResultProperties.getString("String_LineMToLineResult");
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		boolean isConverted = false;
		if (geometry.getGeometry() instanceof GeoLineM) {
			GeoLine geoLine = ((GeoLineM) geometry.getGeometry()).convertToLine();
			for (int i = 0; i < geoLine.getPartCount(); i++) {
				isConverted = recordset.addNew(geoLine, value);
			}
			geoLine.dispose();
		}
		return isConverted;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_LINEM_TO_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_LineMToLine");
	}

}