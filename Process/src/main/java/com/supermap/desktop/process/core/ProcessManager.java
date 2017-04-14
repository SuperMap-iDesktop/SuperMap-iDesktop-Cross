package com.supermap.desktop.process.core;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.dataconversion.MetaProcessImportFactory;
import com.supermap.desktop.process.diagram.ui.ProcessTree;
import com.supermap.desktop.process.diagram.ui.ProcessTreeNode;
import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.meta.metaProcessImplements.*;
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

        ProcessGroup importGroup = new ProcessGroup(standAloneGroup);
        importGroup.setKey(ProcessProperties.getString("String_Import"));
        importGroup.setIconPath("/processresources/Tree_Node1.png");
        createImportGroup(importGroup);

        ProcessGroup interpolatorGroup = new ProcessGroup(standAloneGroup);
        interpolatorGroup.setKey(ProcessProperties.getString("String_Interpolator"));
        interpolatorGroup.setIconPath("/processresources/Tree_Node1.png");
        createInterpolatorGroup(interpolatorGroup);

        ProcessGroup overlayAnalystGroup = new ProcessGroup(standAloneGroup);
        overlayAnalystGroup.setKey(ProcessProperties.getString("String_OverlayAnalyst"));
        overlayAnalystGroup.setIconPath("/processresources/Tree_Node1.png");
        createOverlayAnalystGroup(overlayAnalystGroup);


        ProcessGroup surfaceAnalystGroup = new ProcessGroup(standAloneGroup);
        surfaceAnalystGroup.setKey(ProcessProperties.getString("String_SurfaceAnalyst"));
        surfaceAnalystGroup.setIconPath("/processresources/Tree_Node1.png");
        createSurfaceAnaylstGroup(surfaceAnalystGroup);

        standAloneGroup.addProcess(importGroup);
        standAloneGroup.addProcess(interpolatorGroup);
        standAloneGroup.addProcess(overlayAnalystGroup);
        standAloneGroup.addProcess(surfaceAnalystGroup);
        standAloneGroup.addProcess(new MetaProcessBuffer());
        standAloneGroup.addProcess(new MetaProcessProjection());
        standAloneGroup.addProcess(new MetaProcessSpatialIndex());
        standAloneGroup.addProcess(new MetaProcessSqlQuery());

        root.addProcess(standAloneGroup);
        root.addProcess(online);
        return root;
    }

    private void createSurfaceAnaylstGroup(ProcessGroup surfaceAnalystGroup) {
        surfaceAnalystGroup.addProcess(new MetaProcessISOLine());
        surfaceAnalystGroup.addProcess(new MetaProcessISORegion());
        surfaceAnalystGroup.addProcess(new MetaProcessISOPoint());
    }

    private void createOverlayAnalystGroup(ProcessGroup overlayAnalystGroup) {
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.CLIP));
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.UNION));
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.ERASE));
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.IDENTITY));
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.INTERSECT));
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.UPDATE));
        overlayAnalystGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.XOR));
    }

    private void createInterpolatorGroup(ProcessGroup interpolatorGroup) {
        interpolatorGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.IDW));
        interpolatorGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.RBF));
        interpolatorGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.KRIGING));
        interpolatorGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.SimpleKRIGING));
        interpolatorGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.UniversalKRIGING));
    }

    private void createImportGroup(ProcessGroup importGroup) {
        ProcessGroup autoCADType = new ProcessGroup(importGroup);
        autoCADType.setKey(ProcessProperties.getString("String_filetype_Autocad"));
        autoCADType.setIconPath("/processresources/Tree_Node2.png");
        autoCADType.addProcess(MetaProcessImportFactory.createMetaProcessImport("DXF"));
        autoCADType.addProcess(MetaProcessImportFactory.createMetaProcessImport("DWG"));
        ProcessGroup arcGISType = new ProcessGroup(importGroup);
        arcGISType.setKey(ProcessProperties.getString("String_filetype_ArcGIS"));
        arcGISType.setIconPath("/processresources/Tree_Node2.png");
        arcGISType.addProcess(MetaProcessImportFactory.createMetaProcessImport("SHP"));
        arcGISType.addProcess(MetaProcessImportFactory.createMetaProcessImport("GRD"));
        arcGISType.addProcess(MetaProcessImportFactory.createMetaProcessImport("TXT"));
        arcGISType.addProcess(MetaProcessImportFactory.createMetaProcessImport("E00"));
        arcGISType.addProcess(MetaProcessImportFactory.createMetaProcessImport("DEM"));
        arcGISType.addProcess(MetaProcessImportFactory.createMetaProcessImport("DBF"));
        importGroup.addProcess(autoCADType);
        importGroup.addProcess(arcGISType);
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
