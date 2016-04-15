package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.data.QueryParameter;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.button.SmButton;

public class SQLExpressionDialog extends SmDialog {
	private JButton jButtonCancel;
	private JButton jButtonOK;
	private JTable jTableFieldInfo;
	private JScrollPane jScrollPanel;
	private JComboBox<String> jComboBoxTimeFunction;
	private JComboBox<String> jComboBoxStringFunction;
	private JComboBox<String> jComboBoxMathsOperation;
	private JLabel jLabelTimefunction;
	private JLabel jLabelStringfunction;
	private JLabel jLabelMathsoperation;
	private JPanel jPanelFunction;
	private JTextArea jTextAreaSQLSentence;
	private JPanel jPanelCommonOperator;
	private JButton jButtonOr;
	private JButton jButtonLike;
	private JButton jButtonNot;
	private JButton jButtonAnd;
	private JButton jButtonAndCompute;
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

	private transient Dataset[] filedDatasets;
	private transient List<FieldType> thisFieldTypes;
	private transient Object[][] tableData;
	private DefaultTableModel defaultTableModel;
	private static String[] NAMES = { ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption"),
			ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName"),
			ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType") };
	private transient QueryParameter filedQueryParameter;
	private transient DialogResult dialogResult = DialogResult.CANCEL;
	private MouseAdapter mouseAdapter = new LocalMouseAdapter();
	private ActionListener actionListener = new LocalComboboxAction();
	private ActionListener buttonActionListener = new LocalButtonAction();
	private MouseAdapter tableMouseAdapter = new LocalTableMouseAdapter();

	private static Map<FieldType, String> map;

	private void initialDialog(String expression) {
		setSize(720, 350);
		setTitle(ControlsProperties.getString("String_SQLExpression"));
		setName("SQLExpressionDialog");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		getContentPane().setLayout(new GridBagLayout());
		this.jTextAreaSQLSentence = new JTextArea();
		initPanelCommonOperator();
		initPanelFunction();
		this.jButtonOK = new SmButton();
		this.jButtonCancel = new SmButton();
		this.jScrollPanel = new JScrollPane();
		//@formatter:off
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.jButtonOK,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
		panelButton.add(this.jButtonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
		getContentPane().add(this.jTextAreaSQLSentence, new GridBagConstraintsHelper(0, 0, 6, 3).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5).setWeight(3, 3));
		getContentPane().add(this.jPanelCommonOperator, new GridBagConstraintsHelper(0, 3, 2, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3,5,5,3).setWeight(0, 0).setIpad(10, 30));
		getContentPane().add(this.jPanelFunction,       new GridBagConstraintsHelper(2, 3, 2, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3,0,5,3).setWeight(0, 0).setIpad(0, 30));
		getContentPane().add(this.jScrollPanel,         new GridBagConstraintsHelper(4, 3, 2, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3,0,5,5).setWeight(0, 0).setIpad(20, 30));
		getContentPane().add(panelButton,               new GridBagConstraintsHelper(0, 5, 6, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
		//@formatter:on
		this.jScrollPanel.setViewportView(getTableFieldInfo());
		this.jTextAreaSQLSentence.setText(expression);
		intializeForm();
		initialFieldTypeMap();
		initResources();
		registActionListener();
//		initTraversalPolicy();
		this.jTextAreaSQLSentence.select(0, expression.length());
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		unRegistActionListener();
		this.jButtonPlus.addMouseListener(this.mouseAdapter);
		this.jButtonSubtract.addMouseListener(this.mouseAdapter);
		this.jButtonMultiply.addMouseListener(this.mouseAdapter);
		this.jButtonDivide.addMouseListener(this.mouseAdapter);
		this.jButtonMore.addMouseListener(this.mouseAdapter);
		this.jButtonLess.addMouseListener(this.mouseAdapter);
		this.jButtonEqual.addMouseListener(this.mouseAdapter);
		this.jButtonBracket.addMouseListener(this.mouseAdapter);
		this.jButtonMoreOrEqual.addMouseListener(this.mouseAdapter);
		this.jButtonLessOrEqual.addMouseListener(this.mouseAdapter);
		this.jButtonLeftBracket.addMouseListener(this.mouseAdapter);
		this.jButtonRightBracket.addMouseListener(this.mouseAdapter);
		this.jButtonAndCompute.addMouseListener(this.mouseAdapter);
		this.jButtonAnd.addMouseListener(this.mouseAdapter);
		this.jButtonNot.addMouseListener(this.mouseAdapter);
		this.jButtonLike.addMouseListener(this.mouseAdapter);
		this.jButtonOr.addMouseListener(this.mouseAdapter);
		this.jComboBoxMathsOperation.addActionListener(this.actionListener);
		this.jComboBoxStringFunction.addActionListener(this.actionListener);
		this.jComboBoxTimeFunction.addActionListener(this.actionListener);
		this.jButtonOK.addActionListener(this.buttonActionListener);
		this.jButtonCancel.addActionListener(this.buttonActionListener);
		this.jTableFieldInfo.addMouseListener(this.tableMouseAdapter);
	}

	/**
	 * 注销事件
	 */
	private void unRegistActionListener() {
		this.jButtonPlus.removeMouseListener(this.mouseAdapter);
		this.jButtonSubtract.removeMouseListener(this.mouseAdapter);
		this.jButtonMultiply.removeMouseListener(this.mouseAdapter);
		this.jButtonDivide.removeMouseListener(this.mouseAdapter);
		this.jButtonMore.removeMouseListener(this.mouseAdapter);
		this.jButtonLess.removeMouseListener(this.mouseAdapter);
		this.jButtonEqual.removeMouseListener(this.mouseAdapter);
		this.jButtonBracket.removeMouseListener(this.mouseAdapter);
		this.jButtonMoreOrEqual.removeMouseListener(this.mouseAdapter);
		this.jButtonLessOrEqual.removeMouseListener(this.mouseAdapter);
		this.jButtonLeftBracket.removeMouseListener(this.mouseAdapter);
		this.jButtonRightBracket.removeMouseListener(this.mouseAdapter);
		this.jButtonAndCompute.removeMouseListener(this.mouseAdapter);
		this.jButtonAnd.removeMouseListener(this.mouseAdapter);
		this.jButtonNot.removeMouseListener(this.mouseAdapter);
		this.jButtonLike.removeMouseListener(this.mouseAdapter);
		this.jButtonOr.removeMouseListener(this.mouseAdapter);
		this.jComboBoxMathsOperation.removeActionListener(this.actionListener);
		this.jComboBoxStringFunction.removeActionListener(this.actionListener);
		this.jComboBoxTimeFunction.removeActionListener(this.actionListener);
		this.jButtonOK.removeActionListener(this.buttonActionListener);
		this.jButtonCancel.removeActionListener(this.buttonActionListener);
		this.jTableFieldInfo.removeMouseListener(this.tableMouseAdapter);
	}

	private void initResources() {
		this.jButtonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.jButtonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.jComboBoxMathsOperation
				.setModel(new DefaultComboBoxModel<String>(new String[] { "", "Abs()", "Acos()", "Asin()", "Atan()", "Atn2()", "Ceiling()", "Cos()", "Cot()",
						"Degrees()", "Exp()", "Floor()", "Log()", "Log10()", "PI()", "Power()", "Radians()", "Rand()", "Round()", "Sign()", "Sin()",
						"Square()", "Sqrt()", "Tan()", "CBool()", "CByte()", "CCur()", "CDate()", "CDbl()", "CInt()", "CLng()", "CSng()", "CStr()", "Int()",
						"Fix()" }));
		this.jComboBoxStringFunction.setModel(new DefaultComboBoxModel<String>(new String[] { "", "Ascii()", "Char()", "Charindex()", "Difference()", "Left()",
				"Len()", "Lower()", "Ltrim()", "Nchar()", "Patindex()", "Replace()", "Replicate()", "Quotename()", "Reverse()", "Right()", "Rtrim()",
				"Soundex()", "Space()", "Str()", "Stuff()", "Substring()", "Unicode()", "Upper()" }));
		this.jComboBoxTimeFunction.setModel(new DefaultComboBoxModel<String>(new String[] { "", "DateAdd()", "Datediff()", "Datename()", "Datepart()", "Day()",
				"Getdate()", "Getutcdate()", "Month()", "Year()" }));
		this.jLabelMathsoperation.setText(ControlsProperties.getString("String_ArithmeticOperation"));
		this.jLabelStringfunction.setText(ControlsProperties.getString("String_CharacterHandling"));
		this.jLabelTimefunction.setText(ControlsProperties.getString("String_DataAndTime"));
	}

	private void initPanelFunction() {
		this.jPanelFunction = new JPanel();
		this.jPanelFunction.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), ControlsProperties.getString("String_CommonFunction"),
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		this.jLabelMathsoperation = new JLabel();

		this.jLabelStringfunction = new JLabel();
		this.jLabelTimefunction = new JLabel();
		this.jComboBoxMathsOperation = new JComboBox<String>();
		this.jComboBoxStringFunction = new JComboBox<String>();
		this.jComboBoxTimeFunction = new JComboBox<String>();

		//@formatter:off
		this.jPanelFunction.setLayout(new GridBagLayout());
		this.jPanelFunction.add(this.jLabelMathsoperation,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1));
		this.jPanelFunction.add(this.jComboBoxMathsOperation, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelFunction.add(this.jLabelStringfunction,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1));
		this.jPanelFunction.add(this.jComboBoxStringFunction, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelFunction.add(this.jLabelTimefunction,      new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1));
		this.jPanelFunction.add(this.jComboBoxTimeFunction,   new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void initPanelCommonOperator() {
		this.jPanelCommonOperator = new JPanel();
		this.jPanelCommonOperator.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), ControlsProperties.getString("String_CommonOperator"),
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
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
		this.jButtonLike = new JButton("Like");
		this.jButtonAndCompute = new JButton("&");
		this.jButtonOr = new JButton("Or");
		setOperatorButtonSize();
		//@formatter:off
		this.jPanelCommonOperator.setLayout(new GridBagLayout());
		this.jPanelCommonOperator.add(this.jButtonPlus,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonSubtract, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonMultiply, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonDivide,   new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonAnd,      new GridBagConstraintsHelper(4, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		
		this.jPanelCommonOperator.add(this.jButtonMore,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonLess,     new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonEqual,    new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonBracket,  new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonNot,      new GridBagConstraintsHelper(4, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		
		this.jPanelCommonOperator.add(this.jButtonMoreOrEqual, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonLessOrEqual, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonLeftBracket, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonRightBracket,new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonLike,        new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		
		this.jPanelCommonOperator.add(this.jButtonAndCompute,  new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.jPanelCommonOperator.add(this.jButtonOr,          new GridBagConstraintsHelper(4, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void setOperatorButtonSize() {
		Dimension dimension = new Dimension(24, 30);
		this.jButtonPlus.setPreferredSize(dimension);
		this.jButtonSubtract.setPreferredSize(dimension);
		this.jButtonMultiply.setPreferredSize(dimension);
		this.jButtonDivide.setPreferredSize(dimension);
		this.jButtonMore.setPreferredSize(dimension);
		this.jButtonLess.setPreferredSize(dimension);
		this.jButtonEqual.setPreferredSize(dimension);
		this.jButtonBracket.setPreferredSize(dimension);
		this.jButtonMoreOrEqual.setPreferredSize(dimension);
		this.jButtonLessOrEqual.setPreferredSize(dimension);
		this.jButtonLeftBracket.setPreferredSize(dimension);
		this.jButtonRightBracket.setPreferredSize(dimension);
		this.jButtonAndCompute.setPreferredSize(dimension);
		Dimension tempDimension = new Dimension(36, 30);
		this.jButtonAnd.setPreferredSize(tempDimension);
		this.jButtonNot.setPreferredSize(tempDimension);
		this.jButtonLike.setPreferredSize(tempDimension);
		this.jButtonOr.setPreferredSize(tempDimension);
	}

	// 初始化操作
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 默认构造函数
	 */
	public SQLExpressionDialog() {
		super();
		setModal(true);
	}

	/**
	 * // 显示对话框，过滤字段类型
	 * 
	 * @param datasets
	 * @param fieldTypes
	 */
	public DialogResult showDialog(Dataset[] datasets, List<FieldType> fieldTypes, String sqlExpression) {
		initialDialog(sqlExpression);

		this.filedDatasets = datasets;

		if (fieldTypes == null) {
			this.thisFieldTypes.add(FieldType.BOOLEAN);
			this.thisFieldTypes.add(FieldType.BYTE);
			this.thisFieldTypes.add(FieldType.INT16);
			this.thisFieldTypes.add(FieldType.INT32);
			this.thisFieldTypes.add(FieldType.SINGLE);
			this.thisFieldTypes.add(FieldType.DOUBLE);
			this.thisFieldTypes.add(FieldType.TEXT);
			this.thisFieldTypes.add(FieldType.LONGBINARY);
			this.thisFieldTypes.add(FieldType.CHAR);
			this.thisFieldTypes.add(FieldType.DATETIME);
			this.thisFieldTypes.add(FieldType.WTEXT);
		} else {
			this.thisFieldTypes = fieldTypes;
		}

		setTableFieldInfo(datasets, thisFieldTypes);
		this.setVisible(true);
		return dialogResult;
	}

	/**
	 * // 显示对话框，不过滤字段类型
	 * 
	 * @param datasets
	 */
	public DialogResult showDialog(String sqlExpression, Dataset... datasets) {
		initialDialog(sqlExpression);

		this.filedDatasets = datasets;

		setTableFieldInfo(filedDatasets);
		this.setVisible(true);
		return dialogResult;

	}

	private void initTraversalPolicy() {
		this.componentList.add(this.jButtonOK);
		this.componentList.add(this.jButtonCancel);
		this.setFocusTraversalPolicy(policy);
		this.getRootPane().setDefaultButton(this.jButtonOK);
	}

	private void intializeForm() {
		try {
			this.requestFocusInWindow();
			this.jComboBoxMathsOperation.setSelectedIndex(0);
			this.jComboBoxStringFunction.setSelectedIndex(0);
			this.jComboBoxTimeFunction.setSelectedIndex(0);

		} catch (Exception e) {
			// do nothing
		}

	}

	/**
	 * 填充TableFieldInfo，不过滤字段
	 */
	private void setTableFieldInfo(Dataset[] datasets) {
		int allFieldCount = 0;
		DatasetVector datasetVector = null;

		for (int i = 0; i < datasets.length; i++) {
			datasetVector = (DatasetVector) datasets[i];

			if (datasetVector != null) {
				allFieldCount += datasetVector.getFieldCount();
			}
		}

		this.tableData = new Object[allFieldCount][3];

		int rowCount = 0;
		String fieldNameColumn = "";
		String fieldTypeColumn = "";
		String fieldCaptionColumn = "";
		FieldType fieldType;

		for (int i = 0; i < datasets.length; i++) {
			datasetVector = (DatasetVector) datasets[i];

			if (datasetVector != null) {
				for (int fieldCount = 0; fieldCount < datasetVector.getFieldCount(); fieldCount++, rowCount++) {

					fieldType = datasetVector.getFieldInfos().get(fieldCount).getType();
					if (datasets.length == 1) {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getFieldInfos().get(fieldCount).getName();
					} else {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getTableName() + "." + datasetVector.getFieldInfos().get(fieldCount).getName();
					}
					this.tableData[rowCount][0] = fieldCaptionColumn;
					this.tableData[rowCount][1] = fieldNameColumn;
					this.tableData[rowCount][2] = fieldTypeColumn;
				}
			}
		}

		this.defaultTableModel = new cellEditableModel(tableData, NAMES);
		this.jTableFieldInfo.setModel(defaultTableModel);
		this.jTableFieldInfo.repaint();
		resetTableCell();
	}

	/**
	 * // 填充TableFieldInfo，过滤字段类型
	 * 
	 * @param datasets
	 * @param fieldTypes
	 */
	private void setTableFieldInfo(Dataset[] datasets, List<FieldType> fieldTypes) {
		int allFieldCount = 0;
		DatasetVector datasetVector = null;
		FieldType fieldType;

		for (int i = 0; i < datasets.length; i++) {
			datasetVector = (DatasetVector) datasets[i];

			if (datasetVector != null) {
				for (int fieldCount = 0; fieldCount < datasetVector.getFieldCount(); fieldCount++) {
					fieldType = datasetVector.getFieldInfos().get(fieldCount).getType();

					if (fieldTypes.contains(fieldType)) {
						allFieldCount++;
					}
				}
			}
		}

		this.tableData = new Object[allFieldCount][3];

		int rowCount = 0;
		String fieldNameColumn = null;
		String fieldTypeColumn = null;
		String fieldCaptionColumn = null;
		for (int ndataset = 0; ndataset < datasets.length; ndataset++) {
			datasetVector = (DatasetVector) datasets[ndataset];
			for (int fieldCount = 0; fieldCount < datasetVector.getFieldCount(); fieldCount++) {

				fieldType = datasetVector.getFieldInfos().get(fieldCount).getType();
				if (fieldTypes.contains(fieldType)) {
					if (datasets.length == 1) {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getFieldInfos().get(fieldCount).getName();
					} else {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getTableName() + "." + datasetVector.getFieldInfos().get(fieldCount).getName();
					}
					this.tableData[rowCount][0] = fieldCaptionColumn;
					this.tableData[rowCount][1] = fieldNameColumn;
					this.tableData[rowCount][2] = fieldTypeColumn;
					rowCount++;
				}
			}
		}

		this.defaultTableModel = new cellEditableModel(tableData, NAMES);
		this.jTableFieldInfo.setModel(defaultTableModel);
		this.jTableFieldInfo.repaint();
		resetTableCell();
	}

	private void resetTableCell() {
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setPreferredWidth(
				80);
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setCellRenderer(
				TableTooltipCellRenderer.getInstance());
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName"))
				.setPreferredWidth(100);
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName")).setCellRenderer(
				TableTooltipCellRenderer.getInstance());
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setPreferredWidth(80);
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setCellRenderer(
				TableTooltipCellRenderer.getInstance());
	}

	/**
	 * 构造JTable类型的FieldInfo，其中实现了方法：双击将table中的信息并将该信息加到TextArea中
	 */
	private JTable getTableFieldInfo() {
		if (this.jTableFieldInfo == null) {
			this.defaultTableModel = new DefaultTableModel(NAMES, 0);
			this.jTableFieldInfo = new JTable(defaultTableModel);
			this.jTableFieldInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return this.jTableFieldInfo;
	}

	/**
	 * 点击Button之后，将Button上的字符内容输入到textArea中
	 * 
	 * @param botton
	 */
	private void buttonOperator_Click(JButton botton) {
		this.jTextAreaSQLSentence.requestFocusInWindow();

		String opertorString = botton.getText();
		opertorString = " " + opertorString;
		setSQLSentenceText(this.jTextAreaSQLSentence, opertorString, "");
	}

	/**
	 * 选择各项函数填充表达式文本框
	 * 
	 * @param comboBox
	 */
	private void comboBoxFunction_SelectedIndexChanged(JComboBox<?> comboBox) {
		this.jTextAreaSQLSentence.requestFocusInWindow();

		if (comboBox.getSelectedIndex() != 0) {
			String functionString = comboBox.getSelectedItem().toString();
			functionString = " " + functionString;
			setSQLSentenceText(this.jTextAreaSQLSentence, functionString, "fuction");
		}
	}

	public QueryParameter getQueryParameter() {
		return this.filedQueryParameter;

	}

	public void setQueryParameter(QueryParameter queryParameter) {
		this.filedQueryParameter = queryParameter;

	}

	// 构造控件相关信息///////////////////////////////////////////////////////////////////////////////////////////

	private static void setSQLSentenceText(JTextArea textArea, String sentence, String type) {
		StringBuilder sqlSentence = new StringBuilder(textArea.getText());

		int startPosition = textArea.getSelectionStart();
		int endPosition = textArea.getSelectionEnd();

		sqlSentence.delete(startPosition, endPosition);
		textArea.setText(sqlSentence.toString());

		textArea.insert(sentence, startPosition);
		textArea.setSelectionStart(startPosition + sentence.length());

		// 当为函数形式时，光标应该在括号里面
		if (type == "fuction") {
			textArea.setCaretPosition(startPosition + sentence.length() - 1);
		} else {
			textArea.setCaretPosition(startPosition + sentence.length());
		}

		textArea.requestFocusInWindow();
	}

	/**
	 *
	 * @author 重载DefaultTableModel，因为涉及到的isCellEditable在默认的DefaultTableModel中为true，导致无法双击选中单元格
	 */
	class cellEditableModel extends DefaultTableModel {
		public cellEditableModel(Object[][] data, Object[] columnNames) {
			setDataVector(data, columnNames);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	/**
	 * 判断sql字符串中括号是否全,如果全就返回true，否则返回false
	 */
	public Boolean bracketCheck(String sqlStr) {
		Boolean checkingResult = false;
		StringBuilder sqlStrbuilBuilder = new StringBuilder(sqlStr);
		int leftBracketCount = 0;
		int rightBracketCount = 0;

		for (int i = 0; i < sqlStrbuilBuilder.length(); i++) {
			char c = sqlStrbuilBuilder.charAt(i);
			if (c == '(') {
				leftBracketCount++;
			} else if (c == ')') {
				rightBracketCount++;
			}
		}
		if (leftBracketCount == rightBracketCount) {
			checkingResult = true;
		}

		return checkingResult;
	}

	/**
	 * 将字段类型由英文转换为中文
	 */
	private String getFiledTypeChineseName(FieldType fieldType) {
		return map.get(fieldType);
	}

	private static void initialFieldTypeMap() {
		map = new HashMap<FieldType, String>();
		map.put(FieldType.BOOLEAN, ControlsProperties.getString("String_FiledType_Boolean"));
		map.put(FieldType.BYTE, ControlsProperties.getString("String_FiledType_Byte"));
		map.put(FieldType.CHAR, ControlsProperties.getString("String_FiledType_Character"));
		map.put(FieldType.DOUBLE, ControlsProperties.getString("String_NumberType_Double"));
		map.put(FieldType.DATETIME, ControlsProperties.getString("String_FiledType_Date"));
		map.put(FieldType.INT16, ControlsProperties.getString("String_FiledType_Integer16"));
		map.put(FieldType.INT32, ControlsProperties.getString("String_FiledType_Integer32"));
		map.put(FieldType.INT64, ControlsProperties.getString("String_FiledType_Integer64"));
		map.put(FieldType.LONGBINARY, ControlsProperties.getString("String_FiledType_Binary"));
		map.put(FieldType.SINGLE, ControlsProperties.getString("String_FiledType_SinglePrecision"));
		map.put(FieldType.TEXT, ControlsProperties.getString("String_FiledType_Text"));
		map.put(FieldType.WTEXT, ControlsProperties.getString("String_FiledType_WText"));
	}

	private void buttonOkClicked() {
		filedQueryParameter = new QueryParameter();
		filedQueryParameter.setAttributeFilter(jTextAreaSQLSentence.getText());
		dialogResult = DialogResult.OK;
		dispose();
	}

	private void buttonCancelClicked() {
		dialogResult = DialogResult.CANCEL;
		unRegistActionListener();
		dispose();
	}

	class LocalTableMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			// //双击ListViewFieldInfo的字段信息，将字段名插入到表达式文本框的SelectionStart位置
			if (e.getClickCount() == 2) {
				Point point = new Point(e.getX(), e.getY());
				int row = jTableFieldInfo.getSelectedRow();
				int column = jTableFieldInfo.getSelectedColumn();

				if (jTableFieldInfo.getCellRect(row, column, false).contains(point)) {
					String text = jTableFieldInfo.getValueAt(row, 1).toString();
					if (jTextAreaSQLSentence.getSelectionStart() != 0) {
						text = " " + text;
					}

					setSQLSentenceText(jTextAreaSQLSentence, text, "");
				}
			}
		}
	}

	class LocalButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jButtonOK) {
				buttonOkClicked();
			}
			if (e.getSource() == jButtonCancel) {
				buttonCancelClicked();
			}
		}

	}

	class LocalComboboxAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			comboBoxFunction_SelectedIndexChanged((JComboBox<?>) e.getSource());
		}
	}

	class LocalMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			buttonOperator_Click((JButton) e.getSource());
		}
	}

	static class TableTooltipCellRenderer extends JLabel implements TableCellRenderer {
		private static TableTooltipCellRenderer tooltipCellRenderer;

		private TableTooltipCellRenderer() {

		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			if (null != value) {
				this.setText((String) value);
				this.setToolTipText((String) value);
			} else {
				this.setText((String) value);
			}
			if (isSelected) {
				this.setOpaque(true);
				this.setBackground(new Color(36, 124, 255));
			} else {
				this.setOpaque(true);
				this.setBackground(Color.white);
			}
			return this;
		}

		public static TableTooltipCellRenderer getInstance() {
			if (null == tooltipCellRenderer) {
				tooltipCellRenderer = new TableTooltipCellRenderer();
			}
			return tooltipCellRenderer;
		}
	}
}
