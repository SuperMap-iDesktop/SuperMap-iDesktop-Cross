package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/1/24.
 */
public class ProcessGraph extends RectangleGraph {

	private IProcess process;

	public ProcessGraph(GraphCanvas canvas) {
		super(canvas);
	}

	public ProcessGraph(GraphCanvas canvas, IProcess process) {
		super(canvas);
		this.process = process;
	}

	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {
		super.paint(g, isHot, isSelected);
		paintText(g);
	}

	@Override
	public void paintPreview(Graphics2D g) {
		super.paintPreview(g);
		paintText(g);
	}

	private void paintText(Graphics2D g) {
		Font font = new Font("微软雅黑", Font.BOLD | Font.PLAIN, 20);
		g.setFont(font);
		g.setColor(Color.darkGray);

		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), "缓冲区分析");
		int fontDescent = getCanvas().getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		g.drawString("缓冲区分析", doubleToInt(getX() + (getWidth() - fontWidth) / 2), doubleToInt(getY() + getHeight() / 2 + fontHeight / 2 - fontDescent));
	}

	private int doubleToInt(double d) {
		return Double.valueOf(d).intValue();
	}

	@Override
	public IGraph clone() {
		ProcessGraph graph = new ProcessGraph(getCanvas(), this.process);
		graph.setX(getX());
		graph.setY(getY());
		graph.setWidth(getWidth());
		graph.setHeight(getHeight());
		graph.setArcWidth(getArcWidth());
		graph.setArcHeight(getArcHeight());
		return graph;
	}
}
