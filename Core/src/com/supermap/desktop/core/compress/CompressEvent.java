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
	private int percent = 0;
	private boolean isCancel = false;

	public CompressEvent(Object source, FileSize totalSize, FileSize compressedSize) {
		super(source);
		this.totalSize = totalSize;
		this.compressedSize = compressedSize;
		this.percent = new Double(FileSize.divide(this.compressedSize, this.totalSize)).intValue();
	}

	public FileSize getTotalSize() {
		return totalSize;
	}

	public FileSize getCompressedSize() {
		return compressedSize;
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
