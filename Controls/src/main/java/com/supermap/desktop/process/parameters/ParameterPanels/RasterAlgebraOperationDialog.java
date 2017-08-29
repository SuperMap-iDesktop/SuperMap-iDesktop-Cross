package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by lixiaoyao on 2017/8/29.
 */
public class RasterAlgebraOperationDialog extends SmDialog {
	private JPanel panelSelectSearchData = new JPanel();
	// 参与查询的数据
	private JScrollPane scrollPaneWorkspaceTree = new JScrollPane();
	private WorkspaceTree workspaceTree;
	private TextArea textAreaOperationString = new TextArea();
	private JPanel panelFunction = new JPanel();
	private JPanel panelOperators = new JPanel();
	private JButton jButtonNot = new JButton();
	private JButton jButtonOr = new JButton();
	private JButton jButtonXOR = new JButton();
	private JButton jButtonAnd = new JButton();
	private JComboBox<String> jComboBoxBasicMathFunction; // 算数函数
	private JComboBox<String> jComboBoxTriangleFunction;  //三角函数
	private JComboBox<String> jComboBoxSeniorMathFunction;   //指数或对数
	private JComboBox<String> jComboBoxOtherFunction;   //其他函数
	//private JButton jButtonAndCompute=new JButton();
	//private JButton jButtonSpace=new JButton();
	//private JButton jButtonC=new JButton();
	private JButton jButtonRightBracket = new JButton();
	private JButton jButtonLeftBracket = new JButton();
	private JButton jButtonLessOrEqual = new JButton();
	private JButton jButtonMoreOrEqual = new JButton();
	private JButton jButtonBracket = new JButton();
	private JButton jButtonEqual = new JButton();
	private JButton jButtonLess = new JButton();
	private JButton jButtonMore = new JButton();
	private JButton jButtonDivide = new JButton();
	private JButton jButtonMultiply = new JButton();
	private JButton jButtonSubtract = new JButton();
	private JButton jButtonPlus = new JButton();
	private JButton jButtonImport = new JButton();
	private JButton jButtonExport = new JButton();
	private SmButton buttonOK = new SmButton();
	private SmButton buttonClear = new SmButton();
	private SmButton buttonClose = new SmButton();

	public RasterAlgebraOperationDialog() {
		super();
		this.setTitle(ControlsProperties.getString("String_RasterAlgebraOperationDialogTitle"));
		this.setSize(new Dimension(825, 452));
		initComponents();
		initLayout();
	}

	private void initComponents() {
		this.jComboBoxBasicMathFunction = new JComboBox<>();
		this.jComboBoxTriangleFunction = new JComboBox<>();
		this.jComboBoxSeniorMathFunction = new JComboBox<>();
		this.jComboBoxOtherFunction = new JComboBox<>();
		initWorkspaceTree();
	}

	private void initLayout() {
		initPanelSelectSearchData();

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.panelSelectSearchData)
								.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(this.panelFunction)
												.addComponent(this.panelOperators))
										.addComponent(this.textAreaOperationString)))
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.jButtonImport)
								.addComponent(this.jButtonExport)
								.addGap(200, 200, Short.MAX_VALUE)
								.addComponent(this.buttonOK)
								.addComponent(this.buttonClear)
								.addComponent(this.buttonClose)))

		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(this.panelSelectSearchData)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(this.panelFunction)
										.addComponent(this.panelOperators))
								.addComponent(this.textAreaOperationString)))
				.addContainerGap(10, 10)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.jButtonImport)
						.addComponent(this.jButtonExport)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonClear)
						.addComponent(this.buttonClose))
		);

		getContentPane().setLayout(groupLayout);
	}

	/**
	 * 初始化参与查询数据面板
	 */
	private void initPanelSelectSearchData() {
		//panelSelectSearchData.setBorder(BorderFactory.cr);
		this.panelSelectSearchData.setLayout(new GridBagLayout());
		this.panelSelectSearchData.add(this.scrollPaneWorkspaceTree,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
	}

	private void initPanelFunction(){
		this.panelFunction.setLayout(new GridBagLayout());
		this.panelFunction.add(this.jComboBoxBasicMathFunction,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
	}

	private void intiPanelOperators(){

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
			for (int j = childDatasourceTreeNode.getChildCount() - 1; j >= 0; j--) {
				DefaultMutableTreeNode datasetTreeNode = (DefaultMutableTreeNode) childDatasourceTreeNode.getChildAt(j);
				TreeNodeData treeNodeData = (TreeNodeData) datasetTreeNode.getUserObject();
				Dataset dataset = (Dataset) treeNodeData.getData();
				if (dataset.getType() != DatasetType.GRID) {
					childDatasourceTreeNode.remove(j);
				}
			}
		}
		this.workspaceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.scrollPaneWorkspaceTree.setViewportView(workspaceTree);
	}
}
