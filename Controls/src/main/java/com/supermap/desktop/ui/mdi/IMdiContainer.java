package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.layout.ILayoutStrategy;

/**
 * Created by highsad on 2016/12/13.
 */
public interface IMdiContainer {

	ILayoutStrategy getLayoutStrategy();

	void setLayoutStrategy(ILayoutStrategy strategy);

	MdiGroup[] getGroups();

	MdiGroup getSelectedGroup();

	int indexOf(MdiGroup group);

	MdiGroup getGroup(int index);

	int getGroupCount();

	void active(MdiGroup group);
}
