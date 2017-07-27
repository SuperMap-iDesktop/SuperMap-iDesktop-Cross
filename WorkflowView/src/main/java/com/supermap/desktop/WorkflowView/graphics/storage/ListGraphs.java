package com.supermap.desktop.WorkflowView.graphics.storage;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.WorkflowView.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.WorkflowView.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.WorkflowView.graphics.events.GraphCreatingEvent;
import com.supermap.desktop.WorkflowView.graphics.events.GraphRemovedEvent;
import com.supermap.desktop.WorkflowView.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by highsad on 2017/3/2.
 */
public class ListGraphs extends AbstractGraphStorage {
	private GraphCanvas canvas;
	private Vector<IGraph> graphs = new Vector();
	private Rectangle box = null;

	private Vector<IGraph> removing = new Vector<>();

	private GraphBoundsChangedListener graphBoundsChangedListener = new GraphBoundsChangedListener() {
		@Override
		public void graghBoundsChanged(GraphBoundsChangedEvent e) {
			ListGraphs.this.graphBoundsChanged(e);
		}
	};

	public ListGraphs(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public int getCount() {
		return this.graphs.size();
	}

	@Override
	public IGraph[] getGraphs() {
		return this.graphs.toArray(new IGraph[this.graphs.size()]);
	}

	@Override
	public IGraph getGraph(int index) {
		return this.graphs.get(index);
	}

	@Override
	public Rectangle getBounds(IGraph graph) {
		return null;
	}

	@Override
	public boolean contains(IGraph graph) {
		return this.graphs.contains(graph);
	}

	@Override
	public void add(IGraph graph) {
		add(graph, graph.getBounds());
	}

	@Override
	public void add(IGraph graph, Rectangle bounds) {
		GraphCreatingEvent creatingEvent = new GraphCreatingEvent(this.canvas, graph);
		fireGraphCreating(creatingEvent);

		if (creatingEvent.isCancel()) {
			return;
		}

		this.graphs.add(graph);
		if (this.box == null) {
			this.box = bounds;
		} else {
			this.box = this.box.union(bounds);
		}
		fireGraphCreated(new GraphCreatedEvent(this.canvas, graph));
	}

	@Override
	public void remove(IGraph graph) {
		if (graph == null) {
			return;
		}

		if (!this.graphs.contains(graph)) {
			return;
		}

		// 正在执行删除的 graph，不做多次删除
		if (this.removing.contains(graph)) {
			return;
		}

		try {
			this.removing.add(graph);
			GraphRemovingEvent removingEvent = new GraphRemovingEvent(this.canvas, graph);
			fireGraphRemoving(removingEvent);

			if (removingEvent.isCancel()) {
				return;
			}

			int index = this.graphs.indexOf(graph);
			if (index > -1) {
				this.graphs.remove(graph);
				computeBox();
			}
			fireGraphRemoved(new GraphRemovedEvent(this.canvas, graph));
		} finally {
			this.removing.remove(graph);
		}
	}

	@Override
	public IGraph findGraph(Point point) {
		IGraph graph = null;
		for (int i = 0; i < this.graphs.size(); i++) {
			IGraph item = this.graphs.get(i);
			if (item.contains(point)) {
				graph = item;
				break;
			}
		}
		return graph;
	}

	@Override
	public IGraph[] findGraphs(Point point) {
		ArrayList<IGraph> re = new ArrayList<>();
		for (int i = 0; i < this.graphs.size(); i++) {
			IGraph item = this.graphs.get(i);
			if (item.contains(point)) {
				re.add(item);
			}
		}
		return re.toArray(new IGraph[re.size()]);
	}

	@Override
	public IGraph[] findIntersetctedGraphs(int x, int y, int width, int height) {
		return findIntersetctedGraphs(new Rectangle(x, y, width, height));
	}

	@Override
	public IGraph[] findIntersetctedGraphs(Rectangle rect) {
		ArrayList<IGraph> re = new ArrayList<>();
		for (int i = 0; i < this.graphs.size(); i++) {
			IGraph item = this.graphs.get(i);
			if (rect.intersects(item.getBounds())) {
				re.add(this.graphs.get(i));
			}
		}
		return re.toArray(new IGraph[re.size()]);
	}

	@Override
	public IGraph[] findContainedGraphs(int x, int y, int width, int height) {
		return findContainedGraphs(new Rectangle(x, y, width, height));
	}

	@Override
	public IGraph[] findContainedGraphs(Rectangle rect) {
		ArrayList<IGraph> re = new ArrayList<>();
		for (int i = 0; i < this.graphs.size(); i++) {
			IGraph item = this.graphs.get(i);
			if (rect.contains(item.getBounds())) {
				re.add(this.graphs.get(i));
			}
		}
		return re.toArray(new IGraph[re.size()]);
	}

	@Override
	public Rectangle getBounds() {
		computeBox();
		return box;
	}

	private void graphBoundsChanged(GraphBoundsChangedEvent e) {
		this.box = null;
		computeBox();
	}

	private void computeBox() {
		this.box = null;
		for (IGraph graph : this.graphs) {
			if (this.box == null) {
				this.box = graph.getBounds();
			} else {
				this.box = this.box.union(graph.getBounds());
			}
		}
	}

	@Override
	public void clear() {
		this.graphs.clear();
		this.box = null;
	}
}
