package com.supermap.desktop.Interface;

public interface IContextMenuManager {

	/**
	* 获取指定索引的菜单。 
	*/
	IPopupMenu get(int index);
	
	/**
	* 获取指定索引的菜单。 
	*/
	IPopupMenu get(String id);
	
	/**
	* 获取所有菜单的个数。 
	*/
	int getCount();

	/**
	* 检查程序是否包含指定的菜单。
	*/
    boolean contains(IPopupMenu item);
}
