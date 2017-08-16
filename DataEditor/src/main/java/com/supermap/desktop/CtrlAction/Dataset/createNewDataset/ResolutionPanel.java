package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.TextFields.RightValueListener;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;

import javax.swing.*;

/**
 * Created by yuanR on 2017/8/15 .
 * 新建影像数据-分辨率面板
 */
public class ResolutionPanel extends JPanel {
	private JLabel resolutionXLabel;
	private JLabel resolutionYLabel;
	private JLabel rowCountLabel;
	private JLabel columnCountLabel;

	public WaringTextField getTextFieldResolutionX() {
		return textFieldResolutionX;
	}

	public WaringTextField getTextFieldResolutionY() {
		return textFieldResolutionY;
	}

	public JTextField getTextFieldRowCount() {
		return textFieldRowCount;
	}

	public JTextField getTextFieldColumnCount() {
		return textFieldColumnCount;
	}

	private WaringTextField textFieldResolutionX;
	private WaringTextField textFieldResolutionY;

	private JTextField textFieldRowCount;
	private JTextField textFieldColumnCount;

	private RightValueListener rightValueListener = new RightValueListener() {
		@Override
		public void update(String value) {
		}
	};

	public ResolutionPanel() {
		initComponents();
		initLayout();
		initStates();
		registerEvent();
	}

	private void initComponents() {
		this.resolutionXLabel = new JLabel(ControlsProperties.getString("String_LabelXPixelFormat"));
		this.resolutionYLabel = new JLabel(ControlsProperties.getString("String_LabelYPixelFormat"));
		this.rowCountLabel = new JLabel(ControlsProperties.getString("String_LabelRowsSize"));
		this.columnCountLabel = new JLabel(ControlsProperties.getString("String_LabelColumnsSize"));

		this.textFieldResolutionX = new WaringTextField();
		this.textFieldResolutionY = new WaringTextField();
		this.textFieldRowCount = new JTextField();
		this.textFieldColumnCount = new JTextField();
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.resolutionXLabel)
								.addComponent(this.resolutionYLabel)
								.addComponent(this.rowCountLabel)
								.addComponent(this.columnCountLabel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.textFieldResolutionX, 150, 150, Short.MAX_VALUE)
								.addComponent(this.textFieldResolutionY, 150, 150, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(16).addComponent(this.textFieldRowCount, 150, 150, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(16).addComponent(this.textFieldColumnCount, 150, 150, Short.MAX_VALUE)))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.resolutionXLabel)
						.addComponent(this.textFieldResolutionX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.resolutionYLabel)
						.addComponent(this.textFieldResolutionY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.rowCountLabel)
						.addComponent(this.textFieldRowCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.columnCountLabel)
						.addComponent(this.textFieldColumnCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on

	}

	private void initStates() {

		textFieldResolutionX.setText("0.5");
		textFieldResolutionY.setText("0.5");
		textFieldResolutionX.setInitInfo(0, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "22");

		textFieldResolutionY.setInitInfo(0, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "22");


		textFieldColumnCount.setText("800");
		textFieldRowCount.setText("800");
		textFieldColumnCount.setEditable(false);
		textFieldRowCount.setEditable(false);

	}

	private void registerEvent() {
		this.textFieldResolutionX.addRightValueListener(rightValueListener);
		this.textFieldResolutionY.addRightValueListener(rightValueListener);
	}
}
