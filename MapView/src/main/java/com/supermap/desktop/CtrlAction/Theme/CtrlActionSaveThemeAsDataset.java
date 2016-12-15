package com.supermap.desktop.CtrlAction.Theme;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.newtheme.saveThemeAsDataset.DiglogSaveThemeAsDataset;
import com.supermap.mapping.ThemeType;

/**
 * @author YuanR
 */
public class CtrlActionSaveThemeAsDataset extends CtrlAction {

	public CtrlActionSaveThemeAsDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		DiglogSaveThemeAsDataset diglogSaveThemeAsDataset = new DiglogSaveThemeAsDataset();
		diglogSaveThemeAsDataset.showDialog();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			//当前专题图不可见时，功能不可用
			if (ThemeUtil.getActiveLayer().isVisible() && (ThemeUtil.getActiveLayer().getTheme().getType().equals(ThemeType.LABEL))) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}