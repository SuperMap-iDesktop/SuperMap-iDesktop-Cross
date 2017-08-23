package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDataEntry;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author XiaJT
 */
public class CtrlActionOpenWorkflow extends CtrlAction {
	public CtrlActionOpenWorkflow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IDataEntry<String> workflowEntry = (IDataEntry<String>) ((TreeNodeData) ((DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent()).getUserObject()).getData();
		CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.WORKFLOW, workflowEntry.getKey());
	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
