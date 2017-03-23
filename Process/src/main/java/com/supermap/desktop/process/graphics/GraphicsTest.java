package com.supermap.desktop.process.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/3/9.
 */
public class GraphicsTest extends JPanel {
	private int count = 0;
	private static boolean buttonRefresh = false;
	private RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Double();

	public GraphicsTest() {


		this.roundRectangle2D.setRoundRect(100, 100, 200, 200, 30, 30);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g;

		graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

//		AffineTransform origin = graphics2D.getTransform();
//		AffineTransform newT = new AffineTransform(origin);
//		newT.scale(scale, scale);
//		graphics2D.setTransform(newT);
//		graphics2D.setColor(Color.PINK);
//		graphics2D.fill(this.roundRectangle2D);
//
//		BasicStroke stroke = new BasicStroke(2);
//		graphics2D.setStroke(stroke);
//		graphics2D.setColor(Color.BLACK);
//		graphics2D.draw(roundRectangle2D);
//		graphics2D.setTransform(origin);
		graphics2D.setColor(Color.RED);
		Rectangle rect = new Rectangle(200, 200, 100, 100);
		graphics2D.fill(rect);
	}

	private static double scale = 1;

	public static void main(String[] args) {
//		Main.main(args);
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(1000, 1000);

		final GraphicsTest test = new GraphicsTest();
		frame.add(test, BorderLayout.CENTER);

		JButton button = new JButton("Scale");
		frame.add(button, BorderLayout.NORTH);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scale = 3;
				test.repaint();
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

//	public static void main(String[] args) {
//		final GraphicsTest test = new GraphicsTest();
//
//		final JFrame frame = new JFrame();
//		frame.setSize(600, 400);
//		frame.setLayout(new BorderLayout());
////		frame.add(test, BorderLayout.CENTER);
////
////		JButton button = new JButton("test");
////		frame.add(button, BorderLayout.NORTH);
////
////		JButton button1 = new JButton("test1");
////		frame.add(button1, BorderLayout.WEST);
////
////		button.addActionListener(new ActionListener() {
////			@Override
////			public void actionPerformed(ActionEvent e) {
////				test.repaint(new Rectangle(220, 230, 600, 200));
////			}
////		});
////
////		test.addMouseMotionListener(new MouseMotionListener() {
////			@Override
////			public void mouseDragged(MouseEvent e) {
////
////			}
////
////			int i = 0;
////
////			@Override
////			public void mouseMoved(MouseEvent e) {
////				i++;
////				System.out.println(i);
////			}
////		});
//
//		Rectangle rect1 = new Rectangle(0, 0, 0, 0);
//		Rectangle rect2 = new Rectangle(-100, -100, 100, 100);
//		Rectangle rect3 = rect1.union(rect2);
//		System.out.println(rect3);
//		JScrollPane scrollPane = new JScrollPane();
//		final GraphCanvas canvas = new GraphCanvas();
//		scrollPane.setViewportView(canvas);
//		scrollPane.getHorizontalScrollBar().setMinimum(-200);
//		frame.add(scrollPane, BorderLayout.CENTER);
//
//		JButton button = new JButton("create");
//		frame.add(button, BorderLayout.NORTH);
//		button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				RectangleGraph graph = new RectangleGraph(canvas);
//				graph.setSize(200, 80);
//				graph.setArcHeight(10);
//				graph.setArcWidth(10);
//
//				canvas.create(graph);
//				System.out.println(canvas.getLocation());
//			}
//		});
//
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				frame.setVisible(true);
//			}
//		});
//	}
}
