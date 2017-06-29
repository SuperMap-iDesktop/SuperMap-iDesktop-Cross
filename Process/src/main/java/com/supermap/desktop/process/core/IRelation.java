package com.supermap.desktop.process.core;

/**
 * Created by xie on 2017/3/13.
 * Extensible interface if you want add you constraint(two
 * or more nodes)
 */
public interface IRelation<T> {

	T getForm();

	T getTo();

	/**
	 * how to relate.
	 */
	void relate();

	/**
	 * clear relation
	 */
	void clear();
}
