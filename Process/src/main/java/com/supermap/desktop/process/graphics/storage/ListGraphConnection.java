package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.DefaultGraphConnection;
import com.supermap.desktop.process.graphics.connection.IConnectable;
import com.supermap.desktop.process.graphics.connection.IConnection;
import com.supermap.desktop.process.graphics.events.*;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/4/5.
 */
public class ListGraphConnection implements IConnectionManager {
	private GraphCanvas canvas;
	private java.util.List<IConnection> connections = new ArrayList<>();
	private EventListenerList listenerList = new EventListenerList();

	public ListGraphConnection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public IConnection[] getConnections() {
		return this.connections.toArray(new IConnection[this.connections.size()]);
	}

	@Override
	public void connect(IConnectable start, IConnectable end) {
		connect(start, end, null);
	}

	@Override
	public void connect(IConnectable start, IConnectable end, String message) {
		if (isConnected(start, end)) {
			return;
		}

		if (start != null && end != null && start != end) {
			IConnection connection = new DefaultGraphConnection(start, end, message);
			this.connections.add(connection);
		}
	}

	@Override
	public void removeConnection(IConnectable connectable) {
		for (int i = this.connections.size() - 1; i >= 0; i--) {
			IConnection connection = this.connections.get(i);

			if (connection.getStart() == connectable || connection.getEnd() == connectable) {
				this.connections.remove(i);
			}
		}
	}

	@Override
	public void removeConnection(IGraph graph) {
		for (int i = this.connections.size() - 1; i >= 0; i--) {
			IConnection connection = this.connections.get(i);

			if (connection.getStartGraph() == graph || connection.getEndGraph() == graph) {
				this.connections.remove(i);
			}
		}
	}

	@Override
	public void removeConnection(IConnection connection) {
		if (connection == null) {
			return;
		}

		this.connections.remove(connection);
	}

	@Override
	public IGraph[] getPreGraphs(IGraph end) {
		if (end == null) {
			return null;
		}

		ArrayList<IGraph> ret = new ArrayList<>();
		for (int i = 0, size = this.connections.size(); i < size; i++) {
			IConnection connection = this.connections.get(i);

			if (connection.getEndGraph() == end && connection.getStartGraph() != null
					&& !ret.contains(connection.getStartGraph())) {
				ret.add(connection.getStartGraph());
			}
		}
		return ret.toArray(new IGraph[ret.size()]);
	}

	@Override
	public IGraph[] getNextGraphs(IGraph start) {
		if (start == null) {
			return null;
		}

		ArrayList<IGraph> ret = new ArrayList<>();
		for (int i = 0, size = this.connections.size(); i < size; i++) {
			IConnection connection = this.connections.get(i);

			if (connection.getStartGraph() == start && connection.getEndGraph() != null
					&& !ret.contains(connection.getEndGraph())) {
				ret.add(connection.getEndGraph());
			}
		}
		return ret.toArray(new IGraph[ret.size()]);
	}

	@Override
	public boolean isConnectedAsStart(IGraph start) {
		boolean ret = false;
		for (int i = 0, size = this.connections.size(); i < size; i++) {
			IConnection connection = this.connections.get(i);
			if (connection.getStartGraph() == start && connection.getEndGraph() != null) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean isConnectedAsEnd(IGraph end) {
		boolean ret = false;
		for (int i = 0, size = this.connections.size(); i < size; i++) {
			IConnection connection = this.connections.get(i);

			if (connection.getEndGraph() == end && connection.getStartGraph() != null) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean isConnected(IConnectable connectable1, IConnectable connectable2) {
		boolean ret = false;
		for (int i = 0, size = this.connections.size(); i < size; i++) {
			IConnection connection = this.connections.get(i);

			if ((connection.getStart() == connectable1 && connection.getEnd() == connectable2)
					|| (connection.getStart() == connectable2 && connection.getEnd() == connectable1)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean isConnected(IGraph graph1, IGraph graph2) {
		boolean ret = false;
		for (int i = 0, size = this.connections.size(); i < size; i++) {
			IConnection connection = this.connections.get(i);
			if ((connection.getStartGraph() == graph1 && connection.getEndGraph() == graph2)
					|| (connection.getStartGraph() == graph2 && connection.getEndGraph() == graph1)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public void addConnectionAddedListener(ConnectionAddedListener listener) {
		this.listenerList.add(ConnectionAddedListener.class, listener);
	}

	@Override
	public void removeConnectionAddedListener(ConnectionAddedListener listener) {
		this.listenerList.remove(ConnectionAddedListener.class, listener);
	}

	@Override
	public void addConnectionRemovingListener(ConnectionRemovingListener listener) {
		this.listenerList.add(ConnectionRemovingListener.class, listener);
	}

	@Override
	public void removeConnectionRemovingListener(ConnectionRemovingListener listener) {
		this.listenerList.remove(ConnectionRemovingListener.class, listener);
	}

	@Override
	public void addConnectionRemovedListener(ConnectionRemovedListener listener) {
		this.listenerList.add(ConnectionRemovedListener.class, listener);
	}

	@Override
	public void removeConnectionRemovedListener(ConnectionRemovedListener listener) {
		this.listenerList.remove(ConnectionRemovedListener.class, listener);
	}

	protected void fireConnectionAdded(ConnectionAddedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatedListener.class) {
				((ConnectionAddedListener) listeners[i + 1]).connectionAdded(e);
			}
		}
	}

	protected void fireConnectionRemoving(ConnectionRemovingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatedListener.class) {
				((ConnectionRemovingListener) listeners[i + 1]).connectionRemoving(e);
			}
		}
	}

	protected void fireConnectionRemoved(ConnectionRemovedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatedListener.class) {
				((ConnectionRemovedListener) listeners[i + 1]).connectionRemoved(e);
			}
		}
	}
}
