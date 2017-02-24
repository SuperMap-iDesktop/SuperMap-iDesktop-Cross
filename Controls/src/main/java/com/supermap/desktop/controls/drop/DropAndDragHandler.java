package com.supermap.desktop.controls.drop;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;

/**
 * Created by xie on 2017/2/23.
 * 用于处理拖拽的类
 * 将控件设置为拖拽源时,各个子类根据需要重写方法,
 * 为控件添加拖拽信息时，调用addDropTarget()方法，并重写drop(),并保证先后顺序
 */
public abstract class DropAndDragHandler implements IDropAndDrag {
    //支持的拖拽类型
    private DataFlavor[] supportFlavors;

    public DropAndDragHandler() {

    }

    public DropAndDragHandler(DataFlavor... flavors) {
        this.supportFlavors = flavors;
    }

    /**
     * 将控件设置为拖拽源时
     * 此方法在实例化时必须调用，不然后续流程将出现问题
     *
     * @param dragComponent
     * @return
     */
    public IDropAndDrag bindSource(JComponent dragComponent) {
        DragSource dragSource = DragSource.getDefaultDragSource();// 创建拖拽源
        dragSource.createDefaultDragGestureRecognizer(dragComponent, DnDConstants.ACTION_COPY_OR_MOVE, new DefaultDragGestureListener(this)); // 建立拖拽源和事件的联系
        return this;
    }

    /**
     * 接收拖拽信息
     *
     * @param component
     */
    public IDropAndDrag addDropTarget(JComponent component) {
        new DropTarget(component, new DefaultTargetAdapter(this));
        return this;
    }

    /**
     * 子类必须重写此方法来提供拖拽源
     *
     * @param dge
     * @return
     */
    public abstract Object getTransferData(DragGestureEvent dge);

    /**
     * 获取支持的拖拽类型
     *
     * @return
     */
    public DataFlavor[] getSupportFlavors() {
        return supportFlavors;
    }

    /**
     * 拖拽源拖拽完成后的处理
     *
     * @param dragSourceDropEvent
     */
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        if (dragSourceDropEvent.getDropSuccess()) {
            int dropAction = dragSourceDropEvent.getDropAction();
            if (dropAction == DnDConstants.ACTION_MOVE) {
                System.out.println("MOVE: move node");
            }
        }
    }

    /**
     * 拖拽源拖拽进入时的处理
     *
     * @param dragSourceDragEvent
     */
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        DragSourceContext context = dragSourceDragEvent.getDragSourceContext();
        int dropAction = dragSourceDragEvent.getDropAction();
        if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
            context.setCursor(DragSource.DefaultCopyDrop);
        } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
            context.setCursor(DragSource.DefaultMoveDrop);
        } else {
            context.setCursor(DragSource.DefaultCopyNoDrop);
        }

    }

    public void dragExit(DragSourceEvent dragSourceEvent) {

    }

    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {

    }

    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {

    }

    public void drop(DropTargetDropEvent dtde) {

    }

}
