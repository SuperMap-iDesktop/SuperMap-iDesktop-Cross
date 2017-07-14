package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.events.GraphRemovedEvent;
import com.supermap.desktop.process.graphics.events.GraphRemovedListener;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.decorators.LineMessageDecorator;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class ConnectionLineGraph extends LineGraph {
	private final static String DECORATOR_KEY_LINE_MESSAGE = "DecoratorLineMessageKey";
	private IGraph from;
	private IGraph to;
	private LineMessageDecorator messageDecorator;
	private boolean isSelected = true;


	private GraphBoundsChangedListener startGraphBoundsChangedListener = new GraphBoundsChangedListener() {
		@Override
		public void graghBoundsChanged(GraphBoundsChangedEvent e) {
			startGraphBoundsChanged(e);
		}
	};

	private GraphBoundsChangedListener endGraphBoundsChangedListener = new GraphBoundsChangedListener() {
		@Override
		public void graghBoundsChanged(GraphBoundsChangedEvent e) {
			endGraphBoundsChanged(e);
		}
	};

	private GraphRemovedListener graphRemovedListener = new GraphRemovedListener() {
		@Override
		public void graphRemoved(GraphRemovedEvent e) {
			connectedGraphRemoved(e);
		}
	};

	public ConnectionLineGraph(GraphCanvas canvas, IGraph from, IGraph to) {
		this(canvas, from, to, null);
	}


	public ConnectionLineGraph(GraphCanvas canvas, IGraph from, IGraph to, String message) {
		super(canvas);
		this.from = from;
		this.to = to;

		this.messageDecorator = new LineMessageDecorator(getCanvas(), message);
		addDecorator(DECORATOR_KEY_LINE_MESSAGE, this.messageDecorator);

		if (this.from != null && this.to != null) {
			computeFirstAndLastPoints();
			this.from.addGraphBoundsChangedListener(this.startGraphBoundsChangedListener);
			this.to.addGraphBoundsChangedListener(this.endGraphBoundsChangedListener);
		}

		if (canvas != null) {
			canvas.addGraphRemovedListener(this.graphRemovedListener);
		}
	}

	public IGraph getFrom() {
		return this.from;
	}

	public IGraph getTo() {
		return this.to;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	private void startGraphBoundsChanged(GraphBoundsChangedEvent e) {
		computeFirstAndLastPoints();
	}

	private void endGraphBoundsChanged(GraphBoundsChangedEvent e) {
		computeFirstAndLastPoints();
	}

	private void connectedGraphRemoved(GraphRemovedEvent e) {
		if (e.getGraph() == this.from || e.getGraph() == this.to) {
			getCanvas().removeGraph(this);
		}

		// 如果删除了自己，就清理资源
		if (e.getGraph() == this) {
			this.from.removeGraghBoundsChangedListener(this.startGraphBoundsChangedListener);
			this.to.removeGraghBoundsChangedListener(this.endGraphBoundsChangedListener);
			getCanvas().removeGraphRemovedListener(this.graphRemovedListener);

			this.from = null;
			this.to = null;
		}
	}

	private void computeFirstAndLastPoints() {
		if (this.from != null && this.to != null) {
			Point p = getPointCount() > 2 ? getPoint(1) : this.to.getCenter();
			Point firstPoint = GraphicsUtil.chop(((AbstractGraph) this.from).getShape(), p);
			setFirstPoint(firstPoint);

			p = getPointCount() > 2 ? getPoint(getPointCount() - 1) : this.from.getCenter();
			Point lastPoint = GraphicsUtil.chop(((AbstractGraph) this.to).getShape(), p);
			setLastPoint(lastPoint);
		}
	}
}
