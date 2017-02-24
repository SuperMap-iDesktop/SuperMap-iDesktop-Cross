package com.supermap.desktop.controls.drop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by xie on 2017/2/23.
 */
public class DefaultTransferable implements Transferable {

    private Object transferData;
    private DataFlavor[] supportFlavors;

    public DefaultTransferable(Object transferData, DataFlavor[] supportFlavors) {
        this.transferData = transferData;
        this.supportFlavors = supportFlavors;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportFlavors;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return transferData;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (DataFlavor supportFlavor : supportFlavors) {
            if (flavor == supportFlavor) {
                return true;
            }
        }
        return false;
    }
}
