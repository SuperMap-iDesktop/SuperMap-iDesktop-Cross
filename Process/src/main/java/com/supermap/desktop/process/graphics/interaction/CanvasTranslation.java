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
 * 在一个 AffineTransform 里，进行的任何设置都会与已有设置进行运算，实际的数据存储使用画布坐标，绘制过程使用屏幕坐标，100% 大小的画布坐标与屏幕坐标的像素
 * 为 1:1 的关系。
 * 1. 缩放和平移都只影响显示结果，不影响实际数据存储。
 * 2. 缩放和平移操作之后，我们鼠标点击、鼠标移动都是在当前缩放以及平移状态下的值，要查询源数据就需要进行转换。
 * 3. 借助 AffineTransform 进行绘制上的仿射变换，AffineTransform 缩放以左上角为基点，AffineTransform 缩放和平移操作相互之间会有直接影响。
 * 4. 由于缩放操作需要使用鼠标位置作为基点，而 AffineTransform 缩放不支持指定基点，因此就需要根据当前鼠标位置、当前缩放倍率以及即将缩放的倍率计算左上角的位置。
 * 5. 缩放时需要保证基点坐标不变，需要使用基点在当前视图中的比例关系计算缩放过后，缩放基点需要进行的位移。
 * 6. 由于画布的绘制范围和可视范围并不相同，而最终绘制的时候是使用可视范围查询数据进行绘制，因此最终可拆解为两个问题，①如何处理坐标关系，使得绘图元素在缩放之后
 * 6. 能摆在正确的位置？②缩放之后的画布可视范围能绘制那些绘图元素？
 * 7. 使用 AffineTransform 进行缩放n倍之后，画面上的 n个单位相当于缩放之前的 1个单位，无论缩放多少倍，缩放之后的绘图元素在画布上的位置比例一定要是相同的。
 * 8. 根据第七点，如果原绘图元素的左上角坐标是 (20,20),放大 n倍之后，在屏幕上看到的坐标将会是 (20n,20n)。
 * 9. 因为绘图元素的坐标在缩放之后会有倍乘关系，而 AffineTransform 先缩放再平移，平移的距离同样会倍乘缩放系数，因此为了简化坐标转化过程，就使用 AffineTransform
 * 9. 先缩放再平移。
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
		Point scaleCenter = e.getPoint();
		if (this.canvas.getCanvasViewBounds().contains(scaleCenter)) {
			int preLocationX = this.canvas.getCanvasViewBounds().x;
			int preLocationY = this.canvas.getCanvasViewBounds().y;

			// -1 for zooming in,1 form zooming out.
			int unitStep = e.getWheelRotation() == -1 ? this.step : -1 * this.step;

			// 缩放之后，在画布坐标系统下需要进行的位移量
			double dx = (100 * scaleCenter.getX()) / (100 + this.transform.getScaleValue() + unitStep) - (100 * scaleCenter.getX()) / (100 + this.transform.getScaleValue());
			double dy = (100 * scaleCenter.getY()) / (100 + this.transform.getScaleValue() + unitStep) - (100 * scaleCenter.getY()) / (100 + this.transform.getScaleValue());

			this.transform.translate(dx, dy);
			this.transform.scale(unitStep);
			this.canvas.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (GraphicsUtil.isPointValid(this.start) && SwingUtilities.isMiddleMouseButton(e)) {

			// 无论缩放倍数是多少，鼠标拖拽移动的位移就是画布平移的位移
			// no matter what number the scale is,the translate value of the canvas is the equivalent of the mouse movement.
			this.transform.translate((e.getX() - this.start.x) / this.transform.getScalePercentage(), (e.getY() - this.start.y) / this.transform.getScalePercentage());
			// reset the start point.
			this.start = e.getPoint();
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
