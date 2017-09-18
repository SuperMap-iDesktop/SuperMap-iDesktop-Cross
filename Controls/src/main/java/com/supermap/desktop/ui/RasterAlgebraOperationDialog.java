package com.supermap.desktop.ui;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.ExpressionComponent.ButtonOperatorListener;
import com.supermap.desktop.ui.controls.ExpressionComponent.FunctionComboBox;
import com.supermap.desktop.ui.controls.ExpressionComponent.FunctionComboBoxSelectedChangeListener;
import com.supermap.desktop.ui.controls.ExpressionComponent.OperatorsPanel;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by lixiaoyao on 2017/8/29.
 */
public class RasterAlgebraOperationDialog extends SmDialog {
	private JPanel panelSelectSearchData = new JPanel();
	// 参与查询的数据
	private JScrollPane scrollPaneWorkspaceTree = new JScrollPane();
	private WorkspaceTree workspaceTree;
	private JScrollPane scrollPaneOperationString = new JScrollPane();
	private JTextArea textAreaOperationString = new JTextArea();
	private JPanel panelFunction = new JPanel();
	private JLabel labelBasicMathFunction;
	private JLabel labelTriangleFunction;
	private JLabel labelSeniorMathFunction;
	private JLabel labelOtherFunction;
	private FunctionComboBox jComboBoxBasicMathFunction; // 算数函数
	private FunctionComboBox jComboBoxTriangleFunction;  //三角函数
	private FunctionComboBox jComboBoxSeniorMathFunction;   //指数或对数
	private FunctionComboBox jComboBoxOtherFunction;   //其他函数
	private OperatorsPanel operatorsPanel;
	private SmButton buttonOK = new SmButton();
	private SmButton buttonClear = new SmButton();
	private SmButton buttonClose = new SmButton();
	private String preExpression = "";
	private PixelFormat prePixelFormat = null;
	private boolean isFirstClickTree=false;

	public RasterAlgebraOperationDialog(String preExpression) {
		super();
		this.preExpression = preExpression;
		this.setTitle(ControlsProperties.getString("String_RasterAlgebraOperationDialogTitle"));
		this.setSize(new Dimension(620, 400));
		initComponents();
		initLayout();
		initComponentsState();
		initResources();
		removeListener();
		registerListener();
		this.setLocationRelativeTo(null);
		this.componentList.add(this.buttonOK);
		this.componentList.add(this.buttonClear);
		this.componentList.add(this.buttonClose);
		this.setFocusTraversalPolicy(policy);
	}

	private void initComponents() {
		this.jComboBoxBasicMathFunction = new FunctionComboBox(new String[]{"abs()",
				"mod(,)", "floor()"});
		this.jComboBoxTriangleFunction = new FunctionComboBox(new String[]{"sin()",
				"cos()", "tan()", "cot()", "asin()", "acos()", "atan()", "acot()", "sinh()",
				"cosh()", "tanh()", "coth()"});
		this.jComboBoxSeniorMathFunction = new FunctionComboBox(new String[]{"exp()",
				"pow(,)", "sqrt()", "ln()", "log()"});
		this.jComboBoxOtherFunction = new FunctionComboBox(new String[]{"Con(,,)",
				"IsNull()", "Pick(,,)"});
		this.labelBasicMathFunction = new JLabel(ControlsProperties.getString("String_BasicMathFunction"));
		this.labelTriangleFunction = new JLabel(ControlsProperties.getString("String_TriangleFunction"));
		this.labelSeniorMathFunction = new JLabel(ControlsProperties.getString("String_SeniorMathFunction"));
		this.labelOtherFunction = new JLabel(ControlsProperties.getString("String_OtherFunction"));
		this.operatorsPanel=new OperatorsPanel();
		initWorkspaceTree();
		this.textAreaOperationString.setMinimumSize(new Dimension(550, 160));
		this.panelSelectSearchData.setMinimumSize(new Dimension(200, 305));
	}

	private void initLayout() {
		initPanelSelectSearchData();
		initPanelFunction();

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
												.addComponent(this.operatorsPanel)
										)
										.addComponent(this.scrollPaneOperationString)))
						.addGroup(groupLayout.createSequentialGroup()
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
										.addComponent(this.operatorsPanel)
								)
								.addComponent(this.scrollPaneOperationString, 160, 160, Short.MAX_VALUE)))
				.addContainerGap(10, 10)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonClear)
						.addComponent(this.buttonClose))
		);
		this.scrollPaneOperationString.setViewportView(this.textAreaOperationString);
		getContentPane().setLayout(groupLayout);
	}

	private void initPanelSelectSearchData() {
		this.panelSelectSearchData.setLayout(new GridBagLayout());
		this.panelSelectSearchData.add(this.scrollPaneWorkspaceTree,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.SOUTH));
	}

	private void initPanelFunction() {
		this.panelFunction.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_CommonFunction")));
		this.panelFunction.setLayout(new GridBagLayout());
		this.panelFunction.add(this.labelBasicMathFunction, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.jComboBoxBasicMathFunction, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.labelTriangleFunction, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.jComboBoxTriangleFunction, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.labelSeniorMathFunction, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.jComboBoxSeniorMathFunction, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.labelOtherFunction, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelFunction.add(this.jComboBoxOtherFunction, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(3, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
	}

	private void initComponentsState() {
		this.textAreaOperationString.setLineWrap(true);
		this.textAreaOperationString.setWrapStyleWord(true);
		this.textAreaOperationString.setText(this.preExpression);
		if (StringUtilities.isNullOrEmpty(this.preExpression)){
			this.isFirstClickTree=true;
		}
	}

	private void initResources() {
		this.buttonOK.setText(ControlsProperties.getString("String_Button_Ok"));
		this.buttonClear.setText(ControlsProperties.getString("String_GeometryPropertyStyle3DControl_buttonClearMarkerIconFile"));
		this.buttonClose.setText(ControlsProperties.getString("String_Button_Cancel"));
	}

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

	private void registerListener() {
		this.workspaceTree.addMouseListener(this.mouseAdapterWorkspaceTress);
		this.jComboBoxBasicMathFunction.addFunctionListener(this.comboBoxSelectedChangeListener);
		this.jComboBoxTriangleFunction.addFunctionListener(this.comboBoxSelectedChangeListener);
		this.jComboBoxSeniorMathFunction.addFunctionListener(this.comboBoxSelectedChangeListener);
		this.jComboBoxOtherFunction.addFunctionListener(this.comboBoxSelectedChangeListener);
		this.buttonOK.addActionListener(this.buttonDefaultActionListener);
		this.buttonClear.addActionListener(this.buttonDefaultActionListener);
		this.buttonClose.addActionListener(this.buttonDefaultActionListener);
		this.operatorsPanel.addButtonOperatorListener(this.buttonOperatorListener);
	}

	private void removeListener() {
		this.workspaceTree.removeMouseListener(this.mouseAdapterWorkspaceTress);
		this.jComboBoxBasicMathFunction.removeFunctionListener(this.comboBoxSelectedChangeListener);
		this.jComboBoxTriangleFunction.removeFunctionListener(this.comboBoxSelectedChangeListener);
		this.jComboBoxSeniorMathFunction.removeFunctionListener(this.comboBoxSelectedChangeListener);
		this.jComboBoxOtherFunction.removeFunctionListener(this.comboBoxSelectedChangeListener);
		this.buttonOK.removeActionListener(this.buttonDefaultActionListener);
		this.buttonClear.removeActionListener(this.buttonDefaultActionListener);
		this.buttonClose.removeActionListener(this.buttonDefaultActionListener);
		this.operatorsPanel.removeButtonOperatorListener(this.buttonOperatorListener);
	}

	private ButtonOperatorListener buttonOperatorListener=new ButtonOperatorListener() {
		@Override
		public void buttonOperator_Click(String operatorString) {
			textAreaOperationString.requestFocusInWindow();
			operatorString = " " + operatorString;
			setExpressionSentenceText(textAreaOperationString, operatorString, "");
		}
	};

	private FunctionComboBoxSelectedChangeListener comboBoxSelectedChangeListener=new FunctionComboBoxSelectedChangeListener() {
		@Override
		public void comboBoxFunction_SelectedChanged(String functionString) {
			textAreaOperationString.requestFocusInWindow();
			setExpressionSentenceText(textAreaOperationString, functionString, "function");
		}
	};

	private MouseAdapter mouseAdapterWorkspaceTress = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				int index = workspaceTree.getRowForLocation(e.getX(), e.getY());
				TreePath treePath = workspaceTree.getPathForRow(index);
				if (treePath != null) {
					DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
					Object data = ((TreeNodeData) selNode.getUserObject()).getData();
					if (data != null && data instanceof Dataset) {
						changePixFormat((Dataset) data);
						textAreaOperationString.requestFocusInWindow();
						setExpressionSentenceText(textAreaOperationString, "[" + ((Dataset) data).getDatasource().getAlias() + "." + ((Dataset) data).getName() + "]", "datasetName");
					}
				}
			}
		}
	};

	private ActionListener buttonDefaultActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(buttonOK)) {
				RasterAlgebraOperationDialog.this.setDialogResult(DialogResult.OK);
				RasterAlgebraOperationDialog.this.dispose();
			} else if (e.getSource().equals(buttonClear)) {
				textAreaOperationString.setText("");
				isFirstClickTree=true;
			} else if (e.getSource().equals(buttonClose)) {
				RasterAlgebraOperationDialog.this.dispose();
			}
		}
	};

	private void changePixFormat(Dataset dataset) {
		if (this.isFirstClickTree) {
			this.prePixelFormat=((DatasetGrid) dataset).getPixelFormat();
			this.isFirstClickTree=false;
		}
	}

	private static void setExpressionSentenceText(JTextArea textArea, String sentence, String type) {
		StringBuilder sqlSentence = new StringBuilder(textArea.getText());

		int startPosition = textArea.getSelectionStart();
		int endPosition = textArea.getSelectionEnd();

		sqlSentence.delete(startPosition, endPosition);
		textArea.setText(sqlSentence.toString());

		textArea.insert(sentence, startPosition);
		textArea.setSelectionStart(startPosition + sentence.length());

		// 当为函数形式时，光标应该在括号里面
		if (type == "function") {
			int lastLength = sentence.length() - 1;
			if (sentence.indexOf(",") != -1) {
				lastLength = sentence.indexOf(",");
			}
			textArea.setCaretPosition(startPosition + lastLength);
		} else {
			textArea.setCaretPosition(startPosition + sentence.length());
		}

		textArea.requestFocusInWindow();
	}

	public String getExpression() {
		return this.textAreaOperationString.getText();
	}

	public PixelFormat getPixelFormat() {
		return this.prePixelFormat;
	}

}
