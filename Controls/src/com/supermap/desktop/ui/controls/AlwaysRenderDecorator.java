package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.realspace.Layer3D;

/**
 * 三维图层是否始终渲染装饰器
 * 
 * @author xuzw
 *
 */
class AlwaysRenderDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		boolean isAlwaysRender = false;
		ImageIcon icon = (ImageIcon) label.getIcon();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		Object object = data.getData();
		isAlwaysRender = getState(object);
		if (isAlwaysRender) {
			graphics.drawImage(InternalImageIconFactory.ALWAYS_RENDER.getImage(), 0, 0, label);
		} else {
			graphics.drawImage(InternalImageIconFactory.UNALWAYS_RENDER.getImage(), 0, 0, label);
		}
		icon.setImage(bufferedImage);
	}

	private boolean getState(Object obj) {
		boolean isAlwaysRender = false;
		if (obj instanceof Layer3D) {
			Layer3D layer3D = (Layer3D) obj;
			isAlwaysRender = layer3D.isAlwaysRender();
		}
		return isAlwaysRender;
	}

}
