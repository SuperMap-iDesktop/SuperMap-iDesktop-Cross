package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Enum;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterEnum;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

/**
 * @author XiaJT
 */
public class ParameterEnumPanel extends JPanel {

	private ParameterEnum parameterEnum;
	// 防止多次触发事件
	private boolean isSelectingItem = false;

	private JLabel label = new JLabel();
	private JComboBox comboBox = new JComboBox();

	public ParameterEnumPanel(ParameterEnum parameterEnum) {
		this.parameterEnum = parameterEnum;

		label.setText(parameterEnum.getDescribe());
		// todo 枚举资源化如何实现
//		comboBox.setRenderer();
		initComboBoxItems();
		comboBox.setSelectedItem(parameterEnum.getSelectedItem());
		initListeners();
		initLayout();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		comboBox.setPreferredSize(new Dimension(20, 23));
		this.setLayout(new GridBagLayout());
		this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		this.add(comboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
	}

	/**
	 * 根据class添加comboBox子项
	 */
	private void initComboBoxItems() {
		boolean isSuperMapEnum = false;
		Class enumClass = parameterEnum.getEnumClass();
		Class[] classes = enumClass.getClasses();
		if (classes.length > 0) {
			for (Class aClass : classes) {
				if (aClass == Enum.class) {
					isSuperMapEnum = true;
					break;
				}
			}
		}
		if (isSuperMapEnum) {
			try {
				Method getEnums = enumClass.getMethod("getEnums", Class.class);
				Enum[] enums = (Enum[]) getEnums.invoke(enumClass);
				for (Enum anEnum : enums) {
					comboBox.addItem(anEnum);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		} else if (enumClass.isEnum()) {
			Object[] enumConstants = enumClass.getEnumConstants();
			if (enumConstants != null && enumConstants.length > 0) {
				for (Object anEnum : enumConstants) {
					comboBox.addItem(anEnum);
				}
			}
		}
	}

	private void initListeners() {
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					ParameterEnumPanel.this.parameterEnum.setSelectedItem(comboBox.getSelectedItem());
					isSelectingItem = false;
				}
			}
		});
		parameterEnum.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					ParameterEnumPanel.this.comboBox.setSelectedItem(evt.getNewValue());
					isSelectingItem = false;
				}
			}
		});
	}
}
