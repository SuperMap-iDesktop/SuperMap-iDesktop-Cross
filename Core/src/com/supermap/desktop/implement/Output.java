package com.supermap.desktop.implement;

import com.supermap.desktop.Interface.IOutput;
import com.supermap.desktop.enums.InfoType;

public class Output implements IOutput {

	@Override
	public String getLineText(int index) {
		return null;
	}

	@Override
	public int getLineCount() {
		return 0;
	}

	@Override
	public int getMaxLineCount() {
		return 0;
	}

	@Override
	public void setMaxLineCount(int maxCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean getIsWordWrapped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsWordWrapped(Boolean isWordWrapped) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean getIsTimePrefixAdded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsTimePrefixAdded(Boolean isTimePrefixAdded) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTimePrefixFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTimePrefixFormat(String timePrefixFormat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void output(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void output(Exception exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void output(String message, InfoType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearOutput() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canCopy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void copy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canClear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void output(Throwable e) {
		// TODO Auto-generated method stub

	}
}
