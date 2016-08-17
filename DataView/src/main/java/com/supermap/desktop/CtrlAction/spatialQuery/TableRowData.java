package com.supermap.desktop.CtrlAction.spatialQuery;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.Application;
import com.supermap.mapping.Layer;

/**
 * @author XiaJT
 */
public class TableRowData {
	private boolean selected = false;
	private Layer layer = null;
	private SpatialQueryMode spatialQueryMode = null;
	private String sql = "";
	private boolean isShowInTabular = true;
	private boolean isShowInMap = true;
	private boolean isShowInScene = false;
	private boolean isSave = false;
	private Datasource resultDatasource = null;
	private String resultDataset = null;
	private boolean onlySaveSpatialInfo = false;

	public void reset() {
		spatialQueryMode = null;
		sql = "";
		isShowInTabular = true;
		isShowInMap = true;
		isShowInScene = false;
		isSave = false;
		resultDatasource = null;
		resultDataset = null;
		onlySaveSpatialInfo = false;
	}

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


	public Dataset getCurrentDataset() {
		return layer.getDataset();
	}


	public boolean isQueryEnable() {
		return selected && spatialQueryMode != null && (isShowInTabular || isShowInMap || isShowInScene || (isSave() && resultDatasource != null && resultDataset != null));
	}

	public Recordset queryRecordset(Recordset searchingFeatures) {
		QueryParameter queryParameter = new QueryParameter();
		Recordset query = null;
		try {
			queryParameter.setHasGeometry(true);
			queryParameter.setCursorType(CursorType.DYNAMIC);
			queryParameter.setSpatialQueryObject(searchingFeatures);
			queryParameter.setSpatialQueryMode(spatialQueryMode);
			queryParameter.setAttributeFilter(sql);
			query = ((DatasetVector) layer.getDataset()).query(queryParameter);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			queryParameter.dispose();
		}
		return query;
	}
}
