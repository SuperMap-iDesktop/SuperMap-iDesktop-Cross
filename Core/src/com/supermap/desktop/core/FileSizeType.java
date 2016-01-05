package com.supermap.desktop.core;

public enum FileSizeType {
	BYTE(1), KB(2), MB(4), GB(8), TB(16), PB(32);

	private int value;

	private FileSizeType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
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
