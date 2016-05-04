package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Charset;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.AddToWindowMode;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.utilties.StringUtilties;

import java.text.MessageFormat;

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
		charset = Charset.UTF8;
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
		if (encodeType != EncodeType.NONE && datasetType != DatasetType.LINE && datasetType != DatasetType.REGION) {
			encodeType = EncodeType.NONE;
		}
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
		if (datasetType == DatasetType.LINE || datasetType == DatasetType.REGION) {
			this.encodeType = encodeType;
		} else {
			this.encodeType = EncodeType.NONE;
		}
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean createDataset() {
		boolean result = false;
		if (!StringUtilties.isNullOrEmpty(datasetName)) {
			DatasetVectorInfo info = new DatasetVectorInfo(datasetName, datasetType);
			info.setEncodeType(encodeType);
			Dataset dataset = null;
			try {
				dataset = datasource.getDatasets().create(info);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Failed"), datasetName, datasource.getAlias()));
			}

			if (dataset != null) {
				result = true;
				String information = MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Success"), datasetName,
						datasource.getAlias());
				Application.getActiveApplication().getOutput().output(information);
			}
		}
		return result;
	}
}
