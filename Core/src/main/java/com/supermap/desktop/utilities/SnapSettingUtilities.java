package com.supermap.desktop.utilities;

import com.supermap.mapping.SnapMode;
import com.supermap.mapping.SnapSetting;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

/**
 * @author XiaJT
 */
public class SnapSettingUtilities {
	private static SnapSetting snapSetting;
	private static final String SnapSettingXMLPath = "../Configuration/SnapSetting.xml";

	public static SnapSetting getSnapSetting() {
		if (snapSetting == null) {
			snapSetting = SnapSettingUtilities.getDefaultSnapSetting();
		}
		return snapSetting;
	}

	public static void setSnapSetting(SnapSetting snapSetting) {
		SnapSettingUtilities.snapSetting = snapSetting;
	}

	public static SnapSetting getDefaultSnapSetting() {
		if (new File(PathUtilities.getFullPathName(SnapSettingXMLPath, false)).exists()) {
			snapSetting = parseSnapSetting();
		}
		return snapSetting;
	}

	/**
	 * 从SnapSetting.xml文件中获取SnapSetting节点，并通过该节点构造一个SnapSetting类
	 *
	 * @return
	 */
	public static SnapSetting parseSnapSetting() {
		SnapSetting result = new SnapSetting();
		Document document = XmlUtilities.getDocument(PathUtilities.getFullPathName(SnapSettingXMLPath, false));
		if (null != document) {
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
	 * 重置SnapSetting.xml文件
	 *
	 * @param snapSetting
	 */
	public static void resetSnapSettingFile(SnapSetting snapSetting) {
		Document document = XmlUtilities.getDocument(PathUtilities.getFullPathName(SnapSettingXMLPath, false));
		Element element = document.getDocumentElement();
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node tempNode = nodeList.item(i);
			if ((tempNode != null && tempNode.getNodeType() == Node.ELEMENT_NODE) && "Node".equals(tempNode.getNodeName())) {
				resetSnapSettingNode(snapSetting, tempNode);
			}
		}
		XmlUtilities.saveXml(PathUtilities.getFullPathName(SnapSettingXMLPath, false), document,
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
}
