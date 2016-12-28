package com.supermap.desktop.iDesktop;

import org.apache.felix.main.Main;

import javax.swing.*;

/**
 * Created by highsad on 2016/8/3.
 */
public class Startup {
	public static void main(String[] args) {
		try {
			if (!System.getProperties().getProperty("os.name").startsWith("Windows")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
//			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//			UIManager.put("RootPane.setupButtonVisible", false);
//			BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
//			BeautyEyeLNFHelper.setMaximizedBoundForFrame = false;
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
