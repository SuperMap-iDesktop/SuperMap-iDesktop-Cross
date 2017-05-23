package com.supermap.desktop.CtrlAction.Datasource;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.DatasourceUtilities;

public class CtrlActionDatasourceNewMemory extends CtrlAction {

	public CtrlActionDatasourceNewMemory(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			String datasourceAlias = DatasourceUtilities.getAvailableDatasourceAlias("MemoryDatasource", 0);
			DatasourceConnectionInfo info = new DatasourceConnectionInfo();
			info.setEngineType(EngineType.MEMORY);
			info.setAlias(datasourceAlias);
			Datasource datasource = datasources.create(info);
			if (datasource != null) {
//				UICommonToolkit.getWorkspaceTreeDatasourceNode();
			}

			JDialogDatasourceNewMemory dialog = new JDialogDatasourceNewMemory();
			dialog.setVisible(true);			
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}