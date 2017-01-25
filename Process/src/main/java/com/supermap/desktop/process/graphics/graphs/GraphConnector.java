package com.supermap.desktop.process.graphics.graphs;

import java.awt.*;

/**
 * 用来管理 Graph 的对外连接点
 * Created by highsad on 2017/1/19.
 */
public class GraphConnector {
	private IGraph graph;

	public GraphConnector(IGraph graph) {
		this.graph = graph;
	}

	public IGraph getGraph() {
		return this.graph;
	}

	public Point getTopConnector() {
		Point point = new Point();
		point.setLocation(this.graph.getX() + this.graph.getWidth() / 2, this.graph.getY());
		return point;
	}

	public Point getLeftConnector() {
		Point point = new Point();
		point.setLocation(this.graph.getX(), this.graph.getY() + this.graph.getHeight() / 2);
		return point;
	}

	public Point getRightConnector() {
		Point point = new Point();
		point.setLocation(this.graph.getX() + this.graph.getWidth(), this.graph.getY() + this.graph.getHeight() / 2);
		return point;
	}

	public Point getBottomConnector() {
		Point point = new Point();
		point.setLocation(this.graph.getX() + this.graph.getWidth() / 2, this.graph.getY() + this.graph.getHeight());
		return point;
	}
}
