package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/2/21.
 * sql查询简单实现
 */
public class MetaProcessSqlQuery extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "QueryResult";
	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterTextArea parameterAttributeFilter;
	private ParameterTextArea parameterResultFields;
	private ParameterSaveDataset parameterSaveDataset;
	private DatasetType[] datasetTypes = new DatasetType[]{
			DatasetType.POINT, DatasetType.LINE, DatasetType.REGION,
			DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D,
			DatasetType.TEXT, DatasetType.TABULAR, DatasetType.CAD
	};

	public MetaProcessSqlQuery() {
		initMetaInfo();
	}

	private void initMetaInfo() {

		datasource = new ParameterDatasourceConstrained();
		this.datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameters = new DefaultParameters();
		this.dataset = new ParameterSingleDataset(datasetTypes);
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
		}
		parameterResultFields = new ParameterTextArea(CommonProperties.getString("String_QueryField"));
		parameterAttributeFilter = new ParameterTextArea(CommonProperties.getString("String_QueryCondition"));
		parameterSaveDataset = new ParameterSaveDataset();
		parameterSaveDataset.setDatasetName("QueryResult");
		initParameterConstraint();

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, this.dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

//		ParameterCombine parameterCombineSetting = new ParameterCombine();
//		parameterCombineSetting.addParameters(this.parameterResultFields, this.parameterAttributeFilter);
//		parameterCombineSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));

		ParameterCombine parameterCombineResultData = new ParameterCombine();
		parameterCombineResultData.addParameters(parameterSaveDataset);
		parameterCombineResultData.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(parameterCombineSourceData, this.parameterResultFields, this.parameterAttributeFilter, parameterCombineResultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, parameterCombineResultData);
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SqlQuery");
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public void run() {
		fireRunning(new RunningEvent(this, 0, "start"));
		DatasetVector currentDatasetVector = null;
		if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof DatasetVector) {
			currentDatasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
		} else {
			currentDatasetVector = (DatasetVector) dataset.getSelectedItem();
		}

		if (null != currentDatasetVector) {
			// 构建查询语句
			QueryParameter queryParameter = new QueryParameter();
			queryParameter.setCursorType(CursorType.DYNAMIC);
			queryParameter.setHasGeometry(true);

			// 查询字段
			String queryFields = (String) parameterResultFields.getSelectedItem();
			String[] queryFieldNames = getQueryFieldNames(queryFields);
			queryParameter.setResultFields(queryFieldNames);
			// 查询条件
			queryParameter.setAttributeFilter((String) parameterAttributeFilter.getSelectedItem());
			preProcessSQLQuery(queryParameter);
			queryParameter.setSpatialQueryObject(currentDatasetVector);
			Recordset resultRecord = currentDatasetVector.query(queryParameter);
			if (resultRecord != null && resultRecord.getRecordCount() > 0) {
				if (StringUtilities.isNullOrEmpty(queryFields)) {
					resultRecord.dispose();
					resultRecord = null;
				}

				fireRunning(new RunningEvent(this, 100, "finished"));
				setFinished(true);
				// 保存查询结果
				DatasetVector datasetVector = saveQueryResult(resultRecord);
				this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(datasetVector);
			}
		}

	}

	@Override
	public String getKey() {
		return MetaKeys.SQL_QUERY;
	}

	private DatasetVector saveQueryResult(Recordset resultRecord) {
		DatasetVector resultDataset = null;
		Datasource resultDatasource = parameterSaveDataset.getResultDatasource();
		String datasetName = parameterSaveDataset.getDatasetName();
		if (resultDatasource != null && !StringUtilities.isNullOrEmpty(datasetName)) {
			try {
				resultDataset = resultDatasource.recordsetToDataset(resultRecord, datasetName);
			} catch (Exception e) {
				resultDataset = null;
			}
			resultRecord.moveFirst();
			if (resultDataset == null) {
				Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_SQLQuerySaveAsResultFaield"));
			} else {
				Application.getActiveApplication().getOutput()
						.output(MessageFormat.format(CommonProperties.getString("String_SQLQuerySavaAsResultSucces"), resultDataset.getName()));
			}
		}

		return resultDataset;
	}

	private void preProcessSQLQuery(QueryParameter queryParameter) {
		try {
			for (String field : queryParameter.getResultFields()) {
				String strText = field.toUpperCase();
				if (strText.contains("SUM(") || strText.contains("MAX(") || strText.contains("MIN(") || strText.contains("AVG(") || strText.contains("COUNT(")
						|| strText.contains("STDEV(") || strText.contains("STDEVP(") || strText.contains("VAR(") || strText.contains("VARP(")) {
					queryParameter.setCursorType(CursorType.STATIC);
					break;
				}
			}

			if (queryParameter.getGroupBy().length > 0) {
				queryParameter.setCursorType(CursorType.STATIC);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private String[] getQueryFieldNames(String queryFields) {
		int bracketsCount = 0;
		java.util.List<String> fieldNames = new ArrayList<>();
		char[] fieldNamesChars = queryFields.toCharArray();
		StringBuilder builderFieldName = new StringBuilder();
		for (char fieldNamesChar : fieldNamesChars) {
			if (fieldNamesChar == ',' && bracketsCount == 0 && builderFieldName.length() > 0) {
				fieldNames.add(builderFieldName.toString());
				builderFieldName.setLength(0);
			} else {
				builderFieldName.append(fieldNamesChar);
				if (fieldNamesChar == '(') {
					bracketsCount++;
				} else if (fieldNamesChar == ')' && bracketsCount > 0) {
					bracketsCount--;
				}
			}
		}
		if (builderFieldName.length() > 0) {
			fieldNames.add(builderFieldName.toString());
			builderFieldName.setLength(0);
		}
		return fieldNames.toArray(new String[fieldNames.size()]);
	}
}
