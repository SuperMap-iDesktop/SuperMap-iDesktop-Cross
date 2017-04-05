package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextArea;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/2/21.
 * sql查询简单实现
 */
public class MetaProcessSqlQuery extends MetaProcess {
	private ParameterDatasource datasource;
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
		datasource = new ParameterDatasource();
		this.datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameters = new DefaultParameters();
		this.dataset = new ParameterSingleDataset(datasetTypes);
		if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
			this.dataset.setSelectedItem(Application.getActiveApplication().getActiveDatasets()[0]);
		}
		parameterResultFields = new ParameterTextArea(CommonProperties.getString("String_QueryField"));
		parameterResultFields.setSelectedItem("RoadLine.*");
		parameterAttributeFilter = new ParameterTextArea(CommonProperties.getString("String_QueryCondition"));
		parameterAttributeFilter.setSelectedItem("RoadLine.RoadLeve = 'Lev1'");
		parameterSaveDataset = new ParameterSaveDataset();
		parameterSaveDataset.setDatasetName("QueryResult");
		initParameterConstraint();
		parameters.setParameters(datasource, this.dataset, this.parameterResultFields,
				this.parameterAttributeFilter, this.parameterSaveDataset);
		processTask = new ProcessTask(this);
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(dataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
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
		DatasetVector currentDatasetVector = inputs.getData() instanceof DatasetVector ? ((DatasetVector) inputs.getData()) : null;
		if (currentDatasetVector == null && dataset.getSelectedItem() instanceof DatasetVector) {
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
				// 保存查询结果
//				DatasetVector datasetVector = saveQueryResult(resultRecord);
//				ProcessData processData = new ProcessData();
//				processData.setData(datasetVector);
//				outPuts.add(0, processData);
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

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/sqlQuery.png");
	}
}
