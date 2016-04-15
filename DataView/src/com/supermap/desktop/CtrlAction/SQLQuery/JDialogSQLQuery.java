package com.supermap.desktop.CtrlAction.SQLQuery;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.JoinItems;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.SQLQuery.components.FieldInfoTable;
import com.supermap.desktop.CtrlAction.SQLQuery.components.ISQLBuildComponent;
import com.supermap.desktop.CtrlAction.SQLQuery.components.PanelSaveSearchResult;
import com.supermap.desktop.CtrlAction.SQLQuery.components.SQLTable;
import com.supermap.desktop.CtrlAction.SQLQuery.components.SQLTextarea;
import com.supermap.desktop.CtrlAction.SQLQuery.components.SQLTextfield;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.dialogs.dialogJoinItems.JDialogJoinItems;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.desktop.utilties.XmlUtilties;
import com.supermap.mapping.Layer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * SQL查询主界面
 *
 * @author xiajt
 */
public class JDialogSQLQuery extends SmDialog {
	// region 定义成员变量
	/**
	 * 直接添加
	 */
	public static final int ADD_DIRECT = 1;
	/**
	 * 需要考虑加','的情况，并将光标放置在括号中
	 */
	public static final int ADD_FUNCTION_OR_FIELD = 2;
	/**
	 * 添加运算符号，需要判断位置是否需要在中间
	 */
	public static final int ADD_OPERATOR = 3;

	private Dataset currentDataset = null;

	private JPanel panelContent = new JPanel();
	private JPanel panelSelectSearchData = new JPanel();
	private JPanel panelShowResult = new JPanel();
	private PanelSaveSearchResult panelSaveSearchResult = new PanelSaveSearchResult();
	private JPanel panelButton = new JPanel();

	private JLabel labelFieldInfo = new JLabel("fieldInfo");
	private JLabel labelQueryMode = new JLabel("queryMode");
	private JLabel labelOperator = new JLabel("operator");
	private JLabel labelCommonFunction = new JLabel("commonFunction");
	private JLabel labelGoTO = new JLabel("goTO");
	private JLabel labelQueryField = new JLabel("queryField");
	private JLabel labelQueryCondition = new JLabel("queryCondition");
	private JLabel labelGroupField = new JLabel("groupField");
	private JLabel labelOrderByField = new JLabel("orderByField");

	private SmButton buttonJoinItems = new SmButton("joinItem");
	private SmButton buttonImport = new SmButton("import");
	private SmButton buttonExport = new SmButton("export");
	private JButton buttonGetAllValue = new JButton("getAllValue");
	private SmButton buttonQuery = new SmButton("query");
	private SmButton buttonClear = new SmButton("clear");
	private SmButton buttonClose = new SmButton("close");

	private JTextField textFieldGOTO = new JTextField();
	private SQLTextfield textFieldGroupField = new SQLTextfield();

	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton radioButtonQuerySpaceAndProperty = new JRadioButton("querySpaceAndProperty");
	private JRadioButton radioButtonQueryAttributeInfo = new JRadioButton("queryAttributeInfo");

	private JCheckBox checkBoxShowTabular = new JCheckBox("showTabular");
	private JCheckBox checkBoxHighLigthMap = new JCheckBox("highLigthMap");
	private JCheckBox checkBoxHighLigthScene = new JCheckBox("highLightScene");

	private JComboBox<String> jComboBoxOperator = new JComboBox<String>();
	private JComboBox<String> jComboBoxAggregationFunction = new JComboBox<String>();
	private JComboBox<String> jComboBoxMathsOperation = new JComboBox<String>();
	private JComboBox<String> jComboBoxStringFunction = new JComboBox<String>();
	private JComboBox<String> jComboBoxTimeFunction = new JComboBox<String>();

	// 参与查询的数据
	private JScrollPane scrollPaneWorkspaceTree = new JScrollPane();
	private WorkspaceTree workspaceTree;
	// 字段信息
	private JScrollPane scrollPaneFieldInfo = new JScrollPane();
	private FieldInfoTable tableFieldInfo = new FieldInfoTable();
	// 获取唯一值
	private JScrollPane scrollPaneAllValue = new JScrollPane();
	private GetAllValueList listAllValue;
	// 查询字段
	private JScrollPane scrollPaneQueryField = new JScrollPane();
	private SQLTextarea textareaQueryField = new SQLTextarea();
	// 查询条件
	private JScrollPane scrollPaneQueryCondition = new JScrollPane();
	private SQLTextarea textareaQueryCondition = new SQLTextarea();
	// 排序字段
	private JScrollPane scrollPaneOrderByField = new JScrollPane();
	private SQLTable sqlTableOrderByField = new SQLTable();

	private ISQLBuildComponent lastComponent = textareaQueryField;
	private JoinItems joinItems = new JoinItems();


	// endregion

	public JDialogSQLQuery() {
		super();
		this.setTitle(DataViewProperties.getString("String_SQLFormSQLQuery"));
		this.setSize(new Dimension(800, 750));
		initComponents();
		initLayout();
		initComponentsState();
		initResources();
		registListeners();
		initUndoComponents();
		this.setLocationRelativeTo(null);
		this.componentList.add(this.buttonQuery);
		this.componentList.add(this.buttonClear);
		this.componentList.add(this.buttonClose);
		this.setFocusTraversalPolicy(policy);
		this.getRootPane().setDefaultButton(this.buttonClose);
	}

	/**
	 * 初始化控件
	 */
	private void initComponents() {
		buttonJoinItems.setUseDefaultSize(false);
		initWorkspaceTree();
		initTableFieldInfo();
		initGetAllValue();
		initQueryField();
		initQueryCondition();
		initOrderByField();

		initJComboBoxOperator();
		initJComboBoxAggregationFunction();
		initJComboBoxMathsOperation();
		initJComboBoxStringFunction();
		initJComboBoxTimeFunction();

		buttonGroup.add(radioButtonQueryAttributeInfo);
		buttonGroup.add(radioButtonQuerySpaceAndProperty);
		this.getRootPane().setDefaultButton(buttonQuery);
	}

	/**
	 * 初始化控件状态
	 */
	private void initComponentsState() {
		radioButtonQuerySpaceAndProperty.setSelected(true);
		jComboBoxAggregationFunction.setEnabled(false);
		buttonGetAllValue.setEnabled(false);
		textFieldGOTO.setEnabled(false);
		textFieldGroupField.setEnabled(false);
		checkBoxShowTabular.setSelected(true);
		buttonQuery.setEnabled(false);
		if (Application.getActiveApplication().getActiveDatasets().length > 0) {
			currentDataset = Application.getActiveApplication().getActiveDatasets()[0];
			setWorkspaceTreeSelectedDataset(currentDataset);
			// panelSaveSearchResult.setSelectedDatasources(currentDataset.getDatasource());
			// 初始化字段信息表
			tableFieldInfo.setDataset(currentDataset);
			if (currentDataset.getType() == DatasetType.TABULAR) {
				radioButtonQuerySpaceAndProperty.setEnabled(false);
				radioButtonQueryAttributeInfo.setSelected(true);
			}
		} else if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			// panelSaveSearchResult.setSelectedDatasources(Application.getActiveApplication().getActiveDatasources()[0]);
			setWorkspaceTreeSelectedDatasources(Application.getActiveApplication().getActiveDatasources()[0]);
		}
	}

	private void setWorkspaceTreeSelectedDatasources(Datasource datasource) {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		for (int i = 0; i < datasourceTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			TreeNodeData selectedDatasourceNodeData = (TreeNodeData) childDatasourceTreeNode.getUserObject();
			if (null != selectedDatasourceNodeData && selectedDatasourceNodeData.getData() instanceof Datasource) {
				Datasource datasourceTemp = (Datasource) selectedDatasourceNodeData.getData();
				if (datasourceTemp == datasource) {
					TreePath path = new TreePath(childDatasourceTreeNode.getPath());
					workspaceTree.setSelectionPath(path);
					workspaceTree.scrollPathToVisible(path);
					workspaceTree.expandPath(path);
					return;
				}
			}
		}
	}

	/**
	 * 选中指定数据集
	 *
	 * @param dataset 需要选中的数据集
	 */

	private void setWorkspaceTreeSelectedDataset(Dataset dataset) {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		for (int i = 0; i < datasourceTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			TreeNodeData selectedDatasourceNodeData = (TreeNodeData) childDatasourceTreeNode.getUserObject();
			if (null != selectedDatasourceNodeData && selectedDatasourceNodeData.getData() instanceof Datasource) {
				Datasource datasource = (Datasource) selectedDatasourceNodeData.getData();
				if (datasource == dataset.getDatasource()) {
					for (int j = 0; j < childDatasourceTreeNode.getChildCount(); j++) {
						DefaultMutableTreeNode childDatasetTreeNode = (DefaultMutableTreeNode) childDatasourceTreeNode.getChildAt(j);
						TreeNodeData selectedNodeData = (TreeNodeData) childDatasetTreeNode.getUserObject();
						if (null != selectedNodeData && selectedNodeData.getData() instanceof Dataset && selectedNodeData.getData() == dataset) {
							TreePath path = new TreePath(childDatasetTreeNode.getPath());
							workspaceTree.setSelectionPath(path);
							workspaceTree.scrollPathToVisible(path);
							return;
						} else if (childDatasetTreeNode.getChildCount() > 0) {
							for (int k = 0; k < childDatasetTreeNode.getChildCount(); k++) {
								DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode) childDatasetTreeNode.getChildAt(k);
								TreeNodeData nodeData = (TreeNodeData) childTreeNode.getUserObject();
								if (null != nodeData && nodeData.getData() instanceof Dataset && nodeData.getData() == dataset) {
									TreePath path = new TreePath(childTreeNode.getPath());
									workspaceTree.setSelectionPath(path);
									workspaceTree.scrollPathToVisible(path);
									return;
								}
							}
						}
					}
					// 没有return就没找到
					TreePath path = new TreePath(childDatasourceTreeNode.getPath());
					workspaceTree.setSelectionPath(path);
					workspaceTree.scrollPathToVisible(path);
					workspaceTree.expandPath(path);
					return;
				}
			}
		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelFieldInfo.setText(DataViewProperties.getString("String_SQLQueryFieldInfo"));
		this.labelQueryMode.setText(DataViewProperties.getString("String_SQLQueryLabelQueryMode"));
		this.radioButtonQuerySpaceAndProperty.setText(DataViewProperties.getString("String_SQLQueryRadioQuerySpatialandAttributeInfo"));
		this.radioButtonQueryAttributeInfo.setText(DataViewProperties.getString("String_QueryProperty"));
		this.labelOperator.setText(DataViewProperties.getString("String_SQLQueryLabelOperator"));
		this.buttonGetAllValue.setText(DataViewProperties.getString("String_getallvalue"));
		this.labelCommonFunction.setText(DataViewProperties.getString("String_SQLQueryLabelCommonFunction"));
		this.labelGoTO.setText(DataViewProperties.getString("String_SQLQueryGoTo"));
		this.labelQueryField.setText(DataViewProperties.getString("String_SQLQueryLabelQueryField"));
		this.labelQueryCondition.setText(DataViewProperties.getString("String_SQLQueryLabelCondition"));
		this.labelGroupField.setText(DataViewProperties.getString("String_SQLQueryLabelGroupField"));
		this.labelOrderByField.setText(DataViewProperties.getString("String_SQLQueryLabelOrderBy"));
		this.checkBoxShowTabular.setText(DataViewProperties.getString("String_SQLQueryShowTabular"));
		this.checkBoxHighLigthMap.setText(DataViewProperties.getString("String_SQLQueryHighLightMapWnd"));

		this.buttonJoinItems.setText(DataViewProperties.getString("String_SQLQueryRelated"));
		this.buttonExport.setText(ControlsProperties.getString("String_Export"));
		this.buttonImport.setText(ControlsProperties.getString("String_Import"));
		this.buttonQuery.setText(DataViewProperties.getString("String_SQLButtonQuery"));
		this.buttonClear.setText(DataViewProperties.getString("String_SQLButtonClean"));
		this.buttonClose.setText(DataViewProperties.getString("String_SQLButtonClose"));
	}

	/**
	 * 注册事件
	 */
	private void registListeners() {
		// 编辑框
		this.textFieldGroupField.addFocusListener(this.focusAdapter);
		this.textareaQueryCondition.addFocusListener(this.focusAdapter);
		this.textareaQueryField.addFocusListener(this.focusAdapter);
		this.sqlTableOrderByField.addFocusListener(this.focusAdapter);
		// 排序字段
		this.scrollPaneOrderByField.addMouseListener(this.scrollPaneorderByField);
		// 下拉框

		this.jComboBoxOperator.addActionListener(this.comboBoxOperatorActionListener);
		this.jComboBoxAggregationFunction.addItemListener(this.itemListener);
		this.jComboBoxMathsOperation.addItemListener(this.itemListener);
		this.jComboBoxStringFunction.addItemListener(this.itemListener);
		this.jComboBoxTimeFunction.addItemListener(this.itemListener);
		// 工作空间树
		this.workspaceTree.getSelectionModel().addTreeSelectionListener(this.treeSelectionListener);
		// 字段信息表
		this.tableFieldInfo.addMouseListener(this.mouseAdapter);
		this.tableFieldInfo.getSelectionModel().addListSelectionListener(this.listSelectionListener);
		// 查询模式
		this.radioButtonQueryAttributeInfo.addChangeListener(this.changeListener);
		// 获取唯一值按钮
		this.buttonGetAllValue.addActionListener(this.actionListener);
		// 定位文本框
		this.textFieldGOTO.addKeyListener(this.keyAdapter);
		// 唯一值列表
		this.listAllValue.addMouseListener(this.listAllValueMouseAdapter);
		// 查询字段内容改变判断查询按钮状态
		this.textareaQueryField.getDocument().addDocumentListener(this.documentListenerCheckQueryState);
		// 设置关联字段
		this.buttonJoinItems.addActionListener(buttonJoinItemsAction);
		// 导入
		this.buttonImport.addActionListener(this.buttonImportActionListener);
		// 导出
		this.buttonExport.addActionListener(this.buttonExportActionListener);
		// 查询
		this.buttonQuery.addActionListener(this.queryActionListener);
		// 清除
		this.buttonClear.addActionListener(this.clearActionListener);
		// 关闭
		this.buttonClose.addActionListener(this.closeActionlistener);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}

		});
	}

	/**
	 * 注销事件
	 */

	private void unRegistListeners() {
		// 编辑框
		this.textFieldGroupField.removeFocusListener(this.focusAdapter);
		this.textareaQueryCondition.removeFocusListener(this.focusAdapter);
		this.textareaQueryField.removeFocusListener(this.focusAdapter);
		this.sqlTableOrderByField.removeFocusListener(this.focusAdapter);
		// 排序字段
		this.scrollPaneOrderByField.removeMouseListener(this.scrollPaneorderByField);
		// 下拉框
		this.jComboBoxOperator.removeActionListener(this.comboBoxOperatorActionListener);
		this.jComboBoxAggregationFunction.removeItemListener(this.itemListener);
		this.jComboBoxMathsOperation.removeItemListener(this.itemListener);
		this.jComboBoxStringFunction.removeItemListener(this.itemListener);
		this.jComboBoxTimeFunction.removeItemListener(this.itemListener);
		// 工作空间树
		this.workspaceTree.getSelectionModel().removeTreeSelectionListener(this.treeSelectionListener);
		// 字段信息表
		this.tableFieldInfo.removeMouseListener(this.mouseAdapter);
		this.tableFieldInfo.getSelectionModel().removeListSelectionListener(this.listSelectionListener);
		// 查询模式
		this.radioButtonQueryAttributeInfo.removeChangeListener(this.changeListener);
		// 获取唯一值按钮
		this.buttonGetAllValue.removeActionListener(this.actionListener);
		// 定位文本框
		this.textFieldGOTO.removeKeyListener(this.keyAdapter);
		// 唯一值列表
		this.listAllValue.removeMouseListener(this.listAllValueMouseAdapter);
		// 查询字段内容改变判断查询按钮状态
		this.textareaQueryField.getDocument().removeDocumentListener(this.documentListenerCheckQueryState);
		// 导入
		this.buttonImport.removeActionListener(this.buttonImportActionListener);
		// 导出
		this.buttonExport.removeActionListener(this.buttonExportActionListener);
		// 查询
		this.buttonQuery.removeActionListener(this.queryActionListener);
		// 清除
		this.buttonClear.removeActionListener(this.clearActionListener);
		// 关闭
		this.buttonClose.removeActionListener(this.closeActionlistener);
	}

	/**
	 * 隐藏未实现控件
	 */
	private void initUndoComponents() {
		this.buttonExport.setVisible(false);
		this.buttonImport.setVisible(false);
		this.checkBoxHighLigthScene.setVisible(false);
	}

	// region 控件初始化

	/**
	 * 初始化唯一值列表
	 */
	private void initListAllValue() {
		listAllValue = new GetAllValueList();
	}

	/**
	 * 初始化工作空间树
	 */
	private void initWorkspaceTree() {
		this.workspaceTree = new WorkspaceTree(Application.getActiveApplication().getWorkspace());
		this.workspaceTree.setLayoutsNodeVisible(false);
		this.workspaceTree.setMapsNodeVisible(false);
		this.workspaceTree.setScenesNodeVisible(false);
		this.workspaceTree.setResourcesNodeVisible(false);
		this.workspaceTree.setSelectionPath(UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionPath());
		for (MouseMotionListener mouseMotionListener : this.workspaceTree.getMouseMotionListeners()) {
			// 拖拽监听事件删除
			this.workspaceTree.removeMouseMotionListener(mouseMotionListener);
		}
		for (KeyListener keyListener : this.workspaceTree.getKeyListeners()) {
			// 移除按键删除节点的监听器
			this.workspaceTree.removeKeyListener(keyListener);
		}
		this.workspaceTree.setEditable(false);
		// 删除不支持的数据集
		DefaultTreeModel treeModel = (DefaultTreeModel) this.workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		this.workspaceTree.expandRow(1);
		for (int i = datasourceTreeNode.getChildCount() - 1; i >= 0; i--) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			// TODO 屏蔽OGCWFC数据源
			// if(((Datasource)((TreeNodeData) childDatasourceTreeNode.getUserObject()).getData()).getEngineType() == EngineType.OGC
			// && ((Datasource)((TreeNodeData) childDatasourceTreeNode.getUserObject()).getData()).getConnectionInfo().getDriver().equals()){
			// datasourceTreeNode.remove(i);
			// }
			for (int j = childDatasourceTreeNode.getChildCount() - 1; j >= 0; j--) {
				DefaultMutableTreeNode datasetTreeNode = (DefaultMutableTreeNode) childDatasourceTreeNode.getChildAt(j);
				TreeNodeData treeNodeData = (TreeNodeData) datasetTreeNode.getUserObject();
				Dataset dataset = (Dataset) treeNodeData.getData();
				if (!(dataset instanceof DatasetVector) && dataset.getType() != DatasetType.TABULAR) {
					// 不为矢量且不为属性表
					childDatasourceTreeNode.remove(j);
				}
			}
		}
		this.workspaceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.scrollPaneWorkspaceTree.setViewportView(workspaceTree);
	}

	/**
	 * 初始化字段信息表
	 */
	private void initTableFieldInfo() {
		this.scrollPaneFieldInfo.setViewportView(tableFieldInfo);
	}

	private void initGetAllValue() {
		initListAllValue();
		this.scrollPaneAllValue.setViewportView(listAllValue);
	}

	/**
	 * 初始化运算符号下拉框
	 */
	private void initJComboBoxOperator() {
		jComboBoxOperator.setModel(new DefaultComboBoxModel<String>(new String[] { " + ", " - ", " * ", " / ", " \\ ", " > ", " = ", " < ", " >= ", " <= ",
				" <> ", " ! ", " () ", " [] ", " '' ", " % ", " # ", " ^ ", " . ", " Mod ", " AND ", " OR ", " NOT ", " In ", " Between ", " Like ",
				" Is Null ", " Is TRUE ", " Is FALSE " }));
		jComboBoxOperator.setSelectedIndex(5);
	}

	/**
	 * 初始化聚合函数下拉框
	 */
	private void initJComboBoxAggregationFunction() {
		jComboBoxAggregationFunction.setModel(new DefaultComboBoxModel<String>(new String[] {
				DataViewProperties.getString("String_SQLQueryCommonFuncAggregation"), "Avg()", "Count()", "Max()", "Min()", "Sum()", "Stdev()", "Stdevp()",
				"Var()", "Varp()" }));
	}

	/**
	 * 初始化数学函数下拉框
	 */
	private void initJComboBoxMathsOperation() {
		jComboBoxMathsOperation.setModel(new DefaultComboBoxModel<String>(new String[] { DataViewProperties.getString("String_SQLQueryCommonFuncMath"),
				"Abs()", "Acos()", "Asin()", "Atan()", "Atn2()", "Ceiling()", "Cos()", "Cot()", "Degrees()", "Exp()", "Floor()", "Log()", "Log10()", "PI()",
				"Power()", "Radians()", "Rand()", "Round()", "Sign()", "Sin()", "Square()", "Sqrt()", "Tan()", "CBool()", "CByte()", "CCur()", "CDate()",
				"CDbl()", "CInt()", "CLng()", "CSng()", "CStr()", "Int()", "Fix()" }));
	}

	/**
	 * 初始化字符函数下拉框
	 */
	private void initJComboBoxStringFunction() {
		jComboBoxStringFunction.setModel(new DefaultComboBoxModel<>(new String[] { DataViewProperties.getString("String_SQLQueryCommonFuncString"),
				"Ascii()", "Char()", "Charindex()", "Difference()", "Left()", "Len()", "Lower()", "Ltrim()", "Nchar()", "Patindex()", "Replace()",
				"Replicate()", "Quotename()", "Reverse()", "Right()", "Rtrim()", "Soundex()", "Space()", "Str()", "Stuff()", "Substring()", "Unicode()",
				"Upper()" }));
	}

	/**
	 * 初始化日期函数下拉框
	 */
	private void initJComboBoxTimeFunction() {
		jComboBoxTimeFunction.setModel(new DefaultComboBoxModel<String>(new String[] { DataViewProperties.getString("String_SQLQueryCommonFuncTime"),
				"DateAdd()", "Datediff()", "Datename()", "Datepart()", "Day()", "Getdate()", "Getutcdate()", "Month()", "Year()" }));
	}

	private void initQueryField() {
		this.scrollPaneQueryField.setViewportView(textareaQueryField);
	}

	private void initQueryCondition() {
		this.scrollPaneQueryCondition.setViewportView(textareaQueryCondition);
	}

	private void initOrderByField() {
		JPanel viewPanel = new JPanel();
		JPanel emptyPanel = new JPanel();
		viewPanel.setLayout(new GridBagLayout());
		viewPanel.add(sqlTableOrderByField, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		viewPanel.add(emptyPanel, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setIpad(0, 5));
		emptyPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 清除表格选中
				sqlTableOrderByField.clearSelection();
				sqlTableOrderByField.requestFocus();
			}
		});
		emptyPanel.setBackground(sqlTableOrderByField.getBackground());
		this.scrollPaneOrderByField.setViewportView(viewPanel);
	}

	// endregion

	// region 初始化面板布局

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		initPanelSelectSearchData();
		initPanelShowResult();
		initPanelSaveSearchResult();
		initPanelButton();

		// @formatter:off
		this.panelContent.setLayout(new GridBagLayout());
		this.panelContent.add(panelSelectSearchData, new GridBagConstraintsHelper(0, 0, 1, 18).setFill(GridBagConstraints.BOTH).setWeight(2.2, 30).setIpad(160, 0));
		this.panelContent.add(labelQueryMode, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 0.2).setAnchor(GridBagConstraints.CENTER).setInsets(0,5,0,5));
		this.panelContent.add(radioButtonQuerySpaceAndProperty, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(radioButtonQueryAttributeInfo, new GridBagConstraintsHelper(3, 0, 2, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));

		this.panelContent.add(labelOperator, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 0.2).setAnchor(GridBagConstraints.CENTER).setInsets(0,5,0,5));
		this.panelContent.add(jComboBoxOperator, new GridBagConstraintsHelper(2, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(buttonGetAllValue, new GridBagConstraintsHelper(3, 1, 2, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST).setInsets(0,10,0,0));

		this.panelContent.add(labelCommonFunction, new GridBagConstraintsHelper(1, 2, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 0.2).setAnchor(GridBagConstraints.CENTER).setInsets(0,5,0,5));
		this.panelContent.add(jComboBoxAggregationFunction, new GridBagConstraintsHelper(2, 2, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(jComboBoxMathsOperation, new GridBagConstraintsHelper(2, 3, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(jComboBoxStringFunction, new GridBagConstraintsHelper(2, 4, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(jComboBoxTimeFunction, new GridBagConstraintsHelper(2, 5, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(scrollPaneAllValue, new GridBagConstraintsHelper(3, 2, 2, 3).setFill(GridBagConstraints.BOTH).setWeight(1, 0.6).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,0,0));
		this.panelContent.add(labelGoTO, new GridBagConstraintsHelper(3, 5, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 0.2).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(textFieldGOTO, new GridBagConstraintsHelper(4, 5, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(0.8, 0.2).setAnchor(GridBagConstraints.CENTER));

		this.panelContent.add(labelQueryField, new GridBagConstraintsHelper(1, 6, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0,5,0,5));
		this.panelContent.add(scrollPaneQueryField, new GridBagConstraintsHelper(2, 6, 3, 2).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,0,5,0));

		this.panelContent.add(labelQueryCondition, new GridBagConstraintsHelper(1, 8, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0,5,0,5));
		this.panelContent.add(scrollPaneQueryCondition, new GridBagConstraintsHelper(2, 8, 3, 2).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));

		this.panelContent.add(labelGroupField, new GridBagConstraintsHelper(1, 10, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,5,0,5));
		this.panelContent.add(textFieldGroupField, new GridBagConstraintsHelper(2, 10, 3, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));

		this.panelContent.add(labelOrderByField, new GridBagConstraintsHelper(1, 11, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0.2, 1.5).setAnchor(GridBagConstraints.NORTH).setInsets(0,5,0,5));
		this.panelContent.add(scrollPaneOrderByField, new GridBagConstraintsHelper(2, 11, 3, 3).setFill(GridBagConstraints.BOTH).setWeight(1, 1.5).setAnchor(GridBagConstraints.CENTER));

		this.panelContent.add(panelShowResult, new GridBagConstraintsHelper(1, 14, 4, 1).setFill(GridBagConstraints.BOTH).setWeight(2.2, 0.1).setAnchor(GridBagConstraints.CENTER));
		this.panelContent.add(panelSaveSearchResult, new GridBagConstraintsHelper(1, 15, 4, 3).setFill(GridBagConstraints.BOTH).setWeight(2.2, 0.5).setAnchor(GridBagConstraints.CENTER).setIpad(0, 100));

		this.panelContent.add(panelButton,new GridBagConstraintsHelper(0,18,5,1).setFill(GridBagConstraints.BOTH).setWeight(3.2,0.1).setAnchor(GridBagConstraints.CENTER));

		this.setLayout(new GridBagLayout());
		this.add(panelContent,new GridBagConstraintsHelper(0,0,1,1).setWeight(1,1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10));
		// @formatter:on
	}

	/**
	 * 初始化参与查询数据面板
	 */
	private void initPanelSelectSearchData() {
		// @formatter:Off
		panelSelectSearchData.setBorder(BorderFactory.createTitledBorder(DataViewProperties.getString("String_SQLQueryDataSources")));
		panelSelectSearchData.setLayout(new GridBagLayout());
		panelSelectSearchData.add(scrollPaneWorkspaceTree,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
		panelSelectSearchData.add(
				labelFieldInfo,
				new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST)
						.setInsets(3, 0, 3, 0));
		panelSelectSearchData.add(scrollPaneFieldInfo,
				new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.NORTH));
		// @formatter:on
	}

	/**
	 * 结果显示面板
	 */
	private void initPanelShowResult() {
		// @formatter:off
		panelShowResult.setBorder(BorderFactory.createTitledBorder(DataViewProperties.getString("String_SQLQueryResultVisible")));
		panelShowResult.setLayout(new GridBagLayout());
		panelShowResult.add(checkBoxShowTabular,new GridBagConstraintsHelper(0,0,1,1).setWeight(1,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
		panelShowResult.add(checkBoxHighLigthMap,new GridBagConstraintsHelper(1,0,1,1).setWeight(1,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
		panelShowResult.add(checkBoxHighLigthScene,new GridBagConstraintsHelper(2,0,1,1).setWeight(1,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
		// @formatter:on

	}

	/**
	 * 初始化按钮面板
	 */
	private void initPanelButton() {
		// @formatter:off
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonJoinItems, new GridBagConstraintsHelper(0,0,1,1).setWeight(0,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0,5,0,0));
		panelButton.add(buttonImport,new GridBagConstraintsHelper(1,0,1,1).setWeight(0,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0,10,0,0));
		panelButton.add(buttonExport,new GridBagConstraintsHelper(2,0,1,1).setWeight(100,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0,10,0,0));
		panelButton.add(buttonQuery,new GridBagConstraintsHelper(3,0,1,1).setWeight(100,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0,0,0,10));
		panelButton.add(buttonClear,new GridBagConstraintsHelper(4,0,1,1).setWeight(0,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0,0,0,10));
		panelButton.add(buttonClose,new GridBagConstraintsHelper(5,0,1,1).setWeight(0,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0,0,0,10));


		// @formatter:on
	}

	private void initPanelSaveSearchResult() {
		// 设置之后会使得panel不能再缩小，能不用就不用吧
		// panelSaveSearchResult.setMinimumSize(new Dimension(100,120));
	}

	// endregion

	// region 监听事件

	/**
	 * 通用的下拉监听事件
	 */
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int addMode = 0;
				Object selectItem = e.getItem();
				if (!((JComboBox) e.getSource()).getItemAt(0).equals(selectItem)) {
					addMode = ADD_FUNCTION_OR_FIELD;
					((JComboBox) e.getSource()).setSelectedIndex(0);
				}

				if (addMode != 0 && lastComponent != sqlTableOrderByField && lastComponent != textFieldGroupField) {
					lastComponent.push(selectItem.toString(), addMode);
				}
			}
		}
	};

	/**
	 * 通用的选择编辑框改变事件，用来保存最后一次选择可编辑的框
	 */
	private FocusAdapter focusAdapter = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			lastComponent = ((ISQLBuildComponent) e.getSource());
		}

		@Override
		public void focusLost(FocusEvent e) {
			lastComponent = ((ISQLBuildComponent) e.getSource());
			lastComponent.rememberSelectstate();
		}
	};

	/**
	 * 工作空间树鼠标改变事件，更新字段信息表
	 */
	private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (joinItems.getCount() > 0) {
				joinItems.removeRange(0, joinItems.getCount());
			}
			Object data = ((TreeNodeData) ((DefaultMutableTreeNode) workspaceTree.getLastSelectedPathComponent()).getUserObject()).getData();
			if (data instanceof Dataset) {
				setCurrentDataset((Dataset) data);
				if (currentDataset.getType() == DatasetType.TABULAR) {
					radioButtonQueryAttributeInfo.setSelected(true);
					radioButtonQuerySpaceAndProperty.setEnabled(false);
				} else {
					radioButtonQuerySpaceAndProperty.setEnabled(true);
					radioButtonQuerySpaceAndProperty.setSelected(true);
				}
			} else {
				setCurrentDataset(null);
			}
		}
	};

	private void setCurrentDataset(Dataset data) {
		currentDataset = data;
		tableFieldInfo.setDataset(currentDataset);
	}

	/**
	 * 字段信息表鼠标事件， 1.双击时添加到最后选择的控件中 2.打开连接信息页面
	 */
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int row = tableFieldInfo.rowAtPoint(e.getPoint());
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				if (row == 0) {
					if (lastComponent == textareaQueryField) {
						lastComponent.push(tableFieldInfo.getSqlValueAt(row, 1), ADD_FUNCTION_OR_FIELD);
					} else {
						// doNothing 当选中行为0时只有查询字段可以添加，所以2个if语句不能合并
					}
				}
//				else if (row == tableFieldInfo.getRowCount() - 1) {
//					JDialogJoinItems jDialogJoinItem = new JDialogJoinItems(joinItems);
//					jDialogJoinItem.setCurrentDataset(currentDataset);
//					if (jDialogJoinItem.showDialog() == DialogResult.OK) {
//						// 先设值再释放资源
//						JoinItems joinItems = JDialogSQLQuery.this.joinItems;
//						JDialogSQLQuery.this.joinItems = jDialogJoinItem.getJoinItems();
//						tableFieldInfo.setJoinItem(JDialogSQLQuery.this.joinItems);
//						joinItems.dispose();
//					}
//				}
				else if (row != -1) {
//					lastComponent.push(tableFieldInfo.getValueAt(row, 1).toString(), ADD_FUNCTION_OR_FIELD);
					lastComponent.push(tableFieldInfo.getSqlValueAt(row, 1), ADD_FUNCTION_OR_FIELD);
				}
			}
		}
	};

	/**
	 * 字段信息表改变事件 负责修改获取唯一值按钮状态
	 */
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			checkButtonGetAllValueState();
		}
	};

	private void checkButtonGetAllValueState() {
		int row = tableFieldInfo.getSelectedRow();
		buttonGetAllValue.setEnabled(row != 0 && row != -1);
		clearScrollpaneallvalue();
	}

	/**
	 * 清空唯一值列表
	 */
	private void clearScrollpaneallvalue() {
		listAllValue.removeAllElements();
		textFieldGOTO.setEnabled(false);
	}

	/**
	 * 查询模式改变设置对应控件状态
	 */
	private ChangeListener changeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			boolean isRadioButtonQueryAttributeInfoSelected = radioButtonQueryAttributeInfo.isSelected();
			jComboBoxAggregationFunction.setEnabled(isRadioButtonQueryAttributeInfoSelected);
			textFieldGroupField.setEnabled(isRadioButtonQueryAttributeInfoSelected);
			checkBoxHighLigthMap.setEnabled(!isRadioButtonQueryAttributeInfoSelected);
			checkBoxHighLigthScene.setEnabled(!isRadioButtonQueryAttributeInfoSelected);
		}
	};
	/**
	 * 获取唯一值按钮显示所有唯一值。
	 */
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] allValue = tableFieldInfo.getAllValue();
			listAllValue.removeAllElements();
			if (allValue != null && allValue.length > 0) {
				listAllValue.resetValue(allValue);
				textFieldGOTO.setEnabled(true);
				buttonGetAllValue.setEnabled(false);
			}
		}
	};

	/**
	 * 定位文本框
	 */
	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			listAllValue.setSelectedIndex(listAllValue.getValueIndex(textFieldGOTO.getText()));
			listAllValue.ensureIndexIsVisible(listAllValue.getSelectedIndex());
		}
	};

	private MouseAdapter listAllValueMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && listAllValue.getSelectedIndex() != -1
					&& lastComponent == textareaQueryCondition) {
				textareaQueryCondition.push(listAllValue.getSelectedValue().toString(), ADD_DIRECT);
			}
		}
	};

	private DocumentListener documentListenerCheckQueryState = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			checkButtonQueryState();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			checkButtonQueryState();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			checkButtonQueryState();
		}
	};

	private void checkButtonQueryState() {
		if (this.textareaQueryField.getText() != null && textareaQueryField.getText().length() > 0) {
			this.buttonQuery.setEnabled(true);
			getRootPane().setDefaultButton(this.buttonQuery);
		} else {
			this.buttonQuery.setEnabled(false);
		}
	}

	/**
	 * 排序字段鼠标事件
	 */
	private MouseAdapter scrollPaneorderByField = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			sqlTableOrderByField.requestFocus();
			lastComponent = sqlTableOrderByField;
			if (sqlTableOrderByField.rowAtPoint(e.getPoint()) == -1) {
				sqlTableOrderByField.clearSelection();
			}
		}
	};

	/**
	 * 清除按钮事件
	 */
	private ActionListener clearActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			clear();
		}
	};

	private ActionListener buttonJoinItemsAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogJoinItems jDialogJoinItem = new JDialogJoinItems(joinItems);
			jDialogJoinItem.setCurrentDataset(currentDataset);
			if (jDialogJoinItem.showDialog() == DialogResult.OK) {
				// 先设值再释放资源
				JoinItems joinItems = JDialogSQLQuery.this.joinItems;
				JDialogSQLQuery.this.joinItems = jDialogJoinItem.getJoinItems();
				tableFieldInfo.setJoinItem(JDialogSQLQuery.this.joinItems);
				joinItems.dispose();
			}
		}
	};
	;

	private final ActionListener buttonImportActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String modelName = "SqlQueryImport";
				if (!SmFileChoose.isModuleExist(modelName)) {
					String fileFilters = SmFileChoose.createFileFilter(DataViewProperties.getString("String_SQLFilter"), "xml");
					SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
							DataViewProperties.getString("String_openDialogTitle"), modelName, "OpenOne");
				}
				SmFileChoose smFileChoose = new SmFileChoose(modelName);

				Datasource datasource = null;
				Datasource datasourceJoin = null;
				boolean hasJoin = false;
				boolean isAllInfo = true;

				if (smFileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
					String filePath = smFileChoose.getFilePath();
					Document document = XmlUtilties.getDocument(filePath);
					NodeList childNodes = document.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						Node node = childNodes.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							String nodeName = node.getNodeName();
							if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlqueryMode"))) {
								// TODO: 2016/3/17
								Node value = node.getAttributes().getNamedItem(DataViewProperties.getString("String_xmlValue"));
								if (value != null) {
									String nodeValue = value.getNodeValue();
									if (nodeValue != null) {
										isAllInfo = (Integer.valueOf(nodeValue) == 0);
									}
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsqldataset"))) {
								Node nodeDatasource = node.getAttributes().getNamedItem(DataViewProperties.getString("String_xmldatasource"));
								if (nodeDatasource != null) {
									String datasourceName = nodeDatasource.getNodeValue();
									datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
									if (datasource != null) {
										setCurrentDataset(null);
										Node nodeDataset = node.getAttributes().getNamedItem(DataViewProperties.getString("String_xmldataset"));
										if (nodeDataset != null) {
											String datasetName = nodeDataset.getNodeValue();
											Dataset dataset = datasource.getDatasets().get(datasetName);
											setCurrentDataset(dataset);
										}
									}
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsqlField"))) {
								Node nodeValue = node.getAttributes().getNamedItem(DataViewProperties.getString("String_xmlValue"));
								if (nodeValue != null) {
									textareaQueryField.setText(nodeValue.getNodeValue());
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsqlCondition"))) {
								Node nodeValue = node.getAttributes().getNamedItem(DataViewProperties.getString("String_xmlValue"));
								if (nodeValue != null) {
									textareaQueryCondition.setText(nodeValue.getNodeValue());
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsqlGroupField"))) {
								Node nodeValue = node.getAttributes().getNamedItem(DataViewProperties.getString("String_xmlValue"));
								if (nodeValue != null) {
									textFieldGroupField.setText(nodeValue.getNodeValue());
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsqlOrderby"))) {
								sqlTableOrderByField.clear();
								NodeList nodeChildNodes = node.getChildNodes();
								for (int j = 0; j < nodeChildNodes.getLength(); j++) {
									Node nodeTemp = nodeChildNodes.item(j);
									if (nodeTemp.getNodeType() == Node.ELEMENT_NODE) {
										NamedNodeMap attributes = nodeTemp.getAttributes();
										if (attributes != null) {
											String value;
											String order;
											Node nodeValue = attributes.getNamedItem(DataViewProperties.getString("String_xmlValue"));
											if (nodeValue == null) {
												continue;
											} else {
												value = nodeValue.getNodeValue();
											}
											Node nodeOrder = attributes.getNamedItem(DataViewProperties.getString("String_xmlValue"));

											if (nodeOrder == null) {
												continue;
											} else {
												order = nodeOrder.getNodeValue();
											}
											sqlTableOrderByField.add(value, order);
										}

									}
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlstandardsql"))) {
								// // TODO: 2016/3/17 sql语句
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlshoweTabular"))) {
								Node value = node.getAttributes().getNamedItem("String_xmlValue");
								if (value != null) {
									checkBoxShowTabular.setSelected(Boolean.valueOf(value.getNodeValue()));
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlshowOnMap"))) {
								Node value = node.getAttributes().getNamedItem("String_xmlValue");
								if (value != null) {
									checkBoxHighLigthMap.setSelected(Boolean.valueOf(value.getNodeValue()));
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlshowOnScene"))) {
								// // TODO: 2016/3/17 场景
								// Node value = node.getAttributes().getNamedItem("String_xmlValue");
								// if (value != null) {
								// checkBoxHighLigthMap.setSelected(Boolean.valueOf(value.getNodeValue()));
								// }
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsaveResult"))) {
								Node value = node.getAttributes().getNamedItem("String_xmlValue");
								if (value != null) {
									panelSaveSearchResult.setSelected(Boolean.valueOf(value.getNodeValue()));
								}
							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsqlRelated"))) {
								Node value = node.getAttributes().getNamedItem("String_xmlValue");
								if (value != null) {
									checkBoxHighLigthMap.setSelected(Boolean.valueOf(value.getNodeValue()));
								}

							} else if (nodeName.equalsIgnoreCase(DataViewProperties.getString("String_xmlsaveResult"))) {
								datasourceJoin = getJoinItem(node);
								if (datasourceJoin != null) {
									hasJoin = true;
								} else {
									hasJoin = false;
									joinItems = new JoinItems();
								}
							}
						}
					}
				}
				// // TODO: 2016/3/17

				if (isAllInfo) {
					radioButtonQueryAttributeInfo.setSelected(true);
				} else {
					radioButtonQuerySpaceAndProperty.setSelected(true);
				}

			} catch (Exception e1) {
				Application.getActiveApplication().getOutput().output(e1);
			}
		}
	};

	private Datasource getJoinItem(Node node) {
		return null;
	}

	private final ActionListener buttonExportActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

		}
	};

	/**
	 * 关闭按钮事件
	 */
	private ActionListener closeActionlistener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogSQLQuery.this.dispose();
		}
	};

	@Override
	public void dispose() {
		this.unRegistListeners();
		this.workspaceTree.dispose();
		super.dispose();
	}

	/**
	 * 查询按钮事件
	 */
	private ActionListener queryActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			query();
		}
	};

	private void query() {
		boolean isSuccessed;
		QueryParameter queryParameter = null;
		Recordset resultRecord = null;
		try {

			if (!panelSaveSearchResult.isSaveResult() && !checkBoxShowTabular.isSelected() && !checkBoxHighLigthMap.isSelected()
					&& !checkBoxHighLigthScene.isSelected()) {
				UICommonToolkit.showMessageDialog(DataViewProperties.getString("string_SQLQueryMessage"));
				return;
			}
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			if (currentDataset != null && currentDataset instanceof DatasetVector) {
				// 构建查询语句
				DatasetVector currentDatasetVector = ((DatasetVector) currentDataset);
				queryParameter = new QueryParameter();
				queryParameter.setCursorType(CursorType.DYNAMIC);
				queryParameter.setHasGeometry(true);

				// 查询字段
				String queryFields = textareaQueryField.getSQLExpression();
				String[] queryFieldNames = getQueryFieldNames(queryFields);
				queryParameter.setResultFields(queryFieldNames);
				// 查询条件
				queryParameter.setAttributeFilter(textareaQueryCondition.getSQLExpression());
				// 分组字段
				if (radioButtonQueryAttributeInfo.isSelected()) {
					queryParameter.setGroupBy(textFieldGroupField.getSQLExpression().split(","));
				}
				// 排序字段
				queryParameter.setOrderBy(sqlTableOrderByField.getSQLExpression().split(","));
				// 关联表信息
				if (joinItems != null) {
					queryParameter.setJoinItems(joinItems);
				}
				// 查询模式：空间和属性信息 只属性信息
				queryParameter.setHasGeometry(radioButtonQuerySpaceAndProperty.isSelected());
				// 查询预处理
				PreProcessSQLQuery(queryParameter);
				queryParameter.setSpatialQueryObject(currentDatasetVector);
				resultRecord = currentDatasetVector.query(queryParameter);
				// 判断是否有查询结果，没有查询结果给出提示
				if (resultRecord != null && resultRecord.getRecordCount() > 0) {
					if (StringUtilties.isNullOrEmpty(queryFields)) {
						resultRecord.dispose();
						resultRecord = null;
					}
					// 浏览地图
					if (queryParameter.getHasGeometry() && checkBoxHighLigthMap.isSelected()) {
						// 有空间信息时才有效
						ShowResultInMap(currentDatasetVector, resultRecord);
					}
					// TODO 浏览场景

					// 浏览属性表
					if (checkBoxShowTabular.isSelected()) {
						ShowResultInTabular(currentDatasetVector, resultRecord);
					}
					// TODO 关联浏览

					// 保存查询结果
					if (panelSaveSearchResult.isSaveResult()) {
						SaveQueryResult(resultRecord);
					}
					isSuccessed = true;
					if (isSuccessed) {
						JDialogSQLQuery.this.dispose();
					}
				} else {
					Application.getActiveApplication().getOutput().output(DataViewProperties.getString("String_SQLQueryFailed"));
				}
			} else {
				Application.getActiveApplication().getOutput().output(DataViewProperties.getString("String_PleaseSelectDataset"));
			}
		} catch (Exception e1) {
			Application.getActiveApplication().getOutput().output(e1);
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			if (queryParameter != null) {
				queryParameter.dispose();
			}
			if (resultRecord != null && (!checkBoxShowTabular.isSelected() || resultRecord.getRecordCount() == 0)) {
				resultRecord.dispose();
				resultRecord = null;
			}
		}
	}

	private String[] getQueryFieldNames(String queryFields) {
		int bracketsCount = 0;
		java.util.List<String> fieldNames = new ArrayList<>();
		char[] fieldNamesChars = queryFields.toCharArray();
		StringBuilder builderFieldName = new StringBuilder();
		for (char fieldNamesChar : fieldNamesChars) {
			if (fieldNamesChar == ',' && bracketsCount == 0 && builderFieldName.length() > 0) {
				fieldNames.add(builderFieldName.toString());
				builderFieldName.setLength(0);
			} else {
				builderFieldName.append(fieldNamesChar);
				if (fieldNamesChar == '(') {
					bracketsCount++;
				} else if (fieldNamesChar == ')' && bracketsCount > 0) {
					bracketsCount--;
				}
			}
		}
		if (builderFieldName.length() > 0) {
			fieldNames.add(builderFieldName.toString());
			builderFieldName.setLength(0);
		}
		return fieldNames.toArray(new String[fieldNames.size()]);
	}

	private void SaveQueryResult(Recordset resultRecord) {
		Datasource resultDatasource = panelSaveSearchResult.getSelectDatasouce();
		String datasetName = panelSaveSearchResult.getDatasetName();
		if (resultDatasource != null && !StringUtilties.isNullOrEmpty(datasetName)) {
			DatasetVector resultDataset = null;
			try {
				resultDataset = resultDatasource.recordsetToDataset(resultRecord, datasetName);
			} catch (Exception e) {
				resultDataset = null;
			}
			resultRecord.moveFirst();
			if (resultDataset == null) {
				Application.getActiveApplication().getOutput().output(DataViewProperties.getString("String_SQLQuerySaveAsResultFaield"));
			} else {
				Application.getActiveApplication().getOutput()
						.output(MessageFormat.format(DataViewProperties.getString("String_SQLQuerySavaAsResultSucces"), resultDataset.getName()));
			}
		}

	}

	private void ShowResultInTabular(DatasetVector currentDatasetVector, Recordset resultRecord) {
		// 属性表中显示
		IFormTabular formTabular = (IFormTabular) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.TABULAR);
		if (formTabular != null) {
			formTabular.setText(MessageFormat.format("{0}@{1}", currentDatasetVector.getName(), currentDatasetVector.getDatasource().getAlias()));
			formTabular.setRecordset(resultRecord);
		}
	}

	private void ShowResultInMap(DatasetVector currentDatasetVector, Recordset resultRecord) {
		// 地图中显示
		IFormMap formMap = null;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			formMap = ((IFormMap) activeForm);
		}
		// else {
		// for (int i = 0; i < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); i++) {
		// if (Application.getActiveApplication().getMainFrame().getFormManager().get(i).getWindowType() == WindowType.MAP) {
		// formMap = ((IFormMap) Application.getActiveApplication().getMainFrame().getFormManager().get(i));
		// Application.getActiveApplication().getMainFrame().getFormManager().setActiveForm(formMap);
		// }
		// }
		// }
		// 新地图打开
		if (formMap == null) {
			String name = MapUtilties.getAvailableMapName(
					MessageFormat.format("{0}@{1}", currentDatasetVector.getName(), currentDatasetVector.getDatasource().getAlias()), true);
			formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);
		}

		if (formMap != null) {
			Layer layer = MapUtilties.findLayerByDatasetWithoutLabelTheme(formMap.getMapControl().getMap(), currentDatasetVector);
			if (layer == null || layer.isDisposed()) {
				layer = MapUtilties.addDatasetToMap(formMap.getMapControl().getMap(), currentDatasetVector, true);
			}
			if (layer != null && !layer.isDisposed()) {
				layer.getSelection().clear();
				layer.getSelection().setDefaultStyleEnabled(false);
				java.util.List<Integer> smIdList = new ArrayList<>();
				resultRecord.moveFirst();
				while (!resultRecord.isEOF()) {
					smIdList.add(resultRecord.getID());
					resultRecord.moveNext();
				}
				int[] smIds = new int[smIdList.size()];
				for (int i = 0; i < smIdList.size(); i++) {
					smIds[i] = smIdList.get(i);
				}
				layer.getSelection().addRange(smIds);
				formMap.updataSelectNumber();
				formMap.getMapControl().getMap().refresh();
			}
		}
	}

	private void PreProcessSQLQuery(QueryParameter queryParameter) {
		try {
			for (String field : queryParameter.getResultFields()) {
				String strText = field.toUpperCase();
				if (strText.contains("SUM(") || strText.contains("MAX(") || strText.contains("MIN(") || strText.contains("AVG(") || strText.contains("COUNT(")
						|| strText.contains("STDEV(") || strText.contains("STDEVP(") || strText.contains("VAR(") || strText.contains("VARP(")) {
					queryParameter.setCursorType(CursorType.STATIC);
					break;
				}
			}

			if (queryParameter.getGroupBy().length > 0) {
				queryParameter.setCursorType(CursorType.STATIC);
			}

			if (!checkBoxShowTabular.isSelected()) {
				queryParameter.setCursorType(CursorType.STATIC);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private ActionListener comboBoxOperatorActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (lastComponent != sqlTableOrderByField && lastComponent != textFieldGroupField) {
				lastComponent.push(jComboBoxOperator.getSelectedItem().toString(), ADD_OPERATOR);
			}
		}
	};

	// endregion

	class GetAllValueList extends JList {
		public GetAllValueList() {
			super(new DefaultListModel<String>());
		}

		public int getValueIndex(String value) {
			int defaultValue = this.getSelectedIndex();
			for (int i = 0; i < this.getModel().getSize(); i++) {
				if (this.getModel().getElementAt(i).toString().startsWith(value)) {
					return i;
				}
			}
			return defaultValue;
		}

		public void removeAllElements() {
			((DefaultListModel) this.getModel()).removeAllElements();
		}

		public void resetValue(String[] allValue) {
			this.removeAllElements();
			if (allValue != null && allValue.length > 0) {
				for (String anAllValue : allValue) {
					((DefaultListModel<String>) this.getModel()).addElement(anAllValue);
				}
				this.setSelectedIndex(0);
			}
		}
	}

	private void clear() {
		clearScrollpaneallvalue();
		textFieldGOTO.setText("");
		checkButtonGetAllValueState();

		textareaQueryField.clear();
		textareaQueryCondition.clear();
		textFieldGroupField.clear();
		sqlTableOrderByField.clear();
	}
}
