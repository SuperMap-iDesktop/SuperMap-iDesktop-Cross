package com.supermap.desktop.ui;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by xie on 2016/8/31.
 * 字段选择器
 */
public class FieldsSetDialog extends SmDialog {
    //    private JLabel labelSourceFields;//数据源的字段
    private JTable tableSourceFields;
    //    private JLabel labelOverlayAnalystFields;//叠加分析的字段
    private JTable tableOverlayAnalystFields;
    private JButton buttonSourceFieldsSelectAll;//全选
    private JButton buttonSourceFieldsSelectReverse;//反选
    private JButton buttonOverlayAnalystSelectAll;
    private JButton buttonOverlayAnalystSelectReverse;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane scrollpaneSourceFields;
    private JScrollPane scrollpaneOverlayAnalystFields;
    private final Object[] tableTitle = {"", CommonProperties.getString("String_Field_Caption")};
    private String[] sourceFields;
    private String[] overlayAnalystFields;
    private static final int TABLE_COLUMN_CHECKABLE = 0;
    private static final int TABLE_COLUMN_CAPTION = 1;
    private static final Color COLOR_SYSTEM_SELECTED = new Color(185, 214, 244);
    private static final Color COLOR_SYSTEM_NOT_SELECTED = new Color(230, 230, 230);

    private DatasetVector sourceDataset, overlayAnalystDataset;
    private ActionListener buttonSourceFieldsSelectAllListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectAll(tableSourceFields);
        }
    };
    private ActionListener buttonSourceFieldsSelectReverseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectReverse(tableSourceFields);
        }
    };
    private ActionListener buttonOverlayAnalystSelectAllListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectAll(tableOverlayAnalystFields);
        }
    };
    private ActionListener buttonOverlayAnalystSelectReverseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectReverse(tableOverlayAnalystFields);
        }
    };
    private ActionListener buttonOKListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> sourceFieldList = new ArrayList<String>();
            ArrayList<String> overlayAnaylstList = new ArrayList<String>();
            for (int i = 0; i < tableSourceFields.getRowCount(); i++) {
                for (int j = 0; j < sourceDataset.getFieldInfos().getCount(); j++) {
                    if ((Boolean) tableSourceFields.getValueAt(i, TABLE_COLUMN_CHECKABLE) && sourceDataset.getFieldInfos().get(j).getCaption().equals(tableSourceFields.getValueAt(i, TABLE_COLUMN_CAPTION))) {
                        sourceFieldList.add(sourceDataset.getFieldInfos().get(j).getName());
                    }
                }
            }

            for (int i = 0; i < tableOverlayAnalystFields.getRowCount(); i++) {
                for (int j = 0; j < overlayAnalystDataset.getFieldInfos().getCount(); j++) {
                    if ((Boolean) tableOverlayAnalystFields.getValueAt(i, TABLE_COLUMN_CHECKABLE) && overlayAnalystDataset.getFieldInfos().get(j).getCaption().equals(tableOverlayAnalystFields.getValueAt(i, TABLE_COLUMN_CAPTION))) {
                        overlayAnaylstList.add(overlayAnalystDataset.getFieldInfos().get(j).getName());
                    }
                }
            }
            sourceFields = sourceFieldList.toArray(new String[sourceFieldList.size()]);
            overlayAnalystFields = overlayAnaylstList.toArray(new String[overlayAnaylstList.size()]);
            dialogResult = DialogResult.OK;
            FieldsSetDialog.this.dispose();
        }
    };
    private ActionListener buttonCancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            disposeInfo();
        }
    };

    public FieldsSetDialog(DatasetVector sourceDataset, DatasetVector overlayAnalystDataset) {
        super();
        this.sourceDataset = sourceDataset;
        this.overlayAnalystDataset = overlayAnalystDataset;
    }

    public DialogResult showDialog() {
        initComponents();
        initLayout();
        initResources();
        registEvents();
        setSize(500, 340);
        setMinimumSize(new Dimension(500, 340));
        setLocationRelativeTo(null);
        setVisible(true);
        return dialogResult;
    }

    private void selectAll(JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(new Boolean(true), i, TABLE_COLUMN_CHECKABLE);
        }
    }

    private void selectReverse(JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(!((Boolean) table.getValueAt(i, TABLE_COLUMN_CHECKABLE)), i, TABLE_COLUMN_CHECKABLE);
        }
    }

    private void registEvents() {
        removeEvents();
        this.buttonSourceFieldsSelectAll.addActionListener(this.buttonSourceFieldsSelectAllListener);
        this.buttonSourceFieldsSelectReverse.addActionListener(this.buttonSourceFieldsSelectReverseListener);
        this.buttonOverlayAnalystSelectAll.addActionListener(this.buttonOverlayAnalystSelectAllListener);
        this.buttonOverlayAnalystSelectReverse.addActionListener(this.buttonOverlayAnalystSelectReverseListener);
        this.buttonOK.addActionListener(this.buttonOKListener);
        this.buttonCancel.addActionListener(this.buttonCancelListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeInfo();
            }
        });
    }

    private void disposeInfo() {
        removeEvents();
        FieldsSetDialog.this.dispose();
    }

    private void removeEvents() {
        this.buttonSourceFieldsSelectAll.removeActionListener(this.buttonSourceFieldsSelectAllListener);
        this.buttonSourceFieldsSelectReverse.removeActionListener(this.buttonSourceFieldsSelectReverseListener);
        this.buttonOverlayAnalystSelectAll.removeActionListener(this.buttonOverlayAnalystSelectAllListener);
        this.buttonOverlayAnalystSelectReverse.removeActionListener(this.buttonOverlayAnalystSelectReverseListener);
        this.buttonOK.removeActionListener(this.buttonOKListener);
        this.buttonCancel.removeActionListener(this.buttonCancelListener);
    }

    private void initLayout() {
        JPanel panelSourceFields = new JPanel();
        JPanel panelOverlayAnalystFields = new JPanel();
        scrollpaneSourceFields = new JScrollPane();
        scrollpaneOverlayAnalystFields = new JScrollPane();
        panelSourceFields.setLayout(new GridBagLayout());
        panelSourceFields.add(scrollpaneSourceFields, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelSourceFields.add(this.buttonSourceFieldsSelectAll, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        panelSourceFields.add(this.buttonSourceFieldsSelectReverse, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        scrollpaneSourceFields.setViewportView(tableSourceFields);
        panelOverlayAnalystFields.setLayout(new GridBagLayout());
        panelOverlayAnalystFields.add(scrollpaneOverlayAnalystFields, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelOverlayAnalystFields.add(this.buttonOverlayAnalystSelectAll, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        panelOverlayAnalystFields.add(this.buttonOverlayAnalystSelectReverse, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        scrollpaneOverlayAnalystFields.setViewportView(tableOverlayAnalystFields);

        panelSourceFields.setBorder(new TitledBorder(ControlsProperties.getString("String_Label_SourceDatasetFields")));
        panelOverlayAnalystFields.setBorder(new TitledBorder(ControlsProperties.getString("String_Label_OverlayDatasetFields")));
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        this.setLayout(new GridBagLayout());
        this.add(panelSourceFields, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(panelOverlayAnalystFields, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(panelButton, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
    }

    private void initResources() {
        this.buttonSourceFieldsSelectAll.setText(ControlsProperties.getString("String_SelectAll"));
        this.buttonSourceFieldsSelectReverse.setText(ControlsProperties.getString("String_SelectReverse"));
        this.buttonOverlayAnalystSelectAll.setText(ControlsProperties.getString("String_SelectAll"));
        this.buttonOverlayAnalystSelectReverse.setText(ControlsProperties.getString("String_SelectReverse"));
        this.setTitle(ControlsProperties.getString("String_Form_FieldsSetting"));
    }

    private void initComponents() {
        this.tableSourceFields = new JTable();
        initTable(tableSourceFields, sourceDataset);
        this.tableOverlayAnalystFields = new JTable();
        initTable(tableOverlayAnalystFields, overlayAnalystDataset);
        this.buttonSourceFieldsSelectAll = new JButton();
        this.buttonSourceFieldsSelectReverse = new JButton();
        this.buttonOverlayAnalystSelectAll = new JButton();
        this.buttonOverlayAnalystSelectReverse = new JButton();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
    }

    private void initTable(JTable table, DatasetVector dataset) {
        int count = 0;
        for (int i = 0; i < dataset.getFieldInfos().getCount(); i++) {
            if (!dataset.getFieldInfos().get(i).isSystemField()) {
                count++;
            }
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[count][2], tableTitle) {


            public Class getColumnClass(int column) {
                if (TABLE_COLUMN_CHECKABLE == column) {
                    return Boolean.class;
                }
                return tableTitle[column].getClass();
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (TABLE_COLUMN_CHECKABLE == columnIndex) {
                    return true;
                }
                return false;
            }
        };
        table.setModel(defaultTableModel);
        int length = 0;
        for (int i = 0; i < dataset.getFieldInfos().getCount(); i++) {
            if (!dataset.getFieldInfos().get(i).isSystemField()) {
                table.setValueAt(false, length, TABLE_COLUMN_CHECKABLE);
                table.setValueAt(dataset.getFieldInfos().get(i).getCaption(), length, TABLE_COLUMN_CAPTION);
                length++;
            }
        }
        table.getColumn(table.getModel().getColumnName(TABLE_COLUMN_CHECKABLE)).setMaxWidth(40);
    }

    public String[] getSourceFields() {
        return sourceFields;
    }

    public String[] getOverlayAnalystFields() {
        return overlayAnalystFields;
    }
}
