package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.util.Map;

/**
 * Created by yuanR on 2017/8/8  .
 * 网络数据集转换为线数据集
 */
public class MetaProcessNetWorkToLine extends MetaProcessPointLineRegion {

	public MetaProcessNetWorkToLine() {
		super(DatasetType.NETWORK, DatasetType.LINE);
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "NetWorkToLineResult";
	}

	@Override
	protected String getOutputName() {
		return "result_networkToLine";
	}

	@Override
	protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
		boolean isConverted = true;

		if (geometry instanceof ILineFeature) {
			GeoLine geoLine = (GeoLine) geometry.getGeometry();
			for (int i = 0; i < geoLine.getPartCount(); i++) {
				recordset.addNew(geoLine, value);
			}
			geoLine.dispose();
		} else {
			isConverted = false;
		}
		return isConverted;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_NETWORK_TO_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_NetworkToLine");
	}

}