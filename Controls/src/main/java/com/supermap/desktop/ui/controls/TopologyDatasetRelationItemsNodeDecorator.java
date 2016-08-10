package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 拓扑预处理项节点装饰器
 * @author xuzw
 *
 */
class TopologyDatasetRelationItemsNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.TOPOLOGY_DATASET_RELATIONS)){
			DatasetImage datasetImage = (DatasetImage) data.getData();
			label.setText(datasetImage.getName());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
