package com.supermap.desktop.ui;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.smTables.TableFactory;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldName;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

    private TableFieldName tableFieldNameSourceFields;
    private TableFieldName tableFieldNameOverlayAnalystFields;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane scrollpaneSourceFields;
    private JScrollPane scrollpaneOverlayAnalystFields;
    private String[] sourceFields;
    private String[] overlayAnalystFields;

    private DatasetVector sourceDataset, overlayAnalystDataset;
    private ActionListener buttonOKListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> sourceFieldList = tableFieldNameSourceFields.getSelectedFieldsName();
            ArrayList<String> overlayAnaylstList = tableFieldNameOverlayAnalystFields.getSelectedFieldsName();

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

    public FieldsSetDialog() {
        super();
    }

    public DialogResult showDialog(DatasetVector sourceDataset, DatasetVector overlayAnalystDataset) {
        this.sourceDataset = sourceDataset;
        this.overlayAnalystDataset = overlayAnalystDataset;
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

    private void registEvents() {
        removeEvents();
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
        scrollpaneSourceFields.setViewportView(tableFieldNameSourceFields);
        panelOverlayAnalystFields.setLayout(new GridBagLayout());
        panelOverlayAnalystFields.add(scrollpaneOverlayAnalystFields, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        scrollpaneOverlayAnalystFields.setViewportView(this.tableFieldNameOverlayAnalystFields);


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
        this.setTitle(ControlsProperties.getString("String_Form_FieldsSetting"));
    }

    private void initComponents() {
        this.tableFieldNameSourceFields= (TableFieldName) TableFactory.getTable("FieldName");
        this.tableFieldNameSourceFields.setDatasetVector(sourceDataset);
        this.tableFieldNameOverlayAnalystFields= (TableFieldName) TableFactory.getTable("FieldName");
        this.tableFieldNameOverlayAnalystFields.setDatasetVector(overlayAnalystDataset);


        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
    }

    public String[] getSourceFields() {
        return sourceFields;
    }

    public String[] getOverlayAnalystFields() {
        return overlayAnalystFields;
    }
}
