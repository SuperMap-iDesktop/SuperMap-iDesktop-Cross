package com.supermap.desktop.dialog.cacheClip;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;

/**
 * Created by xie on 2017/6/12.
 */
public class DialogCacheUpdate extends SmDialog {
	private JLabel labelTip;
	private JFileChooserControl sciFileChooserControl;
	private JButton buttonOK;
	private JButton buttonCancel;

	public DialogCacheUpdate() {
		init();
	}

	private void init() {
		this.labelTip = new JLabel();
		this.sciFileChooserControl = new JFileChooserControl();
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
	}
}
