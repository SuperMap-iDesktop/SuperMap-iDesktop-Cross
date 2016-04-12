package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class SmDialog extends JDialog implements WindowListener {

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
		try {
			this.setDialogResult(DialogResult.APPLY);
			this.setVisible(true);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return this.getDialogResult();
	}

	/**
	 * 覆盖JDialog的createRootPane方 法已达到焦点在子类窗体内部时，点击 Enter，Esc时实现子类自定义的确定， 取消功能
	 *
	 * @return
	 */
	@Override
	protected JRootPane createRootPane() {
		KeyStroke strokeForESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				escapePressed();
			}
		}, strokeForESC, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke strokForEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enterPressed();
			}
		}, strokForEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	@Override
	public void setFocusTraversalPolicy(FocusTraversalPolicy policy) {
		// TODO Auto-generated method stub
		super.setFocusTraversalPolicy(policy);
	}

	/**
	 * 自定义的ESC按键功能
	 */
	public abstract void escapePressed();

	/**
	 * 自定义的ENTER按键功能
	 */
	public abstract void enterPressed();

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
