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
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessProjection extends MetaProcess {
	private ParameterComboBox parameterComboBox;
	private IParameters parameters;

	public MetaProcessProjection() {
		parameters = new DefaultParameters();
		ParameterDataNode[] parameterDataNodes = new ParameterDataNode[]{
				// fixme 随便加了几个投影
				new ParameterDataNode(GeoCoordSysType.GCS_ADINDAN.name(), GeoCoordSysType.GCS_ADINDAN),
				new ParameterDataNode(GeoCoordSysType.GCS_WGS_1984.name(), GeoCoordSysType.GCS_WGS_1984),
				new ParameterDataNode(GeoCoordSysType.GCS_AGD_1966.name(), GeoCoordSysType.GCS_AGD_1966),
				new ParameterDataNode(GeoCoordSysType.GCS_AGD_1984.name(), GeoCoordSysType.GCS_AGD_1984),
				new ParameterDataNode(GeoCoordSysType.GCS_AIRY_MOD.name(), GeoCoordSysType.GCS_AIRY_MOD),
				new ParameterDataNode(GeoCoordSysType.GCS_ARC_1960.name(), GeoCoordSysType.GCS_ARC_1960),
				new ParameterDataNode(GeoCoordSysType.GCS_AIN_EL_ABD_1970.name(), GeoCoordSysType.GCS_AIN_EL_ABD_1970),
				new ParameterDataNode(GeoCoordSysType.GCS_ANTIGUA_ISLAND_1943.name(), GeoCoordSysType.GCS_ANTIGUA_ISLAND_1943),
				new ParameterDataNode(GeoCoordSysType.GCS_CAPE.name(), GeoCoordSysType.GCS_CAPE),
				new ParameterDataNode(GeoCoordSysType.GCS_BESSEL_1841.name(), GeoCoordSysType.GCS_BESSEL_1841)
		};
		parameterComboBox = new ParameterComboBox(CoreProperties.getString("String_ProjectionInfo"));
		parameterComboBox.setItems(parameterDataNodes);
		parameters.setParameters(parameterComboBox);
		processTask = new ProcessTask(this);
	}

	@Override
	public JComponent getComponent() {
		return parameters.getPanel();
	}

	@Override
	public void run() {
		Dataset dataset = (Dataset) inputs.getData();
		fireRunning(new RunningEvent(this, 0, "Start set geoCoorSys"));
		GeoCoordSysType geoCoordSysType = (GeoCoordSysType) ((ParameterDataNode) parameterComboBox.getSelectedItem()).getData();
		GeoCoordSys geoCoordSys = new GeoCoordSys(geoCoordSysType, GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE);
		PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
		prjCoordSys.setGeoCoordSys(geoCoordSys);
		dataset.setPrjCoordSys(prjCoordSys);
		fireRunning(new RunningEvent(this, 100, "set geoCoorSys finished"));
		ProcessData processData = new ProcessData();
		processData.setData(dataset);
		outPuts.add(0, processData);
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
}
