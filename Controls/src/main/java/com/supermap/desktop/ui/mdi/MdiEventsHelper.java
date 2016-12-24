package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.events.*;

import javax.swing.event.EventListenerList;

/**
 * Created by highsad on 2016/12/5.
 */
public class MdiEventsHelper {

	private EventListenerList listenerList = new EventListenerList();

	public void addPageAddedListener(PageAddedListener listener) {
		this.listenerList.add(PageAddedListener.class, listener);
	}

	public void removePageAddedListener(PageAddedListener listener) {
		this.listenerList.remove(PageAddedListener.class, listener);
	}

	public void addPageAddingListener(PageAddingListener listener) {
		this.listenerList.add(PageAddingListener.class, listener);
	}

	public void removePageAddingListener(PageAddingListener listener) {
		this.listenerList.remove(PageAddingListener.class, listener);
	}

	public void addPageClosedListener(PageClosedListener listener) {
		this.listenerList.add(PageClosedListener.class, listener);
	}

	public void removePageClosedListener(PageClosedListener listener) {
		this.listenerList.remove(PageClosedListener.class, listener);
	}

	public void addPageClosingListener(PageClosingListener listener) {
		this.listenerList.add(PageClosingListener.class, listener);
	}

	public void removePageClosingListener(PageClosingListener listener) {
		this.listenerList.remove(PageClosingListener.class, listener);
	}

	public void addPageActivatingListener(PageActivatingListener listener) {
		this.listenerList.add(PageActivatingListener.class, listener);
	}

	public void removePageActivatingListener(PageActivatingListener listener) {
		this.listenerList.remove(PageActivatingListener.class, listener);
	}

	public void addPageActivatedListener(PageActivatedListener listener) {
		this.listenerList.add(PageActivatedListener.class, listener);
	}

	public void removePageActivatedListener(PageActivatedListener listener) {
		this.listenerList.remove(PageActivatedListener.class, listener);
	}

	void firePageAdding(PageAddingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageAddingListener.class) {
				((PageAddingListener) listeners[i + 1]).pageAdding(e);

				if (e.isCancel()) {
					break;
				}
			}
		}
	}

	void firePageAdded(PageAddedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageAddedListener.class) {
				((PageAddedListener) listeners[i + 1]).pageAdded(e);
			}
		}
	}

	void firePageClosed(PageClosedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageClosedListener.class) {
				((PageClosedListener) listeners[i + 1]).pageClosed(e);
			}
		}
	}

	void firePageClosing(PageClosingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageClosingListener.class) {
				((PageClosingListener) listeners[i + 1]).pageClosing(e);

				if (e.isCancel()) {
					break;
				}
			}
		}
	}

	void firePageActivating(PageActivatingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageActivatingListener.class) {
				((PageActivatingListener) listeners[i + 1]).pageActivating(e);
			}
		}
	}

	void firePageActivated(PageActivatedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageActivatedListener.class) {
				((PageActivatedListener) listeners[i + 1]).pageActivated(e);
			}
		}
	}
}
