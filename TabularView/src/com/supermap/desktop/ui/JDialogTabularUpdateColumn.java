package com.supermap.desktop.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.infonode.tabbedpanel.Tab;

import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

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

	public JDialogTabularUpdateColumn() {
		super();
		setSize(500,300);
		setLocationRelativeTo(null);
		initComponents();
		initResources();
	}

	private void initComponents() {
		JPanel contentPanel = (JPanel) this.getContentPane();
		contentPanel.setLayout(new GridBagLayout());
		initComboBoxUpdateField();
		this.labelUpdateScope = new JLabel();
		this.checkBoxUpdateColumn = new JCheckBox();
		this.checkBoxUpdateSelect = new JCheckBox();
		initComboBoxSourceOfField();
		initComboBoxOperationField();
		initComobBoxMethod();
		initTextFieldSecondField();
		initTextFieldOperationEQ();
		//@formatter:off
		contentPanel.add(this.labelUpdataField,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10,10,5,0));
		contentPanel.add(this.comboBoxUpdateField,    new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(10,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.labelFieldType,         new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		contentPanel.add(this.labelUpdateScope,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		contentPanel.add(this.checkBoxUpdateColumn,   new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0,10,5,0));
		contentPanel.add(this.checkBoxUpdateSelect,   new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0,10,5,10));
		contentPanel.add(this.labelSourceOfField,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		contentPanel.add(this.comboBoxSourceOfField,  new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.checkBoxInversion,      new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10));
		contentPanel.add(this.labelOperationField,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		contentPanel.add(this.comboBoxOperationField, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.labelOperationFieldType,new GridBagConstraintsHelper(5, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		contentPanel.add(this.labelMethod,            new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		contentPanel.add(this.comboBoxMethod,         new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.textFieldX,             new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.textFieldY,             new GridBagConstraintsHelper(4, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.labelSecondField,       new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.textFieldSecondField,   new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.labelOperationEQ,       new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(this.textFieldOperationEQ,   new GridBagConstraintsHelper(1, 6, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.add(initButtonPanel(),           new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private Component initButtonPanel() {
		JPanel panelButton = new JPanel();
		return panelButton;
	}

	private void initTextFieldOperationEQ() {
		this.labelOperationEQ = new JLabel();
		this.textFieldOperationEQ = new JTextField();
	}

	private void initTextFieldSecondField() {
		this.labelSecondField = new JLabel();
		this.textFieldSecondField = new JTextField();
	}

	private void initComobBoxMethod() {
		this.labelMethod = new JLabel();
		this.comboBoxMethod = new JComboBox<String>();
		this.textFieldX = new JTextField();
		this.textFieldY = new JTextField();

	}

	private void initComboBoxOperationField() {
		this.labelOperationField = new JLabel();
		this.comboBoxOperationField = new JComboBox<String>();
		this.labelOperationFieldType = new JLabel();
	}

	private void initComboBoxUpdateField() {
		this.labelUpdataField = new JLabel();
		this.comboBoxUpdateField = new JComboBox<String>();
		this.labelFieldType = new JLabel();

	}

	private void initComboBoxSourceOfField() {
		this.labelSourceOfField = new JLabel();
		this.comboBoxSourceOfField = new JComboBox<String>();
		this.checkBoxInversion = new JCheckBox();

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

}
