package com.supermap.desktop.dialog;

import com.supermap.desktop.Interface.IPasswordCheck;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogChangePassword extends SmDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JPasswordField textFieldOldPassword;
	private JPasswordField textFieldNewPassword;
	private JPasswordField textFieldConfirm;
	private JLabel labelOldPassword;
	private JLabel labelNewPassword;
	private JLabel labelConfirm;
	private SmButton buttonCancel;
	private SmButton buttonOk;
	private String oldPassword = "";
	private String newPassword = "";
	private String confirmPassword = "";

	// 是否大小写不敏感，默认为 true
	private boolean isIgnoreCase = true;

	private transient IPasswordCheck passwordCheck = null;

	/**
	 * Create the dialog.
	 */
	public JDialogChangePassword() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		initializeComponents();
		initializeResources();
		registerEvents();
		setLocationRelativeTo(null);
		initTraversalPolicy();
	}

	public JDialogChangePassword(String oldPassword) {
		this();
		this.oldPassword = oldPassword;
		setButtonOKEnabledInEDT(false);
		initTraversalPolicy();
	}

	public JDialogChangePassword(String oldPassword, boolean isIgnoreCase) {
		this();
		this.oldPassword = oldPassword;
		this.isIgnoreCase = isIgnoreCase;
		setButtonOKEnabledInEDT(false);
		initTraversalPolicy();
	}

	private void initTraversalPolicy(){
		if (this.componentList.size()>0) {
			this.componentList.clear();
		}
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}
	
	public boolean isIgnoreCase() {
		return isIgnoreCase;
	}

	public void setIgnoreCase(boolean isIgnoreCase) {
		this.isIgnoreCase = isIgnoreCase;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
		this.textFieldOldPassword.setText(this.oldPassword);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
		this.textFieldNewPassword.setText(this.newPassword);
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
		this.textFieldConfirm.setText(this.confirmPassword);
	}

	public IPasswordCheck getPasswordCheck() {
		return passwordCheck;
	}

	public void setPasswordCheck(IPasswordCheck passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	private void initializeComponents() {
		setBounds(100, 100, 391, 160);

		this.getRootPane().setDefaultButton(buttonOk);
		textFieldOldPassword = new JPasswordField();
		textFieldNewPassword = new JPasswordField();
		textFieldConfirm = new JPasswordField();
		labelOldPassword = new JLabel("Old Password:");
		labelNewPassword = new JLabel("New Password:");
		labelConfirm = new JLabel("Confirm:");
		buttonCancel = new SmButton("Cancel");
		buttonOk = new SmButton("OK");

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(labelOldPassword)
								.addComponent(labelNewPassword)
								.addComponent(labelConfirm))
							.addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(textFieldConfirm, Alignment.LEADING)
								.addComponent(textFieldNewPassword)
								.addComponent(textFieldOldPassword, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(buttonOk,GroupLayout.PREFERRED_SIZE, 75 ,GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonCancel,GroupLayout.PREFERRED_SIZE, 75 ,GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldOldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelOldPassword))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldNewPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelNewPassword))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldConfirm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelConfirm))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonCancel)
						.addComponent(buttonOk))
					.addContainerGap())
		);
		// @formatter:on
	}

	private void initializeResources() {
		this.setTitle(ControlsProperties.getString("String_Form_ModifyPassWord"));
		this.labelOldPassword.setText(ControlsProperties.getString("String_Label_OldPassword"));
		this.labelNewPassword.setText(ControlsProperties.getString("String_Label_NewPassword"));
		this.labelConfirm.setText(ControlsProperties.getString("String_Label_ConfirmPassword"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
	}

	private void registerEvents() {
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelClicked();
			}
		});

		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOKClicked();
			}
		});

		this.textFieldOldPassword.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				oldPasswordChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				oldPasswordChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// 目前默认实现，后续提供初始化操作
			}
		});

		this.textFieldNewPassword.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				newPasswordChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				newPasswordChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// 目前默认实现，后续提供初始化操作
			}
		});

		this.textFieldConfirm.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				confirmPasswordChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				confirmPasswordChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO 目前默认实现，后续会增加一些初始化操作
			}
		});
	}

	private void setButtonOKEnabeld() {
		// 密码校验器为空，当然不能继续了
		if (this.passwordCheck == null) {
			this.buttonOk.setToolTipText(ControlsProperties.getString("String_PasswordCheckIsNull"));
			setButtonOKEnabledInEDT(false);
			return;
		}

		// 输入的旧密码错误
		if (!this.passwordCheck.checkPassword(this.getOldPassword())) {
			this.buttonOk.setToolTipText(ControlsProperties.getString("String_OldPasswordIsFalse"));
			setButtonOKEnabledInEDT(false);
			return;
		}

		// 新密码与校验密码不同时为空，不能继续
		if (!StringUtilties.stringEquals(this.newPassword, this.confirmPassword, this.isIgnoreCase)) {
			this.buttonOk.setToolTipText(ControlsProperties.getString("String_NewPasswordDiff"));
			setButtonOKEnabledInEDT(false);
			return;
		}

		// 新密码与旧密码相同，不能继续
		if (StringUtilties.stringEquals(this.newPassword, this.oldPassword, this.isIgnoreCase)) {
			this.buttonOk.setToolTipText(ControlsProperties.getString("String_NewPasswordNeedChange"));
			setButtonOKEnabledInEDT(false);
			return;
		}
		// 所有条件满足设为 true
		this.buttonOk.setToolTipText("");
		setButtonOKEnabledInEDT(true);

	}

	private void setButtonOKEnabledInEDT(final boolean isEnabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				buttonOk.setEnabled(isEnabled);
			}
		});
	}

	private void buttonCancelClicked() {
		this.dialogResult = DialogResult.CANCEL;
		setVisibleInEDT(false);
	}

	private void buttonOKClicked() {
		// 旧密码不对，禁止修改
		this.dialogResult = DialogResult.OK;
		setVisibleInEDT(false);

	}

	private void setVisibleInEDT(final boolean isVisible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(isVisible);
			}
		});
	}

	private void oldPasswordChanged() {
		this.oldPassword = String.valueOf(this.textFieldOldPassword.getPassword());
		setButtonOKEnabeld();
	}

	private void newPasswordChanged() {
		this.newPassword = String.valueOf(this.textFieldNewPassword.getPassword());
		setButtonOKEnabeld();
	}

	private void confirmPasswordChanged() {
		this.confirmPassword = String.valueOf(this.textFieldConfirm.getPassword());
		setButtonOKEnabeld();
	}

	@Override
	public void escapePressed() {
		buttonCancelClicked();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.buttonOk) {
			buttonOKClicked();
		}
		if (this.getRootPane().getDefaultButton() == this.buttonCancel) {
			buttonCancelClicked();
		}
	}
}
