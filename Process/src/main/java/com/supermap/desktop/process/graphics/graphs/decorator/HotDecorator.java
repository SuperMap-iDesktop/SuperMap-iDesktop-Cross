package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.decorator.AbstractDecorator;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public class HotDecorator extends AbstractDecorator {

	public HotDecorator(GraphCanvas canvas) {
		super(canvas, null);
	}

	@Override
	public Rectangle getBounds() {
		Rectangle rect = null;
		if (getGraph() != null) {
			rect = (Rectangle) getGraph().getBounds().clone();
			rect.grow(1, 1);
		}
		return rect;
	}
}
