package com.supermap.desktop.ui.mdi.action;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.plaf.feature.IMdiFeature;
import com.supermap.desktop.ui.mdi.util.MdiResource;

import javax.swing.*;

/**
 * Created by highsad on 2016/9/19.
 */
public abstract class AbstractAction implements IMdiAction {

	private Icon icon;
	private Icon iconDisable;
	private Icon iconActive;

	public AbstractAction(Icon icon) {
		this(icon, null, null);
	}

	public AbstractAction(Icon icon, Icon iconDisable, Icon iconActive) {
		this.icon = icon;
		this.iconDisable = iconDisable;
		this.iconActive = iconActive;
	}

	/**
	 * 不指定则默认
	 *
	 * @return
	 */
	@Override
	public Icon getIcon() {
		return this.icon == null ? MdiResource.getIcon(MdiResource.ACTION) : this.icon;
	}

	/**
	 * 不指定则默认
	 *
	 * @return
	 */
	@Override
	public Icon getIconDisable() {
		if (this.iconDisable == null) {

			// 如果 icon 不会空，那么就使用 icon，否则就是用默认图标
			return this.icon == null ? MdiResource.getIcon(MdiResource.ACTION_DISABLE) : this.icon;
		} else {
			return this.iconDisable;
		}
	}

	/**
	 * 不指定则默认
	 *
	 * @return
	 */
	@Override
	public Icon getIconActive() {
		if (this.iconActive == null) {

			// 如果 icon 不会空，那么就使用 icon，否则就是用默认图标
			return this.icon == null ? MdiResource.getIcon(MdiResource.ACTION_ACTIVE) : this.icon;
		} else {
			return this.iconActive;
		}
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public ActionMode getMode() {
		return ActionMode.PAGE;
	}

	/**
	 * @param page 当前操作直接作用的 page，如果在 Tab 上，则为每一个 Tab 绑定的 page，如果在 group 上，则为 activePage
	 */
	@Override
	public void action(MdiPage page, IMdiFeature feature) {

	}

	/**
	 * @param page 当前操作直接作用的 page，如果在 Tab 上，则为每一个 Tab 绑定的 page，如果在 group 上，则为 activePage
	 * @return
	 */
	@Override
	public boolean enabled(MdiPage page) {
		return true;
	}
}
