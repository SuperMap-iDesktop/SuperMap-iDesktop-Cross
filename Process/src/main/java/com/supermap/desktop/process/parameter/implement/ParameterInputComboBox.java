package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.IGraphConnection;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;
import com.supermap.desktop.process.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.process.graphics.events.GraphRemovingListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class ParameterInputComboBox extends ParameterComboBox {
//	private Type type;
//	private boolean isLoadedItems = false;
//	private boolean isDeleting = false;
//	private String inputDataName;
//
//
//	public ParameterInputComboBox(Type type, String inputDataName) {
//		super();
//		this.type = type;
//		this.inputDataName = inputDataName;
//		this.setDescribe(inputDataName + ":");
//		setIConGetter(new IConGetter() {
//			private Icon icon = ProcessResources.getIcon("/processresources/ProcessOutputIcon.png");
//
//			@Override
//			public Icon getICon(ParameterDataNode parameterDataNode) {
//				return icon;
//			}
//		});
//	}
//
//	@Override
//	public IParameterPanel getParameterPanel() {
//		if (!isLoadedItems) {
//			isLoadedItems = true;
//			loadItems();
//			initListener();
//			initSelectedItem();
//		}
//		return super.getParameterPanel();
//	}
//
//	private void initListener() {
//		IForm activeForm = Application.getActiveApplication().getActiveForm();
//		if (activeForm instanceof FormWorkflow) {
//			GraphCanvas canvas = ((FormWorkflow) activeForm).getCanvas();
//			canvas.addGraphCreatedListener(new GraphCreatedListener() {
//				@Override
//				public void graphCreated(GraphCreatedEvent e) {
//					if (e.getGraph() instanceof OutputGraph && ((OutputGraph) e.getGraph()).getProcessGraph().getProcess() != parameters.getProcess()
//							&& type.intersects(((OutputGraph) e.getGraph()).getProcessData().getType())) {// 不一定都是OutputGraph
//						addItem(new ParameterDataNode(((OutputGraph) e.getGraph()).getProcessGraph().getTitle() + "_" + ((OutputGraph) e.getGraph()).getTitle(), e.getGraph()));
//					}
//				}
//			});
//			canvas.addGraphRemovingListener(new GraphRemovingListener() {
//				@Override
//				public void graphRemoving(GraphRemovingEvent e) {
//					isDeleting = true;
//					try {
//						IGraph graph = e.getGraph();
//						if (graph instanceof OutputGraph) {
//							removeItem(e.getGraph());
//						}
//					} catch (Exception e1) {
//						Application.getActiveApplication().getOutput().output(e1);
//					} finally {
//						isDeleting = false;
//					}
//				}
//			});
//		}
//	}
//
//	private void loadItems() {
//		IForm form = Application.getActiveApplication().getActiveForm();
//		if (!(form instanceof FormWorkflow)) {
//			return;
//		}
//		FormWorkflow activeForm = (FormWorkflow) form;
//		ArrayList<IGraph> allDataNode = activeForm.getAllDataNode(type);
//		for (IGraph graph : allDataNode) {
//			if (((OutputGraph) graph).getProcessGraph().getProcess() != this.parameters.getProcess())
//				addItem(new ParameterDataNode(((OutputGraph) graph).getProcessGraph().getTitle() + "_" + ((OutputGraph) graph).getTitle(), graph));
//		}
//	}
//
//	private void initSelectedItem() {
//		IForm activeForm = Application.getActiveApplication().getActiveForm();
//		if (activeForm != null && activeForm instanceof FormWorkflow) {
//			IGraph currentGraph = getCurrentGraph();
//			if (currentGraph != null) {
//				GraphCanvas canvas = ((FormWorkflow) activeForm).getCanvas();
//				IGraphConnection[] connections = canvas.getConnection().getConnections();
//				for (IGraphConnection connection : connections) {
//					boolean isSelected = false;
//					if (connection.getEndGraph() == currentGraph && connection.getMessage().equals(inputDataName)) {
//						ArrayList<ParameterDataNode> items = getItems();
//						for (ParameterDataNode item : items) {
//							if (item.getData() == connection.getStartGraph()) {
//								this.value = item;
//								isSelected = true;
//								break;
//							}
//						}
//					}
//					if (isSelected) {
//						break;
//					}
//				}
//			}
//		}
//
//	}
//
//	public void setSelectedGraph(IGraph startGraph) {
//		ArrayList<ParameterDataNode> items = getItems();
//		for (ParameterDataNode item : items) {
//			if (item.getData() == startGraph) {
//				this.setSelectedItem(item);
//			}
//		}
//	}
//
//	private IGraph getCurrentGraph() {
//		IForm activeForm = Application.getActiveApplication().getActiveForm();
//		if (activeForm != null && activeForm instanceof FormWorkflow) {
//			IGraph[] graphs = ((FormWorkflow) activeForm).getCanvas().getGraphStorage().getGraphs();
//			for (IGraph graph : graphs) {
//				if (graph instanceof ProcessGraph && ((ProcessGraph) graph).getProcess() == parameters.getProcess()) {
//					return graph;
//				}
//			}
//		}
//		return null;
//	}
//
//
//	public boolean isDeleting() {
//		return isDeleting;
//	}
//
//	public String getInputDataName() {
//		return inputDataName;
//	}
}
