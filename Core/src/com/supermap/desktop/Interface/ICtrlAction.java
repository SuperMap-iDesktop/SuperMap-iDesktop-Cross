package com.supermap.desktop.Interface;

public interface ICtrlAction {
	
	/**
	* 获取或设置执行控件。
	*/
	IBaseItem getCaller();
	void setCaller(IBaseItem caller);
	
	/**
	* 获取或设置控件关联的窗体。
	*/
	IForm getFormClass();
	void setFormClass(IForm form);
	
	/**
	* 执行内容以响应控件发生的事件。
	*/
    void run();
    
    /**
	* 设置控件对象是否可用。可用返回 true，否则返回 false。
	*/
    boolean enable();
    
    /**
	* 设置控件对象的选中状态。
	*/
    boolean check();
}
