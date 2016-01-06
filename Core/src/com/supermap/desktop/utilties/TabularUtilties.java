package com.supermap.desktop.utilties;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.properties.CoreProperties;

public class TabularUtilties {

	/**
	 * 返回属性表的名字 数据集@数据源
	 * 
	 * @return
	 */
	private TabularUtilties() {
		// 工具类不提供构造函数
	}

	public static String getTabularName() {
		String name = "New Tabular";
		if(Application.getActiveApplication().getActiveDatasets()!=null && Application.getActiveApplication().getActiveDatasets().length>0){
			name = Application.getActiveApplication().getActiveDatasets()[0].getName() + "@"
					+ Application.getActiveApplication().getActiveDatasources()[0].getAlias();
		}
		return name;
	}

	/**
	 * 打开属性表
	 * 
	 * @param dataset
	 */
	public static IFormTabular openDatasetVectorFormTabular(Dataset dataset) {
		IFormTabular formTabular = null;

		try {
			formTabular = (IFormTabular) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.TABULAR);
			if (formTabular == null) {
				Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_OpenDatasetVectorFormTabular_Failed"));
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return formTabular;
	}

}
