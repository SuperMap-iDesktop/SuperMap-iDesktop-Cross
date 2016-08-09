package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionRebuildBounds extends CtrlAction {

	public CtrlActionRebuildBounds(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (Application.getActiveApplication().getActiveDatasets().length > 0) {
				for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
					if (isEnable(dataset)) {
						DatasetVector datasetVector = (DatasetVector) dataset;
						if (datasetVector.getRecordCount() > 0) {
							datasetVector.computeBounds();
						}
						String format = DataEditorProperties.getString("String_DatasetComputeBounds");
						String message = String.format(format, dataset.getName() + "@" + dataset.getDatasource().getAlias());
						Application.getActiveApplication().getOutput().output(message);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		Boolean enable = false;
		try {
			if (Application.getActiveApplication().getActiveDatasets().length > 0) {
				for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
					if (isEnable(dataset)) {
						enable = true;
						break;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

	private boolean isEnable(Dataset dataset) {
		boolean enable = false;
		try {
			if (!dataset.getDatasource().isReadOnly() && !dataset.isReadOnly() && dataset instanceof DatasetVector && dataset.getType() != DatasetType.TABULAR
					&& dataset.getType() != DatasetType.LINKTABLE) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}
