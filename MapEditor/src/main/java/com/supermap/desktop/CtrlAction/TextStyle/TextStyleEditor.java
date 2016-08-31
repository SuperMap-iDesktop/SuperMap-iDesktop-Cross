package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.DockingWindowAdapter;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.event.WindowClosingEvent;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.*;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;

import java.util.ArrayList;

public class TextStyleEditor extends AbstractEditor {

    private TextStyleContainer textStyleContainer;
    private static IDockbar dockbarTextStyleContainer;
    private final String TEXTSTYLECONTAINER = "com.supermap.desktop.CtrlAction.TextStyle.TextStyleContainer";
    private MapClosedListener mapClosedListener = new MapClosedListener() {
        @Override
        public void mapClosed(MapClosedEvent mapClosedEvent) {
            if (null != textStyleContainer) {
                textStyleContainer.setNullPanel();
            }
        }
    };
    private GeometrySelectChangedListener geometrySelectChangedListener = new GeometrySelectChangedListener() {
        @Override
        public void geometrySelectChanged(GeometrySelectChangedEvent geometrySelectChangedEvent) {
            textStyleContainer.setModify(false);
        }
    };

    @Override
    public boolean enble(EditEnvironment environment) {
        boolean editable = isEditable(environment.getMap());
        Recordset recordset = getActiveRecordset(environment.getMap());
        if (null != textStyleContainer && editable == false) {
            // 线判断风格设置界面是否可用
            textStyleContainer.enabled(false);
        } else if (null != textStyleContainer && null != recordset) {
            // 显示当前选中对象的风格
            textStyleContainer.showDialog(recordset);
        } else if (null != textStyleContainer && null == recordset) {
            // 显示空界面
            textStyleContainer.setNullPanel();
        }
        return ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D);
    }

    private boolean isEditable(Map map) {
        try {
            ArrayList<Layer> layers = MapUtilities.getLayers(map);
            for (Layer layer : layers) {
                Recordset recordset = getActiveRecordset(map);
                try {
                    if (recordset != null && layer.getDataset() == recordset.getDataset() && layer.isEditable()) {
                        return true;
                    } else if (recordset == null && layer.isEditable()) {
                        return true;
                    }
                } catch (Exception e) {
                    // ignore
                } finally {
                    if (recordset != null) {
                        recordset.dispose();
                    }
                }
            }
        } catch (Exception ignore) {
            // 地图dispose没接口判断
        }
        return false;
    }

    @Override
    public void activate(final EditEnvironment environment) {
        if (ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D)) {
            try {
                dockbarTextStyleContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TEXTSTYLECONTAINER));
                if (dockbarTextStyleContainer != null && null != dockbarTextStyleContainer.getComponent()) {
                    dockbarTextStyleContainer.setVisible(true);
                    dockbarTextStyleContainer.active();
                    textStyleContainer = (TextStyleContainer) dockbarTextStyleContainer.getComponent();
                    if (null != getActiveRecordset(environment.getMap())) {
                        textStyleContainer.showDialog(getActiveRecordset(environment.getMap()));
                    }
                }
                ((Dockbar) dockbarTextStyleContainer).addListener(new DockingWindowAdapter() {

                    @Override
                    public void windowClosing(WindowClosingEvent evt) throws OperationAbortedException {
                        // 关闭dockbar时，关闭编辑
                        environment.stopEditor();
                    }

                    @Override
                    public void windowClosed(DockingWindow window) {
                        environment.stopEditor();
                    }

                });
                registEvents(environment);
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    }

    private void registEvents(EditEnvironment environment) {
        removeEvents(environment);
        environment.getMap().addMapClosedListener(this.mapClosedListener);
        environment.getMapControl().addGeometrySelectChangedListener(this.geometrySelectChangedListener);
    }

    private void removeEvents(EditEnvironment environment) {
        environment.getMap().removeMapClosedListener(this.mapClosedListener);
        environment.getMapControl().removeGeometrySelectChangedListener(this.geometrySelectChangedListener);
    }

    private Recordset getActiveRecordset(Map map) {
        Recordset recordset = null;
        if (map.findSelection(true).length > 0) {
            Selection selection = MapUtilities.getActiveMap().findSelection(true)[0];
            recordset = selection.toRecordset();
        }
        return recordset;
    }
}
