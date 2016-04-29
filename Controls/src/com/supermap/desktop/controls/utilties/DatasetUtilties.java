package com.supermap.desktop.controls.utilties;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfos;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dialog.JDialogConfirm;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SillyB on 2016/1/2.
 */
public class DatasetUtilties {

	/**
	 * 确认数据集是否关闭，如未关闭弹出提示信息
	 *
	 * @param datasets 需要确认的数据集
	 * @return 已关闭的数据集
	 */
	public static List<Dataset> sureDatasetClosed(List<Dataset> datasets) {
		List<Dataset> resultDataset = new ArrayList<>();
		boolean isCloseDataset = false;
		boolean isShowDialog = true;

		JDialogConfirm dialogConfirm = new JDialogConfirm();
		for (Dataset dataset : datasets) {
			if (CommonToolkit.DatasetWrap.isDatasetOpened(dataset)) {
				// 如果数据集打开
				if (!isShowDialog) {
					// 不弹出提示框
					if (isCloseDataset) {
						resultDataset.add(dataset);
					} else {
						// 上面2个if不可合并
					}
				} else {

					String message = MessageFormat.format(CoreProperties.getString("String_InfoDatasetOpened"), dataset.getName());
					dialogConfirm.setMessage(message);
					DialogResult dialogResult = dialogConfirm.showDialog();
					if (dialogResult == DialogResult.OK) {
						resultDataset.add(dataset);
					}
					if (dialogConfirm.isUsedAsDefault()) {
						isShowDialog = false;
						isCloseDataset = dialogResult == DialogResult.OK;
					}
				}
			} else {
				resultDataset.add(dataset);
			}
		}
		CommonToolkit.DatasetWrap.CloseDataset((Dataset[]) resultDataset.toArray(new Dataset[resultDataset.size()]));

		return resultDataset;
	}

	/**
	 * 得到传入数据集的公共字段
	 */
	public static String[] getCommonFields(List<Dataset> selectedDatasets) {
		List<String> fieldNames = new ArrayList<>();
		for (int i = 0; i < selectedDatasets.size(); i++) {
			if (i == 0) {
				FieldInfos fieldInfos = ((DatasetVector) selectedDatasets.get(i)).getFieldInfos();
				List<String> datasetField = new ArrayList<>();
				for (int j = 0; j < fieldInfos.getCount(); j++) {
					if (!fieldInfos.get(j).isSystemField()) {
						fieldNames.add(fieldInfos.get(j).getName());
					}
				}
			} else {
				FieldInfos fieldInfos = ((DatasetVector) selectedDatasets.get(i)).getFieldInfos();
				for (int j = fieldNames.size() - 1; j >= 0; j--) {
					if (fieldInfos.get(fieldNames.get(j)) == null) {
						fieldNames.remove(j);
					}
				}
			}
		}
		return fieldNames.toArray(new String[fieldNames.size()]);
	}

	/**
	 * 获取数据源下的数据集，包括网络数据集的子数据集
	 *
	 * @param datasetName 数据集名称
	 * @param datasource  数据源
	 * @return 找到的数据集，如果不存在则返回null
	 */
	public static Dataset getDatasetFromDatasource(String datasetName, Datasource datasource) {
		Dataset result = null;
		try {
			Datasets datasets = datasource.getDatasets();
			for (int j = 0; j < datasets.getCount(); j++) {
				Dataset dataset = datasets.get(j);
				if (dataset.getName().equals(datasetName)) {
					result = datasets.get(datasetName);
					break;
				} else if (dataset instanceof DatasetVector && ((DatasetVector) dataset).getChildDataset() != null && ((DatasetVector) dataset).getChildDataset().getName().equals(datasetName)) {
					result = ((DatasetVector) dataset).getChildDataset();
					break;
				}

			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
