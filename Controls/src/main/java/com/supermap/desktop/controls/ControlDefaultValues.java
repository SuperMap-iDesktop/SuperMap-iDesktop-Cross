package com.supermap.desktop.controls;

import java.awt.*;

public class ControlDefaultValues {
	public static final Dimension DEFAULT_PREFERREDSIZE = new Dimension(45, 23);

	public static final String PROPERTYNAME_VALUE = "value";
	public static final String SCALE_PROPERTY_VALUE = "SCALE_PROPERTY_VALUE";

	//确定取消按钮间隔值
	public static final int DEFAULT_PREFERREDSIZE_GAP = 10;
	// 面板之间间距
	public static final int DEFAULT_PANEL_GAP = 5;

	private static double COPY_CURRENT_MAPBOUNDS_LEFT = 0.0;
	private static double COPY_CURRENT_MAPBOUNDS_TOP = 0.0;
	private static double COPY_CURRENT_MAPBOUNDS_RIGHT = 0.0;
	private static double COPY_CURRENT_MAPBOUNDS_BOTTOM = 0.0;

	private ControlDefaultValues() {
		// 工具类，不提供构造函数

	}

	// 在地图导出为图片功能中点击复制按钮，将地图范围值赋予，当需要粘贴时再从此公共类中获得--yuanR 2017.3.21
	public static void setCopyCurrentMapboundsLeft(double value) {
		COPY_CURRENT_MAPBOUNDS_LEFT = value;
	}

	public static void setCopyCurrentMapboundsTop(double value) {
		COPY_CURRENT_MAPBOUNDS_TOP = value;
	}

	public static void setCopyCurrentMapboundsRight(double value) {
		COPY_CURRENT_MAPBOUNDS_RIGHT = value;
	}

	public static void setCopyCurrentMapboundsBottom(double value) {
		COPY_CURRENT_MAPBOUNDS_BOTTOM = value;
	}

	public static double getCopyCurrentMapboundsLeft() {
		return COPY_CURRENT_MAPBOUNDS_LEFT;
	}

	public static double getCopyCurrentMapboundsTop() {
		return COPY_CURRENT_MAPBOUNDS_TOP;
	}

	public static double getCopyCurrentMapboundsRight() {
		return COPY_CURRENT_MAPBOUNDS_RIGHT;
	}

	public static double getCopyCurrentMapboundsBottom() {
		return COPY_CURRENT_MAPBOUNDS_BOTTOM;
	}
}
