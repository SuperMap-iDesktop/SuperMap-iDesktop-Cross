package com.supermap.desktop.ui.controls;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.desktop.utilties.XmlUtilties;

/**
 * 文件选择器
 *
 * @author XiaJT
 */
public class SmFileChoose extends JFileChooser {

	private static final long serialVersionUID = 1L;

	private static long fileLastModifiedTime;

	public static long getFileLastModifiedTime() {
		return fileLastModifiedTime;
	}

	public static void setFileLastModifiedTime(long fileLastModifiedTime) {
		SmFileChoose.fileLastModifiedTime = fileLastModifiedTime;
	}

	private static Document documentFileChoose;
	private static Node lastPathsNode;
	private transient Node nowNode;

	private static String encodingType;
	/**
	 * 选择器标题
	 */
	private String title;
	/**
	 * 上次关闭时的路径
	 */
	private String moduleLastPath;
	/**
	 * 文件选择器类型
	 */
	private String moduleType;
	/**
	 * 文件过滤信息
	 */
	private String moduleFileFilters;
	/**
	 * 处理后的文件路径
	 */
	private String filePath;
	/**
	 * 处理后的文件名
	 */
	private String fileName;
	/**
	 * 多选时的文件名集合
	 */
	private String[] selectFileNames;

	/**
	 * 多选时的文件集合
	 */
	private File[] selectFiles;

	/**
	 * 根据配置文件生成文件选择器
	 *
	 * @param moduleName 模块名称
	 */

	public static Document getDocunmentFileChoose() {
		File file = new File(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true));
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream fOutputStream = new FileOutputStream(file.getPath());
				OutputStreamWriter OutputStreamWriter = new OutputStreamWriter(fOutputStream, "UTF-8");
				OutputStreamWriter.write(ControlsProperties.getString("String_InitRecntFileString"));
				OutputStreamWriter.flush();
				OutputStreamWriter.close();
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		if (SmFileChoose.getFileLastModifiedTime() != new File(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true))
				.lastModified()) {
			documentFileChoose = XmlUtilties.getDocument(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true));
			SmFileChoose.setFileLastModifiedTime(new File(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true))
					.lastModified());
		}
		return documentFileChoose;
	}

	public static Node getLastPathsNode() {
		getDocunmentFileChoose();
		NodeList docunmentStartUp = documentFileChoose.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < docunmentStartUp.getLength(); i++) {
			Node node = docunmentStartUp.item(i);
			if ((node != null && node.getNodeType() == Node.ELEMENT_NODE) && "LastFilePaths".equals(node.getNodeName())) {
				lastPathsNode = node;
				break;
			}
		}
		return lastPathsNode;
	}

	public static String getEncodingType() {
		getDocunmentFileChoose();
		if (encodingType == null || encodingType.length() <= 0) {
			encodingType = documentFileChoose.getXmlEncoding();
		}
		return encodingType;
	}

	public static boolean isModuleExist(String moduleName) {
		getLastPathsNode();
		NodeList childNodes = lastPathsNode.getChildNodes();
		Node node = null;
		boolean flag = false;
		for (int i = 0; i < childNodes.getLength(); i++) {
			node = childNodes.item(i);
			if ((node != null && node.getNodeType() == Node.ELEMENT_NODE)
					&& (node.getAttributes().getNamedItem("ModuleName").getNodeValue().equals(moduleName))) {
				flag = true;
			}
		}
		return flag;
	}

	public static String createFileFilter(String filterName, String... filters) {
		StringBuffer fileFilter = new StringBuffer();
		fileFilter.append(filterName).append('|');
		for (int i = 0; i < filters.length; i++) {
			fileFilter.append(filters[i]);
			if (i != filters.length - 1) {
				fileFilter.append("+");
			}
		}
		return fileFilter.toString();
	}

	public static String bulidFileFilters(String... fileFilters) {
		StringBuffer bufferFileFilter = new StringBuffer();
		for (int i = 0; i < fileFilters.length; i++) {
			if (fileFilters[i] == null || fileFilters[i].length() <= 0) {
				continue;
			}
			bufferFileFilter.append(fileFilters[i]);
			bufferFileFilter.append("#");
		}
		if (bufferFileFilter.length() > 0) {
			bufferFileFilter.deleteCharAt(bufferFileFilter.length() - 1);
		}
		return bufferFileFilter.toString();
	}

	public static void addNewNode(String fileFilters, String lastPath, String title, String moduleName, String type) {
		getDocunmentFileChoose();
		Element element = documentFileChoose.createElement("LastFilePath");
		Attr attrFileFilter = documentFileChoose.createAttribute("FileFilters");
		attrFileFilter.setValue(fileFilters);
		Attr attrLastPath = documentFileChoose.createAttribute("LastPath");
		attrLastPath.setValue(lastPath);
		Attr attrTitle = documentFileChoose.createAttribute("Title");
		attrTitle.setValue(title);
		Attr attrModuleName = documentFileChoose.createAttribute("ModuleName");
		attrModuleName.setValue(moduleName);
		Attr attrType = documentFileChoose.createAttribute("Type");
		attrType.setValue(type);
		element.setAttributeNode(attrFileFilter);
		element.setAttributeNode(attrLastPath);
		element.setAttributeNode(attrTitle);
		element.setAttributeNode(attrModuleName);
		element.setAttributeNode(attrType);
		getLastPathsNode();
		lastPathsNode.appendChild(element);
		try {
			XmlUtilties.saveXml(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true), (Node) documentFileChoose,
					getEncodingType());
			SmFileChoose.setFileLastModifiedTime(new File(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true))
					.lastModified());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	public SmFileChoose(String moduleName) {
		super();
		// windows下设置显示详细信息
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			this.getActionMap().get("viewTypeDetails").actionPerformed(null);
		}
		try {

			boolean findFlag = false;

			// 获取LastFilePaths节点
			getDocunmentFileChoose();

			getLastPathsNode();
			NodeList LastFilePaths = null;
			if (lastPathsNode != null) {
				findFlag = true;
				LastFilePaths = lastPathsNode.getChildNodes();
			}

			// 获取modeuleName符合条件的LastFilePath
			if (findFlag) {
				findFlag = false;
				Node LastFilePath = null;
				for (int i = 0; i < LastFilePaths.getLength(); i++) {
					LastFilePath = LastFilePaths.item(i);
					if ((LastFilePath != null && LastFilePath.getNodeType() == Node.ELEMENT_NODE)
							&& (LastFilePath.getAttributes().getNamedItem("ModuleName").getNodeValue().equals(moduleName))) {
						this.nowNode = LastFilePath;
						this.title = LastFilePath.getAttributes().getNamedItem("Title").getNodeValue();
						this.moduleType = LastFilePath.getAttributes().getNamedItem("Type").getNodeValue();
						this.moduleLastPath = LastFilePath.getAttributes().getNamedItem("LastPath").getNodeValue();
						this.moduleFileFilters = LastFilePath.getAttributes().getNamedItem("FileFilters").getNodeValue();
						findFlag = true;
						break;
					}
				}
			}
			if (moduleLastPath != null && moduleLastPath.length() > 0) {
				this.setCurrentDirectory(new File(moduleLastPath));
			}
			if (title != null && title.length() > 0) {
				this.setDialogTitle(title);
			}
			this.setAcceptAllFileFilterUsed(false);
			setFileFilters();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	public String getFilePath() {
		return this.filePath;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String[] getSelectFileNames() {
		return this.selectFileNames;
	}

	/**
	 * 设置文件过滤器
	 */
	private void setFileFilters() {
		if (moduleFileFilters != null && moduleFileFilters.length() > 0) {
			String[] fileFilters = moduleFileFilters.split("\\#");
			String fileFilterName = "";
			String[] fileFilterTypes = null;
			for (int i = 0; i < fileFilters.length; i++) {
				fileFilterName = fileFilters[i].split("\\|")[0];
				fileFilterTypes = fileFilters[i].split("\\|")[1].split("\\+");
				this.addChoosableFileFilter(new FileNameExtensionFilter(fileFilterName, fileFilterTypes));
			}
		}
	}

	/**
	 * 显示面板并处理得到的路径与文件名
	 */
	public int showDefaultDialog() {
		int result = -1;
		if (moduleType == null) {
			moduleType = "OpenOne";
		}
		if ("OpenOne".equals(moduleType)) {
			// 以只读方式打开按钮
			this.setMultiSelectionEnabled(false);
			this.setFileSelectionMode(JFileChooser.FILES_ONLY);
			result = this.showOpenDialog((Component) Application.getActiveApplication().getMainFrame());
		} else if ("SaveOne".equals(moduleType)) {
			this.setMultiSelectionEnabled(false);
			this.setFileSelectionMode(JFileChooser.FILES_ONLY);
			result = this.showSaveDialog((Component) Application.getActiveApplication().getMainFrame());
		} else if ("OpenMany".equals(moduleType)) {
			// 以只读方式打开按钮
			this.setMultiSelectionEnabled(true);
			this.setFileSelectionMode(JFileChooser.FILES_ONLY);
			result = this.showOpenDialog((Component) Application.getActiveApplication().getMainFrame());
		} else if ("GetDirectories".equals(moduleType)) {
			this.setMultiSelectionEnabled(false);
			this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			this.setAcceptAllFileFilterUsed(true);
			result = this.showOpenDialog((Component) Application.getActiveApplication().getMainFrame());
		}
		return result;
	}

	/**
	 * 设置过滤器 1.选择文件时，当文件不存在时提示重新选择 2.保存文件时，当文件存在时提示是否覆盖 3.过滤非法文件名
	 */
	@Override
	public void approveSelection() {
		/**
		 * 文件名后缀是否符合当前过滤器
		 */
		boolean findFlag = false;
		/**
		 * 文件是否存在
		 */
		boolean fileExistFlag = false;
		String[] fileFilters = null;
		// 当前文件过滤器为所有文件
		if (this.getFileFilter() == this.getAcceptAllFileFilter()) {
			String[] everyoneFileFilters = {""};
			if (moduleFileFilters != null && moduleFileFilters.length() > 0) {
				everyoneFileFilters = moduleFileFilters.split("\\#");
			}
			StringBuilder allFileFilter = new StringBuilder();
			for (int i = 0; i < everyoneFileFilters.length; i++) {
				allFileFilter.append(everyoneFileFilters[i].split("\\|"));
				if (i != everyoneFileFilters.length) {
					allFileFilter.append("\\+");
				}
			}
			fileFilters = allFileFilter.toString().split("\\+");
		} else if(this.getFileFilter() instanceof FileNameExtensionFilter){
			fileFilters = ((FileNameExtensionFilter) this.getFileFilter()).getExtensions();
		}
		// 文件过滤器为空直接返回当前选择的
		if (fileFilters == null || fileFilters.length <= 0) {
			this.filePath = this.getSelectedFile().getAbsolutePath();
			this.fileName = this.getSelectedFile().getName();
			if(getSelectedFiles().length>0){
				selectFileNames = new String[getSelectedFiles().length];
				for (int i = 0; i < getSelectedFiles().length; i++) {
					selectFileNames[i] = getSelectedFiles()[i].getName();
				}
			}
			super.approveSelection();
			saveFilePath();
			return;
		}
		// 分模式处理
		if ("OpenOne".equals(moduleType) || "SaveOne".equals(moduleType)) {
			File file = getSelectedFile();

			// 打开时文件名不能为空
			if ("OpenOne".equals(moduleType) && (file.getName() == null || file.getName().length() <= 0)) {
				UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_Error_FileNameNull"));
				return;
			}
			// 非法文件名
			if (!validateFileName(file.getName())) {
				UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_IllegalName"));
				return;
			}
			filePath = file.getAbsolutePath();
			fileName = file.getName();
			int fileFilterIndex = fileName.lastIndexOf(".");
			// 后缀存在
			if (fileFilterIndex > 0 && fileFilterIndex < fileName.length() - 1) {
				String fileExtension = fileName.substring(fileFilterIndex + 1).toLowerCase();
				for (int j = 0; j < fileFilters.length; j++) {
					// 后缀匹配
					if (fileExtension.equalsIgnoreCase(fileFilters[j])) {
						findFlag = true;
						if (file.exists()) {
							fileExistFlag = true;
						}
						break;
					}
				}
			}
			// 没输入后缀名或点号后的后缀名不符合当前过滤器，自行拼接后缀查看文件是否存在
			else if (findFlag == false) {
				for (int j = 0; j < fileFilters.length; j++) {
					if (new File(filePath + "." + fileFilters[j]).exists()) {
						filePath = filePath + "." + fileFilters[j];
						fileName = fileName + "." + fileFilters[j];
						fileExistFlag = true;
						break;
					}
				}
				// 文件不存在且模式为 保存 时，拼接文件过滤器后缀
				if (false == fileExistFlag && "SaveOne".equals(moduleType)) {
					filePath = filePath + "." + fileFilters[0];
					fileName = fileName + "." + fileFilters[0];
				}
			}
			// 打开时没找到文件
			if (!fileExistFlag && "OpenOne".equals(moduleType)) {
				UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_Error_FileUnExisted"));
				return;
			}
			// 保存时文件已存在
			if (fileExistFlag && "SaveOne".equals(moduleType)) {
				int result = UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_RenameFile_Message"));
				if (JOptionPane.OK_OPTION != result) {
					return;
				}
			}
		} else if ("OpenMany".equals(moduleType)) { // OpenMany未测试
			File[] selectFilesTemp = this.getSelectedFiles();
			if (selectFilesTemp == null || selectFilesTemp.length <= 0) {
				UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_Error_FileNameNull"));
				return;
			}
			selectFilesTemp = dealSelectFiles(selectFilesTemp, fileFilters);
			this.setSelectFiles(selectFilesTemp);
			this.filePath = selectFilesTemp[0].getAbsolutePath();
			StringBuffer fileNames = new StringBuffer();
			StringBuffer wrongNames = new StringBuffer("\"");
			for (int i = 0; i < selectFilesTemp.length; i++) {
				if (selectFilesTemp[i].exists()) {
					if (fileNames != null && fileNames.length() > 0) {
						fileNames.append("&");
					}
					fileNames.append(selectFilesTemp[i].getName());
				} else {
					if (wrongNames.length() > 1) {
						wrongNames.append("\",\"");
					}
					wrongNames.append(selectFilesTemp[i].getName());
					wrongNames.append("\"");
				}
			}
			selectFileNames = fileNames.toString().split("&");

			if (wrongNames.length() > 1) {
				String message = MessageFormat.format(ControlsProperties.getString("String_Err_WrongNames"), wrongNames);
				UICommonToolkit.showErrorMessageDialog(message);
				return;
			}
		} else if ("GetDirectories".equals(moduleType)) {
			this.filePath = getSelectedFile().getAbsolutePath();
		}
		saveFilePath();
		super.approveSelection();
		return;
	}

	private File[] dealSelectFiles(File[] selectFiles, String[] fileFilters) {
		for (int i = 0; i < selectFiles.length; i++) {
			File selectFile = selectFiles[i];
			if (!selectFile.exists()) {
				for (String fileFilter : fileFilters) {
					if (new File(selectFile.getPath() + "." + fileFilter).exists()) {
						selectFiles[i] = new File(selectFile.getPath() + "." + fileFilter);
						break;
					} else if (new File(this.getCurrentDirectory().getPath() + File.separatorChar + selectFile.getPath() + "." + fileFilter).exists()) {
						selectFiles[i] = new File(this.getCurrentDirectory().getPath() + File.separatorChar + selectFile.getPath() + "." + fileFilter);
						break;
					}
				}
			}
		}
		return selectFiles;
	}

	private void saveFilePath() {
		String menuPath = "";
		if (filePath == null || filePath.length() <= 0) {
			return;
		}
		int i = filePath.lastIndexOf("\\");
		if (i > 0 && i < filePath.length() - 1) {
			menuPath = filePath.substring(0, i);
		} else {
			menuPath = filePath;
		}
		nowNode.getAttributes().getNamedItem("LastPath").setNodeValue(menuPath);
		getEncodingType();
		try {
			XmlUtilties.saveXml(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true), (Node) documentFileChoose,
					encodingType);
			setFileLastModifiedTime(new File(PathUtilties.getFullPathName(ControlsProperties.getString("SmFileChooseXMLFilePath"), true)).lastModified());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private boolean validateFileName(String fileName) {
		if (fileName.indexOf('/') != -1 || fileName.indexOf('\\') != -1 || fileName.indexOf(':') != -1 || fileName.indexOf('*') != -1
				|| fileName.indexOf('?') != -1 || fileName.indexOf('"') != -1 || fileName.indexOf('<') != -1 || fileName.indexOf('>') != -1
				|| fileName.indexOf('|') != -1) {
			return false;
		} else {
			return true;
		}
	}

	public File[] getSelectFiles() {
		return selectFiles;
	}

	public void setSelectFiles(File[] selectFiles) {
		this.selectFiles = selectFiles;
	}
}
