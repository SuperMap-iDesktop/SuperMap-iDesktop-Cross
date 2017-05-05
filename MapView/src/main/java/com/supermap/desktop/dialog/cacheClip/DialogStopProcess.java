package com.supermap.desktop.dialog.cacheClip;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2017/4/25.
 */
public class DialogStopProcess extends SmDialog {
    private JLabel labelWarnning;
    private JRadioButton stopCurrentProcess;
    private JRadioButton stopCurrentProcessWhenMissionFinished;
    private JButton buttonOk;
    private JButton buttonCancel;

    public DialogStopProcess() {
        super();
        init();
    }

    private void init() {
        initComponents();
        initResources();
        initLayout();
    }

    private void initComponents() {
        this.labelWarnning = new JLabel();
        this.stopCurrentProcess = new JRadioButton();
        this.stopCurrentProcessWhenMissionFinished = new JRadioButton();
        this.stopCurrentProcessWhenMissionFinished.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(stopCurrentProcess);
        group.add(stopCurrentProcessWhenMissionFinished);
        this.buttonOk = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.setSize(320, 200);
        this.setLocationRelativeTo(null);
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_ProcessStopType"));
        this.labelWarnning.setText(MapViewProperties.getString("String_StopProcessesMethod"));
        this.stopCurrentProcess.setText(MapViewProperties.getString("String_StopCurrentProcess"));
        this.stopCurrentProcessWhenMissionFinished.setText(MapViewProperties.getString("String_StopCurrentProcessWhenMissionFinished"));
    }

    private void initLayout() {
        JPanel panelContent = (JPanel) this.getContentPane();
        panelContent.setLayout(new GridBagLayout());
        panelContent.add(this.labelWarnning, new GridBagConstraintsHelper(0, 0, 4, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 20, 10, 10).setIpad(0,10).setWeight(0, 1));
        panelContent.add(this.stopCurrentProcess, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 5, 10));
        panelContent.add(this.stopCurrentProcessWhenMissionFinished, new GridBagConstraintsHelper(0, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 5, 10));
        panelContent.add(this.buttonOk, new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(1, 0));
        panelContent.add(this.buttonCancel, new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(0, 0));
    }
}
