package com.supermap.desktop.dialog.cacheClip;

/**
 * Created by xie on 2017/5/3.
 */
public class EnabledEvent {
    boolean enabled;
    public EnabledEvent(boolean enabled){
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
