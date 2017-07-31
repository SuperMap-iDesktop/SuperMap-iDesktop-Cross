package com.supermap.desktop.WorkflowView.graphics.storage;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.connection.IConnectable;
import com.supermap.desktop.WorkflowView.graphics.connection.IGraphConnection;
import com.supermap.desktop.WorkflowView.graphics.events.ConnectionAddedListener;
import com.supermap.desktop.WorkflowView.graphics.events.ConnectionRemovedListener;
import com.supermap.desktop.WorkflowView.graphics.events.ConnectionRemovingListener;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/4/5.
 */
public interface IConnectionManager {
	GraphCanvas getCanvas();

	IGraphConnection[] getConnections();

	void connect(IConnectable start, IConnectable end);

	void connect(IConnectable start, IConnectable end, String message);

	void connect(IGraphConnection connection);

	void removeConnection(IConnectable connectable);

	void removeConnection(IGraph connector);

	void removeConnection(IGraphConnection connection);

	IGraph[] getPreGraphs(IGraph end);

	IGraph[] getNextGraphs(IGraph start);

	boolean isConnectedAsStart(IGraph start);

	boolean isConnectedAsEnd(IGraph end);

	boolean isConnected(IConnectable connectable1, IConnectable connectable2);

	boolean isConnected(IGraph graph1, IGraph graph2);

	void addConnectionAddedListener(ConnectionAddedListener listener);

	void removeConnectionAddedListener(ConnectionAddedListener listener);

	void addConnectionRemovingListener(ConnectionRemovingListener listener);

	void removeConnectionRemovingListener(ConnectionRemovingListener listener);

	void addConnectionRemovedListener(ConnectionRemovedListener listener);

	void removeConnectionRemovedListener(ConnectionRemovedListener listener);
}
