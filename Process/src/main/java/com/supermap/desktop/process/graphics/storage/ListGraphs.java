package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.util.Vector;

/**
 * Created by highsad on 2017/3/2.
 */
public class ListGraphs implements IGraphStorage {
	private Vector graphs = new Vector();

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public IGraph[] getGraphs() {
		return new IGraph[0];
	}

	@Override
	public IGraph getGraph(int index) {
		return null;
	}

	@Override
	public Rectangle getBounds(IGraph graph) {
		return null;
	}

	@Override
	public boolean contains(IGraph graph) {
		return false;
	}

	@Override
	public void add(IGraph graph) {

	}

	@Override
	public IGraph findGraph(Point point) {
		return null;
	}

	@Override
	public IGraph[] findGraphs(Point point) {
		return new IGraph[0];
	}

	@Override
	public IGraph[] findGraphs(Rectangle rect) {
		return new IGraph[0];
	}

	@Override
	public void clear() {

	}
}
