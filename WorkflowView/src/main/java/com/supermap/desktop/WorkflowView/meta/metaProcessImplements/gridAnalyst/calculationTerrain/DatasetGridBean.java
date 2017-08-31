package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.data.ColorDictionary;
import com.supermap.data.DatasetGrid;
import com.supermap.mapping.LayerSettingGrid;

/**
 * Created by yuanR on 2017/8/31 0031.
 * 栅格数据集相关属性管理类
 * 目前主要负责LayerSettingGrid和originKeys属性的管理
 */
public class DatasetGridBean {

	private DatasetGrid datasetGrid;

	public DatasetGrid getDatasetGrid() {
		return datasetGrid;
	}

	public LayerSettingGrid getLayerSettingGrid() {
		return layerSettingGrid;
	}

	public void setLayerSettingGrid(LayerSettingGrid layerSettingGrid) {
		this.layerSettingGrid = layerSettingGrid;
	}

	public double[] getOriginKeys() {
		return originKeys;
	}

	public void setOriginKeys(double[] originKeys) {
		this.originKeys = originKeys;
	}

	private LayerSettingGrid layerSettingGrid;
	private double[] originKeys;

	public DatasetGridBean(DatasetGrid datasetGrid) {
		this.datasetGrid = datasetGrid;
		init();
	}

	private void init() {
		// originKeys
		int colorLegth = this.datasetGrid.getColorTable().getCount();
		double minValue = this.datasetGrid.getMinValue();
		double maxValue = this.datasetGrid.getMaxValue();
		this.originKeys = new double[colorLegth];
		double valueGap = (maxValue - minValue) / (colorLegth - 1);
		for (int i = 0; i < colorLegth; i++) {
			this.originKeys[i] = minValue + valueGap * i;
		}
		// LayerSettingGrid
		ColorDictionary colorDictionary = new ColorDictionary();
		for (int i = 0; i < this.datasetGrid.getColorTable().getCount(); i++) {
			colorDictionary.setColor(this.originKeys[i], this.datasetGrid.getColorTable().get(i));
		}
		this.layerSettingGrid = new LayerSettingGrid();
		this.layerSettingGrid.setColorDictionary(colorDictionary);
		colorDictionary.clear();
		colorDictionary.dispose();
	}
}
