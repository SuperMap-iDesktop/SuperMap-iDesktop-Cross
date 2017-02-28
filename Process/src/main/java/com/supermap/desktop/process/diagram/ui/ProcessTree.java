package com.supermap.desktop.process.diagram.ui;

import com.supermap.desktop.controls.drop.DropAndDragHandler;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IProcessGroup;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragGestureEvent;

/**
 * Created by xie on 2017/2/23.
 */
public class ProcessTree extends JPanel {
    private JTree processTree;
    private JScrollPane processTreeView;
    private DefaultMutableTreeNode rootNode;

    public ProcessTree() {
        init();
    }

    private void init() {
        initComponents();
        initLayout();
        initResouces();
        new TreeDropAndDragHandler(DataFlavor.stringFlavor).bindSource(this.processTree).addDropTarget(this.processTree);
    }

    class TreeDropAndDragHandler extends DropAndDragHandler {
        public TreeDropAndDragHandler(DataFlavor... flavor) {
            super(flavor);
        }

        @Override
        public Object getTransferData(DragGestureEvent dge) {
            JTree tree = (JTree) dge.getComponent();
            String result = "";
            if (((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject() instanceof IProcess) {
                result = ((IProcess) ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject()).getTitle();
            } else {
                result = ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject().toString();
            }
            return result;
        }
    }

    public void initComponents() {
        this.rootNode = new DefaultMutableTreeNode(ProcessProperties.getString("String_Process"));
        this.processTreeView = new JScrollPane();
        this.processTree = new JTree(rootNode);
        this.processTree.putClientProperty("JTree.lineStyle", "Horizontal");
        this.processTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * 添加节点
     *
     * @param processGroup
     */
    public void createNodes(IProcessGroup... processGroup) {
        for (IProcessGroup iProcessGroup : processGroup) {
            DefaultMutableTreeNode processGroupNode = new DefaultMutableTreeNode(iProcessGroup);
            int childCount = iProcessGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode processNode = new DefaultMutableTreeNode(iProcessGroup.getProcessByIndex(i));
                processGroupNode.add(processNode);
            }
            this.rootNode.add(processGroupNode);
        }
    }

    public void initLayout() {
        this.setLayout(new GridBagLayout());
        this.add(this.processTreeView, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(0));
        this.processTreeView.setViewportView(processTree);
    }

    public void initResouces() {

    }
}
