package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.process.graphics.connection.IConnection;
import com.supermap.desktop.process.graphics.events.*;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/3/2.
 */
public class ListGraphs implements IGraphStorage {
	private GraphCanvas canvas;
	private IConnectionManager connectionManager = new ListConnectionManager(this.canvas);
	private Map<IConnection, IGraph> connectionMap = new ConcurrentHashMap<>();
	private Vector<IGraph> graphs = new Vector();
	private Rectangle box = null;

	private ConnectionAddedListener connectionAddedListener = new ConnectionAddedListener() {
		@Override
		public void connectionAdded(ConnectionAddedEvent e) {
			ListGraphs.this.connectionAdded(e);
		}
	};
	private ConnectionRemovingListener connectionRemovingListener = new ConnectionRemovingListener() {
		@Override
		public void connectionRemoving(ConnectionRemovingEvent e) {
			if (!e.isCancel()) {
				ListGraphs.this.connectionRemoving(e);
			}
		}
	};
	private GraphBoundsChangedListener graphBoundsChangedListener = new GraphBoundsChangedListener() {
		@Override
		public void graghBoundsChanged(GraphBoundsChangedEvent e) {
			ListGraphs.this.graphBoundsChanged(e);
		}
	};

	public ListGraphs(GraphCanvas canvas) {
		this.canvas = canvas;
		this.connectionManager = new ListConnectionManager(this.canvas);
		this.connectionManager.addConnectionAddedListener(this.connectionAddedListener);
		this.connectionManager.addConnectionRemovingListener(this.connectionRemovingListener);
	}

	@Override
	public IConnectionManager getConnectionManager() {
		return connectionManager;
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
		this.graphs.add(graph);
		if (this.box == null) {
			this.box = bounds;
		} else {
			this.box = this.box.union(bounds);
		}
	}

	@Override
	public void remove(IGraph graph) {
		int index = this.graphs.indexOf(graph);
		if (index > -1) {
			this.graphs.remove(graph);
			computeBox();
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
		return this.box;
	}

	private void connectionAdded(ConnectionAddedEvent e) {
		IConnection connection = e.getConnection();
		if (connection != null && connection.getStart() != null && connection.getEnd() != null) {
			ConnectionLineGraph lineGraph = new ConnectionLineGraph(this.canvas, connection);
			add(lineGraph);
			this.connectionMap.put(connection, lineGraph);
		}
	}

	private void connectionRemoving(ConnectionRemovingEvent e) {
		IConnection connection = e.getConnection();
		if (this.connectionMap.containsKey(connection)) {
			IGraph graph = this.connectionMap.get(connection);
			remove(graph);
		}
	}

	private void graphBoundsChanged(GraphBoundsChangedEvent e) {
		this.box = null;
		computeBox();
	}

	private void computeBox() {
		for (int i = 0; i < this.graphs.size(); i++) {
			if (this.box == null) {
				this.box = this.graphs.get(i).getBounds();
			} else {
				this.box = this.box.union(this.graphs.get(i).getBounds());
			}
		}
	}

	@Override
	public void clear() {
		this.graphs.clear();
		this.box = null;
	}
}
