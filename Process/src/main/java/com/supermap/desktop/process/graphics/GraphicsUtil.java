package com.supermap.desktop.process.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

/**
 * Created by highsad on 2017/1/17.
 */
public class GraphicsUtil {
	public static final int OUT_TOP = 2;
	public static final int OUT_BOTTOM = 8;
	public static final int OUT_LEFT = 1;
	public static final int OUT_RIGHT = 4;

	private GraphicsUtil() {
	}

	public static boolean isPointValid(Point point) {
		return point != null && point.x != Integer.MAX_VALUE && point.x != Integer.MIN_VALUE
				&& point.y != Integer.MAX_VALUE && point.y != Integer.MIN_VALUE;
	}

	public static boolean isRegionValid(Rectangle rect) {
		return rect != null
				&& (rect.x != Integer.MAX_VALUE && rect.x != Integer.MIN_VALUE)
				&& (rect.y != Integer.MAX_VALUE && rect.y != Integer.MIN_VALUE)
				&& rect.width != 0 && rect.height != 0;
	}

	public static int getFontHeight(JComponent component) {
		return getFontHeight(component, component.getFont());
	}

	public static int getFontHeight(JComponent component, Font font) {
		return component == null ? -1 : component.getFontMetrics(font).getHeight();
	}

	public static Color transparentColor(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public static Rectangle createRectangle(Rectangle2D rectangle2D) {
		return createRectangle(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
	}

	public static Rectangle createRectangle(double x, double y, double width, double height) {
		Rectangle rectangle = new Rectangle();
		rectangle.setRect(x, y, width, height);
		return rectangle;
	}

	public static Rectangle createRectangle(Point start, Point end) {
		if (isPointValid(start) && isPointValid(end)) {
			int locationX = Math.min(start.x, end.x);
			int locationY = Math.min(start.y, end.y);
			int width = Math.abs(end.x - start.x);
			int height = Math.abs(end.y - start.y);
			return new Rectangle(locationX, locationY, width, height);
		} else {
			return null;
		}
	}

	public static Point chop(Shape shape, Point p) {
		Rectangle2D bounds = shape.getBounds2D();
		java.awt.geom.Point2D.Double ctr = new java.awt.geom.Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		double cx = -1.0D;
		double cy = -1.0D;
		double len = 1.7976931348623157E308D;
		PathIterator i = shape.getPathIterator(new AffineTransform(), 1.0D);
		double[] coords = new double[6];
		i.currentSegment(coords);
		double prevX = coords[0];
		double prevY = coords[1];
		double moveToX = prevX;
		double moveToY = prevY;
		i.next();

		while (!i.isDone()) {
			switch (i.currentSegment(coords)) {
				case 0:
					moveToX = coords[0];
					moveToY = coords[1];
					break;
				case 4:
					coords[0] = moveToX;
					coords[1] = moveToY;
			}

			java.awt.geom.Point2D.Double l = intersect(prevX, prevY, coords[0], coords[1], p.x, p.y, ctr.x, ctr.y);
			if (l != null) {
				double cl = length2(l.x, l.y, p.x, p.y);
				if (cl < len) {
					len = cl;
					cx = l.x;
					cy = l.y;
				}
			}

			prevX = coords[0];
			prevY = coords[1];
			i.next();
		}

		if (len == 1.7976931348623157E308D) {
			for (i = shape.getPathIterator(new AffineTransform(), 1.0D); !i.isDone(); i.next()) {
				i.currentSegment(coords);
				double l1 = length2(ctr.x, ctr.y, coords[0], coords[1]);
				if (l1 < len) {
					len = l1;
					cx = coords[0];
					cy = coords[1];
				}
			}
		}

		return new Point((int) cx, (int) cy);
	}

	public static boolean lineContainsPoint(int x1, int y1, int x2, int y2, int px, int py) {
		return lineContainsPoint(x1, y1, x2, y2, px, py, 3.0D);
	}

	public static boolean lineContainsPoint(int x1, int y1, int x2, int y2, int px, int py, double tolerance) {
		Rectangle r = new Rectangle(new Point(x1, y1));
		r.add(x2, y2);
		r.grow(Math.max(2, (int) Math.ceil(tolerance)), Math.max(2, (int) Math.ceil(tolerance)));
		if (!r.contains(px, py)) {
			return false;
		} else if (x1 == x2) {
			return (double) Math.abs(px - x1) <= tolerance;
		} else if (y1 == y2) {
			return (double) Math.abs(py - y1) <= tolerance;
		} else {
			double a = (double) (y1 - y2) / (double) (x1 - x2);
			double b = (double) y1 - a * (double) x1;
			double x = ((double) py - b) / a;
			double y = a * (double) px + b;
			return Math.min(Math.abs(x - (double) px), Math.abs(y - (double) py)) <= tolerance;
		}
	}

	public static boolean lineContainsPoint(double x1, double y1, double x2, double y2, double px, double py, double tolerance) {
		Rectangle2D.Double r = new Rectangle2D.Double(x1, y1, 0.0D, 0.0D);
		r.add(x2, y2);
		double grow = (double) Math.max(2, (int) Math.ceil(tolerance));
		r.x -= grow;
		r.y -= grow;
		r.width += grow * 2.0D;
		r.height += grow * 2.0D;
		if (!r.contains(px, py)) {
			return false;
		} else if (x1 == x2) {
			return Math.abs(px - x1) <= tolerance;
		} else if (y1 == y2) {
			return Math.abs(py - y1) <= tolerance;
		} else {
			double a = (y1 - y2) / (x1 - x2);
			double b = y1 - a * x1;
			double x = (py - b) / a;
			double y = a * px + b;
			return Math.min(Math.abs(x - px), Math.abs(y - py)) <= tolerance;
		}
	}

	public static int direction(int x1, int y1, int x2, int y2) {
		boolean direction = false;
		int vx = x2 - x1;
		int vy = y2 - y1;
		byte direction1;
		if (vy < vx && vx > -vy) {
			direction1 = 4;
		} else if (vy > vx && vy > -vx) {
			direction1 = 2;
		} else if (vx < vy && vx < -vy) {
			direction1 = 1;
		} else {
			direction1 = 8;
		}

		return direction1;
	}

	public static int direction(double x1, double y1, double x2, double y2) {
		boolean direction = false;
		double vx = x2 - x1;
		double vy = y2 - y1;
		byte direction1;
		if (vy < vx && vx > -vy) {
			direction1 = 4;
		} else if (vy > vx && vy > -vx) {
			direction1 = 2;
		} else if (vx < vy && vx < -vy) {
			direction1 = 1;
		} else {
			direction1 = 8;
		}

		return direction1;
	}

	public static int outcode(Rectangle r1, Rectangle r2) {
		int outcode = 0;
		if (r2.x > r1.x + r1.width) {
			outcode = 4;
		} else if (r2.x + r2.width < r1.x) {
			outcode = 1;
		}

		if (r2.y > r1.y + r1.height) {
			outcode |= 8;
		} else if (r2.y + r2.height < r1.y) {
			outcode |= 2;
		}

		return outcode;
	}

	public static int outcode(Rectangle2D.Double r1, Rectangle2D.Double r2) {
		int outcode = 0;
		if (r2.x > r1.x + r1.width) {
			outcode = 4;
		} else if (r2.x + r2.width < r1.x) {
			outcode = 1;
		}

		if (r2.y > r1.y + r1.height) {
			outcode |= 8;
		} else if (r2.y + r2.height < r1.y) {
			outcode |= 2;
		}

		return outcode;
	}

	public static Point south(Rectangle r) {
		return new Point(r.x + r.width / 2, r.y + r.height);
	}

	public static java.awt.geom.Point2D.Double south(Rectangle2D.Double r) {
		return new java.awt.geom.Point2D.Double(r.x + r.width / 2.0D, r.y + r.height);
	}

	public static Point center(Rectangle r) {
		return new Point(r.x + r.width / 2, r.y + r.height / 2);
	}

	public static java.awt.geom.Point2D.Double center(Rectangle2D.Double r) {
		return new java.awt.geom.Point2D.Double(r.x + r.width / 2.0D, r.y + r.height / 2.0D);
	}

	public static java.awt.geom.Point2D.Double chop(Shape shape, java.awt.geom.Point2D.Double p) {
		Rectangle2D bounds = shape.getBounds2D();
		java.awt.geom.Point2D.Double ctr = new java.awt.geom.Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		double cx = -1.0D;
		double cy = -1.0D;
		double len = 1.7976931348623157E308D;
		PathIterator i = shape.getPathIterator(new AffineTransform(), 1.0D);
		double[] coords = new double[6];
		i.currentSegment(coords);
		double prevX = coords[0];
		double prevY = coords[1];
		double moveToX = prevX;
		double moveToY = prevY;
		i.next();

		while (!i.isDone()) {
			switch (i.currentSegment(coords)) {
				case 0:
					moveToX = coords[0];
					moveToY = coords[1];
					break;
				case 4:
					coords[0] = moveToX;
					coords[1] = moveToY;
			}

			java.awt.geom.Point2D.Double l = intersect(prevX, prevY, coords[0], coords[1], p.x, p.y, ctr.x, ctr.y);
			if (l != null) {
				double cl = length2(l.x, l.y, p.x, p.y);
				if (cl < len) {
					len = cl;
					cx = l.x;
					cy = l.y;
				}
			}

			prevX = coords[0];
			prevY = coords[1];
			i.next();
		}

		if (len == 1.7976931348623157E308D) {
			for (i = shape.getPathIterator(new AffineTransform(), 1.0D); !i.isDone(); i.next()) {
				i.currentSegment(coords);
				double l1 = length2(ctr.x, ctr.y, coords[0], coords[1]);
				if (l1 < len) {
					len = l1;
					cx = coords[0];
					cy = coords[1];
				}
			}
		}

		return new java.awt.geom.Point2D.Double(cx, cy);
	}

	public static Point west(Rectangle r) {
		return new Point(r.x, r.y + r.height / 2);
	}

	public static java.awt.geom.Point2D.Double west(Rectangle2D.Double r) {
		return new java.awt.geom.Point2D.Double(r.x, r.y + r.height / 2.0D);
	}

	public static Point east(Rectangle r) {
		return new Point(r.x + r.width, r.y + r.height / 2);
	}

	public static java.awt.geom.Point2D.Double east(Rectangle2D.Double r) {
		return new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height / 2.0D);
	}

	public static Point north(Rectangle r) {
		return new Point(r.x + r.width / 2, r.y);
	}

	public static java.awt.geom.Point2D.Double north(Rectangle2D.Double r) {
		return new java.awt.geom.Point2D.Double(r.x + r.width / 2.0D, r.y);
	}

	public static int range(int min, int max, int value) {
		if (value < min) {
			value = min;
		}

		if (value > max) {
			value = max;
		}

		return value;
	}

	public static double range(double min, double max, double value) {
		if (value < min) {
			value = min;
		}

		if (value > max) {
			value = max;
		}

		return value;
	}

	public static long length2(int x1, int y1, int x2, int y2) {
		return (long) ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	public static long length(int x1, int y1, int x2, int y2) {
		return (long) Math.sqrt((double) length2(x1, y1, x2, y2));
	}

	public static double length2(double x1, double y1, double x2, double y2) {
		return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
	}

	public static double length(double x1, double y1, double x2, double y2) {
		return Math.sqrt(length2(x1, y1, x2, y2));
	}

	public static double length(java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2) {
		return Math.sqrt(length2(p1.x, p1.y, p2.x, p2.y));
	}

	public static java.awt.geom.Point2D.Double cap(java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2, double radius) {
		double angle = 1.5707963267948966D - Math.atan2(p2.x - p1.x, p2.y - p1.y);
		java.awt.geom.Point2D.Double p3 = new java.awt.geom.Point2D.Double(p2.x + radius * Math.cos(angle), p2.y + radius * Math.sin(angle));
		return p3;
	}

	public static double pointToAngle(Rectangle r, Point p) {
		int px = p.x - (r.x + r.width / 2);
		int py = p.y - (r.y + r.height / 2);
		return Math.atan2((double) (py * r.width), (double) (px * r.height));
	}

	public static double pointToAngle(Rectangle2D.Double r, java.awt.geom.Point2D.Double p) {
		double px = p.x - (r.x + r.width / 2.0D);
		double py = p.y - (r.y + r.height / 2.0D);
		return Math.atan2(py * r.width, px * r.height);
	}

	public static double angle(double x1, double y1, double x2, double y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}

	public static Point angleToPoint(Rectangle r, double angle) {
		double si = Math.sin(angle);
		double co = Math.cos(angle);
		double e = 1.0E-4D;
		int x = 0;
		int y = 0;
		if (Math.abs(si) > e) {
			x = (int) ((1.0D + co / Math.abs(si)) / 2.0D * (double) r.width);
			x = range(0, r.width, x);
		} else if (co >= 0.0D) {
			x = r.width;
		}

		if (Math.abs(co) > e) {
			y = (int) ((1.0D + si / Math.abs(co)) / 2.0D * (double) r.height);
			y = range(0, r.height, y);
		} else if (si >= 0.0D) {
			y = r.height;
		}

		return new Point(r.x + x, r.y + y);
	}

	public static java.awt.geom.Point2D.Double angleToPoint(Rectangle2D.Double r, double angle) {
		double si = Math.sin(angle);
		double co = Math.cos(angle);
		double e = 1.0E-4D;
		double x = 0.0D;
		double y = 0.0D;
		if (Math.abs(si) > e) {
			x = (1.0D + co / Math.abs(si)) / 2.0D * r.width;
			x = range(0.0D, r.width, x);
		} else if (co >= 0.0D) {
			x = r.width;
		}

		if (Math.abs(co) > e) {
			y = (1.0D + si / Math.abs(co)) / 2.0D * r.height;
			y = range(0.0D, r.height, y);
		} else if (si >= 0.0D) {
			y = r.height;
		}

		return new java.awt.geom.Point2D.Double(r.x + x, r.y + y);
	}

	public static Point polarToPoint(double angle, double fx, double fy) {
		double si = Math.sin(angle);
		double co = Math.cos(angle);
		return new Point((int) (fx * co + 0.5D), (int) (fy * si + 0.5D));
	}

	public static java.awt.geom.Point2D.Double polarToPoint2D(double angle, double fx, double fy) {
		double si = Math.sin(angle);
		double co = Math.cos(angle);
		return new java.awt.geom.Point2D.Double(fx * co + 0.5D, fy * si + 0.5D);
	}

	public static Point ovalAngleToPoint(Rectangle r, double angle) {
		Point center = center(r);
		Point p = polarToPoint(angle, (double) (r.width / 2), (double) (r.height / 2));
		return new Point(center.x + p.x, center.y + p.y);
	}

	public static java.awt.geom.Point2D.Double ovalAngleToPoint(Rectangle2D.Double r, double angle) {
		java.awt.geom.Point2D.Double center = center(r);
		java.awt.geom.Point2D.Double p = polarToPoint2D(angle, r.width / 2.0D, r.height / 2.0D);
		return new java.awt.geom.Point2D.Double(center.x + p.x, center.y + p.y);
	}

	public static Point intersect(int xa, int ya, int xb, int yb, int xc, int yc, int xd, int yd) {
		double denom = (double) ((xb - xa) * (yd - yc) - (yb - ya) * (xd - xc));
		double rnum = (double) ((ya - yc) * (xd - xc) - (xa - xc) * (yd - yc));
		if (denom == 0.0D) {
			return rnum != 0.0D ? null : ((xa >= xb || xb >= xc && xb >= xd) && (xa <= xb || xb <= xc && xb <= xd) ? new Point(xa, ya) : new Point(xb, yb));
		} else {
			double r = rnum / denom;
			double snum = (double) ((ya - yc) * (xb - xa) - (xa - xc) * (yb - ya));
			double s = snum / denom;
			if (0.0D <= r && r <= 1.0D && 0.0D <= s && s <= 1.0D) {
				int px = (int) ((double) xa + (double) (xb - xa) * r);
				int py = (int) ((double) ya + (double) (yb - ya) * r);
				return new Point(px, py);
			} else {
				return null;
			}
		}
	}

	public static java.awt.geom.Point2D.Double intersect(double xa, double ya, double xb, double yb, double xc, double yc, double xd, double yd) {
		double denom = (xb - xa) * (yd - yc) - (yb - ya) * (xd - xc);
		double rnum = (ya - yc) * (xd - xc) - (xa - xc) * (yd - yc);
		if (denom == 0.0D) {
			return rnum != 0.0D ? null : ((xa >= xb || xb >= xc && xb >= xd) && (xa <= xb || xb <= xc && xb <= xd) ? new java.awt.geom.Point2D.Double(xa, ya) : new java.awt.geom.Point2D.Double(xb, yb));
		} else {
			double r = rnum / denom;
			double snum = (ya - yc) * (xb - xa) - (xa - xc) * (yb - ya);
			double s = snum / denom;
			if (0.0D <= r && r <= 1.0D && 0.0D <= s && s <= 1.0D) {
				double px = xa + (xb - xa) * r;
				double py = ya + (yb - ya) * r;
				return new java.awt.geom.Point2D.Double(px, py);
			} else {
				return null;
			}
		}
	}

	public static java.awt.geom.Point2D.Double intersect(double xa, double ya, double xb, double yb, double xc, double yc, double xd, double yd, double limit) {
		double denom = (xb - xa) * (yd - yc) - (yb - ya) * (xd - xc);
		double rnum = (ya - yc) * (xd - xc) - (xa - xc) * (yd - yc);
		if (denom == 0.0D) {
			return rnum != 0.0D ? null : ((xa >= xb || xb >= xc && xb >= xd) && (xa <= xb || xb <= xc && xb <= xd) ? new java.awt.geom.Point2D.Double(xa, ya) : new java.awt.geom.Point2D.Double(xb, yb));
		} else {
			double r = rnum / denom;
			double snum = (ya - yc) * (xb - xa) - (xa - xc) * (yb - ya);
			double s = snum / denom;
			double px;
			double py;
			if (0.0D <= r && r <= 1.0D && 0.0D <= s && s <= 1.0D) {
				px = xa + (xb - xa) * r;
				py = ya + (yb - ya) * r;
				return new java.awt.geom.Point2D.Double(px, py);
			} else {
				px = xa + (xb - xa) * r;
				py = ya + (yb - ya) * r;
				return length(xa, ya, px, py) > limit && length(xb, yb, px, py) > limit && length(xc, yc, px, py) > limit && length(xd, yd, px, py) > limit ? null : new java.awt.geom.Point2D.Double(px, py);
			}
		}
	}

	public static double distanceFromLine(int xa, int ya, int xb, int yb, int xc, int yc) {
		int xdiff = xb - xa;
		int ydiff = yb - ya;
		long l2 = (long) (xdiff * xdiff + ydiff * ydiff);
		if (l2 == 0L) {
			return (double) length(xa, ya, xc, yc);
		} else {
			double rnum = (double) ((ya - yc) * (ya - yb) - (xa - xc) * (xb - xa));
			double r = rnum / (double) l2;
			if (r >= 0.0D && r <= 1.0D) {
				double xi = (double) xa + r * (double) xdiff;
				double yi = (double) ya + r * (double) ydiff;
				double xd = (double) xc - xi;
				double yd = (double) yc - yi;
				return Math.sqrt(xd * xd + yd * yd);
			} else {
				return 1.7976931348623157E308D;
			}
		}
	}

	public static void grow(Rectangle2D.Double r, double h, double v) {
		r.x -= h;
		r.y -= v;
		r.width += h * 2.0D;
		r.height += v * 2.0D;
	}

	public static boolean contains(Rectangle2D.Double r1, Rectangle2D.Double r2) {
		return r2.x >= r1.x && r2.y >= r1.y && r2.x + Math.max(0.0D, r2.width) <= r1.x + Math.max(0.0D, r1.width) && r2.y + Math.max(0.0D, r2.height) <= r1.y + Math.max(0.0D, r1.height);
	}

	public static boolean contains(Rectangle2D r1, Rectangle2D r2) {
		return r2.getX() >= r1.getX() && r2.getY() >= r1.getY() && r2.getX() + Math.max(0.0D, r2.getWidth()) <= r1.getX() + Math.max(0.0D, r1.getWidth()) && r2.getY() + Math.max(0.0D, r2.getHeight()) <= r1.getY() + Math.max(0.0D, r1.getHeight());
	}
}
