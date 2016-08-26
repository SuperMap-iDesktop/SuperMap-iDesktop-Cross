package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;

import java.util.ArrayList;

/**
 * Created by xie on 2016/8/10.
 */
public class CADStyleEditor extends AbstractEditor {

    private final String CADSTYLECONTAINER = "com.supermap.desktop.CtrlAction.CADStyle.CADStyleContainer";
//    private CADStyleDialog dialog;

    //    private CADStyleContainer dialog;
    private CADStyleContainer cadStyleContainer;
    private IDockbar dockbarCADStyleContainer;

    @Override
    public void activate(final EditEnvironment environment) {
        try {
            dockbarCADStyleContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(CADSTYLECONTAINER));
            if (dockbarCADStyleContainer != null && null != dockbarCADStyleContainer.getComponent()) {
                dockbarCADStyleContainer.setVisible(true);
                dockbarCADStyleContainer.active();
                cadStyleContainer = (CADStyleContainer) dockbarCADStyleContainer.getComponent();
                if (null != getActiveRecordset(environment.getMap())) {
                    cadStyleContainer.showDialog(getActiveRecordset(environment.getMap()));
                }
            }
            environment.getMapControl().addGeometrySelectChangedListener(new GeometrySelectChangedListener() {
                @Override
                public void geometrySelectChanged(GeometrySelectChangedEvent geometrySelectChangedEvent) {
//                    textStyleContainer.setModify(false);
                }
            });
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
//        dialog = CADStyleDialog.getInstance(environment);
//        if (null != getActiveRecordset(environment.getMap())) {
//            dialog.showDialog(getActiveRecordset(environment.getMap()));
//        }
    }

    @Override
    public void deactivate(EditEnvironment environment) {

    }


    @Override
    public boolean enble(EditEnvironment environment) {
        boolean editable = isEditable(environment.getMap());
        Recordset recordset = getActiveRecordset(environment.getMap());
        if (null != cadStyleContainer && editable == false) {
            cadStyleContainer.enabled(false);
        } else if (null != cadStyleContainer && null != recordset) {
            cadStyleContainer.showDialog(recordset);
        } else if (null != cadStyleContainer && null == recordset) {
            cadStyleContainer.setNullPanel();
        }
        return ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOPOINT, GeometryType.GEOPOINT3D, GeometryType.GEOMULTIPOINT,
                GeometryType.GEOLINE, GeometryType.GEOLINE3D, GeometryType.GEOBSPLINE, GeometryType.GEOCARDINAL, GeometryType.GEOCURVE, GeometryType.GEOLINEM, GeometryType.GEOPARAMETRICLINE, GeometryType.GEOPARAMETRICLINECOMPOUND, GeometryType.GEOARC,
                GeometryType.GEOCHORD, GeometryType.GEOCIRCLE, GeometryType.GEOCOMPOUND, GeometryType.GEOELLIPSE, GeometryType.GEOELLIPTICARC, GeometryType.GEOPARAMETRICREGION, GeometryType.GEOPARAMETRICREGIONCOMPOUND,
                GeometryType.GEOPIE, GeometryType.GEOPIE3D, GeometryType.GEOREGION, GeometryType.GEOREGION3D, GeometryType.GEOROUNDRECTANGLE);
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
