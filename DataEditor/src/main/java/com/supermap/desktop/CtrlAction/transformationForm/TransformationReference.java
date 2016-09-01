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
public class TransformationReference extends TransformationBase implements ITransformation, IFormMap {

	private FormTransformation formTransformation;
	private MapControl mapControl;
	private ArrayList<Map> addedMaps;

	public TransformationReference(FormTransformation formTransformation) {
		this.formTransformation = formTransformation;
		this.mapControl = new MapControl();
		mapControl.getMap().setDynamicProjection(true);
		mapControl.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_ReferLayer"));
		this.addedMaps = new ArrayList<>();
	}


	@Override
	public void addDatas(List<Object> datas) {
		boolean isViewEntire = mapControl.getMap().getLayers().getCount() == 0;
		Layers layers = mapControl.getMap().getLayers();
		ArrayList<Dataset> datasets = new ArrayList<>();
		for (Object listObject : datas) {
			if (listObject instanceof Map) {
				Map map = (Map) listObject;
				LayerGroup layerGroup = layers.addGroup(map.getName());
				for (int j = 0; j < map.getLayers().getCount(); j++) {
					layerGroup.add(map.getLayers().get(j));
				}
//				map.close();
			} else if (listObject instanceof Dataset) {
				datasets.add((Dataset) listObject);
			}
		}
		MapViewUIUtilities.addDatasetsToMap(mapControl.getMap(), datasets.toArray(new Dataset[datasets.size()]), true);
		if (isViewEntire) {
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
