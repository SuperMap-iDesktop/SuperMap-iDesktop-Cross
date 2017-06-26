package com.supermap.desktop.process.tasks.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/6/24.
 */
public class TestFrame extends JFrame {
	private JButton buttonExecute = new JButton();

	public TestFrame() {
		this.buttonExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TestWorker worker = new TestWorker();
				TestProgressForm progressForm = new TestProgressForm();
				progressForm.setVisible(true);
				progressForm.execute(worker);
			}
		});

		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		getContentPane().add(buttonExecute, BorderLayout.CENTER);
		setSize(800, 600);
	}

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		frame.setVisible(true);
	}
}
