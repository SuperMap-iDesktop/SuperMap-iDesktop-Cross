package com.supermap.desktop.event;

import java.util.EventObject;

import javax.swing.JFrame;

import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.*;

public class NewWindowEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient IForm newWindow = null;
	private transient WindowType newWindowType = WindowType.UNKNOWN;
	private String windowName = "";

	public NewWindowEvent(Object source, WindowType newWindowType) {
		super(source);
		this.newWindowType = newWindowType;
	}

	public IForm getNewWindow() {
		return this.newWindow;
	}

	public void setNewWindow(IForm newWindow) {
		this.newWindow = newWindow;
	}

	public WindowType getNewWindowType() {
		return this.newWindowType;
	}

	/**
	 * 获取和设置新创建的窗口的名称，只有打开地图、布局或者场景的时候才需要设置该属性
	 * 
	 * @param name 指定的窗口名称，如果是地图、场景或者布局子窗体，则默认打开指定名称的地图、布局或者场景。
	 */
	public String getNewWindowName() {
		return this.windowName;
	}

	public void setNewWindowName(String value) {
		this.windowName = value;
	}
}
