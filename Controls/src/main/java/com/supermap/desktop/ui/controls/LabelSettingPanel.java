package com.supermap.desktop.ui.controls;

import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.ui.controls.textStyle.ITextStyle;
import com.supermap.desktop.ui.controls.textStyle.ResetTextStyleUtil;
import com.supermap.desktop.ui.controls.textStyle.TextBasicPanel;
import com.supermap.desktop.ui.controls.textStyle.TextStyleChangeListener;
import com.supermap.mapping.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Created By Chens on 2017/7/24 0024
 */
public class LabelSettingPanel extends JPanel {
    private ITextStyle textStylePanel;
    private TextStyle textStyle;

    public LabelSettingPanel(TextStyle textStyle) {
        this.textStyle = textStyle;
        textStylePanel = new TextBasicPanel();
        this.textStylePanel = new TextBasicPanel();
        this.textStylePanel.setTextStyle(textStyle);
        this.textStylePanel.setOutLineWidth(true);
        this.textStylePanel.setProperty(false);
        this.textStylePanel.setUnityVisible(true);
        this.textStylePanel.initTextBasicPanel();
        this.textStylePanel.initCheckBoxState();
        this.textStylePanel.enabled(true);
        this.setLayout(new GridBagLayout());
        this.add(this.textStylePanel.getBasicsetPanel(), new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(2, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
        this.add(this.textStylePanel.getEffectPanel(), new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(2, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));

        textStylePanel.addTextStyleChangeListener(new TextStyleChangeListener() {
            @Override
            public void modify(TextStyleType newValue) {
                resetTextStyle(newValue);
            }
        });
    }

    private void resetTextStyle(TextStyleType newValue) {
        ResetTextStyleUtil.resetTextStyle(newValue,textStyle,textStylePanel.getResultMap().get(newValue));
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }
}

