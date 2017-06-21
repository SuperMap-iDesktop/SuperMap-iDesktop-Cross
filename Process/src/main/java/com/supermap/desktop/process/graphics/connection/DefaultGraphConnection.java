package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/5/5.
 */
public class DefaultGraphConnection implements IGraphConnection {
	private IConnectable start;
	private IConnectable end;
	private String message;

	public DefaultGraphConnection(IConnectable start, IConnectable end) {
		this(start, end, null);
	}

	public DefaultGraphConnection(IConnectable start, IConnectable end, String message) {
		this.start = start;
		this.end = end;
		this.message = message;
	}

	@Override
	public void disconnect() {

	}

	@Override
	public IGraph getStartGraph() {
		return this.start == null ? null : this.start.getConnector();
	}

	@Override
	public IConnectable getStart() {
		return this.start;
	}

	@Override
	public IGraph getEndGraph() {
		return this.end == null ? null : this.end.getConnector();
	}

	@Override
	public IConnectable getEnd() {
		return this.end;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
