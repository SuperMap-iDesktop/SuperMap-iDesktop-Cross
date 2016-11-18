package com.supermap.desktop.ui.mdi.action;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.plaf.feature.IMdiFeature;
import com.supermap.desktop.ui.mdi.plaf.feature.MdiGroupFeature;
import com.supermap.desktop.ui.mdi.util.MdiResource;

/**
 * Created by highsad on 2016/9/19.
 */
public class ActionPrePage extends AbstractAction {

	public ActionPrePage() {
		super(MdiResource.getIcon(MdiResource.PRE), MdiResource.getIcon(MdiResource.PRE_DISABLE), MdiResource.getIcon(MdiResource.PRE_ACTIVE));
	}

	@Override
	public void action(MdiPage page, IMdiFeature feature) {
		if (page != null) {
			MdiGroup group = page.getGroup();
			MdiGroupFeature groupFeature = (MdiGroupFeature) group.getUI().getGroupFeature();
			groupFeature.getTabsFeature().forward();
		}
	}

	@Override
	public boolean enabled(MdiPage page) {
		if (page == null) {
			return false;
		}

		MdiGroup group = page.getGroup();
		MdiGroupFeature groupFeature = (MdiGroupFeature) group.getUI().getGroupFeature();
		return groupFeature.getTabsFeature().canForward();
	}

	@Override
	public ActionMode getMode() {
		return ActionMode.GROUP;
	}
}
