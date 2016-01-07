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

// $Id: PolygonBorder.java,v 1.8 2005/02/16 11:28:13 jesper Exp $
package net.infonode.gui.shaped.border;

import net.infonode.gui.colorprovider.ColorProvider;

import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.8 $
 */
public class PolygonBorder extends AbstractPolygonBorder {
	private static final long serialVersionUID = 1;

	private int[] coords;
	private float[] widthFactors;
	private float[] heightFactors;

	public PolygonBorder(ColorProvider lineColor, int[] coords, float[] widthFactors, float[] heightFactors) {
		super(lineColor);
		this.coords = coords;
		this.widthFactors = widthFactors;
		this.heightFactors = heightFactors;
	}

	public PolygonBorder(ColorProvider lineColor, ColorProvider highlightColor, int[] coords, float[] widthFactors, float[] heightFactors) {
		super(lineColor, highlightColor);
		this.coords = coords;
		this.widthFactors = widthFactors;
		this.heightFactors = heightFactors;
	}

	public PolygonBorder(ColorProvider lineColor, ColorProvider highlightColor, ColorProvider middleColor, ColorProvider shadowColor, int[] coords,
			float[] widthFactors, float[] heightFactors) {
		super(lineColor, highlightColor, middleColor, shadowColor);
		this.coords = coords;
		this.widthFactors = widthFactors;
		this.heightFactors = heightFactors;
	}

	@Override
	protected Polygon createPolygon(Component component, int width, int height) {
		int[] xc = new int[coords.length / 2];
		int[] yc = new int[coords.length / 2];
		int n = 0;

		for (int i = 0; i < xc.length; i++) {
			xc[i] = (int) (widthFactors[n] * width) + (int) (heightFactors[n] * height) + coords[n];
			n++;
			yc[i] = (int) (widthFactors[n] * width) + (int) (heightFactors[n] * height) + coords[n];
			n++;
		}

		return new Polygon(xc, yc, xc.length);
	}

}
