package com.supermap.desktop.process.diagram.ui;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.ProcessGroup;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;

/**
 * @author XiaJT
 */
public class ProcessTreeNode extends DefaultMutableTreeNode {

	private IProcess process;

	public ProcessTreeNode(ProcessTreeNode parent, IProcess process) {
		super(process);
		this.process = process;
		this.parent = parent;
		if (process != null && process instanceof ProcessGroup) {
			children = new Vector();
			for (int i = 0; i < ((ProcessGroup) process).getChildCount(); i++) {
				children.add(new ProcessTreeNode(this, ((ProcessGroup) process).getProcessByIndex(i)));
			}
		}
	}

//	@Override
//	public int getChildCount() {
//		if (process == null) {
//			return 0;
//		}
//		if (process instanceof ProcessGroup) {
//			return ((ProcessGroup) process).getChildCount();
//		}
//		return 0;
//	}
//
//	@Override
//	public boolean isLeaf() {
//		return process == null || !(process instanceof IProcessGroup);
//	}

	public Icon getIcon() {
		return process.getIcon();
	}

}
