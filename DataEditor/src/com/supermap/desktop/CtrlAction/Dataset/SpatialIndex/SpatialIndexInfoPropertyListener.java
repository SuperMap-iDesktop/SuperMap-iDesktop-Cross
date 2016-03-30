package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

/**
 * 空间索引面板改变时修改对应的属性
 *
 * @author XiaJT
 */
public interface SpatialIndexInfoPropertyListener {
	public static String GRID_X = "GRID_X";
	public static String GRID_Y = "GRID_Y";
	public static String GRID_SIZE_0 = "GRID_SIZE_0";
	public static String GRID_SIZE_1 = "GRID_SIZE_1";
	public static String GRID_SIZE_2 = "GRID_SIZE_2";

	public static String TILE_FIELD = "TILE_FIELD";
	public static String TILE_WIDTH = "TILE_WIDTH";
	public static String TILE_HEIGHT = "TILE_HEIGHT";

	void propertyChanged(String propetName, Object value);
}
