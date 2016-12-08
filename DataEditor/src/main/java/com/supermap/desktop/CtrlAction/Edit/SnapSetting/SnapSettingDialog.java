package com.supermap.desktop.CtrlAction.Edit.SnapSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.SnapSetting;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2016/12/7.
 */
public class SnapSettingDialog extends SmDialog {
    private JTabbedPane tabbedPane;
    private JPanel panelSnapMode;
    private JPanel panelSnapParams;
    private SnapSetting defaultSnapSetting;
    private JScrollPane scrollPaneSnapMode;
    private JTable tableSnapMode;
    private JButton buttonMoveNext;
    private JButton buttonMoveForward;
    private JButton buttonMoveFrist;
    private JButton buttonMoveLast;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonRecover;
    private JLabel labelSnapTolarence;
    private JTextField textFieldSnapTolarence;
    private JLabel labelFixedAngle;
    private JTextField textFieldFixedAngle;
    private JLabel labelMaxSnappedCount;
    private JTextField textFieldMaxSnappedCount;
    private JLabel labelFixedLength;
    private JTextField textFieldFixedLength;
    private JLabel labelMinSnappedLength;
    private JTextField textFieldMinSnappedLength;
    private JCheckBox checkBoxSnappedLineBroken;
    private JPanel panelTolarenceView;

    public SnapSettingDialog(SnapSetting snapSetting) {
        this.defaultSnapSetting = new SnapSetting(snapSetting);
    }

    private void init() {
        initComponents();
        initLayout();
        initResouces();
        registEvents();
    }

    private void registEvents() {
        removeEvents();
    }

    private void removeEvents() {
    }

    private void initResouces() {
        this.setTitle(DataEditorProperties.getString("String_SnapSetting"));
        this.labelSnapTolarence.setText(DataEditorProperties.getString("String_SnapSettingTolerance"));
        this.labelFixedAngle.setText(DataEditorProperties.getString("String_SnapSettingFixedAngle"));
        this.labelFixedLength.setText(DataEditorProperties.getString("String_SnapSettingFixedLength"));
        this.labelMaxSnappedCount.setText(DataEditorProperties.getString("String_SnapSettingMaxSnappedCount"));
        this.labelMinSnappedLength.setText(DataEditorProperties.getString("String_SnapSettingMinSnappedLength"));
        this.checkBoxSnappedLineBroken.setText(DataEditorProperties.getString("String_SnapSettingLineBroken"));
        this.buttonRecover.setText(DataEditorProperties.getString("String_SnapSettingResume"));
    }

    private void initLayout() {
        this.setSize(new Dimension(560, 420));
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonRecover, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setInsets(2, 0, 10, 5));
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 5));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));

        this.setLayout(new GridBagLayout());
        this.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(panelButton, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
        this.tabbedPane.addTab(DataEditorProperties.getString("String_SnapSetting_TabPageType"), panelSnapMode);
        this.tabbedPane.addTab(DataEditorProperties.getString("String_SnapSetting_TabPageParameters"), panelSnapParams);
        initPanelSnapModeLayout();
        initPanelSnapParamsLayout();
    }

    private void initPanelSnapParamsLayout() {
        this.panelSnapParams.setLayout(new GridBagLayout());
        this.panelSnapParams.add(this.labelSnapTolarence, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(this.textFieldSnapTolarence, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSnapParams.add(this.labelFixedAngle, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(this.textFieldFixedAngle, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSnapParams.add(this.labelMaxSnappedCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(this.textFieldMaxSnappedCount, new GridBagConstraintsHelper(1, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSnapParams.add(this.labelFixedLength, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(this.textFieldFixedLength, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSnapParams.add(this.labelMinSnappedLength, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(this.textFieldMinSnappedLength, new GridBagConstraintsHelper(1, 4, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSnapParams.add(this.checkBoxSnappedLineBroken, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
    }

    private void initPanelSnapModeLayout() {
        this.panelSnapMode.setLayout(new GridBagLayout());
        this.panelSnapMode.add(scrollPaneSnapMode, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.scrollPaneSnapMode.setViewportView(tableSnapMode);
    }

    private void initComponents() {
        this.tabbedPane = new JTabbedPane();
        this.panelSnapMode = new JPanel();
        this.panelSnapParams = new JPanel();
        this.panelTolarenceView = new JPanel();
        this.scrollPaneSnapMode = new JScrollPane();
        this.tableSnapMode = new JTable();
        this.buttonMoveNext = new JButton();
        this.buttonMoveForward = new JButton();
        this.buttonMoveFrist = new JButton();
        this.buttonMoveLast = new JButton();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.buttonRecover = new SmButton();
        this.labelSnapTolarence = new JLabel();
        this.textFieldSnapTolarence = new JTextField();
        this.labelFixedAngle = new JLabel();
        this.textFieldFixedAngle = new JTextField();
        this.labelMaxSnappedCount = new JLabel();
        this.textFieldMaxSnappedCount = new JTextField();
        this.labelFixedLength = new JLabel();
        this.textFieldFixedLength = new JTextField();
        this.labelMinSnappedLength = new JLabel();
        this.textFieldMinSnappedLength = new JTextField();
        this.checkBoxSnappedLineBroken = new JCheckBox();

    }

    public DialogResult showDialog() {
        init();
        setVisible(true);
        setLocationRelativeTo((Component) Application.getActiveApplication().getMainFrame());
        return dialogResult;
    }
}
