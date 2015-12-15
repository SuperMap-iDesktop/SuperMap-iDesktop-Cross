package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.realspace.Layer3DKML;

/**
 * KML图层节点装饰器
 * @author xuzw
 *
 */
class Layer3DKMLNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.LAYER3D_KML)){
			Layer3DKML layer3DKML = (Layer3DKML) data.getData();
			label.setText(layer3DKML.getCaption());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(
					IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.LAYER3DKML
					.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
