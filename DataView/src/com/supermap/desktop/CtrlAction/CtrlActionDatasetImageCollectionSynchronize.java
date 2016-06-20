package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetImageCollection;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.progress.FormProgress;

/**
 * @author XiaJt
 */
public class CtrlActionDatasetImageCollectionSynchronize extends CtrlAction {
	public CtrlActionDatasetImageCollectionSynchronize(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
//		for (Dataset dataset : Application.getActiveApplication().getActiveDatasets()) {
//			if (((DatasetImageCollection) dataset).synchronize()) {
//				Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_MSG_DatasetImageCollectionSynchronizeSuccess"), dataset.getName()));
//			} else {
//				Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_MSG_DatasetImageCollectionSynchronizeFailed"), dataset.getName()));
//			}
//		}
		// 组件没做事件，无进度
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();

		FormProgress formProgress = new FormProgress(DataViewProperties.getString("String_Form_DatasetImageCollectionSynchronize"));
		formProgress.doWork(new DatasetImageCollectionSynchronizeCallable(activeDatasets));
	}

	@Override
	public boolean enable() {
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		for (Dataset activeDataset : activeDatasets) {
			if (!(activeDataset instanceof DatasetImageCollection)) {
				return false;
			}
		}
		return true;
	}
}
