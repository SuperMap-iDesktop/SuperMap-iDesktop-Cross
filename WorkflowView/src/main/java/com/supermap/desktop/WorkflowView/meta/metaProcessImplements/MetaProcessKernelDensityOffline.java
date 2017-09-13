package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.DensityAnalyst;
import com.supermap.analyst.spatialanalyst.DensityAnalystParameter;
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
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/22 0022
 * 核密度分析（本地）
 */
public class MetaProcessKernelDensityOffline extends MetaProcess {
	private final static String INPUT_DATA = SOURCE_PANEL_DESCRIPTION;
	private final static String OUTPUT_DATA = "KernelDensityOfflineResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterSaveDataset resultDataset;
	private ParameterFieldComboBox comboBoxField;
	private ParameterNumber numberTop;
	private ParameterNumber numberBottom;
	private ParameterNumber numberRight;
	private ParameterNumber numberLeft;
	private ParameterNumber numberCellSize;
	private ParameterNumber numberRadius;

	public MetaProcessKernelDensityOffline() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE);
		resultDataset = new ParameterSaveDataset();
		comboBoxField = new ParameterFieldComboBox(ProcessProperties.getString("String_DensityAnalyst_DensityField"));
		numberCellSize = new ParameterNumber(ProcessProperties.getString("String_Resolution"));
		numberTop = new ParameterNumber(ControlsProperties.getString("String_LabelTop"));
		numberBottom = new ParameterNumber(ControlsProperties.getString("String_LabelBottom"));
		numberRight = new ParameterNumber(ControlsProperties.getString("String_LabelRight"));
		numberLeft = new ParameterNumber(ControlsProperties.getString("String_LabelLeft"));
		numberRadius = new ParameterNumber(CommonProperties.getString("String_SearchRadius"));

		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceCombine.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(comboBoxField, numberRadius, numberLeft, numberBottom, numberRight, numberTop, numberCellSize);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultCombine.addParameters(resultDataset);

		parameters.setParameters(sourceCombine, settingCombine, resultCombine);
		this.parameters.addInputParameters(INPUT_DATA, new DatasetTypes("",DatasetTypes.POINT.getValue()|DatasetTypes.LINE.getValue()), sourceCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_KernelsDensityAnalysisResult"),
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
		numberRadius.setSelectedItem(0);
		numberRadius.setMinValue(0);
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT,DatasetType.LINE);
		if (dataset != null) {
			sourceDatasource.setSelectedItem(dataset.getDatasource());
			sourceDataset.setSelectedItem(dataset);
			comboBoxField.setFieldName((DatasetVector) dataset);
			updateBound(dataset);
		}
		comboBoxField.setFieldType(fieldType);
		comboBoxField.setShowSystemField(true);
		comboBoxField.setSelectedItem("SmID");
		resultDataset.setSelectedItem("result_kernelDensity");
		numberRight.setMinValue(Double.parseDouble(numberLeft.getSelectedItem()));
		numberLeft.setMaxValue(Double.parseDouble(numberRight.getSelectedItem()));
		numberTop.setMinValue(Double.parseDouble(numberBottom.getSelectedItem()));
		numberBottom.setMaxValue(Double.parseDouble(numberTop.getSelectedItem()));
	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof Dataset) {
					updateBound((Dataset) evt.getNewValue());
					comboBoxField.setSelectedItem("SmID");
				}
			}
		});
		numberBottom.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberTop.setMinValue(Double.parseDouble(numberBottom.getSelectedItem()));
			}
		});
		numberTop.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberBottom.setMaxValue(Double.parseDouble(numberTop.getSelectedItem()));
			}
		});
		numberRight.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberLeft.setMaxValue(Double.parseDouble(numberRight.getSelectedItem()));
			}
		});
		numberLeft.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				numberRight.setMinValue(Double.parseDouble(numberLeft.getSelectedItem()));
			}
		});
	}

	private void updateBound(Dataset dataset) {
		Rectangle2D rectangle2D = dataset.getBounds();
		numberBottom.setSelectedItem(DoubleUtilities.getFormatString(rectangle2D.getBottom()));
		numberLeft.setSelectedItem(DoubleUtilities.getFormatString(rectangle2D.getLeft()));
		numberRight.setSelectedItem(DoubleUtilities.getFormatString(rectangle2D.getRight()));
		numberTop.setSelectedItem(DoubleUtilities.getFormatString(rectangle2D.getTop()));
		numberTop.setMinValue(rectangle2D.getBottom());
		numberBottom.setMaxValue(rectangle2D.getTop());
		numberLeft.setMaxValue(rectangle2D.getRight());
		numberRight.setMinValue(rectangle2D.getLeft());
		Double x = rectangle2D.getWidth() / 500;
		Double y = rectangle2D.getHeight() / 500;
		Double cellSize = x > y ? y : x;
		numberCellSize.setSelectedItem(DoubleUtilities.getFormatString(cellSize));
		double maxRadius = rectangle2D.getWidth() > rectangle2D.getHeight() ? rectangle2D.getWidth() : rectangle2D.getHeight();
		numberRadius.setSelectedItem(DoubleUtilities.getFormatString(maxRadius/30));
		numberRadius.setMaxValue(maxRadius);
		numberCellSize.setMaxValue(maxRadius);
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
			DatasetVector src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) sourceDataset.getSelectedItem();
			}
			DensityAnalyst.addSteppedListener(steppedListener);
			double top = Double.parseDouble(numberTop.getSelectedItem());
			double bottom = Double.parseDouble(numberBottom.getSelectedItem());
			double right = Double.parseDouble(numberRight.getSelectedItem());
			double left = Double.parseDouble(numberLeft.getSelectedItem());
			densityAnalystParameter.setBounds(new Rectangle2D(left, bottom, right, top));
			densityAnalystParameter.setResolution(Double.parseDouble(numberCellSize.getSelectedItem()));
			densityAnalystParameter.setSearchRadius(Double.parseDouble(numberRadius.getSelectedItem()));
			DatasetGrid result = DensityAnalyst.kernelDensity(densityAnalystParameter, src, comboBoxField.getFieldName(),
					resultDataset.getResultDatasource(), resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
			isSuccessful = result != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			densityAnalystParameter.dispose();
			DensityAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.KERNEL_DENSITY_OFFLINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_KernelDensityAnalyst");
	}
}
