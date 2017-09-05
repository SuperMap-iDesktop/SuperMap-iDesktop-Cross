package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessGridAnalyst;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCheckBox;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterTextField;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessFlowDirection extends MetaProcessGridAnalyst {
	private static final String INPUT_DATA = SOURCE_PANEL_DESCRIPTION;
	private static final String OUTPUT_DATA = "FlowDirectionResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterDatasource resultDatasource;
	private ParameterTextField directionGrid;
	private ParameterTextField dropGrid;
	private ParameterCheckBox checkBoxForceOut;
	private ParameterCheckBox checkBoxCreateDrop;

	public MetaProcessFlowDirection() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.GRID);
		checkBoxCreateDrop = new ParameterCheckBox(ProcessProperties.getString("String_CheckBox_CreateDrop"));
		checkBoxForceOut = new ParameterCheckBox(ProcessProperties.getString("String_CheckBox_ForceOut"));
		resultDatasource = new ParameterDatasource();
		directionGrid = new ParameterTextField(ProcessProperties.getString("String_Label_DirectionGrid"));
		dropGrid = new ParameterTextField(ProcessProperties.getString("String_Label_DropGrid"));

		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(INPUT_DATA);
		sourceCombine.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine settingCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(checkBoxForceOut, checkBoxCreateDrop);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(RESULT_PANEL_DESCRIPTION);
		resultCombine.addParameters(resultDatasource, directionGrid, dropGrid);

		parameters.setParameters(sourceCombine, settingCombine, resultCombine);
		parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceCombine);
		parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_DirectionGrid"),DatasetTypes.GRID,directionGrid);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(directionGrid, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(dropGrid, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			sourceDataset.setSelectedItem(datasetGrid);
		}
		directionGrid.setSelectedItem("result_directionGrid");
		dropGrid.setSelectedItem("result_dropGrid");
		checkBoxCreateDrop.setSelectedItem(true);
	}

	private void registerListener() {
		checkBoxCreateDrop.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				dropGrid.setEnabled(Boolean.valueOf(checkBoxCreateDrop.getSelectedItem().toString()));
			}
		});
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public boolean childExecute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			DatasetGrid src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetGrid) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetGrid) sourceDataset.getSelectedItem();
			}
			HydrologyAnalyst.addSteppedListener(steppedListener);
			boolean forceFlowAtEdge = Boolean.parseBoolean(checkBoxForceOut.getSelectedItem().toString());
			boolean createDrop = Boolean.parseBoolean(checkBoxCreateDrop.getSelectedItem().toString());
			DatasetGrid resultDirection = null;
			if (createDrop) {
				resultDirection = HydrologyAnalyst.flowDirection(src,forceFlowAtEdge,resultDatasource.getSelectedItem(),
						resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(directionGrid.getSelectedItem().toString()),
						resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(dropGrid.getSelectedItem().toString()));
			} else {
				resultDirection = HydrologyAnalyst.flowDirection(src,forceFlowAtEdge,resultDatasource.getSelectedItem(),
						resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(directionGrid.getSelectedItem().toString()));
			}
			isSuccessful = resultDirection != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDirection);
			fireRunning(new RunningEvent(this,100,"finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
			HydrologyAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.FLOW_DIRECTION;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_FlowDirection");
	}
}
