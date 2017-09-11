package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.CommonToolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * 矢量数据集节点装饰器
 * 
 * @author xuzw
 *
 */
class DatasetVectorNodeDecorator implements TreeNodeDecorator {

	protected NodeDataType type;
	public DatasetVectorNodeDecorator(NodeDataType type){
		this.type = type;
	}

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(type)) {
			DatasetVector datasetVector = (DatasetVector) data.getData();
			label.setText(datasetVector.getName());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			DatasetType type = datasetVector.getType();
			String path = CommonToolkit.DatasetImageWrap.getImageIconPath(type);
			URL url = DatasetVectorNodeDecorator.class.getResource(path);
			try {
				graphics.drawImage(new ImageIcon(url).getImage(), 0, 0, label);
			} catch (Exception e) {
				e.printStackTrace();
			}

			icon.setImage(bufferedImage);
		}
	}

}
