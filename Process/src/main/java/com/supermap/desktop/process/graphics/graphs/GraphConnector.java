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

	public boolean canConnect(Point point) {
		Point topP = getTopConnector();
		Point leftP = getLeftConnector();
		Point bottomP = getBottomConnector();
		Point rightP = getRightConnector();

		Rectangle topR = new Rectangle(topP.x - 2, topP.x - 2, 5, 5);
		Rectangle leftR = new Rectangle(leftP.x - 2, leftP.x - 2, 5, 5);
		Rectangle bottomR = new Rectangle(bottomP.x - 2, bottomP.x - 2, 5, 5);
		Rectangle rightR = new Rectangle(rightP.x - 2, rightP.x - 2, 5, 5);
		return topR.contains(point) || leftR.contains(point) || bottomR.contains(point) || rightR.contains(point);
	}
}
