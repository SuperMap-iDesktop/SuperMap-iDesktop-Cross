package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.DatasetUtilities;

import javax.swing.*;
import java.text.MessageFormat;

public class CtrlActionDatasourcesCloseAll extends CtrlAction {

	public CtrlActionDatasourcesCloseAll(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		Datasource[] datasources1 = new Datasource[datasources.getCount()];
		for (int i = 0; i < datasources1.length; i++) {
			datasources1[i] = datasources.get(i);
		}
		close(datasources1);
	}

	public static void close(Datasource[] datasources) {
		StringBuilder datasourceAlias = new StringBuilder();
		if (datasources.length <= 0) {
			return;
		}
		for (int i = 0; i <= datasources.length - 2; i++) {
			datasourceAlias.append(datasources[i].getAlias() + '"' + ' ' + '"');
		}
		datasourceAlias.append(datasources[datasources.length - 1].getAlias());
		String message = MessageFormat.format(DataViewProperties.getString("String_CloseDatasourseInfo"), datasourceAlias.toString());
		try {
			if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(message)) {
				// 关闭已经打开的所有数据集，地图和图层
				for (int i = 0; i < datasources.length; i++) {
					Datasource tempDatasource = datasources[i];
					Datasets tempDatasets = tempDatasource.getDatasets();
					DatasetUtilities.closeDataset(tempDatasets);
					String resultInfo = MessageFormat.format(ControlsProperties.getString("String_CloseDatasourseSuccessful"), tempDatasource.getAlias());
					tempDatasource.close();
					Application.getActiveApplication().getOutput().output(resultInfo);
				}
				// 关闭数据源
				ToolbarUIUtilities.updataToolbarsState();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (datasources.getCount() > 0) {
			enable = true;
		}
		return enable;
	}

}