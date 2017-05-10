package com.supermap.desktop.process.diagram.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.drop.DropAndDragHandler;
import com.supermap.desktop.controls.utilities.JTreeUIUtilities;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.core.WorkflowParser;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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

	public ProcessTree() {
		init();
	}

	public ProcessTree(ProcessTreeNode processTreeNode) {
		super(processTreeNode);
		init();
	}

	private void init() {
		initComponents();
		new TreeDropAndDragHandler(DataFlavor.stringFlavor).bindSource(this);
		JTreeUIUtilities.expandTree(this);
		this.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

				ProcessTreeNodeBean bean = (ProcessTreeNodeBean) ((DefaultMutableTreeNode) value).getUserObject();
				JLabel jLabel = new JLabel();
				jLabel.setOpaque(true);
				jLabel.setText(bean.getName());
				jLabel.setIcon(bean.getIcon());
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
					Object lastSelectedPathComponent = getLastSelectedPathComponent();
					if (lastSelectedPathComponent == null) {
						return;
					}
					ProcessTreeNodeBean userObject = (ProcessTreeNodeBean) ((DefaultMutableTreeNode) lastSelectedPathComponent).getUserObject();
					if (!StringUtilities.isNullOrEmpty(userObject.getKey()) && Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof FormProcess) {
						((FormProcess) Application.getActiveApplication().getActiveForm()).addProcess(WorkflowParser.getMetaProcess(userObject.getKey()));
					}
//					if (userObject instanceof IProcess && !(userObject instanceof IProcessGroup)) {
//						if (Application.getActiveApplication().getActiveForm() instanceof FormProcess) {
//							((FormProcess) Application.getActiveApplication().getActiveForm()).addProcess(WorkflowParser.getMetaProcess(((IProcess) userObject).getKey()));
////							((FormProcess) Application.getActiveApplication().getActiveForm()).addProcess((IProcess) userObject);
//						}
//					}
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
			if (tree.getLastSelectedPathComponent() == null) {
				return "";
			}
			return ((ProcessTreeNodeBean) ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject()).getKey();
		}
	}

	public void initComponents() {
		this.putClientProperty("JTree.lineStyle", "Horizontal");
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}
}
