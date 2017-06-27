package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.IRelation;
import com.supermap.desktop.process.core.NodeMatrix;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/21.
 */
public class RelationRemovingEvent<T> extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public RelationRemovingEvent(Object source) {
		super(source);
	}
//	private NodeMatrix<T> matrix;
//	private IRelation<T> relation;
//
//	public RelationRemovingEvent(NodeMatrix<T> matrix, IRelation relation) {
//		super(matrix);
//		this.matrix = matrix;
//		this.relation = relation;
//	}
//
//	public NodeMatrix<T> getMatrix() {
//		return this.matrix;
//	}
//
//	public IRelation<T> getRelation() {
//		return relation;
//	}
}
