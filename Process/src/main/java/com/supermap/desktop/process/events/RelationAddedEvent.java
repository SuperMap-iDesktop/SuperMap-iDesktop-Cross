package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.IRelation;
import com.supermap.desktop.process.core.NodeMatrix;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/21.
 */
public class RelationAddedEvent<T> extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public RelationAddedEvent(Object source) {
		super(source);
	}
//	private NodeMatrix<T> matrix;
//	private IRelation<T> relation;
//
//	/**
//	 * Constructs a prototypical Event.
//	 *
//	 * @param matrix The object on which the Event initially occurred.
//	 * @throws IllegalArgumentException if source is null.
//	 */
//	public RelationAddedEvent(NodeMatrix<T> matrix, IRelation<T> relation) {
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
//		return this.relation;
//	}
}
