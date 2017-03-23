package com.supermap.desktop.process.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/18.
 * Use this class to store node matrix;
 */
public class Workflow {
    private NodeMatrix matrix;
    private Document document;

    public Workflow(NodeMatrix matrix) {
        this.matrix = matrix;
        createDocument();
    }


    public void parseToXmlFile(String fileName) {
        Element nodeMatrix = createWorkflow();
        nodeMatrix.appendChild(createNodeElements());
        nodeMatrix.appendChild(createProcessesElemet());
        DOMSource source = new DOMSource(this.document);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                parseFileToXML(transformer, source, file);
            } else if (JOptionPane.OK_OPTION == new SmOptionPane().showConfirmDialog(ControlsProperties.getString("String_RenameFile_Message"))) {
                parseFileToXML(transformer, source, file);
            }

        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }

    }

    private void parseFileToXML(Transformer transformer, DOMSource source, File file) throws FileNotFoundException, TransformerException {
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        PrintWriter pw = new PrintWriter(file);
        StreamResult streamResult = new StreamResult(pw);
        transformer.transform(source, streamResult);
    }

    private Element createProcessesElemet() {
        Element processes = createProcesses();
        CopyOnWriteArrayList list = this.matrix.listAllNodes();
        int nodeSize = list.size();
        for (int i = 0; i < nodeSize; i++) {
            Element process = createProcess();
            if (list.get(i) instanceof IProcess) {
                process.setAttribute("title", ((IProcess) list.get(i)).getTitle());
                process.setAttribute("key", ((IProcess) list.get(i)).getKey());
                Element parametersElement = createParameters();
                IParameters parameters = ((IProcess) list.get(i)).getParameters();
                IParameter[] parameterArray = parameters.getParameters();
                int length = parameterArray.length;
                for (int j = 0; j < length; j++) {
                    Element parameter = createParameter();
                    parameter.setAttribute("type", parameterArray[j].getType());
                    parameter.setAttribute("describe",parameterArray[j].getDescribe());
                    if (parameterArray[j] instanceof ISelectionParameter && null != ((ISelectionParameter) parameterArray[j]).getSelectedItem()) {
                        if (parameterArray[j].getType().equals(ParameterType.COMBO_BOX)){
                            parameter.setAttribute("value", ((ParameterDataNode)((ISelectionParameter) parameterArray[j]).getSelectedItem()).getData().toString());
                        }else {
                            parameter.setAttribute("value", ((ISelectionParameter) parameterArray[j]).getSelectedItem().toString());
                        }
                    }else{
                        parameter.setAttribute("value","");
                    }
                    parametersElement.appendChild(parameter);
                }
                process.appendChild(parametersElement);
            } else {
                process.setAttribute("title", list.get(i).toString());
            }
            processes.appendChild(process);
        }
        return processes;
    }

    private Element createNodeElements() {
        Element nodes = createNodes();
        CopyOnWriteArrayList list = this.matrix.listAllNodes();
        int nodeSize = list.size();
        for (int i = 0; i < nodeSize; i++) {
            Element node = createNode();
            Element process = createProcess();
            if (list.get(i) instanceof IProcess) {
                process.appendChild(this.document.createTextNode(((IProcess) list.get(i)).getKey()));
            } else {
                process.appendChild(this.document.createTextNode(list.get(i).toString()));
            }
            node.appendChild(process);
            if (null != list.get(i)) {
                try {
                    CopyOnWriteArrayList preProcessList = this.matrix.getPreNodes(list.get(i));
                    int preProcessSize = preProcessList.size();
                    for (int j = 0; j < preProcessSize; j++) {
                        Element preProcess = createPreProcess();
                        if (preProcessList.get(j) instanceof IProcess) {
                            preProcess.appendChild(this.document.createTextNode(((IProcess) preProcessList.get(j)).getKey()));
                        } else {
                            preProcess.appendChild(this.document.createTextNode(preProcessList.get(j).toString()));
                        }
                        node.appendChild(preProcess);
                    }
                    CopyOnWriteArrayList nextProcessList = this.matrix.getNextNodes(list.get(i));
                    int nextProcessSize = nextProcessList.size();
                    for (int j = 0; j < nextProcessSize; j++) {
                        Element nextProcess = createNextProcess();
                        if (nextProcessList.get(j) instanceof IProcess) {
                            nextProcess.appendChild(this.document.createTextNode(((IProcess) nextProcessList.get(j)).getKey()));
                        } else {
                            nextProcess.appendChild(this.document.createTextNode(nextProcessList.get(j).toString()));
                        }
                        node.appendChild(nextProcess);
                    }
                } catch (NodeException e) {
                    Application.getActiveApplication().getOutput().output(e);
                }
            }
            nodes.appendChild(node);
        }
        return nodes;
    }

    private void createDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        document = documentBuilder.newDocument();
    }

    private Element createWorkflow() {
        Element nodeMatrix = document.createElement("Workflow");
        nodeMatrix.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
        nodeMatrix.setAttribute("version", "9.0.x");
        document.appendChild(nodeMatrix);
        return nodeMatrix;
    }

    private Element createNodes() {
        return document.createElement("Nodes");
    }

    private Element createNode() {
        return document.createElement("Node");
    }


    private Element createProcess() {
        return document.createElement("Process");
    }

    private Element createPreProcess() {
        return document.createElement("PreProcess");
    }

    private Element createNextProcess() {
        return document.createElement("NextProcess");
    }

    private Element createProcesses() {
        return document.createElement("Processes");
    }

    private Element createParameters() {
        return document.createElement("Parameters");
    }

    private Element createParameter() {
        return document.createElement("Parameter");
    }

    /**
     * Constraint element,your constraint for nodes(How to store constraint determined by yourself);
     *
     * @return
     */
    private Element createConstraint() {
        return document.createElement("Constraint");
    }

}
