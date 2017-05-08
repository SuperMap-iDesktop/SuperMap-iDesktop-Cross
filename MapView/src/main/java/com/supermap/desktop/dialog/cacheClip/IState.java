package com.supermap.desktop.dialog.cacheClip;

/**
 * Created by xie on 2017/4/26.
 *
 */
public interface IState {
    void addEnabledListener(EnabledListener enabledListener);

    void removeEnabledListener(EnabledListener enabledListener);
}
