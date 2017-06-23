package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

import java.util.List;

/**
 * Created by xie on 2017/3/23.
 */
public class CtrlActionInsertDataset extends CtrlAction {
	public CtrlActionInsertDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {

			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (formMap != null) {
				DatasetType[] datasetTypes = new DatasetType[]{
						DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT, DatasetType.CAD, DatasetType.NETWORK,
						DatasetType.LINEM, DatasetType.GRID, DatasetType.IMAGE, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D,
						DatasetType.GRIDCOLLECTION, DatasetType.IMAGECOLLECTION, DatasetType.PARAMETRICLINE, DatasetType.PARAMETRICREGION,
						DatasetType.NETWORK3D
				};
				DatasetChooser datasetChooser = new DatasetChooser();
				datasetChooser.setSupportDatasetTypes(datasetTypes);
				if (datasetChooser.showDialog() == DialogResult.OK) {
					List<Dataset> datasetsToMap = datasetChooser.getSelectedDatasets();
					Layer[] activeLayers = formMap.getActiveLayers();

					if (activeLayers == null || activeLayers.length == 0) {
						return;
					}

					int activeIndex = formMap.getMapControl().getMap().getLayers().indexOf(activeLayers[0].getName());
					Layer[] insertLayers = new Layer[datasetsToMap.size()];
					for (int i = datasetsToMap.size() - 1; i >= 0; i--) {
						insertLayers[i] = MapUtilities.insertDatasetToMap(formMap.getMapControl().getMap(), datasetsToMap.get(i), activeIndex);
					}

					formMap.setActiveLayers(insertLayers);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {

		boolean enable = false;

		try {
			if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0
					&& null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}
