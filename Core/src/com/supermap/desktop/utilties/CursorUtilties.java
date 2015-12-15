package com.supermap.desktop.utilties;

import java.awt.Cursor;

import javax.swing.JFrame;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMain;

public class CursorUtilties {

	/**
	 * 设置主窗口上的光标为等待
	 */
	private CursorUtilties() {
		// 工具类不提供构造函数
	}

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
}
