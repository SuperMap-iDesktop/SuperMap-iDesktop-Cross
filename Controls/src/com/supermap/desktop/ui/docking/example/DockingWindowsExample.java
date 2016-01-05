/*
 * Copyright (c) 2004 NNL Technology AB
 * All rights reserved.
 *
 * "Work" shall mean the contents of this file.
 *
 * Redistribution, copying and use of the Work, with or without
 * modification, is permitted without restrictions.
 *
 * Visit www.infonode.net for information about InfoNode(R)
 * products and how to contact NNL Technology AB.
 *
 * THE WORK IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THE WORK, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

// $Id: DockingWindowsExample.java,v 1.28 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking.example;

import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.util.Direction;

import javax.swing.*;

import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.docking.*;
import com.supermap.desktop.ui.docking.drag.DockingWindowDragSource;
import com.supermap.desktop.ui.docking.drag.DockingWindowDragger;
import com.supermap.desktop.ui.docking.drag.DockingWindowDraggerProvider;
import com.supermap.desktop.ui.docking.mouse.DockingWindowActionMouseButtonListener;
import com.supermap.desktop.ui.docking.properties.RootWindowProperties;
import com.supermap.desktop.ui.docking.theme.*;
import com.supermap.desktop.ui.docking.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;

/**
 * A small example on how to use InfoNode Docking Windows. This example shows how to handle both static and dynamic views in the same root window.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.28 $
 */
public class DockingWindowsExample {
	private static final int ICON_SIZE = 8;

	/**
	 * Custom view icon.
	 */
	private static final Icon VIEW_ICON = new Icon() {
		@Override
		public int getIconHeight() {
			return ICON_SIZE;
		}

		@Override
		public int getIconWidth() {
			return ICON_SIZE;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color oldColor = g.getColor();

			g.setColor(new Color(70, 70, 70));
			g.fillRect(x, y, ICON_SIZE, ICON_SIZE);

			g.setColor(new Color(100, 230, 100));
			g.fillRect(x + 1, y + 1, ICON_SIZE - 2, ICON_SIZE - 2);

			g.setColor(oldColor);
		}
	};

	/**
	 * Custom view button icon.
	 */
	private static final Icon BUTTON_ICON = new Icon() {
		@Override
		public int getIconHeight() {
			return ICON_SIZE;
		}

		@Override
		public int getIconWidth() {
			return ICON_SIZE;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color oldColor = g.getColor();

			g.setColor(Color.BLACK);
			g.fillOval(x, y, ICON_SIZE, ICON_SIZE);

			g.setColor(oldColor);
		}
	};

	/**
	 * The one and only root window
	 */
	private RootWindow rootWindow;

	/**
	 * An array of the static views
	 */
	private View[] views = new View[10];

	/**
	 * Contains all the static views
	 */
	private ViewMap viewMap = new ViewMap();

	/**
	 * The view menu items
	 */
	private JMenuItem[] viewItems = new JMenuItem[views.length];

	/**
	 * Contains the dynamic views that has been added to the root window
	 */
	private HashMap dynamicViews = new HashMap();

	/**
	 * The currently applied docking windows theme
	 */
	private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();

	/**
	 * A dynamically created view containing an id.
	 */
	private static class DynamicView extends View {
		private int id;

		/**
		 * Constructor.
		 *
		 * @param title the view title
		 * @param icon the view icon
		 * @param component the view component
		 * @param id the view id
		 */
		DynamicView(String title, Icon icon, Component component, int id) {
			super(title, icon, component);
			this.id = id;
		}

		/**
		 * Returns the view id.
		 *
		 * @return the view id
		 */
		public int getId() {
			return id;
		}
	}

	/**
	 * In this properties object the modified property values for close buttons etc. are stored. This object is cleared when the theme is changed.
	 */
	private RootWindowProperties properties = new RootWindowProperties();

	/**
	 * Where the layouts are stored.
	 */
	private byte[][] layouts = new byte[3][];

	/**
	 * Menu item for enabling/disabling adding of a menu bar and a status label to all new floating windows.
	 */
	private JCheckBoxMenuItem enableMenuAndStatusLabelMenuItem = new JCheckBoxMenuItem("Add Menu Bar and Status Label to all New Floating Windows", true);

	/**
	 * The application frame
	 */
	private JFrame frame = new JFrame("InfoNode Docking Windows Example");

	private DockingWindowDragSource dockingWindowDragSource;

	public DockingWindowsExample() {
		createRootWindow();
		setDefaultLayout();
		showFrame();
	}

	/**
	 * Creates a view component containing the specified text.
	 *
	 * @param text the text
	 * @return the view component
	 */
	private static JComponent createViewComponent(String text) {
		StringBuffer sb = new StringBuffer();

		for (int j = 0; j < 100; j++)
			sb.append(text + ". This is line " + j + "\n");

		return new JScrollPane(new JTextArea(sb.toString()));
	}

	/**
	 * Returns a dynamic view with specified id, reusing an existing view if possible.
	 *
	 * @param id the dynamic view id
	 * @return the dynamic view
	 */
	private View getDynamicView(int id) {
		View view = (View) dynamicViews.get(new Integer(id));

		if (view == null)
			view = new DynamicView("Dynamic View " + id, VIEW_ICON, createViewComponent("Dynamic View " + id), id);

		return view;
	}

	/**
	 * Returns the next available dynamic view id.
	 *
	 * @return the next available dynamic view id
	 */
	private int getDynamicViewId() {
		int id = 0;

		while (dynamicViews.containsKey(new Integer(id)))
			id++;

		return id;
	}

	/**
	 * Creates the root window and the views.
	 */
	private void createRootWindow() {
		// Create the views
		for (int i = 0; i < views.length; i++) {
			views[i] = new View("View " + i, VIEW_ICON, createViewComponent("View " + i));
			viewMap.addView(i, views[i]);
		}

		// Create a custom view button and add it to view 2
		JButton button = new JButton(BUTTON_ICON);
		button.setOpaque(false);
		button.setBorder(null);
		button.setFocusable(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UICommonToolkit.showMessageDialog("You clicked the custom view button.");
			}
		});
		views[2].getCustomTabComponents().add(button);

		// The mixed view map makes it easy to mix static and dynamic views inside the same root window
		MixedViewHandler handler = new MixedViewHandler(viewMap, new ViewSerializer() {
			@Override
			public void writeView(View view, ObjectOutputStream out) throws IOException {
				out.writeInt(((DynamicView) view).getId());
			}

			@Override
			public View readView(ObjectInputStream in) throws IOException {
				return getDynamicView(in.readInt());
			}
		});

		rootWindow = DockingUtil.createRootWindow(viewMap, handler, true);

		// Set gradient theme. The theme properties object is the super object of our properties object, which
		// means our property value settings will override the theme values
		properties.addSuperObject(currentTheme.getRootWindowProperties());

		// Our properties object is the super object of the root window properties object, so all property values of the
		// theme and in our property object will be used by the root window
		rootWindow.getRootWindowProperties().addSuperObject(properties);

		// Enable the bottom window bar
		rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);

		// Add a listener which shows dialogs when a window is closing or closed.
		rootWindow.addListener(new DockingWindowAdapter() {
			@Override
			public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow) {
				updateViews(addedWindow, true);

				// If the added window is a floating window, then update it
				if (addedWindow instanceof FloatingWindow)
					updateFloatingWindow((FloatingWindow) addedWindow);
			}

			@Override
			public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow) {
				updateViews(removedWindow, false);
			}

			public void windowClosing(DockingWindow window) throws OperationAbortedException {
				// Confirm close operation
				if (UICommonToolkit.showConfirmDialog("Really close window '" + window + "'?") != JOptionPane.YES_OPTION)
					throw new OperationAbortedException("Window close was aborted!");
			}

			@Override
			public void windowDocking(DockingWindow window) throws OperationAbortedException {
				// Confirm dock operation
				if (UICommonToolkit.showConfirmDialog("Really dock window '" + window + "'?") != JOptionPane.YES_OPTION)
					throw new OperationAbortedException("Window dock was aborted!");
			}

			@Override
			public void windowUndocking(DockingWindow window) throws OperationAbortedException {
				// Confirm undock operation
				if (UICommonToolkit.showConfirmDialog("Really undock window '" + window + "'?") != JOptionPane.YES_OPTION)
					throw new OperationAbortedException("Window undock was aborted!");
			}

		});

		// Add a mouse button listener that closes a window when it's clicked with the middle mouse button.
		rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
	}

	/**
	 * Update view menu items and dynamic view map.
	 *
	 * @param window the window in which to search for views
	 * @param added if true the window was added
	 */
	private void updateViews(DockingWindow window, boolean added) {
		if (window instanceof View) {
			if (window instanceof DynamicView) {
				if (added)
					dynamicViews.put(new Integer(((DynamicView) window).getId()), window);
				else
					dynamicViews.remove(new Integer(((DynamicView) window).getId()));
			} else {
				for (int i = 0; i < views.length; i++)
					if (views[i] == window && viewItems[i] != null)
						viewItems[i].setEnabled(!added);
			}
		} else {
			for (int i = 0; i < window.getChildWindowCount(); i++)
				updateViews(window.getChildWindow(i), added);
		}
	}

	/**
	 * Sets the default window layout.
	 */
	private void setDefaultLayout() {
		TabWindow tabWindow = new TabWindow(views);

		rootWindow.setWindow(new SplitWindow(true, 0.3f, new SplitWindow(false, 0.7f, new TabWindow(new View[] { views[0], views[1] }), views[2]), tabWindow));

		WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);

		while (windowBar.getChildWindowCount() > 0)
			windowBar.getChildWindow(0).close();

		windowBar.addTab(views[3]);
	}

	/**
	 * Initializes the frame and shows it.
	 */
	private void showFrame() {
		frame.getContentPane().add(createToolBar(), BorderLayout.NORTH);
		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		frame.setJMenuBar(createMenuBar());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Creates the frame tool bar.
	 *
	 * @return the frame tool bar
	 */
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		JLabel label = new JLabel("Drag New View");
		toolBar.add(label);
		this.setDockingWindowDragSource(new DockingWindowDragSource(label, new DockingWindowDraggerProvider() {
			@Override
			public DockingWindowDragger getDragger(MouseEvent mouseEvent) {
				return getDynamicView(getDynamicViewId()).startDrag(rootWindow);
			}
		}));
		return toolBar;
	}

	/**
	 * Creates the frame menu bar.
	 *
	 * @return the menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.add(createLayoutMenu());
		menu.add(createFocusViewMenu());
		menu.add(createThemesMenu());
		menu.add(createPropertiesMenu());
		menu.add(createWindowBarsMenu());
		menu.add(createViewMenu());
		menu.add(createFloatingWindowMenu());
		return menu;
	}

	/**
	 * Creates the menu where layout can be saved/loaded and a frame shown with Java pseudo-like code over the current layout in the root window.
	 *
	 * @return the layout menu
	 */
	private JMenu createLayoutMenu() {
		JMenu layoutMenu = new JMenu("Layout");

		layoutMenu.add("Default Layout").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaultLayout();
			}
		});

		layoutMenu.addSeparator();

		for (int i = 0; i < layouts.length; i++) {
			final int j = i;

			layoutMenu.add("Save Layout " + i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// Save the layout in a byte array
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(bos);
						rootWindow.write(out, false);
						out.close();
						layouts[j] = bos.toByteArray();
					} catch (IOException e1) {
						throw new RuntimeException(e1);
					}
				}
			});
		}

		layoutMenu.addSeparator();

		for (int i = 0; i < layouts.length; i++) {
			final int j = i;

			layoutMenu.add("Load Layout " + j).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (layouts[j] != null) {
								try {
									// Load the layout from a byte array
									ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(layouts[j]));
									rootWindow.read(in, true);
									in.close();
								} catch (IOException e1) {
									throw new RuntimeException(e1);
								}
							}
						}
					});
				}
			});
		}

		layoutMenu.addSeparator();

		layoutMenu.add("Show Window Layout Frame").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeveloperUtil.createWindowLayoutFrame("Root Window Layout as Java Pseudo-like Code", rootWindow).setVisible(true);
			}
		});
		return layoutMenu;
	}

	/**
	 * Creates the menu where views can be shown and focused.
	 *
	 * @return the focus view menu
	 */
	private JMenu createFocusViewMenu() {
		JMenu viewsMenu = new JMenu("Focus View");

		for (int i = 0; i < views.length; i++) {
			final View view = views[i];
			viewsMenu.add("Focus " + view.getTitle()).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							// Ensure the view is shown in the root window
							DockingUtil.addWindow(view, rootWindow);

							// Transfer focus to the view
							view.restoreFocus();
						}
					});
				}
			});
		}

		return viewsMenu;
	}

	/**
	 * Creates the menu where the theme can be changed.
	 *
	 * @return the theme menu
	 */
	private JMenu createThemesMenu() {
		JMenu themesMenu = new JMenu("Themes");

		final RootWindowProperties titleBarStyleProperties = PropertiesUtil.createTitleBarStyleRootWindowProperties();

		final JCheckBoxMenuItem titleBarStyleItem = new JCheckBoxMenuItem("Title Bar Style Theme");
		titleBarStyleItem.setSelected(false);
		titleBarStyleItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (titleBarStyleItem.isSelected())
					properties.addSuperObject(titleBarStyleProperties);
				else
					properties.removeSuperObject(titleBarStyleProperties);
			}
		});

		themesMenu.add(titleBarStyleItem);
		themesMenu.add(new JSeparator());

		DockingWindowsTheme[] themes = { new DefaultDockingTheme(), new LookAndFeelDockingTheme(), new BlueHighlightDockingTheme(), new SlimFlatDockingTheme(),
				new GradientDockingTheme(), new ShapedGradientDockingTheme(), new SoftBlueIceDockingTheme(), new ClassicDockingTheme() };

		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < themes.length; i++) {
			final DockingWindowsTheme theme = themes[i];

			JRadioButtonMenuItem item = new JRadioButtonMenuItem(theme.getName());
			item.setSelected(i == 4);
			group.add(item);

			themesMenu.add(item).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Clear the modified properties values
					properties.getMap().clear(true);

					setTheme(theme);
				}
			});
		}

		return themesMenu;
	}

	/**
	 * Creates the menu where different property values can be modified.
	 *
	 * @return the properties menu
	 */
	private JMenu createPropertiesMenu() {
		JMenu buttonsMenu = new JMenu("Properties");

		buttonsMenu.add("Enable Close").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				properties.getDockingWindowProperties().setCloseEnabled(true);
			}
		});

		buttonsMenu.add("Hide Close Buttons").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				properties.getDockingWindowProperties().setCloseEnabled(false);
			}
		});

		buttonsMenu.add("Freeze Layout").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freezeLayout(true);
			}
		});

		buttonsMenu.add("Unfreeze Layout").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freezeLayout(false);
			}
		});

		return buttonsMenu;
	}

	/**
	 * Freezes or unfreezes the window layout and window operations.
	 *
	 * @param freeze true for freeze, otherwise false
	 */
	private void freezeLayout(boolean freeze) {
		// Freeze window operations
		properties.getDockingWindowProperties().setDragEnabled(!freeze);
		properties.getDockingWindowProperties().setCloseEnabled(!freeze);
		properties.getDockingWindowProperties().setMinimizeEnabled(!freeze);
		properties.getDockingWindowProperties().setRestoreEnabled(!freeze);
		properties.getDockingWindowProperties().setMaximizeEnabled(!freeze);
		properties.getDockingWindowProperties().setUndockEnabled(!freeze);
		properties.getDockingWindowProperties().setDockEnabled(!freeze);

		// Freeze tab reordering inside tabbed panel
		properties.getTabWindowProperties().getTabbedPanelProperties().setTabReorderEnabled(!freeze);
	}

	/**
	 * Creates the menu where individual window bars can be enabled and disabled.
	 *
	 * @return the window bar menu
	 */
	private JMenu createWindowBarsMenu() {
		JMenu barsMenu = new JMenu("Window Bars");

		for (int i = 0; i < 4; i++) {
			final Direction d = Direction.getDirections()[i];
			JCheckBoxMenuItem item = new JCheckBoxMenuItem("Toggle " + d);
			item.setSelected(d == Direction.DOWN);
			barsMenu.add(item).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Enable/disable the window bar
					rootWindow.getWindowBar(d).setEnabled(!rootWindow.getWindowBar(d).isEnabled());
				}
			});
		}

		return barsMenu;
	}

	/**
	 * Creates the menu where not shown views can be shown.
	 *
	 * @return the view menu
	 */
	private JMenu createViewMenu() {
		JMenu menu = new JMenu("Views");

		for (int i = 0; i < views.length; i++) {
			final View view = views[i];
			viewItems[i] = new JMenuItem(view.getTitle());
			viewItems[i].setEnabled(views[i].getRootWindow() == null);
			menu.add(viewItems[i]).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (view.getRootWindow() != null)
						view.restoreFocus();
					else {
						DockingUtil.addWindow(view, rootWindow);
					}
				}
			});
		}

		return menu;
	}

	/**
	 * Creates the menu where a floating window with a dynamic view can be created.
	 *
	 * @return the floating window menu
	 */
	private JMenu createFloatingWindowMenu() {
		JMenu menu = new JMenu("Floating Window");
		menu.add(enableMenuAndStatusLabelMenuItem);

		JMenuItem item1 = new JMenuItem("Create Floating Window with Dynamic View");
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Floating windows are created via the root window
				FloatingWindow fw = rootWindow.createFloatingWindow(new Point(50, 50), new Dimension(300, 200), getDynamicView(getDynamicViewId()));

				// Show the window
				fw.getTopLevelAncestor().setVisible(true);
			}
		});
		menu.add(item1);

		return menu;
	}

	/**
	 * Update the floating window by adding a menu bar and a status label if menu option is choosen.
	 *
	 * @param fw the floating window
	 */
	private void updateFloatingWindow(FloatingWindow fw) {
		// Only update with if menu is selected
		if (enableMenuAndStatusLabelMenuItem.isSelected()) {
			// Create a dummy menu bar as example
			JMenuBar bar = new JMenuBar();
			bar.add(new JMenu("Menu 1")).add(new JMenuItem("Menu 1 Item 1"));
			bar.add(new JMenu("Menu 2")).add(new JMenuItem("Menu 2 Item 1"));

			// Set it in the root pane of the floating window
			fw.getRootPane().setJMenuBar(bar);

			// Create and add a status label
			JLabel statusLabel = new JLabel("I'm a status label!");

			// Add it as the SOUTH component to the root pane's content pane. Note that the actual floating
			// window is placed in the CENTER position and must not be removed.
			fw.getRootPane().getContentPane().add(statusLabel, BorderLayout.SOUTH);
		}
	}

	/**
	 * Sets the docking windows theme.
	 *
	 * @param theme the docking windows theme
	 */
	private void setTheme(DockingWindowsTheme theme) {
		properties.replaceSuperObject(currentTheme.getRootWindowProperties(), theme.getRootWindowProperties());
		currentTheme = theme;
	}

	public static void main(String[] args) throws Exception {

		// Set InfoNode Look and Feel
		UIManager.setLookAndFeel(new InfoNodeLookAndFeel());

		// Docking windwos should be run in the Swing thread
		SwingUtilities.invokeLater(new Runnable() {
			private DockingWindowsExample dockingWindowsExample;

			@Override
			public void run() {
				setDockingWindowsExample(new DockingWindowsExample());
			}

			public DockingWindowsExample getDockingWindowsExample() {
				return dockingWindowsExample;
			}

			public void setDockingWindowsExample(DockingWindowsExample dockingWindowsExample) {
				this.dockingWindowsExample = dockingWindowsExample;
			}
		});
	}

	public DockingWindowDragSource getDockingWindowDragSource() {
		return dockingWindowDragSource;
	}

	public void setDockingWindowDragSource(DockingWindowDragSource dockingWindowDragSource) {
		this.dockingWindowDragSource = dockingWindowDragSource;
	}
}
