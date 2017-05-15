package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class CtrlActionProcess extends CtrlAction {
	private static final String processTreeClassName = "com.supermap.desktop.process.core.ProcessManager";
	private static final String ParameterManagerClassName = "com.supermap.desktop.process.ParameterManager";

	public CtrlActionProcess(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(processTreeClassName)).setVisible(true);
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(ParameterManagerClassName)).setVisible(true);
			FormProcess formProcess = new FormProcess();

			ArrayList<IWorkFlow> workFlows = Application.getActiveApplication().getWorkFlows();
			ArrayList<String> names = new ArrayList<>();
			for (IWorkFlow workFlow : workFlows) {
				names.add(workFlow.getName());
			}
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < formManager.getCount(); i++) {
				if (formManager.get(i) instanceof FormProcess) {
					names.add(formManager.get(i).getText());
				}
			}
			formProcess.setText(StringUtilities.getUniqueName(ControlsProperties.getString("String_WorkFlows"), names));
			Application.getActiveApplication().getMainFrame().getFormManager().add(formProcess);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}



	@Override
	public boolean enable() {
		return true;
	}
}
