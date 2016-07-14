package com.supermap.desktop.ui.controls;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import com.supermap.desktop.controls.utilities.JTreeUIUtilities;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.synth.SynthComboBoxUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

/**
 * 树下拉列表
 *
 * @author xuzw
 */
public class TreeComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;
	/**
	 * 显示用的树
	 */
	private JTree tree;

	private int popupMenuPreferredHeight = 120;

	public TreeComboBox(JTree tree) {
		this.setTree(tree);
		this.setFocusable(false);
	}

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
	 * @param isNotShowRoot
	 */
	public TreeComboBox(JTree tree, TreeCellRenderer treeCellRenderer, boolean isNotShowRoot) {
		this.setTree(tree);
		tree.setCellRenderer(treeCellRenderer);
		if (!isNotShowRoot) {
			tree.expandPath(new TreePath(tree.getModel().getRoot()));
			tree.setRootVisible(true);
		}
		this.setFocusable(false);
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
	public Object getSelectedItem() {
		TreePath treePath = (TreePath) super.getSelectedItem();
		if (treePath == null) {
			return null;
		}
		return treePath.getLastPathComponent();
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
		} else if (comboBoxUi instanceof SynthComboBoxUI) {
			comboBoxUi = new SynthTreeComboBoxUI();
		}
		setUI(comboBoxUi);
	}

	public int getPopupMenuPreferredHeight() {
		return popupMenuPreferredHeight;
	}

	public void setPopupMenuPreferredHeight(int popupMenuPreferredHeight) {
		this.popupMenuPreferredHeight = popupMenuPreferredHeight;
	}

// richer:皮肤

	class MetalTreeComboBoxUI extends MetalComboBoxUI {
		@Override
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	class SynthTreeComboBoxUI extends MetalComboBoxUI {
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
			if (value != null && value instanceof TreeNode) {
				Component treeCellRendererComponent = tree.getCellRenderer().getTreeCellRendererComponent(tree, value, isSelected, false, ((TreeNode) value).isLeaf(), index, cellHasFocus);
				if (treeCellRendererComponent instanceof JLabel) {
					((JLabel) treeCellRendererComponent).setHorizontalAlignment(JLabel.CENTER);
					((JLabel) treeCellRendererComponent).setIcon(getDisabledIcon());
				}
				return treeCellRendererComponent;
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

	private transient MyAWTEventListener myAWTEventListener = new MyAWTEventListener();

	private void abstractTreeSelectListener(MouseEvent e) {
		JTree tree = (JTree) e.getSource();
		TreePath treePath = tree.getPathForLocation(e.getPoint().x, e.getPoint().y);
		if (treePath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

		if (node.isLeaf() && e.getButton() == 1) {
			togglePopup();
			treeComboBox.setSelectedItem(treePath);
			MenuSelectionManager.defaultManager().clearSelectedPath();
		}

		if (!node.isLeaf()) {
			togglePopup();
			treeComboBox.setSelectedItem(treePath);
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
		Toolkit.getDefaultToolkit().addAWTEventListener(myAWTEventListener, AWTEvent.MOUSE_EVENT_MASK | sun.awt.SunToolkit.GRAB_EVENT_MASK);
		try {
			this.show(treeComboBox, 0, treeComboBox.getHeight());
		} catch (IllegalComponentStateException e) {
			e.printStackTrace();
			// richer:这里有可能会抛出一个异常，可以不用处理
		}
		this.treeComboBox.getTree().requestFocus();
	}

	@Override
	public void hide() {
		Toolkit.getDefaultToolkit().removeAWTEventListener(myAWTEventListener);
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
		this.setPreferredSize(new Dimension(this.treeComboBox.getSize().width, treeComboBox.getPopupMenuPreferredHeight()));
		Object selectedObj = this.treeComboBox.getSelectedItem();
		if (selectedObj != null) {

			TreePath tp = JTreeUIUtilities.getPath((TreeNode) selectedObj);
			this.treeComboBox.getTree().setSelectionPath(tp);
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

	private class MyAWTEventListener implements AWTEventListener {

		@Override
		public void eventDispatched(AWTEvent event) {
			if (event instanceof sun.awt.UngrabEvent) {
				cancelPopupMenu();
				return;
			}
			if (!(event instanceof MouseEvent)) {
				// We are interested in MouseEvents only
				return;
			}
			MouseEvent me = (MouseEvent) event;
			Component src = me.getComponent();
			switch (me.getID()) {
				case MouseEvent.MOUSE_PRESSED:
					if (isInPopup(src) ||
							(src instanceof JMenu && ((JMenu) src).isSelected())) {
						return;
					}
					// Cancel popup only if this property was not set.
					// If this property is set to TRUE component wants
					// to deal with this event by himself.
					cancelPopupMenu();
					if (src != TreePopup.this.treeComboBox && src != TreePopup.this.treeComboBox.getComponent(0)) {
						// 触发自己就又弹出来了！
						src.dispatchEvent(event);
					}
					// Ask UIManager about should we consume event that closes
					// popup. This made to match native apps behaviour.
					boolean consumeEvent =
							UIManager.getBoolean("PopupMenu.consumeEventOnClose");
					// Consume the event so that normal processing stops.
					if (consumeEvent && !(src instanceof MenuElement)) {
						me.consume();
					}
					break;

				case MouseEvent.MOUSE_RELEASED:
					if (!(src instanceof MenuElement)) {
						// Do not forward event to MSM, let component handle it
						if (isInPopup(src)) {
							break;
						}
					}
					if (src instanceof JMenu || !(src instanceof JMenuItem)) {
						MenuSelectionManager.defaultManager().
								processMouseEvent(me);
					}
					break;
				case MouseEvent.MOUSE_DRAGGED:
					if (!(src instanceof MenuElement)) {
						// For the MOUSE_DRAGGED event the src is
						// the Component in which mouse button was pressed.
						// If the src is in popupMenu,
						// do not forward event to MSM, let component handle it.
						if (isInPopup(src)) {
							break;
						}
					}
					MenuSelectionManager.defaultManager().
							processMouseEvent(me);
					break;
				case MouseEvent.MOUSE_WHEEL:
					if (isInPopup(src)) {
						return;
					}
					cancelPopupMenu();
					break;
			}
		}


		private void cancelPopupMenu() {
			TreePopup.this.hide();
		}

		private boolean isInPopup(Component src) {
			return getParentPopupMenu(src) == TreePopup.this;
		}

		private JPopupMenu getParentPopupMenu(Component src) {
			for (Component c = src; c != null; c = c.getParent()) {
				if (c instanceof Applet || c instanceof Window) {
					break;
				} else if (c instanceof JPopupMenu) {
					return ((JPopupMenu) c);
				}
			}
			return null;
		}
	}
}
