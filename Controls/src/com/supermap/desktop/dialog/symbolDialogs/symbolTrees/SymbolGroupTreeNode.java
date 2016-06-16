package com.supermap.desktop.dialog.symbolDialogs.symbolTrees;

import com.supermap.data.SymbolGroup;
import com.supermap.data.SymbolGroups;
import com.supermap.desktop.core.IteratorEnumerationAdapter;
import com.supermap.desktop.utilities.LogUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author XiaJt
 */
public class SymbolGroupTreeNode extends DefaultMutableTreeNode {

	private SymbolGroup currentGroup;
	private SymbolGroupTreeNode parentNode;
	private List<SymbolGroupTreeNode> childRens;

	public SymbolGroupTreeNode(SymbolGroupTreeNode parentNode, SymbolGroup symbolGroup) {
		if (symbolGroup == null) {
			LogUtilities.debug("symbolGroup is empty");
			return;
		}
		this.parentNode = parentNode;
		this.currentGroup = symbolGroup;
		SymbolGroups childGroups = symbolGroup.getChildGroups();
		if (childGroups.getCount() > 0) {
			childRens = new ArrayList<>();
			for (int i = 0; i < childGroups.getCount(); i++) {
				SymbolGroupTreeNode subNode = new SymbolGroupTreeNode(this, childGroups.get(i));
				childRens.add(subNode);
			}
		}
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return childRens.get(childIndex);
	}

	@Override
	public int getChildCount() {
		if (childRens == null) {
			return 0;
		}
		return childRens.size();
	}

	@Override
	public TreeNode getParent() {
		return parentNode;
	}

	@Override
	public int getIndex(TreeNode node) {
		if (node == null || !(node instanceof SymbolGroupTreeNode)) {
			return -1;
		}
		return childRens.lastIndexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return childRens == null || childRens.size() <= 0;
	}

	@Override
	public Enumeration children() {
		return new IteratorEnumerationAdapter<>(childRens.iterator());
	}

	public String getName() {
		return currentGroup.getName();
	}

	public void setName(String name) {
		if (!currentGroup.getName().equals(name)) {
			currentGroup.setName(name);
		}
	}

	public SymbolGroup getCurrentGroup() {
		return currentGroup;
	}
}
