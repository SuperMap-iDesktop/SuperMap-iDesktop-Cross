package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.data.QueryParameter;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

public class SQLExpressionDialog extends SmDialog {
	private JButton jButtonCancel;
	private JButton jButtonOK;
	private JTable jTableFieldInfo;
	private JScrollPane jScrollPanel;
	private JComboBox jComboBoxTimeFunction;
	private JComboBox jComboBoxStringFunction;
	private JComboBox jComboBoxMathsOperation;
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

	private static Map<FieldType, String> map;

	// 初始化操作
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 默认构造函数
	 */
	public SQLExpressionDialog() {
		super();
		setModal(true);
	}

	private void initialDialog(String expression) {
		setSize(650, 350);
		setResizable(false);
		intializeForm();

		getContentPane().setLayout(null);
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

		getContentPane().add(getTextAreaSQLSentence());
		getContentPane().add(getPanelFunction());
		getContentPane().add(getPanel());
		getContentPane().add(getButtonOK());
		getContentPane().add(getButtonCancel());

		getContentPane().add(getPanelCommonOperator());
		this.jTextAreaSQLSentence.setText(expression);
		initialFieldTypeMap();
	}

	/**
	 * // 显示对话框，过滤字段类型
	 * 
	 * @param datasets
	 * @param fieldTypes
	 */
	public DialogResult showDialog(Dataset[] datasets, List<FieldType> fieldTypes, String sqlExpression) {
		initialDialog(sqlExpression);

		filedDatasets = datasets;

		if (fieldTypes == null) {
			thisFieldTypes.add(FieldType.BOOLEAN);
			thisFieldTypes.add(FieldType.BYTE);
			thisFieldTypes.add(FieldType.INT16);
			thisFieldTypes.add(FieldType.INT32);
			thisFieldTypes.add(FieldType.SINGLE);
			thisFieldTypes.add(FieldType.DOUBLE);
			thisFieldTypes.add(FieldType.TEXT);
			thisFieldTypes.add(FieldType.LONGBINARY);
			thisFieldTypes.add(FieldType.CHAR);
			thisFieldTypes.add(FieldType.DATETIME);
			thisFieldTypes.add(FieldType.WTEXT);
		} else {
			thisFieldTypes = fieldTypes;
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

		filedDatasets = datasets;

		setTableFieldInfo(filedDatasets);
		this.setVisible(true);
		return dialogResult;

	}

	private void intializeForm() {
		try {
			this.requestFocusInWindow();
			sqlExpressionForm_GotFocus();

			jComboBoxMathsOperation.setSelectedIndex(0);
			jComboBoxStringFunction.setSelectedIndex(0);
			jComboBoxTimeFunction.setSelectedIndex(0);

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

		tableData = new Object[allFieldCount][3];

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
					if (0 == i) {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getFieldInfos().get(fieldCount).getName();
					} else {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getTableName() + "." + datasetVector.getFieldInfos().get(fieldCount).getName();
					}
					tableData[rowCount][0] = fieldCaptionColumn;
					tableData[rowCount][1] = fieldNameColumn;
					tableData[rowCount][2] = fieldTypeColumn;
				}
			}
		}

		defaultTableModel = new cellEditableModel(tableData, NAMES);
		jTableFieldInfo.setModel(defaultTableModel);
		jTableFieldInfo.repaint();
		jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setPreferredWidth(80);
		jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName")).setPreferredWidth(100);
		jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setPreferredWidth(80);
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

		tableData = new Object[allFieldCount][3];

		int rowCount = 0;
		String fieldNameColumn = null;
		String fieldTypeColumn = null;
		String fieldCaptionColumn = null;
		for (int ndataset = 0; ndataset < datasets.length; ndataset++) {
			datasetVector = (DatasetVector) datasets[ndataset];
			for (int fieldCount = 0; fieldCount < datasetVector.getFieldCount(); fieldCount++) {

				fieldType = datasetVector.getFieldInfos().get(fieldCount).getType();
				if (fieldTypes.contains(fieldType)) {
					if (0 == ndataset) {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getFieldInfos().get(fieldCount).getName();
					} else {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getTableName() + "." + datasetVector.getFieldInfos().get(fieldCount).getName();
					}
					tableData[rowCount][0] = fieldCaptionColumn;
					tableData[rowCount][1] = fieldNameColumn;
					tableData[rowCount][2] = fieldTypeColumn;
					rowCount++;
				}
			}
		}

		defaultTableModel = new cellEditableModel(tableData, NAMES);
		jTableFieldInfo.setModel(defaultTableModel);
		jTableFieldInfo.repaint();
		jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setPreferredWidth(80);
		jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName")).setPreferredWidth(100);
		jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setPreferredWidth(80);
	}

	// 初始化操作
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	// 事件处理
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * // 获得焦点时，全选表达框中的文本
	 */
	private void sqlExpressionForm_GotFocus() {
		jTextAreaSQLSentence.setSelectionStart(0);
		jTextAreaSQLSentence.selectAll();
	}

	// 事件处理
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	// 构造控件相关信息///////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 构造panel，FieldInfo
	 */
	private JScrollPane getPanel() {
		if (jScrollPanel == null) {
			jScrollPanel = new JScrollPane();
			jScrollPanel.setBounds(396, 144, 238, 140);
			jScrollPanel.setViewportView(add(getTableFieldInfo()));

			jScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jScrollPanel;
	}

	/**
	 * 构造JTable类型的FieldInfo，其中实现了方法：双击将table中的信息并将该信息加到TextArea中
	 */
	private JTable getTableFieldInfo() {
		if (jTableFieldInfo == null) {
			defaultTableModel = new DefaultTableModel(NAMES, 0);
			jTableFieldInfo = new JTable(defaultTableModel);
			jTableFieldInfo.setSize(800, 500);
			jTableFieldInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTableFieldInfo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// //双击ListViewFieldInfo的字段信息，将字段名插入到表达式文本框的SelectionStart位置
					if (e.getClickCount() == 2) {
						Point point = new Point(e.getX(), e.getY());
						int row = jTableFieldInfo.getSelectedRow();
						int column = jTableFieldInfo.getSelectedColumn();

						if (column == 1 && jTableFieldInfo.getCellRect(row, column, false).contains(point)) {
							String text = jTableFieldInfo.getValueAt(row, column).toString();
							if (jTextAreaSQLSentence.getSelectionStart() != 0) {
								text = " " + text;
							}

							setSQLSentenceText(jTextAreaSQLSentence, text, "");
						}
					}
				}
			});
		}
		return jTableFieldInfo;
	}

	/**
	 * “+”
	 */
	private JButton getButtonPlus() {
		if (jButtonPlus == null) {
			jButtonPlus = new JButton();
			jButtonPlus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonPlus);
				}
			});

			jButtonPlus.setBounds(10, 21, 27, 24);
			jButtonPlus.setFont(new Font(ControlsProperties.getString("String_FontSong"), Font.BOLD, 12));
			jButtonPlus.setName("");
			jButtonPlus.setMargin(new Insets(0, 0, 0, 0));
			jButtonPlus.setText("+");
		}
		return jButtonPlus;
	}

	/**
	 * “-”
	 */
	private JButton getButtonSubtract() {
		if (jButtonSubtract == null) {
			jButtonSubtract = new JButton();
			jButtonSubtract.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonSubtract);
				}
			});
			jButtonSubtract.setBounds(44, 21, 27, 24);
			jButtonSubtract.setFont(new Font("Cambria Math", Font.BOLD, 12));
			jButtonSubtract.setMargin(new Insets(0, 0, 0, 0));
			jButtonSubtract.setText("-");
		}
		return jButtonSubtract;
	}

	/**
	 * “*”
	 */
	private JButton getButtonMultiply() {
		if (jButtonMultiply == null) {
			jButtonMultiply = new JButton();
			jButtonMultiply.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonMultiply);
				}
			});
			jButtonMultiply.setBounds(78, 21, 27, 24);
			jButtonMultiply.setFont(new Font("", Font.BOLD, 16));
			jButtonMultiply.setMargin(new Insets(0, 0, 0, 0));
			jButtonMultiply.setText("*");
		}
		return jButtonMultiply;
	}

	/**
	 * “/”
	 */
	private JButton getButtonDivide() {
		if (jButtonDivide == null) {
			jButtonDivide = new JButton();
			jButtonDivide.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					buttonOperator_Click(jButtonDivide);
				}
			});
			jButtonDivide.setBounds(112, 21, 27, 24);
			jButtonDivide.setMargin(new Insets(0, 0, 0, 0));
			jButtonDivide.setText("/");
		}
		return jButtonDivide;
	}

	/**
	 * ">"
	 */
	private JButton getButtonMore() {
		if (jButtonMore == null) {
			jButtonMore = new JButton();
			jButtonMore.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonMore);
				}
			});
			jButtonMore.setBounds(10, 51, 27, 24);
			jButtonMore.setMargin(new Insets(0, 0, 0, 0));
			jButtonMore.setText(">");
		}
		return jButtonMore;
	}

/**
	 * "<"
	 */
	private JButton getButtonLess() {
		if (jButtonLess == null) {
			jButtonLess = new JButton();
			jButtonLess.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLess);
				}
			});
			jButtonLess.setBounds(44, 51, 27, 24);
			jButtonLess.setMargin(new Insets(0, 0, 0, 0));
			jButtonLess.setText("<");
		}
		return jButtonLess;
	}

	/**
	 * "="
	 */
	private JButton getButtonEqual() {
		if (jButtonEqual == null) {
			jButtonEqual = new JButton();
			jButtonEqual.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonEqual);
				}
			});
			jButtonEqual.setBounds(78, 51, 27, 24);
			jButtonEqual.setMargin(new Insets(0, 0, 0, 0));
			jButtonEqual.setText("=");
		}
		return jButtonEqual;
	}

	/**
	 * "<>"
	 */
	private JButton getButtonBracket() {
		if (jButtonBracket == null) {
			jButtonBracket = new JButton();
			jButtonBracket.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonBracket);
				}
			});
			jButtonBracket.setBounds(112, 51, 27, 24);
			jButtonBracket.setMargin(new Insets(0, 0, 0, 0));
			jButtonBracket.setText("<>");
		}
		return jButtonBracket;
	}

	/**
	 * ">="
	 */
	private JButton getButtonMoreOrEqual() {
		if (jButtonMoreOrEqual == null) {
			jButtonMoreOrEqual = new JButton();
			jButtonMoreOrEqual.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonMoreOrEqual);
				}
			});
			jButtonMoreOrEqual.setBounds(10, 81, 27, 24);
			jButtonMoreOrEqual.setMargin(new Insets(0, 0, 0, 0));
			jButtonMoreOrEqual.setText(">=");
		}
		return jButtonMoreOrEqual;
	}

	/**
	 * "<="
	 */
	private JButton getButtonLessOrEqual() {
		if (jButtonLessOrEqual == null) {
			jButtonLessOrEqual = new JButton();
			jButtonLessOrEqual.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLessOrEqual);
				}
			});
			jButtonLessOrEqual.setBounds(44, 81, 27, 24);
			jButtonLessOrEqual.setMargin(new Insets(0, 0, 0, 0));
			jButtonLessOrEqual.setText("<=");
		}
		return jButtonLessOrEqual;
	}

	/**
	 * "("
	 */
	private JButton getButtonLeftBracket() {
		if (jButtonLeftBracket == null) {
			jButtonLeftBracket = new JButton();
			jButtonLeftBracket.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLeftBracket);
				}
			});
			jButtonLeftBracket.setBounds(78, 81, 27, 24);
			jButtonLeftBracket.setMargin(new Insets(0, 0, 0, 0));
			jButtonLeftBracket.setText("(");
		}
		return jButtonLeftBracket;
	}

	/**
	 * ")"
	 */
	private JButton getButtonRightBracket() {
		if (jButtonRightBracket == null) {
			jButtonRightBracket = new JButton();
			jButtonRightBracket.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonRightBracket);
				}
			});
			jButtonRightBracket.setBounds(112, 81, 27, 24);
			jButtonRightBracket.setMargin(new Insets(0, 0, 0, 0));
			jButtonRightBracket.setText(")");
		}
		return jButtonRightBracket;
	}

	/**
	 * "&"
	 */
	private JButton getButtonAndCompute() {
		if (jButtonAndCompute == null) {
			jButtonAndCompute = new JButton();
			jButtonAndCompute.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonAndCompute);
				}
			});
			jButtonAndCompute.setBounds(9, 111, 27, 24);
			jButtonAndCompute.setMargin(new Insets(0, 0, 0, 0));
			jButtonAndCompute.setText("&");
		}
		return jButtonAndCompute;
	}

	/**
	 * "And"
	 */
	private JButton getButtonAnd() {
		if (jButtonAnd == null) {
			jButtonAnd = new JButton();
			jButtonAnd.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonAnd);
				}
			});
			jButtonAnd.setBounds(146, 21, 40, 24);
			jButtonAnd.setMargin(new Insets(0, 0, 0, 0));
			jButtonAnd.setText("And");
		}
		return jButtonAnd;
	}

	/**
	 * "Not"
	 */
	private JButton getButtonNot() {
		if (jButtonNot == null) {
			jButtonNot = new JButton();
			jButtonNot.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonNot);
				}
			});
			jButtonNot.setBounds(146, 51, 40, 24);
			jButtonNot.setMargin(new Insets(0, 0, 0, 0));
			jButtonNot.setText("Not");
		}
		return jButtonNot;
	}

	/**
	 * "Like"
	 */
	private JButton getButtonLike() {
		if (jButtonLike == null) {
			jButtonLike = new JButton();
			jButtonLike.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					// do nothing
				}
			});
			jButtonLike.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLike);
				}
			});
			jButtonLike.setBounds(146, 81, 40, 24);
			jButtonLike.setMargin(new Insets(0, 0, 0, 0));
			jButtonLike.setText("Like");
		}
		return jButtonLike;
	}

	/**
	 * "Or"
	 */
	private JButton getButtonOr() {
		if (jButtonOr == null) {
			jButtonOr = new JButton();
			jButtonOr.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonOr);
				}
			});
			jButtonOr.setBounds(146, 111, 40, 24);
			jButtonOr.setMargin(new Insets(0, 0, 0, 0));
			jButtonOr.setText("Or");
		}
		return jButtonOr;
	}

	/**
	 * // 点击Button之后，将Button上的字符内容输入到textArea中
	 * 
	 * @param botton
	 */
	private void buttonOperator_Click(JButton botton) {
		jTextAreaSQLSentence.requestFocusInWindow();

		String opertorString = botton.getText();
		opertorString = " " + opertorString;
		setSQLSentenceText(jTextAreaSQLSentence, opertorString, "");
	}

	/**
	 * // 选择各项函数填充表达式文本框
	 * 
	 * @param comboBox
	 */
	private void comboBoxFunction_SelectedIndexChanged(JComboBox<?> comboBox) {
		jTextAreaSQLSentence.requestFocusInWindow();

		if (comboBox.getSelectedIndex() != 0) {
			String functionString = comboBox.getSelectedItem().toString();
			functionString = " " + functionString;
			setSQLSentenceText(jTextAreaSQLSentence, functionString, "fuction");
		}
	}

	/**
	 * 将各种Button加到panel上
	 */
	private JPanel getPanelCommonOperator() {
		if (jPanelCommonOperator == null) {
			jPanelCommonOperator = new JPanel();
			jPanelCommonOperator.setBounds(10, 135, 194, 149);
			jPanelCommonOperator.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), ControlsProperties.getString("String_CommonOperator"),
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanelCommonOperator.setLayout(null);
			jPanelCommonOperator.add(getButtonPlus());
			jPanelCommonOperator.add(getButtonSubtract());
			jPanelCommonOperator.add(getButtonMultiply());
			jPanelCommonOperator.add(getButtonDivide());
			jPanelCommonOperator.add(getButtonAnd());
			jPanelCommonOperator.add(getButtonMore());
			jPanelCommonOperator.add(getButtonLess());
			jPanelCommonOperator.add(getButtonEqual());
			jPanelCommonOperator.add(getButtonBracket());
			jPanelCommonOperator.add(getButtonNot());
			jPanelCommonOperator.add(getButtonMoreOrEqual());
			jPanelCommonOperator.add(getButtonLessOrEqual());
			jPanelCommonOperator.add(getButtonLeftBracket());
			jPanelCommonOperator.add(getButtonRightBracket());
			jPanelCommonOperator.add(getButtonLike());
			jPanelCommonOperator.add(getButtonAndCompute());
			jPanelCommonOperator.add(getButtonOr());
		}
		return jPanelCommonOperator;
	}

	/**
	 * 构造文本框
	 */
	private JTextArea getTextAreaSQLSentence() {
		if (jTextAreaSQLSentence == null) {
			jTextAreaSQLSentence = new JTextArea();
			jTextAreaSQLSentence.setBounds(10, 10, 624, 119);
			jTextAreaSQLSentence.setLineWrap(true);

		}
		return jTextAreaSQLSentence;
	}

	/**
	 * 构造放Function的panel
	 */
	private JPanel getPanelFunction() {
		if (jPanelFunction == null) {
			jPanelFunction = new JPanel();
			jPanelFunction.setLayout(null);
			jPanelFunction.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), ControlsProperties.getString("String_CommonFunction"),
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanelFunction.setBounds(210, 135, 180, 149);
			jPanelFunction.add(getLabelMathsoperationLabel());
			jPanelFunction.add(getLabelStringfunctionLabel());
			jPanelFunction.add(getTimefunctionLabel());
			jPanelFunction.add(getComboBoxMathsOperation());
			jPanelFunction.add(getComboBoxStringFunction());
			jPanelFunction.add(getComboBoxTimeFunction());
		}
		return jPanelFunction;
	}

	/**
	 * 构造MathsoperationLabel名称显示的Label
	 */
	private JLabel getLabelMathsoperationLabel() {
		if (jLabelMathsoperation == null) {
			jLabelMathsoperation = new JLabel();
			jLabelMathsoperation.setBounds(10, 22, 94, 18);
			jLabelMathsoperation.setText(ControlsProperties.getString("String_ArithmeticOperation"));
		}
		return jLabelMathsoperation;
	}

	/**
	 * 构造Stringfunction名称显示的label
	 */
	private JLabel getLabelStringfunctionLabel() {
		if (jLabelStringfunction == null) {
			jLabelStringfunction = new JLabel();
			jLabelStringfunction.setText(ControlsProperties.getString("String_CharacterHandling"));
			jLabelStringfunction.setBounds(10, 66, 94, 29);
		}
		return jLabelStringfunction;
	}

	/**
	 * 构造Timefunction名称显示的label
	 */
	private JLabel getTimefunctionLabel() {
		if (jLabelTimefunction == null) {
			jLabelTimefunction = new JLabel();
			jLabelTimefunction.setText(ControlsProperties.getString("String_DataAndTime"));
			jLabelTimefunction.setBounds(10, 118, 94, 18);
		}
		return jLabelTimefunction;
	}

	/**
	 * 构造可选择MathsOperation的Combox
	 */
	private JComboBox getComboBoxMathsOperation() {
		if (jComboBoxMathsOperation == null) {
			jComboBoxMathsOperation = new JComboBox();
			jComboBoxMathsOperation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					comboBoxFunction_SelectedIndexChanged(jComboBoxMathsOperation);
				}
			});

			jComboBoxMathsOperation.setModel(new DefaultComboBoxModel(new String[] { "", "Abs()", "Acos()", "Asin()", "Atan()", "Atn2()", "Ceiling()", "Cos()",
					"Cot()", "Degrees()", "Exp()", "Floor()", "Log()", "Log10()", "PI()", "Power()", "Radians()", "Rand()", "Round()", "Sign()", "Sin()",
					"Square()", "Sqrt()", "Tan()", "CBool()", "CByte()", "CCur()", "CDate()", "CDbl()", "CInt()", "CLng()", "CSng()", "CStr()", "Int()",
					"Fix()" }));
			jComboBoxMathsOperation.setBounds(71, 20, 99, 22);
		}
		return jComboBoxMathsOperation;
	}

	/**
	 * 构造可选择StringFunction的Combox
	 */
	private JComboBox getComboBoxStringFunction() {
		if (jComboBoxStringFunction == null) {
			jComboBoxStringFunction = new JComboBox();
			jComboBoxStringFunction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					comboBoxFunction_SelectedIndexChanged(jComboBoxStringFunction);
				}
			});

			jComboBoxStringFunction.setModel(new DefaultComboBoxModel(new String[] { "", "Ascii()", "Char()", "Charindex()", "Difference()", "Left()", "Len()",
					"Lower()", "Ltrim()", "Nchar()", "Patindex()", "Replace()", "Replicate()", "Quotename()", "Reverse()", "Right()", "Rtrim()", "Soundex()",
					"Space()", "Str()", "Stuff()", "Substring()", "Unicode()", "Upper()" }));
			jComboBoxStringFunction.setBounds(71, 69, 99, 22);
		}
		return jComboBoxStringFunction;
	}

	/**
	 * 构造可选择TimeFunction的Combox
	 */
	private JComboBox getComboBoxTimeFunction() {
		if (jComboBoxTimeFunction == null) {
			jComboBoxTimeFunction = new JComboBox();
			jComboBoxTimeFunction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					comboBoxFunction_SelectedIndexChanged(jComboBoxTimeFunction);
				}
			});

			jComboBoxTimeFunction.setModel(new DefaultComboBoxModel(new String[] { "", "DateAdd()", "Datediff()", "Datename()", "Datepart()", "Day()",
					"Getdate()", "Getutcdate()", "Month()", "Year()" }));
			jComboBoxTimeFunction.setBounds(71, 116, 99, 22);
		}
		return jComboBoxTimeFunction;
	}

	/**
	 * OK
	 */
	private JButton getButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					filedQueryParameter = new QueryParameter();
					filedQueryParameter.setAttributeFilter(jTextAreaSQLSentence.getText());
					dialogResult = DialogResult.OK;
					dispose();
				}
			});
			jButtonOK.setText(CommonProperties.getString("String_Button_OK"));
			jButtonOK.setBounds(428, 293, 75, 21);
		}
		return jButtonOK;
	}

	public QueryParameter getQueryParameter() {
		return filedQueryParameter;

	}

	public void setQueryParameter(QueryParameter queryParameter) {
		filedQueryParameter = queryParameter;

	}

	/**
	 * Cancel
	 */
	private JButton getButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});

			jButtonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			jButtonCancel.setBounds(526, 293, 75, 21);
		}
		return jButtonCancel;
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
	 * @author caijunxia
	 * @//重载DefaultTableModel，因为涉及到的isCellEditable在默认的DefaultTableModel中为true，导致无法双击选中单元格
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

}
