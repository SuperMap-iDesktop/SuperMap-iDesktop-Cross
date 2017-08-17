package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.GeneralizeAnalyst;
import com.supermap.analyst.spatialanalyst.ResampleMode;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
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
import com.supermap.desktop.utilities.ResampleModeUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/14 0014
 * 栅格重采样
 */
public class MetaProcessGridResample extends MetaProcess {
	private final static String INPUT_DATA = SOURCE_PANEL_DESCRIPTION;
	private final static String OUTPUT_DATA = "GridResampleResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterSaveDataset resultDataset;
	private ParameterTextField textFieldSourceXPixel;
	private ParameterTextField textFieldSourceYPixel;
	private ParameterTextField textFieldSourceRow;
	private ParameterTextField textFieldSourceColumn;
	private ParameterComboBox comboBoxMethod;
	private ParameterNumber numberPixel;
	private ParameterTextField textFieldRow;
	private ParameterTextField textFieldColumn;


	public MetaProcessGridResample() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.GRID, DatasetType.IMAGE);
		resultDataset = new ParameterSaveDataset();
		textFieldSourceXPixel = new ParameterTextField(ControlsProperties.getString("String_LabelXPixelFormat"));
		textFieldSourceYPixel = new ParameterTextField(ControlsProperties.getString("String_LabelYPixelFormat"));
		textFieldSourceRow = new ParameterTextField(CommonProperties.getString("String_Row"));
		textFieldSourceColumn = new ParameterTextField(CommonProperties.getString("String_Column"));
		comboBoxMethod = new ParameterComboBox(ProcessProperties.getString("String_Label_ResampleMethod"));
		numberPixel = new ParameterNumber(CommonProperties.getString("String_Resolution"));
		textFieldRow = new ParameterTextField(CommonProperties.getString("String_Row"));
		textFieldColumn = new ParameterTextField(CommonProperties.getString("String_Column"));

		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceCombine.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine infoCombine = new ParameterCombine();
		infoCombine.setDescribe(ProcessProperties.getString("String_GroupBox_SourceInfo"));
		infoCombine.addParameters(textFieldSourceXPixel,textFieldSourceYPixel,textFieldSourceRow,textFieldSourceColumn);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(comboBoxMethod,numberPixel,textFieldRow,textFieldColumn);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultCombine.addParameters(resultDataset);

		parameters.setParameters(sourceCombine, infoCombine, settingCombine, resultCombine);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.ALL_RASTER, sourceCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.ALL_RASTER, resultCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.GRID,DatasetType.IMAGE);
		if (dataset != null) {
			sourceDatasource.setSelectedItem(dataset.getDatasource());
			sourceDataset.setSelectedItem(dataset);
			this.resultDataset.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_gridResample"));
			Rectangle2D bounds = dataset.getBounds();
			double cellSize = Math.sqrt(Math.pow(bounds.getHeight(), 2) + Math.pow(bounds.getWidth(), 2)) / 500;
			textFieldSourceXPixel.setSelectedItem(cellSize);
			textFieldSourceYPixel.setSelectedItem(cellSize);
			textFieldSourceRow.setSelectedItem((int) (bounds.getHeight() / cellSize));
			textFieldSourceColumn.setSelectedItem((int) (bounds.getWidth() / cellSize));
			numberPixel.setSelectedItem(cellSize*2);
			textFieldRow.setSelectedItem((int) (bounds.getHeight() / (2*cellSize)));
			textFieldColumn.setSelectedItem((int) (bounds.getWidth() / (2*cellSize)));
		}
		comboBoxMethod.setItems(new ParameterDataNode(ResampleModeUtilities.toString(ResampleMode.BILINEAR),ResampleMode.BILINEAR),
				new ParameterDataNode(ResampleModeUtilities.toString(ResampleMode.CUBIC),ResampleMode.CUBIC),
				new ParameterDataNode(ResampleModeUtilities.toString(ResampleMode.NEAREST),ResampleMode.NEAREST));
		textFieldSourceXPixel.setEnabled(false);
		textFieldSourceYPixel.setEnabled(false);
		textFieldSourceRow.setEnabled(false);
		textFieldSourceColumn.setEnabled(false);
		textFieldRow.setEnabled(false);
		textFieldColumn.setEnabled(false);
	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof Dataset) {
					Dataset dataset = (Dataset) evt.getNewValue();
					Rectangle2D bounds = dataset.getBounds();
					double cellSize = Math.sqrt(Math.pow(bounds.getHeight(), 2) + Math.pow(bounds.getWidth(), 2)) / 500;
					textFieldSourceXPixel.setSelectedItem(cellSize);
					textFieldSourceYPixel.setSelectedItem(cellSize);
					textFieldSourceRow.setSelectedItem((int) (bounds.getHeight() / cellSize));
					textFieldSourceColumn.setSelectedItem((int) (bounds.getWidth() / cellSize));
					numberPixel.setSelectedItem(cellSize*2);
					textFieldRow.setSelectedItem((int) (bounds.getHeight() / (2*cellSize)));
					textFieldColumn.setSelectedItem((int) (bounds.getWidth() / (2*cellSize)));
				}
			}
		});
		numberPixel.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				double cellSize = Double.parseDouble(numberPixel.getSelectedItem().toString());
				Rectangle2D bounds = sourceDataset.getSelectedDataset().getBounds();
				textFieldRow.setSelectedItem((int) (bounds.getHeight() / cellSize));
				textFieldColumn.setSelectedItem((int) (bounds.getWidth() / cellSize));
			}
		});
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.GRID_RESAMPLE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_GridResample");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			Dataset src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (Dataset) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = sourceDataset.getSelectedItem();
			}
			double cellSize = Double.parseDouble(numberPixel.getSelectedItem().toString());
			ResampleMode mode = (ResampleMode) comboBoxMethod.getSelectedData();
			GeneralizeAnalyst.addSteppedListener(steppedListener);
			Dataset result = GeneralizeAnalyst.resample(src, cellSize, mode, resultDataset.getResultDatasource(), resultDataset.getDatasetName());
			isSuccessful = result != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			fireRunning(new RunningEvent(this,100,"finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			GeneralizeAnalyst.removeSteppedListener(steppedListener);
		}

		return isSuccessful;
	}
}
