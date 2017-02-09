package com.supermap.desktop.process.graphics;

import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * 四叉树，用来进行平面空间几何对象的位置存储和索引
 * TODO 后续使用希尔伯特曲线来做更为优化的几何对象索引
 * Created by highsad on 2017/1/19.
 */
public class QuadTree<T> {
	private HashMap<T, Rectangle2D.Double> outside = new HashMap();
	private QuadTree<T>.QuadNode root;
	private int maxCapacity = 32;
	private int minSize = 32;
	private int maxOutside = 32;

	public QuadTree() {
		this.root = new QuadTree.QuadNode(new Rectangle2D.Double(0.0D, 0.0D, 8000.0D, 6000.0D));
	}

	public QuadTree(Rectangle2D.Double bounds) {
		this.root = new QuadTree.QuadNode(bounds);
	}

	public void add(T o, Rectangle2D.Double bounds) {
		if (this.root.bounds.contains(bounds)) {
			this.root.add(o, (Rectangle2D.Double) bounds.clone());
		} else {
			this.outside.put(o, (Rectangle2D.Double) bounds.clone());
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
		Map.Entry<T, Rectangle2D.Double> entry = (Map.Entry) i.next();
		Rectangle2D.Double treeBounds = (Rectangle2D.Double) (entry.getValue()).clone();

		while (i.hasNext()) {
			entry = (Map.Entry) i.next();
			Rectangle2D.Double bounds = entry.getValue();
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

	public Collection<T> findContains(java.awt.geom.Point2D.Double p) {
		HashSet result = new HashSet();
		this.root.findContains(p, result);
		Iterator iterator = this.outside.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (((Rectangle2D.Double) entry.getValue()).contains(p)) {
				result.add(entry.getKey());
			}
		}

		return result;
	}

	public Collection<T> findIntersects(Rectangle2D r) {
		return this.findIntersects(new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight()));
	}

	public Collection<T> findIntersects(Rectangle2D.Double r) {
		HashSet result = new HashSet();
		this.root.findIntersects(r, result);
		Iterator iterator = this.outside.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (((Rectangle2D.Double) entry.getValue()).intersects(r)) {
				result.add(entry.getKey());
			}
		}

		return result;
	}

	public Collection<T> findInside(Rectangle2D.Double r) {
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
		private Rectangle2D.Double bounds;
		private HashMap<T, Rectangle2D.Double> objects;
		private QuadTree<T>.QuadNode northEast;
		private QuadTree<T>.QuadNode northWest;
		private QuadTree<T>.QuadNode southEast;
		private QuadTree<T>.QuadNode southWest;

		public QuadNode(Rectangle2D.Double bounds) {
			this.bounds = bounds;
			this.objects = new HashMap();
		}

		public Rectangle2D.Double getBounds() {
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

		public void add(T o, Rectangle2D.Double oBounds) {
			if (this.isLeaf() && this.objects.size() >= QuadTree.this.maxCapacity && this.bounds.width > (double) QuadTree.this.minSize && this.bounds.height > (double) QuadTree.this.minSize) {
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
				double hw = this.bounds.width / 2.0D;
				double hh = this.bounds.height / 2.0D;
				this.northWest = QuadTree.this.new QuadNode(new Rectangle2D.Double(this.bounds.x, this.bounds.y, hw, hh));
				this.northEast = QuadTree.this.new QuadNode(new Rectangle2D.Double(this.bounds.x + hw, this.bounds.y, this.bounds.width - hw, hh));
				this.southWest = QuadTree.this.new QuadNode(new Rectangle2D.Double(this.bounds.x, this.bounds.y + hh, hw, this.bounds.height - hh));
				this.southEast = QuadTree.this.new QuadNode(new Rectangle2D.Double(this.bounds.x + hw, this.bounds.y + hh, this.bounds.width - hw, this.bounds.height - hh));
				HashMap temp = this.objects;
				this.objects = new HashMap();
				Iterator iterator = temp.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle2D.Double> entry = (Map.Entry) iterator.next();
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

		public void findContains(java.awt.geom.Point2D.Double p, HashSet<T> result) {
			if (this.bounds.contains(p)) {
				Iterator iterator = this.objects.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle2D.Double> entry = (Map.Entry) iterator.next();
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

		public void findIntersects(Rectangle2D.Double r, HashSet<T> result) {
			if (this.bounds.intersects(r)) {
				int oldSize = result.size();
				Iterator iterator = this.objects.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle2D.Double> entry = (Map.Entry) iterator.next();
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

		public void findInside(Rectangle2D.Double r, HashSet<T> result) {
			if (this.bounds.intersects(r)) {
				Iterator iterator = this.objects.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry<T, Rectangle2D.Double> entry = (Map.Entry) iterator.next();
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
