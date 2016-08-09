package com.supermap.desktop.ui.controls;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 栅格数据集节点装饰器
 * 
 * @author huangbiao
 *
 */
class DatasetRelationshipNodeDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.DATASET_RELATION_SHIP)) {
			label.setText("");
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.DT_RELATIONSHIP.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
