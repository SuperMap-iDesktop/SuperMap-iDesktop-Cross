package com.supermap.desktop.ui.controls;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
	private JButton jButtonSpace;
	private JButton jButtonC;
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
	private static String[] NAMES = {ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption"),
			ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName"),
			ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")};
	private transient QueryParameter filedQueryParameter;
	private transient DialogResult dialogResult = DialogResult.CANCEL;
	private MouseAdapter mouseAdapter = new LocalMouseAdapter();
	private ActionListener actionListener = new LocalComboboxAction();
	private ActionListener buttonActionListener = new LocalButtonAction();
	private MouseAdapter tableMouseAdapter = new LocalTableMouseAdapter();

	private static Map<FieldType, String> map;

	//获取唯一值面板相关控件-yuanR
	private JPanel jPanelGetAllValue;
	private JButton jButtonGetAllValue;
	private JScrollPane scrollPaneAllValue;
	private GetAllValueList listAllValue;
	//	private JLabel labelGoTO;
//	private JTextField textFieldGOTO;
	//清除按钮-yuanR
	private JButton jButtonClear;


	private void initialDialog(String expression) {
		setSize(850, 550);
		setMinimumSize(new Dimension(850, 550));
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

		this.jTextAreaSQLSentence = new JTextArea();
		this.jTextAreaSQLSentence.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		this.jTextAreaSQLSentence.setLineWrap(true);

		initPanelCommonOperator();
		initPanelGetAllValue();
		initPanelFunction();
		this.jButtonOK = new SmButton();
		this.jButtonCancel = new SmButton();
		this.jButtonClear = new SmButton("clear");
		this.jScrollPanel = new JScrollPane();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.jButtonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(2, 0, 5, 5));
		panelButton.add(this.jButtonClear, new GridBagConstraintsHelper(1, 0, 1, 1).setInsets(2, 0, 5, 5));
		panelButton.add(this.jButtonCancel, new GridBagConstraintsHelper(2, 0, 1, 1).setInsets(2, 0, 5, 5));

		mainPanel.add(this.jScrollPanel, new GridBagConstraintsHelper(0, 0, 2, 2).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(15, 15, 5, 5));
		mainPanel.add(this.jPanelFunction, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.BOTH).setInsets(15, 5, 5, 15));
		mainPanel.add(this.jPanelCommonOperator, new GridBagConstraintsHelper(2, 1, 1, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 5, 15).setIpad(30, 50));

		mainPanel.add(this.jPanelGetAllValue, new GridBagConstraintsHelper(0, 2, 1, 2).setWeight(0, 0).setFill(GridBagConstraints.BOTH).setInsets(5, 15, 5, 5).setIpad(100, 120));
		mainPanel.add(this.jTextAreaSQLSentence, new GridBagConstraintsHelper(1, 2, 2, 2).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 5, 15).setIpad(0, 120));
		//将获取唯一值面板加入主panel3, 2, 1, 2
		mainPanel.add(panelButton, new GridBagConstraintsHelper(2, 4, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 15, 15));
		this.add(mainPanel);
		this.jScrollPanel.setViewportView(getTableFieldInfo());
		this.jScrollPanel.getViewport().setBackground(Color.white);
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
		this.jButtonSpace.addMouseListener(this.mouseAdapter);
		this.jButtonC.addMouseListener(this.mouseAdapter);
		this.jButtonAnd.addMouseListener(this.mouseAdapter);
		this.jButtonNot.addMouseListener(this.mouseAdapter);
		this.jButtonLike.addMouseListener(this.mouseAdapter);
		this.jButtonOr.addMouseListener(this.mouseAdapter);
		this.jComboBoxMathsOperation.addActionListener(this.actionListener);
		this.jComboBoxStringFunction.addActionListener(this.actionListener);
		this.jComboBoxTimeFunction.addActionListener(this.actionListener);
		this.jButtonOK.addActionListener(this.buttonActionListener);
		this.jButtonCancel.addActionListener(this.buttonActionListener);
		this.jButtonClear.addActionListener(this.buttonActionListener);
		this.jTableFieldInfo.addMouseListener(this.tableMouseAdapter);

		//获取唯一值相关控件监听事件-yuanR
		// 添加jtable选择改变事件
		this.jTableFieldInfo.getSelectionModel().addListSelectionListener(this.listSelectionListener);
		this.jButtonGetAllValue.addActionListener(this.buttonActionListener);
//		this.textFieldGOTO.addKeyListener(this.keyAdapter);
		this.listAllValue.addMouseListener(this.listAllValueMouseAdapter);
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
		this.jButtonSpace.removeMouseListener(this.mouseAdapter);
		this.jButtonC.removeMouseListener(this.mouseAdapter);
		this.jButtonAnd.removeMouseListener(this.mouseAdapter);
		this.jButtonNot.removeMouseListener(this.mouseAdapter);
		this.jButtonLike.removeMouseListener(this.mouseAdapter);
		this.jButtonOr.removeMouseListener(this.mouseAdapter);
		this.jComboBoxMathsOperation.removeActionListener(this.actionListener);
		this.jComboBoxStringFunction.removeActionListener(this.actionListener);
		this.jComboBoxTimeFunction.removeActionListener(this.actionListener);
		this.jButtonOK.removeActionListener(this.buttonActionListener);
		this.jButtonCancel.removeActionListener(this.buttonActionListener);
		this.jButtonClear.removeActionListener(this.buttonActionListener);
		this.jTableFieldInfo.removeMouseListener(this.tableMouseAdapter);
		//获取唯一值相关控件监听事件-yuanR
		this.jTableFieldInfo.getSelectionModel().removeListSelectionListener(this.listSelectionListener);
		this.jButtonGetAllValue.removeActionListener(this.buttonActionListener);
//		this.labelGoTO.removeKeyListener(this.keyAdapter);
		this.listAllValue.removeMouseListener(this.listAllValueMouseAdapter);
	}

	/**
	 * 初始化常用运算符的ComboBox
	 */
	private void initResources() {
		this.jButtonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.jButtonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.jButtonClear.setText(ControlsProperties.getString("String_GeometryPropertyStyle3DControl_buttonClearMarkerIconFile"));
		this.jComboBoxMathsOperation
				.setModel(new DefaultComboBoxModel<String>(new String[]{"", "Abs()", "Acos()", "Asin()", "Atan()", "Atn2()", "Ceiling()", "Cos()", "Cot()",
						"Degrees()", "Exp()", "Floor()", "Log()", "Log10()", "PI()", "Power()", "Radians()", "Rand()", "Round()", "Sign()", "Sin()",
						"Square()", "Sqrt()", "Tan()", "CBool()", "CByte()", "CCur()", "CDate()", "CDbl()", "CInt()", "CLng()", "CSng()", "CStr()", "Int()",
						"Fix()"}));
		this.jComboBoxStringFunction.setModel(new DefaultComboBoxModel<String>(new String[]{"", "Ascii()", "Char()", "Charindex()", "Difference()", "Left()",
				"Len()", "Lower()", "Ltrim()", "Nchar()", "Patindex()", "Replace()", "Replicate()", "Quotename()", "Reverse()", "Right()", "Rtrim()",
				"Soundex()", "Space()", "Str()", "Stuff()", "Substring()", "Unicode()", "Upper()"}));
		this.jComboBoxTimeFunction.setModel(new DefaultComboBoxModel<String>(new String[]{"", "DateAdd()", "Datediff()", "Datename()", "Datepart()", "Day()",
				"Getdate()", "Getutcdate()", "Month()", "Year()"}));
		this.jLabelMathsoperation.setText(ControlsProperties.getString("String_ArithmeticOperation"));
		this.jLabelStringfunction.setText(ControlsProperties.getString("String_CharacterHandling"));
		this.jLabelTimefunction.setText(ControlsProperties.getString("String_DataAndTime"));

		//获取唯一值面板控件的资源化-yuanR
		this.jButtonGetAllValue.setText(ControlsProperties.getString("String_getallvalue"));
//		this.labelGoTO.setText(ControlsProperties.getString("String_Location"));
	}

	/**
	 * 初始化常用函数面板
	 */
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

		GroupLayout panelBufferDataLayout = new GroupLayout(this.jPanelFunction);
		panelBufferDataLayout.setAutoCreateContainerGaps(true);
		panelBufferDataLayout.setAutoCreateGaps(true);
		this.jPanelFunction.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.jLabelMathsoperation)
						.addComponent(this.jLabelStringfunction)
						.addComponent(this.jLabelTimefunction))
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.jComboBoxMathsOperation)
						.addComponent(this.jComboBoxStringFunction)
						.addComponent(this.jComboBoxTimeFunction)));
		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.jLabelMathsoperation)
						.addComponent(this.jComboBoxMathsOperation,20,20,20))
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.jLabelStringfunction)
						.addComponent(this.jComboBoxStringFunction,20,20,20))
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.jLabelTimefunction)
						.addComponent(this.jComboBoxTimeFunction,20,20,20)));
		//@formatter:on
	}

	/**
	 * 初始化获取唯一值面板
	 * yuanR
	 */
	private void initPanelGetAllValue() {
		this.jPanelGetAllValue = new JPanel();
		this.jPanelGetAllValue.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), "",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		this.jButtonGetAllValue = new JButton("getAllValue");
		this.jButtonGetAllValue.setEnabled(false);
		this.scrollPaneAllValue = new JScrollPane();
		initListAllValue();
		this.scrollPaneAllValue.setViewportView(listAllValue);
//		this.labelGoTO = new JLabel("goTo");
//		this.textFieldGOTO = new JTextField();
//		this.textFieldGOTO.setEnabled(false);

		this.jPanelGetAllValue.setLayout(new GridBagLayout());
		this.jPanelGetAllValue.add(jButtonGetAllValue, new GridBagConstraintsHelper(0, 0, 2, 1).setFill(GridBagConstraints.BOTH).setInsets(2, 2, 2, 2));
		this.jPanelGetAllValue.add(scrollPaneAllValue, new GridBagConstraintsHelper(0, 1, 2, 3).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(2, 2, 2, 2));
		//去除定位功能-yaunR 1.17
		//this.jPanelGetAllValue.add(labelGoTO, new GridBagConstraintsHelper(3, 5, 1, 1).setFill(GridBagConstraints.NONE).setInsets(2, 2, 2, 2));
		//this.jPanelGetAllValue.add(textFieldGOTO, new GridBagConstraintsHelper(4, 5, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(2, 2, 2, 2));
	}

	/**
	 * 初始化唯一值列表-yuanR
	 */
	private void initListAllValue() {
		listAllValue = new GetAllValueList();
	}

	/**
	 * 初始化常用运算符面板
	 */
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
		this.jButtonSpace = new JButton("Space");
		this.jButtonC = new JButton("C");
		this.jButtonOr = new JButton("Or");
		setOperatorButtonSize();
		//@formatter:off
        this.jPanelCommonOperator.setLayout(new GridBagLayout());
        this.jPanelCommonOperator.add(this.jButtonPlus, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonSubtract, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonMultiply, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonDivide, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonAnd, new GridBagConstraintsHelper(4, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

        this.jPanelCommonOperator.add(this.jButtonMore, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonLess, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonEqual, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonBracket, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonNot, new GridBagConstraintsHelper(4, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

        this.jPanelCommonOperator.add(this.jButtonMoreOrEqual, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonLessOrEqual, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonLeftBracket, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonRightBracket, new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonLike, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

        this.jPanelCommonOperator.add( this.jButtonAndCompute , new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonSpace, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonC, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
        this.jPanelCommonOperator.add(this.jButtonOr, new GridBagConstraintsHelper(4, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
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
		this.jButtonSpace.setPreferredSize(dimension);
		this.jButtonC.setPreferredSize(dimension);
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
		this.componentList.add(this.jButtonClear);
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
						fieldNameColumn = datasetVector.getName() + "." + datasetVector.getFieldInfos().get(fieldCount).getName();
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
//		resetTableCell();
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
						fieldNameColumn = datasetVector.getName() + "." + datasetVector.getFieldInfos().get(fieldCount).getName();
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
//		resetTableCell();
	}

//	private void resetTableCell() {
////		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setPreferredWidth(
////				80);
////		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName"))
////				.setPreferredWidth(100);
////		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setPreferredWidth(80);
//	}

	/**
	 * 构造JTable类型的FieldInfo，其中实现了方法：双击将table中的信息并将该信息加到TextArea中
	 */
	private JTable getTableFieldInfo() {
		if (this.jTableFieldInfo == null) {
			this.defaultTableModel = new DefaultTableModel(NAMES, 0);
			this.jTableFieldInfo = new JTable(defaultTableModel);
			//为什么要关闭列宽自动适应？？---yuanR 17.1.11
			//去除。实现jatble跟随窗口大小的改变自动适应
			//	this.jTableFieldInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			//仅支持单选--yuanR 17.1.12
			this.jTableFieldInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//行高
			this.jTableFieldInfo.setRowHeight(25);
			//取消网格
			this.jTableFieldInfo.setShowHorizontalLines(false);
			this.jTableFieldInfo.setShowVerticalLines(false);
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
		//当按钮为“Space”||为“C”时-yuanR
		if (opertorString.equals("Space")) {
			opertorString = " ";
			setSQLSentenceText(this.jTextAreaSQLSentence, opertorString, "");
		} else if (opertorString.equals("C")) {
			this.jTextAreaSQLSentence.setText("");
		} else {
			opertorString = " " + opertorString;
			setSQLSentenceText(this.jTextAreaSQLSentence, opertorString, "");
		}
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
//			functionString = " " + functionString;
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
		unRegistActionListener();
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
					String text = " " + jTableFieldInfo.getValueAt(row, 1).toString();
//					if (jTextAreaSQLSentence.getSelectionStart() != 0) {
//						text = " " + text;
//					}

					setSQLSentenceText(jTextAreaSQLSentence, text, "");
				}
			}
		}
	}

	class LocalButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jButtonClear) {
				//清空表达式面板-yuanR
				jTextAreaSQLSentence.setText("");
			}
			if (e.getSource() == jButtonOK) {
				buttonOkClicked();
			}
			if (e.getSource() == jButtonCancel) {
				buttonCancelClicked();
			}
			//但点击了获取唯一值按钮-yuanR
			if (e.getSource() == jButtonGetAllValue) {
				//获取字段信息
				//YR存疑：为什么filedDatasets是数据集数组？？因为当设置了关联属性表，其数据集数量会增加（1.12）
				Object[] allValue = getListValue(filedDatasets);
				listAllValue.removeAllElements();
				if (allValue != null && allValue.length > 0) {
					listAllValue.resetValue(allValue);
//					textFieldGOTO.setEnabled(true);
					jButtonGetAllValue.setEnabled(false);
				}
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

	//获取唯一值，list-yuanR
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

		public void resetValue(Object[] allValue) {
			this.removeAllElements();
			if (allValue != null && allValue.length > 0) {
				for (Object anAllValue : allValue) {
					((DefaultListModel<Object>) this.getModel()).addElement(anAllValue);
				}
				this.setSelectedIndex(0);
			}
		}
	}

	/**
	 * 定位文本框改变监听
	 * yuanR
	 */
//	private KeyAdapter keyAdapter = new KeyAdapter() {
//		@Override
//		public void keyReleased(KeyEvent e) {
////			listAllValue.setSelectedIndex(listAllValue.getValueIndex(textFieldGOTO.getText()));
////			listAllValue.ensureIndexIsVisible(listAllValue.getSelectedIndex());
//		}
//	};

	/**
	 * 获取唯一值列表点击的值，并加入sql表达式中
	 * yuanR
	 */
	private MouseAdapter listAllValueMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && listAllValue.getSelectedIndex() != -1) {
				//通过setSQLSentenceText（）方法将双击选中的唯一值加入"jTextAreaSQLSentence"--yuanR 1.12
				setSQLSentenceText(jTextAreaSQLSentence, " " + "'" + listAllValue.getSelectedValue().toString() + "'", "");
			}
		}
	};

	/**
	 * 字段信息表改变事件 负责修改获取唯一值按钮状态
	 * yuanR
	 */
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			checkButtonGetAllValueState();
		}
	};

	/**
	 * 设置获取唯一值按钮，列表的属性，
	 * yuanR
	 */
	private void checkButtonGetAllValueState() {
		int row = this.jTableFieldInfo.getSelectedRow();
		this.jButtonGetAllValue.setEnabled(row != -1);
		clearScrollpaneallvalue();
	}

	/**
	 * 清空唯一值列表
	 * yuanR
	 */
	private void clearScrollpaneallvalue() {
		listAllValue.removeAllElements();
//		textFieldGOTO.setEnabled(false);
	}

	/**
	 * 获得list里需要填入的值
	 * yuanR
	 */
	private Object[] getListValue(Dataset[] datasets) {
		//首先筛选出选中的数据集
		//数据集名称
		String fieldName = jTableFieldInfo.getValueAt(jTableFieldInfo.getSelectedRow(), 1).toString();
		//数据集序号，默认为0
		int datasetVectorCount = 0;
		//当数据集字段名称中含有“.”，说明设置了关联属性表，其数据集数量大于1
		if (fieldName.indexOf(".") != -1) {
			String datasetName = jTableFieldInfo.getValueAt(jTableFieldInfo.getSelectedRow(), 1).toString();
			datasetName = datasetName.substring(0, datasetName.indexOf("."));
			//字段名称
			fieldName = fieldName.substring(fieldName.indexOf(".") + 1);
			DatasetVector datasetVector = null;
			for (int i = 0; i < datasets.length; i++) {
				datasetVector = (DatasetVector) datasets[i];
				if (datasetVector.getName().equals(datasetName)) {
					datasetVectorCount = i;
					break;
				}
			}
		}
		DatasetVector selectedDatasetVector = (DatasetVector) datasets[datasetVectorCount];
		// 得到字段类型
		FieldType fieldType = selectedDatasetVector.getFieldInfos().get(fieldName).getType();
		//  2017/2/10 lixiaoyao 针对原来的唯一值获取查询进行优化，原本是遍历获取，现在改为空间查询
		String queryString = selectedDatasetVector.getName() + '.' + fieldName;
		QueryParameter queryParameter = new QueryParameter();
		queryParameter.setCursorType(CursorType.STATIC);
		queryParameter.setHasGeometry(false);
		queryParameter.setOrderBy(new String[]{queryString + " asc",});
		queryParameter.setGroupBy(new String[]{queryString});
		queryParameter.setResultFields(new String[]{queryString});
		Recordset recordset = selectedDatasetVector.query(queryParameter);
		//Recordset recordset = selectedDatasetVector.getRecordset(false, CursorType.STATIC);
		LinkedHashMap<Object, String> map = new LinkedHashMap<>();
		try {
			recordset.moveFirst();
			for (; !recordset.isEOF(); recordset.moveNext()) {
				//获得选中字段的当前记录
				Object result = formatData(recordset.getFieldValue(fieldName), fieldType);
				if (result != null) {
					map.put(result, "");
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			recordset.dispose();
		}
		Object[] result = new Object[map.size()];
		Iterator<Object> iterator = map.keySet().iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			result[i] = iterator.next();
		}
		//对数组进行排序
		//SortUIUtilities.sortList(result);
		return result;
	}

	/**
	 * 对索引到的数据进行显示控制-yuanR 2017.2.16
	 *
	 * @param fieldValue
	 * @param fieldType
	 * @return
	 */
	private Object formatData(Object fieldValue, FieldType fieldType) {
		if (fieldValue == null) {
			return null;
		}
		if (fieldType == FieldType.DATETIME) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy" + "/" + "MM" + "/" + "dd hh:mm:ss");
			return dateFormat.format(fieldValue);
		} else if (fieldType == FieldType.BOOLEAN) {
			if (fieldValue.equals(true)) {
				return "True";
			} else if (fieldValue.equals(false)) {
				return "False";
			} else {
				return null;
			}
		} else if (fieldType == FieldType.LONGBINARY) {
			return "BinaryData";
		} else {
			return fieldValue;
		}
	}
}
