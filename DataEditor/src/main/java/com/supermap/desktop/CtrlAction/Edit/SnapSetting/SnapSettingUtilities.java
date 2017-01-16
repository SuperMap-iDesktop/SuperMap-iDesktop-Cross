package com.supermap.desktop.CtrlAction.Edit.SnapSetting;

import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import com.supermap.mapping.SnapMode;
import com.supermap.mapping.SnapSetting;
import com.supermap.ui.MapControl;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xie on 2016/12/12.
 */
public class SnapSettingUtilities {

    private static final String SNAPSETTING_PATH = "String_SnapSettingXMLPath";
    private static final String DEFAULT_SNAPSETTING_PATH = "String_DefaultSnapSettingXMLPath";

    private static Document getDocument(String str) {
        Document document = null;
        String filePath = PathUtilities.getFullPathName(DataEditorProperties.getString(str), false);
        File file = new File(filePath);
        if (file.exists() && null != XmlUtilities.getDocument(file, 0)) {
            document = XmlUtilities.getDocument(file, 0);
        }
        return document;
    }

    /**
     * 根据mapName获取对应的SnapSetting节点，保证一个MapControl只有一个默认的SnapSetting
     *
     * @param mapControl
     * @return
     */
    private static Node getDefaultSnapSettingNode(MapControl mapControl) {
        if (null != getDocument(DEFAULT_SNAPSETTING_PATH)) {
            Document document = getDocument(DEFAULT_SNAPSETTING_PATH);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if ((node != null && node.getNodeType() == Node.ELEMENT_NODE) && "SnapSetting".equals(node.getNodeName()) && mapControl.getMap().getName().equals(node.getAttributes().getNamedItem("MapName").getNodeValue())) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * 判断mapControlD对应的SnapSetting节点是否存在
     *
     * @param mapControl
     * @return
     */
    public static boolean isDefaultSnapSettingExists(MapControl mapControl) {
        return null != getDefaultSnapSettingNode(mapControl);
    }

    /**
     * 从SnapSetting.xml文件中获取SnapSetting节点，并通过该节点构造一个SnapSetting类
     *
     * @return
     */
    public static SnapSetting parseSnapSetting() {
        SnapSetting result = new SnapSetting();
        if (null != getDocument(SNAPSETTING_PATH)) {
            Document document = getDocument(SNAPSETTING_PATH);
            Element node = document.getDocumentElement();
            NodeList nodeList = node.getChildNodes();
            int length = nodeList.getLength();
            for (int i = 0; i < length; i++) {
                Node tempNode = nodeList.item(i);
                if ((tempNode != null && tempNode.getNodeType() == Node.ELEMENT_NODE) && "Node".equals(tempNode.getNodeName())) {
                    resetSnapSetting(result, tempNode);
                }
            }
        }
        return result;
    }

    /**
     * 根据mapcontrol的map的名称从DefaultSnapSetting.xml文件中获取SnapSetting节点，并通过该节点构造一个SnapSetting类
     *
     * @param mapControl
     * @return
     */
    public static SnapSetting parseDefaultSnapSetting(MapControl mapControl) {
        SnapSetting result = new SnapSetting();
        Node node = getDefaultSnapSettingNode(mapControl);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            if ((tempNode != null && tempNode.getNodeType() == Node.ELEMENT_NODE) && "Node".equals(tempNode.getNodeName())) {
                resetSnapSetting(result, tempNode);
            }
        }
        return result;
    }

    private static void resetSnapSetting(SnapSetting result, Node node) {
        String elementName = node.getAttributes().getNamedItem("name").getNodeValue();
        int id = -1;
        String isSelect = "";
        if (elementName.equals("Tolerance")) {
            result.setTolerance(Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue()));
        } else if (elementName.equals("FixedAngle")) {
            result.setFixedAngle(Double.parseDouble(node.getAttributes().getNamedItem("value").getNodeValue()));
        } else if (elementName.equals("MaxSnappedCount")) {
            result.setMaxSnappedCount(Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue()));
        } else if (elementName.equals("FixedLength")) {
            result.setFixedLength(Double.parseDouble(node.getAttributes().getNamedItem("value").getNodeValue()));
        } else if (elementName.equals("MinSnappedLength")) {
            result.setMinSnappedLength(Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue()));
        } else if (elementName.equals("SnappedLineBroken")) {
            if (node.getAttributes().getNamedItem("value").getNodeValue().equals("true")) {
                result.setSnappedLineBroken(true);
            } else {
                result.setSnappedLineBroken(false);
            }
        } else if (elementName.equals("PointOnEndPoint")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.POINT_ON_ENDPOINT, true);
            } else {
                result.set(SnapMode.POINT_ON_ENDPOINT, false);
            }
            result.moveTo(SnapMode.POINT_ON_ENDPOINT, id);
        } else if (elementName.equals("PointOnPoint")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.POINT_ON_POINT, true);
            } else {
                result.set(SnapMode.POINT_ON_POINT, false);
            }
            result.moveTo(SnapMode.POINT_ON_POINT, id);
        } else if (elementName.equals("PointOnLine")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.POINT_ON_LINE, true);
            } else {
                result.set(SnapMode.POINT_ON_LINE, false);
            }
            result.moveTo(SnapMode.POINT_ON_LINE, id);
        } else if (elementName.equals("PointOnMidPoint")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.POINT_ON_MIDPOINT, true);
            } else {
                result.set(SnapMode.POINT_ON_MIDPOINT, false);
            }
            result.moveTo(SnapMode.POINT_ON_MIDPOINT, id);
        } else if (elementName.equals("PointOnExtension")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.POINT_ON_EXTENSION, true);
            } else {
                result.set(SnapMode.POINT_ON_EXTENSION, false);
            }
            result.moveTo(SnapMode.POINT_ON_EXTENSION, id);
        } else if (elementName.equals("LineWithFixedAngle")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.LINE_WITH_FIXED_ANGLE, true);
            } else {
                result.set(SnapMode.LINE_WITH_FIXED_ANGLE, false);
            }
            result.moveTo(SnapMode.LINE_WITH_FIXED_ANGLE, id);
        } else if (elementName.equals("LineWithFixedLength")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.LINE_WITH_FIXED_LENGTH, true);
            } else {
                result.set(SnapMode.LINE_WITH_FIXED_LENGTH, false);
            }
            result.moveTo(SnapMode.LINE_WITH_FIXED_LENGTH, id);
        } else if (elementName.equals("LineWithHorizontal")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.LINE_WITH_HORIZONTAL, true);
            } else {
                result.set(SnapMode.LINE_WITH_HORIZONTAL, false);
            }
            result.moveTo(SnapMode.LINE_WITH_HORIZONTAL, id);
        } else if (elementName.equals("LineWithVertical")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.LINE_WITH_VERTICAL, true);
            } else {
                result.set(SnapMode.LINE_WITH_VERTICAL, false);
            }
            result.moveTo(SnapMode.LINE_WITH_VERTICAL, id);
        } else if (elementName.equals("LineWithParallel")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.LINE_WITH_PARALLEL, true);
            } else {
                result.set(SnapMode.LINE_WITH_PARALLEL, false);
            }
            result.moveTo(SnapMode.LINE_WITH_PARALLEL, id);
        } else if (elementName.equals("LineWithPerpendicular")) {
            isSelect = node.getAttributes().getNamedItem("isSelected").getNodeValue();
            id = Integer.parseInt(node.getAttributes().getNamedItem("index").getNodeValue());
            if (isSelect.equals("true")) {
                result.set(SnapMode.LINE_WITH_PERPENDICULAR, true);
            } else {
                result.set(SnapMode.LINE_WITH_PERPENDICULAR, false);
            }
            result.moveTo(SnapMode.LINE_WITH_PERPENDICULAR, id);
        }
    }

    /**
     * 交换SnapMode的优先级
     *
     * @param source
     * @param target
     */
    public static void replaceSnapMode(SnapSetting source, SnapSetting target) {
        int pointOnEndPointIndex = source.indexOf(SnapMode.POINT_ON_ENDPOINT);
        target.moveTo(SnapMode.POINT_ON_ENDPOINT, pointOnEndPointIndex);
        int pointOnPointIndex = source.indexOf(SnapMode.POINT_ON_POINT);
        target.moveTo(SnapMode.POINT_ON_POINT, pointOnPointIndex);
        int pointOnLineIndex = source.indexOf(SnapMode.POINT_ON_LINE);
        target.moveTo(SnapMode.POINT_ON_LINE, pointOnLineIndex);
        int pointOnMidPointIndex = source.indexOf(SnapMode.POINT_ON_MIDPOINT);
        target.moveTo(SnapMode.POINT_ON_MIDPOINT, pointOnMidPointIndex);
        int pointOnExtensionIndex = source.indexOf(SnapMode.POINT_ON_EXTENSION);
        target.moveTo(SnapMode.POINT_ON_EXTENSION, pointOnExtensionIndex);
        int lineWithFixedAngleIndex = source.indexOf(SnapMode.LINE_WITH_FIXED_ANGLE);
        target.moveTo(SnapMode.LINE_WITH_FIXED_ANGLE, lineWithFixedAngleIndex);
        int lineWithFixedLengthIndex = source.indexOf(SnapMode.LINE_WITH_FIXED_LENGTH);
        target.moveTo(SnapMode.LINE_WITH_FIXED_LENGTH, lineWithFixedLengthIndex);
        int lineWithHorizontalIndex = source.indexOf(SnapMode.LINE_WITH_HORIZONTAL);
        target.moveTo(SnapMode.LINE_WITH_HORIZONTAL, lineWithHorizontalIndex);
        int lineWithVerticalIndex = source.indexOf(SnapMode.LINE_WITH_VERTICAL);
        target.moveTo(SnapMode.LINE_WITH_VERTICAL, lineWithVerticalIndex);
        int lineWithParallelIndex = source.indexOf(SnapMode.LINE_WITH_PARALLEL);
        target.moveTo(SnapMode.LINE_WITH_PARALLEL, lineWithParallelIndex);
        int lineWithPerpendicularIndex = source.indexOf(SnapMode.LINE_WITH_PERPENDICULAR);
        target.moveTo(SnapMode.LINE_WITH_PERPENDICULAR, lineWithPerpendicularIndex);

        target.set(SnapMode.POINT_ON_ENDPOINT, source.get(SnapMode.POINT_ON_ENDPOINT));
        target.set(SnapMode.POINT_ON_POINT, source.get(SnapMode.POINT_ON_POINT));
        target.set(SnapMode.POINT_ON_LINE, source.get(SnapMode.POINT_ON_LINE));
        target.set(SnapMode.POINT_ON_MIDPOINT, source.get(SnapMode.POINT_ON_MIDPOINT));
        target.set(SnapMode.POINT_ON_EXTENSION, source.get(SnapMode.POINT_ON_EXTENSION));
        target.set(SnapMode.LINE_WITH_FIXED_ANGLE, source.get(SnapMode.LINE_WITH_FIXED_ANGLE));
        target.set(SnapMode.LINE_WITH_FIXED_LENGTH, source.get(SnapMode.LINE_WITH_FIXED_LENGTH));
        target.set(SnapMode.LINE_WITH_HORIZONTAL, source.get(SnapMode.LINE_WITH_HORIZONTAL));
        target.set(SnapMode.LINE_WITH_VERTICAL, source.get(SnapMode.LINE_WITH_VERTICAL));
        target.set(SnapMode.LINE_WITH_PARALLEL, source.get(SnapMode.LINE_WITH_PARALLEL));
        target.set(SnapMode.LINE_WITH_PERPENDICULAR, source.get(SnapMode.LINE_WITH_PERPENDICULAR));
    }

    /**
     * 添加SnapSetting节点
     *
     * @param mapcontrol
     */
    public static void addDefaultSnapSettingNode(MapControl mapcontrol) {
        Document document = getDocument(DEFAULT_SNAPSETTING_PATH);
        Element snapSettingElement = document.createElement("SnapSetting");
        snapSettingElement.setAttribute("MapName", mapcontrol.getMap().getName());
        Element root = document.getDocumentElement();
        SnapSetting snapSetting = mapcontrol.getSnapSetting();
        appendNodes(snapSettingElement, document, snapSetting);
        root.appendChild(snapSettingElement);
        XmlUtilities.saveXml(PathUtilities.getFullPathName(DataEditorProperties.getString(DEFAULT_SNAPSETTING_PATH), false), document,
                document.getXmlEncoding());
    }


    /**
     * 创建SnapSetting.xml文件，用于存放全局的SnapSetting
     */
    public static void createSnapSettingFile(SnapSetting snapSetting) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("SnapSetting");
            root.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
            root.setAttribute("version", "8.1.x");
            document.appendChild(root);
            appendNodes(root, document, snapSetting);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            File file = new File(PathUtilities.getFullPathName(DataEditorProperties.getString("String_SnapSettingXMLPath"), false));
            if (!file.exists()) {
                file.createNewFile();
                parseFileToXML(transformer, source, file);
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置SnapSetting.xml文件
     *
     * @param snapSetting
     */
    public static void resetSnapSettingFile(SnapSetting snapSetting) {
        Document document = getDocument(SNAPSETTING_PATH);
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            if ((tempNode != null && tempNode.getNodeType() == Node.ELEMENT_NODE) && "Node".equals(tempNode.getNodeName())) {
                resetSnapSettingNode(snapSetting, tempNode);
            }
        }
        XmlUtilities.saveXml(PathUtilities.getFullPathName(DataEditorProperties.getString(SNAPSETTING_PATH), false), document,
                document.getXmlEncoding());
    }

    private static void resetSnapSettingNode(SnapSetting snapSetting, Node node) {
        String elementName = node.getAttributes().getNamedItem("name").getNodeValue();
        int id = -1;
        String isSelect = "";
        if (elementName.equals("Tolerance")) {
            node.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(snapSetting.getTolerance()));
        } else if (elementName.equals("FixedAngle")) {
            node.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(snapSetting.getFixedAngle()));
        } else if (elementName.equals("MaxSnappedCount")) {
            node.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(snapSetting.getMaxSnappedCount()));
        } else if (elementName.equals("FixedLength")) {
            node.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(snapSetting.getFixedLength()));
        } else if (elementName.equals("MinSnappedLength")) {
            node.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(snapSetting.getMinSnappedLength()));
        } else if (elementName.equals("SnappedLineBroken")) {
            node.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(snapSetting.isSnappedLineBroken()).toLowerCase());
        } else if (elementName.equals("PointOnEndPoint")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.POINT_ON_ENDPOINT)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_ENDPOINT)));
        } else if (elementName.equals("PointOnPoint")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.POINT_ON_POINT)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_POINT)));
        } else if (elementName.equals("PointOnLine")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.POINT_ON_LINE)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_LINE)));
        } else if (elementName.equals("PointOnMidPoint")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.POINT_ON_MIDPOINT)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_MIDPOINT)));
        } else if (elementName.equals("PointOnExtension")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.POINT_ON_EXTENSION)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_EXTENSION)));
        } else if (elementName.equals("LineWithFixedAngle")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.LINE_WITH_FIXED_ANGLE)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_FIXED_ANGLE)));
        } else if (elementName.equals("LineWithFixedLength")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.LINE_WITH_FIXED_LENGTH)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_FIXED_LENGTH)));
        } else if (elementName.equals("LineWithHorizontal")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.LINE_WITH_HORIZONTAL)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_HORIZONTAL)));
        } else if (elementName.equals("LineWithVertical")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.LINE_WITH_VERTICAL)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_VERTICAL)));
        } else if (elementName.equals("LineWithParallel")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.LINE_WITH_PARALLEL)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_PARALLEL)));
        } else if (elementName.equals("LineWithPerpendicular")) {
            node.getAttributes().getNamedItem("isSelected").setNodeValue(String.valueOf(snapSetting.get(SnapMode.LINE_WITH_PERPENDICULAR)).toLowerCase());
            node.getAttributes().getNamedItem("index").setNodeValue(String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_PERPENDICULAR)));
        }
    }


    private static void appendNodes(Element root, Document document, SnapSetting snapSetting) {
        Element tolerance = document.createElement("Node");
        tolerance.setAttribute("name", "Tolerance");
        tolerance.setAttribute("value", String.valueOf(snapSetting.getTolerance()));
        Element fixedAngle = document.createElement("Node");
        fixedAngle.setAttribute("name", "FixedAngle");
        fixedAngle.setAttribute("value", String.valueOf(snapSetting.getFixedAngle()));
        Element maxSnappedCount = document.createElement("Node");
        maxSnappedCount.setAttribute("name", "MaxSnappedCount");
        maxSnappedCount.setAttribute("value", String.valueOf(snapSetting.getMaxSnappedCount()));
        Element fixedLength = document.createElement("Node");
        fixedLength.setAttribute("name", "FixedLength");
        fixedLength.setAttribute("value", String.valueOf(snapSetting.getFixedLength()));
        Element minSnappedLength = document.createElement("Node");
        minSnappedLength.setAttribute("name", "MinSnappedLength");
        minSnappedLength.setAttribute("value", String.valueOf(snapSetting.getMinSnappedLength()));
        Element snappedLineBroken = document.createElement("Node");
        snappedLineBroken.setAttribute("name", "SnappedLineBroken");
        snappedLineBroken.setAttribute("value", String.valueOf(snapSetting.isSnappedLineBroken()).toLowerCase());

        root.appendChild(tolerance);
        root.appendChild(fixedAngle);
        root.appendChild(maxSnappedCount);
        root.appendChild(fixedLength);
        root.appendChild(minSnappedLength);
        root.appendChild(snappedLineBroken);

        Element pointOnEndPoint = document.createElement("Node");
        pointOnEndPoint.setAttribute("name", "PointOnEndPoint");
        pointOnEndPoint.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_ENDPOINT)));
        pointOnEndPoint.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.POINT_ON_ENDPOINT)).toLowerCase());

        Element pointOnPoint = document.createElement("Node");
        pointOnPoint.setAttribute("name", "PointOnPoint");
        pointOnPoint.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_POINT)));
        pointOnPoint.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.POINT_ON_POINT)).toLowerCase());

        Element pointOnLine = document.createElement("Node");
        pointOnLine.setAttribute("name", "PointOnLine");
        pointOnLine.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_LINE)));
        pointOnLine.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.POINT_ON_LINE)).toLowerCase());

        Element pointOnMidPoint = document.createElement("Node");
        pointOnMidPoint.setAttribute("name", "PointOnMidPoint");
        pointOnMidPoint.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_MIDPOINT)));
        pointOnMidPoint.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.POINT_ON_MIDPOINT)).toLowerCase());

        Element pointOnExtension = document.createElement("Node");
        pointOnExtension.setAttribute("name", "PointOnExtension");
        pointOnExtension.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.POINT_ON_EXTENSION)));
        pointOnExtension.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.POINT_ON_EXTENSION)).toLowerCase());

        Element lineWithFixedAngle = document.createElement("Node");
        lineWithFixedAngle.setAttribute("name", "LineWithFixedAngle");
        lineWithFixedAngle.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_FIXED_ANGLE)));
        lineWithFixedAngle.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.LINE_WITH_FIXED_ANGLE)).toLowerCase());

        Element lineWithFixedLength = document.createElement("Node");
        lineWithFixedLength.setAttribute("name", "LineWithFixedLength");
        lineWithFixedLength.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_FIXED_LENGTH)));
        lineWithFixedLength.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.LINE_WITH_FIXED_LENGTH)).toLowerCase());

        Element lineWithHorizontal = document.createElement("Node");
        lineWithHorizontal.setAttribute("name", "LineWithHorizontal");
        lineWithHorizontal.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_HORIZONTAL)));
        lineWithHorizontal.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.LINE_WITH_HORIZONTAL)).toLowerCase());

        Element lineWithVertical = document.createElement("Node");
        lineWithVertical.setAttribute("name", "LineWithVertical");
        lineWithVertical.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_VERTICAL)));
        lineWithVertical.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.LINE_WITH_VERTICAL)).toLowerCase());

        Element lineWithParallel = document.createElement("Node");
        lineWithParallel.setAttribute("name", "LineWithParallel");
        lineWithParallel.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_PARALLEL)));
        lineWithParallel.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.LINE_WITH_PARALLEL)).toLowerCase());

        Element lineWithPerpendicular = document.createElement("Node");
        lineWithPerpendicular.setAttribute("name", "LineWithPerpendicular");
        lineWithPerpendicular.setAttribute("index", String.valueOf(snapSetting.indexOf(SnapMode.LINE_WITH_PERPENDICULAR)));
        lineWithPerpendicular.setAttribute("isSelected", String.valueOf(snapSetting.get(SnapMode.LINE_WITH_PERPENDICULAR)).toLowerCase());

        root.appendChild(pointOnMidPoint);
        root.appendChild(pointOnEndPoint);
        root.appendChild(pointOnLine);
        root.appendChild(pointOnPoint);
        root.appendChild(pointOnExtension);
        root.appendChild(lineWithFixedAngle);
        root.appendChild(lineWithFixedLength);
        root.appendChild(lineWithHorizontal);
        root.appendChild(lineWithVertical);
        root.appendChild(lineWithParallel);
        root.appendChild(lineWithPerpendicular);
    }

    /**
     * 创建DefaultSnapSetting.xml文件，用于存放Mapcontrol的默认SnapSetting
     */
    public static void createDefaultSnapSettingFile() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("SnapSettings");
            root.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
            root.setAttribute("version", "8.1.x");
            document.appendChild(root);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            File file = new File(PathUtilities.getFullPathName(DataEditorProperties.getString(DEFAULT_SNAPSETTING_PATH), false));
            if (!file.exists()) {
                file.createNewFile();
                parseFileToXML(transformer, source, file);
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseFileToXML(Transformer transformer, DOMSource source, File file) throws FileNotFoundException, TransformerException {
        PrintWriter pw = new PrintWriter(file);
        StreamResult streamResult = new StreamResult(pw);
        transformer.transform(source, streamResult);
    }

    /**
     * 判断SnapSetting.xml文件是否存在
     *
     * @return
     */
    public static boolean isSnapSettingFileExists() {
        File file = new File(PathUtilities.getFullPathName(DataEditorProperties.getString(SNAPSETTING_PATH), false));
        return file.exists();
    }

    /**
     * 判断DefaultSnapSetting.xml文件是否存在
     *
     * @return
     */
    public static boolean isDefaultSnapSettingFileExists() {
        File file = new File(PathUtilities.getFullPathName(DataEditorProperties.getString(DEFAULT_SNAPSETTING_PATH), false));
        return file.exists();
    }


}
