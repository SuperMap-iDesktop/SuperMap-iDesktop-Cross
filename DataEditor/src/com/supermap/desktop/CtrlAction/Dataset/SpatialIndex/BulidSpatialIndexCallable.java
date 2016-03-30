package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

import java.text.MessageFormat;
import java.util.List;

/**
 * 空间分析进度条
 *
 * @author XiaJT
 */
public class BulidSpatialIndexCallable extends UpdateProgressCallable {
	private List<SpatialIndexTableModelBean> tableModelBeans;
	private SpatialIndexTableModel spatialIndexTableModel;
	private int totalSize;


	public BulidSpatialIndexCallable(List<SpatialIndexTableModelBean> tableModelBeans, SpatialIndexTableModel spatialIndexTableModel) {
		this.tableModelBeans = tableModelBeans;
		this.spatialIndexTableModel = spatialIndexTableModel;
		this.totalSize = tableModelBeans.size();
		if (totalSize == 0) {
			totalSize = 1;
		}
	}

	@Override
	public Boolean call() throws Exception {
		int i = 0;
		long startTime = System.currentTimeMillis();
		for (SpatialIndexTableModelBean tableModelBean : tableModelBeans) {
			i++;
			String message = MessageFormat.format(DataEditorProperties.getString("String_BuildDatasetSpatialIndex"), i, totalSize);
			updateProgress(i * 100 / (totalSize), getRemainTime(i, startTime), message);
			tableModelBean.bulid();
			spatialIndexTableModel.fireTableDataChanged();
		}
		return true;
	}

	private String getRemainTime(int i, long startTime) {
		long currentTimeMillis = System.currentTimeMillis();
		long remainTime = (currentTimeMillis - startTime) / i * (totalSize - i);
		return String.valueOf(remainTime / 1000);
	}

}
