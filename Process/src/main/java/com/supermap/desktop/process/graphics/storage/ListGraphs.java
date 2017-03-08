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
		this.graphs.add(graph);
		this.rects.add(graph.getBounds());
	}

	@Override
	public void add(IGraph graph, Rectangle bounds) {
		this.graphs.add(graph);
		this.rects.add(bounds);
	}

	@Override
	public void remove(IGraph graph) {
		int index = this.graphs.indexOf(graph);
		if (index > -1) {
			this.graphs.remove(graph);
			this.rects.remove(index);
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
		Rectangle rect = new Rectangle(x, y, width, height);

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
		return new IGraph[0];
	}

	@Override
	public void clear() {
		this.graphs.clear();
		this.rects.clear();
	}
}
