package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Interface.IWorkFlow;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class WorkFlowDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.WORK_FLOW)) {
			label.setText(((IWorkFlow) data.getData()).getName());
		}
	}
}
