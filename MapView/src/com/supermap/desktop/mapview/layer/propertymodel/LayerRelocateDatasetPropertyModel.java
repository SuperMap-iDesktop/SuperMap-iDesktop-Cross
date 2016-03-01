package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;

public class LayerRelocateDatasetPropertyModel extends LayerPropertyModel {

	public static final String DATASET = "dataset";

	private Dataset layerRelocateDataset;

	public LayerRelocateDatasetPropertyModel() {
		// TODO
	}

	public LayerRelocateDatasetPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public Dataset getDataset() {
		return this.layerRelocateDataset;
	}

	public void setDataset(Dataset dataset) {
		this.layerRelocateDataset = dataset;
	}

	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		if (layers == null || layers.length <= 0 || layers[0] == null || layers[0].isDisposed()) {
			return;
		}
		this.layerRelocateDataset = layers[0].getDataset();
		this.propertyEnabled.put(DATASET, true);
		checkPropertyEnabled();

		if (formMap != null && layers.length > 0) {
			for (Layer layer : layers) {
				if (layer == null || layer.isDisposed()) {
					break;
				}

				this.layerRelocateDataset = ComplexPropertyUtilties.union(this.layerRelocateDataset, layer.getDataset());
			}
		}
	}

	@Override
	protected void apply(Layer layer) {
		boolean isNeedReload = false;
		if(layer != null && this.layerRelocateDataset != null ) {
			if (layer.getDataset() == null) {
				isNeedReload = true;
			}
			layer.setDataset(this.layerRelocateDataset);
		}
		if(isNeedReload){
			getFormMap().setActiveLayers(getFormMap().getActiveLayers());
		}
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		this.layerRelocateDataset = ((LayerRelocateDatasetPropertyModel) model).getDataset();
	}

	/**
	 * 子类必须重写这个方法
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerRelocateDatasetPropertyModel basePropertyModel = (LayerRelocateDatasetPropertyModel) model;

		return super.equals(basePropertyModel) && this.layerRelocateDataset == basePropertyModel.getDataset();
	}

	private void checkPropertyEnabled() {
		try {
			if (getLayers() != null && getFormMap() != null && getFormMap().getMapControl() != null && getFormMap().getMapControl().getMap() != null
					&& getLayers().length > 1) {
				checkPropertyEnabled(DATASET, false);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
