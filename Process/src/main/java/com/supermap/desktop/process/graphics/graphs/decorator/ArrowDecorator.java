package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.LineGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class ArrowDecorator extends AbstractDecorator {

	private LineGraph line;

	public ArrowDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public IGraph clone() {
		return null;
	}
}
