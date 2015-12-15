package com.supermap.samplecode.control;

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

import com.supermap.desktop.ui.controls.*;

public class WorkspaceTreeTransferHandler extends TransferHandler {
	JTree source = null;

	public WorkspaceTreeTransferHandler() {
	}

	protected Transferable createTransferable(JComponent c) {
		if (c instanceof WorkspaceTree) {
			source = (WorkspaceTree) c;
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)source.getLastSelectedPathComponent();
			TreeNodeData data = (TreeNodeData)treeNode.getUserObject();
			return new TransferableTreeNode(data);
		}
		return null;
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	class TransferableTreeNode implements Transferable {
		public DataFlavor TREE_NODE_FLAVOR = new DataFlavor(TreeNodeData.class,
				"TreeNodeData");

		DataFlavor flavors[] = { TREE_NODE_FLAVOR };

		TreeNodeData data;

		public TransferableTreeNode(TreeNodeData dt) {
			data = dt;
		}

		public synchronized DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return (flavor.getRepresentationClass() == TreeNodeData.class);
		}

		public synchronized Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor)) {
				return (Object) data;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}
	}
}
