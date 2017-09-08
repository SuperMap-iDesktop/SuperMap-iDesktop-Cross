package com.supermap.desktop.ui.controls.trees;

import com.supermap.desktop.controls.utilities.JTreeUIUtilities;
import com.supermap.desktop.ui.controls.comboBox.JSearchComboBox;
import com.supermap.desktop.ui.controls.comboBox.SearchItemValueGetter;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author XiaJT
 */
public class SmTreeSearchComboBox<T extends TreeNode> extends JSearchComboBox<T> {
	private JTree tree;
	private SmTreeSearchComboBoxModel<T> model;

	public SmTreeSearchComboBox(final JTree tree) {
		super();
		if (!(tree instanceof SearchItemValueGetter)) {
			throw new UnsupportedOperationException("tree should implements SearchItemValueGetter");
		}
		this.setPreferredSize(new Dimension(200, 23));
		this.tree = tree;
		model = new SmTreeSearchComboBoxModel<>(this, (SearchItemValueGetter) tree);
		this.setModel(model);
		this.setRenderer(new ListCellRenderer<T>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
				return tree.getCellRenderer().getTreeCellRendererComponent(tree, value, isSelected, true, true, tree.getRowForPath(JTreeUIUtilities.getPath(value)), cellHasFocus);
			}
		});
	}

	public void search(String keyWord) {
		model.search(keyWord);
	}

	public JTree getTree() {
		return tree;
	}

	@Override
	public void setSelectedItem(Object anObject) {
		if (anObject instanceof TreeNode) {
			TreePath path = JTreeUIUtilities.getPath(((TreeNode) anObject));
			tree.scrollPathToVisible(path);
			tree.setSelectionPath(path);
		} else if (anObject instanceof String) {
			model.updateModel((String) anObject);
		}
	}
}
