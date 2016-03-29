package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.SortTable.SortTable;
import com.supermap.desktop.ui.controls.SortTable.SortableTableModel;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasetCellRender;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasourceCellRender;
import com.supermap.desktop.utilties.DatasetTypeUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.desktop.utilties.TableUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据集选择界面
 * <p/>
 *
 * @author XiaJT
 */
public class DatasetChooser extends SmDialog {
	private static final long serialVersionUID = 1L;
	protected SortTable table;
	protected MySortableTableModel tableModel;
	protected SmButton buttonOk = new SmButton("string_button_sure");
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private SmButton cancelButton = new SmButton("string_button_quit");

	private JLabel labelPath = new JLabel("String_FormDatasetBrowse_ToolStripLabelPath");
	private JLabel labelScense = new JLabel("String_FormDatasetBrowse_ToolStripLabelDisplayType");
	private JLabel labelSearch = new JLabel();

	private JPanel panelTable = new JPanel();

	private JTextField textFieldSearch;
	private JTextField textFieldPath = new JTextField();

	private WorkspaceTree workspaceTree;

	private DatasetTypeComboBox datasetTypeComboBox;

	protected transient WorkspaceSelectChangeListener selectChangeListener = new WorkspaceSelectChangeListener();

	protected static final int COLUMN_INDEX_DATASET = 0;

	protected static final int COLUMN_INDEX_CURRENT_DATASOURCE = 1;

	protected static final int COLUMN_INDEX_DATASET_TYPE = 2;

	protected transient Datasource datasource;

	private List<Dataset> selectedDatasets = new ArrayList<>();

	private WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			dispose();
			removeWindowListener(this);
		}
	};

	public DatasetChooser() {
		init();
	}

	public DatasetChooser(JDialog owner) {
		super(owner, true);
		init();
	}

	private void init() {

		setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		setSize(677, 456);
		this.setLocationRelativeTo(null);
		initComponent();
		initResources();
		initComponentStates();
	}

	public void initComponent() {
		this.getRootPane().setDefaultButton(buttonOk);
		setResizable(false);
		initWorkspaceTree();


		JPanel contentPane = new JPanel();
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
		datasetTypeComboBox = new DatasetTypeComboBox();
		datasetTypeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// 关联查询
				compositeSearch();
			}
		});
		datasetTypeComboBox.setMaximumRowCount(10);

		toolBar.add(datasetTypeComboBox);

		JSeparator separatorT = new JSeparator();
		separatorT.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorT);
		textFieldSearch = new JTextField();
		toolBar.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
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

		table = new SortTable();
		tableModel = new MySortableTableModel();
		table.setModel(tableModel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
//		CommonListCellRenderer render = new CommonListCellRenderer();
		table.getColumnModel().getColumn(MySortableTableModel.COLUMN_DATASET_NAME).setCellRenderer(new TableDatasetCellRender());
		table.getColumnModel().getColumn(MySortableTableModel.COLUMN_DATASOURCE).setCellRenderer(new TableDatasourceCellRender());
//		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setCellRenderer(render);
//		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setResizable(true);
		scrollPaneTable.setViewportView(table);
		panelTable.setLayout(gl_panelTable);

		contentPane.setLayout(gl_contentPane);
		//@formatter:on
		// table监听选中行数改变事件
		buttonOk.setEnabled(false);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonOkState();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					// 双击关闭
					buttonOkClicked();
				}
			}
		});
	}

	private void initWorkspaceTree() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspaceTree = new WorkspaceTree(workspace);
		workspaceTree.setMapsNodeVisible(false);
		workspaceTree.setResourcesNodeVisible(false);
		workspaceTree.setScenesNodeVisible(false);
		workspaceTree.setLayoutsNodeVisible(false);
		workspaceTree.addTreeSelectionListener(selectChangeListener);
		// 删除不用显示的数据集节点
		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		workspaceTree.expandRow(1);
		for (int i = datasourceTreeNode.getChildCount() - 1; i >= 0; i--) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			for (int j = 0; j < childDatasourceTreeNode.getChildCount(); j++) {
				childDatasourceTreeNode.removeAllChildren();
			}

			Datasource datasource = (Datasource) ((TreeNodeData) childDatasourceTreeNode.getUserObject()).getData();
			if (!isSupportDatasource(datasource)) {
				datasourceTreeNode.remove(childDatasourceTreeNode);
			}
		}
		// 不可编辑
		workspaceTree.setEditable(false);

		// 拖拽监听事件删除
		for (MouseMotionListener mouseMotionListener : this.workspaceTree.getMouseMotionListeners()) {
			this.workspaceTree.removeMouseMotionListener(mouseMotionListener);
		}

		// 移除按键删除节点的监听器
		for (KeyListener keyListener : this.workspaceTree.getKeyListeners()) {
			this.workspaceTree.removeKeyListener(keyListener);
		}
		this.workspaceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		workspaceTree.updateUI();
	}

	private void initResources() {
//		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDataset"));
//		table.getColumnModel().getColumn(COLUMN_INDEX_CURRENT_DATASOURCE).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDatasource"));
//		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET_TYPE).setHeaderValue(CommonProperties.getString("String_ColumnHeader_DatasetType"));
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

	private void initComponentStates() {
		datasetTypeComboBox.setSelectedIndex(0);

		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		if (datasourceTreeNode.getChildCount() > 0) {
			workspaceTree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) datasourceTreeNode.getChildAt(0)).getPath()));
		}

	}

	/**
	 * 判断数据源是否显示
	 *
	 * @param datasource 数据源
	 * @return true 显示 / false 不显示
	 */
	protected boolean isSupportDatasource(Datasource datasource) {
		return true;
	}

	@Override
	public void dispose() {
		this.workspaceTree.removeTreeSelectionListener(this.selectChangeListener);
		this.workspaceTree.dispose();
		super.dispose();
	}

	@Override
	public DialogResult showDialog() {
		this.workspaceTree.removeTreeSelectionListener(this.selectChangeListener);
		this.workspaceTree.addTreeSelectionListener(this.selectChangeListener);
		this.table.clearSelection();
		this.selectedDatasets.clear();
		this.addWindowListener(this.windowAdapter);
		return super.showDialog();
	}

	public List<Dataset> getSelectedDatasets() {
		return this.selectedDatasets;
	}

	/**
	 * 下拉列表与输入框联合查询的方法入口
	 */
	private void compositeSearch() {
		try {
			this.table.clearSelection();
			this.tableModel.removeAll();
			initializeTableInfo();
			checkButtonOkState();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 初始化表格信息
	 */
	private void initializeTableInfo() {
		if (this.workspaceTree.getLastSelectedPathComponent() != null) {
			TreeNodeData userObject = (TreeNodeData) ((DefaultMutableTreeNode) this.workspaceTree.getLastSelectedPathComponent()).getUserObject();
			if (userObject != null && userObject.getData() instanceof Datasource) {
				Datasource datasource = (Datasource) userObject.getData();
				Datasets datasets = datasource.getDatasets();
				for (int i = 0; i < datasets.getCount(); i++) {
					Dataset dataset = datasets.get(i);
					if (isAllowedDataset(dataset)) {
						this.tableModel.addDataset(dataset);
					}
					if (dataset instanceof DatasetVector && ((DatasetVector) dataset).getChildDataset() != null) {
						DatasetVector childDataset = ((DatasetVector) dataset).getChildDataset();
						if (isAllowedDataset(childDataset)) {
							this.tableModel.addDataset(childDataset);
						}
					}
				}
			}
		}
	}

	private boolean isAllowedDataset(Dataset dataset) {
		return isAllowedDatasetType(dataset.getType()) && isAllowedDatasetName(dataset.getName()) && isAllowedDatasetShown(dataset);
	}

	private boolean isAllowedDatasetType(DatasetType type) {
		DatasetType[] selectedDatasetTypes = this.datasetTypeComboBox.getSelectedDatasetTypes();
		for (DatasetType selectedDatasetType : selectedDatasetTypes) {
			if (selectedDatasetType == type) {
				return true;
			}
		}
		return false;
	}

	private boolean isAllowedDatasetName(String name) {
		String text = this.textFieldSearch.getText().toLowerCase();
		return StringUtilties.isNullOrEmpty(text) || name.toLowerCase().contains(text);
	}

	/**
	 * 判断数据集是否显示
	 *
	 * @param dataset 数据集
	 * @return true显示
	 */
	protected boolean isAllowedDatasetShown(Dataset dataset) {
		return true;
	}

	public void setSelectedDatasource(Datasource datasource) {
		this.workspaceTree.setSelectedDatasource(datasource);
	}

	class WorkspaceSelectChangeListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			compositeSearch();

			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
			if (null != selectedNode) {
				TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
				if (null != selectedNodeData && selectedNodeData.getData() instanceof Datasource) {
					DatasetChooser.this.datasource = (Datasource) selectedNodeData.getData();
					DatasetChooser.this.textFieldPath.setText(DatasetChooser.this.datasource.getConnectionInfo().getServer());
				}
			}
		}
	}


	private void buttonOkClicked() {
		setDialogResult(DialogResult.OK);
		resetSelectDataset();
		dispose();
	}

	private void resetSelectDataset() {
		int[] selectedRows = this.table.getSelectedRows();
		this.selectedDatasets.clear();
		this.selectedDatasets = this.tableModel.getSelectedDatasets(selectedRows);
	}

	public void checkButtonOkState() {
		this.buttonOk.setEnabled(this.table.getSelectedRowCount() > 0);
	}

	public void setSupportDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypeComboBox.setDatasetTypes(datasetTypes);
		compositeSearch();
	}

	private class CommonButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			JComponent c = (JComponent) e.getSource();
			if (c == DatasetChooser.this.buttonSelectAll) {
				// 全选
				DatasetChooser.this.table.setRowSelectionAllowed(true);
				if (DatasetChooser.this.table != null && DatasetChooser.this.table.getRowCount() > 0) {
					DatasetChooser.this.table.setRowSelectionInterval(0, DatasetChooser.this.table.getRowCount() - 1);
				}
			} else if (c == DatasetChooser.this.buttonInvertSelect) {
				// 反选
				TableUtilties.invertSelection(DatasetChooser.this.table);
			} else if (c == DatasetChooser.this.cancelButton) {
				// 关闭
				setDialogResult(DialogResult.CANCEL);
				dispose();
			} else if (c == DatasetChooser.this.buttonOk) {
				// 确定
				buttonOkClicked();
			}
		}
	}

	private class MySortableTableModel extends SortableTableModel {
		private List<Dataset> datasetList = new ArrayList<>();
		public static final int COLUMN_DATASET_NAME = 0;
		public static final int COLUMN_DATASOURCE = 1;
		public static final int COLUMN_DATASET_TYPE = 2;

		private final String[] columnNames = {
				CommonProperties.getString(CommonProperties.stringDataset),
				CommonProperties.getString(CommonProperties.stringDatasource),
				CommonProperties.getString(CommonProperties.STRING_DATASET_TYPE)
		};

		@Override
		public Object getValueAt(int row, int col) {
			if (this.datasetList == null || this.datasetList.size() <= 0) {
				return null;
			}
			row = getIndexRow(row)[0];
			Dataset dataset = this.datasetList.get(row);
			switch (col) {
				case COLUMN_DATASET_NAME:
					return dataset;
				case COLUMN_DATASOURCE:
					return dataset.getDatasource();
				case COLUMN_DATASET_TYPE:
					return DatasetTypeUtilties.toString(dataset.getType());
				default:
					return null;
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public int getRowCount() {
			if (this.datasetList == null) {
				return 0;
			}
			return this.datasetList.size();
		}

		@Override
		public int getColumnCount() {
			return this.columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return this.columnNames[column];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == COLUMN_DATASET_NAME || columnIndex == COLUMN_DATASOURCE) {
				return DataCell.class;
			} else {
				return String.class;
			}
		}

		public void removeAll() {
			int[] deleteRows = new int[this.datasetList.size()];
			for (int i = 0; i < this.datasetList.size(); i++) {
				deleteRows[i] = i;
			}
			super.removeRows(deleteRows);
			this.datasetList.clear();
			fireTableDataChanged();
		}

		public void addDataset(Dataset dataset) {
			if (this.datasetList == null) {
				this.datasetList = new ArrayList<>();
			}
			this.datasetList.add(dataset);
			super.addIndexRow(getRowCount() - 1);
			fireTableDataChanged();
		}

		public List<Dataset> getSelectedDatasets(int[] selectedRows) {
			selectedRows = getIndexRow(selectedRows);
			List<Dataset> resultDataset = new ArrayList<>();
			for (int selectedRow : selectedRows) {
				resultDataset.add(this.datasetList.get(selectedRow));
			}
			return resultDataset;
		}
	}
}
