package com.supermap.desktop.ui.mdi.action;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.plaf.feature.IMdiFeature;

import javax.swing.*;

public interface IMdiAction {

	Icon getIcon();

	Icon getIconDisable();

	Icon getIconActive();

	String getDescription();

	ActionMode getMode();

	void action(MdiPage page, IMdiFeature feature);

	boolean enabled(MdiPage page);
}