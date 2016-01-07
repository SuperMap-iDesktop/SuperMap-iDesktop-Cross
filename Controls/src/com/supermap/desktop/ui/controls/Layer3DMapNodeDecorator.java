package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.realspace.Layer3DMap;

/**
 * 三维地图图层节点装饰器
 * @author xuzw
 *
 */
class Layer3DMapNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.LAYER3D_MAP)){
			Layer3DMap layer3DMap = (Layer3DMap) data.getData();
			label.setText(layer3DMap.getCaption());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(
					IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.LAYER3DMAP
					.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
