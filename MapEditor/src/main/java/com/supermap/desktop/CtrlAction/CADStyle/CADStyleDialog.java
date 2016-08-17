package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.GeometryUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 复合风格界面
 * Created by xie on 2016/8/10.
 */
public class CADStyleDialog extends SmDialog {
    // 点风格
    private ICADStylePanel panelPointStyle;
    private JLabel labelPointColor;
    private ICADStylePanel panelPointColor;
    private JLabel labelPointSize;
    private JComboBox comboboxPointSize;
    private JButton buttonPointModel;
    // 线风格
    private ICADStylePanel panelLineStyle;
    private JLabel labelLineColor;
    private ICADStylePanel panelLineColor;
    private JLabel labelLineWidth;
    private JComboBox comboboxLineWidth;
    private JButton buttonLineModel;
    // 面风格
    private ICADStylePanel panelFillStyle;
    private JLabel labelFillForeColor;
    private ICADStylePanel panelFillForeColor;
    private JLabel labelFillOqaue;
    private JSpinner spinnerFillSize;
    private JButton buttonFillModel;
    // 关闭按钮
    private JButton buttonClose;
    private JPanel panelFill;
    private JPanel panelPoint;
    private JPanel panelLine;

    private static CADStyleDialog dialog;
    private Recordset recordset;
    private EditEnvironment environment;
    private boolean isDisposed;

    private String[] stringPointSizes = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "1.5", "3", "4", "5", "6", "7", "8", "9", "10", "15", "10"};
    private String[] stringLineWidths = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            disposeInfo();
        }
    };

    public CADStyleDialog(EditEnvironment environment) {
        super();
        this.environment = environment;
        this.setSize(300, 440);
        this.setTitle(MapEditorProperties.getString("String_CADStyle"));
        this.setLocationRelativeTo(null);
        initComponents();
        setModal(false);
    }

    public static CADStyleDialog getInstance(EditEnvironment environment) {
        if (null == dialog) {
            dialog = new CADStyleDialog(environment);
        }
        dialog.setVisible(true);
        return dialog;
    }

    public void showDialog(Recordset recordset) {
        if (null != this.recordset) {
            this.recordset.dispose();
        }
        this.recordset = recordset;
        initComponents();
        initResources();
        registEvents();
        setEnabled();
    }

    private void setPanelPointEnabled(boolean enabled) {
        this.panelPointStyle.setEnable(enabled);
        this.labelPointColor.setEnabled(enabled);
        this.panelPointColor.setEnable(enabled);
        this.labelPointSize.setEnabled(enabled);
        this.comboboxPointSize.setEnabled(enabled);
    }

    private void setPanelFillEnabled(boolean enabled) {
        this.panelFillStyle.setEnable(enabled);
        this.labelFillForeColor.setEnabled(enabled);
        this.panelFillForeColor.setEnable(enabled);
        this.labelFillOqaue.setEnabled(enabled);
        this.spinnerFillSize.setEnabled(enabled);
    }

    private void setPanelLineEnabled(boolean enabled) {
        this.panelLineStyle.setEnable(enabled);
        this.labelLineColor.setEnabled(enabled);
        this.labelLineWidth.setEnabled(enabled);
        this.panelLineColor.setEnable(enabled);
        this.comboboxLineWidth.setEnabled(enabled);
    }

    public void enabled(boolean enabled) {
        setPanelPointEnabled(enabled);
        setPanelLineEnabled(enabled);
        setPanelFillEnabled(enabled);
    }

    public void setEnabled() {
        recordset.moveFirst();
        while (!recordset.isEOF()) {
            if (GeometryUtilities.isPointGeometry(recordset.getGeometry())) {
                setPanelPointEnabled(true);
                panelPointStyle.setRecordset(recordset);
                panelPointColor.setRecordset(recordset);
//                this.comboboxPointSize.setSelectedItem(String.valueOf(recordset.getGeometry().getStyle().getMarkerSize().getWidth()));
            } else if (GeometryUtilities.isLineGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                panelLineStyle.setRecordset(recordset);
                panelLineColor.setRecordset(recordset);
//                this.comboboxLineWidth.setSelectedItem(String.valueOf(recordset.getGeometry().getStyle().getLineWidth()));
            } else if (GeometryUtilities.isRegionGeometry(recordset.getGeometry())){
                setPanelLineEnabled(true);
                setPanelFillEnabled(true);
                panelLineStyle.setRecordset(recordset);
                panelLineColor.setRecordset(recordset);
                panelFillStyle.setRecordset(recordset);
                panelFillForeColor.setRecordset(recordset);
//                this.spinnerFillSize.setValue(recordset.getGeometry().getStyle().getFillOpaqueRate());
            }else{
                this.getContentPane().removeAll();
                ((JPanel)this.getContentPane()).updateUI();
            }
            recordset.moveNext();
        }
    }


    private void registEvents() {
        removeEvents();
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeInfo();
            }
        };
        this.buttonClose.addActionListener(actionListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!isDisposed) {
                    disposeInfo();
                }
            }
        });
    }

    public void disposeInfo() {
        if (null != dialog) {
            removeEvents();
            dialog.dispose();
            dialog = null;
            recordset.dispose();
            isDisposed = true;
            environment.stopEditor();
        }
    }

    public void removeEvents() {
        this.buttonClose.removeActionListener(actionListener);
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
        JPanel panel = (JPanel) this.getContentPane();
        this.buttonClose = ComponentFactory.createButtonClose();
        panel.removeAll();
        panel.updateUI();
        initPanelPointComponents();
        initPanelLineComponents();
        initPanelFillComponents();
        panel.setLayout(new GridBagLayout());
        panel.add(panelPoint, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 10, 5).setWeight(1, 1));
        panel.add(panelLine, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 10, 5).setWeight(1, 1));
        panel.add(panelFill, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 0, 5).setWeight(1, 1));
        panel.add(this.buttonClose, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(5, 0, 10, 10));
    }

    private void initPanelFillComponents() {
        this.panelFillStyle = new CADStylePanel(CADStylePanel.FILL_TYPE, CADStylePanel.VERTICAL);
        this.labelFillForeColor = new JLabel();
        this.panelFillForeColor = new CADStylePanel(CADStylePanel.FILL_TYPE, CADStylePanel.HORIZONTAL);
        this.labelFillOqaue = new JLabel();
        this.spinnerFillSize = new JSpinner();
        this.spinnerFillSize.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        panelFill = new JPanel();
        panelFill.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_FillStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelFill.setLayout(new GridBagLayout());
        panelFill.add((JPanel) this.panelFillStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(10, 10));
        panelFill.add(this.labelFillForeColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 0, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add((JPanel) panelFillForeColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelFill.add(this.labelFillOqaue, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add(this.spinnerFillSize, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(50, 0));
        setPanelFillEnabled(false);
    }

    private void initPanelLineComponents() {
        this.panelLineStyle = new CADStylePanel(CADStylePanel.LINE_TYPE, CADStylePanel.VERTICAL);
        this.labelLineColor = new JLabel();
        this.panelLineColor = new CADStylePanel(CADStylePanel.LINE_TYPE, CADStylePanel.HORIZONTAL);
        this.labelLineWidth = new JLabel();
        this.comboboxLineWidth = new JComboBox();
        this.comboboxLineWidth.setModel(new DefaultComboBoxModel(this.stringLineWidths));
        this.comboboxLineWidth.setEditable(true);
        panelLine = new JPanel();
        panelLine.setBorder(new TitledBorder(null, MapEditorProperties.getString("Stirng_LineStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelLine.setLayout(new GridBagLayout());
        panelLine.add((JPanel) this.panelLineStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(20, 10));
        panelLine.add(this.labelLineColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 0, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLine.add((JPanel) panelLineColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelLine.add(this.labelLineWidth, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLine.add(this.comboboxLineWidth, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        setPanelLineEnabled(false);
    }

    private void initPanelPointComponents() {
        this.panelPointStyle = new CADStylePanel(CADStylePanel.MARKER_TYPE, CADStylePanel.VERTICAL);
        this.labelPointColor = new JLabel();
        this.panelPointColor = new CADStylePanel(CADStylePanel.MARKER_TYPE, CADStylePanel.HORIZONTAL);
        this.labelPointSize = new JLabel();
        this.comboboxPointSize = new JComboBox();
        this.comboboxPointSize.setModel(new DefaultComboBoxModel(stringPointSizes));
        this.comboboxPointSize.setEditable(true);
        panelPoint = new JPanel();
        panelPoint.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_PointStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelPoint.setLayout(new GridBagLayout());
        panelPoint.add((JPanel) this.panelPointStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(20, 10));
        panelPoint.add(this.labelPointColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPoint.add((JPanel) panelPointColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelPoint.add(this.labelPointSize, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPoint.add(this.comboboxPointSize, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setIpad(10, 0).setFill(GridBagConstraints.HORIZONTAL));
        setPanelPointEnabled(false);
    }

}
