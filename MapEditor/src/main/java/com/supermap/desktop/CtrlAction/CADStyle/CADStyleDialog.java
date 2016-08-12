package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.JPanelSymbols;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 复合风格界面
 * Created by xie on 2016/8/10.
 */
public class CADStyleDialog extends SmDialog {
    // 点风格
    private JButton buttonPointStyle;
    private JLabel labelPointColor;
    private JButton buttonPointColor;
    private JLabel labelPointSize;
    private JComboBox comboboxPointSize;
    private JButton buttonPointModel;
    // 线风格
    private JButton buttonLineStyle;
    private JLabel labelLineColor;
    private JButton buttonLineColor;
    private JLabel labelLineWidth;
    private JComboBox comboboxLineWidth;
    private JButton buttonLineModel;
    // 面风格
    private JButton buttonFillStyle;
    private JLabel labelFillForeColor;
    private JButton buttonFillForeColor;
    private JLabel labelFillOqaue;
    private JComboBox comboboxFillOqaue;
    private JButton buttonFillModel;
    // 关闭按钮
    private JButton buttonClose;

    private JPanelSymbols panelSymbols;

    public CADStyleDialog() {
        super();
        initComponents();
        initResources();
        registEvents();
    }

    private void registEvents() {
        removeEvents();
    }

    private void removeEvents() {
    }

    private void initResources() {
        this.labelPointColor.setText(ControlsProperties.getString("String_Column_Color")+":");
        this.labelPointSize.setText(CommonProperties.getString("String_Size")+":");
        this.labelLineColor.setText(ControlsProperties.getString("String_Column_Color")+":");
        this.labelLineWidth.setText(ControlsProperties.getString("String_GeometryPropertyStyle3DControl_labelLineWidth"));
        this.labelFillForeColor.setText(MapEditorProperties.getString("String_FillForeColor"));
        this.labelFillOqaue.setText(MapEditorProperties.getString("String_Oqaue_I"));
    }

    private void initComponents() {
        initLayout();
        this.setSize(600,400);
        this.setLocationRelativeTo(null);
    }

    private void initLayout() {
        JPanel panel = (JPanel) this.getContentPane();
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        panel.add(initPanelPointStyle(), new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 10, 5).setWeight(1, 1));
        panel.add(initPanelLineStyle(), new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 10, 5).setWeight(1, 1));
        panel.add(initPanelFillStyle(), new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 5).setWeight(1, 1));
        panel.add(this.buttonClose, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(5, 0, 10, 10));
    }

    private JPanel initPanelFillStyle() {
        JPanel panelFillStyle = new JPanel();
        this.buttonFillStyle = new JButton();
        this.labelFillForeColor = new JLabel();
        this.buttonFillForeColor = new JButton();
        this.labelFillOqaue = new JLabel();
        this.comboboxFillOqaue = new JComboBox();
        panelFillStyle.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_FillStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelFillStyle.setLayout(new GridBagLayout());
        panelFillStyle.add(this.buttonFillStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelFillStyle.add(this.labelFillForeColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelFillStyle.add(this.buttonFillForeColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelFillStyle.add(this.labelFillOqaue, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelFillStyle.add(this.comboboxFillOqaue, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        return panelFillStyle;
    }

    private JPanel initPanelLineStyle() {
        JPanel panelLineStyle = new JPanel();
        this.buttonLineStyle = new JButton();
        this.labelLineColor = new JLabel();
        this.buttonLineColor = new JButton();
        this.labelLineWidth = new JLabel();
        this.comboboxLineWidth = new JComboBox();
        panelLineStyle.setBorder(new TitledBorder(null, MapEditorProperties.getString("Stirng_LineStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelLineStyle.setLayout(new GridBagLayout());
        panelLineStyle.add(this.buttonLineStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelLineStyle.add(this.labelLineColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelLineStyle.add(this.buttonLineColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelLineStyle.add(this.labelLineWidth, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelLineStyle.add(this.comboboxLineWidth, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        return panelLineStyle;
    }

    private JPanel initPanelPointStyle() {
        JPanel panelPointStyle = new JPanel();
        this.buttonPointStyle = new JButton();
        this.labelPointColor = new JLabel();
        this.buttonPointColor = new JButton();
        this.labelPointSize = new JLabel();
        this.comboboxPointSize = new JComboBox();
        panelPointStyle.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_PointStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelPointStyle.setLayout(new GridBagLayout());
        panelPointStyle.add(this.buttonPointStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelPointStyle.add(this.labelPointColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelPointStyle.add(this.buttonPointColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelPointStyle.add(this.labelPointSize, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        panelPointStyle.add(this.comboboxPointSize, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 0).setWeight(1, 1));
        return panelPointStyle;
    }
}
