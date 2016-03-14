package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.data.PrjCoordSys;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.SmTextField;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilties.PrjCoordSysUtilties;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MapPrjCoordSysControl extends AbstractPropertyControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBoxIsDynamicProjection;
	private JButton buttonProjectionSetting;
	private JLabel labelCoordName;
	private JTextField textFieldCoordName;
	private JLabel labelCoordUnit;
	private JTextField textFieldCoordUnit;
	private JTextArea textAreaCoordInfo;

	private int PRJCOORSYS = 3;
	
	private PrjCoordSys currentPrjCoorSys;
	private boolean isDynamicProjection = false;

	private ItemListener checkboxItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			checkBoxIsDynamicProjectionCheckedChange();
		}
	};

	private ActionListener ButtonProjectionSettingListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ButtonProjectionSettingClicked();
		}
	};
	
	/**
	 * Create the panel.
	 */
	public MapPrjCoordSysControl() {
		super(MapViewProperties.getString("String_TabPage_ProjectionSetting"));
	}

	@Override
	public void apply() {
		getMap().setDynamicProjection(isDynamicProjection);
		getMap().setPrjCoordSys(currentPrjCoorSys);
		this.textFieldCoordName.setText(getMap().getPrjCoordSys().getName());
		this.textFieldCoordUnit.setText(getMap().getPrjCoordSys().getCoordUnit().toString());
		this.textAreaCoordInfo.setText(PrjCoordSysUtilties.getDescription(getMap().getPrjCoordSys()));
		IForm form = Application.getActiveApplication().getActiveForm();
		if(form != null && form instanceof FormMap){
			FormMap formMap = (FormMap)form;
			((SmTextField)formMap.getStatusbar().getComponent(PRJCOORSYS)).setText(getMap().getPrjCoordSys().getName());
		}
		getMap().refresh();
	}

	@Override
	protected void initializeComponents() {
		this.checkBoxIsDynamicProjection = new JCheckBox("DynamicProjection");
		this.buttonProjectionSetting = new JButton("ProjectionSetting");
		this.labelCoordName = new JLabel("CoordName:");
		this.textFieldCoordName = new JTextField();
		this.textFieldCoordName.setEditable(false);
		this.labelCoordUnit = new JLabel("CoordUnit:");
		this.textFieldCoordUnit = new JTextField();
		this.textFieldCoordUnit.setEditable(false);
		this.textAreaCoordInfo = new JTextArea();
		this.textAreaCoordInfo.setEditable(false);
		this.textAreaCoordInfo.setBorder(MetalBorders.getTextFieldBorder());

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.checkBoxIsDynamicProjection)
						.addComponent(this.buttonProjectionSetting, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(this.labelCoordName)
				.addComponent(this.textFieldCoordName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.labelCoordUnit)
				.addComponent(this.textFieldCoordUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.textAreaCoordInfo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxIsDynamicProjection)
						.addComponent(this.buttonProjectionSetting, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.labelCoordName)
				.addComponent(this.textFieldCoordName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.labelCoordUnit)
				.addComponent(this.textFieldCoordUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.textAreaCoordInfo, 100, 200, Short.MAX_VALUE));
		// @formatter:on
	}

	@Override
	protected void initializeResources() {
		this.checkBoxIsDynamicProjection.setText(MapViewProperties.getString("String_CheckBox_DynamicProjection"));
		this.buttonProjectionSetting.setText(MapViewProperties.getString("String_Button_ProjectionSetting"));
		this.labelCoordName.setText(MapViewProperties.getString("String_Label_PrjCoordSysName"));
		this.labelCoordUnit.setText(MapViewProperties.getString("String_Label_PrjCoordSysUnit"));
	}

	@Override
	protected void initializePropertyValues(Map map) {
		this.isDynamicProjection = map.isDynamicProjection();
		this.currentPrjCoorSys = map.getPrjCoordSys();
	}

	@Override
	protected void registerEvents() {
		super.registerEvents();
		this.checkBoxIsDynamicProjection.addItemListener(this.checkboxItemListener);
		this.buttonProjectionSetting.addActionListener(this.ButtonProjectionSettingListener);
	}

	@Override
	protected void unregisterEvents() {
		super.unregisterEvents();
		this.checkBoxIsDynamicProjection.removeItemListener(this.checkboxItemListener);
		this.buttonProjectionSetting.removeActionListener(this.ButtonProjectionSettingListener);
	}

	@Override
	protected void fillComponents() {
		this.checkBoxIsDynamicProjection.setSelected(this.isDynamicProjection);
		this.textFieldCoordName.setText(getMap().getPrjCoordSys().getName());
		this.textFieldCoordUnit.setText(getMap().getPrjCoordSys().getCoordUnit().toString());
		this.textAreaCoordInfo.setText(PrjCoordSysUtilties.getDescription(getMap().getPrjCoordSys()));
	}

	@Override
	protected void setComponentsEnabled() {
		this.buttonProjectionSetting.setEnabled(true);
		this.textFieldCoordName.setEditable(false);
		this.textFieldCoordUnit.setEditable(false);
		this.textAreaCoordInfo.setEditable(false);
	}

	@Override
	protected boolean verifyChange() {
		return this.isDynamicProjection != getMap().isDynamicProjection() || this.currentPrjCoorSys != getMap().getPrjCoordSys();
	}

	private void checkBoxIsDynamicProjectionCheckedChange() {
		try {
			this.isDynamicProjection = this.checkBoxIsDynamicProjection.isSelected();
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}
	
	private void ButtonProjectionSettingClicked(){
		try {
			JDialogPrjCoordSysSettings prjSettings = new JDialogPrjCoordSysSettings();
			prjSettings.setPrjCoordSys(currentPrjCoorSys);
			if(prjSettings.showDialog() == DialogResult.OK){
				this.currentPrjCoorSys = prjSettings.getPrjCoordSys();
				verify();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
