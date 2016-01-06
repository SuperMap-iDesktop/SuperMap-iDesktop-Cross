package com.supermap.desktop.event;

import java.util.EventListener;

public interface TableCellValueChangeListener extends EventListener {
	void tableCellValueChange(TableCellValueChangeEvent e);
}
