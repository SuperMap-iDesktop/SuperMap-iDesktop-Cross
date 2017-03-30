package com.supermap.desktop.ui.controls.TextFields;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.dialog.symbolDialogs.SymbolSpinnerUtilties;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xie on 2016/12/13.
 */
public class WaringTextField extends JPanel {
	private JLabel labelWarning;
	private JTextField textField;
	private Double startValue;
	private Double endValue;
	private String defaultValue;
	public static final int INTEGER_TYPE = 0;
	public static final int FLOAT_TYPE = 1;
	public static final int MEMORY_LABEL_TYPE = 2;
	public static final int POSITIVE_INTEGER_TYPE = 3;
	private int type;
	private ArrayList listeners;
	private DecimalFormat format = new DecimalFormat("0.#######");
	private static final DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###");
	private boolean isNeedDisplayAThousandPoints = false;

	private String customTipText;

	private CaretListener caretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			String text = textField.getText();
			if (isNeedDisplayAThousandPoints) {
				text = backNumberFromFormat(text);
			}
			// 当数据类型为正整数时，对获得的字符串需要预处理，进行范围判断的值是纯数字--yuanR 2017.3.18
			if (type == MEMORY_LABEL_TYPE) {
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(text);
				text = m.replaceAll("").trim();
			}
			if (null != startValue && null != endValue && !SymbolSpinnerUtilties.isLegitNumber(startValue, endValue, text)) {
				labelWarning.setText("");
				labelWarning.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
				setInitInfo(startValue, endValue, type, floatLength);
			} else if (!StringUtilities.isNullOrEmpty(text) && StringUtilities.isNumber(text)) {
				labelWarning.setText(null);
				//当不需要警告图标时，设置一个透明的图标--yuanR 2017.3.27
				labelWarning.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/clarity.png"));
				fireListener(text);
			}
		}
	};

	/**
	 * 重写键盘输入限制事件--yuanR 2017.3.1
	 * 根据传入的类型设置其可输入的字符串
	 * 其中类型暂定有：
	 * 整型： INTEGER_TYPE   能输入数字，负号
	 * 浮点型： FLOAT_TYPE   能输入数字，负号，小数点
	 * 正整数  POSITIVE_INTEGER_TYPE 只有数字
	 */
	private KeyListener keyAdapter = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			int keyChar = e.getKeyChar();
			String text = textField.getText();
			if (isNeedDisplayAThousandPoints) {
				text = backNumberFromFormat(text);
			}
			//“-”负号在首位，并且只能输入一次
			if (!StringUtilities.isNullOrEmpty(text) && keyChar == KeyEvent.VK_MINUS) {// keyChar == 45代表负号
				e.consume();
			}
			//“.”不能在首位，并且只能输入一次
			if (StringUtilities.isNullOrEmpty(text) && keyChar == KeyEvent.VK_PERIOD || text.contains(".") && keyChar == KeyEvent.VK_PERIOD) {//keyChar == 46代表小数点
				e.consume();
			}
			// 负号后面不能跟小数点
			if (keyChar == KeyEvent.VK_PERIOD && text.equals("-")) {
				e.consume();
			}
			// 限制只能输入数字、负号、小数点
			if (keyChar < 45 || keyChar > 57) {
				e.consume();
			}
			if (keyChar == 47) {
				e.consume();
			}

			// 整型限制输入小数点
			if (type == INTEGER_TYPE && keyChar == 46) {
				e.consume();
			}
			// 正整型限制输入负号和小数点
			if (type == POSITIVE_INTEGER_TYPE && keyChar == 46 || type == POSITIVE_INTEGER_TYPE && keyChar == 45) {
				e.consume();
			}
		}
	};

	private String floatLength;

	public WaringTextField() {
		this("");
	}

	public WaringTextField(boolean isNeedDisplayAThousandPoints) {
		this("", isNeedDisplayAThousandPoints);
	}

	public WaringTextField(String defaultValue) {
		super();
		this.defaultValue = defaultValue;
		initComponents();
		initLayout();
	}

	public WaringTextField(String defaultValue, boolean isNeedDisplayAThousandPoints) {
		super();
		this.isNeedDisplayAThousandPoints = isNeedDisplayAThousandPoints;
		if (!StringUtilities.isNullOrEmpty(defaultValue)) {
			if (this.isNeedDisplayAThousandPoints) {
				defaultValue = numberFormat(Double.valueOf(defaultValue));
			}
		}

		this.defaultValue = defaultValue;
		initComponents();
		initLayout();
	}

	public void setInitInfo(double startValue, double endValue, int type, String floatLength) {
		this.startValue = startValue;
		this.endValue = endValue;
		this.type = type;
		this.floatLength = floatLength;
		String text = textField.getText();
		if (this.isNeedDisplayAThousandPoints) {
			text = backNumberFromFormat(text);
		}

		double currentValue = 0;
		if (!StringUtilities.isNullOrEmpty(text)) {
			try {
				currentValue = DoubleUtilities.stringToValue(text);
			} catch (Exception e) {
				// ignore
			}
		}

		// 当最大值或最小值为无穷大时，设置显示的tip信息为：正负无穷大符号--yuanR 2017.3.16
		if (this.type == INTEGER_TYPE || this.type == POSITIVE_INTEGER_TYPE) {
			if (currentValue > this.endValue) {// 大于最大值
				this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_IntegerMaxWarning"), "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
			} else {//小于最小值
				this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_IntegerMinWarning"), "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
			}
		} else if (this.type == FLOAT_TYPE) {
			// 对浮点类型的提示信息做以修改，去除：大于最大值/小于最小值之分
			// 因为加入无穷大的值域时，当对话框为空时，也会触发提示按钮，此时统一显示为超出值域范围就好
			// 之前判断写得有问题，参考整型类型的判断方法
			if (startValue == -(Double.MAX_VALUE) || endValue == Double.MAX_VALUE) {
				if (startValue == -(Double.MAX_VALUE) && endValue == Double.MAX_VALUE) {
					this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatWarning"), floatLength, "(" + "-∞" + "," + "+∞" + ")"));
				} else if (startValue == -(Double.MAX_VALUE)) {
					this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatWarning"), floatLength, "(" + "-∞" + "," + format.format(endValue) + "]"));

				} else if (endValue == Double.MAX_VALUE) {
					this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatWarning"), floatLength, "[" + format.format(startValue) + "," + "+∞" + ")"));
				}
			} else {
				this.labelWarning.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_FloatWarning"), floatLength, "[" + format.format(startValue) + "," + format.format(endValue) + "]"));
			}

			// 添加一种类型，目前用于地图输出为图片中剩余内存警告--yuanR 2017.3.15
		} else if (this.type == MEMORY_LABEL_TYPE && !StringUtilities.isNullOrEmpty(customTipText)) {
			if (currentValue > this.endValue) {
				this.labelWarning.setToolTipText(this.customTipText);
			}
		}

		this.labelWarning.setPreferredSize(new Dimension(23, 23));
		// 每次设置完范围，当即做一次是否超限的判断
		if (!SymbolSpinnerUtilties.isLegitNumber(startValue, endValue, text)) {
			labelWarning.setText("");
			labelWarning.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
		} else if (!StringUtilities.isNullOrEmpty(text) && StringUtilities.isNumber(text)) {
			labelWarning.setText(null);
			//当不需要警告图标时，设置一个透明的图标--yuanR 2017.3.27
			labelWarning.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/clarity.png"));
			fireListener(text);
		}
	}

	/**
	 * 自定义提示信息
	 * yuanR 2017.3.15
	 */
	public void setCustomLabelToolTipText(String tipText) {
		this.customTipText = tipText;
	}

	private void initComponents() {
		this.labelWarning = new JLabel();
		this.textField = new JFormattedTextField();
		this.textField.setPreferredSize(new Dimension(100, 23));
		this.listeners = new ArrayList();
		this.textField.setText(defaultValue);
		this.labelWarning.setText(" ");
		ComponentUIUtilities.setName(this.textField,"WaringTextField_textField");
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		this.setLayout(groupLayout);

		// @formatter off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.labelWarning)
				.addComponent(this.textField));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(this.labelWarning)
				.addComponent(this.textField));
		// @formatter on

	}

	public void addRightValueListener(RightValueListener listener) {
		if (null != listener && !listeners.contains(listener)) {
			listeners.add(listener);
		}
		registEvents();
	}

	public void removeRightValueListener(RightValueListener listener) {
		if (null != listener && !listeners.contains(listener)) {
			listeners.remove(listener);
		}
		registEvents();
	}

	private void fireListener(String value) {
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			if (null != listeners.get(i) && listeners.get(i) instanceof RightValueListener) {
				if (this.isNeedDisplayAThousandPoints) {
					value = backNumberFromFormat(value);
				}
				((RightValueListener) listeners.get(i)).update(value);
			}
		}
	}

	public void registEvents() {
		removeEvents();
		this.textField.addCaretListener(this.caretListener);
		this.textField.addKeyListener(this.keyAdapter);
	}

	public void removeEvents() {
		this.textField.removeCaretListener(this.caretListener);
		this.textField.removeKeyListener(this.keyAdapter);
	}

	public JTextField getTextField() {
		return textField;
	}


	public void setText(String str) {
		if (this.isNeedDisplayAThousandPoints) {
			str = backNumberFromFormat(str);
			str = numberFormat(Double.valueOf(str));
		}
		textField.setText(str);
	}


	public void setEnable(boolean enable) {
		this.labelWarning.setEnabled(enable);
		this.textField.setEnabled(enable);
	}

	//    供外部调用判断当前的textfield输入内容是否合法
	public boolean isError() {
		if (this.labelWarning.getIcon() != null) {
			return true;
		} else {
			return false;
		}
	}

	public JLabel getLabelWarning() {
		return labelWarning;
	}

	public String getText() {
		String result = this.textField.getText();
		if (this.isNeedDisplayAThousandPoints) {
			result = backNumberFromFormat(result);
		}
		return result;
	}

	//Display data in a thousand bit format   将数据以千分位形式显示   by  lixiaoyao
	private String numberFormat(double temp) {
		String tempStr = String.valueOf(temp);
		String integerValue = "";
		String floatValue = "";
		String result = "";
		if (tempStr.indexOf(".") != -1) {
			String[] splitValue = tempStr.split("\\.");
			integerValue = splitValue[0];
			floatValue = splitValue[1];
			if (!floatValue.equals("0")) {
				result = COMMA_FORMAT.format(Integer.valueOf(integerValue)) + "." + floatValue;
			} else {
				result = COMMA_FORMAT.format(Integer.valueOf(integerValue));
			}
		} else {
			result = COMMA_FORMAT.format(Integer.valueOf(tempStr));
		}
		if (Double.compare(temp, 0) == -1 && result.indexOf("-") == -1) {
			result = "-" + result;
		}
		return result;
	}


	//Thousand bit format elimination          消除千分位格式        by  李逍遥
	private String backNumberFromFormat(String text) {
		String result = text.replace(",", "");
		return result;
	}
}
