package com.supermap.desktop.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.TextOutputCallback;
import javax.swing.*;

import com.sun.org.apache.xml.internal.serialize.TextSerializer;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * 更新列主界面
 * 
 * @author xie 2016.6.23
 *
 */
public class JDialogTabularUpdateColumn extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelUpdataField;// 待更新字段
	private JComboBox<String> comboBoxUpdateField;
	private JLabel labelFieldType;// 字段类型
	private JLabel labelUpdateScope;// 更新范围
	private JCheckBox checkBoxUpdateColumn;// 整列更新
	private JCheckBox checkBoxUpdateSelect;// 更新选中记录
	private JLabel labelSourceOfField;// 数值来源
	private JComboBox<String> comboBoxSourceOfField;
	private JCheckBox checkBoxInversion;// 反向
	private JLabel labelOperationField;// 第一运算字段/运算字段
	private JLabel labelOperationFieldType;// 第一运算字段类型
	private JComboBox<String> comboBoxOperationField;
	private JLabel labelMethod;// 运算方式
	private JComboBox<String> comboBoxMethod;
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JLabel labelSecondField;// 第二运算字段/用来更新的值/运算因子
	private JLabel labelSecondFieldType;// 第二运算字段类型
	private JTextField textFieldSecondField;
	private JComboBox<String> comboBoxSecondField;
	private JLabel labelOperationEQ;// 运算方程式
	private JTextField textFieldOperationEQ;
	private JLabel labelEQTip;// 运算方式提示

	private JButton buttonApply;
	private JButton buttonClose;
	private IFormTabular tabular;

	private Map<Integer, FieldInfo> fieldInfoMap = new HashMap<Integer, FieldInfo>();// 字段信息MAP，用于存放可更新的列
	private ArrayList<String> integerExpressionsList = new ArrayList<String>();// 整数型函数运算表达式List
	private ArrayList<String> textExpressionsList = new ArrayList<String>();// 文本类型函数运算表达式List
	private ArrayList<String> dateTimeExpressionsList = new ArrayList<String>();
	private ItemListener updateFieldListener;
	private ActionListener checkBoxListener;
	private ItemListener comboBoxSourceOfFieldListener;

	private final int UNITY_EVALUATION = 0;// 统一赋值
	private final int SINGLE_FIELD = 1;// 单字段运算
	private final int DOUBLE_FIELD = 2;// 双字段运算
	private final int FUNCTION = 3;// 函数运算
	private final int EXPRESSION = 4;// 表达式

	private final String[] integerExpressions = { "Abs", "Sqrt", "Ln", "Log", "Int", "ObjectCenterX", "ObjectCenterY", "ObjectLeft", "ObjectRight",
			"ObjectTop", "ObjectButtom", "ObjectWidth", "ObjectHeight" };
	private final String[] textExpressions = { "Left", "Right", "Mid", "UCase", "LCase", "Trim", "TrimEnd", "TirmStart", "ObjectCenterX", "ObjectCenterY",
			"ObjectLeft", "ObjectRight", "ObjectTop", "ObjectButtom", "ObjectWidth", "ObjectHeight", "LRemove", "RRemove", "Replace" };
	private final String[] dateTimeExpressions = {"AddDays","AddHours","AddMilliseconds","AddSeconds","AddMinutes","AddMonths","AddYears","Date","Now"};
	
	private JPanel contentPanel;

	public JDialogTabularUpdateColumn(IFormTabular tabular) {
		super();
		this.tabular = tabular;
		setTitle(TabularViewProperties.getString("String_FormTabularUpdataColumn_Title") + tabular.getText());
		setSize(500, 300);
		setLocationRelativeTo(null);
		initComponents();
		initResources();
		registEvents();
	}

	private void initComponents() {
		this.contentPanel = (JPanel) this.getContentPane();
		this.contentPanel.removeAll();
		this.contentPanel.setLayout(new GridBagLayout());
		initComboBoxUpdateField();
		this.labelUpdateScope = new JLabel();
		this.checkBoxUpdateColumn = new JCheckBox();
		this.checkBoxUpdateSelect = new JCheckBox();
		boolean updateColumn = tabular.getjTableTabular().getSelectedRowCount() > 0
				&& tabular.getjTableTabular().getSelectedRowCount() == tabular.getRowCount() ? true : false;
		this.checkBoxUpdateColumn.setSelected(updateColumn);
		this.checkBoxUpdateSelect.setSelected(!updateColumn);
		initComboBoxSourceOfField();
		initComboBoxOperationField();
		initComobBoxMethod();
		initTextFieldSecondField();
		initTextFieldOperationEQ();
		initLayout();
	}

	private void initLayout() {
		//@formatter:off
		this.contentPanel.add(this.labelUpdataField,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10,10,5,0));
		this.contentPanel.add(this.comboBoxUpdateField,    new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(10,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelFieldType,         new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10,10,5,10));
		this.contentPanel.add(this.labelUpdateScope,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.checkBoxUpdateColumn,   new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.checkBoxUpdateSelect,   new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelSourceOfField,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.comboBoxSourceOfField,  new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(55, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.checkBoxInversion,      new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelOperationField,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.comboBoxOperationField, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelOperationFieldType,new GridBagConstraintsHelper(5, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelMethod,            new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.comboBoxMethod,         new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldX,             new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldY,             new GridBagConstraintsHelper(4, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelSecondField,       new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldSecondField,   new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelSecondFieldType,   new GridBagConstraintsHelper(5, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelOperationEQ,       new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldOperationEQ,   new GridBagConstraintsHelper(1, 6, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelEQTip,             new GridBagConstraintsHelper(1, 7, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(initButtonPanel(),           new GridBagConstraintsHelper(0, 8, 6, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
		//@formatter:on
	}

	private Component initButtonPanel() {
		JPanel panelButton = new JPanel();
		this.buttonApply = ComponentFactory.createButtonApply();
		this.buttonClose = ComponentFactory.createButtonClose();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonApply, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
		panelButton.add(this.buttonClose, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
		return panelButton;
	}

	private void initTextFieldOperationEQ() {
		this.labelOperationEQ = new JLabel();
		this.textFieldOperationEQ = new JTextField();
		initTextFieldOperationEQText(tabular.getRecordset().getFieldInfos().get(0).getType());
		this.textFieldOperationEQ.setEnabled(false);
		this.labelEQTip = new JLabel();
	}

	private void initTextFieldSecondField() {
		this.labelSecondField = new JLabel();
		this.textFieldSecondField = new JTextField();
		this.labelSecondFieldType = new JLabel();
		this.labelSecondField.setPreferredSize(new Dimension(100, 23));
		this.comboBoxSecondField = new JComboBox<String>();
		this.comboBoxSecondField.setEditable(true);
		((JTextField) this.comboBoxSecondField.getEditor().getEditorComponent()).setEditable(false);
		this.comboBoxSecondField.removeAllItems();
		for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
			comboBoxSecondField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
		}

	}

	private void initTextFieldOperationEQText(FieldType type) {
		if (FieldTypeUtilities.isNumber(type)) {
			this.textFieldOperationEQ.setText("0");
		} else {
			this.textFieldOperationEQ.setText("");
		}
	}

	private void initComobBoxMethod() {
		this.labelMethod = new JLabel();
		this.comboBoxMethod = new JComboBox<String>();
		this.comboBoxMethod.setEditable(true);
		((JTextField) this.comboBoxMethod.getEditor().getEditorComponent()).setEditable(false);
		this.comboBoxMethod.getComponent(0).setBackground(Color.lightGray);
		this.textFieldX = new JTextField();
		this.textFieldY = new JTextField();
		this.comboBoxMethod.removeAllItems();
		this.comboBoxMethod.addItem("+");
		this.comboBoxMethod.setEnabled(false);
		this.textFieldX.setEnabled(false);
		this.textFieldY.setEnabled(false);
	}

	private void initComboBoxOperationField() {
		// 初始化运算字段
		this.labelOperationField = new JLabel();
		this.comboBoxOperationField = new JComboBox<String>();
		this.comboBoxOperationField.setEditable(true);
		((JTextField) this.comboBoxOperationField.getEditor().getEditorComponent()).setEditable(false);
		this.labelOperationFieldType = new JLabel();
		this.comboBoxOperationField.removeAllItems();
		for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
			this.comboBoxOperationField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
		}
		this.comboBoxOperationField.setEnabled(false);
		this.labelOperationField.setPreferredSize(new Dimension(80, 23));
	}

	private void initComboBoxUpdateField() {
		// 初始化待更新字段下拉列表
		this.labelUpdataField = new JLabel();
		this.comboBoxUpdateField = new JComboBox<String>();
		this.comboBoxUpdateField.setEditable(true);
		// 设置comboboxOperationField的样式
		((JTextField) this.comboBoxUpdateField.getEditor().getEditorComponent()).setEditable(false);
		this.labelFieldType = new JLabel();
		this.comboBoxUpdateField.removeAllItems();
		int count = 0;
		for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
			if (!tabular.getRecordset().getFieldInfos().get(i).isSystemField()) {
				this.comboBoxUpdateField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
				fieldInfoMap.put(count, tabular.getRecordset().getFieldInfos().get(i));
				count++;
			}
		}
		this.labelFieldType.setPreferredSize(new Dimension(60, 23));
		this.labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos().get(0).getType()));
	}

	private void initComboBoxSourceOfField() {
		// 初始化数值来源下拉列表
		this.labelSourceOfField = new JLabel();
		this.comboBoxSourceOfField = new JComboBox<String>();
		this.comboBoxSourceOfField.setEditable(true);
		((JTextField) this.comboBoxSourceOfField.getEditor().getEditorComponent()).setEditable(false);
		this.checkBoxInversion = new JCheckBox();
		this.checkBoxInversion.setEnabled(false);
		this.comboBoxSourceOfField.setModel(new DefaultComboBoxModel<String>(new String[] {
				TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"),
				TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"),
				TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"),
				TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"),
				CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression") }));
	}

	private void initResources() {
		this.labelUpdataField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataField"));
		this.labelUpdateScope.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataBounds"));
		this.checkBoxUpdateColumn.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_radioButtonUpdateTotalColumn"));
		this.checkBoxUpdateSelect.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_radioButtonUpdateSelectedRows"));
		this.labelSourceOfField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataMode"));
		this.checkBoxInversion.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_checkBoxReverse"));
		this.labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		this.labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		this.labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
		this.labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
	}

	private void registEvents() {
		// 为控件注册事件
		this.updateFieldListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int updateFieldIndex = comboBoxUpdateField.getSelectedIndex();
					labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(fieldInfoMap.get(updateFieldIndex).getType()));
					initTextFieldOperationEQText(fieldInfoMap.get(updateFieldIndex).getType());
				}
			}
		};
		this.comboBoxUpdateField.addItemListener(this.updateFieldListener);
		this.checkBoxListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(checkBoxUpdateColumn)) {
					boolean updateColumn = checkBoxUpdateColumn.isSelected();
					checkBoxUpdateSelect.setSelected(!updateColumn);
					return;
				}
				if (e.getSource().equals(checkBoxUpdateSelect)) {
					boolean updateSelect = checkBoxUpdateSelect.isSelected();
					checkBoxUpdateColumn.setSelected(!updateSelect);
					return;
				}
			}
		};
		this.comboBoxSourceOfFieldListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int sourceOfFieldIndex = comboBoxSourceOfField.getSelectedIndex();
				switch (sourceOfFieldIndex) {
				case UNITY_EVALUATION:
					setUnityEvaluationInfo();
					break;
				case SINGLE_FIELD:
					setSingleFieldInfo();
					break;
				case DOUBLE_FIELD:
					setDoubleFieldInfo();
					break;
				case FUNCTION:
					setFunctionInfo();
					break;
				default:
					break;
				}
			}

		};
		this.comboBoxSourceOfField.addItemListener(comboBoxSourceOfFieldListener);
		this.checkBoxUpdateColumn.addActionListener(this.checkBoxListener);
		this.checkBoxUpdateSelect.addActionListener(this.checkBoxListener);
	}

	private void setFunctionInfo() {
		// 设置函数运算界面
		checkBoxInversion.setEnabled(true);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		comboBoxOperationField.setEnabled(true);
		labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
				.get(comboBoxOperationField.getSelectedIndex()).getType()));
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorFunction"));
		comboBoxMethod.setEnabled(true);
		comboBoxMethod.removeAllItems();
		FieldType updateFieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		if (FieldTypeUtilities.isNumber(updateFieldType)) {
			for (int i = 0; i < integerExpressions.length; i++) {
				comboBoxMethod.addItem(integerExpressions[i]);
				integerExpressionsList.add(integerExpressions[i]);
			}
			textFieldX.setEnabled(false);
			textFieldOperationEQ.setText(integerExpressions[0] + "(" + comboBoxOperationField.getSelectedItem().toString() + ")");
			labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_Abs"));
		}
		if (FieldTypeUtilities.isString(updateFieldType)||updateFieldType.equals(FieldType.CHAR)) {
			for (int i = 0; i < textExpressions.length; i++) {
				comboBoxMethod.addItem(textExpressions[i]);
				textExpressionsList.add(textExpressions[i]);
			}
			textFieldX.setEnabled(true);
			textFieldX.setText("1");
			textFieldOperationEQ.setText(textExpressions[0]+"("+comboBoxOperationField.getSelectedItem().toString()+",1"+")");
			labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_Left"));
		}
		if (updateFieldType.equals(FieldType.DATETIME)) {
			for (int i = 0; i < dateTimeExpressions.length; i++) {
				comboBoxMethod.addItem(dateTimeExpressions[i]);
				dateTimeExpressionsList.add(dateTimeExpressions[i]);
			}
			textFieldX.setEnabled(true);
			textFieldX.setText("0");
			textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString()+"."+dateTimeExpressions[0]+"(0)");
			labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_AddDays"));
		}
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
		labelSecondField.setEnabled(false);
		contentPanel.remove(comboBoxSecondField);
		contentPanel.add(
				textFieldSecondField,
				new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.updateUI();
		textFieldSecondField.setEnabled(false);
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
	}

	private void setDoubleFieldInfo() {
		// 双字段运算时的界面设置
		checkBoxInversion.setEnabled(false);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelFirstField"));
		comboBoxOperationField.setEnabled(true);
		labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
				.get(comboBoxOperationField.getSelectedIndex()).getType()));
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		comboBoxMethod.setEnabled(true);
		comboBoxMethod.removeAllItems();
		comboBoxMethod.addItem("+");
		textFieldX.setEnabled(false);
		textFieldY.setEnabled(false);
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField1"));
		contentPanel.remove(textFieldSecondField);
		contentPanel.add(
				comboBoxSecondField,
				new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.updateUI();
		labelSecondFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos().get(comboBoxSecondField.getSelectedIndex())
				.getType()));
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + comboBoxMethod.getSelectedItem().toString()
				+ comboBoxSecondField.getSelectedItem().toString());
	}

	private void setSingleFieldInfo() {
		// 单字段运算时的界面设置
		checkBoxInversion.setEnabled(true);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		comboBoxOperationField.setEnabled(true);
		labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
				.get(comboBoxOperationField.getSelectedIndex()).getType()));
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		comboBoxMethod.removeAllItems();
		comboBoxMethod.addItem("+");
		comboBoxMethod.setEnabled(true);
		textFieldX.setEnabled(false);
		textFieldY.setEnabled(false);
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelFieldValue"));
		contentPanel.remove(comboBoxSecondField);
		contentPanel.add(
				textFieldSecondField,
				new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.updateUI();
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		String expression = comboBoxOperationField.getSelectedItem().toString() + comboBoxMethod.getSelectedItem().toString();
		if (StringUtilities.isNullOrEmpty(textFieldSecondField.getText())
				&& FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			expression += "0";
		}
		if (!StringUtilities.isNullOrEmpty(textFieldSecondField.getText())
				&& FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			expression += textFieldSecondField.getText();
		}
		textFieldOperationEQ.setText(expression);
	}

	private void setUnityEvaluationInfo() {
		// 统一赋值时的界面设置
		checkBoxInversion.setEnabled(false);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		comboBoxOperationField.setEnabled(false);
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		comboBoxMethod.setEnabled(false);
		comboBoxMethod.removeAllItems();
		comboBoxMethod.addItem("+");
		textFieldX.setEnabled(false);
		textFieldY.setEnabled(false);
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
		textFieldSecondField.setEnabled(true);
		contentPanel.remove(comboBoxSecondField);
		contentPanel.add(
				textFieldSecondField,
				new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.updateUI();
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		if (StringUtilities.isNullOrEmpty(textFieldSecondField.getText())
				&& FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			textFieldOperationEQ.setText("0");
		}
		if (!StringUtilities.isNullOrEmpty(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(textFieldSecondField.getText());
		} else {
			textFieldOperationEQ.setText("");
		}
	}

}
