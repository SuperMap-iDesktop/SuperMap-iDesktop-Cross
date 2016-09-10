package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMain;

import javax.swing.*;
import java.awt.*;

public class CursorUtilities {

	/**
	 * 设置主窗口上的光标为等待
	 */
	private CursorUtilities() {
		// 工具类不提供构造函数
	}

	/**
	 * 设置主窗口上的光标为等待光标
	 */
	public static void setWaitCursor() {
		IFormMain formMain = Application.getActiveApplication().getMainFrame();

		if (formMain instanceof JFrame) {
			((JFrame) formMain).setCursor(new Cursor(Cursor.WAIT_CURSOR));
		}
	}

	/**
	 * 设置主窗口上的光标为默认光标
	 */
	public static void setDefaultCursor() {
		IFormMain formMain = Application.getActiveApplication().getMainFrame();

		if (formMain instanceof JFrame) {
			((JFrame) formMain).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public static void setHandCursor() {
		IFormMain formMain = Application.getActiveApplication().getMainFrame();

		if (formMain instanceof JFrame) {
			((JFrame) formMain).setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	/**
	 * 设置指定控件的光标为等待光标
	 *
	 * @param component
	 */
	public static void setWaitCursor(Component component) {
		component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	/**
	 * 设置指定控件的光标为默认光标
	 *
	 * @param component
	 */
	public static void setDefaultCursor(Component component) {
		component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public static void setHandCursor(Component component) {
		component.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

}
