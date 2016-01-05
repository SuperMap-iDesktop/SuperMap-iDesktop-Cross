package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.realspace.Layer3DModel;

/**
 * 模型缓存图层节点装饰器
 * @author xuzw
 *
 */
class Layer3DModelNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.LAYER3D_MODEL)){
			Layer3DModel layer3DModel = (Layer3DModel) data.getData();
			label.setText(layer3DModel.getCaption());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(
					IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.LAYER3D_MODEL
					.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
