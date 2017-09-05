package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridDistance;

import com.supermap.analyst.spatialanalyst.DistanceAnalyst;
import com.supermap.analyst.spatialanalyst.DistanceAnalystParameter;
import com.supermap.analyst.spatialanalyst.DistanceAnalystResult;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

/**
 * Created by lixiaoyao on 2017/8/7.
 */
public class MetaProcessCreateDistanceRaster extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String COST_DATA = ProcessProperties.getString("String_GroupBox_CostData");
	private final static String OUTPUT_DATA_DISTANCE = "CreateDistanceResult";
	private final static String OUTPUT_DATA_DIRECTION = "CreateDirectionResult";
	private final static String OUTPUT_DATA_ALLOCATION = "CreateAllocationResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterDatasourceConstrained costDatasource;
	private ParameterSingleDataset costDataset;
	private ParameterNumber parameterNumberMaxDistance;
	private ParameterNumber parameterNumberResolvingPower;
	private ParameterDatasource resultDatasource;
	private ParameterTextField resultDistanceDataset;
	private ParameterTextField resultDirectionDataset;
	private ParameterTextField resultAllocationDataset;

	public MetaProcessCreateDistanceRaster() {
		initParameters();
		initParametersState();
		initParameterConstraint();
		registerListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.sourceDataset = new ParameterSingleDataset(DatasetType.GRID, DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		this.sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.costDatasource = new ParameterDatasourceConstrained();
		this.costDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.costDataset = new ParameterSingleDataset(DatasetType.GRID);
		this.costDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.parameterNumberMaxDistance = new ParameterNumber(ProcessProperties.getString("String_MaxDistance"));
		this.parameterNumberResolvingPower = new ParameterNumber(ProcessProperties.getString("String_Resolution"));
		this.resultDatasource = new ParameterDatasource();
		this.resultDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.resultDistanceDataset = new ParameterTextField(ProcessProperties.getString("String_Distance_Dataset"));
		this.resultDirectionDataset = new ParameterTextField(ProcessProperties.getString("String_Direction_Dataset"));
		this.resultAllocationDataset = new ParameterTextField(ProcessProperties.getString("String_Allocation_Dataset"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(this.sourceDatasource, this.sourceDataset);
		ParameterCombine costData = new ParameterCombine();
		costData.setDescribe(ProcessProperties.getString("String_GroupBox_CostData"));
		costData.addParameters(this.costDatasource, this.costDataset);
		ParameterCombine parameterSetting = new ParameterCombine();
		parameterSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterSetting.addParameters(this.parameterNumberMaxDistance, this.parameterNumberResolvingPower);
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(this.resultDatasource, this.resultDistanceDataset, this.resultDirectionDataset, this.resultAllocationDataset);

		this.parameters.setParameters(sourceData, costData, parameterSetting, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.SIMPLE_VECTOR_AND_GRID, sourceData);
		this.parameters.addInputParameters(COST_DATA, DatasetTypes.GRID, costData);
		this.parameters.addOutputParameters(OUTPUT_DATA_DISTANCE,
				ProcessOutputResultProperties.getString("String_DistanceResult"),
				DatasetTypes.GRID, this.resultDistanceDataset);
		this.parameters.addOutputParameters(OUTPUT_DATA_DIRECTION,
				ProcessOutputResultProperties.getString("String_DirectionResult"),
				DatasetTypes.GRID, this.resultDirectionDataset);
		this.parameters.addOutputParameters(OUTPUT_DATA_ALLOCATION,
				ProcessOutputResultProperties.getString("String_AllocationResult"),
				DatasetTypes.GRID, this.resultAllocationDataset);
	}

	private void initParametersState() {
		this.costDataset.setShowNullValue(true);
		this.parameterNumberMaxDistance.setMinValue(0);
		this.parameterNumberMaxDistance.setIsIncludeMin(true);
		this.parameterNumberMaxDistance.setSelectedItem(0);
		this.parameterNumberMaxDistance.setRequisite(true);
		this.parameterNumberResolvingPower.setMinValue(0);
		this.parameterNumberResolvingPower.setIsIncludeMin(false);
		this.parameterNumberResolvingPower.setRequisite(true);
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.GRID);
		if (dataset != null) {
			this.sourceDatasource.setSelectedItem(dataset.getDatasource());
			this.sourceDataset.setSelectedItem(dataset);
			this.costDatasource.setSelectedItem(dataset.getDatasource());
			this.costDataset.setDatasource(dataset.getDatasource());
			this.resultDatasource.setSelectedItem(dataset.getDatasource());
			this.resultDistanceDataset.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Distance"));
			this.resultDirectionDataset.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Direction"));
			this.resultAllocationDataset.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Allocation"));

			parameterNumberResolvingPower.setSelectedItem(convertToDecimal(updateCellSize(dataset, (DatasetGrid) costDataset.getSelectedItem())));
		}
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint directionConstraint = new EqualDatasourceConstraint();
		directionConstraint.constrained(this.costDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		directionConstraint.constrained(this.costDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.resultDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(this.resultDistanceDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(this.resultDirectionDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(this.resultAllocationDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		this.sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof Dataset) {
					parameterNumberResolvingPower.setSelectedItem(convertToDecimal(updateCellSize((Dataset) evt.getNewValue(), (DatasetGrid) costDataset.getSelectedItem())));
				}
			}
		});
		this.costDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				parameterNumberResolvingPower.setSelectedItem(convertToDecimal(updateCellSize(sourceDataset.getSelectedItem(), (DatasetGrid) evt.getNewValue())));
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessCreateDistanceRaster.this, 0, "start"));

			DistanceAnalystParameter distanceAnalystParameter = new DistanceAnalystParameter();
			String distanceDatasetName = this.resultDistanceDataset.getSelectedItem().toString();
			distanceDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(distanceDatasetName);
			String directionDatasetName = this.resultDirectionDataset.getSelectedItem().toString();
			directionDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(directionDatasetName);
			String allocationDatasetName = this.resultAllocationDataset.getSelectedItem().toString();
			allocationDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(allocationDatasetName);

			Dataset src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (Dataset) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = sourceDataset.getSelectedItem();
			}
			DatasetGrid cost = null;
			if (this.getParameters().getInputs().getData(COST_DATA).getValue() != null) {
				cost = (DatasetGrid) this.getParameters().getInputs().getData(COST_DATA).getValue();
			} else if(costDataset.getSelectedItem()!=null) {
				cost = (DatasetGrid) costDataset.getSelectedItem();
			}
			DistanceAnalyst.addSteppedListener(steppedListener);
			distanceAnalystParameter.setDistanceGridName(distanceDatasetName);
			distanceAnalystParameter.setDirectionGridName(directionDatasetName);
			distanceAnalystParameter.setAllocationGridName(allocationDatasetName);
			distanceAnalystParameter.setSourceDataset(src);
			distanceAnalystParameter.setTargetDatasource(this.resultDatasource.getSelectedItem());
			distanceAnalystParameter.setMaxDistance(Double.valueOf(this.parameterNumberMaxDistance.getSelectedItem().toString()));
			distanceAnalystParameter.setCellSize(Double.valueOf(this.parameterNumberResolvingPower.getSelectedItem().toString()));
			DistanceAnalystResult distanceAnalystResult = null;
			if (cost != null) {
				distanceAnalystParameter.setCostGrid(cost);
				distanceAnalystResult = DistanceAnalyst.costDistance(distanceAnalystParameter);
			} else {
				distanceAnalystResult = DistanceAnalyst.straightDistance(distanceAnalystParameter);
			}

			this.getParameters().getOutputs().getData(OUTPUT_DATA_DISTANCE).setValue(distanceAnalystResult.getDistanceDatasetGrid());
			this.getParameters().getOutputs().getData(OUTPUT_DATA_DIRECTION).setValue(distanceAnalystResult.getDirectionDatasetGrid());
			this.getParameters().getOutputs().getData(OUTPUT_DATA_ALLOCATION).setValue(distanceAnalystResult.getAllocationDatasetGrid());
			isSuccessful = distanceAnalystResult.getDistanceDatasetGrid() != null && distanceAnalystResult.getDirectionDatasetGrid() != null && distanceAnalystResult.getAllocationDatasetGrid() != null;
			fireRunning(new RunningEvent(MetaProcessCreateDistanceRaster.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			DistanceAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CREATE_DISTANCE_RASTER;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CreateDistanceRaster");
	}

	private double updateCellSize(Dataset dataset, DatasetGrid datasetGrid) {
		double cellSize = 1;
		if (datasetGrid != null) {
			cellSize = datasetGrid.getBounds().getWidth() / datasetGrid.getWidth();
		} else if (dataset != null) {
			Rectangle2D bounds = dataset.getBounds();
			if (dataset.getType().equals(DatasetType.GRID)) {
				cellSize = bounds.getWidth() / ((DatasetGrid) dataset).getWidth();
			} else {
				cellSize = bounds.getWidth() > bounds.getHeight() ? bounds.getWidth() : bounds.getHeight();
				if (dataset.getType().equals(DatasetType.POINT) && ((DatasetVector) dataset).getRecordCount() == 1) {
					cellSize = Math.abs(bounds.getLeft()) < Math.abs(bounds.getBottom()) ? Math.abs(bounds.getLeft()) : Math.abs(bounds.getBottom());
				}
				cellSize = cellSize / 500;
			}
		}

		return cellSize;
	}

	private String convertToDecimal(double num) {
		DecimalFormat format = new DecimalFormat("#0.000000000000000");
		String result = format.format(num);
		while (result.charAt(result.length() - 1) == '0') {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}
