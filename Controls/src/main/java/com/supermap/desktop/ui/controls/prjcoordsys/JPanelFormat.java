package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * 用来切换 度 <--> 度分秒的Panel
 *
 * @author XiaJT
 */
public class JPanelFormat extends JPanel {
	public static final boolean angle = true;
	public static final boolean ANGLE_M_S = false;

	private boolean mode = angle;

	private SmTextFieldLegit smTextFieldAngle = new SmTextFieldLegit();
	private SmButton buttonExchange = new SmButton();

	private SmTextFieldLegit smTextFieldA = new SmTextFieldLegit();
	private SmTextFieldLegit smTextFieldM = new SmTextFieldLegit();
	private SmTextFieldLegit smTextFieldS = new SmTextFieldLegit();

	private JLabel labelA = new JLabel();
	private JLabel labelM = new JLabel();

	private double value = 0;
	private DecimalFormat df = new DecimalFormat("0.######################");
	private boolean lock = false;

	public JPanelFormat() {
		init();
	}

	private void init() {
		this.setPreferredSize(new Dimension(50, 23));
		initComponents();
		addListeners();
		initResources();
		initComponentState();
	}

	private void initResources() {
		labelA.setText(ControlsProperties.getString("String_Colon"));
		labelM.setText(ControlsProperties.getString("String_Colon"));
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
	}

	private void initLayout() {
		this.removeAll();
		lock = true;
		if (mode == angle) {
			this.add(smTextFieldAngle, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));

		} else {
			this.add(smTextFieldA, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
			this.add(labelA, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
			this.add(smTextFieldM, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
			this.add(labelM, new GridBagConstraintsHelper(3, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
			this.add(smTextFieldS, new GridBagConstraintsHelper(4, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));

		}
		initValue();
		lock = false;
	}

	private void initValue() {
		if (mode == angle) {
			this.smTextFieldAngle.setText(df.format(value));
		} else {
			int a = (int) ((value > 0 ? 1 : -1) * Math.floor(Math.abs(value)));
			this.smTextFieldA.setText(String.valueOf(a));
			int m = (int) Math.floor((Math.abs(value) - Math.abs(a)) * 60);
			this.smTextFieldM.setText(String.valueOf(m));
			double s = Math.floor((Math.abs(value) - Math.abs(a) - (double) m / 60) * 3600);
			this.smTextFieldS.setText(String.valueOf(s));
		}
	}

	private void addListeners() {
		smTextFieldAngle.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Double aDouble = Double.valueOf(textFieldValue);
					return !(aDouble < -180 || aDouble > 180) && updateValue();
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		smTextFieldA.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Integer integer = Integer.valueOf(textFieldValue);
					return !(integer > 180 || integer < -180) && updateValue();
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		smTextFieldM.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Integer integer = Integer.valueOf(textFieldValue);
					return !(integer > 59 || integer < 0) && updateValue();
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		smTextFieldS.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Double aDouble = Double.valueOf(textFieldValue);
					return !(aDouble > 59 || aDouble < 0) && updateValue();
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
	}

	private boolean updateValue() {
		if (!lock) {
			double backValue = value;
			if (mode == angle) {
				value = Double.valueOf(smTextFieldAngle.getText());
			} else {
				Double a = Double.valueOf(smTextFieldA.getText());
				Double m = Double.valueOf(smTextFieldM.getText());
				Double s = Double.valueOf(smTextFieldS.getText());
				value = Math.abs(a) + m / 60 + s / 3600;
				if (a < 0) {
					value = -value;
				}
			}
			if (value < -180 || value > 180) {
				value = backValue;
				return false;
			}
		}
		firePropertyChange("GeoCoordSysType", "", "");
		return true;
	}

	public double getValue() {
		return value;
	}

	private void initComponentState() {
		initLayout();
	}

	public boolean isMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
		initLayout();
	}

	public void setValue(double value) {
		this.value = value;
		initValue();
	}
}
