package com.supermap.desktop.ui.controls;

import javax.swing.tree.DefaultMutableTreeNode;

public class HitTestInfo {
	private DefaultMutableTreeNode defaultMutableTreeNode = null;
	private TreeNodeData treeNodeData = null;
	private int index;
	private int indexCount;
	private HitTestIconType iconType;
	
	HitTestInfo(DefaultMutableTreeNode node, int index){
		this(node, index, HitTestIconType.NONE);
	}
	
	HitTestInfo(DefaultMutableTreeNode node, int index, HitTestIconType iconType){
		this.defaultMutableTreeNode = node;
		treeNodeData = (TreeNodeData)node.getUserObject();
		this.index = index;
		this.iconType = iconType;
	}
	
	
	/**
	 * 获取当前鼠标点击T所对应的树节点
	 * @return 
	 */
	public DefaultMutableTreeNode getDefaultMutableTreeNode(){
		return this.defaultMutableTreeNode;
	}
	
	
	/**
	 * 获取当前树节点对应的模型数据,Layer,ThemeGraghItem,ThemeRangeItem等
	 * @return
	 */
	public TreeNodeData getData(){
		return this.treeNodeData;
	}
	
	/**
	 * 获取当前鼠标点击的操作索引
	 * @return
	 */
	public int getIndex(){
		return index;
	}
	
	public HitTestIconType getIconType(){
		return iconType;
	}
	
	int getIndexCount(){
		return indexCount;
	}
	
	void setIndexCount(int count){
		this.indexCount = count;
	}
	
}
