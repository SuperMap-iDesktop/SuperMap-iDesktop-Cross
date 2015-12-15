package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImageCollection;
import com.supermap.mapping.Layer;

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
