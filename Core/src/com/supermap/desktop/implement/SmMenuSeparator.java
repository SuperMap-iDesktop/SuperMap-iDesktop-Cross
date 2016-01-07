package com.supermap.desktop.implement;

import javax.swing.JSeparator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;

public class SmMenuSeparator extends JSeparator implements IBaseItem {
	private static final long serialVersionUID = 1L;

	public SmMenuSeparator() {
		// do nothing
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setIndex(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICtrlAction getCtrlAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCtrlAction(ICtrlAction ctrlAction) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub

	}

}
