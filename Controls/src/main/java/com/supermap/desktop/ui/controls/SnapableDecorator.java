package com.supermap.desktop.ui.controls;

import com.supermap.mapping.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图层是否可捕捉装饰器
 * 
 * @author xuzw
 *
 */
class SnapableDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (DecoratorUnities.isDecoratorShow(data.getData())) {
			return;
		}
		boolean isSnapable = false;
		ImageIcon icon = (ImageIcon) label.getIcon();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		isSnapable = getState(data.getData());
		boolean isVisible = LayersTreeUtilties.isTreeNodeDataVisible(data.getData());

		if (isSnapable && isVisible) {
			graphics.drawImage(InternalImageIconFactory.SNAPABLE.getImage(), 0, 0, label);
		} else {
			graphics.drawImage(InternalImageIconFactory.UNSNAPABLE.getImage(), 0, 0, label);
		}
		icon.setImage(bufferedImage);
	}

	public boolean getState(Object data) {
		boolean isSnapable = false;
		if (data instanceof Layer) {
			Layer layer = (Layer) data;
			isSnapable = layer.isSnapable();
		}
		return isSnapable;
	}

}
