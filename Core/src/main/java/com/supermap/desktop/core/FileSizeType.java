package com.supermap.desktop.core;

public enum FileSizeType {
	BYTE(1, "Byte"), KB(2, "KB"), MB(4, "MB"), GB(8, "GB"), TB(16, "TB"), PB(32, "PB");

	private int value;
	private String text;

	private FileSizeType(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.text;
	}

	public static FileSizeType valueOf(int value) {
		if (value == 1) {
			return FileSizeType.BYTE;
		} else if (value == 2) {
			return FileSizeType.KB;
		} else if (value == 4) {
			return FileSizeType.MB;
		} else if (value == 8) {
			return FileSizeType.GB;
		} else if (value == 16) {
			return FileSizeType.TB;
		} else if (value == 32) {
			return FileSizeType.PB;
		} else {
			return FileSizeType.BYTE;
		}
	}
}
