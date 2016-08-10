package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * WMS子图层节点装饰器
 *
 * @author xuzw
 */
class WMSSubLayerDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.WMSSUB_LAYER)) {
			String name = (String) data.getData();
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(((ImageIcon) ControlsResources.getIcon(CommonToolkit.DatasetImageWrap.getImageIconPath(DatasetType.WMS))).getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
			label.setText(name);
		}
	}

}
