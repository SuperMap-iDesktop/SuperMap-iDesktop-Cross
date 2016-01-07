/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */

// $Id: InfoNodeLookAndFeel.java,v 1.22 2005/12/04 13:46:03 jesper Exp $
package net.infonode.gui.laf;

import net.infonode.gui.icon.button.TreeIcon;
import net.infonode.gui.laf.ui.SlimComboBoxUI;
import net.infonode.gui.laf.ui.SlimInternalFrameUI;
import net.infonode.gui.laf.ui.SlimMenuItemUI;
import net.infonode.gui.laf.ui.SlimSplitPaneUI;
import net.infonode.util.ArrayUtil;
import net.infonode.util.ColorUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.supermap.desktop.Application;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * A Look and Feel that's based on Metal. It's slimmer and use other colors than the standard Metal Look and Feel. Under Java 1.5 the currect Metal theme is
 * stored when the InfoNode Look and Feel is applied, and restored when another Look and Feel is set. Under Java 1.4 or earlier it is not possible to get the
 * current theme and a DefaultMetalTheme is set instead.
 * <p>
 * To set the look and feel use:
 * 
 * <pre>
 * UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
 * </pre>
 * 
 * Or, if you want to use a different theme, use:
 * 
 * <pre>
 * InfoNodeLookAndFeelTheme theme = new InfoNodeLookAndFeelTheme(...);
 * // Modify the theme colors, fonts etc.
 * UIManager.setLookAndFeel(new InfoNodeLookAndFeel(theme));
 * </pre>
 * 
 * Do not modify the theme after it has been used in the look and feel!
 *
 * @author $Author: jesper $
 * @version $Revision: 1.22 $
 */
public class InfoNodeLookAndFeel extends MetalLookAndFeel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final UIManager.LookAndFeelInfo LOOK_AND_FEEL_INFO = new UIManager.LookAndFeelInfo("InfoNode", InfoNodeLookAndFeel.class.getName());

	private static MetalTheme oldMetalTheme;

	public static MetalTheme getOldMetalTheme() {
		return oldMetalTheme;
	}

	public static void setOldMetalTheme(MetalTheme oldMetalTheme) {
		InfoNodeLookAndFeel.oldMetalTheme = oldMetalTheme;
	}

	private transient InfoNodeLookAndFeelTheme theme;

	private transient DefaultMetalTheme defaultTheme = new DefaultMetalTheme() {
		@Override
		public ColorUIResource getPrimaryControlHighlight() {
			return theme.getPrimaryControlHighlightColor();
		}

		@Override
		public ColorUIResource getMenuBackground() {
			return theme.getControlColor();
		}

		@Override
		public ColorUIResource getControlHighlight() {
			return theme.getControlHighlightColor();
		}

		@Override
		public ColorUIResource getControl() {
			return theme.getControlColor();
		}

		@Override
		public ColorUIResource getControlShadow() {
			return theme.getControlShadowColor();
		}

		@Override
		public ColorUIResource getControlDarkShadow() {
			return theme.getControlDarkShadowColor();
		}

		// Scrollbars, popups etc.
		@Override
		public ColorUIResource getPrimaryControl() {
			return theme.getPrimaryControlColor();
		}

		@Override
		public ColorUIResource getPrimaryControlShadow() {
			return theme.getPrimaryControlShadowColor();
		}

		@Override
		public ColorUIResource getPrimaryControlDarkShadow() {
			return theme.getPrimaryControlDarkShadowColor();
		}

		// End scrollbars
		@Override
		public ColorUIResource getTextHighlightColor() {
			return theme.getSelectedTextBackgroundColor();
		}

		@Override
		public ColorUIResource getMenuSelectedBackground() {
			return theme.getSelectedMenuBackgroundColor();
		}

		@Override
		public ColorUIResource getWindowBackground() {
			return theme.getBackgroundColor();
		}

		@Override
		protected ColorUIResource getWhite() {
			return theme.getBackgroundColor();
		}

		@Override
		public ColorUIResource getDesktopColor() {
			return theme.getDesktopColor();
		}

		@Override
		public ColorUIResource getHighlightedTextColor() {
			return theme.getSelectedTextColor();
		}

		@Override
		protected ColorUIResource getBlack() {
			return theme.getTextColor();
		}

		@Override
		public ColorUIResource getMenuForeground() {
			return theme.getTextColor();
		}

		@Override
		public ColorUIResource getMenuSelectedForeground() {
			return theme.getSelectedMenuForegroundColor();
		}

		@Override
		public ColorUIResource getFocusColor() {
			return theme.getFocusColor();
		}

		@Override
		public ColorUIResource getControlDisabled() {
			return theme.getControlColor();
		}

		@Override
		public ColorUIResource getSystemTextColor() {
			return theme.getTextColor();
		}

		@Override
		public ColorUIResource getControlTextColor() {
			return theme.getTextColor();
		}

		@Override
		public ColorUIResource getInactiveControlTextColor() {
			return theme.getInactiveTextColor();
		}

		@Override
		public ColorUIResource getInactiveSystemTextColor() {
			return theme.getInactiveTextColor();
		}

		@Override
		public ColorUIResource getUserTextColor() {
			return theme.getTextColor();
		}

		// --------------- Fonts --------------------------
		@Override
		public FontUIResource getControlTextFont() {
			return getSystemTextFont();
		}

		@Override
		public FontUIResource getSystemTextFont() {
			return theme.getFont();
		}

		@Override
		public FontUIResource getUserTextFont() {
			return getSystemTextFont();
		}

		@Override
		public FontUIResource getMenuTextFont() {
			return getSystemTextFont();
		}

		@Override
		public FontUIResource getWindowTitleFont() {
			return getSystemTextFont();
		}

		@Override
		public FontUIResource getSubTextFont() {
			return getSystemTextFont();
		}

	};

	/**
	 * Constructor.
	 */
	public InfoNodeLookAndFeel() {
		this(new InfoNodeLookAndFeelTheme());
	}

	/**
	 * Constructor.
	 *
	 * @param theme the theme to use. Do not modify the theme after this constructor has been called!
	 */
	public InfoNodeLookAndFeel(InfoNodeLookAndFeelTheme theme) {
		this.theme = theme;
	}

	/**
	 * Gets the active theme
	 *
	 * @return the active theme
	 */
	public InfoNodeLookAndFeelTheme getTheme() {
		return theme;
	}

	@Override
	public void initialize() {
		super.initialize();

		if (oldMetalTheme == null) {
			// Try to obtain the old Metal theme if possible
			try {
				setOldMetalTheme((MetalTheme) MetalLookAndFeel.class.getMethod("getCurrentTheme", null).invoke(null, null));
			} catch (NoSuchMethodException e) {
				// Ignore
			} catch (IllegalAccessException e) {
				// Ignore
			} catch (InvocationTargetException e) {
				// Ignore
			}
		}

		setCurrentTheme(defaultTheme);
	}

	@Override
	public void uninitialize() {
		setCurrentTheme(oldMetalTheme == null ? new DefaultMetalTheme() : oldMetalTheme);
		setOldMetalTheme(null);
	}

	@Override
	public String getName() {
		return LOOK_AND_FEEL_INFO.getName();
	}

	@Override
	public String getDescription() {
		return "A slim look and feel based on Metal.";
	}

	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);

		try {

			Class slimSplitPaneUI = SlimSplitPaneUI.class;
			table.put("SplitPaneUI", slimSplitPaneUI.getName());
			table.put(slimSplitPaneUI.getName(), slimSplitPaneUI);

			Class slimInternalFrameUI = SlimInternalFrameUI.class;
			table.put("InternalFrameUI", slimInternalFrameUI.getName());
			table.put(slimInternalFrameUI.getName(), slimInternalFrameUI);

			Class slimComboBoxUI = SlimComboBoxUI.class;
			SlimComboBoxUI.setNormalBorder(theme.getListItemBorder());
			SlimComboBoxUI.setFocusBorder(theme.getListFocusedItemBorder());
			table.put("ComboBoxUI", slimComboBoxUI.getName());
			table.put(slimComboBoxUI.getName(), slimComboBoxUI);

			Class slimMenuItemUI = SlimMenuItemUI.class;
			table.put("MenuItemUI", slimMenuItemUI.getName());
			table.put(slimMenuItemUI.getName(), slimMenuItemUI);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static class MyListCellRenderer extends DefaultListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private transient Border normalBorder;
		private transient Border focusBorder;

		MyListCellRenderer(Border normalBorder, Border focusBorder) {
			this.normalBorder = normalBorder;
			this.focusBorder = focusBorder;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setBorder(cellHasFocus ? focusBorder : normalBorder);
			return label;
		}

		public static class UIResourceTemp extends MyListCellRenderer implements javax.swing.plaf.UIResource {
			public UIResourceTemp(Border normalBorder, Border focusBorder) {
				super(normalBorder, focusBorder);
			}
		}
	}

	@Override
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);

		Class iconClass = MetalLookAndFeel.class;
		UIResource menuItemBorder = new MetalBorders.MenuItemBorder() {
			@Override
			public Insets getBorderInsets(Component component) {
				return new Insets(2, 0, 2, 0);
			}
		};

		Object[] defaults = {
				"SplitPane.dividerSize",
				new Integer(theme.getSplitPaneDividerSize()),
				// "SplitPaneDivider.border", new BorderUIResource(splitPaneDividerBorder),
				// "SplitPane.border", new BorderUIResource(splitPaneBorder),

				// "TabbedPane.contentBorderInsets", tabbedPaneContentInsets,
				// "TabbedPane.tabAreaInsets", new InsetsUIResource(0, 0, 0, 0),
				// "TabbedPane.selectedTabPadInsets", new InsetsUIResource(0, 0, 0, 0),
				// "TabbedPane.tabInsets", new InsetsUIResource(0, 0, 0, 0),
				"TabbedPane.background",
				theme.getControlLightShadowColor(),
				// "TabbedPane.darkShadow", new Color(160, 160, 160),

				"ComboBox.selectionBackground",
				theme.getSelectedMenuBackgroundColor(),
				"ComboBox.selectionForeground",
				theme.getSelectedMenuForegroundColor(),

				"List.cellRenderer",
				new UIDefaults.ActiveValue() {
					@Override
					public Object createValue(UIDefaults table) {
						return new MyListCellRenderer.UIResourceTemp(theme.getListItemBorder(), theme.getListFocusedItemBorder());
					}
				},

				"ToolTip.foreground",
				theme.getTooltipForegroundColor(),
				"ToolTip.background",
				theme.getTooltipBackgroundColor(),

				"Viewport.background",
				theme.getBackgroundColor(),

				"ScrollBar.background",
				theme.getScrollBarBackgroundColor(),
				"ScrollBar.shadow",
				theme.getScrollBarBackgroundShadowColor(),
				"ScrollBar.width",
				new Integer(theme.getScrollBarWidth()),
				// "ScrollBar.border", new BorderUIResource(new LineBorder(Color.GRAY, 1)),

				// "ScrollBar.thumb", new RelativeColor(thumbColor, 0.8).getColor(),
				// "ScrollBar.thumbShadow", new RelativeColor(thumbColor, 0.7).getColor(),
				// "ScrollBar.thumbHighlight", new RelativeColor(thumbColor, 1).getColor(),

				/*
				 * "ScrollBar.thumb", new ColorUIResource(255, 255, 255), "ScrollBar.thumbShadow", new ColorUIResource(0, 255, 0), "ScrollBar.thumbHighlight",
				 * new ColorUIResource(255, 255, 255),
				 */

				"Table.focusCellBackground",
				new ColorUIResource(ColorUtil.mult(theme.getSelectedMenuBackgroundColor(), 1.40f)),
				"Table.focusCellForeground",
				theme.getSelectedMenuForegroundColor(),

				"TableHeader.cellBorder",
				theme.getTableHeaderCellBorder(),

				"InternalFrame.activeTitleBackground",
				theme.getActiveInternalFrameTitleBackgroundColor(),
				"InternalFrame.activeTitleForeground",
				theme.getActiveInternalFrameTitleForegroundColor(),
				"InternalFrame.activeTitleGradient",
				theme.getActiveInternalFrameTitleGradientColor(), // ColorUtil.mult(theme.getActiveInternalFrameTitleBackgroundColor(), 1.2),
				"InternalFrame.inactiveTitleBackground",
				theme.getInactiveInternalFrameTitleBackgroundColor(),
				"InternalFrame.inactiveTitleForeground",
				theme.getInactiveInternalFrameTitleForegroundColor(),
				"InternalFrame.inactiveTitleGradient",
				theme.getInactiveInternalFrameTitleGradientColor(), // ColorUtil.mult(theme.getInactiveInternalFrameTitleBackgroundColor(), 1.2),
				"InternalFrame.icon",
				theme.getInternalFrameIcon(),
				"InternalFrame.iconifyIcon",
				theme.getInternalFrameIconifyIcon(),
				"InternalFrame.minimizeIcon",
				theme.getInternalFrameMinimizeIcon(),
				"InternalFrame.maximizeIcon",
				theme.getInternalFrameMaximizeIcon(),
				"InternalFrame.closeIcon",
				theme.getInternalFrameCloseIcon(),
				"InternalFrame.border",
				theme.getInternalFrameBorder(),
				"InternalFrame.titleFont",
				theme.getInternalFrameTitleFont(),

				"MenuBar.border",
				theme.getMenuBarBorder(),

				"MenuItem.border",
				menuItemBorder,
				"Menu.border",
				menuItemBorder,
				// "CheckBoxMenuItem.border", menuItemBorder,
				// "RadioButtonMenuItem.border", menuItemBorder,

				"Spinner.border",
				theme.getTextFieldBorder(),
				"Spinner.background",
				new ColorUIResource(theme.getBackgroundColor()),

				"PopupMenu.border",
				theme.getPopupMenuBorder(),

				"TextField.border",
				theme.getTextFieldBorder(),
				"FormattedTextField.border",
				theme.getTextFieldBorder(),

				// "Button.border", new BorderUIResource(buttonBorder),
				// "Button.disabledShadow", new ColorUIResource(Color.GREEN), //ColorUtil.blend(textColor, controlColor, 0.5f)),
				"Button.textShiftOffset",
				new Integer(2),
				"Button.select",
				theme.getControlLightShadowColor(),
				// "Button.focus", focusColor,
				"Button.margin",
				theme.getButtonMargin(),
				"Button.disabledText",
				theme.getInactiveTextColor(),
				// "Button.background", buttonBackground.getColor(),

				"ToggleButton.margin", theme.getButtonMargin(), "ToggleButton.select", theme.getControlLightShadowColor(), "ToggleButton.textShiftOffset",
				new Integer(2),

				"Tree.openIcon", theme.getTreeOpenIcon(), "Tree.closedIcon", theme.getTreeClosedIcon(), "Tree.leafIcon", theme.getTreeLeafIcon(),
				"Tree.collapsedIcon", new IconUIResource(new TreeIcon(TreeIcon.PLUS, 10, 10, true, theme.getTextColor(), theme.getTreeIconBackgroundColor())),
				"Tree.expandedIcon", new IconUIResource(new TreeIcon(TreeIcon.MINUS, 10, 10, true, theme.getTextColor(), theme.getTreeIconBackgroundColor())),
				"Tree.leftChildIndent", new Integer(5), "Tree.rightChildIndent",
				new Integer(11),
				// "Tree.rowHeight", new Integer(12),

				"OptionPane.errorIcon", LookAndFeel.makeIcon(iconClass, "icons/Error.gif"), "OptionPane.informationIcon",
				LookAndFeel.makeIcon(iconClass, "icons/Inform.gif"), "OptionPane.warningIcon", LookAndFeel.makeIcon(iconClass, "icons/Warn.gif"),
				"OptionPane.questionIcon", LookAndFeel.makeIcon(iconClass, "icons/Question.gif"), "OptionPane.buttonFont", theme.getOptionPaneButtonFont(), };

		table.putDefaults(defaults);
	}

	/**
	 * Installs this look and feel with the {@link UIManager}, if it's not already installed.
	 */
	public static void install() {
		if (!ArrayUtil.contains(UIManager.getInstalledLookAndFeels(), LOOK_AND_FEEL_INFO))
			UIManager.installLookAndFeel(LOOK_AND_FEEL_INFO);
	}

}
