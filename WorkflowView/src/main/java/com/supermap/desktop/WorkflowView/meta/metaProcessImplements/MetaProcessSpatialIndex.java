package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.SpatialIndexTypeUtilities;

/**
 * @author XiaJT
 */
public class MetaProcessSpatialIndex extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "output";
	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterComboBox parameterComboBox;

	public MetaProcessSpatialIndex() {

		ParameterDataNode[] parameterDataNodes = new ParameterDataNode[]{
				// fixme 支持的索引类型和数据源类型相关，目前只把所有的索引类型添加进去，未处理不支持的情况
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.NONE), SpatialIndexType.NONE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.RTREE), SpatialIndexType.RTREE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.MULTI_LEVEL_GRID), SpatialIndexType.MULTI_LEVEL_GRID),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.TILE), SpatialIndexType.TILE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.QTREE), SpatialIndexType.QTREE),
		};
		datasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
		}
		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameterComboBox = new ParameterComboBox(ControlsProperties.getString("String_LabelSpatialIndexType"));
		parameterComboBox.setItems(parameterDataNodes);
		parameters.setParameters(datasource, dataset, parameterComboBox);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, datasource, dataset);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, dataset);
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
