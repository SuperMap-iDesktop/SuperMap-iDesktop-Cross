package org.flexdock.demos;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by highsad on 2016/11/11.
 */
public class MyDemo extends JFrame {
	public static void main(String[] args) {
		final MyDemo demo = new MyDemo();
		demo.setSize(600, 400);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				demo.setVisible(true);
			}
		});
	}

	public MyDemo() {
		Viewport viewport = new Viewport();
		this.getContentPane().setLayout(new BorderLayout());
		JButton button = new JButton("可见控制");

		this.getContentPane().add(button, BorderLayout.NORTH);
		this.getContentPane().add(viewport, BorderLayout.CENTER);

		JPanel panelMain = new JPanel();
		panelMain.setBackground(Color.lightGray);
		final View view = new View("main", "main");
		view.setContentPane(panelMain);
		view.setTitlebar(null);
		view.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		JPanel panel1 = createPanel("panel1");
		JPanel panel2 = createPanel("panel2");
		JPanel panel3 = createPanel("panel3");
		JPanel panel4 = createPanel("panel4");

		final View view1 = new View("panel1", "panel1");
		view1.setTerritoryBlocked(DockingConstants.WEST_REGION, true);
		view1.setTerritoryBlocked(DockingConstants.EAST_REGION, true);
		view1.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		view1.setContentPane(panel1);

		View view2 = new View("panel2", "panel2");
		view2.setContentPane(panel2);

		View view3 = new View("panel3", "panel3");
		view3.setContentPane(panel3);

		View view4 = new View("panel4", "panel4");
		view4.setContentPane(panel4);
		viewport.dock(view);

//		viewport.setRegionBlocked(DockingConstants.CENTER_REGION, true);
//		viewport.dock(((Dockable) view3), DockingConstants.EAST_REGION);
//		viewport.dock((Dockable) view1, DockingConstants.SOUTH_REGION);
//		viewport.dock(((Dockable) view2), DockingConstants.WEST_REGION);

		view.dock((Dockable) view2, DockingConstants.WEST_REGION);
		view.dock((Dockable) view3, DockingConstants.EAST_REGION);
		view.dock((Dockable) view1, DockingConstants.SOUTH_REGION);

//		view.dock((Dockable) view2, DockingConstants.CENTER_REGION);
//		view.dock((Dockable) view3, DockingConstants.CENTER_REGION);
//		view.dock((Dockable) view1, DockingConstants.CENTER_REGION);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (DockingManager.isDocked((Dockable) view1)) {
					DockingManager.close(view1);
				} else {
					DockingManager.display(view1);
				}
//				System.out.println(DockingManager.getDockingState(view1).toString());
			}
		});
	}

	private JPanel createPanel(String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JButton(title), BorderLayout.CENTER);
		return panel;
	}
}
