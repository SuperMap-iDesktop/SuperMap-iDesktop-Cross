package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/3/2.
 */
public class QuadTreeGraphs implements IGraphStorage {
	@Override
	public IConnectionManager getConnectionManager() {
		return null;
	}

	@Override
	public GraphCanvas getCanvas() {
		return null;
	}

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
	public void add(IGraph graph, Rectangle bounds) {

	}

	@Override
	public void remove(IGraph graph) {

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
	public IGraph[] findIntersetctedGraphs(int x, int y, int width, int height) {
		return new IGraph[0];
	}

	@Override
	public IGraph[] findIntersetctedGraphs(Rectangle rect) {
		return new IGraph[0];
	}

	@Override
	public IGraph[] findContainedGraphs(int x, int y, int width, int height) {
		return new IGraph[0];
	}

	@Override
	public IGraph[] findContainedGraphs(Rectangle rect) {
		return new IGraph[0];
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public void modifyGraphBounds(IGraph graph, int x, int y, int width, int height) {

	}

	@Override
	public void clear() {

	}
}
