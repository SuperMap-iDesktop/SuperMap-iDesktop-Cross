package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.ComboBoxCellEditor;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilties.CharsetUtilties;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class JDialogDatasetNew extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_INDEX_INDEX = 0;
	private static final int COLUMN_INDEX_TARGETDATASOURCE = 1;
	private static final int COLUMN_INDEX_DatasetType = 2;
	private static final int COLUMN_INDEX_DatasetName = 3;
	private static final int COLUMN_INDEX_EncodeType = 4;
	private static final int COLUMN_INDEX_Charset = 5;
	private static final int COLUMN_INDEX_WindowMode = 6;

	final JPanel contentPanel = new JPanel();
	private JToolBar toolBar;
	private JButton buttonSelectAll;
	private JButton buttonSelectInvert;
	private JButton buttonDelete;
	private JButton buttonSetting;
	private MutiTable table;
	private JCheckBox checkboxAutoClose;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private String defaultDatasetName = "";
	private transient Datasource targetDatasource;

	/**
	 * Create the dialog.
	 */
	public JDialogDatasetNew() {

	}

	public void init() {
		this.setModal(true);
		setTitle("Template");
		setBounds(100, 100, 677, 405);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		this.table = new MutiTable();
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		DDLExportTableModel tableModel = new DDLExportTableModel(new String[] { "Index", "TargetDatasource", "CreateType", "DatasetName", "EncodeType",
				"Charset", "AddToMap" }) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, true, true, true, true, true, true };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};

		this.table.setModel(tableModel);
		this.table.putClientProperty("terminateEditOnFocusLost", true);
		initializeColumns();

		this.table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				table_ValueChanged(e);
			}
		});
		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkButtonState();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				checkButtonState();
			}
		});
		this.table.setRowHeight(23);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);

		// @formatter:off
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(7)
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
		);
		// @formatter:on

		this.buttonSelectAll = new JButton();
		this.buttonSelectAll.setIcon(new ImageIcon(JDialogDatasetNew.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		this.buttonSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectAll_Click();
			}
		});
		this.toolBar.add(buttonSelectAll);
		this.buttonSelectInvert = new JButton();
		this.buttonSelectInvert.setIcon(new ImageIcon(JDialogDatasetNew.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		this.buttonSelectInvert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectInvert_Click();
			}
		});
		this.toolBar.add(buttonSelectInvert);
		this.toolBar.add(createSeparator());

		this.buttonDelete = new JButton();
		this.buttonDelete
				.setIcon(new ImageIcon(JDialogDatasetNew.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonDelete_Click();
			}
		});
		this.buttonDelete.setHorizontalAlignment(SwingConstants.LEFT);
		this.buttonDelete.setEnabled(false);
		this.toolBar.add(buttonDelete);
		this.toolBar.add(createSeparator());

		this.buttonSetting = new JButton();
		this.buttonSetting.setIcon(new ImageIcon(JDialogDatasetNew.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Setting.PNG")));
		this.buttonSetting.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSetting_Click();
			}
		});
		this.buttonSetting.setEnabled(false);
		this.toolBar.add(buttonSetting);
		this.contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		checkboxAutoClose = new JCheckBox("Auto Close");
		checkboxAutoClose.setVerticalAlignment(SwingConstants.TOP);
		checkboxAutoClose.setHorizontalAlignment(SwingConstants.LEFT);
		checkboxAutoClose.setSelected(true);
		buttonOk = new SmButton();
		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_Click();
			}
		});
		this.getRootPane().setDefaultButton(buttonOk);
		buttonCancel = new SmButton();
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_Click();
			}
		});

		// @formatter:off
		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(checkboxAutoClose)
						.addPreferredGap(ComponentPlacement.RELATED, 227, Short.MAX_VALUE).addComponent(buttonOk, 75, 75, 75)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(buttonCancel, 75, 75, 75).addContainerGap()));
		gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_buttonPane
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_buttonPane.createParallelGroup(Alignment.BASELINE, false).addComponent(buttonOk).addComponent(checkboxAutoClose)
										.addComponent(buttonCancel)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		buttonPane.setLayout(gl_buttonPane);
		// @formatter:on

		initializeResources();
		this.setLocationRelativeTo(null);

		// 添加默认要创建的数据集行
		copyPreDatasetInfo(-1);

		// 添加预加载的一行
		copyPreDatasetInfo(0);
		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(this.policy);
	}

	public DialogResult showDialog() {
		init();
		this.setVisible(true);
		return this.dialogResult;
	}

	public boolean isAutoClose() {
		return this.checkboxAutoClose.isSelected();
	}

	private void initializeResources() {
		if (table != null) {
			this.setTitle(DataEditorProperties.getString("String_ToolStripMenuItem_NewDataset"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));

			this.table.getColumnModel().getColumn(COLUMN_INDEX_INDEX).setHeaderValue(CommonProperties.getString("String_ColumnHeader_Index"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_TARGETDATASOURCE)
					.setHeaderValue(CommonProperties.getString("String_ColumnHeader_TargetDatasource"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_DatasetType).setHeaderValue(DataEditorProperties.getString("String_CreateType"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_DatasetName).setHeaderValue(DataEditorProperties.getString("String_ColumnTitle_DtName"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_EncodeType).setHeaderValue(CommonProperties.getString("String_ColumnHeader_EncodeType"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Charset).setHeaderValue(DataEditorProperties.getString("String_Charset"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_WindowMode)
					.setHeaderValue(DataEditorProperties.getString("String_DataGridViewComboBoxColumn_Name"));

			this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
			this.buttonSelectInvert.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
			this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
			this.buttonSetting.setToolTipText(CommonProperties.getString("String_ToolBar_SetBatch"));
			this.checkboxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
		}
	}

	public NewDatasetInfo[] getDatasets() {
		ArrayList<NewDatasetInfo> infos = new ArrayList<NewDatasetInfo>();
		try {
			String modelTargetDatasource = this.table.getModel().getValueAt(0, COLUMN_INDEX_TARGETDATASOURCE).toString();
			if ("".equals(modelTargetDatasource)) {
				return new NewDatasetInfo[0];
			}
			for (int index = 0; index < this.table.getRowCount() - 1; index++) {
				NewDatasetInfo info = new NewDatasetInfo();
				String datasourceName = this.table.getModel().getValueAt(index, COLUMN_INDEX_TARGETDATASOURCE).toString();
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				info.setTargetDatasource(datasource);

				String typeName = this.table.getModel().getValueAt(index, COLUMN_INDEX_DatasetType).toString();
				DatasetType datasetType = CommonToolkit.DatasetTypeWrap.findType(typeName);
				info.setDatasetType(datasetType);

				String datasetName = this.table.getModel().getValueAt(index, COLUMN_INDEX_DatasetName).toString();
				info.setDatasetName(datasetName);

				String encodeName = this.table.getModel().getValueAt(index, COLUMN_INDEX_EncodeType).toString();
				EncodeType encodeType = CommonToolkit.EncodeTypeWrap.findType(encodeName);
				info.setEncodeType(encodeType);

				String charsetName = this.table.getModel().getValueAt(index, COLUMN_INDEX_Charset).toString();
				Charset charset = CharsetUtilties.valueOf(charsetName);
				info.setCharset(charset);

				String modeTypeName = this.table.getModel().getValueAt(index, COLUMN_INDEX_WindowMode).toString();
				AddToWindowMode modeType = AddToWindowMode.getWindowMode(modeTypeName);
				info.setModeType(modeType);

				infos.add(info);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return infos.toArray(new NewDatasetInfo[infos.size()]);
	}

	/**
	 * 初始化列表的列枚举类型
	 */
	@SuppressWarnings("unchecked")
	private void initializeColumns() {
		try {
			// 目标数据源
			int count = Application.getActiveApplication().getWorkspace().getDatasources().getCount();
			String[] datasources = new String[count];
			int selectedIndex = -1;
			String activeDatasource = "";
			if (0 < Application.getActiveApplication().getActiveDatasources().length) {
				activeDatasource = Application.getActiveApplication().getActiveDatasources()[0].getAlias();
			}

			for (int index = 0; index < count; index++) {
				datasources[index] = Application.getActiveApplication().getWorkspace().getDatasources().get(index).getAlias();
				if (datasources[index].equals(activeDatasource)) {
					selectedIndex = index;
				}
			}
			final DatasourceComboBox datasourceComboBox = new DatasourceComboBox(Application.getActiveApplication().getWorkspace().getDatasources());
			datasourceComboBox.setSelectedIndex(selectedIndex);
			datasourceComboBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						try {
							setAbstractDatasourceComboBox(datasourceComboBox);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}

			});

			DefaultCellEditor targetDatasourceCellEditor = new DefaultCellEditor(datasourceComboBox);
			TableColumn targetDatasourceColumn = table.getColumnModel().getColumn(COLUMN_INDEX_TARGETDATASOURCE);
			targetDatasourceColumn.setCellEditor(targetDatasourceCellEditor);
			// 设置渲染
			CommonListCellRenderer renderer = new CommonListCellRenderer();
			targetDatasourceColumn.setCellRenderer(renderer);
			// 可创建的数据集类型
			ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
			datasetTypes.add(DatasetType.POINT);
			datasetTypes.add(DatasetType.LINE);
			datasetTypes.add(DatasetType.REGION);
			datasetTypes.add(DatasetType.TEXT);
			datasetTypes.add(DatasetType.CAD);
			datasetTypes.add(DatasetType.TABULAR);
			datasetTypes.add(DatasetType.POINT3D);
			datasetTypes.add(DatasetType.LINE3D);
			datasetTypes.add(DatasetType.REGION3D);
			final DatasetComboBox comboBoxDatasetType = new DatasetComboBox(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
			DefaultCellEditor datasetTypeCellEditor = new DefaultCellEditor(comboBoxDatasetType);
			TableColumn datasetTypeColumn = table.getColumnModel().getColumn(COLUMN_INDEX_DatasetType);
			datasetTypeColumn.setCellEditor(datasetTypeCellEditor);
			datasetTypeColumn.setPreferredWidth(100);
			datasetTypeColumn.setCellRenderer(renderer);
			comboBoxDatasetType.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						setAbstractComboBoxDatasetTypeLisenter(comboBoxDatasetType);
					}
				}

			});

			// 编码类型
			TableColumn encodeTypeColumn = table.getColumnModel().getColumn(COLUMN_INDEX_EncodeType);
			encodeTypeColumn.setCellEditor(new EncodingTypeCellEditor(new JComboBox()));

			// 字符集
			TableColumn charseteColumn = table.getColumnModel().getColumn(COLUMN_INDEX_Charset);
			charseteColumn.setCellEditor(new CharsetTypeCellEditor(new JComboBox()));

			// 填充添加到图层列
			ArrayList<String> addTos = new ArrayList<String>();
			addTos.add(AddToWindowMode.toString(AddToWindowMode.NONEWINDOW));
			addTos.add(AddToWindowMode.toString(AddToWindowMode.NEWWINDOW));
			if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				addTos.add(AddToWindowMode.toString(AddToWindowMode.CURRENTWINDOW));
			}

			ComboBoxCellEditor addToCellEditor = new ComboBoxCellEditor();
			addToCellEditor.getComboBox().setModel(new DefaultComboBoxModel<Object>(addTos.toArray(new String[addTos.size()])));
			TableColumn addToColumn = table.getColumnModel().getColumn(COLUMN_INDEX_WindowMode);
			addToColumn.setCellEditor(addToCellEditor);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void setAbstractComboBoxDatasetTypeLisenter(final DatasetComboBox comboBoxDatasetType) {
		// 如果当前行为最后一行先自动增加一行，当前行数据集默认给一个名字
		int rowIndex = table.getSelectedRow();
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		String item = comboBoxDatasetType.getSelectItem();
		Datasource datasource = workspace.getDatasources().get(table.getModel().getValueAt(table.getSelectedRow(), COLUMN_INDEX_TARGETDATASOURCE).toString());
		DatasetType datasetType = CommonToolkit.DatasetTypeWrap.findType(item);
		String datasetName = CommonToolkit.DatasetWrap.getAvailableDatasetName(datasource, getDefaultDatasetName(datasetType),
				getAllDatasetNames(datasource, rowIndex));
		if (rowIndex == table.getRowCount() - 1) {
			copyPreDatasetInfo(rowIndex);
			checkButtonState();
		}
		this.table.getModel().setValueAt(datasetName, rowIndex, COLUMN_INDEX_DatasetName);
	}

	private void setAbstractDatasourceComboBox(final DatasourceComboBox datasourceComboBox) {
		try {
			// 选择不同的数据源时更新要新建的数据集的名称
			String item = datasourceComboBox.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(item);
			String datasetName = table.getModel().getValueAt(table.getSelectedRow(), COLUMN_INDEX_DatasetName).toString();
			if (table.getSelectedRow() != table.getRowCount() - 1) {
				table.setValueAt(datasource.getDatasets().getAvailableDatasetName(datasetName), table.getSelectedRow(), COLUMN_INDEX_DatasetName);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 复制一行记录
	 *
	 * @param srcIndex
	 *            上一行的索引，如果是-1，则会创建默认的一行
	 */
	private void copyPreDatasetInfo(int rowIndex) {
		try {
			Object[] datas = new Object[7];
			if (rowIndex == -1) {
				datas[COLUMN_INDEX_INDEX] = "1";
				if (0 < Application.getActiveApplication().getActiveDatasources().length) {
					this.targetDatasource = Application.getActiveApplication().getActiveDatasources()[0];
					DataCell datasourceCell = new DataCell();
					datasourceCell.initDatasourceType(targetDatasource);
					datas[COLUMN_INDEX_TARGETDATASOURCE] = datasourceCell;
					String datasetName = this.getDefaultDatasetName(DatasetType.POINT);
					this.defaultDatasetName = targetDatasource.getDatasets().getAvailableDatasetName(datasetName);
					datas[COLUMN_INDEX_DatasetName] = defaultDatasetName;
				} else {
					datas[COLUMN_INDEX_TARGETDATASOURCE] = "";
					datas[COLUMN_INDEX_DatasetName] = "";
				}
				String dataTypeName = CommonToolkit.DatasetTypeWrap.findName(DatasetType.POINT);
				DataCell datasetTypeCell = new DataCell();
				datasetTypeCell.initDatasetType(DatasetType.POINT, dataTypeName);
				datas[COLUMN_INDEX_DatasetType] = datasetTypeCell;

				datas[COLUMN_INDEX_EncodeType] = CommonProperties.getString("String_EncodeType_None");
				datas[COLUMN_INDEX_Charset] = CharsetUtilties.getCharsetName(Charset.UTF8);
				datas[COLUMN_INDEX_WindowMode] = AddToWindowMode.toString(AddToWindowMode.NONEWINDOW);
			} else {
				datas[COLUMN_INDEX_INDEX] = this.table.getRowCount() + 1;
				datas[COLUMN_INDEX_TARGETDATASOURCE] = this.table.getModel().getValueAt(rowIndex, COLUMN_INDEX_TARGETDATASOURCE);
				datas[COLUMN_INDEX_DatasetType] = this.table.getModel().getValueAt(rowIndex, COLUMN_INDEX_DatasetType);
				datas[COLUMN_INDEX_DatasetName] = "";
				datas[COLUMN_INDEX_EncodeType] = this.table.getModel().getValueAt(rowIndex, COLUMN_INDEX_EncodeType);
				datas[COLUMN_INDEX_Charset] = this.table.getModel().getValueAt(rowIndex, COLUMN_INDEX_Charset);
				datas[COLUMN_INDEX_WindowMode] = this.table.getModel().getValueAt(rowIndex, COLUMN_INDEX_WindowMode);
			}
			this.table.addRow(datas);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private String getDefaultDatasetName(DatasetType datasetType) {
		String newDatasetName = "";
		if (datasetType == DatasetType.POINT) {
			newDatasetName = "New_Point";
		} else if (datasetType == DatasetType.LINE) {
			newDatasetName = "New_Line";
		} else if (datasetType == DatasetType.REGION) {
			newDatasetName = "New_Region";
		} else if (datasetType == DatasetType.TEXT) {
			newDatasetName = "New_Text";
		} else if (datasetType == DatasetType.CAD) {
			newDatasetName = "New_CAD";
		} else if (datasetType == DatasetType.TABULAR) {
			newDatasetName = "New_Tabular";
		} else if (datasetType == DatasetType.POINT3D) {
			newDatasetName = "New_Point3D";
		} else if (datasetType == DatasetType.LINE3D) {
			newDatasetName = "New_Line3D";
		} else if (datasetType == DatasetType.REGION3D) {
			newDatasetName = "New_Region3D";
		} else if (datasetType == DatasetType.PARAMETRICLINE) {
			newDatasetName = "New_ParametricLine";
		} else if (datasetType == DatasetType.PARAMETRICREGION) {
			newDatasetName = "New_ParametricRegion";
		} else if (datasetType == DatasetType.IMAGECOLLECTION) {
			newDatasetName = "New_ImageCollection";
		}

		return newDatasetName;
	}

	/**
	 * 创建一个工具条的分隔符
	 *
	 * @return
	 */
	private JToolBar.Separator createSeparator() {
		JToolBar.Separator separator = new JToolBar.Separator();
		separator.setOrientation(SwingConstants.VERTICAL);
		return separator;
	}

	/**
	 * 用数据集全名匹配默认数据集名
	 *
	 * @param wholeName
	 *            数据集名
	 * @param typeName
	 *            默认数据集名
	 * @return 符合默认命名格式，返回true
	 */
	private boolean testDefaultName(String wholeName, String typeName) {
		boolean result = false;
		if (wholeName.equalsIgnoreCase(typeName)) {
			result = true;
		} else if (wholeName.indexOf(typeName) == 0) {
			String leftedString = wholeName.substring(typeName.length(), wholeName.length());
			// 如果是 "_" 开头，则去掉下划线
			if (leftedString.startsWith("_")) {
				leftedString = leftedString.substring(leftedString.lastIndexOf("_") + 1, leftedString.length());
			}

			if (Integer.valueOf(leftedString) >= 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 检索是否是默认指定的名字
	 *
	 * @param datasetName
	 *            被检索的数据集名
	 * @return 符合默认命名格式，返回true
	 */
	private boolean isDefaultName(String datasetName) {
		boolean result = false;
		String defaultTypeName = "";

		if (datasetName.indexOf("New_Point3D") == 0) {
			defaultTypeName = "New_Point3D";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Line3D") == 0) {
			defaultTypeName = "New_Line3D";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Region3D") == 0) {
			defaultTypeName = "New_Region3D";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Text") == 0) {
			defaultTypeName = "New_Text";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_CAD") == 0) {
			defaultTypeName = "New_CAD";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Tabular") == 0) {
			defaultTypeName = "New_Tabular";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Template") == 0) {
			defaultTypeName = "New_Template";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Point") == 0) {
			defaultTypeName = "New_Point";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Line") == 0) {
			defaultTypeName = "New_Line";
			result = testDefaultName(datasetName, defaultTypeName);
		} else if (datasetName.indexOf("New_Region") == 0) {
			defaultTypeName = "New_Region";
			result = testDefaultName(datasetName, defaultTypeName);
		}
		return result;
	}

	private String[] getAllDatasetNames(Datasource datasource, int rowIndex) {
		ArrayList<String> datasetNames = new ArrayList<String>();
		try {
			if (datasource != null) {
				for (int index = 0; index < this.table.getRowCount(); index++) {
					if (this.table.getModel().getValueAt(index, COLUMN_INDEX_TARGETDATASOURCE) != null
							&& datasource.getAlias().equals(this.table.getModel().getValueAt(index, COLUMN_INDEX_TARGETDATASOURCE).toString())) {
						datasetNames.add(this.table.getModel().getValueAt(index, COLUMN_INDEX_DatasetName).toString().toLowerCase());
					}
				}
			} else {
				for (int index = 0; index < this.table.getRowCount(); index++) {
					if (index != rowIndex && this.table.getModel().getValueAt(index, COLUMN_INDEX_TARGETDATASOURCE) != null
							&& this.table.getModel().getValueAt(index, COLUMN_INDEX_TARGETDATASOURCE).toString() != "") {
						datasetNames.add(this.table.getModel().getValueAt(index, COLUMN_INDEX_DatasetName).toString().toLowerCase());
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return datasetNames.toArray(new String[datasetNames.size()]);
	}

	private void table_ValueChanged(TableModelEvent e) {
		int rowIndex = e.getFirstRow();
		int columnIndex = e.getColumn();
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		String datasourceName = this.table.getModel().getValueAt(rowIndex, COLUMN_INDEX_TARGETDATASOURCE).toString();
		try {
			// 如果是编辑数据集名称
			if (e.getColumn() == COLUMN_INDEX_DatasetName && this.table.getModel().getValueAt(rowIndex, columnIndex) != null) {
				if (rowIndex == table.getRowCount() - 1) {
					copyPreDatasetInfo(rowIndex);
					checkButtonState();
				}
				String datasetName = table.getModel().getValueAt(rowIndex, columnIndex).toString();
				Datasource datasource = workspace.getDatasources().get(datasourceName);
				if (!datasource.getDatasets().isAvailableDatasetName(datasetName)) {
					// 如果输入的数据集名称非法，重置名称
					datasetName = CommonToolkit.DatasetWrap.getAvailableDatasetName(datasource, datasetName, getAllDatasetNames(datasource, rowIndex));
					this.table.getModel().setValueAt(datasetName, rowIndex, columnIndex);
				} else if (hasSameDatasetName(datasetName, rowIndex)) {
					datasetName = datasetName + "_1";
					this.table.getModel().setValueAt(datasetName, rowIndex, columnIndex);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private boolean hasSameDatasetName(String datasetName, int rowIndex) {
		for (int i = 0; i < table.getRowCount(); i++) {
			if (i != rowIndex) {
				String tempName = table.getModel().getValueAt(i, COLUMN_INDEX_DatasetName).toString();
				if (datasetName.equals(tempName)) {
					return true;
				}
			}
		}
		return false;
	}

	private void checkButtonState() {
		boolean state = false;
		if (table.getSelectedRow() != -1 && table.getSelectedRow() != table.getRowCount() - 1) {
			state = true;
		}
		this.buttonDelete.setEnabled(state);
		if (table.getSelectedRow() != -1) {
			this.buttonSetting.setEnabled(true);
		} else {
			this.buttonSetting.setEnabled(false);
		}
	}

	private void buttonSelectAll_Click() {
		try {
			ListSelectionModel selectionModel = this.table.getSelectionModel();
			selectionModel.addSelectionInterval(0, this.table.getRowCount() - 1);
			if (1 != table.getRowCount()) {
				checkButtonState();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonSelectInvert_Click() {
		try {
			int[] temp = this.table.getSelectedRows();
			ArrayList<Integer> selectedRows = new ArrayList<Integer>();
			for (int index = 0; index < temp.length; index++) {
				selectedRows.add(temp[index]);
			}

			ListSelectionModel selectionModel = table.getSelectionModel();
			selectionModel.clearSelection();
			for (int index = 0; index < this.table.getRowCount(); index++) {
				if (!selectedRows.contains(index)) {
					selectionModel.addSelectionInterval(index, index);
				}
			}
			checkButtonState();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonDelete_Click() {
		try {
			boolean isResetIndex = false;
			int[] selectedRows = this.table.getSelectedRows();
			for (int index = selectedRows.length - 1; index >= 0; index--) {
				if (selectedRows[index] != this.table.getRowCount() - 1) {
					this.table.removeRow(selectedRows[index]);
					isResetIndex = true;
				}
			}

			if (isResetIndex) {
				for (int j = 0; j < this.table.getRowCount(); j++) {
					this.table.getModel().setValueAt(j + 1, j, 0);
				}
			}

			// 选中第一行
			table.setRowSelectionInterval(0, 0);
			checkButtonState();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// 打开统一设置界面
	private void buttonSetting_Click() {
		try {
			JDialogSetAll dialogSetAll = new JDialogSetAll();
			if (dialogSetAll.showDialog() == DialogResult.OK) {

				int[] selectedRows = table.getSelectedRows();
				Object dialogTargetDatasource = dialogSetAll.getTargetDatasource();
				Object datasetType = dialogSetAll.getDatasetType();
				Object encodingType = dialogSetAll.getEncodingType();
				Object charset = dialogSetAll.getCharset();
				Object addTomap = dialogSetAll.getAddtoMap();
				Workspace workspace = Application.getActiveApplication().getWorkspace();

				for (int i : selectedRows) {
					boolean isDatasourceChanged = false;
					boolean isDatasetTypeChanged = false;

					if (dialogTargetDatasource != null
							&& (!((DataCell) table.getValueAt(i, COLUMN_INDEX_TARGETDATASOURCE)).getDataName().equals(
									((DataCell) dialogTargetDatasource).getDataName()))) {
						table.setValueAt(dialogTargetDatasource, i, COLUMN_INDEX_TARGETDATASOURCE);
						isDatasourceChanged = true;
					}
					if (datasetType != null
							&& (!((DataCell) table.getValueAt(i, COLUMN_INDEX_DatasetType)).getDataName().equals(((DataCell) datasetType).getDataName()))) {
						table.setValueAt(datasetType, i, COLUMN_INDEX_DatasetType);
						isDatasetTypeChanged = true;
					}
					if (encodingType != null && isSupportDatasetType(table.getValueAt(i, COLUMN_INDEX_DatasetType), encodingType)) {
						table.setValueAt(encodingType, i, COLUMN_INDEX_EncodeType);
					}
					if (charset != null) {
						table.setValueAt(charset, i, COLUMN_INDEX_Charset);
					}
					if (addTomap != null) {
						table.setValueAt(addTomap, i, COLUMN_INDEX_WindowMode);
					}
					if (isDatasourceChanged || isDatasetTypeChanged) {
						// 数据源或数据集类型改变需要判断数据集名称的合法性
						Datasource datasource = workspace.getDatasources().get(table.getValueAt(i, COLUMN_INDEX_TARGETDATASOURCE).toString());
						String datasetName = "";
						if (!StringUtilties.isNullOrEmptyString(table.getValueAt(i, COLUMN_INDEX_DatasetName).toString())) {
							datasetName = table.getValueAt(i, COLUMN_INDEX_DatasetName).toString();
						}

						if (isDatasetTypeChanged || StringUtilties.isNullOrEmpty(datasetName)) {
							// 类型改变或原来为空重新取数据集名字
							DatasetType datasetTypeInfo = CommonToolkit.DatasetTypeWrap.findType(table.getModel().getValueAt(i, COLUMN_INDEX_DatasetType)
									.toString());
							datasetName = getDefaultDatasetName(datasetTypeInfo);
						}
						// 获得合法名称
						datasetName = CommonToolkit.DatasetWrap.getAvailableDatasetName(datasource, datasetName, getAllDatasetNames(null, i));
						table.setValueAt(datasetName, i, COLUMN_INDEX_DatasetName);
					}

					if (i == table.getRowCount() - 1 && !StringUtilties.isNullOrEmpty((String) table.getValueAt(i, COLUMN_INDEX_DatasetName))) {

						// 修改了最后一行且最后一行数据集名称不为空则增加一行
						copyPreDatasetInfo(i);
					}
				}
				checkButtonState();
				table.updateUI();

			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public boolean isSupportDatasetType(Object datasetType, Object encodingType) {
		if (encodingType.equals(CommonToolkit.EncodeTypeWrap.findName(EncodeType.NONE))) {
			return true;
		} else if (CommonToolkit.DatasetTypeWrap.findName(DatasetType.LINE).equals(datasetType.toString())
				|| CommonToolkit.DatasetTypeWrap.findName(DatasetType.REGION).equals(datasetType.toString())) {
			return true;
		}
		return false;
	}

	private void okButton_Click() {
		try {
			if (this.checkboxAutoClose.isSelected()) {
				this.dispose();
			}

			this.dialogResult = DialogResult.OK;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void cancelButton_Click() {
		try {
			this.setVisible(false);
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public class CharsetTypeCellEditor extends DefaultCellEditor {

		private JComboBox comboboxCharsetType = new JComboBox();

		public CharsetTypeCellEditor(JComboBox comboBox) {
			super(comboBox);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			comboboxCharsetType.removeAll();

			ArrayList<String> tempcharsharsetes = new ArrayList<String>();
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.OEM));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.EASTEUROPE));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.THAI));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.RUSSIAN));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.BALTIC));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.ARABIC));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.HEBREW));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.VIETNAMESE));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.TURKISH));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.GREEK));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.CHINESEBIG5));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.JOHAB));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.HANGEUL));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.SHIFTJIS));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.MAC));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.SYMBOL));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.DEFAULT));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.ANSI));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.UTF8));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.UTF7));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.WINDOWS1252));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.KOREAN));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.UNICODE));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.CYRILLIC));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.XIA5));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.XIA5GERMAN));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.XIA5SWEDISH));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.XIA5NORWEGIAN));
			tempcharsharsetes.add(CharsetUtilties.getCharsetName(Charset.GB18030));
			comboboxCharsetType.setModel(new DefaultComboBoxModel<String>(tempcharsharsetes.toArray(new String[tempcharsharsetes.size()])));
			comboboxCharsetType.setSelectedItem(value);
			return comboboxCharsetType;
		}

		@Override
		public Object getCellEditorValue() {
			return comboboxCharsetType.getSelectedItem();
		}
	}

	public class EncodingTypeCellEditor extends DefaultCellEditor {

		private JComboBox comboboxEncodingType = new JComboBox();

		public EncodingTypeCellEditor(JComboBox comboBox) {
			super(comboBox);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			comboboxEncodingType.removeAll();

			Object datasetType = table.getValueAt(row, COLUMN_INDEX_DatasetType).toString();
			ArrayList<String> tempEncodeType = new ArrayList<String>();
			tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.NONE));
			if (CommonToolkit.DatasetTypeWrap.findName(DatasetType.LINE).equals(datasetType)
					|| CommonToolkit.DatasetTypeWrap.findName(DatasetType.REGION).equals(datasetType)) {
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.BYTE));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT16));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT24));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT32));
			}
			comboboxEncodingType.setModel(new DefaultComboBoxModel<String>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
			comboboxEncodingType.setSelectedItem(value);
			return comboboxEncodingType;
		}

		@Override
		public Object getCellEditorValue() {
			return comboboxEncodingType.getSelectedItem();
		}
	}

}
