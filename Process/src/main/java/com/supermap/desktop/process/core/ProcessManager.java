package com.supermap.desktop.process.core;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.diagram.ui.ProcessTree;
import com.supermap.desktop.process.diagram.ui.ProcessTreeNode;
import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessBuffer;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessHeatMap;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessISOLine;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessImport;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessKernelDensity;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessProjection;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessSpatialIndex;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessSqlQuery;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.enums.OverlayAnalystType;
import org.jhotdraw.samples.draw.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessManager extends JPanel {

	public ProcessTree processTree;
	public JScrollPane jScrollPane;

	public static void main(String[] args) {
		Main.main(args);
//		final JFrame frame = new JFrame();
//		frame.setSize(1000, 650);
//
//		DrawingPanel view = new DrawingPanel();
//		frame.getContentPane().setLayout(new BorderLayout());
//		frame.getContentPane().add(view, BorderLayout.CENTER);
//
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				frame.setVisible(true);
//			}
//		});
	}

	private Vector<IProcessLoader> loaders = new Vector<>();
	private Vector<IProcess> processes = new Vector<>();
	public static final ProcessManager SINGLETON = new ProcessManager();

	public ProcessManager() {
		initComponents();
		initLayout();
		registerProcessLoader(DefaultProcessLoader.SINGLETON);
	}

	private void initComponents() {
		ProcessGroup processGroup = initProcessGroup();
		this.processTree = new ProcessTree(new ProcessTreeNode(null, processGroup));
		this.jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(processTree);
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}

	private ProcessGroup initProcessGroup() {
		ProcessGroup root = new ProcessGroup(null);
		root.setIconPath("/processresources/Process/root.png");
		root.setKey(ProcessProperties.getString("String_Process"));

		ProcessGroup online = new ProcessGroup(root);
		online.setKey("Online");
		online.setIconPath("/processresources/Process/online.png");
		online.addProcess(new MetaProcessHeatMap());
		online.addProcess(new MetaProcessKernelDensity());

		ProcessGroup standAloneGroup = new ProcessGroup(root);
		standAloneGroup.setIconPath("/processresources/Process/native.png");
		standAloneGroup.setKey(ProcessProperties.getString("String_StandAlone"));
		standAloneGroup.addProcess(new MetaProcessBuffer());
		standAloneGroup.addProcess(new MetaProcessImport());
		standAloneGroup.addProcess(new MetaProcessProjection());
		standAloneGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.KRIGING));
		standAloneGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.INTERSECT));
		standAloneGroup.addProcess(new MetaProcessSpatialIndex());
		standAloneGroup.addProcess(new MetaProcessSqlQuery());
		standAloneGroup.addProcess(new MetaProcessISOLine());
		root.addProcess(standAloneGroup);
		root.addProcess(online);
		return root;
	}

	public void registerProcessLoader(IProcessLoader loader) {
		this.loaders.add(loader);
		addProcesses(loader.loadProcesses());
	}

	public void addProcesses(IProcess... processes) {
		if (processes != null && processes.length > 0) {
			Collections.addAll(this.processes, processes);
		}
	}
}
