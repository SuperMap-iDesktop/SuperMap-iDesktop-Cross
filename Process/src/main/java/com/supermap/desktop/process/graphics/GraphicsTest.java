package com.supermap.desktop.process.graphics;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Created by highsad on 2017/3/9.
 */
public class GraphicsTest extends JPanel {
	private int count = 0;
	private static boolean buttonRefresh = false;

	public GraphicsTest() {

		setSize(new Dimension(600, 400));
		JButton button = new JButton("panelButton");
		setLayout(new BorderLayout());
		add(button, BorderLayout.WEST);


	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		AffineTransform origin = ((Graphics2D) g).getTransform();
		AffineTransform transform = new AffineTransform(origin);
		transform.scale(2, 2);
		transform.translate(10, 10);
		((Graphics2D) g).setTransform(transform);

		g.setColor(Color.BLACK);
		((Graphics2D) g).fillRect(10, 10, 10, 10);
		((Graphics2D) g).setTransform(origin);
		System.out.println(origin.getTranslateX() + " " + origin.getTranslateY());
	}

	public static void main(String[] args) {
		final GraphicsTest test = new GraphicsTest();

		final JFrame frame = new JFrame();
		frame.setSize(600, 400);
		frame.setLayout(new BorderLayout());
		frame.add(test, BorderLayout.CENTER);

		JButton button = new JButton("test");
		frame.add(button, BorderLayout.NORTH);

		JButton button1 = new JButton("test1");
		frame.add(button1, BorderLayout.WEST);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				test.repaint(new Rectangle(220, 230, 600, 200));
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
