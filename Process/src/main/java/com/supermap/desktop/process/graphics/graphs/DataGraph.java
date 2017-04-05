package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.datas.IDataDescription;

/**
 * Created by highsad on 2017/1/24.
 */
public class DataGraph extends EllipseGraph {

	private IDataDescription data;

	public DataGraph(GraphCanvas canvas, IDataDescription data) {
		super(canvas);
		this.data = data;
	}

	public IDataDescription getData() {
		return this.data;
	}
}
