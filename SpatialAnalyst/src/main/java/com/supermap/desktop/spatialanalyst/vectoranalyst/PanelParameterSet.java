package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.TextFields.RightValueListener;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 缓冲区分析-参数设置面板
 * yuanR 2017.8.10重构
 * 添加控件間的监听控制
 * 替换“半圆弧线度数”控件
 */
public class PanelParameterSet extends JPanel {


	private static final long serialVersionUID = 1L;

	private JCheckBox checkBoxUnionBuffer;
	private JCheckBox checkBoxRemainAttributes;
	private JCheckBox checkBoxDisplayInMap;
	private JCheckBox checkBoxDisplayInScene;
	private JLabel labelSemicircleLineSegment;
	private WaringTextField textFieldSemicircleLineSegment;

	private final static int DEFAULT_MIN = 4;
	private final static int DEFAULT_MAX = 200;

	public JCheckBox getCheckBoxUnionBuffer() {
		return checkBoxUnionBuffer;
	}

	public JCheckBox getCheckBoxRemainAttributes() {
		return checkBoxRemainAttributes;
	}

	public JCheckBox getCheckBoxDisplayInMap() {
		return checkBoxDisplayInMap;
	}

	public JCheckBox getCheckBoxDisplayInScene() {
		return checkBoxDisplayInScene;
	}

	public WaringTextField getTextFieldSemicircleLineSegment() {
		return textFieldSemicircleLineSegment;
	}

	public PanelParameterSet() {
		initComponent();
		initResources();
		setOtherPanelResultSetLayout();
//		setOtherPanelResultSetLayout();
		registerEvent();
	}

	private void initComponent() {
		this.checkBoxUnionBuffer = new JCheckBox("UnionBuffer");
		this.checkBoxRemainAttributes = new JCheckBox("RemainInAttributes");
		this.checkBoxRemainAttributes.setSelected(true);
		this.checkBoxDisplayInMap = new JCheckBox("DisplayInMap");
		this.checkBoxDisplayInMap.setSelected(true);
		this.checkBoxDisplayInScene = new JCheckBox("DisPlayInScene");
		this.checkBoxDisplayInScene.setVisible(false);
		this.labelSemicircleLineSegment = new JLabel("SemicircleLineSegment");

		this.textFieldSemicircleLineSegment = new WaringTextField("4");
		this.textFieldSemicircleLineSegment.setInitInfo(DEFAULT_MIN, DEFAULT_MAX, WaringTextField.INTEGER_TYPE, "null");

	}

	private void initResources() {
		this.checkBoxUnionBuffer.setText(SpatialAnalystProperties.getString("String_UnionBufferItem"));
		this.checkBoxRemainAttributes.setText(SpatialAnalystProperties.getString("String_RetainAttribute"));
		this.checkBoxDisplayInMap.setText(SpatialAnalystProperties.getString("String_DisplayInMap"));
		this.checkBoxDisplayInScene.setText(SpatialAnalystProperties.getString("String_DisplayInScene"));
		this.labelSemicircleLineSegment.setText(SpatialAnalystProperties.getString("String_Label_SemicircleLineSegment"));
	}

//	private void setPanelResultSetLayout() {
//		this.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_GroupBox_ParamSetting")));
//		GroupLayout panelResultSetLayout = new GroupLayout(this);
//		panelResultSetLayout.setAutoCreateContainerGaps(true);
//		panelResultSetLayout.setAutoCreateGaps(true);
//		this.setLayout(panelResultSetLayout);
//
//		//@formatter:off
//		panelResultSetLayout.setHorizontalGroup(panelResultSetLayout.createSequentialGroup()
//				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.checkBoxUnionBuffer)
//						.addComponent(this.checkBoxDisplayInMap)
//						.addComponent(this.labelSemicircleLineSegment))
//				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.checkBoxRemainAttributes)
//						.addComponent(this.checkBoxDisplayInScene)
//						.addComponent(this.textFieldSemicircleLineSegment,5,5,Short.MAX_VALUE)));
//
//		panelResultSetLayout.setVerticalGroup(panelResultSetLayout.createSequentialGroup()
//				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.checkBoxUnionBuffer)
//						.addComponent(this.checkBoxRemainAttributes))
//				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.checkBoxDisplayInMap)
//						.addComponent(this.checkBoxDisplayInScene))
//				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.BASELINE)
//						.addComponent(this.labelSemicircleLineSegment)
//						.addComponent(this.textFieldSemicircleLineSegment)));
//
//		//@formatter:on
//	}

	public void setOtherPanelResultSetLayout() {
		this.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_GroupBox_ParamSetting")));
		GroupLayout panelResultSetLayout = new GroupLayout(this);
		panelResultSetLayout.setAutoCreateContainerGaps(true);
		panelResultSetLayout.setAutoCreateGaps(true);
		this.setLayout(panelResultSetLayout);

		//@formatter:off
		panelResultSetLayout.setHorizontalGroup(panelResultSetLayout.createSequentialGroup()
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.checkBoxUnionBuffer)
						.addComponent(this.checkBoxDisplayInMap)
						.addComponent(this.checkBoxRemainAttributes)
						.addComponent(this.labelSemicircleLineSegment))
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldSemicircleLineSegment)));

		panelResultSetLayout.setVerticalGroup(panelResultSetLayout.createSequentialGroup()
				.addGroup(panelResultSetLayout.createSequentialGroup()
						.addComponent(this.checkBoxUnionBuffer)
						.addComponent(this.checkBoxDisplayInMap)
						.addComponent(this.checkBoxRemainAttributes))
				.addGroup(panelResultSetLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.labelSemicircleLineSegment)
						.addComponent(this.textFieldSemicircleLineSegment)));
		//@formatter:on
	}

	private void registerEvent() {
		// 合并缓冲区checkBox监听：选中时保留原对象属性checkBox不可用
		checkBoxUnionBuffer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				checkBoxRemainAttributes.setSelected(false);
				checkBoxRemainAttributes.setEnabled(!checkBoxUnionBuffer.isSelected());
			}
		});
		textFieldSemicircleLineSegment.addRightValueListener(new RightValueListener() {
			@Override
			public void update(String value) {

			}
		});
	}

	/**
	 * 创建面板是否可用方法
	 * 2017.3.2 yuanR
	 *
	 * @param isEnable
	 */
	public void setPanelEnable(boolean isEnable) {
		this.checkBoxUnionBuffer.setEnabled(isEnable);
		this.checkBoxRemainAttributes.setEnabled(isEnable);
		this.checkBoxDisplayInMap.setEnabled(isEnable);
		this.checkBoxDisplayInScene.setEnabled(isEnable);
		this.textFieldSemicircleLineSegment.setEnabled(isEnable);
	}
}