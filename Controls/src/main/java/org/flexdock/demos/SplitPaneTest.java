package org.flexdock.demos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

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
		final ArrayList<JSplitPane> splitPanes = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			JSplitPane splitPane = createSplitPane("Split " + Integer.toString(i));
			double weight = 1d / (4 - i);
//			weight = Double.compare(weight, 1d) == 1 ? 1 : weight;
//			splitPane.setResizeWeight(1d / (4 - i));
			splitPane.setResizeWeight(1d / (4 - i));
//			splitPane.setDividerLocation(1d / (4 - i));
			if (i != 0) {
				splitPanes.get(i - 1).setRightComponent(splitPane);
			}
			splitPanes.add(splitPane);
		}

		for (int i = 0; i < 4; i++) {

		}

		setLayout(new BorderLayout());
		add(splitPanes.get(0));
		setSize(1280, 800);
		setLocationRelativeTo(null);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
//				for (int i = splitPanes.size() - 1; i >= 0; i--) {
//					splitPanes.get(i).setDividerLocation(0.6);
//				}
				for (int i = 0; i < splitPanes.size(); i++) {
//					splitPanes.get(i).setResizeWeight(0);
//					splitPanes.get(i).setDividerLocation(0.5);
//					splitPanes.get(i).setResizeWeight(1);
				}
//				splitPane.setDividerLocation(0.3);
			}
		});

		JButton button = new JButton("test");
		add(button, BorderLayout.NORTH);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < splitPanes.size(); i++) {
//					splitPanes.get(i).setDividerLocation(0.5);
//					if (i == 0) {
					splitPanes.get(i).setOrientation(JSplitPane.VERTICAL_SPLIT);
						splitPanes.get(i).setResizeWeight(1d / (4 - i));
					splitPanes.get(i).setDividerLocation(1d / (4 - i));
					splitPanes.get(i).doLayout();
//					}
				}
			}
		});
	}

	private JSplitPane createSplitPane(String string) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(string));
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		// remove the border from the split pane
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(panel);
		splitPane.setRightComponent(null);
//		splitPane.setResizeWeight(1);
//		splitPane.setResizeWeight(0.5);

		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt.getNewValue().toString());
			}
		});

		splitPane.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
//				splitPane.setDividerLocation(0.5);
			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});
		return splitPane;
	}
}
