package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetGridCollection;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.DatasetType;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CursorUtilities;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CtrlActionDeleteImagePyramid extends CtrlAction {

	public CtrlActionDeleteImagePyramid(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		List<Dataset> datasets = new ArrayList<>();
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		Collections.addAll(datasets, activeDatasets);
		datasets = DatasetUIUtilities.sureDatasetClosed(datasets);
		deleteImagePyramid(datasets);
	}

	public static void deleteImagePyramid(List<Dataset> datasets) {
		try {
			//region 删除时没进度条
//			FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_DeleteDatasetPyramid"));
//			formProgressTotal.doWork(new DeleteImagePyramidCallable(datasets.toArray(new Dataset[datasets.size()])));
			//endregion
			CursorUtilities.setWaitCursor();
			boolean isSucceeded = false;

			for (Dataset dataset : datasets) {
				// 非栅格或者影像数据集，跳过
				if (!(dataset instanceof DatasetGrid) && !(dataset instanceof DatasetImage) && !(dataset instanceof DatasetGridCollection) && !(dataset instanceof DatasetImageCollection)) {
					continue;
				}


				if (dataset instanceof DatasetImage) {
					isSucceeded = ((DatasetImage) dataset).removePyramid();
					dataset.close();
				} else if (dataset instanceof DatasetGrid) {
					isSucceeded = ((DatasetGrid) dataset).removePyramid();
					dataset.close();
				} else if (dataset instanceof DatasetImageCollection) {
					isSucceeded = ((DatasetImageCollection) dataset).removePyramid();
					dataset.close();
				} else {
					isSucceeded = ((DatasetGridCollection) dataset).removePyramid();
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

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
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
				} else if (dataset.getType() == DatasetType.IMAGECOLLECTION) {
					DatasetImageCollection datasetImageCollection = (DatasetImageCollection) dataset;
					if (datasetImageCollection.getHasPyramid()) {
						enable = true;
						break;
					}
				} else if (dataset.getType() == DatasetType.GRIDCOLLECTION) {
					DatasetGridCollection datasetGridCollection = (DatasetGridCollection) dataset;
					if (datasetGridCollection.getHasPyramid()) {
						enable = true;
						break;
					}
				}
			}
		}

		return enable;
	}
}
