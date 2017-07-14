package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.IRelation;
import com.supermap.desktop.process.core.NodeMatrix;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/21.
 */
public class RelationRemovedEvent<T> extends EventObject {
	private NodeMatrix<T> matrix;
	private T fromNode;
	private T toNode;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param matrix The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public RelationRemovedEvent(NodeMatrix matrix, T fromNode, T toNode) {
		super(matrix);
		this.matrix = matrix;
		this.fromNode = fromNode;
		this.toNode = toNode;
	}

	public NodeMatrix<T> getMatrix() {
		return this.matrix;
	}

	public T getFromNode() {
		return fromNode;
	}

	public T getToNode() {
		return toNode;
	}
}
