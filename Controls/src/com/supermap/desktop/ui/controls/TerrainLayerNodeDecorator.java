package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.realspace.TerrainLayer;

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
