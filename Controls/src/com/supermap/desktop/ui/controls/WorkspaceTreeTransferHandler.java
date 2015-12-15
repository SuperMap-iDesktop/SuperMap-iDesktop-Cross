package com.supermap.desktop.ui.controls;

/*
 * ArrayListTransferHandler.java is used by the 1.4
 * DragListDemo.java example.
 */

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;

public class WorkspaceTreeTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTree source = null;

	public WorkspaceTreeTransferHandler() {
		// 默认实现，后续进行初始化操作
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		if (c instanceof WorkspaceTree) {
			source = (WorkspaceTree) c;
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) source.getLastSelectedPathComponent();
			TreeNodeData data = (TreeNodeData) treeNode.getUserObject();
			return new TransferableTreeNode(data);
		}
		return null;
	}

	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	class TransferableTreeNode implements Transferable {
		private DataFlavor TREE_NODE_FLAVOR = new DataFlavor(TreeNodeData.class, "TreeNodeData");

		DataFlavor flavors[] = { TREE_NODE_FLAVOR };

		TreeNodeData data;

		public TransferableTreeNode(TreeNodeData dt) {
			data = dt;
		}

		@Override
		public synchronized DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.getRepresentationClass() == TreeNodeData.class;
		}

		@Override
		public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor)) {
				return (Object) data;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}
	}
}
