package com.supermap.desktop.ui.controls;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * 定义组件自己的Button类型，重写processKeyEvent方法，以支持回车键
 * 
 * @author xuzw
 *
 */
public class ControlButton extends JButton {

	private static final long serialVersionUID = 1L;

	public ControlButton() {
		super();
	}

	public ControlButton(String text) {
		super(text);
	}

	/**
	 * 
	 * 解决初始化图标的问题
	 * 
	 * @author zhaosy
	 */
	public ControlButton(String text, Icon icon) {
		super(text, icon);
	}

	public ControlButton(Icon icon) {
		super(icon);
	}

	@Override
	protected void processKeyEvent(KeyEvent event) {
		super.processKeyEvent(event);
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			doClick();
		}
	}
}
