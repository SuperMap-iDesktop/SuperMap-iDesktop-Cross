package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.desktop.controls.ControlsProperties;

/**
 * 数据源集合节点装饰器
 * 
 * @author xuzw
 *
 */
class DatasourcesNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.DATASOURCES)) {
			ImageIcon icon = (ImageIcon) label.getIcon();
			label.setText(ControlsProperties.getString(ControlsProperties.DatasourcesNodeName));
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.DATASOURCES.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
