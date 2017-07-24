package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.CollectionDataset.JDialogCreateCollectionDataset;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2017/7/19.
 * 新建数据集集合
 */
public class CtrlActionNewCollectionDataset extends CtrlAction {
	public CtrlActionNewCollectionDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		//0表示创建的是矢量数据集集合
		new JDialogCreateCollectionDataset(0).showDialog();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			for (Datasource datasource : datasources) {
				//暂时只支持postgerSql的引擎类型
				if (null != datasource && !datasource.isReadOnly() && datasource.getEngineType().equals(EngineType.POSTGRESQL)) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}
}
