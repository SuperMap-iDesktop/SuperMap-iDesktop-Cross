package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.WorkflowParser;
import com.supermap.desktop.process.graphics.GraphCanvas;

/**
 * Created by highsad on 2017/1/24.
 */
public class ProcessGraph extends RectangleGraph {

	private IProcess process;

	private ProcessGraph() {
		super(null);
	}


	public ProcessGraph(GraphCanvas canvas, IProcess process) {
		super(canvas);
		this.process = process;
		if (getCanvas() != null) {
		}
	}

	public IProcess getProcess() {
		return process;
	}

	public String getTitle() {
		return this.process == null ? "未知" : this.process.getTitle();
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		super.toXmlHook(jsonObject);
		jsonObject.put("process", process.getKey());
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		super.formXmlHook(xml);
		String key = (String) xml.get("process");
		process = WorkflowParser.getMetaProcess(key);
	}


}
