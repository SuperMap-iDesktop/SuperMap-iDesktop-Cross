package com.supermap.desktop.baseUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Interface.IImportSetttingTransform;

import javax.swing.*;

/**
 * Created by xie on 2016/9/30.
 */
public class PanelTransform extends JPanel implements IImportSetttingTransform {

    protected ImportSetting importSetting;

    public PanelTransform(ImportSetting importSetting) {
        this.importSetting = importSetting;
        initComponents();
        initLayerout();
        initResources();
    }

    ;

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
