package com.supermap.desktop.process.events;

import com.supermap.desktop.event.CancellationEvent;
import com.supermap.desktop.process.core.NodeMatrix;

/**
 * Created by highsad on 2017/6/21.
 */
public class MatrixNodeAddingEvent<T extends Object> extends CancellationEvent {
	public MatrixNodeAddingEvent(Object source, boolean isCancel) {
		super(source, isCancel);
	}
//	private NodeMatrix<T> matrix;
//	private T node;
//
//	public MatrixNodeAddingEvent(NodeMatrix<T> matrix, T addingNode, boolean isCancel) {
//		super(matrix, isCancel);
//		this.matrix = matrix;
//		this.node = addingNode;
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
