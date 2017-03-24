package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/3/23.
 */
public class RelationLine extends AbstractLine implements GraphBoundsChangedListener {
	private IGraph start;
	private IGraph end;

	public RelationLine(GraphCanvas canvas, IGraph start, IGraph end) {
		super(canvas);
		setStartGraph(start);
		setEndGraph(end);
	}

	@Override
	public Point getStartPoint() {
		return this.start != null ? this.start.getCenter() : null;
	}

	@Override
	public Point getEndPoint() {
		return this.end != null ? this.end.getCenter() : null;
	}

	public IGraph getStartGraph() {
		return this.start;
	}

	public void setStartGraph(IGraph start) {
		if (this.start != null) {
			this.start.removeGraghBoundsChangedListener(this);
		}

		this.start = start;

		if (this.start != null) {
			setStartPoint(this.start.getCenter());
			this.start.addGraphBoundsChangedListener(this);
		} else {
			setStartPoint(null);
		}
	}

	public IGraph getEndGraph() {
		return this.end;
	}

	public void setEndGraph(IGraph end) {
		if (this.end != null) {
			this.end.removeGraghBoundsChangedListener(this);
		}

		this.end = end;

		if (this.end != null) {
			setEndPoint(getEndLocation());
			this.end.addGraphBoundsChangedListener(this);
		} else {
			setStartPoint(null);
		}
	}

	@Override
	public void graghBoundsChanged(GraphBoundsChangedEvent e) {
		if (e.getGraph() == this.start) {
			setStartPoint(this.start.getCenter());
			setEndPoint(getEndLocation());
		} else if (e.getGraph() == this.end) {
			setEndPoint(getEndLocation());
		}
	}

	private Point getEndLocation() {
		if (this.start != null && this.end != null) {
			return GraphicsUtil.chop(((AbstractGraph) this.end).getShape(), this.start.getCenter());
		} else {
			return null;
		}
	}
}
