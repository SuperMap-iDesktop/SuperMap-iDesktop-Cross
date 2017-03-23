package com.supermap.desktop.process.core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/13.
 * <pre>NodeMatrix store your node info(INodeConstraint),
 * EveryThing you like can be a node<pre/>
 */
public class NodeMatrix {

    private volatile ConcurrentHashMap<Object, SecondNodeMatrix> nodeMatrix = new ConcurrentHashMap();

    private volatile CopyOnWriteArrayList nodeList = new CopyOnWriteArrayList();


    public NodeMatrix() {

    }

    /**
     * Add node to nodeList
     *
     * @param node
     */
    public synchronized void addNode(Object node) {
        if (!this.nodeList.contains(node)) {
            this.nodeList.add(node);
        }
    }

    /**
     * Remove node and next node's constraint
     *
     * @param node
     * @param nextNode
     * @return If constraint remove success return true,
     * else return false;
     */
    public synchronized boolean removeNodeConstraint(Object node, Object nextNode) throws NodeException {
        boolean hasRemoved = false;
        if (!nodeList.contains(node) || !nodeList.contains(nextNode)) {
            throw new NodeException("Node not exits");
        }
        if (nodeMatrix.containsKey(node)) {
            SecondNodeMatrix secondNodeMatrix = nodeMatrix.get(node);
            if (secondNodeMatrix.vector.containsKey(nextNode)) {
                Object object = secondNodeMatrix.vector.get(nextNode);
                object = null;
                hasRemoved = true;
            }
        }
        return hasRemoved;
    }

    /**
     * Remove node from matrix;
     *
     * @param node
     * @return If node remove success return true,
     * else return false;
     */
    public synchronized boolean removeNode(Object node) throws NodeException {
        boolean result = false;
        if (!nodeList.contains(node)) {
            throw new NodeException("Node not exits");
        }
        if (nodeMatrix.containsKey(node)) {
            nodeMatrix.remove(node);
            result = true;
        } else {
            int size = nodeList.size();
            for (int i = 0; i < size; i++) {
                if (nodeMatrix.containsKey(nodeList.get(i)) && nodeList.get(i) != node) {
                    if (nodeMatrix.get(nodeList.get(i)).vector.containsKey(node)) {
                        nodeMatrix.get(nodeList.get(i)).vector.remove(node);
                        result = true;
                    }
                }
            }
        }
        nodeList.remove(node);
        return result;
    }

    /**
     * Get single nodes exits in matrix;
     *
     * @return
     */
    public synchronized CopyOnWriteArrayList getSingleNodes() {
        CopyOnWriteArrayList result = new CopyOnWriteArrayList();
        int nodeSize = nodeList.size();
        for (int i = 0; i < nodeSize; i++) {
            if (!nodeMatrix.containsKey(nodeList.get(i))) {
                result.add(nodeList.get(i));
            }
        }
        return result;
    }

    /**
     * If node has not previous or next node return true,else return false;
     *
     * @param node
     * @return
     */
    public synchronized boolean isSingleNode(Object node) {
        boolean result = false;
        if (nodeList.contains(node) && !nodeMatrix.containsKey(node)) {
            result = true;
        }
        return result;
    }

    /**
     * Get node's previous nodes
     *
     * @param node
     * @return
     */
    public synchronized CopyOnWriteArrayList getPreNodes(Object node) throws NodeException {
        CopyOnWriteArrayList result = new CopyOnWriteArrayList();
        if (!this.nodeList.contains(node)) {
            throw new NodeException("Node note exists");
        } else {
            int nodeSize = nodeList.size();
            for (int i = 0; i < nodeSize; i++) {
                Object tempNode = nodeList.get(i);
                if (nodeMatrix.containsKey(tempNode)) {
                    Iterator<Map.Entry<Object, INodeConstraint>> iterator = nodeMatrix.get(tempNode).vector.entrySet().iterator();
                    while (iterator.hasNext()) {
                        if (node == iterator.next().getKey()) {
                            result.add(tempNode);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get node's previous nodes
     *
     * @param node
     * @return If null menu that no such node
     */
    public synchronized CopyOnWriteArrayList getNextNodes(Object node) throws NodeException {
        CopyOnWriteArrayList result = new CopyOnWriteArrayList();
        if (!this.nodeList.contains(node)) {
            throw new NodeException("Node note exists");
        } else if (nodeMatrix.containsKey(node)) {
            Iterator<Map.Entry<Object, INodeConstraint>> iterator = nodeMatrix.get(node).vector.entrySet().iterator();
            while (iterator.hasNext()) {
                result.add(iterator.next().getKey());
            }
        }
        return result;
    }

    /**
     * Get all start nodes
     *
     * @return
     */
    public synchronized CopyOnWriteArrayList getAllStartNodes() {
        CopyOnWriteArrayList result = new CopyOnWriteArrayList();
        int nodeSize = nodeList.size();
        for (int i = 0; i < nodeSize; i++) {
            Object tempNode = nodeList.get(i);
            if (!nodeMatrix.containsKey(tempNode)) {
                continue;
            } else {
                boolean hasNode = false;
                for (SecondNodeMatrix matrix : nodeMatrix.values()) {
                    if (matrix.vector.containsKey(tempNode)) {
                        hasNode = true;
                        break;
                    }
                }
                if (!hasNode) {
                    result.add(tempNode);
                }
            }
        }
        return result;
    }

    /**
     * Get all end nodes
     *
     * @return
     */
    public synchronized CopyOnWriteArrayList getAllEndNodes() {
        CopyOnWriteArrayList result = new CopyOnWriteArrayList();
        int nodeSize = nodeList.size();
        for (int i = 0; i < nodeSize; i++) {
            Object tempNode = nodeList.get(i);
            if (nodeMatrix.containsKey(tempNode)) {
                continue;
            } else {
                boolean hasNode = true;
                for (SecondNodeMatrix matrix : nodeMatrix.values()) {
                    if (matrix.vector.containsKey(tempNode)) {
                        hasNode = false;
                        break;
                    }
                }
                if (!hasNode) {
                    result.add(tempNode);
                }
            }
        }
        return result;
    }

    /**
     * Add constraint between two nodes
     *
     * @param node
     * @param nextNode
     * @param constraint
     * @return If node1 can add constraint with node2
     */
    public synchronized boolean addConstraint(Object node, Object nextNode, INodeConstraint constraint) {
        if (!nodeList.contains(node) || !nodeList.contains(nextNode)) {
            return false;
        }
        if (nodeMatrix.containsKey(node)) {
            nodeMatrix.get(node).vector.put(nextNode, constraint);
        } else {
            SecondNodeMatrix vectorInfo = new SecondNodeMatrix();
            vectorInfo.vector.put(nextNode, constraint);
            nodeMatrix.put(node, vectorInfo);
        }
        return true;
    }

    public synchronized CopyOnWriteArrayList listAllNodes() {
        return this.nodeList;
    }

    /**
     * Constraint store your own constraint for nodes
     *
     * @param <C>
     */
    class SecondNodeMatrix<C> {
        private volatile Hashtable<Object, C> vector = new Hashtable<>();
    }

}
