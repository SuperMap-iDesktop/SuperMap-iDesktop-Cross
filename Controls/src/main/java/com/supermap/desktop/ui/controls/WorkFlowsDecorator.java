package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class WorkFlowsDecorator implements TreeNodeDecorator {
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.WORK_FLOWS)) {
			label.setText(ControlsProperties.getString("String_WorkFlows"));
			// TODO: 2017/3/24 工作流图片
		}
	}
}
