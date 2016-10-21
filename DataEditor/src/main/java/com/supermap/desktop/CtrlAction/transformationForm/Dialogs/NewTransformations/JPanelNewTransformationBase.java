package com.supermap.desktop.CtrlAction.transformationForm.Dialogs.NewTransformations;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public abstract class JPanelNewTransformationBase extends JPanel {
	private JPanel panelTitle = new JPanel();
	private JLabel labelTitle = new JLabel();

	private JPanel panelDescribe = new JPanel();
	private JTextArea textArea = new JTextArea();

	public JPanelNewTransformationBase() {
		textArea.setEditable(false);
		textArea.setOpaque(false);
//		labelTitle.setText(getPanelTitle());
		textArea.setText(getDescribeText());
		initLayout();
	}

	private void initLayout() {
		this.panelTitle.setLayout(new GridBagLayout());
		this.panelTitle.add(labelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER));

		this.panelDescribe.setLayout(new GridBagLayout());
		this.panelDescribe.add(textArea, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));

		this.setLayout(new GridBagLayout());
		this.add(panelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER));
		this.add(getCenterPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
		this.add(panelDescribe, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER));
	}

	protected abstract String getPanelTitle();

	protected abstract String getDescribeText();

	protected abstract JPanel getCenterPanel();


	protected abstract void setButtonNext(SmButton buttonNext);
}
