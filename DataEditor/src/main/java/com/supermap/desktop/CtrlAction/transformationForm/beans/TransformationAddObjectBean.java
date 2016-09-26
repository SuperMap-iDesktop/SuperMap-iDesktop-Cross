package com.supermap.desktop.CtrlAction.transformationForm.beans;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;
import com.supermap.data.TransformationResampleMode;
import com.supermap.mapping.Map;

/**
 * @author XiaJT
 */
public class TransformationAddObjectBean {
	private Dataset dataset;
	private Map map;
	private boolean isSaveAs = false;
	private Datasource resultDatasource;
	private String resultDatasetName;
	// 是否重采样
	private boolean isResample;
	private TransformationResampleMode transformationResampleMode = TransformationResampleMode.CUBIC;
	private double cellSize;

	public TransformationAddObjectBean() {
	}

	public TransformationAddObjectBean(Map map) {
		this.map = map;
	}

	public TransformationAddObjectBean(Dataset dataset, Datasource resultDatasource, String resultDatasetName) {
		this.dataset = dataset;
		if (resultDatasetName != null) {
			isSaveAs = true;
			this.resultDatasource = resultDatasource;
			this.resultDatasetName = resultDatasetName;
		}
		if (dataset instanceof DatasetImage) {
			cellSize = dataset.getBounds().getWidth() / ((DatasetImage) dataset).getWidth();
		} else if (dataset instanceof DatasetGrid) {
			cellSize = dataset.getBounds().getWidth() / ((DatasetGrid) dataset).getWidth();
		}
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

	public boolean isResample() {
		return isResample;
	}

	public void setResample(boolean resample) {
		isResample = resample;
	}

	public TransformationResampleMode getTransformationResampleMode() {
		return transformationResampleMode;
	}

	public void setTransformationResampleMode(TransformationResampleMode transformationResampleMode) {
		this.transformationResampleMode = transformationResampleMode;
	}

	public double getCellSize() {
		return cellSize;
	}

	public void setCellSize(double cellSize) {
		this.cellSize = cellSize;
	}

	public boolean isSaveAs() {
		return isSaveAs;
	}

	public void setSaveAs(boolean saveAs) {
		isSaveAs = saveAs;
	}
}
