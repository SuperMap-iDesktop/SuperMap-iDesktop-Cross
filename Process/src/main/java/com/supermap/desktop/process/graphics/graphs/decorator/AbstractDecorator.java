package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public abstract class AbstractDecorator extends AbstractGraph {

	private AbstractGraph graph;

	public AbstractDecorator(GraphCanvas canvas, Shape shape) {
		super(canvas, shape);
	}

	public AbstractGraph getGraph() {
		return graph;
	}

	public void decorate(AbstractGraph graph) {
			this.graph = graph;
	}

	public void undecorate() {
		if (this.graph != null) {
			this.graph = null;
		}
	}

	public boolean isDecorating() {
		return this.graph != null;
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

	@Override
	public IGraph clone() {
		throw new UnsupportedOperationException();
	}
}
