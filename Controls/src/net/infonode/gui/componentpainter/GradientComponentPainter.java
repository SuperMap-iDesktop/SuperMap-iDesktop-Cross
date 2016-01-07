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

// $Id: GradientComponentPainter.java,v 1.12 2009/02/05 15:57:56 jesper Exp $
package net.infonode.gui.componentpainter;

import java.awt.*;
import java.awt.image.MemoryImageSource;
import java.lang.ref.SoftReference;

import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.util.ColorUtil;
import net.infonode.util.Direction;
import net.infonode.util.ImageUtils;

/**
 * A painter that paints an gradient area specified by four corner colors.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.12 $
 */
public class GradientComponentPainter extends AbstractComponentPainter {
	private static final long serialVersionUID = 1;

	private final ColorProvider[] colorProviders = new ColorProvider[4];
	private transient Color[] colors;
	private final int size = 128;
	private transient SoftReference[] images;
	private transient boolean hasAlpha;

	/**
	 * Constructor.
	 *
	 * @param topLeftColor the top left corner color
	 * @param topRightColor the top right corner color
	 * @param bottomLeftColor the bottom left corner color
	 * @param bottomRightColor the bottom right corner color
	 */
	public GradientComponentPainter(Color topLeftColor, Color topRightColor, Color bottomLeftColor, Color bottomRightColor) {
		this(new FixedColorProvider(topLeftColor), new FixedColorProvider(topRightColor), new FixedColorProvider(bottomLeftColor), new FixedColorProvider(
				bottomRightColor));
	}

	/**
	 * Constructor.
	 *
	 * @param topLeftColor the top left corner color provider
	 * @param topRightColor the top right corner color provider
	 * @param bottomLeftColor the bottom left corner color provider
	 * @param bottomRightColor the bottom right corner color provider
	 */
	public GradientComponentPainter(ColorProvider topLeftColor, ColorProvider topRightColor, ColorProvider bottomLeftColor, ColorProvider bottomRightColor) {
		colorProviders[0] = topLeftColor;
		colorProviders[1] = topRightColor;
		colorProviders[2] = bottomLeftColor;
		colorProviders[3] = bottomRightColor;
	}

	@Override
	public void paint(Component component, Graphics graphics, int x, int y, int width, int height, Direction direction, boolean horizontalFlip,
			boolean verticalFlip) {
		updateColors(component);

		if (colors[0] != null && colors[1] != null && colors[2] != null && colors[3] != null) {
			if (colors[0].equals(colors[2]) && colors[1].equals(colors[3]) && colors[0].equals(colors[1])) {
				graphics.setColor(colors[0]);
				graphics.fillRect(x, y, width, height);
			} else {
				int imageIndex = direction.getValue() + (horizontalFlip ? 4 : 0) + (verticalFlip ? 8 : 0);
				SoftReference softReference = images[imageIndex];
				Image image = softReference == null ? null : (Image) softReference.get();

				if (image == null) {
					image = createGradientImage(fixColors(direction, horizontalFlip, verticalFlip));
					images[imageIndex] = new SoftReference(image);
				}

				graphics.drawImage(image, x, y, width, height, null);
			}
		}

	}

	private Color[] fixColors(Direction direction, boolean horizontalFlip, boolean verticalFlip) {
		Color[] fixcolors = new Color[4];
		Color colorTemp;

		if (horizontalFlip) {
			fixcolors[0] = colors[1];
			fixcolors[1] = colors[0];
			fixcolors[2] = colors[3];
			fixcolors[3] = colors[2];
		} else {
			fixcolors[0] = colors[0];
			fixcolors[1] = colors[1];
			fixcolors[2] = colors[2];
			fixcolors[3] = colors[3];
		}

		if (verticalFlip) {
			colorTemp = fixcolors[2];
			fixcolors[2] = fixcolors[0];
			fixcolors[0] = colorTemp;

			colorTemp = fixcolors[3];
			fixcolors[3] = fixcolors[1];
			fixcolors[1] = colorTemp;
		}

		if (direction == Direction.RIGHT) {
			return fixcolors;
		} else if (direction == Direction.DOWN) {
			colorTemp = fixcolors[0];
			fixcolors[0] = fixcolors[2];
			fixcolors[2] = fixcolors[3];
			fixcolors[3] = fixcolors[1];
			fixcolors[1] = colorTemp;
		} else if (direction == Direction.LEFT) {
			colorTemp = fixcolors[0];
			fixcolors[0] = fixcolors[3];
			fixcolors[3] = colorTemp;

			colorTemp = fixcolors[1];
			fixcolors[1] = fixcolors[2];
			fixcolors[2] = colorTemp;
		} else if (direction == Direction.UP) {
			colorTemp = fixcolors[0];
			fixcolors[0] = fixcolors[1];
			fixcolors[1] = fixcolors[3];
			fixcolors[3] = fixcolors[2];
			fixcolors[2] = colorTemp;
		}

		return fixcolors;
	}

	private Image createGradientImage(Color[] colors) {
		return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(size, size, ImageUtils.createGradientPixels(colors, size, size), 0, size));
	}

	private void updateColors(Component component) {
		if (images == null) {
			images = new SoftReference[16];
		}

		if (colors == null)
			colors = new Color[4];

		for (int i = 0; i < colors.length; i++) {
			Color color = colorProviders[i].getColor(component);

			if (color != null && !color.equals(colors[i])) {
				for (int j = 0; j < images.length; j++) {
					images[j] = null;
				}
			}

			colors[i] = color;
			hasAlpha |= color != null && color.getAlpha() != 255;
		}
	}

	@Override
	public boolean isOpaque(Component component) {
		updateColors(component);
		return !hasAlpha;
	}

	@Override
	public Color getColor(Component component) {
		updateColors(component);
		return ColorUtil.blend(ColorUtil.blend(colors[0], colors[1], 0.5), ColorUtil.blend(colors[2], colors[3], 0.5), 0.5);
	}
}
