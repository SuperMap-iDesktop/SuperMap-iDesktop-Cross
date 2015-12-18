package com.supermap.desktop.spatialanalyst.vectoranalyst;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import com.supermap.desktop.properties.CoreProperties;

public class PanelButton extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton buttonOk;
	private JButton buttonCancel;

	public JButton getButtonOk() {
		return buttonOk;
	}

	public void setButtonOk(JButton buttonOk) {
		this.buttonOk = buttonOk;
	}

	public JButton getButtonCancel() {
		return buttonCancel;
	}

	public void setButtonCancel(JButton buttonCancel) {
		this.buttonCancel = buttonCancel;
	}

	public PanelButton() {
		initComponent();
		initResources();
		setPanelButtonLayout();

	}

	private void initComponent() {
		this.buttonOk = new JButton("Ok");
		this.buttonCancel = new JButton("Cancel");

	}

	private void initResources() {
		this.buttonOk.setText(CoreProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CoreProperties.getString("String_Button_Cancel"));

	}

	private void setPanelButtonLayout() {
		GroupLayout panelButtonLayout = new GroupLayout(this);
		this.setLayout(panelButtonLayout);

		//@formatter:off
		panelButtonLayout.setHorizontalGroup(panelButtonLayout.createSequentialGroup()
				.addGap(420)
				.addComponent(this.buttonOk).addGap(15)
				.addComponent(this.buttonCancel));
		panelButtonLayout.setVerticalGroup(panelButtonLayout.createSequentialGroup()
				.addGroup(panelButtonLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonOk)
						.addComponent(buttonCancel)).addGap(5));
		//@formatter:on
	}

}
