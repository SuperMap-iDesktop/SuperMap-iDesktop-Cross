package com.supermap.desktop.core;

import java.math.BigDecimal;

import com.supermap.desktop.utilties.MathUtilities;

public class FileSize {
	public double size = 0.0;
	public FileSizeType sizeType = FileSizeType.BYTE;
	public static final FileSize ZERO = new FileSize(0d, FileSizeType.BYTE);

	public FileSize(double size, FileSizeType sizeType) {
		this.size = size;
		this.sizeType = sizeType;
	}

	public double getSize() {
		return size;
	}

	void setSize(double size) {
		this.size = size;
	}

	public FileSizeType getSizeType() {
		return sizeType;
	}

	void setSizeType(FileSizeType sizeType) {
		this.sizeType = sizeType;
	}

	public FileSize ConvertTo(FileSizeType targetType) {
		FileSize fileSize = new FileSize(this.size, this.sizeType);
		if (targetType != this.sizeType) {
			fileSize.setSizeType(targetType);
			int targetRatio = Double.valueOf(MathUtilities.log(targetType.getValue(), 2)).intValue();
			int sourceRatio = Double.valueOf(MathUtilities.log(this.sizeType.getValue(), 2)).intValue();
			int minusRatio = sourceRatio - targetRatio;
			fileSize.setSize(Math.pow(1024, minusRatio) * this.size);
		}

		return fileSize;
	}

	public static boolean equals(FileSize left, FileSize right) {
		return (left.getSize() == right.getSize() && left.getSizeType() == right.getSizeType());
	}

	public static FileSize divide(FileSize left, double right) {
		if (Double.doubleToRawLongBits(right) == 0) {
			return left;
		} else {
			return new FileSize(left.getSize() / right, left.getSizeType());
		}
	}

	public static double divide(FileSize left, FileSize right) {
		double result = 0d;

		if (FileSize.equals(right, FileSize.ZERO)) {
			return result;
		}

		if (left.getSizeType() == right.getSizeType()) {
			result = left.getSize() / right.getSize();
		} else {
			FileSize tmpRightSize = right.ConvertTo(left.getSizeType());
			result = left.getSize() / tmpRightSize.getSize();
		}

		return result;
	}

	public static FileSize multiply(FileSize left, double right) {
		return new FileSize(left.getSize() * right, left.getSizeType());
	}

	public static FileSize add(FileSize left, double right) {
		return new FileSize(left.getSize() + right, left.getSizeType());
	}

	public static FileSize add(FileSize left, FileSize right) {
		if (left.getSizeType() == right.getSizeType()) {
			return new FileSize(left.getSize() + right.getSize(), left.getSizeType());
		} else {
			right.setSizeType(left.getSizeType());

			int targetRatio = Double.valueOf(MathUtilities.log(left.getSizeType().getValue(), 2)).intValue();
			int sourceRatio = Double.valueOf(MathUtilities.log(right.getSizeType().getValue(), 2)).intValue();
			int minusRatio = sourceRatio - targetRatio;

			right.setSize(Math.pow(1024, minusRatio) * left.getSize());
			return new FileSize(left.getSize() + right.getSize(), left.getSizeType());
		}
	}

	public static FileSize subtract(FileSize left, double right) {
		if ((left.getSize() - right) < 0) {
			return new FileSize(0, left.getSizeType());
		} else {
			return new FileSize(left.getSize() - right, left.getSizeType());
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%.2f", this.size) + " " + this.sizeType.toString();
	}

	// 智能字符串转换 举例说明：如果当前单位是KB，实际数值大于1MB即转为 10+MB字符串显示
	public String ToStringClever() {
		FileSize fileSize = this;

		while (fileSize.getSizeType() != FileSizeType.PB && fileSize.getSize() > 1024) {
			FileSizeType targetSizeType = FileSizeType.valueOf(fileSize.getSizeType().getValue() * 2);
			fileSize = fileSize.ConvertTo(targetSizeType);
		}

		return fileSize.toString();
	}
}
