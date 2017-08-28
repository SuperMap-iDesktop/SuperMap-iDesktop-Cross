package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDataEntry;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.WorkflowView.FormWorkflow;
import com.supermap.desktop.WorkflowView.WorkflowViewProperties;
import com.supermap.desktop.dialog.SmDialogFormSaveAs;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author XiaJT
 */
public class CtrlActionTreeWorkflowSaveAs extends CtrlAction {
	public CtrlActionTreeWorkflowSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		IDataEntry<String> workflowEntry = (IDataEntry<String>) ((TreeNodeData) ((DefaultMutableTreeNode) workspaceTree.getLastSelectedPathComponent()).getUserObject()).getData();

		SmDialogFormSaveAs dialogSaveAs = new SmDialogFormSaveAs();
		dialogSaveAs.setDescription(WorkflowViewProperties.getString("String_NewWorkflowName"));
		dialogSaveAs.setCurrentFormName(workflowEntry.getKey());
		for (IDataEntry entry : Application.getActiveApplication().getWorkflowEntries()) {
			dialogSaveAs.addExistNames(entry.getKey());
		}
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

		IForm currentForm = null;
		for (int i = 0; i < formManager.getCount(); i++) {
			if (formManager.get(i) instanceof FormWorkflow) {
				String title = formManager.get(i).getText();
				dialogSaveAs.addExistNames(title);
				if (title.equals(workflowEntry.getKey())) {
					currentForm = formManager.get(i);
				}
			}
		}
		dialogSaveAs.setTitle(WorkflowViewProperties.getString("Sting_SaveAsWorkflow"));
		if (dialogSaveAs.showDialog() == DialogResult.OK) {
			if (currentForm != null) {
				currentForm.setText(dialogSaveAs.getCurrentFormName());
			}

			String newName = dialogSaveAs.getCurrentFormName();
			Application.getActiveApplication().addWorkflow(newName, workflowEntry.getValue());
		}
	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
