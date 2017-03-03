package com.supermap.desktop.importUI;

import com.supermap.data.conversion.*;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.TristateCheckBox;
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
    private TristateCheckBox checkBoxPyramidBuild;// 创建影像金字塔
    private TristateCheckBox checkBoxAttributeIgnored;//忽略属性信息
    private final int PYRAMIDBUILD = 0;
    private final int ATTRIBUTEIGNORED = 1;

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
                    return;
                }
                if (importSetting instanceof ImportSettingGBDEM) {
                    ((ImportSettingGBDEM) importSetting).setPyramidBuilt(checkBoxPyramidBuild.isSelected());
                    return;
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
                    return;
                }
                if (importSetting instanceof ImportSettingE00) {
                    ((ImportSettingE00) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                    return;
                }
                if (importSetting instanceof ImportSettingLIDAR) {
                    ((ImportSettingLIDAR) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                    return;
                }
                if (importSetting instanceof ImportSettingTAB) {
                    ((ImportSettingTAB) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                    return;
                }
                if (importSetting instanceof ImportSettingMIF) {
                    ((ImportSettingMIF) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                    return;
                }
                if (importSetting instanceof ImportSettingFileGDBVector) {
                    ((ImportSettingFileGDBVector) importSetting).setAttributeIgnored(checkBoxAttributeIgnored.isSelected());
                    return;
                }
            }
        }
    };

    public PanelTransformForGRD(ImportSetting importSetting) {
        super(importSetting);
        setComponentName();
        registEvents();
    }

    public PanelTransformForGRD(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        initLayerout();
        setComponentName();
        registEvents();
    }

    @Override
    public void initComponents() {
        if (importSetting instanceof ImportSettingDBF) {
            return;
        } else if (importSetting instanceof ImportSettingGRD || importSetting instanceof ImportSettingGBDEM) {
            this.checkBoxPyramidBuild = new TristateCheckBox();
            initCheckBoxPyramindBuild();
        } else {
            this.checkBoxAttributeIgnored = new TristateCheckBox();
            initCheckBoxAttributeIgenored();
        }
    }
    public void setComponentName() {
        super.setComponentName();
        ComponentUIUtilities.setName(this.checkBoxPyramidBuild, "PanelTransformForGRD_checkBoxPyramidBuild");
        ComponentUIUtilities.setName(this.checkBoxAttributeIgnored, "PanelTransformForGRD_checkBoxAttributeIgnored");
    }
    private void initCheckBoxAttributeIgenored() {
        if (null == panelImports) {
            if (importSetting instanceof ImportSettingSHP) {
                checkBoxAttributeIgnored.setSelected(((ImportSettingSHP) importSetting).isAttributeIgnored());
                return;
            }
            if (importSetting instanceof ImportSettingE00) {
                checkBoxAttributeIgnored.setSelected(((ImportSettingE00) importSetting).isAttributeIgnored());
                return;
            }
            if (importSetting instanceof ImportSettingLIDAR) {
                checkBoxAttributeIgnored.setSelected(((ImportSettingLIDAR) importSetting).isAttributeIgnored());
                return;
            }
            if (importSetting instanceof ImportSettingTAB) {
                checkBoxAttributeIgnored.setSelected(((ImportSettingTAB) importSetting).isAttributeIgnored());
                return;
            }
            if (importSetting instanceof ImportSettingMIF) {
                checkBoxAttributeIgnored.setSelected(((ImportSettingMIF) importSetting).isAttributeIgnored());
                return;
            }
            if (importSetting instanceof ImportSettingFileGDBVector) {
                checkBoxAttributeIgnored.setSelected(((ImportSettingFileGDBVector) importSetting).isAttributeIgnored());
                return;
            }
        }
    }

    private void initCheckBoxPyramindBuild() {
        if (null == panelImports) {
            if (importSetting instanceof ImportSettingGRD) {
                checkBoxPyramidBuild.setSelected(((ImportSettingGRD) importSetting).isPyramidBuilt());
                return;
            }
            if (importSetting instanceof ImportSettingGBDEM) {
                checkBoxPyramidBuild.setSelected(((ImportSettingGBDEM) importSetting).isPyramidBuilt());
                return;
            }
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        if (importSetting instanceof ImportSettingDBF) {
            return;
        } else if (importSetting instanceof ImportSettingGRD || importSetting instanceof ImportSettingGBDEM) {
            this.add(this.checkBoxPyramidBuild, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            if (null != panelImports) {
                this.checkBoxPyramidBuild.setSelectedEx(externalDataSelectAll(PYRAMIDBUILD));
            }
        } else {
            this.add(this.checkBoxAttributeIgnored, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            if (null != panelImports) {
                this.checkBoxAttributeIgnored.setSelectedEx(externalDataSelectAll(ATTRIBUTEIGNORED));
            }
        }
    }

    private Boolean externalDataSelectAll(int type) {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelImport tempPanel : panelImports) {
            boolean select = getCheckboxState(tempPanel, type);
            if (select) {
                selectCount++;
            } else if (!select) {
                unSelectCount++;

            }
        }
        if (selectCount == panelImports.size()) {
            result = true;
        } else if (unSelectCount == panelImports.size()) {
            result = false;
        }
        return result;
    }

    private boolean getCheckboxState(PanelImport panelImport, int type) {
        boolean result = false;
        if (type == PYRAMIDBUILD) {
            result = ((PanelTransformForGRD) panelImport.getTransform()).getCheckBoxPyramidBuild().isSelected();
        } else if (type == ATTRIBUTEIGNORED) {
            result = ((PanelTransformForGRD) panelImport.getTransform()).getCheckBoxAttributeIgnored().isSelected();
        }
        return result;
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
