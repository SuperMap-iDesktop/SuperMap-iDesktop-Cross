package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoLine;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;

import java.util.Map;

public class MetaProcessRegionToLine extends MetaProcessPointLineRegion {
    public MetaProcessRegionToLine() {
        super(DatasetType.REGION, DatasetType.LINE);
    }

    @Override
    protected void initHook() {
        OUTPUT_DATA="RegionToLineResult";
    }

    @Override
    protected String getOutputName() {
        return "result_regionToLine";
    }

    @Override
    protected boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value) {
        boolean isConverted = true;

        if (geometry instanceof IRegionFeature && geometry instanceof ILineConvertor) {
            GeoLine geoLine = null;
            try {
                geoLine = ((ILineConvertor) geometry).convertToLine(120);
            } catch (UnsupportedOperationException e) {
                Application.getActiveApplication().getOutput().output(e);
            }

            if (geoLine != null) {
                recordset.addNew(geoLine, value);
                geoLine.dispose();
            } else {
                isConverted = false;
            }
        } else {
            isConverted = false;
        }
        return isConverted;
    }

    @Override
    public String getKey() {
        return MetaKeys.CONVERSION_REGION_TO_LINE;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Title_RegionToLine");
    }
}
