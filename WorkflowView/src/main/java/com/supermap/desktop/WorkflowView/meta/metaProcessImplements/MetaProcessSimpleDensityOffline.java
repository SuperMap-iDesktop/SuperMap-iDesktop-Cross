package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.DensityAnalyst;
import com.supermap.analyst.spatialanalyst.DensityAnalystParameter;
import com.supermap.analyst.spatialanalyst.NeighbourShape;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/22 0022
 * 点密度（本地）
 */
public class MetaProcessSimpleDensityOffline extends MetaProcess {
	private final static String INPUT_DATA = SOURCE_PANEL_DESCRIPTION;
	private final static String OUTPUT_DATA = "SimpleDensityOfflineResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterSaveDataset resultDataset;
	private ParameterFieldComboBox comboBoxField;
	private ParameterShapeType shapeType;
	private ParameterNumber numberTop;
	private ParameterNumber numberBottom;
	private ParameterNumber numberRight;
	private ParameterNumber numberLeft;
	private ParameterNumber numberCellSize;

	public MetaProcessSimpleDensityOffline() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.POINT);
		resultDataset = new ParameterSaveDataset();
		comboBoxField = new ParameterFieldComboBox(ProcessProperties.getString("String_DensityAnalyst_DensityField"));
		shapeType = new ParameterShapeType();
		numberCellSize = new ParameterNumber(ProcessProperties.getString("String_Resolution"));
		numberTop = new ParameterNumber(ControlsProperties.getString("String_LabelTop"));
		numberBottom = new ParameterNumber(ControlsProperties.getString("String_LabelBottom"));
		numberRight = new ParameterNumber(ControlsProperties.getString("String_LabelRight"));
		numberLeft = new ParameterNumber(ControlsProperties.getString("String_LabelLeft"));

		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceCombine.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(comboBoxField, numberTop, numberLeft, numberBottom, numberRight, numberCellSize, shapeType);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultCombine.addParameters(resultDataset);

		parameters.setParameters(sourceCombine, settingCombine, resultCombine);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, sourceCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_SimpleDensityAnalysisResult"),
				DatasetTypes.GRID, resultCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxField, ParameterFieldComboBox.DATASET_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		numberLeft.setSelectedItem(0);
		numberRight.setSelectedItem(0);
		numberTop.setSelectedItem(0);
		numberBottom.setSelectedItem(0);
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
		if (dataset != null) {
			sourceDatasource.setSelectedItem(dataset.getDatasource());
			sourceDataset.setSelectedItem(dataset);
			comboBoxField.setFieldName((DatasetVector) dataset);
			updateBound(dataset);
		}
		comboBoxField.setFieldType(fieldType);
		resultDataset.setSelectedItem("result_simpleDensity");
		numberRight.setMinValue(Double.parseDouble(numberLeft.getSelectedItem().toString()));
		numberLeft.setMaxValue(Double.parseDouble(numberRight.getSelectedItem().toString()));
		numberTop.setMinValue(Double.parseDouble(numberBottom.getSelectedItem().toString()));
		numberBottom.setMaxValue(Double.parseDouble(numberTop.getSelectedItem().toString()));
	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof Dataset) {
					updateBound((Dataset) evt.getNewValue());
				}
			}
		});
		numberBottom.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberTop.setMinValue(Double.parseDouble(numberBottom.getSelectedItem().toString()));
			}
		});
		numberTop.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberBottom.setMaxValue(Double.parseDouble(numberTop.getSelectedItem().toString()));
			}
		});
		numberRight.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberLeft.setMaxValue(Double.parseDouble(numberRight.getSelectedItem().toString()));
			}
		});
		numberLeft.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberRight.setMinValue(Double.parseDouble(numberLeft.getSelectedItem().toString()));
			}
		});
	}

	private void updateBound(Dataset dataset) {
		Rectangle2D rectangle2D = dataset.getBounds();
		numberBottom.setSelectedItem(rectangle2D.getBottom());
		numberLeft.setSelectedItem(rectangle2D.getLeft());
		numberRight.setSelectedItem(rectangle2D.getRight());
		numberTop.setSelectedItem(rectangle2D.getTop());
		Double x = rectangle2D.getWidth() / 500;
		Double y = rectangle2D.getHeight() / 500;
		Double cellSize = x > y ? y : x;
		numberCellSize.setSelectedItem(cellSize);
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DensityAnalystParameter densityAnalystParameter = new DensityAnalystParameter();
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			DatasetVector src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) sourceDataset.getSelectedItem();
			}
			DensityAnalyst.addSteppedListener(steppedListener);
			double top = Double.parseDouble(numberTop.getSelectedItem().toString());
			double bottom = Double.parseDouble(numberBottom.getSelectedItem().toString());
			double right = Double.parseDouble(numberRight.getSelectedItem().toString());
			double left = Double.parseDouble(numberLeft.getSelectedItem().toString());
			densityAnalystParameter.setBounds(new Rectangle2D(left, bottom, right, top));
			densityAnalystParameter.setResolution(Double.parseDouble(numberCellSize.getSelectedItem().toString()));
			densityAnalystParameter.setSearchNeighbourhood((NeighbourShape) shapeType.getSelectedItem());
			DatasetGrid result = DensityAnalyst.pointDensity(densityAnalystParameter, src, comboBoxField.getFieldName(),
					resultDataset.getResultDatasource(), resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
			isSuccessful = result != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			fireRunning(new RunningEvent(this,100,"finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			DensityAnalyst.removeSteppedListener(steppedListener);
			densityAnalystParameter.dispose();
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.SIMPLE_DENSITY_OFFLINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SimpleDensityAnalyst");
	}
}
