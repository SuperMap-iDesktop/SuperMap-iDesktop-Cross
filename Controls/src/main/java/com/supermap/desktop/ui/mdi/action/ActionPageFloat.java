package com.supermap.desktop.ui.mdi.action;

import com.supermap.desktop.ui.mdi.util.MdiResource;

/**
 * Created by highsad on 2016/9/19.
 */
public class ActionPageFloat extends AbstractAction {

	public ActionPageFloat() {
		super(MdiResource.getIcon(MdiResource.FLOAT), MdiResource.getIcon(MdiResource.FLOAT_DISABLE), MdiResource.getIcon(MdiResource.FLOAT_ACTIVE));
	}

	@Override
	public ActionMode getMode() {
		return ActionMode.PAGE_GROUP;
	}
}
