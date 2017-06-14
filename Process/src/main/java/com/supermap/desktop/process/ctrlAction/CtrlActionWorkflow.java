package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class CtrlActionWorkflow extends CtrlAction {
	private static final String processTreeClassName = "com.supermap.desktop.process.core.ProcessManager";
	private static final String ParameterManagerClassName = "com.supermap.desktop.process.ParameterManager";

	public CtrlActionWorkflow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(processTreeClassName)).setVisible(true);
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(ParameterManagerClassName)).setVisible(true);
			FormWorkflow formWorkflow = new FormWorkflow();

			ArrayList<IWorkflow> workFlows = Application.getActiveApplication().getWorkFlows();
			ArrayList<String> names = new ArrayList<>();
			for (IWorkflow workFlow : workFlows) {
				names.add(workFlow.getName());
			}
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < formManager.getCount(); i++) {
				if (formManager.get(i) instanceof FormWorkflow) {
					names.add(formManager.get(i).getText());
				}
			}
			formWorkflow.setText(StringUtilities.getUniqueName(ControlsProperties.getString("String_WorkFlows"), names));
			Application.getActiveApplication().getMainFrame().getFormManager().add(formWorkflow);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}



	@Override
	public boolean enable() {
		return true;
	}
}
