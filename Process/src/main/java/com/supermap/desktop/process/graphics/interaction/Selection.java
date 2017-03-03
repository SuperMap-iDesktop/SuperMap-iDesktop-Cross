package com.supermap.desktop.process.graphics.interaction;

import com.supermap.desktop.process.graphics.GraphCanvas;

/**
 * Created by highsad on 2017/3/2.
 */
public abstract class Selection {
	private GraphCanvas canvas;

	public Selection(GraphCanvas canvas) {
		this.canvas = canvas;
	}
}
