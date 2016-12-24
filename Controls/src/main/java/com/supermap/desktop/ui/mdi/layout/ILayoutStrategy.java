package com.supermap.desktop.ui.mdi.layout;

import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;

/**
 * Created by highsad on 2016/12/14.
 */
public interface ILayoutStrategy {

	IMdiContainer getContainer();

	boolean addGroup(MdiGroup group);

	boolean removeGroup(MdiGroup group);

	void layoutGroups();

	void reset();
}
