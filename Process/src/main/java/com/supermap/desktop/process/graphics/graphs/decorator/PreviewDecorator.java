package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class PreviewDecorator extends AbstractDecorator {

	public PreviewDecorator(GraphCanvas canvas) {
		super(canvas, null);
	}

	@Override
	public Shape getShape() {
		return getGraph().getShape();
	}

	@Override
	public Rectangle getBounds() {
		return getGraph().getBounds();
	}

	@Override
	public Point getLocation() {
		return getGraph().getLocation();
	}

	@Override
	public Point getCenter() {
		return getGraph().getCenter();
	}

	@Override
	public int getWidth() {
		return getGraph().getWidth();
	}

	@Override
	public int getHeight() {
		return getGraph().getHeight();
	}

	@Override
	public void setLocation(Point point) {
		getGraph().setLocation(point);
	}

	@Override
	public void setSize(int width, int height) {
		getGraph().setSize(width, height);
	}

	@Override
	public boolean contains(Point point) {
		return getGraph().contains(point);
	}
}
