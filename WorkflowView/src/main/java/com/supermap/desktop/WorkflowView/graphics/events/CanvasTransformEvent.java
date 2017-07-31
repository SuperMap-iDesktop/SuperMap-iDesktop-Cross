package com.supermap.desktop.WorkflowView.graphics.events;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;

import java.util.EventObject;

/**
 * Created by highsad on 2017/3/16.
 */
public class CanvasTransformEvent extends EventObject {
	public final static int TYPE_UNKOWN = 0;
	public final static int TYPE_TRANSLATE = 1;
	public final static int TYPE_SCALE = 2;

	private GraphCanvas canvas;
	private int type = TYPE_UNKOWN;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param canvas The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public CanvasTransformEvent(GraphCanvas canvas, int type) {
		super(canvas);
		this.canvas = canvas;
		this.type = type == TYPE_SCALE || type == TYPE_TRANSLATE ? type : TYPE_UNKOWN;
	}

	public GraphCanvas getCanvas() {
		return canvas;
	}

	public int getType() {
		return type;
	}
}
