package org.flexdock.demos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by highsad on 2016/12/1.
 */
public class SplitPaneTest extends JFrame {
	public static final void main(String[] args) {
		final SplitPaneTest t = new SplitPaneTest();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				t.setVisible(true);
			}
		});
	}

	public SplitPaneTest() {
		JButton button1 = new JButton("Button1");
		JButton button2 = new JButton("Button2");
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(button1);
		splitPane.setRightComponent(button2);

		splitPane.setResizeWeight(0.5);

		setLayout(new BorderLayout());
		add(splitPane);
		setSize(1280, 800);
		setLocationRelativeTo(null);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				splitPane.setDividerLocation(0.3);
			}
		});
	}
}
