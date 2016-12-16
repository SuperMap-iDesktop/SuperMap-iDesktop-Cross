package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IDockbar;

import java.util.EventObject;

/**
 * Created by highsad on 2016/12/15.
 */
public class DockbarClosedEvent extends EventObject {

	private IDockbar dockbar;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param dockbar The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public DockbarClosedEvent(IDockbar dockbar) {
		super(dockbar);
		this.dockbar = dockbar;
	}

	public IDockbar getDockbar() {
		return dockbar;
	}
}
