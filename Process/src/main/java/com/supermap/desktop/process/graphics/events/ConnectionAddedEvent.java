package com.supermap.desktop.process.graphics.events;

import java.util.EventObject;

/**
 * Created by highsad on 2017/5/8.
 */
public class ConnectionAddedEvent extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public ConnectionAddedEvent(Object source) {
		super(source);
	}
}
