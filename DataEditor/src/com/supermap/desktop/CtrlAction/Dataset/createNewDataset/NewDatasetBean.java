package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.AddToWindowMode;

/**
 * @author XiaJT
 */
public class NewDatasetBean {
	private Datasource datasource;
	private String datasetName;
	private DatasetType datasetType;
	private EncodeType encodeType;
	private Charset charset;
	private AddToWindowMode addToWindowMode;

	public NewDatasetBean() {
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		datasource = activeDatasources.length > 0 ? activeDatasources[0] : Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		datasetName = "";
		datasetType = DatasetType.POINT;
		encodeType = EncodeType.NONE;
		charset = Charset.DEFAULT;
		addToWindowMode = AddToWindowMode.NONEWINDOW;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public DatasetType getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(DatasetType datasetType) {
		this.datasetType = datasetType;
	}

	public AddToWindowMode getAddToWindowMode() {
		return addToWindowMode;
	}

	public void setAddToWindowMode(AddToWindowMode addToWindowMode) {
		this.addToWindowMode = addToWindowMode;
	}

	public EncodeType getEncodeType() {
		return encodeType;
	}

	public void setEncodeType(EncodeType encodeType) {
		this.encodeType = encodeType;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
