package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.connection.GraphConnectionLine;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/4/5.
 */
public class ListGraphConnection implements IConnectionManager {
	private GraphCanvas canvas;
	private java.util.List<GraphConnectionLine> lines = new ArrayList<>();

	public ListGraphConnection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public GraphConnectionLine[] getLines() {
		return this.lines.toArray(new GraphConnectionLine[this.lines.size()]);
	}

	@Override
	public void connect(IGraph start, IGraph end) {
		connect(start, end, null);
	}

	@Override
	public void connect(IGraph start, IGraph end, String message) {
		if (isConnected(start, end)) {
			return;
		}

		if (start != null && end != null && start != end) {
			GraphConnectionLine line = new GraphConnectionLine(this.canvas, start, end, message);
			this.lines.add(line);
			line.repaint();
		}
	}

	@Override
	public void removeConnection(IGraph graph) {
		for (int i = this.lines.size() - 1; i >= 0; i--) {
			GraphConnectionLine line = this.lines.get(i);
			if (line.getStartGraph() == graph || line.getEndGraph() == graph) {
				this.lines.remove(i);
				line.clear();
			}
		}
	}

	@Override
	public void removeConnectLine(GraphConnectionLine line) {
		if (line == null) {
			return;
		}

		this.lines.remove(line);
		line.clear();
	}

	@Override
	public IGraph[] getPreGraphs(IGraph end) {
		if (end == null) {
			return null;
		}

		ArrayList<IGraph> ret = new ArrayList<>();
		for (int i = 0, size = this.lines.size(); i < size; i++) {
			GraphConnectionLine line = this.lines.get(i);
			if (line.getEndGraph() == end && line.getStartGraph() != null && !ret.contains(line.getStartGraph())) {
				ret.add(line.getStartGraph());
			}
		}
		return ret.toArray(new IGraph[ret.size()]);
	}

	@Override
	public IGraph[] getNextGraphs(IGraph start) {
		if (start == null) {
			return null;
		}

		ArrayList<IGraph> ret = new ArrayList<>();
		for (int i = 0, size = this.lines.size(); i < size; i++) {
			GraphConnectionLine line = this.lines.get(i);
			if (line.getStartGraph() == start && line.getEndGraph() != null && !ret.contains(line.getEndGraph())) {
				ret.add(line.getEndGraph());
			}
		}
		return ret.toArray(new IGraph[ret.size()]);
	}

	@Override
	public boolean isConnectedAsStart(IGraph start) {
		boolean ret = false;
		for (int i = 0, size = this.lines.size(); i < size; i++) {
			GraphConnectionLine line = this.lines.get(i);
			if (line.getStartGraph() == start && line.getEndGraph() != null) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean isConnectedAsEnd(IGraph end) {
		boolean ret = false;
		for (int i = 0, size = this.lines.size(); i < size; i++) {
			GraphConnectionLine line = this.lines.get(i);
			if (line.getEndGraph() == end && line.getStartGraph() != null) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean isConnected(IGraph graph1, IGraph graph2) {
		boolean ret = false;
		for (int i = 0, size = this.lines.size(); i < size; i++) {
			GraphConnectionLine line = this.lines.get(i);
			if ((line.getStartGraph() == graph1 && line.getEndGraph() == graph2)
					|| (line.getStartGraph() == graph2 && line.getEndGraph() == graph1)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public GraphConnectionLine find(Point point) {
		GraphConnectionLine result = null;
		Point inverse = this.canvas.getCoordinateTransform().inverse(point);

		for (int i = 0; i < this.lines.size(); i++) {
			GraphConnectionLine line = this.lines.get(i);
			Point start = line.getStartPoint();
			Point end = line.getEndPoint();
			Boolean isPointOnLine = GraphicsUtil.lineContainsPoint(start.x, start.y, end.x, end.y, inverse.x, inverse.y, 4);
			if (isPointOnLine) {
				result = line;
				break;
			}
		}
		return result;
	}
}
