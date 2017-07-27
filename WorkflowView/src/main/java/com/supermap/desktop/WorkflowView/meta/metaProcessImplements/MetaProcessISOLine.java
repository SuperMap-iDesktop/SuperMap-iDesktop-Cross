package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by xie on 2017/3/6.
 */
public class MetaProcessISOLine extends MetaProcessISO {



	public MetaProcessISOLine() {
	}

	@Override
	protected String getDefaultResultName() {
		return "result_ISOLine";
	}


	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISOLine");
	}


	public DatasetVector subExecute(SurfaceExtractParameter surfaceExtractParameter, DatasetGrid dataset, Datasource datasource, String datasetName) {
		return SurfaceAnalyst.extractIsoline(surfaceExtractParameter, dataset, datasource, datasetName);
	}


	@Override
	public String getKey() {
		return MetaKeys.ISOLINE;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

}
