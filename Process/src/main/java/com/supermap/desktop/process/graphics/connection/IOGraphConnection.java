package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/5/27.
 */
public class IOGraphConnection implements IGraphConnection {
	private IConnectable start;
	private IConnectable end;
	private InputData input;
	private OutputData output;
	private String dataName;

	public IOGraphConnection(IConnectable start, IConnectable end, String dataName) {
		if (start == null
				|| end == null
				|| !(start.getConnector() instanceof OutputGraph)
				|| !(end.getConnector() instanceof ProcessGraph)
				|| StringUtilities.isNullOrEmpty(dataName)) {
			throw new IllegalArgumentException();
		}
		OutputGraph outputGraph = (OutputGraph) start.getConnector();
		ProcessGraph processGraph = (ProcessGraph) end.getConnector();

		if (outputGraph.getProcessGraph() == null || outputGraph.getProcessData() == null) {
			throw new IllegalArgumentException();
		}

		OutputData output = outputGraph.getProcessData();
		Inputs inputs = processGraph.getProcess().getInputs();

		if (!inputs.isContains(dataName)) {
			throw new IllegalArgumentException("The specified dataName do not exist.");
		}

		InputData input = inputs.getData(dataName);
		this.start = start;
		this.end = end;
		this.dataName = dataName;
		this.input = input;
		this.output = output;

		input.bind(output);
	}

	@Override
	public void disconnect() {
		if (this.input != null && this.output != null && this.input.isBind(this.output)) {
			this.input.unbind();
		}
	}

	@Override
	public IGraph getStartGraph() {
		return this.start != null ? this.start.getConnector() : null;
	}

	@Override
	public IConnectable getStart() {
		return this.start;
	}

	@Override
	public IGraph getEndGraph() {
		return this.end != null ? this.end.getConnector() : null;
	}

	@Override
	public IConnectable getEnd() {
		return this.end;
	}

	@Override
	public String getMessage() {
		return dataName;
	}
}
