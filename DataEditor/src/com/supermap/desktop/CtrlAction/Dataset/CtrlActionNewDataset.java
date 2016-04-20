package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;

import java.util.ArrayList;

public class CtrlActionNewDataset extends CtrlAction {

	public CtrlActionNewDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			JDialogDatasetNew dialog = new JDialogDatasetNew();
			ArrayList<Dataset> addToCurrentMap = new ArrayList<Dataset>();
			ArrayList<Dataset> addToNewMap = new ArrayList<Dataset>();
			DialogResult result = dialog.showDialog();
			if (null != result && result == DialogResult.OK) {
				if (null == dialog.getDatasets()) {
					return;
				}
				NewDatasetInfo[] datasets = dialog.getDatasets();
				int count = 0;
				for (int index = 0; index < datasets.length; index++) {
					NewDatasetInfo newDatasetInfo = datasets[index];
					DatasetVectorInfo info = new DatasetVectorInfo(newDatasetInfo.getDatasetName(), newDatasetInfo.getDatasetType());
					info.setEncodeType(newDatasetInfo.getEncodeType());
					Dataset dataset = null;
					try {
						dataset = newDatasetInfo.getTargetDatasource().getDatasets().create(info);
					} catch (Exception e) {
						// 创建失败，统计结果
					}
					if (dataset != null) {
						dataset.setPrjCoordSys(dataset.getDatasource().getPrjCoordSys());
						((DatasetVector) dataset).setCharset(newDatasetInfo.getCharset());
						String information = String.format(DataEditorProperties.getString("String_CreateNewDT_Success"), newDatasetInfo.getDatasetName(),
								newDatasetInfo.getTargetDatasource().getAlias());
						Application.getActiveApplication().getOutput().output(information);
						count++;

						if (newDatasetInfo.getModeType() == AddToWindowMode.CURRENTWINDOW) {
							addToCurrentMap.add(dataset);
						} else if (newDatasetInfo.getModeType() == AddToWindowMode.NEWWINDOW) {
							addToNewMap.add(dataset);
						}
						// 刷新数据集对应的数据源节点并选中数据集
						UICommonToolkit.refreshSelectedDatasetNode(dataset);
					} else {
						String information = String.format(DataEditorProperties.getString("String_CreateNewDT_Failed"), newDatasetInfo.getDatasetName(),
								newDatasetInfo.getTargetDatasource().getAlias());
						Application.getActiveApplication().getOutput().output(information);
					}
				}
				String information = String.format(DataEditorProperties.getString("String_CreateNewDT_Message"), datasets.length, count,
						datasets.length - count);
				Application.getActiveApplication().getOutput().output(information);

				if (!addToCurrentMap.isEmpty()) {
					IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
					Map map = formMap.getMapControl().getMap();
					for (Dataset dataset : addToCurrentMap) {
						if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
							map.getLayers().add(dataset, true);
						}

						map.refresh();
						UICommonToolkit.getLayersManager().setMap(map);
					}
				}

				if (!addToNewMap.isEmpty()) {
					String name = MapUtilties
							.getAvailableMapName(String.format("%s@%s", addToNewMap.get(0).getName(), addToNewMap.get(0).getDatasource().getAlias()), true);
					IFormMap formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);

					if (formMap != null) {
						Map map = formMap.getMapControl().getMap();
						for (Dataset dataset : addToNewMap) {
							if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
								map.getLayers().add(dataset, true);
							}
						}

						map.refresh();
						UICommonToolkit.getLayersManager().setMap(map);

						// add by huchenpu 20150716
						// 新建的地图窗口，修改默认的Action为漫游
						formMap.getMapControl().setAction(Action.PAN);
					}
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			for (Datasource datasource : datasources) {
				if (null != datasource && !datasource.isReadOnly()) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}

}