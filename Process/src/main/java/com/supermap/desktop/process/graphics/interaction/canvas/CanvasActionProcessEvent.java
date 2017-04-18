package com.supermap.desktop.process.graphics.interaction.canvas;

import java.util.EventObject;

/**
 * Created by highsad on 2017/4/18.
 */
public class CanvasActionProcessEvent extends EventObject {
	public final static int START = 1;
	public final static int STOP = 2;

	private CanvasAction action;
	private int status;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param action The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public CanvasActionProcessEvent(CanvasAction action, int status) {
		super(action);
		if (status != START && status != STOP) {
			throw new IllegalArgumentException();
		}

		this.status = status;
		this.action = action;
	}

	public CanvasAction getAction() {
		return action;
	}

	public int getStatus() {
		return status;
	}
}
