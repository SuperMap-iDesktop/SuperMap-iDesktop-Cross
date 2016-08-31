package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.*;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.textStyle.ITextStyle;
import com.supermap.desktop.ui.controls.textStyle.ResetTextStyleUtil;
import com.supermap.desktop.ui.controls.textStyle.TextBasicPanel;
import com.supermap.desktop.ui.controls.textStyle.TextStyleChangeListener;
import com.supermap.desktop.utilities.MapUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 文本风格对话框
 *
 * @author xie 2016-6-3
 */
public class TextStyleContainer extends JPanel {

    private static final long serialVersionUID = 1L;

    private transient ITextStyle textBasicPanel;

    private Geometry geometry;
    private String text;
    private TextStyle tempTextStyle;// 用于预览的TextStyle
    private Double rotation;
    private Recordset recordset;
    private transient TextStyleChangeListener textStyleChangeListener;
    private ActionListener buttonCloseListener;
    private boolean isModify;
    private EditHistory editHistory;
    private JScrollPane scrollPane;
    public static JPanel panelTextInfo;

    public TextStyleContainer() {
        super();
        isModify = false;
        this.setLayout(new GridBagLayout());
        this.scrollPane = new JScrollPane();
        this.panelTextInfo = new JPanel();
        this.scrollPane.setBorder(new LineBorder(Color.lightGray));
        this.add(this.scrollPane, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
        this.scrollPane.setViewportView(this.panelTextInfo);
    }

    /**
     * 资源化
     */

    public void showDialog(Recordset recordset) {
        if (!isModify) {
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
                this.scrollPane.setViewportView(panelTextInfo);
                return;
            }
            initMainPanel();
            registEvents();
        }
    }

    private void removePanels() {
        if (null != panelTextInfo) {
            remove(panelTextInfo);
        }
        for (int i = getComponents().length - 1; i >= 0; i--) {
            if (getComponent(i) instanceof JPanel) {
                remove(getComponent(i));
            }
        }
    }

    public void setNullPanel() {
        this.isModify = false;
        this.tempTextStyle = null;
        removePanels();
        this.scrollPane.setViewportView(panelTextInfo);
        if (null != recordset) {
            recordset.close();
            recordset.dispose();
        }
    }

    private void initMainPanel() {
        this.textBasicPanel = new TextBasicPanel();
        this.textBasicPanel.setTextStyle(tempTextStyle);
        this.textBasicPanel.setTextStyleSet(true);
        this.textBasicPanel.initTextBasicPanel();
        this.textBasicPanel.initCheckBoxState();
        this.textBasicPanel.enabled(true);
        removePanels();
        JPanel panelText = new JPanel();
        panelText.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.add(panelText, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
        panelText.add(textBasicPanel.getBasicsetPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
        panelText.add(textBasicPanel.getEffectPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
        this.scrollPane.setViewportView(panel);
        repaint();
    }

    public void registEvents() {
        this.textStyleChangeListener = new TextStyleChangeListener() {

            @Override
            public void modify(TextStyleType newValue) {
                isModify = true;
                updateGeometries(newValue);
            }
        };

        removeEvents();
        this.textBasicPanel.addTextStyleChangeListener(this.textStyleChangeListener);
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
        if (null != textBasicPanel) {
            this.textBasicPanel.enabled(enabled);
        }
    }

    ;

    private void removeEvents() {
        this.textBasicPanel.removeTextStyleChangeListener(this.textStyleChangeListener);
    }

    public void setModify(boolean modify) {
        isModify = modify;
    }
}
