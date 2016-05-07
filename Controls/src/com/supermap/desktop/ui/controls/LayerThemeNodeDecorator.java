package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.Dataset;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeType;

class LayerThemeNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		Layer layer = (Layer) data.getData();
		label.setText(layer.getCaption());
		Dataset dataset = layer.getDataset();
		if (dataset == null) {
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		} else {
			Theme theme = layer.getTheme();
			if (theme != null) {
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				ThemeType type = theme.getType();
				if (type.equals(ThemeType.CUSTOM)) {
					graphics.drawImage(InternalImageIconFactory.THEME_CUSTOM.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.DOTDENSITY)) {
					graphics.drawImage(InternalImageIconFactory.THEME_DOTDENSITY.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.GRADUATEDSYMBOL)) {
					graphics.drawImage(InternalImageIconFactory.THEME_GRADUATEDSYMBOL.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.GRAPH)) {
					graphics.drawImage(InternalImageIconFactory.THEME_GRAPH.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.GRIDRANGE)) {
					graphics.drawImage(InternalImageIconFactory.THEME_GRIDRANGE.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.GRIDUNIQUE)) {
					graphics.drawImage(InternalImageIconFactory.THEME_GRIDUNIQUE.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.LABEL)) {
					ThemeLabel themeLabel = (ThemeLabel) theme;
					if (themeLabel.getCount() > 0) {
						// 分段标签
						graphics.drawImage(InternalImageIconFactory.THEME_LABEL_RANGE.getImage(), 0, 0, label);
					}
					if (themeLabel.getCount() == 0 && null == themeLabel.getUniformMixedStyle()) {
						// 统一标签
						graphics.drawImage(InternalImageIconFactory.THEME_LABEL.getImage(), 0, 0, label);
					}
					if (themeLabel.getCount() == 0 && null != themeLabel.getUniformMixedStyle()) {
						// 复合标签
						graphics.drawImage(InternalImageIconFactory.THEME_LABEL_COMPLICATED.getImage(), 0, 0, label);
					}
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.RANGE)) {
					graphics.drawImage(InternalImageIconFactory.THEME_RANGE.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
				if (type.equals(ThemeType.UNIQUE)) {
					graphics.drawImage(InternalImageIconFactory.THEME_UNIQUE.getImage(), 0, 0, label);
					icon.setImage(bufferedImage);
					return;
				}
			}
		}
	}
}
