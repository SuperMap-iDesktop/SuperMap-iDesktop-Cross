package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

/**
 * 树下拉列表
 * 
 * @author xuzw
 *
 */
class TreeComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;
	/**
	 * 显示用的树
	 */
	private JTree tree;

	public TreeComboBox() {
		this(new JTree(), "");
	}

	public TreeComboBox(JTree tree, String rootName) {
		this(tree, null, false);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(rootName);
		this.setSelectedItem(new TreePath(node));
	}

	public TreeComboBox(JTree tree, TreeCellRenderer treeCellRenderer) {
		this(tree, treeCellRenderer, false);
	}

	/**
	 * 显示根节点的TreeComboBox
	 * 
	 * @param tree
	 * @param treeCellRenderer
	 * @param showRoot
	 */
	public TreeComboBox(JTree tree, TreeCellRenderer treeCellRenderer, boolean showRoot) {
		this.setTree(tree);
		tree.setCellRenderer(treeCellRenderer);
		if (!showRoot) {
			tree.expandPath(new TreePath(tree.getModel().getRoot()));
			tree.setRootVisible(true);
		}

	}

	/**
	 * 设置树
	 * 
	 * @param tree JTree
	 */
	public void setTree(JTree tree) {

		this.tree = tree;
		if (tree != null) {
			this.setSelectedItem(tree.getSelectionPath());
			this.setRenderer(new TreeComboBoxRenderer());
		}
		this.updateUI();
	}

	/**
	 * 取得树
	 * 
	 * @return JTree
	 */
	public JTree getTree() {
		return tree;
	}

	/**
	 * 设置当前选择的树路径
	 * 
	 * @param o Object
	 */
	@Override
	public void setSelectedItem(Object o) {
		this.tree.setSelectionPath((TreePath) o);
		this.getModel().setSelectedItem(o);
	}

	/**
	 * 获得当前选中的节点的值
	 * 
	 * @return o Object
	 */
	public Object getSelectedObject() {
		TreePath treePath = (TreePath) this.getSelectedItem();
		if (treePath == null) {
			return null;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		return node.getUserObject();
	}

	@Override
	public void updateUI() {
		ComboBoxUI comboBoxUi = (ComboBoxUI) UIManager.getUI(this);
		if (comboBoxUi instanceof MetalComboBoxUI) {
			comboBoxUi = new MetalTreeComboBoxUI();
		} else if (comboBoxUi instanceof com.sun.java.swing.plaf.motif.MotifComboBoxUI) {
			comboBoxUi = new MotifTreeComboBoxUI();
		} else if (comboBoxUi instanceof WindowsComboBoxUI) {
			comboBoxUi = new WindowsTreeComboBoxUI();
		}
		setUI(comboBoxUi);
	}

	// richer:皮肤

	class MetalTreeComboBoxUI extends MetalComboBoxUI {
		@Override
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	class WindowsTreeComboBoxUI extends WindowsComboBoxUI {
		@Override
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	class MotifTreeComboBoxUI extends MotifComboBoxUI {
		private static final long serialVersionUID = 1L;

		@Override
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	private class TreeComboBoxRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		private transient Object valueTemp;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			setValueTemp(value);
			if (value != null) {
				TreePath path = (TreePath) value;
				TreeNode node = (TreeNode) path.getLastPathComponent();
				setValueTemp(node);
				TreeCellRenderer treeCellRenderer = tree.getCellRenderer();
				JLabel lb = (JLabel) treeCellRenderer.getTreeCellRendererComponent(tree, value, isSelected, false, node.isLeaf(), index, cellHasFocus);
				return lb;
			}
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}

		public Object getValueTemp() {
			return valueTemp;
		}

		public void setValueTemp(Object valueTemp) {
			this.valueTemp = valueTemp;
		}
	}
}

/*
 * richer:弹出部分
 */
class TreePopup extends JPopupMenu implements ComboPopup {
	private static final long serialVersionUID = 1L;
	protected TreeComboBox treeComboBox;
	protected JScrollPane scrollPane;

	protected transient MouseMotionListener mouseMotionListenerTemp;
	protected transient MouseListener mouseListenerTemp;
	// 键盘事件，支持回车键
	protected transient KeyListener keyListenerTemp = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			abstractkeyListenerTempLisenter(e);

		}

	};

	private transient MouseListener treeSelectListener = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			abstractTreeSelectListener(e);

		}
	};

	private void abstractTreeSelectListener(MouseEvent e) {
		JTree tree = (JTree) e.getSource();
		TreePath treePath = tree.getPathForLocation(e.getPoint().x, e.getPoint().y);
		if (treePath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

		if (node.isLeaf() && e.getButton() == 1) {
			treeComboBox.setSelectedItem(treePath);
			togglePopup();
			MenuSelectionManager.defaultManager().clearSelectedPath();
		}

		if (!node.isLeaf()) {
			treeComboBox.setSelectedItem(treePath);
			togglePopup();
			MenuSelectionManager.defaultManager().clearSelectedPath();
		}
	}

	private void abstractkeyListenerTempLisenter(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() instanceof JTree) {
			JTree tree = (JTree) e.getSource();
			TreePath path = tree.getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

			if (node.isLeaf()) {
				treeComboBox.setSelectedItem(path);
				togglePopup();
				MenuSelectionManager.defaultManager().clearSelectedPath();
			}

			if (!node.isLeaf()) {
				treeComboBox.setSelectedItem(path);
				togglePopup();
				MenuSelectionManager.defaultManager().clearSelectedPath();
			}
		}
	}

	public TreePopup(JComboBox comboBox) {
		this.treeComboBox = (TreeComboBox) comboBox;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		this.setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());
		JTree tree = this.treeComboBox.getTree();
		if (tree != null) {
			this.scrollPane = new JScrollPane(tree);
			this.scrollPane.setBorder(null);
			this.add(this.scrollPane, BorderLayout.CENTER);
			tree.addMouseListener(this.treeSelectListener);
			tree.addKeyListener(keyListenerTemp);
		}
	}

	@Override
	public void show() {
		this.updatePopup();
		try {
			this.show(treeComboBox, 0, treeComboBox.getHeight());
		} catch (IllegalComponentStateException e) {
			// richer:这里有可能会抛出一个异常，可以不用处理
		}
		this.treeComboBox.getTree().requestFocus();
	}

	@Override
	public void hide() {
		this.setVisible(false);
		this.treeComboBox.firePropertyChange("popupVisible", true, false);
	}

	protected JList list = new JList();

	@Override
	public JList getList() {
		return list;
	}

	@Override
	public MouseMotionListener getMouseMotionListener() {
		if (mouseMotionListenerTemp == null) {
			mouseMotionListenerTemp = new MouseMotionAdapter() {
			};
		}
		return mouseMotionListenerTemp;
	}

	@Override
	public KeyListener getKeyListener() {
		return null;
	}

	@Override
	public void uninstallingUI() {
		// 默认实现，后续进行初始化操作
	}

	/**
	 * Implementation of ComboPopup.getMouseListener().
	 * 
	 * @return a <code>MouseListener</code> or null
	 * @see ComboPopup#getMouseListener
	 */
	@Override
	public MouseListener getMouseListener() {
		if (mouseListenerTemp == null) {
			mouseListenerTemp = new InvocationMouseHandler();
		}
		return mouseListenerTemp;
	}

	protected void togglePopup() {
		if (this.isVisible()) {
			this.hide();
		} else {
			this.show();
		}
	}

	protected void updatePopup() {
		this.setPreferredSize(new Dimension(this.treeComboBox.getSize().width, 120));
		Object selectedObj = this.treeComboBox.getSelectedItem();
		if (selectedObj != null) {
			TreePath tp = (TreePath) selectedObj;
			((TreeComboBox) this.treeComboBox).getTree().setSelectionPath(tp);
		}
	}

	protected class InvocationMouseHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e) || !treeComboBox.isEnabled()) {
				return;
			}
			if (treeComboBox.isEditable()) {
				Component comp = treeComboBox.getEditor().getEditorComponent();
				if ((!(comp instanceof JComponent)) || ((JComponent) comp).isRequestFocusEnabled()) {
					comp.requestFocus();
				}
			} else if (treeComboBox.isRequestFocusEnabled()) {
				treeComboBox.requestFocus();
			}
			togglePopup();
		}
	}
}
