package com.supermap.desktop.process.core;

import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.process.util.WorkFlowXmlUtilties;

/**
 * Created by xie on 2017/3/18.
 * WorkFLow应该只存描述字符串，而不是具体的对象。不然打开多个会导致多个窗体指向同一个对象。
 */
public class Workflow implements IWorkFlow {
	private String name = "workFLow";
	private String matrixXml;

	public Workflow(String name) {
		this.name = name;
	}

	public void setMatrix(NodeMatrix matrix) {
		this.matrixXml = WorkFlowXmlUtilties.parseToXml(matrix, name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		setMatrix(getMatrix()); // 把名字改了
	}
	public NodeMatrix getMatrix() {
		return WorkFlowXmlUtilties.stringToNodeMatrix(matrixXml);
	}

	@Override
	public String getMatrixXml() {
		return matrixXml;
	}

	@Override
	public void setMatrixXml(String matrixXml) {
		this.matrixXml = matrixXml;
	}
}
