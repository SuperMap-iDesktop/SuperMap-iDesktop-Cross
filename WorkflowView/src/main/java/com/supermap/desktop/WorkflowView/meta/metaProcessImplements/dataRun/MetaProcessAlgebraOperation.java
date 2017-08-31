package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/8/29.
 */
public class MetaProcessAlgebraOperation extends MetaProcess {
	private final static String OUTPUT_DATA = "AlgebraOperationResult";


	private ParameterComboBox comboBoxPixelFormat;
	private ParameterCheckBox checkBoxCompress;
	private ParameterCheckBox checkBoxIgnoreNoValueCell;
	private ParameterTextArea textAreaExpression;
	private ParameterRasterAlgebraOperationExpression buttonExpression;
	private ParameterSaveDataset resultDataset;

	public MetaProcessAlgebraOperation() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		this.comboBoxPixelFormat = new ParameterComboBox(CommonProperties.getString("String_PixelFormat"));
		this.checkBoxCompress = new ParameterCheckBox(ControlsProperties.getString("String_DatasetCompress"));
		this.checkBoxIgnoreNoValueCell = new ParameterCheckBox(ControlsProperties.getString("String_IgnoreNoValueRasterCell"));
		this.textAreaExpression = new ParameterTextArea(ProcessProperties.getString("String_AlgebraOperationExpression"));
		this.buttonExpression = new ParameterRasterAlgebraOperationExpression(ProcessProperties.getString("String_SetAlgebraOperationExpression"));
		this.buttonExpression.setAnchor(GridBagConstraints.WEST);

		ParameterCombine setting = new ParameterCombine();
		setting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		setting.addParameters(this.comboBoxPixelFormat, this.checkBoxCompress, this.checkBoxIgnoreNoValueCell, this.textAreaExpression, this.buttonExpression);

		this.resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(resultDataset);

		this.parameters.setParameters(setting, resultData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_AlgebraOperationResult"), DatasetTypes.GRID, resultData);
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		this.comboBoxPixelFormat.setEnabled(false);
		this.checkBoxCompress.setEnabled(false);
		this.checkBoxIgnoreNoValueCell.setEnabled(false);
		this.textAreaExpression.setEnabled(false);
		this.textAreaExpression.setLineWrap(true);
		this.textAreaExpression.setWrapStyleWord(true);
		this.resultDataset.setSelectedItem("result_AlgebraOperation");
	}

	private void registerListener() {
		this.buttonExpression.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() == null) {
					comboBoxPixelFormat.removeAllItems();
					ParameterDataNode parameterDataNode = new ParameterDataNode(PixelFormatUtilities.toString(buttonExpression.getPixelFormat()), buttonExpression.getPixelFormat());
					comboBoxPixelFormat.addItem(parameterDataNode);
					comboBoxPixelFormat.setSelectedItem(parameterDataNode);
					checkBoxCompress.setSelectedItem(buttonExpression.isZip());
					checkBoxIgnoreNoValueCell.setSelectedItem(buttonExpression.isIgnoreNoValue());
					textAreaExpression.setSelectedItem(buttonExpression.getExpression());
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessAlgebraOperation.this, 0, "start"));
			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			MathAnalyst.addSteppedListener(steppedListener);
			DatasetGrid result = MathAnalyst.execute(this.buttonExpression.getExpression(), null, this.buttonExpression.getPixelFormat(),
					this.buttonExpression.isZip(), this.buttonExpression.isIgnoreNoValue(), resultDataset.getResultDatasource(), datasetName);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
			fireRunning(new RunningEvent(MetaProcessAlgebraOperation.this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			MathAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_RasterAlgebraOperation");
	}

	@Override
	public String getKey() {
		return MetaKeys.ALGEBRA_OPERATION;
	}
}
