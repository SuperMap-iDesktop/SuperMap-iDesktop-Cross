package com.supermap.desktop.importUI;


import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.baseUI.PanelResultset;
import com.supermap.desktop.baseUI.PanelSourceInfo;
import com.supermap.desktop.iml.ImportInfo;
import com.supermap.desktop.iml.PanelTransformFactory;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2016/9/30.
 */
public class PanelImport extends JPanel implements IPanelImport {

    private ImportSetting importSetting;
    private IImportSettingResultset resultset;
    private IImportSettingSourceInfo sourceInfo;
    private IImportSetttingTransform transform;
    private JDialog owner;
    private ImportInfo importInfo;

    @Override
    public ImportInfo getImportInfo() {
        return importInfo;
    }

    public PanelImport(JDialog owner, ImportInfo importInfo) {
        this.owner = owner;
        this.importInfo = importInfo;
        this.importSetting = importInfo.getImportSetting();
        if (null == importSetting) {
            return;
        }
        initComponents();
        initLayerout();
    }

    @Override
    public void initComponents() {
        IPanelTransformFactory transformFactory = new PanelTransformFactory();
        this.resultset = new PanelResultset(importInfo);
        this.sourceInfo = new PanelSourceInfo(owner, importSetting);
        this.transform = transformFactory.createPanelTransform(importSetting);
    }

    @Override
    public void initLayerout() {
        JPanel panelContents = new JPanel();
        this.setLayout(new GridBagLayout());
        this.add(panelContents, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL));
        panelContents.setLayout(new GridBagLayout());
        panelContents.add((Component) this.resultset, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelContents.add((Component) this.transform, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelContents.add((Component) this.sourceInfo, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
    }

    @Override
    public void registEvents() {

    }

    @Override
    public void removeEvents() {

    }
}
