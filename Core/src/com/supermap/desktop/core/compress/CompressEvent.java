package com.supermap.desktop.core.compress;

import java.util.EventObject;

import com.supermap.desktop.core.FileSize;

public class CompressEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileSize totalSize = FileSize.ZERO;
	private FileSize compressedSize = FileSize.ZERO;
	private int currentEntry = 0;
	private int totalEntry = 0;
	private int percent = 0;
	private boolean isCancel = false;

	public CompressEvent(Object source, FileSize totalSize, FileSize compressedSize, int currentEntry, int totalEntry) {
		super(source);
		this.totalSize = totalSize;
		this.compressedSize = compressedSize;
		this.currentEntry = currentEntry;
		this.totalEntry = totalEntry;
		this.percent = new Double(100 * FileSize.divide(this.compressedSize, this.totalSize)).intValue();
	}

	public FileSize getTotalSize() {
		return totalSize;
	}

	public FileSize getCompressedSize() {
		return compressedSize;
	}

	public int getCurrentEntry() {
		return this.currentEntry;
	}

	public int getTotalEntry() {
		return this.totalEntry;
	}

	public int getPercent() {
		return percent;
	}

	public boolean isCancel() {
		return this.isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
}
