package com.supermap.desktop.WorkflowView.ui;

import com.supermap.desktop.controls.drop.DropAndDragHandler;
import com.supermap.desktop.process.ProcessManager;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.IProcessGroup;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.ui.SmDialogProcess;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragGestureEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by xie on 2017/2/23.
 */
public class ProcessTree extends JTree {
	private static final Icon ICON_GROUP = ProcessResources.getIcon("/processresources/Image_ProcessGroup.png");
	private static final Icon ICON_PROCESS = ProcessResources.getIcon("/processresources/Image_Process.png");

	public ProcessTree(TreeNode root) {
		super(root);
		init();
	}

	private Icon icon(Object userObject) {
		if (userObject instanceof ProcessManager) {
			return ICON_GROUP;
		} else if (userObject instanceof IProcessGroup) {
			return ICON_GROUP;
		} else if (userObject instanceof IProcessLoader) {
			return ICON_PROCESS;
		}
		return null;
	}

	private String getTitle(Object userObject) {
		if (userObject instanceof IProcessGroup) {
			return ((IProcessGroup) userObject).getTitle();
		} else if (userObject instanceof IProcessLoader) {
			return ((IProcessLoader) userObject).getTitle();
		}
		return "Unknown";
	}

	private void init() {
		initComponents();
		new TreeDropAndDragHandler(DataFlavor.stringFlavor).bindSource(this);
		//modify by xie 按要求不展开节点
//		JTreeUIUtilities.expandTree(this);
		this.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

				Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
//				ProcessTreeNodeBean bean = (ProcessTreeNodeBean) ((DefaultMutableTreeNode) value).getUserObject();
				JLabel jLabel = new JLabel();
				jLabel.setOpaque(true);
				jLabel.setText(getTitle(userObject));
				jLabel.setIcon(icon(userObject));
				if (selected) {
					jLabel.setForeground(Color.WHITE);
					jLabel.setBackground(new Color(150, 185, 255));
				} else {
					jLabel.setBackground(Color.WHITE);
				}

				return jLabel;
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// 原来是双击添加到地图中，暂时改为双击后弹出窗口，方案决定后再修改
					Object lastSelectedPathComponent = getLastSelectedPathComponent();
					if (lastSelectedPathComponent == null) {
						return;
					}
					Object userObject = ((DefaultMutableTreeNode) lastSelectedPathComponent).getUserObject();
					if (userObject instanceof IProcessLoader) {
						IProcess process = ((IProcessLoader) userObject).loadProcess();
						if (process != null) {
							SmDialogProcess smDialogProcess = new SmDialogProcess(process);
							smDialogProcess.showDialog();
						}
					}
				}
			}
		});
	}

	class TreeDropAndDragHandler extends DropAndDragHandler {
		public TreeDropAndDragHandler(DataFlavor... flavor) {
			super(flavor);
		}

		@Override
		public Object getTransferData(DragGestureEvent dge) {
			JTree tree = (JTree) dge.getComponent();
			Object userObject = ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject();

			if (userObject instanceof IProcessLoader) {
				return ((IProcessLoader) userObject).getKey();
			} else {
				return "";
			}
		}
	}

	public void initComponents() {
		this.putClientProperty("JTree.lineStyle", "Horizontal");
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}
}
