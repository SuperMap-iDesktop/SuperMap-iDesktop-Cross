package com.supermap.desktop.process.graphics.interaction;

import com.supermap.desktop.process.graphics.CoordinateTransform;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.handler.canvas.CanvasEventAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Scale and translate the canvas.
 * Created by highsad on 2017/3/8.
 */
public class CanvasTranslation extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private CoordinateTransform transform;
	private Point start = null;

	public CanvasTranslation(GraphCanvas canvas) {
		this.canvas = canvas;
		this.transform = canvas.getCoordinateTransform();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isMiddleMouseButton(e)) {
			start = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isMiddleMouseButton(e)) {
			start = null;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point scaleStart = e.getPoint();
		if (this.canvas.getCanvasViewBounds().contains(scaleStart)) {
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("dragged");
	}

	@Override
	public void clean() {

	}

	public boolean isTranslating() {
		return this.start != null;
	}
}
