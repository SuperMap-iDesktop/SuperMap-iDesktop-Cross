package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
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
		setSize(700, 350);
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

	private void intializeForm() {
		try {
			this.requestFocusInWindow();
			sqlExpressionForm_GotFocus();

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
					if (datasets.length==1) {
						fieldCaptionColumn = datasetVector.getFieldInfos().get(fieldCount).getCaption();
						fieldTypeColumn = getFiledTypeChineseName(fieldType);
						fieldNameColumn = datasetVector.getFieldInfos().get(fieldCount).getName();
					}else{
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
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setPreferredWidth(80);
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldCaption")).setCellRenderer(TableTooltipCellRenderer.getInstance());
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName")).setPreferredWidth(140);
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DataGridViewColumnFieldName")).setCellRenderer(TableTooltipCellRenderer.getInstance());
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setPreferredWidth(80);
		this.jTableFieldInfo.getColumn(ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")).setCellRenderer(TableTooltipCellRenderer.getInstance());
	}

	// 初始化操作
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	// 事件处理
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * // 获得焦点时，全选表达框中的文本
	 */
	private void sqlExpressionForm_GotFocus() {
		this.jTextAreaSQLSentence.setSelectionStart(0);
		this.jTextAreaSQLSentence.selectAll();
	}

	// 事件处理
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	// 构造控件相关信息///////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 构造panel，FieldInfo
	 */
	private JScrollPane getPanel() {
		if (this.jScrollPanel == null) {
			this.jScrollPanel = new JScrollPane();
			this.jScrollPanel.setBounds(406, 142, 280, 140);
			this.jScrollPanel.setViewportView(add(getTableFieldInfo()));

			this.jScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			this.jScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jScrollPanel;
	}

	/**
	 * 构造JTable类型的FieldInfo，其中实现了方法：双击将table中的信息并将该信息加到TextArea中
	 */
	private JTable getTableFieldInfo() {
		if (this.jTableFieldInfo == null) {
			this.defaultTableModel = new DefaultTableModel(NAMES, 0);
			this.jTableFieldInfo = new JTable(defaultTableModel);
			this.jTableFieldInfo.setSize(800, 500);
			this.jTableFieldInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.jTableFieldInfo.addMouseListener(new MouseAdapter() {
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
			});
		}
		return this.jTableFieldInfo;
	}

	/**
	 * “+”
	 */
	private JButton getButtonPlus() {
		if (this.jButtonPlus == null) {
			this.jButtonPlus = new JButton();
			this.jButtonPlus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonPlus);
				}
			});

			this.jButtonPlus.setBounds(10, 21, 27, 24);
			this.jButtonPlus.setFont(new Font(ControlsProperties.getString("String_FontSong"), Font.BOLD, 12));
			this.jButtonPlus.setName("");
			this.jButtonPlus.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonPlus.setText("+");
		}
		return jButtonPlus;
	}

	/**
	 * “-”
	 */
	private JButton getButtonSubtract() {
		if (this.jButtonSubtract == null) {
			this.jButtonSubtract = new JButton();
			this.jButtonSubtract.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonSubtract);
				}
			});
			this.jButtonSubtract.setBounds(44, 21, 27, 24);
			this.jButtonSubtract.setFont(new Font("Cambria Math", Font.BOLD, 12));
			this.jButtonSubtract.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonSubtract.setText("-");
		}
		return this.jButtonSubtract;
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
		if (this.jButtonBracket == null) {
			this.jButtonBracket = new JButton();
			this.jButtonBracket.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonBracket);
				}
			});
			this.jButtonBracket.setBounds(112, 51, 27, 24);
			this.jButtonBracket.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonBracket.setText("<>");
		}
		return this.jButtonBracket;
	}

	/**
	 * ">="
	 */
	private JButton getButtonMoreOrEqual() {
		if (this.jButtonMoreOrEqual == null) {
			this.jButtonMoreOrEqual = new JButton();
			this.jButtonMoreOrEqual.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonMoreOrEqual);
				}
			});
			this.jButtonMoreOrEqual.setBounds(10, 81, 27, 24);
			this.jButtonMoreOrEqual.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonMoreOrEqual.setText(">=");
		}
		return this.jButtonMoreOrEqual;
	}

	/**
	 * "<="
	 */
	private JButton getButtonLessOrEqual() {
		if (this.jButtonLessOrEqual == null) {
			this.jButtonLessOrEqual = new JButton();
			this.jButtonLessOrEqual.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLessOrEqual);
				}
			});
			this.jButtonLessOrEqual.setBounds(44, 81, 27, 24);
			this.jButtonLessOrEqual.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonLessOrEqual.setText("<=");
		}
		return this.jButtonLessOrEqual;
	}

	/**
	 * "("
	 */
	private JButton getButtonLeftBracket() {
		if (this.jButtonLeftBracket == null) {
			this.jButtonLeftBracket = new JButton();
			this.jButtonLeftBracket.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLeftBracket);
				}
			});
			this.jButtonLeftBracket.setBounds(78, 81, 27, 24);
			this.jButtonLeftBracket.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonLeftBracket.setText("(");
		}
		return this.jButtonLeftBracket;
	}

	/**
	 * ")"
	 */
	private JButton getButtonRightBracket() {
		if (this.jButtonRightBracket == null) {
			this.jButtonRightBracket = new JButton();
			this.jButtonRightBracket.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonRightBracket);
				}
			});
			this.jButtonRightBracket.setBounds(112, 81, 27, 24);
			this.jButtonRightBracket.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonRightBracket.setText(")");
		}
		return jButtonRightBracket;
	}

	/**
	 * "&"
	 */
	private JButton getButtonAndCompute() {
		if (this.jButtonAndCompute == null) {
			this.jButtonAndCompute = new JButton();
			this.jButtonAndCompute.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonAndCompute);
				}
			});
			this.jButtonAndCompute.setBounds(9, 111, 27, 24);
			this.jButtonAndCompute.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonAndCompute.setText("&");
		}
		return this.jButtonAndCompute;
	}

	/**
	 * "And"
	 */
	private JButton getButtonAnd() {
		if (this.jButtonAnd == null) {
			this.jButtonAnd = new JButton();
			this.jButtonAnd.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonAnd);
				}
			});
			this.jButtonAnd.setBounds(146, 21, 40, 24);
			this.jButtonAnd.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonAnd.setText("And");
		}
		return this.jButtonAnd;
	}

	/**
	 * "Not"
	 */
	private JButton getButtonNot() {
		if (this.jButtonNot == null) {
			this.jButtonNot = new JButton();
			this.jButtonNot.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonNot);
				}
			});
			this.jButtonNot.setBounds(146, 51, 40, 24);
			this.jButtonNot.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonNot.setText("Not");
		}
		return this.jButtonNot;
	}

	/**
	 * "Like"
	 */
	private JButton getButtonLike() {
		if (this.jButtonLike == null) {
			this.jButtonLike = new JButton();
			this.jButtonLike.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					// do nothing
				}
			});
			this.jButtonLike.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonLike);
				}
			});
			this.jButtonLike.setBounds(146, 81, 40, 24);
			this.jButtonLike.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonLike.setText("Like");
		}
		return this.jButtonLike;
	}

	/**
	 * "Or"
	 */
	private JButton getButtonOr() {
		if (this.jButtonOr == null) {
			this.jButtonOr = new JButton();
			this.jButtonOr.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					buttonOperator_Click(jButtonOr);
				}
			});
			this.jButtonOr.setBounds(146, 111, 40, 24);
			this.jButtonOr.setMargin(new Insets(0, 0, 0, 0));
			this.jButtonOr.setText("Or");
		}
		return this.jButtonOr;
	}

	/**
	 * // 点击Button之后，将Button上的字符内容输入到textArea中
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
	 * // 选择各项函数填充表达式文本框
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

	/**
	 * 将各种Button加到panel上
	 */
	private JPanel getPanelCommonOperator() {
		if (this.jPanelCommonOperator == null) {
			this.jPanelCommonOperator = new JPanel();
			this.jPanelCommonOperator.setBounds(10, 135, 194, 149);
			this.jPanelCommonOperator.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), ControlsProperties
					.getString("String_CommonOperator"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			this.jPanelCommonOperator.setLayout(null);
			this.jPanelCommonOperator.add(getButtonPlus());
			this.jPanelCommonOperator.add(getButtonSubtract());
			this.jPanelCommonOperator.add(getButtonMultiply());
			this.jPanelCommonOperator.add(getButtonDivide());
			this.jPanelCommonOperator.add(getButtonAnd());
			this.jPanelCommonOperator.add(getButtonMore());
			this.jPanelCommonOperator.add(getButtonLess());
			this.jPanelCommonOperator.add(getButtonEqual());
			this.jPanelCommonOperator.add(getButtonBracket());
			this.jPanelCommonOperator.add(getButtonNot());
			this.jPanelCommonOperator.add(getButtonMoreOrEqual());
			this.jPanelCommonOperator.add(getButtonLessOrEqual());
			this.jPanelCommonOperator.add(getButtonLeftBracket());
			this.jPanelCommonOperator.add(getButtonRightBracket());
			this.jPanelCommonOperator.add(getButtonLike());
			this.jPanelCommonOperator.add(getButtonAndCompute());
			this.jPanelCommonOperator.add(getButtonOr());
		}
		return jPanelCommonOperator;
	}

	/**
	 * 构造文本框
	 */
	private JTextArea getTextAreaSQLSentence() {
		if (this.jTextAreaSQLSentence == null) {
			this.jTextAreaSQLSentence = new JTextArea();
			this.jTextAreaSQLSentence.setBounds(10, 10, 674, 119);
			this.jTextAreaSQLSentence.setLineWrap(true);

		}
		return this.jTextAreaSQLSentence;
	}

	/**
	 * 构造放Function的panel
	 */
	private JPanel getPanelFunction() {
		if (this.jPanelFunction == null) {
			this.jPanelFunction = new JPanel();
			this.jPanelFunction.setLayout(null);
			this.jPanelFunction.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1, false), ControlsProperties.getString("String_CommonFunction"),
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			this.jPanelFunction.setBounds(210, 135, 190, 149);
			this.jPanelFunction.add(getLabelMathsoperationLabel());
			this.jPanelFunction.add(getLabelStringfunctionLabel());
			this.jPanelFunction.add(getTimefunctionLabel());
			this.jPanelFunction.add(getComboBoxMathsOperation());
			this.jPanelFunction.add(getComboBoxStringFunction());
			this.jPanelFunction.add(getComboBoxTimeFunction());
		}
		return this.jPanelFunction;
	}

	/**
	 * 构造MathsoperationLabel名称显示的Label
	 */
	private JLabel getLabelMathsoperationLabel() {
		if (this.jLabelMathsoperation == null) {
			this.jLabelMathsoperation = new JLabel();
			this.jLabelMathsoperation.setBounds(10, 22, 94, 18);
			this.jLabelMathsoperation.setText(ControlsProperties.getString("String_ArithmeticOperation"));
		}
		return this.jLabelMathsoperation;
	}

	/**
	 * 构造Stringfunction名称显示的label
	 */
	private JLabel getLabelStringfunctionLabel() {
		if (this.jLabelStringfunction == null) {
			this.jLabelStringfunction = new JLabel();
			this.jLabelStringfunction.setText(ControlsProperties.getString("String_CharacterHandling"));
			this.jLabelStringfunction.setBounds(10, 66, 94, 29);
		}
		return this.jLabelStringfunction;
	}

	/**
	 * 构造Timefunction名称显示的label
	 */
	private JLabel getTimefunctionLabel() {
		if (this.jLabelTimefunction == null) {
			this.jLabelTimefunction = new JLabel();
			this.jLabelTimefunction.setText(ControlsProperties.getString("String_DataAndTime"));
			this.jLabelTimefunction.setBounds(10, 118, 94, 18);
		}
		return this.jLabelTimefunction;
	}

	/**
	 * 构造可选择MathsOperation的Combox
	 */
	private JComboBox getComboBoxMathsOperation() {
		if (this.jComboBoxMathsOperation == null) {
			this.jComboBoxMathsOperation = new JComboBox();
			this.jComboBoxMathsOperation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					comboBoxFunction_SelectedIndexChanged(jComboBoxMathsOperation);
				}
			});

			this.jComboBoxMathsOperation.setModel(new DefaultComboBoxModel(new String[] { "", "Abs()", "Acos()", "Asin()", "Atan()", "Atn2()", "Ceiling()",
					"Cos()", "Cot()", "Degrees()", "Exp()", "Floor()", "Log()", "Log10()", "PI()", "Power()", "Radians()", "Rand()", "Round()", "Sign()",
					"Sin()", "Square()", "Sqrt()", "Tan()", "CBool()", "CByte()", "CCur()", "CDate()", "CDbl()", "CInt()", "CLng()", "CSng()", "CStr()",
					"Int()", "Fix()" }));
			this.jComboBoxMathsOperation.setBounds(80, 20, 99, 22);
		}
		return this.jComboBoxMathsOperation;
	}

	/**
	 * 构造可选择StringFunction的Combox
	 */
	private JComboBox getComboBoxStringFunction() {
		if (this.jComboBoxStringFunction == null) {
			this.jComboBoxStringFunction = new JComboBox();
			this.jComboBoxStringFunction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					comboBoxFunction_SelectedIndexChanged(jComboBoxStringFunction);
				}
			});

			this.jComboBoxStringFunction.setModel(new DefaultComboBoxModel(new String[] { "", "Ascii()", "Char()", "Charindex()", "Difference()", "Left()",
					"Len()", "Lower()", "Ltrim()", "Nchar()", "Patindex()", "Replace()", "Replicate()", "Quotename()", "Reverse()", "Right()", "Rtrim()",
					"Soundex()", "Space()", "Str()", "Stuff()", "Substring()", "Unicode()", "Upper()" }));
			this.jComboBoxStringFunction.setBounds(80, 69, 99, 22);
		}
		return this.jComboBoxStringFunction;
	}

	/**
	 * 构造可选择TimeFunction的Combox
	 */
	private JComboBox getComboBoxTimeFunction() {
		if (this.jComboBoxTimeFunction == null) {
			this.jComboBoxTimeFunction = new JComboBox();
			this.jComboBoxTimeFunction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					comboBoxFunction_SelectedIndexChanged(jComboBoxTimeFunction);
				}
			});

			this.jComboBoxTimeFunction.setModel(new DefaultComboBoxModel(new String[] { "", "DateAdd()", "Datediff()", "Datename()", "Datepart()", "Day()",
					"Getdate()", "Getutcdate()", "Month()", "Year()" }));
			this.jComboBoxTimeFunction.setBounds(80, 116, 99, 22);
		}
		return this.jComboBoxTimeFunction;
	}

	/**
	 * OK
	 */
	private JButton getButtonOK() {
		if (this.jButtonOK == null) {
			this.jButtonOK = new JButton();
			this.jButtonOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					filedQueryParameter = new QueryParameter();
					filedQueryParameter.setAttributeFilter(jTextAreaSQLSentence.getText());
					dialogResult = DialogResult.OK;
					dispose();
				}
			});
			this.jButtonOK.setText(CommonProperties.getString("String_Button_OK"));
			this.jButtonOK.setBounds(488, 293, 75, 21);
		}
		getRootPane().setDefaultButton(this.jButtonOK);
		return this.jButtonOK;
	}

	public QueryParameter getQueryParameter() {
		return this.filedQueryParameter;

	}

	public void setQueryParameter(QueryParameter queryParameter) {
		this.filedQueryParameter = queryParameter;

	}

	/**
	 * Cancel
	 */
	private JButton getButtonCancel() {
		if (this.jButtonCancel == null) {
			this.jButtonCancel = new JButton();
			this.jButtonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});

			this.jButtonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			this.jButtonCancel.setBounds(566, 293, 75, 21);
		}
		return this.jButtonCancel;
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

	@Override
	public void escapePressed() {
		dialogResult = DialogResult.CANCEL;
		dispose();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.jButtonOK) {
			filedQueryParameter = new QueryParameter();
			filedQueryParameter.setAttributeFilter(jTextAreaSQLSentence.getText());
			dialogResult = DialogResult.OK;
			dispose();
		}
		if (this.getRootPane().getDefaultButton() == this.jButtonCancel) {
			dialogResult = DialogResult.CANCEL;
			dispose();
		}
	}

	public static class TableTooltipCellRenderer extends JLabel implements TableCellRenderer {
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
