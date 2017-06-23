package com.supermap.desktop.iDesktop;

import com.alibaba.fastjson.JSONArray;
import org.apache.felix.main.Main;

import javax.swing.*;
import java.util.Collections;

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
			args = new String[]{
					"C:\\Users\\Administrator\\Desktop\\2.smwu"
			};
			JSONArray jsonArray = new JSONArray();
			Collections.addAll(jsonArray, args);
			System.setProperty("DesktopCrossStartArgs", jsonArray.toJSONString());
			Main.main(new String[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
