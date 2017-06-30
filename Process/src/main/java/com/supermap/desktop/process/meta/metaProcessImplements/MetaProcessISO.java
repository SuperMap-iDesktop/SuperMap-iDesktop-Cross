package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterNumber;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public abstract class MetaProcessISO extends MetaProcess {

	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;
	private ParameterTextField maxGrid;
	private ParameterTextField minGrid;
	private ParameterTextField maxISOLine;
	private ParameterTextField minISOLine;
	private ParameterTextField isoLine;
	private ParameterNumber datumValue;
	private ParameterNumber interval;
	private ParameterNumber resampleTolerance;
	private ParameterComboBox smoothMethod;
	private ParameterNumber smoothNess;


	public MetaProcessISO() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		initParametersListener();
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDatasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (defaultDatasetGrid != null) {
			sourceDatasource.setSelectedItem(defaultDatasetGrid.getDatasource());
			dataset.setSelectedItem(defaultDatasetGrid);
			saveDataset.setResultDatasource(defaultDatasetGrid.getDatasource());
			saveDataset.setSelectedItem(defaultDatasetGrid.getDatasource().getDatasets().getAvailableDatasetName("ISOResult"));
		} else {
			saveDataset.setSelectedItem(getDefaultResultName());
		}

		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		if (null != dataset.getSelectedItem() && dataset.getSelectedItem() instanceof DatasetGrid) {
			maxGrid.setSelectedItem(((DatasetGrid) dataset.getSelectedItem()).getMaxValue());
			minGrid.setSelectedItem(((DatasetGrid) dataset.getSelectedItem()).getMinValue());
		}
		ParameterDataNode selectedNode = new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE);
		this.smoothMethod.setItems(selectedNode,
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		this.smoothMethod.setSelectedItem(selectedNode);
		this.smoothNess.setEnabled(false);
		reloadValue();
	}

	protected abstract String getDefaultResultName();

	private void initParameters() {

		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.GRID);
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			dataset.setSelectedItem(datasetGrid);
		}
		this.saveDataset = new ParameterSaveDataset();
		this.maxGrid = new ParameterTextField(CommonProperties.getString("String_MAXGrid"));
		this.minGrid = new ParameterTextField(CommonProperties.getString("String_MINGrid"));
		this.maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
		this.minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
		this.isoLine = new ParameterTextField(CommonProperties.getString("String_ISOData"));
		this.datumValue = new ParameterNumber(CommonProperties.getString("String_DatumValue"));
		datumValue.setSelectedItem("0");
		this.interval = new ParameterNumber(CommonProperties.getString("String_Interval"));
		interval.setMinValue(0);
		interval.setIsIncludeMin(false);
		interval.setSelectedItem("100");

		this.resampleTolerance = new ParameterNumber(CommonProperties.getString("String_ResampleTolerance"));
		resampleTolerance.setMinValue(0);
		resampleTolerance.setSelectedItem(0);
		resampleTolerance.setIsIncludeMin(true);
		this.smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
		this.smoothNess = new ParameterNumber(CommonProperties.getString("String_SmoothNess"));
		smoothNess.setMinValue(2);
		smoothNess.setMaxValue(5);
		smoothNess.setMaxBit(0);
		this.smoothNess.setSelectedItem("2");

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_TargetData"));
		targetData.addParameters(saveDataset);
		ParameterCombine resultInfo = new ParameterCombine();
		resultInfo.setDescribe(CommonProperties.getString("String_ResultInfo"));
		resultInfo.addParameters(maxGrid, minGrid, maxISOLine, minISOLine, isoLine);
		resultInfo.setEnabled(false);

		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(datumValue, interval, resampleTolerance, smoothMethod, smoothNess);
		this.parameters.setParameters(sourceData, paramSet, resultInfo, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE, targetData);
	}

	private void initParametersListener() {
		smoothMethod.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					smoothNess.setEnabled(smoothMethod.getSelectedIndex() != 0);
				}
			}
		});

		dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterSingleDataset.DATASET_FIELD_NAME)) {
					reloadValue();
				}
			}
		});
		datumValue.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					reloadValue();
				}
			}
		});
		interval.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					reloadValue();
				}
			}
		});
	}

	private void reloadValue() {
		Dataset datasetGrid = dataset.getSelectedDataset();
		if (dataset != null && datasetGrid instanceof DatasetGrid) {
			try {
				((DatasetGrid) datasetGrid).getMaxValue();
			} catch (Exception e) {
				// 辣鸡数据
				return;
			}
			int maxValue = (int) ((DatasetGrid) datasetGrid).getMaxValue();
			int minValue = (int) ((DatasetGrid) datasetGrid).getMinValue();
			maxGrid.setSelectedItem(DoubleUtilities.getFormatString(maxValue));
			minGrid.setSelectedItem(DoubleUtilities.getFormatString(minValue));
			double baseValue = Double.valueOf((String) datumValue.getSelectedItem());
			double lineDistance = Double.valueOf((String) interval.getSelectedItem());
			double dRemain = baseValue % lineDistance;
			double maxIsoValue = (int) ((maxValue - dRemain) / lineDistance) * lineDistance + dRemain;
			double minIsoValue = (int) ((minValue - dRemain) / lineDistance + 1) * lineDistance + dRemain;
			int isoCount = (int) ((maxValue - minValue) / lineDistance) + 1;
			maxISOLine.setSelectedItem(DoubleUtilities.getFormatString(maxIsoValue));
			minISOLine.setSelectedItem(DoubleUtilities.getFormatString(minIsoValue));
			isoLine.setSelectedItem(String.valueOf(isoCount));
		}
	}


	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISOLine");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {
			SurfaceExtractParameter surfaceExtractParameter = new SurfaceExtractParameter();
			surfaceExtractParameter.setDatumValue(Double.valueOf(datumValue.getSelectedItem().toString()));
			surfaceExtractParameter.setInterval(Double.valueOf(interval.getSelectedItem().toString()));
			surfaceExtractParameter.setResampleTolerance(Double.valueOf(resampleTolerance.getSelectedItem().toString()));
			surfaceExtractParameter.setSmoothMethod((SmoothMethod) ((ParameterDataNode) smoothMethod.getSelectedItem()).getData());
			surfaceExtractParameter.setSmoothness(Integer.valueOf(smoothNess.getSelectedItem().toString()));
			SurfaceAnalyst.addSteppedListener(steppedListener);

			DatasetGrid src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetGrid) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetGrid) dataset.getSelectedItem();
			}
			DatasetVector result = subExecute(surfaceExtractParameter, src, saveDataset.getResultDatasource(), saveDataset.getDatasetName());
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
			fireRunning(new RunningEvent(this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			SurfaceAnalyst.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	protected abstract DatasetVector subExecute(SurfaceExtractParameter surfaceExtractParameter, DatasetGrid src, Datasource resultDatasource, String datasetName);


	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}


}