package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop._XMLTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import java.io.UnsupportedEncodingException;

public class XmlUtilties {
	private XmlUtilties() {
		// 工具类不提供构造函数
	}

	public static Document getDocument(String filePath) {
		Document document = getDocument(filePath, 0);
		return document;
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
			String xmlns = rootElement.getAttribute(_XMLTag.g_AttributionXMLNamespace);
			if (!xmlns.equalsIgnoreCase(_XMLTag.g_ValueXMLNamespace)) {
				rootElement = null;
			}
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
	public static String nodeToString(Node node, String encoding) throws UnsupportedEncodingException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			writeXml(outputStream, node, encoding);
		} catch (TransformerException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return outputStream.toString(encoding);
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
	public static void saveXml(final String fileName, final Node node, String encoding) throws FileNotFoundException {
		try {
			writeXml(new FileOutputStream(fileName), node, encoding);
		} catch (TransformerException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
