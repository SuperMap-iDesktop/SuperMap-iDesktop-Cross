package com.supermap.desktop.process.parameter;

import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;

/**
 * Created by xie on 2017/2/14.
 */
public class ParameterOverlayAnalystInfo {
	public Datasource sourceDatatsource;
	public DatasetVector sourceDataset;
	public Datasource overlayAnalystDatasource;
	public DatasetVector overlayAnalystDataset;
	public Datasource targetDatasource;
	public String targetDataset;
	public OverlayAnalystParameter analystParameter;
}
