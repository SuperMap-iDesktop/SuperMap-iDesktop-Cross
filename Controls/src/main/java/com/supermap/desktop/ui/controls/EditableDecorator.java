package com.supermap.desktop.ui.controls;

import com.supermap.mapping.Layer;
import com.supermap.realspace.Layer3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图层是否可编辑装饰器
 * 
 * @author xuzw
 *
 */
class EditableDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (DecoratorUnities.isDecoratorShow(data.getData())) {
			return;
		}
		boolean isEidtable = false;
		ImageIcon icon = (ImageIcon) label.getIcon();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		isEidtable = getState(data.getData());
		boolean isVisible = LayersTreeUtilties.isTreeNodeDataVisible(data.getData());

		if (isEidtable && isVisible) {
			graphics.drawImage(InternalImageIconFactory.EDITABLE.getImage(), 0, 0, label);
		} else {
			graphics.drawImage(InternalImageIconFactory.UNEDITABLE.getImage(), 0, 0, label);
		}
		icon.setImage(bufferedImage);
	}

	public boolean getState(Object data) {
		boolean isEditable = false;
		if (data instanceof Layer) {
			Layer layer = (Layer) data;
			isEditable = layer.isEditable();
		} else if (data instanceof Layer3D) {
			Layer3D layer3D = (Layer3D) data;
			isEditable = layer3D.isEditable();
		}
		return isEditable;
	}

}
