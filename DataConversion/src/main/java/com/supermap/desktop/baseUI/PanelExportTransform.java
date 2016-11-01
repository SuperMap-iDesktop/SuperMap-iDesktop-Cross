package com.supermap.desktop.baseUI;

import com.supermap.data.conversion.ExportSetting;
import com.supermap.desktop.Interface.IPanelModel;

import javax.swing.*;

/**
 * Created by xie on 2016/10/26.
 * 导出数据集参数设置界面父类
 */
public class PanelExportTransform extends JPanel implements IPanelModel {

    private ExportSetting exportSetting;

    public PanelExportTransform(ExportSetting exportSetting) {
        this.exportSetting = exportSetting;
        initComponents();
        initLayerout();
        initResources();
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void initLayerout() {

    }

    @Override
    public void registEvents() {

    }

    @Override
    public void removeEvents() {

    }

    public void initResources() {

    }

    public ExportSetting getExportSetting() {
        return exportSetting;
    }
}
