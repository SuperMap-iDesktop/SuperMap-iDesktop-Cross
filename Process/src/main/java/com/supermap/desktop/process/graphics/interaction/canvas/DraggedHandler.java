package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/3.
 */
public class DraggedHandler extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private IGraph dragged = null;
	private Point dragStart = null;
	private Rectangle dirty = null;

	public DraggedHandler(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void clean() {
		this.dragged = null;
		this.dragStart = null;
		this.dirty = null;
		this.canvas.setEventHandlerEnabled(Selection.class, true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {

			// 鼠标位置根据 bounds 查询到的 graph
			IGraph hitGraph = canvas.findGraph(e.getPoint());
			Point canvasP = canvas.getCoordinateTransform().inverse(e.getPoint());

			boolean isDragged = hitGraph instanceof AbstractGraph ? ((AbstractGraph) hitGraph).getShape().contains(canvasP) : hitGraph != null;
			if (isDragged) {
				this.dragged = hitGraph;
				this.dragStart = canvasP;
				this.dirty = new Rectangle(this.dragged.getBounds());
				this.canvas.setEventHandlerEnabled(Selection.class, false);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clean();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) && this.dragged != null) {
			Point dragEnd = canvas.getCoordinateTransform().inverse(e.getPoint());
			int desX = this.dragged.getLocation().x + dragEnd.x - this.dragStart.x;
			int desY = this.dragged.getLocation().y + dragEnd.y - this.dragStart.y;
			Rectangle desRect = new Rectangle(desX, desY, this.dragged.getWidth(), this.dragged.getHeight());
			if (this.canvas.getCanvasRect().contains(desRect)) {
				this.canvas.modifyGraphBounds(this.dragged, desX, desY, this.dragged.getWidth(), this.dragged.getHeight());

				Rectangle refreshRect;
				if (GraphicsUtil.isRegionValid(this.dirty)) {
					refreshRect = this.dirty.union(desRect);
				} else {
					refreshRect = desRect;
				}
				this.dirty = desRect;
				this.dragStart = dragEnd;
				refreshRect.grow(3, 3);
				refreshRect = this.canvas.getCoordinateTransform().transform(refreshRect);
				this.canvas.repaint(refreshRect);
			}
		}
	}
}
