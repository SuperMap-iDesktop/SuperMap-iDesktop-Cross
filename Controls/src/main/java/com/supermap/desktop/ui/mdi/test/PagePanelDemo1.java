package com.supermap.desktop.ui.mdi.test;

import javax.swing.*;

/**
 * Created by highsad on 2016/9/26.
 */
public class PagePanelDemo1 extends JPanel {
	private JTree tree = new JTree();
	private JLabel label1 = new JLabel("数据源:");
	private JTextField textField1 = new JTextField();
	private JLabel label2 = new JLabel("数据集:");
	private JTextField textField2 = new JTextField();
	private JButton buttonOK = new JButton("OK");
	private JButton buttonCancel = new JButton("Cancel");

	public PagePanelDemo1() {
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(this.tree, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(this.label1)
								.addComponent(this.textField1, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
								.addComponent(this.label2)
								.addComponent(this.textField2, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
								.addGap(10, 10, Short.MAX_VALUE)
								.addComponent(this.buttonOK)
								.addComponent(this.buttonCancel))));

		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(this.tree, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.label1)
								.addComponent(this.textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.label2)
								.addComponent(this.textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(5, 5, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.buttonOK)
								.addComponent(this.buttonCancel))));
	}
}
