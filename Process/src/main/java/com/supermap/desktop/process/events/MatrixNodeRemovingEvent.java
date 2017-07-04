package com.supermap.desktop.process.events;

import com.supermap.desktop.event.CancellationEvent;
import com.supermap.desktop.process.core.NodeMatrix;

/**
 * Created by highsad on 2017/6/21.
 */
public class MatrixNodeRemovingEvent<T extends Object> extends CancellationEvent {
	public MatrixNodeRemovingEvent(Object source, boolean isCancel) {
		super(source, isCancel);
	}
//	private NodeMatrix<T> matrix;
//	private T node;
//
//	public MatrixNodeRemovingEvent(NodeMatrix<T> matrix, T removingNode, boolean isCancel) {
//		super(matrix, isCancel);
//		this.matrix = matrix;
//		this.node = removingNode;
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
