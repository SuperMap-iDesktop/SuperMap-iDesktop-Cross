package com.supermap.desktop.process.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.events.MatrixNodeAddedEvent;
import com.supermap.desktop.process.events.MatrixNodeAddedListener;
import com.supermap.desktop.process.events.MatrixNodeAddingEvent;
import com.supermap.desktop.process.events.MatrixNodeAddingListener;
import com.supermap.desktop.process.events.MatrixNodeRemovedEvent;
import com.supermap.desktop.process.events.MatrixNodeRemovedListener;
import com.supermap.desktop.process.events.MatrixNodeRemovingEvent;
import com.supermap.desktop.process.events.MatrixNodeRemovingListener;
import com.supermap.desktop.process.events.RelationAddedEvent;
import com.supermap.desktop.process.events.RelationAddedListener;
import com.supermap.desktop.process.events.RelationRemovedEvent;
import com.supermap.desktop.process.events.RelationRemovedListener;
import com.supermap.desktop.process.events.RelationRemovingEvent;
import com.supermap.desktop.process.events.RelationRemovingListener;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xie on 2017/3/13.
 * <pre>NodeMatrix store your node info(IRelation),
 * EveryThing you like can be a node<pre/>
 */
public class NodeMatrix<T extends Object> {

	private Vector<T> nodes = new Vector();
	private Vector<Map<T, ArrayList<IRelation<T>>>> matrix = new Vector<>();
	private EventListenerList listenerList = new EventListenerList();

	public NodeMatrix() {

	}

	/**
	 * Add node to nodes
	 *
	 * @param node
	 */
	public synchronized void addNode(T node) {
		Objects.requireNonNull(node);

		if (!this.nodes.contains(node)) {
			MatrixNodeAddingEvent<T> addingEvent = new MatrixNodeAddingEvent<T>(this, node, false);
			fireMatrixNodeAdding(addingEvent);

			if (!addingEvent.isCancel()) {
				this.nodes.add(node);
				this.matrix.add(new ConcurrentHashMap<T, ArrayList<IRelation<T>>>());
				fireMatrixNodeAdded(new MatrixNodeAddedEvent<T>(this, node));
			}
		}
	}

	/**
	 * Remove node from matrix;
	 *
	 * @param node
	 * @return If node remove success return true,
	 * else return false;
	 */
	public synchronized void removeNode(T node) {
		validateNode(node);

		MatrixNodeRemovingEvent<T> removingEvent = new MatrixNodeRemovingEvent<>(this, node, false);
		fireMatrixNodeRemoving(removingEvent);

		if (removingEvent.isCancel()) {
			return;
		}

		for (int i = 0, size = this.matrix.size(); i < size; i++) {
			Map<T, ArrayList<IRelation<T>>> map = this.matrix.get(i);

			if (map.containsKey(node)) {
				ArrayList<IRelation<T>> relationCollection = map.get(node);
				for (int j = relationCollection.size() - 1; j >= 0; j--) {
					removeRelation(relationCollection.get(j));
				}
				map.remove(node);
			}
		}

		// remove values
		int index = this.nodes.indexOf(node);
		Map<T, ArrayList<IRelation<T>>> relations = this.matrix.get(index);
		for (Map.Entry<T, ArrayList<IRelation<T>>> entry :
				relations.entrySet()) {
			ArrayList<IRelation<T>> relationCollection = entry.getValue();
			for (int j = relationCollection.size(); j > 0; j--) {
				removeRelation(relationCollection.get(j));
			}
		}

		// remove node
		this.matrix.get(index).clear();
		this.matrix.remove(index);
		this.nodes.remove(index);
		fireMatrixNodeRemoved(new MatrixNodeRemovedEvent<T>(this, node));
	}

	/**
	 * Get free nodes exiting in matrix;
	 *
	 * @return
	 */
	public synchronized Vector<T> getFreeNodes() {
		Vector<T> freeNodes = new Vector<>();

		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			T node = this.nodes.get(i);

			if (!isRelatedFormSomeone(node) && !isRelateToAnyone(node)) {
				freeNodes.add(node);
			}
		}
		return freeNodes;
	}

	/**
	 * Returns <tt>true<tt/> if the specified node relates to any other node,<tt>false</tt> otherwise.
	 *
	 * @param node
	 * @return
	 */
	public synchronized boolean isRelateToAnyone(T node) {
		validateNode(node);

		int index = this.nodes.indexOf(node);
		return this.matrix.get(index).size() > 0;
	}

	/**
	 * Returns <tt>true<tt/> if the specified node is related from some other nodes,<tt>false</tt> otherwise.
	 *
	 * @param node
	 * @return
	 */
	public synchronized boolean isRelatedFormSomeone(T node) {
		validateNode(node);
		boolean ret = false;

		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			if (isRelateTo(this.nodes.get(i), node)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Returns <tt>true</tt> if the specified fromNode relates to the specified toNode,<tt>false</tt> otherwise.
	 *
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	public synchronized boolean isRelateTo(T fromNode, T toNode) {
		validateNode(fromNode);
		validateNode(toNode);

		int fromIndex = this.nodes.indexOf(fromNode);
		Map<T, ArrayList<IRelation<T>>> map = this.matrix.get(fromIndex);
		return map.containsKey(toNode);
	}

	private void validateNode(T node) {
		Objects.requireNonNull(node);
		if (!this.nodes.contains(node)) {
			throw new UnsupportedOperationException("Node not exits");
		}
	}

	/**
	 * If node has not previous or next node return true,else return false;
	 *
	 * @param node
	 * @return
	 */
	public synchronized boolean isFreeNode(T node) {
		return !isRelateToAnyone(node) && !isRelatedFormSomeone(node);
	}

	public synchronized boolean isLeadingNode(T node) {
		return isLeadingNode(node, false);
	}

	public synchronized boolean isLeadingNode(T node, boolean exceptFreeNodes) {
		validateNode(node);
		return exceptFreeNodes ? !isRelatedFormSomeone(node) && isRelateToAnyone(node) : !isRelatedFormSomeone(node);
	}

	/**
	 * 获得前一节点
	 * Get node's previous nodes
	 *
	 * @param node
	 * @return
	 */
	public synchronized Vector<T> getFromNodes(T node) {
		validateNode(node);

		Vector<T> fromNodes = new Vector<>();

		for (int i = 0, size = this.matrix.size(); i < size; i++) {
			Map<T, ArrayList<IRelation<T>>> map = this.matrix.get(i);
			if (map.containsKey(node)) {
				fromNodes.add(this.nodes.get(i));
			}
		}
		return fromNodes;
	}

	/**
	 * Get node's previous nodes
	 *
	 * @param node
	 * @return If null menu that no such node
	 */
	public synchronized Vector<T> getToNodes(T node) {
		validateNode(node);

		Vector<T> toNodes = new Vector<>();

		Map<T, ArrayList<IRelation<T>>> map = this.matrix.get(this.nodes.indexOf(node));
		toNodes.addAll(map.keySet());
		return toNodes;
	}

	/**
	 * Get all leading nodes
	 *
	 * @return
	 */
	public synchronized Vector<T> getLeadingNodes() {
		return getLeadingNodes(false);
	}

	/**
	 * Get all leading nodes
	 *
	 * @return
	 */
	public synchronized Vector<T> getLeadingNodes(boolean exceptFreeNodes) {
		Vector<T> leadingNodes = new Vector<>();

		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			T node = this.nodes.get(i);

			if (!isRelatedFormSomeone(node)) {
				if (exceptFreeNodes) {
					if (isRelateToAnyone(node)) {
						leadingNodes.add(node);
					}
				} else {
					leadingNodes.add(node);
				}
			}
		}
		return leadingNodes;
	}

	/**
	 * Get all end nodes
	 *
	 * @return
	 */
	public synchronized Vector<T> getEndNodes() {
		return getEndNodes(false);
	}

	/**
	 * Get all end nodes
	 *
	 * @return
	 */
	public synchronized Vector<T> getEndNodes(boolean exceptFreeNodes) {
		Vector<T> endNodes = new Vector<>();

		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			T node = this.nodes.get(i);

			if (!isRelateToAnyone(node)) {
				if (exceptFreeNodes) {
					if (isRelatedFormSomeone(node)) {
						endNodes.add(node);
					}
				} else {
					endNodes.add(node);
				}
			}
		}
		return endNodes;
	}


	public synchronized boolean contains(T node) {
		return this.nodes.contains(node);
	}

	/**
	 * Add relation.
	 *
	 * @param relation
	 */
	public synchronized void addRelation(IRelation<T> relation) {
		if (relation == null) {
			throw new IllegalArgumentException("Relation can not be null.");
		}

		validateNode(relation.getFrom());
		validateNode(relation.getTo());

		try {
			ArrayList<IRelation<T>> relationCollection = this.matrix.get(this.nodes.indexOf(relation.getFrom())).get(relation.getTo());
			if (relationCollection != null) {
				relationCollection.add(relation);
			} else {
				ArrayList<IRelation<T>> iRelations = new ArrayList<>();
				iRelations.add(relation);
				this.matrix.get(this.nodes.indexOf(relation.getFrom())).put(relation.getTo(), iRelations);
			}
			fireRelationAdded(new RelationAddedEvent<T>(this, relation));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * Remove formNode and toNode's relation
	 *
	 * @param fromNode
	 * @param toNode
	 */
	public synchronized void removeRelation(T fromNode, T toNode) {
		validateNode(fromNode);
		validateNode(toNode);

		Map<T, ArrayList<IRelation<T>>> relations = this.matrix.get(this.nodes.indexOf(fromNode));
		if (relations.containsKey(toNode)) {
			ArrayList<IRelation<T>> currentRelations = relations.get(toNode);
			for (IRelation<T> relation : currentRelations) {
				fireRelationRemoving(new RelationRemovingEvent<T>(this, relation));
				clearRelation(relation);
			}
			relations.remove(toNode);
			fireRelationRemoved(new RelationRemovedEvent<T>(this, fromNode, toNode));
		}
	}

	public synchronized void removeRelation(IRelation<T> relation) {
		if (relation == null) {
			return;
		}

		if (containsRelation(relation)) {
			T from = relation.getFrom();
			T to = relation.getTo();

			Map<T, ArrayList<IRelation<T>>> relations = this.matrix.get(this.nodes.indexOf(from));
			fireRelationRemoving(new RelationRemovingEvent<T>(this, relation));
			clearRelation(relation);
			ArrayList<IRelation<T>> iRelations = relations.get(to);
			iRelations.remove(relation);
			if (iRelations.size() == 0) {
				relations.remove(to);
			}
			fireRelationRemoved(new RelationRemovedEvent<T>(this, from, to));
		}
	}

	public synchronized boolean containsRelation(IRelation<T> relation) {
		if (relation == null) {
			return false;
		}

		if (!this.nodes.contains(relation.getFrom()) || !this.nodes.contains(relation.getTo())) {
			return false;
		}

		T from = relation.getFrom();
		T to = relation.getTo();

		Map<T, ArrayList<IRelation<T>>> relations = this.matrix.get(this.nodes.indexOf(from));
		return relations.get(to).contains(relation);
	}

	public synchronized ArrayList<IRelation<T>> getRelations(T fromNode, T toNode) {
		validateNode(fromNode);
		validateNode(toNode);

		Map<T, ArrayList<IRelation<T>>> map = this.matrix.get(this.nodes.indexOf(fromNode));
		return map.containsKey(toNode) ? map.get(toNode) : null;
	}

	public synchronized int getRelationCount() {
		int count = 0;

		for (Map<T, ArrayList<IRelation<T>>> map : this.matrix) {
			for (ArrayList<IRelation<T>> iRelations : map.values()) {
				count += iRelations.size();
			}
		}
		return count;
	}

	public synchronized Vector<T> getNodes() {
		Vector<T> nodes = new Vector<>();
		nodes.addAll(this.nodes);
		return nodes;
	}

	public synchronized Vector<IRelation<T>> getRelations() {
		Vector<IRelation<T>> relations = new Vector<>();

		for (Map<T, ArrayList<IRelation<T>>> tArrayListMap : this.matrix) {
			for (ArrayList<IRelation<T>> iRelations : tArrayListMap.values()) {
				relations.addAll(iRelations);
			}
		}
		return relations;
	}

	public synchronized int getCount() {
		return this.nodes.size();
	}

	private void clearRelation(IRelation relation) {
		if (relation != null) {
			relation.clear();
		}
	}

	public void addMatrixNodeAddingListener(MatrixNodeAddingListener<T> listener) {
		this.listenerList.add(MatrixNodeAddingListener.class, listener);
	}

	private void removeMatrixNodeAddingListener(MatrixNodeAddingListener<T> listener) {
		this.listenerList.remove(MatrixNodeAddingListener.class, listener);
	}

	public void addMatrixNodeAddedListener(MatrixNodeAddedListener<T> listener) {
		this.listenerList.add(MatrixNodeAddedListener.class, listener);
	}

	public void removeMatrixNodeAddedListener(MatrixNodeAddedListener<T> listener) {
		this.listenerList.remove(MatrixNodeAddedListener.class, listener);
	}

	public void addMatrixNodeRemovingListener(MatrixNodeRemovingListener<T> listener) {
		this.listenerList.add(MatrixNodeRemovingListener.class, listener);
	}

	public void removeMatrixNodeRemovingListener(MatrixNodeRemovingListener<T> listener) {
		this.listenerList.remove(MatrixNodeRemovingListener.class, listener);
	}

	public void addMatrixNodeRemovedListener(MatrixNodeRemovedListener<T> listener) {
		this.listenerList.add(MatrixNodeRemovedListener.class, listener);
	}

	public void removeMatrixNodeRemovedListener(MatrixNodeRemovedListener<T> listener) {
		this.listenerList.remove(MatrixNodeRemovedListener.class, listener);
	}

	public void addRelationAddedListener(RelationAddedListener<T> listener) {
		this.listenerList.add(RelationAddedListener.class, listener);
	}

	public void removeRelationAddedListener(RelationAddedListener<T> listener) {
		this.listenerList.remove(RelationAddedListener.class, listener);
	}

	public void addRelationRemovingListener(RelationRemovingListener<T> listener) {
		this.listenerList.add(RelationRemovingListener.class, listener);
	}

	public void removeRelationRemovingListener(RelationRemovingListener<T> listener) {
		this.listenerList.remove(RelationRemovingListener.class, listener);
	}

	public void addRelationRemovedListener(RelationRemovedListener<T> listener) {
		this.listenerList.add(RelationRemovedListener.class, listener);
	}

	public void removeRelationRemovedListener(RelationRemovedListener<T> listener) {
		this.listenerList.remove(RelationRemovedListener.class, listener);
	}

	protected void fireMatrixNodeAdding(MatrixNodeAddingEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == MatrixNodeAddingListener.class) {
				((MatrixNodeAddingListener<T>) listeners[i + 1]).matrixNodeAdding(e);
			}
		}
	}

	protected void fireMatrixNodeAdded(MatrixNodeAddedEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == MatrixNodeAddedListener.class) {
				((MatrixNodeAddedListener<T>) listeners[i + 1]).matrixNodeAdded(e);
			}
		}
	}

	protected void fireMatrixNodeRemoving(MatrixNodeRemovingEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == MatrixNodeRemovingListener.class) {
				((MatrixNodeRemovingListener<T>) listeners[i + 1]).matrixNodeRemoving(e);
			}
		}
	}

	protected void fireMatrixNodeRemoved(MatrixNodeRemovedEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == MatrixNodeRemovedListener.class) {
				((MatrixNodeRemovedListener<T>) listeners[i + 1]).matrixNodeRemoved(e);
			}
		}
	}

	protected void fireRelationAdded(RelationAddedEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == RelationAddedListener.class) {
				((RelationAddedListener<T>) listeners[i + 1]).relationAdded(e);
			}
		}
	}

	protected void fireRelationRemoving(RelationRemovingEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == RelationRemovingListener.class) {
				((RelationRemovingListener<T>) listeners[i + 1]).relaitonRemoving(e);
			}
		}
	}

	protected void fireRelationRemoved(RelationRemovedEvent<T> e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i = i - 2) {
			if (listeners[i] == RelationRemovedListener.class) {
				((RelationRemovedListener<T>) listeners[i + 1]).relationRemoved(e);
			}
		}
	}
}
