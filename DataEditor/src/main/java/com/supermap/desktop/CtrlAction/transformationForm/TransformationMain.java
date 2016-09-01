package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormTransformation;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationMain extends TransformationBase implements ITransformation, IFormMap {

	private FormTransformation formTransformation;
	private MapControl mapControl;
	private ArrayList<TransformationBean> transformationBeens;

	public TransformationMain(FormTransformation formTransformation) {
		transformationBeens = new ArrayList<>();
		this.formTransformation = formTransformation;
		this.mapControl = new MapControl();
		this.mapControl.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		this.mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_TargetLayer"));
	}

	@Override
	public void addDatas(List<Object> datas) {
		int size = transformationBeens.size();
		ArrayList<Dataset> datasets = new ArrayList<>();
		for (Object data : datas) {
			if (data instanceof TransformationBean) {
				transformationBeens.add((TransformationBean) data);
				if (((TransformationBean) data).getDataset() != null) {
					datasets.add(((TransformationBean) data).getDataset());
				} else if (((TransformationBean) data).getMap() != null) {
					Map map = ((TransformationBean) data).getMap();
					Layers layers = mapControl.getMap().getLayers();
					LayerGroup layerGroup = layers.addGroup(map.getName());
					for (int i = 0; i < map.getLayers().getCount(); i++) {
						layerGroup.add(map.getLayers().get(i));
					}
				}
			}
		}
		MapViewUIUtilities.addDatasetsToMap(mapControl.getMap(), datasets.toArray(new Dataset[datasets.size()]), true);
		if (size == 0 && size != transformationBeens.size()) {
			mapControl.getMap().viewEntire();
		}
	}


	@Override
	public void clean() {

	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public MapControl getMapControl() {
		return mapControl;
	}


	@Override
	public void removeActiveLayersByDatasets(Dataset... datasets) {

	}


}
