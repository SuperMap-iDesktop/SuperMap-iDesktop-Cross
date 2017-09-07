package com.supermap.desktop.ui.controls.DateTimeComponent;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/9/6.
 */
public class CalendarTextField extends JPopupMenu {

	public CalendarTextField(){
		super();
		CalendarChooser ser = CalendarChooser.getInstance();
		ser.register(this);
	}
}
