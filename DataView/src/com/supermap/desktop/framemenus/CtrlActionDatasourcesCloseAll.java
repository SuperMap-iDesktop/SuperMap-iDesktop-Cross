package com.supermap.desktop.framemenus;

import java.text.MessageFormat;

import javax.swing.JOptionPane;

import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;

public class CtrlActionDatasourcesCloseAll extends CtrlAction {

	public CtrlActionDatasourcesCloseAll(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		StringBuilder datasourAlias = new StringBuilder();
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (datasources.getCount() <= 0) {
			return;
		}
		for (int i = 0; i <= Application.getActiveApplication().getWorkspace().getDatasources().getCount() - 2; i++) {
			datasourAlias.append(datasources.get(i).getAlias() + '"' + ' ' + '"');
		}
		datasourAlias.append(datasources.get(datasources.getCount() - 1).getAlias());
		String message = MessageFormat.format(DataViewProperties.getString("String_CloseDatasourseInfo"), datasourAlias.toString());
		try {
			if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(message)) {
				// 关闭已经打开的所有数据集，地图和图层
				for (int i = 0; i < datasources.getCount(); i++) {
					Datasource tempDatasource = datasources.get(i);
					Datasets tempDatasets = tempDatasource.getDatasets();
					CommonToolkit.DatasetWrap.CloseDataset(tempDatasets);
				}
				// 关闭数据源
				datasources.closeAll();
				String resultInfo = DataViewProperties.getString("String_CloseDatasourseSuccessful");
				Application.getActiveApplication().getOutput().output(resultInfo);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}

}