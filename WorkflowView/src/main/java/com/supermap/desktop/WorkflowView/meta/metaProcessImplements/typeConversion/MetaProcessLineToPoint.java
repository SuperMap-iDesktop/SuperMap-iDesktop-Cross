package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;

import java.util.Map;

public class MetaProcessLineToPoint extends MetaProcessPointLineRegion {
    public MetaProcessLineToPoint() {
        super(DatasetType.LINE, DatasetType.POINT);
    }

    @Override
    protected void initHook() {
        OUTPUT_DATA="LineToPointResult";
    }

    @Override
    protected String getOutputName() {
        return "result_lineToPoint";
    }

    @Override
    protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
        boolean isConverted = true;

        if (geometry instanceof ILineFeature) {
            GeoLine geoLine = ((ILineFeature) geometry).convertToLine(120);

            for (int i = 0; i < geoLine.getPartCount(); i++) {
                Point2Ds points = geoLine.getPart(i);

                for (int j = 0; j < points.getCount(); j++) {
                    GeoPoint geoPoint = new GeoPoint(points.getItem(j));
                    recordset.addNew(geoPoint, value);
                    geoPoint.dispose();
                }
            }
            geoLine.dispose();
        } else {
            isConverted = false;
        }
        return isConverted;
    }

    @Override
    public String getKey() {
        return MetaKeys.Conversion_LineToPoint;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Title_LineToPoint");
    }
}
