package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.CanvasCursor;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/3.
 */
public class GraphDragAction extends CanvasActionAdapter {
	private GraphCanvas canvas;
	private IGraph[] draggedGraphs = null;
	private Point dragStart = null;
	private Rectangle[] dirtys = null;

	public GraphDragAction(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void clean() {
		this.draggedGraphs = null;
		this.dragStart = null;
		this.dirtys = null;
		fireCanvasActionStop();
		CanvasCursor.resetCursor(this.canvas);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {

			// 鼠标位置根据 bounds 查询到的 graph
			IGraph hitGraph = canvas.findGraph(e.getPoint());

			if (hitGraph != null && !(hitGraph instanceof LineGraph)) {
				this.dragStart = canvas.getCoordinateTransform().inverse(e.getPoint());

				if (this.canvas.getSelection().isSelected(hitGraph)) {
					this.draggedGraphs = this.canvas.getSelection().getSelectedItems();
				} else {
					this.draggedGraphs = new IGraph[]{hitGraph};
				}

				this.dirtys = new Rectangle[this.draggedGraphs.length];
				for (int i = 0, size = this.draggedGraphs.length; i < size; i++) {
					IGraph dragged = this.draggedGraphs[i];
					this.dirtys[i] = new Rectangle(dragged.getBounds());
				}

				fireCanvasActionStart();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clean();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) && this.draggedGraphs != null && this.draggedGraphs.length > 0) {
			CanvasCursor.setItemDraggedCursor(this.canvas);
			Point dragEnd = canvas.getCoordinateTransform().inverse(e.getPoint());
			int offsetX = dragEnd.x - this.dragStart.x;
			int offsetY = dragEnd.y - this.dragStart.y;

			for (int i = 0, size = this.draggedGraphs.length; i < size; i++) {
				IGraph dragged = this.draggedGraphs[i];

				if (!isGraphDraggable(dragged)) {
					continue;
				}

				int originX = dragged.getLocation().x;
				int originY = dragged.getLocation().y;
				dragged.setLocation(new Point(originX + offsetX, originY + offsetY));

				Rectangle desRect = dragged.getTotalBounds();
				if (this.canvas.getCanvasRect().contains(desRect)) {
					Rectangle refreshRect;

					if (GraphicsUtil.isRegionValid(this.dirtys[i])) {
						refreshRect = this.dirtys[i].union(desRect);
					} else {
						refreshRect = desRect;
					}
					this.dirtys[i] = desRect;
					this.dragStart = dragEnd;
					refreshRect = this.canvas.getCoordinateTransform().transform(refreshRect);
					refreshRect.grow(2, 2);
//					this.canvas.repaint(refreshRect);
					this.canvas.repaint();
				} else {
					dragged.setLocation(new Point(originX, originY));
				}
			}
		}
	}

	private boolean isGraphDraggable(IGraph graph) {
		return !(graph instanceof LineGraph);
	}
}
