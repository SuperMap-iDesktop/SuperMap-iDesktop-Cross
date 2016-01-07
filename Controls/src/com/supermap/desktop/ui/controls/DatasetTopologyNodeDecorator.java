package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.DatasetTopology;

/**
 * 拓扑数据集节点装饰器
 * @author xuzw
 *
 */
class DatasetTopologyNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.DATASET_TOPOLOGY)) {
			DatasetTopology datasetTopology = (DatasetTopology) data.getData();
			label.setText(datasetTopology.getName());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.DT_TOPOLOGY.getImage(),
					0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
