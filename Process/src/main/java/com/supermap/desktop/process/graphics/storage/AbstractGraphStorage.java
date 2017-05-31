package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.events.*;

import javax.swing.event.EventListenerList;

/**
 * Created by highsad on 2017/5/27.
 */
public abstract class AbstractGraphStorage implements IGraphStorage {
	private EventListenerList listenerList = new EventListenerList();

	@Override
	public void addGraphCreatingListener(GraphCreatingListener listener) {
		this.listenerList.add(GraphCreatingListener.class, listener);
	}

	@Override
	public void removeGraphCreatingListener(GraphCreatingListener listener) {
		this.listenerList.remove(GraphCreatingListener.class, listener);
	}

	@Override
	public void addGraphCreatedListener(GraphCreatedListener listener) {
		this.listenerList.add(GraphCreatedListener.class, listener);
	}

	@Override
	public void removeGraphCreatedListener(GraphCreatedListener listener) {
		this.listenerList.remove(GraphCreatedListener.class, listener);
	}

	@Override
	public void addGraphRemovingListener(GraphRemovingListener listener) {
		this.listenerList.add(GraphRemovingListener.class, listener);
	}

	@Override
	public void removeGraphRemovingListener(GraphRemovingListener listener) {
		this.listenerList.remove(GraphRemovingListener.class, listener);
	}

	@Override
	public void addGraphRemovedListener(GraphRemovedListener listener) {
		this.listenerList.add(GraphRemovedListener.class, listener);
	}

	@Override
	public void removeGraphRemovedListener(GraphRemovedListener listener) {
		this.listenerList.remove(GraphRemovedListener.class, listener);
	}

	protected void fireGraphCreating(GraphCreatingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatingListener.class) {
				((GraphCreatingListener) listeners[i + 1]).graphCreating(e);
			}
		}
	}

	protected void fireGraphCreated(GraphCreatedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatedListener.class) {
				((GraphCreatedListener) listeners[i + 1]).graphCreated(e);
			}
		}
	}

	protected void fireGraphRemoving(GraphRemovingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphRemovingListener.class) {
				((GraphRemovingListener) listeners[i + 1]).graphRemoving(e);
			}
		}
	}

	protected void fireGraphRemoved(GraphRemovedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphRemovedListener.class) {
				((GraphRemovedListener) listeners[i + 1]).graphRemoved(e);
			}
		}
	}
}
