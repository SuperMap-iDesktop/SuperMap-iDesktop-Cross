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

// $Id: ClassicTheme.java,v 1.15 2008/04/04 12:43:22 jesper Exp $

package net.infonode.tabbedpanel.theme;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUIDefaults;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.border.OpenContentBorder;
import net.infonode.tabbedpanel.internal.TwoColoredLineBorder;
import net.infonode.tabbedpanel.titledtab.TitledTabBorderSizePolicy;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.util.Direction;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * A theme with a "classic" look and with round edges for the titled tabs.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.15 $
 * @since ITP 1.2.0
 */
public class ClassicTheme extends TabbedPanelTitledTabTheme {
	private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();
	private TitledTabProperties titledTabProperties = new TitledTabProperties();
	private ColorProvider highlightColor = UIManagerColorProvider.TABBED_PANE_HIGHLIGHT;
	private ColorProvider shadowColor = UIManagerColorProvider.TABBED_PANE_SHADOW;
	private ColorProvider darkShadow = UIManagerColorProvider.TABBED_PANE_DARK_SHADOW;
	private int raised;

	private Border shadowBorder;

	/**
	 * Constructs a default Classic Theme
	 */
	public ClassicTheme() {
		this(2);
	}

	/**
	 * Constructs a Classic Theme
	 *
	 * @param raised number of pixels for the highlight raised effect
	 */
	public ClassicTheme(final int raised) {
		this.raised = raised;

		shadowBorder = new ShadowBorder();

		Border contentBorder = new CompoundBorder(new OpenContentBorder(highlightColor, darkShadow, null, 1), new OpenContentBorder(null, shadowColor, null, 1));
		tabbedPanelProperties.getContentPanelProperties().getComponentProperties().setBorder(contentBorder).setInsets(new Insets(1, 1, 1, 1));

		tabbedPanelProperties.setTabSpacing(-raised - 1).setShadowEnabled(false);

		tabbedPanelProperties.getTabAreaComponentsProperties().getComponentProperties().setBorder(doCreateTabBorder(false, false, true, true));

		Border normalBorder = createInsetsTabBorder(true, true, false);
		Border highlightBorder = doCreateTabBorder(true, true, true, false);
		Insets normalInsets = new Insets(0, 3, 0, 3);
		Insets highlightInsets = new Insets(0, (raised + 1) / 2 + (((raised + 1) & 1) == 1 ? 1 : 0) + normalInsets.left, 1, (raised + 1) / 2
				+ normalInsets.right);

		titledTabProperties.setHighlightedRaised(raised).setBorderSizePolicy(TitledTabBorderSizePolicy.INDIVIDUAL_SIZE);
		titledTabProperties.getNormalProperties().getComponentProperties().setBorder(normalBorder).setInsets(normalInsets);
		titledTabProperties.getHighlightedProperties().getComponentProperties().setBorder(highlightBorder).setInsets(highlightInsets);
	}

	/**
	 * Gets the name for this theme
	 *
	 * @return the name
	 */
	@Override
	public String getName() {
		return "Classic Theme";
	}

	/**
	 * Gets the TabbedPanelProperties for this theme
	 *
	 * @return the TabbedPanelProperties
	 */
	@Override
	public TabbedPanelProperties getTabbedPanelProperties() {
		return tabbedPanelProperties;
	}

	/**
	 * Gets the TitledTabProperties for this theme
	 *
	 * @return the TitledTabProperties
	 */
	@Override
	public TitledTabProperties getTitledTabProperties() {
		return titledTabProperties;
	}

	/**
	 * Creates a tab border with extra insets border
	 *
	 * @param roundEdges true for round edges
	 * @param open true for open
	 * @param highlight true for highlight
	 * @return the created border
	 */
	public Border createInsetsTabBorder(boolean roundEdges, boolean open, boolean highlight) {
		Border insetsBorder = new InsetsBorder();
		return new CompoundBorder(insetsBorder, doCreateTabBorder(roundEdges, open, highlight, true));
	}

	/**
	 * Creates a tab border without extra insets border
	 *
	 * @param roundEdges true for round edges
	 * @param open true for open
	 * @param highlight true for highlight
	 * @return the created border
	 */
	public Border createTabBorder(boolean roundEdges, boolean open, boolean highlight) {
		return doCreateTabBorder(roundEdges, open, true, highlight);
	}

	private ColorProvider createNormalHighlightColorProvider() {
		return new SetColorProvider();
	}

	private Border doCreateTabBorder(boolean roundEdges, boolean open, boolean highlight, final boolean equalInset) {
		return new CompoundBorder(new TwoColoredLineBorder(highlight ? highlightColor : createNormalHighlightColorProvider(), darkShadow, roundEdges, open) {
			@Override
			protected Insets getShapedBorderInsets(Component c) {
				return equalInset ? new Insets(1, 1, 1, 1) : super.getShapedBorderInsets(c);
			}
		}, shadowBorder);
	}

	class ShadowBorder implements Border {

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
			if (tp != null) {
				Direction d = tp.getProperties().getTabAreaOrientation();
				g.setColor(shadowColor.getColor());

				if (d == Direction.UP || d == Direction.DOWN)
					GraphicsUtil.drawOptimizedLine(g, x + width - 1, y, x + width - 1, y + height - 1);
				else
					GraphicsUtil.drawOptimizedLine(g, x, y + height - 1, x + width - 1, y + height - 1);
			}
		}

		@Override
		public Insets getBorderInsets(Component c) {
			TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
			if (tp != null) {
				Direction d = tp.getProperties().getTabAreaOrientation();
				return new Insets(0, 0, (d == Direction.LEFT || d == Direction.RIGHT) ? 1 : 0, (d == Direction.UP || d == Direction.DOWN) ? 1 : 0);
			}
			return new Insets(0, 0, 0, 0);
		}

	}

	class SetColorProvider implements ColorProvider {

		@Override
		public Color getColor() {
			Color highlightBackground = TabbedUIDefaults.getHighlightedStateBackground();
			highlightBackground = new Color(highlightBackground.getRed() == 0 ? 1 : highlightBackground.getRed(), highlightBackground.getGreen() == 0 ? 1
					: highlightBackground.getGreen(), highlightBackground.getBlue() == 0 ? 1 : highlightBackground.getBlue());

			Color normalBackgroundColor = TabbedUIDefaults.getNormalStateBackground();

			Color color = highlightColor.getColor();

			int r = (int) (normalBackgroundColor.getRed() * ((float) (color.getRed()) / (float) (highlightBackground.getRed())));
			int g = (int) (normalBackgroundColor.getGreen() * ((float) (color.getGreen()) / (float) (highlightBackground.getGreen())));
			int b = (int) (normalBackgroundColor.getBlue() * ((float) (color.getBlue()) / (float) (highlightBackground.getBlue())));

			r = (r + color.getRed()) / 2;
			g = (g + color.getGreen()) / 2;
			b = (b + color.getBlue()) / 2;

			return new Color(r > 255 ? 255 : r, g > 255 ? 255 : g, b > 255 ? 255 : b);
		}

		@Override
		public Color getColor(Component component) {
			return getColor();
		}

	}

	class InsetsBorder implements Border {

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			// do nothing
		}

		@Override
		public Insets getBorderInsets(Component c) {
			TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
			if (tp != null) {
				Direction d = tp.getProperties().getTabAreaOrientation();
				return new Insets(d == Direction.RIGHT || d == Direction.LEFT ? raised : 0, d == Direction.UP || d == Direction.DOWN ? raised : 0,
						d == Direction.RIGHT || d == Direction.LEFT ? 1 : 0, d == Direction.UP || d == Direction.DOWN ? 1 : 0);
			}
			return new Insets(0, 0, 0, 0);
		}

	}
}