package com.supermap.desktop.WorkflowView.graphics;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * 四叉树，用来进行平面空间几何对象的位置存储和索引
 * Created by highsad on 2017/1/19.
 */
public class QuadTreeTemp<T> {
	private HashMap<T, Rectangle> outside = new HashMap();
	private QuadTreeTemp<T>.QuadNode root;
	private int maxCapacity = 2;
	private int minSize = 32;
	private int maxOutside = 32;

	public QuadTreeTemp() {
		this.root = new QuadTreeTemp.QuadNode(new Rectangle(0, 0, 2000, 2000));
	}

	public QuadTreeTemp(Rectangle bounds) {
		this.root = new QuadTreeTemp.QuadNode(bounds);
	}

	public void add(T o, Rectangle bounds) {
		if (this.root.bounds.contains(bounds)) {
			this.root.add(o, (Rectangle) bounds.clone());
		} else {
			this.outside.put(o, (Rectangle) bounds.clone());
			if (this.outside.size() > this.maxOutside) {
				this.reorganize();
			}
		}

	}

	public void reorganize() {
		this.root.join();
		this.outside.putAll(this.root.objects);
		this.root.objects.clear();
		Iterator i = this.outside.entrySet().iterator();
		Map.Entry<T, Rectangle> entry = (Map.Entry) i.next();
		Rectangle treeBounds = (Rectangle) (entry.getValue()).clone();

		while (i.hasNext()) {
			entry = (Map.Entry) i.next();
			Rectangle bounds = entry.getValue();
			treeBounds.add(bounds);
		}

		this.root.bounds = treeBounds;
		i = this.outside.entrySet().iterator();

		while (i.hasNext()) {
			entry = (Map.Entry) i.next();
			this.root.add(entry.getKey(), entry.getValue());
		}

		this.outside.clear();
	}

	public void remove(T o) {
		this.outside.remove(o);
		this.root.remove(o);
	}

	public Collection<T> findAll() {
		return findInside(this.root.getBounds());
	}

	public Vector<T> getDatasInside() {
		Vector<T> re = new Vector<>();
		Collection<T> c = findAll();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			re.add((T) it.next());
		}
		return re;
	}

	public Vector<T> search(Point p) {
		Vector<T> re = new Vector<>();
		Collection<T> c = findContains(p);
		Iterator it = c.iterator();
		while (it.hasNext()) {
			re.add((T) it.next());
		}
		return re;
	}

	public Collection<T> findContains(Point p) {
		HashSet result = new HashSet();
		this.root.findContains(p, result);
		Iterator iterator = this.outside.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (((Rectangle) entry.getValue()).contains(p)) {
				result.add(entry.getKey());
			}
		}

		return result;
	}

	public Collection<T> findIntersects(Rectangle2D r) {
		return this.findIntersects(GraphicsUtil.createRectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight()));
	}

	public Collection<T> findIntersects(Rectangle r) {
		HashSet result = new HashSet();
		this.root.findIntersects(r, result);
		Iterator iterator = this.outside.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (((Rectangle) entry.getValue()).intersects(r)) {
				result.add(entry.getKey());
			}
		}

		return result;
	}

	public Collection<T> findInside(Rectangle r) {
		HashSet result = new HashSet();
		this.root.findInside(r, result);
		Iterator iterator = this.outside.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (r.contains((Rectangle2D) entry.getValue())) {
				result.add(entry.getKey());
			}
		}

		return result;
	}

	private class QuadNode {
		private Rectangle bounds;
		private HashMap<T, Rectangle> objects;
		private QuadNode northEast;
		private QuadNode northWest;
		private QuadNode southEast;
		private QuadNode southWest;

		public QuadNode(Rectangle bounds) {
			this.bounds = bounds;
			this.objects = new HashMap();
		}

		public Rectangle getBounds() {
			return this.bounds;
		}

		public boolean isLeaf() {
			return this.northEast == null;
		}

		public void remove(T o) {
			if (this.objects.remove(o) == null && !this.isLeaf()) {
				this.northEast.remove(o);
				this.northWest.remove(o);
				this.southEast.remove(o);
				this.southWest.remove(o);
			}

		}

		public void add(T o, Rectangle oBounds) {
			if (this.isLeaf() && this.objects.size() >= QuadTreeTemp.this.maxCapacity && this.bounds.width > (double) QuadTreeTemp.this.minSize && this.bounds.height > (double) QuadTreeTemp.this.minSize) {
				this.split();
			}

			if (!this.isLeaf() && !oBounds.contains(this.bounds)) {
				if (this.northEast.bounds.intersects(oBounds)) {
					this.northEast.add(o, oBounds);
				}

				if (this.northWest.bounds.intersects(oBounds)) {
					this.northWest.add(o, oBounds);
				}

				if (this.southEast.bounds.intersects(oBounds)) {
					this.southEast.add(o, oBounds);
				}

				if (this.southWest.bounds.intersects(oBounds)) {
					this.southWest.add(o, oBounds);
				}
			} else {
				this.objects.put(o, oBounds);
			}

		}

		public void split() {
			if (this.isLeaf()) {
				double hw = this.bounds.getWidth() / 2.0D;
				double hh = this.bounds.getHeight() / 2.0D;
				this.northWest = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX(), this.bounds.getY(), hw, hh));
				this.northEast = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX() + hw, this.bounds.getY(), this.bounds.getWidth() - hw, hh));
				this.southWest = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX(), this.bounds.getY() + hh, hw, this.bounds.getHeight() - hh));
				this.southEast = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX() + hw, this.bounds.getY() + hh, this.bounds.getWidth() - hw, this.bounds.getHeight() - hh));
				HashMap temp = this.objects;
				this.objects = new HashMap();
				Iterator iterator = temp.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle> entry = (Map.Entry) iterator.next();
					this.add(entry.getKey(), entry.getValue());
				}
			}

		}

		public void join() {
			if (!this.isLeaf()) {
				this.northWest.join();
				this.northEast.join();
				this.southWest.join();
				this.southEast.join();
				this.objects.putAll(this.northWest.objects);
				this.objects.putAll(this.northEast.objects);
				this.objects.putAll(this.southWest.objects);
				this.objects.putAll(this.southEast.objects);
				this.northWest = null;
				this.northEast = null;
				this.southWest = null;
				this.southEast = null;
			}

		}

		public void findContains(Point p, HashSet<T> result) {
			if (this.bounds.contains(p)) {
				Iterator iterator = this.objects.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle> entry = (Map.Entry) iterator.next();
					if ((entry.getValue()).contains(p)) {
						result.add(entry.getKey());
					}
				}

				if (!this.isLeaf()) {
					this.northWest.findContains(p, result);
					this.northEast.findContains(p, result);
					this.southWest.findContains(p, result);
					this.southEast.findContains(p, result);
				}
			}

		}

		public void findIntersects(Rectangle r, HashSet<T> result) {
			if (this.bounds.intersects(r)) {
				int oldSize = result.size();
				Iterator iterator = this.objects.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle> entry = (Map.Entry) iterator.next();
					if ((entry.getValue()).intersects(r)) {
						result.add(entry.getKey());
					}
				}

				if (!this.isLeaf()) {
					this.northWest.findIntersects(r, result);
					this.northEast.findIntersects(r, result);
					this.southWest.findIntersects(r, result);
					this.southEast.findIntersects(r, result);
				}
			}

		}

		public void findInside(Rectangle r, HashSet<T> result) {
			if (this.bounds.intersects(r)) {
				Iterator iterator = this.objects.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle> entry = (Map.Entry) iterator.next();
					if (r.contains(entry.getValue())) {
						result.add(entry.getKey());
					}
				}

				if (!this.isLeaf()) {
					this.northWest.findInside(r, result);
					this.northEast.findInside(r, result);
					this.southWest.findInside(r, result);
					this.southEast.findInside(r, result);
				}
			}

		}
	}
}
