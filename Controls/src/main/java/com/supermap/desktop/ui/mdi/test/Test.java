package com.supermap.desktop.ui.mdi.test;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.MdiPane;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test {

	public static void main(String[] args) {
		final MdiPane pane = new MdiPane();
//		group.setFloatOnPage(true);
		MdiPage page = MdiPage.createMdiPage(new PagePanelDemo("test1"), "test1");
		pane.addPage(page);

		MdiPage page1 = MdiPage.createMdiPage(new PagePanelDemo1(), "test2");
		pane.addNewGroup(page1);

		MdiPage page2 = MdiPage.createMdiPage(new PagePanelDemo("test3"), "test3");
		pane.addNewGroup(page2);

		MdiPage page3 = MdiPage.createMdiPage(new PagePanelDemo1(), "test4");
		pane.addNewGroup(page3);
		final JFrame frame = new JFrame();
		frame.setSize(600, 600);

		JButton button = new JButton("Add Page");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MdiPage page = MdiPage.createMdiPage(new PagePanelDemo1(), "test" + Integer.toString(pane.getPageCount()));
				pane.addPage(page);
			}
		});

		frame.setLayout(new BorderLayout());
		frame.add(button, BorderLayout.NORTH);
		frame.add(pane, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
