package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/5/9.
 */
public class LineSelectedDecorator extends AbstractDecorator {

	public LineSelectedDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public void decorate(IGraph graph) {
		if (graph instanceof LineGraph) {
			super.decorate(graph);
		}
	}

	@Override
	public boolean contains(Point point) {
		return super.contains(point);
	}

	@Override
	public Rectangle getBounds() {
		return super.getBounds();
	}

	@Override
	protected void onPaint(Graphics g) {

	}

	private LineGraph getDecoratedLine() {
		if (getGraph() instanceof LineGraph) {
			return (LineGraph) getGraph();
		} else {
			return null;
		}
	}
}
