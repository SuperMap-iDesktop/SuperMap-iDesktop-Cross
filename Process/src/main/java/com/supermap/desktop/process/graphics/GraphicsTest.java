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
	private final JScrollBar hBar;
	private final JScrollBar vBar;

	public GraphicsTest() {

		setSize(new Dimension(600, 400));
		JButton button = new JButton("panelButton");
		setLayout(new BorderLayout());
		add(button, BorderLayout.WEST);

		hBar = new JScrollBar();
		hBar.setOrientation(JScrollBar.HORIZONTAL);
		vBar = new JScrollBar();
		vBar.setOrientation(JScrollBar.VERTICAL);

		add(hBar, BorderLayout.SOUTH);
		add(vBar, BorderLayout.EAST);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Rectangle rect = getVisibleRect();
//		rect.setLocation(rect.x + 5, rect.y + 5);
//		rect.setSize(rect.width - 5 - 5 - vBar.getWidth(), rect.height - 5 - 5 - hBar.getHeight());
		g.setColor(Color.BLACK);
		((Graphics2D) g).fill(rect);
	}

	@Override
	public Rectangle getVisibleRect() {
		Rectangle rect = super.getVisibleRect();
		rect.setLocation(rect.x + 5, rect.y + 5);
		rect.setSize(rect.width - 5 - 5 - vBar.getWidth(), rect.height - 5 - 5 - hBar.getHeight());
		return rect;
	}

	public static void main(String[] args) {
		final GraphicsTest test = new GraphicsTest();

		final JFrame frame = new JFrame();
		frame.setSize(600, 400);
		frame.setLayout(new BorderLayout());
//		frame.add(test, BorderLayout.CENTER);
//
//		JButton button = new JButton("test");
//		frame.add(button, BorderLayout.NORTH);
//
//		JButton button1 = new JButton("test1");
//		frame.add(button1, BorderLayout.WEST);
//
//		button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				test.repaint(new Rectangle(220, 230, 600, 200));
//			}
//		});
//
//		test.addMouseMotionListener(new MouseMotionListener() {
//			@Override
//			public void mouseDragged(MouseEvent e) {
//
//			}
//
//			int i = 0;
//
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				i++;
//				System.out.println(i);
//			}
//		});

		JScrollPane scrollPane = new JScrollPane();
		GraphCanvas canvas = new GraphCanvas();
		scrollPane.setViewportView(canvas);
		frame.add(scrollPane, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
