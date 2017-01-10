package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ITextFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import java.util.List;

/**
 * Created by lixiaoyao on 2017/1/6.
 */
public class DefaultTextStyle {
    private static double rotationAngle = 0;
    private static TextStyle defaultTextStyle = null;

    public static void setRotationAngle(double tempRotationAngle) {
        rotationAngle = tempRotationAngle;
    }

    public static double getRotationAngle() {
        return rotationAngle;
    }

    public static void setDefaultGeoStyle(TextStyle tempGeoStyle) {
        defaultTextStyle = tempGeoStyle.clone();
    }

    public static TextStyle getDefaultGeoStyle() {
        return defaultTextStyle;
    }

    public static void setTextDefaultStyle(EditEnvironment environment) {
        List<Layer> layers = environment.getEditProperties().getSelectedLayers();

        for (Layer layer : layers) {
            if (layer.isEditable()
                    && layer.getDataset() != null
                    && layer.getDataset() instanceof DatasetVector
                    && (layer.getDataset().getType() == DatasetType.TEXT || layer.getDataset().getType() == DatasetType.CAD)
                    && layer.getSelection().getCount() > 0) {

                Recordset recordset = layer.getSelection().toRecordset();
                try {
                    while (!recordset.isEOF()) {
                        IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
                        if (geometry instanceof ITextFeature) {
                            defaultTextStyle = (((GeoText) recordset.getGeometry()).getTextStyle().clone());
                            rotationAngle=(((GeoText) recordset.getGeometry()).getPart(0).getRotation());
                            break;
                        }
                        geometry.dispose();
                        geometry = null;
                        recordset.moveNext();
                    }
                } catch (Exception ex) {
                    Application.getActiveApplication().getOutput().output(ex);
                } finally {
                    if (recordset != null) {
                        recordset.close();
                        recordset.dispose();
                    }
                }
            }
        }
    }
}
