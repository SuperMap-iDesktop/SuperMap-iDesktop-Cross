package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.desktop.controls.ControlsProperties;

/**
 * 线型库节点装饰器
 * 
 * @author xuzw
 *
 */
class SymbolLineLibNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.SYMBOL_LINE_LIBRARY)) {
			label.setText(ControlsProperties.getString(ControlsProperties.SymbolLineLibNodeName));
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.SYMBOLLINELIB.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
