package com.supermap.desktop.process.parameter.interfaces;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessData {

	private String key;
	private String text;
	private Object data;
	private Class dataType;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Class getDataType() {
		return dataType;
	}

	public void setDataType(Class dataType) {
		this.dataType = dataType;
	}

	/**
	 * 数据来源很灵活
	 *
	 * @return
	 */
	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean match(ProcessData data) {
		if (this.dataType == null || data == null || data.getDataType() == null) {
			return false;
		}

		return this.dataType.equals(dataType);
	}
}