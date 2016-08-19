package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.data.TextStyle;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;
import com.supermap.ui.GeometrySelectChangedEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EventObject;

public class TextStyleEditor extends AbstractEditor {

    private TextStyleDialog dialog;

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
    public void activate(final EditEnvironment environment) {
        if (ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D)) {
            dialog = TextStyleDialog.createInstance(environment);
            if (null != getActiveRecordset(environment.getMap())) {
                dialog.showDialog(getActiveRecordset(environment.getMap()));
            }
        }
    }


    private void removeDialog() {
        if (null != dialog) {
            TextStyle textStyle = dialog.getTempTextStyle();
            textStyle = null;
            ((JPanel) dialog.getContentPane()).removeAll();
            ((JPanel) dialog.getContentPane()).updateUI();
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
