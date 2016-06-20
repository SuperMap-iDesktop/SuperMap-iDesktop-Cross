package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import java.text.DecimalFormat;

/**
 * @author XiaJT
 */
public abstract class GeometryParameterModel {
	private DecimalFormat df = new DecimalFormat("0.0000");

	public abstract Object getValue(int row, int column);

	/**
	 * 第一行中心点选中时单独处理，相关类需要重写这个方法
	 *
	 * @param row
	 * @param column
	 * @param isSelected 是否选中  @return 显示的值
	 */
	public Object getValueAtRow0(int row, int column, boolean isSelected) {
		if (isSelected) {
			return String.valueOf(getValue(row, column));
		}
		return String.valueOf(df.format(getValue(row, column)));
	}

	public abstract int getRowCount();

	public int getColumnCount() {
		return 2;
	}

	/**
	 * 提供给子类得到格式化中心点的方法
	 *
	 * @param x          X坐标
	 * @param y          Y坐标
	 * @param isSelected 是否选中
	 * @return 格式化后的值
	 */
	protected String getCenterString(double x, double y, boolean isSelected) {
		if (isSelected) {
			return (String.valueOf(x) + "," + String.valueOf(y));
		} else {
			return df.format(x) + "," + df.format(y);
		}
	}

	public String getColumnName(int columnIndex) {
		return "";
	}

}
