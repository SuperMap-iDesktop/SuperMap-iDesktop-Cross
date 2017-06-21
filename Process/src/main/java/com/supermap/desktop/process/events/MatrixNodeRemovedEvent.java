package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.NodeMatrix;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/21.
 */
public class MatrixNodeRemovedEvent<T extends Object> extends EventObject {
	private NodeMatrix matrix;
	private T node;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param matrix The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public MatrixNodeRemovedEvent(NodeMatrix matrix, T removedNode) {
		super(matrix);
		this.matrix = matrix;
		this.node = removedNode;
	}

	public NodeMatrix getMatrix() {
		return matrix;
	}

	public T getNode() {
		return node;
	}
}
