package com.supermap.desktop.WorkflowView.graphics.interaction.canvas;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * 用于向 Graph 转发事件
 * Created by highsad on 2017/3/3.
 */
public class GraphEventDispatcher extends CanvasActionAdapter {
	private GraphCanvas canvas;

	public GraphEventDispatcher(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		super.mouseExited(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		IGraph graph = this.canvas.findGraph(e.getPoint());
		if (graph!=null) {

		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
	}

	@Override
	public void clean() {

	}
}
