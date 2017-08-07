package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by caolp on 2017-07-27.
 */
public class ParameterInputDataType extends ParameterCombine {
	private ParameterComboBox parameterDataInputWay = new ParameterComboBox(ProcessProperties.getString("String_DataInputWay"));
	private ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();

	private ParameterTextField parameterDataSourceType = new ParameterTextField(ProcessProperties.getString("String_DataSourceType"));
	private ParameterTextField parameterDataSourcePath = new ParameterTextField(ProcessProperties.getString("String_DataSourcePath"));
	private ParameterTextField parameterDatasetName = new ParameterTextField(ProcessProperties.getString("String_DatasetName"));
	private ParameterComboBox parameterDatasetType = new ParameterComboBox(ProcessProperties.getString("String_DatasetType"));
	private ParameterTextField parameterSpark = new ParameterTextField(ProcessProperties.getString("String_numSlices"));

	private ParameterBigDatasourceDatasource parameterSourceDatasource = new ParameterBigDatasourceDatasource();
	private ParameterSingleDataset parameterSourceDataset = new ParameterSingleDataset();
	private ParameterTextField parameterEngineType = new ParameterTextField(ProcessProperties.getString("String_EngineType"));
	private ParameterTextField parameterDataBaseName = new ParameterTextField(ProcessProperties.getString("String_DataBaseName"));
	private ParameterTextField parameterTextFieldAddress = new ParameterTextField(CoreProperties.getString("String_Server"));
	private ParameterTextField parameterTextFieldUserName = new ParameterTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));

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
		parameterDataSourceType.setSelectedItem("UDB");
		parameterDataSourceType.setEnabled(false);
		parameterDataSourcePath.setSelectedItem("G:\\\\ProcessingData\\\\processing.udb");
		parameterDatasetName.setSelectedItem("newyorkPoint_P");
		parameterSpark.setSelectedItem("36");
		ParameterCombine parameterCombine1 = new ParameterCombine();
		parameterCombine1.addParameters(parameterDataSourceType,
				parameterDataSourcePath,
				parameterDatasetName,
				parameterDatasetType,
				parameterSpark);
		//pg数据库
		parameterSourceDatasource.setDescribe(ProcessProperties.getString("String_DataSourceName"));
		parameterSourceDataset.setDescribe(ProcessProperties.getString("String_DatasetName"));
		parameterEngineType.setSelectedItem("POSTGRESQL");
		parameterEngineType.setEnabled(false);
		parameterTextFieldAddress.setSelectedItem("192.168.15.248");
		parameterDataBaseName.setSelectedItem("supermap");
		parameterTextFieldUserName.setSelectedItem("postgres");
		parameterTextFieldPassword.setSelectedItem("supermap");
		ParameterCombine parameterCombine2 = new ParameterCombine();
		parameterCombine2.addParameters(
				parameterEngineType,
				parameterTextFieldAddress,
				parameterDataBaseName,
				parameterTextFieldUserName,
				parameterTextFieldPassword,
				parameterSourceDatasource,
				parameterSourceDataset,
				parameterDatasetType);
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
		this.addParameters(parameterDataInputWay, parameterSwitch);
		this.setDescribe(ProcessProperties.getString("String_FileInputPath"));
	}

	private void initConstraint() {
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

	public void initSourceInput(CommonSettingCombine input) {
		CommonSettingCombine datasetInfo = new CommonSettingCombine("datasetInfo", "");
		if (parameterDataInputWay.getSelectedData().toString().equals("0")) {
			CommonSettingCombine filePath = new CommonSettingCombine("filePath", parameterHDFSPath.getSelectedItem().toString());
			input.add(filePath);
		} else if (parameterDataInputWay.getSelectedData().toString().equals("1")) {
			CommonSettingCombine type = new CommonSettingCombine("type", parameterDataSourceType.getSelectedItem().toString());
			CommonSettingCombine url = new CommonSettingCombine("url", parameterDataSourcePath.getSelectedItem().toString());
			CommonSettingCombine datasetName = new CommonSettingCombine("datasetName", parameterDatasetName.getSelectedItem().toString());
			CommonSettingCombine datasetType = new CommonSettingCombine("datasetType", (String) parameterDatasetType.getSelectedData());
			CommonSettingCombine numSlices = new CommonSettingCombine("numSlices", parameterSpark.getSelectedItem().toString());
			datasetInfo.add(type, url, datasetName, datasetType);
			input.add(datasetInfo, numSlices);
		} else {
			Dataset sourceDataset = parameterSourceDataset.getSelectedDataset();
			//CommonSettingCombine dataSourceName = new CommonSettingCombine("dataSourceName", parameterSourceDatasource.getSelectedItem().getAlias());
			CommonSettingCombine name = new CommonSettingCombine("name", sourceDataset.getName());
			CommonSettingCombine type = new CommonSettingCombine("type", (String) parameterDatasetType.getSelectedData());
			CommonSettingCombine engineType = new CommonSettingCombine("engineType", parameterEngineType.getSelectedItem().toString());
			CommonSettingCombine server = new CommonSettingCombine("server", parameterTextFieldAddress.getSelectedItem().toString());
			CommonSettingCombine dataBase = new CommonSettingCombine("dataBase", parameterDataBaseName.getSelectedItem().toString());
			CommonSettingCombine user = new CommonSettingCombine("user", parameterTextFieldUserName.getSelectedItem().toString());
			CommonSettingCombine password = new CommonSettingCombine("password", parameterTextFieldPassword.getSelectedItem().toString());
			CommonSettingCombine datasourceConnectionInfo = new CommonSettingCombine("datasourceConnectionInfo", "");
			datasourceConnectionInfo.add(engineType, server, dataBase, user, password);
			datasetInfo.add(type, name, datasourceConnectionInfo);
			input.add(datasetInfo);
		}
	}

	public void setSupportDatasetType(DatasetType... datasetTypes) {
		parameterDatasetType.removeAllItems();
		for (DatasetType datasetType : datasetTypes) {
			parameterDatasetType.addItem(new ParameterDataNode(DatasetTypeUtilities.toString(datasetType), datasetType.name()));
		}
		parameterSourceDataset.setDatasetTypes(datasetTypes);

	}
}


