package com.supermap.desktop.enums;

public enum InfoType {
	/**
	* 信息类型。
	*/
    Information("Information", 1), 
    
    /**
	* 异常类型。
	*/
    Exception("Exception", 2);
    
    
    // 成员变量
    private String name;
    private int value;

    // 构造方法
    private InfoType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(int value) {
        for (InfoType c : InfoType.values()) {
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
