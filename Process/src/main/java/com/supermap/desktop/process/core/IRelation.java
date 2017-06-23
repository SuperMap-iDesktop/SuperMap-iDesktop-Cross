package com.supermap.desktop.process.core;

/**
 * Created by xie on 2017/3/13.
 * Extensible interface if you want add you constraint(two
 * or more nodes)
 */
public interface IRelation<T> {
	/**
	 * how to relate.
	 *
	 * @param from
	 * @param to
	 */
	void relate(T from, T to);

	/**
	 * clear relation
	 */
	void clear();
}
