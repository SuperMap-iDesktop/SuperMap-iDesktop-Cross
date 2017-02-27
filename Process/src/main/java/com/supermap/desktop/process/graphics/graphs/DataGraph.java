package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.IData;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/1/24.
 */
public class DataGraph extends EllipseGraph {

	private IData data;

	public DataGraph(GraphCanvas canvas) {
		super(canvas);
	}

	private void paintText(Graphics2D g) {
		Font font = new Font("微软雅黑", Font.BOLD | Font.PLAIN, 20);
		g.setFont(font);
		g.setColor(Color.darkGray);

		String text = this.data == null ? "未知数据" : this.data.toString();
		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), text);
		int fontDescent = getCanvas().getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
//		g.drawString(text, doubleToInt(getX() + (getWidth() - fontWidth) / 2), doubleToInt(getY() + getHeight() / 2 + fontHeight / 2 - fontDescent));
	}

	private int doubleToInt(double d) {
		return Double.valueOf(d).intValue();
	}


	@Override
	public IGraph clone() {
		DataGraph graph = new DataGraph(getCanvas());

		return graph;
	}
}
