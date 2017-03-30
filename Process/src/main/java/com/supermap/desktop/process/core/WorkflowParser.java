package com.supermap.desktop.process.core;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.meta.metaProcessImplements.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.ui.enums.OverlayAnalystType;
import com.supermap.desktop.utilities.StringUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/21.
 * Parse xml to NodeMatrix
 */
public class WorkflowParser {
    private static final int ITEM_PROCESSES = 3;
    private static final int ITEM_NODES = 1;
    private static final int ITME_WORKFLOW = 0;

    public WorkflowParser() {

    }

    public NodeMatrix parseXMLToMatrix(String path) {
        NodeMatrix nodeMatrix = new NodeMatrix();
        if (!StringUtilities.isNullOrEmpty(path)) {
            File xmlFile = new File(path);
            if (xmlFile.exists()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(xmlFile);
                    NodeList documentChildNodes = document.getChildNodes();
                    NodeList processesList = documentChildNodes.item(ITME_WORKFLOW).getChildNodes().item(ITEM_PROCESSES).getChildNodes();
                    for (int i = 0; i < processesList.getLength(); i++) {
                        if ("Process".equals(processesList.item(i).getNodeName())) {
                            Node process = processesList.item(i);
                            NamedNodeMap attrs = process.getAttributes();
                            MetaProcess metaProcess = null;
                            if (attrs.getLength() > 0 && attrs != null) {
                                Node attr = attrs.getNamedItem("key");
                                metaProcess = getMetaProcess(attr.getNodeValue());
                                nodeMatrix.addNode(metaProcess);
                            }

                            IParameters processParameters = metaProcess.getParameters();
                            Node parameters = process.getChildNodes().item(1);
                            NodeList parameterList = parameters.getChildNodes();
                            for (int j = 0; j < parameterList.getLength(); j++) {
                                if ("Parameter".equals(parameterList.item(j).getNodeName())) {
                                    Node parameterNode = parameterList.item(j);
                                    NamedNodeMap parameterAttrs = parameterNode.getAttributes();
                                    if (parameterAttrs.getLength() > 0 && parameterAttrs != null) {
//                                        String type = parameterAttrs.getNamedItem("type").getNodeValue();
                                        String describe = parameterAttrs.getNamedItem("describe").getNodeValue();
                                        String value = parameterAttrs.getNamedItem("value").getNodeValue();
                                        setParameterValue(processParameters, describe, value);
                                    }
                                }
                            }
                        }
                    }
                    NodeList nodesList = documentChildNodes.item(0).getChildNodes().item(ITEM_NODES).getChildNodes();
                    for (int i = 0; i < nodesList.getLength(); i++) {
                        if ("Node".equals(nodesList.item(i).getNodeName())) {
                            NodeList nodeInfo = nodesList.item(i).getChildNodes();
                            MetaProcess process = null;
                            MetaProcess preProcess = null;
                            MetaProcess nextProcess = null;
                            for (int j = 0; j < nodeInfo.getLength(); j++) {
                                if ("Process".equals(nodeInfo.item(j).getNodeName())) {
                                    String key = nodeInfo.item(j).getTextContent();
                                    process = getMetaProcess(key, nodeMatrix);
                                }
                                if ("PreProcess".equals(nodeInfo.item(j).getNodeName())) {
                                    String key = nodeInfo.item(j).getTextContent();
                                    preProcess = getMetaProcess(key, nodeMatrix);
                                }
                                if ("NextProcess".equals(nodeInfo.item(j).getNodeName())) {
                                    String key = nodeInfo.item(j).getTextContent();
                                    nextProcess = getMetaProcess(key, nodeMatrix);
                                }
                            }
                            if (null != preProcess && null != process) {
                                //TODO
                                //INodeConstriant now not exist in xml file,so add a new INodeConstriant
//                                process.getInputs().followProcess(preProcess);
                                nodeMatrix.addConstraint(preProcess, process, new INodeConstraint() {
                                });
                            }
                            if (null != process && null != nextProcess) {
//                                nextProcess.getInputs().followProcess(process);
                                nodeMatrix.addConstraint(process, nextProcess, new INodeConstraint() {
                                });
                            }
                        }
                    }
                } catch (ParserConfigurationException e) {
                    Application.getActiveApplication().getOutput().output(e);
                } catch (SAXException e) {
                    Application.getActiveApplication().getOutput().output(e);
                } catch (IOException e) {
                    Application.getActiveApplication().getOutput().output(e);
                }
            }
        }
        return nodeMatrix;
    }

    private MetaProcess getMetaProcess(String key, NodeMatrix nodeMatrix) {
        MetaProcess result = null;
        CopyOnWriteArrayList metaProcesses = nodeMatrix.listAllNodes();
        for (int i = 0; i < metaProcesses.size(); i++) {
            if (metaProcesses.get(i) instanceof MetaProcess && key.equals(((MetaProcess) metaProcesses.get(i)).getKey())) {
                result = (MetaProcess) metaProcesses.get(i);
            }
        }
        return result;
    }


    public void setParameterValue(IParameters parameters, String describe, String value) {
        int size = parameters.size();
        for (int i = 0; i < size; i++) {
            if (describe.equals(parameters.getParameter(i).getDescribe()) && parameters.getParameter(i) instanceof ISelectionParameter) {
                ((ISelectionParameter) parameters.getParameter(i)).setSelectedItem(value);
            }
        }
    }

    public MetaProcess getMetaProcess(String key) {
        MetaProcess result = null;
        if (MetaKeys.BUFFER.equals(key)) {
            result = new MetaProcessBuffer();
        } else if (MetaKeys.HEAT_MAP.equals(key)) {
            result = new MetaProcessHeatMap();
        } else if (MetaKeys.IMPORT.equals(key)) {
            result = new MetaProcessImport();
        } else if (MetaKeys.INTERPOLATOR.equals(key)) {
            result = new MetaProcessInterpolator(InterpolationAlgorithmType.IDW);
        } else if (MetaKeys.ISOLINE.equals(key)) {
            result = new MetaProcessISOLine();
        } else if (MetaKeys.ISOPOINT.equals(key)) {
            result = new MetaProcessISOPoint();
        } else if (MetaKeys.ISOREGION.equals(key)) {
            result = new MetaProcessISORegion();
        } else if (MetaKeys.KERNEL_DENSITY.equals(key)) {
            result = new MetaProcessKernelDensity();
        } else if (MetaKeys.PROJECTION.equals(key)) {
            result = new MetaProcessProjection();
        } else if (MetaKeys.SPATIAL_INDEX.equals(key)) {
            result = new MetaProcessSpatialIndex();
        } else if (MetaKeys.OVERLAY_ANALYST.equals(key)) {
            result = new MetaProcessOverlayAnalyst(OverlayAnalystType.CLIP);
        } else if (MetaKeys.SQL_QUERY.equals(key)) {
            result = new MetaProcessSqlQuery();
        }
        return result;
    }
}
