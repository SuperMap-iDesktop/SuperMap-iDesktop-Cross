package com.supermap.desktop.process.core;

import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.samples.draw.DrawView;
import org.jhotdraw.samples.draw.DrawingPanel;
import org.jhotdraw.samples.draw.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessManager extends JPanel {

	public static void main(String[] args) {
//		Main.main(args);
		final JFrame frame = new JFrame();
		frame.setSize(1000, 650);

		DrawingPanel view = new DrawingPanel();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(view, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	private Vector<IProcessLoader> loaders = new Vector<>();
	private Vector<IProcess> processes = new Vector<>();
	public static final ProcessManager SINGLETON = new ProcessManager();

	private ProcessManager() {
		registerProcessLoader(DefaultProcessLoader.SINGLETON);
	}

	public void registerProcessLoader(IProcessLoader loader) {
		this.loaders.add(loader);
		addProcesses(loader.loadProcesses());
	}

	public void addProcesses(IProcess[] processes) {
		if (processes != null && processes.length > 0) {
			for (int i = 0; i < processes.length; i++) {
				this.processes.add(processes[i]);
			}
		}
	}
}
