package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.CollectionDataset.JDialogCreateCollectionDataset;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2017/7/19.
 */
public class CtrlActionNewCollectionDataset extends CtrlAction {
	public CtrlActionNewCollectionDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
			new JDialogCreateCollectionDataset().showDialog();
	}
	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			for (Datasource datasource : datasources) {
				if (null != datasource && !datasource.isReadOnly()) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}
}
