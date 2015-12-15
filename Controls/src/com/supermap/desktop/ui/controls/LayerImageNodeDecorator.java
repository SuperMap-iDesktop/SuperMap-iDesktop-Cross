package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.mapping.Layer;

 class LayerImageNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.LAYER_IMAGE)) {
			Layer layer = (Layer) data.getData();
			label.setText(layer.getCaption());
			Dataset dataset = layer.getDataset();
			if (dataset instanceof DatasetImage) {
				DatasetImage datasetImage = (DatasetImage) dataset;
				DatasetType type = datasetImage.getType();
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(
						IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
						BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				if (type.equals(DatasetType.IMAGE)) {
					graphics.drawImage(
							InternalImageIconFactory.DT_IMAGE
							.getImage(), 0, 0, label);
				} else if (type.equals(DatasetType.WCS)) {
					graphics.drawImage(
							InternalImageIconFactory.DT_WCS.getImage(),
							0, 0, label);
				} else if (type.equals(DatasetType.WMS)) {
					graphics.drawImage(
							InternalImageIconFactory.DT_WMS.getImage(),
							0, 0, label);
				}
				icon.setImage(bufferedImage);
			}
		}
	}
	
}
