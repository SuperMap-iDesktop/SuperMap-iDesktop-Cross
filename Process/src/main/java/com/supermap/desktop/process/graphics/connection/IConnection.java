package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/5/4.
 */
public interface IConnection {
	IGraph getStartGraph();

	IConnectable getStart();

	void setStart(IConnectable start);

	IGraph getEndGraph();

	IConnectable getEnd();

	void setEnd(IConnectable end);

	String getMessage();
}
