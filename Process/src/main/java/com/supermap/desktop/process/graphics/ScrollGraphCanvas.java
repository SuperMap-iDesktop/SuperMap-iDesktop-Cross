package com.supermap.desktop.process.graphics;

import com.supermap.desktop.process.graphics.events.CanvasTransformEvent;
import com.supermap.desktop.process.graphics.events.CanvasTransformListener;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by highsad on 2017/3/16.
 */
public class ScrollGraphCanvas extends JPanel {
	private GraphCanvas canvas;
	private JScrollBar hBar;
	private JScrollBar vBar;
	private JPanel corner;
	private GraphCanvasListener canvasListener = new GraphCanvasListener();

	private boolean isHandleScrollBar = false;
	private boolean isHandleCanvas = false;

	public ScrollGraphCanvas() {
		this(new GraphCanvas());
	}

	public ScrollGraphCanvas(final GraphCanvas canvas) {
		this.canvas = canvas;
		setOpaque(true);
		initilaizeComponents();
		this.canvas.getCoordinateTransform().addCanvasTransformListener(this.canvasListener);
		this.canvas.addGraphCreatedListener(this.canvasListener);
		this.canvas.addComponentListener(this.canvasListener);
		this.canvas.addAncestorListener(this.canvasListener);
	}

	public GraphCanvas getCanvas() {
		return canvas;
	}

	private void initilaizeComponents() {
		this.corner = new JPanel();
		this.corner.setOpaque(true);
		this.hBar = new HScrollBar();
		this.vBar = new VScrollBar();

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.canvas)
						.addComponent(this.hBar))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.vBar)
						.addComponent(this.corner, this.vBar.getWidth(), this.vBar.getWidth(), this.vBar.getWidth())));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.canvas)
						.addComponent(this.vBar))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.hBar)
						.addComponent(this.corner, this.hBar.getHeight(), this.hBar.getHeight(), this.hBar.getHeight())));

		Rectangle canvasRect = this.canvas.getCanvasRect();
		this.hBar.setMinimum(canvasRect.x);
		this.hBar.setMaximum(canvasRect.x + canvasRect.width);
		this.hBar.setValue(0);
		this.vBar.setMinimum(canvasRect.y);
		this.vBar.setMaximum(canvasRect.y + canvasRect.height);
		this.hBar.setValue(0);
	}

	private class HScrollBar extends JScrollBar implements AdjustmentListener {

		public HScrollBar() {
			setOrientation(JScrollBar.HORIZONTAL);
			setOpaque(true);
			setMinimum(0);
			setMaximum(1000);
			addAdjustmentListener(this);
		}

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isHandleCanvas) {
				isHandleScrollBar = true;
				canvas.getCoordinateTransform().translateXTo(-1 * e.getValue());
				canvas.repaint();
				isHandleScrollBar = false;
			}
		}
	}

	private class VScrollBar extends JScrollBar implements AdjustmentListener {
		public VScrollBar() {
			setOrientation(JScrollBar.VERTICAL);
			setOpaque(true);
			addAdjustmentListener(this);
		}

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isHandleCanvas) {
				isHandleScrollBar = true;
				canvas.getCoordinateTransform().translateYTo(-1 * e.getValue());
				canvas.repaint();
				isHandleScrollBar = false;
			}
		}
	}

	private class GraphCanvasListener extends ComponentAdapter implements CanvasTransformListener, GraphCreatedListener, AncestorListener {

		@Override
		public void graphCreated(GraphCreatedEvent e) {

		}

		@Override
		public void canvasTransform(CanvasTransformEvent e) {
			if (!isHandleScrollBar) {
				isHandleCanvas = true;
				processScrollBars();
				isHandleCanvas = false;
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			processScrollBars();
		}

		private void processScrollBars() {
			Rectangle visibleRect = canvas.getVisibleCanvasRect();
			if (visibleRect.width >= canvas.getCanvasRect().width) {
				hBar.setVisible(false);
			} else {
				hBar.setVisible(true);
				hBar.setValue(visibleRect.x);
				hBar.setVisibleAmount(visibleRect.width);
			}

			if (visibleRect.height > canvas.getCanvasRect().height) {
				vBar.setVisible(false);
			} else {
				vBar.setVisible(true);
				vBar.setValue(visibleRect.y);
				vBar.setVisibleAmount(visibleRect.height);
			}
		}

		@Override
		public void ancestorAdded(AncestorEvent event) {

			// 初始化的时候先做个偏移，以使得进度条滑块居中
			Rectangle visibleRect = canvas.getVisibleCanvasRect();
			canvas.getCoordinateTransform().translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
		}

		@Override
		public void ancestorRemoved(AncestorEvent event) {

		}

		@Override
		public void ancestorMoved(AncestorEvent event) {

		}
	}
}
