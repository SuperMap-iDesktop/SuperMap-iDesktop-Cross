package com.supermap.desktop.controls.utilties;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author XiaJT
 */
public class JTreeUtilties {
	/**
	 * 定位到指定节点并选中
	 */
	public static void locateNode(JTree jTree, DefaultMutableTreeNode node) {
		if (node == null || node.getParent() == null || node.getRoot() == null) {
			return;
		}

		// 获取新创建节点的 Path
		TreePath treePath = new TreePath(node.getPath());
		// 展开该节点的父节点
		if (!jTree.isExpanded(treePath.getParentPath())) {
			jTree.expandPath(treePath.getParentPath());
		}
		// 使新创建的节点可见
		jTree.scrollPathToVisible(treePath);
		// 选中新创建的节点
		jTree.setSelectionPath(treePath);
	}
}
