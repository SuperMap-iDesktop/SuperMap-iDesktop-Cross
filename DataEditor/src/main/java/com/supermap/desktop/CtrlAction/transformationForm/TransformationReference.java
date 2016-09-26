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
		mapControl.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_ReferLayer"));
		this.addedMaps = new ArrayList<>();
	}


	@Override
	public void addDatas(List<Object> datas) {
		boolean isViewEntire = mapControl.getMap().getLayers().getCount() == 0;
		ArrayList<Dataset> datasets = new ArrayList<>();
		ArrayList<Map> maps = new ArrayList<>();
		for (Object listObject : datas) {
			if (listObject instanceof Map) {
				Map map = (Map) listObject;
				maps.add(map);
			} else if (listObject instanceof Dataset) {
				datasets.add((Dataset) listObject);
			}
		}
		if (maps.size() == 1) {
			mapControl.getMap().open(maps.get(0).getName());
			mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_ReferLayer"));
		} else if (maps.size() > 1) {
			Layers layers = mapControl.getMap().getLayers();
			for (Map map : maps) {
				LayerGroup layerGroup = layers.addGroup(map.getName());
				for (int j = 0; j < map.getLayers().getCount(); j++) {
					layerGroup.add(map.getLayers().get(j));
				}
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



}
