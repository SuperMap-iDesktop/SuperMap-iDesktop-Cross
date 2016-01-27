package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SmDialog extends JDialog implements WindowListener {

	public SmDialog() {
		super((Frame) Application.getActiveApplication().getMainFrame(), true);
		this.addWindowListener(this);
	}

	public SmDialog(JFrame owner) {
		super(owner, false);
		this.addWindowListener(this);
	}

	public SmDialog(JDialog owner) {
		super(owner, false);
		this.addWindowListener(this);
	}

	public SmDialog(JFrame owner, boolean modal) {
		super(owner, modal);
		this.addWindowListener(this);
	}

	public SmDialog(JDialog owner, boolean modal) {
		super(owner, modal);
		this.addWindowListener(this);
	}

	public DialogResult showDialog() {
		this.setDialogResult(DialogResult.APPLY);
		this.setVisible(true);
		return this.getDialogResult();
	}

	protected transient DialogResult dialogResult = DialogResult.APPLY;

	public DialogResult getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(DialogResult dialogResult) {
		this.dialogResult = dialogResult;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// do nothing
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// do nothing
	}

}
