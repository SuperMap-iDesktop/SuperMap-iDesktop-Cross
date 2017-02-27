package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.graphs.EllipseGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.LineGraph;
import com.supermap.desktop.process.graphics.graphs.RectangleGraph;
import com.supermap.desktop.process.graphics.graphs.decorator.ArrowDecorator;
import com.supermap.desktop.process.graphics.graphs.decorator.HotDecorator;
import com.supermap.desktop.process.graphics.graphs.decorator.SelectedDecorator;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class DefaultGraphPainter implements IGraphPainter {

	private IStyleFactory factory = DefaultStyleFactory.INSTANCE;
	private Graphics graphics = null;
	private IGraph graph = null;

	@Override
	public IStyleFactory getStyleFactory() {
		return this.factory;
	}

	@Override
	public void setStyleFactory(IStyleFactory styleFactory) {
		this.factory = styleFactory;
	}

	@Override
	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	@Override
	public void setGraph(IGraph graph) {
		this.graph = graph;
	}

	@Override
	public void paint() {
		if (this.graph != null && this.graphics != null) {
			paint(this.graphics, this.graph);
		}
	}

	@Override
	public void paint(Graphics graphics, IGraph graph) {
		if (graph instanceof LineGraph) {
			paintLineGraph(graphics, (LineGraph) graph);
		} else if (graph instanceof RectangleGraph) {
			paintRectangleGraph(graphics, (RectangleGraph) graph);
		} else if (graph instanceof EllipseGraph) {
			paintEllipseGraph(graphics, (EllipseGraph) graph);
		} else if (graph instanceof HotDecorator) {
			paintHotDecorator(graphics, (HotDecorator) graph);
		} else if (graph instanceof SelectedDecorator) {
			paintSelectedDecorator(graphics, (SelectedDecorator) graph);
		} else if (graph instanceof ArrowDecorator) {
			paintArrowDecorator(graphics, (ArrowDecorator) graph);
		}
	}

	protected void paintLineGraph(Graphics graphics, LineGraph lineGraph) {

	}

	protected void paintRectangleGraph(Graphics graphics, RectangleGraph rectangleGraph) {

	}

	protected void paintEllipseGraph(Graphics graphics, EllipseGraph ellipseGraph) {

	}

	protected void paintHotDecorator(Graphics graphics, HotDecorator hotDecorator) {

	}

	protected void paintSelectedDecorator(Graphics graphics, SelectedDecorator selectedDecorator) {

	}

	protected void paintArrowDecorator(Graphics graphics, ArrowDecorator arrowDecorator) {

	}

	protected void paintText(Graphics graphics, IGraph graph) {
//		Font font = new Font("微软雅黑", Font.BOLD | Font.PLAIN, 20);
//		g.setFont(font);
//		g.setColor(Color.darkGray);
//
//		String text = this.data == null ? "未知数据" : this.data.toString();
//		int fontHeight = getCanvas().getFontMetrics(font).getHeight();
//		int fontWidth = SwingUtilities2.stringWidth(getCanvas(), getCanvas().getFontMetrics(font), text);
//		int fontDescent = getCanvas().getFontMetrics(font).getDescent();
//
//		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
//		g.drawString(text, doubleToInt(getX() + (getWidth() - fontWidth) / 2), doubleToInt(getY() + getHeight() / 2 + fontHeight / 2 - fontDescent));
	}
}
