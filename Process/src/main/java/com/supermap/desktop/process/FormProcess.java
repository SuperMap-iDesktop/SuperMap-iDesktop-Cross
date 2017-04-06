package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.*;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.GraphSelectChangedListener;
import com.supermap.desktop.process.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.ScrollGraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;
import com.supermap.desktop.process.graphics.graphs.DataGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.graphs.RectangleGraph;
import com.supermap.desktop.process.graphics.interaction.canvas.Selection;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.Dockbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by highsad on 2017/1/6.
 */
public class FormProcess extends FormBaseChild implements IForm {
	private ScrollGraphCanvas graphCanvas = new ScrollGraphCanvas();


	public FormProcess() {
		super("", null, null);
		setLayout(new BorderLayout());
		add(graphCanvas, BorderLayout.CENTER);
		graphCanvas.getCanvas().getSelection().addGraphSelectChangedListener(new GraphSelectChangedListener() {

			@Override
			public void graphSelectChanged(GraphSelectedChangedEvent e) {
				try {
					ParameterManager component = (ParameterManager) ((Dockbar) Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName("com.supermap.desktop.process.ParameterManager"))).getInnerComponent();
					Selection selection = e.getSelection();
					if (selection.getItem(0) instanceof ProcessGraph) {
						component.setProcess(((ProcessGraph) selection.getItem(0)).getProcess());
					} else {
						component.setProcess(null);
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		graphCanvas.getCanvas().addGraphCreatedListener(new GraphCreatedListener() {
			@Override
			public void graphCreated(GraphCreatedEvent e) {
				if (e.getGraph() instanceof ProcessGraph) {
					ProcessGraph processGraph = (ProcessGraph) e.getGraph();
					IProcess process = processGraph.getProcess();

					int gap = 20;
					OutputData[] outputs = process.getOutputs().getDatas();
					int length = outputs.length;
					DataGraph[] dataGraphs = new DataGraph[length];
					int totalHeight = gap * (length - 1);

					for (int i = 0; i < length; i++) {
						dataGraphs[i] = new DataGraph(graphCanvas.getCanvas(), outputs[i]);
						graphCanvas.getCanvas().addGraph(dataGraphs[i]);
						graphCanvas.getCanvas().getConnection().connect(processGraph, dataGraphs[i]);
						totalHeight += dataGraphs[i].getHeight();
					}

					int locationX = processGraph.getLocation().x + processGraph.getWidth() * 3 / 2;
					int locationY = processGraph.getLocation().y + (processGraph.getHeight() - totalHeight) / 2;
					for (int i = 0; i < length; i++) {
						dataGraphs[i].setLocation(new Point(locationX, locationY));
						locationY = dataGraphs[i].getHeight() + gap;
					}
					graphCanvas.getCanvas().repaint();
				}
			}
		});
	}

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(1000, 650);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new FormProcess(), BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	//region ignore
	@Override
	public String getText() {
		return "工作流";
	}

	@Override
	public void setText(String text) {

	}

	@Override
	public WindowType getWindowType() {
		return WindowType.UNKNOWN;
	}

	@Override
	public boolean save() {
		return false;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean isNeedSave() {
		return false;
	}

	@Override
	public void setNeedSave(boolean needSave) {

	}

	@Override
	public boolean isActivated() {
		return false;
	}

	@Override
	public void actived() {

	}

	@Override
	public void deactived() {

	}

	@Override
	public void formShown(FormShownEvent e) {

	}

	@Override
	public void formClosing(FormClosingEvent e) {

	}

	@Override
	public void formClosed(FormClosedEvent e) {

	}

	@Override
	public void addFormActivatedListener(FormActivatedListener listener) {

	}

	@Override
	public void removeFormActivatedListener(FormActivatedListener listener) {

	}

	@Override
	public void addFormDeactivatedListener(FormDeactivatedListener listener) {

	}

	@Override
	public void removeFormDeactivatedListener(FormDeactivatedListener listener) {

	}

	@Override
	public void addFormClosingListener(FormClosingListener listener) {

	}

	@Override
	public void removeFormClosingListener(FormClosingListener listener) {

	}

	@Override
	public void addFormClosedListener(FormClosedListener listener) {

	}

	@Override
	public void removeFormClosedListener(FormClosedListener listener) {

	}

	@Override
	public void addFormShownListener(FormShownListener listener) {

	}

	@Override
	public void removeFormShownListener(FormShownListener listener) {

	}

	@Override
	public void clean() {

	}

	@Override
	public boolean isClosed() {
		return false;
	}

	public GraphCanvas getCanvas() {
		return this.graphCanvas.getCanvas();
	}

	public void addProcess(IProcess process) {
		RectangleGraph graph = new ProcessGraph(graphCanvas.getCanvas(), process);
		graphCanvas.getCanvas().create(graph);
	}
	//endregion
}
