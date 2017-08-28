package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by lixiaoyao on 2017/8/23.
 */
public class LayerGridAggregationNodeDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.GRID_AGGREGATION)) {
			Object layer = data.getData();
			Dataset dataset = null;
			if (layer instanceof Layer) {
				Layer layer1 = (Layer) layer;
				label.setText(layer1.getCaption());
				dataset = layer1.getDataset();
			}

			if (dataset != null && dataset instanceof DatasetVector) {
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(
						IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
						BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				graphics.drawImage(
						InternalImageIconFactory.GRID_AGGREGATION.getImage(), 0,
						0, label);
				icon.setImage(bufferedImage);
			}
		}
	}
}
