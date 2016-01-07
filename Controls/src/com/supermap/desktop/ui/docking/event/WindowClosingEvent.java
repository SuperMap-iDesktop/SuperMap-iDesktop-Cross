package com.supermap.desktop.ui.docking.event;

import java.util.EventObject;
import com.supermap.desktop.event.CancelEventObject;
import com.supermap.desktop.ui.docking.DockingWindow;

public class WindowClosingEvent extends CancelEventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DockingWindow window = null;

	public WindowClosingEvent(Object source) {
		super(source);
		this.window = (DockingWindow) source;
	}

	public DockingWindow getWindow() {
		return this.window;
	}

	public void setWindow(DockingWindow window) {
		this.window = window;
	}
}
