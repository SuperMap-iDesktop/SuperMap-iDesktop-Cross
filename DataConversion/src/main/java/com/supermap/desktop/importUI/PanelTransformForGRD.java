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
 * Created by xie on 2016/9/30.
 * shp,txt,dem,e00,grd,dbf,雷达文件（txt）
 * tab,mif参数设置界面
 */
public class PanelTransformForGRD extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
    private JCheckBox checkBoxPyramidBuild;// 创建影像金字塔
    private JCheckBox checkBoxAttributeIgnored;//忽略属性信息
    private ItemListener checkBoxPyramidBuildListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForGRD) tempPanelImport.getTransform()).getCheckBoxPyramidBuild().setSelected(checkBoxPyramidBuild.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingGRD) {
                    ((ImportSettingGRD) importSetting).setPyramidBuilt(checkBoxPyramidBuild.isSelected());
                }
                if (importSetting instanceof ImportSettingGBDEM) {
                    ((ImportSettingGBDEM) importSetting).setPyramidBuilt(checkBoxPyramidBuild.isSelected());
                }
            }
        }
    };
    private ItemListener checkBoxAttributeIgnoredListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForGRD) tempPanelImport.getTransform()).getCheckBoxAttributeIgnored().setSelected(checkBoxAttributeIgnored.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingSHP) {
                    ((ImportSettingSHP) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                }
                if (importSetting instanceof ImportSettingE00) {
                    ((ImportSettingE00) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                }
                if (importSetting instanceof ImportSettingLIDAR) {
                    ((ImportSettingLIDAR) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                }
                if (importSetting instanceof ImportSettingTAB) {
                    ((ImportSettingTAB) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                }
                if (importSetting instanceof ImportSettingMIF) {
                    ((ImportSettingMIF) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                }
            }
        }
    };

    public PanelTransformForGRD(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForGRD(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        initLayerout();
        registEvents();
    }

    @Override
    public void initComponents() {
        if (importSetting instanceof ImportSettingDBF) {
            return;
        } else if (importSetting instanceof ImportSettingGRD || importSetting instanceof ImportSettingGBDEM) {
            this.checkBoxPyramidBuild = new JCheckBox();
            initCheckBoxPyramindBuild();
        } else {
            this.checkBoxAttributeIgnored = new JCheckBox();
            initCheckBoxAttributeIgenored();
        }
    }

    private void initCheckBoxAttributeIgenored() {
        if (importSetting instanceof ImportSettingSHP) {
            checkBoxAttributeIgnored.setSelected(((ImportSettingSHP) importSetting).isAttributeIgnored());
        }
        if (importSetting instanceof ImportSettingE00) {
            checkBoxAttributeIgnored.setSelected(((ImportSettingE00) importSetting).isAttributeIgnored());
        }
        if (importSetting instanceof ImportSettingLIDAR) {
            checkBoxAttributeIgnored.setSelected(((ImportSettingLIDAR) importSetting).isAttributeIgnored());
        }
        if (importSetting instanceof ImportSettingTAB) {
            checkBoxAttributeIgnored.setSelected(((ImportSettingTAB) importSetting).isAttributeIgnored());
        }
        if (importSetting instanceof ImportSettingMIF) {
            checkBoxAttributeIgnored.setSelected(((ImportSettingMIF) importSetting).isAttributeIgnored());
        }
    }

    private void initCheckBoxPyramindBuild() {
        if (importSetting instanceof ImportSettingGRD) {
            checkBoxPyramidBuild.setSelected(((ImportSettingGRD) importSetting).isPyramidBuilt());
        }
        if (importSetting instanceof ImportSettingGBDEM) {
            checkBoxPyramidBuild.setSelected(((ImportSettingGBDEM) importSetting).isPyramidBuilt());
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        if (importSetting instanceof ImportSettingDBF) {
            return;
        } else if (importSetting instanceof ImportSettingGRD || importSetting instanceof ImportSettingGBDEM) {
            this.add(this.checkBoxPyramidBuild, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        } else {
            this.add(this.checkBoxAttributeIgnored, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        }
    }

    @Override
    public void registEvents() {
        removeEvents();
        if (null != this.checkBoxPyramidBuild) {
            this.checkBoxPyramidBuild.addItemListener(this.checkBoxPyramidBuildListener);
        }
        if (null != this.checkBoxAttributeIgnored) {
            this.checkBoxAttributeIgnored.addItemListener(this.checkBoxAttributeIgnoredListener);
        }
    }

    @Override
    public void removeEvents() {
        if (null != this.checkBoxPyramidBuild) {
            this.checkBoxPyramidBuild.removeItemListener(this.checkBoxPyramidBuildListener);
        }
        if (null != this.checkBoxAttributeIgnored) {
            this.checkBoxAttributeIgnored.removeItemListener(this.checkBoxAttributeIgnoredListener);
        }
    }

    @Override
    public void initResources() {
        if (importSetting instanceof ImportSettingDBF) {
            return;
        } else if (null != this.checkBoxPyramidBuild) {
            this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
            this.checkBoxPyramidBuild.setText(DataConversionProperties.getString("string_checkbox_chckbxImageInfo"));
        }
        if (null != this.checkBoxAttributeIgnored) {
            this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
            this.checkBoxAttributeIgnored.setText(DataConversionProperties.getString("string_checkbox_chckIngoreProperty"));
        }
    }

    public JCheckBox getCheckBoxPyramidBuild() {
        return checkBoxPyramidBuild;
    }

    public JCheckBox getCheckBoxAttributeIgnored() {
        return checkBoxAttributeIgnored;
    }
}
