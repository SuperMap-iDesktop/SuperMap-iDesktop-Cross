package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.FormBase;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;

/**
 * Created by highsad on 2016/12/16.
 */
public class CtrlActionBeautyEyeLF extends CtrlAction {
	public CtrlActionBeautyEyeLF(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		UIManager.put("RootPane.setupButtonVisible", false);
		BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
		BeautyEyeLNFHelper.setMaximizedBoundForFrame = false;
		SwingUtilities.updateComponentTreeUI(((FormBase) Application.getActiveApplication().getMainFrame()));
	}
}
