package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.SpatialIndex.JDialogBulidSpatialIndex;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;
import java.awt.*;

public class CtrlActionCreateSpatialIndex extends CtrlAction {

	public CtrlActionCreateSpatialIndex(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			JDialogBulidSpatialIndex jDialogBulidSpatialIndex = new JDialogBulidSpatialIndex();
			jDialogBulidSpatialIndex.showDialog();
//			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.WAIT_CURSOR);
//			boolean isSucceeded = false;
//			boolean isBuild = true;
//
//			for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
//				if (dataset instanceof DatasetVector) {
//					DatasetVector datasetVector = (DatasetVector) dataset;
//					// 创建和删除索引前需要关闭数据集
//					if (DatasetWrap.isDatasetOpened(datasetVector)) {
//						String message = MessageFormat.format(DataEditorProperties.getString("String_InfoDatasetOpened"), datasetVector.getName());
//
//						// 提示关闭数据集
//						int result = UICommonToolkit.showConfirmDialog(message);
//						if (result == JOptionPane.YES_OPTION) {
//							isBuild = true;
//							DatasetWrap.CloseDataset(datasetVector);
//						} else {
//							isBuild = false;
//							continue;
//						}
//					}
//
//					if (isBuild) {
//
//						// 如果是无索引、R树索引或者四叉树索引
//						// 则直接调用BuildSpatialIndex(SpatialIndexType)进行索引创建，
//						// 否调用BuildSpatialIndex(SpatialIndexInfo)
//						isSucceeded = datasetVector.buildSpatialIndex(SpatialIndexType.RTREE);
//						datasetVector.close();
//
//						if (isSucceeded) {
//							String message =MessageFormat.format(DataEditorProperties.getString("String_DatasetCreateIndex_Success"), datasetVector.getName());
//							Application.getActiveApplication().getOutput().output(message);
//						} else {
//							String message = "";
//							if (datasetVector.getRecordCount() <= 1000) {
//								message = MessageFormat.format(DataEditorProperties.getString("String_BuildSpatialIndex_Error"), datasetVector.getName());
//							} else {
//								message = MessageFormat.format(DataEditorProperties.getString("String_Message_CreateSpatialIndexFailed"), datasetVector.getName());
//							}
//							Application.getActiveApplication().getOutput().output(message);
//						}
//					}
//				}
//			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.DEFAULT_CURSOR);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
			enable = !Application.getActiveApplication().getActiveDatasets()[0].getDatasource().isReadOnly();
		}
		return enable;
	}
}
