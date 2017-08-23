package com.supermap.desktop.utilities;

import com.supermap.desktop.Interface.ICloneable;

/**
 * 只供StringUtilties做字符串匹配使用，故只提供包内权限
 *
 * @author XiaJT
 */
class BracketData implements ICloneable {

	char left;
	char right;

	BracketData(char left, char right) {
		this.left = left;
		this.right = right;
	}

	public boolean isComplete(char currentChar) {
		return right == currentChar;
	}

	public boolean isStart(char currentChar) {
		return left == currentChar;
	}

	public char getLeft() {
		return left;
	}

	public void setLeft(char left) {
		this.left = left;
	}

	public char getRight() {
		return right;
	}

	public void setRight(char right) {
		this.right = right;
	}

	@Override
	public BracketData clone() {
		return new BracketData(this.left, this.right);
	}
}
