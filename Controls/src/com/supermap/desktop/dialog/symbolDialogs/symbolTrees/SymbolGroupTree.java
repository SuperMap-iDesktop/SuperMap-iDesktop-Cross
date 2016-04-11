package com.supermap.desktop.dialog.symbolDialogs.symbolTrees;

import com.supermap.data.SymbolGroup;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * 符号管理器中的树
 *
 * @author XiaJt
 */
public class SymbolGroupTree extends JTree {

	public SymbolGroupTree(SymbolGroup symbolGroup) {
		this.setRootVisible(true);
		this.setShowsRootHandles(true);
		this.setModel(new SymbolGroupTreeModel(new SymbolGroupTreeNode(null, symbolGroup)));
		DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				JLabel jLabel = new JLabel(((SymbolGroupTreeNode) value).getName());
				jLabel.setOpaque(true);
				if (selected) {
					jLabel.setBackground(new Color(150, 185, 255));
				}
				if (leaf) {
					jLabel.setIcon(getLeafIcon());
				} else if (expanded) {
					jLabel.setIcon(getOpenIcon());
				} else {
					jLabel.setIcon(getClosedIcon());
				}
				return jLabel;
			}
		};
		this.setCellRenderer(cellRenderer);
		cellRenderer.setLeafIcon(InternalImageIconFactory.RESOURCE_NODE_VISIBLE);
		cellRenderer.setOpenIcon(InternalImageIconFactory.RESOURCE_NODE_VISIBLE);
		cellRenderer.setClosedIcon(InternalImageIconFactory.RESOURCE_NODE_VISIBLE);
		this.setCellEditor(new SymbolGroupTreeCellEditor(this, cellRenderer));
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	public void setSelectedSymbolGroup(SymbolGroup currentSymbolGroup) {
		SymbolGroupTreeNode root = (SymbolGroupTreeNode) this.getModel().getRoot();
		SymbolGroupTreeNode treeNode = findTreeNode(root, currentSymbolGroup);
		if (treeNode != null) {
			TreePath path = new TreePath(treeNode);
			this.setSelectionPath(path);
			if (this.getParent() != null && this.getParent().getParent() instanceof JScrollPane) {
				this.scrollPathToVisible(path);
			}
		}
	}

	private SymbolGroupTreeNode findTreeNode(SymbolGroupTreeNode node, SymbolGroup currentSymbolGroup) {
		if (node.getCurrentGroup() == currentSymbolGroup) {
			return node;
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				SymbolGroupTreeNode treeNode = findTreeNode(((SymbolGroupTreeNode) node.getChildAt(0)), currentSymbolGroup);
				if (treeNode != null) {
					return treeNode;
				}
			}
		}
		return null;
	}
}
