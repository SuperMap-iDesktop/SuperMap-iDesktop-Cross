package com.supermap.desktop.netservices.iserver;

import java.io.File;

/**
 * 带选择状态的文件封装
 * 
 * @author highsad
 *
 */
public class SelectableFile {
	private File file;
	private boolean isSelected = false;

	public SelectableFile(File file) {
		this.file = file;
	}

	public SelectableFile(String filePath) {
		this(new File(filePath));
	}

	public SelectableFile(File file, boolean isSelected) {
		this(file);
		this.isSelected = isSelected;
	}

	public File getFile() {
		return this.file;
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isDirectory() {
		return this.file.isDirectory();
	}

	public boolean isHidden() {
		return this.file.isHidden();
	}

	public boolean isAbsolute() {
		return this.file.isAbsolute();
	}

	public String getName() {
		return this.file.getName();
	}

	public long length() {
		return this.file.length();
	}

	public long lastModified() {
		return this.file.lastModified();
	}
}
