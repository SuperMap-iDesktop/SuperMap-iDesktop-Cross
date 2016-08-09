package com.supermap.desktop.implement;

import com.sun.java.swing.plaf.windows.WindowsToolBarUI;

import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.metal.MetalToolBarUI;
import javax.swing.plaf.synth.SynthToolBarUI;

/**
 * @author XiaJT
 */
public class ToolBarUIFactory {
	private ToolBarUIFactory() {
		// 工厂方法类
	}

	public static ToolBarUI getToolBarUIFactory(ToolBarUI toolBarUI) {
		if (toolBarUI instanceof WindowsToolBarUI) {
			return new MyWindowsToolBarUI();
		} else if (toolBarUI instanceof MetalToolBarUI) {
			return new MyMetalToolBarUI();
		} else if (toolBarUI instanceof SynthToolBarUI) {
			return new MySynthToolBarUI();
		}
		return new MyWindowsToolBarUI();
	}
}
