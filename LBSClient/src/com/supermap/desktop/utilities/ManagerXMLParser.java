package com.supermap.desktop.utilities;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.supermap.Interface.*;
import com.supermap.desktop.lbsclient.LBSClientProperties;

public class ManagerXMLParser {

	private static Document getCurrentDocument() {
		Document document = null;
		String filePath = PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false);
		File file = new File(filePath);
		if (file.exists() && null != XmlUtilities.getDocument(file, 0)) {
			document = XmlUtilities.getDocument(file, 0);
		}
		return document;
	}

	/**
	 * 删除任务节点
	 * 
	 * @param taskType
	 * @param property
	 * 
	 */
	public static void removeTask(TaskEnum taskType, String property) {
		if (null != getCurrentDocument()) {
			Document document = getCurrentDocument();
			switch (taskType) {
			case DOWNLOADTASK:
				NodeList list = document.getElementsByTagName("DownLoadTask");
				for (int i = 0; i < list.getLength(); i++) {
					Element temp = (Element) list.item(i);
					if (!StringUtilities.isNullOrEmpty(property) && temp.getAttribute("URL").equals(property)) {
						list.item(i).getParentNode().removeChild(temp);
						break;
					}
				}

				try {
					XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
							document.getXmlEncoding());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 获取属性信息List
	 * 
	 * @return
	 */
	public static List<String> getTaskPropertyList() {
		List<String> resultList = new CopyOnWriteArrayList<String>();
		if (null != getCurrentDocument()) {
			Document document = getCurrentDocument();
			NodeList list = document.getElementsByTagName("DownLoadTask");
			for (int i = 0; i < list.getLength(); i++) {
				Element temp = (Element) list.item(i);
				String tempStr = "URL=" + temp.getAttribute("URL");
				resultList.add(tempStr);
			}
		}
		return resultList;
	}

	/**
	 * 添加任务节点
	 * 
	 * @param taskType
	 * @param property
	 *            :属性字符串（如DownloadTask可设置为URL=......）
	 */
	public static void addTask(TaskEnum taskType, String property) {
		// 如果文件存在则直接添加
		if (null != getCurrentDocument()) {
			Document document = getCurrentDocument();
			switch (taskType) {
			case DOWNLOADTASK:
				NodeList list = document.getElementsByTagName("DownLoadTasks");
				Node downLoadTasksNode = list.item(0);
				Element newDownLoadTaskElement = document.createElement("DownLoadTask");
				String[] propertyArray = property.split("=");
				newDownLoadTaskElement.setAttribute(propertyArray[0], propertyArray[1]);
				if (null != downLoadTasksNode) {
					downLoadTasksNode.appendChild(newDownLoadTaskElement);
					try {
						XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
								document.getXmlEncoding());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		} else {
			// 如果文件不存在则先创建一个设置好格式的空文件再添加节点
			createEmtyTaskModel(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false));
			addTask(taskType, property);
		}
	}

	/**
	 * 新建任务模板XML
	 * 
	 * @param filePath
	 */
	public static void createEmtyTaskModel(String filePath) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element tasks = document.createElement("Tasks");
			tasks.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
			tasks.setAttribute("version", "8.1.x");
			document.appendChild(tasks);
			Element downLoadTasks = document.createElement("DownLoadTasks");
			Element upLoadTasks = document.createElement("UpLoadTasks");
			Element kernelDensityTasks = document.createElement("KernelDensityTasks");
			tasks.appendChild(downLoadTasks);
			tasks.appendChild(upLoadTasks);
			tasks.appendChild(kernelDensityTasks);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			File file = new File(filePath);
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

}