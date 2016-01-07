package com.supermap.desktop.ui.controls;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 三维图层树点击信息
 * @author xuzw
 *
 */
class Layer3DsTreeHitTestInfo {
	private DefaultMutableTreeNode defaultMutableTreeNode = null;
	private Object data = null;
	private int index;

	Layer3DsTreeHitTestInfo(DefaultMutableTreeNode node, int index) {
		this.defaultMutableTreeNode = node;
		TreeNodeData userObject = (TreeNodeData) defaultMutableTreeNode
				.getUserObject();
		this.data = userObject.getData();
		this.index = index;
	}

	/**
	 * 获取当前鼠标点击所对应的树节点
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getDefaultMutableTreeNode() {
		return this.defaultMutableTreeNode;
	}

	/**
	 * 获取当前树节点对应的模型数据,Layer3D,Theme3DUniqueItem,Theme3DRangeItem等
	 * 
	 * @return
	 */
	public Object getData() {
		return this.data;
	}

	/**
	 * 获取当前鼠标点击的操作索引
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}
}
