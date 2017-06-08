package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;


/**
 * @author XiaJT
 */
public class CtrlActionProcessDelete extends CtrlAction {
	public CtrlActionProcessDelete(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();
		for (TreePath treePath : workspaceTree.getSelectionPaths()) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) treeNode.getUserObject();
			Workflow workflow = (Workflow) selectedNodeData.getData();
			workflows.add(workflow);
		}
		deleteProcess(workflows);
		ToolbarUIUtilities.updataToolbarsState();
	}

	public static void deleteProcess(ArrayList<Workflow> workflows) {
		try {
			String message = CoreProperties.getString("String_ProcessDelete_Confirm");
			if (workflows.size() == 1) {
				message = message + "\r\n" + String.format(CoreProperties.getString("String_ProcessDelete_Confirm_One"),workflows.get(0).getName());
			} else {
				message = message + "\r\n" + String.format(CoreProperties.getString("String_ProcessDelete_Confirm_Multi"), workflows.size());
			}
			if (JOptionPaneUtilities.showConfirmDialog(message) == JOptionPane.OK_OPTION) {
				for (Workflow workflow : workflows) {
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
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}