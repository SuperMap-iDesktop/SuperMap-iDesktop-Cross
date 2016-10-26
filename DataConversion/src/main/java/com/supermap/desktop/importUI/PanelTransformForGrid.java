package com.supermap.desktop.importUI;

import com.supermap.data.conversion.*;
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
 * Created by xie on 2016/10/11.
 * raw,.b,bin,bip,bil,bsq,dem等栅格文件的导入参数设置界面
 */
public class PanelTransformForGrid extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
    private JCheckBox checkBoxBuildImgPyramid;
    private ItemListener buildImgPyramidListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForGrid) tempPanelImport.getTransform()).getCheckBoxBuildImgPyramid().setSelected(checkBoxBuildImgPyramid.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingRAW) {
                    ((ImportSettingRAW) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
                if (importSetting instanceof ImportSettingTEMSClutter) {
                    ((ImportSettingTEMSClutter) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
                if (importSetting instanceof ImportSettingBIP) {
                    ((ImportSettingBIP) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
                if (importSetting instanceof ImportSettingBSQ) {
                    ((ImportSettingBSQ) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
                if (importSetting instanceof ImportSettingGBDEM) {
                    ((ImportSettingGBDEM) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
                if (importSetting instanceof ImportSettingUSGSDEM) {
                    ((ImportSettingUSGSDEM) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
            }
        }
    };

    public PanelTransformForGrid(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForGrid(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxBuildImgPyramid = new JCheckBox();
        if (importSetting instanceof ImportSettingRAW) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingRAW) importSetting).isPyramidBuilt());
        }
        if (importSetting instanceof ImportSettingTEMSClutter) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingTEMSClutter) importSetting).isPyramidBuilt());
        }
        if (importSetting instanceof ImportSettingBIP) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingBIP) importSetting).isPyramidBuilt());
        }
        if (importSetting instanceof ImportSettingBSQ) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingBSQ) importSetting).isPyramidBuilt());
        }
        if (importSetting instanceof ImportSettingGBDEM) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingGBDEM) importSetting).isPyramidBuilt());
        }
        if (importSetting instanceof ImportSettingUSGSDEM) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingUSGSDEM) importSetting).isPyramidBuilt());
        }
    }

    @Override
    public void initLayerout() {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        this.add(this.checkBoxBuildImgPyramid, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0));
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.checkBoxBuildImgPyramid.addItemListener(this.buildImgPyramidListener);
    }

    @Override
    public void removeEvents() {
        this.checkBoxBuildImgPyramid.removeItemListener(this.buildImgPyramidListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.checkBoxBuildImgPyramid.setText(DataConversionProperties.getString("string_checkbox_chckbxImageInfo"));
    }

    public JCheckBox getCheckBoxBuildImgPyramid() {
        return checkBoxBuildImgPyramid;
    }
}
