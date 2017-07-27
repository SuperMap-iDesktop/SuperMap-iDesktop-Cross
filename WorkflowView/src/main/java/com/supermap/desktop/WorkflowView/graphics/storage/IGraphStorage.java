package com.supermap.desktop.WorkflowView.graphics.storage;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.events.GraphCreatedListener;
import com.supermap.desktop.WorkflowView.graphics.events.GraphCreatingListener;
import com.supermap.desktop.WorkflowView.graphics.events.GraphRemovedListener;
import com.supermap.desktop.WorkflowView.graphics.events.GraphRemovingListener;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/3/2.
 */
public interface IGraphStorage {

	GraphCanvas getCanvas();

	int getCount();

	IGraph[] getGraphs();

	IGraph getGraph(int index);

	Rectangle getBounds(IGraph graph);

	boolean contains(IGraph graph);

	void add(IGraph graph);

	void add(IGraph graph, Rectangle bounds);

	void remove(IGraph graph);

	/**
	 * 获取指定点最上层的 IGraph
	 *
	 * @param point
	 * @return
	 */
	IGraph findGraph(Point point);

	/**
	 * 获取指定点的所有 Graph
	 *
	 * @param point
	 * @return
	 */
	IGraph[] findGraphs(Point point);

	IGraph[] findIntersetctedGraphs(int x, int y, int width, int height);

	IGraph[] findIntersetctedGraphs(Rectangle rect);

	IGraph[] findContainedGraphs(int x, int y, int width, int height);

	IGraph[] findContainedGraphs(Rectangle rect);

	Rectangle getBounds();

	void clear();

	void addGraphCreatingListener(GraphCreatingListener listener);

	void removeGraphCreatingListener(GraphCreatingListener listener);

	void addGraphCreatedListener(GraphCreatedListener listener);

	void removeGraphCreatedListener(GraphCreatedListener listener);

	void addGraphRemovingListener(GraphRemovingListener listener);

	void removeGraphRemovingListener(GraphRemovingListener listener);

	void addGraphRemovedListener(GraphRemovedListener listener);

	void removeGraphRemovedListener(GraphRemovedListener listener);
}
