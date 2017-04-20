package com.supermap.desktop.event;

import com.supermap.data.Resources;
import com.supermap.data.SymbolGroup;

/**
 * Created by xie on 2017/4/20.
 */
public class ResourcesChangedEvent {
    private Resources newResources;
    private SymbolGroup symbolGroup;

    public ResourcesChangedEvent(Resources newResources, SymbolGroup symbolGroup) {
        this.newResources = newResources;
        this.symbolGroup = symbolGroup;
    }

    public Resources getNewResources() {
        return newResources;
    }

    public SymbolGroup getSymbolGroup() {
        return symbolGroup;
    }
}
