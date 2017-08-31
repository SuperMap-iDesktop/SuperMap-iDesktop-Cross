package com.supermap.desktop.ui;

import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by lixiaoyao on 2017/8/30.
 */
public class RasterAlgebraExpressionXml {
	private String expression = "";
	private PixelFormat pixelFormat = null;
	private boolean isZip = false;
	private boolean isIgnoreNoValue = false;
	private boolean importResult = true;

	public RasterAlgebraExpressionXml() {
	}

	public void parserXml(String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(fileName);
			Document document = db.parse(file);

			Element root = document.getDocumentElement();// get root Node
			NodeList childNodes = root.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if (node.getNodeName().equals("SmXml:Expression")) {
					this.expression = node.getTextContent();
				} else if (node.getNodeName().equals("SmXml:PixelFormat")) {
					this.pixelFormat = PixelFormatUtilities.valueOf(Integer.valueOf(node.getTextContent()).intValue());
				} else if (node.getNodeName().equals("SmXml:BZip")) {
					this.isZip = strToBoolean(node.getTextContent());
				} else if (node.getNodeName().equals("SmXml:BNoValue")) {
					this.isIgnoreNoValue = strToBoolean(node.getTextContent());
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			this.importResult = false;
		}
	}

	private boolean strToBoolean(String str) {
		boolean result = false;
		if (str.equals("TRUE") || str.equals("true")) {
			result = true;
		}
		return result;
	}


	public String getExpression() {
		return this.expression;
	}

	public PixelFormat getPixelFormat() {
		return this.pixelFormat;
	}

	public boolean isZip() {
		return this.isZip;
	}

	public boolean isIgnoreNoValue() {
		return this.isIgnoreNoValue;
	}

	public boolean isImportResult() {
		return this.importResult;
	}
}
