package com.supermap.desktop.controls.property;

import com.supermap.data.PrjCoordSysType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysTranslator;
import com.supermap.desktop.utilties.PrjCoordSysUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author highsad
 */
public class PrjCoordSysPropertyControl extends AbstractPropertyControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_COMPONENT_WIDTH = 120;
	private static final int DEFAULT_BUTTON_WIDTH = 75;

	private JLabel labelName;
	private JTextField textFieldName;
	private JLabel labelCoordUnit;
	private JTextField textFieldCoordUnit;
	private JLabel labelCoordInfo;
	private JTextArea textAreaCoordInfo;
	private SmButton buttonExport;
	private SmButton buttonCopy;
	private SmButton buttonSet;
	private SmButton buttonConvert;

	private boolean covertFlag;
	private transient PrjCoordSysHandle prjHandle;
	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (covertFlag) {
				if (e.getSource() == buttonExport) {
					throw new UnsupportedOperationException();
				} else if (e.getSource() == buttonCopy) {
					throw new UnsupportedOperationException();
				} else if (e.getSource() == buttonSet) {
					buttonSetClicked();
				} else if (e.getSource() == buttonConvert) {
					buttonConvertClicked();
				}
			}
		}
	};

	/**
	 * @param prjHandle
	 * @param covertFlag 设置是否可以使用投影相关的功。只读数据源不能更改投影。
	 */
	public PrjCoordSysPropertyControl(PrjCoordSysHandle prjHandle, boolean covertFlag) {
		super(ControlsProperties.getString("String_ProjectionInfo"));
		initializeComponents();
		initializeResources();
		registerEvents();
		setPrjCoordSys(prjHandle, covertFlag);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.PRJCOORDSYS;
	}

	private void initializeComponents() {
		this.labelName = new JLabel("Name:");
		this.textFieldName = new JTextField();
		this.textFieldName.setEditable(false);
		this.labelCoordUnit = new JLabel("CoordUnit:");
		this.textFieldCoordUnit = new JTextField();
		this.textFieldCoordUnit.setEditable(false);
		this.labelCoordInfo = new JLabel("CoordInfo:");
		this.textAreaCoordInfo = new JTextArea();
		this.textAreaCoordInfo.setEditable(false);
		this.buttonExport = new SmButton("Export Coord...");
		this.buttonExport.setVisible(false); // 关闭投影导出的功能，后续实现之后再开放
		this.buttonCopy = new SmButton("Copy Coord...");
		this.buttonCopy.setVisible(false); // 关闭投影复制的功能，后续实现之后再开放
		this.buttonSet = new SmButton("Set Coord..");
		this.buttonConvert = new SmButton("Convert ProjectionSystem...");

		JScrollPane scrollPane = new JScrollPane(this.textAreaCoordInfo);
		scrollPane.setBorder(MetalBorders.getTextFieldBorder());

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.labelName)
						.addComponent(this.textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.labelCoordUnit)
						.addComponent(this.textFieldCoordUnit, GroupLayout.PREFERRED_SIZE, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH))
				.addComponent(this.labelCoordInfo)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.buttonExport, DEFAULT_BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonCopy, DEFAULT_BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonSet, DEFAULT_BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonConvert, DEFAULT_BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelName)
						.addComponent(this.textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelCoordUnit)
						.addComponent(this.textFieldCoordUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.labelCoordInfo)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonExport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonCopy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonConvert, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on
	}

	private void initializeResources() {
		this.labelName.setText(ControlsProperties.getString("String_Message_CoordSysName"));
		this.labelCoordUnit.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
		this.labelCoordInfo.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelProjectionInfo"));
		this.buttonExport.setText(ControlsProperties.getString("String_ProjectionInfoControl_ButtonOutputProjectionInfo"));
		this.buttonCopy.setText(ControlsProperties.getString("String_ButtonCopyCoordSys"));
		this.buttonSet.setText(ControlsProperties.getString("String_ProjectionInfoControl_ButtonResetProjectionInfo"));
		this.buttonConvert.setText(ControlsProperties.getString("String_ProjectionInfoControl_ButtonProjectionConversion"));
	}

	public void initButtonState() {
		// 目前看来，复制，转换，设置是否能使用是一致的。
		this.buttonConvert.setEnabled(covertFlag);
		this.buttonCopy.setEnabled(covertFlag);
		this.buttonSet.setEnabled(covertFlag);
	}

	public PrjCoordSysHandle getPrjCoordSysHandle() {
		return this.prjHandle;
	}

	public void setPrjCoordSys(PrjCoordSysHandle prjHandle, boolean covertFlag) {
		this.prjHandle = prjHandle;
		this.covertFlag = covertFlag;
		fillComponents();
	}

	@Override
	public void refreshData() {
		setPrjCoordSys(this.prjHandle, this.covertFlag);
	}

	private void fillComponents() {
		this.textFieldName.setText(this.prjHandle.getPrj().getName());
		this.textFieldCoordUnit.setText(this.prjHandle.getPrj().getCoordUnit().toString());
		this.textAreaCoordInfo.setText(PrjCoordSysUtilties.getDescription(this.prjHandle.getPrj()));
		setControlsEnabled();
	}

	private void registerEvents() {
		this.buttonSet.addActionListener(this.actionListener);
		this.buttonConvert.addActionListener(this.actionListener);
	}

	private void buttonSetClicked() {
		JDialogPrjCoordSysSettings prjSettings = new JDialogPrjCoordSysSettings();
		prjSettings.setPrjCoordSys(prjHandle.getPrj());
		if (prjSettings.showDialog() == DialogResult.OK) {
			prjHandle.change(prjSettings.getPrjCoordSys());
			fillComponents();
		}
	}

	private void buttonConvertClicked() {
		JDialogPrjCoordSysTranslator dialogTranslator = new JDialogPrjCoordSysTranslator(prjHandle.getPrj());
		if (dialogTranslator.showDialog() == DialogResult.OK) {
			this.prjHandle.convert(dialogTranslator.getMethod(), dialogTranslator.getParameter(), dialogTranslator.getTargetPrj());
			fillComponents();
		}
	}

	private void setControlsEnabled() {
		if (!covertFlag) {
			this.buttonConvert.setEnabled(false);
			this.buttonCopy.setEnabled(false);
			this.buttonSet.setEnabled(false);
		} else {
			this.buttonCopy.setEnabled(true);
			this.buttonSet.setEnabled(true);
			if (this.prjHandle.getPrj().getType() == PrjCoordSysType.PCS_NON_EARTH) {
				this.buttonConvert.setEnabled(false);
			} else {
				this.buttonConvert.setEnabled(true);
			}
		}
	}
}
