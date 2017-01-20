package com.supermap.desktop.event;

import java.util.EventListener;

/**
 * Created by Chens on 2017/1/20 0020.
 */
public interface ColorTableChangeListener extends EventListener {
    void colorTableChange(ColorTableChangeEvent event);
}
