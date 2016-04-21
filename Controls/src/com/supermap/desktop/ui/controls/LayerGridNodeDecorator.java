package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.mapping.Layer;
import com.supermap.realspace.Layer3DDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class LayerGridNodeDecorator implements TreeNodeDecorator{

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.LAYER_GRID)) {
			Object layer = data.getData();
			Dataset dataset = null;
			if (layer instanceof Layer) {
				Layer layer1 = (Layer) layer;
				label.setText(layer1.getCaption());
				dataset = layer1.getDataset();
			} else if (layer instanceof Layer3DDataset) {
				Layer3DDataset layer3D = (Layer3DDataset) layer;
				label.setText(layer3D.getCaption());
				dataset = layer3D.getDataset();
			}

			if (dataset != null && dataset instanceof DatasetGrid) {
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(
						IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
						BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				graphics.drawImage(
						InternalImageIconFactory.DT_GRID.getImage(), 0,
						0, label);
				icon.setImage(bufferedImage);
			}
		}
	}

}
