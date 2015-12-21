package com.supermap.desktop.ui.controls;

import com.supermap.mapping.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * 图层管理树单元格编辑器
 *
 * @author xuzw
 */
class LayersTreeCellEditor implements TreeCellEditor, KeyListener, ActionListener, TreeSelectionListener {

	private LayersTree layersTree = null;

	private LayersTreeCellRenderer layersTreeCellRenderer = null;

	private JTextField jTextFieldName = null;

	private Layer currentLayer = null;

	private ThemeUniqueItem uniqueItem = null;

	private ThemeRangeItem rangeItem = null;

	private ThemeLabelItem labelItem = null;

	private ThemeGridRangeItem gridRangeItem = null;

	private ThemeGridUniqueItem gridUniqueItem = null;

	private Timer timer;

	private TreePath lastPath;

	private TreeNodeData currentTreeNodeData;

	public TreeNodeData getCurrentTreeNodeData() {
		return currentTreeNodeData;
	}

	public void setCurrentTreeNodeData(TreeNodeData currentTreeNodeData) {
		this.currentTreeNodeData = currentTreeNodeData;
	}

	public LayersTreeCellEditor(LayersTree tree, LayersTreeCellRenderer cellRenderer) {
		layersTreeCellRenderer = cellRenderer;
		layersTree = tree;
		layersTree.addTreeSelectionListener(this);
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		TreeNodeData obj = (TreeNodeData) node.getUserObject();
		JPanel panel = (JPanel) layersTreeCellRenderer.getPanel(obj);
		NodeDataType type = obj.getType();
		jTextFieldName = new JTextField();
		jTextFieldName.setPreferredSize(new Dimension(150, 20));
		if (type != NodeDataType.WMSSUB_LAYER) {
			currentTreeNodeData = obj;
			Object innerData = obj.getData();
			if (innerData instanceof Layer) {
				currentLayer = (Layer) innerData;
				jTextFieldName.setText(currentLayer.getCaption());
			}
			if (innerData instanceof ThemeUniqueItem) {
				uniqueItem = (ThemeUniqueItem) innerData;
				jTextFieldName.setText(uniqueItem.getCaption());
			}
			if (innerData instanceof ThemeRangeItem) {
				rangeItem = (ThemeRangeItem) innerData;
				jTextFieldName.setText(rangeItem.getCaption());
			}
			if (innerData instanceof ThemeLabelItem) {
				labelItem = (ThemeLabelItem) innerData;
				jTextFieldName.setText(labelItem.getCaption());
			}
			if (innerData instanceof ThemeGridRangeItem) {
				gridRangeItem = (ThemeGridRangeItem) innerData;
				jTextFieldName.setText(gridRangeItem.getCaption());
			}
			if (innerData instanceof ThemeGridUniqueItem) {
				gridUniqueItem = (ThemeGridUniqueItem) innerData;
				jTextFieldName.setText(gridUniqueItem.getCaption());
			}
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
		// 默认实现，后续进行初始化操作
	}

	@Override
	public Object getCellEditorValue() {
		return jTextFieldName.getText();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		boolean editable = false;
		if (anEvent instanceof MouseEvent) {
			TreePath path = layersTree.getPathForLocation(((MouseEvent) anEvent).getX(), ((MouseEvent) anEvent).getY());
			editable = lastPath != null && path != null && lastPath.equals(path);
		}
		if (editable && shouldStartEditingTimer(anEvent)) {
			startEditingTimer();
		} else if (timer != null && timer.isRunning())
			timer.stop();
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
		layersTree.updateUI();
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
		if (null != currentLayer) {
			currentLayer.setCaption(caption);
		} else if (null != uniqueItem) {
			uniqueItem.setCaption(caption);
		} else if (null != rangeItem) {
			rangeItem.setCaption(caption);
		} else if (null != labelItem) {
			labelItem.setCaption(caption);
		} else if (null != gridUniqueItem) {
			gridUniqueItem.setCaption(caption);
		} else if (null != gridRangeItem) {
			gridRangeItem.setCaption(caption);
		}
		updateUI();
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

	/**
	 * Starts the editing timer.
	 */
	protected void startEditingTimer() {
		if (timer == null) {
			timer = new Timer(1200, this);
			timer.setRepeats(false);
		}
		timer.start();
	}

	protected boolean shouldStartEditingTimer(EventObject event) {
		if ((event instanceof MouseEvent) && SwingUtilities.isLeftMouseButton((MouseEvent) event)) {
			MouseEvent me = (MouseEvent) event;
			HitTestInfo hitTestInfo = layersTree.hitTest(me.getX(), me.getY());
			if (hitTestInfo != null) {
				return me.getClickCount() == 1 && hitTestInfo.getIndex() == hitTestInfo.getIndexCount() - 1;
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (layersTree != null && lastPath != null) {
			layersTree.startEditingAtPath(lastPath);
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (layersTree != null) {
			if (layersTree.getSelectionCount() == 1)
				lastPath = layersTree.getSelectionPath();
			else
				lastPath = null;
		}
		if (timer != null) {
			timer.stop();
		}
	}

}
