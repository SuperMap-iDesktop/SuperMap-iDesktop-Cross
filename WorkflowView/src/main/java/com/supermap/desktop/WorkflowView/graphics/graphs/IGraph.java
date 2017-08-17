package com.supermap.desktop.WorkflowView.graphics.graphs;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.WorkflowView.graphics.graphs.decorators.IDecorator;

import java.awt.*;

/**
 * Graph 类自身不做绘制的好处在于，不同操作系统的风格、绘制工具等不尽相同，当需要适应不同的操作系统
 * 以及适应不同的 L&F 时，仅仅需要新增对应的绘制方法即可，无需更改 Graph 的实现
 * Created by highsad on 2017/1/18.
 */
public interface IGraph {

	void setCanvas(GraphCanvas canvas);

	GraphCanvas getCanvas();

	IDecorator[] getDecorators();

	int getDecoratorSize();

	IDecorator getDecorator(String key);

	void addDecorator(String key, IDecorator decorator);

	void removeDecorator(IDecorator decorator);

	void removeDecorator(String key);

	boolean isDecoratedBy(IDecorator decorator);

	boolean isDecoratedBy(String key);

	Rectangle getBounds();

	Rectangle getTotalBounds();

	Point getLocation();

	Point getCenter();

	int getWidth();

	int getHeight();

	void setLocation(Point point);

	void setSize(int width, int height);

	boolean contains(Point point);

	void addGraphBoundsChangedListener(GraphBoundsChangedListener listener);

	void removeGraghBoundsChangedListener(GraphBoundsChangedListener listener);

	void paint(Graphics g);
}
