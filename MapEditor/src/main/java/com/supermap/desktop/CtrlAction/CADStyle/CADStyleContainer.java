package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.ColorSelectButton;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by xie on 2016/8/25.
 */
public class CADStyleContainer extends JPanel {

    private JPanel panelCADInfo;
    private JScrollPane scrollPane;
    // 点风格
    private ICADStylePanel panelPointStyle;
    private JLabel labelPointColor;
    //    private ICADStylePanel panelPointColor;
    private ColorSelectButton buttonPointColor;
    private JLabel labelPointSize;
    private JComboBox comboboxPointSize;
    private JTextField textFieldPointSize;
    private JButton buttonPointModel;
    private JLabel labelPointWidth;// 点宽度
    private JSpinner spinnerPointWidth;
    private JLabel labelPointWidthUnity;
    private JLabel labelPointHeight; // 点高度
    private JSpinner spinnerPointHeight;
    private JLabel labelPointHeightUnity;
    private JCheckBox checkboxWAndH;//固定宽高比
    private JLabel labelPointRotation;//点旋转角度
    private JSpinner spinnerPointRotation;
    private JLabel labelPointRotationUnity;
    private JLabel labelPointOpaque;//点透明度
    private JSpinner spinnerPointOpaque;
    private JLabel labelPointOpaqueUnity;
    // 线风格
    private ICADStylePanel panelLineStyle;
    private JLabel labelLineColor;
    //    private ICADStylePanel panelLineColor;
    private ColorSelectButton buttonLineColor;//线颜色
    private JLabel labelLineWidth;
    private JComboBox comboboxLineWidth;//线宽
    private JTextField textFieldLineWidth;
    private JButton buttonLineModel;
    // 面风格
    private ICADStylePanel panelFillStyle;
    private JLabel labelFillForeColor;
    //    private ICADStylePanel panelFillForeColor;
    private ColorSelectButton buttonFillForeColor;//前景色
    private JLabel labelFillBackColor;
    private ColorSelectButton buttonFillBackColor;//背景色
    private JCheckBox checkboxBackOpaque;//背景透明
    private CompTitledPane panelFillGradient;// 渐变填充
    private JCheckBox checkboxFillGradient;
    private JLabel labelFillGriadientModel;// 渐变填充类型
    private JComboBox comboBoxFillGriadientModel;
    private JLabel labelFillGriadientOffsetX;// 渐变填充水平偏移量
    private JSpinner spinnerFillGriadientOffsetX;
    private JLabel labelFillGriadientOffsetXUnity;
    private JLabel labelFillGriadientOffsetY;// 渐变填充垂直偏移量
    private JSpinner spinnerFillGriadientOffsetY;
    private JLabel labelFillGriadientOffsetYUnity;
    private JLabel labelFillGriadientAngel;// 渐变填充旋转角度
    private JSpinner spinnerFillGriadientAngel;
    private JLabel labelFillGriadientAngelUnity;

    private JLabel labelFillOpaque;
    private JSpinner spinnerFillOpaque;
    private JLabel labelFillOpaqueUnity;

    private JTextField textFieldFillOpaque;
    private JButton buttonFillModel;

    private JPanel panelFill;
    private JPanel panelPoint;
    private JPanel panelLine;
    private EditHistory editHistory;

    private Recordset recordset;
    private boolean isDisposed;
    private String pointsize;
    private String lineWidth;
    private int fillOqaue;

    private String[] stringPointSizes = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "1.5", "3", "4", "5", "6", "7", "8", "9", "10", "15", "10"};
    private String[] stringLineWidths = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
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
                    spinnerFillOpaque.setValue(fillOqaue);
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

    public CADStyleContainer() {
        super();
        this.isDisposed = false;
        this.setLayout(new GridBagLayout());
        this.scrollPane = new JScrollPane();
        this.panelCADInfo = new JPanel();
        this.scrollPane.setBorder(new LineBorder(Color.lightGray));
        this.add(this.scrollPane, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
        this.scrollPane.setViewportView(this.panelCADInfo);
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
//        this.panelPointColor.setEnable(enabled);
        this.buttonPointColor.setEnabled(enabled);
//        this.labelPointSize.setEnabled(enabled);
//        this.comboboxPointSize.setEnabled(enabled);
    }

    private void setPanelFillEnabled(boolean enabled) {
        this.panelFillStyle.setEnable(enabled);
        this.labelFillForeColor.setEnabled(enabled);
//        this.panelFillForeColor.setEnable(enabled);
        this.buttonFillForeColor.setEnabled(enabled);
        this.labelFillOpaque.setEnabled(enabled);
        this.spinnerFillOpaque.setEnabled(enabled);
    }

    private void setPanelLineEnabled(boolean enabled) {
        this.panelLineStyle.setEnable(enabled);
        this.labelLineColor.setEnabled(enabled);
        this.labelLineWidth.setEnabled(enabled);
//        this.panelLineColor.setEnable(enabled);
        this.buttonLineColor.setEnabled(enabled);
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
//                panelPointColor.setRecordset(recordset);
            } else if (GeometryUtilities.isLineGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                panelLineStyle.setRecordset(recordset);
//                panelLineColor.setRecordset(recordset);
            } else if (GeometryUtilities.isRegionGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                setPanelFillEnabled(true);
                panelLineStyle.setRecordset(recordset);
//                panelLineColor.setRecordset(recordset);
                panelFillStyle.setRecordset(recordset);
//                panelFillForeColor.setRecordset(recordset);
            } else {
                // 设置为空界面
//                this.getContentPane().removeAll();
//                ((JPanel) this.getContentPane()).updateUI();
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
        this.spinnerFillOpaque.addChangeListener(this.spinnerFillOqaueListener);
        this.textFieldFillOpaque.addFocusListener(this.symbolFocusListener);
        this.textFieldFillOpaque.addKeyListener(this.keyListener);
    }

    private void removePanels() {
        if (null != panelCADInfo) {
            remove(panelCADInfo);
        }
        for (int i = getComponents().length - 1; i >= 0; i--) {
            if (getComponent(i) instanceof JPanel) {
                remove(getComponent(i));
            }
        }
    }

    public void removeEvents() {
        this.comboboxPointSize.removeItemListener(this.itemListener);
        this.textFieldPointSize.removeFocusListener(this.symbolFocusListener);
        this.textFieldPointSize.removeKeyListener(this.keyListener);
        this.comboboxLineWidth.removeItemListener(this.itemListener);
        this.textFieldLineWidth.removeFocusListener(this.symbolFocusListener);
        this.textFieldLineWidth.removeKeyListener(this.keyListener);
        this.spinnerFillOpaque.removeChangeListener(this.spinnerFillOqaueListener);
        this.textFieldFillOpaque.removeFocusListener(this.symbolFocusListener);
        this.textFieldFillOpaque.removeKeyListener(this.keyListener);
    }

    private void initResources() {
        this.labelPointRotation.setText(ControlsProperties.getString("String_RotationAngle") + ":");
        this.labelPointRotationUnity.setText(CommonProperties.getString("String_AngleUnit_Degree"));
        this.labelPointOpaque.setText(ControlsProperties.getString("String_Label_Transparence"));
        this.labelPointOpaqueUnity.setText("%");
        this.labelPointWidth.setText(ControlsProperties.getString("String_ShowWidth"));
        this.labelPointWidthUnity.setText("mm");
        this.labelPointHeight.setText(ControlsProperties.getString("String_ShowHeight"));
        this.labelPointHeightUnity.setText("mm");
        this.checkboxWAndH.setText(ControlsProperties.getString("String_CheckBox_ChangeMarkerWidthAndHeight"));
        this.labelPointColor.setText(ControlsProperties.getString("String_SymbolColor") + ":");

        this.labelLineColor.setText(ControlsProperties.getString("String_Column_Color") + ":");
        this.labelLineWidth.setText(ControlsProperties.getString("String_GeometryPropertyStyle3DControl_labelLineWidth"));

        this.labelFillForeColor.setText(CoreProperties.getString("String_ForColor") + ":");
        this.labelFillBackColor.setText(CoreProperties.getString("String_BackColor") + ":");
        this.checkboxBackOpaque.setText(CoreProperties.getString("String_BackOpaque"));
        this.labelFillOpaque.setText(CoreProperties.getString("String_Label_OpaqueRate"));
        this.labelFillOpaqueUnity.setText("%");
        this.checkboxFillGradient.setText(CoreProperties.getString("String_Label_FillGradient"));
        this.labelFillGriadientModel.setText(ControlsProperties.getString("String_Label_Type"));
        this.labelFillGriadientOffsetX.setText(CoreProperties.getString("String_Label_XOffsetRate"));
        this.labelFillGriadientOffsetXUnity.setText("%");
        this.labelFillGriadientOffsetY.setText(CoreProperties.getString("String_Label_YOffsetRate"));
        this.labelFillGriadientOffsetYUnity.setText("%");
        this.labelFillGriadientAngel.setText(CoreProperties.getString("String_GeometryDrawingParam_Angle"));
        this.labelFillGriadientAngelUnity.setText(CoreProperties.getString("String_Degree_Format_Degree"));
        this.labelFillOpaque.setText(MapEditorProperties.getString("String_Oqaue_I"));
    }

    public void setNullPanel() {
//        this.isModify = false;
        removePanels();
        this.scrollPane.setViewportView(panelCADInfo);
    }

    private void initComponents() {
        initPanelPointComponents();
        initPanelLineComponents();
        initPanelFillComponents();
        removePanels();
        JPanel panelText = new JPanel();
        panelText.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.add(panelText, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
        panelText.add(panelPoint, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 10, 5).setWeight(1, 1));
        panelText.add(panelLine, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 10, 5).setWeight(1, 1));
        panelText.add(panelFill, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 0, 5).setWeight(1, 1));
        this.scrollPane.setViewportView(panel);
        repaint();
    }

    private void initPanelFillComponents() {
        this.panelFillStyle = new CADStylePanel(CADStylePanel.FILL_TYPE);
        this.labelFillForeColor = new JLabel();
        this.buttonFillForeColor = new ColorSelectButton(recordset.getGeometry().getStyle().getFillForeColor());
        this.labelFillBackColor = new JLabel();
        this.buttonFillBackColor = new ColorSelectButton(recordset.getGeometry().getStyle().getFillBackColor());
        this.checkboxBackOpaque = new JCheckBox();
        this.labelFillOpaque = new JLabel();
        this.spinnerFillOpaque = new JSpinner();
        this.spinnerFillOpaque.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        this.textFieldFillOpaque = ((JSpinner.NumberEditor) spinnerFillOpaque.getEditor()).getTextField();
        if (null != recordset.getGeometry().getStyle()) {
            this.fillOqaue = recordset.getGeometry().getStyle().getFillOpaqueRate();
            this.spinnerFillOpaque.setValue(fillOqaue);
        }
        this.labelFillOpaqueUnity = new JLabel();

        JPanel panelFillGradient = new JPanel();
        this.checkboxFillGradient = new JCheckBox();
        this.labelFillGriadientModel = new JLabel();
        this.comboBoxFillGriadientModel = new JComboBox();
        this.labelFillGriadientOffsetX = new JLabel();
        this.spinnerFillGriadientOffsetX = new JSpinner();
        this.spinnerFillGriadientOffsetX.setModel(new SpinnerNumberModel(0, -100, 100, 1));
        this.labelFillGriadientOffsetXUnity = new JLabel();
        this.labelFillGriadientOffsetY = new JLabel();
        this.spinnerFillGriadientOffsetY = new JSpinner();
        this.spinnerFillGriadientOffsetY.setModel(new SpinnerNumberModel(0, -100, 100, 1));
        this.labelFillGriadientOffsetYUnity = new JLabel();
        this.labelFillGriadientAngel = new JLabel();
        this.spinnerFillGriadientAngel = new JSpinner();
        this.spinnerFillGriadientAngel.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        this.labelFillGriadientAngelUnity = new JLabel();

        panelFillGradient.setLayout(new GridBagLayout());
        panelFillGradient.add(this.labelFillGriadientModel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 10, 0).setWeight(0, 1));
        panelFillGradient.add(this.comboBoxFillGriadientModel, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillGradient.add(this.labelFillGriadientOffsetX, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1));
        panelFillGradient.add(this.spinnerFillGriadientOffsetX, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillGradient.add(this.labelFillGriadientOffsetXUnity, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 10).setWeight(0, 1));
        panelFillGradient.add(this.labelFillGriadientOffsetY, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1));
        panelFillGradient.add(this.spinnerFillGriadientOffsetY, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillGradient.add(this.labelFillGriadientOffsetYUnity, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 10).setWeight(0, 1));
        panelFillGradient.add(this.labelFillGriadientAngel, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1));
        panelFillGradient.add(this.spinnerFillGriadientAngel, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillGradient.add(this.labelFillGriadientAngelUnity, new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 0).setWeight(0, 1));
        this.panelFillGradient = new CompTitledPane(this.checkboxFillGradient, panelFillGradient);

        panelFill = new JPanel();
        panelFill.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_FillStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelFill.setLayout(new GridBagLayout());
        panelFill.add((JPanel) this.panelFillStyle, new GridBagConstraintsHelper(0, 0, 1, 4).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setIpad(10, 10));
        panelFill.add(this.labelFillForeColor, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(0, 1));
        panelFill.add(this.buttonFillForeColor, new GridBagConstraintsHelper(2, 0, 3, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(5, 10, 5, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add(this.labelFillBackColor, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(0, 1));
        panelFill.add(this.buttonFillBackColor, new GridBagConstraintsHelper(2, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add(this.checkboxBackOpaque, new GridBagConstraintsHelper(1, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(1, 1));
        panelFill.add(this.labelFillOpaque, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(0, 1));
        panelFill.add(this.spinnerFillOpaque, new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add(this.labelFillOpaqueUnity, new GridBagConstraintsHelper(4, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(0, 1));
        panelFill.add(this.panelFillGradient, new GridBagConstraintsHelper(5, 0, 1, 4).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        setPanelFillEnabled(false);
    }

    private void initPanelLineComponents() {
        this.panelLineStyle = new CADStylePanel(CADStylePanel.LINE_TYPE);
        this.labelLineColor = new JLabel();
//        this.panelLineColor = new CADStylePanel(CADStylePanel.LINE_TYPE, CADStylePanel.HORIZONTAL);
        this.buttonLineColor = new ColorSelectButton(recordset.getGeometry().getStyle().getLineColor());
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
        panelLine.add(this.labelLineColor, new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLine.add(this.buttonLineColor, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLine.add(this.labelLineWidth, new GridBagConstraintsHelper(5, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLine.add(this.comboboxLineWidth, new GridBagConstraintsHelper(6, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        setPanelLineEnabled(false);
    }

    private void initPanelPointComponents() {
        this.panelPointStyle = new CADStylePanel(CADStylePanel.MARKER_TYPE);
        this.labelPointColor = new JLabel();
        this.labelPointRotation = new JLabel();
        this.spinnerPointRotation = new JSpinner();
        this.spinnerPointRotation.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        this.labelPointRotationUnity = new JLabel();
        this.labelPointOpaque = new JLabel();
        this.spinnerPointOpaque = new JSpinner();
        this.spinnerPointOpaque.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        this.labelPointOpaqueUnity = new JLabel();
        this.labelPointWidth = new JLabel();
        this.spinnerPointWidth = new JSpinner();
        this.spinnerPointWidth.setModel(new SpinnerNumberModel(0.0, 0.0, null, 1.0));
        this.labelPointWidthUnity = new JLabel();
        this.labelPointHeight = new JLabel();
        this.spinnerPointHeight = new JSpinner();
        this.spinnerPointHeight.setModel(new SpinnerNumberModel(0.0, 0.0, null, 1.0));
        this.labelPointHeightUnity = new JLabel();
        this.checkboxWAndH = new JCheckBox();
        this.buttonPointColor = new ColorSelectButton(recordset.getGeometry().getStyle().getLineColor());
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
        panelPoint.add(this.labelPointRotation, new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 5, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPoint.add(this.spinnerPointRotation, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));
        panelPoint.add(this.labelPointRotationUnity, new GridBagConstraintsHelper(8, 0, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(10, 0, 10, 10));
        panelPoint.add(this.labelPointOpaque, new GridBagConstraintsHelper(5, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));
        panelPoint.add(this.spinnerPointOpaque, new GridBagConstraintsHelper(6, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));
        panelPoint.add(this.labelPointOpaqueUnity, new GridBagConstraintsHelper(8, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

        panelPoint.add(this.checkboxWAndH, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPoint.add(this.labelPointColor, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1));
        panelPoint.add(this.buttonPointColor, new GridBagConstraintsHelper(1, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));

        panelPoint.add(this.labelPointWidth, new GridBagConstraintsHelper(5, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1));
        panelPoint.add(this.spinnerPointWidth, new GridBagConstraintsHelper(6, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));
        panelPoint.add(this.labelPointWidthUnity, new GridBagConstraintsHelper(8, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPoint.add(this.labelPointHeight, new GridBagConstraintsHelper(5, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 0).setWeight(0, 1));
        panelPoint.add(this.spinnerPointHeight, new GridBagConstraintsHelper(6, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setIpad(20, 0));
        panelPoint.add(this.labelPointHeightUnity, new GridBagConstraintsHelper(8, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

        setPanelPointEnabled(false);
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
            if (!e.getSource().equals(textFieldFillOpaque)) {
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
                newSize = textFieldFillOpaque.getText();
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
