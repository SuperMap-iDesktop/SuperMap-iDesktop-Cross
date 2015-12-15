package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.mapping.Layer;
import com.supermap.realspace.Layer3D;

/**
 * 图层中对象是否可选择装饰器
 * 
 * @author xuzw
 *
 */
class SelectableDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (DecoratorUnities.isDecoratorShow(data.getData())) {
			return;
		}
		boolean isSelectable = false;
		ImageIcon icon = (ImageIcon) label.getIcon();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		isSelectable = getState(data.getData());
		boolean isVisible = LayersTreeUtilties.isTreeNodeDataVisible(data.getData());

		if (isSelectable && isVisible) {
			graphics.drawImage(InternalImageIconFactory.SELECTABLE.getImage(), 0, 0, label);
		} else {
			graphics.drawImage(InternalImageIconFactory.UNSELECTABLE.getImage(), 0, 0, label);
		}
		icon.setImage(bufferedImage);

	}

	public boolean getState(Object data) {
		boolean isSelectable = false;
		if (data instanceof Layer) {
			Layer layer = (Layer) data;
			isSelectable = layer.isSelectable();
		} else if (data instanceof Layer3D) {
			Layer3D layer3D = (Layer3D) data;
			isSelectable = layer3D.isSelectable();
		}
		return isSelectable;
	}

}
