package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JDialogGetPassword extends SmDialog{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	/**
	 * 密码输入提示信息
	 */
	private JLabel jlabelPromptMessage;
	/**
	 * 密码错误提示信息
	 */
	private JLabel jlabelPasswordMessage;
	private JPasswordField jpasswordField;


	private SmButton buttonOk;
	private SmButton buttonCancel;
	
	private String promptMessage;
	/**
	 * 创建密码输入框
	 *
	 * @param promptMessage 提示信息
	 */
	public JDialogGetPassword(String promptMessage) {
		this.promptMessage = promptMessage;
		initComponent();
		addListeners();
		this.setTitle(ControlsProperties.getString("String_Prompt"));
		this.setSize(500, 160);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		
		this.contentPanel = new JPanel();
		
		this.jlabelPromptMessage = new JLabel(this.promptMessage);
		this.jpasswordField = new JPasswordField();
		this.jlabelPasswordMessage = new JLabel(ControlsProperties.getString("String_LOGIN_PASSWORD_Message"));
		this.jlabelPasswordMessage.setForeground(Color.RED);
		this.jlabelPasswordMessage.setVisible(false);

		this.buttonOk = new SmButton(ControlsProperties.getString("String_Button_Ok"));
		this.buttonCancel = new SmButton(ControlsProperties.getString("String_Button_Cancel"));
		


		// @formatter:off
		this.contentPanel.setLayout(new GridBagLayout());
		this.contentPanel.add(jlabelPromptMessage, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST)  .setInsets(10, 10, 2, 10).setWeight(1, 1));
		this.contentPanel.add(jpasswordField,      new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2, 10, 2, 10).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(jlabelPasswordMessage, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(2, 10, 2, 10).setWeight(1, 1));
		this.contentPanel.add(buttonOk,     new GridBagConstraintsHelper(0, 3).setAnchor(GridBagConstraints.EAST)  .setWeight(1, 1).setInsets(2, 10, 10, 2));
		this.contentPanel.add(buttonCancel, new GridBagConstraintsHelper(1, 3).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setInsets(2, 2, 10, 10));
		this.add(contentPanel);
		// @formatter:on

		
	}

	private void addListeners(){
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canelButton_Click();
			}
		});
		
		this.buttonOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_Click();
			}
		});
		
		this.jpasswordField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				JDialogGetPassword.this.jlabelPasswordMessage.setVisible(false);
				if(KeyEvent.VK_ENTER == e.getKeyCode()){
					okButton_Click();
				} else if(KeyEvent.VK_ESCAPE == e.getKeyCode()){
					canelButton_Click();
				}
			}
		});
	}

	private void canelButton_Click() {
		try {
			this.setDialogResult(DialogResult.CANCEL);
			this.dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void okButton_Click(){
		if(this.isRightPassword(getPassword())){
			// 密码正确
			this.setDialogResult(DialogResult.OK);
			this.dispose();
		} else {
			// 密码错误
			this.jpasswordField.setText("");
			this.jpasswordField.requestFocus();
			this.jlabelPasswordMessage.setVisible(true);
		}
			
	}

	/**
	 * 判断密码是否正确，需要根据对应情况自行重新
	 * @param password 当前输入的密码
	 * @return
	 */
	public boolean isRightPassword(String password){
		return true;
	}
	
	public String getPassword(){
		// TODO 密码加密解密
		return String.valueOf(this.jpasswordField.getPassword());
	}

}
