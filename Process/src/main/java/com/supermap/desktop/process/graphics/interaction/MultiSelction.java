package com.supermap.desktop.process.graphics.interaction;

import com.supermap.desktop.process.graphics.GraphCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/2.
 */
public class MultiSelction extends Selection {
	private Point selectionStart = Selection.UNKOWN_POINT;
	private Rectangle selectionRegion = new Rectangle(0, 0, 0, 0);
	private int selectionBorderWidth = 2;

	public MultiSelction(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public boolean isSelecting() {
		return this.selectionStart != Selection.UNKOWN_POINT;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.selectionStart = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point selectionEnd = e.getPoint();

		if (this.selectionStart != Selection.UNKOWN_POINT
				&& selectionEnd != Selection.UNKOWN_POINT
				&& this.selectionStart != selectionEnd) {

			// get selection region.
			int x = Math.min(this.selectionStart.x, selectionEnd.x);
			int y = Math.min(this.selectionStart.y, selectionEnd.y);
			int width = Math.abs(this.selectionStart.x - selectionEnd.x);
			int height = Math.abs(this.selectionStart.y - selectionEnd.y);
			this.selectionRegion.setBounds(x, y, width, height);

			// 往四周扩展两个像素，以便能完整的绘制出边框
			// grow two pixels in horizontal and vertical directions so that the selection border should be drawn completely.
			this.selectionRegion.grow(2, 2);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) && this.selectionStart != Selection.UNKOWN_POINT) {
			Point selectionEnd = e.getPoint();

			// 往四周扩展两个像素，以便能完整的绘制出边框
			// grow two pixels in horizontal and vertical directions so that the selection border should be drawn completely.
			int x = Math.min(this.selectionStart.x, selectionEnd.x) - this.selectionBorderWidth;
			int y = Math.min(this.selectionStart.y, selectionEnd.y) - this.selectionBorderWidth;
			int width = Math.abs(this.selectionStart.x - selectionEnd.x) + this.selectionBorderWidth * 2;
			int height = Math.abs(this.selectionStart.y - selectionEnd.y) + this.selectionBorderWidth * 2;

		}
	}

	/***
	 * 当鼠标移出了画布，则结束选择，并回到开始选择之前的状态
	 * @param e
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void paint(Graphics graphics) {

	}

	@Override
	public void clean() {

	}
}
