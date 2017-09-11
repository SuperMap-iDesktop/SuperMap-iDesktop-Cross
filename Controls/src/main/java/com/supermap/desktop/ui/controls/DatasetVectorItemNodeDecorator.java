package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by xie on 2017/7/20.
 */
public class DatasetVectorItemNodeDecorator extends DatasetVectorNodeDecorator {
	public DatasetVectorItemNodeDecorator(NodeDataType type) {
		super(type);
	}

	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(type)) {
			WorkspaceTree.CollectionInfo collectionInfo = (WorkspaceTree.CollectionInfo) data.getData();
			label.setText(collectionInfo.getCollectionDatasetInfo().getDatasetName());
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			DatasetType type = collectionInfo.getDatasetType();
			String path = CommonToolkit.DatasetImageWrap.getImageIconPath(type);
			if (StringUtilities.isNullOrEmpty(path)){
				path = "/controlsresources/WorkspaceManager/Dataset/Image_DatasetImage_temp.png";
			}
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
