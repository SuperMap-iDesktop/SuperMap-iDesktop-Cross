package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessFillSink extends MetaProcessHydrology {
	private static final String SINK_DATA = ProcessProperties.getString("String_GroupBox_SinkData");

	private ParameterDatasourceConstrained sinkDatasource;
	private ParameterSingleDataset sinkDataset;

	@Override
	protected void initField() {
		INPUT_DATA = SOURCE_PANEL_DESCRIPTION;
		OUTPUT_DATA = "FillSinkResult";
	}

	@Override
	protected void initParaComponent() {
		sinkDatasource = new ParameterDatasourceConstrained();
		sinkDataset = new ParameterSingleDataset(DatasetType.REGION, DatasetType.POINT);

		EqualDatasourceConstraint constraintSource1 = new EqualDatasourceConstraint();
		constraintSource1.constrained(sinkDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource1.constrained(sinkDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.REGION);
		if (dataset != null) {
			sinkDatasource.setSelectedItem(dataset.getDatasource());
			sinkDataset.setDatasource(dataset.getDatasource());
			sinkDataset.setShowNullValue(true);
		}

		ParameterCombine sinkCombine = new ParameterCombine();
		sinkCombine.setDescribe(SINK_DATA);
		sinkCombine.addParameters(sinkDatasource, sinkDataset);

		parameters.setParameters(sourceCombine,sinkCombine,resultCombine);
		parameters.addInputParameters(SINK_DATA, new DatasetTypes("",DatasetTypes.POINT.getValue()|DatasetTypes.REGION.getValue()), sinkCombine);
	}

	@Override
	protected String getResultName() {
		return "result_fillSink";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_FillSink");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		DatasetVector srcSink = null;
		if (parameters.getInputs().getData(SINK_DATA).getValue() != null) {
			srcSink = (DatasetVector) parameters.getInputs().getData(SINK_DATA).getValue();
		} else if (sinkDataset.getSelectedItem() != null) {
			srcSink = (DatasetVector) sinkDataset.getSelectedItem();
		}
		DatasetGrid result = null;
		if (srcSink != null) {
			result= HydrologyAnalyst.fillSink(src,resultDataset.getResultDatasource(),
					resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getSelectedItem().toString()),srcSink);
		} else {
			result= HydrologyAnalyst.fillSink(src,resultDataset.getResultDatasource(),
					resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getSelectedItem().toString()));
		}
		return result;
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.FILL_SINK;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_FillSink");
	}
}
