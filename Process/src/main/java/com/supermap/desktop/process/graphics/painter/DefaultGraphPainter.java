package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.*;
import com.supermap.desktop.process.graphics.graphs.decorator.*;
import com.supermap.desktop.utilities.DoubleUtilities;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class DefaultGraphPainter implements IGraphPainter {

	private IStyleFactory styleFactory = DefaultStyleFactory.INSTANCE;
	private GraphCanvas canvas;
	private Graphics graphics = null;
	private IGraph graph = null;

	@Override
	public IStyleFactory getStyleFactory() {
		return this.styleFactory;
	}

	@Override
	public void setStyleFactory(IStyleFactory styleFactory) {
		this.styleFactory = styleFactory;
	}

	public void setCanvas(GraphCanvas canvas) {
		this.canvas = canvas;
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
		} else if (graph instanceof ProcessGraph) {
			paintProcessGraph(graphics, (ProcessGraph) graph);
		} else if (graph instanceof RectangleGraph) {
			paintRectangleGraph(graphics, (RectangleGraph) graph);
		} else if (graph instanceof EllipseGraph) {
			paintEllipseGraph(graphics, (EllipseGraph) graph);
		} else if (graph instanceof AbstractDecorator) {
			AbstractDecorator decorator = (AbstractDecorator) graph;
			if (decorator instanceof PreviewDecorator) {
				paintPreviewDecorator(graphics, (PreviewDecorator) decorator);
			} else {
				paint(graphics, decorator.getGraph());
				paintDecorator(graphics, decorator);
			}
		}
	}

	private void paintDecorator(Graphics graphics, AbstractDecorator decorator) {
		if (decorator instanceof HotDecorator) {
			paintHotDecorator(graphics, (HotDecorator) graph);
		} else if (decorator instanceof SelectedDecorator) {
			paintSelectedDecorator(graphics, (SelectedDecorator) graph);
		} else if (decorator instanceof ArrowDecorator) {
			paintArrowDecorator(graphics, (ArrowDecorator) graph);
		} else if (decorator instanceof PreviewDecorator) {
			paintPreviewDecorator(graphics, (PreviewDecorator) graph);
		}
	}

	protected void paintLineGraph(Graphics graphics, LineGraph lineGraph) {

	}

	protected void paintRectangleGraph(Graphics graphics, RectangleGraph rectangleGraph) {
		this.styleFactory.normalRegion(graphics);
		((Graphics2D) graphics).fill(rectangleGraph.getShape());
	}

	protected void paintEllipseGraph(Graphics graphics, EllipseGraph ellipseGraph) {
		this.styleFactory.normalRegion(graphics);
		((Graphics2D) graphics).fill(ellipseGraph.getShape());
	}

	protected void paintHotDecorator(Graphics graphics, HotDecorator hotDecorator) {
		if (hotDecorator.isDecorating()) {
//			graphics.setColor(Color.GRAY);
//			((Graphics2D) graphics).draw(hotDecorator.getShape());
		}
	}

	protected void paintSelectedDecorator(Graphics graphics, SelectedDecorator selectedDecorator) {

	}

	protected void paintArrowDecorator(Graphics graphics, ArrowDecorator arrowDecorator) {

	}

	protected void paintPreviewDecorator(Graphics graphics, PreviewDecorator previewDecorator) {
		if (previewDecorator.isDecorating()) {
			this.styleFactory.previewRegion(graphics);
			((Graphics2D) graphics).fill(previewDecorator.getShape());

			if (previewDecorator.getGraph() instanceof ProcessGraph) {
				ProcessGraph processGraph = (ProcessGraph) previewDecorator.getGraph();

				Font font = new Font("宋体", Font.PLAIN, 24);
				graphics.setFont(font);
				graphics.setColor(Color.darkGray);

				int fontHeight = this.canvas.getFontMetrics(font).getHeight();
				int fontWidth = SwingUtilities2.stringWidth(this.canvas, this.canvas.getFontMetrics(font), processGraph.getTitle());
				int fontDescent = this.canvas.getFontMetrics(font).getDescent();

				// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
				Point location = processGraph.getLocation();
				double width = processGraph.getWidth();
				double height = processGraph.getHeight();
				graphics.drawString(processGraph.getTitle(), intValue(location.getX() + (width - fontWidth) / 2), intValue(location.getY() + height / 2 + fontHeight / 2 - fontDescent));
			}
		}
	}

	protected void paintProcessGraph(Graphics graphics, ProcessGraph processGraph) {
		paintRectangleGraph(graphics, processGraph);
		Font font = new Font("宋体", Font.PLAIN, 24);
		graphics.setFont(font);
		graphics.setColor(Color.darkGray);

		int fontHeight = this.canvas.getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(this.canvas, this.canvas.getFontMetrics(font), processGraph.getTitle());
		int fontDescent = this.canvas.getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		Point location = processGraph.getLocation();
		double width = processGraph.getWidth();
		double height = processGraph.getHeight();
		graphics.drawString(processGraph.getTitle(), intValue(location.getX() + (width - fontWidth) / 2), intValue(location.getY() + height / 2 + fontHeight / 2 - fontDescent));
	}

	private static int intValue(double value) {
		Double d = new Double(value);
		return d.intValue();
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
