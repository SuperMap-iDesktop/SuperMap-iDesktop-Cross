package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * 颜色样板，该类用于装饰组件
 * 
 * @author xuzw
 *
 */
public class ColorSwatch implements Icon {

	private Color selectedColor;

	private int iconHeight;

	private int iconWidth;

	/**
	 * 构造函数
	 * 
	 * @param color
	 *            Icon的颜色
	 * @param height
	 *            Icon的高度
	 * @param width
	 *            Icon的宽度
	 */
	public ColorSwatch(Color color, int height, int width) {
		selectedColor = color;
		iconHeight = height;
		iconWidth = width;
	}
@Override
	public int getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}
@Override
	public int getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}

	public Color getColor() {
		return selectedColor;
	}

	public void setColor(Color color) {
		this.selectedColor = color;
	}

	/**
	 * 绘制
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.fillRect(x, y, getIconWidth(), getIconHeight());
		g.setColor(selectedColor);
		g.fillRect(x + 1, y + 1, getIconWidth() - 2, getIconHeight() - 2);
	}

}
