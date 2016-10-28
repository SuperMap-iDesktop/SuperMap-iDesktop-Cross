package com.supermap.desktop.exportUI;

import com.supermap.data.conversion.ExportSetting;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by xie on 2016/10/26.
 * 导出栅格数据集矢量参数设置
 */
public class PanelExportTransformForGrid extends PanelExportTransform {
    private JLabel labelCompressionRatio;
    private JTextField textFieldCompressionRatio;
    private JLabel labelPrjFile;
    private FileChooserControl prjFileChooser;
    private JCheckBox checkBoxExportTFW;
    private JLabel labelPassword;
    private JPasswordField passwordField;
    private JLabel labelPasswordConfrim;
    private JPasswordField passwordFieldConfrim;

    public PanelExportTransformForGrid(ExportSetting exportSetting) {
        super(exportSetting);
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelCompressionRatio = new JLabel();
        this.textFieldCompressionRatio = new JTextField();
        this.labelPrjFile = new JLabel();
        this.prjFileChooser = new FileChooserControl();
        this.checkBoxExportTFW = new JCheckBox();
        this.labelPassword = new JLabel();
        this.passwordField = new JPasswordField();
        this.labelPasswordConfrim = new JLabel();
        this.passwordFieldConfrim = new JPasswordField();
    }

    @Override
    public void initLayerout() {
        JPanel panelContent = new JPanel();
        panelContent.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());
        this.add(panelContent, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));

        panelContent.add(this.labelCompressionRatio, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.textFieldCompressionRatio, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.prjFileChooser, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.checkBoxExportTFW, new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.labelPassword, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.passwordField, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelPasswordConfrim, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.passwordFieldConfrim, new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
    }

    @Override
    public void registEvents() {
        removeEvents();
    }

    @Override
    public void removeEvents() {

    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelproperty")));
        this.labelCompressionRatio.setText(DataConversionProperties.getString("String_CompressionRatio"));
        this.labelPrjFile.setText(DataConversionProperties.getString("string_label_lblFile"));
        this.checkBoxExportTFW.setText(DataConversionProperties.getString("String_ExportingAsGeoTransformFile"));
        this.labelPassword.setText(DataConversionProperties.getString("string_label_lblPassword"));
        this.labelPasswordConfrim.setText(DataConversionProperties.getString("string_label_lblConfrimPassword"));
    }
}
