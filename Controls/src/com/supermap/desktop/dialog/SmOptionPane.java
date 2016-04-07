package com.supermap.desktop.dialog;

import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 弹出提示框
 *
 * @author xiajt
 */
public class SmOptionPane extends SmDialog {

	private JPanel panelButton;
	private SmButton buttonYes;
	private SmButton buttonNo;
	private SmButton buttonCancel;

	private JTextArea textAreaMessage;
//	private JLabel labelIcon;


	/**
	 * 与JOptionPane结果保持一致，此处用int类型存储结果
	 */
	private int result = JOptionPane.CLOSED_OPTION;
	private String defaultTitle = CoreProperties.getString("String_MessageBox_Title");

	private static final Dimension size = new Dimension((int) (350 * SystemPropertyUtilties.getSystemSizeRate()), (int) (160 * SystemPropertyUtilties.getSystemSizeRate()));

	public SmOptionPane() {
		init();
	}

	public SmOptionPane(JDialog owner) {
		super(owner);
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		addListeners();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		this.panelButton = new JPanel();
		this.buttonYes = new SmButton();
		this.buttonNo = new SmButton();
		this.buttonCancel = new SmButton();
		this.textAreaMessage = new JTextArea();
		this.textAreaMessage.setFont(textAreaMessage.getFont().deriveFont(Font.PLAIN, 15));
		this.textAreaMessage.setLineWrap(true);
		this.textAreaMessage.setEditable(false);
		this.textAreaMessage.setOpaque(false);
		this.setSize(size);
		this.setMinimumSize(size);
		this.setLocationRelativeTo(null);
		this.setTitle(defaultTitle);
		this.getRootPane().setDefaultButton(buttonYes);
	}

	private void initLayout() {
		this.panelButton.setLayout(new GridBagLayout());

		this.panelButton.add(buttonYes, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 10, 10));
		this.panelButton.add(buttonNo, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 10, 10));
		this.panelButton.add(buttonCancel, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 10, 10));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
//		panel.add(labelIcon, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, (int) (10 * SystemPropertyUtilties.getSystemSizeRate()), 0, 0).setIpad((int) (10 * SystemPropertyUtilties.getSystemSizeRate()), 0));
		panel.add(textAreaMessage, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10));
		panel.add(panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 0).setAnchor(GridBagConstraints.SOUTH));

		this.setLayout(new GridBagLayout());
		this.add(panel, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}

	private void addListeners() {

		buttonYes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = JOptionPane.OK_OPTION;
				setVisible(false);
			}
		});

		buttonNo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = JOptionPane.NO_OPTION;
				setVisible(false);
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = JOptionPane.CANCEL_OPTION;
				setVisible(false);
			}
		});
	}

	private void initResources() {
		buttonYes.setText(CommonProperties.getString(CommonProperties.OK));
		buttonNo.setText(CommonProperties.getString(CommonProperties.Cancel));
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initComponentState() {
		this.buttonCancel.setVisible(false);
	}

	public int showMessageDialog(String message) {
		this.buttonNo.setVisible(false);
//		this.labelIcon.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
		return showDialog(message);
	}


	/**
	 * 询问
	 *
	 * @param message 信息
	 * @return 结果
	 */
	public int showConfirmDialog(String message) {
//		this.labelIcon.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
		return showDialog(message);
	}

	public int showConfirmDialogWithCancle(String message) {
//		this.labelIcon.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
		this.buttonYes.setText(CommonProperties.getString(CommonProperties.yes));
		this.buttonNo.setText(CommonProperties.getString(CommonProperties.no));
		this.buttonCancel.setVisible(true);
		return showDialog(message);
	}

	/**
	 * 错误提示
	 *
	 * @param message 信息
	 * @return 结果
	 */
	public int showErrorDialog(String message) {
		this.buttonNo.setVisible(false);
//		this.labelIcon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		return showDialog(message);
	}

	public int showWarningDialog(String message) {
//		this.labelIcon.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
		return showDialog(message);
	}

	private int showDialog(String message) {
		this.result = JOptionPane.CLOSED_OPTION;
		this.textAreaMessage.setText(message);
		this.buttonYes.requestFocus();
		this.setVisible(true);

		// 还原修改
		this.buttonYes.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonNo.setText(CommonProperties.getString(CommonProperties.Cancel));
		this.buttonNo.setVisible(true);
		this.buttonCancel.setVisible(false);
		return result;
	}

	@Override
	public void escapePressed() {
		result = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.buttonYes) {
			result = JOptionPane.OK_OPTION;
			dispose();
		}
		if (this.getRootPane().getDefaultButton() == this.buttonNo) {
			result = JOptionPane.NO_OPTION;
			setVisible(false);
		}
		if (this.getRootPane().getDefaultButton() == this.buttonCancel) {
			result = JOptionPane.CANCEL_OPTION;
			dispose();
		}
	}

}
