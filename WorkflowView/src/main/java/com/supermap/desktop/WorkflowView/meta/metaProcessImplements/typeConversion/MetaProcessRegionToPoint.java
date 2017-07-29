package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;

import java.util.Map;

public class MetaProcessRegionToPoint extends MetaProcessPointLineRegion {
    public MetaProcessRegionToPoint() {
        super(DatasetType.REGION, DatasetType.POINT);
    }

    @Override
    protected void initHook() {
        OUTPUT_DATA="RegionToPointResult";
    }

    @Override
    protected String getOutputName() {
        return "result_regionToPoint";
    }

    @Override
    protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
        boolean isConverted = true;

        if (geometry instanceof IRegionFeature) {
            GeoRegion geoRegion = ((IRegionFeature) geometry).convertToRegion(120);

            for (int i = 0; i < geoRegion.getPartCount(); i++) {
                Point2Ds points = geoRegion.getPart(i);

                for (int j = 0; j < points.getCount(); j++) {
                    GeoPoint geoPoint = new GeoPoint(points.getItem(j));
                    recordset.addNew(geoPoint, value);
                    geoPoint.dispose();
                }
            }
            geoRegion.dispose();
        } else {
            isConverted = false;
        }
        return isConverted;
    }

    @Override
    public String getKey() {
        return MetaKeys.CONVERSION_REGION_TO_POINT;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Title_RegionToPoint");
    }
}
