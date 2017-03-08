package com.supermap.desktop.iDesktop;

import org.apache.felix.main.Main;

import javax.swing.*;

/**
 * Created by highsad on 2016/8/3.
 */
public class Startup {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel(new NimbusLookAndFeel());
//			UIManager.setLookAndFeel(UIManager.get);
//			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//			UIManager.put("RootPane.setupButtonVisible", false);
//			BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
//			BeautyEyeLNFHelper.setMaximizedBoundForFrame = false;
//			if(UIManager.getLookAndFeel() instanceof MetalLookAndFeel){
//				((MetalLookAndFeel)UIManager.getLookAndFeel()).setCurrentTheme(new OceanTheme());
//			}
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
