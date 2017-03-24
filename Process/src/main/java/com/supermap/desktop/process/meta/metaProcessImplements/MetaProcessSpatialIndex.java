package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.utilities.SpatialIndexTypeUtilities;

/**
 * @author XiaJT
 */
public class MetaProcessSpatialIndex extends MetaProcess {
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

		parameterComboBox = new ParameterComboBox(ControlsProperties.getString("String_LabelSpatialIndexType"));
		parameterComboBox.setItems(parameterDataNodes);
		parameters.setParameters(parameterComboBox);
		processTask = new ProcessTask(this);
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public void run() {
		DatasetVector dataset = (DatasetVector) inputs.getData();
		SpatialIndexType spatialIndexType = (SpatialIndexType) ((ParameterDataNode) parameterComboBox.getSelectedItem()).getData();
		fireRunning(new RunningEvent(this, 0, "start build spatial index"));
		dataset.buildSpatialIndex(spatialIndexType);
		fireRunning(new RunningEvent(this, 100, "build spatial index finished"));
		ProcessData processData = new ProcessData();
		processData.setData(dataset);
		outPuts.add(0, processData);
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
		return "重建空间索引";
	}
}
