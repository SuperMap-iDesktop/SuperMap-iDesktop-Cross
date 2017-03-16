package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.RightValueListener;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lixiaoyao on 2017/3/14.
 */
public class BatchAddColorTableDailog extends SmDialog {
    private JLabel startValue = new JLabel();
    private JLabel endValue = new JLabel();
    private JRadioButton stepLength = new JRadioButton();
    private JRadioButton seriesNum = new JRadioButton();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JCheckBox resetEndValue = new JCheckBox();
    private WaringTextField startValueText;
    private WaringTextField endValueText;
    private WaringTextField stepLengthText;
    private WaringTextField seriesNumText;
    private SmButton okSmButton = new SmButton();
    private SmButton cancelSmButton = new SmButton();
    private final int ROW_HRIGHT = 23;
    private double keys[];
    private boolean isNeedResetCalculEndValue = false;
    private boolean isNeedEachOtherWarn = false;
    private double resultKeys[];

    public BatchAddColorTableDailog(double keys[], JFrame owner, boolean model) {
        super(owner, model);
        this.keys = keys;
        initComponents();
        initResources();
        removeEvents();
        registerEvents();
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == BatchAddColorTableDailog.this.okSmButton) {
                calculResultKeys();
                BatchAddColorTableDailog.this.setDialogResult(DialogResult.OK);
                BatchAddColorTableDailog.this.dispose();
            } else if (e.getSource() == BatchAddColorTableDailog.this.cancelSmButton) {
                BatchAddColorTableDailog.this.dispose();
            } else if (e.getSource() == BatchAddColorTableDailog.this.resetEndValue) {
                isNeedResetCalculEndValue = BatchAddColorTableDailog.this.resetEndValue.isSelected();
            } else if (e.getSource() == BatchAddColorTableDailog.this.stepLength) {
                resetTextEnableForRadio();
            } else if (e.getSource() == BatchAddColorTableDailog.this.seriesNum) {
                resetTextEnableForRadio();
            }
        }
    };

    private RightValueListener rightStartValue = new RightValueListener() {
        @Override
        public void update(String value) {
            startValurOrEndValueChange();
        }
    };

    private RightValueListener rightEndValue = new RightValueListener() {
        @Override
        public void update(String value) {
            startValurOrEndValueChange();
        }
    };

    private RightValueListener rightStepValue = new RightValueListener() {
        @Override
        public void update(String value) {
            if (!isNeedEachOtherWarn) {
                isNeedEachOtherWarn = true;
                double currentStepValue = Double.valueOf(value);
                double currentSeriesNum = (Double.valueOf(endValueText.getTextField().getText()) - Double.valueOf(startValueText.getTextField().getText())) / currentStepValue;
                Integer cSeriesNum = (int) currentSeriesNum;
                seriesNumText.getTextField().setText(String.valueOf(cSeriesNum));
            } else {
                isNeedEachOtherWarn = false;
            }
        }
    };

    private RightValueListener rightSeriesNum = new RightValueListener() {
        @Override
        public void update(String value) {
            if (!isNeedEachOtherWarn) {
                isNeedEachOtherWarn = true;
                Integer currentSeriesNum = Integer.valueOf(value);
                double currentStepValue = (Double.valueOf(endValueText.getTextField().getText()) - Double.valueOf(startValueText.getTextField().getText())) / currentSeriesNum;
                stepLengthText.getTextField().setText(String.valueOf(currentStepValue));
            } else {
                isNeedEachOtherWarn = false;
            }
        }
    };


    private void initComponents() {
        Dimension dimension = new Dimension(336, 226);
        setSize(dimension);
        setMinimumSize(dimension);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(okSmButton);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        this.startValueText = new WaringTextField(String.valueOf(this.keys[0]));
        this.startValueText.setInitInfo(Short.MIN_VALUE, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "8");
        this.endValueText = new WaringTextField(String.valueOf(this.keys[this.keys.length - 1]));
        this.endValueText.setInitInfo(Short.MIN_VALUE, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "8");
        this.stepLengthText = new WaringTextField(String.valueOf(this.keys[1] - this.keys[0]));
        this.stepLengthText.setInitInfo(Short.MIN_VALUE, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "8");
        this.seriesNumText = new WaringTextField(String.valueOf(this.keys.length));
        this.seriesNumText.setInitInfo(2, Short.MAX_VALUE, WaringTextField.INTEGER_TYPE, "null");


        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(startValue)
                                        .addComponent(endValue)
                                        .addComponent(stepLength)
                                        .addComponent(seriesNum))
                                .addContainerGap(50, 50)
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(startValueText)
                                        .addComponent(endValueText)
                                        .addComponent(stepLengthText)
                                        .addComponent(seriesNumText)))
                        .addComponent(resetEndValue)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5, 5, Integer.MAX_VALUE)
                                .addComponent(okSmButton)
                                .addComponent(cancelSmButton)))
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(startValue)
                        .addComponent(startValueText, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(endValue)
                        .addComponent(endValueText, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(stepLength)
                        .addComponent(stepLengthText, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(seriesNum)
                        .addComponent(seriesNumText, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
                .addComponent(resetEndValue)
                .addContainerGap(0, Short.MAX_VALUE)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(okSmButton)
                        .addComponent(cancelSmButton))
        );
        buttonGroup.add(stepLength);
        buttonGroup.add(seriesNum);
        getContentPane().setLayout(groupLayout);
        this.seriesNum.setSelected(true);
        resetTextEnableForRadio();
    }

    private void initResources() {
        setTitle(MapViewProperties.getString("String_BatchAddColorTable"));
        this.startValue.setText(MapViewProperties.getString("String_BatchAddColorTableMinValue"));
        this.endValue.setText(MapViewProperties.getString("String_BatchAddColorTableMaxValue"));
        this.stepLength.setText(MapViewProperties.getString("String_BatchAddColorTableStepLength"));
        this.seriesNum.setText(MapViewProperties.getString("String_BatchAddColorTableSeriesNum"));
        this.resetEndValue.setText(MapViewProperties.getString("String_BatchAddColorTableResetCalculValue"));
        this.okSmButton.setText(MapViewProperties.getString("String_BatchAddColorTableOKButton"));
        this.cancelSmButton.setText(MapViewProperties.getString("String_BatchAddColorTableCancelButton"));
    }

    private void registerEvents() {
        this.okSmButton.addActionListener(actionListener);
        this.cancelSmButton.addActionListener(actionListener);
        this.resetEndValue.addActionListener(actionListener);
        this.stepLength.addActionListener(actionListener);
        this.seriesNum.addActionListener(actionListener);
        this.startValueText.addRightValueListener(rightStartValue);
        this.endValueText.addRightValueListener(rightEndValue);
        this.stepLengthText.addRightValueListener(rightStepValue);
        this.seriesNumText.addRightValueListener(rightSeriesNum);
    }

    private void removeEvents() {
        this.okSmButton.removeActionListener(actionListener);
        this.cancelSmButton.removeActionListener(actionListener);
        this.resetEndValue.removeActionListener(actionListener);
        this.stepLength.removeActionListener(actionListener);
        this.seriesNum.removeActionListener(actionListener);
        this.startValueText.removeRightValueListener(rightStartValue);
        this.endValueText.removeRightValueListener(rightEndValue);
        this.stepLengthText.removeRightValueListener(rightStepValue);
        this.seriesNumText.removeRightValueListener(rightSeriesNum);
    }

    //  起始值或者结尾值更改则更改相对应的步长与级数
    private void startValurOrEndValueChange() {
        if (this.seriesNum.isSelected()) {
            Integer currentSeriesNum = Integer.valueOf(this.seriesNumText.getTextField().getText());
            double currentStepValue = (Double.valueOf(this.endValueText.getTextField().getText()) - Double.valueOf(this.startValueText.getTextField().getText())) / currentSeriesNum;
            this.stepLengthText.getTextField().setText(String.valueOf(currentStepValue));
        } else {
            double currentStepValue = Double.valueOf(this.stepLengthText.getTextField().getText());
            double currentSeriesNum = (Double.valueOf(this.endValueText.getTextField().getText()) - Double.valueOf(this.startValueText.getTextField().getText())) / currentStepValue;
            Integer cSeriesNum = (int) currentSeriesNum;
            this.seriesNumText.getTextField().setText(String.valueOf(cSeriesNum));
        }
    }

    private void calculResultKeys(){
        if (!this.startValueText.isError() && !this.endValueText.isError() && !this.stepLengthText.isError() && !this.seriesNumText.isError()) {
            Integer currentSeriesNum = Integer.valueOf(this.seriesNumText.getTextField().getText());
            double start = Double.valueOf(this.startValueText.getTextField().getText());
            double end = Double.valueOf(this.endValueText.getTextField().getText());
            double step = Double.valueOf(this.stepLengthText.getTextField().getText());
            this.resultKeys = new double[currentSeriesNum];
            for (int i = 0; i < currentSeriesNum; i++) {
                if (i + 1 == currentSeriesNum && !isNeedResetCalculEndValue) {
                    resultKeys[i] = end;
                } else {
                    resultKeys[i] = start + i * step;
                }
            }
        }
    }

    public double[] getResultKeys(){
        return this.resultKeys;
    }

    private void resetTextEnableForRadio() {
        if (this.seriesNum.isSelected()) {
            this.seriesNumText.setEnable(true);
            this.stepLengthText.setEnable(false);
        } else {
            this.seriesNumText.setEnable(false);
            this.stepLengthText.setEnable(true);
        }
    }

}
