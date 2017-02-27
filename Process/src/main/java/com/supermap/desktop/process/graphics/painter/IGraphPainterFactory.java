package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public interface IGraphPainterFactory {
	IGraphPainter getPainter(IGraph graph, Graphics graphics);

	IGraphPainter getPainter(IGraph graph, Graphics graphics, IStyleFactory styleFactory);
}
