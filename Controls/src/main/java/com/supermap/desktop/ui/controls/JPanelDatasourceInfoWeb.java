package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
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
	private JTextField jTextFieldServerAddress;
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
	}

	private void initComponents() {

		jLabelServerAddress = new JLabel("ServerAddress:");
		jLabelServerType = new JLabel("ServerType:");
		jLabelUserName = new JLabel("UserName:");
		jLabelPassword = new JLabel("Password:");
		jLabelDatasourceAlias = new JLabel("DatasourceAlias:");
		jLabelOpenType = new JLabel("OpenType:");
		jTextFieldServerAddress = new JTextField("DatabaseName");
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

	protected void jComboBoxServerTypeChange() {
		String item = (String) this.jComboBoxServerType.getSelectedItem();
		this.jTextFieldDatasourceAlias.setText(item);
	}

	public void setDatasourceType(EngineType engineType) {
		try {
			this.engineType = engineType;
			if (engineType == EngineType.OGC) {
				this.jComboBoxServerType.removeAllItems();
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWMS"));
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWFS"));
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWCS"));
				this.jComboBoxServerType.addItem(ControlsProperties.getString("String_OGCWMTS"));
				this.jComboBoxServerType.setSelectedIndex(0);
				this.jTextFieldServerAddress.setText("");
				this.jLabelPassword.setText(ControlsProperties.getString("String_Label_UserPassword"));
				this.jTextFieldServerAddress.setEnabled(true);
				this.jComboBoxServerType.setEnabled(true);
				this.jTextFieldUserName.setEnabled(false);
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_OGCWMS"));
			}
			if (engineType == EngineType.ISERVERREST) {
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_iServerRest"));
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldServerAddress.setEnabled(true);
				this.jTextFieldServerAddress.setText("");
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			}
			if (engineType == EngineType.SUPERMAPCLOUD) {
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_SuperMapCloudServer"));
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_SuperMapCloud"));
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldServerAddress.setEnabled(true);
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			}
			if (engineType == EngineType.MAPWORLD) {
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_MapWorldServer"));
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_MapWorld"));
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldServerAddress.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			}
			if (engineType == EngineType.GOOGLEMAPS) {
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_GoogleMapsServer"));
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_GoogleMaps"));
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldServerAddress.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jTextFieldPassword.setEnabled(true);
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			}
			if (engineType == EngineType.BAIDUMAPS) {
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_BaiduMapServer"));
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_BaiduMap"));
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldServerAddress.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
			}
			if (engineType == EngineType.OPENSTREETMAPS) {
				this.jComboBoxServerType.removeAllItems();
				this.jTextFieldServerAddress.setText(ControlsProperties.getString("String_OpenStreetMapsServer"));
				this.jTextFieldDatasourceAlias.setText(ControlsProperties.getString("String_OpenStreetMaps"));
				this.jComboBoxServerType.setEnabled(false);
				this.jTextFieldServerAddress.setEnabled(false);
				this.jTextFieldUserName.setEnabled(false);
				this.jTextFieldPassword.setEnabled(false);
				this.jTextFieldDatasourceAlias.setEnabled(true);
				this.jLabelPassword.setText(CoreProperties.getString("String_Label_Key"));
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
			if (engineType == EngineType.GOOGLEMAPS) {
				connectionInfo.setPassword(String.valueOf(jTextFieldPassword.getPassword()));
			}
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			Datasource datasource = null;
			boolean datasetHasOpened = false;
			if (service.isEmpty()) {
				UICommonToolkit.showMessageDialog(ControlsProperties.getString("String_InputServiceAddress"));
				jTextFieldServerAddress.requestFocus();
				return 0;
			} else if (engineType != EngineType.MAPWORLD && !service.isEmpty() && null == datasources.get(datasourceName)) {
				connectionInfo.setAlias(datasourceName);
				datasource = datasources.open(connectionInfo);
			} else {
				datasetHasOpened = true;
			}
			if (null == datasource && !datasetHasOpened) {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasourceFaild"));
				connFlag = LOAD_DATASOURCE_FAILED;
			} else if (datasetHasOpened) {
				connFlag = LOAD_DATASOURCE_SUCCESSFUL;
				UICommonToolkit.refreshSelectedDatasourceNode(datasourceName);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasourceSuccessful"));
				connFlag = LOAD_DATASOURCE_SUCCESSFUL;
				UICommonToolkit.refreshSelectedDatasourceNode(datasourceName);
			}
		} catch (Exception ex) {
			connFlag = LOAD_DATASOURCE_EXCEPTION;
			Application.getActiveApplication().getOutput().output(ex.getMessage());
			Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasetDatasourceFaild"));
		}
		return connFlag;
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
