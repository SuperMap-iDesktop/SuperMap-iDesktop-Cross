package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Graph 绘制工具的工厂类，主要根据指定的 IGraph 获取对应的绘制工具。
 * 与 {@link IGraphPainter}、{@link IStyleFactory} 搭配使用。
 * {@link IGraphPainter} 用于对 Graph 进行绘制，而 IStyleFactory 用于定制对应 Painter 的 Graphics 绘制属性。
 * Created by highsad on 2017/2/25.
 */
public interface IGraphPainterFactory {
	IGraphPainter getPainter(IGraph graph, Graphics graphics);

	IGraphPainter getPainter(IGraph graph, Graphics graphics, IStyleFactory styleFactory);
}
