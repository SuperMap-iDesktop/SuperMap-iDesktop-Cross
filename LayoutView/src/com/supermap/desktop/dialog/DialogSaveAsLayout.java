package com.supermap.desktop.dialog;

import com.supermap.data.Layouts;
import com.supermap.desktop.Application;
import com.supermap.desktop.layoutview.LayoutViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.LayoutUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DialogSaveAsLayout extends SmDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabelLayoutName;
	private JTextField textFieldLayoutName;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private boolean isNewWindow = false;
	private transient Layouts layouts;

	private String oldLayoutName;

	/**
	 * Create the dialog.
	 */
	public DialogSaveAsLayout() {
		setTitle("Layout Save As...");
		setBounds(0, 0, 359, 127);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lblNewLabelLayoutName = new JLabel("Layout Name:");
		textFieldLayoutName = new JTextField();
		textFieldLayoutName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				textFieldLayoutName_ActionPerformed();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				textFieldLayoutName_ActionPerformed();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				textFieldLayoutName_ActionPerformed();
			}
		});
		textFieldLayoutName.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(textFieldLayoutName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(lblNewLabelLayoutName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addComponent(lblNewLabelLayoutName).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textFieldLayoutName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(176, Short.MAX_VALUE)));
		contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonOk = new SmButton("OK");
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_Click();
			}
		});
		buttonOk.setActionCommand("OK");
		buttonPane.add(buttonOk);
		getRootPane().setDefaultButton(buttonOk);
		buttonCancel = new SmButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_Click();
			}
		});
		buttonCancel.setActionCommand("Cancel");
		buttonPane.add(buttonCancel);

		initializeResources();
		this.setLocationRelativeTo(null);
		getRootPane().setDefaultButton(buttonOk);
	}

	public String getLayoutName() {
		return this.textFieldLayoutName.getText();
	}

	public void setLayoutName(String name) {
		this.oldLayoutName = name;
		this.textFieldLayoutName.setText(name);
		this.textFieldLayoutName.selectAll();
	}

	public Layouts getLayouts() {
		return this.layouts;
	}

	public void setLayouts(Layouts layouts) {
		this.layouts = layouts;
	}

	public boolean isNewWindow() {
		return this.isNewWindow;
	}

	public void setIsNewWindow(boolean isNewWindow) {
		this.isNewWindow = isNewWindow;

		if (this.isNewWindow) {
			this.setTitle(LayoutViewProperties.getString("String_Form_SaveLayout"));
		} else {
			this.setTitle(LayoutViewProperties.getString("String_Form_SaveAsLayout"));
		}
	}

	private void initializeResources() {
		try {
			this.setTitle(LayoutViewProperties.getString("String_Form_SaveAsLayout"));
			this.lblNewLabelLayoutName.setText(LayoutViewProperties.getString("String_FormSaveAsLayout_LabelLayoutName"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void textFieldLayoutName_ActionPerformed() {
		try {
			String name = this.textFieldLayoutName.getText();
			if (name == null || name.length() <= 0) {
				this.buttonOk.setEnabled(false);
			} else if (!UICommonToolkit.isLawName(name, false)) {
				this.buttonOk.setEnabled(false);
			} else {
				this.buttonOk.setEnabled(true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void okButton_Click() {
		try {
			DialogResult dialogResult = DialogResult.NO;

			if (LayoutUtilties.checkAvailableLayoutName(this.textFieldLayoutName.getText(), oldLayoutName)) {
				dialogResult = DialogResult.YES;
			} else {
				String message = String.format(LayoutViewProperties.getString("String_SaveAsLayout_ExistName"), this.textFieldLayoutName.getText());
				UICommonToolkit.showErrorMessageDialog(message);
			}

			if (dialogResult == DialogResult.YES) {
				this.dispose();
				this.dialogResult = dialogResult;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void cancelButton_Click() {
		try {
			this.dispose();
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void escapePressed() {
		cancelButton_Click();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.buttonOk) {
			okButton_Click();
		}
		if (this.getRootPane().getDefaultButton() == this.buttonCancel) {
			cancelButton_Click();
		}
	}
}
