package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Geometry;

/**
 * 表示多部分聚合特性
 * 
 * @author highsad
 *
 * @param <T>
 *            表示组成部分的类型
 */
public interface IMultiPartFeature<T> extends IGeometryFeature {
	public int getPartCount();

	public T getPart(int index);

	public void addPart(T part);

	public void addPart(Geometry geometry);

	public Geometry[] divide();

	boolean setPart(int partIndex, T part);
}
