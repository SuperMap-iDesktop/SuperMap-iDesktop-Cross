package com.supermap.desktop.ui.mdi.layout;

import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;

/**
 * Created by highsad on 2016/12/14.
 */
public interface ILayoutStrategy {

	IMdiContainer getContainer();

	boolean addGroup(MdiGroup group);

	boolean removeGroup(MdiGroup group);

	/**
	 * 当添加新页面的时候，分配一个 MdiGroup
	 * @param page
	 * @return
	 */
	MdiGroup dispatchGroup(MdiPage page);

	void layoutGroups();

	void reset();
}
