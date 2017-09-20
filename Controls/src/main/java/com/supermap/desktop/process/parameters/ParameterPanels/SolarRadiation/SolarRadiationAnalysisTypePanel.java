package com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation;

import com.supermap.analyst.spatialanalyst.SolarRadiationParameter;
import com.supermap.analyst.spatialanalyst.SolarTimeMode;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DateTimeComponent.DateSpinner;
import com.supermap.desktop.ui.controls.DateTimeComponent.DateSpinnerFormat;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lixiaoyao on 2017/9/7.
 */
public class SolarRadiationAnalysisTypePanel extends JPanel {

	private ButtonGroup buttonGroup;
	private JRadioButton radioButtonYear;
	private JRadioButton radioButtonDay;
	private JLabel labelSpecificDay;
	private DateSpinner calendarTextFieldSpecificDay;
	private JLabel labelStartDay;
	private JLabel labelEndDay;
	private DateSpinner calendarTextFieldStartDay;
	private DateSpinner calendarTextFieldEndDay;
	private JLabel labelStartTime;
	private JLabel labelEndTime;
	private DateSpinner dateSpinnerStartTime;
	private DateSpinner dateSpinnerEndTime;
	private JLabel labelDayInterval;
	private JLabel labelHourInterval;
	private SmTextFieldLegit textFieldDayInterval;
	private SmTextFieldLegit textFieldHourInterval;

	private Calendar calendar = Calendar.getInstance();
	private Calendar initCalendar = Calendar.getInstance();
	private ArrayList<SolarRadiationParameterListener> solarRadiationParameterListener = new ArrayList<>();
	private SolarRadiationParameter solarRadiationParameter = new SolarRadiationParameter();

	public SolarRadiationAnalysisTypePanel() {
		initComponents();
		initLayout();
		initComponentsState();
		initResources();
		removeEvent();
		registerEvent();
	}

	private void initComponents() {
		this.buttonGroup = new ButtonGroup();
		this.radioButtonDay = new JRadioButton();
		this.radioButtonYear = new JRadioButton();
		this.labelSpecificDay = new JLabel();
//		this.calendarTextFieldSpecificDay = new CalendarTextField("MM-dd");
		this.calendarTextFieldSpecificDay = new DateSpinner(new SpinnerDateModel());
		this.labelStartDay = new JLabel();
		this.labelEndDay = new JLabel();
//		this.calendarTextFieldStartDay = new CalendarTextField("MM-dd");
//		this.calendarTextFieldEndDay = new CalendarTextField("MM-dd");
		this.calendarTextFieldStartDay = new DateSpinner(new SpinnerDateModel());
		this.calendarTextFieldEndDay = new DateSpinner(new SpinnerDateModel());
		this.labelStartTime = new JLabel();
		this.labelEndTime = new JLabel();
		this.dateSpinnerStartTime = new DateSpinner(new SpinnerDateModel());
		this.dateSpinnerEndTime = new DateSpinner(new SpinnerDateModel());
		this.labelDayInterval = new JLabel();
		this.labelHourInterval = new JLabel();
		this.textFieldDayInterval = new SmTextFieldLegit("14");
		this.textFieldHourInterval = new SmTextFieldLegit("1");
	}

	private void initLayout() {
		this.setPreferredSize(new Dimension(200, 170));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.radioButtonYear)
						.addComponent(this.labelSpecificDay)
						.addComponent(this.labelStartDay)
						.addComponent(this.labelEndDay)
						.addComponent(this.labelStartTime)
						.addComponent(this.labelEndTime)
						.addComponent(this.labelDayInterval)
						.addComponent(this.labelHourInterval))
				.addGap(20)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.radioButtonDay)
						.addComponent(this.calendarTextFieldSpecificDay)
						.addComponent(this.calendarTextFieldStartDay)
						.addComponent(this.calendarTextFieldEndDay)
						.addComponent(this.dateSpinnerStartTime)
						.addComponent(this.dateSpinnerEndTime)
						.addComponent(this.textFieldDayInterval)
						.addComponent(this.textFieldHourInterval))

		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.radioButtonYear)
						.addComponent(this.radioButtonDay))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelSpecificDay)
						.addComponent(this.calendarTextFieldSpecificDay))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelStartDay)
						.addComponent(this.calendarTextFieldStartDay))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelEndDay)
						.addComponent(this.calendarTextFieldEndDay))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelStartTime)
						.addComponent(this.dateSpinnerStartTime))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelEndTime)
						.addComponent(this.dateSpinnerEndTime))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDayInterval)
						.addComponent(this.textFieldDayInterval))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelHourInterval)
						.addComponent(this.textFieldHourInterval))

		);
		this.setLayout(groupLayout);
	}

	private void initComponentsState() {
		this.buttonGroup.add(this.radioButtonYear);
		this.buttonGroup.add(this.radioButtonDay);
		this.radioButtonYear.setSelected(true);
//		this.calendarTextFieldSpecificDay.setText(this.calendar.get(Calendar.MONTH) + "-" + this.calendar.get(Calendar.DAY_OF_MONTH));
//		this.calendarTextFieldStartDay.setText("01-05");
//		this.calendarTextFieldEndDay.setText("06-09");
		this.calendarTextFieldSpecificDay.setSpinnerFormat(DateSpinnerFormat.MONTH_DAY);
		this.calendarTextFieldSpecificDay.setValue(this.calendar.getTime());
		this.calendarTextFieldStartDay.setSpinnerFormat(DateSpinnerFormat.MONTH_DAY);
		this.calendarTextFieldEndDay.setSpinnerFormat(DateSpinnerFormat.MONTH_DAY);
		this.dateSpinnerStartTime.setSpinnerFormat(DateSpinnerFormat.HOUR_MINUTE);
		this.dateSpinnerEndTime.setSpinnerFormat(DateSpinnerFormat.HOUR_MINUTE);
		this.calendar.set(Calendar.HOUR_OF_DAY, 0);
		this.calendar.set(Calendar.MINUTE, 0);
		this.dateSpinnerStartTime.setValue(this.calendar.getTime());
		this.calendar.set(Calendar.HOUR_OF_DAY, 23);
		this.calendar.set(Calendar.MINUTE, 59);
		this.dateSpinnerEndTime.setValue(this.calendar.getTime());
		this.calendar.set(Calendar.DAY_OF_YEAR, calculationDayCount(this.calendar.get(Calendar.YEAR), 1, 5, true));
		this.calendarTextFieldStartDay.setValue(this.calendar.getTime());
		//this.calendar.set(Calendar.MONTH, 6);
		this.calendar.set(Calendar.DAY_OF_YEAR, calculationDayCount(this.calendar.get(Calendar.YEAR), 6, 9, true));
		this.calendarTextFieldEndDay.setValue(this.calendar.getTime());
		this.labelDayInterval.setToolTipText(ControlsProperties.getString("String_ValidRange") + "(0,365]");
		this.labelHourInterval.setToolTipText(ControlsProperties.getString("String_ValidRange") + "(0,24]");
		this.solarRadiationParameter.setDayInterval(14);
		this.solarRadiationParameter.setHourInterval(1);
		hideComponent();
		resetSolarRadiationParameter();
		endDayChange();
		startTimeChange();
		endTimeChange();
	}

	private void hideComponent() {
		if (this.radioButtonYear.isSelected()) {
			this.labelSpecificDay.setVisible(false);
			this.calendarTextFieldSpecificDay.setVisible(false);
			this.labelStartTime.setVisible(false);
			this.dateSpinnerStartTime.setVisible(false);
			this.labelEndTime.setVisible(false);
			this.dateSpinnerEndTime.setVisible(false);
			this.labelStartDay.setVisible(true);
			this.calendarTextFieldStartDay.setVisible(true);
			this.labelEndDay.setVisible(true);
			this.calendarTextFieldEndDay.setVisible(true);
			this.labelDayInterval.setVisible(true);
			this.textFieldDayInterval.setVisible(true);
		} else {
			this.labelSpecificDay.setVisible(true);
			this.calendarTextFieldSpecificDay.setVisible(true);
			this.labelStartTime.setVisible(true);
			this.dateSpinnerStartTime.setVisible(true);
			this.labelEndTime.setVisible(true);
			this.dateSpinnerEndTime.setVisible(true);
			this.labelStartDay.setVisible(false);
			this.calendarTextFieldStartDay.setVisible(false);
			this.labelEndDay.setVisible(false);
			this.calendarTextFieldEndDay.setVisible(false);
			this.labelDayInterval.setVisible(false);
			this.textFieldDayInterval.setVisible(false);
		}
	}

	private void initResources() {
		this.radioButtonYear.setText(ControlsProperties.getString("String_SolarRadiationAnalysisYear"));
		this.radioButtonDay.setText(ControlsProperties.getString("String_SolarRadiationAnalysisDay"));
		this.labelSpecificDay.setText(ControlsProperties.getString("String_SolarRadiationAnalysisSpecificDay"));
		this.labelStartDay.setText(ControlsProperties.getString("String_SolarRadiationAnalysisStartDay"));
		this.labelEndDay.setText(ControlsProperties.getString("String_SolarRadiationAnalysisEndDay"));
		this.labelStartTime.setText(ControlsProperties.getString("String_SolarRadiationAnalysisStartDay"));
		this.labelEndTime.setText(ControlsProperties.getString("String_SolarRadiationAnalysisEndDay"));
		this.labelDayInterval.setText(ControlsProperties.getString("String_SolarRadiationAnalysisDayInterval"));
		this.labelHourInterval.setText(ControlsProperties.getString("String_SolarRadiationAnalysisHourInterval"));
	}

	private void registerEvent() {
		this.radioButtonYear.addActionListener(this.selectedChangeListener);
		this.radioButtonDay.addActionListener(this.selectedChangeListener);
		this.dateSpinnerStartTime.addChangeListener(this.changeListenerStartTime);
		this.dateSpinnerEndTime.addChangeListener(this.changeListenerEndTime);
		this.calendarTextFieldSpecificDay.addChangeListener(this.changeListenerSpecific);
		this.calendarTextFieldStartDay.addChangeListener(this.changeListenerStartDay);
		this.calendarTextFieldEndDay.addChangeListener(this.changeListenerEndDay);
		this.textFieldDayInterval.setSmTextFieldLegit(iSmTextFieldLegitDayInterval);
		this.textFieldHourInterval.setSmTextFieldLegit(iSmTextFieldLegitHourInterval);
	}

	private void removeEvent() {
		this.radioButtonYear.removeActionListener(this.selectedChangeListener);
		this.radioButtonDay.removeActionListener(this.selectedChangeListener);
		this.dateSpinnerStartTime.removeChangeListener(this.changeListenerStartTime);
		this.dateSpinnerEndTime.removeChangeListener(this.changeListenerEndTime);
		this.calendarTextFieldSpecificDay.removeChangeListener(this.changeListenerSpecific);
		this.calendarTextFieldStartDay.removeChangeListener(this.changeListenerStartDay);
		this.calendarTextFieldEndDay.removeChangeListener(this.changeListenerEndDay);
		this.textFieldDayInterval.removeEvents();
		this.textFieldHourInterval.removeEvents();
	}

	private ActionListener selectedChangeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			hideComponent();
			resetSolarRadiationParameter();
		}
	};

	private ChangeListener changeListenerStartTime = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			startTimeChange();
		}
	};

	private ChangeListener changeListenerEndTime = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			endTimeChange();
		}
	};

	private ChangeListener changeListenerSpecific = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			specificDateChange();
		}
	};

	private ChangeListener changeListenerStartDay = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			startDayChange();
		}
	};

	private ChangeListener changeListenerEndDay = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			endDayChange();
		}
	};

	private ISmTextFieldLegit iSmTextFieldLegitDayInterval = new ISmTextFieldLegit() {
		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			if (StringUtilities.isNullOrEmpty(textFieldValue)) {
				return false;
			}
			try {
				Integer integer = Integer.valueOf(textFieldValue);
				if (integer <= 0 || integer >= 365) {
					return false;
				}
				solarRadiationParameter.setDayInterval(integer);
				fireChangeListener();
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	private ISmTextFieldLegit iSmTextFieldLegitHourInterval = new ISmTextFieldLegit() {
		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			if (StringUtilities.isNullOrEmpty(textFieldValue)) {
				return false;
			}
			try {
				Double temp = Double.valueOf(textFieldValue);
				if (Double.compare(temp, 0) != 1 || Double.compare(temp, 24) == 1) {
					return false;
				}
				solarRadiationParameter.setHourInterval(temp);
				fireChangeListener();
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	private void specificDateChange() {
		Date date = (Date) this.calendarTextFieldSpecificDay.getValue();
		this.initCalendar.setTime(date);
		this.solarRadiationParameter.setDayStart(calculationDayCount(this.initCalendar.get(Calendar.YEAR), this.initCalendar.get(Calendar.MONTH), this.initCalendar.get(Calendar.DAY_OF_MONTH), false));
		fireChangeListener();

	}

	private void startDayChange() {
		Date date = (Date) this.calendarTextFieldStartDay.getValue();
		this.initCalendar.setTime(date);
		this.solarRadiationParameter.setDayStart(calculationDayCount(this.initCalendar.get(Calendar.YEAR), this.initCalendar.get(Calendar.MONTH), this.initCalendar.get(Calendar.DAY_OF_MONTH), false));
		fireChangeListener();
	}

	private void endDayChange() {
		Date date = (Date) this.calendarTextFieldEndDay.getValue();
		this.initCalendar.setTime(date);
		int temp = calculationDayCount(this.initCalendar.get(Calendar.YEAR), this.initCalendar.get(Calendar.MONTH), this.initCalendar.get(Calendar.DAY_OF_MONTH), false);
		this.solarRadiationParameter.setDayEnd(temp);
		fireChangeListener();
	}

	private void startTimeChange() {
		Date date = (Date) this.dateSpinnerStartTime.getValue();
		this.calendar.setTime(date);
		double temp = this.calendar.get(Calendar.HOUR_OF_DAY) + this.calendar.get(Calendar.MINUTE) / 60.0;
		this.solarRadiationParameter.setHourStart(temp);
		fireChangeListener();
	}

	private void endTimeChange() {
		Date date = (Date) this.dateSpinnerEndTime.getValue();
		this.calendar.setTime(date);
		double temp = this.calendar.get(Calendar.HOUR_OF_DAY) + this.calendar.get(Calendar.MINUTE) / 60.0;
		this.solarRadiationParameter.setHourEnd(temp);
		fireChangeListener();
	}

	// According to the daily value of the month, the date of this year's code is very winding,
	// the interface saw in June 9th, in fact, get the value of May 9th. Therefore,
	// we need to add 1 to the month, and at the time of initialization, we have added 1. After initialization,
	// we will assign the solar radiation parameters, and then we will calculate it again.
	// Therefore, a constant sign is required to indicate whether to initialize or change the value.

	private int calculationDayCount(int year, int month, int day, boolean isInit) {
		int count = 0;
		int days = 0;
		if (!isInit) {
			month = month + 1;
		}
		if (year > 0 && month > 0 && month < 13 && day > 0 && day < 32) {
			for (int i = 1; i < month; i++) {
				switch (i) {
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:
						days = 31;
						break;
					case 4:
					case 6:
					case 9:
					case 11:
						days = 30;
						break;
					case 2: {
						if ((year % 4 == 0 && year % 1 != 0) || (year % 400 == 0)) {
							days = 29;
						} else {
							days = 28;
						}
						break;
					}
				}
				count = count + days;
			}
			count = count + day;
		}
		return count;
	}

	private void resetSolarRadiationParameter() {
		if (this.radioButtonYear.isSelected()) {
			this.solarRadiationParameter.setTimeMode(SolarTimeMode.MULTIDAYS);
			startDayChange();
		} else {
			this.solarRadiationParameter.setTimeMode(SolarTimeMode.WITHINDAY);
			specificDateChange();
		}

	}

	private void fireChangeListener() {
		for (int i = 0; i < this.solarRadiationParameterListener.size(); i++) {
			this.solarRadiationParameterListener.get(i).updateSolarRadiationParameter(this.solarRadiationParameter);
		}
	}

	public void addSolarRadiationParameterListener(SolarRadiationParameterListener solarRadiationParameterListener) {
		this.solarRadiationParameterListener.clear();
		this.solarRadiationParameterListener.add(solarRadiationParameterListener);
		fireChangeListener();
	}

	public void removeSolarRadiationParameterListener(SolarRadiationParameterListener solarRadiationParameterListener) {
		if (this.solarRadiationParameterListener.contains(solarRadiationParameterListener)) {
			this.solarRadiationParameterListener.remove(solarRadiationParameterListener);
		}
	}
}
