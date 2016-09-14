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
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by xie on 2016/8/25.
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
    private EditHistory editHistory;

    private Recordset recordset;
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
    private boolean isDisposed;

    private final String[] stringPointSizes = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "1.5", "3", "4", "5", "6", "7", "8", "9", "10", "15", "10"};
    private final String[] stringLineWidths = new String[]{"1.1", "1.3", "1.5", "1.7", "1.9", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final String[] stringGradientModel = new String[]{CoreProperties.getString("String_LINEAR"), CoreProperties.getString("String_RADIAL"), CoreProperties.getString("String_SQUARE")};
    private boolean modify;
    private PropertyChangeListener propertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Color currentColor = ((ColorSelectButton) evt.getSource()).getColor();
            try {
                if (!isDisposed) {
                    Object newSize = null;
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        if (evt.getSource().equals(buttonPointColor)) {
                            // 修改点颜色
                            geoStyle.setLineColor(currentColor);
                        } else if (evt.getSource().equals(buttonLineColor)) {
                            // 修改线颜色
                            geoStyle.setLineColor(currentColor);
                        } else if (evt.getSource().equals(buttonFillForeColor)) {
                            // 修改面的前景色
                            geoStyle.setFillForeColor(currentColor);
                        } else if (evt.getSource().equals(buttonFillBackColor)) {
                            // 修改面的背景色
                            geoStyle.setFillBackColor(currentColor);
                        } else {
                            break;
                        }
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
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
                    if (!isDisposed) {
                        recordset.moveFirst();
                        while (!recordset.isEOF()) {
                            editHistory.add(EditType.MODIFY, recordset, true);
                            recordset.edit();
                            Geometry tempGeometry = recordset.getGeometry();
                            GeoStyle geoStyle = new GeoStyle();
                            if (null != geoStyle) {
                                geoStyle = tempGeometry.getStyle();
                            } else {
                                break;
                            }
                            resetFillModel(index, geoStyle);
                            tempGeometry.setStyle(geoStyle);
                            recordset.setGeometry(tempGeometry);
                            tempGeometry.dispose();
                            recordset.update();
                            recordset.moveNext();
                        }
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
    private CaretListener textFieldPointRotationCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                if (!isDisposed) {
                    String text = textFieldPointRotation.getText();
                    if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
                        textFieldPointRotation.setForeground(wrongColor);
                        return;
                    } else {
                        textFieldPointRotation.setForeground(defaultColor);
                    }
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        double rotation = Double.valueOf(text);
                        if (!DoubleUtilities.equals(rotation, geoStyle.getMarkerAngle(), pow)) {
                            //设置点旋转角度
                            geoStyle.setMarkerAngle(rotation);
                        } else {
                            break;
                        }
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private CaretListener textFieldOpaqueCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                if (!isDisposed) {
                    String text = ((JFormattedTextField) e.getSource()).getText();
                    if (!SymbolSpinnerUtilties.isLegitNumber(0, 100, text)) {
                        ((JFormattedTextField) e.getSource()).setForeground(wrongColor);
                        return;
                    } else {
                        ((JFormattedTextField) e.getSource()).setForeground(defaultColor);
                    }
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        int opaque = Integer.valueOf(text);
                        if (geoStyle.getFillOpaqueRate() != opaque) {
                            //设置点/面透明度
                            geoStyle.setFillOpaqueRate(100 - opaque);
                        } else {
                            break;
                        }
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private CaretListener textFieldPointSizeCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                if (isSizeListenersEnable && e.getSource() instanceof JFormattedTextField && !isDisposed) {
                    // 重新设置
                    String text = ((JFormattedTextField) e.getSource()).getText();
                    if (!SymbolSpinnerUtilties.isLegitNumber(-500d, 500d, text)) {
                        ((JFormattedTextField) e.getSource()).setForeground(wrongColor);
                        return;
                    } else {
                        ((JFormattedTextField) e.getSource()).setForeground(defaultColor);
                    }
                    setSizeControllerSize((JFormattedTextField) e.getSource(), Double.valueOf(text));
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        Size2D newSize = new Size2D();
                        double width = Double.valueOf(textFieldPointWidth.getText());
                        double height = Double.valueOf(textFieldPointHeight.getText());
                        newSize.setHeight(height);
                        newSize.setWidth(width);
                        if (!newSize.equals(geoStyle.getMarkerSize())) {
                            //设置点符号大小
                            geoStyle.setMarkerSize(newSize);
                        } else {
                            break;
                        }
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private CaretListener textFieldGradientOffsetCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                if (!isDisposed) {
                    String text = ((JFormattedTextField) e.getSource()).getText();
                    if (!SymbolSpinnerUtilties.isLegitNumber(-100d, 100d, text)) {
                        ((JFormattedTextField) e.getSource()).setForeground(wrongColor);
                        return;
                    } else {
                        ((JFormattedTextField) e.getSource()).setForeground(defaultColor);
                    }
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        double newOffset = Double.valueOf(text);
                        if (e.getSource().equals(textFieldFillGradientOffsetX) && !DoubleUtilities.equals(newOffset, geoStyle.getFillGradientOffsetRatioX(), pow)) {
                            // 修改水平渐变偏移量
                            geoStyle.setFillGradientOffsetRatioX(newOffset);
                        } else if (e.getSource().equals(textFieldFillGradientOffsetY) && !DoubleUtilities.equals(newOffset, geoStyle.getFillGradientOffsetRatioX(), pow)) {
                            // 修改垂直渐变偏移量
                            geoStyle.setFillGradientOffsetRatioY(newOffset);
                        } else {
                            break;
                        }
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private CaretListener textFieldFillGradientAngelCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                if (!isDisposed) {
                    String text = ((JFormattedTextField) e.getSource()).getText();
                    if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
                        ((JFormattedTextField) e.getSource()).setForeground(wrongColor);
                        return;
                    } else {
                        ((JFormattedTextField) e.getSource()).setForeground(defaultColor);
                    }
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        double newAngel = Double.valueOf(text);
                        if (!DoubleUtilities.equals(newAngel, geoStyle.getFillGradientAngle(), pow)) {
                            geoStyle.setFillGradientAngle(newAngel);
                        } else {
                            break;
                        }
                        double newOffset = Double.valueOf(text);
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
                    editHistory.batchEnd();
                    MapUtilities.getActiveMap().refresh();
                    modify = true;
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private CaretListener textFieldLineWidthCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                if (!isDisposed) {
                    String text = ((JTextField) e.getSource()).getText();
                    if (!SymbolSpinnerUtilties.isLegitNumber(0d, 20d, text)) {
                        ((JTextField) e.getSource()).setForeground(wrongColor);
                        return;
                    } else {
                        ((JTextField) e.getSource()).setForeground(defaultColor);
                    }
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        double newLineWidth = Double.valueOf(text);
                        if (!DoubleUtilities.equals(newLineWidth, geoStyle.getLineWidth(), pow)) {
                            geoStyle.setLineWidth(newLineWidth);
                        } else {
                            break;
                        }
                        double newOffset = Double.valueOf(text);
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
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

    private void resetFillModel(int index, GeoStyle geoStyle) {
        switch (index) {
            case 0:
                geoStyle.setFillGradientMode(FillGradientMode.LINEAR);
                break;
            case 1:
                geoStyle.setFillGradientMode(FillGradientMode.RADIAL);
                break;
            case 2:
                geoStyle.setFillGradientMode(FillGradientMode.SQUARE);
                break;
            default:
                break;
        }
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (!isDisposed) {
                    recordset.moveFirst();
                    while (!recordset.isEOF()) {
                        editHistory.add(EditType.MODIFY, recordset, true);
                        recordset.edit();
                        Geometry tempGeometry = recordset.getGeometry();
                        GeoStyle geoStyle = new GeoStyle();
                        if (null != geoStyle) {
                            geoStyle = tempGeometry.getStyle();
                        }
                        if (e.getSource().equals(checkboxBackOpaque)) {
                            boolean isOpaque = checkboxBackOpaque.isSelected();
                            buttonFillBackColor.setEnabled(!isOpaque);
                            geoStyle.setFillBackOpaque(!isOpaque);
                        } else if (e.getSource().equals(checkboxFillGradient)) {
                            setFillGradientEnabled(checkboxFillGradient.isSelected());
                            if (checkboxFillGradient.isSelected()) {
                                int modelIndex = comboBoxFillGradientModel.getSelectedIndex();
                                resetFillModel(modelIndex, geoStyle);
                            } else {
                                geoStyle.setFillGradientMode(FillGradientMode.NONE);
                            }
                        } else {
                            break;
                        }
                        tempGeometry.setStyle(geoStyle);
                        recordset.setGeometry(tempGeometry);
                        tempGeometry.dispose();
                        recordset.update();
                        recordset.moveNext();
                    }
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

    public void showDialog(Recordset recordset) {
        if (!modify) {
            setRecordset(recordset);
            editHistory = MapUtilities.getMapControl().getEditHistory();
            initComponents();
            initResources();
            registEvents();
            setEnabled();
        }
    }

    private void setSizeControllerSize(JFormattedTextField source, double value) {
        if (source == ((JSpinner.NumberEditor) spinnerPointHeight.getEditor()).getTextField()) {
            symbolMarkerSizeController.setSymbolShowHeight(value);
        } else if (source == ((JSpinner.NumberEditor) spinnerPointWidth.getEditor()).getTextField()) {
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
        if (null != panelPointStyle.getInitializeGeoStyle()) {
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
        if (null != panelFillStyle.getInitializeGeoStyle() && 7 >= panelFillStyle.getInitializeGeoStyle().getFillSymbolID()) {
            setSpinnerFillOpaqueEnable(true);
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
    }

    public void enabled(boolean enabled) {
        setPanelPointEnabled(enabled);
        setPanelLineEnabled(enabled);
        setPanelFillEnabled(enabled);
    }

    public void setEnabled() {
        recordset.moveFirst();
        boolean containsPoint = false;
        boolean containsLine = false;
        boolean containsRegion = false;
        while (!recordset.isEOF()) {
            if (GeometryUtilities.isPointGeometry(recordset.getGeometry())) {
                setPanelPointEnabled(true);
                containsPoint = true;
            } else if (GeometryUtilities.isLineGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                containsLine = true;
            } else if (GeometryUtilities.isRegionGeometry(recordset.getGeometry())) {
                setPanelLineEnabled(true);
                setPanelFillEnabled(true);
                containsLine = true;
                containsRegion = true;
            } else {
                // 设置为空界面
                this.scrollPane.setViewportView(panelCADInfo);
            }
            recordset.moveNext();
        }
        if (!containsPoint) {
            this.buttonPointColor.setColor(Color.gray);
        }
        if (!containsLine) {
            this.buttonLineColor.setColor(Color.gray);
        }
        if (!containsRegion) {
            this.buttonFillForeColor.setColor(Color.gray);
            this.buttonFillBackColor.setColor(Color.gray);
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
        this.textFieldPointRotation.addCaretListener(this.textFieldPointRotationCaretListener);
        this.textFieldPointOpaque.addCaretListener(this.textFieldOpaqueCaretListener);
        this.textFieldFillOpaque.addCaretListener(this.textFieldOpaqueCaretListener);
        this.textFieldPointWidth.addCaretListener(this.textFieldPointSizeCaretListener);
        this.textFieldPointHeight.addCaretListener(this.textFieldPointSizeCaretListener);
        this.textFieldFillGradientOffsetX.addCaretListener(this.textFieldGradientOffsetCaretListener);
        this.textFieldFillGradientOffsetY.addCaretListener(this.textFieldGradientOffsetCaretListener);
        this.textFieldFillGradientAngel.addCaretListener(this.textFieldFillGradientAngelCaretListener);
        this.textFieldLineWidth.addCaretListener(this.textFieldLineWidthCaretListener);
        this.comboBoxFillGradientModel.addItemListener(this.itemListener);
        this.checkboxBackOpaque.addActionListener(this.actionListener);
        this.checkboxFillGradient.addActionListener(this.actionListener);
        this.checkboxWAndH.addActionListener(this.lockListener);
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
        this.buttonPointColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonLineColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonFillForeColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.buttonFillBackColor.removePropertyChangeListener("m_selectionColors", this.propertyListener);
        this.textFieldPointRotation.removeCaretListener(this.textFieldPointRotationCaretListener);
        this.textFieldPointOpaque.removeCaretListener(this.textFieldOpaqueCaretListener);
        this.textFieldFillOpaque.removeCaretListener(this.textFieldOpaqueCaretListener);
        this.textFieldPointWidth.removeCaretListener(this.textFieldPointSizeCaretListener);
        this.textFieldPointHeight.removeCaretListener(this.textFieldPointSizeCaretListener);
        this.textFieldFillGradientOffsetX.removeCaretListener(this.textFieldGradientOffsetCaretListener);
        this.textFieldFillGradientOffsetY.removeCaretListener(this.textFieldGradientOffsetCaretListener);
        this.textFieldFillGradientAngel.removeCaretListener(this.textFieldFillGradientAngelCaretListener);
        this.textFieldLineWidth.removeCaretListener(this.textFieldLineWidthCaretListener);
        this.comboBoxFillGradientModel.removeItemListener(this.itemListener);
        this.checkboxBackOpaque.removeActionListener(this.actionListener);
        this.checkboxFillGradient.removeActionListener(this.actionListener);
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
        removePanels();
        this.scrollPane.setViewportView(panelCADInfo);
        if (null != recordset) {
            recordset.close();
            recordset.dispose();
            isDisposed = true;
        }
    }

    private void initComponents() {
        removePanels();
        initPanelPointComponents();
        initPanelLineComponents();
        initPanelFillComponents();
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
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        if (null != recordset.getGeometry() && null != recordset.getGeometry().getStyle()) {
            this.buttonFillBackColor = new ColorSelectButton(recordset.getGeometry().getStyle().getFillBackColor());
            this.buttonFillForeColor = new ColorSelectButton(recordset.getGeometry().getStyle().getFillForeColor());
            this.spinnerFillOpaque.setValue(100.0 - recordset.getGeometry().getStyle().getFillOpaqueRate());
            this.checkboxBackOpaque.setSelected(!recordset.getGeometry().getStyle().getFillBackOpaque());
            if (recordset.getGeometry().getStyle().getFillGradientMode().equals(FillGradientMode.NONE)) {
                this.checkboxFillGradient.setSelected(false);
                setFillGradientEnabled(false);
            } else {
                this.checkboxFillGradient.setSelected(true);
                setFillGradientEnabled(true);
                if (recordset.getGeometry().getStyle().getFillGradientMode().equals(FillGradientMode.LINEAR)) {
                    this.comboBoxFillGradientModel.setSelectedIndex(0);
                } else if (recordset.getGeometry().getStyle().getFillGradientMode().equals(FillGradientMode.RADIAL)) {
                    this.comboBoxFillGradientModel.setSelectedIndex(1);
                } else if (recordset.getGeometry().getStyle().getFillGradientMode().equals(FillGradientMode.SQUARE)) {
                    this.comboBoxFillGradientModel.setSelectedIndex(2);
                }
                this.spinnerFillGradientOffsetX.setValue(recordset.getGeometry().getStyle().getFillGradientOffsetRatioX());
                this.spinnerFillGradientOffsetY.setValue(recordset.getGeometry().getStyle().getFillGradientOffsetRatioY());
                this.spinnerFillGradientAngel.setValue(recordset.getGeometry().getStyle().getFillGradientAngle());
            }
        }
        panelFill = new JPanel();
        panelFill.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_FillStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelFill.setLayout(new GridBagLayout());
        JPanel panelFillComponents = new JPanel();
        panelFill.add(this.panelFillStyle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelFill.add(panelFillComponents, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 0, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

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
        setPanelFillEnabled(false);
    }

    private void initPanelLineComponents() {
        // 初始化线风格界面
        this.panelLineStyle = new CADStyleTitlePanel(this, CADStyleTitlePanel.GEOLINETYPE);
        this.labelLineColor = new JLabel();
        this.labelLineWidth = new JLabel();
        this.comboboxLineWidth = new JComboBox();
        this.comboboxLineWidth.setModel(new DefaultComboBoxModel(this.stringLineWidths));
        this.comboboxLineWidth.setEditable(true);
        if (null != recordset.getGeometry() && null != recordset.getGeometry().getStyle()) {
            this.buttonLineColor = new ColorSelectButton(recordset.getGeometry().getStyle().getLineColor());
            this.lineWidth = String.valueOf(recordset.getGeometry().getStyle().getLineWidth());
            this.comboboxLineWidth.setSelectedItem(this.lineWidth);
        }
        panelLine = new JPanel();
        JPanel panelLineComponents = new JPanel();
        panelLine.setBorder(new TitledBorder(null, MapEditorProperties.getString("Stirng_LineStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelLine.setLayout(new GridBagLayout());
        panelLine.add(this.panelLineStyle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLine.add(panelLineComponents, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 0, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

        panelLineComponents.setLayout(new GridBagLayout());
        panelLineComponents.add(this.labelLineColor, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelLineComponents.add(this.buttonLineColor, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraintsHelper.WEST).setInsets(5, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelLineComponents.add(this.labelLineWidth, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 10, 5).setWeight(0, 1).setFill(GridBagConstraints.NONE));
        panelLineComponents.add(this.comboboxLineWidth, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 10, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        setPanelLineEnabled(false);
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
        if (null != recordset.getGeometry() && null != recordset.getGeometry().getStyle()) {
            this.buttonPointColor = new ColorSelectButton(recordset.getGeometry().getStyle().getLineColor());
            this.spinnerPointRotation.setValue(recordset.getGeometry().getStyle().getMarkerAngle());
            this.spinnerPointOpaque.setValue(100.0 - recordset.getGeometry().getStyle().getFillOpaqueRate());
            this.spinnerPointWidth.setValue(recordset.getGeometry().getStyle().getMarkerSize().getWidth());
            this.spinnerPointHeight.setValue(recordset.getGeometry().getStyle().getMarkerSize().getHeight());
        }
        panelPoint = new JPanel();
        JPanel panelPointComponents = new JPanel();
        panelPoint.setBorder(new TitledBorder(null, MapEditorProperties.getString("String_PointStyle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelPoint.setLayout(new GridBagLayout());
        panelPoint.add(this.panelPointStyle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 0).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
        panelPoint.add(panelPointComponents, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(5, 0, 5, 5).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));

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
        setPanelPointEnabled(false);
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public void setRecordset(Recordset recordset) {
        if (null != this.recordset) {
            this.recordset.dispose();
        }
        this.recordset = recordset;
        isDisposed = false;
    }
}
