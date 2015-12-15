package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.Workspace;
import com.supermap.desktop.controls.ControlsProperties;

/**
 * 工作空间节点装饰器
 * 
 * @author xuzw
 *
 */
class WorkspaceNodeDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.WORKSPACE)) {
			Workspace workspace = (Workspace) data.getData();
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.WORKSPACE.getImage(), 0, 0, label);
			if ("UntitledWorkspace".equalsIgnoreCase(workspace.getCaption())) {
				label.setText(ControlsProperties.getString(ControlsProperties.WorkspaceNodeDefaultName));
			} else {
				label.setText(workspace.getCaption());
			}
			icon.setImage(bufferedImage);
		}
	}

}
