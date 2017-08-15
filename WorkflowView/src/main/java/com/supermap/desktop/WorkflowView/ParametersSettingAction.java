package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.ProcessGraph;
import com.supermap.desktop.WorkflowView.graphics.interaction.canvas.CanvasActionAdapter;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/8/15.
 */
public class ParametersSettingAction extends CanvasActionAdapter {
	private static final String PARAMETER_MANAGER_CLASS_NAME = "com.supermap.desktop.WorkflowView.ParameterManager";
	private WorkflowCanvas canvas;

	public ParametersSettingAction(WorkflowCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {

			// 双击流程图节点，进行参数设置
			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
				IDockbar parametersDockbar = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(PARAMETER_MANAGER_CLASS_NAME));
				if (parametersDockbar.isVisible()) {
					return;
				}

				IGraph graph = this.canvas.findGraph(e.getPoint());

				if (graph instanceof ProcessGraph) {
					parametersDockbar.setVisible(true);
				}
			}
		} catch (Exception e1) {
			Application.getActiveApplication().getOutput().output(e1);
		}
	}

	@Override
	public void clean() {

	}
}
