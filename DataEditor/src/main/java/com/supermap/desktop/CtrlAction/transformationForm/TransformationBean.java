package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.mapping.Map;

/**
 * @author XiaJT
 */
public class TransformationBean {
	private Dataset dataset;
	private Map map;
	private Datasource resultDatasource;
	private String resultDatasetName;

	public TransformationBean() {
	}

	public TransformationBean(Map map) {
		this.map = map;
	}

	public TransformationBean(Dataset dataset, Datasource resultDatasource, String resultDatasetName) {
		this.dataset = dataset;
		this.resultDatasource = resultDatasource;
		this.resultDatasetName = resultDatasetName;
	}


	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Datasource getResultDatasource() {
		return resultDatasource;
	}

	public void setResultDatasource(Datasource resultDatasource) {
		this.resultDatasource = resultDatasource;
	}

	public String getResultDatasetName() {
		return resultDatasetName;
	}

	public void setResultDatasetName(String resultDatasetName) {
		this.resultDatasetName = resultDatasetName;
	}
}
