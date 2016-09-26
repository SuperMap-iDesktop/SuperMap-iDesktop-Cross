package com.supermap.desktop.CtrlAction.CADStyle;

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
import java.util.ArrayList;

/**
 * 文本风格对话框
 *
 * @author xie 2016-6-3
 */
public class TextStyleContainer extends JPanel {

    private static final long serialVersionUID = 1L;

    private transient ITextStyle textBasicPanel;
    private TextStyle tempTextStyle = new TextStyle();// 用于预览的TextStyle
    private Double rotation = 0.0;
    private transient TextStyleChangeListener textStyleChangeListener;
    private EditHistory editHistory;
    private JScrollPane scrollPane;
    private static JPanel panelTextInfo;
    private CADStyleContainer parent;

    public TextStyleContainer() {
        super();
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

    public void init(CADStyleContainer parent) {
        this.parent = parent;
        tempTextStyle = getGeoText();
        if (null != tempTextStyle) {
            // 不为文本类型时，全部灰选
            initMainPanel();
            registEvents();
        } else {
            enabled(false);
        }
    }

    public TextStyle getGeoText() {
        TextStyle textStyle = null;
        ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
        if (null == recordsets) {
            return null;
        }
        int count = recordsets.size();
        for (int i = 0; i < count; i++) {
            Recordset recordset = recordsets.get(i);
            recordset.moveFirst();
            while (!recordset.isEOF()) {
                Geometry tempGeoMetry = recordset.getGeometry();
                if (tempGeoMetry instanceof GeoText) {
                    GeoStyle geoStyle = null;
                    textStyle = ((GeoText) tempGeoMetry).getTextStyle();
                    rotation = ((GeoText) tempGeoMetry).getPart(0).getRotation();
                    break;
                }
                recordset.moveNext();
            }
            recordset.dispose();
        }
        return textStyle;
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

    private void initMainPanel() {
        this.textBasicPanel = new TextBasicPanel();
        initTextBasicPanel();
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
    }

    public void initTextBasicPanel() {
        this.textBasicPanel.setTextStyle(tempTextStyle);
        this.textBasicPanel.setTextStyleSet(true);
        this.textBasicPanel.initTextBasicPanel();
        this.textBasicPanel.initCheckBoxState();
        this.textBasicPanel.enabled(true);
        this.textBasicPanel.getSpinnerRotationAngl().setValue(rotation);
    }

    public void registEvents() {
        this.textStyleChangeListener = new TextStyleChangeListener() {

            @Override
            public void modify(TextStyleType newValue) {
                updateGeometries(newValue);
                parent.setModify(true);
            }
        };

        removeEvents();
        this.textBasicPanel.addTextStyleChangeListener(this.textStyleChangeListener);
    }

    private void updateGeometries(TextStyleType newValue) {
        editHistory = MapUtilities.getMapControl().getEditHistory();
        ArrayList<Recordset> records = CADStyleUtilities.getActiveRecordset(MapUtilities.getActiveMap());
        if (null == records) {
            return;
        }
        int count = records.size();
        for (int i = 0; i < count; i++) {
            Recordset recordset = records.get(i);
            recordset.moveFirst();
            while (!recordset.isEOF()) {
                editHistory.add(EditType.MODIFY, recordset, true);
                recordset.edit();
                Geometry tempGeometry = recordset.getGeometry();
                Object newGeoStyleProperty = textBasicPanel.getResultMap().get(newValue);
                if (tempGeometry instanceof GeoText && !newValue.equals(TextStyleType.FIXEDSIZE)) {
                    if (newValue.equals(TextStyleType.ROTATION)) {
                        for (int j = 0; j < ((GeoText) tempGeometry).getPartCount(); j++) {
                            ((GeoText) tempGeometry).getPart(i).setRotation((Double) newGeoStyleProperty);
                        }
                    } else {
                        ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText) tempGeometry).getTextStyle(), newGeoStyleProperty);
                    }
                }
                if (tempGeometry instanceof GeoText && newValue.equals(TextStyleType.FIXEDSIZE)) {
                    ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText) tempGeometry).getTextStyle(), newGeoStyleProperty);
                    ResetTextStyleUtil.resetTextStyle(TextStyleType.FONTHEIGHT, ((GeoText) tempGeometry).getTextStyle(),
                            textBasicPanel.getResultMap().get(TextStyleType.FONTHEIGHT));
                }
                recordset.setGeometry(tempGeometry);
                tempGeometry.dispose();
                recordset.update();
                recordset.moveNext();
            }
            editHistory.batchEnd();
            recordset.dispose();
        }
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

}
