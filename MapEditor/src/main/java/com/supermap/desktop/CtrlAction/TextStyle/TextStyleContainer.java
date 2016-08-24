package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.*;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.textStyle.ITextStyle;
import com.supermap.desktop.ui.controls.textStyle.ResetTextStyleUtil;
import com.supermap.desktop.ui.controls.textStyle.TextBasicPanel;
import com.supermap.desktop.ui.controls.textStyle.TextStyleChangeListener;
import com.supermap.desktop.utilities.MapUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 文本风格对话框
 *
 * @author xie 2016-6-3
 */
public class TextStyleContainer extends JPanel {

    private static final long serialVersionUID = 1L;

    private transient JButton buttonClose;

    private transient ITextStyle textBasicPanel;

    private Geometry geometry;
    private String text;
    private TextStyle tempTextStyle;// 用于预览的TextStyle
    private Double rotation;
    private Recordset recordset;
    private transient TextStyleChangeListener textStyleChangeListener;
    private ActionListener buttonCloseListener;
    private boolean isDisposed;
    private EditHistory editHistory;
    private EditEnvironment environment;

    private static volatile TextStyleContainer dialog;

//    public static TextStyleContainer createInstance(EditEnvironment environment) {
//        if (null == dialog) {
//            dialog = new TextStyleContainer(environment);
//            dialog.setVisible(true);
//        }
//        return dialog;
//    }

    private TextStyleContainer() {
        this.environment = environment;
        isDisposed = false;
        initMainPanel();
        registEvents();
//        this.setTitle(ControlsProperties.getString("String_TextStyleSet"));
//        setLocationRelativeTo(null);
    }

    public void showDialog(Recordset recordset) {
        if (null != this.recordset) {
            this.recordset.dispose();
        }
        this.recordset = recordset;
        this.recordset.moveFirst();
        boolean hasGeoText = false;
        while (!recordset.isEOF()) {
            Geometry tempGeoMetry = recordset.getGeometry();
            if (tempGeoMetry instanceof GeoText || tempGeoMetry instanceof GeoText3D) {
                if (tempGeoMetry instanceof GeoText) {
                    text = ((GeoText) tempGeoMetry).getText();
                    tempTextStyle = ((GeoText) tempGeoMetry).getTextStyle();
                    rotation = ((GeoText) tempGeoMetry).getPart(0).getRotation();
                    this.geometry = tempGeoMetry;
                } else if (tempGeoMetry instanceof GeoText3D) {
                    text = ((GeoText3D) tempGeoMetry).getText();
                    tempTextStyle = ((GeoText3D) tempGeoMetry).getTextStyle();
                    this.geometry = tempGeoMetry;
                }
                hasGeoText = true;
                break;
            }
            recordset.moveNext();
        }
        if (!hasGeoText) {
            // 不为文本类型时显示为空
            this.removeAll();
            this.updateUI();
            return;
        }
        this.textBasicPanel.setTextStyle(tempTextStyle);
    }

    private void initMainPanel() {
//        JPanel panel = (JPanel) this.getContentPane();
//        panel.removeAll();
        this.textBasicPanel = new TextBasicPanel();
        this.textBasicPanel.setTextStyle(new TextStyle());
        this.textBasicPanel.setTextStyleSet(true);
        this.textBasicPanel.initTextBasicPanel();
        this.textBasicPanel.initCheckBoxState();
        this.textBasicPanel.enabled(true);
        this.setLayout(new GridBagLayout());
        this.buttonClose = ComponentFactory.createButtonClose();
        //@formatter:off
        this.add(textBasicPanel.getBasicsetPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
        this.add(textBasicPanel.getEffectPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
        this.add(this.buttonClose, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(5, 10, 10, 10));
        //@formatter:on
        this.updateUI();
    }

    public void registEvents() {
        this.textStyleChangeListener = new TextStyleChangeListener() {

            @Override
            public void modify(TextStyleType newValue) {
                updateGeometries(newValue);
            }
        };

        this.buttonCloseListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isDisposed) {
                    disposeInfo();
                }
            }
        };
//        this.addWindowListener(new WindowAdapter() {
//
//            @Override
//            public void windowClosing(WindowEvent e) {
//                if (!isDisposed) {
//                    disposeInfo();
//                }
//            }
//
//        });
        removeEvents();
        this.textBasicPanel.addTextStyleChangeListener(this.textStyleChangeListener);
        this.buttonClose.addActionListener(this.buttonCloseListener);
    }

    ;

    public void disposeInfo() {
        if (null != dialog) {
            removeEvents();
//            dialog.dispose();
            dialog = null;
            recordset.dispose();
            isDisposed = true;
            environment.stopEditor();
        }
    }

    private void updateGeometries(TextStyleType newValue) {
        editHistory = MapUtilities.getMapControl().getEditHistory();
        editHistory.batchBegin();
        recordset.moveFirst();
        while (!recordset.isEOF()) {
            editHistory.add(EditType.MODIFY, recordset, true);
            recordset.edit();
            Geometry tempGeometry = recordset.getGeometry();
            if (tempGeometry instanceof GeoText && !newValue.equals(TextStyleType.FIXEDSIZE)) {
                ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
            }
            if (tempGeometry instanceof GeoText && newValue.equals(TextStyleType.FIXEDSIZE)) {
                ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
                ResetTextStyleUtil.resetTextStyle(TextStyleType.FONTHEIGHT, ((GeoText) tempGeometry).getTextStyle(),
                        textBasicPanel.getResultMap().get(TextStyleType.FONTHEIGHT));
            }
            if (tempGeometry instanceof GeoText3D && !newValue.equals(TextStyleType.FIXEDSIZE)) {
                ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText3D) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
            }
            if (tempGeometry instanceof GeoText3D && newValue.equals(TextStyleType.FIXEDSIZE)) {
                ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText3D) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
                ResetTextStyleUtil.resetTextStyle(TextStyleType.FONTHEIGHT, ((GeoText3D) tempGeometry).getTextStyle(),
                        textBasicPanel.getResultMap().get(TextStyleType.FONTHEIGHT));
            }
            recordset.setGeometry(tempGeometry);
            tempGeometry.dispose();
            recordset.update();
            recordset.moveNext();
        }
        editHistory.batchEnd();
        MapUtilities.getActiveMap().refresh();
    }

    public void enabled(boolean enabled) {
        if (null != textBasicPanel && null != buttonClose) {
            this.textBasicPanel.enabled(enabled);
            this.buttonClose.setEnabled(enabled);
        }
    }

    ;

    private void removeEvents() {
        this.buttonClose.removeActionListener(this.buttonCloseListener);
        this.textBasicPanel.removeTextStyleChangeListener(this.textStyleChangeListener);
    }

    public TextStyle getTempTextStyle() {
        return tempTextStyle;
    }

    public void setTempTextStyle(TextStyle tempTextStyle) {
        this.tempTextStyle = tempTextStyle;
    }

    public void setEnvironment(EditEnvironment environment) {
        this.environment = environment;
    }
}
