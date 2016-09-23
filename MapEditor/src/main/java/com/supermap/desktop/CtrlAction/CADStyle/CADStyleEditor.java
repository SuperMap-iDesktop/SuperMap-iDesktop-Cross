package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.DatasetType;
import com.supermap.data.GeometryType;
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


    @Override
    public void activate(final EditEnvironment environment) {
        try {
            dockbarCADStyleContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(CADSTYLECONTAINER));
            if (dockbarCADStyleContainer != null && null != dockbarCADStyleContainer.getComponent()) {
                dockbarCADStyleContainer.setVisible(true);
                dockbarCADStyleContainer.active();
                cadStyleContainer = (CADStyleContainer) dockbarCADStyleContainer.getComponent();
                if (null != CADStyleUtilities.getActiveRecordset(environment.getMap())) {
                    cadStyleContainer.init();
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
        Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
            @Override
            public void activeFormChanged(ActiveFormChangedEvent e) {
                if (null != cadStyleContainer) {
                    cadStyleContainer.setModify(false);
                }
                if (null == e.getNewActiveForm() && null != cadStyleContainer) {
                    cadStyleContainer.setNullPanel();
                }
            }
        });
    }

    private void removeEvents(EditEnvironment environment) {
        environment.getMapControl().removeGeometrySelectedListener(this.geometrySelectChangedListener);
    }

    @Override
    public void deactivate(EditEnvironment environment) {

    }


    @Override
    public boolean enble(EditEnvironment environment) {

        boolean editable = isEditable(environment.getMap());
        Recordset recordset = CADStyleUtilities.getActiveRecordset(environment.getMap());
        // 初始化判断
        // 若选中的记录集所在的数据集不为（CAD/文本）数据集，直接屏蔽掉
        if (null != recordset && !recordset.getDataset().getType().equals(DatasetType.CAD) && !recordset.getDataset().getType().equals(DatasetType.TEXT)) {
            return false;
        }
        if (null != cadStyleContainer && editable == false) {
            cadStyleContainer.enabled(false);
        } else if (null != cadStyleContainer && null != recordset && null != dockbarCADStyleContainer) {
            cadStyleContainer.showDialog();
        } else if (null != cadStyleContainer && null == recordset) {
            cadStyleContainer.setNullPanel();
        }
        return ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOPOINT, GeometryType.GEOPOINT3D, GeometryType.GEOMULTIPOINT,
                GeometryType.GEOLINE, GeometryType.GEOLINE3D, GeometryType.GEOBSPLINE, GeometryType.GEOCARDINAL, GeometryType.GEOCURVE, GeometryType.GEOLINEM, GeometryType.GEOPARAMETRICLINE, GeometryType.GEOPARAMETRICLINECOMPOUND, GeometryType.GEOARC,
                GeometryType.GEOCHORD, GeometryType.GEOCIRCLE, GeometryType.GEOCOMPOUND, GeometryType.GEOELLIPSE, GeometryType.GEOELLIPTICARC, GeometryType.GEOPARAMETRICREGION, GeometryType.GEOPARAMETRICREGIONCOMPOUND,
                GeometryType.GEOPIE, GeometryType.GEOPIE3D, GeometryType.GEOREGION, GeometryType.GEOREGION3D, GeometryType.GEOROUNDRECTANGLE, GeometryType.GEOTEXT, GeometryType.GEOTEXT3D);
    }

    private boolean isEditable(Map map) {
        try {
            ArrayList<Layer> layers = MapUtilities.getLayers(map);
            for (Layer layer : layers) {
                Recordset recordset = CADStyleUtilities.getActiveRecordset(map);
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
    public boolean check(EditEnvironment environment) {
        return true;
    }
}
