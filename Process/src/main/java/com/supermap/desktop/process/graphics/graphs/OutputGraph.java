package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;

/**
 * Created by highsad on 2017/2/28.
 */
public class OutputGraph extends EllipseGraph {

	private ProcessGraph processGraph;
	private OutputData processData;

	private OutputGraph() {
		super(null);
	}

	public OutputGraph(GraphCanvas canvas, ProcessGraph processGraph, OutputData processData) {
		super(canvas);
		this.processGraph = processGraph;
		this.processData = processData;
	}

	public ProcessGraph getProcessGraph() {
		return processGraph;
	}

	public OutputData getProcessData() {
		return processData;
	}

	public String getTitle() {
		return this.processData.getName();
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		super.toXmlHook(jsonObject);
		jsonObject.put("processData", processData.toString());
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		super.formXmlHook(xml);
		processData = OutputData.formString(((String) xml.get("processData")));
	}

	public void setProcessGraph(ProcessGraph processGraph) {
		this.processGraph = processGraph;
	}
}
