package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/5/4.
 */
public interface IConnection {
	IGraph getStart();

	IGraph getEnd();
}
