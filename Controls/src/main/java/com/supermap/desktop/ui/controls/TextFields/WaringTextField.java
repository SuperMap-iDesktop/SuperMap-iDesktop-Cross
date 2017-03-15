package com.supermap.desktop.ui.controls.TextFields;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.dialog.symbolDialogs.SymbolSpinnerUtilties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2016/12/13.
 */
public class WaringTextField extends JPanel {
    private JLabel labelWarning;
    private JTextField textField;
    private Double startValue;
    private Double endValue;
    private String defaultValue;
    public static final int INTEGER_TYPE = 0;
    public static final int FLOAT_TYPE = 1;
    private int type;
    private ArrayList listeners;
	private DecimalFormat format = new DecimalFormat("0.#######");

    private CaretListener caretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            String text = textField.getText();
            if (null != startValue && null != endValue && !SymbolSpinnerUtilties.isLegitNumber(startValue, endValue, text)) {
                labelWarning.setText("");
                labelWarning.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
	            setInitInfo(startValue, endValue, type, floatLength);
            } else if (!StringUtilities.isNullOrEmpty(text) && StringUtilities.isNumber(text)) {
                labelWarning.setText(" ");
                labelWarning.setIcon(null);
                fireListener(text);
            }
        }
    };
    private KeyListener keyAdapter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            int keyChar = e.getKeyChar();
            if (type == INTEGER_TYPE && keyChar == 46) {
                e.consume();
            }
            if ((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == 45
                    || keyChar == 43 || keyChar == 46) {
                return;
            } else {
                e.consume();
            }

        }
    };
	private String floatLength;

	public WaringTextField() {
		this("");
    }

    public WaringTextField(String defaultValue) {
        super();
        this.defaultValue = defaultValue;
        initComponents();
        initLayout();
    }

    public void setInitInfo(double startValue, double endValue, int type, String floatLength) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.type = type;
	    this.floatLength = floatLength;
	    String text = textField.getText();
	    double currentValue = 0;
	    if (!StringUtilities.isNullOrEmpty(text)) {
		    try {
			    currentValue = DoubleUtilities.stringToValue(text);
		    } catch (Exception e) {
			    // ignore
		    }
	    }
	    if (type == INTEGER_TYPE) {
		    if (currentValue > endValue) {
			    this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_IntegerMaxWarning"), "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
		    } else {
			    this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_IntegerMinWarning"), "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
		    }
        } else {
		    if (currentValue > endValue) {
			    this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatMaxWarning"), floatLength, "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
		    } else {
			    this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatMinWarning"), floatLength, "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
		    }
        }
        this.labelWarning.setPreferredSize(new Dimension(23, 23));
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
        this.textField.addKeyListener(this.keyAdapter);
    }

    public void removeEvents() {
        this.textField.removeCaretListener(this.caretListener);
        this.textField.removeKeyListener(this.keyAdapter);
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setText(String str) {
        textField.setText(str);
    }

    public void setEnable(boolean enable) {
        this.labelWarning.setEnabled(enable);
        this.textField.setEnabled(enable);
    }

    public boolean isError(){
        if (this.labelWarning.getIcon()!=null){
            return true;
        }else{
            return false;
        }
    }

}
