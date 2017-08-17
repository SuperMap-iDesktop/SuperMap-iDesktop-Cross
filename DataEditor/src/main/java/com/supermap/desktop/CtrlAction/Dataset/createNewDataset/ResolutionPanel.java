package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.desktop.controls.ControlsProperties;
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

//	private FocusAdapter focusAdapter = new FocusAdapter() {
//		@Override
//		public void focusLost(FocusEvent e) {
//			WaringTextField textField = (WaringTextField) e.getSource();
//			String value = textField.getTextField().getText();
//			if (StringUtilities.isNullOrEmpty(value)) {
//				textField.getTextField().setText("0.5");
//			}
//			if (Double.valueOf(value) <= 0) {
//				textField.getTextField().setText("0.5");
//			}
//		}
//	};

	public ResolutionPanel() {
		initComponents();
		initLayout();
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
		textFieldResolutionX.setInitInfo(0, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "22");
		textFieldResolutionY.setInitInfo(0, Short.MAX_VALUE, WaringTextField.FLOAT_TYPE, "22");
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


	public void initStates(Double x, Double y, int column, int row) {
		textFieldResolutionX.setText(String.valueOf(x));
		textFieldResolutionY.setText(String.valueOf(y));

		textFieldColumnCount.setText(String.valueOf(column));
		textFieldRowCount.setText(String.valueOf(row));

		textFieldColumnCount.setEditable(false);
		textFieldRowCount.setEditable(false);
	}

	private void registerEvent() {
		this.textFieldResolutionX.registEvents();
		this.textFieldResolutionY.registEvents();
	}
}
