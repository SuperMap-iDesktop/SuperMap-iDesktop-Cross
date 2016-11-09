package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingMAPGIS;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.localUtilities.CommonUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/30.
 * wat,wal,wap,wan转化参数设置界面
 */
public class PanelTransformForMapGIS extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
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
            String filePath = fileChooserColorIndex.getEditor().getText();
            if (!StringUtilities.isNullOrEmpty(filePath)) {
                fileChooser.setSelectedFile(new File(filePath));
            }
            int state = fileChooser.showDefaultDialog();
            File file = fileChooser.getSelectedFile();
            if (state == JFileChooser.APPROVE_OPTION && null != file) {
                fileChooserColorIndex.getEditor().setText(file.getAbsolutePath());
                // 设置颜色索引文件
                String colorFile = fileChooserColorIndex.getEditor().getText();
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        ((PanelTransformForMapGIS) tempPanelImport.getTransform()).getFileChooserColorIndex().getEditor().setText(colorFile);
                    }
                } else {
                    if (CommonUtilities.isExtendsFile(colorFile)) {
                        ((ImportSettingMAPGIS) importSetting).setColorIndexFilePath(colorFile);
                    }
                }
            }
        }
    };

    public PanelTransformForMapGIS(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForMapGIS(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        initLayerout();
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelColorIndex = new JLabel();
        this.fileChooserColorIndex = new FileChooserControl();
        String filePath = ((ImportSettingMAPGIS) importSetting).getColorIndexFilePath();
        if (!StringUtilities.isNullOrEmpty(filePath)) {
            fileChooserColorIndex.getEditor().setText(new File(filePath).getAbsolutePath());
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelColorIndex, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.fileChooserColorIndex, new GridBagConstraintsHelper(1, 0, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        setColorIndexText();
    }

    private void setColorIndexText() {
        if (null != panelImports) {
            this.fileChooserColorIndex.getEditor().setText(getColorIndex());
        }
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

    public FileChooserControl getFileChooserColorIndex() {
        return fileChooserColorIndex;
    }

    public String getColorIndex() {
        String result = "";
        String temp = ((PanelTransformForMapGIS) panelImports.get(0).getTransform()).getFileChooserColorIndex().getEditor().getText();
        boolean isSame = true;
        for (PanelImport tempPanel : panelImports) {
            String tempObject = ((PanelTransformForMapGIS) tempPanel.getTransform()).getFileChooserColorIndex().getEditor().getText();
            if (!temp.equals(tempObject)) {
                isSame = false;
                break;
            }
        }
        if (isSame) {
            result = temp;
        }
        return result;
    }
}
