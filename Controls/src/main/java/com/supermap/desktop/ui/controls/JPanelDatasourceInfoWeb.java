package com.supermap.desktop.ui.controls;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TextFields.DefaultValueTextField;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * @author Administrator
 */
public class JPanelDatasourceInfoWeb extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel jLabelServerAddress;
	private JLabel jLabelServerType;
	private JLabel jLabelUserName;
	private JLabel jLabelPassword;
	private JLabel jLabelDatasourceAlias;
	private JLabel jLabelOpenType;
	private DefaultValueTextField jTextFieldServerAddress;
	private JComboBox<String> jComboBoxServerType;
	private JTextField jTextFieldUserName;
	private JPasswordField jTextFieldPassword;
	private JTextField jTextFieldDatasourceAlias;
	private JCheckBox jCheckBoxReadonly;
	private transient EngineType engineType;
	private final static int LOAD_DATASOURCE_FAILED = 0;
	public final static int LOAD_DATASOURCE_SUCCESSFUL = 1;
	public final static int LOAD_DATASOURCE_EXCEPTION = 2;

	public JPanelDatasourceInfoWeb() {
		initComponents();
		initResources();
		setComponentName();
	}

	private void initComponents() {

		jLabelServerAddress = new JLabel("ServerAddress:");
		jLabelServerType = new JLabel("ServerType:");
		jLabelUserName = new JLabel("UserName:");
		jLabelPassword = new JLabel("Password:");
		jLabelDatasourceAlias = new JLabel("DatasourceAlias:");
		jLabelOpenType = new JLabel("OpenType:");
		jTextFieldServerAddress = new DefaultValueTextField();
		jComboBoxServerType = new JComboBox<String>();
		jComboBoxServerType.setEditable(true);
		jTextFieldUserName = new JTextField();
		jTextFieldPassword = new JPasswordField();
		jTextFieldDatasourceAlias = new JTextField(ControlsProperties.getString("String_OGCWMS"));
		jCheckBoxReadonly = new JCheckBox();
		jCheckBoxReadonly.setSelected(true);
		jCheckBoxReadonly.setEnabled(false);
		this.jComboBoxServerType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jComboBoxServerTypeChange();
			}
		});
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
										GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup()
												.addGroup(
														layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabelServerAddress)
																.addComponent(jLabelServerType).addComponent(jLabelUserName).addComponent(jLabelPassword)
																.addComponent(jLabelDatasourceAlias).addComponent(jLabelOpenType))
												.addGap(20, 20, 20)
												.addGroup(
														layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jTextFieldServerAddress)
																.addComponent(jComboBoxServerType).addComponent(jTextFieldUserName)
																.addComponent(jTextFieldPassword).addComponent(jTextFieldDatasourceAlias)
																.addComponent(jCheckBoxReadonly)))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jLabelServerAddress)
												.addComponent(jTextFieldServerAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jComboBoxServerType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE).addComponent(jLabelServerType))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jTextFieldUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE).addComponent(jLabelUserName))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jTextFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE).addComponent(jLabelPassword))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jTextFieldDatasourceAlias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE).addComponent(jLabelDatasourceAlias))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jCheckBoxReadonly, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE).addComponent(jLabelOpenType))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.jLabelServerAddress, "JPanelDatasourceInfoWeb_jLabelServerAddress");
		ComponentUIUtilities.setName(this.jLabelServerType, "JPanelDatasourceInfoWeb_jLabelServerType");
		ComponentUIUtilities.setName(this.jLabelUserName, "JPanelDatasourceInfoWeb_jLabelUserName");
		ComponentUIUtilities.setName(this.jLabelPassword, "JPanelDatasourceInfoWeb_jLabelPassword");
		ComponentUIUtilities.setName(this.jLabelDatasourceAlias, "JPanelDatasourceInfoWeb_jLabelDatasourceAlias");
		ComponentUIUtilities.setName(this.jLabelOpenType, "JPanelDatasourceInfoWeb_jLabelOpenType");
		ComponentUIUtilities.setName(this.jTextFieldServerAddress, "JPanelDatasourceInfoWeb_jTextFieldServerAddress");
		ComponentUIUtilities.setName(this.jComboBoxServerType, "JPanelDatasourceInfoWeb_jComboBoxServerType");
		ComponentUIUtilities.setName(this.jTextFieldUserName, "JPanelDatasourceInfoWeb_jTextFieldUserName");
		ComponentUIUtilities.setName(this.jTextFieldPassword, "JPanelDatasourceInfoWeb_jTextFieldPassword");
		ComponentUIUtilities.setName(this.jTextFieldDatasourceAlias, "JPanelDatasourceInfoWeb_jTextFieldDatasourceAlias");
		ComponentUIUtilities.setName(this.jCheckBoxReadonly, "JPanelDatasourceInfoWeb_jCheckBoxReadonly");
	}

	protected void jComboBoxServerTypeChange() {
		String item = (String) this.jComboBoxServerType.getSelectedItem();
		this.jTextFieldDatasourceAlias.setText(item);
	}

	public void setDatasourceType(EngineType engineType) {
		try {
			this.engineType = engineType;
			jTextFieldUserName.setText("");
			jTextFieldPassword.setText("");
			if (engineType == EngineType.OGC) {
				this.jComboBoxServerType.removeAllItems();
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWMS"));
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWFS"));
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWCS"));
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWMTS"));
				this.jComboBoxServerType.setSelectedIndex(0);
				this.jTextFieldServerAddress.setText("");
				jTextFieldServerAddress.setDefaulWarningText("");
				this.jLabelPassword.setText(ControlsProperties.getString("String_Label_UserPassword"));
				this.jTextFieldServerAddress.setEnabled(true);
				this.jComboBoxServerType.setEnabled(true);
				this.jTextFieldUserName.setEnabled(false);
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_OGCWMS"));
			} else if (engineType == EngineType.ISERVERREST) {
				jTextFieldServerAddress.setDefaulWarningText("");
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_iServerRest"));
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldServerAddress.setEnabled(true);
				this.jTextFieldServerAddress.setText("");
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			} else if (engineType == EngineType.SUPERMAPCLOUD) {
				jTextFieldServerAddress.setDefaulWarningText("");
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_SuperMapCloudServer"));
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_SuperMapCloud"));
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldServerAddress.setEnabled(true);
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			} else
//            if (engineType == EngineType.MAPWORLD) {
//                this.jComboBoxServerType.removeAllItems();
//                this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_MapWorldServer"));
//                this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_MapWorld"));
//                this.jComboBoxServerType.setEnabled(false);
//                this.jTextFieldServerAddress.setEnabled(false);
//                this.jTextFieldUserName.setEnabled(false);
//                this.jTextFieldPassword.setEnabled(false);
//                this.jTextFieldDatasourceAlias.setEnabled(true);
//                this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
//            }
				if (engineType == EngineType.GOOGLEMAPS) {
					jTextFieldServerAddress.setDefaulWarningText("");

					this.jComboBoxServerType.removeAllItems();
					this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_GoogleMapsServer"));
					this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_GoogleMaps"));
					this.jComboBoxServerType.setEnabled(false);
					this.jTextFieldServerAddress.setEnabled(false);
					this.jTextFieldUserName.setEnabled(true);
					this.jTextFieldDatasourceAlias.setEnabled(true);
					this.jTextFieldPassword.setEnabled(true);
					this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
				} else if (engineType == EngineType.BAIDUMAPS) {
					jTextFieldServerAddress.setDefaulWarningText("");
					this.jComboBoxServerType.removeAllItems();
					this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_BaiduMapServer"));
					this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_BaiduMap"));
					this.jComboBoxServerType.setEnabled(false);
					this.jTextFieldServerAddress.setEnabled(false);
					this.jTextFieldUserName.setEnabled(false);
					this.jTextFieldPassword.setEnabled(false);
					this.jTextFieldDatasourceAlias.setEnabled(true);
					this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
				} else if (engineType == EngineType.OPENSTREETMAPS) {
					jTextFieldServerAddress.setDefaulWarningText("");
					this.jComboBoxServerType.removeAllItems();
					this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_OpenStreetMapsServer"));
					this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_OpenStreetMaps"));
					this.jComboBoxServerType.setEnabled(false);
					this.jTextFieldServerAddress.setEnabled(false);
					this.jTextFieldUserName.setEnabled(false);
					this.jTextFieldPassword.setEnabled(false);
					this.jTextFieldDatasourceAlias.setEnabled(true);
//					this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
				} else if (engineType == EngineType.DATASERVER) {
					jTextFieldServerAddress.setText(null);
					jTextFieldServerAddress.setDefaulWarningText("{ip}:{port}");
					this.jComboBoxServerType.removeAllItems();
					this.jTextFieldDatasourceAlias.setText("BigDataStore");
					this.jComboBoxServerType.setEnabled(false);
					this.jTextFieldServerAddress.setEnabled(true);
					this.jTextFieldUserName.setEnabled(false);// 暂不支持
					this.jTextFieldPassword.setEnabled(false);// 暂不支持
					this.jTextFieldDatasourceAlias.setEnabled(true);
				}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public int loadDatasource() {
		int connFlag = -1;
		try {
			DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
			connectionInfo.setEngineType(engineType);
			String datasourceName = jTextFieldDatasourceAlias.getText();
			String service = jTextFieldServerAddress.getText().trim();
			connectionInfo.setServer(service);
			if (engineType == EngineType.OGC) {
				connectionInfo.setDriver((String) jComboBoxServerType.getSelectedItem());
			}
			if (!StringUtilities.isNullOrEmpty(jTextFieldUserName.getText())) {
				connectionInfo.setUser(jTextFieldUserName.getText());
			}
			if (!StringUtilities.isNullOrEmpty(String.valueOf(jTextFieldPassword.getPassword()))) {
				connectionInfo.setPassword(String.valueOf(jTextFieldPassword.getPassword()));
			}
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			Datasource datasource = null;
			boolean datasetHasOpened = false;
			if (service.isEmpty()) {
				UICommonToolkit.showMessageDialog(ControlsProperties.getString("String_InputServiceAddress"));
				jTextFieldServerAddress.requestFocus();
				return 0;
			} else if (!service.isEmpty() && null == datasources.get(datasourceName)) {
				connectionInfo.setAlias(datasourceName);
				datasource = datasources.open(connectionInfo);
			} else {
				datasetHasOpened = true;
			}
			if (null == datasource && !datasetHasOpened) {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasourceFaild"));
				connFlag = LOAD_DATASOURCE_FAILED;
			} else if (datasetHasOpened) {
				connFlag = LOAD_DATASOURCE_FAILED;
				Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_DatasourceHasOpened"), datasourceName));
				UICommonToolkit.refreshSelectedDatasourceNode(datasourceName);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasourceSuccessful"));
				connFlag = LOAD_DATASOURCE_SUCCESSFUL;
				UICommonToolkit.refreshSelectedDatasourceNode(datasourceName);
				if (engineType == EngineType.DATASERVER) {
					openBigDataStorePG(datasource);
				}
			}
		} catch (Exception ex) {
			connFlag = LOAD_DATASOURCE_EXCEPTION;
			Application.getActiveApplication().getOutput().output(ex.getMessage());
			Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasetDatasourceFaild"));
		}
		return connFlag;
	}

	private void openBigDataStorePG(Datasource datasource) {
		if (datasource != null) {
			String datasourceAlias = datasource.getAlias();
			Workspace workspace = null;
			if (datasource.getDatasets().getCount() <= 0) {
				datasource.getDatasets().create(new DatasetVectorInfo("new_point", DatasetType.POINT));
				workspace = new Workspace();
				DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
				datasourceConnectionInfo.setEngineType(EngineType.DATASERVER);
				datasourceConnectionInfo.setServer(datasource.getConnectionInfo().getServer());
				datasourceConnectionInfo.setAlias("datasource");
				datasource = workspace.getDatasources().open(datasourceConnectionInfo);
			}

			ArrayList<String> dataBaseNames = new ArrayList<>();
			for (int i = 0; i < datasource.getDatasets().getCount(); i++) {
				String description = datasource.getDatasets().get(i).getDescription();
				if (!dataBaseNames.contains(description)) {
					openDatasetPG(description, datasourceAlias);
					dataBaseNames.add(description);
				}
			}
			if (workspace != null) {
				workspace.getDatasources().get(0).getDatasets().delete(0);
				workspace.getDatasources().close(0);
				workspace.close();
				workspace.dispose();
			}
		}
	}

	private void openDatasetPG(String description, String datasourceAlias) {
		Document rootNode = XmlUtilities.stringToDocument(description);
		Node datasourceNode = rootNode.getChildNodes().item(0);
		String server = XmlUtilities.getChildElementNodeByName(datasourceNode, "sml:Server").getChildNodes().item(0).getNodeValue();
		String driver = XmlUtilities.getChildElementNodeByName(datasourceNode, "sml:Driver").getChildNodes().item(0).getNodeValue();
		String database = XmlUtilities.getChildElementNodeByName(datasourceNode, "sml:Database").getChildNodes().item(0).getNodeValue();
		String user = XmlUtilities.getChildElementNodeByName(datasourceNode, "sml:User").getChildNodes().item(0).getNodeValue();
		String password = XmlUtilities.getChildElementNodeByName(datasourceNode, "sml:Password").getChildNodes().item(0).getNodeValue();

		DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
		datasourceConnectionInfo.setEngineType(EngineType.POSTGRESQL);
		datasourceConnectionInfo.setServer(server);
		datasourceConnectionInfo.setDriver(driver);
		datasourceConnectionInfo.setUser(user);
		datasourceConnectionInfo.setPassword(password);
		datasourceConnectionInfo.setDatabase(database);
		datasourceConnectionInfo.setAlias(DatasourceUtilities.getAvailableDatasourceAlias((datasourceAlias + "_" + database), 0));
		Application.getActiveApplication().getWorkspace().getDatasources().open(datasourceConnectionInfo);
	}

	private void initResources() {
		this.jLabelServerAddress.setText(CommonProperties.getString("String_Label_DataSource_ServiceAddress"));
		this.jLabelServerType.setText(CommonProperties.getString("String_Label_DataSource_ServerType"));
		this.jLabelUserName.setText(ControlsProperties.getString("String_Label_UserName"));
		this.jLabelDatasourceAlias.setText(ControlsProperties.getString("String_Label_DatasourseAlias"));
		this.jLabelOpenType.setText(ControlsProperties.getString("String_Label_OpenType"));
		this.jCheckBoxReadonly.setText(CoreProperties.getString("String_ReadOnly"));
	}
}
