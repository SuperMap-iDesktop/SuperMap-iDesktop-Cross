package com.supermap.desktop.event;

import java.util.EventObject;

public class TableCellValueChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int row = 0;
	private int column = 0;

	public TableCellValueChangeEvent(Object source, int row, int column) {
		super(source);
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}
