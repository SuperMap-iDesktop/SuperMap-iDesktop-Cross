package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class XmlUtilities {
	private XmlUtilities() {
		// 工具类不提供构造函数
	}

	/**
	 * 根据文件获取Document
	 *
	 * @param file
	 * @param i
	 * @return
	 */
	public static Document getDocument(File file, int i) {
		Document document = null;
		if (i < 3000) {
			try {
				Thread.sleep((long) (Math.random() * i));
				if (file.exists()) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = factory.newDocumentBuilder();
					documentBuilder.setErrorHandler(new DefaultHandler() {
						@Override
						public void fatalError(SAXParseException e) throws SAXException {
							// 不输出到控制台
							throw e;
						}
					});
					document = documentBuilder.parse(file);
				}
			} catch (Exception e) {
				// 文件正在写或文件错误,重新取文件
				if (i == 0) {
					i++;
				} else {
					i += i;
				}
				document = getDocument(file, i);
			}
		}
		return document;
	}

	public static Document getDocument(String filePath) {
		return getDocument(filePath, 0);
	}

	private static Document getDocument(String filePath, int i) {
		Document document = null;
		if (i < 3000) {
			try {
				Thread.sleep((long) (Math.random() * i));
				if (filePath != null && !filePath.isEmpty()) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = factory.newDocumentBuilder();
					documentBuilder.setErrorHandler(new DefaultHandler() {
						@Override
						public void fatalError(SAXParseException e) throws SAXException {
							// 不输出到控制台
							throw e;
						}
					});
					File file = new File(filePath);
					document = documentBuilder.parse(file);
				}
			} catch (Exception e) {
				// 文件正在写或文件错误,重新取文件
				if (i == 0) {
					i++;
				} else {
					i += i;
				}
				document = getDocument(filePath, i);
			}
		}
		return document;
	}

	public static Document getDocument(InputStream stream) {
		Document document = null;

		try {
			if (stream != null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = factory.newDocumentBuilder();
				document = documentBuilder.parse(stream);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return document;
	}

	/**
	 * 获取指定XML文档的根元素。
	 *
	 * @param xmlFile
	 * @return
	 */
	public static Element getRootNode(String xmlFile) {
		Element rootElement = null;
		try {
			Document doc = getDocument(xmlFile);
			rootElement = doc.getDocumentElement();
			// 必须有我们的命名空间
//			String xmlns = rootElement.getAttribute(_XMLTag.g_AttributionXMLNamespace);
//			if (!xmlns.equalsIgnoreCase(_XMLTag.g_ValueXMLNamespace)) {
//				rootElement = null;
//			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
			rootElement = null;
		}
		return rootElement;
	}

	/**
	 * 将指定的Node写到指定的OutputStream流中。
	 *
	 * @param os       将要写入的流。
	 * @param node     将要写入的节点。
	 * @param encoding 编码。
	 */
	public static void writeXml(OutputStream os, Node node, String encoding) throws TransformerException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);

		DOMSource source = new DOMSource();
		source.setNode(node);
		StreamResult result = new StreamResult();
		result.setOutputStream(os);

		transformer.transform(source, result);
	}

	/**
	 * 将指定节点输出为字符串形式。
	 *
	 * @param node
	 * @param encoding
	 * @return
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException
	 */
	public static String nodeToString(Node node, String encoding) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String result = null;
		try {
			writeXml(outputStream, node, encoding);
			result = outputStream.toString(encoding);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static Document stringToDocument(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 将指定的节点保存为 文件。
	 *
	 * @param fileName
	 * @param node
	 * @param encoding
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public static void saveXml(final String fileName, final Node node, String encoding) {

		try (FileOutputStream os = new FileOutputStream(fileName)) {
			writeXml(os, node, encoding);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_SaveXmlFailed"));
		}
	}

	/**
	 * Parse file to xml type
	 *
	 * @param transformer
	 * @param source
	 * @param file
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public static void parseFileToXML(Transformer transformer, DOMSource source, File file) throws FileNotFoundException, TransformerException {
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		PrintWriter pw = new PrintWriter(file);
		StreamResult streamResult = new StreamResult(pw);
		transformer.transform(source, streamResult);
	}

	public static Document getEmptyDocument() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			return null;
		}
		return documentBuilder.newDocument();
	}

	/**
	 * 获取指定节点下，找到的第一个 name 节点
	 *
	 * @param node
	 * @param name
	 * @return
	 */
	public static Node getChildElementNodeByName(Node node, String name) {
		if (node == null) {
			return null;
		}
		NodeList nodeList = node.getChildNodes();
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node item = nodeList.item(i);
				if (item != null && (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.DOCUMENT_NODE) && name.equalsIgnoreCase(item.getNodeName())) {
					return item;
				}
			}
		}
		return null;
	}

	public static Element createRoot(Document document, String title) {
		Element node = document.createElement(title);
		node.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
		node.setAttribute("version", "9.0.x");
		document.appendChild(node);
		return node;
	}

	/**
	 * 获取指定节点下所有的 name 节点
	 *
	 * @param node
	 * @param name
	 * @return
	 */
	public static Element[] getChildElementNodesByName(Node node, String name) {
		if (node == null) {
			return null;
		}

		ArrayList<Element> elements = new ArrayList<>();
		NodeList nodeList = node.getChildNodes();
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node item = nodeList.item(i);
				if (item != null && (node.getNodeType() == Node.ELEMENT_NODE) && name.equalsIgnoreCase(item.getNodeName())) {
					elements.add((Element) item);
				}
			}
		}
		return elements.toArray(new Element[elements.size()]);
	}
}
