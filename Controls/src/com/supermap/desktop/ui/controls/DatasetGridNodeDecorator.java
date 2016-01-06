package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.DatasetGrid;

/**
 * 栅格数据集节点装饰器
 * @author xuzw
 *
 */
class DatasetGridNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.DATASET_GRID)){
			DatasetGrid datasetGrid = (DatasetGrid) data.getData();
			label.setText(datasetGrid.getName());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(
					InternalImageIconFactory.DT_GRID.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
