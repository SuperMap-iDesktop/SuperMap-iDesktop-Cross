package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/5/2.
 */
public class LineArrowDecorator extends AbstractDecorator {

	public LineArrowDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	private LineGraph getDecoratedLine() {
		if (getGraph() instanceof LineGraph) {
			return (LineGraph) getGraph();
		} else {
			return null;
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
	public void undecorate() {
		super.undecorate();
	}

	@Override
	public void decorate(IGraph graph) {
		if (graph instanceof LineGraph) {
			super.decorate(graph);
		}
	}

	@Override
	protected void onPaint(Graphics g) {

	}
}
