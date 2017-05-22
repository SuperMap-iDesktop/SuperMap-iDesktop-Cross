package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/4/26.
 */
public interface IDecorator {
	GraphCanvas getCanvas();

	/**
	 * 获取 Decorator 的优先级。数值越小，优先级越高。
	 * Get the priority of current decorator.
	 * The smaller the number value,the higher the priority.
	 *
	 * @return
	 */
	IGraph getGraph();

	int getPriority();

	void setPriority(int priority);

	void decorate(IGraph graph);

	void undecorate();

	boolean contains(Point point);

	Rectangle getBounds();

	void paint(Graphics g);
}
