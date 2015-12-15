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

// $Id: TranslatingShape.java,v 1.4 2005/02/16 11:28:13 jesper Exp $
package net.infonode.gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 */
public class TranslatingShape implements Shape {
	private Shape shape;
	private double doublex;
	private double doubley;

	public TranslatingShape(Shape shape, double dx, double dy) {
		this.shape = shape;
		this.doublex = dx;
		this.doubley = dy;
	}

	@Override
	public Rectangle getBounds() {
		Rectangle rectangle = shape.getBounds();
		rectangle.translate((int) doublex, (int) doubley);
		return rectangle;
	}

	@Override
	public Rectangle2D getBounds2D() {
		Rectangle2D rectangle2D = shape.getBounds2D();
		rectangle2D.setRect(rectangle2D.getMinX() + doublex, rectangle2D.getMinY() + doubley, rectangle2D.getWidth(), rectangle2D.getHeight());
		return rectangle2D;
	}

	@Override
	public boolean contains(double x, double y) {
		return shape.contains(x - doublex, y - doubley);
	}

	@Override
	public boolean contains(Point2D point2D) {
		return contains(point2D.getX(), point2D.getY());
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return shape.intersects(x - doublex, y - doubley, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D rectangle2D) {
		return intersects(rectangle2D.getMinX(), rectangle2D.getMinY(), rectangle2D.getWidth(), rectangle2D.getHeight());
	}

	@Override
	public boolean contains(double x, double y, double weight, double height) {
		return shape.contains(x - doublex, y - doubley, weight, height);
	}

	@Override
	public boolean contains(Rectangle2D rectangle2D) {
		return contains(rectangle2D.getMinX() - doublex, rectangle2D.getMinY() - doubley, rectangle2D.getWidth(), rectangle2D.getHeight());
	}

	@Override
	public PathIterator getPathIterator(AffineTransform affineTransform) {
		return new Iterator(shape.getPathIterator(affineTransform));
	}

	@Override
	public PathIterator getPathIterator(AffineTransform affineTransform, double flatness) {
		return new Iterator(shape.getPathIterator(affineTransform, flatness));
	}

	private class Iterator implements PathIterator {
		private PathIterator iteratorTemPathIterator;

		Iterator(PathIterator iterator) {
			this.iteratorTemPathIterator = iterator;
		}

		@Override
		public int getWindingRule() {
			return iteratorTemPathIterator.getWindingRule();
		}

		@Override
		public boolean isDone() {
			return iteratorTemPathIterator.isDone();
		}

		@Override
		public void next() {
			iteratorTemPathIterator.next();
		}

		@Override
		public int currentSegment(float[] coords) {
			int result = iteratorTemPathIterator.currentSegment(coords);
			int coordslength = coords.length;
			for (int i = 0; i < coordslength; i++) {
				int iCount = i++;
				coords[iCount] += doublex;
				coords[i] += doubley;
			}

			return result;
		}

		@Override
		public int currentSegment(double[] coords) {
			int result = iteratorTemPathIterator.currentSegment(coords);
			int coordsLength = coords.length;
			for (int i = 0; i < coordsLength; i++) {
				int count = i++;
				coords[count] += doublex;
				coords[i] += doubley;
			}

			return result;
		}
	}
}
