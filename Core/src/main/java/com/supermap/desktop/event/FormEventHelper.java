package com.supermap.desktop.event;

import javax.swing.event.EventListenerList;

/**
 * Created by highsad on 2016/12/27.
 */
public class FormEventHelper {
	private EventListenerList listenerList = new EventListenerList();

	public void addFormActivatedListener(FormActivatedListener listener) {
		this.listenerList.add(FormActivatedListener.class, listener);
	}

	public void removeFormActivatedListener(FormActivatedListener listener) {
		this.listenerList.remove(FormActivatedListener.class, listener);
	}

	public void addFormDeactivatedListener(FormDeactivatedListener listener) {
		this.listenerList.add(FormDeactivatedListener.class, listener);
	}

	public void removeFormDeactivatedListener(FormDeactivatedListener listener) {
		this.listenerList.remove(FormDeactivatedListener.class, listener);
	}

	/**
	 * 注册子窗口关闭前事件
	 *
	 * @param listener
	 */
	public void addFormClosingListener(FormClosingListener listener) {
		this.listenerList.add(FormClosingListener.class, listener);
	}

	/**
	 * 注销子窗口关闭前事件
	 *
	 * @param listener
	 */
	public void removeFormClosingListener(FormClosingListener listener) {
		this.listenerList.remove(FormClosingListener.class, listener);
	}

	/**
	 * 注册子窗口关闭后事件
	 *
	 * @param listener
	 */
	public void addFormClosedListener(FormClosedListener listener) {
		this.listenerList.add(FormClosedListener.class, listener);
	}

	public void removeFormClosedListener(FormClosedListener listener) {
		this.listenerList.remove(FormClosedListener.class, listener);
	}

	public void addFormShownListener(FormShownListener listener) {
		this.listenerList.add(FormShownListener.class, listener);
	}

	public void removeFormShownListener(FormShownListener listener) {
		this.listenerList.remove(FormShownListener.class, listener);
	}

	public void addActiveFormChangedListener(ActiveFormChangedListener listener) {
		this.listenerList.add(ActiveFormChangedListener.class, listener);
	}

	public void removeActiveFormChangedListener(ActiveFormChangedListener listener) {
		this.listenerList.remove(ActiveFormChangedListener.class, listener);
	}

	public void fireFormActivated(FormActivatedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FormActivatedListener.class) {
				((FormActivatedListener) listeners[i + 1]).formActivated(e);
			}
		}
	}

	public void fireFormDeactivated(FormDeactivatedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FormDeactivatedListener.class) {
				((FormDeactivatedListener) listeners[i + 1]).formDeactivated(e);
			}
		}
	}

	public void fireFormClosing(FormClosingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FormClosingListener.class) {
				((FormClosingListener) listeners[i + 1]).formClosing(e);
			}
		}
	}

	public void fireFormClosed(FormClosedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FormClosedListener.class) {
				((FormClosedListener) listeners[i + 1]).formClosed(e);
			}
		}
	}

	public void fireFormShown(FormShownEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FormShownListener.class) {
				((FormShownListener) listeners[i + 1]).formShown(e);
			}
		}
	}

	public void fireActiveFormChanged(ActiveFormChangedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActiveFormChangedListener.class) {
				((ActiveFormChangedListener) listeners[i + 1]).activeFormChanged(e);
			}
		}
	}
}
