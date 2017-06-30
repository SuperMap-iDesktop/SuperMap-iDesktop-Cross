package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by xie on 2017/3/7.
 */
public class MetaProcessISORegion extends MetaProcessISO {

	public MetaProcessISORegion() {

	}

	@Override
	protected String getDefaultResultName() {
		return "ISORegion";
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISORegion");
	}

	@Override
	protected DatasetVector subExecute(SurfaceExtractParameter surfaceExtractParameter, DatasetGrid src, Datasource resultDatasource, String datasetName) {
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

}
