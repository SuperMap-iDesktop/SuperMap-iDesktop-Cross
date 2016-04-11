package com.supermap.desktop.netservices.iserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CancellationException;

public class JDialogServerRelease extends SmDialog implements ActionListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOCALHOST = "localhost";

	private JRadioButton radioButtonLocalHost;
	private JRadioButton radioButtonRemoteHost;
	private JLabel labelServer;
	private JTextField textFieldHost;
	private JLabel labelColon;
	private JTextField textFieldPort;

	private JLabel labelUserName;
	private JLabel labelPassword;
	private JTextField textFieldUserName;
	private JPasswordField textFieldPassword;

	private JCheckBox checkBoxRestData;
	private JCheckBox checkBoxRestRealspace;
	private JCheckBox checkBoxRestMap;
	private JCheckBox checkBoxRestTransAnalyst;
	private JCheckBox checkBoxRestSpatialAnalyst;

	private JCheckBox checkBoxWCS111;
	private JCheckBox checkBoxWMS111;
	private JCheckBox checkBoxWCS112;
	private JCheckBox checkBoxWMS130;
	private JCheckBox checkBoxWFS100;
	private JCheckBox checkBoxWMTS100;
	private JCheckBox checkBoxWPS100;
	private JCheckBox checkBoxWMTSCHINA;
	private JCheckBox checkBoxIsEditable;

	private SmButton buttonRelease;
	private SmButton buttonClose;

	private WorkspaceInfo workspaceInfo;
	private int servicesType;
	private boolean isEditable;
	private String remoteHost;
	private String port;
	private String adminName;
	private String adminPassword;
	private int hostType;
	// 能否发布：目前，只有文件型工作空间+文件型数据源发布到远程服务器，并且它们不在同一个目录下时，为false
	private boolean canRelease;

	private DocumentListener textFieldHostDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldHostChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldHostChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldHostChange();
		}
	};

	private DocumentListener textFieldPortDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldPortChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldPortChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldPortChange();
		}
	};

	private DocumentListener textFieldUserNameDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldUserNameChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldUserNameChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldUserNameChange();
		}
	};

	private DocumentListener textFieldPasswordDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldPasswordChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldPasswordChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldPasswordChange();
		}
	};

	/**
	 * Create the dialog.
	 */
	public JDialogServerRelease(WorkspaceInfo workspaceInfo) {
		initializeComponents();
		initializeResources();

		this.workspaceInfo = workspaceInfo;
		this.canRelease = true;

		initializeParameters();
		initializeControls();
		registerEvents();
		setButtonReleaseEnabled();
		this.componentList.add(buttonRelease);
		this.componentList.add(buttonClose);
		this.setFocusTraversalPolicy(policy);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.radioButtonLocalHost) {
			radioButtonLocalHostSelectedChange();
		} else if (e.getSource() == this.radioButtonRemoteHost) {
			radioButtonRemoteHostSelectedChange();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonRelease) {
			buttonReleaseClicked();
		} else if (e.getSource() == this.buttonClose) {
			buttonCloseClicked();
		} else if (e.getSource() == this.checkBoxWPS100) {
			checkBoxWPS100CheckChange();
		} else if (e.getSource() == this.checkBoxWMTS100) {
			checkBoxWMTS100CheckChange();
		} else if (e.getSource() == this.checkBoxWMTSCHINA) {
			checkBoxWMTSCHINACheckChange();
		} else if (e.getSource() == this.checkBoxWMS130) {
			checkBoxWMS130CheckChange();
		} else if (e.getSource() == this.checkBoxWMS111) {
			checkBoxWMS111CheckChange();
		} else if (e.getSource() == this.checkBoxWFS100) {
			checkBoxWFS100CheckChange();
		} else if (e.getSource() == this.checkBoxWCS112) {
			checkBoxWCS112CheckChange();
		} else if (e.getSource() == this.checkBoxWCS111) {
			checkBoxWCS111CheckChange();
		} else if (e.getSource() == this.checkBoxRestData) {
			checkBoxRestDataCheckChange();
		} else if (e.getSource() == this.checkBoxIsEditable) {
			checkBoxIsEditableCheckChange();
		} else if (e.getSource() == this.checkBoxRestMap) {
			checkBoxRestMapCheckChange();
		} else if (e.getSource() == this.checkBoxRestRealspace) {
			checkBoxRestRealspaceCheckChange();
		} else if (e.getSource() == this.checkBoxRestSpatialAnalyst) {
			checkBoxRestSpatialAnalystCheckChange();
		} else if (e.getSource() == this.checkBoxRestTransAnalyst) {
			checkBoxRestTransAnalystCheckChange();
		}
	}

	private void initializeComponents() {

		// 服务器设置面板
		JPanel panelService = new JPanel();
		panelService.setBorder(BorderFactory.createTitledBorder(NetServicesProperties.getString("String_iServer_Panel_Services")));
		this.radioButtonLocalHost = new JRadioButton("LocalHost");
		this.radioButtonRemoteHost = new JRadioButton("RemoteHost");
		this.labelServer = new JLabel("Server:");
		this.textFieldHost = new JTextField("localhost");
		this.labelColon = new JLabel(":");
		this.textFieldPort = new JTextField("8090");
		this.textFieldPort.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.radioButtonLocalHost);
		buttonGroup.add(this.radioButtonRemoteHost);

		GridBagLayout gbl_panelService = new GridBagLayout();
		panelService.setLayout(gbl_panelService);

		panelService.add(this.radioButtonLocalHost, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,
				10, 0, 0), 0, 0));
		panelService.add(this.radioButtonRemoteHost, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,
				5, 0, 0), 0, 0));
		panelService.add(this.labelServer, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 10, 0),
				0, 0));
		panelService.add(this.textFieldHost, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 10, 0), 0, 0));
		panelService.add(this.labelColon, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),
				0, 0));
		panelService.add(this.textFieldPort, new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 10, 10), 0, 0));

		// 用户设置面板
		JPanel panelUser = new JPanel();
		panelUser.setBorder(BorderFactory.createTitledBorder(NetServicesProperties.getString("String_iServer_Panel_Admin")));
		this.labelUserName = new JLabel("UserName:");
		this.textFieldUserName = new JTextField();
		this.textFieldUserName.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.labelPassword = new JLabel("Password:");
		this.textFieldPassword = new JPasswordField();
		this.textFieldPassword.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		GridBagLayout gbl_panelUser = new GridBagLayout();
		panelUser.setLayout(gbl_panelUser);
		panelUser.add(this.labelUserName, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0),
				0, 0));
		panelUser.add(this.textFieldUserName, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 0,
				10), 0, 0));
		panelUser.add(this.labelPassword, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 10, 0),
				0, 0));
		panelUser.add(this.textFieldPassword, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 10,
				10), 0, 0));

		// Rest服务类型设置面板
		JPanel panelRestService = new JPanel();
		panelRestService.setBorder(BorderFactory.createTitledBorder(NetServicesProperties.getString("String_iServer_Panel_RestServices")));
		this.checkBoxRestData = new JCheckBox("RestData");
		this.checkBoxRestRealspace = new JCheckBox("Realspace");
		this.checkBoxRestMap = new JCheckBox("RestMap");
		this.checkBoxRestTransAnalyst = new JCheckBox("RestTransAnalyst");
		this.checkBoxRestSpatialAnalyst = new JCheckBox("RestSpatialAnalyst");

		GridBagLayout gbl_panelRestService = new GridBagLayout();
		panelRestService.setLayout(gbl_panelRestService);
		panelRestService.add(this.checkBoxRestData, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,
				10, 0, 0), 0, 0));
		panelRestService.add(this.checkBoxRestRealspace, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
				10, 5, 0, 10), 0, 0));
		panelRestService.add(this.checkBoxRestMap, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelRestService.add(this.checkBoxRestTransAnalyst, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 0, 10), 0, 0));
		panelRestService.add(this.checkBoxRestSpatialAnalyst, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 10, 10, 0), 0, 0));

		// OGC服务类型设置面板
		JPanel panelOGCService = new JPanel();
		panelOGCService.setBorder(BorderFactory.createTitledBorder(NetServicesProperties.getString("String_iServer_Panel_OGCServices")));
		this.checkBoxWCS111 = new JCheckBox("WCS1.1.1");
		this.checkBoxWMS111 = new JCheckBox("WMS1.1.1");
		this.checkBoxWCS112 = new JCheckBox("WCS1.1.2");
		this.checkBoxWMS130 = new JCheckBox("WMS1.3.0");
		this.checkBoxWFS100 = new JCheckBox("WFS1.0.0");
		this.checkBoxWMTS100 = new JCheckBox("WMTS1.0.0");
		this.checkBoxWPS100 = new JCheckBox("WPS1.0.0");
		this.checkBoxWMTSCHINA = new JCheckBox("WMTS-CHINA");

		GridBagLayout gbl_panelOGCService = new GridBagLayout();
		panelOGCService.setLayout(gbl_panelOGCService);
		panelOGCService.add(this.checkBoxWCS111, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMS111, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5,
				0, 10), 0, 0));
		panelOGCService.add(this.checkBoxWCS112, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMS130, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0,
				10), 0, 0));
		panelOGCService.add(this.checkBoxWFS100, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMTS100, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5,
				0, 10), 0, 0));
		panelOGCService.add(this.checkBoxWPS100, new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10,
				0, 0), 0, 0));
		panelOGCService.add(this.checkBoxWMTSCHINA, new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5,
				10, 10), 0, 0));

		// 附加设置面板
		JPanel panelAdditionalSetting = new JPanel();
		panelAdditionalSetting.setBorder(BorderFactory.createTitledBorder(NetServicesProperties.getString("String_iServer_Panel_Setting")));
		this.checkBoxIsEditable = new JCheckBox("IsEditable");

		GridBagLayout gbl_panelAdditionalSetting = new GridBagLayout();
		panelAdditionalSetting.setLayout(gbl_panelAdditionalSetting);
		panelAdditionalSetting.add(this.checkBoxIsEditable, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(10, 10, 10, 10), 0, 0));

		// 主面板
		JPanel panelMain = new JPanel();
		setContentPane(panelMain);
		this.buttonRelease = new SmButton("Release");
		this.buttonClose = new SmButton("Close");

		GridBagLayout gbl_panelMain = new GridBagLayout();
		setLayout(gbl_panelMain);
		panelMain
				.add(panelService, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		panelMain.add(panelUser, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 0, 0));
		panelMain.add(panelRestService, new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 0,
				0));
		panelMain.add(panelOGCService, new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 0,
				0));
		panelMain.add(panelAdditionalSetting, new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0,
				10), 0, 0));
		panelMain.add(this.buttonRelease, new GridBagConstraints(0, 5, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 10, 0),
				0, 0));
		panelMain.add(this.buttonClose, new GridBagConstraints(1, 5, 1, 1, 0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 10, 10), 0,
				0));

		setSize(new Dimension(450, 650));
		setLocationRelativeTo(null);
	}

	private void initializeResources() {
		this.setTitle(NetServicesProperties.getString("String_iServer_ServerRelease"));
		this.radioButtonLocalHost.setText(NetServicesProperties.getString("String_iServer_RadioButton_LocalHost"));
		this.radioButtonRemoteHost.setText(NetServicesProperties.getString("String_iServer_RadioButton_RemoteHost"));
		this.labelServer.setText(NetServicesProperties.getString("String_iServer_Label_Server"));
		this.labelUserName.setText(NetServicesProperties.getString("String_iServer_Label_UserName"));
		this.labelPassword.setText(NetServicesProperties.getString("String_iServer_Label_UserPassword"));
		this.checkBoxRestData.setText(NetServicesProperties.getString("String_iServer_ServicesType_RestData"));
		this.checkBoxRestRealspace.setText(NetServicesProperties.getString("String_iServer_ServicesType_RestRealspace"));
		this.checkBoxRestMap.setText(NetServicesProperties.getString("String_iServer_ServicesType_RestMap"));
		this.checkBoxRestTransAnalyst.setText(NetServicesProperties.getString("String_iServer_ServicesType_RestTransprotationAnalyst"));
		this.checkBoxRestSpatialAnalyst.setText(NetServicesProperties.getString("String_iServer_ServicesType_RestSpatialAnalyst"));
		this.checkBoxWCS111.setText(NetServicesProperties.getString("String_iServer_ServicesType_WCS111"));
		this.checkBoxWMS111.setText(NetServicesProperties.getString("String_iServer_ServicesType_WMS111"));
		this.checkBoxWCS112.setText(NetServicesProperties.getString("String_iServer_ServicesType_WCS112"));
		this.checkBoxWMS130.setText(NetServicesProperties.getString("String_iServer_ServicesType_WMS130"));
		this.checkBoxWFS100.setText(NetServicesProperties.getString("String_iServer_ServicesType_WFS100"));
		this.checkBoxWMTS100.setText(NetServicesProperties.getString("String_iServer_ServicesType_WMTS100"));
		this.checkBoxWPS100.setText(NetServicesProperties.getString("String_iServer_ServicesType_WPS100"));
		this.checkBoxWMTSCHINA.setText(NetServicesProperties.getString("String_iServer_ServicesType_WMTSCHINA"));
		this.checkBoxIsEditable.setText(CommonProperties.getString(CommonProperties.IsEditable));
		this.buttonRelease.setText(NetServicesProperties.getString("String_Release"));
		this.buttonClose.setText(CommonProperties.getString(CommonProperties.Close));
	}

	private void registerEvents() {
		this.radioButtonLocalHost.addItemListener(this);
		this.radioButtonRemoteHost.addItemListener(this);
		this.textFieldHost.getDocument().addDocumentListener(this.textFieldHostDocumentListener);
		this.textFieldPort.getDocument().addDocumentListener(this.textFieldPortDocumentListener);
		this.textFieldUserName.getDocument().addDocumentListener(this.textFieldUserNameDocumentListener);
		this.textFieldPassword.getDocument().addDocumentListener(this.textFieldPasswordDocumentListener);
		this.checkBoxRestData.addActionListener(this);
		this.checkBoxRestRealspace.addActionListener(this);
		this.checkBoxRestMap.addActionListener(this);
		this.checkBoxRestTransAnalyst.addActionListener(this);
		this.checkBoxRestSpatialAnalyst.addActionListener(this);
		this.checkBoxWCS111.addActionListener(this);
		this.checkBoxWMS111.addActionListener(this);
		this.checkBoxWCS112.addActionListener(this);
		this.checkBoxWMS130.addActionListener(this);
		this.checkBoxWFS100.addActionListener(this);
		this.checkBoxWMTS100.addActionListener(this);
		this.checkBoxWPS100.addActionListener(this);
		this.checkBoxWMTSCHINA.addActionListener(this);
		this.checkBoxIsEditable.addActionListener(this);
		this.buttonRelease.addActionListener(this);
		this.buttonClose.addActionListener(this);
	}

	// 发布文件型工作空间到远程服务器还没有API
	// 因此先处理为文件型的时候，远程选项不可用
	// 文件型工作空间+文件型数据源，需要工作空间和数据源在同一个目录下，打包上传
	// 文件型工作空间+数据库型数据源，只需要上传文件型工作空间
	private boolean canRemoteRelease() {
		Boolean canRemoteRelease = true;
		try {
			if (!StringUtilties.isNullOrEmpty(this.workspaceInfo.getWorkspacePath())) {
				File workspaceDirectory = new File(workspaceInfo.getWorkspacePath()).getParentFile(); // 工作空间所在路径

				ArrayList<String> datasourcesPath = this.workspaceInfo.getDatasourcesPath();
				for (int i = 0; i < datasourcesPath.size(); i++) {
					File datasourceDirectory = new File(datasourcesPath.get(i)).getParentFile(); // 数据源所在的路径
					if (!workspaceDirectory.equals(datasourceDirectory)) {
						canRemoteRelease = false;
						break;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return canRemoteRelease;
	}

	// 根据工作空间的数据类型默认发布超图规范的REST服务
	// OGC服务默认不勾选
	private void initializeParameters() {
		try {
			this.isEditable = false;
			this.port = "8090";
			this.remoteHost = "";
			this.adminName = "";
			this.adminPassword = "";
			this.hostType = HostType.LOCAL;

			this.servicesType = ServiceType.NONE;
			if (this.workspaceInfo != null) {
				if (this.workspaceInfo.getContainTypes().size() > 0) {
					this.servicesType = this.servicesType | ServiceType.RESTDATA;
				}

				if (this.workspaceInfo.getMapCounts() > 0) {
					this.servicesType = this.servicesType | ServiceType.RESTMAP;
				}

				if (this.workspaceInfo.getSceneCounts() > 0) {
					this.servicesType = this.servicesType | ServiceType.RESTREALSPACE;
				}

				if (ListUtilties.isListContainAny(this.workspaceInfo.getContainTypes(), DatasetType.CAD, DatasetType.GRID, DatasetType.LINE, DatasetType.LINEM,
						DatasetType.NETWORK, DatasetType.POINT, DatasetType.REGION, DatasetType.TABULAR)) {
					this.servicesType = this.servicesType | ServiceType.RESTSPATIALANALYST;
				}

				if (this.workspaceInfo.getContainTypes().contains(DatasetType.NETWORK)) {
					this.servicesType = this.servicesType | ServiceType.RESTTRANSPORTATIONANALYST;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initializeControls() {
		initializeUser();
		initializeServicesTypes();
		initializeHostType();
		initializeHostValue();
	}

	// 根据工作空间的数据类型默认发布超图规范的REST服务
	// OGC服务默认不勾选
	private void initializeServicesTypes() {
		try {
			this.checkBoxRestData.setEnabled(false);
			this.checkBoxIsEditable.setEnabled(false);
			this.checkBoxRestMap.setEnabled(false);
			this.checkBoxRestRealspace.setEnabled(false);
			this.checkBoxRestSpatialAnalyst.setEnabled(false);
			this.checkBoxRestTransAnalyst.setEnabled(false);

			if ((this.servicesType & ServiceType.RESTDATA) == ServiceType.RESTDATA) {
				this.checkBoxRestData.setEnabled(true);
				this.checkBoxRestData.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTMAP) == ServiceType.RESTMAP) {
				this.checkBoxRestMap.setEnabled(true);
				this.checkBoxRestMap.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTREALSPACE) == ServiceType.RESTREALSPACE) {
				this.checkBoxRestRealspace.setEnabled(true);
				this.checkBoxRestRealspace.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTSPATIALANALYST) == ServiceType.RESTSPATIALANALYST) {
				this.checkBoxRestSpatialAnalyst.setEnabled(true);
				this.checkBoxRestSpatialAnalyst.setSelected(true);
			}
			if ((this.servicesType & ServiceType.RESTTRANSPORTATIONANALYST) == ServiceType.RESTTRANSPORTATIONANALYST) {
				this.checkBoxRestTransAnalyst.setEnabled(true);
				this.checkBoxRestTransAnalyst.setSelected(true);
			}
			setCheckBoxEditableState();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void setCheckBoxEditableState() {
		if ((this.servicesType & ServiceType.RESTDATA) == ServiceType.RESTDATA || (this.servicesType & ServiceType.WFS100) == ServiceType.WFS100
				|| (this.servicesType & ServiceType.WCS111) == ServiceType.WCS111 || (this.servicesType & ServiceType.WCS112) == ServiceType.WCS112) {
			this.checkBoxIsEditable.setEnabled(true);
			this.checkBoxIsEditable.setSelected(this.isEditable);
		} else {
			this.checkBoxIsEditable.setEnabled(false);
			this.checkBoxIsEditable.setSelected(false);
		}
	}

	private void initializeHostType() {
		try {
			if (this.hostType == HostType.LOCAL) {
				this.radioButtonLocalHost.setSelected(true);
			} else if (this.hostType == HostType.REMOTE) {
				this.radioButtonRemoteHost.setSelected(true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initializeHostValue() {
		try {
			if (this.hostType == HostType.LOCAL) {
				this.textFieldHost.setText(LOCALHOST);
				this.textFieldHost.setEditable(false);
			} else if (this.hostType == HostType.REMOTE) {
				this.textFieldHost.setText(this.remoteHost);
				this.textFieldHost.setEditable(true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initializeUser() {
		this.textFieldUserName.setText(this.adminName);
		this.textFieldPassword.setText(this.adminPassword);
	}

	private void setButtonReleaseEnabled() {
		try {
			if (!this.canRelease || this.servicesType == ServiceType.NONE || StringUtilties.isNullOrEmpty(this.adminName)
					|| StringUtilties.isNullOrEmpty(this.adminPassword) || StringUtilties.isNullOrEmpty(this.port)
					|| (this.hostType == HostType.REMOTE && StringUtilties.isNullOrEmpty(this.remoteHost))) {
				this.buttonRelease.setEnabled(false);
			} else {
				this.buttonRelease.setEnabled(true);
				getRootPane().setDefaultButton(this.buttonRelease);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void textFieldPasswordChange() {
		this.adminPassword = new String(this.textFieldPassword.getPassword());
		setButtonReleaseEnabled();
	}

	private void textFieldUserNameChange() {
		this.adminName = this.textFieldUserName.getText();
		setButtonReleaseEnabled();
	}

	private void textFieldHostChange() {
		try {
			if (this.hostType == HostType.REMOTE) {
				this.remoteHost = this.textFieldHost.getText().trim();
				if (this.remoteHost.toLowerCase().startsWith("http://")) {
					this.remoteHost = this.remoteHost.toLowerCase().substring(7);
				}
				setButtonReleaseEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void textFieldPortChange() {
		try {
			this.port = this.textFieldPort.getText();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWPS100CheckChange() {
		try {
			if (this.checkBoxWPS100.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WPS100;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WPS100;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMTSCHINACheckChange() {
		try {
			if (this.checkBoxWMTSCHINA.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMTSCHINA;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMTSCHINA;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMTS100CheckChange() {
		try {
			if (this.checkBoxWMTS100.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMTS100;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMTS100;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMS130CheckChange() {
		try {
			if (this.checkBoxWMS130.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMS130;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMS130;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWMS111CheckChange() {
		try {
			if (this.checkBoxWMS111.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WMS111;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WMS111;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWFS100CheckChange() {
		try {
			if (this.checkBoxWFS100.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WFS100;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WFS100;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWCS112CheckChange() {
		try {
			if (this.checkBoxWCS112.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WCS112;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WCS112;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxWCS111CheckChange() {
		try {
			if (this.checkBoxWCS111.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.WCS111;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.WCS111;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestDataCheckChange() {
		try {
			if (this.checkBoxRestData.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTDATA;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTDATA;
			}

			setCheckBoxEditableState();
			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxIsEditableCheckChange() {
		this.isEditable = this.checkBoxIsEditable.isSelected();
		setButtonReleaseEnabled();
	}

	private void checkBoxRestMapCheckChange() {
		try {
			if (this.checkBoxRestMap.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTMAP;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTMAP;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestRealspaceCheckChange() {
		try {
			if (this.checkBoxRestRealspace.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTREALSPACE;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTREALSPACE;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestSpatialAnalystCheckChange() {
		try {
			if (this.checkBoxRestSpatialAnalyst.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTSPATIALANALYST;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTSPATIALANALYST;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxRestTransAnalystCheckChange() {
		try {
			if (this.checkBoxRestTransAnalyst.isSelected()) {
				this.servicesType = this.servicesType | ServiceType.RESTTRANSPORTATIONANALYST;
			} else {
				this.servicesType = this.servicesType ^ ServiceType.RESTTRANSPORTATIONANALYST;
			}

			setButtonReleaseEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonLocalHostSelectedChange() {
		try {
			if (this.radioButtonLocalHost.isSelected()) {
				this.hostType = HostType.LOCAL;
				initializeHostValue();
				this.canRelease = true;
				setButtonReleaseEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonRemoteHostSelectedChange() {
		try {
			if (this.radioButtonRemoteHost.isSelected()) {
				this.hostType = HostType.REMOTE;
				initializeHostValue();

				if (!canRemoteRelease()) {
					this.canRelease = false;
					Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_Remote_Forbidden"));
				} else {
					this.canRelease = true;
				}
				setButtonReleaseEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void buttonReleaseClicked() {
		CursorUtilties.setWaitCursor();
		try {
			ServerRelease serverRelease = FillServerRelease();
			// 需要打包上传，需要展示将要打包的数据
			if (!StringUtilties.isNullOrEmpty(serverRelease.getWorkspacePath()) && serverRelease.getHostType() == HostType.REMOTE) {
				ArrayList<SelectableFile> files = new ArrayList<>();
				File workDirectory = new File(serverRelease.getWorkDirectory());
				File[] childFiles = workDirectory.listFiles();
				for (int i = 0; i < childFiles.length; i++) {
					files.add(SelectableFile.fromFile(childFiles[i], !childFiles[i].isHidden()));
				}
				JDialogFolderSelector folderSelector = new JDialogFolderSelector(files, serverRelease.getWorkspacePath());
				if (folderSelector.showDialog() == DialogResult.OK) {
					serverRelease.getFiles().clear();

					for (int i = 0; i < files.size(); i++) {
						SelectableFile selectableFile = files.get(i);
						if (!selectableFile.isSelected()) {
							continue;
						}

						serverRelease.getFiles().add(selectableFile);
					}
				} else {
					Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_Operation_Cancel"));
					return;
				}
			}

			FormProgressTotal formProgressTotal = new FormProgressTotal();
			formProgressTotal.doWork(new ServerReleaseCallable(serverRelease), new IAfterWork<Boolean>() {

				@Override
				public void afterWork(Boolean param) {
					if (param) {
						if (SwingUtilities.isEventDispatchThread()) {
							JDialogServerRelease.this.setVisible(false);
						} else {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									JDialogServerRelease.this.setVisible(false);
								}
							});
						}
					}
				}
			});
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}

	private void buttonCloseClicked() {
		try {
			setDialogResult(DialogResult.CANCEL);
			setVisible(false);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private ServerRelease FillServerRelease() {
		ServerRelease serverRelease = new ServerRelease();
		serverRelease.setHostType(this.hostType);
		if (this.hostType == HostType.LOCAL) {
			serverRelease.setHost(LOCALHOST);
		} else if (this.hostType == HostType.REMOTE) {
			serverRelease.setHost(this.remoteHost);
		}
		serverRelease.setPort(this.port);
		serverRelease.setServicesType(this.servicesType);
		serverRelease.setAdminName(this.adminName);
		serverRelease.setAdminPassword(this.adminPassword);
		serverRelease.setEditable(this.isEditable);
		serverRelease.setConnectionInfo(this.workspaceInfo.getWorkspaceConnectionInfo());
		serverRelease.setWorkspacePath(this.workspaceInfo.getWorkspacePath());
		// serverRelease.DatasourcesPath = this.m_workspaceInfo.DatasourcesPath;

		return serverRelease;
	}

	private class ServerReleaseCallable extends UpdateProgressCallable {

		private ServerRelease serverRelease;
		private FunctionProgressListener functionProgressListener = new FunctionProgressListener() {

			@Override
			public void functionProgress(FunctionProgressEvent event) {
				try {
					updateProgressTotal(event.getCurrentProgress(), event.getCurrentMessage(), event.getTotalProgress(), event.getTotalMessage());
				} catch (CancellationException e) {
					event.setCancel(true);
				}
			}
		};

		public ServerReleaseCallable(ServerRelease serverRelease) {
			this.serverRelease = serverRelease;
		}

		@Override
		public Boolean call() throws Exception {
			boolean result = true;
			try {
				// 发布服务开始，给出提示，并开始统计时间
				Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_iServer_Message_ReleaseStart"));
				Date startTime = new Date();
				this.serverRelease.addFunctionProgressListener(this.functionProgressListener);

				boolean releaseResult = this.serverRelease.release();
				if (releaseResult) {
					serverReleaseSetting();
					long totalTime = (new Date().getTime()) - startTime.getTime(); // 单位毫秒
					Application
							.getActiveApplication()
							.getOutput()
							.output(MessageFormat.format(NetServicesProperties.getString("String_iServer_Message_ReleaseSuccess"),
									String.valueOf(totalTime / 1000)));
					Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_iServer_Message_ReleaseSuccessResult"));

					ArrayList<String> resultServers = getResultServers(this.serverRelease.getResultURL());
					for (int i = 0; i < resultServers.size(); i++) {
						Application.getActiveApplication().getOutput().output(resultServers.get(i));
					}
				} else {
					result = false;
					Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_iServer_Message_ReleaseFaild"));
				}
			} catch (Exception e) {
				result = false;
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				this.serverRelease.removeFunctionProgressListener(this.functionProgressListener);
				ServerRelease.clearTmp();
			}
			return result;
		}

		private void serverReleaseSetting() {
			if (JDialogServerRelease.this.hostType == HostType.REMOTE) {
				// ServerReleaseSetting setting = new ServerReleaseSetting();
				// setting.RemoteHost = this.m_remoteHost;
				// setting.Save();
			}
		}

		private ArrayList<String> getResultServers(String links) {
			ArrayList<String> result = new ArrayList<>();
			try {
				Object obj = JSON.parse(links);
				if (obj instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) obj;

					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						if (jsonObject.containsKey(JsonKey.ReleaseWorkspaceResponse.SERVICE_TYPE)
								&& jsonObject.containsKey(JsonKey.ReleaseWorkspaceResponse.SERVICE_ADDRESS)) {
							result.add(ServiceType.serviceTypeLocalized(jsonObject.get(JsonKey.ReleaseWorkspaceResponse.SERVICE_TYPE).toString()) + ":"
									+ jsonObject.get(JsonKey.ReleaseWorkspaceResponse.SERVICE_ADDRESS) + ";");
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return result;
		}
	}

	@Override
	public void escapePressed() {
		buttonCloseClicked();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.buttonRelease) {
			buttonReleaseClicked();
		}
		if (this.getRootPane().getDefaultButton() == this.buttonClose) {
			buttonCloseClicked();
		}
	}
}
