package com.supermap.desktop.ui.controls;

import com.supermap.realspace.TerrainLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 地形图层节点装饰器
 * @author xuzw
 *
 */
class TerrainLayerNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.TERRAIN_LAYER)){
			TerrainLayer terrainLayer = (TerrainLayer) data.getData();
			label.setText(terrainLayer.getCaption());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(
					IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.TERRAIN_LAYER
					.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
