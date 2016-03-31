package com.supermap.desktop.dialog;

import com.supermap.data.Maps;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.MapUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DialogSaveAsMap extends SmDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabelMapName;
	private JTextField textFieldMapName;
	/**
	 * 另存或保存地图时原来的地图名称
	 */
	private String myMapName;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private boolean isNewWindow = false;
	private Maps maps;

	/**
	 * Create the dialog.
	 */
	public DialogSaveAsMap() {
		setTitle("Map Save As...");
		setBounds(100, 100, 359, 127);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		this.lblNewLabelMapName = new JLabel("Map Name:");
		this.textFieldMapName = new JTextField();
		this.textFieldMapName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				textFieldMapName_KeyPerformed();
			}
		});
		this.textFieldMapName.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(this.textFieldMapName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(this.lblNewLabelMapName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addComponent(lblNewLabelMapName).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(this.textFieldMapName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(176, Short.MAX_VALUE)));
		this.contentPanel.setLayout(gl_contentPanel);
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
		this.buttonOk.setActionCommand("OK");
		buttonPane.add(buttonOk);
		getRootPane().setDefaultButton(buttonOk);
		buttonCancel = new SmButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_Click();
			}
		});
		this.buttonCancel.setActionCommand("Cancel");
		buttonPane.add(buttonCancel);
		getRootPane().setDefaultButton(this.buttonOk);
		this.setLocationRelativeTo(null);
		initializeResources();
	}

	public String getMapName() {
		return this.textFieldMapName.getText();
	}

	/**
	 * 设置保存地图的前缀名称
	 * 
	 * @param name
	 *            地图名称
	 * @param isNewWindow
	 *            是否为新窗体
	 */
	public void setMapName(String name, boolean isNewWindow) {
		this.myMapName = name;
		if (!isNewWindow) {
			this.textFieldMapName.setText(MapUtilties.getAvailableMapName(myMapName, true));
		} else {
			this.textFieldMapName.setText(myMapName);
		}
		this.textFieldMapName.selectAll();
	}

	public Maps getMaps() {
		return this.maps;
	}

	public void setMaps(Maps maps) {
		this.maps = maps;
	}

	public boolean isNewWindow() {
		return this.isNewWindow;
	}

	public void setIsNewWindow(boolean isNewWindow) {
		this.isNewWindow = isNewWindow;

		if (this.isNewWindow) {
			this.setTitle(MapViewProperties.getString("String_Form_SaveMap"));
		} else {
			this.setTitle(MapViewProperties.getString("String_Form_SaveAsMap"));
		}
	}

	private void initializeResources() {
		try {
			this.setTitle(MapViewProperties.getString("String_Form_SaveAsMap"));
			this.lblNewLabelMapName.setText(MapViewProperties.getString("String_Label_InputMapName"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void textFieldMapName_KeyPerformed() {
		try {
			String name = this.textFieldMapName.getText();
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
			if (MapUtilties.checkAvailableMapName(this.textFieldMapName.getText(), myMapName)) {
				dialogResult = DialogResult.YES;
			} else {
				String message = String.format(MapViewProperties.getString("String_SaveAsMap_ExistName"), this.textFieldMapName.getText());
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
