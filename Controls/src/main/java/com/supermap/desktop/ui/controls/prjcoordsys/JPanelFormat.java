package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private JLabel labelAngle = new JLabel();
	private SmTextFieldLegit smTextFieldAngle = new SmTextFieldLegit();
	private JButton buttonExchange = new JButton();

	private SmTextFieldLegit smTextFieldA = new SmTextFieldLegit();
	private SmTextFieldLegit smTextFieldM = new SmTextFieldLegit();
	private SmTextFieldLegit smTextFieldS = new SmTextFieldLegit();

	private JLabel labelA = new JLabel();
	private JLabel labelM = new JLabel();
	private JLabel labelS = new JLabel();

	private double value = 0;
	private DecimalFormat df = new DecimalFormat("0.######################");
	private boolean lock = false;

	public JPanelFormat() {
		init();
	}

	private void init() {
		this.setPreferredSize(new Dimension(80, 23));
		initComponents();
		addListeners();
		initResources();
		initComponentState();
	}

	private void initResources() {
		smTextFieldA.setHorizontalAlignment(SwingConstants.LEFT);
		smTextFieldM.setHorizontalAlignment(SwingConstants.LEFT);
		smTextFieldS.setHorizontalAlignment(SwingConstants.LEFT);
		smTextFieldAngle.setHorizontalAlignment(SwingConstants.LEFT);
		Color borderColor = new Color(171, 173, 179);
		smTextFieldA.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, borderColor));
		Dimension textFieldSize = new Dimension(32, 23);
		smTextFieldA.setPreferredSize(textFieldSize);
		smTextFieldM.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, borderColor));
		smTextFieldM.setPreferredSize(textFieldSize);
		smTextFieldS.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, borderColor));
		smTextFieldS.setPreferredSize(textFieldSize);

		labelA.setText(CommonProperties.getString("String_AngleSign_Degree"));
		labelA.setOpaque(true);
		Color background = smTextFieldA.getBackground();
		labelA.setBackground(background);
		MatteBorder matteBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, borderColor);
		labelA.setBorder(matteBorder);

		labelM.setText(CommonProperties.getString("String_AngleSign_Minute"));
		labelM.setOpaque(true);
		labelM.setBorder(matteBorder);
		labelM.setBackground(background);

		labelS.setText(CommonProperties.getString("String_AngleSign_Second"));
		labelS.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, borderColor));
		labelS.setOpaque(true);
		labelS.setBackground(background);

		labelAngle.setOpaque(true);
		labelAngle.setBackground(background);
		labelAngle.setText(CommonProperties.getString("String_AngleSign_Degree"));
		labelAngle.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, borderColor));
		smTextFieldAngle.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, borderColor));

		Dimension maximumSize = new Dimension(20, 20);
		buttonExchange.setMaximumSize(maximumSize);
		buttonExchange.setMinimumSize(maximumSize);
		buttonExchange.setPreferredSize(maximumSize);
		buttonExchange.setToolTipText(ControlsProperties.getString("String_Button_Conversion"));

	}

	private void initComponents() {
		buttonExchange.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/exchange.png"));
		buttonExchange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMode(!isMode());
			}
		});
		this.setLayout(new GridBagLayout());
	}

	private void initLayout() {
		this.removeAll();
		lock = true;
		smTextFieldA.setText("0");
		smTextFieldM.setText("0");
		smTextFieldS.setText("0");
		if (mode == angle) {
			this.add(smTextFieldAngle, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
			this.add(labelAngle, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.VERTICAL).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER));
		} else {
			this.add(smTextFieldA, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setIpad(0, 0));
			this.add(labelA, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.VERTICAL).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
			this.add(smTextFieldM, new GridBagConstraintsHelper(3, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0).setIpad(0, 0));
			this.add(labelM, new GridBagConstraintsHelper(4, 0, 1, 1).setFill(GridBagConstraints.VERTICAL).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
			this.add(smTextFieldS, new GridBagConstraintsHelper(5, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
			this.add(labelS, new GridBagConstraintsHelper(6, 0, 1, 1).setFill(GridBagConstraints.VERTICAL).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		}
		this.add(buttonExchange, new GridBagConstraintsHelper(10, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5));//.setIpad(-20, 0)
		this.revalidate();
		this.repaint();
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
			this.smTextFieldS.setText(DoubleUtilities.toString((Math.abs(value) - Math.abs(a) - (double) m / 60) * 3600, 2));
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
		if (this.mode != mode) {
			this.mode = mode;
			initLayout();
		}
	}

	public void setValue(double value) {
		this.value = value;
		initValue();
	}

	@Override
	public String toString() {
		String value = "";
		if (mode == angle) {
			value = smTextFieldAngle.getText();
		} else if (mode == ANGLE_M_S) {
			value = smTextFieldA.getText() + labelA.getText() + smTextFieldM.getText() + labelM.getText() + smTextFieldS.getText() + labelS.getText();
		}
		return value;
	}
}
