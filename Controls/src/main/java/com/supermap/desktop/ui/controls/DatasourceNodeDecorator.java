package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.utilities.DatasourceImageUtilties;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * 数据源节点装饰器
 * @author xuzw
 *
 */
class DatasourceNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.DATASOURCE)){
			Datasource datasource = (Datasource) data.getData();
			label.setText(datasource.getAlias());
			ImageIcon icon = (ImageIcon) label.getIcon();
			EngineType engineType = datasource.getEngineType();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			URL resourceURL = ControlsResources.getResourceURL(DatasourceImageUtilties.getImageIconPath(engineType));
			graphics.drawImage(new ImageIcon(resourceURL).getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
