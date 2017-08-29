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
	private JLabel labelBasicMathFunction;
	private JLabel labelTriangleFunction;
	private JLabel labelSeniorMathFunction;
	private JLabel labelOtherFunction;
	private JComboBox<String> jComboBoxBasicMathFunction; // 算数函数
	private JComboBox<String> jComboBoxTriangleFunction;  //三角函数
	private JComboBox<String> jComboBoxSeniorMathFunction;   //指数或对数
	private JComboBox<String> jComboBoxOtherFunction;   //其他函数
	//private JButton jButtonAndCompute=new JButton();
	//private JButton jButtonSpace=new JButton();
	//private JButton jButtonC=new JButton();
	private JButton jButtonNot ;
	private JButton jButtonOr ;
	private JButton jButtonXOR;
	private JButton jButtonAnd;
	private JButton jButtonRightBracket;
	private JButton jButtonLeftBracket;
	private JButton jButtonLessOrEqual ;
	private JButton jButtonMoreOrEqual ;
	private JButton jButtonBracket ;
	private JButton jButtonEqual;
	private JButton jButtonLess ;
	private JButton jButtonMore ;
	private JButton jButtonDivide ;
	private JButton jButtonMultiply ;
	private JButton jButtonSubtract ;
	private JButton jButtonPlus ;
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
		initComponentsState();
		initResources();
	}

	private void initComponents() {
		this.jComboBoxBasicMathFunction = new JComboBox<>();
		this.jComboBoxTriangleFunction = new JComboBox<>();
		this.jComboBoxSeniorMathFunction = new JComboBox<>();
		this.jComboBoxOtherFunction = new JComboBox<>();
		this.jButtonPlus = new JButton("+");
		this.jButtonSubtract = new JButton("-");
		this.jButtonMultiply = new JButton("*");
		this.jButtonDivide = new JButton("/");
		this.jButtonAnd = new JButton("And");
		this.jButtonMore = new JButton(">");
		this.jButtonLess = new JButton("<");
		this.jButtonEqual = new JButton("=");
		this.jButtonBracket = new JButton("<>");
		this.jButtonNot = new JButton("Not");
		this.jButtonMoreOrEqual = new JButton(">=");
		this.jButtonLessOrEqual = new JButton("<=");
		this.jButtonLeftBracket = new JButton("(");
		this.jButtonRightBracket = new JButton(")");
		this.jButtonXOR = new JButton("Xor");
		this.jButtonOr = new JButton("Or");
		this.labelBasicMathFunction=new JLabel(ControlsProperties.getString("String_BasicMathFunction"));
		this.labelTriangleFunction=new JLabel(ControlsProperties.getString("String_TriangleFunction"));
		this.labelSeniorMathFunction=new JLabel(ControlsProperties.getString("String_SeniorMathFunction"));
		this.labelOtherFunction=new JLabel(ControlsProperties.getString("String_OtherFunction"));
		initWorkspaceTree();
	}

	private void initLayout() {
		initPanelSelectSearchData();
		initPanelFunction();
		intiPanelOperators();

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
		this.panelSelectSearchData.setLayout(new GridBagLayout());
		this.panelSelectSearchData.add(this.scrollPaneWorkspaceTree,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
	}

	private void initPanelFunction(){
		this.panelFunction.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_CommonFunction")));
		GroupLayout groupLayout=new GroupLayout(this.panelFunction);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelBasicMathFunction)
						.addComponent(this.labelTriangleFunction)
						.addComponent(this.labelSeniorMathFunction)
						.addComponent(this.labelOtherFunction))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.jComboBoxBasicMathFunction)
						.addComponent(this.jComboBoxTriangleFunction)
						.addComponent(this.jComboBoxSeniorMathFunction)
						.addComponent(this.jComboBoxOtherFunction))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelBasicMathFunction)
						.addComponent(this.jComboBoxBasicMathFunction))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelTriangleFunction)
						.addComponent(this.jComboBoxTriangleFunction))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelSeniorMathFunction)
						.addComponent(this.jComboBoxSeniorMathFunction))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelOtherFunction)
						.addComponent(this.jComboBoxOtherFunction))
		);

//		this.panelFunction.setLayout(new GridBagLayout());
//		this.panelFunction.add(this.jComboBoxBasicMathFunction,
//				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
//		this.panelFunction.add(this.jComboBoxBasicMathFunction,
//				new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
//		this.panelFunction.add(this.jComboBoxSeniorMathFunction,
//				new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
//		this.panelFunction.add(this.jComboBoxOtherFunction,
//				new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.NORTH));
		this.panelFunction.setLayout(groupLayout);
	}

	private void intiPanelOperators(){
		this.panelOperators.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_CommonOperator")));
		this.panelOperators.setLayout(new GridBagLayout());
		this.panelOperators.add(this.jButtonPlus, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonSubtract, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonMultiply, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonDivide, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		//this.panelOperators.add(this.jButtonAnd, new GridBagConstraintsHelper(4, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.panelOperators.add(this.jButtonMore, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonLess, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonEqual, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonBracket, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		//this.panelOperators.add(this.jButtonNot, new GridBagConstraintsHelper(4, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.panelOperators.add(this.jButtonMoreOrEqual, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonLessOrEqual, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonLeftBracket, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonRightBracket, new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		//this.panelOperators.add(this.jButtonLike, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.panelOperators.add( this.jButtonAnd , new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonXOR, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonOr, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonNot, new GridBagConstraintsHelper(4, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
	}

	private void initComponentsState(){
		this.jComboBoxBasicMathFunction.setModel(new DefaultComboBoxModel<String>(new String[] {"abs()",
		"mod()","floor()"}));
		this.jComboBoxTriangleFunction.setModel(new DefaultComboBoxModel<String>(new String[] {"sin()",
				"cos()","tan()","cot()", "asin()","acos()", "atan()","acot()","sinh()",
				"cosh()","tanh()","coth()"}));
		this.jComboBoxSeniorMathFunction.setModel(new DefaultComboBoxModel<String>(new String[] {"exp()",
				"pow(,)","sqrt()","ln()", "log()"}));
		this.jComboBoxOtherFunction.setModel(new DefaultComboBoxModel<String>(new String[] {"Con(,,)",
				"IsNull(,)","Pick(,,)"}));
	}

	private void initResources(){
		this.jButtonImport.setText(ControlsProperties.getString("string_button_import"));
		this.jButtonExport.setText(ControlsProperties.getString("String_Button_Export"));
		this.buttonOK.setText(ControlsProperties.getString("String_Button_Ok"));
		this.buttonClear.setText(ControlsProperties.getString("String_GeometryPropertyStyle3DControl_buttonClearMarkerIconFile"));
		this.buttonClose.setText(ControlsProperties.getString("String_Button_Cancel"));
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

//	public static void main(String args[]){
//		RasterAlgebraOperationDialog rasterAlgebraOperationDialog= new RasterAlgebraOperationDialog();
//		rasterAlgebraOperationDialog.showDialog();
//	}
}
