package com.supermap.desktop.ui.controls.table;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by hanyz on 2017/2/27.
 */
public class SmTable extends JTable {
	public SmTable() {
		super();
		init();
	}

	public SmTable(TableModel dm) {
		super(dm);
		init();
	}

	private void init() {
		this.setGridColor(Color.lightGray);
	}

}
