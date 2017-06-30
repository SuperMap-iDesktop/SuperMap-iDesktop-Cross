package com.supermap.desktop.ui.controls.datasetChoose;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据集选择界面
 * <p/>
 *
 * @author XiaJT
 */
public class DatasetChooser extends SmDialog {
	private static final long serialVersionUID = 1L;
	protected SmSortTable table;
	protected SmButton buttonOk = new SmButton("string_button_sure");
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private SmButton buttonCancel = new SmButton("string_button_quit");

	private JLabel labelPath = new JLabel("String_FormDatasetBrowse_ToolStripLabelPath");
	private JLabel labelScense = new JLabel("String_FormDatasetBrowse_ToolStripLabelDisplayType");

	private JPanel panelTable = new JPanel();

	private JTextField textFieldSearch = new TextFieldSearch();
	private JTextField textFieldPath = new JTextField();

	private WorkspaceTree workspaceTree;

	private DatasetTypeComboBox datasetTypeComboBox;

	protected transient WorkspaceSelectChangeListener selectChangeListener = new WorkspaceSelectChangeListener();

	protected static final int COLUMN_INDEX_DATASET = 0;

	protected static final int COLUMN_INDEX_CURRENT_DATASOURCE = 1;

	protected static final int COLUMN_INDEX_DATASET_TYPE = 2;

	protected transient Datasource datasource;

	private List<Object> selectedDatasets = new ArrayList<>();

	private HashMap<DatasetChooseMode, IDatasetChoose> datasetChooses = new HashMap<>();

	private IDatasetChoose currentDatasetChoose;

	public DatasetChooser(DatasetChooseMode... chooseModes) {
		init(chooseModes);
	}

	public DatasetChooser(JDialog owner, DatasetChooseMode... chooseModes) {
		super(owner, true);
		init(chooseModes);
	}


	private void init(DatasetChooseMode... chooseModes) {
		if (chooseModes == null || chooseModes.length == 0) {
			chooseModes = new DatasetChooseMode[]{DatasetChooseMode.DATASET};
		}
		for (DatasetChooseMode chooseMode : chooseModes) {
			if (chooseMode == DatasetChooseMode.DATASET) {
				datasetChooses.put(DatasetChooseMode.DATASET, new DatasetChooserDataset(this));
			} else if (chooseMode == DatasetChooseMode.MAP) {
				datasetChooses.put(DatasetChooseMode.MAP, new DatasetChooserMap(this));
			} else if (chooseMode == DatasetChooseMode.SCENE) {
				datasetChooses.put(DatasetChooseMode.SCENE, new DatasetChooserScene(this));
			} else if (chooseMode == DatasetChooseMode.LAYOUT) {
				datasetChooses.put(DatasetChooseMode.LAYOUT, new DatasetChooserLayout(this));
			}
		}

		setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		setSize(677, 456);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		initComponent();
		initResources();
		initComponentStates();
		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(policy);
		this.getRootPane().setDefaultButton(this.buttonCancel);
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

		toolBar.add(this.labelPath);
		this.textFieldPath.setEditable(false);

		toolBar.add(this.textFieldPath);

		JSeparator separator = new JSeparator();
		toolBar.add(separator);

		toolBar.add(this.buttonSelectAll);

		JSeparator separatorF = new JSeparator();
		separatorF.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorF);

		toolBar.add(this.buttonInvertSelect);
		toolBar.add(this.labelScense);
		this.buttonInvertSelect.addActionListener(new CommonButtonAction());
		this.buttonSelectAll.addActionListener(new CommonButtonAction());
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
						.addComponent(buttonCancel)));

		gl_contentPane.setVerticalGroup(gl_contentPane.createSequentialGroup()
				.addComponent(toolBar)
				.addGroup(gl_contentPane.createParallelGroup()
						.addComponent(scrollPaneTree)
						.addComponent(panelTable))
				.addGroup(gl_contentPane.createParallelGroup()
						.addComponent(buttonOk)
						.addComponent(buttonCancel)));

		gl_contentPane.setAutoCreateContainerGaps(true);
		gl_contentPane.setAutoCreateGaps(true);
		scrollPaneTree.setViewportView(workspaceTree);
		//@formatter:on
		JSeparator separatorS = new JSeparator();
		separatorS.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorS);
		this.datasetTypeComboBox = new DatasetTypeComboBox();
		this.datasetTypeComboBox.setAllShown(true);
		this.datasetTypeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// 关联查询
				compositeSearch();
			}
		});
		this.datasetTypeComboBox.setMaximumRowCount(10);
		this.datasetTypeComboBox.setPreferredSize(new Dimension(140, 20));
		toolBar.add(this.datasetTypeComboBox);

		toolBar.add(this.textFieldSearch);
		this.textFieldSearch.setPreferredSize(new Dimension(140, 20));

		this.textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
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
		//@formatter:on
		this.buttonOk.addActionListener(new CommonButtonAction());
		this.buttonOk.setActionCommand("OK");
		this.buttonCancel.addActionListener(new CommonButtonAction());

		this.table = new SmSortTable();
		table.setAutoCreateRowSorter(true);

		this.table.setShowHorizontalLines(false);
		this.table.setShowVerticalLines(false);
		scrollPaneTable.setViewportView(this.table);
		this.panelTable.setLayout(gl_panelTable);

		contentPane.setLayout(gl_contentPane);

		this.buttonOk.setEnabled(false);
		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonOkState();
			}
		});
		this.table.addMouseListener(new MouseAdapter() {
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
		this.workspaceTree = new WorkspaceTree(workspace);
		if (datasetChooses.get(DatasetChooseMode.DATASET) == null) {
			this.workspaceTree.setDatasourcesNodeVisible(false);
		} else {
			// 删除不用显示的数据集节点
			DefaultTreeModel treeModel = (DefaultTreeModel) this.workspaceTree.getModel();
			MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
			MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
			this.workspaceTree.expandRow(1);
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
		}
		if (datasetChooses.get(DatasetChooseMode.MAP) == null) {
			this.workspaceTree.setMapsNodeVisible(false);
		} else {
			// 删除不用显示的地图节点
			DefaultTreeModel treeModel = (DefaultTreeModel) this.workspaceTree.getModel();
			MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
			MutableTreeNode mapNode = (MutableTreeNode) treeNode.getChildAt(1);
			((DefaultMutableTreeNode) mapNode).removeAllChildren();
		}
		if (datasetChooses.get(DatasetChooseMode.SCENE) == null) {
			this.workspaceTree.setScenesNodeVisible(false);
		}
		if (datasetChooses.get(DatasetChooseMode.LAYOUT) == null) {
			this.workspaceTree.setLayoutsNodeVisible(false);
		}
		this.workspaceTree.setResourcesNodeVisible(false);
		this.workspaceTree.addTreeSelectionListener(this.selectChangeListener);

		// 不可编辑
		this.workspaceTree.setEditable(false);

		// 拖拽监听事件删除
		for (MouseMotionListener mouseMotionListener : this.workspaceTree.getMouseMotionListeners()) {
			this.workspaceTree.removeMouseMotionListener(mouseMotionListener);
		}

		// 移除按键删除节点的监听器
		for (KeyListener keyListener : this.workspaceTree.getKeyListeners()) {
			this.workspaceTree.removeKeyListener(keyListener);
		}
		this.workspaceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.workspaceTree.updateUI();
	}

	private void initResources() {
		this.labelPath.setText(CoreProperties.getString("String_FormDatasetBrowse_ToolStripLabelPath"));
		this.labelScense.setText(CoreProperties.getString("String_FormDatasetBrowse_ToolStripLabelDisplayType"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
	}

	private void initComponentStates() {
		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		if (datasetChooses.get(DatasetChooseMode.DATASET) != null) {
			this.datasetTypeComboBox.setSelectedIndex(0);
			MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
			if (datasourceTreeNode.getChildCount() > 0) {
				this.workspaceTree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) datasourceTreeNode.getChildAt(0)).getPath()));
			}
		} else {
			datasetTypeComboBox.removeAllItems();
			datasetTypeComboBox.setEnabled(false);
			if (datasetChooses.get(DatasetChooseMode.MAP) != null) {
				MutableTreeNode mapTreeNode = (MutableTreeNode) treeNode.getChildAt(1);
				this.workspaceTree.setSelectionPath(new TreePath(mapTreeNode));
			}
		}


	}

	/**
	 * 判断数据源是否显示
	 *
	 * @param datasource 数据源
	 * @return true 显示 / false 不显示
	 */
	protected boolean isSupportDatasource(Datasource datasource) {
		// 为了方便不放入DatasetChooserDataset中
		return true;
	}

	@Override
	public void dispose() {
		this.workspaceTree.removeTreeSelectionListener(this.selectChangeListener);
		this.workspaceTree.dispose();
		for (IDatasetChoose iDatasetChoose : datasetChooses.values()) {
			iDatasetChoose.dispose();
		}
		super.dispose();
	}

	@Override
	public DialogResult showDialog() {
		this.workspaceTree.removeTreeSelectionListener(this.selectChangeListener);
		this.workspaceTree.addTreeSelectionListener(this.selectChangeListener);
		this.table.clearSelection();
		this.selectedDatasets.clear();
		return super.showDialog();
	}

	/**
	 * 默认的数据集选择器可使用此方法获取选中数据集
	 *
	 * @return
	 */
	public List<Dataset> getSelectedDatasets() {
		ArrayList<Dataset> result = new ArrayList<>();
		for (Object selectedDataset : selectedDatasets) {
			if (selectedDataset instanceof Dataset) {
				result.add((Dataset) selectedDataset);
			}
		}
		return result;
	}

	/**
	 * 可选择地图/场景/布局的选择器请使用此方法自行判断对象类型
	 *
	 * @return
	 */
	public List<Object> getSelectedObjects() {
		return this.selectedDatasets;
	}

	/**
	 * 下拉列表与输入框联合查询的方法入口
	 */
	private void compositeSearch() {
		try {
			selectedDatasets.clear();
			this.table.clearSelection();
			if (this.workspaceTree.getLastSelectedPathComponent() != null) {
				TreeNodeData userObject = (TreeNodeData) ((DefaultMutableTreeNode) this.workspaceTree.getLastSelectedPathComponent()).getUserObject();
				if (userObject == null) {
					return;
				}
				currentDatasetChoose.initializeTableInfo(userObject.getData());
			}
			checkButtonOkState();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}


	protected boolean isAllowedDataset(Dataset dataset) {
		return isAllowedDatasetType(dataset.getType()) && isAllowedSearchName(dataset.getName()) && isAllowedDatasetShown(dataset);
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

	protected boolean isAllowedSearchName(String name) {
		String text = this.textFieldSearch.getText().toLowerCase();
		return StringUtilities.isNullOrEmpty(text) || name.toLowerCase().contains(text);
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

	protected JTable getTable() {
		return table;
	}

	class WorkspaceSelectChangeListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (null != e.getNewLeadSelectionPath()) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
				if (null != selectedNode) {
					TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
					if (null == selectedNodeData) {
						return;
					}
					if (selectedNodeData.getData() instanceof Datasource) {
						setCurrentDatasetChoose(datasetChooses.get(DatasetChooseMode.DATASET));
						DatasetChooser.this.datasource = (Datasource) selectedNodeData.getData();
						DatasetChooser.this.textFieldPath.setText(DatasetChooser.this.datasource.getConnectionInfo().getServer());
					} else if (selectedNodeData.getData() instanceof Maps) {
						setCurrentDatasetChoose(datasetChooses.get(DatasetChooseMode.MAP));
						DatasetChooser.this.datasource = null;
						DatasetChooser.this.textFieldPath.setText("");
					} else if (selectedNodeData.getData() instanceof Scenes) {
						setCurrentDatasetChoose(datasetChooses.get(DatasetChooseMode.SCENE));
						DatasetChooser.this.datasource = null;
						DatasetChooser.this.textFieldPath.setText("");
					} else if (selectedNodeData.getData() instanceof Layouts) {
						setCurrentDatasetChoose(datasetChooses.get(DatasetChooseMode.LAYOUT));
						DatasetChooser.this.datasource = null;
						DatasetChooser.this.textFieldPath.setText("");
					}
				}
			}
		}
	}

	private void setCurrentDatasetChoose(IDatasetChoose currentDatasetChoose) {
		if (currentDatasetChoose != this.currentDatasetChoose) {
			this.currentDatasetChoose = currentDatasetChoose;
			currentDatasetChoose.initTable();
			selectedDatasets.clear();
		}
		compositeSearch();
	}

	private void buttonOkClicked() {
		setDialogResult(DialogResult.OK);
		resetSelectDataset();
		dispose();
	}

	private void resetSelectDataset() {
		int[] selectedRows = this.table.getSelectedModelRows();
		this.selectedDatasets.clear();
		this.selectedDatasets = this.currentDatasetChoose.getSelectedValues(selectedRows);
	}

	public void checkButtonOkState() {
		if (this.table.getSelectedRowCount() > 0) {
			this.buttonOk.setEnabled(true);
			getRootPane().setDefaultButton(this.buttonOk);
		}
	}

	public void setSupportDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypeComboBox.setSupportedDatasetTypes(datasetTypes);
		compositeSearch();
	}

	public void setSelectionModel(int selectionModel) {
		this.table.setSelectionMode(selectionModel);
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
				TableUtilities.invertSelection(DatasetChooser.this.table);
			} else if (c == DatasetChooser.this.buttonCancel) {
				// 关闭
				cancelButtonClicked();
			} else if (c == DatasetChooser.this.buttonOk) {
				// 确定
				buttonOkClicked();
			}
		}
	}


	public WorkspaceTree getWorkspaceTree() {
		return workspaceTree;
	}

	private void cancelButtonClicked() {
		setDialogResult(DialogResult.CANCEL);
		dispose();
	}


}
