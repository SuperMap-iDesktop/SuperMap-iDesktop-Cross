package com.supermap.desktop.CtrlAction.Map;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DialogMapCacheBuilder;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by lixiaoyao on 2017/3/29.
 */
public class CtrlActionWorkspaceManagerMapCacheBuilder extends CtrlAction {

    public CtrlActionWorkspaceManagerMapCacheBuilder(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
            TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
            String mapName = (String) selectedNodeData.getData();
            Workspace workspace = Application.getActiveApplication().getWorkspace();
            Map map = new Map(workspace);
            if (map.open(mapName)) {
                DialogMapCacheBuilder dialogMapCacheBuilder = new DialogMapCacheBuilder((JFrame) Application.getActiveApplication().getMainFrame(), true, map);
                DialogResult result = dialogMapCacheBuilder.showDialog();
            }else{
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_OpenMapFailed"));
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public boolean enable() {
        boolean enable = false;
        WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
        if (workspaceTree.getSelectionCount() == 1) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
            TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
            if (selectedNodeData != null && selectedNodeData.getType() == NodeDataType.MAP_NAME) {
                enable = true;
            }
        }
        return enable;
    }
}
