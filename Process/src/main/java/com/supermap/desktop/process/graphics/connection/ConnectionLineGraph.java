package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.decorators.LineMessageDecorator;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class ConnectionLineGraph extends LineGraph {
	private final static String DECORATOR_KEY_LINE_MESSAGE = "DecoratorLineMessageKey";
	private IGraphConnection connection;
	private LineMessageDecorator messageDecorator;
	private boolean isEditable = true;
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

	public ConnectionLineGraph(GraphCanvas canvas, IGraphConnection connection) {
		super(canvas);
		this.connection = connection;
		IGraph startGraph = this.connection.getStartGraph();
		IGraph endGraph = this.connection.getEndGraph();

		this.messageDecorator = new LineMessageDecorator(getCanvas(), this.connection.getMessage());
		addDecorator(DECORATOR_KEY_LINE_MESSAGE, this.messageDecorator);

		if (startGraph != null && endGraph != null) {
			computeFirstAndLastPoints();
			startGraph.addGraphBoundsChangedListener(this.startGraphBoundsChangedListener);
			endGraph.addGraphBoundsChangedListener(this.endGraphBoundsChangedListener);
		}
	}

	public IGraphConnection getConnection() {
		return this.connection;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public void startGraphBoundsChanged(GraphBoundsChangedEvent e) {
		computeFirstAndLastPoints();
	}

	public void endGraphBoundsChanged(GraphBoundsChangedEvent e) {
		computeFirstAndLastPoints();
	}

	private void computeFirstAndLastPoints() {
		IGraph start = this.connection.getStartGraph();
		IGraph end = this.connection.getEndGraph();

		if (start != null && end != null) {
			Point p = getPointCount() > 2 ? getPoint(1) : end.getCenter();
			Point firstPoint = GraphicsUtil.chop(((AbstractGraph) start).getShape(), p);
			setFirstPoint(firstPoint);

			p = getPointCount() > 2 ? getPoint(getPointCount() - 1) : start.getCenter();
			Point lastPoint = GraphicsUtil.chop(((AbstractGraph) end).getShape(), p);
			setLastPoint(lastPoint);
		}
	}
}
