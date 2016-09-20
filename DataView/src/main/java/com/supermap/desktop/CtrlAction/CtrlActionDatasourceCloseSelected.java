package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionDatasourceCloseSelected extends CtrlAction {

	public CtrlActionDatasourceCloseSelected(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		close();
	}

	public static void close() {
		try {
			if (Application.getActiveApplication().getActiveDatasources().length > 0) {
				CtrlActionDatasourcesCloseAll.close(Application.getActiveApplication().getActiveDatasources());
			}
//			String message = MessageFormat.format(DataViewProperties.getString("String_CloseDatasourseInfo"), Application.getActiveApplication()
//					.getActiveDatasources()[0].getAlias());
//			if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(message)) {
//				Datasource activeDatasource = Application.getActiveApplication().getActiveDatasources()[0];
//				String resultInfo = "";
//				if (DatasourceUtilities.closeDatasource(activeDatasource)) {
//					Application.getActiveApplication().setActiveDatasources(null);
//					resultInfo = DataViewProperties.getString("String_CloseDatasourseSuccessful");
//				} else {
//					resultInfo = DataViewProperties.getString("String_CloseDatasourseFailed");
//				}
//				Application.getActiveApplication().getOutput().output(resultInfo);
//				ToolbarUIUtilities.updataToolbarsState();
//			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			enable = true;
		}
		return enable;
	}

}