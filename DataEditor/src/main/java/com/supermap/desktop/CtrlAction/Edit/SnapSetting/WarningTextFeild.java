package com.supermap.desktop.CtrlAction.Edit.SnapSetting;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.SymbolSpinnerUtilties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2016/12/8.
 */
public class WarningTextFeild extends JPanel {
    private JLabel labelWarning;
    private JTextField textField;
    private Double startValue;
    private Double endValue;
    private String defaultValue;
    private int type;
    private ArrayList listeners;
    private DecimalFormat format = new DecimalFormat("0");

    private CaretListener caretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            String text = textField.getText();
            if (null != startValue && null != endValue && !SymbolSpinnerUtilties.isLegitNumber(startValue, endValue, text)) {
                labelWarning.setText("<html><font color='red' style='font-weight:bold '>!</font></html>");
                return;
            } else if (!StringUtilities.isNullOrEmpty(text)) {
                labelWarning.setText(" ");
                fireListener(text);
            }
        }
    };

    public WarningTextFeild(String defaultValue) {
        super();
        this.defaultValue = defaultValue;
        initComponents();
        initLayout();
    }

    public void setInitInfo(double startValue, double endValue, int type, String floatLength) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.type = type;
        if (type == 0) {
            this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_IntegerWarning"), "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
        } else {
            this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatWarning"), floatLength, "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
        }
    }

    private void initComponents() {
        this.labelWarning = new JLabel();
        this.textField = new JTextField();
        this.listeners = new ArrayList();
        this.textField.setText(defaultValue);
        this.labelWarning.setText(" ");
    }

    private void initLayout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelWarning, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textField, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.textField.setPreferredSize(new Dimension(100, 23));
    }

    public void addRightValueListener(RightValueListener listener) {
        if (null != listener && !listeners.contains(listener)) {
            listeners.add(listener);
        }
        registEvents();
    }

    public void removeRightValueListener(RightValueListener listener) {
        if (null != listener && !listeners.contains(listener)) {
            listeners.remove(listener);
        }
        registEvents();
    }

    private void fireListener(String value) {
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            if (null != listeners.get(i) && listeners.get(i) instanceof RightValueListener) {
                ((RightValueListener) listeners.get(i)).update(value);
            }
        }
    }

    public void registEvents() {
        removeEvents();
        this.textField.addCaretListener(this.caretListener);
    }

    public void removeEvents() {
        this.textField.removeCaretListener(this.caretListener);
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setText(String str) {
        textField.setText(str);
    }
}
