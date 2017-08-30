package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.PixelFormat;
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
import com.supermap.desktop.utilities.PixelFormatUtilities;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessStreamGrid extends MetaProcess {
	private static final String INPUT_DATA = ProcessProperties.getString("String_GroupBox_FlowData");
	private static final String OUTPUT_DATA = "StreamGridResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterSaveDataset resultDataset;
	private ParameterNumber numberThreshold;
	private ParameterComboBox comboBoxPixel;
	private ParameterCheckBox checkBoxIgnore;
	private ParameterCheckBox checkBoxZip;

	public MetaProcessStreamGrid() {
		initParameters();
		initParameterConstraint();
		initParametersState();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.GRID);
		resultDataset = new ParameterSaveDataset();
		numberThreshold = new ParameterNumber(ProcessProperties.getString("String_Label_ThresholdGreaterThan"));
		comboBoxPixel = new ParameterComboBox(CommonProperties.getString("String_PixelType"));
		checkBoxIgnore = new ParameterCheckBox(ProcessProperties.getString("String_IgnoreNoValue"));
		checkBoxZip = new ParameterCheckBox(ProcessProperties.getString("String_IsZip"));

		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(INPUT_DATA);
		sourceCombine.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		ParameterCombine emptyCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		emptyCombine.addParameters(checkBoxIgnore, checkBoxZip);
		settingCombine.addParameters(numberThreshold, comboBoxPixel, emptyCombine);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(RESULT_PANEL_DESCRIPTION);
		resultCombine.addParameters(resultDataset);

		parameters.setParameters(sourceCombine, settingCombine, resultCombine);
		parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceCombine);
		parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_StreamGrid"),DatasetTypes.GRID,resultCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			sourceDataset.setSelectedItem(datasetGrid);
		}
		resultDataset.setSelectedItem("result_streamGrid");
		numberThreshold.setSelectedItem(1000);
		comboBoxPixel.setItems(new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.SINGLE), PixelFormat.SINGLE),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.DOUBLE), PixelFormat.DOUBLE),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT8), PixelFormat.BIT8),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT16), PixelFormat.BIT16),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT32), PixelFormat.BIT32),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT1), PixelFormat.UBIT1),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT4), PixelFormat.UBIT4),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT8), PixelFormat.UBIT8),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT16), PixelFormat.UBIT16),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT32), PixelFormat.UBIT32)
		);
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.STREAM_GRID;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_StreamGrid");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			DatasetGrid src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetGrid) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetGrid) sourceDataset.getSelectedItem();
			}
			MathAnalyst.addSteppedListener(steppedListener);
			double threshold = Double.parseDouble(numberThreshold.getSelectedItem().toString());
			String expression = "[" + src.getDatasource().getAlias() + "." + src.getName() + "]>" + threshold;
			PixelFormat pixelFormat = (PixelFormat) comboBoxPixel.getSelectedData();
			boolean isZip = Boolean.parseBoolean(checkBoxZip.getSelectedItem().toString());
			boolean isIgnore = Boolean.parseBoolean(checkBoxIgnore.getSelectedItem().toString());
			DatasetGrid result = MathAnalyst.execute(expression, null, pixelFormat, isZip, isIgnore, resultDataset.getResultDatasource(),
					resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
			isSuccessful = result != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			fireRunning(new RunningEvent(this,100,"finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
			MathAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
