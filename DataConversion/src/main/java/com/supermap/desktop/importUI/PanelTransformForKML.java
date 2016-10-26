package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingKML;
import com.supermap.data.conversion.ImportSettingKMZ;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/30.
 * kml,kmz文件的转换参数设置界面
 */
public class PanelTransformForKML extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
    private JCheckBox checkBoxImportInvisible;
    private ItemListener importInvisibleListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForKML) tempPanelImport.getTransform()).getCheckBoxImportInvisible().setSelected(checkBoxImportInvisible.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingKML) {
                    ((ImportSettingKML) importSetting).setUnvisibleObjectIgnored(checkBoxImportInvisible.isSelected());
                } else if (importSetting instanceof ImportSettingKMZ) {
                    ((ImportSettingKMZ) importSetting).setUnvisibleObjectIgnored(checkBoxImportInvisible.isSelected());
                }
            }
        }
    };

    public PanelTransformForKML(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForKML(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxImportInvisible = new JCheckBox();
        if (importSetting instanceof ImportSettingKML) {
            this.checkBoxImportInvisible.setSelected(((ImportSettingKML) importSetting).isUnvisibleObjectIgnored());
        } else if (importSetting instanceof ImportSettingKMZ) {
            this.checkBoxImportInvisible.setSelected(((ImportSettingKMZ) importSetting).isUnvisibleObjectIgnored());
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.checkBoxImportInvisible, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0));
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.checkBoxImportInvisible.addItemListener(this.importInvisibleListener);
    }

    @Override
    public void removeEvents() {
        this.checkBoxImportInvisible.removeItemListener(this.importInvisibleListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.checkBoxImportInvisible.setText(DataConversionProperties.getString("string_checkbox_chckbxImport"));
    }

    public JCheckBox getCheckBoxImportInvisible() {
        return checkBoxImportInvisible;
    }
}
