package com.supermap.desktop.ui;

import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by lixiaoyao on 2017/8/29.
 */
public class RasterAlgebraOperationDialog extends SmDialog {
	private JPanel panelSelectSearchData = new JPanel();
	// 参与查询的数据
	private JScrollPane scrollPaneWorkspaceTree = new JScrollPane();
	private WorkspaceTree workspaceTree;
	private JTextArea textAreaOperationString = new JTextArea();
	private JPanel panelFunction = new JPanel();
	private JPanel panelOperators = new JPanel();
	private JPanel panelSetting = new JPanel();
	private JLabel labelBasicMathFunction;
	private JLabel labelTriangleFunction;
	private JLabel labelSeniorMathFunction;
	private JLabel labelOtherFunction;
	private JComboBox<String> jComboBoxBasicMathFunction; // 算数函数
	private JComboBox<String> jComboBoxTriangleFunction;  //三角函数
	private JComboBox<String> jComboBoxSeniorMathFunction;   //指数或对数
	private JComboBox<String> jComboBoxOtherFunction;   //其他函数
	private JButton jButtonNot;
	private JButton jButtonOr;
	private JButton jButtonXOR;
	private JButton jButtonAnd;
	private JButton jButtonRightBracket;
	private JButton jButtonLeftBracket;
	private JButton jButtonLessOrEqual;
	private JButton jButtonMoreOrEqual;
	private JButton jButtonBracket;
	private JButton jButtonEqual;
	private JButton jButtonLess;
	private JButton jButtonMore;
	private JButton jButtonDivide;
	private JButton jButtonMultiply;
	private JButton jButtonSubtract;
	private JButton jButtonPlus;
	private JButton jButtonImport = new JButton();
	private JButton jButtonExport = new JButton();
	private JLabel labelPixelFormat;
	private JComboBox comboBoxPixelFormat;
	private JCheckBox checkBoxCompress;
	private JCheckBox checkBoxIngoreNoValueCell;
	private SmButton buttonOK = new SmButton();
	private SmButton buttonClear = new SmButton();
	private SmButton buttonClose = new SmButton();
	private String preExpression = "";
	private PixelFormat prePixelFormat = null;
	private boolean isZip = false;
	private boolean isIgnoreNoValue = false;

	public RasterAlgebraOperationDialog(String preExpression, PixelFormat prePixelFormat, boolean isZip, boolean isIgnoreNoValue) {
		super();
		this.preExpression = preExpression;
		this.prePixelFormat = prePixelFormat;
		this.isZip = isZip;
		this.isIgnoreNoValue = isIgnoreNoValue;
		this.setTitle(ControlsProperties.getString("String_RasterAlgebraOperationDialogTitle"));
		this.setSize(new Dimension(820, 460));
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
		this.labelBasicMathFunction = new JLabel(ControlsProperties.getString("String_BasicMathFunction"));
		this.labelTriangleFunction = new JLabel(ControlsProperties.getString("String_TriangleFunction"));
		this.labelSeniorMathFunction = new JLabel(ControlsProperties.getString("String_SeniorMathFunction"));
		this.labelOtherFunction = new JLabel(ControlsProperties.getString("String_OtherFunction"));
		this.labelPixelFormat = new JLabel(CommonProperties.getString("String_PixelFormat"));
		this.comboBoxPixelFormat = new JComboBox();
		this.checkBoxCompress = new JCheckBox(ControlsProperties.getString("String_DatasetCompress"));
		this.checkBoxIngoreNoValueCell = new JCheckBox(ControlsProperties.getString("String_IgnoreNoValueRasterCell"));
		initWorkspaceTree();
		this.textAreaOperationString.setMinimumSize(new Dimension(550, 180));
		this.panelSelectSearchData.setMinimumSize(new Dimension(200, 365));
	}

	private void initLayout() {
		initPanelSelectSearchData();
		initPanelFunction();
		initPanelOperators();
		initPanelSetting();

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
												.addComponent(this.panelOperators)
												.addComponent(this.panelSetting))
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
										.addComponent(this.panelOperators)
										.addComponent(this.panelSetting))
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

	private void initPanelOperators() {
		this.panelOperators.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_CommonOperator")));
		this.panelOperators.setLayout(new GridBagLayout());
		this.panelOperators.add(this.jButtonPlus, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonSubtract, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonMultiply, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonDivide, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.panelOperators.add(this.jButtonMore, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonLess, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonEqual, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonBracket, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.panelOperators.add(this.jButtonMoreOrEqual, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonLessOrEqual, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonLeftBracket, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonRightBracket, new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.panelOperators.add(this.jButtonAnd, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonXOR, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonOr, new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.panelOperators.add(this.jButtonNot, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
	}

	private void initPanelSetting() {
		this.panelSetting.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_FormEdgeCount_Text")));
		GroupLayout groupLayout = new GroupLayout(this.panelSetting);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.labelPixelFormat)
								.addGap(10, 10, 10)
								.addComponent(this.comboBoxPixelFormat))
						.addComponent(this.checkBoxCompress)
						.addComponent(this.checkBoxIngoreNoValueCell))
		);

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelPixelFormat)
						.addComponent(this.comboBoxPixelFormat, 20, 20, 20))
				.addContainerGap(10, Short.MAX_VALUE)
				.addComponent(this.checkBoxCompress)
				.addContainerGap(10, Short.MAX_VALUE)
				.addComponent(this.checkBoxIngoreNoValueCell)
		);

		this.panelSetting.setLayout(groupLayout);
//		this.panelSetting.add(this.labelPixelFormat, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
//		this.panelSetting.add(this.comboBoxPixelFormat, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
//		this.panelSetting.add(this.checkBoxCompress, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
//		this.panelSetting.add(this.checkBoxIngoreNoValueCell, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
	}

	private void initComponentsState() {
		this.jComboBoxBasicMathFunction.setModel(new DefaultComboBoxModel<String>(new String[]{"abs()",
				"mod(,)", "floor()"}));
		this.jComboBoxTriangleFunction.setModel(new DefaultComboBoxModel<String>(new String[]{"sin()",
				"cos()", "tan()", "cot()", "asin()", "acos()", "atan()", "acot()", "sinh()",
				"cosh()", "tanh()", "coth()"}));
		this.jComboBoxSeniorMathFunction.setModel(new DefaultComboBoxModel<String>(new String[]{"exp()",
				"pow(,)", "sqrt()", "ln()", "log()"}));
		this.jComboBoxOtherFunction.setModel(new DefaultComboBoxModel<String>(new String[]{"Con(,,)",
				"IsNull()", "Pick(,,)"}));
		this.comboBoxPixelFormat.setModel(new DefaultComboBoxModel<String>(new String[]{PixelFormatUtilities.toString(PixelFormat.SINGLE),
				PixelFormatUtilities.toString(PixelFormat.DOUBLE), PixelFormatUtilities.toString(PixelFormat.BIT8),
				PixelFormatUtilities.toString(PixelFormat.BIT16), PixelFormatUtilities.toString(PixelFormat.BIT32),
				PixelFormatUtilities.toString(PixelFormat.BIT64), PixelFormatUtilities.toString(PixelFormat.UBIT1),
				PixelFormatUtilities.toString(PixelFormat.UBIT4), PixelFormatUtilities.toString(PixelFormat.UBIT8),
				PixelFormatUtilities.toString(PixelFormat.UBIT16), PixelFormatUtilities.toString(PixelFormat.UBIT32)}));
		this.textAreaOperationString.setLineWrap(true);
		this.textAreaOperationString.setWrapStyleWord(true);
		if (this.prePixelFormat == null) {
			this.comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(PixelFormat.UBIT32));
		} else {
			this.comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(this.prePixelFormat));
		}
		this.checkBoxCompress.setSelected(this.isZip);
		this.checkBoxIngoreNoValueCell.setSelected(this.isIgnoreNoValue);
		this.textAreaOperationString.setText(this.preExpression);
	}

	private void initResources() {
		this.jButtonImport.setText(ControlsProperties.getString("string_button_import"));
		this.jButtonExport.setText(ControlsProperties.getString("String_Button_Export"));
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
		this.jButtonPlus.addMouseListener(this.numberMouseAdapter);
		this.jButtonSubtract.addMouseListener(this.numberMouseAdapter);
		this.jButtonMultiply.addMouseListener(this.numberMouseAdapter);
		this.jButtonDivide.addMouseListener(this.numberMouseAdapter);
		this.jButtonMore.addMouseListener(this.numberMouseAdapter);
		this.jButtonLess.addMouseListener(this.numberMouseAdapter);
		this.jButtonEqual.addMouseListener(this.numberMouseAdapter);
		this.jButtonBracket.addMouseListener(this.numberMouseAdapter);
		this.jButtonMoreOrEqual.addMouseListener(this.numberMouseAdapter);
		this.jButtonLessOrEqual.addMouseListener(this.numberMouseAdapter);
		this.jButtonLeftBracket.addMouseListener(this.numberMouseAdapter);
		this.jButtonRightBracket.addMouseListener(this.numberMouseAdapter);
		this.jButtonAnd.addMouseListener(this.numberMouseAdapter);
		this.jButtonNot.addMouseListener(this.numberMouseAdapter);
		this.jButtonXOR.addMouseListener(this.numberMouseAdapter);
		this.jButtonOr.addMouseListener(this.numberMouseAdapter);
		this.jComboBoxBasicMathFunction.addActionListener(this.functionComboBoxActionListener);
		this.jComboBoxTriangleFunction.addActionListener(this.functionComboBoxActionListener);
		this.jComboBoxSeniorMathFunction.addActionListener(this.functionComboBoxActionListener);
		this.jComboBoxOtherFunction.addActionListener(this.functionComboBoxActionListener);
		this.jButtonExport.addActionListener(this.buttonExportActionListener);
		this.jButtonImport.addActionListener(this.buttonImportActionListener);
		this.buttonOK.addActionListener(this.buttonDefaultActionListener);
		this.buttonClear.addActionListener(this.buttonDefaultActionListener);
		this.buttonClose.addActionListener(this.buttonDefaultActionListener);
	}

	private void removeListener() {
		this.workspaceTree.removeMouseListener(this.mouseAdapterWorkspaceTress);
		this.jButtonPlus.removeMouseListener(this.numberMouseAdapter);
		this.jButtonSubtract.removeMouseListener(this.numberMouseAdapter);
		this.jButtonMultiply.removeMouseListener(this.numberMouseAdapter);
		this.jButtonDivide.removeMouseListener(this.numberMouseAdapter);
		this.jButtonMore.removeMouseListener(this.numberMouseAdapter);
		this.jButtonLess.removeMouseListener(this.numberMouseAdapter);
		this.jButtonEqual.removeMouseListener(this.numberMouseAdapter);
		this.jButtonBracket.removeMouseListener(this.numberMouseAdapter);
		this.jButtonMoreOrEqual.removeMouseListener(this.numberMouseAdapter);
		this.jButtonLessOrEqual.removeMouseListener(this.numberMouseAdapter);
		this.jButtonLeftBracket.removeMouseListener(this.numberMouseAdapter);
		this.jButtonRightBracket.removeMouseListener(this.numberMouseAdapter);
		this.jButtonAnd.removeMouseListener(this.numberMouseAdapter);
		this.jButtonNot.removeMouseListener(this.numberMouseAdapter);
		this.jButtonXOR.removeMouseListener(this.numberMouseAdapter);
		this.jButtonOr.removeMouseListener(this.numberMouseAdapter);
		this.jComboBoxBasicMathFunction.removeActionListener(this.functionComboBoxActionListener);
		this.jComboBoxTriangleFunction.removeActionListener(this.functionComboBoxActionListener);
		this.jComboBoxSeniorMathFunction.removeActionListener(this.functionComboBoxActionListener);
		this.jComboBoxOtherFunction.removeActionListener(this.functionComboBoxActionListener);
		this.jButtonExport.removeActionListener(this.buttonExportActionListener);
		this.jButtonImport.removeActionListener(this.buttonImportActionListener);
		this.buttonOK.removeActionListener(this.buttonDefaultActionListener);
		this.buttonClear.removeActionListener(this.buttonDefaultActionListener);
		this.buttonClose.removeActionListener(this.buttonDefaultActionListener);
	}

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
						textAreaOperationString.requestFocusInWindow();
						setExpressionSentenceText(textAreaOperationString, "[" + ((Dataset) data).getDatasource().getAlias() + "." + ((Dataset) data).getName() + "]", "datasetName");
					}
				}
			}
		}
	};

	private MouseAdapter numberMouseAdapter = new LocalMouseAdapter();

	private ActionListener functionComboBoxActionListener = new LocalComboboxAction();

	private ActionListener buttonExportActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			exportXML();
		}
	};

	private ActionListener buttonImportActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			importXML();
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
			} else if (e.getSource().equals(buttonClose)) {
				RasterAlgebraOperationDialog.this.dispose();
			}
		}
	};

	private void buttonOperator_Click(String operatorString) {
		this.textAreaOperationString.requestFocusInWindow();
		operatorString = " " + operatorString;
		setExpressionSentenceText(this.textAreaOperationString, operatorString, "");
	}

	private void comboBoxFunction_SelectedIndexChanged(JComboBox<?> comboBox) {
		this.textAreaOperationString.requestFocusInWindow();
		String functionString = comboBox.getSelectedItem().toString();
		setExpressionSentenceText(this.textAreaOperationString, functionString, "function");
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

	private void exportXML() {
		String moduleName = "ExportRasterAlgebraExpression";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_RasterAlgebraExpression"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_SaveAsFile"), moduleName, "SaveOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		smFileChoose.setSelectedFile(new File("GridMathAnalystInfo.xml"));
		int state = smFileChoose.showDefaultDialog();
		String filePath = "";
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = smFileChoose.getFilePath();
			File oleFile = new File(filePath);
			filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".xml";
			File NewFile = new File(filePath);
			oleFile.renameTo(NewFile);
			if (oleFile.isFile() && oleFile.exists()) {
				oleFile.delete();
			}
			if (MathAnalyst.toXMLFile(filePath, this.textAreaOperationString.getText(), null, PixelFormatUtilities.valueOf(this.comboBoxPixelFormat.getSelectedItem().toString())
					, this.checkBoxCompress.isSelected(), this.checkBoxIngoreNoValueCell.isSelected())) {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionExportSuccess") + filePath);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionExportFailed"));
			}
		}
	}

	private void importXML() {
		String moduleName = "InputRasterAlgebraExpression";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_RasterAlgebraExpression"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_OpenRasterAlgebraExpressionFile"), moduleName, "OpenMany");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		int state = smFileChoose.showDefaultDialog();
		String filePath = "";
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = smFileChoose.getFilePath();
			RasterAlgebraExpressionXml rasterAlgebraExpressionXml = new RasterAlgebraExpressionXml();
			rasterAlgebraExpressionXml.parserXml(filePath);
			if (rasterAlgebraExpressionXml.isImportResult()) {
				this.textAreaOperationString.setText(rasterAlgebraExpressionXml.getExpression());
				this.comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(rasterAlgebraExpressionXml.getPixelFormat()));
				this.checkBoxCompress.setSelected(rasterAlgebraExpressionXml.isZip());
				this.checkBoxIngoreNoValueCell.setSelected(rasterAlgebraExpressionXml.isIgnoreNoValue());
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionInputSuccess") + filePath);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionInputFailed"));
			}
		}
	}

	public String getExpression() {
		return this.textAreaOperationString.getText();
	}

	public PixelFormat getPixelFormat() {
		return PixelFormatUtilities.valueOf(this.comboBoxPixelFormat.getSelectedItem().toString());
	}

	public boolean isZip() {
		return this.checkBoxCompress.isSelected();
	}

	public boolean isIgnoreNoValue() {
		return this.checkBoxIngoreNoValueCell.isSelected();
	}

	class LocalMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			buttonOperator_Click(((JButton) e.getSource()).getText());
		}
	}

	class LocalComboboxAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			comboBoxFunction_SelectedIndexChanged((JComboBox<?>) e.getSource());
		}
	}
}
