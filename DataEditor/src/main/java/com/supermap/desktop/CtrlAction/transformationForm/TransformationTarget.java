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
import com.supermap.ui.MapControl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationTarget extends TransformationBase implements ITransformation, IFormMap {

	private FormTransformation formTransformation;
	private MapControl mapControl;
	private ArrayList<TransformationAddObjectBean> transformationBeens;

	public TransformationTarget(FormTransformation formTransformation) {
		transformationBeens = new ArrayList<>();
		this.formTransformation = formTransformation;
		this.mapControl = new MapControl();
		this.mapControl.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		this.mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_TargetLayer"));
	}

	@Override
	public void addDatas(List<Object> datas) {
		int size = transformationBeens.size();
		boolean isModified = false;
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
			mapControl.getMap().open(maps.get(0).getName());
			mapControl.getMap().setName(DataEditorProperties.getString("String_Transfernation_TargetLayer"));
			IForm activeForm = Application.getActiveApplication().getActiveForm();
			if (activeForm instanceof IFormTransformation && ((IFormTransformation) activeForm).getCurrentSubFormType() == FormTransformationSubFormType.Target) {
				UICommonToolkit.getLayersManager().setMap(mapControl.getMap());
			}
		} else if (maps.size() > 0) {
			isModified = true;
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
			}
		}
		if (datasets.size() > 0) {
			isModified = true;
			MapViewUIUtilities.addDatasetsToMap(mapControl.getMap(), datasets.toArray(new Dataset[datasets.size()]), true);
		}
		if (size == 0 && isModified) {
			mapControl.getMap().viewEntire();
		}
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
