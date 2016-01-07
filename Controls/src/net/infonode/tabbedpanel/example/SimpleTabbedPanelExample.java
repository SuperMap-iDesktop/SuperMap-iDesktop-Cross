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

// $Id: SimpleTabbedPanelExample.java,v 1.22 2005/12/04 13:46:05 jesper Exp $

package net.infonode.tabbedpanel.example;

import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.componentpainter.SolidColorComponentPainter;
import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.gui.laf.InfoNodeLookAndFeelThemes;
import net.infonode.tabbedpanel.*;
import net.infonode.tabbedpanel.hover.TabbedPanelTitledTabHoverAction;
import net.infonode.tabbedpanel.hover.TitledTabTabbedPanelHoverAction;
import net.infonode.tabbedpanel.theme.*;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabSizePolicy;
import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.supermap.desktop.Application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is an example application that shows how to create a Tabbed Panel, Titled Tabs, modify properties and use themes.
 *
 * @author johan
 * @version $Revision: 1.22 $
 */
public class SimpleTabbedPanelExample {
	/**
	 * Create an empty TitledTabProperties object that will be added as super object to all titled tabs' TitledTabProperties objects so that we only need to set
	 * properties in this object instead of having to iterate and set property for every titled tab in the tabbed panel when we change a property for the titled
	 * tabs. Every property that is set in this object will override any value that is set for the same property in the titled tabs default properties or theme
	 * properties.
	 */
	private TitledTabProperties titledTabProperties = new TitledTabProperties();

	/**
	 * An array of all themes. A tabbed panel and titled tab always have a default theme (look) based on the current look and feel. However, to make it easy to
	 * add/remove a theme we add an empty theme called DefaultTheme that will represent the default theme.
	 */
	private TabbedPanelTitledTabTheme[] themes = new TabbedPanelTitledTabTheme[] { new DefaultTheme(), new LookAndFeelTheme(), new ClassicTheme(),
			new BlueHighlightTheme(), new SmallFlatTheme(), new GradientTheme(), new GradientTheme(true, true), new ShapedGradientTheme(),
			new ShapedGradientTheme(0f, 0f, new FixedColorProvider(new Color(150, 150, 150)), null) {
				@Override
				public String getName() {
					return super.getName() + " Flat with no Slopes";
				}
			} };

	/**
	 * Reference to current active theme
	 */
	private TabbedPanelTitledTabTheme activeTheme;

	private JFrame frame = new JFrame("Simple Tabbed Panel Example");
	private TabbedPanel tabbedPanel = new TabbedPanel();
	private int tabId;
	private boolean hoverEffectActive = false;

	/**
	 * Constructor
	 */
	private SimpleTabbedPanelExample() {
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		frame.getContentPane().add(tabbedPanel, BorderLayout.CENTER);

		// Set the "close all tabs" button as a tab area component
		tabbedPanel.setTabAreaComponents(new JComponent[] { createCloseAllTabsButton(tabbedPanel) });

		// Create 6 titled tabs and add them to the tabbed panel
		for (int i = 0; i < 6; i++)
			tabbedPanel.addTab(createTab());

		// Apply the default theme
		tabbedPanel.getProperties().addSuperObject(themes[0].getTabbedPanelProperties());
		titledTabProperties.addSuperObject(themes[0].getTitledTabProperties());
		activeTheme = themes[0];

		frame.setVisible(true);
	}

	/**
	 * Creates the menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createTabbedPanelMenu());
		menuBar.add(createTitledTabMenu());
		menuBar.add(createThemeMenu());
		menuBar.add(createHoverMenu());
		return menuBar;
	}

	/**
	 * Creates the Tabbed Panel menu
	 *
	 * @return the tabbed panel menu
	 */
	private JMenu createTabbedPanelMenu() {
		JMenu tabbedPanelMenu = new JMenu("Tabbed Panel");

		tabbedPanelMenu.add(createMenuItem("Add a Titled Tab", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.addTab(createTab());
			}
		}));

		tabbedPanelMenu.add(new JSeparator());

		// Get all avaliable directions
		Direction[] directions = Direction.getDirections();
		for (int i = 0; i < directions.length; i++) {
			final Direction direction = directions[i];
			tabbedPanelMenu.add(createMenuItem("Tab Area Orientation: " + direction.getName(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Sets the orientation (direction) for the tab area in the
					// TabbedPanelProperties
					// for the tabbed panel. The tabbed panel is automatically
					// updated.
					tabbedPanel.getProperties().setTabAreaOrientation(direction);
				}
			}));
		}

		tabbedPanelMenu.add(new JSeparator());

		// Get all available tab layout policies i.e. how the tabs can be laid
		// out in the tab area
		TabLayoutPolicy[] tabLayoutPolicies = TabLayoutPolicy.getLayoutPolicies();
		for (int i = 0; i < tabLayoutPolicies.length; i++) {
			final TabLayoutPolicy tabLayoutPolicy = tabLayoutPolicies[i];
			tabbedPanelMenu.add(createMenuItem("Tab Layout: " + tabLayoutPolicy.getName(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Sets the layout for the tab area in the
					// TabbedPanelProperties
					// for the tabbed panel. The tabbed panel is automatically
					// updated.
					tabbedPanel.getProperties().setTabLayoutPolicy(tabLayoutPolicy);
				}
			}));
		}

		tabbedPanelMenu.add(new JSeparator());

		// Get all available tab drop down list visible policies i.e. when to
		// show a button (as
		// a tab area component) that shows a drop down list of all the tabs in
		// the tabbed panel
		// where a tab can be selected.
		TabDropDownListVisiblePolicy[] tabDropDownListVisiblePolicies = TabDropDownListVisiblePolicy.getDropDownListVisiblePolicies();
		for (int i = 0; i < tabDropDownListVisiblePolicies.length; i++) {
			final TabDropDownListVisiblePolicy tabDropDownListVisiblePolicy = tabDropDownListVisiblePolicies[i];
			tabbedPanelMenu.add(createMenuItem("Tab Drop Down List: " + tabDropDownListVisiblePolicy.getName(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPanel.getProperties().setTabDropDownListVisiblePolicy(tabDropDownListVisiblePolicy);
				}
			}));
		}

		tabbedPanelMenu.add(new JSeparator());

		tabbedPanelMenu.add(createMenuItem("Shadow: Enable", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.getProperties().setShadowEnabled(!tabbedPanel.getProperties().getShadowEnabled());
				// Enable or disable (toggle) the shadow for the tabbed panel.
				// The tabbed panel is
				// automatically updated.
				((JMenuItem) e.getSource()).setText("Shadow: " + (tabbedPanel.getProperties().getShadowEnabled() ? "Disable" : "Enable"));
			}
		}));

		return tabbedPanelMenu;
	}

	/**
	 * Creates the Titled Tab menu
	 *
	 * @return the titled tab menu
	 */
	private JMenu createTitledTabMenu() {
		JMenu titledTabMenu = new JMenu("Titled Tab");

		// Get all avaliable directions
		Direction[] directions = Direction.getDirections();
		for (int i = 0; i < directions.length; i++) {
			final Direction direction = directions[i];
			titledTabMenu.add(createMenuItem("Direction: " + direction.getName(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					titledTabProperties.getNormalProperties().setDirection(direction);
				}
			}));
		}

		titledTabMenu.add(new JSeparator());

		// Get all size policies
		TitledTabSizePolicy[] titledTabSizePolicies = TitledTabSizePolicy.getSizePolicies();
		for (int i = 0; i < titledTabSizePolicies.length; i++) {
			final TitledTabSizePolicy titledTabSizePolicy = titledTabSizePolicies[i];
			titledTabMenu.add(createMenuItem("Size Policy: " + titledTabSizePolicy.getName(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					titledTabProperties.setSizePolicy(titledTabSizePolicy);
				}
			}));
		}

		return titledTabMenu;
	}

	/**
	 * Creates the theme menu
	 *
	 * @return the theme menu
	 */
	private JMenu createThemeMenu() {
		JMenu themeMenu = new JMenu("Theme");
		ButtonGroup buttonGroup = new ButtonGroup();

		// Create a menu item for all themes
		for (int i = 0; i < themes.length; i++) {
			JMenuItem themeItem = new JRadioButtonMenuItem(themes[i].getName());
			buttonGroup.add(themeItem);
			final int k = i;
			themeItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Apply the selected theme
					applyTheme(themes[k]);
				}
			});

			themeMenu.add(themeItem);

			// Set the first item (default theme) to be selected
			themeItem.setSelected(i == 0);
		}

		return themeMenu;
	}

	/**
	 * Creates the hover menu
	 *
	 * @return the thover menu
	 */
	private JMenu createHoverMenu() {
		// Creating a solid background painter with a blusih color. Using a
		// component painter instead of just background color so that it works
		// for themes that has component painter. NOTE that it still might not
		// work for all themes such as the GradientTheme!
		ComponentPainter backgroundPainter = new SolidColorComponentPainter(new FixedColorProvider(new Color(128, 128, 255)));

		// Creating hover properties object for tab
		TitledTabProperties titledTabHoverProperties = new TitledTabProperties();
		// Setting the background painter for the highlighted state
		titledTabHoverProperties.getHighlightedProperties().getShapedPanelProperties().setComponentPainter(backgroundPainter);

		// Creating hover properties object for tabbed panel
		TabbedPanelProperties tabbedPanelHoverProperties = new TabbedPanelProperties();
		// Setting the background painter for the content area
		tabbedPanelHoverProperties.getContentPanelProperties().getShapedPanelProperties().setComponentPainter(backgroundPainter);

		// Creating a hover action for the tabbed panel that uses both the
		// hover properties for the tabbed panel and the tab. The titledTabHoverProperties
		// will be used for all the tabs when the tabbed panel is hovered (in our case the
		// content area, see further down).
		final TabbedPanelTitledTabHoverAction tabbedPanelTitledTabHoverAction = new TabbedPanelTitledTabHoverAction(tabbedPanelHoverProperties,
				titledTabHoverProperties);
		// Creating a hover action for the tab that uses both the hover
		// properties for the tab and the tabbed panel. The tabbedPanelHoverProperties
		// will be used for the tabbed panel when the tab is hovered.
		final TitledTabTabbedPanelHoverAction titledTabTabbedPanelAction = new TitledTabTabbedPanelHoverAction(titledTabHoverProperties,
				tabbedPanelHoverProperties);

		JMenu hoverMenu = new JMenu("Hover");

		hoverMenu.add(createMenuItem("Activate Hover Effect", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abstractHoverMenu(tabbedPanelTitledTabHoverAction, titledTabTabbedPanelAction, e);
			}

		}));

		return hoverMenu;
	}

	private void abstractHoverMenu(final TabbedPanelTitledTabHoverAction tabbedPanelTitledTabHoverAction,
			final TitledTabTabbedPanelHoverAction titledTabTabbedPanelAction, ActionEvent e) {
		if (hoverEffectActive) {
			// Removing the hover actions
			tabbedPanel.getProperties().getContentPanelProperties().getMap().removeValue(TabbedPanelContentPanelProperties.HOVER_LISTENER);
			titledTabProperties.getMap().removeValue(TitledTabProperties.HOVER_LISTENER);

			hoverEffectActive = false;
			((JMenuItem) e.getSource()).setText("Activate Hover Effect");
		} else {
			// Check to see if hover is enabled
			if (TabbedUtils.isHoverEnabled()) {
				// Setting the hover actions as hover listener. Note that the
				// action is set for the content area and not for the entire
				// tabbed panel because we only want hovering for the content
				// area (and the tabs).
				tabbedPanel.getProperties().getContentPanelProperties().setHoverListener(tabbedPanelTitledTabHoverAction);
				// Setting the hover actions as hover listener for the tab.
				titledTabProperties.setHoverListener(titledTabTabbedPanelAction);

				hoverEffectActive = true;
				((JMenuItem) e.getSource()).setText("Deactivate Hover Effect");
			} else {
				// Hover is not enabled, most likely because the example was run using web start.
				showHoverDisabledDialog();
			}
		}
	}

	/**
	 * Shows a message dialog saying hover cannot enabled
	 */
	private void showHoverDisabledDialog() {
		JOptionPane
				.showMessageDialog(
						frame,
						"<html><body style='width: 300px;'>Cannot activate hover effects because AWTPermission \"listenToAllAWTEvents\" is not granted. This is most likely beacuse you are running the jar via web start and our jnlp file doesn't ask for security permission. Try downloading the jar and run it locally on your computer instead.</body></html>");
	}

	/**
	 * Creates a menu item
	 *
	 * @param text menu text
	 * @param listener action listener when menu item is selected
	 * @return the menu item
	 */
	private JMenuItem createMenuItem(String text, ActionListener listener) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(listener);
		return item;
	}

	/**
	 * Applies properties from a theme to tabbed panel and all the titled tabs.
	 *
	 * @param theme theme to apply
	 */
	private void applyTheme(TabbedPanelTitledTabTheme theme) {
		// Remove the previous theme. If we hadn't added the DefaultTheme the
		// first time we wouldn't have any theme to remove and thus we would
		// have had to implement a flag or theme reference to tell us if we
		// needed to remove a theme or not.
		tabbedPanel.getProperties().removeSuperObject(activeTheme.getTabbedPanelProperties());
		titledTabProperties.removeSuperObject(activeTheme.getTitledTabProperties());

		// Adding a super object means that any properties that are set in the
		// super
		// object will be used instead of the same properties in the default
		// properties. Note that if you have explicitly set a property, for
		// example setTabAreaOrientation in the properties for the tabbed
		// panel, then that property will not be affected by the super object
		// i.e. it will still return the same value after adding the super
		// object.
		tabbedPanel.getProperties().addSuperObject(theme.getTabbedPanelProperties());
		titledTabProperties.addSuperObject(theme.getTitledTabProperties());

		activeTheme = theme;
	}

	/**
	 * Creates a Titled Tab with a JTextArea as content component and a close button as title component
	 *
	 * @return the titled tab
	 */
	private TitledTab createTab() {
		TitledTab tab = new TitledTab("Tab " + tabId, null, new JTextArea("This is the content for Tab " + tabId), null);
		tabId++;

		// Set a close button as title component for the highlighted state. This
		// means that
		// the close button is only shown if the tab is highlighted.
		tab.setHighlightedStateTitleComponent(createCloseTabButton(tab));

		// By adding titledTabProperties as super object to all tabs we only
		// need to update
		// properties in titledTabProperties instead of having to iterate and
		// change properties
		// for all tabs.
		tab.getProperties().addSuperObject(titledTabProperties);
		return tab;
	}

	/**
	 * Creates a JButton with an X
	 *
	 * @return the button
	 */
	private JButton createXButton() {
		JButton closeButton = new JButton("X");
		closeButton.setOpaque(false);
		closeButton.setMargin(null);
		closeButton.setFont(closeButton.getFont().deriveFont(Font.BOLD).deriveFont((float) 10));
		closeButton.setBorder(new EmptyBorder(1, 1, 1, 1));
		return closeButton;
	}

	/**
	 * Creates a close tab button that closes the given tab when the button is selected
	 *
	 * @param tab the tab what will be closed when the button is pressed
	 * @return the close button
	 */
	private JButton createCloseTabButton(final TitledTab tab) {
		JButton closeButton = createXButton();
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tab.getTabbedPanel().removeTab(tab);
			}
		});
		return closeButton;
	}

	/**
	 * Creates a close all tabs button that closes all tabs in the given Tabbed Panel when the button is selected
	 *
	 * @param tabbedPanel the tabbed panel that the button will close all tabs in
	 * @return the close button
	 */
	private JButton createCloseAllTabsButton(final TabbedPanel tabbedPanel) {
		final JButton closeButton = createXButton();
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Iterate over all tabs and remove them.
				int tabCount = tabbedPanel.getTabCount();
				for (int i = 0; i < tabCount; i++)
					tabbedPanel.removeTab(tabbedPanel.getTabAt(0));
			}
		});
		tabbedPanel.addTabListener(new TabAdapter() {
			@Override
			public void tabAdded(TabEvent event) {
				closeButton.setVisible(true);
			}

			@Override
			public void tabRemoved(TabRemovedEvent event) {
				closeButton.setVisible(event.getTabbedPanel().getTabCount() > 0);
			}
		});
		return closeButton;
	}

	/**
	 * Program main method
	 *
	 * @param args these are not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			private SimpleTabbedPanelExample simpleTabbedPanelExample;

			@Override
			public void run() {
				try {
					// Set the InfoNode Look and Feel with the Gray Theme
					UIManager.setLookAndFeel(new InfoNodeLookAndFeel(InfoNodeLookAndFeelThemes.getGrayTheme()));
				} catch (UnsupportedLookAndFeelException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
				setSimpleTabbedPanelExample(new SimpleTabbedPanelExample());
			}

			public SimpleTabbedPanelExample getSimpleTabbedPanelExample() {
				return simpleTabbedPanelExample;
			}

			public void setSimpleTabbedPanelExample(SimpleTabbedPanelExample simpleTabbedPanelExample) {
				this.simpleTabbedPanelExample = simpleTabbedPanelExample;
			}
		});
	}
}