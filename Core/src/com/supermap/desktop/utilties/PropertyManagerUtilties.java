package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IPropertyManager;

/**
 * Created by Administrator on 2015/11/18.
 */
public class PropertyManagerUtilties {
	private PropertyManagerUtilties(){

	}

	/**
	 * 刷新属性面板
	 */
	public static void refreshPropertyManager(){
		IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
		if(propertyManager != null ){
			propertyManager.refreshData();
		}
	}
}
