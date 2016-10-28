package com.supermap.desktop.exportUI;

import com.supermap.data.conversion.CADVersion;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by xie on 2016/10/27.
 * 导出矢量数据集矢量参数设置
 */
public class PanelExportTransformForVector extends PanelExportTransform {
    private JCheckBox checkBoxExportExternalData;
    private JCheckBox checkBoxExportExternalRecord;
    private JCheckBox checkBoxExportPointAsWKT;
    private JLabel labelDatasets;
    private CharsetComboBox charsetComboBox;
    private JLabel labelCADVersion;
    private JComboBox<String> comboBoxCADVersion;
    private JLabel labelExpression;
    private JTextArea textAreaExpression;
    private JButton buttonExpression;

    public PanelExportTransformForVector(ExportSetting exportSetting) {
        super(exportSetting);
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxExportExternalData = new JCheckBox();
        this.checkBoxExportExternalRecord = new JCheckBox();
        this.checkBoxExportPointAsWKT = new JCheckBox();
        this.labelDatasets = new JLabel();
        this.charsetComboBox = new CharsetComboBox();
        this.labelCADVersion = new JLabel();
        this.comboBoxCADVersion = new JComboBox<>();
        this.labelExpression = new JLabel();
        this.textAreaExpression = new JTextArea();
        this.buttonExpression = new JButton();
        initComboboxCADVersion();
    }

    private void initComboboxCADVersion() {
        this.comboBoxCADVersion.setModel(new DefaultComboBoxModel<String>(new String[]{CADVersion.CAD12.toString(), CADVersion.CAD13.toString(),
                CADVersion.CAD14.toString(), CADVersion.CAD2000.toString(), CADVersion.CAD2004.toString(), CADVersion.CAD2007.toString()}));
        this.charsetComboBox.setEditable(true);
        ((JTextField) this.charsetComboBox.getEditor().getEditorComponent()).setEditable(false);
        this.comboBoxCADVersion.setEditable(true);
        ((JTextField) this.comboBoxCADVersion.getEditor().getEditorComponent()).setEditable(false);
    }

    @Override
    public void initLayerout() {
        JPanel panelContent = new JPanel();
        panelContent.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());
        this.add(panelContent, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));

        panelContent.add(this.checkBoxExportExternalData, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.checkBoxExportExternalRecord, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.checkBoxExportPointAsWKT, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.labelDatasets, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.charsetComboBox, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelCADVersion, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.comboBoxCADVersion, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelExpression, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.textAreaExpression, new GridBagConstraintsHelper(0, 6, 2, 3).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setIpad(0, 120));
        panelContent.add(this.buttonExpression, new GridBagConstraintsHelper(1, 9, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.NONE).setWeight(0, 0));
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelproperty")));
        this.checkBoxExportExternalData.setText(DataConversionProperties.getString("string_chcekbox_extends"));
        this.checkBoxExportExternalRecord.setText(DataConversionProperties.getString("String_ExportExternalRecord"));
        this.checkBoxExportPointAsWKT.setText(DataConversionProperties.getString("String_ExportPointAsWKT"));
        this.labelDatasets.setText(ControlsProperties.getString("String_LabelCharset"));
        this.labelCADVersion.setText(DataConversionProperties.getString("string_label_lblCAD"));
        this.labelExpression.setText(DataConversionProperties.getString("String_FilterExpression"));
        this.buttonExpression.setText(ControlsProperties.getString("String_SQLExpression") + "...");
    }
}
