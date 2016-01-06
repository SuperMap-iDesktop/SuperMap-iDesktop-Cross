package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;

/**
 * 矢量数据集节点装饰器
 * 
 * @author xuzw
 *
 */
class DatasetVectorNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.DATASET_VECTOR)) {
			DatasetVector datasetVector = (DatasetVector) data.getData();
			label.setText(datasetVector.getName());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			DatasetType type = datasetVector.getType();
			if (type.equals(DatasetType.POINT)) {
				graphics.drawImage(InternalImageIconFactory.DT_POINT.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.CAD)) {
				graphics.drawImage(InternalImageIconFactory.DT_CAD.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.LINE)) {
				graphics.drawImage(InternalImageIconFactory.DT_LINE.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.LINEM)) {
				graphics.drawImage(InternalImageIconFactory.DT_LINEM.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.LINKTABLE)) {
				graphics.drawImage(InternalImageIconFactory.DT_LINKTABLE.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.NETWORK)) {
				graphics.drawImage(InternalImageIconFactory.DT_NETWORK.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.NETWORK3D)) {
				graphics.drawImage(InternalImageIconFactory.DT_NETWORK3D.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.REGION)) {
				graphics.drawImage(InternalImageIconFactory.DT_REGION.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.TABULAR)) {
				graphics.drawImage(InternalImageIconFactory.DT_TABULAR.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.TEXT)) {
				graphics.drawImage(InternalImageIconFactory.DT_TEXT.getImage(), 0, 0, label);
			}else if (type.equals(DatasetType.POINT3D)) {
				graphics.drawImage(InternalImageIconFactory.DT_POINT3D.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.LINE3D)) {
				graphics.drawImage(InternalImageIconFactory.DT_LINE3D.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.REGION3D)) {
				graphics.drawImage(InternalImageIconFactory.DT_REGION3D.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.PARAMETRICLINE)) {
				graphics.drawImage(InternalImageIconFactory.DT_PARAMETRICLINE.getImage(), 0, 0, label);
			} else if (type.equals(DatasetType.PARAMETRICREGION)) {
				graphics.drawImage(InternalImageIconFactory.DT_PARAMETRICREGION.getImage(), 0, 0, label);
			} else {
				graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN.getImage(), 0, 0, label);
			}

			icon.setImage(bufferedImage);
		}
	}

}
