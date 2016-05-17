package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class JDialogUserDefinePrjProjection extends SmDialog {

	private JPanelGeoCoordSys panelGeoCoordSys = new JPanelGeoCoordSys();
	private JPanel panelButtons = new JPanel();
	private SmButton buttonOk = new SmButton();
	private SmButton buttonCancle = new SmButton();


	public JDialogUserDefinePrjProjection() {
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
		// TODO: 2016/5/17 初始化
	}

	private void addListeners() {

	}

	//region 初始化布局
	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(panelGeoCoordSys, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(panelButtons, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initPanelGeoDatum() {
	}

	private void initPanelGeoSpheroid() {
	}

	private void initPanelCentralMeridian() {

	}

	private void initPanelButtons() {

	}
	//endregion

	private void initResources() {

	}

	private void initComponentStates() {

	}

}
