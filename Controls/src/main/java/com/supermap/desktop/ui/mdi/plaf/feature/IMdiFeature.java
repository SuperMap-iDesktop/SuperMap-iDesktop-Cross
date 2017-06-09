package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 * Created by highsad on 2016/8/30.
 */
public interface IMdiFeature extends MouseListener, MouseMotionListener, KeyListener {

	/**
	 * 获取 Parent Feature
	 *
	 * @return
	 */
	IMdiFeature getParent();

	/**
	 * 获取该 Feature 所在的 MdiGroup
	 *
	 * @return
	 */
	MdiGroup getGroup();

	/**
	 * 获取 Feature 集合
	 *
	 * @return
	 */
	List<IMdiFeature> getFeatures();

	/**
	 * 获取左上角 X 坐标值
	 *
	 * @return
	 */
	int getX();

	/**
	 * 设置左上角 X 坐标值
	 *
	 * @param x
	 */
	void setX(int x);

	/**
	 * 获取左上角 Y 坐标值
	 *
	 * @return
	 */
	int getY();

	/**
	 * 设置左上角 Y 坐标值
	 *
	 * @param y
	 * @return
	 */
	void setY(int y);

	/**
	 * 获取高度
	 *
	 * @return
	 */
	int getHeight();

	/**
	 * 获取宽度
	 *
	 * @return
	 */
	int getWidth();

	boolean isContains(Point point);

	void repaint();

	/**
	 * 验证基础属性，宽、高等，这些基础属性会影响布局属性（X、Y），在调用 layout 之前建议先调用 validate
	 */
	void validate();

	/**
	 * 重新布局
	 */
	void layout();

	void paint(Graphics graphics);
}
