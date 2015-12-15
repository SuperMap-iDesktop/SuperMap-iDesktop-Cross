package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionCalculateExtremum extends CtrlAction {

	public CtrlActionCalculateExtremum(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			if (Application.getActiveApplication().getActiveDatasets().length > 0) {
				for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
					DatasetGrid datasetGrid = null;
					if (dataset != null && !dataset.isReadOnly()) {
						datasetGrid = (DatasetGrid) dataset;
					}

					String message = "";
					if (datasetGrid != null) {
						if (datasetGrid.calculateExtremum()) {
							message = String.format(DataEditorProperties.getString("String_CalculateExtremum_Success"), dataset.getName() + "@"
									+ dataset.getDatasource().getAlias());
						} else {
							message = String.format(DataEditorProperties.getString("String_CalculateExtremum_Failed"), dataset.getName() + "@"
									+ dataset.getDatasource().getAlias());
						}

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
					if (!dataset.isReadOnly()) {
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
}
