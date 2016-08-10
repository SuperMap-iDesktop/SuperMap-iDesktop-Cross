package com.supermap.desktop.ui.controls;

import com.supermap.realspace.Feature3D;
import com.supermap.realspace.Feature3Ds;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.TerrainLayer;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * 三维图层树单元格编辑器
 * 
 * @author xuzw
 * 
 */
class Layer3DsTreeCellEditor implements TreeCellEditor, KeyListener {

	private Layer3DsTree layer3DsTree = null;

	private Layer3DsTreeCellRenderer treeCellRender = null;

	private JTextField jTextFieldName = null;

	private Layer3D currentLayer = null;

	private TerrainLayer currentTerrainLayer = null;

	private Feature3Ds currentFeature3Ds = null;

	private Feature3D currentFeature3D = null;

	public Layer3DsTreeCellEditor(Layer3DsTree tree, Layer3DsTreeCellRenderer cellRenderer) {
		treeCellRender = cellRenderer;
		layer3DsTree = tree;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) value;
		TreeNodeData nodeData = (TreeNodeData) defaultMutableTreeNode.getUserObject();
		Object obj = nodeData.getData();
		JPanel panel = (JPanel) treeCellRender.getPanel(nodeData);
		currentLayer = null;

		currentTerrainLayer = null;

		currentFeature3Ds = null;

		currentFeature3D = null;

		if (obj instanceof Layer3D) {
			Layer3D layer = (Layer3D) obj;
			currentLayer = layer;
			jTextFieldName = new JTextField(layer.getCaption());
			jTextFieldName.setFont(tree.getFont());
			jTextFieldName.addKeyListener(this);
		} else if (obj instanceof TerrainLayer) {
			TerrainLayer telayer = (TerrainLayer) obj;
			currentTerrainLayer = telayer;
			jTextFieldName = new JTextField(telayer.getCaption());
			jTextFieldName.setFont(tree.getFont());
			jTextFieldName.addKeyListener(this);
		} else if (obj instanceof Feature3Ds) {
			Feature3Ds telayer = (Feature3Ds) obj;
			currentFeature3Ds = telayer;
			jTextFieldName = new JTextField(currentFeature3Ds.getName());
			jTextFieldName.setFont(tree.getFont());
			jTextFieldName.addKeyListener(this);
		} else if (obj instanceof Feature3D) {
			Feature3D telayer = (Feature3D) obj;
			currentFeature3D = telayer;
			jTextFieldName = new JTextField(currentFeature3D.getName());
			jTextFieldName.setFont(tree.getFont());
			jTextFieldName.addKeyListener(this);
		}

		int componetsCount = panel.getComponentCount();
		Component component = panel.getComponent(componetsCount - 1);
		if (component instanceof JLabel) {
			JLabel label = (JLabel) component;
			label.setText("");
		}

		panel.add(jTextFieldName);
		return panel;
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// 默认实现，后续进行初始化操作
	}

	@Override
	public void cancelCellEditing() {
		// do nothing
	}

	@Override
	public Object getCellEditorValue() {
		return jTextFieldName.getText();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) anEvent;
			if (mouseEvent.getClickCount() == 3) {
				return true;
			}
		}
		if (anEvent == null) {
			return true;
		}
		return false;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// 默认实现，后续进行初始化操作
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		validateLayerCaption((String) getCellEditorValue());
		return true;
	}

	private void fireStopCellEditing() {
		stopCellEditing();
	}

	private void fireCellEditable(EventObject obj) {
		isCellEditable(obj);
	}

	private void updateUI() {
		layer3DsTree.updateUI();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// do nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing
	}

	public void validateLayerCaption(String caption) {
		if (currentLayer != null) {
			currentLayer.setCaption(caption);
		} else if (currentTerrainLayer != null) {
			currentTerrainLayer.setCaption(caption);
		} else if (currentFeature3Ds != null) {
			currentFeature3Ds.setName(caption);
		} else if (currentFeature3D != null) {
			currentFeature3D.setName(caption);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int keyCode = e.getKeyChar();
		if (keyCode == KeyEvent.VK_ENTER) {
			fireStopCellEditing();
			fireCellEditable(e);
			updateUI();
		}
	}

}
