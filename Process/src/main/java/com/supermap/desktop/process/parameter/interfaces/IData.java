package com.supermap.desktop.process.parameter.interfaces;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IData {

	String getKey();

	String getText();

	/**
	 * 数据来源很灵活
	 *
	 * @return
	 */
	Object getData();

	void setData();

	boolean match(IData data);
}