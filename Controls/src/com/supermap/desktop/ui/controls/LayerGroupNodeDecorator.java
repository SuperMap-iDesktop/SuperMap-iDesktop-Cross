package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.mapping.LayerGroup;

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
