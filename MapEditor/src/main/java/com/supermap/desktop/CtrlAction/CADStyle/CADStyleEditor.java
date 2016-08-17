package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Created by xie on 2016/8/10.
 */
public class CADStyleEditor extends AbstractEditor {

    private CADStyleDialog dialog;
    private MouseAdapter layerMouseListener;
    private GeometryDeletedListener geometryDeletedListener;
    private GeometryAddedListener geometryAddedListener;
    private GeometryModifiedListener geometryModifiedListener;
    private IEditController editController = new EditControllerAdapter() {
        @Override
        public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0) {
            resetRecordset(environment.getMap());
        }

        @Override
        public void undone(EditEnvironment environment, EventObject arg0) {
            resetRecordset(environment.getMap());
        }
    };

    @Override
    public void activate(final EditEnvironment environment) {
        environment.setEditController(this.editController);
        dialog = CADStyleDialog.getInstance(environment);
        if (null != getActiveRecordset(environment.getMap())) {
            dialog.showDialog(getActiveRecordset(environment.getMap()));
        }
        this.geometryAddedListener = new GeometryAddedListener() {

            @Override
            public void geometryAdded(GeometryEvent arg0) {
                resetRecordset(environment.getMap());
            }
        };
        this.geometryDeletedListener = new GeometryDeletedListener() {

            @Override
            public void geometryDeleted(GeometryEvent arg0) {
                resetRecordset(environment.getMap());
            }
        };
        this.geometryModifiedListener = new GeometryModifiedListener() {

            @Override
            public void geometryModified(GeometryEvent arg0) {
                resetRecordset(environment.getMap());
            }
        };
    }

    @Override
    public void deactivate(EditEnvironment environment) {

    }

    private void resetRecordset(Map map) {
        if (null != dialog && null != getActiveRecordset(map)) {
            dialog.showDialog(getActiveRecordset(map));
        } else if (null == getActiveRecordset(map)) {
            removeDialog();
        }
    }

    private void registEvents(EditEnvironment environment) {
        removeEvents(environment);
        environment.getMapControl().addGeometryAddedListener(geometryAddedListener);
        environment.getMapControl().addGeometryDeletedListener(geometryDeletedListener);
        environment.getMapControl().addGeometryModifiedListener(geometryModifiedListener);
        Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {

            @Override
            public void activeFormChanged(final ActiveFormChangedEvent e) {
                if (e.getNewActiveForm() instanceof IFormMap) {
                    resetRecordset(((IFormMap) e.getNewActiveForm()).getMapControl().getMap());
                    ((IFormMap) e.getNewActiveForm()).getMapControl().addGeometrySelectChangedListener(new GeometrySelectChangedListener() {

                        @Override
                        public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
                            resetRecordset(((IFormMap) e.getNewActiveForm()).getMapControl().getMap());
                            dialog.setEnabled();
                        }
                    });
                } else {
                    removeDialog();
                }
                if (null == e.getNewActiveForm()) {
                    // 销毁
                    if (null != dialog) {
                        dialog.disposeInfo();
                    }
                }
            }
        });
    }

    private void removeEvents(EditEnvironment environment) {
        if (null != environment.getMapControl()) {
            environment.getMapControl().removeGeometryAddedListener(geometryAddedListener);
            environment.getMapControl().removeGeometryDeletedListener(geometryDeletedListener);
            environment.getMapControl().removeGeometryModifiedListener(geometryModifiedListener);
        }
    }

    private void removeDialog() {
        if (null != dialog) {
            dialog.removeEvents();
            ((JPanel) dialog.getContentPane()).removeAll();
            ((JPanel) dialog.getContentPane()).updateUI();
        }
    }

    @Override
    public boolean enble(EditEnvironment environment) {
        boolean editable = isEditable(environment.getMap());
        Recordset recordset = getActiveRecordset(environment.getMap());
        if (null != dialog && editable == false) {
            dialog.enabled(false);
        } else if (null != dialog && null != recordset) {
            dialog.showDialog(recordset);
        }
        return editable && environment.getEditProperties().getSelectedGeometryCount() >= 1
                && ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOPOINT, GeometryType.GEOPOINT3D, GeometryType.GEOMULTIPOINT,
            GeometryType.GEOLINE,GeometryType.GEOLINE3D,GeometryType.GEOBSPLINE,GeometryType.GEOCARDINAL,GeometryType.GEOCURVE,GeometryType.GEOLINEM,GeometryType.GEOPARAMETRICLINE,GeometryType.GEOPARAMETRICLINECOMPOUND,GeometryType.GEOARC,
                GeometryType.GEOCHORD,GeometryType.GEOCIRCLE,GeometryType.GEOCOMPOUND,GeometryType.GEOELLIPSE,GeometryType.GEOELLIPTICARC,GeometryType.GEOPARAMETRICREGION,GeometryType.GEOPARAMETRICREGIONCOMPOUND,
                GeometryType.GEOPIE,GeometryType.GEOPIE3D,GeometryType.GEOREGION,GeometryType.GEOREGION3D,GeometryType.GEOROUNDRECTANGLE);
    }

    private boolean isEditable(Map map) {
        try {
            ArrayList<Layer> layers = MapUtilities.getLayers(map);
            for (Layer layer : layers) {
                Recordset recordset = getActiveRecordset(map);
                try {
                    if (recordset != null && layer.getDataset() == recordset.getDataset() && layer.isEditable()) {
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

    private Recordset getActiveRecordset(Map map) {
        Recordset recordset = null;
        if (map.findSelection(true).length > 0) {
            recordset = map.findSelection(true)[0].toRecordset();
        }
        return recordset;
    }

    @Override
    public boolean check(EditEnvironment environment) {
        return true;
    }
}
