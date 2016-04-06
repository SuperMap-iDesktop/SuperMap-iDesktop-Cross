package com.supermap.desktop.dialog.symbolDialogs.symbolTrees;

import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

/**
 * @author XiaJt
 */
public class SymbolGroupTreeCellEditor extends DefaultTreeCellEditor {
	private static final String[] UN_SUPPORT_CHARS = new String[]{
			"/", "\\", ":", "?", "\"", "<", ">", "|"
	};

	private SmTextFieldLegit textFieldEdit;
	private SymbolGroupTreeNode currentEditSymbolGroupTreeNode;

	public SymbolGroupTreeCellEditor(JTree jTree, DefaultTreeCellRenderer cellRenderer) {
		super(jTree, cellRenderer);
		textFieldEdit = new SmTextFieldLegit();
		textFieldEdit.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return isLegitValue(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		textFieldEdit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					stopCellEditing();
				} else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
					cancelCellEditing();
				}
			}
		});
		textFieldEdit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				stopCellEditing();
			}
		});
		textFieldEdit.setForeground(Color.BLACK);
		editingComponent = textFieldEdit;
	}

	private boolean isLegitValue(String textFieldValue) {
		// 当前节点不为空
		if (currentEditSymbolGroupTreeNode == null) {
			return false;
		}
		// 当前输入不为空
		if (StringUtilties.isNullOrEmpty(textFieldValue)) {
			return false;
		}
		// 不包含非法字符
		for (String unSupportChar : UN_SUPPORT_CHARS) {
			if (textFieldValue.contains(unSupportChar)) {
				return false;
			}
		}
		// 如果为根节点直接返回
		if (currentEditSymbolGroupTreeNode.getParent() == null) {
			return true;
		}
		// 遍历同级是否有相同名称对象
		Enumeration children = currentEditSymbolGroupTreeNode.getParent().children();
		if (children.hasMoreElements()) {
			SymbolGroupTreeNode symbolGroupTreeNode = (SymbolGroupTreeNode) children.nextElement();
			if (symbolGroupTreeNode != currentEditSymbolGroupTreeNode && symbolGroupTreeNode.getName().equals(textFieldValue)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		currentEditSymbolGroupTreeNode = (SymbolGroupTreeNode) value;

		setTree(tree);
		lastRow = row;
		determineOffset(tree, value, isSelected, expanded, leaf, row);

		if (editingComponent != null) {
			editingContainer.remove(editingComponent);
		}

		textFieldEdit.setText(((SymbolGroupTreeNode) value).getName());
		editingComponent = textFieldEdit;

		// this is kept for backwards compatability but isn't really needed
		// with the current BasicTreeUI implementation.
		TreePath newPath = tree.getPathForRow(row);

		canEdit = (lastPath != null && newPath != null &&
				lastPath.equals(newPath));

		Font font = getFont();

		if (font == null) {
			if (renderer != null)
				font = renderer.getFont();
			if (font == null)
				font = tree.getFont();
		}
		editingContainer.setFont(font);
		prepareForEditing();
		return editingContainer;

//		textFieldEdit.setText(currentEditSymbolGroupTreeNode.getName());
//		return textFieldEdit;
	}

	@Override
	public Object getCellEditorValue() {
		return textFieldEdit.getText();
	}

	@Override
	public boolean stopCellEditing() {
		if (!super.stopCellEditing()) {
			return false;
		}
		String text = textFieldEdit.getText();
		if (!isLegitValue(text)) {
			return false;
		}
		currentEditSymbolGroupTreeNode.setName(text);
		tree.getModel().valueForPathChanged(new TreePath(currentEditSymbolGroupTreeNode), text);
		return true;
	}

	@Override
	public void cancelCellEditing() {
		super.cancelCellEditing();
	}

}
