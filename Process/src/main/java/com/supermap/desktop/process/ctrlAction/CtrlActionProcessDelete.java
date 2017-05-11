package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author XiaJT
 */
public class CtrlActionProcessDelete extends CtrlAction {
	public CtrlActionProcessDelete(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
		TreeNodeData nodeData = (TreeNodeData) lastSelectedPathComponent.getUserObject();
		Workflow workflow = (Workflow) nodeData.getData();
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		for (int count = formManager.getCount() - 1; count >= 0; count--) {
			IForm form = formManager.get(count);
			if (form.getWindowType() == WindowType.WORK_FLOW && form.getText().equals(workflow.getName())) {
				formManager.close(form);
				break;
			}
		}
		Application.getActiveApplication().removeWorkFlow(workflow);
	}

	@Override
	public boolean enable() {
		return true;
	}
}
