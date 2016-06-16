package com.supermap.desktop.utilities;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ApplicationUtilities {
	private ApplicationUtilities() {
		// 不提供构造函数
	}

	public static Datasource getActiveDatasource(boolean canWriteOnly) {
		Datasource datasource = null;
		try {
			for (int index = 0; index < Application.getActiveApplication().getWorkspace().getDatasources().getCount(); index++) {
				Datasource item = Application.getActiveApplication().getWorkspace().getDatasources().get(index);
				if (!canWriteOnly || !item.isReadOnly()) {
					datasource = item;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return datasource;
	}

}
