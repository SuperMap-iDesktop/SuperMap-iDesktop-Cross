package com.supermap.desktop.CtrlAction.property;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.property.JDialogDataPropertyContainer;
import com.supermap.desktop.controls.property.WorkspaceTreeDataPropertyFactory;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionDataProperty extends CtrlAction {

	public CtrlActionDataProperty(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		setSelectedDataProperty();
		JDialog dialogPropertyContainer = (JDialog) Application.getActiveApplication().getMainFrame().getPropertyManager();
		dialogPropertyContainer.setVisible(true);
	}

	/**
	 * 设置选中节点的属性信息（这段代码与 WorkspaceComponentManager 里的重复，后续优化属性的时候再行合并）
	 */
	private void setSelectedDataProperty() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();

		if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();

			IProperty[] properties = WorkspaceTreeDataPropertyFactory.createProperties(nodeData);
			IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
			propertyManager.setProperty(properties);
		} else {
			Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
		}
	}
}
