package com.supermap.desktop.controls.drop;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

/**
 * Created by xie on 2017/2/23.
 * 默认的DragSourceListener
 */
public class DefaultDragSourceListener implements DragSourceListener {

    private DropAndDragHandler handler;

    public DefaultDragSourceListener(DropAndDragHandler handler) {
        this.handler = handler;
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        handler.dragDropEnd(dragSourceDropEvent);
    }

    @Override
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        handler.dragEnter(dragSourceDragEvent);
    }

    @Override
    public void dragExit(DragSourceEvent dragSourceEvent) {
        handler.dragExit(dragSourceEvent);
    }

    @Override
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
        handler.dragOver(dragSourceDragEvent);
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
        handler.dropActionChanged(dragSourceDragEvent);
    }

}
