package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * 说明面板
 *
 * @author XiaJT
 */
public class JPanelDescribe extends JPanel {

	private JLabel labelDescribe = new JLabel();
	private JScrollPane scrollPaneDescribe = new JScrollPane();
	private JTextArea textAreaDescribe = new JTextArea();

	public JPanelDescribe() {
		textAreaDescribe.setLineWrap(true);
		textAreaDescribe.setEditable(false);
		initLayout();
		initResources();
	}

	private void initLayout() {
		intScrollPaneDescribe();
		this.setLayout(new GridBagLayout());
		this.add(labelDescribe, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(0, 0, 5, 0).setAnchor(GridBagConstraints.WEST));
		this.add(scrollPaneDescribe, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
	}

	private void intScrollPaneDescribe() {
		scrollPaneDescribe.setViewportView(textAreaDescribe);
	}

	private void initResources() {
		this.labelDescribe.setText(CoreProperties.getString("String_Label_Description"));

	}

	public void setDescirbe(String s) {
		this.textAreaDescribe.setText(s);
	}
}
