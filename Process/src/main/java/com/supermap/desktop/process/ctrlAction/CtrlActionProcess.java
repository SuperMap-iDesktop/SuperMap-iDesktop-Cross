package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;

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
			formProcess.setText(getSingleTitle(ControlsProperties.getString("String_WorkFlows"), names));

			Application.getActiveApplication().getMainFrame().getFormManager().add(formProcess);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String getSingleTitle(String currentName, ArrayList<String> names) {
		return getSingleTitle(currentName, names, 0);
	}

	private String getSingleTitle(String currentName, ArrayList<String> names, int i) {
		String currentNameTemp = currentName;
		if (i != 0) {
			currentNameTemp = currentName + "_" + i;
		}
		if (names.contains(currentNameTemp)) {
			return getSingleTitle(currentName, names, ++i);
		}
		return currentNameTemp;
	}

	@Override
	public boolean enable() {
		return true;
	}
}
