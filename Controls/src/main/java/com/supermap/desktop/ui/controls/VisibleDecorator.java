package com.supermap.desktop.ui.controls;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图层是否可见节点装饰器
 * 
 * @author hmily
 *
 */
class VisibleDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		boolean isVisible = false;
		ImageIcon icon = (ImageIcon) label.getIcon();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		Object obj = data.getData();
		isVisible = LayersTreeUtilties.isTreeNodeDataVisible(obj);

		if (isVisible) {
			graphics.drawImage(InternalImageIconFactory.VISIBLE.getImage(), 0, 0, label);
		} else {
			graphics.drawImage(InternalImageIconFactory.INVISIBLE.getImage(), 0, 0, label);
		}
		icon.setImage(bufferedImage);
	}
}
