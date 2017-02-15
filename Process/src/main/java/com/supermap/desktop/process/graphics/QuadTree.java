package com.supermap.desktop.process.graphics;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/2/15.
 * 用来管理 IGraph 空间信息的存储结构。
 * 采用四叉树结构存储，一个范围 Rectangle 所覆盖的区块均添加该 Rectangle 的管理。
 */
public class QuadTree<T> {
	private Vector<T> datas = new Vector<>();
	private QuadNode root;
	private Rectangle bounds;
	private double minNodeWidth = 32;
	private double minNodeHeight = 32;

	public QuadTree() {
		this(new Rectangle(0, 0, 1920, 1080));
	}

	public QuadTree(Rectangle bounds) {
		this.bounds = bounds;
		this.root = new QuadNode(bounds);
	}

	public void add(T data, Rectangle rect) {
		if (this.datas.size() > 0) {
			insert(data, this.datas.size() - 1, bounds);
		} else {
			insert(data, 0, bounds);
		}
	}

	/**
	 * 将数据插入到指定 index 的位置
	 *
	 * @param data
	 * @param index
	 * @param rect
	 */
	public void insert(T data, int index, Rectangle rect) {
		if (!this.datas.contains(data)) {
			this.datas.add(index, data);
			this.root.add(data, bounds);
		}
	}

	public void remove(T data) {
		if (this.datas.contains(data)) {
			this.datas.remove(data);
			this.root.remove(data);
		}
	}

	private class QuadNode {
		private ConcurrentHashMap<T, Rectangle> datas = new ConcurrentHashMap<>();
		private Rectangle bounds;

		private QuadNode northWest;
		private QuadNode northEast;
		private QuadNode southWest;
		private QuadNode southEast;

		public QuadNode(Rectangle bounds) {

		}

		public void add(T data, Rectangle rect) {
			if (this.bounds.contains(rect)) {
				if (this.isLeaf() && rect.width >= QuadTree.this.minNodeWidth && rect.height >= QuadTree.this.minNodeHeight) {

					// 当 Bounds 的宽或者高小于了最小值，就不再继续分解，直接添加元素，以避免在当前实现逻辑之下，data 的 rect 过于小导致无限细化的问题
					split();
				}

				if (!this.isLeaf()) {
					if (this.northWest.bounds.intersects(rect)) {
						this.northWest.add(data, rect);
					}

					if (this.northEast.bounds.intersects(rect)) {
						this.northEast.add(data, rect);
					}

					if (this.southWest.bounds.intersects(rect)) {
						this.southWest.add(data, rect);
					}

					if (this.southEast.bounds.intersects(rect)) {
						this.southEast.add(data, rect);
					}
				} else {
					this.datas.put(data, rect);
				}
			} else if (this.bounds.intersects(rect)) {

				// 部分相交，以及恰好在中点，即不包含也不相交，那就收下了
				this.datas.put(data, rect);
			}
		}

		public void remove(T data) {
			if (isLeaf()) {
				this.datas.remove(data);
			} else {
				this.northWest.remove(data);
				this.northEast.remove(data);
				this.southWest.remove(data);
				this.southEast.remove(data);
			}
		}

		private void split() {
			if (this.isLeaf()) {
				double hw = this.bounds.getWidth() / 2.0D;
				double hh = this.bounds.getHeight() / 2.0D;
				this.northWest = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX(), this.bounds.getY(), hw, hh));
				this.northEast = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX() + hw, this.bounds.getY(), this.bounds.getWidth() - hw, hh));
				this.southWest = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX(), this.bounds.getY() + hh, hw, this.bounds.getHeight() - hh));
				this.southEast = new QuadNode(GraphicsUtil.createRectangle(this.bounds.getX() + hw, this.bounds.getY() + hh, this.bounds.getWidth() - hw, this.bounds.getHeight() - hh));
			}
		}

		private boolean isLeaf() {
			return this.northWest == null;
		}
	}
}
