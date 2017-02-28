package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * IGraph 的绘制工具。
 * Created by highsad on 2017/2/25.
 */
public interface IGraphPainter {
	IStyleFactory getStyleFactory();

	void setStyleFactory(IStyleFactory styleFactory);

	void setCanvas(GraphCanvas canvas);

	void setGraphics(Graphics graphics);

	void setGraph(IGraph graph);

	void paint();

	void paint(Graphics graphics, IGraph graph);
}
