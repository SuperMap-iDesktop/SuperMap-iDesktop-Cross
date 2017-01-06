package com.supermap.desktop.process.parameter;

/**
 * 参数数据节点表示
 * 因为不方便做资源化，所以需要一个数据-描述对应的类
 *
 * @author XiaJT
 */
public class ParameterDataNode {
	private String describe;
	private Object data;

	public ParameterDataNode(String describe, Object data) {
		this.describe = describe;
		this.data = data;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
