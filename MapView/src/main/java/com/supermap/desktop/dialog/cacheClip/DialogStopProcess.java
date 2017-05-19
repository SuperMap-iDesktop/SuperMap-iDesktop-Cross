package com.supermap.desktop.dialog.cacheClip;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by xie on 2017/4/25.
 */
public class DialogStopProcess extends SmDialog {
	private JLabel labelWarnning;
	private JRadioButton stopCurrentProcessRightNow;
	private JRadioButton stopCurrentProcessWhenMissionFinished;
	private JButton buttonOk;
	private JButton buttonCancel;
	private boolean stopRightNow;
	private ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialogResult = DialogResult.OK;
			stopRightNow = stopCurrentProcessRightNow.isSelected();
			dispose();
		}
	};
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeEvents();
			dispose();
		}
	};

	public DialogStopProcess() {
		super();
		init();
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
	}

	private void registEvents() {
		removeEvents();
		this.buttonOk.addActionListener(okListener);
		this.buttonCancel.addActionListener(cancelListener);
	}

	private void removeEvents() {
		this.buttonOk.removeActionListener(okListener);
		this.buttonCancel.removeActionListener(cancelListener);
	}

	private void initComponents() {
		this.labelWarnning = new JLabel();
		this.stopCurrentProcessRightNow = new JRadioButton();
		this.stopCurrentProcessWhenMissionFinished = new JRadioButton();
		this.stopCurrentProcessWhenMissionFinished.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(stopCurrentProcessRightNow);
		group.add(stopCurrentProcessWhenMissionFinished);
		this.buttonOk = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.setSize(320, 200);
		this.setLocationRelativeTo(null);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_ProcessStopType"));
		this.labelWarnning.setText(MapViewProperties.getString("String_StopProcessesMethod"));
		this.stopCurrentProcessRightNow.setText(MapViewProperties.getString("String_StopCurrentProcess"));
		this.stopCurrentProcessWhenMissionFinished.setText(MapViewProperties.getString("String_StopCurrentProcessWhenMissionFinished"));
	}

	private void initLayout() {
		JPanel panelContent = (JPanel) this.getContentPane();
		panelContent.setLayout(new GridBagLayout());
		panelContent.add(this.labelWarnning, new GridBagConstraintsHelper(0, 0, 4, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 20, 10, 10).setIpad(0, 10).setWeight(0, 1));
		panelContent.add(this.stopCurrentProcessRightNow, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 5, 10));
		panelContent.add(this.stopCurrentProcessWhenMissionFinished, new GridBagConstraintsHelper(0, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 5, 10));
		panelContent.add(this.buttonOk, new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(1, 0));
		panelContent.add(this.buttonCancel, new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(0, 0));
	}

	public boolean isStopRightNow() {
		return stopRightNow;
	}
}
