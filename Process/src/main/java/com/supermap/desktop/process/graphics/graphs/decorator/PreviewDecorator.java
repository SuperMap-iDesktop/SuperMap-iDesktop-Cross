package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class PreviewDecorator extends AbstractDecorator {

	public PreviewDecorator(GraphCanvas canvas) {
		super(canvas, null);
	}

	@Override
	public IGraph clone() {
		return null;
	}

	@Override
	public void setLocation(Point point) {

	}

	@Override
	public void setSize(int width, int height) {

	}

	@Override
	public boolean contains(Point point) {
		return false;
	}
}
