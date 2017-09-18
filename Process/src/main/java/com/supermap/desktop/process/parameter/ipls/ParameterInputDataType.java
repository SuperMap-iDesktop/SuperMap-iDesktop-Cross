package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.*;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by caolp on 2017-07-27.
 */
public class ParameterInputDataType extends ParameterCombine {
	public ParameterComboBox parameterDataInputWay = new ParameterComboBox(ProcessProperties.getString("String_DataInputWay"));
	private ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();

	private ParameterTextField parameterDataSourceType = new ParameterTextField(ProcessProperties.getString("String_DataSourceType"));
	private ParameterFile parameterDataSourcePath = new ParameterFile(ProcessProperties.getString("String_DataSourcePath"));
	private ParameterComboBox parameterDatasetName = new ParameterComboBox(ProcessProperties.getString("String_DatasetName"));
	//	private ParameterDefaultValueTextField parameterDatasetName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_DatasetName"));
//	private ParameterComboBox parameterDatasetType = new ParameterComboBox(ProcessProperties.getString("String_DatasetType"));
	private ParameterDefaultValueTextField parameterSpark = new ParameterDefaultValueTextField(ProcessProperties.getString("String_numSlices"));

	private ParameterBigDatasourceDatasource parameterSourceDatasource = new ParameterBigDatasourceDatasource();
	public static ParameterSingleDataset parameterSourceDataset = new ParameterSingleDataset();
	//	private ParameterComboBox parameterDatasetType1 = new ParameterComboBox(ProcessProperties.getString("String_DatasetType"));
	private ParameterTextField parameterEngineType = new ParameterTextField(ProcessProperties.getString("String_EngineType"));
	private ParameterDefaultValueTextField parameterDataBaseName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_DataBaseName"));
	private ParameterDefaultValueTextField parameterTextFieldAddress = new ParameterDefaultValueTextField(CoreProperties.getString("String_Server"));
	private ParameterDefaultValueTextField parameterTextFieldUserName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));

	public ParameterComboBox bigDataStoreName = new ParameterComboBox(ProcessProperties.getString("String_DatasetName"));
	public ParameterSwitch parameterSwitch = new ParameterSwitch();

	public ParameterInputDataType() {
		super();
		initComponents();
		initParameterState();
		initConstraint();
		registerEvents();
	}

	private void registerEvents() {
		parameterDataInputWay.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					if (parameterDataInputWay.getSelectedData().toString().equals("0")) {
						parameterSwitch.switchParameter("0");
					} else if (parameterDataInputWay.getSelectedData().toString().equals("1")) {
						parameterSwitch.switchParameter("1");
					} else if (parameterDataInputWay.getSelectedData().toString().equals("2")) {
						parameterSwitch.switchParameter("2");
					} else {
						parameterSwitch.switchParameter("3");
					}

				}
			}
		});
		parameterDataSourcePath.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!StringUtilities.isNullOrEmpty(evt.getNewValue().toString())) {
					DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
					connectionInfo.setServer(evt.getNewValue().toString());
					connectionInfo.setEngineType(EngineType.UDB);
					Workspace workspace = new Workspace();
					Datasource datasource = workspace.getDatasources().open(connectionInfo);
					if (null != datasource) {
						Datasets datasets = datasource.getDatasets();
						if (parameterDatasetName.getItems().size() > 0) {
							parameterDatasetName.removeAllItems();
						}
						ParameterDataNode datasetNode = null;
						for (int i = 0, size = datasets.getCount(); i < size; i++) {
							for (int j = 0, length = parameterSourceDataset.getDatasetTypes().length; j < length; j++) {
								if (datasets.get(i).getType() == parameterSourceDataset.getDatasetTypes()[j]) {
									datasetNode = new ParameterDataNode(datasets.get(i).getName(), datasets.get(i).getType().name());
									parameterDatasetName.addItem(datasetNode);
								}
							}
						}
						parameterDatasetName.setSelectedItem(datasetNode);
					}

					datasource.close();
					datasource = null;
					workspace.close();
					workspace = null;
				}
			}
		});
	}

	private void initComponents() {
		parameterDataInputWay.setItems(new ParameterDataNode(ProcessProperties.getString("String_CSVFile"), "0"), new ParameterDataNode(ProcessProperties.getString("String_BigDataStore"), "3"),
				new ParameterDataNode(ProcessProperties.getString("String_UDBFile"), "1"), new ParameterDataNode(ProcessProperties.getString("String_PG"), "2"));
		//csv文件
		parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterHDFSPath);
		//udb文件
		parameterDataSourceType.setSelectedItem("UDB");
		parameterDataSourceType.setEnabled(false);
		parameterDataSourcePath.setRequisite(true);
		parameterDataSourcePath.setModuleName("InputDataTypeDatasource");
		parameterDataSourcePath.addExtension(ProcessProperties.getString("String_UDBFileFilterName"), "udb");
		parameterDataSourcePath.setModuleType("OpenOne");
		parameterDatasetName.setRequisite(true);
//		parameterDatasetName.setDefaultWarningValue("newyorkPoint_P");
		parameterSpark.setRequisite(true);
		parameterSpark.setDefaultWarningValue("36");
		ParameterCombine parameterCombine1 = new ParameterCombine();
		parameterCombine1.addParameters(
				parameterDataSourcePath,
				parameterDatasetName,
//				parameterDatasetType,
				parameterSpark);
		//pg数据库
		parameterSourceDatasource.setRequisite(true);
		parameterSourceDatasource.setDescribe(ProcessProperties.getString("String_DataSourceName"));
		parameterSourceDataset.setRequisite(true);
		parameterSourceDataset.setDescribe(ProcessProperties.getString("String_DatasetName"));
		parameterEngineType.setSelectedItem("POSTGRESQL");
		parameterEngineType.setEnabled(false);
		parameterTextFieldAddress.setRequisite(true);
		parameterTextFieldAddress.setDefaultWarningValue("192.168.15.248");
		parameterDataBaseName.setRequisite(true);
		parameterDataBaseName.setDefaultWarningValue("supermap");
		parameterTextFieldUserName.setRequisite(true);
		parameterTextFieldUserName.setDefaultWarningValue("postgres");
		parameterTextFieldPassword.setRequisite(true);
		parameterTextFieldPassword.setSelectedItem("supermap");
		ParameterCombine parameterCombine2 = new ParameterCombine();
		parameterCombine2.addParameters(
				parameterTextFieldAddress,
				parameterDataBaseName,
				parameterTextFieldUserName,
				parameterTextFieldPassword,
				parameterSourceDatasource,
				parameterSourceDataset
//				,parameterDatasetType1
		);

		//BigDataStore
		ParameterCombine parameterCombine3 = new ParameterCombine();
		parameterCombine3.addParameters(bigDataStoreName);
		IConGetter getter = new IConGetter() {
			@Override
			public Icon getICon(ParameterDataNode parameterDataNode) {
				Icon result = null;
				if (null != parameterDataNode && null != parameterDataNode.getData()) {
					if ("POINT".equals(parameterDataNode.getData().toString())) {
						result = ProcessResources.getIcon("/processresources/Image_DatasetPoint_Normal.png");
					}
					if ("LINE".equals(parameterDataNode.getData().toString())) {
						result = ProcessResources.getIcon("/processresources/Image_DatasetLine_Normal.png");
					}
					if ("REGION".equals(parameterDataNode.getData().toString())) {
						result = ProcessResources.getIcon("/processresources/Image_DatasetRegion_Normal.png");
					}
				}
				return result;
			}
		};
		bigDataStoreName.setIConGetter(getter);
		parameterDatasetName.setIConGetter(getter);
		parameterSwitch.add("0", parameterCombine);
		parameterSwitch.add("1", parameterCombine1);
		parameterSwitch.add("2", parameterCombine2);
		parameterSwitch.add("3", parameterCombine3);
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
		} else if (parameterDataInputWay.getSelectedData().toString().equals("3")) {
			CommonSettingCombine datasetName = new CommonSettingCombine("datasetName", ((ParameterDataNode) bigDataStoreName.getSelectedItem()).getDescribe());
			input.add(datasetName);
		} else if (parameterDataInputWay.getSelectedData().toString().equals("1")) {
			CommonSettingCombine type = new CommonSettingCombine("type", parameterDataSourceType.getSelectedItem().toString());
			CommonSettingCombine url = new CommonSettingCombine("url", parameterDataSourcePath.getSelectedItem().toString());
			ParameterDataNode datasetNode = (ParameterDataNode) parameterDatasetName.getSelectedItem();
			CommonSettingCombine datasetName = new CommonSettingCombine("datasetName", datasetNode.getDescribe());
			CommonSettingCombine datasetType = new CommonSettingCombine("datasetType", datasetNode.getData().toString());
			CommonSettingCombine numSlices = new CommonSettingCombine("numSlices", parameterSpark.getSelectedItem().toString());
			datasetInfo.add(type, url, datasetName, datasetType);
			input.add(datasetInfo, numSlices);
		} else {
			Dataset sourceDataset = parameterSourceDataset.getSelectedDataset();
			CommonSettingCombine name = new CommonSettingCombine("name", sourceDataset.getName());
			CommonSettingCombine type = new CommonSettingCombine("type", sourceDataset.getType().name());
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
//		parameterDatasetType.removeAllItems();
//		parameterDatasetType1.removeAllItems();
		for (DatasetType datasetType : datasetTypes) {
//			parameterDatasetType.addItem(new ParameterDataNode(DatasetTypeUtilities.toString(datasetType), datasetType.name()));
//			parameterDatasetType1.addItem(new ParameterDataNode(DatasetTypeUtilities.toString(datasetType), datasetType.name()));
		}
		parameterSourceDataset.setDatasetTypes(datasetTypes);
	}

}


