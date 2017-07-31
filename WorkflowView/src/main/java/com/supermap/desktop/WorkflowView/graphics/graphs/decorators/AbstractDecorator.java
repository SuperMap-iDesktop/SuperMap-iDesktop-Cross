package com.supermap.desktop.WorkflowView.graphics.graphs.decorators;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.GraphicsUtil;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public abstract class AbstractDecorator implements IDecorator {
	private final static int DEFAULT_PRIORITY = 100;
	private IGraph decoratedGraph;
	private GraphCanvas canvas;
	private int priority = DEFAULT_PRIORITY;

	public AbstractDecorator(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public IGraph getGraph() {
		return this.decoratedGraph;
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public void decorate(IGraph graph) {
		this.decoratedGraph = graph;
	}

	@Override
	public void undecorate() {
		this.decoratedGraph = null;
	}

	@Override
	public boolean contains(Point point) {
		boolean isContain = true;

		if (this.decoratedGraph == null) {
			isContain = false;
		} else {
			Rectangle rect = this.decoratedGraph.getBounds();

			if (GraphicsUtil.isRegionValid(rect)) {
				isContain = rect.contains(point);
			} else {
				isContain = true;
			}
		}
		return isContain;
	}

	@Override
	public Rectangle getBounds() {
		return this.decoratedGraph == null ? null : this.decoratedGraph.getBounds();
	}

	@Override
	public final void paint(Graphics g) {
		if (this.decoratedGraph != null) {
			onPaint(g);
		}
	}

	protected abstract void onPaint(Graphics g);
}
