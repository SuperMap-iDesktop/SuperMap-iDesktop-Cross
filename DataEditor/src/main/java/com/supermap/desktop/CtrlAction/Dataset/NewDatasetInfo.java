package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;

public class NewDatasetInfo {

	private Datasource currentDatasource;

	public Datasource getCurrentDatasource() {
		return currentDatasource;
	}

	public void setCurrentDatasource(Datasource currentDatasource) {
		this.currentDatasource = currentDatasource;
	}

	private Datasource targetDatasource;

	public Datasource getTargetDatasource() {
		return this.targetDatasource;
	}

	public void setTargetDatasource(Datasource value) {
		this.targetDatasource = value;
	}

	private DatasetType datasetType;

	public DatasetType getDatasetType() {
		return this.datasetType;
	}

	public void setDatasetType(DatasetType value) {
		this.datasetType = value;
	}

	private EncodeType encodeType;

	public EncodeType getEncodeType() {
		return this.encodeType;
	}

	public void setEncodeType(EncodeType value) {
		this.encodeType = value;
	}

	private Charset charset;

	public Charset getCharset() {
		return this.charset;
	}

	public void setCharset(Charset value) {
		this.charset = value;
	}

	private String datasetName;

	public String getDatasetName() {
		return this.datasetName;
	}

	public void setDatasetName(String value) {
		this.datasetName = value;
	}

	private AddToWindowMode modeType;

	public AddToWindowMode getModeType() {
		return this.modeType;
	}

	public void setModeType(AddToWindowMode value) {
		this.modeType = value;
	}
}
