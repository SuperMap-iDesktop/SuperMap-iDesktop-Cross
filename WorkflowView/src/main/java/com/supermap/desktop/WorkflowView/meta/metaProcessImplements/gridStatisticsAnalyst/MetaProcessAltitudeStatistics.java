package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridStatisticsAnalyst;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.awt.*;

/**
 * Created by lixiaoyao on 2017/8/17.
 */
public class MetaProcessAltitudeStatistics extends MetaProcess {
	private final static String INTPUT_DATA_POINT = CommonProperties.getString("String_PointData");
	private final static String INTPUT_DATA_GRID = CommonProperties.getString("String_GridData");
	private final static String OUTPUT_DATA = "AltitudeResult";

	private ParameterDatasourceConstrained pointDatasource;
	private ParameterSingleDataset pointDataset;
	private ParameterDatasourceConstrained gridDatasource;
	private ParameterSingleDataset gridDataset;
	private ParameterSaveDataset resultDataset;

	public MetaProcessAltitudeStatistics() {
		initParameters();
		initParametersState();
		initParameterConstrint();
	}

	private void initParameters() {
		this.pointDatasource = new ParameterDatasourceConstrained();
		this.pointDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.pointDataset = new ParameterSingleDataset(DatasetType.POINT);
		this.pointDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.gridDatasource = new ParameterDatasourceConstrained();
		this.gridDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.gridDataset = new ParameterSingleDataset(DatasetType.GRID);
		this.gridDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));

		ParameterCombine pointData = new ParameterCombine();
		pointData.setDescribe(INTPUT_DATA_POINT);
		pointData.addParameters(this.pointDatasource, this.pointDataset);
		ParameterCombine gridData = new ParameterCombine();
		gridData.setDescribe(INTPUT_DATA_GRID);
		gridData.addParameters(this.gridDatasource, this.gridDataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(this.resultDataset);

		this.parameters.setParameters(pointData, gridData, targetData);
		this.parameters.addInputParameters(INTPUT_DATA_POINT, DatasetTypes.POINT, pointData);
		this.parameters.addInputParameters(INTPUT_DATA_GRID, DatasetTypes.GRID, gridData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.POINT3D, targetData);
	}

	private void initParametersState() {
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
		if (dataset != null) {
			this.pointDatasource.setSelectedItem(dataset.getDatasource());
			this.pointDataset.setSelectedItem(dataset);
			this.resultDataset.setResultDatasource(dataset.getDatasource());
			this.resultDataset.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_AltitudeStatistics"));
			Dataset datasetGrid = DatasetUtilities.getDefaultDataset(DatasetType.GRID);
			this.gridDatasource.setSelectedItem(datasetGrid.getDatasource());
			this.gridDataset.setSelectedItem(datasetGrid);
		}
	}

	private void initParameterConstrint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.pointDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.pointDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint gridConstraint = new EqualDatasourceConstraint();
		gridConstraint.constrained(this.gridDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		gridConstraint.constrained(this.gridDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessAltitudeStatistics.this, 0, "start"));
			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			DatasetVector srcPoint = null;
			if (this.getParameters().getInputs().getData(INTPUT_DATA_POINT).getValue() != null) {
				srcPoint = (DatasetVector) this.getParameters().getInputs().getData(INTPUT_DATA_POINT).getValue();
			} else {
				srcPoint = (DatasetVector) pointDataset.getSelectedItem();
			}
			DatasetGrid srcGrid = null;
			if (this.getParameters().getInputs().getData(INTPUT_DATA_GRID).getValue() != null) {
				srcGrid = (DatasetGrid) this.getParameters().getInputs().getData(INTPUT_DATA_GRID).getValue();
			} else {
				srcGrid = (DatasetGrid) gridDataset.getSelectedItem();
			}

			DatasetVector result = null;
			DatasetVector point3DResult = null;
			point3DResult = createPoint3DDataset(datasetName, srcPoint.getPrjCoordSys());

			Recordset sourceRecordset = null;
			if (point3DResult != null) {
				QueryParameter queryParm = new QueryParameter();
				queryParm.setSpatialQueryMode(SpatialQueryMode.CONTAIN);
				queryParm.setSpatialQueryObject(srcGrid.getBounds());
				queryParm.setCursorType(CursorType.STATIC);
				sourceRecordset = srcPoint.query(queryParm);
			}

			// 保存点结果
			if (point3DResult != null) {
				Recordset point3DRecordset = point3DResult.query("", CursorType.DYNAMIC);
				if (point3DRecordset.getBatch() != null) {
					point3DRecordset.getBatch().setMaxRecordCount(2000);
					point3DRecordset.getBatch().begin();
				}

				GeoPoint geoPoint = null;
				sourceRecordset.moveFirst();
				while (!sourceRecordset.isEOF()) {
					geoPoint = (GeoPoint) sourceRecordset.getGeometry();
					Point gridPoint = srcGrid.xyToGrid(geoPoint.getInnerPoint());
					Double altitude = srcGrid.getNoValue();
					if (gridPoint.getX() < srcGrid.getWidth() && gridPoint.getX() >= 0
							&& gridPoint.getY() < srcGrid.getHeight() && gridPoint.getY() >= 0) {
						altitude = srcGrid.getValue((int) gridPoint.getX(), (int) gridPoint.getY());
					}
					GeoPoint3D geoPoint3D = new GeoPoint3D(geoPoint.getX(), geoPoint.getY(), altitude);
					point3DRecordset.addNew(geoPoint3D);
					if (point3DRecordset.getBatch() == null) {
						point3DRecordset.update();
					}
					sourceRecordset.moveNext();
				}

				if (point3DRecordset.getBatch() != null) {
					point3DRecordset.getBatch().update();
				}
				result = point3DResult;
			}

			isSuccessful = result != null;
			fireRunning(new RunningEvent(MetaProcessAltitudeStatistics.this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			return isSuccessful;
		}
	}

	private DatasetVector createPoint3DDataset(String resultName, PrjCoordSys prjCoordSys) {
		DatasetVector dataset = null;
		try {
			DatasetVectorInfo datasetInfo = new DatasetVectorInfo(resultName, DatasetType.POINT3D);
			dataset = this.resultDataset.getResultDatasource().getDatasets().create(datasetInfo);
			dataset.setPrjCoordSys(prjCoordSys);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return dataset;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.HEIGHT_VALUE_STATISTIC;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_HeightValueStatistics");
	}
}
