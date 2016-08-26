package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

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
    private JTextField textFieldPointSize;
    private JButton buttonPointModel;
    // 线风格
    private ICADStylePanel panelLineStyle;
    private JLabel labelLineColor;
    private ICADStylePanel panelLineColor;
    private JLabel labelLineWidth;
    private JComboBox comboboxLineWidth;
    private JTextField textFieldLineWidth;
    private JButton buttonLineModel;
    // 面风格
    private ICADStylePanel panelFillStyle;
    private JLabel labelFillForeColor;
    private ICADStylePanel panelFillForeColor;
    private JLabel labelFillOqaue;
    private JSpinner spinnerFillOqaue;
    private JTextField textFieldFillOqaue;
    private JButton buttonFillModel;
    // 关闭按钮
    private JButton buttonClose;
    private JPanel panelFill;
    private JPanel panelPoint;
    private JPanel panelLine;
    private EditHistory editHistory;

    private static CADStyleDialog dialog;
    private Recordset recordset;
    private EditEnvironment environment;
    private boolean isDisposed;
    private String pointsize;
    private String lineWidth;
    private int fillOqaue;

    private String[] stringPointSizes = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "1.5", "3", "4", "5", "6", "7", "8", "9", "10", "15", "10"};
    private String[] stringLineWidths = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            disposeInfo();
        }
    };
    private boolean isResetFontSize;
    private boolean isResetLineWidth;
    private boolean isResetFillOqaue;
    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource().equals(comboboxPointSize)) {
                    setSymbolSize(1);
                } else if (e.getSource().equals(comboboxLineWidth)) {
                    setSymbolSize(2);
                }
            }
        }
    };
    private FocusListener symbolFocusListener = new FocusListener() {

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource().equals(textFieldPointSize)) {
                if (StringUtilities.isNullOrEmptyString(textFieldPointSize.getText()) || !StringUtilities.isNumber(textFieldPointSize.getText())) {
                    comboboxPointSize.setSelectedItem(pointsize);
                    isResetFontSize = true;
                } else if (StringUtilities.isNullOrEmptyString(textFieldLineWidth.getText()) || !StringUtilities.isNumber(textFieldLineWidth.getText())) {
                    comboboxLineWidth.setSelectedItem(lineWidth);
                    isResetLineWidth = true;
                } else {
                    spinnerFillOqaue.setValue(fillOqaue);
                    isResetFillOqaue = true;
                }
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource().equals(textFieldPointSize)) {
                isResetFontSize = false;
            } else if (e.getSource().equals(textFieldLineWidth)) {
                isResetLineWidth = false;
            } else {
                isResetFillOqaue = false;
            }
        }
    };
    private KeyListener keyListener = new LocalKeyListener();
    private ChangeListener spinnerFillOqaueListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            setSymbolSize(3);
        }
    };

    public CADStyleDialog(EditEnvironment environment) {
        super();
        this.environment = environment;
        this.isDisposed = false;
        this.setSize(300, 440);
        this.setTitle(MapEditorProperties.getString("String_CADStyle"));
        this.setLocationRelativeTo(null);
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
        setRecordset(recordset);
        editHistory = MapUtilities.getMapControl().getEditHistory();
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
        this.spinnerFillOqaue.setEnabled(enabled);
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
            } else if (GeometryUtilities.isLineGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                panelLineStyle.setRecordset(recordset);
                panelLineColor.setRecordset(recordset);
            } else if (GeometryUtilities.isRegionGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                setPanelFillEnabled(true);
                panelLineStyle.setRecordset(recordset);
                panelLineColor.setRecordset(recordset);
                panelFillStyle.setRecordset(recordset);
                panelFillForeColor.setRecordset(recordset);
            } else {
                this.getContentPane().removeAll();
                ((JPanel) this.getContentPane()).updateUI();
            }
            recordset.moveNext();
        }
    }


    private void registEvents() {
        removeEvents();
        this.comboboxPointSize.addItemListener(this.itemListener);
        this.textFieldPointSize.addFocusListener(this.symbolFocusListener);
        this.textFieldPointSize.addKeyListener(this.keyListener);
        this.comboboxLineWidth.addItemListener(this.itemListener);
        this.textFieldLineWidth.addFocusListener(this.symbolFocusListener);
        this.textFieldLineWidth.addKeyListener(this.keyListener);
        this.spinnerFillOqaue.addChangeListener(this.spinnerFillOqaueListener);
        this.textFieldFillOqaue.addFocusListener(this.symbolFocusListener);
        this.textFieldFillOqaue.addKeyListener(this.keyListener);
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
        this.comboboxPointSize.removeItemListener(this.itemListener);
        this.textFieldPointSize.removeFocusListener(this.symbolFocusListener);
        this.textFieldPointSize.removeKeyListener(this.keyListener);
        this.comboboxLineWidth.removeItemListener(this.itemListener);
        this.textFieldLineWidth.removeFocusListener(this.symbolFocusListener);
        this.textFieldLineWidth.removeKeyListener(this.keyListener);
        this.spinnerFillOqaue.removeChangeListener(this.spinnerFillOqaueListener);
        this.textFieldFillOqaue.removeFocusListener(this.symbolFocusListener);
        this.textFieldFillOqaue.removeKeyListener(this.keyListener);
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
        this.spinnerFillOqaue = new JSpinner();
        this.spinnerFillOqaue.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        this.textFieldFillOqaue = ((JSpinner.NumberEditor) spinnerFillOqaue.getEditor()).getTextField();
        if (null != recordset.getGeometry().getStyle()) {
            this.fillOqaue = recordset.getGeometry().getStyle().getFillOpaqueRate();
            this.spinnerFillOqaue.setValue(fillOqaue);
        }
        panelFill = new JPanel();
        panelFill.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_FillStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelFill.setLayout(new GridBagLayout());
        panelFill.add((JPanel) this.panelFillStyle, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(10, 10));
        panelFill.add(this.labelFillForeColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 0, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add((JPanel) panelFillForeColor, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1));
        panelFill.add(this.labelFillOqaue, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add(this.spinnerFillOqaue, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(50, 0));
        setPanelFillEnabled(false);
    }

    private void initPanelLineComponents() {
        this.panelLineStyle = new CADStylePanel(CADStylePanel.LINE_TYPE, CADStylePanel.VERTICAL);
        this.labelLineColor = new JLabel();
        this.panelLineColor = new CADStylePanel(CADStylePanel.LINE_TYPE, CADStylePanel.HORIZONTAL);
        this.labelLineWidth = new JLabel();
        this.comboboxLineWidth = new JComboBox();
        this.comboboxLineWidth.setModel(new DefaultComboBoxModel(this.stringLineWidths));
        this.textFieldLineWidth = (JTextField) this.comboboxLineWidth.getEditor().getEditorComponent();
        this.comboboxLineWidth.setEditable(true);
        if (null != recordset.getGeometry().getStyle()) {
            this.lineWidth = String.valueOf(recordset.getGeometry().getStyle().getLineWidth());
            this.comboboxLineWidth.setSelectedItem(this.lineWidth);
        }
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
        this.textFieldPointSize = (JTextField) this.comboboxPointSize.getEditor().getEditorComponent();
        this.comboboxPointSize.setEditable(true);
        if (null != recordset.getGeometry().getStyle()) {
            this.pointsize = String.valueOf(recordset.getGeometry().getStyle().getMarkerSize().getWidth());
            this.comboboxPointSize.setSelectedItem(this.pointsize);
        }
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

    public boolean isDisposed() {
        return isDisposed;
    }

    public void setDisposed(boolean disposed) {
        isDisposed = disposed;
    }

    class LocalKeyListener extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getSource().equals(textFieldPointSize) && !isResetFontSize) {
                setSymbolSize(1);
            } else if (e.getSource().equals(textFieldLineWidth) && !isResetLineWidth) {
                setSymbolSize(2);
            } else {
                setSymbolSize(3);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            int keyChar = e.getKeyChar();
            if (!e.getSource().equals(textFieldFillOqaue)) {
                // 可输入double型
                if (keyChar != '.' && (keyChar < '0' || keyChar > '9')) {
                    e.consume();
                }
            } else {
                // 输入int型
                if (keyChar < '0' || keyChar > '9') {
                    e.consume();
                }
            }
        }

    }

    private void setSymbolSize(int type) {
        try {
            Object newSize = null;
            if (type == 1) {
                newSize = textFieldPointSize.getText();
            } else if (type == 2) {
                newSize = textFieldLineWidth.getText();
            } else {
                newSize = textFieldFillOqaue.getText();
            }
            if (StringUtilities.isNumber(newSize.toString())) {
                //修改点大小
                recordset.moveFirst();
                while (!recordset.isEOF()) {
                    editHistory.add(EditType.MODIFY, recordset, true);
                    recordset.edit();
                    Geometry tempGeometry = recordset.getGeometry();
                    GeoStyle geoStyle = new GeoStyle();
                    if (null != geoStyle) {
                        geoStyle = tempGeometry.getStyle();
                    }
                    if (type == 1) {
                        // 设置点符号大小
                        geoStyle.setMarkerSize(new Size2D(Double.valueOf(newSize.toString()), Double.valueOf(newSize.toString())));
                        pointsize = newSize.toString();
                    } else if (type == 2) {
                        // 设置线宽
                        geoStyle.setLineWidth(Double.valueOf(newSize.toString()));
                        lineWidth = newSize.toString();
                    } else {
                        // 设置透明度
                        geoStyle.setFillOpaqueRate(Integer.valueOf(newSize.toString()));
                        this.fillOqaue = Integer.valueOf(newSize.toString());
                    }
                    tempGeometry.setStyle(geoStyle);
                    recordset.setGeometry(tempGeometry);
                    tempGeometry.dispose();
                    recordset.update();
                    recordset.moveNext();
                }
                editHistory.batchEnd();
                MapUtilities.getActiveMap().refresh();
            }
        } catch (Exception e2) {
            Application.getActiveApplication().getOutput().output(e2);
        }
    }

    public void setRecordset(Recordset recordset) {
        if (null != this.recordset) {
            this.recordset.dispose();
        }
        this.recordset = recordset;
    }
}

