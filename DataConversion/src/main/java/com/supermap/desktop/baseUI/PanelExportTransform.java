package com.supermap.desktop.baseUI;

import com.supermap.desktop.Interface.IPanelModel;
import com.supermap.desktop.iml.ExportFileInfo;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/26.
 * 导出数据集参数设置界面父类
 */
public class PanelExportTransform extends JPanel implements IPanelModel {

    public ExportFileInfo exportsFileInfo;
    public ArrayList<PanelExportTransform> panels;
    public int layoutType;

    public PanelExportTransform(ExportFileInfo exportsFileInfo) {
        this.exportsFileInfo = exportsFileInfo;
        initComponents();
        initLayerout();
        initResources();
    }

    public PanelExportTransform(ArrayList<PanelExportTransform> panels, int layoutType) {
        this.panels = panels;
        this.layoutType = layoutType;
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

    public void setUnEnabled() {

    }

    public void initResources() {

    }

    public ExportFileInfo getExportsFileInfo() {
        return exportsFileInfo;
    }
}
