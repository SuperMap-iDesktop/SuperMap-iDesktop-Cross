package com.supermap.desktop.process.graphics.interaction;

import java.util.EventObject;

/**
 * Created by highsad on 2017/3/8.
 */
public class GraphCreatingEvent extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphCreatingEvent(Object source) {
		super(source);
	}
}
