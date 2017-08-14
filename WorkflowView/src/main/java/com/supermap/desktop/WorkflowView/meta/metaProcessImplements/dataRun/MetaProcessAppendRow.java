package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.Dataset;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCheckBox;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasetChooseTable;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DatasourceUtilities;

/**
 * Created by xie on 2017/8/8.
 */
public class MetaProcessAppendRow extends MetaProcess {
	private final String INPUT_DATA = CommonProperties.getString("String_ColumnHeader_SourceData");
	private final String OUTPUT_DATA = CommonProperties.getString("String_ColumnHeader_TargetData");
	private ParameterDatasource datasource;
	private ParameterSingleDataset dataset;
	private ParameterCombine targetData;
	private ParameterDatasetChooseTable chooseTable;
	private ParameterCheckBox checkBox;

	public MetaProcessAppendRow() {
		initParameters();
	}

	private void initParameters() {
		this.datasource = new ParameterDatasource();
		this.dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
		this.dataset.setDatasource(DatasourceUtilities.getDefaultResultDatasource());
		Dataset dataset = DatasetUtilities.getDefaultDatasetVector();
		if (null != dataset) {
			this.dataset.setSelectedItem(dataset);
		}
		this.targetData = new ParameterCombine();
		this.targetData.setDescribe(OUTPUT_DATA);
		this.targetData.addParameters(this.datasource, this.dataset);
		this.chooseTable = new ParameterDatasetChooseTable();
		this.checkBox = new ParameterCheckBox(CommonProperties.getString("String_SaveNewFields"));

		this.parameters.setParameters(this.targetData, chooseTable, checkBox);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, this.chooseTable);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, this.targetData);

	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AppendRow");
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public String getKey() {
		return MetaKeys.APPEND_ROWS;
	}
}
