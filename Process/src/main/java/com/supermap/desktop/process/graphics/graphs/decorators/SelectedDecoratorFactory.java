package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/5/9.
 */
public class SelectedDecoratorFactory {

	public static IDecorator createDecorator(IGraph graph) {
		if (graph instanceof LineGraph) {
			return new LineSelectedDecorator(graph.getCanvas());
		} else {
			return new SelectedDecorator(graph.getCanvas());
		}
	}
}
