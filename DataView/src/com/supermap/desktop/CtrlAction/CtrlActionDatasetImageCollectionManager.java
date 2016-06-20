package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetImageCollection;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJt
 */
public class CtrlActionDatasetImageCollectionManager extends CtrlAction {
	public CtrlActionDatasetImageCollectionManager(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		super.run();
	}

	@Override
	public boolean enable() {
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		return !(activeDatasets.length > 1 || activeDatasets.length <= 0) && activeDatasets[0] instanceof DatasetImageCollection;
	}
}
