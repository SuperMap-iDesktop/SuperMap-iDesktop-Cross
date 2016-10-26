package com.supermap.desktop.baseUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Interface.IImportSetttingTransform;
import com.supermap.desktop.importUI.PanelImport;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/30.
 */
public class PanelTransform extends JPanel implements IImportSetttingTransform {

    protected ImportSetting importSetting;
    protected int layoutType;

    public PanelTransform(ImportSetting importSetting) {
        this.importSetting = importSetting;
        initComponents();
        initLayerout();
        initResources();
    }

    public PanelTransform(ArrayList<PanelImport> panelImports, int layoutType) {
        this.layoutType = layoutType;
        this.importSetting = panelImports.get(panelImports.size() - 1).getImportInfo().getImportSetting();
        initComponents();
        if (layoutType == PackageInfo.SAME_TYPE) {
            initLayerout();
        } else if (layoutType == PackageInfo.GRID_TYPE) {
            initGridLayout();
        }
        initResources();
    }

    public void initGridLayout() {
    }

    public void initComponents() {

    }

    public void initLayerout() {

    }

    public void registEvents() {

    }

    public void removeEvents() {

    }

    public void initResources() {

    }

    @Override
    public void initImportSetting() {

    }
}
