package com.supermap.desktop.ui;

import java.util.ArrayList;

/**
 * Created by highsad on 2016/11/17.
 */
public class DockConstraint {
	private ArrayList<DockConstraint> leftDocks = new ArrayList<>();
	private ArrayList<DockConstraint> rightDocks = new ArrayList<>();
	private ArrayList<DockConstraint> topDocks = new ArrayList<>();
	private ArrayList<DockConstraint> bottomDocks = new ArrayList<>();

	private XMLDockbar dockbar;

	public DockConstraint() {

	}

	public DockConstraint[] getLeftDocks() {
		return this.leftDocks.toArray(new DockConstraint[this.leftDocks.size()]);
	}

	public DockConstraint[] getRightDocks() {
		return this.rightDocks.toArray(new DockConstraint[this.rightDocks.size()]);
	}

	public DockConstraint[] getTopDocks() {
		return this.topDocks.toArray(new DockConstraint[this.topDocks.size()]);
	}

	public DockConstraint[] getBottomDocks() {
		return this.bottomDocks.toArray(new DockConstraint[this.bottomDocks.size()]);
	}

	public XMLDockbar getDockbar() {
		return dockbar;
	}

	public void setDockbar(XMLDockbar dockbar) {
		this.dockbar = dockbar;
	}

	public int addLeftDock(DockConstraint left) {
		this.leftDocks.add(left);
		return this.leftDocks.size() - 1;
	}

	public int addRightDock(DockConstraint right) {
		this.rightDocks.add(right);
		return this.rightDocks.size() - 1;
	}

	public int addTopDock(DockConstraint top) {
		this.topDocks.add(top);
		return this.topDocks.size() - 1;
	}

	public int addBottomDock(DockConstraint bottom) {
		this.bottomDocks.add(bottom);
		return this.bottomDocks.size() - 1;
	}

	public void reset() {
		this.leftDocks.clear();
		this.rightDocks.clear();
		this.topDocks.clear();
		this.bottomDocks.clear();
		this.dockbar = null;
	}
}
