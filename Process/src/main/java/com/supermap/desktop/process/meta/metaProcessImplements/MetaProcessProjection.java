package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.GeoCoordSysType;
import com.supermap.data.GeoSpatialRefType;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterEnum;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessProjection extends MetaProcess {
	private ParameterDatasource datasource;
	private ParameterSingleDataset dataset;
	private ParameterEnum parameterComboBox;

	public MetaProcessProjection() {
		parameters = new DefaultParameters();
		datasource = new ParameterDatasource();
		dataset = new ParameterSingleDataset();
		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

		String[] parameterDataNodes = new String[]{
				// fixme 随便加了几个投影
				GeoCoordSysType.GCS_ADINDAN.name(),
				GeoCoordSysType.GCS_WGS_1984.name(),
				GeoCoordSysType.GCS_AGD_1966.name(),
				GeoCoordSysType.GCS_AGD_1984.name(),
				GeoCoordSysType.GCS_AIRY_MOD.name(),
				GeoCoordSysType.GCS_ARC_1960.name(),
				GeoCoordSysType.GCS_AIN_EL_ABD_1970.name(),
				GeoCoordSysType.GCS_ANTIGUA_ISLAND_1943.name(),
				GeoCoordSysType.GCS_CAPE.name(),
				GeoCoordSysType.GCS_BESSEL_1841.name()
		};
		String[] ch = new String[]{"GCS_ADINDAN", "GCS_WGS_1984", "GCS_AGD_1966", "GCS_AGD_1984", "GCS_AIRY_MOD", "GCS_ARC_1960", "GCS_AIN_EL_ABD_1970",
				"GCS_ANTIGUA_ISLAND_1943", "GCS_CAPE", "GCS_BESSEL_1841"};
		parameterComboBox = new ParameterEnum(new EnumParser(GeoCoordSysType.class, parameterDataNodes, ch)).setDescribe(CoreProperties.getString("String_ProjectionInfo"));
		parameterComboBox.setSelectedItem("GCS_WGS_1984");
		parameters.setParameters(datasource, dataset, parameterComboBox);
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

    @Override
    public void run() {
        Dataset dataset = (Dataset) inputs.getData();
        fireRunning(new RunningEvent(this, 0, "Start set geoCoorSys"));
        GeoCoordSysType geoCoordSysType = (GeoCoordSysType) parameterComboBox.getSelectedItem();
        GeoCoordSys geoCoordSys = new GeoCoordSys(geoCoordSysType, GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE);
        PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
        prjCoordSys.setGeoCoordSys(geoCoordSys);
        dataset.setPrjCoordSys(prjCoordSys);
        fireRunning(new RunningEvent(this, 100, "set geoCoorSys finished"));
        setFinished(true);
//        ProcessData processData = new ProcessData();
//        processData.setData(dataset);
//        outPuts.add(0, processData);
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public String getKey() {
        return MetaKeys.PROJECTION;
    }

    @Override
    public String getTitle() {
        return "投影转换";
    }

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/Projection.png");
	}
}
