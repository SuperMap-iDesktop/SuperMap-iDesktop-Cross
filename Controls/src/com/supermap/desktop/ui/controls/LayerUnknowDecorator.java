package com.supermap.desktop.ui.controls;

import com.supermap.realspace.Layer3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public class LayerUnknowDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getData() instanceof Layer3D) {
			Layer3D layer = (Layer3D) data.getData();
			try {
				label.setText(layer.getCaption());
			} catch (Exception e) {
				// layer为空
				return;
			}
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}
}
