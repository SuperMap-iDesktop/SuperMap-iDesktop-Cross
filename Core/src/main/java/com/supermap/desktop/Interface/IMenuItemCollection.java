package com.supermap.desktop.Interface;

public interface IMenuItemCollection {

	/**
	* 获取右键菜单子项集合中指定索引的子项。
	*/
	IBaseItem getAt(int index);

	/**
	* 获取右键菜单子项集合中所包含子项的总数。
	*/
	int getCount();
	
	/**
	* 向右键菜单子项集合中添加新项。
	* @param item
	* 待添加子项
	*/
	int add(IBaseItem item);
	
	/**
	* 向右键菜单子项集合中添加新项。
	* @param items
	* 待添加子项集合
	*/
	void addRange(IBaseItem[] items);

	/**
	* 移除右键菜单子项集合中的所有子项。
	*/
    void clear();

    /**
	* 判断右键菜单子项集合中是否包含指定的子项。。
	* @param items 
	* 待判断子项
	*/
    boolean contains(IBaseItem item);

    /**
	* 获取右键菜单子项集合中指定子项的索引。 
	* @parms item
	* 待获取索引的子项
	*/
    int indexOf(IBaseItem item);

    /**
	* 在右键菜单子项集合指定索引处插入指定的子项。
	* @param index
	* 插入位置索引
	* @param item
	* 待插入子项
	*/
    void insert(int index, IBaseItem item);

    /**
	* 移除右键菜单子项集合中的指定项。
	* @param item
	* 待添加子项
	* */
    void remove(IBaseItem item);

    /**
	* 移除右键菜单子项集合中指定索引的子项。 
	* @parms index
	* 待移除子项的索引
	*/
    void removeAt(int index);
}
