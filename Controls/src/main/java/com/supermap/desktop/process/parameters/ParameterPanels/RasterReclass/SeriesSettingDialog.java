package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
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
 * Created by lixiaoyao on 2017/9/5.
 */
public class SeriesSettingDialog extends SmDialog {

	private JRadioButton stepLength = new JRadioButton();
	private JRadioButton seriesNum = new JRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();
	private WaringTextField stepLengthText;
	private WaringTextField seriesNumText;
	private SmButton okSmButton = new SmButton();
	private SmButton cancelSmButton = new SmButton();
	private final int ROW_HRIGHT = 23;
	private double inputStartValue;
	private double inputEndVale;
	private double inputStepLength;
	private int inputSeriesNum;
	private double resultKeys[];

	public SeriesSettingDialog(double startValue, double endVale, int seriesNum, JFrame owner, boolean model) {
		super(owner, model);
		this.inputStartValue = startValue;
		this.inputEndVale = endVale;
		this.inputSeriesNum = seriesNum;
		initComponents();
		initResources();
		removeEvents();
		registerEvents();
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SeriesSettingDialog.this.okSmButton) {
				calculResultKeys();
				SeriesSettingDialog.this.setDialogResult(DialogResult.OK);
				SeriesSettingDialog.this.dispose();
			} else if (e.getSource() == SeriesSettingDialog.this.cancelSmButton) {
				SeriesSettingDialog.this.dispose();
			} else if (e.getSource() == SeriesSettingDialog.this.stepLength) {
				resetTextEnableForRadio();
			} else if (e.getSource() == SeriesSettingDialog.this.seriesNum) {
				resetTextEnableForRadio();
			}
		}
	};

	private RightValueListener rightStepValue = new RightValueListener() {
		@Override
		public void update(String value) {
			if (!value.isEmpty() && Double.compare(inputStepLength, Double.valueOf(value)) != 0) {
				double currentStepValue = Double.valueOf(value);
				double currentSeriesNum = (inputEndVale - inputStartValue) / currentStepValue;
				Integer seriesNum = (int) Math.ceil(Math.abs(currentSeriesNum));
				if (Double.compare(inputStartValue + seriesNum * currentStepValue, inputEndVale) == -1 || Double.compare(inputStartValue + seriesNum * currentStepValue, inputEndVale) == 0) {
					seriesNum = seriesNum + 1;
				}
				inputSeriesNum = seriesNum;
				seriesNumText.setText(String.valueOf(seriesNum));
			}
		}
	};

	private RightValueListener rightSeriesNum = new RightValueListener() {
		@Override
		public void update(String value) {
			if (!value.isEmpty() && Integer.compare(inputSeriesNum, Integer.valueOf(value)) != 0) {
				Integer currentSeriesNum = Integer.valueOf(value);
				double currentStepValue = (inputEndVale - inputStartValue) / currentSeriesNum;
				inputStepLength = currentStepValue;
				stepLengthText.setText(String.valueOf(currentStepValue));
			}
		}
	};


	private void initComponents() {
		Dimension dimension = new Dimension(280, 150);
		setSize(dimension);
		setMinimumSize(dimension);
		setLocationRelativeTo(null);
		getRootPane().setDefaultButton(okSmButton);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		this.inputStepLength = (this.inputEndVale - this.inputStartValue) / this.inputSeriesNum;
		this.stepLengthText = new WaringTextField(String.valueOf(this.inputStepLength), true);
		this.stepLengthText.setInitInfo(Short.MIN_VALUE, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "null");
		this.seriesNumText = new WaringTextField(String.valueOf(this.inputSeriesNum), true);
		this.seriesNumText.setInitInfo(1, Short.MAX_VALUE, WaringTextField.INTEGER_TYPE, "null");

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(stepLength)
										.addComponent(seriesNum))
								.addContainerGap(50, 50)
								.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(stepLengthText)
										.addComponent(seriesNumText)))
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(5, 5, Integer.MAX_VALUE)
								.addComponent(okSmButton)
								.addComponent(cancelSmButton)))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(stepLength)
						.addComponent(stepLengthText, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(seriesNum)
						.addComponent(seriesNumText, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
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
		setComponentName();
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.stepLength, "BatchAddColorTableDailog_stepLength");
		ComponentUIUtilities.setName(this.seriesNum, "BatchAddColorTableDailog_seriesNum");
		ComponentUIUtilities.setName(this.stepLengthText, "BatchAddColorTableDailog_stepLengthText");
		ComponentUIUtilities.setName(this.seriesNumText, "BatchAddColorTableDailog_seriesNumText");
		ComponentUIUtilities.setName(this.okSmButton, "BatchAddColorTableDailog_okSmButton");
		ComponentUIUtilities.setName(this.cancelSmButton, "BatchAddColorTableDailog_cancelSmButton");
	}

	private void initResources() {
		setTitle(ControlsProperties.getString("String_SeriesSetting"));
		this.stepLength.setText(ControlsProperties.getString("String_StepLength"));
		this.seriesNum.setText(ControlsProperties.getString("String_BatchAddColorTableSeriesNum"));
		this.okSmButton.setText(ControlsProperties.getString("String_BatchAddColorTableOKButton"));
		this.cancelSmButton.setText(ControlsProperties.getString("String_BatchAddColorTableCancelButton"));
	}

	private void registerEvents() {
		this.okSmButton.addActionListener(actionListener);
		this.cancelSmButton.addActionListener(actionListener);
		this.stepLength.addActionListener(actionListener);
		this.seriesNum.addActionListener(actionListener);
		this.stepLengthText.addRightValueListener(rightStepValue);
		this.seriesNumText.addRightValueListener(rightSeriesNum);
	}

	private void removeEvents() {
		this.okSmButton.removeActionListener(actionListener);
		this.cancelSmButton.removeActionListener(actionListener);
		this.stepLength.removeActionListener(actionListener);
		this.seriesNum.removeActionListener(actionListener);
		this.stepLengthText.removeRightValueListener(rightStepValue);
		this.seriesNumText.removeRightValueListener(rightSeriesNum);
	}

	private void calculResultKeys() {
		if (!this.stepLengthText.isError() && !this.seriesNumText.isError()) {
			Integer currentSeriesNum = Integer.valueOf(this.seriesNumText.getText());
			double start = inputStartValue;
			double end = inputEndVale;
			double step = Double.valueOf(this.stepLengthText.getText());

			this.resultKeys = new double[currentSeriesNum+1];
			for (int i = 0; i <= currentSeriesNum; i++) {
				if (i == currentSeriesNum) {
					resultKeys[i] = end;
				} else {
					resultKeys[i] = start + i * step;
				}
			}
		}
	}

	public double[] getResultKeys() {
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
