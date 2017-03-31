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
	 * Sets the cursor image of the main frame.
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
	 * Sets the cursor image to the specified waiting cursor.
	 *
	 * @param component
	 */
	public static void setWaitCursor(Component component) {
		component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	/**
	 * Sets the cursor image to the specified default cursor.
	 *
	 * @param component
	 */
	public static void setDefaultCursor(Component component) {
		component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public static void setHandCursor(Component component) {
		component.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public static void setCustomCursor(Component component, Image cursorImg, String name) {
		setCustomCursor(component, cursorImg, new Point(0, 0), name);
	}

	public static void setCustomCursor(Component component, Image cursorImg, Point hotSpot, String name) {
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, hotSpot, name);

		if (cursor != null) {
			component.setCursor(cursor);
		}
	}
}
