package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;

import java.awt.*;

/**
 * Graph 类自身不做绘制的好处在于，不同操作系统的风格、绘制工具等不尽相同，当需要适应不同的操作系统
 * 以及适应不同的 L&F 时，仅仅需要新增对应的绘制方法即可，无需更改 Graph 的实现
 * Created by highsad on 2017/1/18.
 */
public interface IGraph {
	GraphCanvas getCanvas();

	Rectangle getBounds();

	Point getLocation();

	Point getCenter();

	int getWidth();

	int getHeight();

	void setLocation(Point point);

	void setSize(int width, int height);

	boolean contains(Point point);

	void addGraphBoundsChangedListener(GraphBoundsChangedListener listener);

	void removeGraghBoundsChangedListener(GraphBoundsChangedListener listener);
}
