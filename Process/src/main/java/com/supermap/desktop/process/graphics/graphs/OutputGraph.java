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
	private String name;

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
		return processData != null ? this.processData.getName() : name;
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		super.toXmlHook(jsonObject);
		jsonObject.put("processDataName", processData.getName());
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		super.formXmlHook(xml);
		name = ((String) xml.get("processDataName"));
	}

	public void setProcessGraph(ProcessGraph processGraph) {
		this.processGraph = processGraph;
	}


	public String getName() {
		return name;
	}

	public void setProcessData(OutputData processData) {
		this.processData = processData;
	}
}
