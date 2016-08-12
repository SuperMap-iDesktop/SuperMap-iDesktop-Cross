package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
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
    private JPanel panelPointStyle;
    private JLabel labelPointColor;
    private JPanel panelPointColor;
    private JLabel labelPointSize;
    private JComboBox comboboxPointSize;
    private JButton buttonPointModel;
    // 线风格
    private JPanel panelLineStyle;
    private JLabel labelLineColor;
    private JPanel panelLineColor;
    private JLabel labelLineWidth;
    private JComboBox comboboxLineWidth;
    private JButton buttonLineModel;
    // 面风格
    private JPanel panelFillStyle;
    private JLabel labelFillForeColor;
    private JPanel panelFillForeColor;
    private JLabel labelFillOqaue;
    private JSpinner spinnerFillSize;
    private JButton buttonFillModel;
    // 关闭按钮
    private JButton buttonClose;

    private String[] stringPointSizes = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "1.5", "3", "4", "5", "6", "7", "8", "9", "10", "15", "10"};
    private String[] stringLineWidths = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

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
        this.labelPointColor.setText(ControlsProperties.getString("String_Column_Color") + ":");
        this.labelPointSize.setText(CommonProperties.getString("String_Size") + ":");
        this.labelLineColor.setText(ControlsProperties.getString("String_Column_Color") + ":");
        this.labelLineWidth.setText(ControlsProperties.getString("String_GeometryPropertyStyle3DControl_labelLineWidth"));
        this.labelFillForeColor.setText(MapEditorProperties.getString("String_FillForeColor"));
        this.labelFillOqaue.setText(MapEditorProperties.getString("String_Oqaue_I"));
    }

    private void initComponents() {
        initLayout();
        this.setSize(300, 440);
        this.setTitle(MapEditorProperties.getString("String_CADStyle"));
        this.setLocationRelativeTo(null);
    }

    private void initLayout() {
        JPanel panel = (JPanel) this.getContentPane();
        this.buttonClose = ComponentFactory.createButtonClose();
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        panel.add(initPanelPointStyle(), new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 10, 5).setWeight(1, 1));
        panel.add(initPanelLineStyle(), new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 10, 5).setWeight(1, 1));
        panel.add(initPanelFillStyle(), new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 0, 5).setWeight(1, 1));
        panel.add(this.buttonClose, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(5, 0, 10, 10));
    }

    private JPanel initPanelFillStyle() {
        JPanel panelFillStyle = new JPanel();
        this.panelFillStyle = new CADStylePanel(CADStylePanel.FILL_TYPE,CADStylePanel.VERTICAL);
        this.labelFillForeColor = new JLabel();
        this.panelFillForeColor = new CADStylePanel(CADStylePanel.FILL_TYPE,CADStylePanel.HORIZONTAL);
        this.labelFillOqaue = new JLabel();
        this.spinnerFillSize = new JSpinner();
        this.spinnerFillSize.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        panelFillStyle.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_FillStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelFillStyle.setLayout(new GridBagLayout());
        panelFillStyle.add(this.panelFillStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(10, 10));
        panelFillStyle.add(this.labelFillForeColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 0, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillStyle.add(this.panelFillForeColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelFillStyle.add(this.labelFillOqaue, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillStyle.add(this.spinnerFillSize, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(50,0));
        return panelFillStyle;
    }

    private JPanel initPanelLineStyle() {
        JPanel panelLineStyle = new JPanel();
        this.panelLineStyle = new CADStylePanel(CADStylePanel.LINE_TYPE,CADStylePanel.VERTICAL);
        this.labelLineColor = new JLabel();
        this.panelLineColor = new CADStylePanel(CADStylePanel.LINE_TYPE,CADStylePanel.HORIZONTAL);
        this.labelLineWidth = new JLabel();
        this.comboboxLineWidth = new JComboBox();
        this.comboboxLineWidth.setModel(new DefaultComboBoxModel(this.stringLineWidths));
        this.comboboxLineWidth.setEditable(true);
        panelLineStyle.setBorder(new TitledBorder(null, MapEditorProperties.getString("Stirng_LineStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelLineStyle.setLayout(new GridBagLayout());
        panelLineStyle.add(this.panelLineStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(20, 10));
        panelLineStyle.add(this.labelLineColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 0, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLineStyle.add(this.panelLineColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelLineStyle.add(this.labelLineWidth, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLineStyle.add(this.comboboxLineWidth, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        return panelLineStyle;
    }

    private JPanel initPanelPointStyle() {
        JPanel panelPointStyle = new JPanel();
        this.panelPointStyle = new CADStylePanel(CADStylePanel.POINT_TYPE,CADStylePanel.VERTICAL);
        this.labelPointColor = new JLabel();
        this.panelPointColor = new CADStylePanel(CADStylePanel.POINT_TYPE,CADStylePanel.HORIZONTAL);
        this.labelPointSize = new JLabel();
        this.comboboxPointSize = new JComboBox();
        this.comboboxPointSize.setModel(new DefaultComboBoxModel(stringPointSizes));
        this.comboboxPointSize.setEditable(true);
        panelPointStyle.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_PointStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelPointStyle.setLayout(new GridBagLayout());
        panelPointStyle.add(this.panelPointStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(20, 10));
        panelPointStyle.add(this.labelPointColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPointStyle.add(this.panelPointColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelPointStyle.add(this.labelPointSize, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPointStyle.add(this.comboboxPointSize, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setIpad(10, 0).setFill(GridBagConstraints.HORIZONTAL));
        return panelPointStyle;
    }
}
