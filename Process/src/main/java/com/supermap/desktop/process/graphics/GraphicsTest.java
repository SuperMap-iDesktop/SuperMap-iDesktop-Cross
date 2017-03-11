package com.supermap.desktop.process.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Created by highsad on 2017/3/9.
 */
public class GraphicsTest extends JPanel {
	private int count = 0;

	public GraphicsTest() {
		setSize(new Dimension(600, 400));

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					if (e.getWheelRotation() == -1) {
						count++;
					} else {
						count--;
					}
//					System.out.println(e.getScrollAmount());
//					System.out.println(count);
					repaint();
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		AffineTransform origin = ((Graphics2D) g).getTransform();
		AffineTransform translation = new AffineTransform();

//		translation.scale(2 * (100 + count) / 100d, 2 * (100 + count) / 100d);
//		((Graphics2D) g).setTransform(translation);

		Rectangle rect = new Rectangle(100, 100, 100, 100);
		g.setColor(Color.BLUE);
		((Graphics2D) g).fill(rect);


		((Graphics2D) g).setTransform(origin);
	}

	public static void main(String[] args) {
		GraphicsTest test = new GraphicsTest();

		final JFrame frame = new JFrame();
		frame.setSize(600, 400);
		frame.setLayout(new BorderLayout());
		frame.add(test);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
