package com.supermap.desktop.process.diagram.ui;

import com.supermap.desktop.controls.drop.DropAndDragHandler;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;

/**
 * Created by xie on 2017/2/23.
 */
public class ProcessTree extends JPanel{
    JTree processTree;
    JScrollPane processTreeView;

    public ProcessTree() {
        init();
    }

    private void init() {
        initComponents();
        initLayout();
        initResouces();
        new TreeDropAndDragHandler(DataFlavor.stringFlavor).bindSource(this).addDropTarget(this);
    }

    class TreeDropAndDragHandler extends DropAndDragHandler {
        public TreeDropAndDragHandler(DataFlavor ...flavor){
            super(flavor);
        };
        @Override
        public Object getTransferData(DragGestureEvent dge) {
            JTree tree = (JTree) dge.getComponent();
            return ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject().toString();
        }
    }
    public void initComponents() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("流程控制");
        createNodes(rootNode);
        processTreeView = new JScrollPane();
        processTree = new JTree(rootNode);
        processTree.putClientProperty("JTree.lineStyle", "Horizontal");
        processTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    private void createNodes(DefaultMutableTreeNode rootNode) {
        DefaultMutableTreeNode importProcessNode = new DefaultMutableTreeNode("导入");
        rootNode.add(importProcessNode);

        DefaultMutableTreeNode projectionProcessNode = new DefaultMutableTreeNode("投影转换");
        rootNode.add(projectionProcessNode);
        DefaultMutableTreeNode spatialIndexProcessNode = new DefaultMutableTreeNode("创建空间索引");
        rootNode.add(spatialIndexProcessNode);
        DefaultMutableTreeNode bufferProcessNode = new DefaultMutableTreeNode("缓冲区分析");
        rootNode.add(bufferProcessNode);
    }

    public void initLayout() {
        this.setLayout(new GridBagLayout());
        this.add(this.processTreeView, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(0));
        processTreeView.setViewportView(processTree);
    }

    public void initResouces() {

    }
}
