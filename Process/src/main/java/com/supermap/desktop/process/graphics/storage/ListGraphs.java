package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by highsad on 2017/3/2.
 */
public class ListGraphs implements IGraphStorage {
	private Vector<IGraph> graphs = new Vector();
	private Vector<Rectangle> rects = new Vector<>();
	private Rectangle box = null;

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
		this.rects.add(bounds);
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
			this.rects.remove(index);
			computeBox();
		}
	}

	@Override
	public IGraph findGraph(Point point) {
		IGraph graph = null;
		for (int i = 0; i < this.rects.size(); i++) {
			Rectangle bounds = this.rects.get(i);
			if (bounds.contains(point)) {
				graph = this.graphs.get(i);
				break;
			}
		}
		return graph;
	}

	@Override
	public IGraph[] findGraphs(Point point) {
		ArrayList<IGraph> re = new ArrayList<>();
		for (int i = 0; i < this.rects.size(); i++) {
			Rectangle bounds = this.rects.get(i);
			if (bounds.contains(point)) {
				re.add(this.graphs.get(i));
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
		for (int i = 0; i < this.rects.size(); i++) {
			Rectangle bounds = this.rects.get(i);
			if (bounds.intersects(rect)) {
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
		for (int i = 0; i < this.rects.size(); i++) {
			Rectangle bounds = this.rects.get(i);
			if (rect.contains(bounds)) {
				re.add(this.graphs.get(i));
			}
		}
		return re.toArray(new IGraph[re.size()]);
	}

	@Override
	public Rectangle getBounds() {
		return this.box;
	}

	@Override
	public void modifyGraphBounds(IGraph graph, int x, int y, int width, int height) {
		if (this.graphs.contains(graph)) {
			graph.setLocation(new Point(x, y));
			graph.setSize(width, height);
			int index = this.graphs.indexOf(graph);
			this.rects.remove(index);
			this.rects.add(index, new Rectangle(x, y, width, height));
			computeBox();
		}
	}

	private void computeBox() {
		for (int i = 0; i < this.rects.size(); i++) {
			if (this.box == null) {
				this.box = this.rects.get(i);
			} else {
				this.box = this.box.union(this.rects.get(i));
			}
		}
	}

	@Override
	public void clear() {
		this.graphs.clear();
		this.rects.clear();
		this.box = null;
	}
}
