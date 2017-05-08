package com.supermap.desktop.process.graphics.events;

import java.util.EventObject;

/**
 * Created by highsad on 2017/5/8.
 */
public class ConnectionRemovingEvent extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public ConnectionRemovingEvent(Object source) {
		super(source);
	}
}
