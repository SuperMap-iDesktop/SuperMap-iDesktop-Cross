package com.supermap.desktop.utilities;

import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.lbsclient.LBSClientProperties;
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
     * 删除所有任务
     */
    public static void removeAllTasks() {
        File taskManagerFile = new File(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false));
        if (taskManagerFile.exists()) {
            taskManagerFile.delete();
        }
    }

    /**
     * 删除任务节点
     *
     * @param taskType
     * @param
     */
    public static void removeTask(TaskEnum taskType, String url, String realName) {
        if (null != getCurrentDocument()) {
            Document document = getCurrentDocument();
            switch (taskType) {
                case DOWNLOADTASK:
                    NodeList downloadList = document.getElementsByTagName("DownloadTask");
                    for (int i = 0; i < downloadList.getLength(); i++) {
                        Element temp = (Element) downloadList.item(i);
                        if (!StringUtilities.isNullOrEmpty(url) && !StringUtilities.isNullOrEmpty(realName) && temp.getAttribute("FileName").equals(realName)
                                && temp.getAttribute("URL").equals(url)) {
                            downloadList.item(i).getParentNode().removeChild(temp);
                            break;
                        }
                    }
                    XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
                            document.getXmlEncoding());
                    break;
                case UPLOADTASK:
                    NodeList uploadList = document.getElementsByTagName("UploadTask");
                    for (int i = 0; i < uploadList.getLength(); i++) {
                        Element temp = (Element) uploadList.item(i);
                        if (!StringUtilities.isNullOrEmpty(url) && !StringUtilities.isNullOrEmpty(realName) && temp.getAttribute("FileName").equals(realName)
                                && temp.getAttribute("URL").equals(url)) {
                            uploadList.item(i).getParentNode().removeChild(temp);
                            break;
                        }
                    }
                    XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
                            document.getXmlEncoding());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取任务总数
     *
     * @return
     */
    public static int getTotalTaskCount() {
        int count = 0;
        if (null != getCurrentDocument()) {
            Document document = getCurrentDocument();
            NodeList downloadList = document.getElementsByTagName("DownloadTask");
            NodeList uploadList = document.getElementsByTagName("UploadTask");
            count = downloadList.getLength() + uploadList.getLength();
        }
        return count;
    }

    /**
     * 获取属性信息List
     *
     * @return
     */
    public static List<String> getTaskPropertyList(TaskEnum taskType) {
        List<String> resultList = new CopyOnWriteArrayList<String>();
        if (null != getCurrentDocument()) {
            Document document = getCurrentDocument();

            switch (taskType) {
                case DOWNLOADTASK:
                    NodeList downloadList = document.getElementsByTagName("DownloadTask");
                    resultList.clear();
                    for (int i = 0; i < downloadList.getLength(); i++) {
                        Element temp = (Element) downloadList.item(i);
                        String tempStr = temp.getAttribute("URL") + "," + temp.getAttribute("FileName") + "," + temp.getAttribute("RealName") + "," + temp.getAttribute("FilePath") + ","
                                + temp.getAttribute("FileSize");
                        resultList.add(tempStr);
                    }

                    break;
                case UPLOADTASK:
                    NodeList uploadList = document.getElementsByTagName("UploadTask");
                    resultList.clear();
                    for (int i = 0; i < uploadList.getLength(); i++) {
                        Element temp = (Element) uploadList.item(i);
                        String tempStr = temp.getAttribute("URL") + "," + temp.getAttribute("FileName") + "," + temp.getAttribute("FilePath") + ","
                                + temp.getAttribute("FileSize");
                        resultList.add(tempStr);
                    }
                    break;

                default:
                    break;
            }

        }
        return resultList;
    }

    /**
     * 添加任务节点
     *
     * @param taskType
     * @param property :属性字符串（如DownloadTask可设置为URL=url,FilePath=filepath,......）
     */
    public static void addTask(TaskEnum taskType, String property) {
        // 如果文件存在则直接添加
        if (null != getCurrentDocument()) {
            Document document = getCurrentDocument();
            switch (taskType) {
                case DOWNLOADTASK:
                    NodeList downloadList = document.getElementsByTagName("DownloadTasks");
                    Node downLoadTasksNode = downloadList.item(0);
                    Element newDownloadTaskElement = document.createElement("DownloadTask");
                    String[] propertyArrayForDownload = property.split(",");
                    for (String attributes : propertyArrayForDownload) {
                        String[] attri = attributes.split("=");
                        newDownloadTaskElement.setAttribute(attri[0], attri[1]);
                    }
                    if (null != downLoadTasksNode) {
                        downLoadTasksNode.appendChild(newDownloadTaskElement);
                        XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
                                document.getXmlEncoding());
                    }
                    break;
                case UPLOADTASK:
                    NodeList uploadList = document.getElementsByTagName("UploadTasks");
                    Node uploadTasksNode = uploadList.item(0);
                    Element newUploadTaskElement = document.createElement("UploadTask");
                    String[] propertyArrayForUpload = property.split(",");
                    for (String attributes : propertyArrayForUpload) {
                        String[] attri = attributes.split("=");
                        newUploadTaskElement.setAttribute(attri[0], attri[1]);
                    }
                    if (null != uploadTasksNode) {
                        uploadTasksNode.appendChild(newUploadTaskElement);
                        XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
                                document.getXmlEncoding());
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
            Element downLoadTasks = document.createElement("DownloadTasks");
            Element upLoadTasks = document.createElement("UploadTasks");
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

    /**
     * 删除下载任务
     */
    public static void removeAllChildTasks(TaskEnum taskType) {
        String tasks = "";
        String task = "";
        if (taskType == TaskEnum.DOWNLOADTASK) {
            tasks = "DownloadTasks";
            task = "DownloadTask";
        } else {
            tasks = "UploadTasks";
            task = "UploadTask";
        }
        if (null != getCurrentDocument()) {
            Document document = getCurrentDocument();
            NodeList downloadList = document.getElementsByTagName(tasks);
            Node downloadsNode = downloadList.item(0);
            NodeList nodeList = downloadsNode.getChildNodes();
            int size = nodeList.getLength();
            for (int i = 0; i < size; i++) {
                Node node = nodeList.item(i);
                if ((node != null && node.getNodeType() == Node.ELEMENT_NODE) && node.getNodeName().equals(task))
                    downloadsNode.removeChild(node);
            }
            XmlUtilities.saveXml(PathUtilities.getFullPathName(LBSClientProperties.getString("String_ManangerXMLPath"), false), document,
                    document.getXmlEncoding());
        }
    }


}