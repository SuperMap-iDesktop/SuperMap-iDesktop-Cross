package com.supermap.desktop.CtrlAction.Dataset;

import java.awt.Component;
import java.awt.Cursor;
import java.text.MessageFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit.DatasetWrap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionDeleteSpatialIndex extends CtrlAction {

	public CtrlActionDeleteSpatialIndex(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run() {
		try {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.WAIT_CURSOR);
			boolean isSucceeded = false;
			boolean isBuild = true;

			for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
				if (dataset instanceof DatasetVector) {
					DatasetVector datasetVector = (DatasetVector) dataset;
					// 创建和删除索引前需要关闭数据集
					if (DatasetWrap.isDatasetOpened(datasetVector)) {
						String message = MessageFormat.format(DataEditorProperties.getString("String_InfoDatasetOpened"), datasetVector.getName());

						// 提示关闭数据集
						int result = UICommonToolkit.showConfirmDialog(message);
						if (result == JOptionPane.YES_OPTION) {
							isBuild = true;
							DatasetWrap.CloseDataset(datasetVector);
						} else {
							isBuild = false;
							continue;
						}
					}

					if (isBuild) {

						// 如果是无索引、R树索引或者四叉树索引 则直接调用BuildSpatialIndex(SpatialIndexType)进行索引创建，
						// 否调用BuildSpatialIndex(SpatialIndexInfo)
						isSucceeded = datasetVector.buildSpatialIndex(SpatialIndexType.NONE);
						datasetVector.close();

						if (isSucceeded) {
							String message = MessageFormat.format(DataEditorProperties.getString("String_DatasetDeleteIndex_Success"), datasetVector.getName()); 
							Application.getActiveApplication().getOutput().output(message);
						} else {
							String message = "";
							if (datasetVector.getRecordCount() <= 1000) {
								message = MessageFormat.format(DataEditorProperties.getString("String_BuildSpatialIndex_Error"), datasetVector.getName());
							} else {
								message = MessageFormat.format(DataEditorProperties.getString("String_Message_DeleteSpatialIndexFailed"), datasetVector.getName()); 
							}
							Application.getActiveApplication().getOutput().output(message);

						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.DEFAULT_CURSOR);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveDatasets().length > 0) {
			enable = !Application.getActiveApplication().getActiveDatasets()[0].getDatasource().isReadOnly();
		}

		// notify by huchenpu
		// 暂时先仅仅支持 RTree索引
		if (enable) {
			enable = false;
			for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
				if (dataset instanceof DatasetVector) {
					DatasetVector datasetVector = (DatasetVector) dataset;
					if (datasetVector.isSpatialIndexTypeSupported(SpatialIndexType.RTREE) && datasetVector.getSpatialIndexType() != SpatialIndexType.NONE) {
						enable = true;
						break;
					}
				}
			}
		}
		return enable;
	}
}
