package com.supermap.desktop.dialog;

import com.supermap.data.Scenes;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.SceneUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.realspaceview.RealspaceViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogSaveAsScene extends SmDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabelSceneName;
	private JTextField textFieldSceneName;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private boolean isNewWindow = false;
	private String formTitle = "";
	private transient Scenes scenes;

	private String oldSceneName;

	/**
	 * Create the dialog.
	 */
	public DialogSaveAsScene() {
		setTitle("Scene Save As...");
		setBounds(0, 0, 359, 127);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lblNewLabelSceneName = new JLabel("Scene Name:");
		textFieldSceneName = new JTextField();
		textFieldSceneName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldSceneName_ActionPerformed();
			}
		});
		textFieldSceneName.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(textFieldSceneName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(lblNewLabelSceneName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addComponent(lblNewLabelSceneName).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textFieldSceneName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancel);
	}

	public String getSceneName() {
		return this.textFieldSceneName.getText();
	}

	public void setSceneName(String name) {
		this.oldSceneName = name;
		this.textFieldSceneName.setText(name);
		this.textFieldSceneName.selectAll();
	}

	public Scenes getScenes() {
		return this.scenes;
	}

	public void setScenes(Scenes scenes) {
		this.scenes = scenes;
	}

	public boolean isNewWindow() {
		return this.isNewWindow;
	}

	public void setIsNewWindow(boolean isNewWindow) {
		this.isNewWindow = isNewWindow;

		if (this.isNewWindow) {
			this.setTitle(RealspaceViewProperties.getString("String_Form_SaveScene"));
		} else {
			this.setTitle(RealspaceViewProperties.getString("String_Form_SaveAsScene"));
		}
	}

	public String getFormTitle() {
		return this.formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	private void initializeResources() {
		try {
			this.setTitle(RealspaceViewProperties.getString("String_Form_SaveAsScene"));
			this.lblNewLabelSceneName.setText(RealspaceViewProperties.getString("String_Label_InputSceneName"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void textFieldSceneName_ActionPerformed() {
		try {
			String name = this.textFieldSceneName.getText();
			if (name == "") {
				this.buttonOk.setEnabled(false);
			} else if (!UICommonToolkit.isLawName(name, false)) {
				this.buttonOk.setEnabled(false);
				getRootPane().setDefaultButton(this.buttonOk);
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
			if (SceneUIUtilities.checkAvailableSceneName(this.textFieldSceneName.getText(), oldSceneName)) {
				dialogResult = DialogResult.YES;
			} else {
				String message = String.format(RealspaceViewProperties.getString("String_SaveAsScene_ExistName"), this.textFieldSceneName.getText());
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

}
