package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public abstract class SmDialog extends JDialog implements WindowListener {

	/**
	 * 将要实现tab键切换顺序的控件添加到容器中
	 * 
	 * @return
	 */
	protected ArrayList<Component> componentList = new ArrayList<Component>();

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
				// esc键默认实现关闭事件
				dispose();
			}
		}, strokeForESC, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke strokForEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取选中的按钮，若没有选中的按钮则通过doClick()方法来触发按钮绑定的事件
				if (null != getRootPane().getDefaultButton()) {

					getRootPane().getDefaultButton().doClick();
				}
			}
		}, strokForEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	/**
	 * tab键切换事件
	 */
	protected FocusTraversalPolicy policy = new FocusTraversalPolicy() {
		public Component getFirstComponent(Container focusCycleRoot) {
			return componentList.get(0);
		}

		public Component getLastComponent(Container focusCycleRoot) {
			return componentList.get(componentList.size() - 1);
		}

		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			int index = componentList.indexOf(aComponent);

			return componentList.get((index + 1) % componentList.size());
		}

		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			int index = componentList.indexOf(aComponent);
			return componentList.get((index - 1 + componentList.size()) % componentList.size());
		}

		public Component getDefaultComponent(Container focusCycleRoot) {
			return componentList.get(0);
		}
	};

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
