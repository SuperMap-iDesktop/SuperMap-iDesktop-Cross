package com.supermap.desktop.process.graphics.interaction;

import com.supermap.desktop.process.graphics.CoordinateTransform;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.handler.canvas.CanvasEventAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * 在一个 AffineTransform 里，进行的任何设置都会与已有设置进行运算
 * 1. 缩放和平移都只影响显示结果，不影响实际数据存储。
 * 2. 缩放和平移操作之后，我们鼠标点击、鼠标移动都是在当前缩放以及平移状态下的值，要查询源数据就需要进行转换。
 * 3. 借助 AffineTransform 进行绘制上的仿射变换，AffineTransform 缩放以左上角为基点，AffineTransform 缩放和平移操作相互之间会有直接影响。
 * 4. 由于缩放操作需要使用鼠标位置作为基点，而 AffineTransform 缩放不支持指定基点，因此就需要根据当前鼠标位置、当前缩放倍率以及即将缩放的倍率计算左上角的位置。
 * 5. 缩放时需要保证基点坐标不变，需要使用基点在当前视图中的比例关系计算缩放过后，之前视图的左上角坐标落点。
 * 6. 由于画布的绘制范围和可视范围并不相同，而最终绘制的时候是使用可视范围查询数据进行绘制，因此问题的最终形态就是：缩放和平移之后，画布视图所对应的实际可视范围是多少？
 * 7. 无论在何种缩放倍数下，通过鼠标操作移动的距离，就是实际平移的距离，直接累加累减即可。
 * Scale and translate the canvas.
 * Created by highsad on 2017/3/8.
 */
public class CanvasTranslation extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private CoordinateTransform transform;
	private Point start = null;
	private int step = 2; // once the mouse middle button moved,this canvas zooms in or out for two percent.

	public CanvasTranslation(GraphCanvas canvas) {
		this.canvas = canvas;
		this.transform = canvas.getCoordinateTransform();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isMiddleMouseButton(e)) {
			start = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isMiddleMouseButton(e)) {
			start = null;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point scaleStart = e.getPoint();
		if (this.canvas.getCanvasViewBounds().contains(scaleStart)) {

			// -1 for zooming in,1 form zooming out.
			int unitStep = e.getWheelRotation() == -1 ? this.step : -1 * this.step;
			int dx = scaleStart.x;
			int dy = scaleStart.y;
			this.transform.translate(-1 * (1 + unitStep / 100d) * dx, -1 * (1 + unitStep / 100d) * dy);
			this.transform.scale(unitStep);
			this.canvas.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (GraphicsUtil.isPointValid(this.start) && SwingUtilities.isMiddleMouseButton(e)) {

			// 无论缩放倍数是多少，鼠标拖拽移动的位移就是画布平移的位移
			// no matter what number the scale is,the translate value of the canvas is the equivalent of the mouse movement.
			this.transform.translate(e.getX() - this.start.x, e.getY() - this.start.y);
			this.canvas.repaint();
		}
	}

	@Override
	public void clean() {

	}

	public boolean isTranslating() {
		return this.start != null;
	}
}
