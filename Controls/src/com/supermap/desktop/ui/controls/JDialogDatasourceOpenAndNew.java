package com.supermap.desktop.ui.controls;

import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogDatasourceOpenAndNew extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private int preSelectedIndex = -1;
	private final JPanel contentPanel = new JPanel();
	private javax.swing.JButton buttonCancel;
	private javax.swing.JButton buttonOk;
	private JList<Object> listDatasourceType;
	private JPanelDatasourceInfoDatabase panelDatasourceInfoDatabase;
	private JPanelDatasourceInfoWeb panelDatasourceInfoWeb;
	private transient GroupLayout gl_contentPanel;
	private DatasourceOperatorType datasourceOperatorType;
	private transient EngineType engineTypeTemp;
	private CommonListCellRenderer commonCellRender = new CommonListCellRenderer();

	// UI End of variables declaration

	/**
	 * Create the dialog.
	 *
	 * @param type
	 *            数据源类型
	 */
	public JDialogDatasourceOpenAndNew(JFrame owner, DatasourceOperatorType type) {
		super(owner);
		setModal(true);
		setBounds(100, 100, 575, 301);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		this.listDatasourceType = new JList<Object>();
		Font defaultFont = this.contentPanel.getFont();
		Font font = new Font(defaultFont.getFontName(), defaultFont.getStyle(), (int) (defaultFont.getSize() * 1.4));
		this.listDatasourceType.setFont(font);

		this.listDatasourceType.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				listWorkspaceType_ItemSelectedChanged();
			}

		});

		this.buttonOk = new JButton();
		this.getRootPane().setDefaultButton(this.buttonOk);
		this.buttonOk.setPreferredSize(new Dimension(75, 23));
		if (DatasourceOperatorType.OPENDATABASE == type) {
			this.setTitle(ControlsProperties.getString("String_Title_OpenDatabaseDataSourse"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_Open"));
		} else if (DatasourceOperatorType.NEWDATABASE == type) {
			this.setTitle(ControlsProperties.getString("String_Title_NewDatabaseDataSourse"));
			this.buttonOk.setText(ControlsProperties.getString("String_Button_Creat"));
		} else if (DatasourceOperatorType.OPENWEB == type) {
			this.setTitle(ControlsProperties.getString("String_Title_OpenWebDataSourse"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_Open"));
		}
		this.initializeDatasourceType(type);
		this.datasourceOperatorType = type;

		JPanel panel = this.getPanel(0);
		JScrollPane scrollPane = new JScrollPane();
		this.gl_contentPanel = new GroupLayout(contentPanel);
		this.gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)));
		this.gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup().addGap(78).addContainerGap(156, Short.MAX_VALUE))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE));
		scrollPane.setViewportView(listDatasourceType);
		this.contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		buttonPane.add(buttonOk);

		this.buttonCancel = new JButton();
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonCancel.setPreferredSize(new Dimension(75, 23));
		buttonPane.add(buttonCancel);

		this.buttonOk.addActionListener(okActionListener);
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButtonClicked();
			}
		});
	}

	protected void listWorkspaceType_ItemSelectedChanged() {
		try {
			int index = this.listDatasourceType.getSelectedIndex();

			JPanel existingPanel = getPanel(this.preSelectedIndex);
			JPanel newPanel = getPanel(index);
			if (null != existingPanel) {
				this.gl_contentPanel.replace(existingPanel, newPanel);
			}
			this.preSelectedIndex = index;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 根据类型初始化面板
	 *
	 * @param type
	 *            类型（NEWDATABASE/OPENDATABASE/OPENWEB）
	 */
	private void initializeDatasourceType(DatasourceOperatorType type) {
		try {
			switch (type) {
			case NEWDATABASE:
				this.listDatasourceType.setModel(getListItemForOpenOrNew());
				this.listDatasourceType.setCellRenderer(this.commonCellRender);
				break;
			case OPENDATABASE:
				this.listDatasourceType.setModel(getListItemForOpenOrNew());
				this.listDatasourceType.setCellRenderer(this.commonCellRender);
				// 暂不支持ArcSDE数据源
				break;
			case OPENWEB:
				this.listDatasourceType.setModel(getListItemForOpenWebDatasource());
				this.listDatasourceType.setCellRenderer(this.commonCellRender);
				// 暂不支持天地图
				break;
			default:
				break;
			}
			this.listDatasourceType.setSelectedIndex(0);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private DefaultListModel<Object> getListItemForOpenWebDatasource() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		DataCell ogcDataCell = new DataCell(InternalImageIconFactory.DATASOURCE_OGC, ControlsProperties.getString("String_OGC"));
		DataCell iServerRestDataCell = new DataCell(InternalImageIconFactory.DATASOURCE_ISERVERREST, ControlsProperties.getString("String_iServerRest"));
		DataCell superMapCloudDataCell = new DataCell(InternalImageIconFactory.DATASOURCE_SUPERMAPCLOUD, ControlsProperties.getString("String_SuperMapCloud"));
		DataCell googleMapsDataCell = new DataCell(InternalImageIconFactory.DATASOURCE_GOOGLEMAPS, ControlsProperties.getString("String_GoogleMaps"));
		DataCell baiduMapDataCell = new DataCell(InternalImageIconFactory.DATASOURCE_BAIDUMAPS, ControlsProperties.getString("String_BaiduMap"));
		DataCell openStreetMapsdCell = new DataCell(InternalImageIconFactory.DATASOURCE_DEFAULT, ControlsProperties.getString("String_OpenStreetMaps"));
		listModel.addElement(ogcDataCell);
		listModel.addElement(iServerRestDataCell);
		listModel.addElement(superMapCloudDataCell);
		listModel.addElement(googleMapsDataCell);
		listModel.addElement(baiduMapDataCell);
		listModel.addElement(openStreetMapsdCell);
		return listModel;
	}

	private DefaultListModel<Object> getListItemForOpenOrNew() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		DataCell sqlDataCell = new DataCell();
		sqlDataCell.initDatasourceType(EngineType.SQLPLUS, ControlsProperties.getString("String_SQL"));
		DataCell oracleDataCell = new DataCell();
		oracleDataCell.initDatasourceType(EngineType.ORACLEPLUS, ControlsProperties.getString("String_Oracle"));
		DataCell oracleSpatialDataCell = new DataCell();
		oracleSpatialDataCell.initDatasourceType(EngineType.ORACLESPATIAL, ControlsProperties.getString("String_OracleSpatial"));
		DataCell postgreSqlDataCell = new DataCell();
		postgreSqlDataCell.initDatasourceType(EngineType.POSTGRESQL, ControlsProperties.getString("String_PostgreSQL"));
		DataCell db2DataCell = new DataCell();
		db2DataCell.initDatasourceType(EngineType.DB2, ControlsProperties.getString("String_DB2"));
		DataCell dmDataCell = new DataCell();
		dmDataCell.initDatasourceType(EngineType.DM, ControlsProperties.getString("String_DM"));
		DataCell kingBaseDataCell = new DataCell();
		kingBaseDataCell.initDatasourceType(EngineType.KINGBASE, ControlsProperties.getString("String_KingBase"));
		DataCell mySqlDataCell = new DataCell();
		mySqlDataCell.initDatasourceType(EngineType.MYSQL, ControlsProperties.getString("String_MySQL"));
		if (SystemPropertyUtilties.isWindows()) {
			listModel.addElement(sqlDataCell);
			listModel.addElement(oracleDataCell);
			listModel.addElement(oracleSpatialDataCell);
			listModel.addElement(postgreSqlDataCell);
			listModel.addElement(db2DataCell);
			listModel.addElement(dmDataCell);
			listModel.addElement(kingBaseDataCell);
			listModel.addElement(mySqlDataCell);
		} else {
			listModel.addElement(oracleDataCell);
			listModel.addElement(oracleSpatialDataCell);
			listModel.addElement(postgreSqlDataCell);
			listModel.addElement(db2DataCell);
			listModel.addElement(dmDataCell);
			listModel.addElement(kingBaseDataCell);
			listModel.addElement(mySqlDataCell);
		}
		return listModel;
	}

	private JPanel getPanel(int index) {
		JPanel result = null;
		try {
			EngineType engineType = EngineType.SQLPLUS;
			if (null != datasourceOperatorType) {
				switch (datasourceOperatorType) {
				case NEWDATABASE:
					result = setCaseOpenDatabase(index);
					break;
				case OPENDATABASE:
					result = setCaseOpenDatabase(index);
					break;
				case OPENWEB:
					result = setCaseOpenWeb(index, engineType);
					break;
				default:
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private JPanel setCaseOpenDatabase(int index) {
		JPanel result;
		EngineType engineType;
		if (this.panelDatasourceInfoDatabase == null) {
			this.panelDatasourceInfoDatabase = new JPanelDatasourceInfoDatabase();
		}
		engineType = getEngineType(index);
		// 暂不支持ArcSDE
		this.panelDatasourceInfoDatabase.setDatasourceType(engineType);
		result = this.panelDatasourceInfoDatabase;
		return result;
	}

	private JPanel setCaseOpenWeb(int index, EngineType engineType) {
		engineTypeTemp = engineType;
		JPanel result;
		if (this.panelDatasourceInfoWeb == null) {
			this.panelDatasourceInfoWeb = new JPanelDatasourceInfoWeb();
		}
		switch (index) {
		case 0: // OGC
			engineTypeTemp = EngineType.OGC;
			break;
		case 1: // iServerRest
			engineTypeTemp = EngineType.ISERVERREST;
			break;
		case 2: // SuperMapCloud
			engineTypeTemp = EngineType.SUPERMAPCLOUD;
			break;
		case 3: // GoogleMaps
			engineTypeTemp = EngineType.GOOGLEMAPS;
			break;
		case 4: // BaiduMap
			engineTypeTemp = EngineType.BAIDUMAPS;
			break;
		case 5: // OpenStreetMaps
			engineTypeTemp = EngineType.OPENSTREETMAPS;
			break;
		default:
			break;
		}

		this.panelDatasourceInfoWeb.setDatasourceType(engineTypeTemp);
		result = this.panelDatasourceInfoWeb;
		return result;
	}

	private EngineType getEngineType(int index) {
		EngineType engineType = null;
		if (SystemPropertyUtilties.isWindows()) {
			switch (index) {
			case 0: // SQL
				engineType = EngineType.SQLPLUS;
				break;
			case 1: // Oracle
				engineType = EngineType.ORACLEPLUS;
				break;
			case 2: // OracleSpatial
				engineType = EngineType.ORACLESPATIAL;
				break;
			case 3: // PostgreSQL
				engineType = EngineType.POSTGRESQL;
				break;
			case 4: // DB2
				engineType = EngineType.DB2;
				break;
			case 5: // DM
				engineType = EngineType.DM;
				break;
			case 6: // KingBase
				engineType = EngineType.KINGBASE;
				break;
			case 7: // MySQL
				engineType = EngineType.MYSQL;
				break;
			default:
				break;
			}
		} else {
			switch (index) {
			case 0: // ORACLEPLUS
				engineType = EngineType.ORACLEPLUS;
				break;
			case 1: // ORACLESPATIAL
				engineType = EngineType.ORACLESPATIAL;
				break;
			case 2: // POSTGRESQL
				engineType = EngineType.POSTGRESQL;
				break;
			case 3: // DB2
				engineType = EngineType.DB2;
				break;
			case 4: // DM
				engineType = EngineType.DM;
				break;
			case 5: // KINGBASE
				engineType = EngineType.KINGBASE;
				break;
			case 6: // MYSQL
				engineType = EngineType.MYSQL;
				break;
			default:
				break;
			}
		}
		return engineType;
	}

	private transient ActionListener okActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			okButtonClicked();
		}
	};

	/**
	 * OK按钮点击事件， 点击时调用面板的加载数据源方法。成功加载时调用关闭函数。
	 */
	private void okButtonClicked() {
		int openFlag = -1;
		try {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			if (DatasourceOperatorType.OPENDATABASE == this.datasourceOperatorType) {
				// 打开数据库型数据源

				openFlag = this.panelDatasourceInfoDatabase.loadDatasource();

				if (JPanelDatasourceInfoDatabase.LOAD_DATASOURCE_SUCCESSFUL == openFlag) {
					this.CloseDialog();
				}
			} else if (DatasourceOperatorType.NEWDATABASE == this.datasourceOperatorType) {
				// 新建数据库型数据源
				openFlag = this.panelDatasourceInfoDatabase.createDatasource();
				if (JPanelDatasourceInfoDatabase.CREATE_DATASOURCE_SUCCESSFUL == openFlag) {
					this.CloseDialog();
				}
			} else if (DatasourceOperatorType.OPENWEB == this.datasourceOperatorType) {
				// 打开web型数据源
				openFlag = this.panelDatasourceInfoWeb.loadDatasource();
				if (JPanelDatasourceInfoWeb.LOAD_DATASOURCE_SUCCESSFUL == openFlag) {
					this.CloseDialog();
				}
			}
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * 返回按钮点击事件， 点击时调用关闭函数。
	 *
	 * @see #CloseDialog()
	 */
	private void cancelButtonClicked() {
		this.CloseDialog();
	}

	/**
	 * 关闭对话框
	 */
	private void CloseDialog() {
		this.dispose();
	}

}
