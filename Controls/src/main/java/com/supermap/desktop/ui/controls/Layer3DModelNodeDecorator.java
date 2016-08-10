package com.supermap.desktop.ui.controls;

import com.supermap.realspace.Layer3DModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
