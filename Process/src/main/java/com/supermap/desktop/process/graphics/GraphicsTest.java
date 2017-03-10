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

	public GraphicsTest() {
		setSize(new Dimension(600, 400));
	}

	@Override
	protected void paintComponent(Graphics g) {
		AffineTransform origin = ((Graphics2D) g).getTransform();
		AffineTransform translation = new AffineTransform();

		translation.setToScale(2, 2);
		translation.setToTranslation(100, 100);
		((Graphics2D) g).setTransform(translation);

		Rectangle rect = new Rectangle(0, 0, 100, 100);
		g.setColor(Color.BLUE);
		((Graphics2D) g).fill(rect);


		((Graphics2D) g).setTransform(origin);

		Point2D src = new Point(150, 150);
		Point2D tar = new Point();
		try {
			translation.inverseTransform(src, tar);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		System.out.println(tar);

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				System.out.println(e.getWheelRotation());
			}
		});
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
