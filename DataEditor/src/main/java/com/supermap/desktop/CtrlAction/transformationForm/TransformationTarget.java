package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.FormTransformation;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.enums.FormTransformationSubFormType;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationTarget extends TransformationBase implements ITransformation, IFormMap {

	private FormTransformation formTransformation;
	private ArrayList<TransformationAddObjectBean> transformationBeens;
	private ArrayList<Map> addedMaps = new ArrayList<>();

	public TransformationTarget(FormTransformation formTransformation) {
		super();
		transformationBeens = new ArrayList<>();
		this.formTransformation = formTransformation;
		this.mapControl.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		this.mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_TargetLayer"));
	}

	@Override
	public void addDatas(List<Object> datas) {
		boolean isViewEntire = mapControl.getMap().getLayers().getCount() == 0;
		boolean isModified = false;
		boolean isNeedRefresh = false;
		ArrayList<Dataset> datasets = new ArrayList<>();
		ArrayList<Map> maps = new ArrayList<>();
		for (Object data : datas) {
			if (data instanceof Dataset) {
				if (TransformationUtilties.isSupportDatasetType(((Dataset) data).getType())) {
					Datasource defaultDatasource = TransformationUtilties.getDefaultDatasource(((Dataset) data).getDatasource());
					data = new TransformationAddObjectBean((Dataset) data, defaultDatasource,
							defaultDatasource == null ? null : defaultDatasource.getDatasets().getAvailableDatasetName(((Dataset) data).getName() + "_adjust"));
				}
			} else if (data instanceof Map) {
				data = new TransformationAddObjectBean((Map) data);
			}
			if (data instanceof TransformationAddObjectBean) {
				transformationBeens.add((TransformationAddObjectBean) data);
				if (((TransformationAddObjectBean) data).getDataset() != null) {
					datasets.add(((TransformationAddObjectBean) data).getDataset());
				} else if (((TransformationAddObjectBean) data).getMap() != null) {
					maps.add(((TransformationAddObjectBean) data).getMap());

				}
			}
		}
		if (mapControl.getMap().getLayers().getCount() == 0 && maps.size() == 1) {
			isModified = true;
			isNeedRefresh = true;
			addedMaps.add(maps.get(0));
			mapControl.getMap().open(maps.get(0).getName());
			mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_TargetLayer"));
			IForm activeForm = Application.getActiveApplication().getActiveForm();
			if (activeForm instanceof IFormTransformation && ((IFormTransformation) activeForm).getCurrentSubFormType() == FormTransformationSubFormType.Target) {
				UICommonToolkit.getLayersManager().setMap(mapControl.getMap());
			}
		} else if (maps.size() > 0) {
			isModified = true;
			isNeedRefresh = true;
			for (Map map : maps) {
				Layers layers = mapControl.getMap().getLayers();
				LayerGroup layerGroup = layers.addGroup(map.getName());
				ArrayList<Layer> layerArrayList = new ArrayList<>();
				for (int i = 0; i < map.getLayers().getCount(); i++) {
					layerArrayList.add(map.getLayers().get(i));
				}
				for (Layer layer : layerArrayList) {
					layerGroup.add(layer);
				}
				addedMaps.add(map);
			}
		}
		if (datasets.size() > 0) {
			isModified = true;
			isNeedRefresh = false;
			MapViewUIUtilities.addDatasetsToMap(mapControl.getMap(), datasets.toArray(new Dataset[datasets.size()]), true);
		}
		if (isViewEntire && isModified) {
			mapControl.getMap().viewEntire();
		} else if (isNeedRefresh) {
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


	public Object[] getTransformationObjects() {
		ArrayList<Layer> layers = MapUtilities.getLayers(mapControl.getMap());
		ArrayList<TransformationAddObjectBean> result = new ArrayList<>();
		for (TransformationAddObjectBean transformationBeen : transformationBeens) {
			if (transformationBeen.getDataset() != null) {
				for (Layer layer : layers) {
					if (layer.getDataset() == transformationBeen.getDataset()) {
						result.add(transformationBeen);
						break;
					}
				}
			}
		}
		return result.toArray(new Object[result.size()]);
	}
}
