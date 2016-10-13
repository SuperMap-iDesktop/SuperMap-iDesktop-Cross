package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormTransformation;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.enums.FormTransformationSubFormType;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
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
		this.mapControl.setWaitCursorEnabled(false);
		mapControl.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_ReferLayer"));
		this.addedMaps = new ArrayList<>();
	}


	@Override
	public void addDatas(List<Object> datas) {
		boolean isRefreshMap = false;
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
		if (mapControl.getMap().getLayers().getCount() == 0 && maps.size() == 1) {
			addedMaps.add(maps.get(0));
			mapControl.getMap().open(maps.get(0).getName());
			mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_ReferLayer"));
			IForm activeForm = Application.getActiveApplication().getActiveForm();
			if (activeForm instanceof IFormTransformation && ((IFormTransformation) activeForm).getCurrentSubFormType() == FormTransformationSubFormType.Reference) {
				UICommonToolkit.getLayersManager().setMap(mapControl.getMap());
			}

		} else if (maps.size() >= 1) {
			isRefreshMap = true;
			Layers layers = mapControl.getMap().getLayers();
			for (Map map : maps) {
				addedMaps.add(map);
				LayerGroup layerGroup = layers.addGroup(map.getName());
				ArrayList<Layer> layerArrayList = new ArrayList<>();
				for (int i = 0; i < map.getLayers().getCount(); i++) {
					layerArrayList.add(map.getLayers().get(i));
				}
				for (Layer layer : layerArrayList) {
					layerGroup.add(layer);
				}
			}
		}
		if (datasets.size() > 0) {
			MapViewUIUtilities.addDatasetsToMap(mapControl.getMap(), datasets.toArray(new Dataset[datasets.size()]), true);
			isRefreshMap = false;
		}
		if (isViewEntire) {
			mapControl.getMap().viewEntire();
		} else if (isRefreshMap) {
			mapControl.getMap().refresh();
		}
	}

	@Override
	protected void cleanHook() {
//		for (Map addedMap : addedMaps) {
//			addedMap.close();
//		}
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
	protected void setMapControl(MapControl mapControl) {
		this.mapControl = mapControl;
	}
}
