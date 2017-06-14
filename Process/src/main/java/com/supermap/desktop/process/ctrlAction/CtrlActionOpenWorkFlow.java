package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormProcess;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author XiaJT
 */
public class CtrlActionOpenWorkFlow extends CtrlAction {
	public CtrlActionOpenWorkFlow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		String name = ((IWorkFlow) ((TreeNodeData) ((DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent()).getUserObject()).getData()).getName();
		IFormProcess iFormProcess = (IFormProcess) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.WORK_FLOW, name);

	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
