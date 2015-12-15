package com.supermap.desktop.ui.controls;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.supermap.desktop.Application;

public class SmDialog extends JDialog implements WindowListener {

	public SmDialog() {
		super((Frame) Application.getActiveApplication().getMainFrame(), true);
		this.addWindowListener((WindowListener) this);
	}

	public SmDialog(JFrame owner) {
		super(owner, false);
		this.addWindowListener((WindowListener) this);
	}

	public SmDialog(JDialog owner) {
		super(owner, false);
		this.addWindowListener((WindowListener) this);
	}

	public SmDialog(JFrame owner, boolean modal) {
		super(owner, modal);
		this.addWindowListener((WindowListener) this);
	}

	public SmDialog(JDialog owner, boolean modal) {
		super(owner, modal);
		this.addWindowListener((WindowListener) this);
	}

	public DialogResult showDialog() {
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
