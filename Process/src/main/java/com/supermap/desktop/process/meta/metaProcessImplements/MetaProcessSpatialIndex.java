package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.SpatialIndexTypeUtilities;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessSpatialIndex extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "output";
	private ParameterDatasource datasource;
	private ParameterSingleDataset dataset;
	private ParameterComboBox parameterComboBox;

	public MetaProcessSpatialIndex() {

		parameters = new DefaultParameters();
		ParameterDataNode[] parameterDataNodes = new ParameterDataNode[]{
				// fixme 支持的索引类型和数据源类型相关，目前只把所有的索引类型添加进去，未处理不支持的情况
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.NONE), SpatialIndexType.NONE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.RTREE), SpatialIndexType.RTREE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.MULTI_LEVEL_GRID), SpatialIndexType.MULTI_LEVEL_GRID),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.TILE), SpatialIndexType.TILE),
				new ParameterDataNode(SpatialIndexTypeUtilities.toString(SpatialIndexType.QTREE), SpatialIndexType.QTREE),
		};
		datasource = new ParameterDatasource();
		dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
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
	public void run() {
		DatasetVector src = null;
		if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof DatasetVector) {
			src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
		} else {
			src = (DatasetVector) this.dataset.getSelectedItem();
		}
		SpatialIndexType spatialIndexType = (SpatialIndexType) ((ParameterDataNode) parameterComboBox.getSelectedItem()).getData();
		fireRunning(new RunningEvent(this, 0, "start build spatial index"));
		src.buildSpatialIndex(spatialIndexType);
		fireRunning(new RunningEvent(this, 100, "build spatial index finished"));
		setFinished(true);
		this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(src);
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

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Tree_Node3.png");
	}
}
