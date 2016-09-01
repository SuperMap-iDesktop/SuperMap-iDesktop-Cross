package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by xie on 2016/8/31.
 */
public class FieldsSetDialog extends SmDialog {
    private JLabel labelSourceFields;//数据源的字段
    private JTable tableSourceFields;
    private JLabel labelOverlayAnalystFields;//叠加分析的字段
    private JTable tableOverlayAnalystFields;
    private JButton buttonSourceFieldsSelectAll;//全选
    private JButton buttonSourceFieldsSelectReverse;//反选
    private JButton buttonOverlayAnalystSelectAll;
    private JButton buttonOverlayAnalystSelectReverse;
    private JButton buttonOK;
    private JButton buttonCancel;
    private final String[] tableTitle = {ControlsProperties.getString("String_ColumnHeader_FieldIndexes"), CommonProperties.getString("String_FieldName")};

    private DatasetVector sourceDataset, overlayAnalystDataset;

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
        setSize(380, 340);
        setLocationRelativeTo(null);
        setVisible(true);
        return dialogResult;
    }

    private void removeSelection(JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            table.removeColumnSelectionInterval(i, i);
        }
    }

    private void selectAll(JTable table) {
        removeSelection(table);
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(new Boolean(true), i, 0);
        }
    }

    private void selectReverse(JTable table) {

    }

    private void registEvents() {
        this.buttonSourceFieldsSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        this.buttonSourceFieldsSelectReverse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelection(tableSourceFields);
                for (int i = 0; i < tableSourceFields.getRowCount(); i++) {
                    tableSourceFields.setValueAt(!((Boolean) tableSourceFields.getValueAt(i, 0)), i, 0);
                }
            }
        });
        this.buttonOverlayAnalystSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelection(tableOverlayAnalystFields);
                for (int i = 0; i < tableOverlayAnalystFields.getRowCount(); i++) {
                    tableOverlayAnalystFields.setValueAt(new Boolean(true), i, 0);
                    tableOverlayAnalystFields.updateUI();
                }
            }
        });
        this.buttonOverlayAnalystSelectReverse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelection(tableOverlayAnalystFields);
                for (int i = 0; i < tableOverlayAnalystFields.getRowCount(); i++) {
                    tableOverlayAnalystFields.setValueAt(!((Boolean) tableOverlayAnalystFields.getValueAt(i, 0)), i, 0);
                    tableOverlayAnalystFields.updateUI();
                }
            }
        });
    }

    private void initLayout() {
        JPanel panelSourceFields = new JPanel();
        JScrollPane scrollpaneSourceFields = new JScrollPane();
        JPanel panelOverlayAnalystFields = new JPanel();
        JScrollPane scrollpaneOverlayAnalystFields = new JScrollPane();
        panelSourceFields.setLayout(new GridBagLayout());
        panelSourceFields.add(this.labelSourceFields, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSourceFields.add(scrollpaneSourceFields, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelSourceFields.add(this.buttonSourceFieldsSelectAll, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        panelSourceFields.add(this.buttonSourceFieldsSelectReverse, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        scrollpaneSourceFields.setViewportView(tableSourceFields);
        panelOverlayAnalystFields.setLayout(new GridBagLayout());
        panelOverlayAnalystFields.add(this.labelOverlayAnalystFields, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelOverlayAnalystFields.add(scrollpaneOverlayAnalystFields, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelOverlayAnalystFields.add(this.buttonOverlayAnalystSelectAll, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        panelOverlayAnalystFields.add(this.buttonOverlayAnalystSelectReverse, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        scrollpaneOverlayAnalystFields.setViewportView(tableOverlayAnalystFields);

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
        this.labelSourceFields.setText(SpatialAnalystProperties.getString("String_Label_SourceDatasetFields"));
        this.labelOverlayAnalystFields.setText(SpatialAnalystProperties.getString("String_Label_OverlayDatasetFields"));
        this.buttonSourceFieldsSelectAll.setText(ControlsProperties.getString("String_SelectAll"));
        this.buttonSourceFieldsSelectReverse.setText(ControlsProperties.getString("String_SelectReverse"));
        this.buttonOverlayAnalystSelectAll.setText(ControlsProperties.getString("String_SelectAll"));
        this.buttonOverlayAnalystSelectReverse.setText(ControlsProperties.getString("String_SelectReverse"));
        this.setTitle(SpatialAnalystProperties.getString("String_Form_FieldsSetting"));
    }

    private void initComponents() {
        this.labelSourceFields = new JLabel();
        this.tableSourceFields = new JTable();
        initTable(tableSourceFields, sourceDataset);
        this.labelOverlayAnalystFields = new JLabel();
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
        DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
        defaultTableModel.setColumnIdentifiers(tableTitle);
        for (int i = 0; i < dataset.getFieldInfos().getCount(); i++) {
            if (!dataset.getFieldInfos().get(i).isSystemField()) {
                Object[] sourceTableInfo = {new Boolean(false), dataset.getFieldInfos().get(i).getName()};
                defaultTableModel.addRow(sourceTableInfo);
            }
        }
        table.getColumn(ControlsProperties.getString("String_ColumnHeader_FieldIndexes")).setCellEditor(new CheckBoxCellEditor(dataset));
        table.getColumn(ControlsProperties.getString("String_ColumnHeader_FieldIndexes")).setCellRenderer(new CheckBoxRenderer(dataset));
        table.getColumn(CommonProperties.getString("String_FieldName")).setCellRenderer(new TooltipRender());
    }

    class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JCheckBox checkBox;
        private DatasetVector datasetVector;

        public CheckBoxCellEditor(DatasetVector datasetVector) {
            checkBox = new JCheckBox();
            this.datasetVector = datasetVector;
            checkBox.setHorizontalAlignment(SwingConstants.LEFT);
        }

        @Override
        public Object getCellEditorValue() {
            return Boolean.valueOf(checkBox.isSelected());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            checkBox.setSelected(((Boolean) value).booleanValue());
            checkBox.updateUI();
            for (int i = 0; i < datasetVector.getFieldInfos().getCount(); i++) {
                if (this.datasetVector.getFieldInfos().get(i).getName().equals(table.getValueAt(row, 1).toString())) {
                    checkBox.setText(String.valueOf(i + 1));
                }
            }
            return checkBox;
        }
    }

    class TooltipRender extends JLabel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setToolTipText(value.toString());
            return this;
        }
    }

    class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        private static final long serialVersionUID = 1L;
        Border border = new EmptyBorder(1, 2, 1, 2);
        private DatasetVector datasetVector;

        public CheckBoxRenderer(DatasetVector datasetVector) {
            super();
            setOpaque(true);
            this.datasetVector = datasetVector;
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                setSelected(((Boolean) value).booleanValue());
                this.updateUI();
                for (int i = 0; i < datasetVector.getFieldInfos().getCount(); i++) {
                    if (this.datasetVector.getFieldInfos().get(i).getName().equals(table.getValueAt(row, 1).toString())) {
                        setText(String.valueOf(i + 1));
                    }
                }
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            return this;
        }
    }
}
