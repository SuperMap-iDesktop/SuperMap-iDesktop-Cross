package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.WorkflowView.FormWorkflow;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class CtrlActionNewWorkflow extends CtrlAction {
	public CtrlActionNewWorkflow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		FormWorkflow formWorkflow = new FormWorkflow(new Workflow("Workflow"));

		ArrayList<IWorkflow> workFlows = Application.getActiveApplication().getWorkflows();
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
	}


	@Override
	public boolean enable() {
		return true;
	}
}
