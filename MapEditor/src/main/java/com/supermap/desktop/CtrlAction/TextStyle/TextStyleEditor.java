package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;

import java.util.ArrayList;

public class TextStyleEditor extends AbstractEditor {

    private TextStyleContainer dialog;
    private static IDockbar dockbarTextStyleContainer;
    private final String TEXTSTYLECONTAINER = "com.supermap.desktop.CtrlAction.TextStyle.TextStyleContainer";

    @Override
    public boolean enble(EditEnvironment environment) {
        boolean editable = isEditable(environment.getMap());
        Recordset recordset = getActiveRecordset(environment.getMap());
        if (null != dialog && editable == false) {
            dialog.enabled(false);
        } else if (null != dialog && null != recordset) {
            dialog.showDialog(recordset);
        } else if (null != dialog && null == recordset) {
            removeDialog();
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
    public void activate(EditEnvironment environment) {
        if (ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D)) {
//            dialog = TextStyleContainer.createInstance(environment);
//            try {
//                dockbarTextStyleContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TEXTSTYLECONTAINER));
//                if (dockbarTextStyleContainer != null && null != dockbarTextStyleContainer.getComponent()) {
//                    dialog = (TextStyleContainer) dockbarTextStyleContainer.getComponent();
//                    if (null != getActiveRecordset(environment.getMap())) {
//                        dialog.setEnvironment(environment);
//                        dialog.showDialog(getActiveRecordset(environment.getMap()));
//                    }
//                }
//
//            } catch (Exception ex) {
//                Application.getActiveApplication().getOutput().output(ex);
//            }
        }
    }


    private void removeDialog() {
        if (null != dialog) {
            TextStyle textStyle = dialog.getTempTextStyle();
            textStyle = null;
            dialog.removeAll();
            dialog.updateUI();
        }
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
