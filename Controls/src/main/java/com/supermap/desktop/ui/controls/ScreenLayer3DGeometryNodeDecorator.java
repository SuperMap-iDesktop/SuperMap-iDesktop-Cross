package com.supermap.desktop.ui.controls;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 三维屏幕图层几何对象节点装饰器
 * @author xuzw
 *
 */
class ScreenLayer3DGeometryNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.SCREENLAYER3D_GEOMETRY_TAG)){
			Object internalData = data.getData();
			if(internalData instanceof String){
				String tag = (String)internalData;
				label.setText(tag);
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
						IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				graphics.drawImage(
						InternalImageIconFactory.SCREEN_LAYER_GEOMETRY.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			}
		}
	}

}
