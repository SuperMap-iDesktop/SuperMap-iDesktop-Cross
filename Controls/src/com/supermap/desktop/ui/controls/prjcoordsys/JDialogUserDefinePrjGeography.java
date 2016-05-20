package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.GeoCoordSys;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
public class JDialogUserDefinePrjGeography extends SmDialog {

	private JPanelGeoCoordSys panelGeoCoordSys = new JPanelGeoCoordSys();
	private JPanel panelButtons = new JPanel();
	private SmButton buttonOk = new SmButton();
	private SmButton buttonCancle = new SmButton();


	public JDialogUserDefinePrjGeography() {
		super();
		init();
	}

	private void init() {
		initComponents();
		addListeners();
		initLayout();
		initResources();
		initComponentStates();
	}

	private void initComponents() {
		this.setSize(500, 550);
		this.setLocationRelativeTo(null);
		this.setTitle(ControlsProperties.getString("String_UserDefined_GeoCoordSys"));
	}

	private void addListeners() {
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClicked();
			}
		});

		buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});
	}

	private void buttonOkClicked() {
		// TODO: 2016/5/17
		dialogResult = DialogResult.OK;
	}

	public GeoCoordSys getGeoCoordSys() {
		return panelGeoCoordSys.getGeoCoordSys();
	}

	//region 初始化布局
	private void initLayout() {
		initPanelButtons();
		this.setLayout(new GridBagLayout());
		this.add(panelGeoCoordSys, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(panelButtons, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initPanelButtons() {
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 0));
		panelButtons.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 10));
	}
	//endregion

	private void initResources() {
		buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initComponentStates() {

	}

}
