package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 布局名称节点装饰器
 * @author xuzw
 *
 */
class LayoutNameNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.LAYOUT_NAME)){
			String name = (String) data.getData();
			label.setText(name);
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(
					InternalImageIconFactory.LAYOUT.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
