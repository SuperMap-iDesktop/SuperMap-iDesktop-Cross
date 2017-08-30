package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.analyst.terrainanalyst.StreamOrderType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessStreamToLine extends MetaProcessStreamOrder {
	@Override
	protected Type getOutputType() {
		return DatasetTypes.LINE;
	}

	@Override
	protected void initField() {
		INPUT_DATA = ProcessProperties.getString("String_GroupBox_StreamData");
		OUTPUT_DATA = "StreamToLineResult";
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	protected String getResultName() {
		return "result_streamToLine";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_StreamToLine");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		DatasetGrid srcDirection = null;
		if (parameters.getInputs().getData(DIRECTION_DATA).getValue() != null) {
			srcDirection = (DatasetGrid) parameters.getInputs().getData(DIRECTION_DATA).getValue();
		} else if (directionDataset.getSelectedItem() != null) {
			srcDirection = (DatasetGrid) directionDataset.getSelectedItem();
		}
		StreamOrderType type = (StreamOrderType) comboBox.getSelectedData();
		DatasetVector result= HydrologyAnalyst.streamToLine(src, srcDirection, resultDataset.getResultDatasource(),
				resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()), type);

		return result;
	}

	@Override
	public String getKey() {
		return MetaKeys.STREAM_TO_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_StreamToLine");
	}
}
