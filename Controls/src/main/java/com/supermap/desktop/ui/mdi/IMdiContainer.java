package com.supermap.desktop.ui.mdi;

/**
 * Created by highsad on 2016/12/13.
 */
public interface IMdiContainer {

	MdiGroup getSelectedGroup();

	void active(MdiGroup group);
}
