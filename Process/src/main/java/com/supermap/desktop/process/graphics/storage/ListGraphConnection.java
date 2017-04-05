package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.GraphRelationLine;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.util.ArrayList;

/**
 * Created by highsad on 2017/4/5.
 */
public class ListGraphConnection implements IGraphConnection {
	private GraphCanvas canvas;
	private java.util.List<GraphRelationLine> lines = new ArrayList<>();

	public ListGraphConnection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public GraphRelationLine[] getLines() {
		return this.lines.toArray(new GraphRelationLine[this.lines.size()]);
	}

	@Override
	public void connect(IGraph start, IGraph end) {
		if (isConnected(start, end)) {
			return;
		}

		if (start != null && end != null && start != end) {
			GraphRelationLine line = new GraphRelationLine(this.canvas, start, end);
			this.lines.add(line);
			line.repaint();
		}
	}

	@Override
	public void removeConnection(IGraph graph) {
		for (int i = this.lines.size() - 1; i >= 0; i--) {
			GraphRelationLine line = this.lines.get(i);
			if (line.getStartGraph() == graph || line.getEndGraph() == graph) {
				this.lines.remove(i);
			}
		}
	}

	@Override
	public IGraph[] getPreGraphs(IGraph end) {
		if (end == null) {
			return null;
		}

		ArrayList<IGraph> ret = new ArrayList<>();
		for (int i = 0, size = this.lines.size(); i < size; i++) {
			GraphRelationLine line = this.lines.get(i);
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
			GraphRelationLine line = this.lines.get(i);
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
			GraphRelationLine line = this.lines.get(i);
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
			GraphRelationLine line = this.lines.get(i);
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
			GraphRelationLine line = this.lines.get(i);
			if ((line.getStartGraph() == graph1 && line.getEndGraph() == graph2)
					|| (line.getStartGraph() == graph2 && line.getEndGraph() == graph1)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
