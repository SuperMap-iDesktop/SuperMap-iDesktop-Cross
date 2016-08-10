package com.supermap.desktop.utilities;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.properties.CoreProperties;

public class TabularUtilities {

	/**
	 * 返回属性表的名字 数据集@数据源
	 * 
	 * @return
	 */
	private TabularUtilities() {
		// 工具类不提供构造函数
	}

	public static String getTabularName() {
		String name = "New Tabular";
		if (Application.getActiveApplication().getActiveDatasets() != null && Application.getActiveApplication().getActiveDatasets().length > 0) {
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

	/**
	 * 刷新已打开的指定数据集的属性表
	 * 
	 * @param datasetVector
	 */
	public static void refreshTabularForm(DatasetVector datasetVector) {
		for (int i = 0; i < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); i++) {
			IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(i);

			if (form instanceof IFormTabular && ((IFormTabular) form).getRecordset().getDataset() == datasetVector) {
				// 刷新已打开的当前修改数据的属性表，不同的窗口绑定不同的表格
				Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
				((IFormTabular) form).setRecordset(recordset);
			}
		}
	}
}
