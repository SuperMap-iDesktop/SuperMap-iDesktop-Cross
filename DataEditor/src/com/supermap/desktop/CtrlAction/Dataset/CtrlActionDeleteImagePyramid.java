package com.supermap.desktop.CtrlAction.Dataset;

import java.awt.Component;
import java.awt.Cursor;
import java.text.MessageFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit.DatasetWrap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilties.CursorUtilties;

public class CtrlActionDeleteImagePyramid extends CtrlAction {

	public CtrlActionDeleteImagePyramid(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			CursorUtilties.setWaitCursor();

			boolean isSucceeded = false;
			boolean isBuild = true;

			for (int i = 0; i < Application.getActiveApplication().getActiveDatasets().length; i++) {
				Dataset dataset = Application.getActiveApplication().getActiveDatasets()[i];

				// 非栅格或者影像数据集，跳过
				if (!(dataset instanceof DatasetGrid) && !(dataset instanceof DatasetImage)) {
					continue;
				}

				// 创建和删除金字塔前需要关闭数据集
				if (DatasetWrap.isDatasetOpened(dataset)) {
					String message = MessageFormat.format(DataEditorProperties.getString("String_InfoDatasetOpened"), dataset.getName());

					// 提示关闭数据集
					int result = UICommonToolkit.showConfirmDialog(message);
					if (result == JOptionPane.YES_OPTION) {
						isBuild = true;
						DatasetWrap.CloseDataset(dataset);
					} else {
						isBuild = false;
						continue;
					}
				}

				if (isBuild) {
					if (dataset instanceof DatasetImage) {
						isSucceeded = ((DatasetImage) dataset).removePyramid();
						dataset.close();
					} else if (dataset instanceof DatasetGrid) {
						isSucceeded = ((DatasetGrid) dataset).removePyramid();
						dataset.close();
					}

					if (isSucceeded) {
						String message = MessageFormat.format(DataEditorProperties.getString("String_DeleteImagePyramid_Success"), dataset.getName());
						Application.getActiveApplication().getOutput().output(message);
					} else {
						String message = MessageFormat.format(DataEditorProperties.getString("String_DeleteImagePyramid_Failed"), dataset.getName());
						Application.getActiveApplication().getOutput().output(message);
					}
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
			// 影像数据源，支持创建金字塔
			if (dataset.getDatasource().getConnectionInfo().getEngineType() == EngineType.IMAGEPLUGINS) {
				enable = false;
				break;
			} else if (!Application.getActiveApplication().getActiveDatasets()[0].getDatasource().isReadOnly()) {
				if (dataset.getType() == DatasetType.IMAGE) {
					DatasetImage datasetImage = (DatasetImage) dataset;
					if (datasetImage.getHasPyramid()) {
						enable = true;
						break;
					}
				} else if (dataset.getType() == DatasetType.GRID) {
					DatasetGrid datasetGrid = (DatasetGrid) dataset;
					if (datasetGrid.getHasPyramid()) {
						enable = true;
						break;
					}
				}
			}
		}

		return enable;
	}
}
