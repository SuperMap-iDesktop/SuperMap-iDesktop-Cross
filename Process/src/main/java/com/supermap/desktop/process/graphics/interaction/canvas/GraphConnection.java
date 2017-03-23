package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.Line;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/22.
 */
public class GraphConnection extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private Line line;

	public GraphConnection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public void connecting() {
		this.line = new Line(this.canvas);
		this.canvas.setEventHandlerEnabled(Selection.class, false);
		this.canvas.setEventHandlerEnabled(DraggedHandler.class, false);
		this.canvas.setEventHandlerEnabled(GraphCreator.class, false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			Point canvasPoint = this.canvas.getCoordinateTransform().inverse(e.getPoint());
			IGraph hit = this.canvas.findGraph(e.getPoint());

//			if (hit instanceof DataGraph) {
			boolean ready = hit instanceof AbstractGraph ? ((AbstractGraph) hit).contains(canvasPoint) : hit != null;
			if (ready) {
				this.line.setOrigin(hit);
				this.canvas.addConnection(this.line);
			}
//			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			this.canvas.removeConnection(this.line);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			clean();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {

		}
	}

	@Override
	public void clean() {
		try {
			this.line.setOrigin(null);
			this.line.setDestination(null);
			this.line = null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.canvas.setEventHandlerEnabled(Selection.class, true);
			this.canvas.setEventHandlerEnabled(DraggedHandler.class, true);
			this.canvas.setEventHandlerEnabled(GraphCreator.class, true);
		}
	}
}
