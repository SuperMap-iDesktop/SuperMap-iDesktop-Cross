package com.supermap.desktop.controls.utilities;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author XiaJT
 */
public class JTreeUIUtilities {
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

	public static void expandTree(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), expand);
	}

	private static void expandAll(JTree tree, TreePath parent, boolean expand) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() > 0) {
			for (Enumeration e = node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	public static TreePath getPath(TreeNode treeNode) {
		List<Object> nodes = new ArrayList<Object>();
		if (treeNode != null) {
			nodes.add(treeNode);
			treeNode = treeNode.getParent();
			while (treeNode != null) {
				nodes.add(0, treeNode);
				treeNode = treeNode.getParent();
			}
		}

		return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
	}
}

