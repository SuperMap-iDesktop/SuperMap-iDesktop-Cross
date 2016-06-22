package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.WindowType;

import javax.swing.*;

public interface IToolbarManager {

	JPanel getToolbarsContainer();

	/**
	* 获取指定索引的工具条。 
	*/
	IToolbar get(int index);
	
	/**
	* 获取指定索引的工具条。 
	*/
	IToolbar get(String id);
	
	/**
	* 获取应用程序内所包含的浮动窗口的总数。
	*/
	int getCount();
	
	/**
	* 检查程序是否包含指定的工具条。
	*/
    boolean contains(IToolbar item);
    
    /**
	* 检查指定子窗体类型是否包含指定的子菜单。
	*/
    boolean contains(WindowType windowType, IToolbar item);
    
    /**
	* 获取指定子窗体类型、指定索引的子菜单。 
	*/
    IToolbar getChildToolbar(WindowType windowType, int index);
	
	/**
	* 获取指定子窗体类型、指定索引的子菜单。 
	*/
    IToolbar getChildToolbar(WindowType windowType, String key);
	
	/**
	* 获取指定子窗体类型的所有子菜单的个数。 
	*/
	int getChildToolbarCount(WindowType windowType);

	void update();
}
