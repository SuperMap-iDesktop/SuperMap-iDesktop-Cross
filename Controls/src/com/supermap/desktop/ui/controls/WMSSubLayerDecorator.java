package com.supermap.desktop.ui.controls;

import javax.swing.JLabel;

/**
 * WMS子图层节点装饰器
 * @author xuzw
 *
 */
class WMSSubLayerDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.WMSSUB_LAYER)){
			String name = (String) data.getData();
			label.setText(name);
		}
	}

}
