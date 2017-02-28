package com.supermap.desktop.process.events;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.util.EventObject;

/**
 * Created by highsad on 2017/2/28.
 */
public class GraphSelectedChangedEvent extends EventObject {

	private IGraph selected;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphSelectedChangedEvent(Object source, IGraph selected) {
		super(source);
		this.selected = selected;
	}

	public IGraph getSelected() {
		return selected;
	}
}
