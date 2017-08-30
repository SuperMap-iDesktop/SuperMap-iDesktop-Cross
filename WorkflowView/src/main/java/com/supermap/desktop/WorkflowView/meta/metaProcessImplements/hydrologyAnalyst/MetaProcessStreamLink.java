package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessStreamLink extends MetaProcessHydrology {
	private static final String DIRECTION_DATA = ProcessProperties.getString("String_GroupBox_FlowDirectionData");

	private ParameterDatasourceConstrained directionDatasource;
	private ParameterSingleDataset directionDataset;

	@Override
	protected void initField() {
		INPUT_DATA = ProcessProperties.getString("String_GroupBox_StreamData");
		OUTPUT_DATA = "StreamOrderResult";
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	protected void initParaComponent() {
		directionDatasource = new ParameterDatasourceConstrained();
		directionDataset = new ParameterSingleDataset(DatasetType.GRID);

		EqualDatasourceConstraint constraintSource1 = new EqualDatasourceConstraint();
		constraintSource1.constrained(directionDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource1.constrained(directionDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		ParameterCombine directionCombine = new ParameterCombine();
		directionCombine.setDescribe(DIRECTION_DATA);
		directionCombine.addParameters(directionDatasource, directionDataset);

		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			directionDatasource.setSelectedItem(datasetGrid.getDatasource());
			directionDataset.setSelectedItem(datasetGrid);
		}

		parameters.setParameters(sourceCombine, directionCombine, resultCombine);
		parameters.addInputParameters(DIRECTION_DATA, DatasetTypes.GRID, directionCombine);
	}

	@Override
	protected String getResultName() {
		return "result_streamLink";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_StreamLink");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		DatasetGrid srcDirection = null;
		if (parameters.getInputs().getData(DIRECTION_DATA).getValue() != null) {
			srcDirection = (DatasetGrid) parameters.getInputs().getData(DIRECTION_DATA).getValue();
		} else if (directionDataset.getSelectedItem() != null) {
			srcDirection = (DatasetGrid) directionDataset.getSelectedItem();
		}
		DatasetGrid result= HydrologyAnalyst.streamLink(src, srcDirection, resultDataset.getResultDatasource(),
				resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));

		return result;
	}

	@Override
	public String getKey() {
		return MetaKeys.STREAM_LINK;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_StreamLink");
	}
}
