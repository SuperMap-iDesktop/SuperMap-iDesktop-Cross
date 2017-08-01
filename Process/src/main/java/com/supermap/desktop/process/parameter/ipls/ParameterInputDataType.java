package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by caolp on 2017-07-27.
 */
public class ParameterInputDataType extends ParameterCombine {
	public ParameterComboBox parameterDataInputWay = new ParameterComboBox(ProcessProperties.getString("String_DataInputWay"));
	public ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();

	public ParameterTextField parameterDataSouceType = new ParameterTextField(ProcessProperties.getString("String_DataSourceType"));
	public ParameterTextField parameterDataSoucePath = new ParameterTextField(ProcessProperties.getString("String_DataSourcePath"));
	public ParameterTextField parameterDatasetName = new ParameterTextField(ProcessProperties.getString("String_DatasetName"));
	public ParameterComboBox parameterDatasetType = new ParameterComboBox(ProcessProperties.getString("String_DatasetType"));
	public ParameterTextField parameterSpark = new ParameterTextField(ProcessProperties.getString("String_numSlices"));

	public ParameterBigDatasourceDatasource parameterSourceDatasource = new ParameterBigDatasourceDatasource();
	public ParameterSingleDataset parameterSourceDataset = new ParameterSingleDataset(DatasetType.POINT);
	public ParameterComboBox parameterDatasetType1 = new ParameterComboBox(ProcessProperties.getString("String_DatasetType"));
	public ParameterTextField parameterEngineType = new ParameterTextField(ProcessProperties.getString("String_EngineType"));
	public ParameterTextField parameterDataBaseName = new ParameterTextField(ProcessProperties.getString("String_DataBaseName"));
	public ParameterTextField parameterTextFieldAddress = new ParameterTextField(CoreProperties.getString("String_Server"));
	public ParameterTextField parameterTextFieldUserName = new ParameterTextField(ProcessProperties.getString("String_UserName"));
	public ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));

	public ParameterInputDataType() {
		super();
		initComponents();
		initParameterState();
		initConstraint();
	}

	private void initComponents() {
		parameterDataInputWay.setItems(new ParameterDataNode(ProcessProperties.getString("String_CSVFile"), "0"),
				new ParameterDataNode(ProcessProperties.getString("String_UDBFile"), "1"), new ParameterDataNode(ProcessProperties.getString("String_PGDataBase"), "2"));
		//csv文件
		parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterHDFSPath);
		//udb文件
		parameterDataSouceType.setSelectedItem("UDB");
		parameterDataSouceType.setEnabled(false);
		parameterDataSoucePath.setSelectedItem("F:\\20170707\\China\\China.udb");
		parameterDatasetName.setSelectedItem("China_Capital_pt");
		parameterDatasetType.addItem(new ParameterDataNode(ProcessProperties.getString("String_Point"), "POINT"));
		parameterSpark.setSelectedItem("36");
		ParameterCombine parameterCombine1 = new ParameterCombine();
		parameterCombine1.addParameters(parameterDataSouceType,
				parameterDataSoucePath,
				parameterDatasetName,
				parameterDatasetType,
				parameterSpark);
		//pg数据库
		parameterSourceDatasource.setDescribe(ProcessProperties.getString("String_DataSourceName"));
		parameterSourceDataset.setDescribe(ProcessProperties.getString("String_DatasetName"));
		parameterDatasetType1.addItem(new ParameterDataNode(ProcessProperties.getString("String_Point"), "POINT"));
		parameterEngineType.setSelectedItem("POSTGRESQL");
		parameterEngineType.setEnabled(false);
		parameterTextFieldAddress.setSelectedItem("192.168.15.248");
		parameterDataBaseName.setSelectedItem("supermap");
		parameterTextFieldUserName.setSelectedItem("postgres");
		parameterTextFieldPassword.setSelectedItem("supermap");
		ParameterCombine parameterCombine2 = new ParameterCombine();
		parameterCombine2.addParameters(parameterSourceDatasource,
				parameterSourceDataset,
				parameterDatasetType1,
				parameterEngineType,
				parameterTextFieldAddress,
				parameterDataBaseName,
				parameterTextFieldUserName,
				parameterTextFieldPassword);
		final ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.add("0", parameterCombine);
		parameterSwitch.add("1", parameterCombine1);
		parameterSwitch.add("2", parameterCombine2);
		parameterDataInputWay.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					if (parameterDataInputWay.getSelectedData().toString().equals("0")) {
						parameterSwitch.switchParameter("0");
					} else {
						if (parameterDataInputWay.getSelectedData().toString().equals("1")) {
							parameterSwitch.switchParameter("1");
						} else {
							parameterSwitch.switchParameter("2");
						}
					}
				}
			}
		});
		this.addParameters(parameterDataInputWay,parameterSwitch);
		this.setDescribe(ProcessProperties.getString("String_FileInputPath"));
	}

	private void initConstraint(){
		EqualDatasourceConstraint equalSourceDatasource = new EqualDatasourceConstraint();
		equalSourceDatasource.constrained(parameterSourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalSourceDatasource.constrained(parameterSourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParameterState() {
		Dataset defaultBigDataStoreDataset = DatasetUtilities.getDefaultBigDataStoreDataset();
		if (defaultBigDataStoreDataset != null) {
			parameterSourceDatasource.setSelectedItem(defaultBigDataStoreDataset.getDatasource());
			parameterSourceDataset.setSelectedItem(defaultBigDataStoreDataset);
		}
	}
}


