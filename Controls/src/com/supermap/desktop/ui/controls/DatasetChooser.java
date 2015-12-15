package com.supermap.desktop.ui.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;

/**
 * 数据集选择界面
 * 
 * @author xie
 *
 */
public class DatasetChooser extends SmDialog {

	private static final long serialVersionUID = 1L;
	protected MutiTable table;
	protected JButton buttonOk = new JButton("string_button_sure");
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private JButton cancelButton = new JButton("string_button_quit");

	private JLabel labelPath = new JLabel("String_FormDatasetBrowse_ToolStripLabelPath");
	private JLabel labelScense = new JLabel("String_FormDatasetBrowse_ToolStripLabelDisplayType");
	private JLabel labelSearch = new JLabel();

	private JPanel contentPane;
	private JPanel panelTable = new JPanel();

	private JTextField textFieldShearch;
	private JTextField textFieldPath = new JTextField();

	protected WorkspaceTree workspaceTree;

	private DatasetComboBox comboBoxScense;

	protected transient WorkspaceMouseAdapter mouseAdapter = new WorkspaceMouseAdapter();

	protected static final int COLUMN_INDEX_DATASET = 0;

	protected static final int COLUMN_INDEX_CURRENT_DATASOURCE = 1;

	protected static final int COLUMN_INDEX_DATASET_TYPE = 2;

	protected transient Datasource datasource;

	private String[] datasetTypes = null;
	private String[] datasetWithOutTabular = null;

	/**
	 * 构造方法
	 * 
	 * @param owner 父窗体（JDialog类型）
	 * @param flag 是否设置为模态窗口
	 * @param datasource 指定的数据源
	 */
	public DatasetChooser(JDialog owner, boolean flag, Datasource datasource) {
		super(owner, flag);
		this.datasource = datasource;
		comboBoxScense = new DatasetComboBox();
		initCompanent();
		initResources();
		initializeTableInfo(datasource);
	}

	public DatasetChooser(JFrame owner, boolean flag) {
		super(owner, flag);
		comboBoxScense = new DatasetComboBox();
		initCompanent();
		initResources();
		initializeTableInfo();
	}

	public DatasetChooser(JDialog owner, boolean flag) {
		super(owner, flag);
		comboBoxScense = new DatasetComboBox();
		initCompanent();
		initResources();
		initializeTableInfo();
	}

	/**
	 * 构造方法
	 * 
	 * @param owner 父窗体（JFrame类型）
	 * @param flag 是否设置为模态窗口
	 * @param datasource 指定的数据源
	 */
	public DatasetChooser(JFrame owner, boolean flag, Datasource datasource) {
		super(owner, flag);
		comboBoxScense = new DatasetComboBox();
		initCompanent();
		initResources();
		this.datasource = datasource;
		initializeTableInfo(datasource);
	}

	/**
	 * 
	 * @param owner
	 * @param flag
	 * @param datasource
	 * @param datasetTypes
	 */
	public DatasetChooser(JDialog owner, boolean flag, Datasource datasource, String[] datasetTypes) {
		super(owner, flag);
		this.datasource = datasource;
		this.datasetTypes = datasetTypes;
		comboBoxScense = new DatasetComboBox(datasetTypes);
		initCompanent();
		initResources();
		initializeTableInfo(datasource, datasetTypes);
	}

	/**
	 * 没有纯属性数据集的数据集选择器
	 * 
	 * @param owner
	 * @param flag
	 * @param datasetType
	 */
	public DatasetChooser(JFrame owner, boolean flag, String[] datasetWithOutTabular) {
		super(owner, flag);
		this.datasetWithOutTabular = datasetWithOutTabular;
		comboBoxScense = new DatasetComboBox(datasetWithOutTabular);
		initCompanent();
		initResources();
		Datasource datasourceTemp = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		initializeTableInfoWithOutTabular(datasourceTemp);
	}

	private void initResources() {
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDataset"));
		table.getColumnModel().getColumn(COLUMN_INDEX_CURRENT_DATASOURCE).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDatasource"));
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET_TYPE).setHeaderValue(CommonProperties.getString("String_ColumnHeader_DatasetType"));
		labelPath.setText(CoreProperties.getString("String_FormDatasetBrowse_ToolStripLabelPath"));
		labelScense.setText(CoreProperties.getString("String_FormDatasetBrowse_ToolStripLabelDisplayType"));
		buttonSelectAll.setIcon(new ImageIcon(DatasetChooser.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonInvertSelect.setIcon(new ImageIcon(DatasetChooser.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		labelSearch.setIcon(new ImageIcon(DatasetChooser.class.getResource("/com/supermap/desktop/controlsresources/SortType/Image_FindFiles.png")));
		buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		cancelButton.setText(CommonProperties.getString("String_Button_Cancel"));
		buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
	}

	public void initCompanent() {
		setResizable(false);
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspaceTree = new WorkspaceTree(workspace);
		workspaceTree.setMapsNodeVisible(false);
		workspaceTree.setResourcesNodeVisible(false);
		workspaceTree.setScenesNodeVisible(false);
		workspaceTree.setLayoutsNodeVisible(false);
		workspaceTree.addMouseListener(mouseAdapter);
		// 删除不用显示的数据集节点
		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		workspaceTree.expandRow(1);
		for (int i = 0; i < datasourceTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			for (int j = 0; j < childDatasourceTreeNode.getChildCount(); j++) {
				childDatasourceTreeNode.removeAllChildren();
			}
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(490, 280, 677, 456);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(UIManager.getColor("Button.light"));
		toolBar.setRollover(true);
		toolBar.setFloatable(false);

		toolBar.add(labelPath);
		textFieldPath.setEditable(false);

		toolBar.add(textFieldPath);
		textFieldPath.setColumns(10);

		JSeparator separator = new JSeparator();
		toolBar.add(separator);

		toolBar.add(buttonSelectAll);

		JSeparator separatorF = new JSeparator();
		separatorF.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorF);

		toolBar.add(buttonInvertSelect);
		toolBar.add(labelScense);
		buttonInvertSelect.addActionListener(new CommonButtonAction());
		buttonSelectAll.addActionListener(new CommonButtonAction());
		JScrollPane scrollPaneTree = new JScrollPane();
		//@formatter:off
		//toolBar,
		//scrollPaneTree,PanelTable
		//buttonOutport,buttonQuit
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(toolBar)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(scrollPaneTree, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
						.addComponent(buttonOk)
						.addComponent(cancelButton)));
		
		gl_contentPane.setVerticalGroup(gl_contentPane.createSequentialGroup()
				.addComponent(toolBar)
				.addGroup(gl_contentPane.createParallelGroup()
						.addComponent(scrollPaneTree)
						.addComponent(panelTable))
				.addGroup(gl_contentPane.createParallelGroup()
						.addComponent(buttonOk)
						.addComponent(cancelButton)));
		
		gl_contentPane.setAutoCreateContainerGaps(true);
		gl_contentPane.setAutoCreateGaps(true);
		scrollPaneTree.setViewportView(workspaceTree);
		//@formatter:on
		JSeparator separatorS = new JSeparator();
		separatorS.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorS);

		comboBoxScense.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// 关联查询
				comboBoxScense_selectChange();
			}
		});
		comboBoxScense.setMaximumRowCount(20);

		toolBar.add(comboBoxScense);

		JSeparator separatorT = new JSeparator();
		separatorT.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorT);
		textFieldShearch = new JTextField();
		toolBar.add(textFieldShearch);
		textFieldShearch.setColumns(10);
		textFieldShearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				compositeSearch();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				compositeSearch();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				compositeSearch();
			}
		});
		//@formatter:off
		toolBar.add(labelSearch);

		JScrollPane scrollPaneTable = new JScrollPane();
		GroupLayout gl_panelTable = new GroupLayout(panelTable);
		gl_panelTable.setHorizontalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, 424, Short.MAX_VALUE)
		);
		gl_panelTable.setVerticalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
		);
		buttonOk.addActionListener(new CommonButtonAction());
		buttonOk.setActionCommand("OK");
		cancelButton.addActionListener(new CommonButtonAction());

		table = new MutiTable();
		@SuppressWarnings("serial")
		DDLExportTableModel tableModel = new DDLExportTableModel(new String[] {
				"Dataset", "CurrentDatasource", "DatasetType"}) {
			boolean[] columnEditables = new boolean[] { false, false,false};
@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		table.setModel(tableModel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		CommonListCellRenderer render = new CommonListCellRenderer();
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setCellRenderer(render);
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setResizable(true);
		scrollPaneTable.setViewportView(table);
		panelTable.setLayout(gl_panelTable);

		contentPane.setLayout(gl_contentPane);
		//@formatter:on
		// table监听选中行数改变事件
		buttonOk.setEnabled(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				checkButtonOkState();
			}
		});
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				checkButtonOkState();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				checkButtonOkState();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				checkButtonOkState();
			}
		});
	}

	/**
	 * 下拉列表与输入框联合查询的方法入口
	 */
	private void compositeSearch() {
		try {
			ArrayList<Object[]> arrayListForSearch = new ArrayList<Object[]>();
			if (null != datasetWithOutTabular && null != datasource) {
				arrayListForSearch = initializeTableInfoWithOutTabular(datasource);
				searchForContent(arrayListForSearch);
			}
			if (null == datasetTypes && null != datasource && null == datasetWithOutTabular) {
				arrayListForSearch = initializeTableInfo(datasource);
				searchForContent(arrayListForSearch);
			}
			if (null != datasetTypes && null != datasource) {
				arrayListForSearch = initializeTableInfo(datasource, datasetTypes);
				searchForContent(arrayListForSearch);
			}
			if (null == datasource) {
				arrayListForSearch = initializeTableInfo();
				searchForContent(arrayListForSearch);
			}
			checkButtonOkState();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 联合查询的具体实现
	 * 
	 * @param arrayListForSearch
	 */
	private void searchForContent(ArrayList<Object[]> arrayListForSearch) {
		try {
			String selectItem = comboBoxScense.getSelectItem();
			String contentInfo = textFieldShearch.getText();
			int datasetCount = arrayListForSearch.size();
			ArrayList<Object[]> tempData = new ArrayList<Object[]>();
			ArrayList<Object[]> resultData = new ArrayList<Object[]>();
			if (selectItem.equals(CommonProperties.getString("String_DatasetType_All"))) {
				tempData = arrayListForSearch;
			} else {
				for (int i = 0; i < datasetCount; i++) {
					Object[] vector = arrayListForSearch.get(i);
					Object[] data = new Object[3];
					String datasetType = (String) vector[COLUMN_INDEX_DATASET_TYPE];
					if (datasetType.equals(selectItem)) {
						data[COLUMN_INDEX_DATASET] = vector[COLUMN_INDEX_DATASET];
						data[COLUMN_INDEX_CURRENT_DATASOURCE] = vector[COLUMN_INDEX_CURRENT_DATASOURCE];
						data[COLUMN_INDEX_DATASET_TYPE] = datasetType;
						tempData.add(data);
					}
				}
			}
			for (int i = 0; i < tempData.size(); i++) {
				Object[] vector = tempData.get(i);
				Object[] data = new Object[3];
				String datasetName = vector[COLUMN_INDEX_DATASET].toString();
				// 输入框中没有内容或者输入以空格开始
				if (datasetName.toLowerCase().contains(contentInfo.toLowerCase())) {
					data[COLUMN_INDEX_DATASET] = vector[COLUMN_INDEX_DATASET];
					data[COLUMN_INDEX_CURRENT_DATASOURCE] = vector[COLUMN_INDEX_CURRENT_DATASOURCE];
					data[COLUMN_INDEX_DATASET_TYPE] = vector[COLUMN_INDEX_DATASET_TYPE];
					resultData.add(data);
				}
			}
			if (selectItem.equals(CommonProperties.getString("String_DatasetType_All")) && contentInfo.isEmpty()) {
				resultData = arrayListForSearch;
			}
			Object[][] datas = new Object[resultData.size()][];
			for (int i = 0; i < datas.length; i++) {
				datas[i] = resultData.get(i);
			}
			DDLExportTableModel tableModel = (DDLExportTableModel) table.getModel();
			tableModel.refreshContents(datas);
			table.clearSelection();
			table.updateUI();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	/**
	 * 根据不同的数据集类型进行关联查询
	 * 
	 * @param e
	 */
	protected void comboBoxScense_selectChange() {
		try {
			compositeSearch();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 根据数据源数据及指定的数据集类型初始化table
	 * 
	 * @param datasource
	 * @param datasetTypes
	 * @return
	 */
	private ArrayList<Object[]> initializeTableInfo(Datasource datasource, String[] datasetTypes) {
		ArrayList<Object[]> resultList = new ArrayList<Object[]>();
		try {
			textFieldPath.setText(datasource.getConnectionInfo().getServer());
			int datasetCount = datasource.getDatasets().getCount();
			for (int i = 0; i < datasetCount; i++) {
				Dataset dataset = datasource.getDatasets().get(i);
				for (int j = 0; j < datasetTypes.length; j++) {
					DatasetType tempDatasetType = CommonToolkit.DatasetTypeWrap.findType(datasetTypes[j]);
					if (dataset.getType() == tempDatasetType) {
						Object[] data = new Object[3];
						String path = CommonToolkit.DatasetImageWrap.getImageIconPath(dataset.getType());
						DataCell datasetCell = new DataCell(path, dataset.getName());
						data[COLUMN_INDEX_DATASET] = datasetCell;
						data[COLUMN_INDEX_CURRENT_DATASOURCE] = dataset.getDatasource().getAlias();
						data[COLUMN_INDEX_DATASET_TYPE] = CommonToolkit.DatasetTypeWrap.findName(dataset.getType());
						resultList.add(data);
						table.addRow(data);
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultList;
	}

	/**
	 * 根据数据源数据初始化table
	 * 
	 * @param datasource
	 * @return
	 */
	private ArrayList<Object[]> initializeTableInfo(Datasource datasource) {
		ArrayList<Object[]> resultList = new ArrayList<Object[]>();
		try {
			textFieldPath.setText(datasource.getConnectionInfo().getServer());
			int datasetCount = datasource.getDatasets().getCount();
			for (int i = 0; i < datasetCount; i++) {
				Dataset dataset = datasource.getDatasets().get(i);
				Object[] data = new Object[3];
				String path = CommonToolkit.DatasetImageWrap.getImageIconPath(dataset.getType());
				DataCell datasetCell = new DataCell(path, dataset.getName());
				data[COLUMN_INDEX_DATASET] = datasetCell;
				data[COLUMN_INDEX_CURRENT_DATASOURCE] = dataset.getDatasource().getAlias();
				data[COLUMN_INDEX_DATASET_TYPE] = CommonToolkit.DatasetTypeWrap.findName(dataset.getType());
				resultList.add(data);
				table.addRow(data);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultList;
	}

	/**
	 * 根据工作空间中的第一个数据源数据初始化table
	 * 
	 * @param datasource
	 * @return
	 */
	private ArrayList<Object[]> initializeTableInfo() {
		Datasource datasourceTemp = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		return initializeTableInfo(datasourceTemp);
	}

	/**
	 * 过滤掉纯属性数据集
	 * 
	 * @param datasource
	 * @return
	 */
	private ArrayList<Object[]> initializeTableInfoWithOutTabular(Datasource datasource) {
		ArrayList<Object[]> resultList = new ArrayList<Object[]>();
		try {
			textFieldPath.setText(datasource.getConnectionInfo().getServer());
			int datasetCount = datasource.getDatasets().getCount();
			for (int i = 0; i < datasetCount; i++) {
				Dataset dataset = datasource.getDatasets().get(i);
				if (dataset.getType() != DatasetType.TABULAR) {
					Object[] data = new Object[3];
					String path = CommonToolkit.DatasetImageWrap.getImageIconPath(dataset.getType());
					DataCell datasetCell = new DataCell(path, dataset.getName());
					data[COLUMN_INDEX_DATASET] = datasetCell;
					data[COLUMN_INDEX_CURRENT_DATASOURCE] = dataset.getDatasource().getAlias();
					data[COLUMN_INDEX_DATASET_TYPE] = CommonToolkit.DatasetTypeWrap.findName(dataset.getType());
					resultList.add(data);
					table.addRow(data);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultList;
	}

	/**
	 * 点击数据源时切换现有的数据集
	 * 
	 * @author Administrator
	 *
	 */
	class WorkspaceMouseAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			try {
				if (1 == e.getClickCount()) {
					datasource = UICommonToolkit.getDatasource(workspaceTree);
					// 更换数据源后进行一次查询
					compositeSearch();
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	}

	class CommonButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			JComponent c = (JComponent) e.getSource();
			if (c == buttonSelectAll) {
				// 全选
				table.setRowSelectionAllowed(true);
				if (table != null && table.getRowCount() > 0) {
					table.setRowSelectionInterval(0, table.getRowCount() - 1);
				}
				checkButtonOkState();
			} else if (c == buttonInvertSelect) {
				// 反选
				try {
					int[] temp = table.getSelectedRows();
					ArrayList<Integer> selectedRows = new ArrayList<Integer>();
					for (int index = 0; index < temp.length; index++) {
						selectedRows.add(temp[index]);
					}

					ListSelectionModel selectionModel = table.getSelectionModel();
					selectionModel.clearSelection();
					for (int index = 0; index < table.getRowCount(); index++) {
						if (!selectedRows.contains(index)) {
							selectionModel.addSelectionInterval(index, index);
						}
					}
					checkButtonOkState();
				} catch (Exception ex) {
					Application.getActiveApplication().getOutput().output(ex);
				}
			} else if (c == cancelButton) {
				// 关闭
				workspaceTree.removeMouseListener(mouseAdapter);
				dispose();
			}

		}

	}

	public JTextField getTextFieldShearch() {
		return textFieldShearch;
	}

	public void setTextFieldShearch(JTextField textFieldShearch) {
		this.textFieldShearch = textFieldShearch;
	}

	public DatasetComboBox getComboBoxScense() {
		return comboBoxScense;
	}

	public void setComboBoxScense(DatasetComboBox comboBoxScense) {
		this.comboBoxScense = comboBoxScense;
	}

	public JTextField getTextFieldPath() {
		return textFieldPath;
	}

	public void setTextFieldPath(JTextField textFieldPath) {
		this.textFieldPath = textFieldPath;
	}

	public MutiTable getTable() {
		return table;
	}

	public void setTable(MutiTable table) {
		this.table = table;
	}

	public JButton getOkButton() {
		return buttonOk;
	}

	public void setOkButton(JButton okButton) {
		this.buttonOk = okButton;
	}

	public void checkButtonOkState() {
		if (table.getSelectedRowCount() > 0) {
			buttonOk.setEnabled(true);
		} else {
			buttonOk.setEnabled(false);
		}
	}
}
