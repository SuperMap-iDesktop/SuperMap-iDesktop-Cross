package com.supermap.desktop.WorkflowView.graphics.connection;

import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/5/4.
 */
public interface IGraphConnection {

	void disconnect();

	IGraph getStartGraph();

	IConnectable getStart();

	IGraph getEndGraph();

	IConnectable getEnd();

	String getMessage();
}
