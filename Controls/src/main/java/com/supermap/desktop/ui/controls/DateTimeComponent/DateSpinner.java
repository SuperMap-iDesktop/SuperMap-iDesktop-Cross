package com.supermap.desktop.ui.controls.DateTimeComponent;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/9/5.
 */
public class DateSpinner extends JSpinner {

	private static final long serialVersionUID = 1L;

	// Default display format is：year-month-day-a.m/p.m-hour-minute
	public DateSpinner(SpinnerDateModel spinnerDateModel) {
		super(spinnerDateModel);
	}

	// Sets the time format displayed
	public void setSpinnerFormat(DateSpinnerFormat dateSpinnerFormat) {
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(this, dateSpinnerFormat.getValue());
		this.setEditor(dateEditor);
	}
}
