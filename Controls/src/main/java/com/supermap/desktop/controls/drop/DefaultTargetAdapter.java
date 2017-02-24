package com.supermap.desktop.controls.drop;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

/**
 * Created by xie on 2017/2/23.
 * 默认的接收拖拽源类
 */
public class DefaultTargetAdapter extends DropTargetAdapter {
    private DropAndDragHandler handler;

    public DefaultTargetAdapter(DropAndDragHandler handler) {
        this.handler = handler;
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        if (isDropAcceptable(dtde))
            handler.drop(dtde);
    }

    // 判断是否支持拖拽
    public boolean isDropAcceptable(DropTargetDropEvent event) {
        return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
    }
}
