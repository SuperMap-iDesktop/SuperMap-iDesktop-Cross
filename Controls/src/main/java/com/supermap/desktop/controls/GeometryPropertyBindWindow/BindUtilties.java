package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.ui.FormManager;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.layout.FlowLayoutStrategy;
import com.supermap.desktop.ui.mdi.layout.ILayoutStrategy;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;

public class BindUtilties {

    private static final String KEY_FORM_MAP = "group_formMap";
    private static final String KEY_FORM_TABULAR = "group_formTabular";

    //    private static MdiGroup formMapGroup;
//    private static MdiGroup formTabularGroup;
    private static MapControl mapControl;
    private static IFormTabular tabular;

    public static void windowBindProperty(IFormMap formMap) {
        BindHandler handler = BindHandler.getInstance();
        handler.getFormMapList().clear();
        handler.getFormTabularList().clear();
        handler.getFormMapList().add(formMap);
        handler.getFormTabularList().add(tabular);
        handler.getFormsList().add(formMap);
        handler.getFormsList().add(tabular);
        handler.bindFormMapsAndFormTabulars();
        resetMDILayout();
    }

    public static void resetMDILayout() {
        BindHandler handler = BindHandler.getInstance();
        FormManager formManager = (FormManager) Application.getActiveApplication().getMainFrame().getFormManager();
        int formMapsSize = handler.getFormMapList().size();
        int formsSize = handler.getFormsList().size();
        int formTabularsSize = handler.getFormTabularList().size();
        if (formsSize == formMapsSize) {
            ILayoutStrategy strategy = formManager.getLayoutStrategy();
            if (strategy instanceof FlowLayoutStrategy) {
                ((FlowLayoutStrategy) strategy).setLayoutMode(FlowLayoutStrategy.HORIZONTAL);
            }
            formManager.createGroup();

            int groupCount = formManager.getGroupCount();
            for (int i = 0; i < groupCount; i++) {
                MdiPage mapPage = formManager.getPage((Component) handler.getFormMapList().get(i));
                formManager.getGroup(i).addPage(mapPage);
            }
        } else if (formsSize == formTabularsSize) {
            ILayoutStrategy strategy = formManager.getLayoutStrategy();
            if (strategy instanceof FlowLayoutStrategy) {
                ((FlowLayoutStrategy) strategy).setLayoutMode(FlowLayoutStrategy.VERTICAL);
            }
        } else {

        }
        if (formMapsSize > 0) {
            Application.getActiveApplication().setActiveForm((IForm) handler.getFormMapList().get(0));
        }
    }

    /**
     * 创建属性表
     *
     * @param dataset
     * @param recordset
     */
    public static void openTabular(Dataset dataset, Recordset recordset) {
        // 打开一个默认的属性表，然后修改属性表的title和数据与当前图层对应的数据匹配
        tabular = TabularUtilities.openDatasetVectorFormTabular(dataset);
        tabular.setText(dataset.getName() + "@" + dataset.getDatasource().getAlias());
        tabular.setRecordset(recordset);
    }

    /**
     * 打开关联浏览窗口
     *
     * @param caller
     */
    public static void showPopumenu(IBaseItem caller) {
        Point point = ((SmMenuItem) caller).getParent().getLocation();
        int x = (int) point.getX() + 46;
        final JPopupMenuBind popupMenuBind = JPopupMenuBind.instance();
        int y = (int) point.getY() + 52;
        JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
        popupMenuBind.removeBind();
        popupMenuBind.init();
        popupMenuBind.show(mainFrame, x, y);
        popupMenuBind.setVisible(true);
        //窗口关闭完后删除事件
        Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
            @Override
            public void activeFormChanged(ActiveFormChangedEvent e) {
                if (null == e.getNewActiveForm()) {
                    popupMenuBind.removeBind();
                    popupMenuBind.removeEvents();
                    popupMenuBind.dispose();
                }
            }
        });
    }

}
