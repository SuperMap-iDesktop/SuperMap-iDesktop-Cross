package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Created by caolp on 2017-07-27.
 */
public class ParameterInputDataType extends ParameterCombine {
	public ParameterComboBox parameterDataInputWay = new ParameterComboBox(ProcessProperties.getString("String_DataInputWay"));
	private ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();

	private ParameterTextField parameterDataSourceType = new ParameterTextField(ProcessProperties.getString("String_DataSourceType"));
	private ParameterFile parameterDataSourcePath = new ParameterFile(ProcessProperties.getString("String_DataSourcePath"));
	private ParameterComboBox parameterDatasetName = new ParameterComboBox(CommonProperties.getString("String_Dataset"));
	private ParameterTextField parameterDatasetName1 = new ParameterTextField(ProcessProperties.getString("String_DatasetName"));
	private ParameterComboBox parameterDatasetType = new ParameterComboBox(ProcessProperties.getString("String_DatasetType"));
	private ParameterDefaultValueTextField parameterSpark = new ParameterDefaultValueTextField(ProcessProperties.getString("String_numSlices"));
	private ParameterSwitch parameterSwitchUDB = new ParameterSwitch();
	private ParameterCombine parameterCombineDatasetInfo = new ParameterCombine();

	public ParameterComboBox parameterSourceDataset = new ParameterComboBox(CommonProperties.getString("String_Dataset"));
	private ParameterTextField parameterEngineType = new ParameterTextField(ProcessProperties.getString("String_EngineType"));
	private ParameterDefaultValueTextField parameterDataBaseName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_DataBaseName"));
	private ParameterDefaultValueTextField parameterTextFieldAddress = new ParameterDefaultValueTextField(CoreProperties.getString("String_Server"));
	private ParameterDefaultValueTextField parameterTextFieldUserName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));
	private ParameterButton parameterButton = new ParameterButton(CommonProperties.getString("String_Button_Open"));
	private static final int OVERLAY_ANALYST_GEO = 0;
	private static final int SINGLE_QUERY = 1;
	private static final int POLYGON_AGGREGATION = 2;
	private static final int SUMMARY_REGION = 3;

	public ParameterComboBox bigDataStoreName = new ParameterComboBox(CommonProperties.getString("String_Dataset"));
	public ParameterSwitch parameterSwitch = new ParameterSwitch();
	public DatasetType[] supportDatasetType;
	private Boolean bool = false;
	private ParameterCombine parameterCombine1;

	public ParameterInputDataType() {
		super();
		initComponents();
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
						if (true) {
							parameterCombine1.removeParameter(parameterSpark);
						}
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
					if (!new File(evt.getNewValue().toString()).exists()) {
						parameterSwitchUDB.switchParameter("1");
					} else {
						DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
						connectionInfo.setServer(evt.getNewValue().toString());
						connectionInfo.setEngineType(EngineType.UDB);
						Workspace workspace = new Workspace();

						Datasource datasource = null;
						try {
							datasource = workspace.getDatasources().open(connectionInfo);
						} catch (Exception e) {
							if (parameterSwitchUDB.getCurrentParameter().equals(parameterCombineDatasetInfo)) {
								parameterSwitchUDB.switchParameter("0");
							}
							Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_OpenDatasourceFaild"));
						}
						if (null != datasource) {
							if (parameterSwitchUDB.getCurrentParameter().equals(parameterCombineDatasetInfo)) {
								parameterSwitchUDB.switchParameter("0");
							}
							Datasets datasets = datasource.getDatasets();
							if (parameterDatasetName.getItems().size() > 0) {
								parameterDatasetName.removeAllItems();
							}
							ParameterDataNode datasetNode = null;
							for (int i = 0, size = datasets.getCount(); i < size; i++) {
								for (int j = 0, length = supportDatasetType.length; j < length; j++) {
									if (datasets.get(i).getType() == supportDatasetType[j]) {
										datasetNode = new ParameterDataNode(datasets.get(i).getName(), datasets.get(i).getType().name());
										parameterDatasetName.addItem(datasetNode);
									}
								}
							}
							parameterDatasetName.setSelectedItem(datasetNode);
							datasource.close();
							datasource = null;
							workspace.close();
							workspace = null;
						}
					}
				}
			}
		});
		parameterButton.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pgServer = parameterTextFieldAddress.getSelectedItem().toString();
				String pgDatabase = parameterDataBaseName.getSelectedItem().toString();
				String pgUsername = parameterTextFieldUserName.getSelectedItem().toString();
				String pgPassword = parameterTextFieldPassword.getSelectedItem().toString();
				if (!StringUtilities.isNullOrEmpty(pgServer) && !StringUtilities.isNullOrEmpty(pgDatabase)
						&& !StringUtilities.isNullOrEmpty(pgUsername) && !StringUtilities.isNullOrEmpty(pgPassword)) {
					DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
					connectionInfo.setServer(pgServer);
					connectionInfo.setDatabase(pgDatabase);
					connectionInfo.setUser(pgUsername);
					connectionInfo.setPassword(pgPassword);
					connectionInfo.setEngineType(EngineType.POSTGRESQL);
					Workspace workspace = new Workspace();
					Datasource datasource = null;
					try {
						datasource = workspace.getDatasources().open(connectionInfo);
					} catch (Exception ex) {
						if (parameterSourceDataset.getItems().size() > 0) {
							parameterSourceDataset.removeAllItems();
						}
						Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_OpenDatasetDatasourceFaild"));
					}
					if (null != datasource) {
						Datasets datasets = datasource.getDatasets();
						if (parameterSourceDataset.getItems().size() > 0) {
							parameterSourceDataset.removeAllItems();
						}
						ParameterDataNode datasetNode = null;
						for (int i = 0, size = datasets.getCount(); i < size; i++) {
							for (int j = 0, length = supportDatasetType.length; j < length; j++) {
								if (datasets.get(i).getType() == supportDatasetType[j]) {
									datasetNode = new ParameterDataNode(datasets.get(i).getName(), datasets.get(i).getType().name());
									parameterSourceDataset.addItem(datasetNode);
								}
							}
						}
						parameterSourceDataset.setSelectedItem(datasetNode);
						datasource.close();
						datasource = null;
						workspace.close();
						workspace = null;
					}
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
		parameterDatasetName1.setRequisite(true);
		parameterCombineDatasetInfo.addParameters(parameterDatasetName1, parameterDatasetType);

		parameterSwitchUDB.add("0", parameterDatasetName);
		parameterSwitchUDB.add("1", parameterCombineDatasetInfo);
		parameterSpark.setRequisite(true);
		parameterSpark.setDefaultWarningValue("36");
		parameterCombine1 = new ParameterCombine();
		parameterCombine1.addParameters(
				parameterDataSourcePath,
				parameterSwitchUDB,
				parameterSpark);

		//pg数据库
		parameterSourceDataset.setRequisite(true);
		parameterSourceDataset.setDescribe(ProcessProperties.getString("String_DatasetName"));
		ParameterCombine parameterOpenPG = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterOpenPG.addParameters(parameterSourceDataset, parameterButton);
		parameterEngineType.setSelectedItem("POSTGRESQL");
		parameterEngineType.setEnabled(false);
		parameterTextFieldAddress.setRequisite(true);
		parameterTextFieldAddress.setDefaultWarningValue("{ip}");
		parameterDataBaseName.setRequisite(true);
//		parameterDataBaseName.setDefaultWarningValue("");
		parameterTextFieldUserName.setRequisite(true);
//		parameterTextFieldUserName.setDefaultWarningValue("");
		parameterTextFieldPassword.setRequisite(true);
//		parameterTextFieldPassword.setSelectedItem("supermap");
		ParameterCombine parameterCombine2 = new ParameterCombine();
		parameterCombine2.addParameters(
				parameterTextFieldAddress,
				parameterDataBaseName,
				parameterTextFieldUserName,
				parameterTextFieldPassword,
				parameterOpenPG
		);

		//BigDataStore
		bigDataStoreName.setRequisite(true);
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
		parameterSourceDataset.setIConGetter(getter);
		parameterSwitch.add("0", parameterCombine);
		parameterSwitch.add("1", parameterCombine1);
		parameterSwitch.add("2", parameterCombine2);
		parameterSwitch.add("3", parameterCombine3);
		this.addParameters(parameterDataInputWay, parameterSwitch);
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
			CommonSettingCombine datasetName = null;
			CommonSettingCombine datasetType = null;
			CommonSettingCombine type = new CommonSettingCombine("type", parameterDataSourceType.getSelectedItem().toString());
			String udbPathStr = parameterDataSourcePath.getSelectedItem().toString();
			String udbPath = udbPathStr.replaceAll("\\\\", "\\\\\\\\");
			CommonSettingCombine url = new CommonSettingCombine("url", udbPath);
			if (parameterSwitchUDB.getCurrentParameter().equals(parameterCombineDatasetInfo)) {
				datasetName = new CommonSettingCombine("datasetName", parameterDatasetName1.getSelectedItem().toString());
				datasetType = new CommonSettingCombine("datasetType", parameterDatasetType.getSelectedData().toString());
			} else {
				ParameterDataNode datasetNode = (ParameterDataNode) parameterDatasetName.getSelectedItem();
				datasetName = new CommonSettingCombine("datasetName", datasetNode.getDescribe());
				datasetType = new CommonSettingCombine("datasetType", datasetNode.getData().toString());
			}
			CommonSettingCombine numSlices = new CommonSettingCombine("numSlices", parameterSpark.getSelectedItem().toString());
			datasetInfo.add(type, url, datasetName, datasetType);
			input.add(datasetInfo, numSlices);
		} else {
			ParameterDataNode sourceDataset = (ParameterDataNode) parameterSourceDataset.getSelectedItem();
			CommonSettingCombine name = new CommonSettingCombine("name", sourceDataset.getDescribe());
			CommonSettingCombine type = new CommonSettingCombine("type", sourceDataset.getData().toString());
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

	public void initAnalystInput(CommonSettingCombine analyst, int m) {
		String datasetBigDataStore = null;
		String datasetPG = null;
		switch (m) {
			case OVERLAY_ANALYST_GEO:
				datasetBigDataStore = "datasetOverlay";
				datasetPG = "inputOverlay";
				break;
			case SINGLE_QUERY:
				datasetBigDataStore = "datasetQuery";
				datasetPG = "inputQuery";
				break;
			case POLYGON_AGGREGATION:
				datasetBigDataStore = "regionDataset";
				datasetPG = "regionDatasource";
				break;
			case SUMMARY_REGION:
				datasetBigDataStore = "regionDataset";
				datasetPG = "regionDatasource";
				break;
		}
		if (parameterDataInputWay.getSelectedData().toString().equals("3")) {
			//HDFS
			CommonSettingCombine datasetOverlayCombine = new CommonSettingCombine(datasetBigDataStore, ((ParameterDataNode) bigDataStoreName.getSelectedItem()).getDescribe());
			analyst.add(datasetOverlayCombine);
		} else if (parameterDataInputWay.getSelectedData().toString().equals("1")) {
			//udb
			String inputOverlayStr = null;
			String udbPathStr = parameterDataSourcePath.getSelectedItem().toString();
			String udbPath = udbPathStr.replaceAll("\\\\", "//");
			if (parameterSwitchUDB.getCurrentParameter().equals(parameterCombineDatasetInfo)) {
				inputOverlayStr = "{\\\"type\\\":\\\"udb\\\",\\\"info\\\":[{\\\"server\\\":\\\"" + udbPath + "\\\",\\\"datasetNames\\\":[\\\"" + parameterDatasetName1.getSelectedItem().toString() + "\\\"]}]}";
			} else {
				ParameterDataNode datasetNode = (ParameterDataNode) parameterDatasetName.getSelectedItem();
				inputOverlayStr = "{\\\"type\\\":\\\"udb\\\",\\\"info\\\":[{\\\"server\\\":\\\"" + udbPath + "\\\",\\\"datasetNames\\\":[\\\"" + datasetNode.getDescribe() + "\\\"]}]}";
			}
			CommonSettingCombine inputOverlayCombine = new CommonSettingCombine(datasetPG, inputOverlayStr);
			analyst.add(inputOverlayCombine);
		} else {
			//pg
			ParameterDataNode sourceDataset = (ParameterDataNode) parameterSourceDataset.getSelectedItem();
			String inputOverlayStr = "{\\\"type\\\":\\\"pg\\\",\\\"info\\\":[{\\\"server\\\":\\\"" + parameterTextFieldAddress.getSelectedItem() + "\\\",\\\"datasetNames\\\":[\\\"" + sourceDataset.getDescribe() + "\\\"],\\\"database\\\":\\\"" + parameterDataBaseName.getSelectedItem() + "\\\",\\\"user\\\":\\\"" + parameterTextFieldUserName.getSelectedItem() + "\\\",\\\"password\\\":\\\"" + parameterTextFieldPassword.getSelectedItem() + "\\\"}]}";
			CommonSettingCombine inputOverlayCombine = new CommonSettingCombine(datasetPG, inputOverlayStr);
			analyst.add(inputOverlayCombine);
		}
	}

	public void setSupportDatasetType(DatasetType... datasetTypes) {
		parameterDatasetType.removeAllItems();
		for (DatasetType datasetType : datasetTypes) {
			parameterDatasetType.addItem(new ParameterDataNode(DatasetTypeUtilities.toString(datasetType), datasetType.name()));
		}
		supportDatasetType = datasetTypes;
	}

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}
}


