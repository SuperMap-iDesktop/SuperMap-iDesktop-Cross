package com.supermap.desktop.dialog.cacheClip;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by xie on 2017/4/25.
 * Dialog for creating single process clip or multi process clip
 */
public class DialogMapCacheClip extends SmDialog {
    private JRadioButton singleProcessClip;
    private JRadioButton multiProcessClip;
    private JLabel labelWarning;
    private JRadioButton radioButtonYes;
    private JRadioButton radioButtonNo;
    private JButton buttonOk;
    private JButton buttonCancel;
    private CompTitledPane paneMultiProcessClip;
    private boolean isSingleProcess;
    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            isSingleProcess = singleProcessClip.isSelected();
            dialogResult = DialogResult.OK;
            DialogMapCacheClip.this.dispose();
        }
    };
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DialogMapCacheClip.this.dispose();
        }
    };

    public DialogMapCacheClip() {
        super();
        init();
    }

    private void init() {
        initComponents();
        initResources();
        initLayout();
        registEvents();
        this.componentList.add(this.buttonOk);
        this.componentList.add(this.buttonCancel);
        this.setFocusTraversalPolicy(policy);
    }

    private void registEvents() {
        removeEvents();
        this.buttonOk.addActionListener(this.okListener);
        this.buttonCancel.addActionListener(this.cancelListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeEvents();
            }
        });
    }

    private void removeEvents() {
        this.buttonOk.removeActionListener(this.okListener);
        this.buttonCancel.removeActionListener(this.cancelListener);
    }

    private void initComponents() {
        this.singleProcessClip = new JRadioButton();
        this.multiProcessClip = new JRadioButton();
        ButtonGroup group = new ButtonGroup();
        group.add(this.singleProcessClip);
        group.add(this.multiProcessClip);
        this.multiProcessClip.setSelected(true);
        this.labelWarning = new JLabel();
        ButtonGroup yesOrNoGroup = new ButtonGroup();
        this.radioButtonYes = new JRadioButton();
        this.radioButtonNo = new JRadioButton();
        this.radioButtonYes.setSelected(true);
        yesOrNoGroup.add(this.radioButtonYes);
        yesOrNoGroup.add(this.radioButtonNo);
        this.buttonOk = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.setSize(360, 230);
        this.setLocationRelativeTo(null);
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_ClipCacheType"));
        this.singleProcessClip.setText(MapViewProperties.getString("String_SingleProcessClip"));
        this.multiProcessClip.setText(MapViewProperties.getString("String_MultiProcessClip"));
        this.labelWarning.setText(MapViewProperties.getString("String_ClipProcess"));
        this.radioButtonYes.setText(MapViewProperties.getString("String_ClipProcessYes"));
        this.radioButtonNo.setText(MapViewProperties.getString("String_ClipProcessNo"));
    }

    private void initLayout() {
        JPanel panelContent = (JPanel) this.getContentPane();
        JPanel panelMultiProcessClip = new JPanel();
        panelMultiProcessClip.setLayout(new GridBagLayout());
        panelMultiProcessClip.add(this.labelWarning, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
        panelMultiProcessClip.add(this.radioButtonYes, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelMultiProcessClip.add(this.radioButtonNo, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        this.paneMultiProcessClip = new CompTitledPane(this.multiProcessClip, panelMultiProcessClip);
        panelContent.setLayout(new GridBagLayout());
        panelContent.add(this.singleProcessClip, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 20, 5, 10).setWeight(0, 0));
        panelContent.add(this.paneMultiProcessClip, new GridBagConstraintsHelper(0, 1, 4, 3).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(0, 10, 5, 10).setWeight(1, 1));
        panelContent.add(this.buttonOk, new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(1, 0));
        panelContent.add(this.buttonCancel, new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(0, 0));
    }

    public boolean isSingleProcess() {
        return isSingleProcess;
    }
}
