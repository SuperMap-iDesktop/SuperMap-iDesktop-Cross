package com.supermap.desktop.http;

import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

public class FileManagerContainer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<FileManager> items;

	GroupLayout groupLayout;
	Group horizontalGroup = null;
	Group verticalGroup = null;

	public FileManagerContainer() {
		items = new ArrayList<FileManager>();

		initializeComponents();
		initializeResources();
	}

	private void initializeComponents() {

		groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		updateItems();
	}

	private void initializeResources() {
		// this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
	}

	private void updateItems() {
		removeAll();
		horizontalGroup = groupLayout.createParallelGroup(Alignment.CENTER);
		groupLayout.setHorizontalGroup(horizontalGroup);

		verticalGroup = groupLayout.createSequentialGroup();
		groupLayout.setVerticalGroup(verticalGroup);

		for (FileManager item : items) {
			horizontalGroup.addComponent(item);
			verticalGroup.addComponent(item);
		}
	}

	public void addItem(FileManager fileManager) {
		items.add(fileManager);
		updateItems();
	}

	public void removeItem(DownloadInfo downloadInfo) {
		for (FileManager item : items) {
			if (item.getDownloadInfo().equals(downloadInfo)) {
				items.remove(item);
				groupLayout.removeLayoutComponent(item);
				break;
			}
		}
		updateItems();
	}
}
