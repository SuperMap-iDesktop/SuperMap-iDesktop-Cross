package com.supermap.desktop.process.meta.metaProcessImplements.DatasetFieldWithDatasource;

import com.supermap.data.*;
import com.supermap.desktop.implement.SmComboBox;
import com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField.StatisticsFieldTableModel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by Chen on 2017/6/26 0026.
 */
public class DatasetFieldWithDatasourcePanel extends JPanel {
    private DatasetFieldWithDatasourceTableModel tableModel;
    private Workspace workspace;

    /*component*/
    private JScrollPane scrollPane;
    private SmSortTable table;
    private JToolBar toolBar;
    private SmButton buttonAdd;
    private SmButton buttonSelectAll;
    private SmButton buttonSelectInvert;
    private SmButton buttonDelete;

    public DatasetFieldWithDatasourcePanel(Workspace workspace) {
        super();
        this.workspace = workspace;
        init();
    }

    private void init() {
        initComponent();
        initResource();
        initLayout();
        registerListener();
        checkState();
    }

    private void initComponent() {
        scrollPane = new JScrollPane();
        table = new SmSortTable();
        toolBar = new JToolBar();
        buttonAdd = new SmButton();
        buttonSelectAll = new SmButton();
        buttonSelectInvert = new SmButton();
        buttonDelete = new SmButton();

        initTable();
    }

    private void initResource() {
        buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
        buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
        buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
        buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));

        buttonAdd.setToolTipText(CommonProperties.getString(CommonProperties.Add));
        buttonSelectAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
        buttonSelectInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));
        buttonDelete.setToolTipText(CommonProperties.getString(CommonProperties.Delete));
    }

    private void initLayout() {
        initToolBar();

        this.setLayout(new GridBagLayout());
        this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
        this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
    }

    private void initToolBar() {
        toolBar.setFloatable(false);
        toolBar.add(buttonAdd);
        toolBar.addSeparator();
        toolBar.add(buttonSelectAll);
        toolBar.add(buttonSelectInvert);
        toolBar.addSeparator();
        toolBar.add(buttonDelete);
    }

    private void registerListener() {

    }

    private void checkState() {

    }

    private void initTable() {
        scrollPane.setViewportView(table);
        table.setModel(tableModel);
    }

    /**
     * 添加弹窗
     */
    private class DialogAdd extends SmDialog {
        private Workspace workspace;
        private ArrayList<Dataset> datasets;

        private JScrollPane scrollPane;
        private JToolBar toolBar;
        private SmSortTable table;
        private JLabel label;
        private JComboBox comboBoxDatasource;
        private SmButton buttonSelectAll;
        private SmButton buttonSelectInvert;
        private SmButton buttonConfirm;
        private SmButton buttonCancel;
        private DatasetWithTypeTableModel tableModel;

        public DialogAdd(Workspace workspace) {
            this.workspace = workspace;

            this.initComponent();
            this.initLayout();
            this.registerListener();
            this.pack();
            this.setLocationRelativeTo(null);
            buttonConfirm.requestFocus();
        }

        private void registerListener() {
            this.buttonSelectAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table.setRowSelectionInterval(0, table.getRowCount() - 1);
                }
            });
            this.buttonSelectInvert.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableUtilities.invertSelection(table);
                }
            });
            this.buttonConfirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialogResult = DialogResult.OK;
                    int[] selectedModelRows = table.getSelectedModelRows();
                    DatasetWithTypeTableModel model = (DatasetWithTypeTableModel) table.getModel();
                    datasets.clear();
                    for (int selectedModelRow : selectedModelRows) {
                        datasets.add(model.getRow(selectedModelRow));
                    }
                    dispose();
                }
            });
            buttonCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialogResult = DialogResult.CANCEL;
                    dispose();
                }
            });
            comboBoxDatasource.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("selectedItem")) {
                        tableModel.setDatasets(((Datasource)comboBoxDatasource.getSelectedItem()));
                    }
                }
            });
        }

        private void initLayout() {
            initToolBar();

            JPanel panelButton = new JPanel();
            panelButton.setLayout(new GridBagLayout());
            panelButton.add(this.buttonConfirm, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST));
            panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));

            this.setLayout(new GridBagLayout());
            this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
            this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
            this.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,20,10,01));
        }

        private void initComponent() {
            scrollPane = new JScrollPane();
            toolBar = new JToolBar();
            table = new SmSortTable();
            label.setText(CommonProperties.getString("String_Label_Datasource"));

            Datasources datasources = workspace.getDatasources();
            Datasource[] datasources1 = new Datasource[datasources.getCount()];
            for (int i = 0; i < datasources.getCount(); i++) {
                datasources1[i] = datasources.get(i);
            }

            comboBoxDatasource = new JComboBox(datasources1);
            buttonSelectAll = new SmButton();
            buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
            buttonSelectInvert = new SmButton();
            buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
            buttonConfirm = new SmButton(CommonProperties.getString(CommonProperties.OK));
            buttonCancel = new SmButton(CommonProperties.getString(CommonProperties.Cancel));

            initTable();
            checkState();
        }

        private void initToolBar() {
            toolBar.setFloatable(false);
            toolBar.add(label);
            toolBar.add(comboBoxDatasource);
            toolBar.addSeparator();
            toolBar.add(buttonSelectAll);
            toolBar.add(buttonSelectInvert);
        }

        private void initTable() {
            scrollPane.setViewportView(table);

            tableModel = new DatasetWithTypeTableModel((Datasource) comboBoxDatasource.getSelectedItem());
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            table.setModel(tableModel);
        }

        private void checkState() {
            this.buttonSelectAll.setEnabled(table.getRowCount() > 0);
            this.buttonSelectInvert.setEnabled(table.getRowCount() > 0);
        }
    }
}
