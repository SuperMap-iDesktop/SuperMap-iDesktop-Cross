package com.supermap.desktop.process.graphics.events;

import java.util.EventObject;

/**
 * Created by highsad on 2017/5/8.
 */
public class ConnectionRemovedEvent extends EventObject {


	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public ConnectionRemovedEvent(Object source) {
		super(source);
	}
}
