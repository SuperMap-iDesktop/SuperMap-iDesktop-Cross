package com.supermap.desktop.controls.drop;


import javax.swing.*;

/**
 * Created by xie on 2017/2/23.
 * 拖拽接口规范类
 */
public interface IDropAndDrag {
    /**
     * 将控件设置为拖拽源时
     * 此方法在实例化时必须调用，不然后续流程将出现问题
     *
     * @param dragComponent
     * @return
     */
    public IDropAndDrag bindSource(JComponent dragComponent);

    /**
     * 接收拖拽信息
     *
     * @param component
     */
    public IDropAndDrag addDropTarget(JComponent component);
}
