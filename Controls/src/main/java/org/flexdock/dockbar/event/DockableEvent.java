package org.flexdock.dockbar.event;

import org.flexdock.docking.Dockable;
import org.flexdock.event.Event;

import java.util.EventObject;

/**
 * Created by highsad on 2016/12/15.
 */
public class DockableEvent extends Event {

	public static final int CLOSING = 1;
	public static final int CLOSED = 2;

	private Dockable dockbar;
	private String id;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param dockbar The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public DockableEvent(Dockable dockbar, int type) {
		super(dockbar, type);
		this.dockbar = dockbar;
		this.id = this.dockbar == null ? null : this.dockbar.getPersistentId();
	}

	public Dockable getDockbar() {
		return dockbar;
	}

	public String getId() {
		return id;
	}
}
