package com.supermap.desktop.controls.drop;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

/**
 * Created by xie on 2017/2/23.
 */
public class DefaultDragGestureListener implements DragGestureListener {

    private DropAndDragHandler handler;

    public DefaultDragGestureListener(DropAndDragHandler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        startDrag(dge);
    }


    public void startDrag(DragGestureEvent dge) {
        Transferable dragAndDropTransferable = new DefaultTransferable(handler.getTransferData(dge), handler.getSupportFlavors());
        dge.startDrag(DragSource.DefaultCopyDrop, dragAndDropTransferable, new DefaultDragSourceListener(handler));
    }

}
