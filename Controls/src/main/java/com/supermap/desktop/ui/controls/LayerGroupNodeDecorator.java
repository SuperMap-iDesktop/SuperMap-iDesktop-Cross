package com.supermap.desktop.ui.controls;

import com.supermap.mapping.LayerGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 分组图层节点装饰器
 * @author xuzw
 *
 */
class LayerGroupNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.LAYER_GROUP)) {
			LayerGroup layerGroup = (LayerGroup)data.getData();
			label.setText(layerGroup.getCaption());

			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(
					IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(
					InternalImageIconFactory.LAYER_GROUP.getImage(), 0,
					0, label);
			icon.setImage(bufferedImage);
			
		}
	}
}
