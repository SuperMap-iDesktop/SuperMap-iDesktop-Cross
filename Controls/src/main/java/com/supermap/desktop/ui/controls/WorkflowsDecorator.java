package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.WorkspaceTreeManagerUIUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJT
 */
public class WorkflowsDecorator implements TreeNodeDecorator {
	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.WORKFLOWS)) {
			label.setText(ControlsProperties.getString("String_Workflows"));
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(new ImageIcon(ControlsResources.getResourceURL(WorkspaceTreeManagerUIUtilities.WorkFlowsIconPath)).getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}
}
