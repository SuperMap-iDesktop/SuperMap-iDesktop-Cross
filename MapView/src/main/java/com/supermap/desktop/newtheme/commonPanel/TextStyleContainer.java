package com.supermap.desktop.newtheme.commonPanel;

import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelAdvancePanel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.textStyle.ITextStyle;
import com.supermap.desktop.ui.controls.textStyle.ResetTextStyleUtil;
import com.supermap.desktop.ui.controls.textStyle.TextBasicPanel;
import com.supermap.desktop.ui.controls.textStyle.TextStyleChangeListener;
import com.supermap.desktop.utilities.FontUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class TextStyleContainer extends ThemeChangePanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private transient Theme theme;
    private transient MixedTextStyle mixedTextStyle;
    private transient Map map;
    private transient TextStyle textStyle;
    private transient Layer themeLayer;

    private int textStyleType = -1;
    private final int GRAPHTEXTFORMAT = 0;// 统计图标注风格
    private final int GRAPHAXISTEXT = 1;// 统计图坐标轴风格
    private final int LABELCOMPLICATEDDEFUALT = 2;// 标签复合风格默认文本风格
    private final int LABELCOMPLICATEDITEMS = 3;// 标签复合风格分段风格风格

    private String layerName;
    private int[] selectRow;
    private boolean isUniformStyle;
    private boolean isRefreshAtOnce;
    private ITextStyle textStylePanel;
    private transient TextStyleChangeListener textStyleChangeListener;
    private transient LocalMapDrawnListener mapDrawnListener = new LocalMapDrawnListener();
    private boolean isResetFontHeight = false;
    private boolean isMapDrawn = false;
    private double fontHeight = 0.0;

    public TextStyleContainer(TextStyle textStyle, Map map, Layer themeLabelLayer) {
        this.textStyle = textStyle.clone();
        this.map = map;
        this.themeLayer = themeLabelLayer;
        this.layerName = themeLabelLayer.getName();
        initComponents();
        registActionListener();
    }

    private void initComponents() {
        this.textStylePanel = new TextBasicPanel();
        this.textStylePanel.setTextStyle(this.textStyle);
        this.textStylePanel.setOutLineWidth(true);
        this.textStylePanel.setProperty(false);
        this.textStylePanel.setUnityVisible(true);
        this.textStylePanel.initTextBasicPanel();
        this.textStylePanel.initCheckBoxState();
        this.textStylePanel.enabled(true);
        this.setLayout(new GridBagLayout());
        //@formatter:off
        JPanel panelContent = new JPanel();
        this.add(panelContent, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
        panelContent.setLayout(new GridBagLayout());
        panelContent.add(this.textStylePanel.getBasicsetPanel(), new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(2, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelContent.add(this.textStylePanel.getEffectPanel(), new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(2, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
        //@formatter:on
    }

    private void refreshMapAtOnce() {
        firePropertyChange("ThemeChange", null, null);
        if (isRefreshAtOnce && null != map) {
            refreshMapAndLayer();
        }
    }

    @Override
    public void refreshMapAndLayer() {
        this.themeLayer = MapUtilities.findLayerByName(map, layerName);
        if (null != theme && theme instanceof ThemeLabel && this.isUniformStyle && this.themeLayer.getTheme() instanceof ThemeLabel) {
            if (null != ThemeLabelAdvancePanel.stringAlignment) {
                ((ThemeLabel) theme).getUniformStyle().setStringAlignment(ThemeLabelAdvancePanel.stringAlignment);
            }
            ((ThemeLabel) this.themeLayer.getTheme()).setUniformStyle(((ThemeLabel) theme).getUniformStyle());
            this.map.refresh();
            return;
        }
        if (null != theme && theme instanceof ThemeLabel && textStyleType != LABELCOMPLICATEDITEMS && textStyleType != LABELCOMPLICATEDDEFUALT
                && !this.isUniformStyle && this.themeLayer.getTheme() instanceof ThemeLabel) {
            for (int i = 0; i < ((ThemeLabel) this.themeLayer.getTheme()).getCount(); i++) {
                if (null != ThemeLabelAdvancePanel.stringAlignment) {
                    ((ThemeLabel) this.themeLayer.getTheme()).getItem(i).getStyle().setStringAlignment(ThemeLabelAdvancePanel.stringAlignment);
                }
            }
            for (int i = 0; i < this.selectRow.length; i++) {
                ((ThemeLabel) this.themeLayer.getTheme()).getItem(this.selectRow[i]).setStyle(((ThemeLabel) theme).getItem(selectRow[i]).getStyle());
            }
            this.map.refresh();
            return;
        }
        if (null != theme && theme instanceof ThemeGraph && textStyleType == GRAPHTEXTFORMAT) {
            ((ThemeGraph) this.themeLayer.getTheme()).setGraphTextStyle(((ThemeGraph) theme).getGraphTextStyle());
            this.map.refresh();
            return;
        }
        if (null != theme && theme instanceof ThemeGraph && textStyleType == GRAPHAXISTEXT) {
            ((ThemeGraph) this.themeLayer.getTheme()).setAxesTextStyle(((ThemeGraph) theme).getAxesTextStyle());
            this.map.refresh();
            return;
        }
        if (textStyleType == LABELCOMPLICATEDDEFUALT) {
            ((ThemeLabel) this.themeLayer.getTheme()).getUniformMixedStyle().setDefaultStyle(mixedTextStyle.getDefaultStyle());
            this.map.refresh();
            return;
        }
        if (textStyleType == LABELCOMPLICATEDITEMS) {
            ((ThemeLabel) this.themeLayer.getTheme()).getUniformMixedStyle().setStyles(mixedTextStyle.getStyles());
            this.map.refresh();
            return;
        }

    }

    @Override
    public void registActionListener() {
        this.textStyleChangeListener = new TextStyleChangeListener() {

            @Override
            public void modify(TextStyleType newValue) {
                if (isMapDrawn) {
                    return;
                }
                if (null != theme && theme instanceof ThemeLabel && isUniformStyle) {
                    resetFontHeightWhileFixedSize(newValue, ((ThemeLabel) theme).getUniformStyle());
                    refreshMapAtOnce();
                    return;
                }
                if (textStyleType != LABELCOMPLICATEDITEMS && textStyleType != LABELCOMPLICATEDDEFUALT && !isUniformStyle && null != theme
                        && theme instanceof ThemeLabel) {
                    // 分段标签专题图设置段风格
                    for (int i = 0; i < selectRow.length; i++) {
                        resetFontHeightWhileFixedSize(newValue, ((ThemeLabel) theme).getItem(selectRow[i]).getStyle());
                    }
                    refreshMapAtOnce();
                    return;
                }
                if (null != theme && theme instanceof ThemeGraph && textStyleType == GRAPHTEXTFORMAT) {
                    // 统计专题图设置标注风格
                    resetFontHeightWhileFixedSize(newValue, ((ThemeGraph) theme).getGraphTextStyle());
                    refreshMapAtOnce();
                    return;
                }
                if (null != theme && theme instanceof ThemeGraph && textStyleType == GRAPHAXISTEXT) {
                    // 统计专题图设置坐标轴风格
                    resetFontHeightWhileFixedSize(newValue, ((ThemeGraph) theme).getAxesTextStyle());
                    refreshMapAtOnce();
                    return;
                }
                if (textStyleType == LABELCOMPLICATEDDEFUALT) {
                    // 复合标签专题图设置默认风格
                    resetFontHeightWhileFixedSize(newValue, mixedTextStyle.getDefaultStyle());
                    refreshMapAtOnce();
                    return;
                }
                if (textStyleType == LABELCOMPLICATEDITEMS) {
                    // 复合标签专题图设置分段风格
                    resetMixedTextStyles(newValue);
                    refreshMapAtOnce();
                    return;
                }
            }

        };
        unregistActionListener();
        this.textStylePanel.addTextStyleChangeListener(textStyleChangeListener);
        this.map.addDrawnListener(this.mapDrawnListener);
        MapUtilities.getMapControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                isMapDrawn = true;
                isResetFontHeight = false;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isMapDrawn = false;
                isResetFontHeight = true;
            }
        });
    }

    private void resetFontHeightWhileFixedSize(TextStyleType newValue, TextStyle textStyle) {
        Object newGeoStyleProperty = textStylePanel.getResultMap().get(newValue);
        if (!newValue.equals(TextStyleType.FIXEDSIZE)) {
            if (newValue.equals(TextStyleType.FONTHEIGHT) && false == textStylePanel.getResultMap().get(TextStyleType.FIXEDSIZE)) {
                fontHeight = (double) newGeoStyleProperty / 100;
                ResetTextStyleUtil.resetTextStyle(newValue, textStyle, (double) newGeoStyleProperty / 100);
            } else {
                ResetTextStyleUtil.resetTextStyle(newValue, textStyle, newGeoStyleProperty);
            }
        }
        if (newValue.equals(TextStyleType.FIXEDSIZE)) {
            ResetTextStyleUtil.resetTextStyle(newValue, textStyle, newGeoStyleProperty);
            ResetTextStyleUtil.resetTextStyle(TextStyleType.FONTHEIGHT, textStyle, textStylePanel.getResultMap().get(TextStyleType.FONTHEIGHT));
            fontHeight = (double) textStylePanel.getResultMap().get(TextStyleType.FONTHEIGHT);
        }
    }

    private void resetMixedTextStyles(TextStyleType newValue) {
        for (int i = 0; i < selectRow.length; i++) {
            resetFontHeightWhileFixedSize(newValue, mixedTextStyle.getStyles()[selectRow[i]]);
            ResetTextStyleUtil.resetTextStyle(newValue, mixedTextStyle.getStyles()[selectRow[i]], textStylePanel.getResultMap().get(newValue));
        }
    }

    class LocalMapDrawnListener implements MapDrawnListener {

        @Override
        public void mapDrawn(MapDrawnEvent mapDrawnEvent) {
            // 由于控件问题暂不支持缩放地图修改字高，字号显示大小变化
            changeFontSizeWithMapObject();
        }
    }

    private void changeFontSizeWithMapObject() {

        try {
            if (isResetFontHeight) {
                return;
            }
            // 非固定文本大小
            if (!((ThemeLabel) theme).getUniformStyle().isSizeFixed()) {
                // 非固定时，地图中显示的字体在屏幕中显示的大小肯定发生了变化，所以需要重新计算现在的字体大小
                // 字体信息从现在的TextStyle属性中获取，经过计算后显示其字号大小
                Double size = FontUtilities.mapHeightToFontSize(fontHeight, map, false);
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String numeric = "0.00";
                if (Double.compare(size, size.intValue()) > 0) {
                    ((JTextField) textStylePanel.getComponentsMap().get(TextStyleType.FONTSIZE)).setText(decimalFormat.format(size));
                } else {
                    decimalFormat = new DecimalFormat("0");
                    ((JTextField) textStylePanel.getComponentsMap().get(TextStyleType.FONTSIZE)).setText(decimalFormat.format(size));
                }
                if (((JTextField) textStylePanel.getComponentsMap().get(TextStyleType.FONTHEIGHT)).getFocusTraversalKeysEnabled()) {
                    ((JTextField) textStylePanel.getComponentsMap().get(TextStyleType.FONTHEIGHT)).setText(new DecimalFormat(numeric).format(size / 0.283));
                }
            } else {
                // 字体是固定大小时，字体显示的大小不发生变化，不需要更新任何控件内容

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void unregistActionListener() {
        this.textStylePanel.removeTextStyleChangeListener(this.textStyleChangeListener);
    }

    public void setTextStyleType(int textStyleType) {
        this.textStyleType = textStyleType;
    }

    @Override
    public Layer getCurrentLayer() {
        return themeLayer;
    }

    @Override
    public void setCurrentLayer(Layer layer) {
        this.themeLayer = layer;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setSelectRow(int[] selectRow) {
        this.selectRow = selectRow;
    }

    @Override
    public Theme getCurrentTheme() {
        return this.theme;
    }

    @Override
    public void setRefreshAtOnce(boolean isRefreshAtOnce) {
        this.isRefreshAtOnce = isRefreshAtOnce;
    }

    public void setUniformStyle(boolean isUniformStyle) {
        this.isUniformStyle = isUniformStyle;
    }

    public void setMixedTextStyle(MixedTextStyle mixedTextStyle) {
        this.mixedTextStyle = mixedTextStyle;
    }

}
