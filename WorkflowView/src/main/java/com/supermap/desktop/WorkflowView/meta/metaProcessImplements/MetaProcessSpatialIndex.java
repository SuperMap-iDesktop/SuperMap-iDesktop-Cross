package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.events.RelationAddedEvent;
import com.supermap.desktop.process.events.RelationAddedListener;
import com.supermap.desktop.process.events.RelationRemovedEvent;
import com.supermap.desktop.process.events.RelationRemovedListener;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.SpatialIndexTypeUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class MetaProcessSpatialIndex extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "SpatialIndexResult";
	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterComboBox parameterComboBox;

	public MetaProcessSpatialIndex() {

		ParameterDataNode[] parameterDataNodes = new ParameterDataNode[]{
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.NONE), SpatialIndexType.NONE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.RTREE), SpatialIndexType.RTREE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.MULTI_LEVEL_GRID), SpatialIndexType.MULTI_LEVEL_GRID),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.TILE), SpatialIndexType.TILE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.QTREE), SpatialIndexType.QTREE),
		};
		datasource = new ParameterDatasourceConstrained() {
			@Override
			protected boolean isDatasourceValueLegal(Datasource parameterValue) {
				return !parameterValue.isReadOnly();
			}
		};
		dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());


		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
		}

		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameterComboBox = new ParameterComboBox(ControlsProperties.getString("String_LabelSpatialIndexType"));

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.setDescribe(SOURCE_PANEL_DESCRIPTION);
		parameterCombine.addParameters(datasource, dataset);

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(SETTING_PANEL_DESCRIPTION);
		parameterCombineSetting.addParameters(parameterComboBox);

		parameterComboBox.setItems(parameterDataNodes);
		parameters.setParameters(parameterCombine, parameterCombineSetting);

		initSpatialIndexTypes((DatasetVector) dataset.getSelectedItem());
		initListeners();

		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, datasource, dataset);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, dataset);
	}

	private void initListeners() {
		dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterSingleDataset.DATASET_FIELD_NAME)) {
					DatasetVector datasetVector = (DatasetVector) dataset.getSelectedItem();
					initSpatialIndexTypes(datasetVector);
				}
			}
		});
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);


	}

	@Override
	protected void workflowChanged(Workflow oldWorkflow, Workflow workflow) {
		getWorkflow().addRelationAddedListener(new RelationAddedListener<IProcess>() {
			@Override
			public void relationAdded(RelationAddedEvent<IProcess> e) {
				if (e.getRelation().getTo() == MetaProcessSpatialIndex.this) {
					initSpatialIndexTypes(null);
				}
			}
		});
		getWorkflow().addRelationRemovedListener(new RelationRemovedListener<IProcess>() {
			@Override
			public void relationRemoved(RelationRemovedEvent<IProcess> e) {
				initSpatialIndexTypes((DatasetVector) dataset.getSelectedItem());
			}
		});
	}

	private void initSpatialIndexTypes(DatasetVector datasetVector) {
		if (datasetVector == null) {
			SpatialIndexType[] allSpatialIndexType = SpatialIndexTypeUtilities.ALL_SPATIAL_INDEX_TYPE;
			ArrayList<ParameterDataNode> items = new ArrayList<>();
			for (SpatialIndexType spatialIndexType : allSpatialIndexType) {
				items.add(new ParameterDataNode(SpatialIndexTypeUtilities.toString(spatialIndexType), spatialIndexType));
			}
			parameterComboBox.setItems(items.toArray(new ParameterDataNode[items.size()]));
		} else {
			SpatialIndexType[] allSpatialIndexType = SpatialIndexTypeUtilities.getDatasetSupportTypes(datasetVector);
			ArrayList<ParameterDataNode> items = new ArrayList<>();
			for (SpatialIndexType spatialIndexType : allSpatialIndexType) {
				items.add(new ParameterDataNode(SpatialIndexTypeUtilities.toString(spatialIndexType), spatialIndexType));
			}
			parameterComboBox.setItems(items.toArray(new ParameterDataNode[items.size()]));
			parameterComboBox.setSelectedItem(datasetVector.getSpatialIndexType());
		}
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {
			DatasetVector src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof DatasetVector) {
				src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) this.dataset.getSelectedItem();
			}
			SpatialIndexType spatialIndexType = (SpatialIndexType) ((ParameterDataNode) parameterComboBox.getSelectedItem()).getData();
			fireRunning(new RunningEvent(this, 0, "start build spatial index"));
			isSuccessful = src.buildSpatialIndex(spatialIndexType);
			fireRunning(new RunningEvent(this, 100, "build spatial index finished"));
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(src);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {

		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.SPATIAL_INDEX;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_RebuildSpatialIndex");
	}
}
