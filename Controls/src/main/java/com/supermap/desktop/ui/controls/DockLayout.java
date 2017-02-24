package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.DockPath;
import org.flexdock.view.View;

/**
 * Created by highsad on 2017/2/21.
 * 用来做浮动窗口自动构建布局和导入导出用的
 * 由于当前浮动窗口的使用方式导致实现过于复杂，难点很多，暂时使用
 * 保留临时解决方案进行浮动窗口的配置文件读取构建，以后有灵感再做
 */
public class DockLayout extends DockStrategy {
	private View mainView;

	public DockLayout(View mainView) {
		this.mainView = mainView;
	}

	public View getMainView() {
		return mainView;
	}

	public void addDock(Dockbar dockbar, DockPath dockPath) {
		if (dockPath == null) {
			return;
		}

		super.addDock(dockbar, dockPath);
	}

	public void optimize() {

	}
}
