package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.ui.Action;

import javax.swing.*;
import java.util.List;

public class DatasetChooserNewMap {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private transient IFormMap formMap;

	DatasetChooser datasetChooser;

	public DatasetChooserNewMap(JFrame owner, IFormMap formMap, DatasetType[] datasetType) {
		this.formMap = formMap;
		datasetChooser = new DatasetChooser();
		datasetChooser.setSupportDatasetTypes(datasetType);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			addDatasetsToNewMap(formMap);
		}
	}

	/**
	 * 将数据集添加到新地图窗口
	 */
	private void addDatasetsToNewMap(IFormMap formMap) {
		try {
			List<Dataset> datasetsToMap = datasetChooser.getSelectedDatasets();

			MapViewUIUtilities.addDatasetsToMap(this.formMap.getMapControl().getMap(), datasetsToMap.toArray(new Dataset[datasetsToMap.size()]), true);
			// 新建的地图窗口，修改默认的Action为漫游
			formMap.getMapControl().setAction(Action.PAN);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
