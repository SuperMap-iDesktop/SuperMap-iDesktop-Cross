package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.TextFields.DefaultValueTextField;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 打开数据库型数据源
 *
 * @author XiaJT
 */
public class JPanelDatasourceInfoDatabase extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final int INPUT_NOT_COMPLITED = 0;
	public static final int LOAD_DATASOURCE_FAILD = 1;
	public static final int LOAD_DATASOURCE_SUCCESSFUL = 2;
	public static final int LOAD_DATASOURCE_EXCEPTION = 3;
	public static final int CREATE_DATASOURCE_SUCCESSFUL = 4;
	public static final int CREATE_DATASOURCE_FAILED = 5;
	// Variables declaration - do not modify
	private JComboBox<String> jComboBoxServer;
	private JLabel jLabelDatabaseName;
	private JLabel jLabelPassword;
	private JLabel jLabelServer;
	private JLabel jLabelUserName;
	private JLabel jLabelDatasourceAlias;
	private JLabel jLabelOpenType;
	private JLabel jLabelMaxConnPoolNum;
	private JTextField jTextFieldMaxConnPoolNum;
	private JTextField jTextFieldDatabaseName;
	private JPasswordField jPasswordField;
	private JTextField jTextFieldUser;
	private JTextField jTextFieldDatasourceAlias;
	private JCheckBox jCheckBoxReadonly;
	private ComponentDropDown.RightArrowButton jButtonDCF;
	private String datasourceAlias;

	private JLabel jLabelServerMongo;
	private WarningOrHelpProvider warningForServerMongo;
	private DefaultValueTextField jTextFieldServerMongo;
	private JLabel jLabelDatabaseNameMongo;
	private JTextField jTextFieldDatabaseNameMongo;
	private JLabel jLabelUserMongo;
	private JTextField jTextFieldUserMongo;
	private JLabel jLabelUserPasswordMongo;
	private JPasswordField jPasswordFieldMongo;

	private transient EngineType engineType;


	private JLabel jLabelEmptyServer;
	// 撑开表格
	private JLabel jLabelEmpty;
	private JLabel jLabelEmptyUser;
	private JLabel jLabelEmptyAlias;
	private int connectionFlag;
	private JPopupMenu jPopupMenu;
	private JMenuItem loadDCFItem;
	private JMenuItem saveDCFItem;
	private WarningOrHelpProvider warningForDatasourceAlias;
	private String plusSign = ControlsProperties.getString("String_PlusSign");
	private String nullStr = "";

	private FocusListener serverNameChangeListener = new FocusListener() {
		@Override
		public void focusLost(FocusEvent e) {
			jComboBoxServerChange();
		}

		@Override
		public void focusGained(FocusEvent e) {
			// 默认实现，后续进行初始化操作
		}
	};
	private KeyListener serverNameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}
	};
	private ItemListener serverNameItemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			jComboBoxServerChange();
		}
	};
	private KeyListener userNameListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyReleased(KeyEvent e) {
			jTextFieldUserNameChange();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// do nothing
		}
	};
	private FocusListener userNameFocusListener = new FocusListener() {
		@Override
		public void focusLost(FocusEvent e) {
			jTextFieldUserNameChange();
		}

		@Override
		public void focusGained(FocusEvent e) {
			// 默认实现，后续进行初始化操作
		}
	};
	private KeyListener datasourceAliasKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyReleased(KeyEvent e) {
			jTextFieldDatasourceAliasChange();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// do nothing
		}
	};
	private FocusListener datasourceAliasFocusListener = new FocusListener() {
		@Override
		public void focusLost(FocusEvent e) {
			jTextFieldDatasourceAliasChange();
		}

		@Override
		public void focusGained(FocusEvent e) {
			// 默认实现，后续进行操作
		}
	};
	private KeyListener databaseNameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}
	};
	private MouseAdapter loadDCFListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			loadDCF();

		}
	};
	private MouseAdapter showMenuListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			jPopupMenu.show(e.getComponent().getParent(), 0, e.getComponent().getParent().getHeight());
		}
	};
	private ActionListener loadDCFActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			loadDCF();
		}
	};
	private ActionListener saveDCFListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			saveDCF();
		}
	};
	private KeyAdapter mongoServerListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			jTextFieldUserNameKeyEvent();
		}
	};


	/**
	 * Creates new form JPanelWorkspaceSaveAsSQL
	 */
	public JPanelDatasourceInfoDatabase() {

	}

	private void initLayout() {
		if (engineType == EngineType.MYSQLPlus) {
			this.removeAll();
			JPanel mySqlPanel = new JPanel();
			JPanel mongoPanel = new JPanel();
			mySqlPanel.setBorder(new TitledBorder("MySQL"));
			mongoPanel.setBorder(new TitledBorder("MongoDB"));
			mySqlPanel.setLayout(new GridBagLayout());
			mySqlPanel.add(this.jLabelServer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mySqlPanel.add(this.jComboBoxServer, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			mySqlPanel.add(this.jLabelDatabaseName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mySqlPanel.add(this.jTextFieldDatabaseName, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			mySqlPanel.add(this.jLabelUserName, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mySqlPanel.add(this.jTextFieldUser, new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			mySqlPanel.add(this.jLabelPassword, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mySqlPanel.add(this.jPasswordField, new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

			mongoPanel.setLayout(new GridBagLayout());
			mongoPanel.add(this.jLabelServerMongo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mongoPanel.add(this.jTextFieldServerMongo, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			mongoPanel.add(this.jLabelDatabaseNameMongo, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mongoPanel.add(this.jTextFieldDatabaseNameMongo, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			mongoPanel.add(this.jLabelUserMongo, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mongoPanel.add(this.jTextFieldUserMongo, new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			mongoPanel.add(this.jLabelUserPasswordMongo, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			mongoPanel.add(this.jPasswordFieldMongo, new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

			this.setLayout(new GridBagLayout());
			this.add(mySqlPanel, new GridBagConstraintsHelper(0, 0, 4, 4).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 0, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 0));
			this.add(mongoPanel, new GridBagConstraintsHelper(0, 4, 4, 4).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 0, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 0));
			this.add(this.jLabelDatasourceAlias, new GridBagConstraintsHelper(0, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.warningForDatasourceAlias, new GridBagConstraintsHelper(1, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 0).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jTextFieldDatasourceAlias, new GridBagConstraintsHelper(2, 8, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelOpenType, new GridBagConstraintsHelper(0, 9, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jCheckBoxReadonly, new GridBagConstraintsHelper(2, 9, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0));
			this.add(this.jButtonDCF, new GridBagConstraintsHelper(0, 10, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 0, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(new JPanel(), new GridBagConstraintsHelper(0, 11, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 0, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		} else {
			this.removeAll();
			this.setLayout(new GridBagLayout());
			this.add(this.jLabelServer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jComboBoxServer, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelDatabaseName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jTextFieldDatabaseName, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelUserName, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jTextFieldUser, new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelPassword, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jPasswordField, new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelDatasourceAlias, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.warningForDatasourceAlias, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 0).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jTextFieldDatasourceAlias, new GridBagConstraintsHelper(2, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelMaxConnPoolNum, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jTextFieldMaxConnPoolNum, new GridBagConstraintsHelper(2, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
			this.add(this.jLabelOpenType, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			this.add(this.jCheckBoxReadonly, new GridBagConstraintsHelper(2, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0));
			this.add(new JPanel(), new GridBagConstraintsHelper(0, 7, 3, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
			this.add(this.jButtonDCF, new GridBagConstraintsHelper(0, 8, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 0, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
			boolean showMaxConnPoolNum = engineType == EngineType.POSTGRESQL || engineType == EngineType.ORACLEPLUS || engineType == EngineType.ORACLESPATIAL;
			this.jLabelMaxConnPoolNum.setVisible(showMaxConnPoolNum);
			this.jTextFieldMaxConnPoolNum.setVisible(showMaxConnPoolNum);
			this.warningForDatasourceAlias.hideWarning();
		}
	}

	public void registEvents() {
		removeEvents();
		jComboBoxServer.getEditor().getEditorComponent().addFocusListener(serverNameChangeListener);
		jComboBoxServer.getEditor().getEditorComponent().addKeyListener(serverNameKeyListener);
		jComboBoxServer.addItemListener(serverNameItemListener);
		jTextFieldUser.addKeyListener(userNameListener);
		jTextFieldUser.addFocusListener(userNameFocusListener);
		jTextFieldDatasourceAlias.addKeyListener(datasourceAliasKeyListener);
		jTextFieldDatasourceAlias.addFocusListener(datasourceAliasFocusListener);
		jTextFieldDatabaseName.addKeyListener(databaseNameKeyListener);
		jButtonDCF.labelText.addMouseListener(loadDCFListener);
		jButtonDCF.labelImage.addMouseListener(showMenuListener);
		loadDCFItem.addActionListener(loadDCFActionListener);
		saveDCFItem.addActionListener(saveDCFListener);
		if (engineType == EngineType.MYSQLPlus) {
			this.jTextFieldServerMongo.addKeyListener(mongoServerListener);
		}
	}

	private void saveDCF() {
		String moduleSaveDCF = "ModuleSaveDCF";
		if (!SmFileChoose.isModuleExist(moduleSaveDCF)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_dcfFile"), "dcf");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"), ControlsProperties.getString("String_saveAsDCF"), moduleSaveDCF, "SaveOne");
		}

		SmFileChoose fileChooserForSaveDCF = new SmFileChoose(moduleSaveDCF);
		fileChooserForSaveDCF.setSelectedFile(new File("DatabaseConnectionFile.dcf"));
		if (fileChooserForSaveDCF.showSaveDialog(JPanelDatasourceInfoDatabase.this) == JFileChooser.APPROVE_OPTION) {
			DatasourceConnectionInfo connectionInfo = getConnectionInfo();
			if (!StringUtilities.isNullOrEmpty(jTextFieldDatasourceAlias.getText())) {
				connectionInfo.setAlias(jTextFieldDatasourceAlias.getText());
			}
			String dcfPath = fileChooserForSaveDCF.getFilePath();
			if (!StringUtilities.isNullOrEmpty(dcfPath)) {
				saveDCFFile(connectionInfo, dcfPath);
			}
		}
	}

	private void saveDCFFile(DatasourceConnectionInfo connectionInfo, String dcfPath) {
		//导出数据源链接信息
		boolean result = connectionInfo.saveAsDCF(dcfPath);
		if (result) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_saveAsDCFSuccess"), dcfPath));
		} else {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_saveAsDCFFailed"), dcfPath));
		}
	}

	private void loadDCF() {
		//导入数据源链接信息
		String moduleLoadDCF = "ModuleLoadDCF";
		if (!SmFileChoose.isModuleExist(moduleLoadDCF)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_dcfFile"), "dcf");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"), ControlsProperties.getString("String_loadFromDCF"), moduleLoadDCF, "OpenOne");
		}

		SmFileChoose fileChooserForLoadDCF = new SmFileChoose(moduleLoadDCF);
		if (fileChooserForLoadDCF.showOpenDialog(JPanelDatasourceInfoDatabase.this) == JFileChooser.APPROVE_OPTION) {
			DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
			if (!StringUtilities.isNullOrEmpty(fileChooserForLoadDCF.getFilePath())) {
				boolean result = connectionInfo.loadFromDCF(fileChooserForLoadDCF.getFilePath());
				EngineType engineType = connectionInfo.getEngineType();
				if (result) {
					Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_loadDCFFileSuccess"));
					if (engineType == this.engineType) {
						if (engineType == EngineType.MYSQLPlus) {
							setMysqlPlusInfo(connectionInfo);
						} else {
							setOtherEngineTypeInfo(connectionInfo, engineType);
						}
						jCheckBoxReadonly.setSelected(connectionInfo.isReadOnly());
					} else {
						Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_ErrorEngineType"));
					}
				} else {
					Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_loadDCFFileFailed"));
				}
			}
		}
	}

	private void setOtherEngineTypeInfo(DatasourceConnectionInfo connectionInfo, EngineType engineType) {
		((JTextField) jComboBoxServer.getEditor().getEditorComponent()).setText(connectionInfo.getServer());
		jTextFieldDatabaseName.setText(connectionInfo.getDatabase());
		jTextFieldUser.setText(connectionInfo.getUser());
		jPasswordField.setText(connectionInfo.getPassword());
		jTextFieldDatasourceAlias.setText(connectionInfo.getAlias());
		jCheckBoxReadonly.setSelected(connectionInfo.isReadOnly());
		if (engineType == EngineType.ORACLEPLUS || engineType == EngineType.ORACLESPATIAL || engineType == EngineType.POSTGRESQL) {
			jTextFieldMaxConnPoolNum.setText(String.valueOf(connectionInfo.getMaxConnPoolNum()));
		}
	}

	private void setMysqlPlusInfo(DatasourceConnectionInfo connectionInfo) {
		//导入成功后设置界面
		String regexPlusSign = "\\+";
		String server = connectionInfo.getServer();
		if (!StringUtilities.isNullOrEmpty(server) && server.contains(plusSign)) {
			if (server.split(regexPlusSign).length == 1 && server.endsWith(plusSign)) {
				((JTextField) jComboBoxServer.getEditor().getEditorComponent()).setText(server.split(regexPlusSign)[0]);
			} else if (server.split(regexPlusSign).length == 1 && server.startsWith(plusSign)) {
				jTextFieldServerMongo.setText(server.split(regexPlusSign)[0]);
			} else if (server.split(regexPlusSign).length == 2) {
				String mysqlServer = server.split(regexPlusSign)[0];
				String mongoServer = server.split(regexPlusSign)[1];
				((JTextField) jComboBoxServer.getEditor().getEditorComponent()).setText(nullStr.equals(mysqlServer) ? "" : mysqlServer);
				jTextFieldServerMongo.setText(mongoServer);
			}
		}
		String dataBase = connectionInfo.getDatabase();
		if (!StringUtilities.isNullOrEmpty(dataBase) && dataBase.contains(plusSign)) {
			if (dataBase.split(regexPlusSign).length == 1 && dataBase.endsWith(plusSign)) {
				jTextFieldDatabaseName.setText(dataBase.split(regexPlusSign)[0]);
			} else if (dataBase.split(regexPlusSign).length == 1 && dataBase.startsWith(plusSign)) {
				jTextFieldDatabaseNameMongo.setText(dataBase.split(regexPlusSign)[0]);
			} else if (dataBase.split(regexPlusSign).length == 2) {
				String mysqlDatabase = dataBase.split(regexPlusSign)[0];
				String mongoDatabase = dataBase.split(regexPlusSign)[1];
				jTextFieldDatabaseName.setText(nullStr.equals(mysqlDatabase) ? "" : mysqlDatabase);
				jTextFieldDatabaseNameMongo.setText(nullStr.equals(mongoDatabase) ? "" : mongoDatabase);
			}
		}
		String user = connectionInfo.getUser();
		if (!StringUtilities.isNullOrEmpty(user) && user.contains(plusSign)) {
			if (user.split(regexPlusSign).length == 1 && user.endsWith(plusSign)) {
				jTextFieldUser.setText(user.split(regexPlusSign)[0]);
			} else if (user.split(regexPlusSign).length == 1 && user.startsWith(plusSign)) {
				jTextFieldUserMongo.setText(user.split(regexPlusSign)[0]);
			} else if (user.split(regexPlusSign).length == 2) {
				String mysqlUser = user.split(regexPlusSign)[0];
				String mongoUser = user.split(regexPlusSign)[1];
				jTextFieldUser.setText(nullStr.equals(mysqlUser) ? "" : mysqlUser);
				jTextFieldUserMongo.setText(nullStr.equals(mongoUser) ? "" : mongoUser);
			}
		}
		String password = connectionInfo.getPassword();
		if (!StringUtilities.isNullOrEmpty(password) && password.contains(plusSign)) {
			if (password.split(regexPlusSign).length == 1 && password.endsWith(plusSign)) {
				jPasswordField.setText(password.split(regexPlusSign)[0]);
			} else if (password.split(regexPlusSign).length == 1 && password.startsWith(plusSign)) {
				jPasswordFieldMongo.setText(password.split(regexPlusSign)[0]);
			} else if (password.split(regexPlusSign).length == 2) {
				String mysqlPassword = password.split(regexPlusSign)[0];
				String mongoPassword = password.split(regexPlusSign)[1];
				jPasswordField.setText(nullStr.equals(mysqlPassword) ? "" : mysqlPassword);
				jPasswordFieldMongo.setText(nullStr.equals(mongoPassword) ? "" : mongoPassword);
			}
		}
		if (!StringUtilities.isNullOrEmpty(connectionInfo.getAlias())) {
			jTextFieldDatasourceAlias.setText(connectionInfo.getAlias());
		}
	}


	public void removeEvents() {
		jComboBoxServer.getEditor().getEditorComponent().removeFocusListener(serverNameChangeListener);
		jComboBoxServer.getEditor().getEditorComponent().removeKeyListener(serverNameKeyListener);
		jComboBoxServer.removeItemListener(serverNameItemListener);
		jTextFieldUser.removeKeyListener(userNameListener);
		jTextFieldUser.removeFocusListener(userNameFocusListener);
		jTextFieldDatasourceAlias.removeKeyListener(datasourceAliasKeyListener);
		jTextFieldDatasourceAlias.removeFocusListener(datasourceAliasFocusListener);
		jTextFieldDatabaseName.removeKeyListener(databaseNameKeyListener);
		jButtonDCF.labelText.removeMouseListener(loadDCFListener);
		jButtonDCF.labelImage.removeMouseListener(showMenuListener);
		loadDCFItem.removeActionListener(loadDCFActionListener);
		saveDCFItem.removeActionListener(saveDCFListener);
		if (engineType == EngineType.MYSQLPlus) {
			this.jTextFieldServerMongo.removeKeyListener(mongoServerListener);
		}
	}
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">

	/**
	 * 公共的组件属性统一初始化
	 */
	private void initComponents() {
		if (engineType == EngineType.MYSQLPlus) {
			jLabelServerMongo = new JLabel(ControlsProperties.getString("String_Label_ServersName"));
			warningForServerMongo = new WarningOrHelpProvider(ControlsProperties.getString("String_WarningForMongoServer"), true);
			jTextFieldServerMongo = new DefaultValueTextField();
			jTextFieldServerMongo.setDefaulWarningText("localhost:27017");
			jLabelDatabaseNameMongo = new JLabel(ControlsProperties.getString("String_Label_DatabaseName"));
			jTextFieldDatabaseNameMongo = new JTextField();
			jLabelUserMongo = new JLabel(ControlsProperties.getString("String_Label_UserName"));
			jTextFieldUserMongo = new JTextField();
			jLabelUserPasswordMongo = new JLabel(ControlsProperties.getString("String_Label_UserPassword"));
			jPasswordFieldMongo = new JPasswordField();
		}
		jLabelServer = new JLabel();
		jLabelDatabaseName = new JLabel(ControlsProperties.getString("String_Label_DatabaseName"));
		jLabelUserName = new JLabel(ControlsProperties.getString("String_Label_UserName"));
		jLabelPassword = new JLabel(ControlsProperties.getString("String_Label_UserPassword"));
		jLabelDatasourceAlias = new JLabel(ControlsProperties.getString("String_Label_DatasourseAlias"));
		jLabelOpenType = new JLabel(ControlsProperties.getString("String_Label_OpenType"));
		jComboBoxServer = new JComboBox<String>();
		jLabelMaxConnPoolNum = new JLabel(ControlsProperties.getString("String_Label_MaxConnPoolCount"));
		jTextFieldMaxConnPoolNum = new JTextField("1");
		jTextFieldDatabaseName = new JTextField("");
		jTextFieldUser = new JTextField("");
		jPasswordField = new JPasswordField("");
		jTextFieldDatasourceAlias = new JTextField("");
		jCheckBoxReadonly = new JCheckBox(CoreProperties.getString("String_ReadOnly"));
		jComboBoxServer.setEditable(true);

		jLabelEmpty = new JLabel();
		jLabelEmptyAlias = new JLabel("!");
		jLabelEmptyServer = new JLabel("!");
		jLabelEmptyUser = new JLabel("!");
		jButtonDCF = new ComponentDropDown(ComponentDropDown.TEXT_TYPE).new RightArrowButton(ControlsProperties.getString("String_loadFromDCF"));
		jPopupMenu = new JPopupMenu();
		loadDCFItem = new JMenuItem(ControlsProperties.getString("String_loadFromDCF"));
		saveDCFItem = new JMenuItem(ControlsProperties.getString("String_saveAsDCF"));
		jPopupMenu.add(loadDCFItem);
		jPopupMenu.add(saveDCFItem);
		jLabelEmptyAlias.setToolTipText(ControlsProperties.getString("String_ToolTipText_AliasShouldNotEmpty"));
		jLabelEmptyUser.setToolTipText(ControlsProperties.getString("String_ToolTipText_UserNameShouldNotEmpty"));
		warningForDatasourceAlias = new WarningOrHelpProvider(ControlsProperties.getString("String_WarningForDatasourceAlias"), true);
		jLabelEmptyAlias.setVisible(false);
		jLabelEmptyServer.setVisible(false);
		jLabelEmptyUser.setVisible(false);
	}

	// End Variables
	private void setComponentName() {
		ComponentUIUtilities.setName(this.jComboBoxServer, "JPanelDatasourceInfoDatabase_jComboBoxServer");
		ComponentUIUtilities.setName(this.jLabelDatabaseName, "JPanelDatasourceInfoDatabase_jLabelDatabaseName");
		ComponentUIUtilities.setName(this.jLabelPassword, "JPanelDatasourceInfoDatabase_jLabelPassword");
		ComponentUIUtilities.setName(this.jLabelServer, "JPanelDatasourceInfoDatabase_jLabelServer");
		ComponentUIUtilities.setName(this.jLabelUserName, "JPanelDatasourceInfoDatabase_jLabelUserName");
		ComponentUIUtilities.setName(this.jLabelDatasourceAlias, "JPanelDatasourceInfoDatabase_jLabelDatasourceAlias");
		ComponentUIUtilities.setName(this.jLabelOpenType, "JPanelDatasourceInfoDatabase_jLabelOpenType");
		ComponentUIUtilities.setName(this.jTextFieldDatabaseName, "JPanelDatasourceInfoDatabase_jTextFieldDatabaseName");
		ComponentUIUtilities.setName(this.jPasswordField, "JPanelDatasourceInfoDatabase_jPasswordFieldPassword");
		ComponentUIUtilities.setName(this.jTextFieldUser, "JPanelDatasourceInfoDatabase_jTextFieldUserName");
		ComponentUIUtilities.setName(this.jTextFieldDatasourceAlias, "JPanelDatasourceInfoDatabase_jTextFieldDatasourceAlias");
		ComponentUIUtilities.setName(this.jCheckBoxReadonly, "JPanelDatasourceInfoDatabase_jCheckBoxReadonly");
		ComponentUIUtilities.setName(this.jLabelEmptyServer, "JPanelDatasourceInfoDatabase_jLabelEmptyServer");
		ComponentUIUtilities.setName(this.jLabelEmpty, "JPanelDatasourceInfoDatabase_jLabelEmpty");
		ComponentUIUtilities.setName(this.jLabelEmptyUser, "JPanelDatasourceInfoDatabase_jLabelEmptyUser");
		ComponentUIUtilities.setName(this.jLabelEmptyAlias, "JPanelDatasourceInfoDatabase_jLabelEmptyAlias");
		ComponentUIUtilities.setName(this.jButtonDCF, "JPanelDatasourceInfoDatabase_jButtonDCF");
		ComponentUIUtilities.setName(this.warningForDatasourceAlias, "JPanelDatasourceInfoDatabase_warningForDatasourceAlias");
		ComponentUIUtilities.setName(this.jLabelServerMongo, "JPanelDatasourceInfoDatabase_jLabelServerMongo");
		ComponentUIUtilities.setName(this.warningForServerMongo, "JPanelDatasourceInfoDatabase_warningForServerMongo");
		ComponentUIUtilities.setName(this.jTextFieldServerMongo, "jTextFieldServerMongo");
		ComponentUIUtilities.setName(this.jLabelDatabaseNameMongo, "JPanelDatasourceInfoDatabase_jLabelDatabaseNameMongo");
		ComponentUIUtilities.setName(this.jTextFieldDatabaseNameMongo, "JPanelDatasourceInfoDatabase_jTextFieldDatabaseNameMongo");
		ComponentUIUtilities.setName(this.jLabelUserMongo, "JPanelDatasourceInfoDatabase_jLabelUserMongo");
		ComponentUIUtilities.setName(this.jTextFieldUserMongo, "JPanelDatasourceInfoDatabase_jTextFieldUserMongo");
		ComponentUIUtilities.setName(this.jLabelUserPasswordMongo, "JPanelDatasourceInfoDatabase_jLabelUserPasswordMongo");
		ComponentUIUtilities.setName(this.jPasswordFieldMongo, "JPanelDatasourceInfoDatabase_jPasswordFieldMongo");
	}

	/**
	 * 根据引擎类型设置jTextFieldDatasourceAlias
	 *
	 * @param engineType
	 */
	private void setjTextFieldDatasourceAlias(EngineType engineType) {

		datasourceAlias = engineType.name();
		jTextFieldDatasourceAlias.setText(datasourceAlias);
	}

	/**
	 * 设置引擎类型时修改组件信息
	 *
	 * @param engineType 引擎类型
	 */
	public void setDatasourceType(EngineType engineType) {
		this.engineType = engineType;
		initComponents();
		jLabelEmptyAlias.setVisible(false);
		jLabelEmptyServer.setVisible(false);
		jLabelEmptyUser.setVisible(false);

		try {
			if (engineType == EngineType.ORACLEPLUS || engineType == EngineType.ORACLESPATIAL || engineType == EngineType.DB2) {
				jLabelServer.setText(ControlsProperties.getString("String_Label_InstanceName"));
				jLabelEmptyServer.setToolTipText(ControlsProperties.getString("String_ToolTipText_InstanceShouldNotEmpty"));
			} else {
				jLabelServer.setText(ControlsProperties.getString("String_Label_ServersName"));
				jLabelEmptyServer.setToolTipText(ControlsProperties.getString("String_ToolTipText_ServersNameShouldNotEmpty"));
			}
			jTextFieldDatabaseName.setText(null);
			jTextFieldUser.setText(null);
			jPasswordField.setText(null);
			setjTextFieldDatasourceAlias(engineType);
			jComboBoxServer.removeAllItems();
			init();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void init() {
		initLayout();
		registEvents();
		setComponentName();
	}

	private DatasourceConnectionInfo getDatasourceConnectionInfo(int connFlag) {
		DatasourceConnectionInfo connInfo = new DatasourceConnectionInfo();
		setConnectionFlag(connFlag);
		try {
			String jComboBoxServerValue = (String) jComboBoxServer.getSelectedItem();
			if (null == jComboBoxServerValue || jComboBoxServerValue.length() <= 0) {
				setConnectionFlag(INPUT_NOT_COMPLITED);
			}
			String jTextFieldUserNameValue = jTextFieldUser.getText();
			if (null == jTextFieldUserNameValue || jTextFieldUserNameValue.length() <= 0) {
				setConnectionFlag(INPUT_NOT_COMPLITED);
			}
			String jTextFieldDatasourceAliasValue = jTextFieldDatasourceAlias.getText();
			if (null == jTextFieldDatasourceAliasValue || jTextFieldDatasourceAliasValue.length() <= 0) {
				setConnectionFlag(INPUT_NOT_COMPLITED);
			}

			Datasources a = Application.getActiveApplication().getWorkspace().getDatasources();
			List<String> nameList = new ArrayList<>();
			int i = 0;
			int dataSoursesLength = a.getCount();
			for (i = 0; i < dataSoursesLength; i++) {
				nameList.add(a.get(i).getAlias());
			}
			boolean isNoSameName = false;
			while (!isNoSameName) {
				isNoSameName = true;
				for (i = 0; i < dataSoursesLength; i++) {
					if (jTextFieldDatasourceAliasValue.equals(nameList.get(i))) {
						isNoSameName = false;
						jTextFieldDatasourceAliasValue += "_1";
					}
				}
			}
			connInfo = getConnectionInfo();
			connInfo.setAlias(jTextFieldDatasourceAliasValue);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return connInfo;
	}

	private DatasourceConnectionInfo getConnectionInfo() {
		DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
		String jTextFieldDatabaseValue = jTextFieldDatabaseName.getText();
		String jPasswordFieldPasswordValue = String.valueOf(jPasswordField.getPassword());
		connectionInfo.setEngineType(engineType);
		if (engineType != EngineType.MYSQLPlus) {
			connectionInfo.setServer(((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText());
			connectionInfo.setDatabase(jTextFieldDatabaseValue);
			connectionInfo.setUser(jTextFieldUser.getText());
			connectionInfo.setPassword(jPasswordFieldPasswordValue);
			if (EngineType.SQLPLUS == engineType) {
				connectionInfo.setDriver("SQL SERVER");
			} else if ((EngineType.ORACLEPLUS == engineType || EngineType.ORACLESPATIAL == engineType || EngineType.POSTGRESQL == engineType)
					&& StringUtilities.isInteger(jTextFieldMaxConnPoolNum.getText())) {
				connectionInfo.setMaxConnPoolNum(Integer.valueOf(jTextFieldMaxConnPoolNum.getText()));
			}
		} else {
			String mongoServer = StringUtilities.isNullOrEmpty(jTextFieldServerMongo.getText()) ? jTextFieldServerMongo.getDefaulWarningText()
					: jTextFieldServerMongo.getText();
			String server = StringUtilities.isNullOrEmpty(((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText()) ? nullStr
					: (((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText());
			connectionInfo.setServer(server + plusSign + mongoServer);
			String dataBase = StringUtilities.isNullOrEmpty(jTextFieldDatabaseName.getText()) ? nullStr : jTextFieldDatabaseName.getText();
			String mongoDatabase = StringUtilities.isNullOrEmpty(jTextFieldDatabaseNameMongo.getText()) ? nullStr : jTextFieldDatabaseNameMongo.getText();
			connectionInfo.setDatabase(dataBase + plusSign + mongoDatabase);
			String user = StringUtilities.isNullOrEmpty(jTextFieldUser.getText()) ? nullStr : jTextFieldUser.getText();
			String userMongo = StringUtilities.isNullOrEmpty(jTextFieldUserMongo.getText()) ? nullStr : jTextFieldUserMongo.getText();
			connectionInfo.setUser(user + plusSign + userMongo);
			String password = StringUtilities.isNullOrEmpty(String.valueOf(jPasswordField.getPassword())) ? nullStr : String.valueOf(jPasswordField.getPassword());
			String passwordMongo = StringUtilities.isNullOrEmpty(String.valueOf(jPasswordFieldMongo.getPassword())) ? nullStr : String.valueOf(jPasswordFieldMongo.getPassword());
			connectionInfo.setPassword(password + plusSign + passwordMongo);
		}
		connectionInfo.setReadOnly(jCheckBoxReadonly.isSelected());
		return connectionInfo;
	}

	/**
	 * 加载数据源到工作空间
	 *
	 * @return
	 */
	public int loadDatasource() {
		int connFlag = -1;
		DatasourceConnectionInfo connInfo = getDatasourceConnectionInfo(connFlag);
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		boolean openFlag = false;
		for (int i = 0; i < datasources.getCount(); i++) {
			Datasource tempDatasource = datasources.get(i);
			if (connInfo == tempDatasource.getConnectionInfo()) {
				openFlag = true;
			}
		}
		if (!openFlag) {
			try {
				String resultInfo;
				// 进度条实现
				Datasource ds = datasources.open(connInfo);
				if (ds == null) {
					resultInfo = ControlsProperties.getString("String_OpenDatasourceFaild");
					connFlag = LOAD_DATASOURCE_FAILD;
				} else {
					resultInfo = ControlsProperties.getString("String_OpenDatasourceSuccessful");
					connFlag = LOAD_DATASOURCE_SUCCESSFUL;
					UICommonToolkit.refreshSelectedDatasourceNode(ds.getAlias());
				}
				Application.getActiveApplication().getOutput().output(resultInfo);
			} catch (Exception e) {
				String message = ControlsProperties.getString("String_OpenDatasetDatasourceFaild");
				Application.getActiveApplication().getOutput().output(message);
			}
		}

		return connFlag;
	}

	public int createDatasource() {
		int connFlag = -1;
		try {
			DatasourceConnectionInfo connectionInfo = getDatasourceConnectionInfo(connFlag);
			String resultInfo = "";
			Datasource newDatasource = Application.getActiveApplication().getWorkspace().getDatasources().create(connectionInfo);
			if (null == newDatasource) {
				resultInfo = "Create Datasourse Failed";
				connFlag = CREATE_DATASOURCE_FAILED;
			} else {
				resultInfo = "Create Datasource Success";
				connFlag = CREATE_DATASOURCE_SUCCESSFUL;
				UICommonToolkit.refreshSelectedDatasourceNode(newDatasource.getAlias());
			}
			Application.getActiveApplication().getOutput().output(resultInfo);
		} catch (Exception ex) {
			connFlag = LOAD_DATASOURCE_EXCEPTION;
			Application.getActiveApplication().getOutput().output(ex);
		}
		return connFlag;
	}

	/**
	 * jTextFieldDatabaseName键盘监听器，在内容改变时更改jTextFieldDatasourceAlias里面的内容
	 */
	private void jTextFieldUserNameKeyEvent() {
		String databaseName = jTextFieldDatabaseName.getText();
		String databaseSever = ((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText();
		if (engineType == EngineType.MYSQLPlus) {
			String databaseServerMongo = jTextFieldServerMongo.getText() == null ? jTextFieldServerMongo.getDefaulWarningText() : jTextFieldServerMongo.getText();
			jTextFieldDatasourceAlias.setText(databaseSever + plusSign + databaseServerMongo);
		} else {
			if (null == databaseName || databaseName.length() <= 0) {
				jTextFieldDatasourceAlias.setText(databaseSever);
			} else {
				jTextFieldDatasourceAlias.setText(databaseSever + "_" + databaseName);
			}
		}
		showWarning();
	}

	public void showWarning() {
		if (StringUtilities.isNullOrEmpty(jTextFieldDatasourceAlias.getText())) {
			warningForDatasourceAlias.showWarning();
		} else {
			warningForDatasourceAlias.hideWarning();
		}
	}

	private void jTextFieldUserNameChange() {
		String userName = jTextFieldUser.getText();
		if (null == userName || userName.length() <= 0) {
			// 默认实现，后续进行初始化操作
		} else {
			jLabelEmptyUser.setVisible(false);
		}
	}


	private void jTextFieldDatasourceAliasChange() {
		showWarning();
	}

	private void jComboBoxServerChange() {
		String server = ((JTextField) jComboBoxServer.getEditor().getEditorComponent()).getText();
		if (null == server || server.length() <= 0) {
			// 默认实现，后续进行初始化操作
		} else {
			jLabelEmptyServer.setVisible(false);
		}
	}

	public int getConnectionFlag() {
		return connectionFlag;
	}

	public void setConnectionFlag(int connectionFlag) {
		this.connectionFlag = connectionFlag;
	}

	public void isOpenDatasource(boolean reload) {
		this.jButtonDCF.setVisible(reload);
		this.jLabelOpenType.setVisible(reload);
		this.jCheckBoxReadonly.setVisible(reload);
	}
}
