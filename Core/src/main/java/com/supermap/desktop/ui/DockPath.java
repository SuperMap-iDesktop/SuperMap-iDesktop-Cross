package com.supermap.desktop.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by highsad on 2016/11/17.
 */
public class DockPath {

	public static final DockPath ROOT = new DockPath();

	private DockPath relateTo;
	private Direction direction = Direction.LEFT;
	private double ratio = 0.5;

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public DockPath getRelateTo() {
		return relateTo;
	}

	public void setRelateTo(DockPath relateTo) {
		this.relateTo = relateTo;
	}

	//	/**
//	 * Dock 方位
//	 */
//	private List<Direction> directions = new ArrayList<>();
//
//
//	/**
//	 * 最后 Dock 位置与相邻同边界 Dockbar 所占的比例
//	 */
//	private double ratio = 0.5;
//
//	/**
//	 * 获取 DockPat 路径
//	 *
//	 * @return
//	 */
//	public Direction[] getDirections() {
//		return this.directions.toArray(new Direction[this.directions.size()]);
//	}
//
//	public double getRatio() {
//		return ratio;
//	}
//
//	public void setRatio(double ratio) {
//		this.ratio = ratio;
//	}
//
//	/**
//	 * 获取路径深度（相对于中间主视图）
//	 *
//	 * @return
//	 */
//	public int getDepth() {
//		return this.directions.size();
//	}
//
//	/**
//	 * 在最后位置添加 DockPath
//	 *
//	 * @param direction
//	 */
//	public void addDirection(Direction direction) {
//		this.directions.add(direction);
//	}
//
//	/**
//	 * 移除从 index 位置往后的所有 DockPath
//	 *
//	 * @param index
//	 */
//	public void removeDirection(int index) {
//		if (index < this.directions.size()) {
//			for (int i = this.directions.size() - 1; i >= index; i--) {
//				this.directions.remove(i);
//			}
//		}
//	}
//
//	/**
//	 * 清空路径
//	 */
//	public void clear() {
//		this.directions.clear();
//		this.ratio = 0.5;
//	}
//
//	/**
//	 * 使用指定的 dockPath 初始化
//	 *
//	 * @param dockPath
//	 */
//	public void init(DockPath dockPath) {
//		if (dockPath != null) {
//			this.ratio = dockPath.getRatio();
//			this.directions.clear();
//			Direction[] directions = dockPath.getDirections();
//			for (int i = 0; i < directions.length; i++) {
//				this.directions.add(directions[i]);
//			}
//		}
//	}
}
