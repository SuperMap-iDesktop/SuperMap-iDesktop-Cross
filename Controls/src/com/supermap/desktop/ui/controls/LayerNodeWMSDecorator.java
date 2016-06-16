package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public class LayerNodeWMSDecorator implements TreeNodeDecorator {
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType() == NodeDataType.LAYER_WMS && !((Layer) data.getData()).isDisposed()) {
			Layer layer = (Layer) data.getData();
			try {
				label.setText(layer.getCaption());
			} catch (Exception e) {
				// layer为空
				return;
			}
			Dataset dataset = layer.getDataset();
			if (dataset == null) {
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			} else {
				DatasetType type = dataset.getType();
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();

				if (type == DatasetType.WMS) {

					graphics.drawImage(getImage(DatasetType.WMS), 0, 0, label);

				}
				icon.setImage(bufferedImage);
			}

		}
	}

	private Image getImage(DatasetType cad) {
		return new ImageIcon(LayerNodeDecorator.class.getResource(CommonToolkit.DatasetImageWrap.getImageIconPath(cad))).getImage();
	}
}
