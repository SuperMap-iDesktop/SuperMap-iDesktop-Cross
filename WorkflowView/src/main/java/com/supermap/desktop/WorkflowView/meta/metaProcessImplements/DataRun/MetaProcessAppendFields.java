package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.DataRun;

import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;

/**
 * Created by xie on 2017/8/5.
 * 追加列
 */
public class MetaProcessAppendFields extends MetaProcess {
	private final String INPUT_DATA = CommonProperties.getString("String_ColumnHeader_SourceData");
	private final String OUTPUT_DATA = CommonProperties.getString("String_ColumnHeader_TargetData");
	private ParameterCombine sourceDataCombine;
	private ParameterDatasource sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterFieldComboBox sourceLinkedField;
	private ParameterCombine targetDataCombine;
	private ParameterDatasource targetDatasource;
	private ParameterSingleDataset targetDataset;
	private ParameterFieldComboBox targetLinkedField;

	public MetaProcessAppendFields() {
		initParameters();
		initConstrant();
	}

	private void initConstrant() {
		EqualDatasourceConstraint sourceDatasourceConstraint = new EqualDatasourceConstraint();
		sourceDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		sourceDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint targetDatasourceConstraint = new EqualDatasourceConstraint();
		targetDatasourceConstraint.constrained(targetDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		targetDatasourceConstraint.constrained(targetDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint datasetConstraint = new EqualDatasetConstraint();
		datasetConstraint.constrained(sourceDataset,ParameterSingleDataset.DATASET_FIELD_NAME);
		datasetConstraint.constrained(targetDataset,ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	private void initParameters() {

		this.targetDatasource = new ParameterDatasource();
		this.targetDataset = new ParameterSingleDataset();
		this.targetDataset.setDatasetTypes(DatasetTypeUtilities.getDatasetTypeVector());
		this.targetLinkedField = new ParameterFieldComboBox();
		this.targetLinkedField.setDescribe(ProcessProperties.getString("String_ConnectionField"));
		this.targetDataCombine = new ParameterCombine();
		this.targetDataCombine.setDescribe(OUTPUT_DATA);
		this.targetDataCombine.addParameters(targetDatasource, targetDataset, targetLinkedField);

		this.sourceDatasource = new ParameterDatasource();
		this.sourceDataset = new ParameterSingleDataset();
		this.sourceDataset.setDatasetTypes(DatasetTypeUtilities.getDatasetTypeVector());
		this.sourceLinkedField = new ParameterFieldComboBox();
		this.sourceLinkedField.setDescribe(ProcessProperties.getString("String_ConnectionField"));
		this.sourceDataCombine = new ParameterCombine();
		this.sourceDataCombine.setDescribe(INPUT_DATA);
		this.sourceDataCombine.addParameters(sourceDatasource, sourceDataset, sourceLinkedField);

		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceDataCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, targetDataCombine);
		this.parameters.setParameters(targetDataCombine, sourceDataCombine);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public String getKey() {
		return MetaKeys.APPENDFIELDS;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AppendFields");
	}

	@Override
	public IParameters getParameters() {
		return this.parameters;
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}
}
