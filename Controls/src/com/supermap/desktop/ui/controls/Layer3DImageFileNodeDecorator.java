package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.realspace.Layer3DImageFile;

/**
 * 影像文件图层节点装饰器
 * @author xuzw
 *
 */
class Layer3DImageFileNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.LAYER3D_IMAGE_FILE)){
			Layer3DImageFile layer3DImageFile = (Layer3DImageFile) data.getData();
			label.setText(layer3DImageFile.getCaption());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(
					IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.LAYER3D_IMAGEFILE
					.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
