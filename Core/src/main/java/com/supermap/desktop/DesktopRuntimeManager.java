package com.supermap.desktop;

import com.supermap.desktop.event.DesktopRuntimeEvent;
import com.supermap.desktop.event.DesktopRuntimeListener;

import javax.swing.event.EventListenerList;

/**
 * @author XiaJT
 */
public class DesktopRuntimeManager {

	private EventListenerList eventListenerList = new EventListenerList();
	private static DesktopRuntimeManager desktopRuntimeManager;

	private DesktopRuntimeManager() {

	}

	public void addRuntimeStateListener(DesktopRuntimeListener desktopRuntimeListener) {
		eventListenerList.add(DesktopRuntimeListener.class, desktopRuntimeListener);
	}

	public void removeRuntimeStateListener(DesktopRuntimeListener desktopRuntimeListener) {
		eventListenerList.remove(DesktopRuntimeListener.class, desktopRuntimeListener);
	}

	public void fireDesktopRuntimeStateListener(DesktopRuntimeEvent event) {
		DesktopRuntimeListener[] listeners = eventListenerList.getListeners(DesktopRuntimeListener.class);
		for (DesktopRuntimeListener listener : listeners) {
			listener.stateChanged(event);
		}
	}

	public static DesktopRuntimeManager getInstance() {
		if (desktopRuntimeManager == null) {
			desktopRuntimeManager = new DesktopRuntimeManager();
		}
		return desktopRuntimeManager;
	}
}
