package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.WindowType;

public interface IFrameMenuManager {
	
	/**
	* 获取指定索引的主菜单。 
	*/
	IMenu get(int index);
	
	/**
	* 获取指定索引的主菜单。 
	*/
	IMenu get(String key);
	
	/**
	* 获取所有主菜单的个数。 
	*/
	int getCount();

	/**
	* 检查是否包含指定的主菜单。
	*/
    boolean contains(IMenu item);    

	/**
	* 检查指定子窗体类型是否包含指定的子菜单。
	*/
    boolean contains(WindowType windowType, IMenu item);
    
    /**
	* 获取指定子窗体类型、指定索引的子菜单。 
	*/
	IMenu getChildMenu(WindowType windowType, int index);
	
	/**
	* 获取指定子窗体类型、指定索引的子菜单。 
	*/
	IMenu getChildMenu(WindowType windowType, String key);
	
	/**
	* 获取指定子窗体类型的所有子菜单的个数。 
	*/
	int getChildMenuCount(WindowType windowType);
}
