package com.supermap.desktop.netservices.iserver;

import java.io.File;

/**
 * 带选择状态的文件封装
 * 
 * @author highsad
 *
 */
public class SelectableFile extends File {
	private File file;
	private boolean isSelected = false;

	public SelectableFile(String pathname, boolean isSelected) {
		super(pathname);
		this.isSelected = isSelected;
	}

	public static SelectableFile fromFile(File file, boolean isSelected) {
		return new SelectableFile(file.getPath(), isSelected);
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
