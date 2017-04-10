package com.supermap.desktop.process.util;

import com.supermap.desktop.process.core.DirectConnect;
import com.supermap.desktop.process.core.NodeMatrix;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.LineGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author XiaJT
 */
public class WorkFlowXmlUtilties {
	private static WorkFlowXmlUtilties workFlowXmlUtilties;
	public NodeMatrix matrix;
	public Document document;

	private WorkFlowXmlUtilties() {

	}

//	private  Element createProcessesElemet() {
//		Element processes = createProcesses();
//		CopyOnWriteArrayList list = this.matrix.getAllNodes();
//		int nodeSize = list.size();
//		for (int i = 0; i < nodeSize; i++) {
//			Element process = createProcess();
//			if (list.get(i) instanceof IProcess) {
//				process.setAttribute("title", ((IProcess) list.get(i)).getTitle());
//				process.setAttribute("key", ((IProcess) list.get(i)).getKey());
//				Element parametersElement = createParameters();
//				IParameters parameters = ((IProcess) list.get(i)).getParameters();
//				IParameter[] parameterArray = parameters.getParameters();
//				int length = parameterArray.length;
//				for (int j = 0; j < length; j++) {
//					Element parameter = createParameter();
//					parameter.setAttribute("type", parameterArray[j].getType());
//					parameter.setAttribute("describe", parameterArray[j].getDescribe());
//					if (parameterArray[j] instanceof ISelectionParameter && null != ((ISelectionParameter) parameterArray[j]).getSelectedItem()) {
//						if (parameterArray[j].getType().equals(ParameterType.COMBO_BOX)) {
//							parameter.setAttribute("value", ((ParameterDataNode) ((ISelectionParameter) parameterArray[j]).getSelectedItem()).getData().toString());
//						} else {
//							parameter.setAttribute("value", ((ISelectionParameter) parameterArray[j]).getSelectedItem().toString());
//						}
//					} else {
//						parameter.setAttribute("value", "");
//					}
//					parametersElement.appendChild(parameter);
//				}
//				process.appendChild(parametersElement);
//			} else {
//				process.setAttribute("title", list.get(i).toString());
//			}
//			processes.appendChild(process);
//		}
//		return processes;
//	}
//
//	private Element createNodeElements() {
//		Element nodes = createNodes();
//		CopyOnWriteArrayList list = this.matrix.getAllNodes();
//		int nodeSize = list.size();
//		for (int i = 0; i < nodeSize; i++) {
//			Element node = createNode();
//			Element process = createProcess();
//			if (list.get(i) instanceof IProcess) {
//				process.appendChild(this.document.createTextNode(((IProcess) list.get(i)).getKey()));
//			} else {
//				process.appendChild(this.document.createTextNode(list.get(i).toString()));
//			}
//			node.appendChild(process);
//			if (null != list.get(i)) {
//				try {
//					CopyOnWriteArrayList preProcessList = this.matrix.getPreNodes(list.get(i));
//					int preProcessSize = preProcessList.size();
//					for (int j = 0; j < preProcessSize; j++) {
//						Element preProcess = createPreProcess();
//						if (preProcessList.get(j) instanceof IProcess) {
//							preProcess.appendChild(this.document.createTextNode(((IProcess) preProcessList.get(j)).getKey()));
//						} else {
//							preProcess.appendChild(this.document.createTextNode(preProcessList.get(j).toString()));
//						}
//						node.appendChild(preProcess);
//					}
//					CopyOnWriteArrayList nextProcessList = this.matrix.getNextNodes(list.get(i));
//					int nextProcessSize = nextProcessList.size();
//					for (int j = 0; j < nextProcessSize; j++) {
//						Element nextProcess = createNextProcess();
//						if (nextProcessList.get(j) instanceof IProcess) {
//							nextProcess.appendChild(this.document.createTextNode(((IProcess) nextProcessList.get(j)).getKey()));
//						} else {
//							nextProcess.appendChild(this.document.createTextNode(nextProcessList.get(j).toString()));
//						}
//						node.appendChild(nextProcess);
//					}
//				} catch (NodeException e) {
//					Application.getActiveApplication().getOutput().output(e);
//				}
//			}
//			nodes.appendChild(node);
//		}
//		return nodes;
//	}


	/**
	 * Constraint element,your constraint for nodes(How to store constraint determined by yourself);
	 *
	 * @return
	 */
	private Element createConstraint() {
		return document.createElement("Constraint");
	}

	public static String parseToXml(NodeMatrix matrix, String name) {
		Document document = XmlUtilities.getEmptyDocument();
		if (document == null) {
			System.out.println("hehe");
			return "";
		}
		Element nodeMatrix = XmlUtilities.createRoot(document, "WorkFlow");
		nodeMatrix.setAttribute("name", name);
		Element processes = document.createElement("graphs");
		nodeMatrix.appendChild(processes);
		CopyOnWriteArrayList allNodes = matrix.getAllNodes();
		ArrayList<IGraph> arrayList = new ArrayList<>();
		for (Object allNode : allNodes) {
			if (allNode instanceof IGraph && !(allNode instanceof LineGraph)) {
				Element graph = document.createElement("graph");
				graph.setAttribute("value", ((IGraph) allNode).toXml());
				graph.setAttribute("ID", String.valueOf(arrayList.size()));
				arrayList.add((IGraph) allNode);
				processes.appendChild(graph);
			}
		}
		Element connections = document.createElement("connections");
		nodeMatrix.appendChild(connections);
		for (Object allNode : allNodes) {
			if (allNode instanceof IGraph && !(allNode instanceof LineGraph)) {
				CopyOnWriteArrayList nextNodes = matrix.getNextNodes(allNode);
				for (Object node : nextNodes) {
					if (node instanceof IGraph && !(node instanceof LineGraph)) {
						Element connect = document.createElement("connect");
						connect.setAttribute("startId", String.valueOf(arrayList.indexOf(allNode)));
						connect.setAttribute("endId", String.valueOf(arrayList.indexOf(node)));
						if (node instanceof ProcessGraph) {
							Inputs inputs = ((ProcessGraph) node).getProcess().getInputs();
							InputData[] datas = inputs.getDatas();
							for (InputData data : datas) {
								if (data.isBind(((OutputGraph) allNode).getProcessData())) {
									connect.setAttribute("inputs", data.getName());
								}
							}
						}
						// TODO: 2017/4/8
						connections.appendChild(connect);
					}
				}
			}
		}
		return XmlUtilities.nodeToString(document, "UTF-8");
	}

	public static NodeMatrix stringToNodeMatrix(String xml) {
		Document document = XmlUtilities.stringToDocument(xml);
		NodeMatrix nodeMatrix = new NodeMatrix();
		Node root = document.getChildNodes().item(0);
		Node graphs = XmlUtilities.getChildElementNodeByName(root, "graphs");
		Element[] graphArrays = XmlUtilities.getChildElementNodesByName(graphs, "graph");
		HashMap<String, IGraph> map = new HashMap<>();
		for (Element graph : graphArrays) {
			String id = graph.getAttribute("ID");
			IGraph iGraph = AbstractGraph.formXmlFile(graph.getAttribute("value"));
			map.put(id, iGraph);
			nodeMatrix.addNode(iGraph);
		}
		Node connections = XmlUtilities.getChildElementNodeByName(root, "connections");
		Element[] connects = XmlUtilities.getChildElementNodesByName(connections, "connect");
		for (Element connect : connects) {
			String startId = connect.getAttribute("startId");
			String endId = connect.getAttribute("endId");
			try {
				String inputs = connect.getAttribute("inputs");
				if (!StringUtilities.isNullOrEmpty(inputs)) {
					((ProcessGraph) map.get(endId)).getProcess().getInputs().bind(inputs, ((OutputGraph) map.get(startId)).getProcessData());
				} else {
					((OutputGraph) map.get(endId)).setProcessData(((ProcessGraph) map.get(startId)).getProcess().getOutputs().getData(((OutputGraph) map.get(endId)).getName()));
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			nodeMatrix.addConstraint(map.get(startId), map.get(endId), new DirectConnect());
		}
		return nodeMatrix;
	}

}
