package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
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
		} else if (graph instanceof OutputGraph) {
			paintOutputGraph(graphics, (OutputGraph) graph);
		} else if (graph instanceof RectangleGraph) {
			paintRectangleGraph(graphics, (RectangleGraph) graph);
		} else if (graph instanceof EllipseGraph) {
			paintEllipseGraph(graphics, (EllipseGraph) graph);
		} else if (graph instanceof AbstractDecorator) {
			AbstractDecorator decorator = (AbstractDecorator) graph;
			if (decorator instanceof PreviewDecorator) {
				paintPreviewDecorator(graphics, (PreviewDecorator) decorator);
			} else {
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
		if (lineGraph.getPreProcess() == null) {
			return;
		}

		graphics.setColor(Color.GRAY);
		Point start = lineGraph.getStart();
		Point end = lineGraph.getEnd();
		graphics.drawLine(start.x, start.y, end.x, end.y);

		if (lineGraph.getPreProcess() instanceof OutputGraph && ((
				lineGraph.getNextProcess() == null && lineGraph.getEnd() != null) ||
				lineGraph.getNextProcess() instanceof ProcessGraph)) {
			Point[] points = calArrow(start, end);
			graphics.drawLine(points[0].x, points[0].y, end.x, end.y);
			graphics.drawLine(points[1].x, points[1].y, end.x, end.y);
		}
	}

	public Point[] calArrow(Point start, Point end) {
		return calArrow(start.x, start.y, end.x, end.y);
	}

	public Point[] calArrow(int startX, int startY, int endX, int endY) {
		double awrad = 15 * Math.PI / 180;// 30表示角度，但是在计算中要用弧度进行计算，所以要把角度转换为弧度
		double arraow_len = 12;// 箭头长度
		double[] arr1 = rotateVec(endX - startX, endY - startY, awrad, arraow_len);
		double[] arr2 = rotateVec(endX - startX, endY - startY, -awrad, arraow_len);
		double x1 = endX - arr1[0]; // (x3,y3)是第一端点
		double y1 = endY - arr1[1];
		double x2 = endX - arr2[0]; // (x4,y4)是第二端点
		double y2 = endY - arr2[1];
		Point point1 = new Point(intValue(x1), intValue(y1));
		Point point2 = new Point(intValue(x2), intValue(y2));
		return new Point[]{point1, point2};
	}

	// 计算
	public double[] rotateVec(int px, int py, double ang, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		double d = Math.sqrt(vx * vx + vy * vy);
		vx = vx / d * newLen;
		vy = vy / d * newLen;
		mathstr[0] = vx;
		mathstr[1] = vy;
		return mathstr;
	}

	protected void paintRectangleGraph(Graphics graphics, RectangleGraph rectangleGraph) {
		this.styleFactory.normalRegion(graphics);
		((Graphics2D) graphics).fill(rectangleGraph.getShape());
	}

	protected void paintEllipseGraph(Graphics graphics, EllipseGraph ellipseGraph) {
		graphics.setColor(Color.decode("#C1FFC1"));
		((Graphics2D) graphics).fill(ellipseGraph.getShape());
	}

	protected void paintHotDecorator(Graphics graphics, HotDecorator hotDecorator) {
		if (hotDecorator.isDecorating()) {
//			graphics.setColor(Color.GRAY);
//			((Graphics2D) graphics).draw(hotDecorator.getShape());
		}
	}

	protected void paintSelectedDecorator(Graphics graphics, SelectedDecorator selectedDecorator) {
		if (selectedDecorator.isDecorating()) {
//			Rectangle rect = (Rectangle) selectedDecorator.getBounds().clone();
//			rect.grow(-1, -1);
//			((Graphics2D) graphics).draw(rect);
			BasicStroke stroke = new BasicStroke(2);
			((Graphics2D) graphics).setStroke(stroke);
			graphics.setColor(Color.BLACK);
			((Graphics2D) graphics).draw(selectedDecorator.getShape());
		}
	}

	protected void paintArrowDecorator(Graphics graphics, ArrowDecorator arrowDecorator) {

	}

	protected void paintPreviewDecorator(Graphics graphics, PreviewDecorator previewDecorator) {
		if (previewDecorator.isDecorating()) {
			this.styleFactory.previewRegion(graphics);
			((Graphics2D) graphics).fill(previewDecorator.getShape());

			if (previewDecorator.getGraph() instanceof ProcessGraph) {
				ProcessGraph processGraph = (ProcessGraph) previewDecorator.getGraph();

				Font font = new Font("宋体", Font.PLAIN, 20);
				graphics.setFont(font);
				graphics.setColor(GraphicsUtil.transparentColor(Color.LIGHT_GRAY, 100));

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

		String tilte = SwingUtilities2.clipStringIfNecessary(this.canvas, this.canvas.getFontMetrics(font), processGraph.getTitle(), processGraph.getWidth());
		int fontHeight = this.canvas.getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(this.canvas, this.canvas.getFontMetrics(font), tilte);
		int fontDescent = this.canvas.getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		Point location = processGraph.getLocation();
		double width = processGraph.getWidth();
		double height = processGraph.getHeight();
		graphics.drawString(tilte, intValue(location.getX() + (width - fontWidth) / 2), intValue(location.getY() + height / 2 + fontHeight / 2 - fontDescent));
	}

	private void paintOutputGraph(Graphics graphics, OutputGraph outputGraph) {
		paintEllipseGraph(graphics, outputGraph);
		Font font = new Font("宋体", Font.PLAIN, 20);
		graphics.setFont(font);
		graphics.setColor(Color.darkGray);

		String text = outputGraph.getTitle();
		int fontHeight = this.canvas.getFontMetrics(font).getHeight();
		int fontWidth = SwingUtilities2.stringWidth(this.canvas, this.canvas.getFontMetrics(font), text);
		int fontDescent = this.canvas.getFontMetrics(font).getDescent();

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		Point location = outputGraph.getLocation();
		double width = outputGraph.getWidth();
		double height = outputGraph.getHeight();
		text = SwingUtilities2.clipStringIfNecessary(this.canvas, this.canvas.getFontMetrics(font), text, DoubleUtilities.intValue(width));
		graphics.drawString(text, intValue(location.getX() + (width - fontWidth) / 2), intValue(location.getY() + height / 2 + fontHeight / 2 - fontDescent));
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
