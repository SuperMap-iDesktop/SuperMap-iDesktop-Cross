package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetImageCollection;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class LayerImageCollectionNodeDecorator implements TreeNodeDecorator{

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.DATASET_IMAGE_COLLECTION)) {
			Layer layer = (Layer) data.getData();
			label.setText(layer.getCaption());
			Dataset dataset = layer.getDataset();
			if (dataset instanceof DatasetImageCollection) {
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(
						IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
						BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				graphics.drawImage(
						InternalImageIconFactory.DT_IMAGECOLLECTION.getImage(), 0,
						0, label);
				icon.setImage(bufferedImage);
			}
		}
	}

}
