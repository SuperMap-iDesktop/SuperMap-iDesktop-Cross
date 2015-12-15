package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.mapping.Layer;
import com.supermap.mapping.ThemeGridRangeItem;
import com.supermap.mapping.ThemeGridUniqueItem;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeRangeItem;
import com.supermap.mapping.ThemeUniqueItem;
import com.supermap.realspace.Feature3D;
import com.supermap.realspace.Feature3Ds;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.ScreenLayer3D;
import com.supermap.realspace.TerrainLayer;
import com.supermap.realspace.Theme3DRangeItem;
import com.supermap.realspace.Theme3DUniqueItem;

/**
 * 图层是否可见节点装饰器
 * 
 * @author hmily
 *
 */
class VisibleDecorator implements TreeNodeDecorator {
	@Override
	public void decorate(JLabel label, TreeNodeData data) {
		boolean isVisible = false;
		ImageIcon icon = (ImageIcon) label.getIcon();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		Object obj = data.getData();
		isVisible = LayersTreeUtilties.isTreeNodeDataVisible(obj);

		if (isVisible) {
			graphics.drawImage(InternalImageIconFactory.VISIBLE.getImage(), 0, 0, label);
		} else {
			graphics.drawImage(InternalImageIconFactory.INVISIBLE.getImage(), 0, 0, label);
		}
		icon.setImage(bufferedImage);
	}
}
