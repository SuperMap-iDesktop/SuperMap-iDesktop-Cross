package com.supermap.desktop.mapview.map.propertycontrols;

import javax.swing.*;

import com.supermap.desktop.ui.controls.SmDialog;

public class ScaleEnabledContainer extends SmDialog {
	private JToolBar toolbar;
	private JButton buttonAddScale;
	private JButton buttonSelectAll;
	private JButton buttonInvertSelect;
	private JButton buttonDelete;
	private JButton buttonImport;
	private JButton buttonExport;
	public ScaleEnabledContainer(){
		
	}
	
	private void initComponent(){
		this.toolbar = new JToolBar();
		initToolBar();
	}

	private void initToolBar() {
		this.buttonAddScale = new JButton();
//		this.buttonAddScale.setIcon();
	}
	
	
}

