package com.supermap.desktop.Interface;

import java.awt.Point;
import java.util.ResourceBundle.Control;

public interface IContextMenu extends IMenu {

	/**
	* 在屏幕左上角位置显示右键菜单。
	*/
	void Show();
	
	/**
	* 根据指定的point坐标位置，显示右键菜单。
	* @param point
	* 指定显示菜单的位置。
	*/
	void Show(Point point);
	
	/**
	* 根据指定的point坐标位置，显示右键菜单。
	* @param control
	* 作为位置参考的控件，显示的右键菜单位置以控件的左上角作为参考点。
	* @param point
	* 指定显示菜单的位置。
	*/
	void Show(Control control, Point point);
	
	/**
	* 在屏幕指定的(X,Y)位置显示右键菜单。
	* @param x
	* 显示位置的 X 坐标。
	* @param y
	* 显示位置的 Y 坐标。
	*/
	void Show(int x, int y);
	
	/**
	* 在控件的相对指定位置（X,Y）显示右键菜单。
	* @param control
	* 作为位置参考的控件，显示的右键菜单位置以控件的左上角作为参考点。
	* @param x
	* 显示位置的 X 坐标。
	* @param y
	* 显示位置的 Y 坐标。
	*/
	void Show(Control control,int x, int y);
}
