package com.supermap.desktop.CtrlAction.spatialQuery;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.SpatialQueryMode;
import com.supermap.mapping.Layer;

/**
 * @author XiaJT
 */
public class TableRowData {
	private boolean selected;
	private Layer layer;
	private SpatialQueryMode spatialQueryMode;
	private String sql;
	private boolean isShowInTabular;
	private boolean isShowInMap;
	private boolean isShowInScene;
	private boolean isSave;
	private Datasource resultDatasource;
	private String  resultDataset;
	private boolean onlySaveSpatialInfo;

	public TableRowData(Layer layer) {
		this.layer = layer;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public DatasetType getDatasetType() {
		return layer.getDataset().getType();
	}

	public String getLayerName() {
		return layer.getCaption();
	}

	public SpatialQueryMode getSpatialQueryMode() {
		return spatialQueryMode;
	}

	public void setSpatialQueryMode(SpatialQueryMode spatialQueryMode) {
		this.spatialQueryMode = spatialQueryMode;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isShowInTabular() {
		return isShowInTabular;
	}

	public void setShowInTabular(boolean showInTabular) {
		isShowInTabular = showInTabular;
	}

	public boolean isShowInMap() {
		return isShowInMap;
	}

	public void setShowInMap(boolean showInMap) {
		isShowInMap = showInMap;
	}

	public boolean isShowInScene() {
		return isShowInScene;
	}

	public void setShowInScene(boolean showInScene) {
		isShowInScene = showInScene;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean save) {
		isSave = save;
	}

	public String getResultDataset() {
		return resultDataset;
	}

	public void setResultDataset(String resultDataset) {
		this.resultDataset = resultDataset;
	}

	public Datasource getResultDatasource() {
		return resultDatasource;
	}

	public void setResultDatasource(Datasource resultDatasource) {
		this.resultDatasource = resultDatasource;
	}

	public boolean isOnlySaveSpatialInfo() {
		return onlySaveSpatialInfo;
	}

	public void setOnlySaveSpatialInfo(boolean onlySaveSpatialInfo) {
		this.onlySaveSpatialInfo = onlySaveSpatialInfo;
	}
}
