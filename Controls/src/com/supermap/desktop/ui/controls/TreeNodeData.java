package com.supermap.desktop.ui.controls;

import com.supermap.mapping.Layer;
import com.supermap.realspace.Layer3D;

/**
 * 树节点数据
 * @author xuzw
 *
 */
public class TreeNodeData {
	private Object data;
	private NodeDataType type;
	private Layer layerParent;
	private Layer3D layer3dParent;
	public TreeNodeData(Object data, NodeDataType type){
		this.data = data;
		this.type = type;
	}

	//该构造函数仅供LayersTree构造专题图子项时使用
	TreeNodeData(Object data, NodeDataType type, Layer parentLayer){
		this.data = data;
		this.type = type;
		this.layerParent = parentLayer;
	}
	
	//该方法仅供LayersTree节点对应的Decorator进行decorate时使用
	Layer getParentLayer(){
		return this.layerParent;
	}
	//该构造函数仅供Layer3DsTree构造专题图子项时使用
	TreeNodeData(Object data, NodeDataType type, Layer3D parentLayer3D){
		this.data = data;
		this.type = type;
		this.layer3dParent = parentLayer3D;
	}
	
	//该方法仅供Layer3DsTree节点对应的Decorator进行decorate时使用
	Layer3D getParentLayer3D(){
		return this.layer3dParent;
	}
	
	
	
	public Object getData(){
		return this.data;
	}
	
	public NodeDataType getType(){
		return this.type;
	}
	
}
