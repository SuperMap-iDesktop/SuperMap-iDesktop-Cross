package com.supermap.desktop.ui.controls.textStyle;

import com.supermap.data.StringAlignment;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.SymbolSpinnerUtilties;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ColorSelectButton;
import com.supermap.desktop.ui.controls.FontComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.FontUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 * 字体基本信息界面
 *
 * @author xie
 */
public class TextBasicPanel extends JPanel implements ITextStyle {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JLabel labelFontName;// 文本名称
    private FontComboBox comboBoxFontName;
    private JLabel labelAlign;// 文本对齐方式
    private JComboBox<String> comboBoxAlign;
    private JLabel labelFontSize; // 字号
    private JComboBox<String> comboBoxFontSize;
    private JLabel labelFontHeight;// 字高
    private JSpinner spinnerFontHeight;
    private JLabel labelFontHeightUnity;// 字高单位
    private JLabel labelFontWidth;// 字宽
    private JSpinner spinnerFontWidth;
    private JLabel labelFontWidthUnity;// 字宽单位
    private JLabel labelRotationAngl;// 旋转角度
    private JSpinner spinnerRotationAngl;
    private JLabel labelInclinationAngl;// 倾斜角度
    private JSpinner spinnerInclinationAngl;
    private JLabel labelFontColor;// 文本颜色
    private ColorSelectButton buttonFontColorSelect;
    private JLabel labelBGColor;// 背景颜色
    private ColorSelectButton buttonBGColorSelect;
    // 对齐方式名称和对齐方式值构成的HashMap
    private static HashMap<String, TextAlignment> hashMapTextAlignment = new HashMap<String, TextAlignment>();

    private HashMap<TextStyleType, Object> textStyleTypeMap;
    private HashMap<TextStyleType, JComponent> componentsMap;

    private JCheckBox checkBoxBorder;// 加粗
    private JCheckBox checkBoxStrickout;// 删除线
    private JCheckBox checkBoxItalic;// 斜体
    private JCheckBox checkBoxUnderline;// 下划线
    private JCheckBox checkBoxShadow;// 阴影
    private JCheckBox checkBoxFixedSize;// 固定大小
    private JCheckBox checkBoxBGOpaque;// 背景透明
    private JCheckBox checkBoxOutline;// 轮廓
    private JSpinner spinnerOutLineWidth;
    // private JSlider sliderOutLineWidth;// 轮廓宽度
    private JLabel labelStringAlignment;// 文本对齐方式
    private JComboBox<String> comboBoxStringAlignment;
    private TextStyle textStyle;

    private JPanel panelBasicset;
    private JPanel panelEffect;

    // 显示精度
    private String numeric = "0.00";
    // 经验值
    private final static double EXPERIENCE = 0.283;
    private boolean isProperty;
    private boolean unityVisible;
    private boolean showOutLineWidth;
    private boolean isTextStyleSet;
    private boolean isSetFontSize;
    private boolean isSetFontHeight;
    protected double UNIT_CONVERSION = 10;
    private final double pow = 2;

    private JTextField textFieldFontSize;
    private JFormattedTextField textFieldFontHeight;
    private JFormattedTextField textFieldFontWidth;
    private JFormattedTextField textFieldFontItalicAngl;
    private JFormattedTextField textFieldFontRotationAngl;
    private Vector<TextStyleChangeListener> textStyleChangedListeners;

    private ItemListener fontNameItemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                textStyleTypeMap.put(TextStyleType.FONTNAME, comboBoxFontName.getSelectedItem().toString());
                fireTextStyleChanged(TextStyleType.FONTNAME);
            }
        }
    };
    private ItemListener alignItemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String textAlignmentName = comboBoxAlign.getSelectedItem().toString();
                TextAlignment textAlignment = hashMapTextAlignment.get(textAlignmentName);
                textStyleTypeMap.put(TextStyleType.ALIGNMENT, textAlignment);
                fireTextStyleChanged(TextStyleType.ALIGNMENT);
            }
        }
    };

    private PropertyChangeListener fontColorProperty = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Color color = buttonFontColorSelect.getColor();
            if (color != null) {
                textStyleTypeMap.put(TextStyleType.FORECOLOR, color);
                fireTextStyleChanged(TextStyleType.FORECOLOR);
            }
        }
    };
    private PropertyChangeListener backColorListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Color color = buttonBGColorSelect.getColor();
            if (color != null) {
                textStyleTypeMap.put(TextStyleType.BACKCOLOR, color);
                fireTextStyleChanged(TextStyleType.BACKCOLOR);
            }
        }
    };
    private ActionListener checkboxListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(checkBoxBGOpaque)) {
                // 控制背景颜色按钮的可显示
                resetBGColorEnabled();
                boolean isOpare = checkBoxBGOpaque.isSelected();
                checkBoxOutline.setEnabled(isOpare);
                textStyleTypeMap.put(TextStyleType.BACKOPAQUE, !isOpare);
                fireTextStyleChanged(TextStyleType.BACKOPAQUE);
                return;
            }
            if (e.getSource().equals(checkBoxBorder)) {
                boolean isBold = checkBoxBorder.isSelected();
                textStyleTypeMap.put(TextStyleType.BOLD, isBold);
                fireTextStyleChanged(TextStyleType.BOLD);
                return;
            }
            if (e.getSource().equals(checkBoxFixedSize) && !StringUtilities.isNullOrEmpty(textFieldFontHeight.getText())) {
                boolean isFixedSize = checkBoxFixedSize.isSelected();
                Double height = 0.0;
                if (isFixedSize) {
                    height = Double.parseDouble(textFieldFontHeight.getText());
                    height /= 10;
                } else {
                    double size = Double.parseDouble(textFieldFontSize.getText());
                    height = FontUtilities.fontSizeToMapHeight(size, MapUtilities.getActiveMap(), isFixedSize);
                }
                if (height > 0) {
                    textStyleTypeMap.put(TextStyleType.FONTHEIGHT, height);
                }
                textStyleTypeMap.put(TextStyleType.FIXEDSIZE, isFixedSize);
                fireTextStyleChanged(TextStyleType.FIXEDSIZE);
                return;
            }
            if (e.getSource().equals(checkBoxItalic)) {
                boolean isItalic = checkBoxItalic.isSelected();
                // 控制倾斜角度的可显示
                spinnerInclinationAngl.setEnabled(isItalic);
                textStyleTypeMap.put(TextStyleType.ITALIC, isItalic);
                fireTextStyleChanged(TextStyleType.ITALIC);
                return;
            }
            if (e.getSource().equals(checkBoxOutline)) {
                boolean isOutLine = checkBoxOutline.isSelected();
                spinnerOutLineWidth.setEnabled(isOutLine);
                // 控制背景颜色按钮的可显示
                resetBGColorEnabled();
                textStyleTypeMap.put(TextStyleType.OUTLINE, isOutLine);
                fireTextStyleChanged(TextStyleType.OUTLINE);
                return;
            }
            if (e.getSource().equals(checkBoxShadow)) {
                boolean isShadow = checkBoxShadow.isSelected();
                textStyleTypeMap.put(TextStyleType.SHADOW, isShadow);
                fireTextStyleChanged(TextStyleType.SHADOW);
                return;
            }
            if (e.getSource().equals(checkBoxStrickout)) {
                boolean isStrickout = checkBoxStrickout.isSelected();
                textStyleTypeMap.put(TextStyleType.STRIKOUT, isStrickout);
                fireTextStyleChanged(TextStyleType.STRIKOUT);
                return;
            }
            if (e.getSource().equals(checkBoxUnderline)) {
                boolean isUnderLine = checkBoxUnderline.isSelected();
                textStyleTypeMap.put(TextStyleType.UNDERLINE, isUnderLine);
                fireTextStyleChanged(TextStyleType.UNDERLINE);
                return;
            }
        }
    };
    private ChangeListener outLineWidthChangeListener = new OutLineChangeListener();
    private ItemListener comboboxListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int stringAlignment = comboBoxStringAlignment.getSelectedIndex();
                switch (stringAlignment) {
                    case 0:
                        textStyleTypeMap.put(TextStyleType.STRINGALIGNMENT, StringAlignment.LEFT);
                        break;
                    case 1:
                        textStyleTypeMap.put(TextStyleType.STRINGALIGNMENT, StringAlignment.CENTER);
                        break;
                    case 2:
                        textStyleTypeMap.put(TextStyleType.STRINGALIGNMENT, StringAlignment.RIGHT);
                        break;
                    case 3:
                        textStyleTypeMap.put(TextStyleType.STRINGALIGNMENT, StringAlignment.DISTRIBUTED);
                        break;
                    default:
                        break;
                }
                fireTextStyleChanged(TextStyleType.STRINGALIGNMENT);
            }
        }
    };

    private JFormattedTextField textfieldOutLineWidth;
    private CaretListener textFieldFontSizeListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (isSetFontSize) {
                String text = textFieldFontSize.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(0.1, 72.17, text)) {
                    textFieldFontSize.setForeground(Color.red);
                    return;
                } else {
                    textFieldFontSize.setForeground(Color.black);
                }
                double size = Double.valueOf(text);
                isSetFontHeight = false;
                textFieldFontHeight.setText(new DecimalFormat(numeric).format((size / EXPERIENCE)));
                double fontHeight = size / EXPERIENCE;
                fontHeight = FontUtilities.fontSizeToMapHeight(size, MapUtilities.getActiveMap(), textStyle.isSizeFixed());
                if (!DoubleUtilities.equals(fontHeight, textStyle.getFontHeight(), pow) && fontHeight > 0) {
                    textStyleTypeMap.put(TextStyleType.FONTSIZE, fontHeight);
                    fireTextStyleChanged(TextStyleType.FONTSIZE);
                }
            }
        }
    };
    private CaretListener textfieldFontHeightListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (isSetFontHeight) {
                String text = textFieldFontHeight.getText();
                if (!SymbolSpinnerUtilties.isLegitNumber(0.1, 255d, text)) {
                    textFieldFontHeight.setForeground(Color.red);
                    return;
                } else {
                    textFieldFontHeight.setForeground(Color.black);
                }
                double logicalHeight = Double.parseDouble(text);
                Double size = logicalHeight * EXPERIENCE;
                isSetFontSize = false;
                comboBoxFontSize.setSelectedItem(new DecimalFormat("0.0").format(size));
                double fontHeight = logicalHeight;
                fontHeight = FontUtilities.fontSizeToMapHeight(size, MapUtilities.getActiveMap(), textStyle.isSizeFixed());
                if (!DoubleUtilities.equals(fontHeight, textStyle.getFontHeight(), pow) && fontHeight > 0) {
                    textStyleTypeMap.put(TextStyleType.FONTHEIGHT, fontHeight);
                    fireTextStyleChanged(TextStyleType.FONTHEIGHT);
                }
            }
        }
    };
    private CaretListener textfieldFontRotationAnglListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            String text = textFieldFontRotationAngl.getText();
            if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
                textFieldFontRotationAngl.setForeground(Color.red);
                return;
            } else {
                textFieldFontRotationAngl.setForeground(Color.black);
            }
            double rotationAngl = Double.parseDouble(text);
            textStyleTypeMap.put(TextStyleType.ROTATION, rotationAngl);
            fireTextStyleChanged(TextStyleType.ROTATION);
        }
    };
    private CaretListener textFieldFontItalicAnglListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            String text = textFieldFontItalicAngl.getText();
            if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
                textFieldFontItalicAngl.setForeground(Color.red);
                return;
            } else {
                textFieldFontItalicAngl.setForeground(Color.black);
            }
            double italicAngl = Double.parseDouble(text);
            textStyleTypeMap.put(TextStyleType.ITALICANGLE, italicAngl);
            fireTextStyleChanged(TextStyleType.ITALICANGLE);
        }
    };
    private CaretListener textfieldOutLineWidthListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            String text = textfieldOutLineWidth.getText();
            if (!SymbolSpinnerUtilties.isLegitNumber(1, 5, text)) {
                textfieldOutLineWidth.setForeground(Color.red);
                return;
            } else {
                textfieldOutLineWidth.setForeground(Color.black);
            }
            textStyleTypeMap.put(TextStyleType.OUTLINEWIDTH, spinnerOutLineWidth.getValue());
            fireTextStyleChanged(TextStyleType.OUTLINEWIDTH);
        }
    };
    private FocusListener textFieldFontSizeFocusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            super.focusGained(e);
            isSetFontSize = true;
        }
    };
    private FocusListener textfieldFontHeightFocusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            super.focusGained(e);
            isSetFontHeight = true;
        }
    };

    public TextBasicPanel() {
        // Do nothing
    }

    private void registEvents() {
        removeEvents();
        this.comboBoxFontName.addItemListener(this.fontNameItemListener);
        this.comboBoxAlign.addItemListener(this.alignItemListener);
        this.textFieldFontSize.addFocusListener(this.textFieldFontSizeFocusListener);
        this.textFieldFontHeight.addFocusListener(this.textfieldFontHeightFocusListener);
        this.textFieldFontSize.addCaretListener(this.textFieldFontSizeListener);
        this.textFieldFontHeight.addCaretListener(this.textfieldFontHeightListener);
        this.textFieldFontRotationAngl.addCaretListener(this.textfieldFontRotationAnglListener);
        this.textFieldFontItalicAngl.addCaretListener(this.textFieldFontItalicAnglListener);
        this.textfieldOutLineWidth.addCaretListener(this.textfieldOutLineWidthListener);
        this.buttonFontColorSelect.addPropertyChangeListener("m_selectionColors", this.fontColorProperty);
        this.buttonBGColorSelect.addPropertyChangeListener("m_selectionColors", this.backColorListener);
        this.checkBoxBGOpaque.addActionListener(this.checkboxListener);
        this.checkBoxBorder.addActionListener(this.checkboxListener);
        this.checkBoxFixedSize.addActionListener(this.checkboxListener);
        this.checkBoxItalic.addActionListener(this.checkboxListener);
        this.checkBoxOutline.addActionListener(this.checkboxListener);
        this.checkBoxShadow.addActionListener(this.checkboxListener);
        this.checkBoxStrickout.addActionListener(this.checkboxListener);
        this.checkBoxUnderline.addActionListener(this.checkboxListener);
        this.comboBoxStringAlignment.addItemListener(this.comboboxListener);
    }

    private void initResources() {
        this.labelFontName.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontName"));
        if (isProperty || !showOutLineWidth) {
            this.labelAlign.setText(ControlsProperties.getString("String_AnchorAlignment"));
        } else {
            this.labelAlign.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelAlinement"));
        }
        if (isTextStyleSet) {
            this.labelStringAlignment.setText(ControlsProperties.getString("String_TextAlignment"));
        } else {
            this.labelStringAlignment.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelAlinement"));
        }
        this.labelFontSize.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontSize"));
        this.labelFontHeight.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontHeight"));
        this.labelFontWidth.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelFontWidth"));
        this.labelRotationAngl.setText(ControlsProperties.getString("String_Label_SymbolAngle"));
        this.labelInclinationAngl.setText(ControlsProperties.getString("String_Label_ItalicAngle"));
        this.labelFontColor.setText(ControlsProperties.getString("String_Label_TextStyleForeColor"));
        this.labelBGColor.setText(ControlsProperties.getString("String_BackgroundColor") + ":");
        this.labelFontHeightUnity.setText("(0.1mm)");
        this.labelFontWidthUnity.setText("(0.1mm)");
        this.checkBoxBorder.setText(ControlsProperties.getString("String_OverStriking"));
        this.checkBoxStrickout.setText(ControlsProperties.getString("String_DeleteLine"));
        this.checkBoxItalic.setText(ControlsProperties.getString("String_Italic"));
        this.checkBoxUnderline.setText(ControlsProperties.getString("String_Underline"));
        this.checkBoxShadow.setText(ControlsProperties.getString("String_Shadow"));
        this.checkBoxFixedSize.setText(ControlsProperties.getString("String_FixedSize"));
        this.checkBoxOutline.setText(ControlsProperties.getString("String_Contour"));
        this.checkBoxBGOpaque.setText(ControlsProperties.getString("String_BackgroundTransparency"));
    }

    /**
     * 初始化风格界面布局
     */
    private void init() {
        initBasicsetPanel();
        initEffectPanel();
    }

    private void initEffectPanel() {
        //@formatter:off
        this.panelEffect = new JPanel();
        this.removeAll();
        initTextStyleTypeMap();
//		initCheckBoxState();
        this.panelEffect.setBorder(new TitledBorder(ControlsProperties.getString("String_GeometryPropertyTextControl_GroupBoxFontEffect")));
        this.panelEffect.setLayout(new GridBagLayout());
        this.panelEffect.add(this.checkBoxBorder, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));
        this.panelEffect.add(this.checkBoxStrickout, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));

        this.panelEffect.add(this.checkBoxItalic, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));
        this.panelEffect.add(this.checkBoxUnderline, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));

        this.panelEffect.add(this.checkBoxShadow, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));
        this.panelEffect.add(this.checkBoxFixedSize, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));

        this.panelEffect.add(this.checkBoxBGOpaque, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));
        if (isProperty) {
            this.panelEffect.add(this.checkBoxOutline, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 2, 10));

            this.panelEffect.add(this.labelStringAlignment, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 15, 2, 10));
            this.panelEffect.add(this.comboBoxStringAlignment, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(2, 10, 2, 10));
        } else if (showOutLineWidth) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(this.checkBoxOutline, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 0, 0, 0));
            jPanel.add(this.spinnerOutLineWidth, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(78, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2, 10, 0, 0));
            jPanel.add(new JLabel(CommonProperties.getString("String_Label_Pixel")), new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2, 10, 0, 0));
            this.panelEffect.add(jPanel, new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
        } else {
            this.panelEffect.add(this.checkBoxOutline, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(2, 10, 0, 10));
        }
        //@formatter:on
    }

    @Override
    public void initCheckBoxState() {
        if (null != this.textStyle) {
            this.checkBoxBorder.setSelected(textStyle.getBold());
            this.checkBoxStrickout.setSelected(textStyle.getStrikeout());
            this.checkBoxItalic.setSelected(textStyle.getItalic());
            this.checkBoxUnderline.setSelected(textStyle.getUnderline());
            this.checkBoxShadow.setSelected(textStyle.getShadow());
            this.checkBoxFixedSize.setSelected(textStyle.isSizeFixed());
            this.checkBoxOutline.setSelected(textStyle.getOutline());
            this.checkBoxBGOpaque.setSelected(!textStyle.getBackOpaque());
            this.checkBoxOutline.setEnabled(!textStyle.getBackOpaque());
            this.spinnerInclinationAngl.setEnabled(textStyle.getItalic());
        }
    }

    private void resetBGColorEnabled() {
        boolean isOpare = this.checkBoxBGOpaque.isSelected();
        boolean isEnabled = this.checkBoxBGOpaque.isEnabled();
        boolean isOutlook = this.checkBoxOutline.isSelected();
        if ((!isOpare || isOutlook) && isEnabled) {
            buttonBGColorSelect.setEnabled(true);
        } else {
            buttonBGColorSelect.setEnabled(false);
        }
    }

    private void initSpinnerOutLineWidth() {
        this.spinnerOutLineWidth.setModel(new SpinnerNumberModel(1, 1, 5, 1));
        this.textfieldOutLineWidth = ((NumberEditor) this.spinnerOutLineWidth.getEditor()).getTextField();
        textfieldOutLineWidth.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            private void changed() {
                String text = textfieldOutLineWidth.getText();
                if (StringUtilities.isNullOrEmpty(text)) {
                    return;
                }
                try {
                    Integer integer = Integer.valueOf(text);
                    if (integer <= 0 || integer > 5) {
                        textfieldOutLineWidth.setForeground(Color.red);
                    } else {
                        textfieldOutLineWidth.setForeground(Color.black);
                        textStyleTypeMap.put(TextStyleType.OUTLINEWIDTH, spinnerOutLineWidth.getValue());
                        fireTextStyleChanged(TextStyleType.OUTLINEWIDTH);
                    }
                } catch (NumberFormatException e) {
                    textfieldOutLineWidth.setForeground(Color.red);
                }
            }
        });
        if (null != this.textStyle) {
            spinnerOutLineWidth.setEnabled(textStyle.getOutline());
            textfieldOutLineWidth.setText(String.valueOf(textStyle.getOutlineWidth()));
        }
    }

    private void initTextStyleTypeMap() {
        this.componentsMap = new HashMap<TextStyleType, JComponent>();
        this.checkBoxBGOpaque = new JCheckBox();
        this.componentsMap.put(TextStyleType.BACKOPAQUE, this.checkBoxBGOpaque);
        this.checkBoxBorder = new JCheckBox();
        this.componentsMap.put(TextStyleType.BOLD, this.checkBoxBorder);
        this.checkBoxFixedSize = new JCheckBox();
        this.componentsMap.put(TextStyleType.FIXEDSIZE, this.checkBoxFixedSize);
        this.checkBoxItalic = new JCheckBox();
        this.componentsMap.put(TextStyleType.ITALIC, this.checkBoxItalic);
        this.checkBoxOutline = new JCheckBox();
        this.componentsMap.put(TextStyleType.OUTLINE, this.checkBoxOutline);
        this.checkBoxShadow = new JCheckBox();
        this.componentsMap.put(TextStyleType.SHADOW, this.checkBoxShadow);
        this.checkBoxStrickout = new JCheckBox();
        this.componentsMap.put(TextStyleType.STRIKOUT, this.checkBoxStrickout);
        this.checkBoxUnderline = new JCheckBox();
        this.componentsMap.put(TextStyleType.UNDERLINE, this.checkBoxUnderline);
        this.componentsMap.put(TextStyleType.OUTLINEWIDTH, this.spinnerOutLineWidth);
        this.componentsMap.put(TextStyleType.FONTSIZE, textFieldFontSize);
        this.componentsMap.put(TextStyleType.FONTHEIGHT, this.textFieldFontHeight);
    }

    private void initBasicsetPanel() {
        panelBasicset = new JPanel();
        panelBasicset.removeAll();
        panelBasicset.setLayout(new GridBagLayout());
        initTextStyleMap();
        this.comboBoxFontName.setSelectedItem(this.textStyle.getFontName());
        initComboBoxAlign();
        initComboBoxFontSize();
        initTextFieldFontWidth();
        initTextFieldFontItalicAngl();
        initTextFieldFontRotation();
        initSpinnerOutLineWidth();
        initLayout();
        this.labelFontWidth.setVisible(false);
        this.spinnerFontWidth.setVisible(false);
        this.labelFontWidthUnity.setVisible(false);
    }

    private void initLayout() {

        initBasicLayout();
        if (!isTextStyleSet) {
            initUnityLayout();
            initPropertyLayout();
        } else {
            initTextStyleSetLayout();
        }
    }

    private void initTextStyleSetLayout() {
        //@formatter:off
        panelBasicset.add(this.labelStringAlignment, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        panelBasicset.add(this.comboBoxStringAlignment, new GridBagConstraintsHelper(1, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelBasicset.add(this.labelRotationAngl, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        panelBasicset.add(this.spinnerRotationAngl, new GridBagConstraintsHelper(1, 4, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelBasicset.add(this.labelFontColor, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        panelBasicset.add(this.buttonFontColorSelect, new GridBagConstraintsHelper(1, 5, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelBasicset.add(this.labelBGColor, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        panelBasicset.add(this.buttonBGColorSelect, new GridBagConstraintsHelper(1, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        //@formatter:on
    }

    private void initPropertyLayout() {
        //@formatter:off
        if (isProperty) {
            panelBasicset.add(this.labelFontColor, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.buttonFontColorSelect, new GridBagConstraintsHelper(1, 5, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelBGColor, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.buttonBGColorSelect, new GridBagConstraintsHelper(1, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        } else {
            panelBasicset.add(this.labelRotationAngl, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.spinnerRotationAngl, new GridBagConstraintsHelper(1, 5, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelInclinationAngl, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.spinnerInclinationAngl, new GridBagConstraintsHelper(1, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelFontColor, new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.buttonFontColorSelect, new GridBagConstraintsHelper(1, 7, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelBGColor, new GridBagConstraintsHelper(0, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.buttonBGColorSelect, new GridBagConstraintsHelper(1, 8, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        }
        //@formatter:on
    }

    private void initUnityLayout() {
        //@formatter:off
        panelBasicset.add(this.labelFontHeight, new GridBagConstraintsHelper(0, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        if (unityVisible) {
            panelBasicset.add(this.spinnerFontHeight, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(70, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelFontHeightUnity, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 0, 5, 10));
            panelBasicset.add(this.labelFontWidth, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.spinnerFontWidth, new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(70, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelFontWidthUnity, new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 0, 5, 10));
        } else {
            panelBasicset.add(this.spinnerFontHeight, new GridBagConstraintsHelper(1, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
            panelBasicset.add(this.labelFontWidth, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
            panelBasicset.add(this.spinnerFontWidth, new GridBagConstraintsHelper(1, 4, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        }
        //@formatter:on
    }

    private void initBasicLayout() {
        //@formatter:off
        panelBasicset.add(this.labelFontName, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10, 10, 5, 0));
        panelBasicset.add(this.comboBoxFontName, new GridBagConstraintsHelper(1, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(10, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelBasicset.add(this.labelAlign, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        panelBasicset.add(this.comboBoxAlign, new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        panelBasicset.add(this.labelFontSize, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
        panelBasicset.add(this.comboBoxFontSize, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(80, 1).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));
        //@formatter:on
    }

    class OutLineChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (null != spinnerOutLineWidth.getValue()) {
                textStyleTypeMap.put(TextStyleType.OUTLINEWIDTH, spinnerOutLineWidth.getValue());
                fireTextStyleChanged(TextStyleType.OUTLINEWIDTH);
            }
        }
    }

    private void initTextStyleMap() {
        this.textStyleTypeMap = new HashMap<TextStyleType, Object>();
        this.labelFontName = new JLabel();
        this.comboBoxFontName = new FontComboBox();
        this.labelAlign = new JLabel();
        this.comboBoxAlign = new JComboBox<String>();
        this.labelFontSize = new JLabel();
        this.comboBoxFontSize = new JComboBox<String>();
        this.labelFontHeight = new JLabel();
        this.spinnerFontHeight = new JSpinner();
        this.labelFontHeightUnity = new JLabel();
        this.labelFontWidth = new JLabel();
        this.spinnerFontWidth = new JSpinner();
        this.labelFontWidthUnity = new JLabel();
        this.labelRotationAngl = new JLabel();
        this.spinnerRotationAngl = new JSpinner();
        this.labelInclinationAngl = new JLabel();
        this.spinnerInclinationAngl = new JSpinner();
        this.spinnerOutLineWidth = new JSpinner();
        this.labelFontColor = new JLabel();
        // 此处直接提取示范数据中的颜色来初始化ColorSelectButton有正确的RGB值
        // 但是显示异常，故新建一个Color来规避此问题
        Color foreColor = new Color(this.textStyle.getForeColor().getRGB());
        Color backColor = new Color(this.textStyle.getBackColor().getRGB());
        this.buttonFontColorSelect = new ColorSelectButton(foreColor);
        this.labelBGColor = new JLabel();
        this.buttonBGColorSelect = new ColorSelectButton(backColor);
        this.buttonBGColorSelect.setColor(this.textStyle.getBackColor());
        this.labelStringAlignment = new JLabel();
        initComboBoxStringAlignMent();
        this.componentsMap = new HashMap<TextStyleType, JComponent>();
        this.componentsMap.put(TextStyleType.STRINGALIGNMENT, comboBoxStringAlignment);
        this.componentsMap.put(TextStyleType.FONTHEIGHT, this.spinnerFontHeight);
        this.componentsMap.put(TextStyleType.FONTSIZE, this.comboBoxFontSize);
        this.componentsMap.put(TextStyleType.ITALICANGLE, this.spinnerInclinationAngl);
        this.componentsMap.put(TextStyleType.BACKCOLOR, this.buttonBGColorSelect);
    }

    private void initComboBoxStringAlignMent() {
        this.comboBoxStringAlignment = new JComboBox<String>();
        this.comboBoxStringAlignment.setModel(new DefaultComboBoxModel<String>(new String[]{ControlsProperties.getString("String_AlignLeft"),
                ControlsProperties.getString("String_AlignCenter"), ControlsProperties.getString("String_AlignRight"),
                ControlsProperties.getString("String_AlignDistributed")}));
        if (textStyle.getStringAlignment().equals(StringAlignment.LEFT)) {
            this.comboBoxStringAlignment.setSelectedIndex(0);
        } else if (textStyle.getStringAlignment().equals(StringAlignment.CENTER)) {
            this.comboBoxStringAlignment.setSelectedIndex(1);
        } else if (this.textStyle.getStringAlignment().equals(StringAlignment.RIGHT)) {
            this.comboBoxStringAlignment.setSelectedIndex(2);
        } else {
            this.comboBoxStringAlignment.setSelectedIndex(3);
        }
    }

    /**
     * 初始化文本对齐方式下拉框
     */
    private void initComboBoxAlign() {
        // 对齐方式名称
        String[] TEXTALIGNMENT_NAMES = {ControlsProperties.getString("String_TextAlignment_LeftTop"),
                ControlsProperties.getString("String_TextAlignment_LeftMid"), ControlsProperties.getString("String_TextAlignment_LeftBottom"),
                ControlsProperties.getString("String_TextAlignment_MidTop"), ControlsProperties.getString("String_TextAlignment_Mid"),
                ControlsProperties.getString("String_TextAlignment_MidBottom"), ControlsProperties.getString("String_TextAlignment_RightTop"),
                ControlsProperties.getString("String_TextAlignment_RightMid"), ControlsProperties.getString("String_TextAlignment_RightBottom"),
                ControlsProperties.getString("String_TextAlignment_LeftBaseline"), ControlsProperties.getString("String_TextAlignment_RightBaseline"),
                ControlsProperties.getString("String_TextAlignment_MidBaseline")};
        this.comboBoxAlign.setModel(new DefaultComboBoxModel<String>(TEXTALIGNMENT_NAMES));
        initHashMapTextAlignment();
        Object[] textAlignmentValues = hashMapTextAlignment.values().toArray();
        Object[] textAlignmentNames = hashMapTextAlignment.keySet().toArray();
        String alignMent = "";
        if (null != this.textStyle) {
            for (int i = 0; i < textAlignmentValues.length; i++) {
                TextAlignment temp = (TextAlignment) textAlignmentValues[i];
                if (temp == this.textStyle.getAlignment()) {
                    alignMent = (String) textAlignmentNames[i];
                }
            }
            this.comboBoxAlign.setSelectedItem(alignMent);
        }
    }

    private void initHashMapTextAlignment() {
        TextAlignment[] textAlignmentValues = {TextAlignment.TOPLEFT, TextAlignment.MIDDLELEFT, TextAlignment.BOTTOMLEFT, TextAlignment.TOPCENTER,
                TextAlignment.MIDDLECENTER, TextAlignment.BOTTOMCENTER, TextAlignment.TOPRIGHT, TextAlignment.MIDDLERIGHT, TextAlignment.BOTTOMRIGHT,
                TextAlignment.BASELINELEFT, TextAlignment.BASELINERIGHT, TextAlignment.BASELINECENTER};
        for (int i = 0; i < textAlignmentValues.length; i++) {
            hashMapTextAlignment.put(comboBoxAlign.getItemAt(i), textAlignmentValues[i]);
        }
    }

    /**
     * 初始化字号下拉框和字高
     */
    private void initComboBoxFontSize() {
        this.comboBoxFontSize.setModel(new DefaultComboBoxModel<String>(new String[]{"1", "2", "3", "4", "5", "5.5", "6.5", "7.5", "8", "9", "10", "11",
                "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"}));
        this.comboBoxFontSize.setEditable(true);
        this.comboBoxFontSize.setSelectedItem(this.textStyle.getFontHeight() * EXPERIENCE);
        this.textFieldFontSize = (JTextField) this.comboBoxFontSize.getEditor().getEditorComponent();
        this.spinnerFontHeight.setModel(new SpinnerNumberModel(0.0, 0.0, null, 1.0));
        NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerFontHeight.getEditor();
        this.textFieldFontHeight = numberEditor.getTextField();
        if (null != this.textStyle) {
            Map map = new Map();
            if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
                map = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
            }
            Double size = FontUtilities.mapHeightToFontSize(textStyle.getFontHeight(), map, textStyle.isSizeFixed());
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String textFieldString = "";
            if (Double.compare(size, size.intValue()) > 0) {
                textFieldString = decimalFormat.format(size);
                this.textFieldFontSize.setText(textFieldString);
            } else {
                decimalFormat = new DecimalFormat("0");
                textFieldString = decimalFormat.format(size);
                this.textFieldFontSize.setText(textFieldString);
            }
            double height = Double.parseDouble(textFieldString);
            this.textFieldFontHeight.setText(new DecimalFormat(numeric).format(height / EXPERIENCE));
        }
    }

    /**
     * 初始化字宽左侧textField值
     */
    private void initTextFieldFontWidth() {
        this.spinnerFontWidth.setModel(new SpinnerNumberModel(0.0, 0.0, null, 1.0));
        NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerFontWidth.getEditor();
        this.textFieldFontWidth = numberEditor.getTextField();
        if (null != this.textStyle) {
            this.textFieldFontWidth.setText(new DecimalFormat(numeric).format(FontUtilities.fontWidthToMapWidth(textStyle.getFontWidth(),
                    MapUtilities.getActiveMap(), textStyle.isSizeFixed()) / 10));
        }
    }

    /**
     * 初始化旋转角度左侧textField值
     */
    private void initTextFieldFontItalicAngl() {
        this.spinnerInclinationAngl.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        this.spinnerInclinationAngl.setEnabled(false);
        NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerInclinationAngl.getEditor();
        this.textFieldFontItalicAngl = numberEditor.getTextField();
        if (null != this.textStyle) {
            this.textFieldFontItalicAngl.setText(new DecimalFormat(numeric).format(textStyle.getItalicAngle()));
        }
    }

    /**
     * 初始化倾斜角度左侧textField值
     */
    private void initTextFieldFontRotation() {
        this.spinnerRotationAngl.setModel(new SpinnerNumberModel(0.0, 0.0, 360.0, 1.0));
        NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerRotationAngl.getEditor();
        this.textFieldFontRotationAngl = numberEditor.getTextField();
        if (null != this.textStyle) {
            this.textFieldFontRotationAngl.setText(new DecimalFormat(numeric).format(textStyle.getRotation()));
        }
    }

    /**
     * 设置面板可用性，组合类需要使用该方法来设置checkbox的初始状态
     */
    @Override
    public void enabled(boolean enabled) {
        this.labelAlign.setEnabled(enabled);
        this.labelBGColor.setEnabled(enabled);
        this.labelFontColor.setEnabled(enabled);
        this.labelFontHeight.setEnabled(enabled);
        this.labelFontHeightUnity.setEnabled(enabled);
        this.labelFontName.setEnabled(enabled);
        this.labelFontSize.setEnabled(enabled);
        this.labelFontWidth.setEnabled(enabled);
        this.labelFontWidthUnity.setEnabled(enabled);
        this.labelInclinationAngl.setEnabled(enabled);
        this.labelRotationAngl.setEnabled(enabled);
        this.comboBoxAlign.setEnabled(enabled);
        this.comboBoxFontName.setEnabled(enabled);
        this.comboBoxFontSize.setEnabled(enabled);
        this.spinnerFontHeight.setEnabled(enabled);
        this.spinnerFontWidth.setEnabled(enabled);
        this.spinnerRotationAngl.setEnabled(enabled);
        this.spinnerInclinationAngl.setEnabled(enabled && this.textStyle.getItalic());
        this.buttonBGColorSelect.setEnabled(false);
        this.buttonFontColorSelect.setEnabled(enabled);
        this.checkBoxBGOpaque.setEnabled(enabled);
        this.checkBoxBorder.setEnabled(enabled);
        this.checkBoxFixedSize.setEnabled(enabled);
        this.checkBoxItalic.setEnabled(enabled);
        this.checkBoxOutline.setEnabled(enabled);
        this.checkBoxShadow.setEnabled(enabled);
        this.checkBoxStrickout.setEnabled(enabled);
        this.checkBoxUnderline.setEnabled(enabled);
        this.labelStringAlignment.setEnabled(enabled);
        this.comboBoxStringAlignment.setEnabled(enabled);
        resetBGColorEnabled();
    }

    @Override
    public HashMap<TextStyleType, Object> getResultMap() {
        return this.textStyleTypeMap;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void removeEvents() {
        this.comboBoxFontName.removeItemListener(this.fontNameItemListener);
        this.comboBoxAlign.removeItemListener(this.alignItemListener);
        this.textFieldFontSize.removeFocusListener(this.textFieldFontSizeFocusListener);
        this.textFieldFontHeight.removeFocusListener(this.textfieldFontHeightFocusListener);
        this.textFieldFontSize.removeCaretListener(this.textFieldFontSizeListener);
        this.textFieldFontHeight.removeCaretListener(this.textfieldFontHeightListener);
        this.textFieldFontRotationAngl.removeCaretListener(this.textfieldFontRotationAnglListener);
        this.textFieldFontItalicAngl.removeCaretListener(this.textFieldFontItalicAnglListener);
        this.textfieldOutLineWidth.removeCaretListener(this.textfieldOutLineWidthListener);
        this.buttonFontColorSelect.removePropertyChangeListener("m_selectionColors", this.fontColorProperty);
        this.buttonBGColorSelect.removePropertyChangeListener("m_selectionColors", this.backColorListener);
        this.checkBoxBGOpaque.removeActionListener(this.checkboxListener);
        this.checkBoxBorder.removeActionListener(this.checkboxListener);
        this.checkBoxFixedSize.removeActionListener(this.checkboxListener);
        this.checkBoxItalic.removeActionListener(this.checkboxListener);
        this.checkBoxOutline.removeActionListener(this.checkboxListener);
        this.checkBoxShadow.removeActionListener(this.checkboxListener);
        this.checkBoxStrickout.removeActionListener(this.checkboxListener);
        this.checkBoxUnderline.removeActionListener(this.checkboxListener);
        this.comboBoxStringAlignment.removeItemListener(this.comboboxListener);
    }

    @Override
    public synchronized void addTextStyleChangeListener(TextStyleChangeListener l) {
        if (textStyleChangedListeners == null) {
            textStyleChangedListeners = new Vector<TextStyleChangeListener>();
        }
        if (!textStyleChangedListeners.contains(l)) {
            textStyleChangedListeners.add(l);
        }
    }

    @Override
    public synchronized void removeTextStyleChangeListener(TextStyleChangeListener l) {
        if (textStyleChangedListeners != null && textStyleChangedListeners.contains(l)) {
            textStyleChangedListeners.remove(l);
        }
    }

    @Override
    public void fireTextStyleChanged(TextStyleType newValue) {
        if (textStyleChangedListeners != null) {
            Vector<TextStyleChangeListener> listeners = textStyleChangedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.elementAt(i).modify(newValue);
            }
        }
    }

    @Override
    public HashMap<TextStyleType, JComponent> getComponentsMap() {
        return componentsMap;
    }

    @Override
    public JPanel getBasicsetPanel() {
        return this.panelBasicset;
    }

    @Override
    public JPanel getEffectPanel() {
        return this.panelEffect;
    }

    @Override
    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle.clone();
    }

    @Override
    public void setProperty(boolean isProperty) {
        this.isProperty = isProperty;
    }

    @Override
    public void setUnityVisible(boolean unityVisible) {
        this.unityVisible = unityVisible;
    }

    @Override
    public void setOutLineWidth(boolean showOutLineWidth) {
        this.showOutLineWidth = showOutLineWidth;
    }

    @Override
    public void setTextStyleSet(boolean isTextStyleSet) {
        this.isTextStyleSet = isTextStyleSet;
    }

    @Override
    public void initTextBasicPanel() {
        init();
        initResources();
        registEvents();
    }

    public JSpinner getSpinnerRotationAngl() {
        return spinnerRotationAngl;
    }
}
