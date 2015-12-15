package com.supermap.desktop.ui.controls;

public enum DatasourceOperatorType {
	
	/**
	* 新建文件类型。
	*/
	NEWFILE("New File", 1), 
	
	/**
	* 新建数据库类型。
	*/
	NEWDATABASE("New Database", 2), 
	
	/**
	* 新建内存类型。
	*/
	NEWMEMORY("New Memory", 3), 
    
	/**
	* 打开文件类型。
	*/
	OPENFILE("Open File", 4), 
	
	/**
	* 打开数据库类型。
	*/
	OPENDATABASE("Open Database", 5), 
	
	/**
	* 打开Web类型。
	*/
	OPENWEB("Open Web", 6);
    
    
    // 成员变量
    private String name;
    private int value;

    // 构造方法
    private DatasourceOperatorType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(int value) {
        for (DatasourceOperatorType c : DatasourceOperatorType.values()) {
            if (c.getValue() == value) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
