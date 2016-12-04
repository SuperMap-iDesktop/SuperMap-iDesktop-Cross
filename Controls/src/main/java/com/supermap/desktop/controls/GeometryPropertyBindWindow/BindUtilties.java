package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;

public class BindUtilties {
    private static IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
    private static MapControl mapControl;
    private static IFormTabular tabular;
    private static IPropertyBindWindow propertyBindWindow;
    private static BindUtilties utilties = new BindUtilties();
    private static ActiveFormChangedListener activeFormChangeListener = utilties.new LocalFormChangedListener();
    static int tabSize = 0;// 属性表个数

//    public static void windowBindProperty(IFormMap formMap, TabWindow tabWindow, Layer layer) {
//        mapControl = formMap.getMapControl();
//        newTabWindow = tabWindow.getChildWindow(tabWindow.getChildWindowCount() - 1);
//        tabSize += 1;
//        if (null == splitWindow) {
//            splitWindow = tabWindow.split(newTabWindow, Direction.DOWN, 0.7f);
//        } else if (splitWindow.getChildWindowCount() > 0) {
//            ((TabWindow) splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1)).addTab(newTabWindow);
//        }
//        propertyBindWindow = new PropertyBindWindow();
//        propertyBindWindow.setFormMap(formMap);
//        propertyBindWindow.setBindProperty(new BindProperty(mapControl));
//        propertyBindWindow.setBindWindow(new BindWindow(tabular), layer);
//        propertyBindWindow.registEvents();
//        newTabWindow.addListener(new DockingWindowAdapter() {
//
//            @Override
//            public void windowClosed(DockingWindow window) {
//                // 当前属性表关闭时清空map
//                tabSize -= 1;
//                if (0 == tabSize) {
//                    splitWindow = null;
//                }
//            }
//
//        });
//        formManager.addActiveFormChangedListener(activeFormChangeListener);
//    }

    public static void openTabular(Dataset dataset, Recordset recordset) {
        // 打开一个默认的属性表，然后修改属性表的title和数据与当前图层对应的数据匹配
        tabular = TabularUtilities.openDatasetVectorFormTabular(dataset);
        tabular.setText(dataset.getName() + "@" + dataset.getDatasource().getAlias());
        tabular.getIdMap().clear();
        tabular.getRowIndexMap().clear();
        tabular.setRecordset(recordset);
    }

    public static void showPopumenu(IBaseItem caller) {
        Point point = ((SmMenuItem) caller).getParent().getLocation();
        int x = (int) point.getX() + 46;
        final JPopupMenuBind popupMenuBind = JPopupMenuBind.instance();
        int y = (int) point.getY() + 52;
        JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
        popupMenuBind.init();
        popupMenuBind.show(mainFrame, x, y);
        popupMenuBind.setVisible(true);
        //窗口关闭完后删除事件
        Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
            @Override
            public void activeFormChanged(ActiveFormChangedEvent e) {
                if (null == e.getNewActiveForm()) {
                    popupMenuBind.removeEvents();
                    popupMenuBind.dispose();
                }
            }
        });
    }

    class LocalFormChangedListener implements ActiveFormChangedListener {

        @Override
        public void activeFormChanged(ActiveFormChangedEvent e) {
            if (null == e.getNewActiveForm()) {
                // 当所有地图关闭时将splitWindow设置为空，重新关联,并移除事件
//                splitWindow = null;
                propertyBindWindow.removeEvents();
            }
        }

    }

}
