package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.NodeMatrix;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/21.
 */
public class MatrixNodeAddedEvent<T extends Object> extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public MatrixNodeAddedEvent(Object source) {
		super(source);
	}
//	private NodeMatrix<T> matrix;
//	private T node;
//
//	/**
//	 * Constructs a prototypical Event.
//	 *
//	 * @param matrix The object on which the Event initially occurred.
//	 * @throws IllegalArgumentException if source is null.
//	 */
//	public MatrixNodeAddedEvent(NodeMatrix<T> matrix, T addedNode) {
//		super(matrix);
//		this.matrix = matrix;
//		this.node = addedNode;
//	}
//
//	public NodeMatrix<T> getMatrix() {
//		return matrix;
//	}
//
//	public T getNode() {
//		return node;
//	}
}
