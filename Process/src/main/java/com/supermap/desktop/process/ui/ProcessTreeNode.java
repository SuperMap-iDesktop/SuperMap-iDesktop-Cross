package com.supermap.desktop.process.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;

/**
 * @author XiaJT
 */
public class ProcessTreeNode extends DefaultMutableTreeNode {

	public ProcessTreeNode(ProcessTreeNode parent, ProcessTreeNodeBean processTreeNodeBean) {
		super(processTreeNodeBean);
		this.parent = parent;
		children = new Vector();
		if (processTreeNodeBean.getChildCount() > 0) {
			for (int i = 0; i < processTreeNodeBean.getChildCount(); i++) {
				children.add(new ProcessTreeNode(this, processTreeNodeBean.getChildAt(i)));
			}
		}
	}

	public String getIcon() {
		return getData().getIconPath();
	}

	public ProcessTreeNodeBean getData() {
		return (ProcessTreeNodeBean) userObject;
	}

}
