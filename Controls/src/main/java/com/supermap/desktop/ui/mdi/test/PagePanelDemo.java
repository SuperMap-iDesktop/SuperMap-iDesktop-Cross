package com.supermap.desktop.ui.mdi.test;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class PagePanelDemo extends JPanel {
	private JTextField textField;

	public PagePanelDemo(String text) {

		JLabel lblNewLabel = new JLabel("New label");

		JButton btnNewButton = new JButton("New button");

		JButton btnNewButton_1 = new JButton("New button");

		textField = new JTextField();
		textField.setColumns(10);
		textField.setText(text);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup().addGap(35).addComponent(lblNewLabel)
														.addGap(18).addComponent(textField, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
												.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addContainerGap(236, Short.MAX_VALUE)
														.addComponent(btnNewButton_1).addGap(18).addComponent(btnNewButton)))
										.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(46)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(textField, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnNewButton).addComponent(btnNewButton_1)).addGap(19)));
		setLayout(groupLayout);
	}
}
