package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.DistanceAnalyst;
import com.supermap.analyst.spatialanalyst.DistanceAnalystParameter;
import com.supermap.analyst.spatialanalyst.PathLineResult;
import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/7 0007
 * 最小耗费距离
 */
public class MetaProcessCostPathLine extends MetaProcess {
	private static final String COST_DATA = ProcessProperties.getString("String_GroupBox_CostData");
	private static final String OUTPUT_DATA = "CostPathLineResult";

	private ParameterDatasourceConstrained costDatasources;
	private ParameterSingleDataset costDataset;
	private ParameterSaveDataset resultDataset;
	private ParameterLabel labelOrigin;
	private ParameterLabel labelTarget;
	private ParameterNumber numberOriginX;
	private ParameterNumber numberOriginY;
	private ParameterNumber numberTargetX;
	private ParameterNumber numberTargetY;
	private ParameterComboBox comboBoxSmoothMethod;
	private ParameterNumber numberSmoothDegree;

	public MetaProcessCostPathLine() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		costDatasources = new ParameterDatasourceConstrained();
		costDataset = new ParameterSingleDataset(DatasetType.GRID);
		resultDataset = new ParameterSaveDataset();
		labelOrigin = new ParameterLabel();
		labelOrigin.setDescribe(ProcessProperties.getString("String_OriginPoint"));
		labelTarget = new ParameterLabel();
		labelTarget.setDescribe(ProcessProperties.getString("String_TargetPoint"));
		numberOriginX = new ParameterNumber(ProcessProperties.getString("String_Xcoordinate"));
		numberOriginY = new ParameterNumber(ProcessProperties.getString("String_Ycoordinate"));
		numberTargetX = new ParameterNumber(ProcessProperties.getString("String_Xcoordinate"));
		numberTargetY = new ParameterNumber(ProcessProperties.getString("String_Ycoordinate"));
		comboBoxSmoothMethod = new ParameterComboBox(CommonProperties.getString("String_SmoothMethod"));
		numberSmoothDegree = new ParameterNumber(ProcessProperties.getString("String_Label_Smoothness"));

		ParameterCombine costCombine = new ParameterCombine();
		costCombine.setDescribe(ProcessProperties.getString("String_GroupBox_CostData"));
		costCombine.addParameters(costDatasources, costDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		settingCombine.addParameters(labelOrigin, numberOriginX, numberOriginY, labelTarget, numberTargetX, numberTargetY, comboBoxSmoothMethod, numberSmoothDegree);
		ParameterCombine outputCombine = new ParameterCombine();
		outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		outputCombine.addParameters(resultDataset);

		parameters.setParameters(costCombine, settingCombine, outputCombine);
		parameters.addInputParameters(COST_DATA, DatasetTypes.GRID, costCombine);
		parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_ShorestPathLineResult"),
				DatasetTypes.LINE, outputCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource1 = new EqualDatasourceConstraint();
		constraintSource1.constrained(costDatasources, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource1.constrained(costDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		numberOriginX.setSelectedItem(0.0);
		numberOriginY.setSelectedItem(0.0);
		numberTargetX.setSelectedItem(0.0);
		numberTargetY.setSelectedItem(0.0);
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			costDatasources.setSelectedItem(datasetGrid.getDatasource());
			costDataset.setSelectedItem(datasetGrid);
			updateCoordinate(datasetGrid);
		}
		resultDataset.setDatasetName("result_costPathLine");
		comboBoxSmoothMethod.setItems(new ParameterDataNode(CommonProperties.getString("String_SmoothMethod_NONE"), SmoothMethod.NONE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMethod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMethod_POLISH"), SmoothMethod.POLISH));
		numberSmoothDegree.setSelectedItem(2);
		numberSmoothDegree.setEnabled(false);
	}

	private void registerListener() {
		comboBoxSmoothMethod.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (comboBoxSmoothMethod.getSelectedData().equals(SmoothMethod.BSPLINE)||comboBoxSmoothMethod.getSelectedData().equals(SmoothMethod.POLISH)) {
					numberSmoothDegree.setEnabled(true);
					numberSmoothDegree.setMinValue(2);
					numberSmoothDegree.setMaxValue(10);
					numberSmoothDegree.setMaxBit(0);
				} else {
					numberSmoothDegree.setEnabled(false);
				}
			}
		});
		costDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (costDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetGrid) {
					updateCoordinate((DatasetGrid) evt.getNewValue());
				}
			}
		});
	}

	private void updateCoordinate(DatasetGrid datasetGrid) {
		Rectangle2D bounds = datasetGrid.getBounds();
		numberOriginX.setSelectedItem(bounds.getLeft());
		numberOriginY.setSelectedItem(bounds.getBottom());
		numberTargetX.setSelectedItem(bounds.getRight());
		numberTargetY.setSelectedItem(bounds.getTop());
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.COST_PATH_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_CostPathLine");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			DistanceAnalyst.addSteppedListener(steppedListener);

			DatasetGrid srcCost = null;
			if (parameters.getInputs().getData(COST_DATA).getValue() != null) {
				srcCost = (DatasetGrid) parameters.getInputs().getData(COST_DATA).getValue();
			} else {
				srcCost = (DatasetGrid) costDataset.getSelectedItem();
			}

			DistanceAnalystParameter distanceAnalystParameter = new DistanceAnalystParameter();
			distanceAnalystParameter.setCostGrid(srcCost);
			distanceAnalystParameter.setPathLineSmoothMethod((SmoothMethod) comboBoxSmoothMethod.getSelectedData());
			if (numberSmoothDegree.isEnabled() && numberSmoothDegree.getSelectedItem() != null) {
				distanceAnalystParameter.setPathLineSmoothDegree(Integer.parseInt(numberSmoothDegree.getSelectedItem().toString()));
			}

			double originX = Double.parseDouble(numberOriginX.getSelectedItem().toString());
			double originY = Double.parseDouble(numberOriginY.getSelectedItem().toString());
			double targetX = Double.parseDouble(numberTargetX.getSelectedItem().toString());
			double targetY = Double.parseDouble(numberTargetY.getSelectedItem().toString());
			Point2D pointOrigin = new Point2D(originX, originY);
			Point2D pointTarget = new Point2D(targetX, targetY);
			PathLineResult pathLineResult = DistanceAnalyst.costPathLine(pointOrigin, pointTarget, distanceAnalystParameter);
			if (pathLineResult != null) {
				DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
				datasetVectorInfo.setName(resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
				datasetVectorInfo.setType(DatasetType.LINE);
				DatasetVector result = resultDataset.getResultDatasource().getDatasets().create(datasetVectorInfo);
				result.setPrjCoordSys(srcCost.getPrjCoordSys());
				Recordset recordset = result.getRecordset(false, CursorType.DYNAMIC);
				recordset.getBatch().setMaxRecordCount(2000);
				recordset.getBatch().begin();
				recordset.addNew(pathLineResult.getPathLine());
				recordset.getBatch().update();
				recordset.dispose();
				parameters.getOutputs().getData(OUTPUT_DATA).setValue(result);
				fireRunning(new RunningEvent(this, 100, "finished"));
			}
			isSuccessful = pathLineResult != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
			DistanceAnalyst.removeSteppedListener(steppedListener);
		}

		return isSuccessful;
	}
}
