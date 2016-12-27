package com.supermap.desktop.ui;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EngineType;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.beans.EditHistoryBean;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.editHistory.TabularEditHistory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.Convert;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.UpdateColumnUtilties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 更新列主界面
 *
 * @author xie 2016.6.23
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
    private JRadioButton radioButtonUpdateColumn;// 整列更新
    private JRadioButton radioButtonUpdateSelect;// 更新选中记录
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
    private JTextField textAreaOperationEQ;
    private JLabel labelEQTip;// 运算方式提示
    private JButton buttonApply;
    private JButton buttonClose;
    private IFormTabular tabular;
    private JPanel contentPanel;
    private FileChooserControl fileChooser;

    private Map<Integer, FieldInfo> fieldInfoMap = new HashMap();// 字段信息MAP，用于存放可更新的列
    private JButton buttonExpression; // 表达式调用入口

    private final String[] integerExpressions = {"Abs", "Sqrt", "Ln", "Log", "Int", "ObjectCenterX", "ObjectCenterY", "ObjectLeft", "ObjectRight",
            "ObjectTop", "ObjectBottom", "ObjectWidth", "ObjectHeight"};
    private final String[] textExpressions = {"Left", "Right", "Mid", "UCase", "LCase", "Trim", "TrimEnd", "TrimStart", "ObjectCenterX", "ObjectCenterY",
            "ObjectLeft", "ObjectRight", "ObjectTop", "ObjectBottom", "ObjectWidth", "ObjectHeight", "LRemove", "RRemove", "Replace"};
    private final String[] dateTimeExpressions = {"AddDays", "AddHours", "AddMilliseconds", "AddSeconds", "AddMinutes", "AddMonths", "AddYears", "Date", "Now"};
    private final String[] dateMethodExpresssions = {"DaysInMonth", "Second", "Minute", "Hour", "Day", "Month", "Year", "DayOfYear", "DayOfWeek"};

    private ItemListener updateFieldListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            updateFieldChanged(e);
        }
    };
    private ActionListener checkBoxListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            checkBoxChanged(e);
        }
    };
    private ItemListener comboBoxSourceOfFieldListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            sourceOfFieldChanged(e);
        }

    };
    private ItemListener comboBoxOperationFieldListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            operationFieldChanged(e);
        }

    };
    private ItemListener comboBoxMethodListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            methodChanged(e);
        }

    };
    private DocumentListener textFieldXChangedListener = new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
            textFieldXChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            textFieldXChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            textFieldXChanged();
        }

    };
    private DocumentListener textFieldYChangedListener = new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
            textFieldYChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            textFieldYChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            textFieldYChanged();
        }

    };
    private DocumentListener textFieldSecondFieldListener = new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
            secondFieldChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            secondFieldChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            secondFieldChanged();
        }
    };
    private ActionListener expressionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            getSqlExpression();
        }
    };
    private ItemListener comboBoxSecondFieldListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            comboBoxSecondFieldChanged(e);
        }
    };
    private ActionListener buttonApplyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonApplyClicked();
        }

    };
    private ActionListener buttonCloseListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            disposeDialog();
        }

    };
    private ActionListener fileChooserListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooserClicked();
        }
    };
    private KeyAdapter textAreaOperationEQKeyListener = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            buttonApply.setEnabled(true);
        }
    };

    public JDialogTabularUpdateColumn(IFormTabular tabular) {
        super();
        this.tabular = tabular;
        setTitle(TabularViewProperties.getString("String_FormTabularUpdataColumn_Title") + tabular.getText());
        setSize(540, 320);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2 - 200);
        initComponents();
        this.componentList.add(this.buttonApply);
        this.componentList.add(this.buttonClose);
        this.setFocusTraversalPolicy(this.policy);
        this.getRootPane().setDefaultButton(this.buttonApply);
        initResources();
        registEvents();
    }

    private void initComponents() {
        this.contentPanel = (JPanel) this.getContentPane();
        this.contentPanel.removeAll();
        this.contentPanel.setLayout(new GridBagLayout());
        initComboBoxUpdateField();
        this.labelUpdateScope = new JLabel();
        this.radioButtonUpdateColumn = new JRadioButton();
        this.radioButtonUpdateSelect = new JRadioButton();
        boolean updateColumn = tabular.getjTableTabular().getSelectedRowCount() > 0
                && tabular.getjTableTabular().getSelectedRowCount() == tabular.getRowCount() ? true : false;
        this.radioButtonUpdateColumn.setSelected(updateColumn);
        this.radioButtonUpdateSelect.setSelected(!updateColumn);
        initComboBoxOperationField();
        initComobBoxMethod();
        initTextFieldOperationEQ();
        this.buttonExpression = new JButton("...");
        initLayout();
        initTextFieldOperationEQText(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType());
        if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.BOOLEAN)) {
            replaceSecondField(comboBoxSecondField, textFieldSecondField, fileChooser);
            setComboBoxSecondFieldItems(FieldType.BOOLEAN);
            this.buttonApply.setEnabled(true);
            this.textAreaOperationEQ.setText("True");
        }
        if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.LONGBINARY)) {
            replaceSecondField(fileChooser, textFieldSecondField, comboBoxSecondField);
            this.textAreaOperationEQ.setText("null");
        }
    }

    private void initLayout() {
        this.contentPanel.removeAll();
        //@formatter:off
        this.contentPanel.add(this.labelUpdataField, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10, 10, 5, 0));
        this.contentPanel.add(this.comboBoxUpdateField, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(10, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.labelFieldType, new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10, 10, 5, 10));
        this.contentPanel.add(this.labelUpdateScope, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        this.contentPanel.add(this.radioButtonUpdateColumn, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 10, 5, 0));
        this.contentPanel.add(this.radioButtonUpdateSelect, new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 10, 5, 10));
        this.contentPanel.add(this.labelSourceOfField, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        this.contentPanel.add(this.comboBoxSourceOfField, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(55, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.checkBoxInversion, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 1).setInsets(0, 10, 5, 10));
        addContentPanel();
        this.contentPanel.add(initButtonPanel(), new GridBagConstraintsHelper(0, 8, 6, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
        //@formatter:on
    }

    private void addContentPanel() {
        //@formatter:off
        this.contentPanel.add(this.labelOperationField, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        this.contentPanel.add(this.comboBoxOperationField, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.labelOperationFieldType, new GridBagConstraintsHelper(5, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 10));
        this.contentPanel.add(this.labelMethod, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        this.contentPanel.add(this.comboBoxMethod, new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.textFieldX, new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));
        this.contentPanel.add(this.textFieldY, new GridBagConstraintsHelper(4, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.labelSecondField, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.textFieldSecondField, new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.labelSecondFieldType, new GridBagConstraintsHelper(5, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 10));
        this.contentPanel.add(this.labelOperationEQ, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.textAreaOperationEQ, new GridBagConstraintsHelper(1, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.buttonExpression, new GridBagConstraintsHelper(4, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 0, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.contentPanel.add(this.labelEQTip, new GridBagConstraintsHelper(1, 7, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0).setFill(GridBagConstraints.HORIZONTAL));
//        this.contentPanel.add(this.buttonExpression, new GridBagConstraintsHelper(4, 7, 1, 1).setAnchor(GridBagConstraints.NORTH).setWeight(10, 1).setInsets(0, 10, 5, 0));
//        scrollPane.setViewportView(this.textAreaOperationEQ);
        //@formatter:on
    }

    private Component initButtonPanel() {
        JPanel panelButton = new JPanel();
        this.buttonApply = ComponentFactory.createButtonApply();
        this.buttonApply.setEnabled(true);
        this.buttonClose = ComponentFactory.createButtonClose();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonApply, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
        panelButton.add(this.buttonClose, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
        return panelButton;
    }

    private void initTextFieldOperationEQ() {
        this.labelOperationEQ = new JLabel();
        this.textAreaOperationEQ = new JTextField();
        this.labelEQTip = new JLabel();
    }

    private void setComboBoxSecondFieldItems(FieldType fieldType) {
        if (fieldType.equals(FieldType.BOOLEAN)) {
            this.comboBoxSecondField.removeAllItems();
            this.comboBoxSecondField.addItem("True");
            this.comboBoxSecondField.addItem("False");
        } else {
            this.comboBoxSecondField.removeAllItems();
            for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
                comboBoxSecondField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
            }
        }
    }

    private void initTextFieldOperationEQText(FieldType type) {
        if (FieldTypeUtilities.isNumber(type) && (!StringUtilities.isNumber(textFieldSecondField.getText()))) {
            this.textFieldSecondField.setText("0");
            this.textAreaOperationEQ.setText("0");
        } else if (type.equals(FieldType.DATETIME)) {
            this.textFieldSecondField.setText("null");
            this.textAreaOperationEQ.setText("null");
            buttonApply.setEnabled(true);
        } else if (FieldTypeUtilities.isTextField(type) || type.equals(FieldType.CHAR)) {
            this.textFieldSecondField.setText("");
            this.textAreaOperationEQ.setText("\"\"");
        }
    }

    private void initComobBoxMethod() {
        this.labelMethod = new JLabel();
        this.comboBoxMethod = new JComboBox<String>();
        this.comboBoxMethod.setEditable(true);
        ((JTextField) this.comboBoxMethod.getEditor().getEditorComponent()).setEditable(false);
        this.textFieldX = new JTextField();
        this.textFieldY = new JTextField();
        resetMethodItems();
        this.comboBoxMethod.setEnabled(false);
        this.textFieldX.setEnabled(false);
        this.textFieldY.setEnabled(false);
        this.comboBoxMethod.setPreferredSize(new Dimension(160, 23));
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
            if (!tabular.getRecordset().getFieldInfos().get(i).getType().equals(FieldType.LONGBINARY)) {
                this.comboBoxOperationField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
            }
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
        this.labelSecondField = new JLabel();
        this.textFieldSecondField = new JTextField();
        this.labelSecondFieldType = new JLabel();
        this.labelSecondField.setPreferredSize(new Dimension(100, 23));
        this.comboBoxSecondField = new JComboBox<String>();
        this.comboBoxSecondField.setEditable(true);
        this.fileChooser = new FileChooserControl();
        ((JTextField) this.comboBoxSecondField.getEditor().getEditorComponent()).setEditable(false);
        this.comboBoxUpdateField.removeAllItems();
        int count = 0;
        String defualtSelectField = "";
        for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
            if (!tabular.getRecordset().getFieldInfos().get(i).isSystemField()) {
                this.comboBoxUpdateField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
                fieldInfoMap.put(count, tabular.getRecordset().getFieldInfos().get(i));
                if (tabular.getjTableTabular().getSelectedColumn() >= 0) {
                    defualtSelectField = tabular.getRecordset().getFieldInfos().get(tabular.getjTableTabular().getSelectedColumn()).getName();
                }
                count++;
            }
        }
        boolean hasItem = false;
        Iterator<FieldInfo> values = fieldInfoMap.values().iterator();
        while (values.hasNext()) {
            if (values.next().getName().equals(defualtSelectField)) {
                hasItem = true;
                break;
            }
        }
        FieldType defualtType = fieldInfoMap.get(0).getType();
        if (!StringUtilities.isNullOrEmptyString(defualtSelectField) && hasItem) {
            // 设置默认选中行
            this.comboBoxUpdateField.setSelectedItem(defualtSelectField);
            defualtType = fieldInfoMap.get(this.comboBoxUpdateField.getSelectedIndex()).getType();
            this.labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(defualtType));
        } else {
            this.labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(defualtType));
        }
        this.labelFieldType.setPreferredSize(new Dimension(60, 23));
        // 初始化数值来源下拉列表
        this.labelSourceOfField = new JLabel();
        this.comboBoxSourceOfField = new JComboBox<String>();
        this.comboBoxSourceOfField.setEditable(true);
        ((JTextField) this.comboBoxSourceOfField.getEditor().getEditorComponent()).setEditable(false);
        this.checkBoxInversion = new JCheckBox();
        this.checkBoxInversion.setEnabled(false);
        setComboBoxSourceOfFieldItems(defualtType);
    }

    private void setComboBoxSourceOfFieldItems(FieldType defualtType) {
        if (defualtType.equals(FieldType.BOOLEAN)) {
            comboBoxSourceOfField.removeAllItems();
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"));
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"));
        } else if (defualtType.equals(FieldType.DATETIME)) {
            comboBoxSourceOfField.removeAllItems();
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"));
        } else if (defualtType.equals(FieldType.LONGBINARY)) {
            comboBoxSourceOfField.removeAllItems();
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
        } else {
            comboBoxSourceOfField.removeAllItems();
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"));
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"));
            comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"));
        }
    }

    private void initResources() {
        this.labelUpdataField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataField"));
        this.labelUpdateScope.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataBounds"));
        this.radioButtonUpdateColumn.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_radioButtonUpdateTotalColumn"));
        this.radioButtonUpdateSelect.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_radioButtonUpdateSelectedRows"));
        this.labelSourceOfField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataMode"));
        this.checkBoxInversion.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_checkBoxReverse"));
        this.labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
        this.labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
        this.labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
        this.labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
    }

    private void registEvents() {
        // 为控件注册事件
        removeEvents();
        this.comboBoxUpdateField.addItemListener(this.updateFieldListener);
        this.comboBoxSourceOfField.addItemListener(this.comboBoxSourceOfFieldListener);
        this.radioButtonUpdateColumn.addActionListener(this.checkBoxListener);
        this.radioButtonUpdateSelect.addActionListener(this.checkBoxListener);
        this.checkBoxInversion.addActionListener(this.checkBoxListener);
        this.comboBoxOperationField.addItemListener(this.comboBoxOperationFieldListener);
        this.comboBoxMethod.addItemListener(this.comboBoxMethodListener);
        this.textFieldX.getDocument().addDocumentListener(this.textFieldXChangedListener);
        this.textFieldY.getDocument().addDocumentListener(this.textFieldYChangedListener);
        this.textFieldSecondField.getDocument().addDocumentListener(this.textFieldSecondFieldListener);
        this.textAreaOperationEQ.addKeyListener(this.textAreaOperationEQKeyListener);
        this.comboBoxSecondField.addItemListener(this.comboBoxSecondFieldListener);
        this.buttonApply.addActionListener(this.buttonApplyListener);
        this.buttonClose.addActionListener(this.buttonCloseListener);
        this.fileChooser.getButton().addActionListener(this.fileChooserListener);
        this.buttonExpression.addActionListener(expressionListener);
    }

    private void disposeDialog() {
        removeEvents();
        fieldInfoMap.clear();
        fieldInfoMap = null;
        dispose();
    }

    private void removeEvents() {
        this.comboBoxUpdateField.removeItemListener(this.updateFieldListener);
        this.comboBoxSourceOfField.removeItemListener(this.comboBoxSourceOfFieldListener);
        this.radioButtonUpdateColumn.removeActionListener(this.checkBoxListener);
        this.radioButtonUpdateSelect.removeActionListener(this.checkBoxListener);
        this.checkBoxInversion.removeActionListener(this.checkBoxListener);
        this.comboBoxOperationField.removeItemListener(this.comboBoxOperationFieldListener);
        this.comboBoxMethod.removeItemListener(this.comboBoxMethodListener);
        this.textFieldX.getDocument().removeDocumentListener(this.textFieldXChangedListener);
        this.textFieldY.getDocument().removeDocumentListener(this.textFieldYChangedListener);
        this.textFieldSecondField.getDocument().removeDocumentListener(this.textFieldSecondFieldListener);
        this.textAreaOperationEQ.removeKeyListener(this.textAreaOperationEQKeyListener);
        this.comboBoxSecondField.addItemListener(this.comboBoxSecondFieldListener);
        if (null != buttonExpression) {
            this.buttonExpression.removeActionListener(this.expressionListener);
        }
        this.comboBoxSecondField.removeItemListener(this.comboBoxSecondFieldListener);
        this.buttonApply.removeActionListener(this.buttonApplyListener);
        this.buttonClose.removeActionListener(this.buttonCloseListener);
        this.buttonExpression.removeActionListener(expressionListener);
    }


    private void getSqlExpression() {
        SQLExpressionDialog dialog = new SQLExpressionDialog();
        DialogResult result = dialog.showDialog(textAreaOperationEQ.getText(), tabular.getRecordset().getDataset());
        if (result == DialogResult.OK) {
            String filter = dialog.getQueryParameter().getAttributeFilter();
            if (!StringUtilities.isNullOrEmpty(filter)) {
                textAreaOperationEQ.setText(filter);
                buttonApply.setEnabled(true);
            }
        }
    }

    private void resetMethodItems() {
        if (comboBoxUpdateField.getSelectedIndex() >= 0) {
            FieldType tempType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
            if (FieldTypeUtilities.isNumber(tempType)) {
                comboBoxMethod.removeAllItems();
                comboBoxMethod.addItem("+");
                comboBoxMethod.addItem("-");
                comboBoxMethod.addItem("*");
                comboBoxMethod.addItem("/");
                comboBoxMethod.addItem("%");
            } else if (FieldTypeUtilities.isTextField(tempType) || tempType.equals(FieldType.CHAR)) {
                comboBoxMethod.removeAllItems();
                comboBoxMethod.addItem("+");
            } else if (tempType.equals(FieldType.BOOLEAN)) {
                comboBoxMethod.removeAllItems();
                comboBoxMethod.addItem("+");
                comboBoxMethod.addItem("-");
                comboBoxMethod.addItem(">");
                comboBoxMethod.addItem(">=");
                comboBoxMethod.addItem("<");
                comboBoxMethod.addItem("<=");
                comboBoxMethod.addItem("==");
                comboBoxMethod.addItem("!=");
            }
        }
    }

    private void setFunctionInfo() {
        // 设置函数运算界面
        labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
        comboBoxOperationField.setEnabled(true);

        FieldType updateFieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
        if (updateFieldType.equals(FieldType.DATETIME)) {
            // 待更新字段为日期型时，运算字段只能有日期型字段
            this.comboBoxOperationField.removeAllItems();
            for (int i = 0; i < tabular.getRecordset().getFieldInfos().getCount(); i++) {
                if (tabular.getRecordset().getFieldInfos().get(i).getType().equals(FieldType.DATETIME)) {
                    this.comboBoxOperationField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
                }
            }
        } else {
            this.comboBoxOperationField.removeAllItems();
            for (int i = 0; i < tabular.getRecordset().getFieldInfos().getCount(); i++) {
                this.comboBoxOperationField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
            }
        }
        labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
                .get(comboBoxOperationField.getSelectedItem().toString()).getType()));
        labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorFunction"));
        comboBoxMethod.setEnabled(true);
        resetMethodItemsForMathModel();
        labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
        labelSecondField.setEnabled(false);
        replaceSecondField(textFieldSecondField, comboBoxSecondField, fileChooser);
        textFieldSecondField.setEnabled(false);
        labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
        labelSecondFieldType.setText("");
        this.buttonExpression.setEnabled(false);
        this.textAreaOperationEQ.setEnabled(false);
        this.checkBoxInversion.setEnabled(false);
        updataExpressionValue();
    }

    private void resetMethodItemsForMathModel() {
        FieldType updateFieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
        FieldType operationFieldType = tabular.getRecordset().getFieldInfos().get(comboBoxOperationField.getSelectedItem().toString()).getType();
        comboBoxMethod.removeAllItems();
        if (FieldTypeUtilities.isNumber(updateFieldType)) {
            if (operationFieldType.equals(FieldType.DATETIME)) {
                for (String dateMethodExpression : dateMethodExpresssions) {
                    comboBoxMethod.addItem(dateMethodExpression);
                }
            } else {
                for (String integerExpression : integerExpressions) {
                    comboBoxMethod.addItem(integerExpression);
                }
            }
        }
        if (FieldTypeUtilities.isTextField(updateFieldType) || updateFieldType.equals(FieldType.CHAR)) {
            if (operationFieldType.equals(FieldType.DATETIME)) {
                for (String dateMethodExpression : dateMethodExpresssions) {
                    comboBoxMethod.addItem(dateMethodExpression);
                }
                for (String textExpression : textExpressions) {
                    comboBoxMethod.addItem(textExpression);
                }
            } else {
                for (String textExpression : textExpressions) {
                    comboBoxMethod.addItem(textExpression);
                }
            }
        }
        if (updateFieldType.equals(FieldType.DATETIME)) {
            for (String dateTimeExpression : dateTimeExpressions) {
                comboBoxMethod.addItem(dateTimeExpression);
            }
            textFieldX.setEnabled(true);
            textFieldX.setText("0");
            textAreaOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + "." + dateTimeExpressions[0] + "(0)");
            labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_AddDays"));
        }
    }

    private void setDoubleFieldInfo() {
        // 双字段运算时的界面设置
        checkBoxInversion.setEnabled(true);
        labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelFirstField"));
        comboBoxOperationField.setEnabled(true);
        labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
                .get(comboBoxOperationField.getSelectedItem().toString()).getType()));
        labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
        updateComboboxMethod();
        comboBoxMethod.setEnabled(true);
        labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField1"));
        replaceSecondField(comboBoxSecondField, textFieldSecondField, fileChooser);
        setComboBoxSecondFieldItems(FieldType.INT16);
        labelSecondFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos().get(comboBoxSecondField.getSelectedIndex())
                .getType()));
        labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
        labelEQTip.setText("");
        this.buttonExpression.setEnabled(true);
        this.textAreaOperationEQ.setEnabled(true);
        this.labelSecondField.setEnabled(true);
        updataExpressionValue();
    }

    private void setSingleFieldInfo() {
        // 单字段运算时的界面设置
        checkBoxInversion.setEnabled(true);
        labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
        comboBoxOperationField.setEnabled(true);
        labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
                .get(comboBoxOperationField.getSelectedItem().toString()).getType()));
        labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
        updateComboboxMethod();
        comboBoxMethod.setEnabled(true);
        labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelFieldValue"));
        replaceSecondField(textFieldSecondField, comboBoxSecondField, fileChooser);
        labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
        textFieldSecondField.setEnabled(true);
        labelEQTip.setText("");
        labelSecondFieldType.setText("");
        this.textAreaOperationEQ.setEnabled(true);
        this.buttonExpression.setEnabled(true);
        this.labelSecondField.setEnabled(true);
        updataExpressionValue();
    }

    private void setUnityEvaluationInfo() {
        // 统一赋值时的界面设置
        checkBoxInversion.setEnabled(false);
        labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
        comboBoxOperationField.setEnabled(false);
        labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
        updateComboboxMethod();
        comboBoxMethod.setEnabled(false);
        textFieldSecondField.setEnabled(true);
        labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
        labelSecondFieldType.setText("");
        labelEQTip.setText("");
        labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
        if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.BOOLEAN)) {
            replaceSecondField(comboBoxSecondField, textFieldSecondField, fileChooser);
            setComboBoxSecondFieldItems(FieldType.BOOLEAN);
            textAreaOperationEQ.setText("True");
        } else if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.LONGBINARY)) {
            replaceSecondField(fileChooser, comboBoxSecondField, textFieldSecondField);
        } else {
            replaceSecondField(textFieldSecondField, comboBoxSecondField, fileChooser);
        }
//        updateEQ(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType(), textFieldSecondField.getText());
        this.buttonExpression.setEnabled(true);
        this.textAreaOperationEQ.setEnabled(true);
        this.labelSecondField.setEnabled(true);
        updataExpressionValue();
    }

    private void updateComboboxMethod() {
        resetMethodItems();
        textFieldX.setEnabled(false);
        textFieldY.setEnabled(false);
    }

    private void replaceSecondField(JComponent component, JComponent removeComponent, JComponent removeComponent1) {
        contentPanel.remove(removeComponent);
        contentPanel.remove(removeComponent1);
        contentPanel.add(component, new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 10, 0)
                .setFill(GridBagConstraints.HORIZONTAL));
        contentPanel.updateUI();
    }

//    private void resetTextFieldOperationEQ() {
//        String info = "";
//        if (checkBoxInversion.isSelected()) {
//            info = comboBoxMethod.getSelectedItem().toString() + comboBoxOperationField.getSelectedItem().toString();
//        } else {
//            info = comboBoxOperationField.getSelectedItem().toString() + comboBoxMethod.getSelectedItem().toString();
//        }
////        updateEQ(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType(), info);
//
//    }

    private void updateFieldChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            int updateFieldIndex = comboBoxUpdateField.getSelectedIndex();
            FieldType fieldType = fieldInfoMap.get(updateFieldIndex).getType();
            setComboBoxSourceOfFieldItems(fieldType);
            if (fieldType.equals(FieldType.BOOLEAN)) {
                replaceSecondField(comboBoxSecondField, textFieldSecondField, fileChooser);
                setComboBoxSecondFieldItems(fieldType);
            }
            comboBoxSourceOfField.setSelectedIndex(0);
            comboBoxOperationField.removeAllItems();
            for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
                if (!tabular.getRecordset().getFieldInfos().get(i).getType().equals(FieldType.LONGBINARY)) {
                    this.comboBoxOperationField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
                }
            }
            labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(fieldInfoMap.get(updateFieldIndex).getType()));
            updataExpressionValue();
        }
    }

    private void checkBoxChanged(ActionEvent e) {
        if (e.getSource().equals(radioButtonUpdateColumn)) {
            boolean updateColumn = radioButtonUpdateColumn.isSelected();
            radioButtonUpdateSelect.setSelected(!updateColumn);
            buttonApply.setEnabled(true);
            return;
        }
        if (e.getSource().equals(radioButtonUpdateSelect)) {
            boolean updateSelect = radioButtonUpdateSelect.isSelected();
            radioButtonUpdateColumn.setSelected(!updateSelect);
            buttonApply.setEnabled(true);
            return;
        }
        if (e.getSource().equals(checkBoxInversion)) {
            updataExpressionValue();
            return;
        }
    }

    private void sourceOfFieldChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String sourceOfField = comboBoxSourceOfField.getSelectedItem().toString();
            if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
                // 设置统一运算界面
                setUnityEvaluationInfo();
                return;
            }
            if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
                // 设置单字段运算界面
                setSingleFieldInfo();
                return;
            }
            if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"))) {
                // 设置双字段运算界面
                setDoubleFieldInfo();
                buttonApply.setEnabled(true);
                return;
            }
            if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
                // 设置函数运算界面
                setFunctionInfo();
                buttonApply.setEnabled(true);
                return;
            }
        }
    }

    private void operationFieldChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            FieldType operationField = tabular.getRecordset().getFieldInfos().get(comboBoxOperationField.getSelectedItem().toString()).getType();
            labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(operationField));
            updataExpressionValue();
        }
    }

    private void resetMethodInfo() {
        resetMethodItemsForMathModel();
        comboBoxMethod.setSelectedIndex(0);
        comboBoxMethod.setEnabled(true);
    }

    private void methodChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String tempOperationField = comboBoxOperationField.getSelectedItem().toString();
            String method = comboBoxMethod.getSelectedItem().toString();
            String sourceOfField = comboBoxSourceOfField.getSelectedItem().toString();
            if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
                // 函数运算
                if (method.equals("Abs") || method.equals("Sqrt") || method.equals("Ln") || method.equals("Int")) {
                    setMethodStatus(true, false, false, method + "(" + tempOperationField + ")", method);
                } else if (UpdateColumnUtilties.isObjectConnect(method)) {
                    setMethodStatus(false, false, false, "Object." + method + "()", method);
                } else if (method.equals("Log")) {
                    textFieldX.setText("10");
                    setMethodStatus(true, true, false, method + "(" + tempOperationField + ",10)", method);
                } else if (method.equals("Left") || method.equals("Right")) {
                    textFieldX.setText("1");
                    setMethodStatus(true, true, false, method + "(" + tempOperationField + ",1)", method);
                } else if (method.equals("LRemove") || method.equals("RRemove")) {
                    textFieldX.setText("0");
                    setMethodStatus(true, true, false, method + "(" + tempOperationField + ",0)", method);
                } else if (method.equals("Mid")) {
                    textFieldX.setText("0");
                    textFieldY.setText("0");
                    setMethodStatus(true, true, true, method + "(" + tempOperationField + ",0,0)", method);
                } else if (method.equals("UCase") || method.equals("LCase") || method.equals("Trim")) {
                    setMethodStatus(true, false, false, method + "(" + tempOperationField + ")", method);
                } else if (method.equals("TrimEnd") || method.equals("TrimStart")) {
                    textFieldX.setText("abc");
                    setMethodStatus(true, true, false, method + "(" + tempOperationField + ",'a','b','c')", method);
                } else if (method.equals("Replace")) {
                    textFieldX.setText("");
                    textFieldY.setText("");
                    setMethodStatus(true, true, true, method + "(" + tempOperationField + ",'','')", method);
                } else if (method.contains("Add")) {
                    textFieldX.setText("0");
                    setMethodStatus(true, true, false, tempOperationField + "." + method + "(0)", method);
                } else if (method.equals("Date") || method.equals("Now")) {
                    setMethodStatus(true, false, false, tempOperationField + "." + method, method);
                } else if (method.equals("DaysInMonth")) {
                    setMethodStatus(true, false, false, method + "(" + tempOperationField + ".Year," + tempOperationField + ".Month)", method);
                } else if (method.equals("Second") || method.equals("Minute") || method.equals("Hour") || method.equals("Day") || method.equals("Month")
                        || method.equals("Year") || method.equals("DayOfYear") || method.equals("DayOfWeek")) {
                    setMethodStatus(false, false, false, tempOperationField + "." + method, method);
                }
                updataExpressionValue();
            }
        }
    }

    private void setMethodStatus(boolean enableField, boolean enableX, boolean enableY, String expression, String method) {
        comboBoxOperationField.setEnabled(enableField);
        textFieldX.setEnabled(enableX);
        textFieldY.setEnabled(enableY);
        textAreaOperationEQ.setText(expression);
        if (!StringUtilities.isNullOrEmptyString(method)) {
            labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_" + method));
        }
    }

    private void textFieldXChanged() {
        updataExpressionValue();
    }

    private void textFieldYChanged() {
        updataExpressionValue();
    }

    private void secondFieldChanged() {
        updataExpressionValue();
    }

    private void updataExpressionValue() {
        try {
            DatasetVector dataset = tabular.getRecordset().getDataset();
            EngineType engineType = dataset.getDatasource().getEngineType();
            FieldType updataFieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
            String expressionValue = "";
            String updateMode = comboBoxSourceOfField.getSelectedItem().toString();
            if (updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
                try {
                    expressionValue = getTextBoxSecondFieldValue(updataFieldType).toString();
                } catch (Exception e) {
                    expressionValue = "null";
                }
                //字符
                if (updataFieldType == FieldType.CHAR && StringUtilities.isNullOrEmpty(expressionValue)) {
                    expressionValue = "null";
                }

                //字符
                if (updataFieldType == FieldType.WTEXT && StringUtilities.isNullOrEmpty(expressionValue)) {
                    expressionValue = "null";
                }

                //二进制,二进制的字段表达式保持为null
                if (updataFieldType == FieldType.LONGBINARY) {
                    expressionValue = "null";
                }
            } else if (updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
                String secondField = getTextBoxSecondFieldValue(updataFieldType).toString();
                String firstField = getRealFieldExpress(comboBoxOperationField.getSelectedItem().toString(), dataset.getFieldInfos().get(comboBoxOperationField.getSelectedItem().toString()).getType(), updataFieldType, engineType);
                String method = comboBoxMethod.getSelectedItem().toString();
                if (!checkBoxInversion.isSelected()) {
                    expressionValue = convertToRealExpress(firstField, secondField, method, updataFieldType, engineType);
                } else {
                    expressionValue = convertToRealExpress(secondField, firstField, method, updataFieldType, engineType);
                }
            } else if (updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"))) {
                String firstField = getRealFieldExpress(comboBoxOperationField.getSelectedItem().toString(), dataset.getFieldInfos().get(comboBoxOperationField.getSelectedItem().toString()).getType(), updataFieldType, engineType);
                String secondField = getRealFieldExpress(comboBoxSecondField.getSelectedItem().toString(), dataset.getFieldInfos().get(comboBoxSecondField.getSelectedItem().toString()).getType(), updataFieldType, engineType);
                if (checkBoxInversion.isSelected()) {
                    expressionValue = convertToRealExpress(secondField, firstField, comboBoxMethod.getSelectedItem().toString(), updataFieldType, engineType);
                } else {
                    expressionValue = convertToRealExpress(firstField, secondField, comboBoxMethod.getSelectedItem().toString(), updataFieldType, engineType);
                }
            } else if (updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
                expressionValue = getExpressionValueMath(expressionValue);
            }
            buttonApply.setEnabled(true);
            textAreaOperationEQ.setText(expressionValue);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private String getExpressionValueMath(String expressionValue) {
        try {
            String fristField = comboBoxOperationField.getSelectedItem().toString();
            String method = comboBoxMethod.getSelectedItem().toString();
            //语法与数据库语法一致，Left(SMID,1)
            if (method == "Left" || method == "Right") {
                expressionValue = method + "(" + fristField + "," + textFieldX.getText() + ")";
            } else if (method == "Mid") {
                expressionValue = method + "(" + fristField + "," + textFieldX.getText() + "," + textFieldY.getText() + ")";
            } else if (method == "UCase" || method == "LCase" || method == "Trim") {
                expressionValue = method + "(" + fristField + ")";
            } else if (method == "TrimEnd" || method == "TrimStart") {
                expressionValue = method + "(" + method + "," + getCharArrayString(textFieldX.getText()) + ")";
            } else if (method == "ObjectCenterX" || method == "ObjectCenterY" || method == "ObjectLeft" ||
                    method == "ObjectRight" || method == "ObjectTop" || method == "ObjectBottom" || method == "ObjectWidth" || method == "ObjectHeight") {
                expressionValue = "Object." + method + "()";
            } else if (method == "LRemove" || method == "RRemove") {
                expressionValue = method + "(" + fristField + "," + textFieldX.getText() + ")";
            } else if (method == "Replace") {
                expressionValue = "Replace(" + fristField + ",\"" + textFieldX.getText() + "\",\"" + textFieldY.getText() + "\")";
            } else if (method == "Parse") {
                expressionValue = method + "(" + fristField + ",NumberStyles.HexNumber)";
            } else if (method == "Abs" || method == "Sqrt" || method == "Ln" || method == "Int") {
                expressionValue = method + "(" + fristField + ")";
            } else if (method == "Log") {
                expressionValue = method + "(" + fristField + "," + textFieldX.getText() + ")";
            } else if (method == "AddDays" || method == "AddHours" || method == "AddMilliseconds" || method == "AddSeconds"
                    || method == "AddMinutes" || method == "AddMonths" || method == "AddYears") {
                expressionValue = fristField + "." + method + "(" + textFieldX.getText() + ")";
            } else if (method == "DaysInMonth") {
                expressionValue = "DateTime.DaysInMonth(" + fristField + ".Year," + fristField + ".Month)";
            } else if (method == "Millisecond" || method == "Second" || method == "Minute" || method == "Hour" || method == "Day" ||
                    method == "Month" || method == "Year" || method == "Date" || method == "DayOfYear" || method == "DayOfWeek") {
                expressionValue = fristField + "." + method;
            } else if (method == "Now") {
                expressionValue = "DateTime.Now";
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return expressionValue;
    }

    private String getCharArrayString(String value) {
        String charArray = "";
        try {
            for (char x : value.toCharArray()) {
                charArray = charArray + "\'" + x + "\',";
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return charArray;
    }

    private Object getTextBoxSecondFieldValue(FieldType updataFieldType) {
        Object value = "";
        try {
            String updateMode = comboBoxSourceOfField.getSelectedItem().toString();
            String secondVaule = textFieldSecondField.getText();
            if (updataFieldType == FieldType.BYTE || updataFieldType == FieldType.SINGLE || updataFieldType == FieldType.INT16 ||
                    updataFieldType == FieldType.INT32 || updataFieldType == FieldType.INT64 || updataFieldType == FieldType.DOUBLE ||
                    updataFieldType == FieldType.BOOLEAN && updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
                try {
                    value = Convert.toDouble(secondVaule);
                } catch (Exception e) {
                    value = 0;
                }
            } else if (updataFieldType == FieldType.BOOLEAN) {
                try {
                    value = Convert.toBoolean(secondVaule);
                } catch (Exception e) {
                    value = null;
                }
            } else if (updataFieldType == FieldType.LONGBINARY) {
                Path path = Paths.get(fileChooser.getEditor().getText());
                try {
                    value = Files.readAllBytes(path);
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    value = null;
                }
            } else if (updataFieldType == FieldType.DATETIME) {
                try {
                    value = Convert.toDateTime(secondVaule);
                } catch (Exception e) {
                    value = null;
                }
            } else if ((updataFieldType == FieldType.TEXT || updataFieldType == FieldType.WTEXT || updataFieldType == FieldType.CHAR)) {
                value = "\'" + secondVaule + "\'";
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return value;
    }

    /**
     * 因为文本型字段的连接运算不能使用“+”运算符，需要使用“||”，所以获取一下正确的表达式
     *
     * @param param1      第一个参数
     * @param param2      第二个参数
     * @param operateType 运算符
     * @param fieldType   字段类型
     * @param engineType  数据源引擎类型
     * @return 正确的表达式
     */
    private String convertToRealExpress(String param1, String param2, String operateType, FieldType fieldType, EngineType engineType) {
        String expresstion = "";
        try {
            if ((fieldType == FieldType.TEXT || fieldType == FieldType.WTEXT || fieldType == FieldType.CHAR) &&
                    operateType.equals("+")) {
                if (engineType != EngineType.SQLPLUS) {
                    expresstion = param1 + " " + "||" + " " + param2;
                } else {
                    expresstion = param1 + " " + operateType + " " + param2;
                }
            } else {
                expresstion = param1 + " " + operateType + " " + param2;
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return expresstion;
    }

    /**
     * 当待更新字段类型与参与更新字段类型不一致时，可能需要转换
     *
     * @param fieldName       参与更新字段名称
     * @param fieldType       参与更新字段类型
     * @param updateFieldType 待更新字段类型
     * @param engineType      引擎类型
     * @return 参与更新字段表达式
     */
    private String getRealFieldExpress(String fieldName, FieldType fieldType, FieldType updateFieldType, EngineType engineType) {
        String result = fieldName;
        try {
            //目前发现SQL Server引擎比较特殊，文本的连接符可以直接用"+"
            //但是如果待更新字段为文本，而参与更新字段是数值时，需要用 Cast(fieldName as varchar)进行转换一下
            if (engineType == EngineType.SQLPLUS) {
                if (FieldTypeUtilities.isTextField(updateFieldType) && FieldTypeUtilities.isNumber(fieldType)) {
                    result = "cast(" + fieldName + " as varchar)";
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return result;
    }


    private void buttonApplyClicked() {
        CursorUtilities.setWaitCursor();
        try {
            String updateMode = comboBoxSourceOfField.getSelectedItem().toString();
            if (updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
                FieldInfo fieldInfo = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex());
                FieldType updataFieldType = fieldInfo.getType();
                Object value = getTextBoxSecondFieldValue(updataFieldType);
                value = formatValue(updataFieldType, value);
                //二进制和日期类型的数据，组件更新失败。
                //绕一下，改成我们以前的方法。
                if (updataFieldType == FieldType.LONGBINARY || updataFieldType == FieldType.DATETIME) {
                    updataModeSetValue(tabular.getRecordset(), value);
                } else {
                    //统一赋值时，字符型的如果有长度限制，可以做截断
                    String result = textAreaOperationEQ.getText();
                    if (updataFieldType == FieldType.CHAR || updataFieldType == FieldType.WTEXT || updataFieldType == FieldType.TEXT) {
                        //这里判断一下，如果以''开始和结束的字符串，表示用户传入的是一个字符串
                        //否则，可能是自定义表达式，自定义表达式就不做任何处理
                        if (isSurrondByquotationmarks(result)) {
                            result = result.substring(1, result.length() - 2);
                            if (fieldInfo.getMaxLength() < result.length()) {
                                result = result.substring(0, fieldInfo.getMaxLength() - 1);
                                textFieldSecondField.setText(result);
                                Application.getActiveApplication().getOutput()
                                        .output(MessageFormat.format(TabularViewProperties.getString("String_FormTabularUpdataColumn_FieldInfoDesValueIsOverlong"), comboBoxUpdateField.getSelectedItem().toString()));
                            }
                            //截断以后，再重新用''包装上
                            result = "\'" + result + "\'";
                        }
                    }
                    updateModeCustom(result);
                }
            } else if (!updateMode.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
                updateModeCustom(textAreaOperationEQ.getText());
            } else {
                updateModeMath();
            }
            buttonApply.setEnabled(false);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        CursorUtilities.setDefaultCursor();
	    ToolbarUIUtilities.updataToolbarsState();
    }

    private void updateModeMath() {
        boolean isUpdateAll = radioButtonUpdateColumn.isSelected();
        FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
        if (isUpdateAll) {
            // 更新选中列
            int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
            for (int i = 0; i < selectRows.length; i++) {
                selectRows[i] = i;
            }
            resetFieldForModeMath(fieldType, selectRows);
        } else {
            resetFieldForModeMath(fieldType, tabular.getSelectedRows());
        }
    }

    private void resetFieldForModeMath(FieldType fieldType, int[] selectRows) {
        if (FieldTypeUtilities.isNumber(fieldType) || fieldType.equals(FieldType.BYTE)) {
            // 整型
            updateFieldModeMathNumber(fieldType, selectRows);
        } else if (FieldTypeUtilities.isTextField(fieldType) || fieldType.equals(FieldType.CHAR)) {
            // 字符串型
            updateModeMathText(fieldType, selectRows);
        } else if (fieldType.equals(FieldType.DATETIME)) {
            // 日期型
            updateModeMathDate(selectRows);
        }
    }

    private void updataModeSetValue(Recordset recordset, Object value) {
        try {
            // 判断当前浏览记录数的判断需要去掉，现在查询，查看属性等各种情况都支持修改
            if (radioButtonUpdateColumn.isSelected()) {
                //属性表历史记录添加
                int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
                for (int i = 0; i < selectRows.length; i++) {
                    selectRows[i] = i;
                }
                updateModeSet(selectRows, value);
            } else {
                updateModeSet(tabular.getSelectedRows(), value);
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            recordset.getBatch().update();
        }

    }

    private void updateModeSet(int[] selectRows, Object value) {
        String updateField = comboBoxUpdateField.getSelectedItem().toString();
        Recordset recordset = tabular.getRecordset();
        recordset.getBatch().setMaxRecordCount(1024);
        recordset.getBatch().begin();
        TabularEditHistory tabularEditHistory = new TabularEditHistory();
        for (int i = 0; i < selectRows.length; i++) {
            int smId = tabular.getSmId(selectRows[i]);
            EditHistoryBean editHistoryBean = new EditHistoryBean();
            editHistoryBean.setSmId(smId);
            editHistoryBean.setBeforeValue(recordset.getFieldValue(updateField));
            editHistoryBean.setFieldName(updateField);
            if (null != value) {
                editHistoryBean.setAfterValue(value);
                recordset.setFieldValue(updateField, value);
                tabularEditHistory.addEditHistoryBean(editHistoryBean);
            }
        }
        recordset.getBatch().update();
        if (tabularEditHistory.getCount() > 0) {
            tabular.getEditHistoryManager().addEditHistory(tabularEditHistory);
        }
        // 重新查询避免操作后记录集清除的异常
        refreshTabular();
    }

    /// <summary>
    /// 判断当前表达式是否以‘'’开始和结束
    /// </summary>
    /// <param name="express">表达式</param>
    /// <returns>是/否</returns>
    private Boolean isSurrondByquotationmarks(String express) {
        Boolean result = false;
        try {
            if (express.startsWith("\\'") && express.endsWith("\\'")) {
                result = true;
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return result;
    }

    private Object formatValue(FieldType fieldType, Object value) {
        try {
            if (fieldType == FieldType.BYTE) {
                try {
                    // 字节型
                    if (StringUtilities.isNullOrEmptyString(value)) {
                        value = (byte) 0;
                    } else if (Convert.toInteger(value) < 128 && Convert.toInteger(value) >= 0) {
                        value = (byte) Convert.toInteger(value);
                    } else if (Convert.toInteger(value) >= 128 || Convert.toInteger(value) < 0) {
                        value = (byte) 0;
                    }
                } catch (Exception e) {
                    value = null;
                }
            } else if (fieldType == FieldType.SINGLE || fieldType == FieldType.DOUBLE) {
                value = Convert.toDouble(value);
            } else if (UpdateColumnUtilties.isIntegerType(fieldType)) {
                value = Convert.toInteger(value);
            } else if (fieldType == FieldType.BOOLEAN) {
                value = Convert.toBoolean(value);
            } else if (fieldType == FieldType.CHAR || fieldType == FieldType.WTEXT || fieldType == FieldType.TEXT) {
                if (StringUtilities.isNullOrEmptyString(value)) {
                    value = "";
                } else {
                    value = value.toString();
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return value;
    }

    private void updateModeCustom(String expression) {
        try {
            String tarExpress = comboBoxUpdateField.getSelectedItem().toString();
            DatasetVector datasetVector = tabular.getRecordset().getDataset();
            if (StringUtilities.isNullOrEmpty(expression)) {
                expression = "''";
            }
            String attributeFilter = "";
            String fieldName = comboBoxUpdateField.getSelectedItem().toString();

            attributeFilter = getAttributeFilter();
            //获取原始值
            List<Object> srcValues = getFieldValues(fieldName, attributeFilter);
            Boolean isSuccess = datasetVector.updateField(tarExpress, expression, attributeFilter);
            // 重新查询避免操作后记录集清除的异常
            if (isSuccess) {
                refreshTabular();

                //获取更新后的值
                //属性表撤销更新
                List<Object> tarValues = getFieldValues(fieldName, attributeFilter);
                if (srcValues.size() == tarValues.size()) {
                    int size = srcValues.size();
                    int[] selectRows = tabular.getSelectedRows();
                    TabularEditHistory tabularEditHistory = new TabularEditHistory();
                    for (int i = 0; i < size; i++) {
                        int smId = tabular.getSmId(selectRows[i]);
                        EditHistoryBean editHistoryBean = new EditHistoryBean();
                        editHistoryBean.setSmId(smId);
                        editHistoryBean.setBeforeValue(srcValues.get(i));
                        editHistoryBean.setFieldName(fieldName);
                        editHistoryBean.setAfterValue(tarValues.get(i));
                        tabularEditHistory.addEditHistoryBean(editHistoryBean);
                    }
                    if (tabularEditHistory.getCount() > 0) {
                        tabular.getEditHistoryManager().addEditHistory(tabularEditHistory);
                    }
                }
            } else {
                Application.getActiveApplication().getOutput().output(TabularViewProperties.getString("String_UpdateColumnFailed"));
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private List<Object> getFieldValues(String fieldName, String attributeFilter) {
        List<Object> result = new ArrayList();
        try {
            Recordset recordset = getRecordset(tabular.getRecordset().getDataset(), attributeFilter);
            recordset.moveFirst();
            while (!recordset.isEOF()) {
                result.add(recordset.getFieldValue(fieldName));
                recordset.moveNext();
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return result;
    }

    private Recordset getRecordset(DatasetVector datasetVector, String attributeFilter) {
        Recordset recordset = null;
        try {
            if (radioButtonUpdateSelect.isSelected()) {
                QueryParameter param = new QueryParameter();
                param.setHasGeometry(false);
                param.setCursorType(CursorType.STATIC);
                param.setAttributeFilter(attributeFilter);
                recordset = datasetVector.query(param);
            } else {
                recordset = datasetVector.getRecordset(false, CursorType.STATIC);
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return recordset;
    }

    private String getAttributeFilter() {
        String result = "SmID in (";

        if (radioButtonUpdateColumn.isSelected()) {
            int tableCount = tabular.getRowCount();
            for (int i = 0; i < tableCount; i++) {
                result += tabular.getSmId(i) + ",";
            }
        } else {
            //获取到当前展示的Smid 进行更新
            int[] selectRows = tabular.getSelectedRows();
            int length = selectRows.length;
            for (int i = 0; i < length; i++) {
                result += tabular.getSmId(selectRows[i]) + ",";
            }
        }
        result = result.substring(0, result.length() - 1) + ")";
        return result;
    }

    private void updateModeMathDate(int[] selectRows) {
        String method = comboBoxMethod.getSelectedItem().toString();
        String updateField = comboBoxUpdateField.getSelectedItem().toString();
        String operationField = comboBoxOperationField.getSelectedItem().toString();
        Recordset recordset = tabular.getRecordset();
        recordset.getBatch().setMaxRecordCount(1024);
        recordset.getBatch().begin();
        Object newValue = null;
        TabularEditHistory tabularEditHistory = new TabularEditHistory();
        for (int i = 0; i < selectRows.length; i++) {
            int smId = tabular.getSmId(selectRows[i]);
            EditHistoryBean editHistoryBean = new EditHistoryBean();
            editHistoryBean.setSmId(smId);
            editHistoryBean.setBeforeValue(recordset.getFieldValue(updateField));
            editHistoryBean.setFieldName(updateField);
            newValue = UpdateColumnUtilties.getUpdataModeMathValueDataTime(recordset.getFieldValue(operationField), recordset.getFieldValue(updateField),
                    method, textFieldX.getText());
            if (null != newValue) {
                editHistoryBean.setAfterValue(newValue);
                recordset.setFieldValue(updateField, newValue);
                tabularEditHistory.addEditHistoryBean(editHistoryBean);
            }
        }
        recordset.getBatch().update();
        if (tabularEditHistory.getCount() > 0) {
            tabular.getEditHistoryManager().addEditHistory(tabularEditHistory);
        }
        // 重新查询避免操作后记录集清除的异常
        refreshTabular();
    }

    private void updateModeMathText(FieldType fieldType, int[] selectRows) {
        String fristField = comboBoxOperationField.getSelectedItem().toString();// 运算字段
        String updateField = comboBoxUpdateField.getSelectedItem().toString();// 更新字段
        String method = comboBoxMethod.getSelectedItem().toString();
        Recordset recordset = tabular.getRecordset();
        boolean beyoundMaxLength = false;
        recordset.getBatch().setMaxRecordCount(1024);
        recordset.getBatch().begin();
        Object newValue = null;
        TabularEditHistory tabularEditHistory = new TabularEditHistory();
        for (int i = 0; i < selectRows.length; i++) {
            int smId = tabular.getSmId(selectRows[i]);
            EditHistoryBean editHistoryBean = new EditHistoryBean();
            editHistoryBean.setSmId(smId);
            editHistoryBean.setBeforeValue(recordset.getFieldValue(updateField));
            editHistoryBean.setFieldName(updateField);
            if (UpdateColumnUtilties.isObjectConnect(method)) {
                newValue = UpdateColumnUtilties.getObjectInfo(method, recordset.getGeometry(), fieldType);
            } else if (UpdateColumnUtilties.isDaysInfo(method)) {
                newValue = UpdateColumnUtilties.getDateInfo(method, (Date) recordset.getFieldValue(comboBoxOperationField.getSelectedItem().toString()));
                newValue = newValue.toString();
            } else {
                newValue = UpdateColumnUtilties.getUpdataModeMathValueText(method, recordset.getFieldValue(fristField), textFieldX.getText(),
                        textFieldY.getText());
            }
            if (newValue.toString().length() > recordset.getFieldInfos().get(updateField).getMaxLength()) {
                beyoundMaxLength = true;
                newValue = newValue.toString().substring(0, recordset.getFieldInfos().get(updateField).getMaxLength());
            }
            editHistoryBean.setAfterValue(newValue);
            recordset.setFieldValue(updateField, newValue);
            tabularEditHistory.addEditHistoryBean(editHistoryBean);
        }
        recordset.getBatch().update();
        if (beyoundMaxLength) {
            Application.getActiveApplication().getOutput()
                    .output(MessageFormat.format(TabularViewProperties.getString("String_FormTabularUpdataColumn_FieldInfoDesValueIsOverlong"), updateField));
        }
        tabular.getEditHistoryManager().addEditHistory(tabularEditHistory);
        // 重新查询避免操作后记录集清除的异常
        refreshTabular();
    }

    private void updateFieldModeMathNumber(FieldType fieldType, int[] selectRows) {
        String fristField = comboBoxOperationField.getSelectedItem().toString();// 运算字段
        String updateField = comboBoxUpdateField.getSelectedItem().toString();// 更新字段
        String method = comboBoxMethod.getSelectedItem().toString();
        Recordset recordset = tabular.getRecordset();
        recordset.getBatch().setMaxRecordCount(1024);
        recordset.getBatch().begin();
        Object newValue = null;
        TabularEditHistory tabularEditHistory = new TabularEditHistory();
        for (int i = 0; i < selectRows.length; i++) {
            int smId = tabular.getSmId(selectRows[i]);
            EditHistoryBean editHistoryBean = new EditHistoryBean();
            editHistoryBean.setSmId(smId);
            editHistoryBean.setBeforeValue(recordset.getFieldValue(updateField));
            editHistoryBean.setFieldName(updateField);
            if (UpdateColumnUtilties.isMathInfo(method)) {
                String textFieldXInfo = "";
                if (null != textFieldX.getText()) {
                    textFieldXInfo = textFieldX.getText();
                }
                String fieldStr = "";
                if (null != recordset.getFieldValue(fristField)) {
                    fieldStr = recordset.getFieldValue(fristField).toString();
                }
                newValue = UpdateColumnUtilties.getMathInfo(method, fieldStr, textFieldXInfo, fieldType);
            } else if (UpdateColumnUtilties.isObjectConnect(method)) {
                newValue = UpdateColumnUtilties.getObjectInfo(method, recordset.getGeometry(), fieldType);
            } else if (UpdateColumnUtilties.isDaysInfo(method)) {
                newValue = UpdateColumnUtilties.getDateInfo(method, (Date) recordset.getFieldValue(comboBoxOperationField.getSelectedItem().toString()));
            }
            editHistoryBean.setAfterValue(newValue);
            recordset.setFieldValue(updateField, newValue);
            tabularEditHistory.addEditHistoryBean(editHistoryBean);
        }
        recordset.getBatch().update();
        tabular.getEditHistoryManager().addEditHistory(tabularEditHistory);
        // 重新查询避免操作后记录集清除的异常
        refreshTabular();
    }

    private void refreshTabular() {
        tabular.getjTableTabular().repaint(); // 重新查询会导致sql查询后的数据集更新列后查询条件丢失

    }

    private void fileChooserClicked() {
        if (!SmFileChoose.isModuleExist("GetLongBinary")) {
            SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), "", "GetLongBinary", "OpenOne");
        }
        SmFileChoose fileChooserc = new SmFileChoose("GetLongBinary");
        fileChooserc.setAcceptAllFileFilterUsed(true);
        int state = fileChooserc.showDefaultDialog();
        if (state == JFileChooser.APPROVE_OPTION && !StringUtilities.isNullOrEmpty(fileChooserc.getFilePath())) {
            fileChooser.getEditor().setText(fileChooserc.getFilePath());
            buttonApply.setEnabled(true);
        }
    }

    private void comboBoxSecondFieldChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED && null != textAreaOperationEQ) {
            if (null != tabular.getRecordset().getFieldInfos().get(comboBoxSecondField.getSelectedItem().toString())) {
                labelSecondFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
                        .get(comboBoxSecondField.getSelectedItem().toString()).getType()));
            }
            buttonApply.setEnabled(true);
            updataExpressionValue();
        }
    }
}
