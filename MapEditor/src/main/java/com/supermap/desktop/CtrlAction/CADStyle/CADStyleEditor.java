package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.DatasetType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.DockingWindowAdapter;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.event.WindowClosingEvent;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapDrawingEvent;
import com.supermap.mapping.MapDrawingListener;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.GeometrySelectedListener;

import java.util.ArrayList;

/**
 * Created by xie on 2016/8/10.
 */
public class CADStyleEditor extends AbstractEditor {

    private final String CADSTYLECONTAINER = "com.supermap.desktop.CtrlAction.CADStyle.CADStyleContainer";

    private CADStyleContainer cadStyleContainer;
    private IDockbar dockbarCADStyleContainer;
    private GeometrySelectedListener geometrySelectChangedListener = new GeometrySelectedListener() {
        @Override
        public void geometrySelected(GeometrySelectedEvent geometrySelectedEvent) {
            if (null != cadStyleContainer) {
                cadStyleContainer.setModify(false);
            }
        }
    };
    private MapDrawingListener mapDrawingListener = new MapDrawingListener() {
        @Override
        public void mapDrawing(MapDrawingEvent mapDrawingEvent) {
            if (null != cadStyleContainer) {
                cadStyleContainer.setModify(true);
            }
        }
    };


    @Override
    public void activate(final EditEnvironment environment) {
        try {
            dockbarCADStyleContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(CADSTYLECONTAINER));
            if (dockbarCADStyleContainer != null && null != dockbarCADStyleContainer.getComponent()) {
                dockbarCADStyleContainer.setVisible(true);
                dockbarCADStyleContainer.active();
                cadStyleContainer = (CADStyleContainer) dockbarCADStyleContainer.getComponent();
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(environment.getMap());
                if (null != recordsets) {
                    cadStyleContainer.init(recordsets);
                }
            }
            ((Dockbar) dockbarCADStyleContainer).addListener(new DockingWindowAdapter() {

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

    private void registEvents(EditEnvironment environment) {
        removeEvents(environment);
        environment.getMapControl().addGeometrySelectedListener(this.geometrySelectChangedListener);
        environment.getMapControl().getMap().addDrawingListener(this.mapDrawingListener);
        Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
            @Override
            public void activeFormChanged(ActiveFormChangedEvent e) {
                if (null != cadStyleContainer) {
                    cadStyleContainer.setModify(false);
                }
                if (null != cadStyleContainer && null == e.getNewActiveForm()) {
                    cadStyleContainer.setNullPanel();
                }
            }
        });
    }

    private void removeEvents(EditEnvironment environment) {
        environment.getMapControl().removeGeometrySelectedListener(this.geometrySelectChangedListener);
        environment.getMapControl().getMap().removeDrawingListener(this.mapDrawingListener);
    }

    @Override
    public void deactivate(EditEnvironment environment) {

    }


    @Override
    public boolean enble(EditEnvironment environment) {

        boolean editable = isEditable(environment.getMap());
        ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(environment.getMap());
        if (null != cadStyleContainer && editable == false) {
            cadStyleContainer.enabled(false);
        } else if (null != cadStyleContainer && editable == true) {
            cadStyleContainer.setModify(false);
        }
        if (null != cadStyleContainer && null != recordsets && null != dockbarCADStyleContainer) {
            cadStyleContainer.showDialog(recordsets);
        } else if (null != cadStyleContainer && null == recordsets) {
            cadStyleContainer.setNullPanel();
        }
        return ListUtilities.isListContainAny(environment.getEditProperties().getSelectedDatasetTypes(), DatasetType.CAD, DatasetType.TEXT);
    }

    private boolean isEditable(Map map) {
        ArrayList<Recordset> recordset = CADStyleUtilities.getActiveRecordset(map);
        if (null == recordset) {
            return true;
        }
        int count = recordset.size();
        try {
            ArrayList<Layer> layers = MapUtilities.getLayers(map);
            for (Layer layer : layers) {
                try {
                    for (int i = 0; i < count; i++) {
                        if (layer.isEditable()) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    // ignore
                } finally {
                    if (recordset != null) {
                        for (int i = 0; i < count; i++) {
                            recordset.get(i).dispose();
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            // 地图dispose没接口判断
        }
        return false;
    }

    @Override
    public boolean check(EditEnvironment environment) {
        return true;
    }
}
