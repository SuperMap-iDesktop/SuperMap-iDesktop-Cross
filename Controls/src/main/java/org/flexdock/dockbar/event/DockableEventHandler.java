package org.flexdock.dockbar.event;

import org.flexdock.event.Event;
import org.flexdock.event.EventHandler;

import java.util.EventListener;

/**
 * Created by highsad on 2016/12/15.
 */
public class DockableEventHandler extends EventHandler {
	/**
	 * This class accepts {@code DockbarEvent}s.
	 *
	 * @param evt
	 */
	public boolean acceptsEvent(Event evt) {
		return evt instanceof DockableEvent;
	}

	public boolean acceptsListener(EventListener listener) {
		return listener instanceof DockableListener;
	}

	public void handleEvent(Event event, EventListener consumer, int eventType) {
		DockableEvent evt = (DockableEvent) event;
		DockableListener listener = (DockableListener) consumer;
		listener.dockable(evt);
	}
}
