package com.supermap.desktop.ui.controls;

/**
 * 点击的Icon的类型，用于LayersTree和Layer3DsTree
 * 
 * @author gouyu
 */
public class HitTestIconType extends com.supermap.data.Enum {

	protected HitTestIconType(int value, int ugcValue) {
		super(value, ugcValue);
	}

	/**
	 * 无效
	 */
	public static final HitTestIconType NONE = new HitTestIconType(0, 0);

	/**
	 * 可见图标
	 */
	public static final HitTestIconType VISIBLE = new HitTestIconType(1, 1);

	/**
	 * 可选择图标
	 */
	public static final HitTestIconType SELECTABLE = new HitTestIconType(2, 2);

	/*
	 * 可编辑图标
	 */
	public static final HitTestIconType EDITABLE = new HitTestIconType(3, 3);

	/**
	 * 可捕捉图标
	 */
	public static final HitTestIconType SNAPABLE = new HitTestIconType(4, 4);

	/**
	 * 始终刷新图标
	 */
	public static final HitTestIconType ALLWAYSRENDER = new HitTestIconType(5, 5);

	/**
	 * 类型图标
	 */
	public static final HitTestIconType TYPE = new HitTestIconType(6, 6);
}
