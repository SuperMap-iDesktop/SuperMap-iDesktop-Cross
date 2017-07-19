package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.dialog.SmDialogFormSaveAs;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author XiaJT
 */
public class CtrlActionTreeWorkFlowSaveAs extends CtrlAction {
	public CtrlActionTreeWorkFlowSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IWorkflow currentWorkFlow = (IWorkflow) ((TreeNodeData) ((DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent()).getUserObject()).getData();

		SmDialogFormSaveAs dialogSaveAs = new SmDialogFormSaveAs();
		dialogSaveAs.setDescription(ProcessProperties.getString("String_NewWorkFlowName"));
		dialogSaveAs.setCurrentFormName(currentWorkFlow.getName());
		for (IWorkflow workFlow : Application.getActiveApplication().getWorkflows()) {
			dialogSaveAs.addExistNames(workFlow.getName());
		}
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

		IForm currentForm = null;
		for (int i = 0; i < formManager.getCount(); i++) {
			if (formManager.get(i) instanceof FormWorkflow) {
				String title = formManager.get(i).getText();
				dialogSaveAs.addExistNames(title);
				if (title.equals(currentWorkFlow.getName())) {
					currentForm = formManager.get(i);
				}
			}
		}
		dialogSaveAs.setTitle(ProcessProperties.getString("Sting_SaveAsWorkFlow"));
		if (dialogSaveAs.showDialog() == DialogResult.OK) {
			if (currentForm != null) {
				currentForm.setText(dialogSaveAs.getCurrentFormName());
			}
			Workflow workflow = new Workflow(dialogSaveAs.getCurrentFormName());
			workflow.serializeFrom(currentWorkFlow.serializeTo());
			Application.getActiveApplication().addWorkflow(workflow);
		}
	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
