package com.supermap.desktop.process.graphics.events;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.interaction.canvas.Selection;

import java.util.EventObject;

/**
 * Created by highsad on 2017/2/28.
 */
public class GraphSelectedChangedEvent extends EventObject {

	private Selection selection;
	private GraphCanvas canvas;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param canvas The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphSelectedChangedEvent(GraphCanvas canvas, Selection selected) {
		super(canvas);
		this.canvas = canvas;
		this.selection = selected;
	}

	public Selection getSelection() {
		return this.selection;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}
}
