package com.supermap.desktop.ui.mdi.action;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.plaf.feature.IMdiFeature;
import com.supermap.desktop.ui.mdi.util.MdiResource;

/**
 * Created by highsad on 2016/9/19.
 */
public class ActionPageClose extends AbstractAction {

	public ActionPageClose() {
		super(MdiResource.getIcon(MdiResource.CLOSE), MdiResource.getIcon(MdiResource.CLOSE_DISABLE), MdiResource.getIcon(MdiResource.CLOSE_ACTIVE));
	}

	@Override
	public void action(MdiPage page, IMdiFeature feature) {
		page.close();
	}

	@Override
	public ActionMode getMode() {
		return ActionMode.PAGE_GROUP;
	}
}
