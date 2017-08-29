package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.iso;

import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by xie on 2017/3/7.
 */
public class MetaProcessISORegion extends MetaProcessISO {

	public MetaProcessISORegion() {
		OUTPUT_DATA_TYPE = "Region";
		super.initParameters();
		super.initParameterConstraint();
		super.initParametersState();
		super.initParametersListener();
	}

	@Override
	protected String getDefaultResultName() {
		return "result_ISORegion";
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISORegion");
	}

	@Override
	public DatasetVector subExecute(SurfaceExtractParameter surfaceExtractParameter, DatasetGrid src, Datasource resultDatasource, String datasetName) {
		return SurfaceAnalyst.extractIsoregion(surfaceExtractParameter, src, resultDatasource, datasetName, null);
	}

	@Override
	public String getKey() {
		return MetaKeys.ISOREGION;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	protected void initHook() {
		OUTPUT_DATA = "ISORegionResult";
	}
}
