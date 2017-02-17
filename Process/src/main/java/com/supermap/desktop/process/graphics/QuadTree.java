package com.supermap.desktop.process.graphics;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/2/15.
 * 用来管理 IGraph 空间信息的存储结构。
 * 采用四叉树结构存储，一个范围 Rectangle 所覆盖的区块均添加该 Rectangle 的管理。
 */
public class QuadTree<T> {
	public final static Rectangle ZERO = new Rectangle(0, 0, 0, 0);

	private Vector<T> datas = new Vector<>();
	private Vector<T> outsideDatas = new Vector<>();
	private ConcurrentHashMap<T, Rectangle> boundsMap = new ConcurrentHashMap<>();
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
		if (this.bounds.contains(rect)) {
			if (!this.datas.contains(data)) {
				this.datas.add(data);
				this.root.add(data, rect);
			}
		} else {
			if (!this.outsideDatas.contains(data)) {
				this.outsideDatas.add(data);
			}
		}

		if (!this.boundsMap.containsKey(data)) {
			this.boundsMap.put(data, rect);
		}
	}

	public void remove(T data) {
		if (this.datas.contains(data)) {
			this.datas.remove(data);
			this.root.remove(data);
		}

		if (this.outsideDatas.contains(data)) {
			this.outsideDatas.remove(data);
		}

		if (this.boundsMap.containsKey(data)) {
			this.boundsMap.remove(data);
		}
	}

	public int getDatasCountInside() {
		return this.datas.size();
	}

	public int getDatasCountOutside() {
		return this.outsideDatas.size();
	}

	public Vector<T> getDatasInside() {
		return this.datas;
	}

	public Vector<T> getDatasOutside() {
		return this.outsideDatas;
	}

	public Vector<T> getAllDatas() {
		Vector<T> vector = new Vector<>();

		for (int i = 0; i < this.datas.size(); i++) {
			vector.add(this.datas.get(i));
		}

		for (int i = 0; i < this.outsideDatas.size(); i++) {
			vector.add(this.outsideDatas.get(i));
		}
		return vector;
	}

	public List<T> search(Point point) {
		return this.root.search(point);
	}

	public Rectangle getBounds(T data) {
		return this.boundsMap.get(data);
	}

	/**
	 * 重新组织
	 */
	public void reorganize() {
		this.datas.clear();
		this.outsideDatas.clear();
		this.root.reset();
		this.root.bounds = (Rectangle) this.bounds.clone();

		Iterator iterator = this.boundsMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<T, Rectangle> entry = (Map.Entry<T, Rectangle>) iterator.next();
			add(entry.getKey(), entry.getValue());
		}
	}

	private class QuadNode {
		private ConcurrentHashMap<T, Rectangle> datas = new ConcurrentHashMap<>();
		private Rectangle bounds;

		private QuadNode northWest;
		private QuadNode northEast;
		private QuadNode southWest;
		private QuadNode southEast;

		public QuadNode() {

		}

		public QuadNode(Rectangle bounds) {
			this.bounds = bounds;
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

		public List<T> search(Point point) {
			if (!this.bounds.contains(point)) {
				return null;
			}

			ArrayList<T> list = new ArrayList<>();
			Iterator iterator = this.datas.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<T, Rectangle> entry = (Map.Entry<T, Rectangle>) iterator.next();
				if (!list.contains(entry.getKey()) && entry.getValue().contains(point)) {
					list.add(entry.getKey());
				}
			}

			if (!this.isLeaf()) {
				addListUnique(list, this.northWest.search(point));
				addListUnique(list, this.northEast.search(point));
				addListUnique(list, this.southWest.search(point));
				addListUnique(list, this.southEast.search(point));
			}

			return list;
		}

		private <T> void addListUnique(List<T> list, List<T> src) {
			if (list == null || src == null || src.size() == 0) {
				return;
			}

			for (int i = 0; i < src.size(); i++) {
				if (!list.contains(src.get(i))) {
					list.add(src.get(i));
				}
			}
		}

		public Map<T, Rectangle> getDatas() {
			if (this.datas.size() == 0) {
				return null;
			}

			ConcurrentHashMap<T, Rectangle> map = new ConcurrentHashMap<>();
			map.putAll(this.datas);
			return map;
		}

		public void getDatas(Map map) {
			if (map == null) {
				return;
			}

			map.putAll(this.datas);
		}

		public Map<T, Rectangle> getAllDatas() {
			if (isLeaf()) {
				return getDatas();
			}

			ConcurrentHashMap<T, Rectangle> map = new ConcurrentHashMap<>();
			map.putAll(this.northWest.getAllDatas());
			map.putAll(this.northEast.getAllDatas());
			map.putAll(this.southWest.getAllDatas());
			map.putAll(this.southEast.getAllDatas());
			return map;
		}

		public void getAllDatas(Map map) {
			if (map == null) {
				return;
			}

			if (isLeaf()) {
				map.putAll(this.datas);
			} else {
				this.northWest.getAllDatas(map);
				this.northEast.getAllDatas(map);
				this.southWest.getAllDatas(map);
				this.southEast.getAllDatas(map);
			}
		}

		public void reset() {
			this.datas.clear();
			this.bounds = ZERO;
			if (!isLeaf()) {
				this.northWest.reset();
				this.northEast.reset();
				this.southWest.reset();
				this.southEast.reset();
				this.northWest = null;
				this.northEast = null;
				this.southWest = null;
				this.southEast = null;
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
