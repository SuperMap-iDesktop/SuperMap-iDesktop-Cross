package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.WorkspaceTreeManagerUIUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 资源节点装饰器
 * 
 * @author xuzw
 *
 */
class ResourcesNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.RESOURCES)) {
			label.setText(ControlsProperties.getString(ControlsProperties.ResourcesNodeName));
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(new ImageIcon(ControlsResources.getResourceURL(WorkspaceTreeManagerUIUtilities.SymbolIconPath)).getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
