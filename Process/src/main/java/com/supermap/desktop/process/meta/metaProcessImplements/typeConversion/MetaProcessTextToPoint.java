package com.supermap.desktop.process.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

import java.util.Map;

/**
 * Created By Chens on 2017/7/22 0022
 */
public class MetaProcessTextToPoint extends MetaProcessPointLineRegion {
    public MetaProcessTextToPoint() {
        super(DatasetType.TEXT, DatasetType.POINT);
    }

    @Override
    protected void initHook() {
        OUTPUT_DATA="TextToPointResult";
    }

    @Override
    protected String getOutputName() {
        return "result_textToPoint";
    }

    @Override
    public String getKey() {
        return MetaKeys.CONVERSION_TEXT_TO_POINT;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Title_TextToPoint");
    }

    @Override
    protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
        boolean isConverted = true;
        GeoText geoText = (GeoText) geometry.getGeometry();

        for (int i = 0; i < geoText.getPartCount(); i++) {
            Point2D point = new Point2D(geoText.getPart(i).getX(), geoText.getPart(i).getY());
            GeoPoint geoPoint = new GeoPoint(point);
            recordset.addNew(geoPoint, value);
            geoPoint.dispose();
            }
        geoText.dispose();

        isConverted = recordset != null;
        return isConverted;
    }
}
