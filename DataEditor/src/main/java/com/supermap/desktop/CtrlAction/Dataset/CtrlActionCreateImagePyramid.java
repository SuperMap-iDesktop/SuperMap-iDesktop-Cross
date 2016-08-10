package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.progress.callable.CreateImagePyramidCallable;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.CursorUtilities;

import java.util.ArrayList;
import java.util.List;

public class CtrlActionCreateImagePyramid extends CtrlAction {

	public CtrlActionCreateImagePyramid(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		CursorUtilities.setWaitCursor();
		try {
			ArrayList<Dataset> datasets = new ArrayList<Dataset>();
			for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
				if (dataset instanceof DatasetGrid || dataset instanceof DatasetImage || dataset instanceof DatasetImageCollection || dataset instanceof DatasetGridCollection) {
					datasets.add(dataset);
				}
			}
			List<Dataset> datasetClosed = DatasetUIUtilities.sureDatasetClosed(datasets);
//			((DatasetImageCollection) datasetClosed.get(0)).buildPyramid();
			if (datasetClosed.size() > 0) {
				FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
				formProgressTotal.doWork(new CreateImagePyramidCallable(datasetClosed.toArray(new Dataset[datasetClosed.size()])));
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
				String server = dataset.getDatasource().getConnectionInfo().getServer();
				if (server.toLowerCase().endsWith(".img") || server.toLowerCase().endsWith(".tif") || server.toLowerCase().endsWith(".tiff")) {
					if (dataset.getType() == DatasetType.IMAGE) {
						DatasetImage datasetImage = (DatasetImage) dataset;
						if (!datasetImage.getHasPyramid()) {
							enable = true;
							break;
						}
					} else if (dataset.getType() == DatasetType.GRID) {
						DatasetGrid datasetGrid = (DatasetGrid) dataset;
						if (!datasetGrid.getHasPyramid()) {
							enable = true;
							break;
						}
					}
				}
			} else if (!Application.getActiveApplication().getActiveDatasets()[0].getDatasource().isReadOnly()) {
				if (dataset.getType() == DatasetType.IMAGE) {
					DatasetImage datasetImage = (DatasetImage) dataset;
					if (!datasetImage.getHasPyramid()) {
						enable = true;
						break;
					}
				} else if (dataset.getType() == DatasetType.GRID) {
					DatasetGrid datasetGrid = (DatasetGrid) dataset;
					if (!datasetGrid.getHasPyramid()) {
						enable = true;
						break;
					}
				} else if (dataset.getType() == DatasetType.IMAGECOLLECTION) {
					DatasetImageCollection datasetImageCollection = (DatasetImageCollection) dataset;
					if (!datasetImageCollection.getHasPyramid()) {
						enable = true;
						break;
					}
				} else if (dataset.getType() == DatasetType.GRIDCOLLECTION) {
					DatasetGridCollection datasetGridCollection = (DatasetGridCollection) dataset;
					if (!datasetGridCollection.getHasPyramid()) {
						enable = true;
						break;
					}
				}
			}
		}

		return enable;
	}
}
