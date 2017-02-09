package com.supermap.desktop.process;

import com.supermap.desktop.ui.FormBaseChild;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.tool.CreationTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/1/6.
 */
public class FormProcess extends JPanel {

	private DefaultDrawingView drawingView;
	private DefaultDrawing drawing;
	private DefaultDrawingEditor drawingEditor;

//	public FormProcess(String title, Icon icon, Component component) {
//		super(title, icon, component);
//	}

	public FormProcess() {
		this.drawing = new DefaultDrawing();
		this.drawingEditor = new DefaultDrawingEditor();
		this.drawingView = new DefaultDrawingView();
		this.drawingView.setDrawing(this.drawing);
		this.drawingEditor.add(this.drawingView);
		this.drawingEditor.setActiveView(this.drawingView);

		JButton button = new JButton("test");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawingEditor.setTool(new CreationTool(new RectangleFigure()));
			}
		});

		setLayout(new BorderLayout());
		add(button, BorderLayout.NORTH);
		add(this.drawingView, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(1000, 650);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new FormProcess(), BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
