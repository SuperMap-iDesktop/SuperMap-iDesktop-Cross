package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPane;
import com.supermap.desktop.ui.mdi.layout.SplitLayoutStrategy;

/**
 * Created by xie on 2016/12/22.
 */
public class BindLayoutStrategy extends SplitLayoutStrategy {

    public BindLayoutStrategy(MdiPane container) {
        super(container);
    }

    @Override
    public IMdiContainer getContainer() {
        return container;
    }

    @Override
    public void addGroup(MdiGroup group) {

    }

    @Override
    public void removeGroup(MdiGroup group) {
        super.removeGroup(group);
    }

    @Override
    public void layoutGroups() {
        super.layoutGroups();
    }

    @Override
    public void reset() {
        super.reset();
    }
}
