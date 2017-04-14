package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.CharsetUtilities;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.ArrayList;

public class JDialogDatasetCopy extends SmDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_INDEX_Dataset = 0;
	private static final int COLUMN_INDEX_CurrentDatasource = 1;
	private static final int COLUMN_INDEX_TargetDatasource = 2;
	private static final int COLUMN_INDEX_TargetDataset = 3;
	private static final int COLUMN_INDEX_EncodeType = 4;
	private static final int COLUMN_INDEX_Charset = 5;

	final JPanel contentPanel = new JPanel();
	private JToolBar toolBar;
	private JButton buttonSelectAll;
	private JButton buttonSelectInvert;
	private JButton buttonDelete;
	private JButton buttonSetting;
	private MutiTable table;
	private JCheckBox checkBoxAutoClose;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private JButton buttonAdd;
	private transient Dataset[] datasets;
	private JScrollPane scrollPaneTable;
	private DatasourceComboBox targetBox;

	private MouseAdapter tableMouseListener;
	private KeyAdapter tableKeyListener;
	private MouseAdapter scrollPaneMouseListener;
	private ActionListener buttonSelectAllListener;
	private ActionListener buttonAddListener;
	private ActionListener buttonDeleteListener;
	private ActionListener buttonSelectInvertListener;
	private ActionListener buttonSettingListener;
	private ActionListener buttonOkListener;
	private ActionListener buttonCancelListener;
	private ActionListener targetBoxListener;

	/**
	 * Create the dialog.
	 */
	public JDialogDatasetCopy() {
		initComponents();
		initResources();
		registActionListener();
		initTraversalPolicy();
	}

	public JDialogDatasetCopy(Dataset[] datasets) {
		this.datasets = datasets;
		initComponents();
		initializeCopyInfo();
		initResources();
		registActionListener();
		initTraversalPolicy();
	}

	private void initTraversalPolicy() {
		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(this.policy);
		this.getRootPane().setDefaultButton(this.buttonCancel);
	}

	private void registActionListener() {
		this.tableMouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tableOrScrollPanel_pressed(e);
			}
		};
		this.tableKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteSelectedRow();
					setButtonState();
				}
			}
		};
		this.scrollPaneMouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tableOrScrollPanel_pressed(e);
			}
		};
		this.buttonSelectAllListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectAll_Click();
			}
		};
		this.buttonAddListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAdd_Click();
				setButtonState();
			}
		};
		this.buttonDeleteListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonDelete_Click();
			}
		};
		this.buttonSelectInvertListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectInvert_Click();
			}
		};
		this.buttonSettingListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSetting_Click();
			}
		};
		this.buttonOkListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_Click();
			}
		};
		this.buttonCancelListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_Click();
			}
		};
		this.targetBoxListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				extractInitilizeColumnTryBlock(targetBox);
			}
		};
		unregistActionListener();
		this.table.addMouseListener(this.tableMouseListener);
		this.table.addKeyListener(this.tableKeyListener);
		this.scrollPaneTable.addMouseListener(this.scrollPaneMouseListener);
		this.buttonSelectAll.addActionListener(this.buttonSelectAllListener);
		this.buttonAdd.addActionListener(this.buttonAddListener);
		this.buttonDelete.addActionListener(this.buttonDeleteListener);
		this.buttonSelectInvert.addActionListener(this.buttonSelectInvertListener);
		this.buttonSetting.addActionListener(this.buttonSettingListener);
		this.buttonOk.addActionListener(this.buttonOkListener);
		this.buttonCancel.addActionListener(this.buttonCancelListener);
		this.targetBox.addActionListener(this.targetBoxListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				unregistActionListener();
			}
		});
	}

	private void unregistActionListener() {
		this.table.removeMouseListener(this.tableMouseListener);
		this.table.removeKeyListener(this.tableKeyListener);
		this.scrollPaneTable.removeMouseListener(this.scrollPaneMouseListener);
		this.buttonSelectAll.removeActionListener(this.buttonSelectAllListener);
		this.buttonAdd.removeActionListener(this.buttonAddListener);
		this.buttonDelete.removeActionListener(this.buttonDeleteListener);
		this.buttonSelectInvert.removeActionListener(this.buttonSelectInvertListener);
		this.buttonSetting.removeActionListener(this.buttonSettingListener);
		this.buttonOk.removeActionListener(this.buttonOkListener);
		this.buttonCancel.removeActionListener(this.buttonCancelListener);
		this.targetBox.removeActionListener(this.targetBoxListener);
	}

	public void initComponents() {
		this.setModal(true);
		setBounds(100, 100, 677, 405);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		this.table = new MutiTable();
		this.table.setRowHeight(23);
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		@SuppressWarnings("serial")
		DDLExportTableModel tableModel = new DDLExportTableModel(new String[]{"Dataset", "CurrentDatasource", "TargetDatasource", "TargetDataset",
				"EncodeType", "Charset"}) {
			boolean[] columnEditables = new boolean[]{false, false, true, true, true, true};

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		this.table.setModel(tableModel);
		this.table.putClientProperty("terminateEditOnFocusLost", true);
		initializeColumns();

		this.scrollPaneTable = new JScrollPane(table);
		// @formatter:off
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
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
		buttonSelectAll = new JButton();
		buttonSelectAll.setEnabled(false);
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));

		buttonAdd = new JButton();
		buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));

		toolBar.add(buttonAdd);
		toolBar.add(buttonSelectAll);

		buttonSelectInvert = new JButton();
		buttonSelectInvert.setEnabled(false);
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));

		this.toolBar.add(buttonSelectInvert);
		this.toolBar.add(createSeparator());
		this.buttonDelete = new JButton();
		this.buttonDelete.setEnabled(false);
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));

		this.buttonDelete.setHorizontalAlignment(SwingConstants.LEFT);
		this.toolBar.add(buttonDelete);
		this.toolBar.add(createSeparator());

		this.buttonSetting = new JButton();
		this.buttonSetting.setEnabled(false);
		this.buttonSetting.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));

		this.toolBar.add(buttonSetting);
		this.contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		this.checkBoxAutoClose = new JCheckBox();
		this.checkBoxAutoClose.setVerticalAlignment(SwingConstants.TOP);
		this.checkBoxAutoClose.setHorizontalAlignment(SwingConstants.LEFT);
		this.checkBoxAutoClose.setSelected(true);
		this.buttonOk = new SmButton("OK");
		this.buttonOk.setEnabled(false);

		this.buttonOk.setToolTipText("");
		this.buttonOk.setActionCommand("OK");

		this.buttonCancel = new SmButton("Cancel");

		this.buttonCancel.setToolTipText("");
		this.buttonCancel.setActionCommand("Cancel");

		// @formatter:off
		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(checkBoxAutoClose)
					.addPreferredGap(ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
					.addComponent(buttonOk, 75, 75, 75)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonCancel, 75, 75, 75)
					.addContainerGap())
		);
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(buttonOk)
						.addComponent(checkBoxAutoClose)
						.addComponent(buttonCancel))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		// @formatter:on
		buttonPane.setLayout(gl_buttonPane);
		table.getColumnModel().getColumn(COLUMN_INDEX_TargetDatasource).setPreferredWidth(40);
		this.setLocationRelativeTo(null);
	}

	private JToolBar.Separator createSeparator() {
		JToolBar.Separator separator = new JToolBar.Separator();
		separator.setOrientation(SwingConstants.VERTICAL);
		return separator;
	}

	/**
	 * 双击table或JScrollPanel打开数据集选择界面,单击选中
	 *
	 * @param e
	 */
	private void tableOrScrollPanel_pressed(MouseEvent e) {
		if (2 == e.getClickCount() && !isPointAtTableTargetDatasetOrEncodeType(e.getPoint())) {
			try {
				extractTryMethod();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	}

	private void extractTryMethod() {
		initializeDatasetChooser();
		setButtonState();
	}

	private boolean isPointAtTableTargetDatasetOrEncodeType(Point point) {
		int row = table.rowAtPoint(point);
		int column = table.columnAtPoint(point);
		if (row == -1 || (column != COLUMN_INDEX_TargetDataset && column != COLUMN_INDEX_EncodeType && column != COLUMN_INDEX_Charset)) {
			return false;
		}
		return true;
	}

	/**
	 * 通过按钮初始化数据集选择界面
	 */
	private void buttonAdd_Click() {
		try {
			initializeDatasetChooser();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 初始化数据集选择界面
	 */
	private void initializeDatasetChooser() {
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
			DatasetChooserDataEditor dataSetChooser = new DatasetChooserDataEditor(this, datasource, table, true);
			dataSetChooser = null;
		}
	}

	private void setButtonState() {
		boolean hasCopyDataset = false;
		if (0 < table.getRowCount()) {
			hasCopyDataset = true;
		}
		if (hasCopyDataset) {
			buttonDelete.setEnabled(true);
			buttonSelectAll.setEnabled(true);
			buttonSelectInvert.setEnabled(true);
			buttonSetting.setEnabled(true);
			buttonOk.setEnabled(true);
			getRootPane().setDefaultButton(buttonOk);
		} else {
			buttonDelete.setEnabled(false);
			buttonSelectAll.setEnabled(false);
			buttonSelectInvert.setEnabled(false);
			buttonSetting.setEnabled(false);
			buttonOk.setEnabled(false);
		}
	}

	public boolean isAutoClose() {
		return this.checkBoxAutoClose.isSelected();
	}

	/**
	 * 初始化复制数据集界面中的table显示
	 */
	private void initializeCopyInfo() {
		try {
			for (int i = 0; i < datasets.length; i++) {
				Dataset dataset = datasets[i];
				Object[] datas = new Object[6];
				DataCell datasetCell = new DataCell();
				datasetCell.initDatasetType(dataset);
				datas[COLUMN_INDEX_Dataset] = datasetCell;
				Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];
				DataCell datasourceCell = new DataCell();
				datasourceCell.initDatasourceType(datasource);
				datas[COLUMN_INDEX_CurrentDatasource] = datasourceCell;
				// 初始jtable时就将目标数据源列存为数据源本身-yuanR 17.2.17
				datas[COLUMN_INDEX_TargetDatasource] = datasource;
				datas[COLUMN_INDEX_TargetDataset] = datasource.getDatasets().getAvailableDatasetName(dataset.getName());
				datas[COLUMN_INDEX_EncodeType] = CommonToolkit.EncodeTypeWrap.findName(dataset.getEncodeType());
				if (dataset instanceof DatasetVector) {
					datas[COLUMN_INDEX_Charset] = CharsetUtilities.toString(((DatasetVector) dataset).getCharset());
				} else {
					datas[COLUMN_INDEX_Charset] = CharsetUtilities.toString(null);
				}

				this.table.addRow(datas);
				if (dataset.getType() == DatasetType.POINT) {
					table.getModel().setValueAt(CommonProperties.getString("String_EncodeType_None"), i, COLUMN_INDEX_EncodeType);
				}
			}
			if (0 < table.getRowCount()) {
				table.setRowSelectionInterval(0, table.getRowCount() - 1);
			}
			setButtonState();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		try {
			this.setTitle(DataEditorProperties.getString("String_CopyDataset"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			this.buttonOk.setText(DataEditorProperties.getString("String_Copy"));

			table.getColumnModel().getColumn(COLUMN_INDEX_Dataset).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDataset"));
			table.getColumnModel().getColumn(COLUMN_INDEX_CurrentDatasource).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDatasource"));
			table.getColumnModel().getColumn(COLUMN_INDEX_TargetDatasource).setHeaderValue(CommonProperties.getString("String_ColumnHeader_TargetDatasource"));
			table.getColumnModel().getColumn(COLUMN_INDEX_TargetDataset).setHeaderValue(CommonProperties.getString("String_ColumnHeader_TargetDataset"));
			table.getColumnModel().getColumn(COLUMN_INDEX_EncodeType).setHeaderValue(CommonProperties.getString("String_ColumnHeader_EncodeType"));
			table.getColumnModel().getColumn(COLUMN_INDEX_Charset).setHeaderValue(DataEditorProperties.getString("String_Charset"));

			buttonAdd.setToolTipText(CommonProperties.getString("String_Add"));
			buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
			buttonSelectInvert.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
			buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
			buttonSetting.setToolTipText(CommonProperties.getString("String_ToolBar_Advanced"));
			this.checkBoxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog1"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 初始化下拉框信息
	 */
	private void initializeColumns() {
		try {
			// 目标数据源
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			ArrayList<Datasource> datasourcesArray = new ArrayList<Datasource>();
			for (int i = 0; i < datasources.getCount(); i++) {
				datasourcesArray.add(datasources.get(i));
			}
			Datasource[] array = new Datasource[datasourcesArray.size()];
			this.targetBox = new DatasourceComboBox(datasourcesArray.toArray(array));

			// 创建jtable编辑器，其中放入comboBox控件
			TableColumn targetDatasourceColumn = table.getColumnModel().getColumn(COLUMN_INDEX_TargetDatasource);
			TableCellEditor targetDatasourceCellEditor = new DefaultCellEditor(targetBox);
			targetDatasourceColumn.setCellEditor(targetDatasourceCellEditor);

			CommonListCellRenderer renderer = new CommonListCellRenderer();
			TableColumn sourceDatasetColumn = table.getColumnModel().getColumn(COLUMN_INDEX_Dataset);
			TableColumn currentDatasourceColumn = table.getColumnModel().getColumn(COLUMN_INDEX_CurrentDatasource);
			sourceDatasetColumn.setCellRenderer(renderer);
			currentDatasourceColumn.setCellRenderer(renderer);
			// 此时若等同地设置“目标数据源”渲染方式，会导致单元格显示为空
			// 因为目标数据源列存的数据对象为数据源本身，需要换一种渲染方式--yuanR
//			targetDatasourceColumn.setCellRenderer(renderer);
			targetDatasourceColumn.setCellRenderer(new TableDataCellRender());

			// 目标数据集
			TableColumn targetDatasetColumn = this.table.getColumnModel().getColumn(COLUMN_INDEX_TargetDataset);
			targetDatasetColumn.setCellEditor(new TargetDatasetNameCellEditor(new JTextField()));

			// 编码类型
			TableColumn encodeTypeColumn = table.getColumnModel().getColumn(COLUMN_INDEX_EncodeType);
			encodeTypeColumn.setCellEditor(new EncodingTypeCellEditor(new JComboBox()));

			// 字符集
			TableColumn charseteColumn = table.getColumnModel().getColumn(COLUMN_INDEX_Charset);
			charseteColumn.setCellEditor(new CharsetTypeCellEditor(new JComboBox()));

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void extractInitilizeColumnTryBlock(final DatasourceComboBox targetBox) {
		// 选择不同的数据源时更新要复制的数据集的名称
		Datasource datasource = (Datasource) targetBox.getSelectedDatasource();
		String dataset = table.getValueAt(table.getSelectedRow(), COLUMN_INDEX_Dataset).toString();
		table.getModel().setValueAt(datasource.getDatasets().getAvailableDatasetName(dataset), table.getSelectedRow(), COLUMN_INDEX_TargetDataset);
	}

	/**
	 * 全选
	 */
	private void buttonSelectAll_Click() {
		try {
			ListSelectionModel selectionModel = this.table.getSelectionModel();
			selectionModel.addSelectionInterval(0, this.table.getRowCount() - 1);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 反选
	 */
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
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 删除
	 */
	private void buttonDelete_Click() {
		deleteSelectedRow();
		setButtonState();
	}

	/**
	 * 删除选择行的实现
	 */
	private void deleteSelectedRow() {
		try {
			// 删除后设置第0行被选中
			if (0 < table.getRowCount()) {
				int[] selectRows = table.getSelectedRows();
				DDLExportTableModel tableModel = (DDLExportTableModel) table.getModel();
				tableModel.removeRows(selectRows);
				table.updateUI();
				if (0 < table.getRowCount()) {
					table.setRowSelectionInterval(0, 0);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 统一设置的简单实现
	 */
	private void buttonSetting_Click() {
		try {
			JDialogUnifiedSet unifiedSet = new JDialogUnifiedSet(this, true, table);
			unifiedSet.setVisible(true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 复制的实现
	 */
	private void copyDataset() {
		// 进度条实现
		FormProgressTotal formProgress = new FormProgressTotal();
		formProgress.setTitle(DataEditorProperties.getString("String_CopyDataset"));
		formProgress.doWork(new DatasetCopyCallable(table));
		if (this.checkBoxAutoClose.isSelected()) {
			this.dispose();
		}
	}

	private void okButton_Click() {
		copyDataset();
	}

	/**
	 * 取消
	 */
	private void cancelButton_Click() {
		try {
			dispose();
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
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

			String datasourceName = table.getModel().getValueAt(row, COLUMN_INDEX_CurrentDatasource).toString();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			String datasetName = table.getValueAt(row, COLUMN_INDEX_Dataset).toString();
			Dataset dataset = datasource.getDatasets().get(datasetName);
			ArrayList<String> tempEncodeType = new ArrayList<>();
			tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.NONE));
			if (dataset instanceof DatasetVector && (DatasetType.LINE == dataset.getType() || DatasetType.REGION == dataset.getType())) {
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.BYTE));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT16));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT24));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.INT32));
			} else if (dataset instanceof DatasetGrid || dataset instanceof DatasetGridCollection) {
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.SGL));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.LZW));
			} else if (dataset instanceof DatasetImage || dataset instanceof DatasetImageCollection) {
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.DCT));
				tempEncodeType.add(CommonToolkit.EncodeTypeWrap.findName(EncodeType.LZW));
			}

			comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
			comboboxEncodingType.setSelectedItem(value);
			return comboboxEncodingType;
		}

		@Override
		public Object getCellEditorValue() {
			return comboboxEncodingType.getSelectedItem();
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

			String datasourceName = table.getValueAt(row, COLUMN_INDEX_CurrentDatasource).toString();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			String datasetName = table.getValueAt(row, COLUMN_INDEX_Dataset).toString();
			Dataset dataset = datasource.getDatasets().get(datasetName);

			ArrayList<String> tempcharsharsetes = new ArrayList<>();
			if (dataset instanceof DatasetVector) {
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.OEM));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.EASTEUROPE));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.THAI));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.RUSSIAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.BALTIC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.ARABIC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.HEBREW));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.VIETNAMESE));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.TURKISH));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.GREEK));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.CHINESEBIG5));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.JOHAB));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.HANGEUL));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.SHIFTJIS));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.MAC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.SYMBOL));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.DEFAULT));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.ANSI));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.UTF8));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.UTF7));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.WINDOWS1252));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.KOREAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.UNICODE));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.CYRILLIC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5GERMAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5SWEDISH));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5NORWEGIAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.GB18030));
			} else {
				tempcharsharsetes.add(CharsetUtilities.toString(null));
			}
			comboboxCharsetType.setModel(new DefaultComboBoxModel<>(tempcharsharsetes.toArray(new String[tempcharsharsetes.size()])));
			comboboxCharsetType.setSelectedItem(value);
			return comboboxCharsetType;
		}

		@Override
		public Object getCellEditorValue() {
			return comboboxCharsetType.getSelectedItem();
		}
	}

	public class TargetDatasetNameCellEditor extends DefaultCellEditor {

		private JTable table;
		private SmTextFieldLegit textField = new SmTextFieldLegit();
		private int row = 0;
		private int column = 0;

		public TargetDatasetNameCellEditor(final JTextField textField) {
			super(textField);
			this.textField.setSmTextFieldLegit(new ISmTextFieldLegit() {
				@Override
				public boolean isTextFieldValueLegit(String textFieldValue) {

					return getAvailableDatasetName(textFieldValue, table, column, row).equals(textFieldValue);
				}

				@Override
				public String getLegitValue(String currentValue, String backUpValue) {
					return getAvailableDatasetName(currentValue, table, column, row);
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.table = table;
			this.row = row;
			this.column = column;
			textField.setText(String.valueOf(value));
			return textField;
		}

		@Override
		public Object getCellEditorValue() {
			String value = textField.getText();
//			String targetDatasourceName = table.getValueAt(row, COLUMN_INDEX_TargetDatasource).toString();
//			Datasource targetDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(targetDatasourceName);

			Datasource targetDatasource = (Datasource) table.getValueAt(row, COLUMN_INDEX_TargetDatasource);
			value = targetDatasource.getDatasets().getAvailableDatasetName(value);
			value = this.getAvailableDatasetName(value, table, column, row);
			return value;
		}

		public String getAvailableDatasetName(String datasetName, JTable table, int column, int row) {
			int suffix = 0;
			String value = null;
			while (true) {
				if (suffix != 0) {
					value = MessageFormat.format(DataEditorProperties.getString("String_TargetDatasetName"), datasetName, suffix);
				} else {

					value = datasetName;
				}
				int i;
				for (i = 0; i < table.getRowCount(); i++) {
					// 跳过本行
					if (i == row) {
						continue;
					} else if (value.equals(table.getValueAt(i, column).toString())) {
						suffix++;
						break;
					}
				}
				if (i == table.getRowCount()) {
					break;
				}
			}
			return value;
		}
	}

}
