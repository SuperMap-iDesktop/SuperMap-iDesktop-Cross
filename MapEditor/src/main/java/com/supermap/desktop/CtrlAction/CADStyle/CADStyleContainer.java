package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.SymbolMarkerSizeController;
import com.supermap.desktop.dialog.symbolDialogs.SymbolSpinnerUtilties;
import com.supermap.desktop.enums.SymbolMarkerType;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.ColorSelectButton;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.MapUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xie on 2016/8/25.
 *
 * @see com.supermap.desktop.ui.controls.textStyle.TextBasicPanel
 * @see SymbolMarkerSizeController
 */
public class CADStyleContainer extends JPanel {

    private JPanel panelCADInfo;
    private JScrollPane scrollPane;
    // 点风格
    private CADStyleTitlePanel panelPointStyle;
    private JLabel labelPointColor;
    private ColorSelectButton buttonPointColor;
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
    private CADStyleTitlePanel panelLineStyle;
    private JLabel labelLineColor;
    private ColorSelectButton buttonLineColor;//线颜色
    private JLabel labelLineWidth;
    private JComboBox comboboxLineWidth;//线宽
    private JButton buttonLineModel;
    // 面风格
    private CADStyleTitlePanel panelFillStyle;
    private JLabel labelFillForeColor;
    private ColorSelectButton buttonFillForeColor;//前景色
    private JLabel labelFillBackColor;
    private ColorSelectButton buttonFillBackColor;//背景色
    private JCheckBox checkboxBackOpaque;//背景透明
    private JCheckBox checkboxFillGradient;// 渐变填充
    private JLabel labelFillGradientModel;// 渐变填充类型
    private JComboBox comboBoxFillGradientModel;
    private JLabel labelFillGradientOffsetX;// 渐变填充水平偏移量
    private JSpinner spinnerFillGradientOffsetX;
    private JLabel labelFillGradientOffsetXUnity;
    private JLabel labelFillGradientOffsetY;// 渐变填充垂直偏移量
    private JSpinner spinnerFillGradientOffsetY;
    private JLabel labelFillGradientOffsetYUnity;
    private JLabel labelFillGradientAngel;// 渐变填充旋转角度
    private JSpinner spinnerFillGradientAngel;
    private JLabel labelFillGradientAngelUnity;

    private JLabel labelFillOpaque;
    private JSpinner spinnerFillOpaque;// 面透明度
    private JLabel labelFillOpaqueUnity;
    private JButton buttonFillModel;

    private JPanel panelFill;
    private JPanel panelPoint;
    private JPanel panelLine;
    private JPanel panelParent;
    private JTabbedPane panelContaints;
    private TextStyleContainer panelText;
    private EditHistory editHistory;

    private String lineWidth;
    private int fillOqaue;

    private final Color wrongColor = Color.red;
    private final Color defaultColor = Color.BLACK;
    private final double pow = 2;

    private JFormattedTextField textFieldPointRotation;
    private JFormattedTextField textFieldPointOpaque;
    private JFormattedTextField textFieldFillOpaque;
    private JFormattedTextField textFieldPointWidth;
    private JFormattedTextField textFieldPointHeight;
    private JFormattedTextField textFieldFillGradientOffsetX;
    private JFormattedTextField textFieldFillGradientOffsetY;
    private JFormattedTextField textFieldFillGradientAngel;
    private JTextField textFieldLineWidth;
    private SymbolMarkerSizeController symbolMarkerSizeController;
    private boolean isSizeListenersEnable = true;

    private final String[] stringPointSizes = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "1.5", "3", "4", "5", "6", "7", "8", "9", "10", "15", "10"};
    private final String[] stringLineWidths = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final String[] stringGradientModel = new String[]{CoreProperties.getString("String_LINEAR"), CoreProperties.getString("String_RADIAL"), CoreProperties.getString("String_SQUARE")};
    private boolean modify;
    private GeoStyle geoPointStyle;
    private GeoStyle geoLineStyle;
    private GeoStyle geoRegionStyle;
    private Geometry geoText;
    private static final int TAB_MARKER = 0;
    private static final int TAB_LINE = 1;
    private static final int TAB_REGION = 2;
    private static final int TAB_TEXT = 3;

    private PropertyChangeListener propertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Color currentColor = ((ColorSelectButton) evt.getSource()).getColor();
            try {
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        // FIXME: 2016/12/30 批量修改cad风格异常处理
                        if (!(tempGeometry instanceof GeoText)) {
                            GeoStyle geoStyle = tempGeometry.getStyle();
                            if (null == geoStyle) {
                                geoStyle = new GeoStyle();
                            }
                            if (evt.getSource().equals(buttonPointColor) && GeometryUtilities.isPointGeometry(tempGeometry)) {
                                // 修改点颜色
                                geoStyle.setLineColor(currentColor);
                                count++;
                            } else if (evt.getSource().equals(buttonLineColor) && (GeometryUtilities.isRegionGeometry(tempGeometry) || GeometryUtilities.isLineGeometry(tempGeometry))) {
                                // 修改线颜色
                                geoStyle.setLineColor(currentColor);
                                count++;
                            } else if (evt.getSource().equals(buttonFillForeColor)) {
                                // 修改面的前景色
                                geoStyle.setFillForeColor(currentColor);
                                count++;
                            } else if (evt.getSource().equals(buttonFillBackColor)) {
                                // 修改面的背景色
                                geoStyle.setFillBackColor(currentColor);
                                count++;
                            }
                            if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                                tempGeometry.setStyle(geoStyle);
                            }
                            recordset.setGeometry(tempGeometry);
                            tempGeometry.dispose();
                            recordset.update();
                        }
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception e2) {
                Application.getActiveApplication().getOutput().output(e2);
            }

        }
    };


    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int index = comboBoxFillGradientModel.getSelectedIndex();
                try {
                    ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                    if (null == recordsets) {
                        return;
                    }
                    int recordsetCount = recordsets.size();
                    int count = 0;
                    editHistory = MapUtilities.getMapControl().getEditHistory();
                    for (int i = 0; i < recordsetCount; i++) {
                        Recordset recordset = recordsets.get(i);
                        recordset.moveFirst();
                        while (!recordset.isEOF()) {
                            editHistory.add(EditType.MODIFY, recordset, true);
                            recordset.edit();
                            Geometry tempGeometry = recordset.getGeometry();
                            GeoStyle geoStyle = tempGeometry.getStyle();
                            if (null == geoStyle) {
                                geoStyle = new GeoStyle();
                            }
                            resetFillModel(index, geoStyle);
                            count++;
                            if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                                tempGeometry.setStyle(geoStyle);
                            }
                            recordset.setGeometry(tempGeometry);
                            tempGeometry.dispose();
                            recordset.update();
                            recordset.moveNext();
                        }
                        recordset.dispose();
                    }
                    if (count > 0) {
                        editHistory.batchEnd();
                        MapUtilities.getActiveMap().refresh();
                        modify = true;
                    }
                } catch (Exception ex) {
                    Application.getActiveApplication().getOutput().output(ex);
                }
            }
        }
    };

    private ChangeListener pointRotationListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isResetPointRotation) {
                rotationUpdate();
            }
        }

        private void rotationUpdate() {
            try {
                String text = textFieldPointRotation.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
                    textFieldPointRotation.setForeground(wrongColor);
                    return;
                } else {
                    textFieldPointRotation.setForeground(defaultColor);
                }
                double rotation = Double.valueOf(text);
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = tempGeometry.getStyle();
                        if (null == geoStyle) {
                            geoStyle = new GeoStyle();
                        }
                        if (!DoubleUtilities.equals(rotation, geoStyle.getMarkerAngle(), pow)) {
                            //设置点旋转角度
                            geoStyle.setMarkerAngle(rotation);
                            count++;
                        } else {
                            break;
                        }
                        if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                            tempGeometry.setStyle(geoStyle);
                        }
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private ChangeListener opaqueListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if ((e.getSource().equals(spinnerPointOpaque) && isResetPointOpaque) || (e.getSource().equals(spinnerFillOpaque) && isResetFillOpaque)) {
                fillOpaqueRateUpdate(e);
            }
        }

        public void fillOpaqueRateUpdate(ChangeEvent e) {
            try {
                JTextField parent = ((JSpinner.NumberEditor) ((JSpinner) e.getSource()).getEditor()).getTextField();
                String text = parent.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(0, 100, text)) {
                    parent.setForeground(wrongColor);
                    return;
                } else {
                    parent.setForeground(defaultColor);
                }
                int opaque = Integer.valueOf(text);
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = tempGeometry.getStyle();
                        if (null == geoStyle) {
                            geoStyle = new GeoStyle();
                        }
                        if (geoStyle.getFillOpaqueRate() != opaque) {
                            //设置点/面透明度
                            if ((parent.equals(textFieldPointOpaque) && GeometryUtilities.isPointGeometry(tempGeometry)) ||
                                    parent.equals(textFieldFillOpaque) && GeometryUtilities.isRegionGeometry(tempGeometry)) {
                                geoStyle.setFillOpaqueRate(100 - opaque);
                                count++;
                            }
                        } else {
                            break;
                        }
                        if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                            tempGeometry.setStyle(geoStyle);
                        }
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private CaretListener pointSizeListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (isResetPointSize) {
                markerSizeUpdate(e);
            }
        }

        private void markerSizeUpdate(CaretEvent e) {
            try {
                JFormattedTextField parent = (JFormattedTextField) e.getSource();
                String text = parent.getText();
                if (isSizeListenersEnable) {
                    // 重新设置
                    if (!SymbolSpinnerUtilties.isLegitNumber(-500d, 500d, text)) {
                        parent.setForeground(wrongColor);
                        return;
                    } else {
                        parent.setForeground(defaultColor);
                    }
                    setSizeControllerSize(parent, Double.valueOf(text));
                    int count = 0;
                    Size2D newSize = new Size2D();
                    double width = Double.valueOf(textFieldPointWidth.getText());
                    double height = Double.valueOf(textFieldPointHeight.getText());
                    newSize.setHeight(height);
                    newSize.setWidth(width);
                    ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                    if (null == recordsets) {
                        return;
                    }
                    int recordsetCount = recordsets.size();
                    editHistory = MapUtilities.getMapControl().getEditHistory();
                    for (int i = 0; i < recordsetCount; i++) {
                        Recordset recordset = recordsets.get(i);
                        recordset.moveFirst();
                        while (!recordset.isEOF()) {
                            editHistory.add(EditType.MODIFY, recordset, true);
                            recordset.edit();
                            Geometry tempGeometry = recordset.getGeometry();
                            GeoStyle geoStyle = tempGeometry.getStyle();
                            if (null == geoStyle) {
                                geoStyle = new GeoStyle();
                            }
                            if (!newSize.equals(geoStyle.getMarkerSize())) {
                                //设置点符号大小
                                geoStyle.setMarkerSize(newSize);
                                count++;
                            } else {
                                break;
                            }
                            if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                                tempGeometry.setStyle(geoStyle);
                            }
                            recordset.setGeometry(tempGeometry);
                            tempGeometry.dispose();
                            recordset.update();
                            recordset.moveNext();
                        }
                        recordset.dispose();
                    }
                    if (count > 0) {
                        editHistory.batchEnd();
                        MapUtilities.getActiveMap().refresh();
                        modify = true;
                    }
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private ChangeListener fillGradientOffsetListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if ((e.getSource().equals(spinnerFillGradientOffsetX) && isResetFillGradientOffsetX) || (e.getSource().equals(spinnerFillGradientOffsetY) && isResetFillGradientOffsetY)) {
                gradientOffsetRatioUpdate(e);
            }
        }

        private void gradientOffsetRatioUpdate(ChangeEvent e) {
            try {
                JTextField parent = ((JSpinner.NumberEditor) ((JSpinner) e.getSource()).getEditor()).getTextField();
                String text = parent.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(-100d, 100d, text)) {
                    parent.setForeground(wrongColor);
                    return;
                } else {
                    parent.setForeground(defaultColor);
                }
                double newOffset = Double.valueOf(text);
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = tempGeometry.getStyle();
                        if (null == geoStyle) {
                            geoStyle = new GeoStyle();
                        }
                        if (parent.equals(textFieldFillGradientOffsetX) && !DoubleUtilities.equals(newOffset, geoStyle.getFillGradientOffsetRatioX(), pow)) {
                            // 修改水平渐变偏移量
                            geoStyle.setFillGradientOffsetRatioX(newOffset);
                            count++;
                        } else if (parent.equals(textFieldFillGradientOffsetY) && !DoubleUtilities.equals(newOffset, geoStyle.getFillGradientOffsetRatioX(), pow)) {
                            // 修改垂直渐变偏移量
                            geoStyle.setFillGradientOffsetRatioY(newOffset);
                            count++;
                        } else {
                            break;
                        }
                        if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                            tempGeometry.setStyle(geoStyle);
                        }
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }

    };

    private ActionListener lockListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            symbolMarkerSizeController.setLockSelected(checkboxWAndH.isSelected());
        }
    };
    private ChangeListener fillGradientAngelListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isResetFillGradientAngel) {
                fillGradientAngleUpdate();
            }
        }

        private void fillGradientAngleUpdate() {
            try {
                String text = textFieldFillGradientAngel.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
                    textFieldFillGradientAngel.setForeground(wrongColor);
                    return;
                } else {
                    textFieldFillGradientAngel.setForeground(defaultColor);
                }
                double newAngel = Double.valueOf(text);
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = tempGeometry.getStyle();
                        if (null == geoStyle) {
                            geoStyle = new GeoStyle();
                        }
                        if (!DoubleUtilities.equals(newAngel, geoStyle.getFillGradientAngle(), pow)) {
                            geoStyle.setFillGradientAngle(newAngel);
                            count++;
                        } else {
                            break;
                        }
                        if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                            tempGeometry.setStyle(geoStyle);
                        }
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private ItemListener lineWidthListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && isResetLineWidth) {
                lineWidthUpdate();
            }
        }

        private void lineWidthUpdate() {
            try {
                String text = textFieldLineWidth.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(0d, 20d, text)) {
                    textFieldLineWidth.setForeground(wrongColor);
                    return;
                } else {
                    textFieldLineWidth.setForeground(defaultColor);
                }
                double newLineWidth = Double.valueOf(text);
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = tempGeometry.getStyle();
                        if (null == geoStyle) {
                            geoStyle = new GeoStyle();
                        }
                        if (!DoubleUtilities.equals(newLineWidth, geoStyle.getLineWidth(), pow)) {
                            geoStyle.setLineWidth(newLineWidth);
                            count++;
                        } else {
                            break;
                        }
                        if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                            tempGeometry.setStyle(geoStyle);
                        }
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private boolean isResetFillOpaque;
    private boolean isResetPointSize;
    private boolean isResetFillGradientAngel;
    private boolean isResetFillGradientOffsetX;
    private boolean isResetFillGradientOffsetY;
    private boolean isResetPointOpaque;
    private boolean isResetPointRotation;
    private boolean isResetLineWidth;
    private FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource().equals(textFieldFillOpaque)) {
                isResetFillOpaque = true;
            } else if (e.getSource().equals(textFieldPointHeight)) {
                isResetPointSize = true;
            } else if (e.getSource().equals(textFieldPointWidth)) {
                isResetPointSize = true;
            } else if (e.getSource().equals(textFieldFillGradientAngel)) {
                isResetFillGradientAngel = true;
            } else if (e.getSource().equals(textFieldFillGradientOffsetX)) {
                isResetFillGradientOffsetX = true;
            } else if (e.getSource().equals(textFieldFillGradientOffsetY)) {
                isResetFillGradientOffsetY = true;
            } else if (e.getSource().equals(textFieldPointOpaque)) {
                isResetPointOpaque = true;
            } else if (e.getSource().equals(textFieldPointRotation)) {
                isResetPointRotation = true;
            } else if (e.getSource().equals(textFieldLineWidth)) {
                isResetLineWidth = true;
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource().equals(textFieldFillOpaque)) {
                isResetFillOpaque = false;
            } else if (e.getSource().equals(textFieldPointHeight)) {
                isResetPointSize = false;
            } else if (e.getSource().equals(textFieldPointWidth)) {
                isResetPointSize = false;
            } else if (e.getSource().equals(textFieldFillGradientAngel)) {
                isResetFillGradientAngel = false;
            } else if (e.getSource().equals(textFieldFillGradientOffsetX)) {
                isResetFillGradientOffsetX = false;
            } else if (e.getSource().equals(textFieldFillGradientOffsetY)) {
                isResetFillGradientOffsetY = false;
            } else if (e.getSource().equals(textFieldPointOpaque)) {
                isResetPointOpaque = false;
            } else if (e.getSource().equals(textFieldPointRotation)) {
                isResetPointRotation = false;
            } else if (e.getSource().equals(textFieldLineWidth)) {
                isResetLineWidth = false;
            }
        }
    };

    private void resetFillModel(int index, GeoStyle geoStyle) {
        switch (index) {
            case 0:
                if (!geoStyle.getFillGradientMode().equals(FillGradientMode.LINEAR)) {
                    geoStyle.setFillGradientMode(FillGradientMode.LINEAR);
                }
                break;
            case 1:
                if (!geoStyle.getFillGradientMode().equals(FillGradientMode.RADIAL)) {
                    geoStyle.setFillGradientMode(FillGradientMode.RADIAL);
                }
                break;
            case 2:
                if (!geoStyle.getFillGradientMode().equals(FillGradientMode.SQUARE)) {
                    geoStyle.setFillGradientMode(FillGradientMode.SQUARE);
                }
                break;
            default:
                break;
        }
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
                if (null == recordsets) {
                    return;
                }
                int recordsetCount = recordsets.size();
                int count = 0;
                editHistory = MapUtilities.getMapControl().getEditHistory();
                for (int i = 0; i < recordsetCount; i++) {
                    Recordset recordset = recordsets.get(i);
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = null;
                        if (null != tempGeometry.getStyle()) {
                            geoStyle = tempGeometry.getStyle();
                        } else {
                            break;
                        }
                        if (e.getSource().equals(checkboxBackOpaque)) {
                            boolean isOpaque = checkboxBackOpaque.isSelected();
                            buttonFillBackColor.setEnabled(!isOpaque);
                            geoStyle.setFillBackOpaque(!isOpaque);
                            count++;
                        } else if (e.getSource().equals(checkboxFillGradient)) {
                            setFillGradientEnabled(checkboxFillGradient.isSelected());
                            if (checkboxFillGradient.isSelected()) {
                                int modelIndex = comboBoxFillGradientModel.getSelectedIndex();
                                resetFillModel(modelIndex, geoStyle);
                                count++;
                            } else {
                                geoStyle.setFillGradientMode(FillGradientMode.NONE);
                                count++;
                            }
                        } else {
                            break;
                        }
                        if (!CADStyleUtilities.is3DGeometry(tempGeometry)) {
                            tempGeometry.setStyle(geoStyle);
                        }
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    recordset.dispose();
                }
                if (count > 0) {
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };

    public CADStyleContainer() {
        super();
        this.setLayout(new GridBagLayout());
        this.scrollPane = new JScrollPane();
        this.panelCADInfo = new JPanel();
        this.scrollPane.setBorder(new LineBorder(Color.lightGray));
        this.add(this.scrollPane, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
        this.scrollPane.setViewportView(this.panelCADInfo);
    }

    public void init(ArrayList<Recordset> recordsets) {
        initComponents();
        initResources();
        registEvents();
        setEnabled(recordsets);
    }

    public void showDialog(ArrayList<Recordset> recordsets) {
        if (!modify) {
            this.scrollPane.setViewportView(panelParent);
            enabled(false);
            setEnabled(recordsets);
            registEvents();
        }
    }

    private void setSizeControllerSize(JFormattedTextField source, double value) {
        if (source == textFieldPointHeight) {
            symbolMarkerSizeController.setSymbolShowHeight(value);
        } else if (source == textFieldPointWidth) {
            symbolMarkerSizeController.setSymbolShowWidth(value);
        }
        loadSizeFormSymbolMarkerSizeController(source);
    }

    private void loadSizeFormSymbolMarkerSizeController(JFormattedTextField source) {
        try {
            isSizeListenersEnable = false;
            if (((JSpinner.NumberEditor) spinnerPointHeight.getEditor()).getTextField() != source) {
                spinnerPointHeight.setValue(symbolMarkerSizeController.getSymbolShowHeight());
            }
            if (((JSpinner.NumberEditor) spinnerPointWidth.getEditor()).getTextField() != source) {
                spinnerPointWidth.setValue(symbolMarkerSizeController.getSymbolShowWidth());
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            isSizeListenersEnable = true;
        }

    }

    private void setPanelPointEnabled(boolean enabled) {
        this.panelPointStyle.enabled(enabled);
        this.labelPointColor.setEnabled(enabled);
        this.buttonPointColor.setEnabled(enabled);
        this.labelPointRotation.setEnabled(enabled);
        this.labelPointRotationUnity.setEnabled(enabled);
        this.labelPointOpaque.setEnabled(enabled);
        this.labelPointOpaqueUnity.setEnabled(enabled);
        this.labelPointWidth.setEnabled(enabled);
        this.spinnerPointWidth.setEnabled(enabled);
        this.labelPointWidthUnity.setEnabled(enabled);
        this.labelPointHeight.setEnabled(enabled);
        this.spinnerPointHeight.setEnabled(enabled);
        this.labelPointHeightUnity.setEnabled(enabled);
        if (true == enabled) {
            if (null != geoPointStyle && null != panelPointStyle.getInitializeGeoStyle()) {
                int symbolID = panelPointStyle.getInitializeGeoStyle().getMarkerSymbolID();
                Resources resources = Application.getActiveApplication().getWorkspace().getResources();
                SymbolGroup group = resources.getMarkerLibrary().getRootGroup();
                for (int i = 0; i < group.getCount(); i++) {
                    if (group.get(i).getID() == symbolID) {
                        Symbol symbol = group.get(i);
                        if (symbol instanceof SymbolMarker3D || SymbolMarkerType.getSymbolMarkerType(((SymbolMarker) symbol)).equals(SymbolMarkerType.Raster)) {
                            setButtonPointColorEnable(false);
                        } else {
                            setButtonPointColorEnable(true);
                        }
                    }
                }
                if (0 >= symbolID) {
                    setSymstemPointEnable(false);
                } else {
                    setSymstemPointEnable(true);
                }
            } else {
                setButtonPointColorEnable(false);
                setSymstemPointEnable(false);
            }
            initPanelPointComponentsInfo();
        } else {
            setButtonPointColorEnable(false);
            setSymstemPointEnable(false);
        }
    }

    private void setPanelFillEnabled(boolean enabled) {
        this.panelFillStyle.enabled(enabled);
        this.labelFillForeColor.setEnabled(enabled);
        this.buttonFillForeColor.setEnabled(enabled);
        this.labelFillBackColor.setEnabled(enabled);
        this.buttonFillBackColor.setEnabled(enabled);
        this.checkboxBackOpaque.setEnabled(enabled);
        this.labelFillOpaque.setEnabled(enabled);
        this.checkboxFillGradient.setEnabled(enabled);
        if (true == enabled) {
            if (null != geoRegionStyle && null != panelFillStyle.getInitializeGeoStyle() && 7 >= panelFillStyle.getInitializeGeoStyle().getFillSymbolID()) {
                setSpinnerFillOpaqueEnable(true);
            } else {
                setSpinnerFillOpaqueEnable(false);
            }
            initPanelFillComponentsInfo();
        } else {
            setSpinnerFillOpaqueEnable(false);
        }
    }

    private void setFillGradientEnabled(boolean enabled) {
        this.labelFillGradientModel.setEnabled(enabled);
        this.comboBoxFillGradientModel.setEnabled(enabled);
        this.labelFillGradientAngelUnity.setEnabled(enabled);
        this.labelFillGradientOffsetX.setEnabled(enabled);
        this.spinnerFillGradientOffsetX.setEnabled(enabled);
        this.labelFillGradientOffsetXUnity.setEnabled(enabled);
        this.labelFillGradientOffsetY.setEnabled(enabled);
        this.spinnerFillGradientOffsetY.setEnabled(enabled);
        this.labelFillGradientOffsetYUnity.setEnabled(enabled);
        this.labelFillGradientAngel.setEnabled(enabled);
        this.spinnerFillGradientAngel.setEnabled(enabled);
        this.labelFillGradientAngelUnity.setEnabled(enabled);
    }

    private void setPanelLineEnabled(boolean enabled) {
        this.panelLineStyle.enabled(enabled);
        this.labelLineColor.setEnabled(enabled);
        this.labelLineWidth.setEnabled(enabled);
        this.buttonLineColor.setEnabled(enabled);
        this.comboboxLineWidth.setEnabled(enabled);
        if (true == enabled) {
            initPanelLineComponentsInfo();
        }
    }

    public void enabled(boolean enabled) {
        setPanelPointEnabled(enabled);
        setPanelLineEnabled(enabled);
        setPanelFillEnabled(enabled);
        panelText.enabled(enabled);
    }

    public void setEnabled(ArrayList<Recordset> recordsets) {
        geoPointStyle = null;
        geoLineStyle = null;
        geoRegionStyle = null;
        geoText = null;
        if (null == recordsets) {
            return;
        }
        if (-1 == panelContaints.getSelectedIndex()) {
            panelContaints.setSelectedIndex(0);
        }
        int selectIndex = panelContaints.getSelectedIndex();
        //用一个Set来存放选择变化集
        Set<Integer> set = new HashSet();
        int count = recordsets.size();
        for (int i = 0; i < count; i++) {
            Recordset recordset = recordsets.get(i);
            recordset.moveFirst();
            while (!recordset.isEOF()) {
                if (null == recordset.getGeometry()) {
                    return;
                }

                Geometry geometry = recordset.getGeometry();
                try {
                    if (GeometryUtilities.isPointGeometry(geometry) && !geometry.getType().equals(GeometryType.GEOPOINT3D)) {
                        geoPointStyle = getGeometryStyle(geometry);
                        addSetItem(set, TAB_MARKER);
                    }
                    if (GeometryUtilities.isLineGeometry(geometry) && !geometry.getType().equals(GeometryType.GEOLINE3D)) {
                        geoLineStyle = getGeometryStyle(geometry);
                        addSetItem(set, TAB_LINE);
                    }
                    if (GeometryUtilities.isRegionGeometry(geometry) && !geometry.getType().equals(GeometryType.GEOREGION3D)) {
                        geoRegionStyle = getGeometryStyle(geometry);
                        addSetItem(set, TAB_LINE);
                        addSetItem(set, TAB_REGION);
                    }
                    if (GeometryUtilities.isTextGeometry(geometry) && !geometry.getType().equals(GeometryType.GEOTEXT3D)) {
                        geoText = geometry;
                        set.add(TAB_TEXT);

                    }
                    recordset.moveNext();
                } finally {
                    if (geometry != null) {
                        geometry.dispose();
                        geometry = null;
                    }
                }
            }
            recordset.dispose();
        }
        int newSelectIndex = -1;
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            newSelectIndex = iterator.next();
            //// FIXME: 2016/12/30 初始化界面优化
            if (newSelectIndex == TAB_MARKER) {
                setPanelPointEnabled(true);
            } else if (newSelectIndex == TAB_LINE) {
                setPanelLineEnabled(true);
            } else if (newSelectIndex == TAB_REGION) {
                setPanelLineEnabled(true);
                setPanelFillEnabled(true);
            } else {
                this.panelText.init(this);
            }
        }
        if (!set.contains(selectIndex) && -1 != newSelectIndex) {
            panelContaints.setSelectedIndex(newSelectIndex);
        }
    }

    private void addSetItem(Set<Integer> set, Integer item) {
        if (!set.contains(item)) {
            set.add(item);
        }
    }

    private GeoStyle getGeometryStyle(Geometry geometry) {
        if (geometry == null) {
            return null;
        } else {
            return geometry.getStyle() == null ? new GeoStyle() : geometry.getStyle().clone();
        }
    }

    private void registEvents() {
        this.textFieldPointRotation = ((JSpinner.NumberEditor) spinnerPointRotation.getEditor()).getTextField();
        this.textFieldPointOpaque = ((JSpinner.NumberEditor) spinnerPointOpaque.getEditor()).getTextField();
        this.textFieldFillOpaque = ((JSpinner.NumberEditor) spinnerFillOpaque.getEditor()).getTextField();
        this.textFieldPointWidth = ((JSpinner.NumberEditor) spinnerPointWidth.getEditor()).getTextField();
        this.textFieldPointHeight = ((JSpinner.NumberEditor) spinnerPointHeight.getEditor()).getTextField();
        this.textFieldFillGradientOffsetX = ((JSpinner.NumberEditor) spinnerFillGradientOffsetX.getEditor()).getTextField();
        this.textFieldFillGradientOffsetY = ((JSpinner.NumberEditor) spinnerFillGradientOffsetY.getEditor()).getTextField();
        this.textFieldFillGradientAngel = ((JSpinner.NumberEditor) spinnerFillGradientAngel.getEditor()).getTextField();
        this.textFieldLineWidth = (JTextField) comboboxLineWidth.getEditor().getEditorComponent();
        removeEvents();
        this.buttonPointColor.addPropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonLineColor.addPropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonFillForeColor.addPropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonFillBackColor.addPropertyChangeListener("m_selectionColors", this.propertyListener);
        this.textFieldPointRotation.addFocusListener(this.focusListener);
        this.textFieldPointOpaque.addFocusListener(this.focusListener);
        this.textFieldFillOpaque.addFocusListener(this.focusListener);
        this.textFieldPointWidth.addFocusListener(this.focusListener);
        this.textFieldPointHeight.addFocusListener(this.focusListener);
        this.textFieldFillGradientOffsetX.addFocusListener(this.focusListener);
        this.textFieldFillGradientOffsetY.addFocusListener(this.focusListener);
        this.textFieldFillGradientAngel.addFocusListener(this.focusListener);
        this.textFieldLineWidth.addFocusListener(this.focusListener);
        this.comboBoxFillGradientModel.addFocusListener(this.focusListener);
        this.checkboxBackOpaque.addFocusListener(this.focusListener);
        this.checkboxFillGradient.addFocusListener(this.focusListener);
        this.spinnerPointRotation.addChangeListener(this.pointRotationListener);
        this.spinnerPointOpaque.addChangeListener(this.opaqueListener);
        this.spinnerFillOpaque.addChangeListener(this.opaqueListener);
        this.textFieldPointWidth.addCaretListener(this.pointSizeListener);
        this.textFieldPointHeight.addCaretListener(this.pointSizeListener);
        this.spinnerFillGradientOffsetX.addChangeListener(this.fillGradientOffsetListener);
        this.spinnerFillGradientOffsetY.addChangeListener(this.fillGradientOffsetListener);
        this.spinnerFillGradientAngel.addChangeListener(this.fillGradientAngelListener);
        this.comboboxLineWidth.addItemListener(this.lineWidthListener);
        this.comboBoxFillGradientModel.addItemListener(this.itemListener);
        this.checkboxBackOpaque.addActionListener(this.actionListener);
        this.checkboxFillGradient.addActionListener(this.actionListener);
        this.checkboxWAndH.addActionListener(this.lockListener);
        // FIXME: 2016/12/23 UGDJ-547
        // 关闭或打开工作空间时重新加载符号响应事件
        this.panelPointStyle.registEvents();
        this.panelFillStyle.registEvents();
        this.panelLineStyle.registEvents();
    }

    public void removeEvents() {
        this.buttonPointColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonLineColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonFillForeColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonFillBackColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.textFieldPointRotation.removeFocusListener(this.focusListener);
        this.textFieldPointOpaque.removeFocusListener(this.focusListener);
        this.textFieldFillOpaque.removeFocusListener(this.focusListener);
        this.textFieldPointWidth.removeFocusListener(this.focusListener);
        this.textFieldPointHeight.removeFocusListener(this.focusListener);
        this.textFieldFillGradientOffsetX.removeFocusListener(this.focusListener);
        this.textFieldFillGradientOffsetY.removeFocusListener(this.focusListener);
        this.textFieldFillGradientAngel.removeFocusListener(this.focusListener);
        this.textFieldLineWidth.removeFocusListener(this.focusListener);
        this.spinnerPointRotation.removeChangeListener(this.pointRotationListener);
        this.spinnerPointOpaque.removeChangeListener(this.opaqueListener);
        this.spinnerFillOpaque.removeChangeListener(this.opaqueListener);
        this.textFieldPointWidth.removeCaretListener(this.pointSizeListener);
        this.textFieldPointHeight.removeCaretListener(this.pointSizeListener);
        this.spinnerFillGradientOffsetX.removeChangeListener(this.fillGradientOffsetListener);
        this.spinnerFillGradientOffsetY.removeChangeListener(this.fillGradientOffsetListener);
        this.spinnerFillGradientAngel.removeChangeListener(this.fillGradientAngelListener);
        this.comboboxLineWidth.addItemListener(this.lineWidthListener);
        this.comboBoxFillGradientModel.removeItemListener(this.itemListener);
        this.checkboxBackOpaque.removeActionListener(this.actionListener);
        this.checkboxFillGradient.removeActionListener(this.actionListener);
        this.checkboxWAndH.removeActionListener(this.lockListener);
        this.panelFillStyle.removeEvents();
        this.panelLineStyle.removeEvents();
        this.panelPointStyle.removeEvents();
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
        this.labelFillGradientModel.setText(ControlsProperties.getString("String_Label_Type"));
        this.labelFillGradientOffsetX.setText(CoreProperties.getString("String_Label_XOffsetRate"));
        this.labelFillGradientOffsetXUnity.setText("%");
        this.labelFillGradientOffsetY.setText(CoreProperties.getString("String_Label_YOffsetRate"));
        this.labelFillGradientOffsetYUnity.setText("%");
        this.labelFillGradientAngel.setText(CoreProperties.getString("String_GeometryDrawingParam_Angle"));
        this.labelFillGradientAngelUnity.setText(CoreProperties.getString("String_Degree_Format_Degree"));
        this.labelFillOpaque.setText(MapEditorProperties.getString("String_Oqaue_I"));
    }

    public void setNullPanel() {
        this.modify = false;
        this.scrollPane.setViewportView(panelCADInfo);
        this.removeEvents();
    }

    private void initComponents() {
        initPanelPointComponents();
        initPanelLineComponents();
        initPanelFillComponents();
        this.panelText = new TextStyleContainer();
        panelContaints = new JTabbedPane();
        panelParent = new JPanel();
        panelParent.setLayout(new GridBagLayout());
        panelParent.add(panelContaints, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
        panelContaints.addTab(CoreProperties.getString("String_SpatialQuery_Point"), panelPoint);
        panelContaints.addTab(CoreProperties.getString("String_SpatialQuery_Line"), panelLine);
        panelContaints.addTab(CoreProperties.getString("String_SpatialQuery_Region"), panelFill);
        panelContaints.addTab(CoreProperties.getString("String_SpatialQuery_Text"), panelText);
        this.scrollPane.setViewportView(panelParent);
        this.scrollPane.setBorder(null);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setPanelFillEnabled(false);
        setPanelPointEnabled(false);
        setPanelLineEnabled(false);
        setFillGradientEnabled(false);
    }

    private void initPanelFillComponents() {
        // 初始化面风格设置界面
        this.panelFillStyle = new CADStyleTitlePanel(this, CADStyleTitlePanel.GEOREGIONTYPE);
        this.labelFillForeColor = new JLabel();
        this.labelFillBackColor = new JLabel();
        this.checkboxBackOpaque = new JCheckBox();
        this.labelFillOpaque = new JLabel();
        this.spinnerFillOpaque = new JSpinner();
        this.spinnerFillOpaque.setModel(new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0));
        this.textFieldFillOpaque = ((JSpinner.NumberEditor) spinnerFillOpaque.getEditor()).getTextField();
        this.labelFillOpaqueUnity = new JLabel();

        this.checkboxFillGradient = new JCheckBox();
        this.labelFillGradientModel = new JLabel();
        this.comboBoxFillGradientModel = new JComboBox();
        this.comboBoxFillGradientModel.setModel(new DefaultComboBoxModel(this.stringGradientModel));
        this.labelFillGradientOffsetX = new JLabel();
        this.spinnerFillGradientOffsetX = new JSpinner();
        this.spinnerFillGradientOffsetX.setModel(new SpinnerNumberModel(0.0, -100.0, 100.0, 1.0));
        this.labelFillGradientOffsetXUnity = new JLabel();
        this.labelFillGradientOffsetY = new JLabel();
        this.spinnerFillGradientOffsetY = new JSpinner();
        this.spinnerFillGradientOffsetY.setModel(new SpinnerNumberModel(0.0, -100.0, 100.0, 1.0));
        this.labelFillGradientOffsetYUnity = new JLabel();
        this.labelFillGradientAngel = new JLabel();
        this.spinnerFillGradientAngel = new JSpinner();
        this.symbolMarkerSizeController = new SymbolMarkerSizeController();
        this.symbolMarkerSizeController.setResources(Application.getActiveApplication().getWorkspace().getResources());
        this.symbolMarkerSizeController.setGeoStyle(new GeoStyle());
        this.spinnerFillGradientAngel.setModel(new SpinnerNumberModel(0.0, 0.0, 360.0, 1.0));
        this.labelFillGradientAngelUnity = new JLabel();
        this.buttonFillBackColor = new ColorSelectButton(Color.gray);
        this.buttonFillForeColor = new ColorSelectButton(Color.gray);
        panelFill = new JPanel();
        panelFill.setLayout(new GridBagLayout());
        JPanel panelFillComponents = new JPanel();
        JPanel panelTemp = new JPanel();
        panelTemp.setLayout(new GridBagLayout());
        panelFill.add(panelTemp, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
        panelTemp.add(this.panelFillStyle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 5, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelTemp.add(panelFillComponents, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 0, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

        panelFillComponents.setLayout(new GridBagLayout());
        panelFillComponents.add(this.labelFillForeColor, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.buttonFillForeColor, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(5, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.labelFillBackColor, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.buttonFillBackColor, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.checkboxBackOpaque, new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(1, 1));
        panelFillComponents.add(this.labelFillOpaque, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.spinnerFillOpaque, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.labelFillOpaqueUnity, new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));

        panelFillComponents.add(this.checkboxFillGradient, new GridBagConstraintsHelper(0, 4, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.labelFillGradientModel, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.comboBoxFillGradientModel, new GridBagConstraintsHelper(1, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.labelFillGradientOffsetX, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.spinnerFillGradientOffsetX, new GridBagConstraintsHelper(1, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.labelFillGradientOffsetXUnity, new GridBagConstraintsHelper(2, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.labelFillGradientOffsetY, new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.spinnerFillGradientOffsetY, new GridBagConstraintsHelper(1, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.labelFillGradientOffsetYUnity, new GridBagConstraintsHelper(2, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.labelFillGradientAngel, new GridBagConstraintsHelper(0, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelFillComponents.add(this.spinnerFillGradientAngel, new GridBagConstraintsHelper(1, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFillComponents.add(this.labelFillGradientAngelUnity, new GridBagConstraintsHelper(2, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
    }

    private void initPanelFillComponentsInfo() {
        if (null != geoRegionStyle) {
            this.buttonFillBackColor.setColor(geoRegionStyle.getFillBackColor());
            this.buttonFillForeColor.setColor(geoRegionStyle.getFillForeColor());
            this.spinnerFillOpaque.setValue(100.0 - geoRegionStyle.getFillOpaqueRate());
            this.checkboxBackOpaque.setSelected(!geoRegionStyle.getFillBackOpaque());
            if (geoRegionStyle.getFillGradientMode().equals(FillGradientMode.NONE)) {
                this.checkboxFillGradient.setSelected(false);
                setFillGradientEnabled(false);
            } else {
                this.checkboxFillGradient.setSelected(true);
                setFillGradientEnabled(true);
                if (geoRegionStyle.getFillGradientMode().equals(FillGradientMode.LINEAR)) {
                    this.comboBoxFillGradientModel.setSelectedIndex(0);
                } else if (geoRegionStyle.getFillGradientMode().equals(FillGradientMode.RADIAL)) {
                    this.comboBoxFillGradientModel.setSelectedIndex(1);
                } else if (geoRegionStyle.getFillGradientMode().equals(FillGradientMode.SQUARE)) {
                    this.comboBoxFillGradientModel.setSelectedIndex(2);
                }
                this.spinnerFillGradientOffsetX.setValue(geoRegionStyle.getFillGradientOffsetRatioX());
                this.spinnerFillGradientOffsetY.setValue(geoRegionStyle.getFillGradientOffsetRatioY());
                this.spinnerFillGradientAngel.setValue(geoRegionStyle.getFillGradientAngle());
            }
        } else {
            this.buttonFillBackColor.setColor(Color.gray);
            this.buttonFillForeColor.setColor(Color.gray);
            setFillGradientEnabled(false);
        }
    }

    private void initPanelLineComponents() {
        // 初始化线风格界面
        this.panelLineStyle = new CADStyleTitlePanel(this, CADStyleTitlePanel.GEOLINETYPE);
        this.labelLineColor = new JLabel();
        this.labelLineWidth = new JLabel();
        this.comboboxLineWidth = new JComboBox();
        this.comboboxLineWidth.setModel(new DefaultComboBoxModel(this.stringLineWidths));
        this.comboboxLineWidth.setEditable(true);
        this.buttonLineColor = new ColorSelectButton(Color.gray);
        panelLine = new JPanel();
        JPanel panelLineComponents = new JPanel();
        panelLine.setLayout(new GridBagLayout());
        JPanel panelTemp = new JPanel();
        panelTemp.setLayout(new GridBagLayout());
        panelLine.add(panelTemp, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
        panelTemp.add(this.panelLineStyle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 5, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelTemp.add(panelLineComponents, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 0, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

        panelLineComponents.setLayout(new GridBagLayout());
        panelLineComponents.add(this.labelLineColor, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelLineComponents.add(this.buttonLineColor, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(5, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLineComponents.add(this.labelLineWidth, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelLineComponents.add(this.comboboxLineWidth, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
    }

    private void initPanelLineComponentsInfo() {
        if (null != geoLineStyle || null != geoRegionStyle) {
            if (null != geoLineStyle) {
                this.buttonLineColor.setColor(geoLineStyle.getLineColor());
                this.lineWidth = String.valueOf(geoLineStyle.getLineWidth());
            } else {
                this.buttonLineColor.setColor(geoRegionStyle.getLineColor());
                this.lineWidth = String.valueOf(geoRegionStyle.getLineWidth());
            }
            this.comboboxLineWidth.setSelectedItem(this.lineWidth);
        } else {
            this.buttonLineColor.setColor(Color.gray);
        }
    }

    public void setSpinnerFillOpaqueEnable(boolean enable) {
        this.spinnerFillOpaque.setEnabled(enable);
        if (false == enable) {
            this.spinnerFillOpaque.setToolTipText(CommonProperties.getString(CommonProperties.UnSupport));
        } else {
            this.spinnerFillOpaque.setToolTipText("");
        }
    }

    public void setButtonPointColorEnable(boolean enable) {
        if (false == enable) {
            buttonPointColor.setToolTipText(CoreProperties.getString("String_Label_ColorSettingTip"));
        } else {
            buttonPointColor.setToolTipText("");
        }
        buttonPointColor.setEnabled(enable);
    }

    public void setSymstemPointEnable(boolean enable) {
        this.checkboxWAndH.setEnabled(enable);
        this.spinnerPointRotation.setEnabled(enable);
        this.spinnerPointOpaque.setEnabled(enable);
        if (false == enable) {
            buttonPointColor.setToolTipText(ControlsProperties.getString("String_vectorSymbolCantSetOpaque"));
        } else {
            buttonPointColor.setToolTipText("");
        }
    }

    private void initPanelPointComponents() {
        // 初始化点风格设置界面
        this.panelPointStyle = new CADStyleTitlePanel(this, CADStyleTitlePanel.GEOPOINTTYPE);
        this.labelPointColor = new JLabel();
        this.labelPointRotation = new JLabel();
        this.spinnerPointRotation = new JSpinner();
        this.spinnerPointRotation.setModel(new SpinnerNumberModel(0.0, 0.0, 360.0, 1.0));
        this.labelPointRotationUnity = new JLabel();
        this.labelPointOpaque = new JLabel();
        this.spinnerPointOpaque = new JSpinner();
        this.spinnerPointOpaque.setModel(new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0));
        this.labelPointOpaqueUnity = new JLabel();
        this.labelPointWidth = new JLabel();
        this.spinnerPointWidth = new JSpinner();
        this.spinnerPointWidth.setModel(new SpinnerNumberModel(0.0, -500.0, 500.0, 1.0));
        this.labelPointWidthUnity = new JLabel();
        this.labelPointHeight = new JLabel();
        this.spinnerPointHeight = new JSpinner();
        this.spinnerPointHeight.setModel(new SpinnerNumberModel(0.0, -500.0, 500.0, 1.0));
        this.labelPointHeightUnity = new JLabel();
        this.checkboxWAndH = new JCheckBox();
        this.checkboxWAndH.setSelected(true);
        this.buttonPointColor = new ColorSelectButton(Color.gray);

        panelPoint = new JPanel();
        JPanel panelPointComponents = new JPanel();
        panelPoint.setLayout(new GridBagLayout());
        JPanel panelTemp = new JPanel();
        panelTemp.setLayout(new GridBagLayout());
        panelPoint.add(panelTemp, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
        panelTemp.add(this.panelPointStyle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 5, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelTemp.add(panelPointComponents, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 0, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

        panelPointComponents.setLayout(new GridBagLayout());
        panelPointComponents.add(this.labelPointColor, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.buttonPointColor, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 10, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelPointComponents.add(this.labelPointRotation, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.spinnerPointRotation, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelPointComponents.add(this.labelPointRotationUnity, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.labelPointOpaque, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.spinnerPointOpaque, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelPointComponents.add(this.labelPointOpaqueUnity, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.checkboxWAndH, new GridBagConstraintsHelper(0, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.labelPointWidth, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.spinnerPointWidth, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelPointComponents.add(this.labelPointWidthUnity, new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.labelPointHeight, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        panelPointComponents.add(this.spinnerPointHeight, new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelPointComponents.add(this.labelPointHeightUnity, new GridBagConstraintsHelper(2, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setFill(GridBagConstraints.NONE).setWeight(0, 1));
    }

    private void initPanelPointComponentsInfo() {
        if (null != geoPointStyle) {
            buttonPointColor.setColor(geoPointStyle.getLineColor());
            this.spinnerPointRotation.setValue(geoPointStyle.getMarkerAngle());
            this.spinnerPointOpaque.setValue(100.0 - geoPointStyle.getFillOpaqueRate());
            this.spinnerPointWidth.setValue(geoPointStyle.getMarkerSize().getWidth());
            this.spinnerPointHeight.setValue(geoPointStyle.getMarkerSize().getHeight());
        } else {
            buttonPointColor.setColor(Color.gray);
        }
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

}
