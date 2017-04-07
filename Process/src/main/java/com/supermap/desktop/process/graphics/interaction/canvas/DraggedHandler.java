package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.CanvasCursor;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/3.
 */
public class DraggedHandler extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private IGraph[] draggedGraphs = null;
	private Point dragStart = null;
	private Rectangle[] dirtys = null;

	public DraggedHandler(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void clean() {
		this.draggedGraphs = null;
		this.dragStart = null;
		this.dirtys = null;
		this.canvas.setEventHandlerEnabled(Selection.class, true);
		CanvasCursor.resetCursor(this.canvas);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {

			// 鼠标位置根据 bounds 查询到的 graph
			IGraph hitGraph = canvas.findGraph(e.getPoint());

			if (hitGraph != null) {
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
		if (SwingUtilities.isLeftMouseButton(e) && this.draggedGraphs != null && this.draggedGraphs.length > 0) {
			CanvasCursor.setItemDraggedCursor(this.canvas);
			Point dragEnd = canvas.getCoordinateTransform().inverse(e.getPoint());
			int offsetX = dragEnd.x - this.dragStart.x;
			int offsetY = dragEnd.y - this.dragStart.y;

			for (int i = 0, size = this.draggedGraphs.length; i < size; i++) {
				IGraph dragged = this.draggedGraphs[i];
				int desX = dragged.getLocation().x + offsetX;
				int desY = dragged.getLocation().y + offsetY;
				Rectangle desRect = new Rectangle(desX, desY, dragged.getWidth(), dragged.getHeight());
				if (this.canvas.getCanvasRect().contains(desRect)) {
					this.canvas.modifyGraphBounds(dragged, desX, desY, dragged.getWidth(), dragged.getHeight());

					Rectangle refreshRect;
					if (GraphicsUtil.isRegionValid(this.dirtys[i])) {
						refreshRect = this.dirtys[i].union(desRect);
					} else {
						refreshRect = desRect;
					}
					this.dirtys[i] = desRect;
					this.dragStart = dragEnd;
					refreshRect.grow(3, 3);
					refreshRect = this.canvas.getCoordinateTransform().transform(refreshRect);
					this.canvas.repaint(refreshRect);
				}
			}
		}
	}
}
