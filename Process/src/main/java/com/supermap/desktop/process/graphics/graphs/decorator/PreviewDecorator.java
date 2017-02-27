package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/2/25.
 */
public class PreviewDecorator extends AbstractDecorator {

	public PreviewDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public IGraph clone() {
		return null;
	}
}
