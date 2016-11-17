package com.supermap.desktop.ui;

import com.supermap.desktop.properties.CoreProperties;

/**
 * Created by highsad on 2016/11/17.
 */
public class Direction {
	private String direction = "Left";

	private Direction(String direction) {
		this.direction = direction;
	}

	public static final Direction LEFT = new Direction(CoreProperties.getString(CoreProperties.Left));
	public static final Direction RIGHT = new Direction(CoreProperties.getString(CoreProperties.Right));
	public static final Direction TOP = new Direction(CoreProperties.getString(CoreProperties.Top));
	public static final Direction BOTTOM = new Direction(CoreProperties.getString(CoreProperties.Bottom));

	/**
	 * 根据指定的方位字符串获取对应的方位对象
	 * left return {@link Direction#LEFT}
	 * right return {@link Direction#RIGHT}
	 * top return {@link Direction#TOP}
	 * bottom return {@link Direction#BOTTOM}
	 *
	 * @param direction
	 * @return
	 */
	public static final Direction valueOf(String direction) {
		if ("left".equalsIgnoreCase(direction)) {
			return Direction.LEFT;
		} else if ("right".equalsIgnoreCase(direction)) {
			return Direction.RIGHT;
		} else if ("top".equalsIgnoreCase(direction)) {
			return Direction.TOP;
		} else if ("bottom".equalsIgnoreCase(direction)) {
			return Direction.BOTTOM;
		}
		return Direction.LEFT;
	}

	@Override
	public String toString() {
		return this.direction;
	}
}
