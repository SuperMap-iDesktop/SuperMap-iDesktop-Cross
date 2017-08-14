package com.supermap.desktop.process.ui;

import com.supermap.desktop.process.ProcessManager;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.IProcessGroup;
import com.supermap.desktop.process.loader.IProcessLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/8/12.
 */
public class ToolBoxMenu extends JMenu {

	public ToolBoxMenu() {
		super("工具箱");
		load();
	}

	private void load() {
		loadGroup(this, ProcessManager.INSTANCE);
	}

	private void loadGroup(JMenu menu, IProcessGroup group) {
		if (group == null || menu == null) {
			return;
		}

		IProcessGroup[] childGroups = group.getGroups();
		for (int i = 0; i < childGroups.length; i++) {
			JMenu groupMenu = new JMenu(childGroups[i].getTitle());
			menu.add(groupMenu);
			loadGroup(groupMenu, childGroups[i]);
		}

		IProcessLoader[] childProcesses = group.getProcesses();
		for (int i = 0; i < childProcesses.length; i++) {
			JMenuItem item = new JMenuItem(childProcesses[i].getProcessDescriptor().getTitle());
			item.addActionListener(new ProcessMenuItemActionListener(childProcesses[i]));
			menu.add(item);
		}
	}


//	private void loadGroup(JMenu parent, IProcessGroup group) {
//		if (group == null || parent == null) {
//			return;
//		}
//
//		JMenu groupMenu = new JMenu(group.getTitle());
//		parent.add(groupMenu);
//
//		IProcessGroup[] childGroups = group.getGroups();
//		for (int i = 0; i < childGroups.length; i++) {
//			loadGroup(groupMenu, childGroups[i]);
//		}
//
//		IProcessLoader[] childProcesses = group.getProcesses();
//		for (int i = 0; i < childProcesses.length; i++) {
//			JMenuItem item = new JMenuItem(childProcesses[i].getProcessDescriptor().getTitle());
//			item.addActionListener(new ProcessMenuItemActionListener(childProcesses[i]));
//			groupMenu.add(item);
//		}
//	}

	private class ProcessMenuItemActionListener implements ActionListener {
		private IProcessLoader loader;

		public ProcessMenuItemActionListener(IProcessLoader loader) {
			if (loader == null) {
				throw new NullPointerException();
			}

			this.loader = loader;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			IProcess process = this.loader.loadProcess();
			if (process != null) {
				SmDialogProcess smDialogProcess = new SmDialogProcess(process);
				smDialogProcess.showDialog();
			}
		}
	}
}
