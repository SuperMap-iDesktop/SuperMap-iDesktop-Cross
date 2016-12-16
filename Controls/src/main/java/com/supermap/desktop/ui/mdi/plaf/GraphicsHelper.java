package com.supermap.desktop.ui.mdi.plaf;

import java.awt.*;

/**
 * Created by highsad on 2016/9/22.
 */
public class GraphicsHelper {

	private Graphics graphics;

	private GraphicsHelper(Graphics graphics) {
		this.graphics = graphics;
	}

	/**
	 * 绘制线段，从点 x1,y1 到点 x2,y2
	 * 系统实现默认渲染 x1,y1 右下角的像素到 x2,y2 右下角的像素，这样显得不够直观
	 * 希望能如所想的一样，从哪儿到哪儿就仅仅绘制 x1,y1 以及 x2,y2 之间的像素（即 x1,y1 和 x2,y2 连线覆盖的像素格）
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		int newX1 = x1 > x2 ? x1 - 1 : x1;
		int newY1 = y1 > y2 ? y1 - 1 : y1;
		int newX2 = x2 > x1 ? x2 - 1 : x2;
		int newY2 = y2 > y1 ? y2 - 1 : y2;
		this.graphics.drawLine(newX1, newY1, newX2, newY2);
	}

	public void dispose() {
		this.graphics.dispose();
	}

	public static GraphicsHelper instance(Graphics graphics) {
		if (graphics != null) {
			return new GraphicsHelper(graphics);
		}
		return null;
	}
}
