package com.supermap.desktop.process.parameter;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IData<T> {

	/**
	 * 数据来源很灵活
	 *
	 * @return
	 */
	T getData();

	boolean match(IData data);
}
