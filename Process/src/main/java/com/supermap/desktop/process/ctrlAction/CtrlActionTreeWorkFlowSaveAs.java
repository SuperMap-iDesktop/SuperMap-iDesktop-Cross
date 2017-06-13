package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.dialog.JDialogFormSaveAs;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.ui.UICommonToolkit;
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
		String name = ((IWorkFlow) ((TreeNodeData) ((DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent()).getUserObject()).getData()).getName();
		JDialogFormSaveAs jDialogFormSaveAs = new JDialogFormSaveAs();
		jDialogFormSaveAs.setTitle(ProcessProperties.getString("Sting_SaveAsWorkFlow"));
		// TODO: 2017/6/13  
	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
