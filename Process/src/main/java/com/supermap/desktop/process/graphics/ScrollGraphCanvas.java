package com.supermap.desktop.process.graphics;

import com.supermap.desktop.process.graphics.events.CanvasTransformEvent;
import com.supermap.desktop.process.graphics.events.CanvasTransformListener;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;

import javax.swing.*;
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

	public ScrollGraphCanvas() {
		this(new GraphCanvas());
	}

	public ScrollGraphCanvas(GraphCanvas canvas) {
		this.canvas = canvas;
		setOpaque(true);
		initilaizeComponents();
		this.canvas.getCoordinateTransform().addCanvasTransformListener(this.canvasListener);
		this.canvas.addGraphCreatedListener(this.canvasListener);
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

		}
	}

	private class GraphCanvasListener extends ComponentAdapter implements CanvasTransformListener, GraphCreatedListener {

		@Override
		public void graphCreated(GraphCreatedEvent e) {

		}

		@Override
		public void canvasTransform(CanvasTransformEvent e) {
			if (e.getType() == CanvasTransformEvent.TYPE_SCALE) {

			} else if (e.getType() == CanvasTransformEvent.TYPE_TRANSLATE) {

			}
		}

		@Override
		public void componentResized(ComponentEvent e) {

		}

		private void computeScrollBarExtent() {
		}
	}

	public static void main(String[] args) {
		ScrollGraphCanvas canvas = new ScrollGraphCanvas();
		final JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
