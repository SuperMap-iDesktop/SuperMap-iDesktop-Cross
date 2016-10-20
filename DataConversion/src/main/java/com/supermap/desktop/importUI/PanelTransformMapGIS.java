package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingMAPGIS;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.util.ImportInfoUtil;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by xie on 2016/9/30.
 * wat,wal,wap,wan转化参数设置界面
 */
public class PanelTransformMapGIS extends PanelTransform {
    private JLabel labelColorIndex;
    private FileChooserControl fileChooserColorIndex;
    private ActionListener fileChooserColorIndexListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!SmFileChoose.isModuleExist("ImportMapGIS")) {
                String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_color"), "wat");
                SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                        DataConversionProperties.getString("String_Import"), "ImportMapGIS", "OpenMany");
            }
            SmFileChoose fileChooser = new SmFileChoose("ImportMapGIS");
            int state = fileChooser.showDefaultDialog();
            File file = fileChooser.getSelectedFile();
            if (state == JFileChooser.APPROVE_OPTION && null != file) {
                fileChooserColorIndex.getEditor().setText(file.getAbsolutePath());
                // 设置颜色索引文件
                String colorFile = fileChooserColorIndex.getEditor().getText();

                if (ImportInfoUtil.isExtendsFile(colorFile)) {
                    ((ImportSettingMAPGIS) importSetting).setColorIndexFilePath(colorFile);
                }
            }
        }
    };

    public PanelTransformMapGIS(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelColorIndex = new JLabel();
        this.fileChooserColorIndex = new FileChooserControl();
        if (StringUtilities.isNullOrEmpty(((ImportSettingMAPGIS) importSetting).getColorIndexFilePath())) {
            fileChooserColorIndex.getEditor().setText(((ImportSettingMAPGIS) importSetting).getColorIndexFilePath());
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelColorIndex, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.fileChooserColorIndex, new GridBagConstraintsHelper(1, 0, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.fileChooserColorIndex.getButton().addActionListener(this.fileChooserColorIndexListener);
    }

    @Override
    public void removeEvents() {
        this.fileChooserColorIndex.getButton().removeActionListener(this.fileChooserColorIndexListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelColorIndex.setText(DataConversionProperties.getString("string_label_lblColorFile"));
    }
}
